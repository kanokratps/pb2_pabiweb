#!/usr/bin/env python
# -*- coding: utf-8 -*-
import xmlrpclib
import base64

def b64str(fname):
	with open(fname) as f:
		encoded = base64.b64encode(f.read())
		return encoded

alfresco = xmlrpclib.ServerProxy("http://002648:password@localhost:8080/alfresco/s/pb/exp/inf")

att1 = b64str('PR_2015011901.pdf')
att2 = b64str('PR_2015011901.pdf')

# action : 1=create

arg = {
	'action':'1',
	'reqBy':'003556',
	'objective':u'Test Comment Interface ทดสอบ',
	'reason':u'Test Reason ทดสอบ',
	'note':u'Test Note ทดสอบ',
	'budgetSrcType':u'U',
	'budgetSrc':u'41',
	'fundId':u'1',
	'costControlTypeId':u'1',
	'costControlId':u'1',
	'costControlFrom':u'2017-01-01',
	'costControlTo':u'2017-02-02',
	'bankType':u'0',
	'bank':u'',
	'isReason':u'0',
	'isSmallAmount':u'0',
	'payType':u'0',
	'payDtl1':u'',
	'payDtl2':u'',
	'payDtl3':u'',
	'total':'1000.00',
	'attachments':[{'name':'A.pdf','content':att1},{'name':'B.pdf','content':att2}],
	'items':[{'activity':'activity','actId':634,'actGrpId':51,'condition1':'','condition2':'','position':'pos','uom':'unit','amount':1000.00}],
	'attendees':[{'type':'E','code':'000367','title':'Miss','fname':'First','lname':'Last','utype':'[105015] งานบริหารระบบสารสนเทศเพื่อการจัดการ','position':'วิศวกรอาวุโส','positionId':'234'}]
}
result = alfresco.use.create(arg);

print result 
