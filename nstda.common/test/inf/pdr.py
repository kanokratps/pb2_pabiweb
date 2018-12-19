#!/usr/bin/env python
# -*- coding: utf-8 -*-

import xmlrpclib
import base64

def b64str(fname):
	with open(fname) as f:
		encoded = base64.b64encode(f.read())
		return encoded


alfresco = xmlrpclib.ServerProxy("http://002648:password@10.226.202.134/alfresco/s/pb/pcm/inf")

doc = b64str('PR_2015011901.pdf')
att1 = b64str('PR_2015011901.pdf')
att2 = b64str('PR_2015011901.pdf')

arg = {
	'action':'1',
	'pdNo':'PD16000179',
	'sectionId':'67',
	'prNo':'PR16000001,PR16000002',
	'docType':'PD1',
	'objective':'Buy Something 1 piece ทดสอบ',
	'total':'100000.00',
	'reqBy':'003556',
	'appBy':'001587',
	'doc':{'name':'PD16000002.pdf','content':doc},
	'attachments':[{'name':'A.pdf','content':att1,'url':'aaaaa'},{'name':'B.pdf','content':att2}]
}
result = alfresco.ord.action(arg);

print result 
