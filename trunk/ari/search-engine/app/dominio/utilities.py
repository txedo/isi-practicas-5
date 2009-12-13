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
        except:
            raise
        return docs
