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

class Cache:
    def __init__(self):
        try:
            self.dao = dao.Dao()
            self.__new = {}
            self.__old = {}
            self.caches = [self.__new, self.__old]
        except:
            raise


    def exists(self, term):
        what_cache = None
        if term in self.__new: what_cache = NEW_CACHE
        elif term in self.__old: what_cache = OLD_CACHE
        else: what_cache = NOT_CACHE
        return what_cache


    def load(self,term,cache,frequency=1):
        self.caches[cache][term] = frequency
        if len(self.__new) + len(self.__old) >= MAX_CACHE_SIZE:
            self.synchronize()


    def get_frequency (self,term,cache):
        return self.caches[cache][term]

    
    def synchronize (self):
        try:
            #print "-->", len(self.__new) + len(self.__old)
            for n in self.__new:
                self.dao.insert_term_dic(n,self.__new[n])
            self.__new={}
            for o in self.__old:
                self.dao.update_term_dic(o,self.__old[o])
            self.__old={}
            self.caches = [self.__new, self.__old]
        except:
            raise
