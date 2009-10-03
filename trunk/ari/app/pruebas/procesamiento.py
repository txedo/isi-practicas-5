# Archivo de prueba para eliminar signos de puntuacion seguidos de espacios en blancos. Se utilizan las expresiones regulares de python
import re

fd = file('ejemplo.txt')
punctuation=re.compile('((\.{3})|(\.\s)|(,\s)|(:\s)|(\?\s))')
string=fd.readline().lower()
result=punctuation.sub(" ",string)

print "\nOriginal String: ", string 
print "Modified String: ", result


