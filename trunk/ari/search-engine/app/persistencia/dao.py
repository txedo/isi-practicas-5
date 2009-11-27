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
from config import *
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
            d1 = datetime.datetime.now()
            sql_view = "CREATE VIEW " + view_name + " AS SELECT DISTINCT p1.id_doc FROM posting_file p1 WHERE p1.term IN ("
            for i in question:
                sql_view += "'" + i + "',"
            sql_view = sql_view[:len(sql_view)-1] + ")"
            self.execute(sql_view)
            #print datetime.datetime.now()-d1
            #d1 = datetime.datetime.now()
            sql_select = "SELECT p.term, p.id_doc, p.frequency, d.num_docs FROM posting_file p, dic d WHERE p.id_doc IN (SELECT * FROM " + view_name + ") and d.term=p.term"
            result = self.query(sql_select)
            sql_drop_view = "DROP VIEW " + view_name
            self.execute(sql_drop_view)
            print datetime.datetime.now()-d1
            #input()
        except:
            raise
        
        #(term, id_doc, frequency, num_docs)
        return result


    """def select (self, question):
        timestamp = str(datetime.datetime.now().microsecond)
        view_name = "view_relevant_docs_"+timestamp
        result = None

        try:
            d1 = datetime.datetime.now()
            sql_view = "CREATE VIEW " + view_name + " AS SELECT DISTINCT p1.id_doc FROM posting_file p1 WHERE p1.term IN ("
            for i in question:
                sql_view += "'" + i + "',"
            sql_view = sql_view[:len(sql_view)-1] + ")"
            self.execute(sql_view)
            #print datetime.datetime.now()-d1
            #d1 = str(datetime.datetime.now().microsecond)
            sql_select = "CREATE VIEW res AS SELECT p.term, p.id_doc, p.frequency, d.num_docs FROM posting_file p, dic d WHERE p.id_doc IN (SELECT * FROM " + view_name + ") and d.term=p.term"
            self.execute(sql_select)
            result = self.query("SELECT * FROM res")
            #print datetime.datetime.now()-d1
            sql_drop_view = "DROP VIEW " + view_name
            self.execute(sql_drop_view)
            sql_drop_view = "DROP VIEW res"
            self.execute(sql_drop_view)
            #input()
            print datetime.datetime.now()-d1
        except:
            raise

        return result"""


    # Metodos para insertar terminos en las tablas de la base de datos     

    """def insert_term_dic_duplicate(self, term_buffer):
        sql = "INSERT INTO dic (term, num_docs) VALUES "
        try:          
            for i in term_buffer:
                sql += "('"+i+"', " + str(term_buffer[i]) + "),"
            sql = sql[:len(sql)-1]+" ON DUPLICATE KEY UPDATE num_docs=num_docs+VALUES(num_docs)"
            self.execute(sql)
        except:
            raise"""


    def insert_aux(self, term_buffer):
        sql = "INSERT INTO aux (term, num_docs) VALUES "
        try:          
            for i in term_buffer:
                sql += "('"+i+"', " + str(term_buffer[i]) + "),"
            sql = sql[:len(sql)-1]+";"
            self.execute(sql)
        except:
            raise


    def update_dic(self):
        timestamp = str(datetime.datetime.now().microsecond)
        view_name = "view_sum_"+timestamp
        sql = "CREATE VIEW " + view_name + " AS SELECT a.term, sum(a.num_docs) AS num_docs FROM aux a GROUP BY a.term"
        try:
            self.execute(sql)
            db_size = self.get_num_docs()
            if db_size >0:
                #timestamp = str(datetime.datetime.now().microsecond)
                #view_name_res = "view_res_"+timestamp
                #sql = "CREATE VIEW " + view_name_res + " AS SELECT d.term, (d.num_docs+v.num_docs) AS num_docs FROM dic d, "+ view_name + " v WHERE d.term=v.term"
                #self.execute(sql)
                #sql = "DELETE FROM DIC d WHERE d.term IN (SELECT v.term FROM " + view_name_res +" v)"
                #self.execute(sql)
                sql = "INSERT INTO dic (term, num_docs) (SELECT * FROM " + view_name+ ") ON DUPLICATE KEY UPDATE dic.num_docs=dic.num_docs+VALUES(num_docs)"
                self.execute(sql)
                #self.execute("DROP VIEW "+view_name_res)
            else:  
                sql = "INSERT INTO dic (term, num_docs) (SELECT * FROM " + view_name + ")"
                self.execute(sql)
            self.execute("DROP VIEW "+view_name)
            self.clean_aux()
        except:
            raise


    def insert_term_posting_file_multi(self, posting_file, last_id):
        sql = "INSERT INTO posting_file (term, id_doc, frequency) VALUES "
        try:
            for k in posting_file:
                sql += "('"+k+"',"+str(last_id)+","+str(posting_file[k])+"),"
            self.execute(sql[:len(sql)-1]+";")
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
            self.execute(sql)
            return (path, last_id)
        except:
            raise

    
    def clean_aux (self):
        sql = "DELETE FROM aux"
        try:
            self.execute(sql)
        except:
            raise


    def close (self):
        try:
            self.__agente.close()
        except:
            raise

