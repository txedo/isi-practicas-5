import psyco
psyco.full()

class Vector:
    
    def __init__(self): 
        self.__components = {}

    def get_similarity (self, question):
        product = 0
        module_vector = 0
        module_question = 0
        for term in self.__components:
            try:
                product += (question[term] * self.__components[term])
                module_question += math.pow(question[term] , 2)
            except:
                pass
            module_vector += math.pow(self.__components[term] , 2)
        module_vector = math.sqrt(module_vector)
        module_question = math.sqrt(module_question)

        return (product / (module_vector * module_question))

    def build (self, lista):
        
            
            
        
        
