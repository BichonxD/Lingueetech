import bottle
import gensim

from bottle import run, route, request
from bottle import response
from json import dumps


model = gensim.models.Word2Vec()
model = model.load_word2vec_format('./vectors.bin', binary=True)

@route('/')
def index():
    """ Display welcome & instruction messages """
    return "<p>Welcome to my extra simple bottle.py powered server !</p> \
    	   <p>There are two ways to invoke the web service :\
	   <ul><li>http://localhost:8080/distance?w=cat&x=dog</li></ul>"

@route('/distance')
def distance():
	return "%s" % (model.similarity(request.query.w,request.params.x))
	
@route('/close')
def close():
	liste = model['lion']
	return dumps(liste)

@route('/similar')
def similar():
	return dumps(model.most_similar(positive=['koala', 'route'], negative=['truie']))

@route('/intrus')
def intrus():
	return dumps(model.doesnt_match("breakfast cereal dinner lunch".split()))
'cereal'




run(host='localhost', port=8080, reloader=True) 