# -*- coding: utf-8 -*-

#     This file is part of Index Engine v1.0
#     Copyright (C) 2009 Jose Domingo López & Juan Andrada Romero

#     This program is free software: you can redistribute it and/or modify
#     it under the terms of the GNU General Public License as published by
#     the Free Software Foundation, either version 3 of the License, or
#     (at your option) any later version.

#     This program is distributed in the hope that it will be useful,
#     but WITHOUT ANY WARRANTY; without even the implied warranty of
#     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
#     GNU General Public License for more details.

#     You should have received a copy of the GNU General Public License
#     along with this program.  If not, see <http://www.gnu.org/licenses/>.


import psyco
psyco.full()
import re
import string

class Parser:

    def __init__(self):
        self.stop_list = (open("../misc/stoplist","r")).read().split("\n")

    def parse(self, line):
        result = []
        word_list = []
        # Evitamos errores de SQLInjection
        line = line.replace("\\","\\\\")
        line = line.replace("'","\\'")
        separadores=string.punctuation+string.whitespace+"·"
        # Definicion de patrones
        ip_pattern = re.compile('([0-9]{1,3})\.([0-9]{1,3})\.([0-9]{1,3})\.([0-9]{1,3})$')
        section_numbers_pattern = re.compile(r'[0-9]\.?[0-9]?\.?[0-9]?\.[0-9]?\.?[0-9]?\.?$')
        #number_pattern = re.compile(r'[0-9]+$')
        acentoA_pattern = "Á|À|Â|Ä|á|à|â|ä"
        acentoE_pattern = "É|È|Ê|Ë|é|è|ê|ë"
        acentoI_pattern = "Í|Ì|Î|Ï|í|ì|î|ï"
        acentoO_pattern = "Ó|Ò|Ô|Ö|ó|ò|ô|ö|Ø"
        acentoU_pattern = "Ú|Ù|Û|Ü|ú|ù|û|ü"
        acentoY_pattern = "ý|Ý|ÿ|Ÿ|ỳ|Ỳ"
        acentoC_pattern = "Ç|ç"
        # Se obtiene una lista de palabras al separar por espacios
        word_list = line.split(" ") 
        for word in word_list:
            # Para cada palabra de la lista, si no esta en la stop_list, no es una palabra vacia y no es un numero, se parsea
            if (word not in string.whitespace) and (word not in self.stop_list):
                # Si la palabra es una direccion IP, se toma dicha palabra como termino (eliminando signos de puntuación menos el punto)
                if ip_pattern.match(word):
                    #word = re.sub('[%s]' % re.escape(separadores.replace(".","")), "", word)
                    result.append(word)
                # Se ignoran separadores de seccion
                elif (not section_numbers_pattern.match(word)):
                #else: # Si es direccion web, email u otra palabra, se separa por signos de puntuacion
                    # Estandarizamos las vocales
                    if re.search(acentoA_pattern, word): 
                        reg = re.compile("Á|À|Â|Ä|á|à|â|ä")
                        word = reg.sub("a",word)
                    if re.search(acentoE_pattern, word): 
                        reg = re.compile("É|È|Ê|Ë|é|è|ê|ë")
                        word = reg.sub("e",word)
                    if re.search(acentoI_pattern, word):            
                        reg = re.compile("Í|Ì|Î|Ï|í|ì|î|ï")         
                        word = reg.sub("i",word)
                    if re.search(acentoO_pattern, word):    
                        reg = re.compile("Ó|Ò|Ô|Ö|ó|ò|ô|ö|Ø")                 
                        word = reg.sub("o",word)
                    if re.search(acentoU_pattern, word):   
                        reg = re.compile("Ú|Ù|Û|Ü|ú|ù|û|ü")                  
                        word = reg.sub("u",word)
                    if re.search(acentoY_pattern, word):   
                        reg = re.compile("ý|Ý|ÿ|Ÿ|ỳ|Ỳ")                  
                        word = reg.sub("y",word)   
                    #if re.search(acentoC_pattern, word):   
                    #    reg = re.compile("Ç|ç")                  
                    #    word = reg.sub("c",word) 


                    # Dividimos la palabra en partes, al reemplazar los separadores      
                    word_parts = (re.sub('[%s]' % re.escape(separadores), " ", word)).split(" ")
                    # Copia auxiliar para poder recorrer toda la lista de palabras
                    aux = word_parts[:]
                    # Se comprueba que al separar la palabra por signos de puntuacion, todas las palabras obtenidas tengan sentido
                    for w in aux:
                        # Un espacio en blanco se elimina. Si no comienza por letra tambien
                        if (w in string.whitespace) or (not w[0].isalnum()) or (w in self.stop_list): 
                            word_parts.remove(w)
                        
                    result.extend(word_parts)
            
        return result
