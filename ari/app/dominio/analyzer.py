import re
import string
import os

class Analyzer:

    # Inicializamos los atributos de la clase
    def __init__(self):    
        self.posting_file = {}    
        self.dic = {}
        self.docs = {}
    
    # Metodo para indexar un directorio
    def file_index(self, path):
        # Se ignoraran signos de puntuacion
        punctuation = "?!.;:[](),\""
        # Leemos del fichero la stop_list
        fd_stop_list = open ("../misc/stoplist","r")
        stop_list = fd_stop_list.read()

        if not os.path.isfile(path):
            pass # Tratamiento de enlaces simbolicos
        else:
            # Abrimos y leemos el documento
            fd = open(path, "r")
            text = fd.read().lower()
            # Eliminamos los signos de puntuacion y obtenemos la lista de palabras que forman el texto
            result = text.translate(string.maketrans("",""),punctuation)
            word_list = re.split('\s+', result)
            # Para cada una de esas palabras, si no pertenece a la stop_list, actualizamos su frecuencia
            for word in word_list:
                if word not in stop_list:
                    try:
                        self.posting_file[word]+=1
                    # Si es la primera vez que aparece la palabra en el documento, se comprueba si esa 
                    # palabra ya aparecia en otro documento
                    except:
                        self.posting_file[word]=1
                        try:
                            self.dic[word]+=1
                        except:
                            self.dic[word]=1        


    # Metodo para indexar un directorio
    def folder_index(self, path):
        # Si no es un directorio, se lanza una excepcion
        if not os.path.isdir(path):
            raise Exception
        else:
            # Se lista el contenido del directorio y se procesan los ficheros
            list_files = os.listdir(path)
            for f in list_files:
                full_path = path+"/"+f
                if os.path.isdir(full_path):
                    pass # Tratamiento de directorios
                else:
                    self.file_index(full_path)


