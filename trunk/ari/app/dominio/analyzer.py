import re
import string
import os

class Analyzer:

    def __init__(self):    
        self.posting_file = {}    
        self.dic = {}
        self.docs = {}
    
    def file_index(self, path):
        punctuation = "?!.;:[](),\""
        if not os.path.isfile(path):
            pass # Tratamiento de enlaces simbolicos
        else:
            fd = open(path, "r")
            text = fd.read().lower()
            result = text.translate(string.maketrans("",""),punctuation)
            word_list = re.split('\s+', result)
            for word in word_list:
                try:
                    self.posting_file[word]+=1
                except:
                    self.posting_file[word]=1
                    try:
                        self.dic[word]+=1
                    except:
                        self.dic[word]=1        


    def folder_index(self, path):
        if not os.path.isdir(path):
            raise Exception
        else:
            list_files = os.listdir(path)
            for f in list_files:
                full_path = path+"/"+f
                if os.path.isdir(full_path):
                    pass # Tratamiento de directorios
                else:
                    self.file_index(full_path)


        
        
        



