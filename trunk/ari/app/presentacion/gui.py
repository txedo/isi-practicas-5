#!/usr/bin/env python

#    gTerm - a text terminal prototype
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
    
    
import pygtk
pygtk.require('2.0')
import gtk, gtk.glade

import string

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
        self.window.resize(325,225)
        #self.window.set_resizable(False)
        self.window.set_title(TITLE)
        #self.window.set_icon(ICON)
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
        
        
    def btnStart_activate_cb(self, widget):
        rbFile = self.gui['rbFile']
        rbDirectory = self.gui['rbDirectory']
        if rbFile.get_active():
            print "Indexamos un fichero"
        elif rbDirectory.get_active():
            print "Indexamos todos los ficheros de un directorio"

        
def main():
    gtk.main()


if __name__ == '__main__':
    app = Aplicacion('gui.glade')
    main()
