import requests
import time
import json
import urllib

import string

import random

with open("httpGetData.txt") as f:
	data0 = f.read()

headers ={
	"content-type":"application/x-www-form-urlencoded", 
	"User-Agent":"Mozilla/5.0 (Linux; Android 7.1; Nexus 6p ) AppleWebKit/537.36 (KHTML, like Gecko) Version/8.0 Chrome/37.0.0.0 Mobile Safari/537.36 MicroMessenger/6.7.3.1341(0x26) NetType/WIFI Language/en Process/appbrand0",
	"charset":"utf-8",
	"Accept-Encoding":"gzip, deflate",
	"Connection":"close"
}

url = 'https://mp.weixin.qq.com/wxa-cgi/innersearch/subsearch'

def _request(data):
	global url, headers
	retry = 3
	while retry > 0:
		retry -= 1
		try:
			time.sleep(random.random()*2 + 2)
			resp = requests.post(url,data=data, headers=headers)
			break
		except Exception, e:
			print '[-]', e
			with open('errors.txt','w') as f:
				f.write('%s -- %s\n' % (e, data))

	with open('lastreq.txt','w') as f:
		f.write(resp.text.encode('utf8'))
	return resp


def _request_by_response(respjs, olddata, onitems=None):

	while respjs['respBody']['continueFlag']:

		o_pre, o_suc = olddata.split('offset_buf=')
		o_suc = o_suc.split('&',1)[1]
		mid = 'offset_buf=%s&' % urllib.quote_plus(respjs['respBody']['offsetBuf'])

		olddata = o_pre + mid + o_suc

		resp = _request(olddata)
		respjs = json.loads(resp.text)
		
		if onitems != None: onitems(respjs['respBody']['items'])
		
		if 'offsetBuf' in respjs['respBody']: print respjs['respBody']['offsetBuf']

def _request_by_word(word, onitems=None):

	word = urllib.quote_plus(word.encode('utf8'))

	tdata = data0
	tdata = tdata.replace('%%QUERY%%', word)

	resp = _request(tdata)
	respjs = json.loads(resp.text)

	if 'respBody' not in respjs:
		with open('norespBody.txt','w') as f:
			f.write('%s  --  %s\n' % (word, tdata))
		return 

	if onitems != None: onitems(respjs['respBody']['items'])
	

	_request_by_response(respjs, tdata, onitems)
