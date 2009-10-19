import exceptions

class FileException(exceptions.Exception):
		
    def __init__(self, path): 
        self.path=path
        return
    def __str__(self): return self.path+" is not a file"


class FolderException(exceptions.Exception):
		
    def __init__(self, path): 
        self.path=path
        return
    def __str__(self): return self.path+" is not a folder"
