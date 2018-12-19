import xmlrpclib

alfresco = xmlrpclib.ServerProxy("http://admin:password@localhost:8080/alfresco/s/pb/pcm/inf")

# C2 : Approve
# X2 : Cancel
result = alfresco.req.action("PR16000074","X2","002648");

print result 
