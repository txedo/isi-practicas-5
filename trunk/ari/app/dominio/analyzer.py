import re
import string
import os
import shutil

class Analyzer:

    # Inicializamos los atributos de la clase (la stop_list)
    def __init__(self):    
        self.stop_list = (open("../misc/stoplist","r")).read()


    # Metodo para indexar un directorio
    def file_index(self, path):

        if not os.path.isfile(path):
            pass # Tratamiento de enlaces simbolicos o ruta incorrecta
        else:
            # Insertamos el documento en la base de datos
            file_name = (path.split("/"))[-1] # Tomamos el nombre del archivo
            (system_path, last_id) = broker.insert_doc(file_name) # Recuperamos la ruta del repositorio y el id de ese documento
            # Copiamos el documento al repositorio
            shutil.copy2(path, system_path)
            # Abrimos y leemos el documento linea a linea, pasando cada linea por el parser
            fd = open(path, "r")
            for line in fd.readlines():
                word_list = parser(line)
                # Para cada una de las palabras procesadas y devueltas por el parser, si no pertenece a la stop_list, actualizamos su frecuencia
                for word in word_list:
                    # Si la palabra ya aparecia en el documento (es decir, en el posting_file de este documento), se aumenta su frecuencia
                    if broker.exist_term_posting_file(word, last_id):
                        broker.update_term_posting_file(word, last_id)
                    else:
                        # Si es la primera vez que aparece la palabra en el documento, se comprueba si esa 
                        # palabra ya aparecia en otro documento. Si no, se añade al diccionario (ademas de al posting_file)
                        if broker.exist_term_dic(word):
                            broker.update_term_dic(word)
                        else:
                            broker.insert_term_dic(word)

                        broker.insert_term_posting_file(word, last_id)
                        
                            

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
        foo = line.translate(string.maketrans("",""),"\t+") # Se cambian los tabuladores por espacios
        word_list = foo.split(" ") # Se obtiene una lista al separar por espacios
        # Para cada palabra de la lista, si NO esta en la stop_list y no es una palabra vacia, se parsea
        for word in word_list:
            if len(word) > 0 and word not in self.stop_list:
                # Si la palabra es una direccion IP, se toma dicha palabra como termino
                if ip_pattern.match(word):
                    result.append(word)
                else: # Si es direccion web, email u otra palabra, se separa por signos de puntuacion
                    word_parts = (re.sub('[%s]' % re.escape(string.punctuation), " ", word)).split(" ")
                    is_compound_word = False
                    # Se comprueba que al separar la palabra por signos de puntuacion, todas las palabras obtenidas tengan sentido
                    for w in word_parts:
                        if w in self.stop_list:
                            is_compound_word = True
                            break
                    # Si se puede separar la palabra, todas ellas se consideran terminos
                    if not is_compound_word:
                        result.extend(word_parts)
                    # Si no se puede separar, se mete la palabra compuesta
                    else:
                        result.append(word)
        return result
