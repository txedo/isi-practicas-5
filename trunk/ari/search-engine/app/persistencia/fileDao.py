# -*- coding: utf-8 -*-

#     This file is part of Index Engine v1.0
#     Copyright (C) 2009 Jose Domingo López & Juan Andrada Romero

#     This program is free software: you can redistribute it and/or modify
#     it under the terms of the GNU General Public License as published by
#     the Free Software Foundation, either version 3 of the License, or
#     (at your option) any later version.

#     This program is distributed in the hope that it will be useful,
#     but WITHOUT ANY WARRANTY; without even the implied warranty of
#     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
#     GNU General Public License for more details.

#     You should have received a copy of the GNU General Public License
#     along with this program.  If not, see <http://www.gnu.org/licenses/>.


import psyco
psyco.full()

import sys, os
sys.path.append(os.getcwd() + "/../dominio")

import file_handler

from config import *


class FileDao:

    def __init__(self):
        self.__file_handler = file_handler.File_Handler()

    def xml_head(self):
        head_list = ["<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n", "<!DOCTYPE Resultado SYSTEM \"Resultados.dtd\">\n", "<?xml-stylesheet type=\"text/xsl\" href=\"Resultados.xsl\"?>\n", "<Resultado>\n"]
        return head_list
        
    def xml_question (self, question_list):
        write_list = []
        write_list.append("\t<Pregunta>\n")
        for q in question_list:
            write_list.append("\t\t<Item>"+str(q)+"</Item>\n")
        write_list.append("\t</Pregunta>\n")
        return write_list

    def xml_results (self, documents):
        write_list = []
        for d in documents: 
            write_list.append("\t<Documento ID=\""+str(int(d[0]))+"\">\n")
            write_list.append("\t\t<Titulo>"+str(d[1][1])+"</Titulo>\n")
            write_list.append("\t\t<Relevancia>"+str(round(d[1][0]*100.0,2))+"%</Relevancia>\n")
            write_list.append("\t\t<Texto>"+REPOSITORY_PATH+str(int(d[0]))+".txt</Texto>\n")
            write_list.append("\t</Documento>\n")              
        write_list.append("<Resultado>")
        return write_list

    def create_xml_file (self, question_list, documents):
        head = self.xml_head()
        question = self.xml_question(question_list)
        results = self.xml_results(documents)
        xml_file=[]
        xml_file.extend(head)
        xml_file.extend(question)
        xml_file.extend(results)
        self.__file_handler.write_text_file(RESULT_PATH+"resultados.xml", xml_file)
