# -*- coding: utf-8 -*-

#    This file is part of pyDMS v1.0: Yet Another Document Management System
#    Copyright (C) 2009, Jose Domingo Lopez Lopez & Juan Andrada Romero
#
#    This program is free software: you can redistribute it and/or modify
#    it under the terms of the GNU General Public License as published by
#    the Free Software Foundation, either version 3 of the License, or
#    (at your option) any later version.
#
#    This program is distributed in the hope that it will be useful,
#    but WITHOUT ANY WARRANTY; without even the implied warranty of
#    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
#    GNU General Public License for more details.
#
#    You should have received a copy of the GNU General Public License
#    along with this program.  If not, see <http://www.gnu.org/licenses/>.

import psyco
psyco.full()

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
        
