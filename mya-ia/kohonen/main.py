import psyco
psyco.full()

from kohonen import *
from fileHandler import *
from abalone import *

FACTOR_APRENDIZAJE_INICIAL = 0.9
DIM = 4

if __name__=="__main__":
    mapa = Kohonen()
    fileHandler = File_Handler()
    # Inicializamos cada neurona con el vector definido en el fichero
    datosPesosIniciales = fileHandler.read_lines_file("./ficheroPesos.txt")
    mapa.inicializarNeuronas(datosPesosIniciales)
    # for i in mapa.neuronas:
    #    print (str(i))     
    # Establecemos el radio inicial a DIM-1, para afectar a todo el mapa
    radio = DIM -1 
    # Establecemos el numero de iteracion
    iteracion = 0
    # Establecemos el factor de aprendizaje inicial a 0.9, que es el maximo
    factorAprendizaje = FACTOR_APRENDIZAJE_INICIAL
    # Leemos todas las lineas del fichero de datos de los abalones y vamos reorganizando el mapa de Kohonen
    # Cada linea representa un abalone con los datos normalizados y separados los valores de los atributos por espacios
    datosAbalone = fileHandler.read_lines_file("./ficheroDatosAbalones.txt")
    # Evaluamos cada vector de entrada (abalone) con el mapa
    for d in datosAbalone:
        valoresAtributos = d.split()
        abalone = Abalone(str(valoresAtributos[0]), float(valoresAtributos[1]), float(valoresAtributos[2]), float(valoresAtributos[3]), float(valoresAtributos[4]), float(valoresAtributos[5]), float(valoresAtributos[6]), float(valoresAtributos[7]))
        # Se evalua la similitud, obteniendo una neurona ganadora
        mapa.evaluarSimilitud(abalone)
        # Se cambian los pesos de la vecindad de la neurona ganadora
        mapa.variarPesosNeuronas(radio, abalone, factorAprendizaje)
        # Se varia el radio de vecindad y el factor de aprendizaje para la siguiente iteracion
        radio = mapa.variarRadioVecindad(iteracion)
        factorAprendizaje = mapa.variarFactorAprendizaje(iteracion)
        # Se aumenta la iteracion
        iteracion = iteracion + 1
