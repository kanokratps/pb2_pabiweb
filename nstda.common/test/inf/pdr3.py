#!/usr/bin/env python
# -*- coding: utf-8 -*-

import xmlrpclib


alfresco = xmlrpclib.ServerProxy("http://002648:password@10.226.202.134/alfresco/s/pb/pcm/inf")

arg = {
	'action':'3',
	'pdNo':'PD16000285',
	'comment':'Invalid Document',
	'reqBy':'002648'
}
result = alfresco.ord.action(arg);

print result 
