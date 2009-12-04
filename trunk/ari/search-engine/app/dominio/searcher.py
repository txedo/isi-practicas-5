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
import datetime
from exception import *
from vector import *
from operator import itemgetter
from fileDao import *

class Searcher:

    def __init__(self):
        self.__dao = dao.Dao()
        self.__total_num_docs = 0
        self.__fileDao = FileDao()
        self.__document_vectors = {}

    # question es un diccionario (termino-peso)
    def search(self, question):
        d1 = datetime.datetime.now()
        module_question = 0.00
        similarity_set = {}
        result=None
        self.__document_vectors = {}
        try:
            total_num_docs = self.__dao.get_num_docs()
            # Si se ha indexado algun documento, se procesa la pregunta
            if total_num_docs > 0:
                # Obtenemos los datos de los documentos con algun termino en comun con la pregunta
                result = self.__dao.select(question.keys())
                # Si existen los terminos de la pregunta en algun documento, se hacen los calculos
                if len(result) > 0:
                    # Calculamos el modulo de la pregunta
                    for term in question:
                        module_question += question[term]
                    module_question = float(math.sqrt(module_question))
                    # Recorremos las filas que devuelve la base de datos y vamos calculando los pesos de cada termino en cada documento
                    for row in result:
                        # row[0] -> term
                        # row[1] -> id_doc
                        # row[2] -> frequency (ftij)
                        # row[3] -> num_docs (fdj)
                        # row[4] -> title
                        # El try indica que algun termino del documento ha aparecido anteriormente, por lo que ya tiene un vector creado
                        # El except indica que es la primera vez que aparece un termino de un documento, por lo que se inicializa el vector de dicho documento
                        # El vector de un documento contiene el termino y su peso
                        # wij = ftij x fidj = ftij x log(d/fdj)
                        try:
                            self.__document_vectors[row[1]].add(row[0], row[2]*(math.log(float(total_num_docs)/float(row[3]))))
                        except:
                            self.__document_vectors[row[1]] = Vector()
                            self.__document_vectors[row[1]].set_title(str(row[4]))
                            self.__document_vectors[row[1]].add(row[0], row[2]*(math.log(float(total_num_docs)/float(row[3]))))
                    # Una vez creados los vectores documentos, se calcula la semejanza de cada uno de ellos y se ordenan de mayor a menor relevancia (devuelve una lista)
                    # Se devuelve el id_documento, su relevancia y el titulo de ese documento
                    for v in self.__document_vectors:
                        similarity_set[v] = (self.__document_vectors[v].get_similarity(question, module_question), self.__document_vectors[v].get_title())
                    sorted_similarity_set = sorted(similarity_set.items(), key=itemgetter(1), reverse=True)

                    # Escribir el fichero xml con los resultados de la busqueda
                    self.__fileDao.open_file()
                    self.__fileDao.write_head()
                    self.__fileDao.write_question(question.keys())
                    self.__fileDao.write_results(sorted_similarity_set)
                    self.__fileDao.close_file()
                else:
                    raise TermNotFound()
            else:
                raise NoFilesIndexed()
            print datetime.datetime.now()-d1
            return sorted_similarity_set
        except:
            raise

