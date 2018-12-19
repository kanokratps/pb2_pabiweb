package pb.repo.hr.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.delegate.DelegateTask;
import org.alfresco.model.ContentModel;
import org.alfresco.repo.security.authentication.AuthenticationUtil;
import org.alfresco.repo.security.authentication.AuthenticationUtil.RunAsWork;
import org.alfresco.repo.version.VersionModel;
import org.alfresco.service.cmr.coci.CheckOutCheckInService;
import org.alfresco.service.cmr.model.FileFolderService;
import org.alfresco.service.cmr.repository.ContentReader;
import org.alfresco.service.cmr.repository.ContentService;
import org.alfresco.service.cmr.repository.ContentWriter;
import org.alfresco.service.cmr.repository.NodeRef;
import org.alfresco.service.cmr.repository.NodeService;
import org.alfresco.service.cmr.search.SearchService;
import org.alfresco.service.cmr.security.PersonService;
import org.alfresco.service.cmr.version.VersionType;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pb.common.constant.CommonConstant;
import pb.common.util.CommonDateTimeUtil;
import pb.repo.admin.constant.MainMasterConstant;
import pb.repo.admin.model.MainMasterModel;
import pb.repo.admin.service.AdminHrEmployeeService;
import pb.repo.admin.service.AdminMasterService;
import pb.repo.admin.service.AlfrescoService;
import pb.repo.hr.constant.HrSalWorkflowConstant;
import pb.repo.hr.model.HrSalModel;

import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;

@Service
public class HrSalSignatureService {
	
	private static Logger log = Logger.getLogger(HrSalSignatureService.class);

	@Autowired
	FileFolderService fileFolderService;
	
	@Autowired
	ContentService contentService;
	
	@Autowired
	CheckOutCheckInService checkOutCheckInService;
	
	@Autowired
	NodeService nodeService;
	
	@Autowired
	PersonService personService;
	
	@Autowired
	HrSalService hrSalService;
	
	@Autowired
	AdminMasterService adminMasterService;
	
	@Autowired
	AlfrescoService alfrescoService;
	
	@Autowired
	SearchService searchService;
	
	@Autowired
	AdminHrEmployeeService adminHrEmployeeService;
	
	public NodeRef addSignature(final DelegateTask task, List<String> users, List<Date> dates) {

		String refId = task.getVariable(HrSalWorkflowConstant.MODEL_PREFIX+"id").toString();
		
		HrSalModel model = hrSalService.get(refId, null);
					
		String docRef = model.getDocRef();
		NodeRef pdfNodeRef = new NodeRef(docRef);
		
		return addSignature(pdfNodeRef, users, dates);
	}
	
	public NodeRef addSignature(final NodeRef pdfNodeRef,final List<String> users, final List<Date> dates) {

		
		return AuthenticationUtil.runAs(new RunAsWork<NodeRef>()
		{
			public NodeRef doWork() throws Exception
			{
				log.info("pdfNodeRef :: " + pdfNodeRef);
				
				CommonDateTimeUtil util = new CommonDateTimeUtil();
				
				ContentReader contentReader = contentService.getReader(pdfNodeRef, ContentModel.PROP_CONTENT);
			
			    InputStream is = contentReader.getContentInputStream();

			    try {
		        		 
				        MainMasterModel posModel = adminMasterService.getSystemConfig(MainMasterConstant.SCC_HR_SAL_SIGN_POS);

				        String[] posList = posModel.getFlag1().split(";");
				        
				        if (posList.length>0) {
					    	PdfReader pdfToWatermark = new PdfReader(is);
							int numberOfPages = pdfToWatermark.getNumberOfPages();
					        ByteArrayOutputStream out = new ByteArrayOutputStream();
					        PdfStamper stamp = new PdfStamper(pdfToWatermark, out);
					        
					        PdfContentByte pageContent = stamp.getOverContent(numberOfPages);
					        
					        int i = 0;
					        
					        for (String posPair : posList) {
					        	log.info("posPair:"+posPair);
					        	
						        String[] pos = posPair.split(",");
						        
					        	Integer[] position = {Integer.parseInt(pos[0]), Integer.parseInt(pos[1])};
					        	
					        	if (position[0] != 0 || position[1] != 0) {
						        	String curUser = users.get(i);
							        String date = util.getSignatureDate(dates.get(i++));
						        	log.info("user:"+curUser+", date:"+date);
						        	
									addOneSignature(pageContent, position, curUser, date);
							        
								}
					        }
					        
					        stamp.close();
					        is.close();
					        out.flush();
					        out.close();
					        
					        if (contentReader.isClosed()) {
					        	try {
						   			  Thread.sleep(2000);
						   		} catch (InterruptedException ie) {
						   			  //Handle exception
						   		}
					        	
					        	alfrescoService.cancelCheckout(pdfNodeRef);
					            NodeRef workingCopy = checkOutCheckInService.checkout(pdfNodeRef);
								ContentWriter contentWriter = contentService.getWriter(workingCopy, ContentModel.PROP_CONTENT, true);
							    contentWriter.setMimetype("application/pdf");
							    
							    contentWriter.putContent(new ByteArrayInputStream(out.toByteArray()));
							    nodeService.setProperty(workingCopy, ContentModel.PROP_CONTENT, contentWriter.getContentData());
							    
							    Map<String, Serializable> versionProperties = new HashMap<String, Serializable>(1);
							    versionProperties.put(VersionModel.PROP_VERSION_TYPE, VersionType.MINOR);
							   
							    log.info("  check in ...");
							    checkOutCheckInService.checkin(workingCopy, versionProperties);
							    log.info("  finish");
					        }
			    	
				        }
			    }  catch (Exception ex) {
					log.error("", ex);
				}
			    
				return pdfNodeRef;
			}
		}, AuthenticationUtil.getAdminUserName());
						
	}
	
	private void addOneSignature(PdfContentByte pageContent, Integer[] signPos, String curUser, String date) throws Exception {
		
		if(signPos[0] != 0 && signPos[1] != 0){
			
			MainMasterModel fontModel = adminMasterService.getSystemConfig(MainMasterConstant.SCC_HR_SAL_SIGN_FONT_SIZE);
			MainMasterModel offsetModel = adminMasterService.getSystemConfig(MainMasterConstant.SCC_HR_SAL_SIGN_DATE_OFFSET);
			
			String basePath = System.getProperty("catalina.base");
			String fontPath = basePath+"/webapps/alfresco/WEB-INF/classes";
		
			Float fontSize = Float.parseFloat(fontModel.getFlag1());
			Font font = FontFactory.getFont(fontPath+"/THSarabun Bold.ttf", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED, fontSize, Font.BOLD);
			BaseFont baseFont = font.getBaseFont();
			
//			BaseFont baseFont = BaseFont.createFont(fontPath+"/THSarabun.ttf", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
			
	        Map<String,Object> empDtl = adminHrEmployeeService.getWithDtl(curUser, null);
	        
			String curUserName = empDtl.get("title_th").toString()+empDtl.get("first_name_th")+" "+empDtl.get("last_name_th");
			
			Float curUserNameWidth = baseFont.getWidthPoint(curUserName, fontSize);
			log.info("curUserNameWidth:"+curUserNameWidth);
			Float dateWidth = baseFont.getWidthPoint(date, fontSize);
			log.info("dateWidth:"+dateWidth);
			
			String[] off = offsetModel.getFlag1().split(",");
			
	        log.info("x::"+signPos[0]);
	        log.info("y::"+signPos[1]);
	        
	     	pageContent.beginText();
		    pageContent.setFontAndSize(baseFont, fontSize);
		    
		    pageContent.setTextMatrix(signPos[0]-(curUserNameWidth/2), signPos[1]);
		    pageContent.showText(curUserName);
		    
		    if (Float.parseFloat(off[0])!=0 || Float.parseFloat(off[1])!=0) {
			    pageContent.setTextMatrix(signPos[0]-(dateWidth/2)+Float.parseFloat(off[0]), signPos[1]+Float.parseFloat(off[1]));
			    pageContent.showText(date);
		    }
		    
		    pageContent.endText(); 
		}
	
	}
	
	
//	public List<NodeRef> addDocSignatureAllApprover(final DelegateTask task, final HashMap<String, Object> signConfMap ,final String prefix) {
//		
//		return AuthenticationUtil.runAs(new RunAsWork<List<NodeRef>>()
//				{
//					public List<NodeRef> doWork() throws Exception
//					{
////						List<Map<String, Object>> docSignPatternList = adminMasterService.listByType(MainMasterConstant.TYPE_SYSTEM_CONFIG, MainMasterConstant.SCC_MEMO_DOC_SIGN_PATTERN+"_", null, null, null);
//						List<Map<String, Object>> docSignPatternList = null;
//						log.info("docSignPatternList :: " + docSignPatternList+ ", "+docSignPatternList.size());
//						
//						List<ActivitiScriptNode> attachDocList = (List<ActivitiScriptNode>)task.getVariable(prefix+"attachDocument");
//						log.info("attachDocList :: " + attachDocList + ", " + attachDocList.size());
//						
//						List<NodeRef> attDocList = new ArrayList<NodeRef>();
//						
//						CommonDateTimeUtil util = new CommonDateTimeUtil();
//						
//					    try {
//					    	List<NodeRef> signatureList = new ArrayList<NodeRef>();
//					    	List<String> signDateList = new ArrayList<String>();
//					    	
//					    	int level = 1;
//					    	
//							NodeRef folderNodeRef = new NodeRef( signConfMap.get(MainMasterConstant.SCC_MAIN_SIGNATURE_PATH).toString());
//							log.info("Signature Folder="+folderNodeRef);
//							
//				        	Object reviewer = task.getVariable(prefix+"reviewer"+level+"Assignee");
//				        	while(reviewer!=null&&!reviewer.toString().equalsIgnoreCase("")) {
//				        		 
//								String reviewerImgName = reviewer.toString().toLowerCase()+".png";
//								log.info("Approver="+reviewerImgName);
//								
//								NodeRef signatureNodeRef = fileFolderService.searchSimple(folderNodeRef, reviewerImgName);
//								log.info("Signature Image="+signatureNodeRef);
//								
//								signatureList.add(signatureNodeRef);
//								signDateList.add(util.getDisplayDate((Date)task.getVariable(prefix+"reviewer"+level+"SignDate")));
//								
//								level++;
//								
//								reviewer = task.getVariable(prefix+"reviewer"+level+"Assignee");
//				        	}
//						        
//						        
//							for (ActivitiScriptNode scriptNodeRef : attachDocList) {
//								NodeRef attachNodeRef = scriptNodeRef.getNodeRef();
//								log.info("attachNodeRef :: " + attachNodeRef);
//								
//								attDocList.add(attachNodeRef);
//								
//								FileInfo fileInfo = fileFolderService.getFileInfo(attachNodeRef);
//								log.info("name:"+fileInfo.getName());
//								
//								for(Map<String, Object> docSignPatternModel : docSignPatternList) {
//									log.info("  regex:"+docSignPatternModel.get(MainMasterConstant.TFN_FLAG1));
//									
//									if (fileInfo.getName().matches((String)docSignPatternModel.get(MainMasterConstant.TFN_FLAG1))) {
//										log.info("   matched");
//							
//										ContentReader contentReader = contentService.getReader(attachNodeRef, ContentModel.PROP_CONTENT);
//										
//									    InputStream is = contentReader.getContentInputStream();
//				
//								    	List<List<String>> signPosList = new ArrayList<List<String>>();
//								    	String signature = (String)docSignPatternModel.get(MainMasterConstant.TFN_FLAG2);
//								    	log.info("signature position : "+signature);
//								    	List<String> s = null;
//										if(signature!=null){
//											signature =  signature.replaceAll("[\\[\\]]", "");
//											log.info("signature[] : "+signature);
//											List<String> position = Arrays.asList(signature.split(","));
//											
//											int i;
//											for(i=0;i<position.size(); i+=2){
//												
//												s = new ArrayList<String>();
//												for(int l=0; l<2; l++){
//													s.add(position.get(i+l));
//												}
//												
//												signPosList.add(s);
//												
//											}
//											
//										}
//								    	
//								    	PdfReader pdfToWatermark = new PdfReader(is);
//										int numberOfPages = pdfToWatermark.getNumberOfPages();
//								        ByteArrayOutputStream out = new ByteArrayOutputStream();
//								        PdfStamper stamp = new PdfStamper(pdfToWatermark, out);
//								        
//								        for(int i=1 ; i<= numberOfPages; i++){
//								        	 PdfContentByte pageContent = stamp.getOverContent(i);
//							        	 
//								        	 int j = 0;
//								        	 for(j=0; j<signatureList.size(); j++) {
//								        		 if (Integer.valueOf(signPosList.get(j).get(0)) != 0 || Integer.valueOf(signPosList.get(j).get(1)) != 0 ) {
//								        			// addOneSignature(pageContent, signPosList.get(j), signatureList.get(j), signDateList.get(j), signConfMap, 0);
//								        		 }
//								        	 }
//							        	}
//								        
//								        stamp.close();
//								        is.close();
//								        out.flush();
//								        out.close();
//								        
//								        if (contentReader.isClosed()) {
//								        	try {
//									   			  Thread.sleep(2000);
//									   		} catch (InterruptedException ie) {
//									   			  //Handle exception
//									   		}
//								            
//								        	alfrescoService.cancelCheckout(attachNodeRef);
//								            NodeRef workingCopy = checkOutCheckInService.checkout(attachNodeRef);
//											ContentWriter contentWriter = contentService.getWriter(workingCopy, ContentModel.PROP_CONTENT, true);
//										    contentWriter.setMimetype("application/pdf");
//										    
//										    contentWriter.putContent(new ByteArrayInputStream(out.toByteArray()));
//										    nodeService.setProperty(workingCopy, ContentModel.PROP_CONTENT, contentWriter.getContentData());
//										    
//										    Map<String, Serializable> versionProperties = new HashMap<String, Serializable>(1);
//										    versionProperties.put(VersionModel.PROP_VERSION_TYPE, VersionType.MINOR);
//										   
//										    log.info("  check in ...");
//										    checkOutCheckInService.checkin(workingCopy, versionProperties);
//										    log.info("  finish");
//								        }
//								        break;
//							        
//									} // if
//								} // for doc sign pattern
//							} // for attach doc
//					    
//					    }  catch (Exception ex) {
//							log.error("", ex);
//						}
//						
//						return attDocList;
//					}
//				}, AuthenticationUtil.getAdminUserName());	
//	}
//		
//	public List<NodeRef> addDocSignature(final DelegateTask task, final HashMap<String, Object> signConfMap ,final String prefix, final Integer level) {
//		
//		return AuthenticationUtil.runAs(new RunAsWork<List<NodeRef>>()
//				{
//					public List<NodeRef> doWork() throws Exception
//					{
////						List<Map<String, Object>> docSignPatternList = adminMasterService.listByType(MainMasterConstant.TYPE_SYSTEM_CONFIG, MainMasterConstant.SCC_MEMO_DOC_SIGN_PATTERN+"_", null, null, null);
//						List<Map<String, Object>> docSignPatternList = null;
//						log.info("docSignPatternList :: " + docSignPatternList+ ", "+docSignPatternList.size());
//						
//						List<ActivitiScriptNode> attachDocList = (List<ActivitiScriptNode>)task.getVariable(prefix+"attachDocument");
//						log.info("attachDocList :: " + attachDocList + ", " + attachDocList.size());
//						
//						List<NodeRef> attDocList = new ArrayList<NodeRef>();
//						
//						CommonDateTimeUtil util = new CommonDateTimeUtil();
//						
//					    try {
//					    	
//					    	
//				        	Object reviewer = task.getVariable(prefix+"reviewer"+level+"Assignee");
//				        	if(reviewer!=null&&!reviewer.toString().equalsIgnoreCase("")) {
//						        String date = util.getDisplayDate((Date)task.getVariable(prefix+"reviewer"+level+"SignDate"));
//						        
//								String fileName = reviewer.toString().toLowerCase()+".png";
//								log.info("Signed user="+fileName);
//								
//								NodeRef nodeRef = new NodeRef( signConfMap.get(MainMasterConstant.SCC_MAIN_SIGNATURE_PATH).toString());
//								log.info("Signature Folder="+nodeRef);
//								
//								nodeRef = fileFolderService.searchSimple(nodeRef, fileName);
//								log.info("Signature Image="+nodeRef);
//						        
//						        
//								for (ActivitiScriptNode scriptNodeRef : attachDocList) {
//									NodeRef attachNodeRef = scriptNodeRef.getNodeRef();
//									log.info("attachNodeRef :: " + attachNodeRef);
//									
//									attDocList.add(attachNodeRef);
//									
//									FileInfo fileInfo = fileFolderService.getFileInfo(attachNodeRef);
//									log.info("name:"+fileInfo.getName());
//									
//									for(Map<String, Object> docSignPatternModel : docSignPatternList) {
//										log.info("  regex:"+docSignPatternModel.get(MainMasterConstant.TFN_FLAG1));
//										
//										if (fileInfo.getName().matches((String)docSignPatternModel.get(MainMasterConstant.TFN_FLAG1))) {
//											log.info("   matched");
//								
//									    	List<List<String>> signList = new ArrayList<List<String>>();
//									    	String signature = (String)docSignPatternModel.get(MainMasterConstant.TFN_FLAG2);
//									    	log.info("signature position : "+signature);
//									    	List<String> s = null;
//											if(signature!=null){
//												signature =  signature.replaceAll("[\\[\\]]", "");
//												log.info("signature[] : "+signature);
//												List<String> position = Arrays.asList(signature.split(","));
//												
//												int i;
//												for(i=0;i<position.size(); i+=2){
//													
//													s = new ArrayList<String>();
//													for(int l=0; l<2; l++){
//														s.add(position.get(i+l));
//													}
//													
//													signList.add(s);
//													
//												}
//												
//											}
//									    	
//									        List<String> position = signList.get(level-1);
//									        
//									        if (Integer.valueOf(position.get(0)) != 0 || Integer.valueOf(position.get(1)) != 0) {
//									        	
//												ContentReader contentReader = contentService.getReader(attachNodeRef, ContentModel.PROP_CONTENT);
//												
//											    InputStream is = contentReader.getContentInputStream();
//											    
//										    	PdfReader pdfToWatermark = new PdfReader(is);
//												int numberOfPages = pdfToWatermark.getNumberOfPages();
//										        ByteArrayOutputStream out = new ByteArrayOutputStream();
//										        PdfStamper stamp = new PdfStamper(pdfToWatermark, out);
//										        
//										        for(int i=1 ; i<= numberOfPages; i++){
//										        	 PdfContentByte pageContent = stamp.getOverContent(i);
//									        	 
//											         //addOneSignature(pageContent, position, nodeRef, date, signConfMap, 0);
//									        	}
//										        
//										        stamp.close();
//										        is.close();
//										        out.flush();
//										        out.close();
//										        
//										        if (contentReader.isClosed()) {
//										        	try {
//											   			  Thread.sleep(2000);
//											   		} catch (InterruptedException ie) {
//											   			  //Handle exception
//											   		}
//										            
//										        	alfrescoService.cancelCheckout(attachNodeRef);
//										            NodeRef workingCopy = checkOutCheckInService.checkout(attachNodeRef);
//													ContentWriter contentWriter = contentService.getWriter(workingCopy, ContentModel.PROP_CONTENT, true);
//												    contentWriter.setMimetype("application/pdf");
//												    
//					//							    contentReader = contentService.getReader(signNodeRef, ContentModel.PROP_CONTENT);
//												    
//					//							    contentWriter.putContent(contentReader);
//												    contentWriter.putContent(new ByteArrayInputStream(out.toByteArray()));
//												    nodeService.setProperty(workingCopy, ContentModel.PROP_CONTENT, contentWriter.getContentData());
//												    
//												    Map<String, Serializable> versionProperties = new HashMap<String, Serializable>(1);
//												    versionProperties.put(VersionModel.PROP_VERSION_TYPE, VersionType.MINOR);
//												   
//												    log.info("  check in ...");
//												    checkOutCheckInService.checkin(workingCopy, versionProperties);
//												    log.info("  finish");
//										        }
//									        
//									        } // position
//									        break;
//								        
//										} // if
//									} // for doc sign pattern
//								} // for attach doc
//					        } // if
//					    
//					    }  catch (Exception ex) {
//							log.error("", ex);
//						}
//						
//						return attDocList;
//					}
//				}, AuthenticationUtil.getAdminUserName());	
//	}
}
