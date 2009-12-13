# -*- coding: utf-8 -*-

#    This file is part of pyDMS v1.0: Yet Another Document Management System
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

import bdDao
from config import *

class Cache:
    def __init__(self):
        try:
            self.buffer = {}
            self.dao = bdDao.Dao()

        except:
            raise


    def add (self, term):
        try:
            self.buffer[term] += 1
        except:
            self.buffer[term] = 1

        if len(self.buffer) >= MAX_CACHE_SIZE:
            self.synchronize()
        

    # Metodo que copia el contenido de las caches a la base de datos. Esto se hace al terminar de procesar un documento 
    # o cuando la cache se llena
    def synchronize (self):        
        self.dao.insert_term_dic_duplicate(self.buffer)
        self.buffer = {}
