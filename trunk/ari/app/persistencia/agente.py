import MySQLdb

class ConnectionException(exception.Exception):
    def __init__(self):
		return
		
	def __str__(self):
		print "Connection cannot be created

    
class Agente(object):


    __instance=None
    __conn=None


    # Constructor
    def __new__(cls):
        try:
            # Si no hay ninguna instancia del agente, se inicializa la conexion de la base de datos
            if not cls.__instance:
                cls.__instance=object.__new__(cls)
            cls.__conn = MySQLdb.connect("localhost", "root", "ari", "ari")
            # Metodo para almacenar las transacciones en la BBDD de manera permanente
            cls.__conn.autocommit(1)
            return cls.__instance
        except ConnectionException, e:
            raise e
                 

    def close ( self ):
        try:
            self.__conn.close()
        except: 
            raise Exception, "Connection to database cannot be closed"


    # Metodos que implementan operacion CRUD (3 comillas en la consulta)
    def query ( self, sql ):
        try:
            cursor=self.__conn.cursor()
            cursor.execute(sql)
            result=cursor.fetchall()
            cursor.close()
            return result
        except:
            raise Exception, "Cannot run a query"


    def execute ( self, sql ):
        try:
            cursor=self.__conn.cursor()
            cursor.execute(sql)
            cursor.close()
        except:
            raise Exception, "Cannot run an action"


