import sys, os
sys.path.append(os.getcwd() + "/../persistencia")

import fileHandler

class Utilities:
    def __init__(self):
        self.fh = fileHandler.File_Handler()

    def read_text_file (self, path):
        try:
            return self.fh.read_text_file(path)
        except:
            raise
