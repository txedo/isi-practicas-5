# -*- coding: utf-8 -*-

#    This file is part of pyDMS v1.0: yet another document management system
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

import os

# Ruta de la base documental del sistem
REPOSITORY_PATH = os.getcwd() + "/../persistencia/repository/"
# Tamaño maximo de la cache
MAX_CACHE_SIZE = 4000
# Ruta donde se almacena el fichero "Resultados.xml"
RESULT_PATH = os.getcwd() + "/../persistencia/resultados/"
