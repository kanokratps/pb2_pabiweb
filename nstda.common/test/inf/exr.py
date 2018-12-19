#!/usr/bin/env python
# -*- coding: utf-8 -*-
import xmlrpclib

alfresco = xmlrpclib.ServerProxy("http://002799:password@10.226.202.134/alfresco/s/pb/exp/inf")

# action : 1=approve, 2=cancel, 3=paid

arg = {
	'action':'1',
	'exNo':'EX16000079',
	'by':'admin',
	'comment':'Cancel',
}
result = alfresco.use.action(arg);

print result 
