from math import *

class Abalone:
    def __init__(self, sex, length, diameter, height, whole_weight, shucked_weight, viscera_weight, shell_weight):
    """ sex es de tipo string y toma los valores "M", "F", "I"
        el resto de atributos son de tipo real
    """
        self.m = 0
        self.f = 0
        self.i = 0
        if sex == "M": self.m = 1
        else if sex == "F": self.f = 1
        else self.i = 1
        self.length
        self.diameter
        self.height
        self.whole_weight
        self.shucked_weight
        self.viscera_weight
        self.shell_weight
    
    def obtenerSimilitud(self, other):
        return sqrt(pow(self.m-other.m, 2)+pow(self.f-other.f, 2)+pow(self.i-other.i, 2)+pow(self.length-other.length, 2)+pow(self.diameter-other.diameter, 2)+pow(self.height-other.height, 2)+pow(self.whole_height-other.whole_height, 2)+pow(self.shucked_weight-other.shucked_weight, 2)+pow(self.viscera_weight-other.viscera_weight, 2)+pow(self.shell_weight-other.shell_weight, 2))
        
