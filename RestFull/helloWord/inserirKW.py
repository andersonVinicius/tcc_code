#!/usr/bin/python

import sqlite3
from random import random
from DAOLampada import  DAOLampada

#conn = sqlite3.connect("/home/anderson/Área de Trabalhocapesfull1.sqlite")
#c=conn.cursor()

for x in  range(1,10790):
	 c1 = DAOLampada()
	 c1 = inserir(random.randint(5,90))
         print "inserindo .."
         time.sleep(4)


#conn.commit()  
#conn.close() 
#(1, 1000)
