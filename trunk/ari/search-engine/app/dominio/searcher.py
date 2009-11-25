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
import math
from vector import *
from operator import itemgetter

class Searcher:

    def __init__(self):
        self.__dao = dao.Dao()
        self.__total_num_docs = 0
        self.__document_vectors = {}

    #wij = ftij x fidj = ftij x log(d/fdj)
    #sorted(d.items(), key=itemgetter(1), reverse=True)
    def search(self, question):
        #question es un diccionario termino-peso
        total_num_docs = self.__dao.get_num_docs()
        result = self.__dao.select(question.keys())
        for row in result:
            #row[0] -> term
            #row[1] -> id_doc
            #row[2] -> frequency
            #row[3] -> num_docs
            try:
                self.__document_vectors[row[1]].add(row[0], row[2]*math.log(total_num_docs/row[3]))
            except:
                self.__document_vectors[row[1]] = Vector()
                self.__document_vectors[row[1]].add(row[0], row[2]*math.log(total_num_docs/row[3]))
        similarity_set = {}
        for v in self.__document_vectors:
            similarity_set[v] = self.__document_vectors[v].get_similarity(question)
        sorted_similarity_set = sorted(similarity_set.items(), key=itemgetter(1), reverse=True)
        return sorted_similarity_set
