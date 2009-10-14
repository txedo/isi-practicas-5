import re
import string
import os
import shutil
import sys
sys.path.append('../persistencia')
import dao

class Analyzer:

    # Inicializamos los atributos de la clase (la stop_list)
    def __init__(self):    
        self.stop_list = (open("../misc/stoplist","r")).read().split("\n")
        self.dao = None


    # Metodo para indexar un fichero
    def file_index(self, path):
        analyzing_directory = True
        if not os.path.isfile(path):
            print path
            pass # Tratamiento de enlaces simbolicos o ruta incorrecta
        else:
            # Controlamos si indexamos un directorio o un fichero
            if self.dao == None:
                self.dao = dao.Dao()
                analyzing_directory = False

            # Insertamos el documento en la base de datos
            file_name = (path.split("/"))[-1] # Tomamos el nombre del archivo
            (system_path, last_id) = self.dao.insert_doc(file_name) # Recuperamos la ruta del repositorio y el id de ese documento
            # Copiamos el documento al repositorio
            shutil.copy2(path, system_path)
            # Abrimos y leemos el documento linea a linea, pasando cada linea por el parser
            fd = open(path, "r")
            for line in fd.readlines():
                word_list = self.parser(line.lower())
                # Si el parser ha procesado alguna palabra, se actualiza la base de datos
                if len(word_list) > 0:
                   # print "Word list definitiva", word_list
                    # Para cada una de las palabras procesadas y devueltas por el parser, si no pertenece a la stop_list, actualizamos su frecuencia
                    for word in word_list:
                        # Si la palabra ya aparecia en el documento (es decir, en el posting_file de este documento), se aumenta su frecuencia
                        if self.dao.exist_term_posting_file(word, last_id):
                            self.dao.update_term_posting_file(word, last_id)
                        else:
                            # Si es la primera vez que aparece la palabra en el documento, se comprueba si esa 
                            # palabra ya aparecia en otro documento. Si no, se anade al diccionario (ademas de al posting_file)
                            if self.dao.exist_term_dic(word):
                                self.dao.update_term_dic(word)
                            else:
                                self.dao.insert_term_dic(word)
                            self.dao.insert_term_posting_file(word, last_id)
                        
            if not analyzing_directory:
                self.dao.close()
                            

    # Metodo para indexar un directorio
    def folder_index(self, path):
        # Si no es un directorio, se lanza una excepcion
        if not os.path.isdir(path):
            raise Exception
        else:
            # Se lista el contenido del directorio y se procesan los ficheros
            list_files = os.listdir(path)
            self.dao = dao.Dao()
            for f in list_files:
                full_path = path+"/"+f
                if os.path.isdir(full_path):
                    pass # Tratamiento de directorios
                else:
                    self.file_index(full_path)
            self.dao.close()


    def parser(self, line):
        result = []
        word_list = []
        separadores=string.punctuation+string.whitespace
        ip_pattern = re.compile('([0-9]{1,3})\.([0-9]{1,3})\.([0-9]{1,3})\.([0-9]{1,3})')
        #foo = line.translate(string.maketrans("",""),string.whitespace) # Se cambian los tabuladores por espacios
        #foo = re.sub('[%s]' % re.escape(string.whitespace), " ", line)
        word_list = line.split(" ") # Se obtiene una lista al separar por espacios
        #print "LINEA: ", word_list
        # Para cada palabra de la lista, si NO esta en la stop_list y no es una palabra vacia, se parsea
        for word in word_list:
            #print "Palabra: ", word
            if (word not in string.whitespace) and (word not in self.stop_list):
                # Si la palabra es una direccion IP, se toma dicha palabra como termino
                if ip_pattern.match(word):
                    result.append(word)
                else: # Si es direccion web, email u otra palabra, se separa por signos de puntuacion
                    #print "word: ", word
                    # Eliminamos el caracter ' (con \\' tambien peta la base de datos)
                    word.replace("\'","a")
                    word_parts = (re.sub('[%s]' % re.escape(separadores), " ", word)).split(" ")
                    #print "word_parts: ", word_parts
                    is_compound_word = False
                    delete = False
                    # Copia auxiliar para poder recorrer toda la lista de palabras
                    aux = word_parts[:]
                    # Se comprueba que al separar la palabra por signos de puntuacion, todas las palabras obtenidas tengan sentido
                    for w in aux:
                        #print "Palabra: ", w
                        if w in string.whitespace: 
                            word_parts.remove(w)
                        # Si una palabra de la stop_list solo va seguida de espacios, se elimina
                        elif w in self.stop_list:
                            for i in aux[aux.index(w)+1:]:
                                if i in string.whitespace:
                                    delete = True                                          
                                else:
                                    delete = False
                                    break
                            if not delete:
                                is_compound_word = True
                                #print "Palabra que no puede separarse porque esta en la stoplist:", w
                                #print self.stop_list[self.stop_list.index(w)]
                                break
                            else:
                                word_parts.remove(w)
                    #print "Word parts despues ", word_parts
                    # Si se puede separar la palabra, todas ellas se consideran terminos
                    if not is_compound_word:
                        #print "Entra aqui"
                        result.extend(word_parts)
                    # Si no se puede separar, se mete la palabra compuesta
                    else:
                        #print "Entra alli"
                        result.append(word)
            
        return result


anal = Analyzer()
anal.file_index("/home/juan/Escritorio/3Dfx-HOWTO.txt")
