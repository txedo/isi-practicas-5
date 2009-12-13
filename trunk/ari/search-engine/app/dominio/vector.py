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

import math

class Vector:
    
    def __init__(self): 
        self.__components = {}
        self.__title = None


    def add(self, term, weight):
        self.__components[term] = weight

    def set_title (self, title):
        self.__title = title

    # Metodo para obtener la semejanza. 
    def get_similarity (self, question, module_question):
        similarity = 0
        product = 0
        module_vector = 0
        for term in self.__components:
            try:
                product += (question[term] * self.__components[term])
            except:
                pass
            module_vector += math.pow(self.__components[term], 2)
        module_vector = float(math.sqrt(module_vector))

        try:
            similarity = float(float(product) / float(module_vector * module_question))
            return similarity
        except ZeroDivisionError:
            raise ZeroDivisionError

    def get_title(self):
        return self.__title

    def get_components(self):
        return self.__components
