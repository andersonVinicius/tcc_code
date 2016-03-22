#!/usr/bin/python
import sqlite3


class DAOLampada:
  	_resultado = "";
	_kw = 0.0;
   	def _init_(self):
	 	print "Opened database successfully";
	
	def obter(self):
		conn = sqlite3.connect('/home/anderson/Documentos/tcc/RestFull/helloWord/energia2.sqlite3')
		
		cursor = conn.execute("Select kw from lampada WHERE id = (SELECT MAX(ID) FROM lampada)");
		for row in cursor:
                   return  str(row[0])   


                
		conn.close()

	def inserir(self,kw):
		conn = sqlite3.connect('/home/anderson/Documentos/tcc/RestFull/helloWord/energia2.sqlite3')

		conn.execute("INSERT INTO lampada (kw,data) \
     			 VALUES ("+str(float(kw))+", datetime('now'))");



		conn.commit()	
		print "Records created successfully";
		conn.close()

	def UpStatus(self,x):
		conn = sqlite3.connect('/home/anderson/Documentos/tcc/RestFull/helloWord/energia2.sqlite3')

		conn.execute(" UPDATE status SET acesso = "+str(x)+" WHERE id = 1");



		conn.commit()	
		print "Records created successfully";
		conn.close()	

	def ObterStatus(self):
		conn = sqlite3.connect('/home/anderson/Documentos/tcc/RestFull/helloWord/energia2.sqlite3')

		cursor = conn.execute("Select acesso from status  where id =1");
		for row in cursor:
                   return  row[0] 	

		print "Records created successfully";
		conn.close()
	def  autenticar(self):
		conn = sqlite3.connect('/home/anderson/Documentos/tcc/RestFull/helloWord/no_central.sqlite3')

		cursor = conn.execute("Select acesso from status  where id =1");
		for row in cursor:
                   return  row[0] 	

		print "Records created successfully";
		conn.close()

#c1 = DAOLampada()
#print c1.obter()
