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

    # Inicializamos los atributos de la clase
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
        # No existe la ruta
        if not os.path.isfile(path):
            raise FileException(path)
        else:
            # Controlamos si indexamos un directorio o un fichero
            try: 
                if not self.__analyzing_directory:
                    self.dao = Dao()
                    self.__current_file = 1
                else:
                    self.__current_file = self.__current_file + 1

                print self.__current_file, path
                self.working=True
                file_name = (path.split("/"))[-1] # Tomamos el nombre del archivo
                self.__current_working_file = file_name
                (system_path, last_id) = self.dao.insert_doc(file_name) # Recuperamos la ruta del repositorio y el id de ese documento
                # Copiamos el documento al repositorio
                shutil.copy2(path, system_path)
                # Abrimos y leemos el documento linea a linea, pasando cada linea por el parser
                fd = open(path, "r")
                document = fd.readlines()
                for line in document:
                    word_list = self.parser(line.lower())
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
                                """
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
                                """
                # Al terminar de procesar el documento, se actualiza la base de datos con los datos de la cache y se escribe el posting_file
                """self.cache.synchronize()
                sql = "INSERT INTO posting_file VALUES "
                for k in self.posting_file:
                    sql += "('"+k+"',"+str(last_id)+","+str(self.posting_file[k])+"),"
                self.dao.execute(sql[:len(sql)-1]+";")
                self.working=False"""
                self.working = self.cache.save_posting(self.posting_file, last_id, self.__current_file, self.__total_files)
                if not self.__analyzing_directory:
                    self.dao.close()
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
                        pass # Tratamiento de directorios (no tiene recursividad)
                    else:
                        self.file_index(full_path)
                self.__total_files = 1
                self.__analyzing_directory = False
                self.dao.close()
            except: 
                raise


    def parser(self, line):
        result = []
        word_list = []
        # Evitamos errores de SQLInjection
        line = line.replace("\\","\\\\")
        line = line.replace("'","\\'")
        separadores=string.punctuation+string.whitespace
        # Definicion de patrones
        ip_pattern = re.compile('([0-9]{1,3})\.([0-9]{1,3})\.([0-9]{1,3})\.([0-9]{1,3})$')
        section_numbers_pattern = re.compile(r'[0-9]\.?[0-9]?\.?[0-9]?\.[0-9]?\.?[0-9]?\.?$')
        #number_pattern = re.compile(r'[0-9]+$')
        acentoA_pattern = "Á|À|Â|Ä|á|à|â|ä"
        acentoE_pattern = "É|È|Ê|Ë|é|è|ê|ë"
        acentoI_pattern = "Í|Ì|Î|Ï|í|ì|î|ï"
        acentoO_pattern = "Ó|Ò|Ô|Ö|ó|ò|ô|ö|Ø"
        acentoU_pattern = "Ú|Ù|Û|Ü|ú|ù|û|ü"
        acentoY_pattern = "ý|Ý|ÿ|Ÿ|ỳ|Ỳ"
        acentoC_pattern = "Ç|ç"
        # Se obtiene una lista de palabras al separar por espacios
        word_list = line.split(" ") 
        for word in word_list:
            # Para cada palabra de la lista, si no esta en la stop_list, no es una palabra vacia y no es un numero, se parsea
            if (word not in string.whitespace) and (word not in self.stop_list):
                # Si la palabra es una direccion IP, se toma dicha palabra como termino (eliminando signos de puntuación menos el punto)
                if ip_pattern.match(word):
                    #word = re.sub('[%s]' % re.escape(separadores.replace(".","")), "", word)
                    result.append(word)
                # Se ignoran separadores de seccion
                elif (not section_numbers_pattern.match(word)):
                #else: # Si es direccion web, email u otra palabra, se separa por signos de puntuacion
                    # Estandarizamos las vocales
                    if re.search(acentoA_pattern, word): 
                        reg = re.compile("Á|À|Â|Ä|á|à|â|ä")
                        word = reg.sub("a",word)
                    if re.search(acentoE_pattern, word): 
                        reg = re.compile("É|È|Ê|Ë|é|è|ê|ë")
                        word = reg.sub("e",word)
                    if re.search(acentoI_pattern, word):            
                        reg = re.compile("Í|Ì|Î|Ï|í|ì|î|ï")         
                        word = reg.sub("i",word)
                    if re.search(acentoO_pattern, word):    
                        reg = re.compile("Ó|Ò|Ô|Ö|ó|ò|ô|ö|Ø")                 
                        word = reg.sub("o",word)
                    if re.search(acentoU_pattern, word):   
                        reg = re.compile("Ú|Ù|Û|Ü|ú|ù|û|ü")                  
                        word = reg.sub("u",word)
                    if re.search(acentoY_pattern, word):   
                        reg = re.compile("ý|Ý|ÿ|Ÿ|ỳ|Ỳ")                  
                        word = reg.sub("y",word)   
                    #if re.search(acentoC_pattern, word):   
                    #    reg = re.compile("Ç|ç")                  
                    #    word = reg.sub("c",word) 


                    # Dividimos la palabra en partes, al reemplazar los separadores      
                    word_parts = (re.sub('[%s]' % re.escape(separadores), " ", word)).split(" ")
                    # Copia auxiliar para poder recorrer toda la lista de palabras
                    aux = word_parts[:]
                    # Se comprueba que al separar la palabra por signos de puntuacion, todas las palabras obtenidas tengan sentido
                    for w in aux:
                        # Un espacio en blanco se elimina. Si no comienza por letra tambien
                        if (w in string.whitespace) or (not w[0].isalpha()) or (w in self.stop_list): 
                            word_parts.remove(w)
                        
                    result.extend(word_parts)
            
        return result
    

