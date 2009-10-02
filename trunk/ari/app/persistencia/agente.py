# Source: http://forja.rediris.es/snippet/detail.php?type=snippet&id=24
# License: GNU General Public License

from sqlobject import *

class Agente:
    
    __conn = None
    __dbuser = "root"
    __dbpassword = "ari"
    __host = "localhost"
    __port = 3306
    __dbname = "ari"
    __mURI="mysql://"+__dbuser+":"+__dbpassword+"@"+__host+":"+str(__port)+"/"+__dbname

    class Singleton:
        def __init_( self ):
            self.Agente = None
            __conn = connectionForURI(__mURI)
        
    def __init_( self ):
        if Agente.__conn is None:
            Agente.__conn = Agente.Singleton()
        self.__dict__['_EventHandler_instance'] = Agente.__mInstancia

    def getDB ( self ):
        return connectionForURI(__mURI)
        
    def __getattr__(self, aAttr):
        return getattr(self.__mInstancia, aAttr)
 
    def __setattr__(self, aAttr, aValue):
        return setattr(self.__mInstancia, aAttr, aValue)
