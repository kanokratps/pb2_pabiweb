package pb.repo.admin.schedule;

import java.io.Serializable;
import java.net.URLEncoder;
import java.security.Security;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;

import org.alfresco.repo.template.DateCompareMethod;
import org.alfresco.repo.template.HasAspectMethod;
import org.alfresco.repo.template.I18NMessageMethod;
import org.alfresco.repo.template.TemplateNode;
import org.alfresco.service.ServiceRegistry;
import org.alfresco.service.cmr.model.FileFolderService;
import org.alfresco.service.cmr.repository.NodeRef;
import org.alfresco.service.cmr.repository.NodeService;
import org.alfresco.service.cmr.repository.Path;
import org.alfresco.service.cmr.repository.Path.Element;
import org.alfresco.service.cmr.repository.TemplateService;
import org.alfresco.service.cmr.search.SearchService;
import org.alfresco.service.cmr.security.AuthenticationService;
import org.alfresco.service.cmr.security.AuthorityService;
import org.alfresco.service.cmr.security.PersonService;
import org.alfresco.service.cmr.security.PersonService.PersonInfo;
import org.alfresco.service.cmr.workflow.WorkflowService;
import org.alfresco.service.cmr.workflow.WorkflowTask;
import org.alfresco.service.namespace.QName;
import org.alfresco.util.ISO9075;
import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import pb.common.constant.CommonConstant;
import pb.common.util.CommonUtil;
import pb.common.util.MailUtil;
import pb.common.util.PersonUtil;
import pb.repo.admin.constant.MainCompleteNotificationConstant;
import pb.repo.admin.constant.MainMasterConstant;
import pb.repo.admin.constant.MainWorkflowConstant;
import pb.repo.admin.model.MainCompleteNotificationModel;
import pb.repo.admin.model.MainMasterModel;
import pb.repo.admin.service.AdminCompleteNotificationService;
import pb.repo.admin.service.AdminMasterService;

import com.github.dynamicextensionsalfresco.jobs.ScheduledQuartzJob;
import com.sun.mail.smtp.SMTPMessage;

/**
 * New Quartz support coming to Dynamic extensions 1.3
 */
@Component
//@ScheduledQuartzJob(name = "mailCompleteNotificationJob", cron="0 2/10 * * * ?", group="pb") // real
//@ScheduledQuartzJob(name = "mailCompleteNotificationJob", cron="0 0/1 * * * ?", group="pb")
public class MailCompleteNotificationScheduleJob implements Job {
	
	private final Logger log = Logger.getLogger(MailCompleteNotificationScheduleJob.class);
	
	@Autowired
	AuthenticationService authService;
	
	@Autowired
	AuthorityService authorityService;
	
	@Autowired
	FileFolderService fileFolderService;
	
	@Autowired
	NodeService nodeService;
	
	@Autowired
	PersonService personService;
	
	@Autowired
	SearchService searchService;
	
	@Autowired
	ServiceRegistry serviceRegistry;
	
	@Autowired
	TemplateService templateService;
	
	@Autowired
	WorkflowService workflowService;
	
	@Autowired
	AdminMasterService masterService;
	
	@Autowired
	AdminCompleteNotificationService completeNotificationService;
	
	public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
		log.info("--- | Mail Complete Notification Schedule | ---");
		
		try {
    		MainMasterModel mailCompleteModel = masterService.getSystemConfig(MainMasterConstant.SCC_PCM_REQ_MAIL_COMPLETE);
    		boolean mailComplete = mailCompleteModel != null && mailCompleteModel.getFlag1().equals(CommonConstant.V_ENABLE);
    		log.info(MainMasterConstant.SCC_PCM_REQ_MAIL_COMPLETE+" :: " + mailComplete);
			
    		MainMasterModel mailNotifyModel = masterService.getSystemConfig(MainMasterConstant.SCC_PCM_REQ_MAIL_NOTIFY);
    		boolean mailNotify = mailNotifyModel != null && mailNotifyModel.getFlag1().equals(CommonConstant.V_ENABLE);
    		log.info(MainMasterConstant.SCC_PCM_REQ_MAIL_NOTIFY+" :: " + mailNotify);
    		
    		if(mailComplete && mailNotify) {
    			List<MainCompleteNotificationModel> list = completeNotificationService.list();
    			for(MainCompleteNotificationModel model : list) {
    				log.info("record : "+model.getId()+" , "+model.getTaskId()+" , "+model.getReceiver());
    				
    				WorkflowTask task = workflowService.getTaskById("activiti$"+model.getTaskId());
    				if (task != null) {
    					log.info("   "+task.getDescription()+" , "+task.getName()+" , "+task.getTitle());
        				completeNotificationService.delete(model.getId());
        				doSendMail(task, model.getReceiver(), model.getTemplate());
    				}
    				else {
    					log.info("   task is null");
    				}
    			}
    		}
		} catch (Exception ex) {
			log.error("", ex);
		}
	}
	
	public void doSendMail(WorkflowTask task, String user, String template){
		
		log.info("--- | Send Mail | ---");
		
		try {
			
			List<NodeRef> userList = new ArrayList<NodeRef>();
		    log.info("  name :: " + task.getName());
			
			userList.add(PersonUtil.getPerson(user, personService));
			log.info("userList :: " + userList.toString());

			MainMasterModel masterModel = null;
			NodeRef emailTemplate = null;
			String workflowMailSubject = null;
			String workflowMailFromLabel = null;
			
			if (template.equals(MainCompleteNotificationConstant.TEMPLATE_COMPLETE)) {
				masterModel = masterService.getSystemConfig(MainMasterConstant.SCC_PCM_REQ_MAIL_COMPLETE_TEMPLATE);
				emailTemplate = new NodeRef(masterModel.getFlag1());
				
				masterModel = masterService.getSystemConfig(MainMasterConstant.SCC_PCM_REQ_MAIL_COMPLETE_SUBJECT);
				workflowMailSubject = masterModel.getFlag1();
				
				masterModel = masterService.getSystemConfig(MainMasterConstant.SCC_PCM_REQ_MAIL_COMPLETE_FROM);
				workflowMailFromLabel = masterModel.getFlag1();
			} else {
				masterModel = masterService.getSystemConfig(MainMasterConstant.SCC_PCM_REQ_MAIL_RELATED_TEMPLATE);
				emailTemplate = new NodeRef(masterModel.getFlag1());
				
				masterModel = masterService.getSystemConfig(MainMasterConstant.SCC_PCM_REQ_MAIL_RELATED_SUBJECT);
				workflowMailSubject = masterModel.getFlag1();
				
				masterModel = masterService.getSystemConfig(MainMasterConstant.SCC_PCM_REQ_MAIL_RELATED_FROM);
				workflowMailFromLabel = masterModel.getFlag1();
			}
			
			if(userList.size() > 0){
				sendMailNotify(task, userList, emailTemplate, workflowMailFromLabel, workflowMailSubject);
			}
		    		
		} catch (Exception ex) {
			log.error("", ex);
		}
	}
	
	public void sendMailNotify(WorkflowTask task, List<NodeRef> userList, NodeRef mailTemplate, String fromLabel, String subjectLabel)
	{
		log.info("Start Send Mail.........");
		
		List<String> usersList = new ArrayList<String>();
		List<String> usersEmailList = new ArrayList<String>();
		
		for (NodeRef user : userList)
    	{
            PersonInfo person = PersonUtil.getPerson(user, personService);
            log.info("User:" + user.getId());
            log.info("Person.getFirstName():" + person.getFirstName());
            usersList.add(person.getFirstName());
            String mail = PersonUtil.getEmail(user, nodeService);
            usersEmailList.add(mail);
			log.info("**** EMAIL : usersEmailList : "+mail);
    	}
		
		try{
			final Properties gProp = CommonUtil.getGlobalProperties();
			Properties props = new Properties();
			props = MailUtil.getMailProperties(gProp);

			Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
			Session session = MailUtil.getDefaultInstance(props, gProp.getProperty(CommonConstant.GP_MAIL_USERNAME), gProp.getProperty(CommonConstant.GP_MAIL_PASSWORD));
			
			Map<String, Object> model = createEmailTemplateModel();
			
			String shareProtocol = gProp.getProperty(CommonConstant.GP_SHARE_PROTOCOL);
			String shareHost = gProp.getProperty(CommonConstant.GP_SHARE_HOST);
			String sharePort = gProp.getProperty(CommonConstant.GP_SHARE_PORT);
			String shareUrl = shareProtocol + "://" +shareHost+":"+sharePort;
			String taskId = task.getId();
			
			// Loop for get all property from task
			Map<QName, Serializable> taskProps = task.getProperties();
			Iterator iterator = taskProps.entrySet().iterator();
			
			while(iterator.hasNext()){
				Map.Entry mapEntry = (Map.Entry) iterator.next();
				QName qName = (QName)mapEntry.getKey();
				
				if(qName.getLocalName().equalsIgnoreCase("folderRef")){
					
					String folderUrl = "";
					
					if(fileFolderService.exists(new NodeRef(mapEntry.getValue().toString()))){
						
						Path path = nodeService.getPath(new NodeRef(mapEntry.getValue().toString()));
						String pathStr = path.toString();
						//log.info("path 1="+path.toString());
						
						String site = null;
						boolean foundSite = false;
						for(Element el:path) {
							log.info("element:"+el.getElementString()+" -- "+el.toString());
							String e = el.toString();
							
							if (e.endsWith("}sites")) {
								foundSite = true;
								continue;
							}
							if (foundSite) {
								int pos = e.indexOf("}");
								site = e.substring(pos+1);
								foundSite = false;
							}
						}
						
						int pos = pathStr.indexOf("documentLibrary/");
						pathStr = pathStr.substring(pos+15);
						
						pos = pathStr.indexOf("{");
						int pos2 = pathStr.indexOf("}");
						while(pos >=0 && pos2>=0) {
							String p = pathStr.substring(0, pos) + pathStr.substring(pos2+1);
							
							pathStr = p;
							
							pos = pathStr.indexOf("{");
							pos2 = pathStr.indexOf("}");
						}
						//log.info("path 2="+pathStr);
						pathStr = ISO9075.decode(pathStr);
						//log.info("path 3="+pathStr);
						
						String pathNormalized = "";
						String s = Normalizer.normalize(pathStr, Normalizer.Form.NFD);
						for(int i=0;i<s.length();++i) {
							String h = Integer.toHexString((int)s.charAt(i));
							if (h.length() == 3) {
								pathNormalized += "%u0" + h.toUpperCase();
							}
							else {
								pathNormalized += s.charAt(i);
							}
						}
						
						//log.info("path 4="+pathNormalized);
						log.info("path 5="+URLEncoder.encode(pathNormalized));
						folderUrl = "page/site/"+site+"/documentlibrary#filter=path%7C"+ URLEncoder.encode(pathNormalized) + "%7C";
						
					}
					model.put(qName.getLocalName().toString(), folderUrl);
				}
				else
				{
					model.put(qName.getLocalName().toString(), mapEntry.getValue());
					
				}
				
				//log.info("VARIABLE: " + qName.getLocalName().toString() + "  VALUE:  " + mapEntry.getValue());
				
			}
			
			String emails = usersEmailList.toString().replace("[", "").replace("]", "");
			String users = usersList.toString().replace("[", "").replace("]", "");
			
			model.put("shareUrl", shareUrl);
			model.put("taskId", taskId);
			model.put("toAssignee", users);
			
			String workflowMailSubject = generateTextFromProperties(subjectLabel, model);
			String workflowMailFromLabel = generateTextFromProperties(fromLabel, model); 
			
			log.info("WorkflowMailSubject:" + workflowMailSubject);
			log.info("WorkflowMailFromLabel:" + workflowMailFromLabel);
			
			String text = MailUtil.processTemplate(mailTemplate.toString(), model, templateService);
			session.setDebug(gProp.getProperty(CommonConstant.GP_MAIL_DEBUG)=="true"?true:false);
			
			Transport transport;
			InternetAddress addressFrom = null;
			transport = session.getTransport();
			addressFrom = new InternetAddress(gProp.getProperty(CommonConstant.GP_MAIL_USERNAME), workflowMailFromLabel, "UTF-8");
			SMTPMessage message = new SMTPMessage(session);
			message.setFrom(addressFrom);
			message.setSender(addressFrom);
			message.setSubject(workflowMailSubject, "UTF-8");
			
			MimeMultipart multipart = new MimeMultipart("mixed");
			MimeBodyPart textPart = new MimeBodyPart();
			textPart.setContent(text, "text/html;charset=UTF-8");
			
			multipart.addBodyPart(textPart);
			message.setContent(multipart);
			
			// -------------------------- Add to recipient --------------------------------
			log.info("**** EMAIL : emails : "+emails);

			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(emails, false));
			message.saveChanges();
			transport.connect();
			Transport.send(message);
			transport.close();
			
			log.info("Sent Mail Successfully!!!");
			
	    }
		catch (Exception ex) {
			log.error("Error Start Sent Mail Complete Notification",ex);
		}
	}
	
	String generateTextFromProperties(String text, Map<String, Object> model){
		
		log.info("generateTextFromProperties...:" + text);
		
		if(text.matches("(.*)\\{(.*)\\}(.*)")){
			String field = text.substring(text.indexOf("{")+1, text.indexOf("}"));
			String _filename = field;
			
			if(model.containsKey(field)){
				if(model.get(field)!=null){
					_filename = model.get(field).toString();
				}
			}
			
			text = text.replace("${"+field+"}", _filename);
			text = generateTextFromProperties(text, model);
		}
		return text;
	}
	
	private Map<String, Object> createEmailTemplateModel()
	{
		Map<String, Object> model = new HashMap<String, Object>(8, 1.0f);
		//List<Map<String, Object>> docListModels = new ArrayList<Map<String, Object>>();
		
		NodeRef person = PersonUtil.getPerson("admin", personService);
		model.put("person", new TemplateNode(person, serviceRegistry, null));
		model.put("date", new Date());
		
		// add custom method objects
		model.put("hasAspect", new HasAspectMethod());
		model.put("message", new I18NMessageMethod());
		model.put("dateCompare", new DateCompareMethod());
		
		return model;
		
	}
	
}
