#!/usr/bin/env python
# -*- coding: utf-8 -*-
import xmlrpclib

alfresco = xmlrpclib.ServerProxy("http://003556:password@localhost:8080/alfresco/s/pb/exp/inf")

arg = {
	'exNo':'EX16001011',
	'by':'002648',
	'task':'Supplier',
	'task_th':'ผู้ผลิต',
	'status':'Product Sent',
	'status_th':'ส่งสินค้า',
	'comment':'Sent Sent Sent',
}
result = alfresco.use.history(arg);

print result 
