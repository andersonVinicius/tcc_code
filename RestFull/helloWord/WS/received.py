#! /usr/bin/python

#este codigo e responsavel 
#por receber todos os dados
#referentes as tomadas 

from DAOLampada import  DAOLampada
from xbee import XBee,ZigBee
import serial
import struct  

#PORT = '/dev/ttyAMA0'
PORT ='/dev/ttyUSB0'
BAUD_RATE = 9600

# def hex(bindata):
#     return ''.join('%02x' % ord(byte) for byte in bindata)

def decodeReceivedFrame(data):
            source_addr_long = data['source_addr_long']
            source_addr = data['source_addr']
            id = data['id']
            samples = data['rf_data']
          #  options = toHex(data['options'])
            return [source_addr_long, source_addr]

def regularTensao110(tension):

          if tension>=394:
            return 150 
          elif tension>=354:
            return 140
          elif tension>=337:
            return  135
          elif tension>=316:
            return  130
          elif tension>=302:
            return  127
          elif tension>=299:
            return  125
          elif tension>=289:
            return  123
          elif tension>=281:
            return  120
          elif tension>=265:
            return  117
          elif tension>=261:
            return  115
          elif tension>=251:
            return  113
          elif tension>=244:
            return  110
          elif tension>=230:
            return  107
          elif tension>=228:
            return  105
          elif tension>=215:
            return  103
          elif tension>=211:
            return  100
          elif tension>=180:
            return  90
          elif tension>=149:
            return  80
          elif tension>=118:
            return 70
          elif tension>=91:
            return  60
          elif tension>=68:
            return  50
          elif tension>=46:
            return  40
          elif tension>=29:
            return  30
          elif tension>=15:
            return  20                 
          else :
            return 10


# Open serial port
ser = serial.Serial(PORT, BAUD_RATE)

# Create API object
xbee = ZigBee(ser)
c1 = DAOLampada()
xbee.tx(dest_addr_long="\x00\x13\xa2\x00\x40\xae\xc5\xbe", dest_addr="\xff\xff", data ='l')
# Continuously read and print packets
while True:
    try:
    	print"..."
        #xbee.at(frame_id='A', command='MY')
        
        response = xbee.wait_read_frame()
        if response['id'] != 'tx_status':
            
        #decodedData = decodeReceivedFrame(response)
        #print decodedData
     	#print (xbee.receive())
          data=response['rf_data'].split("#")
          adrss=decodeReceivedFrame(response)
          print data
          #c1.inserir(data[0].replace("\x00",""),data[1].replace("\x00",""))
        #res = 
          # if int(str(data[1].replace("\x00","")))>=250 & int(str(data[1].replace("\x00","")))<=400:
          #    print "110v"  
          #    c1.inserir(data[0].replace("\x00",""),110)
          tension=int(str(data[1].replace("\x00","")))
          tension=regularTensao110(tension)
          print str(adrss)
          print str(tension)+"V"
          #c1.inserir(data[0].replace("\x00",""),tension)
         # print "current : " + data[0]
         # print repr(data[0].replace("\x00",""))
         # print"\n"
         # print "Tension : " + data[1]
         # print repr(data[1].replace("\x00",""))
         #print x1[1]
         #print(struct.unpack('>h', response['rf_data'])[0])
        
    except KeyboardInterrupt:
        break

ser.close() 
