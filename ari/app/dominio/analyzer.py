import re
import string
import os
import shutil

class Analyzer:

    # Inicializamos los atributos de la clase
    def __init__(self):    
        self.stop_list = (open("../misc/stoplist","r")).read()


    # Metodo para indexar un directorio
    def file_index(self, path):
        # Se ignoraran signos de puntuacion
        #punctuation = "?!.;:[](),\""

        if not os.path.isfile(path):
            pass # Tratamiento de enlaces simbolicos o ruta incorrecta
        else:
            # Insertamos el documento en la base de datos
            file_name = (path.split("/"))[-1]
            (system_path, last_id) = broker.insert_doc(file_name)
            # Copiamos el documento al repositorio
            shutil.copy2(path, system_path)
            # Abrimos y leemos el documento linea a linea, pasando cada linea por el parser
            fd = open(path, "r")
            for line in fd.readlines():
                word_list = parser(line)
                # Para cada una de esas palabras, si no pertenece a la stop_list, actualizamos su frecuencia
                for word in word_list:
                    if broker.exist_term_posting_file(word):
                        broker.update_term_posting_file(word)
                    else:
                        # Si es la primera vez que aparece la palabra en el documento, se comprueba si esa 
                        # palabra ya aparecia en otro documento
                        broker.insert_term_posting_file(word, last_id)
                        if broker.exist_term_dic(word):
                            broker.update_term_dic(word)
                        else:
                            broker.insert_term_dic(word)
                            

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


    def parser(self, line):
        result = {}
        word_list = {}
        ip_pattern = re.compile('([0-9]{1,3})\.([0-9]{1,3})\.([0-9]{1,3})\.([0-9]{1,3})')
        # Separar las palabras por el separador espacio \s
        foo = line.translate(string.maketrans("",""),"\t+")
        word_list = foo.split(" ")
        # Para cada palabra de la lista, si NO esta en la stop_list, parsearla
        for word in word_list:
            if len(word) > 0 and word not in self.stop_list:
                # Si la palabra no es web, ni email, ni direccion IP, se quitan los signos de puntuacion y se comprueba si alguna de las dos partes NO es palabra
                if ip_pattern.match(word): # Es una IP y la tratamos como tal
                    result.append(word)
                else: # sea web, email o lo que sea, quitamos signos de puntuacion
                    word_parts = (re.sub('[%s]' % re.escape(string.punctuation), " ", word)).split(" ")
                    is_compound_word = False
                    for w in word_parts:
                        if w in self.stop_list:
                            is_compound_word = True
                            break
                    # Si se puede separar, meto en la BD todas las partes
                    if not is_compound_word:
                        result.extend(word_parts)
                    # Si no se puede separar, meto la palabra compuesta
                    else:
                        result.append(word)
        return result
