# Source: http://forja.rediris.es/snippet/detail.php?type=snippet&id=24
# License: GNU General Public License

import MySQLdb

class Agente:
    
    __conn = None

    class Singleton:
        def __init_( self ):
            self.Agente = None
            __conn = MySQLdb.connect("localhost", "root", "ari", "ari")
            # Metodo para almacenar las transacciones en la BBDD de manera permanente
            __conn.autocommit()
        
    def __init_( self ):
        if Agente.__conn is None:
            Agente.__conn = Agente.Singleton()
        self.__dict__['_EventHandler_instance'] = Agente.__mInstancia

    # Metodos para obtener y cerrar la conexion con la BBDD
    def getDB ( self ):
        return __conn

    def close ( self ):
        __conn.close()

    # Metodos privados para la gestion del agente       
    def __getattr__( self, aAttr ):
        return getattr(self.__mInstancia, aAttr)
 
    def __setattr__( self, aAttr, aValue ):
        return setattr(self.__mInstancia, aAttr, aValue)

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
    




    
