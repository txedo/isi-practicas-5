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

import psyco
import sys
sys.path.append('../dominio')
import analyzer

psyco.full()

from optparse import OptionParser

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
        analyzer.file_index(options.file_name)
    elif options.directory_name:
        analyzer.folder_index(options.directory_name)
