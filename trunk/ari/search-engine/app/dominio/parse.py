# -*- coding: utf-8 -*-

#    This file is part of pyDMS v1.0: yet another document management system
#    Copyright (C) 2009, Jose Domingo Lopez Lopez & Juan Andrada Romero
#
#    This program is free software: you can redistribute it and/or modify
#    it under the terms of the GNU General Public License as published by
#    the Free Software Foundation, either version 3 of the License, or
#    (at your option) any later version.
#
#    This program is distributed in the hope that it will be useful,
#    but WITHOUT ANY WARRANTY; without even the implied warranty of
#    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
#    GNU General Public License for more details.
#
#    You should have received a copy of the GNU General Public License
#    along with this program.  If not, see <http://www.gnu.org/licenses/>.

import psyco
psyco.full()

import re, string

class Parser:

    def __init__(self):
        self.stop_list = (open("../misc/stoplist","r")).read().split("\n")

    def parse(self, line):
        result = []
        word_list = []
        # Evitamos errores de SQLInjection
        line = line.lower()
        line = line.replace("\\","\\\\")
        line = line.replace("'","\\'")
        separadores=string.punctuation+string.whitespace+"·"
        # Definicion de patrones
        ip_pattern = re.compile('([0-9]{1,3})\.([0-9]{1,3})\.([0-9]{1,3})\.([0-9]{1,3})$')
        section_numbers_pattern = re.compile(r'[0-9]\.?[0-9]?\.?[0-9]?\.[0-9]?\.?[0-9]?\.?$')
        number_pattern = re.compile('[0-9]+$')
        acentoA_pattern = "Á|À|Â|Ä|á|à|â|ä"
        acentoE_pattern = "É|È|Ê|Ë|é|è|ê|ë"
        acentoI_pattern = "Í|Ì|Î|Ï|í|ì|î|ï"
        acentoO_pattern = "Ó|Ò|Ô|Ö|ó|ò|ô|ö|Ø"
        acentoU_pattern = "Ú|Ù|Û|Ü|ú|ù|û|ü"
        acentoY_pattern = "ý|Ý|ÿ|Ÿ|ỳ|Ỳ"
        acentoC_pattern = "Ç|ç"
        # Se obtiene una lista de palabras
        word_list = (re.sub('[%s]' % re.escape(separadores.replace("-.","")), " ", line)).split(" ")
        for word in word_list:
            word_parts = [] 
            word_list_parts = []
            # Para cada palabra de la lista, si no esta en la stop_list y no es una palabra vacia
            if (word not in string.whitespace) and (word not in self.stop_list):
                # Si la palabra es una direccion IP, se toma dicha palabra como termino
                if ip_pattern.match(word):
                    #word = re.sub('[%s]' % re.escape(separadores.replace(".","")), "", word)
                    result.append(word)
                # Se almacenan tambien los numeros 
                elif number_pattern.match(word):
                    result.append(word)
                # Se ignoran separadores de seccion
                elif (not section_numbers_pattern.match(word)):
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
                    if re.search(acentoC_pattern, word):   
                        reg = re.compile("Ç|ç")                  
                        word = reg.sub("c",word) 

                    # Eliminamos los puntos
                    word_list_parts = (re.sub('[%s]' % re.escape("."), " ", word)).split(" ")
                    # De las palabras obtenidas al separar por el punto, si comprueban si tienen guion
                    for w in word_list_parts:
                        # Si la palabra contiene un guion, las palabras separadas por ese guion se almacenan separadas. Tambien se almacenan con el guion
                        if w not in separadores:
                            if w[0].isalnum():
                                if w.find("-")>-1:
                                    word_parts.extend((re.sub('[%s]' % re.escape("-"), " ", w)).split(" "))
                                    # Si la palabra no acaba en guion, se tiene en cuenta
                                    if w[len(w)-1]!="-":
                                        result.append(w) 
                                else:
                                    result.append(w)               
                    # Copia auxiliar para poder recorrer toda la lista de palabras
                    aux = word_parts[:]
                    # Se comprueba que al separar la palabra por signos de puntuacion, todas las palabras obtenidas tengan sentido
                    for a in aux:
                        # Un espacio en blanco se elimina. Si no comienza por una caracter alfanumerico o esta en la stoplist, tambien
                        if (a in string.whitespace) or (not a[0].isalnum()) or (a in self.stop_list): 
                            word_parts.remove(a) 
                          
                    result.extend(word_parts)
            
        return result

    
