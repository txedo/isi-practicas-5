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
            "on_searchButton_activate" : self.searchButton_activate_cb,
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
        self.gui['indexer_window'].connect("delete-event", self.on_delete_event)
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

    def handle_exception_gui (self, title, error):
        message = gtk.MessageDialog(None, gtk.DIALOG_MODAL, gtk.MESSAGE_ERROR, gtk.BUTTONS_NONE, error)
        message.set_title(title)
        message.add_buttons(gtk.STOCK_OK, gtk.RESPONSE_OK)
        return message
         
    def init_pb(self, init):
        pass

    def searchButton_activate_cb(self, widget):
        pass

    def searchButton_clicked_cb(self, widget):
        pass

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
        indexerWindow = self.gui['indexer_window']
        indexerWindow.show()

    def quitMenuItem_activate_cb(self, widget):
        self.destroy(widget)


def main():
    gtk.main()


if __name__ == '__main__':
    app = Aplicacion('gui.glade')
    main()
