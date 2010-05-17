import psyco
pysco.full()

DIM = 4
INFINITO = 999999
FACTOR_APRENDIZAJE_INICIAL = 0.9
FACTOR_ESCALA = 600

class Kohonen:

	def __init__(self):
		self.indiceWinner = None
		self.neuronas = []
		self.inicializarNeuronas()
		
		
    def inicializarNeuronas(self):
        pass
		
		
	def evaluarSimilitud(self, abalone):
	    distancia = INFINITO
	    distanciaAux = 0
	    for i in neuronas:
	        distanciaAux = abalone.obtenerSimilitud(i)
	        if distancia > distanciaAux:
	            distancia = distanciaAux
	            self.indiceWinner = neuronas.index(i)
	    
	    
	def variarGradoVecindad(self, iteracion):
	    # Empezamos con un radio alto y terminamos con un radio bajo
	    radio = 0
	    if iteracion < 1000: radio = 3
	    elif iteracion > 1000 and iteracion < 2000: radio 2
	    elif iteracion > 2000 and iteracion < 3000: radio 1
	    elif iteracion > 3000: radio = 0
	    return radio
	    
	    
	def calcularVecindad (self, radio):
	    vecinas = []
	    if radio == 0:
	        vecinas.append(self.neuronas[self.indiceWinner])
	    elif radio == 3:
	        vecinas.append(self.neuronas)
	    else:
	        fila = self.indiceWinner / DIM
	        columna = self.indiceWinner % DIM
	        limiteIzquierdo = columna - radio
	        if limiteIzquiero < 0: limiteIzquierdo = 0
	        limiteDerecho = columna + radio
	        if limiteDerecho > DIM-1: limiteDerecho = DIM-1
	        limiteSuperior = fila - radio
	        if limiteSuperior < 0: limiteSuperior = 0
	        limiteInferior = fila+radio
	        if limiteInferior > DIM-1 = limiteInferior = DIM-1
            for i in range(limiteSuperior, limiteInferior+1):
                vecinas.append(self.neuronas[limiteIzquierdo+i*DIM:limiteDerecho+i*DIM])
        return vecinas
	
	
	def variarFactorAprendizaje(self, iteracion):
	    return FACTOR_APRENDIZAJE_INICIAL/(1+iteracion/FACTOR_ESCALA)
	
	
	def variarPesoNeurona(self):
	    pass
	    
	    
	def getNeuronaGanadora(self):
	    pass
