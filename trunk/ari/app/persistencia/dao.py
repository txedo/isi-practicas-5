from agente import *
from config import *


class Dao:
    def __init__(self):
        try: 
            self.__agente = Agente()
        except:
            raise

    def insert_term_dic(self, term):
        sql = "INSERT INTO dic (term) VALUES ('"+term+"')"
        try:          
            self.__agente.execute(sql)
        except:
            raise
        

    def insert_term_posting_file(self, term, id_doc):
        sql = "INSERT INTO posting_file (term, id_doc) VALUES ('"+term+"',"+str(id_doc)+")"
        try:
            self.__agente.execute(sql)
        except:
            raise

    
    def insert_doc(self, doc):
        # Insertamos el nuevo documento
        sql = "INSERT INTO doc (id_doc, title) VALUES('%','"+doc+"')"
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
        
    
    def update_term_dic(self, term):
        sql = "UPDATE dic SET num_docs=num_docs+1 WHERE term='"+term+"'"
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


    def exist_term_dic(self, term):
        sql = "SELECT COUNT(*) FROM dic WHERE term='"+term+"'"
        try:            
            result = self.__agente.query(sql)
            existe = True
            if result[0][0] == 0: existe = False
            return existe
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

