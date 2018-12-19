#!/usr/bin/env python
# -*- coding: utf-8 -*-
import xmlrpclib
import base64

def b64str(fname):
	with open(fname) as f:
		encoded = base64.b64encode(f.read())
		return encoded


alfresco = xmlrpclib.ServerProxy("http://admin:password@localhost:8080/alfresco/s/pb/pcm/inf")

doc = b64str('PR_FORM.pdf')
att1 = b64str('PR_FORM.pdf')

# action : 1=create, 2=resubmit, 3=cancel

arg = {
	'action':'2',
	'pdNo':'PD18000062',
	'sectionId':'147',
	'prNo':'PR16000001',
	'docType':'PD2',
	'objective':u'Buy Something 2 piece ทดสอบ resubmit',
	'total':'15001.00',
	'reqBy':'003556',
	'appBy':'002840',
	'comment':'Resubmit Document',
	'doc':{'name':'PD18000062.pdf','content':doc},
	'attachments':[{'name':'A.pdf','content':att1,'desc':'desc1'}],
	'comment':'Resubmit Resubmit'
}
result = alfresco.ord.action(arg);

print result 
