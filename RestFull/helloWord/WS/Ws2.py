
from DAOLampada import  DAOLampada
#install Flask
from flask import make_response,Flask ,jsonify,request
import time
import sqlite3
import random
import thread
import time
from xbee import XBee,ZigBee
import serial
import struct

global x

app = Flask(__name__)
tasks = [
    {
        'MAC': 1,
        'name': u'tv',
        'img': u'tv',
        'priority': u'LOW', 
        
    },
    {
        'MAC': 2,
        'name': u'computer',
        'img': u'computer',
        'priority': u'HIGH', 
        
    },
    {
        'MAC': 3,
        'name': u'lavadora',
        'img': u'lavadora',
        'priority': u'HIGH', 
        
    },
    {
        'MAC': 4,
        'name': u'lavadora',
        'img': u'lavadora',
        'priority': u'HIGH', 
        
    },
    {
        'MAC': 5,
        'name': u'tv',
        'img': u'tv',
        'priority': u'HIGH', 
        
    },
    {
        'MAC': 5,
        'name': u'tv',
        'img': u'tv',
        'priority': u'HIGH', 
        
    }
]


#ZIGBEE_Sender
PORT = '/dev/ttyUSB0'
BAUD_RATE = 9600

# Open serial port
ser = serial.Serial(PORT, BAUD_RATE)

# Create API object
xbee = ZigBee(ser)
import pprint
pprint.pprint(xbee.api_commands)
pprint.pprint(xbee.api_responses)

dest_addr_long ='\x00\x13\xa2\x00\x40\xae\xc5\xbe'
addr = '\x51\x65'

# ser.close()

#mostrar erro
@app.errorhandler(404)
def not_found(error):
    return make_response(jsonify({'error': 'Not found'}), 404)
#consulta KW
@app.route("/")
def randon():

        return str(random.uniform(124, 127))

#Inserir_equipamento
@app.route('/insert', methods=['GET', 'POST'])
def InsertEquipment():
    mac = request.args.get('mac')
    priority = request.args.get('priority')
    id_appliance = request.args.get('id_appliance')
    c1 = DAOLampada()
    c1.AddEquipment(mac,priority,id_appliance)
      
    #     c1.inserir(random.uniform(58.8, 70.9))
    #     print "inserindo ..";
    #     return str(c1.obter())
    # else:
#Edit_equipamento
@app.route('/edit', methods=['GET', 'POST'])
def editEquipment():
    mac = request.args.get('mac')
    priority = request.args.get('priority')
    id_appliance = request.args.get('id_appliance')
    c1 = DAOLampada()
    c1.EditEquipment(mac,priority,id_appliance)
      
    #     c1.inserir(random.uniform(58.8, 70.9))
    #     print "inserindo ..";
    #     return str(c1.obter())
    # else:


    return "cadastrado com sucesso!"
@app.route('/remove', methods=['GET', 'POST'])
def RemoveEquipment():
    mac = request.args.get('mac')
    mac =  mac.replace("'","")
    
    mac2=mac.split("-")
    c1 = DAOLampada()
    for x in range(1,len(mac2)):
    #mac=mac)
        
        print "aqui no for:"+str(mac2[x])
        c1.RemoveEquipment(mac2[x])
      
    #     c1.inserir(random.uniform(58.8, 70.9))
    #     print "inserindo ..";
    #     return str(c1.obter())
    # else:
    print "<><><><><><><><><><><><>"
    print "Aqui no mac: "+ str(mac2)
    return "Removido com sucesso!"
     

#obter_equipamento
@app.route('/equip/<int:task_id>', methods=['GET'])
def getEquipment(task_id):
    c1 = DAOLampada()
    if c1.ObterStatus()==0:
      
        c1.inserir(random.uniform(58.8, 70.9))
        print "inserindo ..";
        return str(c1.obter())
    else:

        return "0.0"

#obter_todos_equipamentos
@app.route('/equips/')
def getEquipaments():
     lista = ["EQ1","EQ2","EQ3"] 
     c1 = DAOLampada()
     
      
    #     c1.inserir(random.uniform(58.8, 70.9))
    #     print "inserindo ..";
    #     return str(c1.obter())
    # else:

     return c1.ConsultEquipment()

@app.route('/macs/')
def getMacs():
     # array=[]
     # lista = ["EQ1","EQ2","EQ3"] 
     c1 = DAOLampada()
     array=c1.ObterMacs()
     for i in range(0,len(array)):
         print array[i]
         c1.ConsultEquipTC(array[i])
    #     c1.inserir(random.uniform(58.8, 70.9))
    #     print "inserindo ..";
    #     return str(c1.obter())
    # else:
     
     

     return str(c1.ObterMacs())


#Obter_RealTime Volatagem
@app.route('/voltage', methods=['GET', 'POST'])
def getVoltage():
    mac = request.args.get('mac')  
       # lista = ["EQ1","EQ2","EQ3"] 
    c1=DAOLampada()
 
    # if c1.ObterStatus()==0:
      
    #     c1.inserir(random.uniform(58.8, 70.9))
    #     print "inserindo ..";
    #return str(c1.obter())
    # else: 
    #'Tvufra'

    return c1.obterTimeR(mac)

@app.route('/consult', methods=['GET', 'POST'])
def getConsultEquipmentToMac():
    mac = request.args.get('mac')
    print "getConsultEquipmentToMac    |   "+mac  
    c1=DAOLampada()
    result=c1.ConsultEquipmentToMac(mac)
    return result

@app.route('/appliances/')
def getAppliances():
    c1=DAOLampada()
    result=c1.getApplianceType()

    return result
        
#verificar status ligado e desligado
#@app.route('/sts/<int:task_id>', methods=['GET'])

# Enviar status dos equipamentos(off/on)
@app.route('/status', methods=['GET', 'POST'])
def status():
    status = request.args.get('status')
    mac = request.args.get('mac')
    print status+" | "+mac
    c1 = DAOLampada()
    c1.UpStatus(status,mac)
    if str(status)=="'on'":
      #c1.UpStatus(status,mac)
       print "send ligado"
       xbee.tx(dest_addr_long="\x00\x13\xa2\x00\x40\xae\xc5\xbe", dest_addr="\xff\xff", data ='l')
    else:
       print "send Desligado"
       xbee.tx(dest_addr_long="\x00\x13\xa2\x00\x40\xae\xc5\xbe", dest_addr="\xff\xff", data ='d')
    return "Ok Update aconteceu "   

@app.route('/status/<int:task_id>', methods=['GET'])
def status2(task_id):
    # xbee = ZigBee(ser)
    # status = request.args.get('status')
    # mac = request.args.get('mac')
    # print status+" | "+mac
    # c1 = DAOLampada()
    # c1.UpStatus(status,mac)
    if task_id==0:
      #c1.UpStatus(status,mac)
       print "send ligado"
       xbee.tx(dest_addr_long="\x00\x13\xa2\x00\x40\xae\xc5\xbe", dest_addr="\xff\xff", data ='l')
    else:
       print "send Desligado"
       xbee.tx(dest_addr_long="\x00\x13\xa2\x00\x40\xae\xc5\xbe", dest_addr="\xff\xff", data ='d')
    ser.close() 
    return "Ok Update aconteceu "   

#autenticar user
@app.route('/login', methods=['GET', 'POST'])
def login():
    username = request.args.get('username')
    print(username)
    password = request.args.get('password')
    print(password) 
    c1 = DAOLampada()
    

    if str(c1.autenticarUser(username,password))=="t": 
       return "T"
    else:
       return "F"

#------------------------------------------
#========Calcular_Potencia_KwH=============
#------------------------------------------       
def convertKw(self,tension, current):
    
    tension=tensio*current 
    result=(tension/3600)/1000
    print result +" KWH"
    return result

#------------------------------------------
#========Rotina_Server=====================
#------------------------------------------


def  requisicaoTime(threadName, delay):
   count = 0
   while count < 5:
      time.sleep(delay)
      count += 1
      print "%s: %s" % ( threadName, time.ctime(time.time()) ) 

#verificacao periodica de inicio de execucao de um determinado equipamento
def  agenda_inicio(threadName, delay):
   
   c1 = DAOLampada()
   c1.obterAgendaInicio()
   count = 0
   while count < 5:
      time.sleep(delay)
      count += 1
      print "%s: %s" % ( threadName, time.ctime(time.time()))

#verificacao periodica de fim de execucao de um determinado equipamento        
def  agenda_fim(threadName, delay):
   count = 0
   while count < 5:
      time.sleep(delay)
      count += 1
      print "%s: %s" % ( threadName, time.ctime(time.time()) )         

# #Start_rotina_server
# try:
#    thread.start_new_thread( agenda_inicio, ("Inicio", 10, ) )
#    thread.start_new_thread( agenda_fim, ("Fim", 10, ) )
  
# except:
#    print "Error: unable to start thread"


#start_WS
if __name__ == "__main__":
   app.debug = True	
   app.run(host='10.11.86.250', port=8085)


   
