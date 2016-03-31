#!/usr/bin/python
import sqlite3
import json
from flask import Flask, g, jsonify, make_response, render_template


class DAOLampada:

    #_resultado = "";
    #_kw = 0.0;
 
    
 def _init_(self):
        print "Opened database successfully";

#------------------------------------------
#========Medicao&Consumo===================
#------------------------------------------        
 
 def obterTimeR(self,mac_node):
  print mac_node
  conn=sqlite3.connect('/home/anderson/Documentos/tcc/RestFull/helloWord/no_central.sqlite3')
  cursor=conn.execute("Select * from  Data_consumer where Mac_node like'%"+mac_node+"%' ORDER BY id_data_consumer DESC");
  data=[]
 # res=cursor.fetchall()
  #conn.close()
  for res in cursor:
  #     consumer={
          # "tension": res[2],
          # "current": res[3],
          # "time_consumer": res[1],
      #     "mac_node": res[4],
      #     "id_data_consumer":res[0]
      # } 
      # data.append(consumer)
      return str(res[2]) 
      break 
  
  # return jsonify( {'data': data } )  
 
 def getApplianceType(self):
  conn=sqlite3.connect('/home/anderson/Documentos/tcc/RestFull/helloWord/no_central.sqlite3')
  cursor=conn.execute("Select * from Appliance_type ");
  data=[]
 # res=cursor.fetchall()
  #conn.close()
  for res in cursor:
      appliances={
          "id_appliance_type": res[0],
          "description": res[1]
          
      } 
      data.append(appliances)
  
  return jsonify( {'data': data } )  
  
 
 def inserir(self,current,tension,mac_node):
  conn=sqlite3.connect('/home/anderson/Documentos/tcc/RestFull/helloWord/no_central.sqlite3')
 # conn.execute("INSERT INTO lampada (kw,data) VALUES ("+str(float(kw))+", datetime('now'))");
  conn.execute("INSERT INTO Data_consumer(Mac_node,time_consumer,tension,current) VALUES ( "+str(mac_node)+", datetime('now'),"+str(tension)+","+str(float(current))+")");
  conn.commit() 
  print "Records created successfully";
  # conn.close()


#------------------------------------------
#========Autenticacao=====================
#------------------------------------------
  
 def autenticarUser(self,login,password):
  conn=sqlite3.connect('/home/anderson/Documentos/tcc/RestFull/helloWord/no_central.sqlite3')
  cursor=conn.execute("Select * from consumer where password='"+str(password)+"' and login= '"+str(login)+"'");
  cont = 0;
  for row in cursor:
      row[0]
      cont=cont+1  
      print cont 
  if cont>=1: 
        return "t"
  else: 
        return "f"
  print "Records created successfully";

 def UpStatus(self,status,mac):
    conn=sqlite3.connect('/home/anderson/Documentos/tcc/RestFull/helloWord/no_central.sqlite3')
    #conn.execute(" UPDATE status SET acesso = "+str(x)+" WHERE id = 1");
    conn.execute("UPDATE node_appliance SET  status= "+str(status)+" WHERE  MAC_node="+str(mac));
    conn.commit() 
    print "UP status successfully";
    # conn.close()

  # conn.close()  
 def ObterStatus(self,mac):
  conn=sqlite3.connect('/home/anderson/Documentos/tcc/RestFull/helloWord/no_central.sqlite3')
  cursor=conn.execute("Select node_appliance from status  where MAC_node ="+str(mac));
  for row in cursor:
      return  row[0]  
  print "Records created successfully";
  # conn.close()
#  def obterAgendaInicio(self,mac):
#   conn=sqlite3.connect('/home/anderson/Documentos/tcc/RestFull/helloWord/no_central.sqlite3')
#   cursor=conn.execute("Select inicio, MAC_node from Schedule where MAC_node ="+str(mac));
#   for row in cursor:
#       return  row[0]  
#   print "Records created successfully";
#   conn.close()

# def obterAgendaFim(self,mac):
#   conn=sqlite3.connect('/home/anderson/Documentos/tcc/RestFull/helloWord/no_central.sqlite3')
#   cursor=conn.execute("Select fim from Schedule  where MAC_node ="+str(mac));
#   for row in cursor:
#       return  row[0]  
#   print "Records created successfully";
#   conn.close()



#------------------------------------------
#========Crud_Equipment=====================
#------------------------------------------

 def AddEquipment(self,mac,priority,id_appliance):
  conn=sqlite3.connect('/home/anderson/Documentos/tcc/RestFull/helloWord/no_central.sqlite3')
  conn.execute("INSERT INTO node_appliance(Mac_node,status,priority,id_appliance_type) VALUES ("+str(mac)+", '0ff',"+str(priority)+","+id_appliance+");");
  conn.commit() 
  print "Inserido no banco de dados!";
  # conn.close()

 def RemoveEquipment(self,mac):
  conn=sqlite3.connect('/home/anderson/Documentos/tcc/RestFull/helloWord/no_central.sqlite3')
  conn.execute("DELETE FROM node_appliance WHERE mac_node='"+str(mac)+"'");
  conn.commit() 
  print "Removido do Banco de Dados :"+mac;
  # conn.close()

 def EditEquipment(self,mac,priority,id_appliance):
  print mac
  print priority 
  print id_appliance
  conn=sqlite3.connect('/home/anderson/Documentos/tcc/RestFull/helloWord/no_central.sqlite3')
  cursor=conn.execute("update node_appliance set  priority ="+str(priority)+", id_appliance_type="+id_appliance+" where mac_node="+str(mac));
  conn.commit() 
  # for row in cursor:
  #     return  row[0]  
  print "Edit successfully!";
  conn.close()
  #conn.close()

 def ConsultEquipment(self):
  conn=sqlite3.connect('/home/anderson/Documentos/tcc/RestFull/helloWord/no_central.sqlite3')
  cursor=conn.execute("SELECT Mac_node, description, priority,status,icon FROM node_appliance as a, Appliance_type as b where  a.id_appliance_type=b.id_appliance_type");
  data=[]
 # res=cursor.fetchall()
  # conn.close()
  for res in cursor:
      equipments={
          "Mac_node": res[0],
          "description": res[1],
          "priority":res[2],
          "status":res[3],
          "icon":res[4]
          
      } 
      data.append(equipments)
  
  return jsonify( {'data': data } )  
    
 def ConsultEquipmentToMac(self,mac_node):
  print "Aqui No DAO   |  "+mac_node
  conn=sqlite3.connect('/home/anderson/Documentos/tcc/RestFull/helloWord/no_central.sqlite3')
  cursor=conn.execute("Select Mac_node, description, priority,status,icon FROM node_appliance as a, Appliance_type as b where a.id_appliance_type=b.id_appliance_type and Mac_node='"+str(mac_node)+"'");
 # cursor=conn.execute("Select * from  Data_consumer where Mac_node like'%"+mac_node+"%' ORDER BY id_data_consumer ");
  data=[]
 # res=cursor.fetchall()
  #conn.close()
  for res in cursor:
      equipments={
          "Mac_node": res[0],
          "description": res[1],
          "priority":res[2],
          "status":res[3],
          "icon":res[4]
          
      } 
      data.append(equipments)
  
  return jsonify( {'data': data } )  
 
 def ConsultDescriptionToMac(self,mac_node):
  print "Aqui No DAO   |  "+mac_node
  conn=sqlite3.connect('/home/anderson/Documentos/tcc/RestFull/helloWord/no_central.sqlite3')
  cursor=conn.execute("Select description FROM node_appliance as a, Appliance_type as b where a.id_appliance_type=b.id_appliance_type and Mac_node='"+str(mac_node)+"'");
 # cursor=conn.execute("Select * from  Data_consumer where Mac_node like'%"+mac_node+"%' ORDER BY id_data_consumer ");

 # res=cursor.fetchall()
  #conn.close()
  for res in cursor:
      
          return res[0]
         

   

 def ObterMacs(self):
  conn=sqlite3.connect('/home/anderson/Documentos/tcc/RestFull/helloWord/no_central.sqlite3')
  cursor=conn.execute("select distinct(mac_node) from data_consumer");
  macs=[]
  for row in cursor:
       macs.append(str(row[0]))  
  
  # print "Records created successfully"
  return macs

 def ConsultEquipTC(self,mac):
  c1=DAOLampada()
  conn=sqlite3.connect('/home/anderson/Documentos/tcc/RestFull/helloWord/no_central.sqlite3')
  cursor=conn.execute("select count(), sum(current), sum(tension),mac_node from data_consumer  where mac_node ='"+str(mac)+"'");
  data=c1.ConsultDescriptionToMac(mac)
 # res=cursor.fetchall()
  # conn.close()
  for res in cursor:
      equipments={
          "quant": res[0],
          "current":res[1],
          "tension":res[2],
          "mac_node":res[3],
          "description": data
          
      } 
      # //data.append(equipments)
  
  return equipments 

#c1 = DAOLampada()
#print c1.obter()
