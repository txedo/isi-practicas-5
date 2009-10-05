import MySQLdb

class Singleton(object):

    __instance=None
    __conn=None

    # Constructor
    def __new__(cls):
        # Si no hay ninguna instancia del agente, se inicializa la conexion de la base de datos
        if not cls.__instance:
            cls.__instance=object.__new__(cls)
        cls.__conn = MySQLdb.connect("localhost", "root", "ari", "ari")
        # Metodo para almacenar las transacciones en la BBDD de manera permanente
        cls.__conn.autocommit(1)
        return cls.__instance

    # Metodos para obtener y cerrar la conexion con la BBDD
    def getDB ( self ):
        return self.__conn

    def close ( self ):
        self.__conn.close()

    # Metodos que implementan operacion CRUD (3 comillas en la consulta)
    def query ( self, sql ):
        cursor=self.__conn.cursor()
        cursor.execute(sql)
        result=cursor.fetchall()
        cursor.close()
        return result

    def execute ( self, sql ):
        cursor=self.__conn.cursor()
        cursor.execute(sql)
        cursor.close()


