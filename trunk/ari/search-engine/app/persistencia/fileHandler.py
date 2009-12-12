
class File_Handler:
    def __init__(self):
        pass

    def read_text_file (self, filename):
        text = ""
        try:    
            f = open (filename, "r")
            for line in f.xreadlines():
                text = text + line
            f.close()
        except:
            raise
        return text

    def write_text_file (self, filename, lines):
        try:
            f = open(filename, "w")
            f.writelines(lines)
            f.close()
        except:
            raise
        
