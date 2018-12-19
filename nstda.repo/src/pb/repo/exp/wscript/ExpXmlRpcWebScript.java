package pb.repo.exp.wscript;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.alfresco.service.cmr.security.AuthenticationService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.extensions.webscripts.WebScriptRequest;
import org.springframework.extensions.webscripts.WebScriptResponse;
import org.springframework.extensions.webscripts.servlet.WebScriptServletRequest;
import org.springframework.stereotype.Component;

import pb.common.constant.CommonConstant;
import pb.repo.admin.service.AdminMasterService;
import pb.repo.admin.service.AdminMsgService;
import pb.repo.admin.service.AlfrescoService;
import pb.repo.exp.xmlrpc.ExpBrwInvocationHandler;
import pb.repo.exp.xmlrpc.ExpUseInvocationHandler;
import redstone.xmlrpc.XmlRpcDispatcher;
import redstone.xmlrpc.XmlRpcServer;

import com.github.dynamicextensionsalfresco.webscripts.AnnotationWebScriptRequest;
import com.github.dynamicextensionsalfresco.webscripts.annotations.HttpMethod;
import com.github.dynamicextensionsalfresco.webscripts.annotations.Uri;
import com.github.dynamicextensionsalfresco.webscripts.annotations.WebScript;

import javax.servlet.http.HttpServletRequest;

@Component
@WebScript
public class ExpXmlRpcWebScript {
	
	private static Logger log = Logger.getLogger(ExpXmlRpcWebScript.class);
	
	private static final String URI_PREFIX = CommonConstant.GLOBAL_URI_PREFIX + "/exp";
	
	@Autowired
	AlfrescoService alfrescoService;
	
	@Autowired
	AdminMasterService masterService;
	
	@Autowired
	AdminMsgService msgService;
	
	@Autowired
	AuthenticationService authService;
	
	@Autowired
	ExpBrwInvocationHandler expBrwInvocationHandler;
	
	@Autowired
	ExpUseInvocationHandler expUseInvocationHandler;
	
	@Uri(method=HttpMethod.POST, value=URI_PREFIX+"/inf")
	public void handleOdooXmlrpc(final WebScriptRequest request, final WebScriptResponse response) throws Throwable {
		
		try {
			log.info("/exp/inf:"+request.getContent().getContent().length());
			String content = request.getContent().getContent();
			if (request.getContent().getContent().length() < 5000) {
				log.info(content);
			}
			
			int pos = content.lastIndexOf("</member>");
			
    		AnnotationWebScriptRequest r = (AnnotationWebScriptRequest)request;
    		WebScriptServletRequest rr = (WebScriptServletRequest)r.getNext();
    		HttpServletRequest req = rr.getHttpServletRequest();
			
			String source = req.getRemoteAddr()+":"+req.getRemotePort();
			
//    		for(String h:request.getHeaderNames()) {
//    			log.info("header:"+h);
//    			String[] v = request.getHeaderValues(h);
//    			for(String s:v) {
//        			log.info("  - "+s);
//    			}
//    		}
    		
			content = content.substring(0, pos + 9) + "<member>"
					+"<name>BG_SOURCE</name>"
					+"<value><string>"+source+"</string></value>"
					+"</member>"
					+ content.substring(pos+9)
					;
			
			
			XmlRpcServer server = new XmlRpcServer();
			server.addInvocationHandler("brw", expBrwInvocationHandler);
			server.addInvocationHandler("use", expUseInvocationHandler);
			
			XmlRpcDispatcher dispatcher = new XmlRpcDispatcher(server, "");
			
			InputStream is = new ByteArrayInputStream(content.getBytes("UTF-8"));
			response.setContentEncoding("UTF-8");
			dispatcher.dispatch(is, response.getWriter());
			
		} catch (Exception ex) {
			log.error(ex);
		}

	}
	
}
