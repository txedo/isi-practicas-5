#!/usr/bin/env python
# -*- coding: utf-8 -*-

#    Index Engine
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

import string

import sys, os
sys.path.append(os.getcwd() + "/../dominio")

import analyzer
import MySQLdb
from exception import *

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
        self.analyzer = analyzer.Analyzer()
        self.working=False
        # Diccionario de senales y callbacks
        self.dic = {
            "on_btnBrowseFile_activate" : self.btnBrowseFile_activate_cb,
            "on_btnBrowseDirectory_activate" : self.btnBrowseDirectory_activate_cb,
            "on_btnStart_activate" : self.btnStart_activate_cb,
            "on_btnBrowseFile_clicked" : self.btnBrowseFile_activate_cb,
            "on_btnBrowseDirectory_clicked" : self.btnBrowseDirectory_activate_cb,
            "on_btnStart_clicked" : self.btnStart_activate_cb,
            }
        # Creo la instancia de la GUI
        self.gui = GUI(glade_file)
        # Conexion de senales con manejadores
        self.gui.signal_autoconnect(self.dic)
        # Obtengo una referencia a la ventana principal
        self.window = self.gui['window']
        # Creo esta conexion para que sea destruida al cerrar
        self.window.connect('destroy', self.destroy)
        # Inicializaciones de la interfaz en codigo
        self.__guiInit()


    def __guiInit(self):
        self.window.resize(325,250)
        #self.window.set_resizable(False)
        self.window.set_title(TITLE)
        #self.window.set_icon(ICON)
        self.gui['progressbar'].set_text("Choose an option...")
        self.gui['progressbar'].set_fraction(0.0)
        # Conectamos los radio buttons con un callback para notificar cuando cambia la seleccion
        rbFile = self.gui['rbFile']
        rbFile.connect("toggled", self.callback, "file")
        rbFile.set_active(True)
        rbDirectory = self.gui['rbDirectory']
        rbDirectory.connect("toggled", self.callback, "directory")
        # Inicializamos activada la parte de ficheros y desactivada la parte de directorios
        txtFilePath = self.gui['txtFilePath']
        txtFilePath.set_editable(False)
        txtDirectoryPath = self.gui['txtDirectoryPath']
        txtDirectoryPath.set_editable(False)
        txtDirectoryPath.set_sensitive(False)
        btnBrowseDirectory = self.gui['btnBrowseDirectory']
        btnBrowseDirectory.set_sensitive(False)


    def callback (self, widget, data=None):
        txtFilePath = self.gui['txtFilePath']
        txtDirectoryPath = self.gui['txtDirectoryPath']
        btnBrowseFile = self.gui['btnBrowseFile']
        btnBrowseDirectory = self.gui['btnBrowseDirectory']
        if widget.get_active() and data == "file":
            txtFilePath.set_sensitive(True)
            btnBrowseFile.set_sensitive(True)
            txtDirectoryPath.set_sensitive(False)
            btnBrowseDirectory.set_sensitive(False)
        elif widget.get_active() and data == "directory":
            txtFilePath.set_sensitive(False)
            btnBrowseFile.set_sensitive(False)
            txtDirectoryPath.set_sensitive(True)
            btnBrowseDirectory.set_sensitive(True)


    def destroy(self, widget):
        if self.analyzer.dao <> None:
            self.analyzer.dao.close()
        gtk.main_quit()
        
        
    def btnBrowseFile_activate_cb(self, widget):
        fcdialog = gtk.FileChooserDialog(title="Select a file to index...", parent=self.window, 
                                         action=gtk.FILE_CHOOSER_ACTION_OPEN, 
                                         buttons=(gtk.STOCK_CANCEL, gtk.RESPONSE_CANCEL, gtk.STOCK_OK, gtk.RESPONSE_OK))
        # Inicializaciones del dialogo
        fcdialog.set_default_response(gtk.RESPONSE_OK)
        # Mostrar dialogo
        response = fcdialog.run()
        # Procesar la respuesta
        if response == gtk.RESPONSE_OK:
            txtFilePath = self.gui['txtFilePath']
            txtFilePath.set_text(fcdialog.get_filename())
        else:
            pass
        fcdialog.destroy()
        
    def btnBrowseDirectory_activate_cb(self, widget):
        fcdialog = gtk.FileChooserDialog(title="Select a directory to index...", parent=self.window, 
                                         action=gtk.FILE_CHOOSER_ACTION_SELECT_FOLDER, 
                                         buttons=(gtk.STOCK_CANCEL, gtk.RESPONSE_CANCEL, gtk.STOCK_OK, gtk.RESPONSE_OK))
        # Inicializaciones del dialogo
        fcdialog.set_default_response(gtk.RESPONSE_OK)
        # Mostrar dialogo
        response = fcdialog.run()
        # Procesar la respuesta
        if response == gtk.RESPONSE_OK:
            txtDirectoryPath = self.gui['txtDirectoryPath']
            txtDirectoryPath.set_text(fcdialog.get_filename())
        else:
            pass
        fcdialog.destroy()
        
        
    def init_pb(self, init):
         time.sleep(0.2)
         while self.analyzer.working:
             self.gui['progressbar'].set_text(str(self.analyzer))
             self.gui['progressbar'].pulse()
             while gtk.events_pending():
                 gtk.main_iteration()
                 time.sleep(0.2)
         self.gui['progressbar'].set_fraction(1.0)
         finish = datetime.datetime.now()
         self.gui['progressbar'].set_text("Index finished in " + str(finish-init))


    def btnStart_activate_cb(self, widget):
        rbFile = self.gui['rbFile']
        rbDirectory = self.gui['rbDirectory']
        try:
            widget.set_sensitive(False)
            init = datetime.datetime.now()
            if rbFile.get_active():
                file_path = self.gui['txtFilePath'].get_text()
                if file_path <> '':
                    t = threading.Thread(target=self.analyzer.file_index,args=(file_path,))
                    t.start()
                    self.init_pb(init)
                else:   
                    message = self.handle_exception_gui("File not found", "Browse a file")
                    resp = message.run()
                    if resp==gtk.RESPONSE_OK:
                        message.destroy()
            elif rbDirectory.get_active():
                folder_path = self.gui['txtDirectoryPath'].get_text()
                if folder_path <> '':
                    t = threading.Thread(target=self.analyzer.folder_index,args=(folder_path,))
                    t.start()
                    self.init_pb(init)
                else:
                    message = self.handle_exception_gui("Folder not found", "Browse a folder")
                    resp = message.run()
                    if resp==gtk.RESPONSE_OK:
                        message.destroy()
            
        except MySQLdb.Error, e:
            message = self.handle_exception_gui("SQL Exception", e.args[1])
            resp = message.run()
            if resp==gtk.RESPONSE_OK:
                message.destroy()
        except FileException, e:
            message = self.handle_exception_gui("File path incorrect", str(e))
            resp = message.run()
            if resp==gtk.RESPONSE_OK:
                message.destroy()
        except FolderException, e:
            message = self.handle_exception_gui("Folder path incorrect", str(e))
            resp = message.run()
            if resp==gtk.RESPONSE_OK:
                message.destroy()
        except Exception, e:
            message = self.handle_exception_gui("Exception", str(e))
            resp = message.run()
            if resp==gtk.RESPONSE_OK:
                message.destroy()
        widget.set_sensitive(True)
        
    def handle_exception_gui (self, title, error):
        message = gtk.MessageDialog(None, gtk.DIALOG_MODAL, gtk.MESSAGE_ERROR, gtk.BUTTONS_NONE, error)
        message.set_title(title)
        message.add_buttons(gtk.STOCK_OK, gtk.RESPONSE_OK)
        return message

        
def main():
    gtk.main()


if __name__ == '__main__':
    app = Aplicacion('gui.glade')
    main()
