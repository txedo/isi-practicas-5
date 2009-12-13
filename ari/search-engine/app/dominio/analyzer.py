# -*- coding: utf-8 -*-

#    This file is part of pyDMS v1.0: yet another document management system
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

import sys, os
sys.path.append(os.getcwd() + "/../persistencia")

import shutil
import bdDao
import string
from parse import *
from exception import *
from config import *
from cache import *

class Analyzer:

    # Inicializamos los atributos de la clase
    def __init__(self):
        self.__parser = Parser()
        self.dao = None
        self.posting_file = {}
        self.cache = Cache()
        self.__current_working_file = None
        self.__current_file = 0
        self.__total_files = 1
        self.working = False
        self.__analyzing_directory = False
        self.exception = None


    def __str__(self):
        return "("+str(self.__current_file)+"/"+str(self.__total_files)+") Indexing "+self.__current_working_file


    # Metodo para indexar un fichero
    def file_index(self, path):
        self.posting_file = {}
        # No existe la ruta
        if not os.path.isfile(path):
            self.exception = FileException(path)
        else:
            # Controlamos si indexamos un directorio o un fichero
            try: 
                if self.dao == None:
                    self.dao = bdDao.Dao()
                if not self.__analyzing_directory:
                    self.__current_file = 1
                else:
                    self.__current_file = self.__current_file + 1

                self.working=True
                file_name = string.join(((path.split("/"))[-1]).split('.')[:-1],'.') # Tomamos el nombre del archivo, sin extension
                self.__current_working_file = file_name
                (system_path, last_id) = self.dao.insert_doc(file_name) # Recuperamos la ruta del repositorio y el id de ese documento
                # Copiamos el documento al repositorio
                shutil.copy2(path, system_path)
                # Abrimos y leemos el documento linea a linea, pasando cada linea por el parser
                fd = open(path, "r")
                document = fd.readlines()
                for line in document:
                    word_list = self.__parser.parse(line)
                    # Si el parser ha procesado alguna palabra, se actualiza la base de datos
                    if len(word_list) > 0:
                        # Para cada una de las palabras procesadas y devueltas por el parser, actualizamos su frecuencia
                        for word in word_list:
                            # Si la palabra ya aparecia en el documento (es decir, en el posting_file de este documento), se aumenta su frecuencia
                            try:
                                self.posting_file[word] += 1
                            except:
                                self.posting_file[word] = 1
                                # Si es la primera vez que aparece la palabra en el documento, se comprueba si esa 
                                # palabra ya aparecia en otro documento. Si no, se anade al diccionario
                                self.cache.add(word)
                # Al terminar de procesar el documento, se actualiza la base de datos con los datos de la cache y se escribe el posting_file
                if len(self.posting_file)>0:
                    self.cache.synchronize()
                    self.dao.insert_term_posting_file_multi(self.posting_file, last_id)                
                self.working=False
            except Exception, e:
                self.exception = e
                            

    # Metodo para indexar un directorio
    def folder_index(self, path):
        # Si no es un directorio, se lanza una excepcion
        if not os.path.isdir(path):
            self.exception = FolderException(path)
        else:
            try:
                self.__analyzing_directory = True
                # Se lista el contenido del directorio y se procesan los ficheros
                list_files = os.listdir(path)
                self.__total_files = len(list_files)
                self.__current_file = 0
                self.dao = bdDao.Dao()
                for f in list_files:
                    full_path = path+"/"+f
                    if os.path.isdir(full_path):
                        pass # Tratamiento de directorios (no tiene recursividad)
                    else:
                        self.file_index(full_path)
                self.__total_files = 1
                # Al terminar de indexar los documentos, vacimos la cache y volcamos la tabla auxiliar al diccionario
                self.__analyzing_directory = False
            except Exception, e: 
                self.exception = e


   
    

