#!/usr/bin/env python
# -*- coding: utf-8 -*-
import xmlrpclib
import base64

def b64str(fname):
	with open(fname) as f:
		encoded = base64.b64encode(f.read())
		return encoded


alfresco = xmlrpclib.ServerProxy("http://003556:password@localhost:8080/alfresco/s/pb/hr/inf")

doc = b64str('SAL_FORM.pdf')
att1 = b64str('PR_2015011901.pdf')
att2 = b64str('PR_2015011901.pdf')

# action : 1=create, 2=resubmit, 3=cancel

arg = {
	'action':'1',
	'salaryNo':'SL18000030',
	'sectionId':'147',
	'objective':u'Test Comment Interface ทดสอบ',
	'total':'102675.00',
	'reqBy':'002648',
	'doc':{'name':'PD18000030.pdf','content':doc},
	'attachments':[{'name':'A.pdf','content':att1},{'name':'B.pdf','content':att2}],
	'comment':'Request Request',
}
result = alfresco.sal.action(arg);

print result 
