from math import *
import psyco
psyco.full()

# Quitamos el sexo para evitar mucha distancia entre vectores. Se tendra en cuenta a la hora de sacar el volumen 
class Abalone:
    def __init__(self, length, diameter, height, whole_weight, shucked_weight, viscera_weight, shell_weight):
        self.length = length
        self.diameter = diameter
        self.height = height
        self.whole_weight = whole_weight
        self.shucked_weight = shucked_weight
        self.viscera_weight = viscera_weight
        self.shell_weight = shell_weight
    
    def obtenerSimilitud(self, other):
        return sqrt(pow(self.length-other.length, 2)+pow(self.diameter-other.diameter, 2)+pow(self.height-other.height, 2)+pow(self.whole_weight-other.whole_weight, 2)+pow(self.shucked_weight-other.shucked_weight, 2)+pow(self.viscera_weight-other.viscera_weight, 2)+pow(self.shell_weight-other.shell_weight, 2))

    def __str__ (self):
        return str(self.length) + " " + str(self.diameter) + " " + str(self.height) + " " + str(self.whole_weight) + " " + str(self.shucked_weight) + " " + str(self.viscera_weight) + " " + str(self.shell_weight)
        
