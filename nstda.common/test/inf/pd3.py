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
	'action':'3',
	'pdNo':'PD16000021',
	'comment':'Invalid Document',
	'reqBy':'002648'
}
result = alfresco.ord.action(arg);

print result 
