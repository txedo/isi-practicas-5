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

import MySQLdb
    
class Agente(object):


    __instance=None
    __conn=None


    # Constructor singleton
    def __new__(cls):
        try:
            # Si no hay ninguna instancia del agente, se inicializa la conexion de la base de datos
            if not cls.__instance:
                cls.__instance=object.__new__(cls)
            cls.__conn = MySQLdb.connect("localhost", "root", "ari", "ari")
            # Metodo para almacenar las transacciones en la BBDD de manera permanente
            cls.__conn.autocommit(1)
            return cls.__instance
        except MySQLdb.Error, e:
            raise e

    def close ( self ):
        try:
            self.__conn.close()
        except MySQLdb.Error, e:
            raise e


    # Metodos que ejecutan sentencias sql en la base de datos
    def query ( self, sql ):
        try:
            cursor=self.__conn.cursor()
            cursor.execute(sql)
            result=cursor.fetchall()
            cursor.close()
            return result
        except MySQLdb.Error, e:
            raise e


    def execute ( self, sql ):
        try:
            cursor=self.__conn.cursor()
            cursor.execute(sql)
            cursor.close()
        except MySQLdb.Error, e:
            raise e


