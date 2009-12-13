#!/usr/bin/env python
# -*- coding: utf-8 -*-

#    This file is part of pyDMS v1.0: Yet Another Document Management System
#    Copyright (C) 2009, Jose Domingo Lopez Lopez & Juan Andrada Romero
#
#    This program is free software: you can redistribute it and/or modify
#    it under the terms of the GNU General Public License as published by
#    the Free Software Foundation, either version 3 of the License, or
#    (at your option) any later version.
#
#    This program is distributed in the hope that it will be useful,
#    but WITHOUT ANY WARRANTY; without even the implied warranty of
#    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
#    GNU General Public License for more details.
#
#    You should have received a copy of the GNU General Public License
#    along with this program.  If not, see <http://www.gnu.org/licenses/>.

import psyco
psyco.full()

import pygtk
pygtk.require('2.0')
import gtk, gtk.glade, gobject

import sys, os
sys.path.append(os.getcwd() + "/../dominio")

import MySQLdb
import threading, time, datetime

import indexEngineGUI
import parse, searcher, utilities
import webbrowser

from exception import * 
from config import *

ICON = gtk.gdk.pixbuf_new_from_file("../misc/pydms.png")
TITLE = "pyDMS v1.0: Yet Another Document Management System"


# Envoltura del fichero de gui generado por Glade
class GUI:
    def __init__(self, file):
        self.app = gtk.glade.XML(file)
    def __getitem__(self, key):
        return self.app.get_widget(key)
    def signal_autoconnect(self, dic):
        self.app.signal_autoconnect(dic)


class Aplicacion:
    global ICON
    global TITLE

    
    def __init__(self, glade_file):
        self.working=False
        self.selected_row = -1
        # Resultados de la busqueda
        self.res = []
        self.resIndexedDocs = []

        # Diccionario de senales y callbacks
        self.dic = {
            "on_searchButton_clicked" : self.searchButton_clicked_cb,
            "on_aboutMenuItem_activate" : self.aboutMenuItem_activate_cb,
            "on_quitMenuItem_activate" : self.quitMenuItem_activate_cb,
            "on_indexedDocsMenuItem_activate" : self.indexedDocsMenuItem_activate_cb,
            "on_indexEngineMenuItem_activate" : self.indexEngineMenuItem_activate_cb,
            "on_similarButton_clicked" : self.similarButton_clicked_cb,
            "on_closeButton_clicked" : self.closeButton_clicked_cb,
            "on_xmlButton_clicked" : self.xmlButton_clicked_cb
            }
        # Creo la instancia de la GUI
        self.gui = GUI(glade_file)
        # Conexion de senales con manejadores
        self.gui.signal_autoconnect(self.dic)
        # Obtengo una referencia a la ventana principal
        self.window = self.gui['searcher_window']
        # Creo esta conexion para que sea destruida al cerrar
        self.window.connect('destroy', self.destroy)
        # Tomamos el dialogo definido en el fichero glade
        self.dialog = self.gui['textDialog']
        self.dialog.set_icon(ICON)
        self.indexedDocsDialog = self.gui['indexedDocsDialog']
        self.indexedDocsDialog.set_icon(ICON)
        # Tomamos el TreeView definido en la interfaz
        self.resultView = self.gui['resultView']
        self.docsView = self.gui['indexedDocsDialog'].get_content_area().get_children()[0].get_children()[0]
        # Conectamos los callback del Tree View
        # Este sirve para abrir el documento cuando haces doble click en una fila
        self.resultView.connect("row_activated", self.open_document)
        self.docsView.connect("row_activated", self.open_document)
        # Este sirve para saber la fila seleccionada, cuando haces clic simple
        self.resultView.connect("cursor_changed", self.select_row)
        self.docsView.connect("cursor_changed", self.select_row)
        # Ponemos el modo de seleccion a una sola fila
        self.resultView.get_selection().set_mode(gtk.SELECTION_SINGLE)
        self.docsView.get_selection().set_mode(gtk.SELECTION_SINGLE)
        # Añadimos las columnas a mostrar en el ListStore
        self.__add_result_column(self.resultView, "Number", 1)
        self.__add_result_column(self.resultView, "Id Doc", 2)
        self.__add_result_column(self.resultView, "Document Title", 3)
        self.__add_result_column(self.resultView, "Relevance", 4)
        self.__add_result_column(self.docsView, "Id Doc", 1)
        self.__add_result_column(self.docsView, "Title" ,2)
        # Se crea el ListStore
        self.resultList = gtk.ListStore(gobject.TYPE_PYOBJECT, gobject.TYPE_STRING, gobject.TYPE_STRING, gobject.TYPE_STRING, gobject.TYPE_STRING)
        self.docsList = gtk.ListStore(gobject.TYPE_PYOBJECT, gobject.TYPE_STRING, gobject.TYPE_STRING)
        # Se asocia el ListStore al TreeView
        self.resultView.set_model(self.resultList)
        self.docsView.set_model(self.docsList)
        self.resultView.set_headers_clickable(False)
        self.docsView.set_headers_clickable(False)

        self.__guiInit()

        try:
            # El searcher debe ser atributo de clase, para no perder los vectores de terminos entre la busqueda normal y la de similares
            self.s = searcher.Searcher()
        except Exception, e:
           self.__showErrorDialog("Exception", str(e))
        

    def __guiInit(self):
        # La ventana inicial no muestra la lista de resultados
        self.window.set_title(TITLE)
        self.window.set_icon(ICON)
        self.gui['progressbar'].set_text("")
        self.gui['progressbar'].set_fraction(0.00)
        defaultWeightRB = self.gui['defaultWeightRadioButton']
        defaultWeightRB.set_active(True)
        customWeightRB = self.gui['customWeightRadioButton']
        customWeightRB.set_active(False)
        self.gui['similarButton'].set_sensitive(False)
        self.gui['xmlButton'].set_sensitive(False)
        self.resultView.set_sensitive(False)

    def on_delete_event(self, widget, event):
        widget.hide()
        return True

    def destroy(self, widget):
        gtk.main_quit()

    def __showInfoDialog (self, title, secondary_text):
        infodialog = gtk.MessageDialog (parent=self.window, flags=gtk.DIALOG_MODAL, type=gtk.MESSAGE_QUESTION, buttons=gtk.BUTTONS_OK_CANCEL)
        infodialog.set_title(title)
        infodialog.format_secondary_text(secondary_text)
        infodialog.set_default_response(gtk.RESPONSE_OK)
        response = infodialog.run()
        infodialog.destroy()
        return response

    def __showErrorDialog (self, title, secondary_text):
        errordialog = gtk.MessageDialog (parent=self.window, flags=gtk.DIALOG_MODAL, type=gtk.MESSAGE_ERROR, buttons=gtk.BUTTONS_NONE)
        errordialog.set_title(title)
        errordialog.format_secondary_text(secondary_text)
        errordialog.add_buttons(gtk.STOCK_OK, gtk.RESPONSE_OK)
        response = errordialog.run()
        errordialog.destroy()
        return response

    # Metodo para añadir una columna al ListStore del TreeView
    def __add_result_column(self, treeView, title, columnId):
        column = gtk.TreeViewColumn(title, gtk.CellRendererText(), text=columnId)
        column.set_resizable(True)
        column.set_sort_column_id(columnId)
        treeView.append_column(column)

    def __resetGUI(self, full=True):
        if full:
            self.gui['textEntry'].set_text("")
        self.gui['lb_status'].set_text("")
        self.gui['xmlButton'].set_sensitive(False)
        self.gui['similarButton'].set_sensitive(False)
        self.resultView.set_sensitive(False)
        self.gui['progressbar'].set_text("")
        self.gui['progressbar'].set_fraction(0.0)
        self.resultList.clear()

    # Metodo para actualizar la barra de progreso
    def init_pb(self, init):
        time.sleep(0.2)
        while self.s.working:
            self.gui['progressbar'].set_text("Searching ...")
            self.gui['progressbar'].pulse()
            while gtk.events_pending():
                gtk.main_iteration()
                time.sleep(0.15)
        self.gui['progressbar'].set_fraction(1.0)
        finish = datetime.datetime.now()
        self.gui['progressbar'].set_text("Search finished in " + str(finish-init))

    # Metodo para abrir un documento cuando se hace doble clic en la fila del ListStore
    def open_document(self, widget, x, y):
        try:
            result = []
            u = utilities.Utilities()
            if widget == self.resultView:
                result = self.res
                self.dialog.set_title("Document: "+str(result[self.selected_row][1][1]))
            elif widget == self.docsView:
                result = self.resIndexedDocs
                self.dialog.set_title("Document: "+str(result[self.selected_row][1]))
            # Tomamos el TextView definido dentro del ScrollBar de ese dialogo
            textArea = self.dialog.get_content_area().get_children()[0].get_children()[0]
            textArea.set_editable(False)
            textArea.set_overwrite(True)
            # Rellenamos el TextView con el texto del fichero
            textArea.get_buffer().set_text(u.read_text_file(REPOSITORY_PATH+"/"+str(result[self.selected_row][0])+".txt"))
            response = self.dialog.run()
            self.dialog.hide()
        except Exception, e:
            self.__showErrorDialog("Error", "Exception: "+str(e))

    # Metodo para almacenar la fila seleccionada en el ListStore
    def select_row (self, widget):
        model, rows = widget.get_selection().get_selected_rows()
        if len(rows)>0:
            # Solo habra una fila por el modo de seleccion simple
            self.selected_row = int(rows[0][0])

    def closeButton_clicked_cb (self, widget):
        self.destroy(widget)

    def xmlButton_clicked_cb (self, widget):
        webbrowser.open(RESULT_PATH+"resultados.xml")

    # Callback del boton para lanzar la busqueda de documentos similares
    def similarButton_clicked_cb (self, widget):
        if self.selected_row == -1:
            self.__showErrorDialog("Error", "A document must be selected")
        else:
            try:
                self.__resetGUI()
                self.gui['textEntry'].set_text("related:"+self.res[self.selected_row][1][1])
                gtk.main_iteration()
                # Se buscan los documentos similares al seleccionado (usando su id_doc)
                init = datetime.datetime.now()
                t = threading.Thread(target=self.s.search, args=(self.s.get_vector(int(self.res[self.selected_row][0])).get_components(),))
                t.start()
                self.init_pb(init)
                t.join()
                self.gui['similarButton'].set_sensitive(True)
                self.gui['xmlButton'].set_sensitive(True)
                self.resultView.set_sensitive(True)
                self.res = self.s.result
                self.show_results()
            except MySQLdb.Error, e:
                self.__showErrorDialog("Error", "SQL Exception: "+e.args[1])
            except TermNotFound, e:
                self.__showErrorDialog("Error", str(e))
            except NoFilesIndexed, e:
                self.__showErrorDialog("Error", str(e))
            except Exception, e:
                self.__showErrorDialog("Error", "Exception: "+str(e))
            widget.set_sensitive(True)

    def searchButton_clicked_cb(self, widget):
        self.__resetGUI(False)
        terms = self.gui['textEntry'].get_text()
        question_dic = {}
        question_parts = []
        search=True
        weights_sum = 0
        response = None
        try:
            if terms <> '':                
                question = self.gui['textEntry'].get_text()
                p = parse.Parser()
                # print "Procesar la pregunta ", question
                question_parts_aux = p.parse(question)
                if len(question_parts_aux) == 0:
                    raise TermNotFound()
                # Se eliminan terminos repetidos en la busqueda
                for q in question_parts_aux: 
                    if q not in question_parts:
                        question_parts.append(q)
                if self.gui['defaultWeightRadioButton'].get_active():
                    # Pesos por defecto
                    for q in question_parts:
                        question_dic[q] = 1
                else:
                    # Pesos personalizados
                    (response, question_dic, weights_sum) = self.create_weight_window(question_parts)
                    if weights_sum == 0:
                        search=False
                if (response == gtk.RESPONSE_OK and self.gui['customWeightRadioButton'].get_active()) or self.gui['defaultWeightRadioButton'].get_active():
                    # Si hay algun peso distinto de 0 y algun termino que no este en la stop_list, se busca
                    if search:
                        if self.gui['customWeightRadioButton'].get_active():
                            time.sleep(0.35)
                        init = datetime.datetime.now()  
                        # Aumentamos el tamaño de la ventana
                        t = threading.Thread(target=self.s.search, args=(question_dic,))
                        t.start()
                        self.init_pb(init)
                        t.join()
                        if self.s.exception <> None:
                            self.gui['progressbar'].set_text("An error has ocurred")
                            self.gui['progressbar'].set_fraction(0.0)
                            raise self.s.exception
                        else:
                            self.res = self.s.result
                            self.show_results()
                            self.gui['similarButton'].set_sensitive(True)
                            self.gui['xmlButton'].set_sensitive(True)
                            self.resultView.set_sensitive(True)
                    else:
                        self.__resetGUI(False)
                        self.__showErrorDialog("Error", "At least one term must have a weight greater than 0")
            else:
                self.__resetGUI()
                self.__showErrorDialog("Error", "Enter any term")

        except MySQLdb.Error, e:
            self.__showErrorDialog("Error", "SQL Exception: "+e.args[1])
        except TermNotFound, e:
            self.__resetGUI(False)
            self.__showErrorDialog("Error", str(e))
        except NoFilesIndexed, e:
            self.__showErrorDialog("Error", str(e))
        except Exception, e:
            self.__showErrorDialog("Error", "Exception: "+str(e))
        widget.set_sensitive(True)
        
    # Metodo para mostrar los resultados de la busqueda en el ListStore
    def show_results(self):
        # Limpiamos la lista de resultados
        self.resultList.clear()
        counter = 1
        for d in self.res:
            self.resultList.append([self, str(counter), str(int(d[0])), str(d[1][1]), str(round(d[1][0]*100, 2))+' %']) 
            counter += 1
        self.gui['lb_status'].set_text(str(len(self.res))+" documents found")

    def create_weight_window(self, question_parts):
        slider_list = []
        question_dic = {}
        weights_sum = 0
        dialog = gtk.Dialog (title="Weight customization", parent=self.window, flags=gtk.DIALOG_MODAL, buttons=(gtk.STOCK_OK, gtk.RESPONSE_OK))
        dialog.set_resizable(False)
        for q in question_parts:
            label = gtk.Label()
            label.set_text(q)
            hbox = gtk.HBox(homogeneous=True, spacing=10)
            hbox.pack_start(label, expand=False, fill=False, padding=10)
            label.show()
            adj = gtk.Adjustment(value=1, lower=0, upper=1, step_incr=0.01, page_incr=0, page_size=0)
            slider = gtk.HScale(adj)
            slider.set_digits(2)
            slider.set_size_request(90,-1)
            hbox.pack_start(slider, expand=True, fill=True, padding=10)
            slider.show()
            slider_list.append(slider)
            dialog.vbox.pack_start(hbox, expand=False, fill=False, padding=0)
            hbox.show()
        dialog.set_icon(ICON)
        response = dialog.run()
        for i in range(len(slider_list)):
            question_dic[question_parts[i]] = float(slider_list[i].get_value())
            weights_sum += slider_list[i].get_value()
        dialog.destroy()
        return (response, question_dic, weights_sum)

    def aboutMenuItem_activate_cb(self, widget):
        try:
            u = utilities.Utilities()
            authors = ["Jose Domingo Lopez Lopez (josed.lopez1@alu.uclm.es)",
                       "Juan Andrada Romero (juan.andrada@alu.uclm.es)"]
            license = u.read_text_file("../license.txt")
            dialog = gtk.AboutDialog()
            dialog.set_name("pyDMS")
            dialog.set_version("v1.0")
            dialog.set_comments("Yet Another Document Management System")
            dialog.set_website("http://www.inf-cr.uclm.es/")
            dialog.set_website_label("Escuela Superior de Informática @ UCLM")
            dialog.set_authors(authors)
            dialog.set_license(license)
            dialog.set_logo(ICON)
            dialog.run()
            dialog.destroy()
        except Exception, e:
            self.__showErrorDialog("Error", "Exception: "+str(e))

    def indexedDocsMenuItem_activate_cb(self, widget):
        self.docsList.clear()
        result = []
        try:
            u = utilities.Utilities()
            self.resIndexedDocs = u.get_indexed_documents()
            label = self.gui['indexedDocsDialog'].get_content_area().get_children()[1].get_children()[0]
            for r in self.resIndexedDocs:
                self.docsList.append([self, str(r[0]), str(r[1])])
            label.set_text(str(len(self.resIndexedDocs)) + " indexed documents")
            self.gui['indexedDocsDialog'].run()
            self.gui['indexedDocsDialog'].hide()
        except Exception, e:
            self.__showErrorDialog("Error", "Exception: "+str(e))

    def indexEngineMenuItem_activate_cb(self, widget):
        indexer = indexEngineGUI.Aplicacion('index-engine-gui.glade')
        indexer.gui['window'].connect("delete-event", self.on_delete_event)
        indexer.gui['window'].set_modal(True)
        indexer.gui['window'].set_transient_for(self.window)
        indexer.gui['window'].set_position(gtk.WIN_POS_CENTER_ON_PARENT)
        indexer.invoked = True

    def quitMenuItem_activate_cb(self, widget):
        self.destroy(widget)


def main():
    gtk.main()


if __name__ == '__main__':
    app = Aplicacion('search-engine-gui.glade')
    main()
