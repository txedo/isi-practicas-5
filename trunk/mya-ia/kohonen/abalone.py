from math import *
import psyco
psyco.full()

class Abalone:
    def __init__(self, m, f, i, length, diameter, height, whole_weight, shucked_weight, viscera_weight, shell_weight):
        # sex es de tipo string y toma los valores "M", "F", "I" El resto de atributos son de tipo real
        self.m = m
        self.f = f
        self.i = i
        self.length = length
        self.diameter = diameter
        self.height = height
        self.whole_weight = whole_weight
        self.shucked_weight = shucked_weight
        self.viscera_weight = viscera_weight
        self.shell_weight = shell_weight
    
    def obtenerSimilitud(self, other):
        return sqrt(pow(self.m-other.m, 2)+pow(self.f-other.f, 2)+pow(self.i-other.i, 2)+pow(self.length-other.length, 2)+pow(self.diameter-other.diameter, 2)+pow(self.height-other.height, 2)+pow(self.whole_weight-other.whole_weight, 2)+pow(self.shucked_weight-other.shucked_weight, 2)+pow(self.viscera_weight-other.viscera_weight, 2)+pow(self.shell_weight-other.shell_weight, 2))

    def __str__ (self):
        return str(self.m) + " " + str(self.f) + " " + str(self.i) + " " + str(self.length) + " " + str(self.diameter) + " " + str(self.height) + " " + str(self.whole_weight) + " " + str(self.shucked_weight) + " " + str(self.viscera_weight) + " " + str(self.shell_weight)
        
