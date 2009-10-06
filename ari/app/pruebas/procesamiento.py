# Archivo de prueba para eliminar signos de puntuacion seguidos de espacios en blancos. Se utilizan las expresiones regulares de python
import re
import datetime

fd = open("../misc/3Dfx-HOWTO.txt", "r")

d1=datetime.datetime.now()
punctuation=re.compile('((\.{3}\s)|(\.\s)|(,\s)|(:\s)|(\?\s)|(\!\s)|(")|(\;\s))')

for string in fd:
    result=punctuation.sub(" ",string)
d2=datetime.datetime.now()

fd.close()

print "\nOriginal String: ", string 
print "Modified String: ", result

print "TIEMPO: ", (d2-d1)

