import sys, os
sys.path.append(os.getcwd() + "/../persistencia")

import fileHandler, bdDao

class Utilities:
    def __init__(self):
        self.fh = fileHandler.File_Handler()

    def read_text_file (self, path):
        try:
            return self.fh.read_text_file(path)
        except:
            raise

    def get_indexed_documents(self):
        docs = []
        try:
            dao = bdDao.Dao()
            docs = dao.get_indexed_documents()
            #dao.close()
        except:
            raise
        return docs
