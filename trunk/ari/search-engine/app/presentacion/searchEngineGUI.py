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

#ICON = gtk.gdk.pixbuf_new_from_file("terminal_icon.jpg")
TITLE = "Index Engine"


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
        # Diccionario de senales y callbacks
        self.dic = {
            "on_searchButton_clicked" : self.searchButton_clicked_cb,
            "on_aboutMenuItem_activate" : self.aboutMenuItem_activate_cb,
            "on_quitMenuItem_activate" : self.quitMenuItem_activate_cb,
            "on_indexEngineMenuItem_activate" : self.indexEngineMenuItem_activate_cb
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
        # Para los dialogos conectar "delete-event" y "close"
        # Inicializaciones de la interfaz en codigo
        self.__guiInit()


    def __guiInit(self):
        #self.window.resize(325,250)
        self.window.set_title(TITLE)
        #self.window.set_icon(ICON)
        self.gui['progressbar'].set_text("")
        self.gui['progressbar'].set_fraction(0.00)
        defaultWeightRB = self.gui['defaultWeightRadioButton']
        defaultWeightRB.set_active(True)
        customWeightRB = self.gui['customWeightRadioButton']
        customWeightRB.set_active(False)

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

         
    def init_pb(self, init):
        pass

    def searchButton_clicked_cb(self, widget):
        terms = self.gui['textEntry'].get_text()
        question_dic = {}
        question_parts = []
        search=True
        weights_sum = 0
        try:
            if terms <> '':                
                question = self.gui['textEntry'].get_text()
                s = searcher.Searcher()
                p = parse.Parser()
                print "Procesar la pregunta ", question
                question_parts_aux = p.parse(question)
                print question_parts_aux
                # Se eliminan terminos repetidos en la busqueda
                for q in question_parts_aux: 
                    if q not in question_parts:
                        question_parts.append(q)
                print question_parts
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
                if search:
                    res = s.search(question_dic)
                    print res
                else:
                    self.__showErrorDialog("Error", "At least one term must have a weight greater than 0")
            else:
                self.__showErrorDialog("Error", "Enter any term")

        except MySQLdb.Error, e:
            self.__showErrorDialog("Error", "SQL Exception: "+e.args[1])
        except TermNotFound, e:
            self.__showErrorDialog("Error", str(e))
        except NoFilesIndexed, e:
            self.__showErrorDialog("Error", str(e))
        except Exception, e:
            self.__showErrorDialog("Error", "Exception: "+str(e))
        widget.set_sensitive(True)
        

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

    def quitMenuItem_activate_cb(self, widget):
        self.destroy(widget)


def main():
    gtk.main()


if __name__ == '__main__':
    app = Aplicacion('search-engine-gui.glade')
    main()
