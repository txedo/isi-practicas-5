import sys, os
sys.path.append(os.getcwd() + "/../persistencia")

import file_handler

class Utilities:
    def __init__(self):
        self.fh = file_handler.File_Handler()

    def read_text_file (self, path):
        try:
            return self.fh.read_text_file(path)
        except:
            raise
