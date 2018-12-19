#!/usr/bin/env python
# -*- coding: utf-8 -*-
import xmlrpclib

alfresco = xmlrpclib.ServerProxy("http://003556:password@localhost:8080/alfresco/s/pb/exp/inf")

# action : 1=approve, 2=cancel, 3=paid

arg = {
	'action':'2',
	'exNo':'EX16000068',
	'by':'002648',
	'comment':'Cancel',
}
result = alfresco.use.action(arg);

print result 
