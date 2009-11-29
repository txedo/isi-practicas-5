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

import dao
from config import *

"""
class Cache:
    def __init__(self):
        try:
            self.dao = dao.Dao()
            self.__new = {}
            self.__old = {}
            self.caches = [self.__new, self.__old]
        except:
            raise

    # Metodo que devuelve si existe un termino en alguna cache
    def exists(self, term):
        what_cache = None
        if term in self.__new: what_cache = NEW_CACHE
        elif term in self.__old: what_cache = OLD_CACHE
        else: what_cache = NOT_CACHE
        return what_cache


    # Metodo que almacena en una cache un termino, con frecuencia 1 por defecto
    def load(self,term,cache,frequency=1):
        self.caches[cache][term] = frequency
        if len(self.__new) + len(self.__old) >= MAX_CACHE_SIZE:
            self.synchronize()


    def get_frequency (self,term,cache):
        return self.caches[cache][term]

    
    # Metodo que copia el contenido de las caches a la base de datos. Esto se hace al terminar de procesar un documento 
    # o cuando la cache se llena
    def synchronize (self):
        try:
            for n in self.__new:
                self.dao.insert_term_dic(n,self.__new[n])
            self.__new={}
            for o in self.__old:
                self.dao.update_term_dic(o,self.__old[o])
            self.__old={}
            self.caches = [self.__new, self.__old]
        except:
            raise

"""

class Cache:
    def __init__(self):
        try:
            self.buffer = {}
            #self.list_postings = {}
            self.dao = dao.Dao()

        except:
            raise


    def add (self, term):
        try:
            self.buffer[term] += 1
        except:
            self.buffer[term] = 1
        """
        self.buffer.append(term)
        """
        if len(self.buffer) >= MAX_CACHE_SIZE:
            self.synchronize()
        

    # Metodo que copia el contenido de las caches a la base de datos. Esto se hace al terminar de procesar un documento 
    # o cuando la cache se llena
    def synchronize (self):        
        self.dao.insert_term_dic_duplicate(self.buffer)
        self.buffer = {}

        
    """def save_posting (self, post, last_id, current_file, total_files):
        self.list_postings[last_id] = post
        if (len(self.list_postings.keys()) >= 50) or (current_file == total_files):
            self.synchronize()
            sql = "INSERT INTO posting_file VALUES "
            for id_doc in self.list_postings:
                for term in self.list_postings[id_doc]:
                    sql += "('"+term+"',"+str(id_doc)+","+str(self.list_postings[id_doc][term])+"),"
            self.dao.execute(sql[:len(sql)-1]+";")
            self.list_postings = {}
        return False # Para actualizar el working"""
