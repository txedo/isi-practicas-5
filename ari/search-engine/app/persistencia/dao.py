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

import datetime
from agente import *


class Dao:
    def __init__(self):
        try: 
            self.__agente = Agente()
        except:
            raise


    def execute (self, sql):
        try:
            self.__agente.execute(sql)
        except:
            raise


    def query (self, sql):
        try:
            return self.__agente.query(sql)
        except:
            raise


    def get_num_docs(self):
        result = 0
        try:
            sql = "SELECT COUNT(id_doc) FROM doc"
            result = int(self.query(sql)[0][0])
        except:
            raise
        return result


    def select (self, question):
        timestamp = str(datetime.datetime.now().microsecond)
        view_name = "view_relevant_docs_"+timestamp
        result = None

        try:
            #d1 = datetime.datetime.now()
            sql_view = "CREATE VIEW " + view_name + " AS SELECT DISTINCT p1.id_doc FROM posting_file p1 WHERE p1.term IN ("
            for i in question:
                sql_view += "'" + i + "',"
            sql_view = sql_view[:len(sql_view)-1] + ")"
            self.execute(sql_view)
            #print datetime.datetime.now()-d1
            #d1 = datetime.datetime.now()
            sql_select = "SELECT p.term, p.id_doc, p.frequency, d.num_docs FROM posting_file p, dic d WHERE p.id_doc IN (SELECT * FROM " + view_name + ") and d.term=p.term"
            result = self.query(sql_select)
            #print datetime.datetime.now()-d1
            sql_drop_view = "DROP VIEW " + view_name
            self.execute(sql_drop_view)
            #input()
        except:
            raise
        
        #(term, id_doc, frequency, num_docs)
        return result


    # Metodos para insertar terminos en las tablas de la base de datos

    def insert_term_dic(self, term, frequency=1):
        sql = "INSERT INTO dic VALUES ('"+term+"',"+str(frequency)+")"
        try:          
            self.__agente.execute(sql)
        except:
            raise
        

    def insert_term_dic_duplicate(self, term_buffer):
        sql = "INSERT INTO dic (term, num_docs) VALUES "
        try:          
            for i in term_buffer:
                sql += "('"+i+"', " + str(term_buffer[i]) + "),"
            sql = sql[:len(sql)-1]+" ON DUPLICATE KEY UPDATE num_docs=num_docs+VALUES(num_docs)"
            self.__agente.execute(sql)
        except:
            raise


    def insert_term_posting_file(self, term, id_doc):
        sql = "INSERT INTO posting_file (term, id_doc) VALUES ('"+term+"',"+str(id_doc)+")"
        try:
            self.__agente.execute(sql)
        except:
            raise


    def insert_term_posting_file(self, term, id_doc, frequency):
        sql = "INSERT INTO posting_file VALUES ('"+term+"',"+str(id_doc)+","+str(frequency)+")"
        try:
            self.__agente.execute(sql)
        except:
            raise


    def insert_term_posting_file_multi(self, posting_file, last_id):
        sql = "INSERT INTO posting_file VALUES "
        try:
            for k in posting_file:
                sql += "('"+k+"',"+str(last_id)+","+str(posting_file[k])+"),"
            self.__agente.execute(sql[:len(sql)-1]+";")
        except:
            raise


    def insert_doc(self, doc):
        # Insertamos el nuevo documento
        sql = "INSERT INTO doc (title) VALUES ('"+doc+"')"
        try:        
            self.__agente.execute(sql)
            # Recuperamos el ID que se le ha asignado
            sql = "SELECT LAST_INSERT_ID()"
            result = self.__agente.query(sql)
            last_id = result[0][0]
            # Actualizamos la ruta del documento
            path = REPOSITORY_PATH + str(last_id) + ".txt"
            sql = "UPDATE doc SET path='" + path + "' WHERE id_doc=" + str(last_id)
            self.__agente.execute(sql)
            return (path, last_id)
        except:
            raise

        
    # Metodos para actualizar frecuencias de los terminos en el diccionario y en el posting_file
    
    def update_term_dic(self, term, frequency):
        sql = "UPDATE dic SET num_docs="+str(frequency)+" WHERE term='"+term+"'"
        try:            
            self.__agente.execute(sql)
        except:
            raise 


    def update_term_posting_file(self, term, id_doc):
        sql = "UPDATE posting_file SET frequency=frequency+1 WHERE term='"+term+"' AND id_doc="+str(id_doc)
        try:            
            self.__agente.execute(sql)
        except:
            raise 


    # Metodos para comporbar si ya existen terminos en el diccionario y en el posting_file

    def exist_term_dic(self, term):
        sql = "SELECT num_docs FROM dic WHERE term='"+term+"'"
        try:
            frequency = 0
            exist = False
            result = self.__agente.query(sql)
            if result:
                exist = True
                frequency = result[0][0]
            return (exist,frequency)
        except:
            raise


    def exist_term_posting_file(self, term, id_doc):
        sql = "SELECT COUNT(*) FROM posting_file WHERE term='"+term+"' AND id_doc="+str(id_doc)
        try:
            result = self.__agente.query(sql)
            existe = True
            if result[0][0] == 0: existe = False
            return existe
        except:
            raise           




    def close (self):
        try:
            self.__agente.close()
        except:
            raise

