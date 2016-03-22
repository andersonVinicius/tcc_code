
from DAOLampada import  DAOLampada

from flask import make_response,Flask ,jsonify,request
import time
import sqlite3
import random
import thread
import time

from xbee import XBee,ZigBee
import serial


global x

PORT = '/dev/ttyUSB0'
BAUD_RATE = 9600

# Open serial port
ser = serial.Serial(PORT, BAUD_RATE)
# Create API object
xbee = ZigBee(ser)


DEST_ADDR_LONG ='\x00\x13\xa2\x00\x40\xae\xc5\xbe'
addr = '\xff\xff'

# Continuously read and print packets
# print "send data"
# xbee.tx(dest_addr_long=DEST_ADDR_LONG, dest_addr=addr, data = 'd')

app = Flask(__name__)

@app.errorhandler(404)
def not_found(error):
    return make_response(jsonify({'error': 'Not found'}), 404)
###############################
@app.route("/")
def getKw():
    c1 = DAOLampada()
    if c1.ObterStatus() ==0:
      
        c1.inserir(random.uniform(58.8, 70.9))
        print "inserindo ..";
        return str(c1.obter())
    else:

        return "0.0"

@app.route('/sts/<int:task_id>', methods=['GET'])
def status(task_id):
    PORT = '/dev/ttyUSB0'
    BAUD_RATE = 9600

    # Open serial port
    ser = serial.Serial(PORT, BAUD_RATE)
    c1 = DAOLampada()
    if task_id==1:
        c1.UpStatus(1)
        print "send data"
        xbee.tx(dest_addr_long=DEST_ADDR_LONG, dest_addr=addr, data = 'l')
        # try:
        #    thread.start_new_thread(consumirZigbee())
  
        # except:
        #       print "Error: unable to start thread"
        
        return "ligou"
        ser.close()    
    else:
        c1.UpStatus(0) 
        print "send data"
        xbee.tx(dest_addr_long=DEST_ADDR_LONG, dest_addr=addr, data = 'd')

        return "Ok Update aconteceu desligou " 
        ser.close()

def  consumirZigbee():
   PORT = '/dev/ttyUSB0'
   BAUD_RATE = 9600

   # Open serial port
   ser = serial.Serial(PORT, BAUD_RATE)
   c1 = DAOLampada()
   while c1.ObterStatus()==1:
    try:
        print"..."
        #xbee.at(frame_id='A', command='MY')
        response = xbee.wait_read_frame()
        #decodedData = decodeReceivedFrame(response)
        #print decodedData
        #print (xbee.receive())
        #res = 
        print str(response)
        #print(struct.unpack('>h', response['rf_data'])[0])
    except KeyboardInterrupt:
        break
    ser.close()    
     
          
###########################################################


#responsavel pela requisicao periodica  de envio de dados para concessionaria
def  requisicaoTime(threadName, delay):
   count = 0
   while count < 5:
      time.sleep(delay)
      count += 1
      print "%s: %s" % ( threadName, time.ctime(time.time()) )   


try:
   thread.start_new_thread( requisicaoTime, ("Thread-1", 60, ) )
  
except:
   print "Error: unable to start thread"



if __name__ == "__main__":
   app.debug = True	
   app.run( port=8085)
   ser.close() 
   
