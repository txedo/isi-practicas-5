# -*- coding: utf-8 -*-

import re
import string
import os
import shutil
import sys
sys.path.append('../persistencia')
import dao
import psyco
from exception import *
from config import *
from cache import *

psyco.full()

class Analyzer:

    # Inicializamos los atributos de la clase (la stop_list)
    def __init__(self):    
        self.stop_list = (open("../misc/stoplist","r")).read().split("\n")
        self.dao = None
        self.posting_file = {}
        self.cache = Cache()


    # Metodo para indexar un fichero
    def file_index(self, path):
        analyzing_directory = True
        # Es un enlace simbolico o no existe la ruta
        if not os.path.isfile(path):
            raise FileException(path)
        else:
            # Controlamos si indexamos un directorio o un fichero
            try: 
                if self.dao == None:
                    self.dao = dao.Dao()
                    analyzing_directory = False
                file_name = (path.split("/"))[-1] # Tomamos el nombre del archivo
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
            except:
                raise
            if not analyzing_directory:
                self.dao.close()
                            

    # Metodo para indexar un directorio
    def folder_index(self, path):
        # Si no es un directorio, se lanza una excepcion
        if not os.path.isdir(path):
            raise FolderException(path)
        else:
            try: 
                # Se lista el contenido del directorio y se procesan los ficheros
                list_files = os.listdir(path)
                self.dao = dao.Dao()
                for f in list_files:
                    full_path = path+"/"+f
                    if os.path.isdir(full_path):
                        pass # Tratamiento de directorios
                    else:
                        self.posting_file = {}
                        self.file_index(full_path)
                self.dao.close()
            except: 
                raise


    def parser(self, line):
        result = []
        word_list = []
        separadores=string.punctuation+string.whitespace
        ip_pattern = re.compile('([0-9]{1,3})\.([0-9]{1,3})\.([0-9]{1,3})\.([0-9]{1,3})')
        word_list = line.split(" ") # Se obtiene una lista al separar por espacios
        # Para cada palabra de la lista, si NO esta en la stop_list y no es una palabra vacia, se parsea
        for word in word_list:
            if (word not in string.whitespace) and (word not in self.stop_list):
                # Evitamos errores de SQLInjection
                word = word.replace("'","\\'")
                # Si la palabra es una direccion IP, se toma dicha palabra como termino
                if ip_pattern.match(word):
                    word = re.sub('[%s]' % re.escape(separadores.replace(".","")), "", word)
                    result.append(word)
                else: # Si es direccion web, email u otra palabra, se separa por signos de puntuacion
                    # Estandarizamos las vocales
                    reg = re.compile("á|à|ä|â")
                    reg.sub("a",word)
                    reg = re.compile("é|è|ë|ê")
                    reg.sub("e",word)
                    reg = re.compile("í|ì|ï|î")
                    reg.sub("i",word)
                    reg = re.compile("ó|ò|ö|ô")
                    reg.sub("o",word)
                    reg = re.compile("ú|ù|ü|û")
                    reg.sub("u",word)
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
                        result.append(word)
            
        return result
    

