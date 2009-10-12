import sys
sys.path.append('../persistencia')
import agente
from config import *


class Broker:
    def __init__(self):
        self.__agente = None


    def insert_term_dic(self, term):
        self.__agente = Agente()
        sql = "INSERT INTO dic (term) VALUES ('"+term+"')"
        self.__agente.execute(sql)
        self.__agente.close()
        

    def insert_term_posting_file(self, term, id_doc):
        self.__agente = Agente ()
        sql = "INSERT INTO posting_file (term, id_doc) VALUES ('"+term+"',"+id_doc+")"
        self.__agente.execute(sql)
        self.__agente.close()

    
    def insert_doc(self, doc):
        self.__agente = Agente()
        # Insertamos el nuevo documento
        sql = "INSERT INTO doc (id_doc, title) VALUES('%','"+doc+"')"
        self.__agente.execute(sql)
        # Recuperamos el ID que se le ha asignado
        sql = "SELECT LAST_INSERT_ID()"
        result = self.__agente.query(sql)
        last_id = result[0][0]
        # Actualizamos la ruta del documento
        path = REPOSITORY_PATH + last_id + ".txt"
        sql = "UPDATE doc SET path='" + path + "' WHERE id_doc=" + last_id
        self.__agente.execute(sql)
        self.__agente.close()
        return (path, last_id)
        
    
    def update_term_dic(self, term):



    def update_term_posting_file(self, term):
        self.__agente = Agente()
        sql = "UPDATE posting_file SET frequency=frequency+1 WHERE term='"+term+"'"
        self.__agente.execute(sql)
        self.__agente.close()


    def exist_term_dic(self, term):



    def exist_term_posting_file(self, term):
        self.__agente = Agente()
        sql = "SELECT COUNT(*) FROM posting_file WHERE term='"+term+"'"
        result = self.__agente.query(sql)
        existe = True
        if result[0][0] == 0: existe = False
        self.__agente.close()
        return existe
