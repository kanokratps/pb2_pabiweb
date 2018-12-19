package pb.common.util;

import org.alfresco.repo.security.authentication.AuthenticationUtil;
import org.alfresco.repo.security.authentication.AuthenticationUtil.RunAsWork;
import org.alfresco.service.cmr.repository.NodeRef;
import org.alfresco.service.cmr.repository.NodeService;
import org.alfresco.service.namespace.QName;

public class NodeUtil {
	
	public static Object getProperty(final NodeRef nodeRef, final QName qname, final NodeService nodeService) {
		return 	AuthenticationUtil.runAs(new RunAsWork<Object>()
			    {
					public Object doWork() throws Exception
					{
						return nodeService.getProperty(nodeRef, qname);
					}
			    }, AuthenticationUtil.getAdminUserName());
	}
	
	public static QName getType(final NodeRef nodeRef,final NodeService nodeService) {
		return 	AuthenticationUtil.runAs(new RunAsWork<QName>()
			    {
					public QName doWork() throws Exception
					{
						return nodeService.getType(nodeRef);
					}
			    }, AuthenticationUtil.getAdminUserName());
	}
	
	public static String trimNodeRef(String nodeRef) {
		return nodeRef.replace("workspace://SpacesStore/", "");
	}
	
	public static String fullNodeRef(String id) {
		return "workspace://SpacesStore/" + id;
	}

}
