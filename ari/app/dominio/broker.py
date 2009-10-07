import sys
sys.path.append('../persistencia')
import agente

class Broker:
    def __init__(self):
        self.__agente = None


    def insert_dic(self, dic):
        self.__agente = Agente()

        for i in dic.keys():
            sql = "INSERT INTO dic VALUES ('"+str(i)+"',"+str(dic[i])+")"
            self.__agente.execute(sql)

        self.__agente.close()
        

    def insert_posting_file(self):

    
    def insert_doc(self, doc):
        
