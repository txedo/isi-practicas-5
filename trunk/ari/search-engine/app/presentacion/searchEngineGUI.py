#!/usr/bin/env python
# -*- coding: utf-8 -*-

#    Search Engine
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
import parse, searcher

from exception import * 
from config import *

#ICON = gtk.gdk.pixbuf_new_from_file("terminal_icon.jpg")
TITLE = "Search Engine"


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
        self.res = {}

        # Diccionario de senales y callbacks
        self.dic = {
            "on_searchButton_clicked" : self.searchButton_clicked_cb,
            "on_aboutMenuItem_activate" : self.aboutMenuItem_activate_cb,
            "on_quitMenuItem_activate" : self.quitMenuItem_activate_cb,
            "on_indexEngineMenuItem_activate" : self.indexEngineMenuItem_activate_cb,
            "on_similarButton_clicked" : self.similarButton_clicked_cb
            }
        # Creo la instancia de la GUI
        self.gui = GUI(glade_file)
        # Conexion de senales con manejadores
        self.gui.signal_autoconnect(self.dic)
        # Obtengo una referencia a la ventana principal
        self.window = self.gui['searcher_window']
        #self.gui['indexer_window'].hide()
        # Creo esta conexion para que sea destruida al cerrar
        self.window.connect('destroy', self.destroy)
        # Tomamos el dialogo definido en el fichero glade
        self.dialog = self.gui['textDialog']

        # Tomamos el TreeView definido en la interfaz
        self.resultView = self.gui['resultView']
        # Conectamos los callback del Tree View
        # Este sirve para abrir el documento cuando haces doble click en una fila
        self.resultView.connect("row_activated", self.open_document)
        # Este sirve para saber la fila seleccionada, cuando haces clic simple
        self.resultView.connect("cursor_changed", self.select_row)

        # Ponemos el modo de seleccion a una sola fila
        self.resultView.get_selection().set_mode(gtk.SELECTION_SINGLE)
    
        # Añadimos las columnas a mostrar en el ListStore
        #self.addResultColumn("Id Document", 1)
        self.addResultColumn("Document Title", 1)
        self.addResultColumn("Relevance", 2)

		# Se crea el ListStore
        self.resultList = gtk.ListStore(gobject.TYPE_PYOBJECT
                                    , gobject.TYPE_STRING
									, gobject.TYPE_STRING)

        # Se asocia el ListStore al TreeView
        self.resultView.set_model(self.resultList)

        self.__guiInit()

        try:
            # El searcher debe ser atributo de clase, para no perder los vectores de terminos entre la busqueda normal y la de similares
            self.s = searcher.Searcher()
        except Exception, e:
           self.__showErrorDialog("Exception", str(e))
        

    def __guiInit(self):
        # La ventana inicial no muestra la lista de resultados
        #self.window.set_size_request(390, 130)
        self.window.set_size_request(486, 427)             
        self.window.set_title(TITLE)
        #self.window.set_icon(ICON)
        self.gui['progressbar'].set_text("")
        self.gui['progressbar'].set_fraction(0.00)
        defaultWeightRB = self.gui['defaultWeightRadioButton']
        defaultWeightRB.set_active(True)
        customWeightRB = self.gui['customWeightRadioButton']
        customWeightRB.set_active(False)
        self.gui['similarButton'].set_sensitive(False)

    def on_delete_event(self, widget, event):
        #print "Delete was called but I won't die!"
        widget.hide()
        return True

    def destroy(self, widget):
        gtk.main_quit()
       
    def __readTextFile (self, filename):
        text = ""
        file = open (filename, "r")
        for line in file.xreadlines():
            text = text + line
        file.close()
        return text

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
    def addResultColumn(self, title, columnId):
		column = gtk.TreeViewColumn(title, gtk.CellRendererText(), text=columnId)
		column.set_resizable(True)
		column.set_sort_column_id(columnId)
		self.resultView.append_column(column)

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
        self.dialog.set_title("Document: "+str(self.res[self.selected_row][1][1]))
        # Tomamos el TextView definido dentro del ScrollBar de ese dialogo
        textArea = self.dialog.get_content_area().get_children()[0].get_children()[0]
        textArea.set_editable(False)
        textArea.set_overwrite(True)
        # Rellenamos el TextView con el texto del fichero
        textArea.get_buffer().set_text(self.__readTextFile(REPOSITORY_PATH+"/"+str(self.res[self.selected_row][0])+".txt"))
        response = self.dialog.run()
        self.dialog.hide()

    # Metodo para almacenar la fila seleccionada en el ListStore
    def select_row (self, widget):
        model, rows = self.resultView.get_selection().get_selected_rows()
        # Solo habra una fila por el modo de seleccion simple
        self.selected_row = int(rows[0][0])
        self.gui['lb_status'].set_text("Row " + str(self.selected_row+1) + "/" + str(len(self.res)) + " selected")

    # Callback del boton para lanzar la busqueda de documentos similares
    def similarButton_clicked_cb (self, widget):
        if self.selected_row == -1:
            self.__showErrorDialog("Error", "A document must be selected")
        else:
            try:
                self.gui['textEntry'].set_text('')
                # Se buscan los documentos similares al seleccionado (usando su id_doc)
                init = datetime.datetime.now()
                t = threading.Thread(target=self.s.search, args=(self.s.get_vector(int(self.res[self.selected_row][0])).get_components(),))
                t.start()
                self.init_pb(init)
                t.join()
                self.res = self.s.result
                self.show_results()
            except MySQLdb.Error, e:
                #self.window.set_size_request(390, 130)
                self.__showErrorDialog("Error", "SQL Exception: "+e.args[1])
            except TermNotFound, e:
                #self.window.set_size_request(390, 130)
                self.__showErrorDialog("Error", str(e))
            except NoFilesIndexed, e:
                #self.window.set_size_request(390, 130)
                self.__showErrorDialog("Error", str(e))
            except Exception, e:
                #self.window.set_size_request(390, 130)
                self.__showErrorDialog("Error", "Exception: "+str(e))
            widget.set_sensitive(True)

    def searchButton_clicked_cb(self, widget):
        self.gui['lb_status'].set_text('')
        terms = self.gui['textEntry'].get_text()
        question_dic = {}
        question_parts = []
        search=True
        weights_sum = 0
        try:
            if terms <> '':                
                question = self.gui['textEntry'].get_text()
                p = parse.Parser()
                # print "Procesar la pregunta ", question
                question_parts_aux = p.parse(question)
                # Se eliminan terminos repetidos en la busqueda
                for q in question_parts_aux: 
                    if q not in question_parts:
                        question_parts.append(q)
                #print question_parts
                if self.gui['defaultWeightRadioButton'].get_active():
                    # Pesos por defecto
                    for q in question_parts:
                        question_dic[q] = 1
                else:
                    # Pesos personalizados
                    #for q in question_parts:
                    #    question_dic[q] = 1
                    (question_dic, weights_sum)= self.create_weight_window(question_parts)
                    if weights_sum == 0:
                        search=False
                # Si hay algun peso distinto de 0 y algun termino que no este en la stop_list, se busca
                if search:
                    if len(question_parts)>0: 
                        init = datetime.datetime.now()  
                        # Aumentamos el tamaño de la ventana
                        #self.window.set_size_request(486, 427)             
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
                    else:
                        raise TermNotFound()
                else:
                    #self.window.set_size_request(390, 130)
                    self.__showErrorDialog("Error", "At least one term must have a weight greater than 0")
            else:
                #self.window.set_size_request(390, 130)
                self.__showErrorDialog("Error", "Enter any term")

        except MySQLdb.Error, e:
            #self.window.set_size_request(390, 130)
            self.__showErrorDialog("Error", "SQL Exception: "+e.args[1])
        except TermNotFound, e:
            #self.window.set_size_request(390, 130)
            self.__showErrorDialog("Error", str(e))
        except NoFilesIndexed, e:
            #self.window.set_size_request(390, 130)
            self.__showErrorDialog("Error", str(e))
        except Exception, e:
            #self.window.set_size_request(390, 130)
            self.__showErrorDialog("Error", "Exception: "+str(e))
        widget.set_sensitive(True)
        
    # Metodo para mostrar los resultados de la busqueda en el ListStore
    def show_results(self):
        # Limpiamos la lista de resultados
        self.resultList.clear()
        #print self.res
        for d in self.res:
            self.resultList.append([self, str(d[1][1]), str(round(d[1][0]*100, 2))+' %']) 
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
            #dialog.vbox.pack_start(label)
            label.show()
            adj = gtk.Adjustment(value=1, lower=0, upper=1, step_incr=0.01, page_incr=0, page_size=0)
            slider = gtk.HScale(adj)
            slider.set_digits(2)
            slider.set_size_request(90,-1)
            hbox.pack_start(slider, expand=True, fill=True, padding=10)
            #dialog.vbox.pack_start(slider)
            slider.show()
            slider_list.append(slider)
            dialog.vbox.pack_start(hbox, expand=False, fill=False, padding=0)
            hbox.show()
        response = dialog.run()
        for i in range(len(slider_list)):
            question_dic[question_parts[i]] = float(slider_list[i].get_value())
            weights_sum += slider_list[i].get_value()
        dialog.destroy()
        return (question_dic, weights_sum)

    def aboutMenuItem_activate_cb(self, widget):
        authors = ["Jose Domingo Lopez Lopez\nJuan Andrada Romero"]
        license = self.__readTextFile ("../license.txt")
        dialog = gtk.AboutDialog()
        dialog.set_name("Program name")
        dialog.set_version("v1.0")
        dialog.set_authors(authors)
        dialog.set_license(license)
        #dialog.set_logo(ICON)

        dialog.run()
        dialog.destroy()

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
