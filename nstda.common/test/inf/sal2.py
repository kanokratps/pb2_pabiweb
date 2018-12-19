#!/usr/bin/env python
# -*- coding: utf-8 -*-
import xmlrpclib
import base64

def b64str(fname):
	with open(fname) as f:
		encoded = base64.b64encode(f.read())
		return encoded


alfresco = xmlrpclib.ServerProxy("http://admin:password@localhost:8080/alfresco/s/pb/hr/inf")

doc = b64str('PR_FORM.pdf')
att1 = b64str('PR_FORM.pdf')

# action : 1=create, 2=resubmit, 3=cancel

arg = {
	'action':'2',
	'salaryNo':'SL17000002',
	'sectionId':'475',
	'objective':u'Buy Something 2 piece ทดสอบ resubmit',
	'total':'15001.00',
	'reqBy':'003556',
	'comment':'Resubmit Document',
	'doc':{'name':'PD16000002.pdf','content':doc},
	'attachments':[{'name':'A.pdf','content':att1}],
	'comment':'Resubmit Resubmit'
}
result = alfresco.sal.action(arg);

print result 
