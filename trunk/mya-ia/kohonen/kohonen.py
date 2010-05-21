import psyco
from abalone import *
psyco.full()

DIM = 4
INFINITO = 999999
FACTOR_APRENDIZAJE_INICIAL = 0.9
FACTOR_ESCALA = 510

class Kohonen:

    def __init__(self):
        self.indiceWinner = None
        self.neuronas = []
		
	# "datos" son las lineas del fichero de texto, donde cada linea representa los datos de cada neurona (los 10 atributos)
    def inicializarNeuronas(self, datos):
        for i in datos:
            # Separamos los atributos por el espacio, para poder tomar los numeros decimales
            valoresAtributos = i.split()
			# Se van tomando los atributos de cada linea del fichero. Entre un atributo y otro del fichero hay un espacio
            neurona = Abalone(float(valoresAtributos[0]), float(valoresAtributos[1]), float(valoresAtributos[2]), float(valoresAtributos[3]), float(valoresAtributos[4]), float(valoresAtributos[5]), float(valoresAtributos[6]))
            self.neuronas.append(neurona)
		
		
	# "abalone" representa cada uno de los vectores de entrada al mapa de kohonen
    def evaluarSimilitud(self, abalone):
	    distancia = INFINITO
	    distanciaAux = 0
	    for i in self.neuronas:
	        distanciaAux = abalone.obtenerSimilitud(i)
	        if distancia > distanciaAux:
	            distancia = distanciaAux
	            self.indiceWinner = self.neuronas.index(i)
	    
	    
    def variarGradoVecindad(self, iteracion):
	    # Empezamos con un radio alto y terminamos con un radio bajo
        radio = 0
        if iteracion < 1000: radio = 3
        elif iteracion > 1000 and iteracion < 2000: radio = 2
        elif iteracion > 2000 and iteracion < 3000: radio = 1
        elif iteracion > 3000: radio = 0
        return radio
	    
	    
    def calcularVecindad (self, radio):
        vecinas = []
        if radio == 0:
            vecinas.append(self.neuronas[self.indiceWinner])
        elif radio == DIM-1:
            # Con radio DIM - 1, las vecinas son todas las neuronas. Se hace una copia de la lista para evitar problemas de referencias
            vecinas = self.neuronas[:]
        else:
            fila = self.indiceWinner / DIM
            columna = self.indiceWinner % DIM
            limiteIzquierdo = columna - radio
            if limiteIzquierdo < 0: limiteIzquierdo = 0
            limiteDerecho = columna + radio
            if limiteDerecho > DIM-1: limiteDerecho = DIM-1 
            limiteSuperior = fila - radio
            if limiteSuperior < 0: limiteSuperior = 0
            limiteInferior = fila+radio
            if limiteInferior > DIM-1: limiteInferior = DIM-1
            for i in range(limiteSuperior, limiteInferior+1):
                vecinas.extend(self.neuronas[limiteIzquierdo+i*DIM:limiteDerecho+i*DIM])
        return vecinas
	
	
	# El factor de aprendizaje decrece mas rapido al principio y se estabiliza al final, quedando entre 0 y 0.1
    def variarFactorAprendizaje(self, iteracion):
	    return FACTOR_APRENDIZAJE_INICIAL/(1+iteracion/FACTOR_ESCALA)
	

	# Se varia el peso de las neuronas de la vecindad (segun el radio), aplicando la formula de clase
	# "abalone" corresponde al vector de entrada
    def variarPesoNeuronas(self, radio, abalone, factorAprendizaje):
        vecinas = self.calcularVecindad(radio)
        for neurona in vecinas:
            neurona.length = neurona.length + (abalone.length - neurona.length)*factorAprendizaje
            neurona.diameter = neurona.diameter + (abalone.diameter - neurona.diameter)*factorAprendizaje
            neurona.height = neurona.height + (abalone.height - neurona.height)*factorAprendizaje
            neurona.whole_weight = neurona.whole_weight + (abalone.whole_weight - neurona.whole_weight)*factorAprendizaje
            neurona.shucked_weight = neurona.shucked_weight + (abalone.shucked_weight - neurona.shucked_weight)*factorAprendizaje
            neurona.viscera_weight = neurona.viscera_weight + (abalone.viscera_weight - neurona.viscera_weight)*factorAprendizaje
            neurona.shell_weight = neurona.shell_weight + (abalone.shell_weight - neurona.shell_weight)*factorAprendizaje
	    
	    
    def getNeuronaGanadora(self):
        return self.indiceWinner

