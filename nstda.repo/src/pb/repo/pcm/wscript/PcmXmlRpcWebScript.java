package pb.repo.pcm.wscript;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.alfresco.service.cmr.security.AuthenticationService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.extensions.webscripts.WebScriptRequest;
import org.springframework.extensions.webscripts.WebScriptResponse;
import org.springframework.stereotype.Component;

import pb.common.constant.CommonConstant;
import pb.repo.admin.service.AdminMasterService;
import pb.repo.admin.service.AdminMsgService;
import pb.repo.admin.service.AlfrescoService;
import pb.repo.pcm.xmlrpc.PcmOrdInvocationHandler;
import pb.repo.pcm.xmlrpc.PcmReqInvocationHandler;
import redstone.xmlrpc.XmlRpcDispatcher;
import redstone.xmlrpc.XmlRpcServer;

import com.github.dynamicextensionsalfresco.webscripts.annotations.HttpMethod;
import com.github.dynamicextensionsalfresco.webscripts.annotations.Uri;
import com.github.dynamicextensionsalfresco.webscripts.annotations.WebScript;

@Component
@WebScript
public class PcmXmlRpcWebScript {
	
	private static Logger log = Logger.getLogger(PcmXmlRpcWebScript.class);
	
	private static final String URI_PREFIX = CommonConstant.GLOBAL_URI_PREFIX + "/pcm";
	
	@Autowired
	AlfrescoService alfrescoService;
	
	@Autowired
	AdminMasterService masterService;
	
	@Autowired
	AdminMsgService msgService;
	
	@Autowired
	AuthenticationService authService;
	
	@Autowired
	PcmReqInvocationHandler pcmReqInvocationHandler;
	
	@Autowired
	PcmOrdInvocationHandler pcmOrdInvocationHandler;
	
	@Uri(method=HttpMethod.POST, value=URI_PREFIX+"/inf")
	public void handleOdooXmlrpc(final WebScriptRequest request, final WebScriptResponse response) throws Throwable {
		
		try {
			log.info("/pcm/inf:"+request.getContent().getContent().length());
			if (request.getContent().getContent().length() < 5000) {
				log.info(request.getContent().getContent());
			}
			
			XmlRpcServer server = new XmlRpcServer();
			server.addInvocationHandler("req", pcmReqInvocationHandler);
			server.addInvocationHandler("ord", pcmOrdInvocationHandler);
			
			XmlRpcDispatcher dispatcher = new XmlRpcDispatcher(server, "");
			
			InputStream is = new ByteArrayInputStream(request.getContent().getContent().getBytes("UTF-8"));
			response.setContentEncoding("UTF-8");
			dispatcher.dispatch(is, response.getWriter());
			
			
		} catch (Exception ex) {
			log.error(ex);
		}

	}
	
}
