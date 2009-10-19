import sys
sys.path.append('../persistencia')
import dao
from config import *

class Cache:
    def __init__(self):
        try:
            self.dao = dao.Dao()
            self.__new = {}
            self.__old = {}
            self.caches = [self.__new, self.__old]
        except:
            raise


    def exists(self, term):
        what_cache = None
        if term in self.__new: what_cache = NEW_CACHE
        elif term in self.__old: what_cache = OLD_CACHE
        else: what_cache = NOT_CACHE
        return what_cache


    def load(self,term,cache,frequency=1):
        self.caches[cache][term] = frequency
        if len(self.__new) + len(self.__old) >= MAX_CACHE_SIZE:
            self.synchronize()


    def get_frequency (self,term,cache):
        return self.caches[cache][term]

    
    def synchronize (self):
        try:
            #print "-->", len(self.__new) + len(self.__old)
            for n in self.__new:
                self.dao.insert_term_dic(n,self.__new[n])
            self.__new={}
            for o in self.__old:
                self.dao.update_term_dic(o,self.__old[o])
            self.__old={}
            self.caches = [self.__new, self.__old]
        except:
            raise
