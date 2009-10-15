import MySQLdb
    
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
        except MySQLdb.Error, e:
            raise e

    def close ( self ):
        try:
            self.__conn.close()
        except MySQLdb.Error, e:
            raise e


    # Metodos que implementan operacion CRUD (3 comillas en la consulta)
    def query ( self, sql ):
        try:
            cursor=self.__conn.cursor()
            cursor.execute(sql)
            result=cursor.fetchall()
            cursor.close()
            return result
        except MySQLdb.Error, e:
            raise e


    def execute ( self, sql ):
        try:
            cursor=self.__conn.cursor()
            cursor.execute(sql)
            cursor.close()
        except MySQLdb.Error, e:
            raise e


