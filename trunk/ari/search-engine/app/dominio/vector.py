import psyco
psyco.full()

import math

class Vector:
    
    def __init__(self): 
        self.__components = {}
        self.__title = None


    def add(self, term, weight):
        self.__components[term] = weight

    def set_title (self, title):
        self.__title = title

    def get_similarity (self, question, module_question):
        similarity = 0
        product = 0
        module_vector = 0
        for term in self.__components:
            try:
                product += (question[term] * self.__components[term])
            except:
                pass
            module_vector += math.pow(self.__components[term], 2)
        module_vector = math.sqrt(module_vector)

        try:
            similarity = (product / (module_vector * module_question))
        except ZeroDivisionError:
            raise ZeroDivisionError
        return similarity

    def get_title(self):
        return self.__title
