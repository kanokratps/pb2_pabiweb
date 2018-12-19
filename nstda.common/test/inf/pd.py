#!/usr/bin/env python
# -*- coding: utf-8 -*-
import xmlrpclib
import base64

def b64str(fname):
	with open(fname) as f:
		encoded = base64.b64encode(f.read())
		return encoded


alfresco = xmlrpclib.ServerProxy("http://003556:password@localhost:8080/alfresco/s/pb/pcm/inf")

doc = b64str('PD.pdf')
att1 = b64str('PR_2015011901.pdf')
att2 = b64str('PR_2015011901.pdf')

# action : 1=create, 2=resubmit, 3=cancel

arg = {
	'action':'1',
	'pdNo':'PD18000062',
	'sectionId':'147',
	'prNo':'PR17000001,PR17000002',
	'docType':'PD2',
	'objective':u'Test Comment Interface ทดสอบ',
	'total':'2675000.00',
	'reqBy':'003556',
	'appBy':'002840',
	'doc':{'name':'PD18000062.pdf','content':doc},
	'attachments':[{'name':'A.pdf','content':att1,'desc':'desc1'},{'name':'B.pdf','content':att2,'desc':'desc2'}],
	'comment':'Request Request',
}
result = alfresco.ord.action(arg);

print result 
