#!/usr/bin/env python
# -*- coding: utf-8 -*-

# Index Engine 1.0
# Date: 2009/10/21
# Authors: josed.lopez1@alu.uclm.es & juan.andrada@alu.uclm.es
# License: As-is; public domain
# Prerequisites: Python 2.5.1
# Type "index-engine-cmd.py -h" for help

# Keywords:
# index engine

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

import analyzer
import MySQLdb
import datetime
import threading, time

from optparse import OptionParser
from exception import *

# Parse arguments
parser=OptionParser(usage="%prog (-f <file_name> | -d <directory_name>)", version="%prog 1.0",
		    description="Index Engine")
parser.set_defaults(sparse_output=False,binarize=False)
parser.add_option("-f","--file",action="store",type="string",dest="file_name",
                  help="name of input file to index")
parser.add_option("-d","--directory",action="store",type="string",dest="directory_name",
		  help="name of the input directory to walk and to index")
(options,args)=parser.parse_args()


if len(sys.argv) < 2:
    print "Usage: "+sys.argv[0]+" -h (prints help message)"
else:
    analyzer = analyzer.Analyzer()
    
    if options.file_name:
        try:
            d1 = datetime.datetime.now()
            t = threading.Thread(target=analyzer.file_index, args=(options.file_name,))
            t.start()
            time.sleep(0.2)
            label = str(analyzer)
            print label
            while analyzer.working:
                new_label = str(analyzer)
                if label <> new_label:
                    print new_label
                time.sleep(0.2)
            d2 = datetime.datetime.now()
            print "Index finished in ", d2-d1
        except MySQLdb.Error, e:
            print "SQL Exception: "+e.args[1]
        except FileException, e:
            print str(e)
        except Exception, e:
            print "Exception: "+str(e)
    elif options.directory_name:
        try:
            ndoc=1
            d1 = datetime.datetime.now()
            t = threading.Thread(target=analyzer.folder_index, args=(options.directory_name,))
            t.start()
            time.sleep(0.2)
            label = str(analyzer)
            print label
            while analyzer.working:
                new_label = str(analyzer)
                # Al cambiar de documento, se actualiza el mensaje que se muestra por pantalla
                if label <> new_label:
                    d3 = datetime.datetime.now()
                    if ndoc==1:
                        print "\t** Time spent:", (d3-d1), "**"
                        d4 = datetime.datetime.now()
                    else: 
                        print "\t** Time spent:", (d3-d4), "**"
                        d4 = datetime.datetime.now()
                    label=new_label
                    print label
                    ndoc+=1
                time.sleep(0.2)
            d2 = datetime.datetime.now()
            print "\t** Time spent:", (d2-d3), "**"
            print "Index finished in ", d2-d1
        except MySQLdb.Error, e:
            print "SQL Exception: "+e.args[1]
        except FolderException, e:
            print str(e)
        except Exception, e:
            print "Exception: "+str(e)

