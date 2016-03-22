
from DAOLampada import  DAOLampada

from flask import make_response,Flask ,jsonify,request
import time
import sqlite3
import random
import thread

global x

app = Flask(__name__)

tasks = [
    {
        'id': 1,
        'title': u'Buy groceries',
        'description': u'Milk, Cheese, Pizza, Fruit, Tylenol', 
        'done': False
    },
    {
        'id': 2,
        'title': u'Learn Python',
        'description': u'Need to find a good Python tutorial on the web', 
        'done': False
    }
]
@app.errorhandler(404)
def not_found(error):
    return make_response(jsonify({'error': 'Not found'}), 404)
###############################
@app.route("/")
def hello():
    c1 = DAOLampada()
    if c1.ObterStatus() ==0:
      
        c1.inserir(random.uniform(58.8, 70.9))
        print "inserindo ..";
        return str(c1.obter())
    else:

        return "0.0"

@app.route('/sts/<int:task_id>', methods=['GET'])
def status(task_id):
    c1 = DAOLampada()
    if task_id==1:
        c1.UpStatus(1)
    else:
        c1.UpStatus(0) 
    return "Ok Update aconteceu  "   
          
###########################################################
@app.route("/brasil")
def hello2():
  
 while True:
	
	 c2 = DAOLampada()
	 
         time.sleep(1)


@app.route('/teste1', methods=['GET'])

def get_tasks():
    return jsonify({'tasks': tasks})



@app.route('/teste2/<int:task_id>', methods=['GET'])
def get_task(task_id):
    task = [task for task in tasks if task['id'] == task_id]
    if len(task) == 0:
        abort(404)
    return jsonify({'task': task[0]})



@app.route('/teste3', methods=['POST'])
def create_task():
    if not request.json or not 'title' in request.json:
        abort(400)
    task = {
        'id': tasks[-1]['id'] + 1,
        'title': request.json['title'],
        'description': request.json.get('description', ""),
        'done': False
    }
    tasks.append(task)
    return jsonify({'task': task}), 201

@app.route('/teste4/<int:task_id>', methods=['PUT'])
def update_task(task_id):
    task = [task for task in tasks if task['id'] == task_id]
    if len(task) == 0:
        abort(404)
    if not request.json:
        abort(400)
    if 'title' in request.json and type(request.json['title']) != unicode:
        abort(400)
    if 'description' in request.json and type(request.json     ['description']) is not unicode:
        abort(400)
    if 'done' in request.json and type(request.json['done']) is not bool:
        abort(400)
    task[0]['title'] = request.json.get('title', task[0]['title'])
    task[0]['description'] = request.json.get('description', task[0]['description'])
    task[0]['done'] = request.json.get('done', task[0]['done'])
    return jsonify({'task': task[0]})


@app.route('/teste5/<int:task_id>', methods=['DELETE'])
def delete_task(task_id):
    task = [task for task in tasks if task['id'] == task_id]
    if len(task) == 0:
        abort(404)
    tasks.remove(task[0])
    return jsonify({'result': True})

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
   app.run(host='10.10.165.31', port=8085)
   


