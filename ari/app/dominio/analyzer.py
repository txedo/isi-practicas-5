# -*- coding: utf-8 -*-

#     This file is part of Index Engine v1.0
#     Copyright (C) 2009 Jose Domingo López & Juan Andrada Romero

#     This program is free software: you can redistribute it and/or modify
#     it under the terms of the GNU General Public License as published by
#     the Free Software Foundation, either version 3 of the License, or
#     (at your option) any later version.

#     This program is distributed in the hope that it will be useful,
#     but WITHOUT ANY WARRANTY; without even the implied warranty of
#     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
#     GNU General Public License for more details.

#     You should have received a copy of the GNU General Public License
#     along with this program.  If not, see <http://www.gnu.org/licenses/>.


import psyco
psyco.full()

import sys, os
sys.path.append(os.getcwd() + "/../persistencia")

import re
import string
import os
import shutil
import dao
from exception import *
from config import *
from cache import *

class Analyzer:

    # Inicializamos los atributos de la clase (la stop_list)
    def __init__(self):    
        self.stop_list = (open("../misc/stoplist","r")).read().split("\n")
        self.dao = None
        self.posting_file = {}
        self.cache = Cache()
        self.__current_working_file = None
        self.__current_file = 0
        self.__total_files = 1
        self.working = False
        self.__analyzing_directory = False


    def __str__(self):
        return "("+str(self.__current_file)+"/"+str(self.__total_files)+") Indexing "+self.__current_working_file


    # Metodo para indexar un fichero
    def file_index(self, path):
        self.posting_file = {}
        # Es un enlace simbolico o no existe la ruta
        if not os.path.isfile(path):
            raise FileException(path)
        else:
            # Controlamos si indexamos un directorio o un fichero
            try: 
                if self.dao == None:
                    self.dao = dao.Dao()
                if not self.__analyzing_directory:
                    self.__current_file = 1
                else:
                    self.__current_file = self.__current_file + 1
                self.working=True
                file_name = (path.split("/"))[-1] # Tomamos el nombre del archivo
                self.__current_working_file = file_name
                (system_path, last_id) = self.dao.insert_doc(file_name) # Recuperamos la ruta del repositorio y el id de ese documento
                # Copiamos el documento al repositorio
                shutil.copy2(path, system_path)
                # Abrimos y leemos el documento linea a linea, pasando cada linea por el parser
                fd = open(path, "r")
                for line in fd.readlines():
                    word_list = self.parser(line.lower())
                    # Si el parser ha procesado alguna palabra, se actualiza la base de datos
                    if len(word_list) > 0:
                        # Para cada una de las palabras procesadas y devueltas por el parser, si no pertenece a la stop_list, actualizamos su frecuencia
                        for word in word_list:
                            # Si la palabra ya aparecia en el documento (es decir, en el posting_file de este documento), se aumenta su frecuencia
                            try:
                                self.posting_file[word] += 1
                            except:
                                self.posting_file[word] = 1
                                # Si es la primera vez que aparece la palabra en el documento, se comprueba si esa 
                                # palabra ya aparecia en otro documento. Si no, se anade al diccionario (ademas de al posting_file)
                                what_cache = self.cache.exists(word)
                                if what_cache == NOT_CACHE:
                                    (exist_term, frequency) = self.dao.exist_term_dic(word)
                                    if not exist_term:
                                        self.cache.load(word,NEW_CACHE)
                                    else:
                                        self.cache.load(word,OLD_CACHE,frequency+1)
                                else:
                                    frequency = self.cache.get_frequency(word,what_cache)
                                    self.cache.load(word,what_cache,frequency+1)
                self.cache.synchronize()
                for k in self.posting_file.keys():
                    self.dao.insert_term_posting_file(k, last_id, self.posting_file[k])

                self.working=False
            except:
                raise
                            

    # Metodo para indexar un directorio
    def folder_index(self, path):
        # Si no es un directorio, se lanza una excepcion
        if not os.path.isdir(path):
            raise FolderException(path)
        else:
            try:
                self.__analyzing_directory = True
                # Se lista el contenido del directorio y se procesan los ficheros
                list_files = os.listdir(path)
                self.__total_files = len(list_files)
                self.__current_file = 0
                self.dao = dao.Dao()
                for f in list_files:
                    full_path = path+"/"+f
                    if os.path.isdir(full_path):
                        pass # Tratamiento de directorios
                    else:
                        self.file_index(full_path)
                self.__total_files = 1
                self.__analyzing_directory = False
            except: 
                raise


    def parser(self, line):
        result = []
        word_list = []
        #line = unicode(line, "iso-8859-1").encode("utf-8")
        #line = re.sub('[%s]' % re.escape(string.whitespace), " ", line)
        # Evitamos errores de SQLInjection
        line = line.replace("\\","\\\\")
        line = line.replace("'","\\'")
        separadores=string.punctuation+string.whitespace
        ip_pattern = re.compile('([0-9]{1,3})\.([0-9]{1,3})\.([0-9]{1,3})\.([0-9]{1,3})')
        word_list = line.split(" ") # Se obtiene una lista al separar por espacios
        # Para cada palabra de la lista, si NO esta en la stop_list y no es una palabra vacia, se parsea
        for word in word_list:
            if (word not in string.whitespace) and (word not in self.stop_list):
                # Si la palabra es una direccion IP, se toma dicha palabra como termino
                if ip_pattern.match(word):
                    word = re.sub('[%s]' % re.escape(separadores.replace(".","")), "", word)
                    result.append(word)
                else: # Si es direccion web, email u otra palabra, se separa por signos de puntuacion
                    # Estandarizamos las vocales
                    reg = re.compile("á|à|ä|â")
                    word = reg.sub("a",word)
                    reg = re.compile("é|è|ë|ê")
                    word = reg.sub("e",word)
                    reg = re.compile("í|ì|ï|î")
                    word = reg.sub("i",word)
                    reg = re.compile("ó|ò|ö|ô")
                    word = reg.sub("o",word)
                    reg = re.compile("ú|ù|ü|û")
                    word = reg.sub("u",word)
                    reg = re.compile("´|`|¨|^|§")
                    word = reg.sub(" ",word)
                    
                    word_parts = (re.sub('[%s]' % re.escape(separadores), " ", word)).split(" ")
                    is_compound_word = False
                    delete = False
                    # Copia auxiliar para poder recorrer toda la lista de palabras
                    aux = word_parts[:]
                    # Se comprueba que al separar la palabra por signos de puntuacion, todas las palabras obtenidas tengan sentido
                    for w in aux:
                        if w in string.whitespace: 
                            word_parts.remove(w)
                        # Si en la lista solo aparece una palabra de la stop_list con espacios, se elimina
                        elif w in self.stop_list:
                            for i in aux:
                                if i is not w:
                                    if i in string.whitespace:
                                        delete = True
                                    else:
                                        delete = False
                                        break
                            if not delete:
                                is_compound_word = True
                                break
                            else:
                                word_parts.remove(w)
                    # Si se puede separar la palabra, todas ellas se consideran terminos
                    if not is_compound_word:
                        result.extend(word_parts)
                    # Si no se puede separar, se mete la palabra compuesta
                    else:
                        word = re.sub('[%s]' % re.escape(string.whitespace), "", word)
                        result.append(word)
            
        return result
    

