import psyco
psyco.full()

from kohonen import *
from fileHandler import *
from abalone import *


if __name__=="__main__":
    mapa = Kohonen()
    fileHandler = File_Handler()
    # Leer neuronas obtenidas en el algoritmo
    res = fileHandler.read_lines_file("./resultados.txt")
    mapa.inicializarNeuronas(res)    

    # Leemos el conjunto de test
    datosAbalone = fileHandler.read_lines_file("./test.txt")
    for d in datosAbalone:
        valoresAtributos = d.split()
        abalone = Abalone(float(valoresAtributos[0]), float(valoresAtributos[1]), float(valoresAtributos[2]), float(valoresAtributos[3]), float(valoresAtributos[4]), float(valoresAtributos[5]), float(valoresAtributos[6]))
        # Se evalua la similitud, obteniendo una neurona ganadora
        mapa.evaluarSimilitud(abalone)

        print (mapa.getNeuronaGanadora())
