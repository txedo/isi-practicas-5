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


class NoFilesIndexed(exceptions.Exception):
		
    def __init__(self): 
        return
    def __str__(self): return "There is no file indexed in the database"


class TermNotFound(exceptions.Exception):
		
    def __init__(self): 
        return
    def __str__(self): return "There is no document that contains those terms"

