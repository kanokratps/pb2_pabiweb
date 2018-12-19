package pb.repo.admin.service;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.alfresco.model.ApplicationModel;
import org.alfresco.model.ContentModel;
import org.alfresco.repo.security.authentication.AuthenticationUtil;
import org.alfresco.repo.security.authentication.AuthenticationUtil.RunAsWork;
import org.alfresco.service.cmr.coci.CheckOutCheckInService;
import org.alfresco.service.cmr.dictionary.DictionaryService;
import org.alfresco.service.cmr.dictionary.PropertyDefinition;
import org.alfresco.service.cmr.model.FileFolderService;
import org.alfresco.service.cmr.model.FileInfo;
import org.alfresco.service.cmr.repository.AssociationRef;
import org.alfresco.service.cmr.repository.ChildAssociationRef;
import org.alfresco.service.cmr.repository.ContentReader;
import org.alfresco.service.cmr.repository.ContentService;
import org.alfresco.service.cmr.repository.ContentWriter;
import org.alfresco.service.cmr.repository.MimetypeService;
import org.alfresco.service.cmr.repository.NodeRef;
import org.alfresco.service.cmr.repository.NodeService;
import org.alfresco.service.cmr.repository.Path;
import org.alfresco.service.cmr.security.PermissionService;
import org.alfresco.service.cmr.version.Version;
import org.alfresco.service.cmr.version.VersionService;
import org.alfresco.service.namespace.NamespaceService;
import org.alfresco.service.namespace.QName;
import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pb.common.model.FileModel;
import pb.common.util.CommonDateTimeUtil;
import pb.common.util.CommonUtil;

@Service
public class AlfrescoService {
	
	private static Logger log = Logger.getLogger(AlfrescoService.class);

	@Autowired
	ContentService contentService;
	
	@Autowired
	NodeService nodeService;
	
	@Autowired
	FileFolderService fileFolderService;
	
	@Autowired
	MimetypeService mimeTypeService;
	
	@Autowired
	PermissionService permissionService;
	
	@Autowired
	VersionService versionService;
	
	@Autowired
	CheckOutCheckInService checkOutCheckInService;
	
	@Autowired
	AdminHrEmployeeService adminHrEmployeeService;
	
	@Autowired
	DictionaryService dictionaryService;
	
    public ContentReader getContentByNodeRef(final NodeRef nodeRef) {
		
		ContentReader contentReader = AuthenticationUtil.runAs(new RunAsWork<ContentReader>()
			    {
					public ContentReader doWork() throws Exception
					{
						return contentService.getReader(nodeRef, ContentModel.PROP_CONTENT);
					}
			    }, AuthenticationUtil.getAdminUserName());
		
		return contentReader;

	}
    
    public NodeRef searchSimple(final NodeRef parentNodeRef, final String name) {
		
		NodeRef nodeRef = AuthenticationUtil.runAs(new RunAsWork<NodeRef>()
			    {
					public NodeRef doWork() throws Exception
					{
						return fileFolderService.searchSimple(parentNodeRef,  name);
					}
			    }, AuthenticationUtil.getAdminUserName());
		
		return nodeRef;

	}
    
    public NodeRef createDoc(NodeRef parentFolder, Object obj, String ecmFileName, String desc) throws Exception {
//		byte[] fileContent = IOUtils.toByteArray(new FileInputStream(file));
//		
//		InputStream inputStream = new ByteArrayInputStream(fileContent);
		FileInfo fileInfo = fileFolderService.create(parentFolder, ecmFileName, ContentModel.TYPE_CONTENT);
	    NodeRef newNode = fileInfo.getNodeRef();

	    String fileName = null;
        if (obj instanceof File) {
        	fileName = ((File)obj).getName();
        } else {
            fileName = ecmFileName;
        }
	    
	    String ext = FilenameUtils.getExtension(fileName);
	    String mimeType = mimeTypeService.getMimetypesByExtension().get(ext);
        log.info("Mimetype : "+mimeType);

        ContentWriter contentWriter = contentService.getWriter(newNode, ContentModel.PROP_CONTENT, true);
        contentWriter.setEncoding("UTF-8");
        contentWriter.setMimetype(mimeType);
        
        InputStream is = null;
        if (obj instanceof File) {
        	is = new FileInputStream((File)obj);
        } else {
            is = (InputStream)obj;
        }
        
        contentWriter.putContent(is);
        
        if (desc!=null) {
        	nodeService.setProperty(newNode, ContentModel.PROP_DESCRIPTION, desc);
        }

        return newNode;
    }
    
    public NodeRef createDoc(NodeRef parentFolder, NodeRef oldNodeRef, Object file, String ecmFileName, String desc, String summary) throws Exception {
//		byte[] fileContent = IOUtils.toByteArray(new FileInputStream(file));
//		
//		InputStream inputStream = new ByteArrayInputStream(fileContent);
    	NodeRef newNode = oldNodeRef;
    	if (newNode==null) {
			FileInfo fileInfo = fileFolderService.create(parentFolder, ecmFileName, ContentModel.TYPE_CONTENT);
		    newNode = fileInfo.getNodeRef();
    	}
	    
	    String ext = null;
        InputStream in = null;
        if (file instanceof File) {
        	in = new FileInputStream((File)file);
    	    ext = FilenameUtils.getExtension(((File)file).getName());
        } else {
        	in = (InputStream)file;
    	    ext = FilenameUtils.getExtension(ecmFileName);
        }
    
	    String mimeType = mimeTypeService.getMimetypesByExtension().get(ext);
        log.info("Mimetype : "+mimeType);

        ContentWriter contentWriter = contentService.getWriter(newNode, ContentModel.PROP_CONTENT, true);
        contentWriter.setEncoding("UTF-8");
        contentWriter.setMimetype(mimeType);
        
        contentWriter.putContent(in);
        
        if (desc!=null) {
        	nodeService.setProperty(newNode, ContentModel.PROP_DESCRIPTION, desc);
        }

        if (summary!=null) {
        	nodeService.setProperty(newNode, QName.createQName(NamespaceService.CONTENT_MODEL_PREFIX, "summary"), summary);
        }
        
        return newNode;
    }
    
    public NodeRef createLink(NodeRef parentFolder, NodeRef docRef, String desc) throws Exception {
    	
		Map<QName, Serializable> props = new HashMap<QName, Serializable>(2, 1.0f);
//		String targetNewName = desc + ".url";
		String targetNewName = desc;
		
		String docDesc = (String)nodeService.getProperty(docRef, ContentModel.PROP_DESCRIPTION);
		
//		Map<QName,Serializable> propss = nodeService.getProperties(docRef);
//		for(Entry e : propss.entrySet()) {
//			log.info("docRef -- "+e.getKey()+":"+e.getValue());
//		}
//		
//		Map<QName, Serializable> allNodeProps = nodeService.getProperties(docRef);
//		Map<QName, PropertyDefinition> aspectPropDefs = dictionaryService.getAspect(ContentModel.ASPECT_TITLED).getProperties(); // including inherited props
//		Map<QName, Serializable> nodeProps = new HashMap<QName, Serializable>(aspectPropDefs.size());
//		for (QName propQName : aspectPropDefs.keySet())
//		{
//		    Serializable value = allNodeProps.get(propQName);
//		    if (value != null)
//		    {
//		        log.info("aspect:"+propQName+":"+value);
//		    }
//		}
		
		// common properties
		props.put(ContentModel.PROP_NAME, targetNewName);
		props.put(ContentModel.PROP_LINK_DESTINATION, docRef);
		props.put(ContentModel.PROP_DESCRIPTION, docDesc);
    	
		ChildAssociationRef childRef = nodeService.createNode(parentFolder, ContentModel.ASSOC_CONTAINS,
				QName.createQName(NamespaceService.CONTENT_MODEL_1_0_URI, targetNewName),
				ApplicationModel.TYPE_FILELINK, props);

		// apply the titled aspect - title and description
		
		Map<QName, Serializable> titledProps = new HashMap<QName, Serializable>(2, 1.0f);
		titledProps.put(ContentModel.PROP_TITLE, docDesc);
		titledProps.put(ContentModel.PROP_DESCRIPTION, docDesc);
		
		nodeService.addAspect(childRef.getChildRef(), ContentModel.ASPECT_TITLED, titledProps);

		NodeRef newNode = childRef.getChildRef();
		
//        if (docDesc!=null) {
//        	nodeService.setProperty(newNode, ContentModel.PROP_DESCRIPTION, docDesc);
//        }

        return newNode;
    }
    
    
    public void updateDoc(NodeRef docRef, Object obj) throws Exception {
    	
        ContentWriter contentWriter = contentService.getWriter(docRef, ContentModel.PROP_CONTENT, true);
        InputStream is = null;
        
        if (obj instanceof File) {
        	String fileName = ((File)obj).getName();
        	
    	    String ext = FilenameUtils.getExtension(fileName);
    	    String mimeType = mimeTypeService.getMimetypesByExtension().get(ext);
            log.info("Mimetype : "+mimeType);

            contentWriter.setEncoding("UTF-8");
            contentWriter.setMimetype(mimeType);
//        } else {
//            throw new Exception("Invalid Parameter obj:"+obj);
        	is = new FileInputStream((File)obj);
        } else {
            is = (InputStream)obj;
        }
        
        contentWriter.putContent(is);
    }
    
    public String getFolderPath(NodeRef folderRef) throws Exception {
    	Path path = nodeService.getPath(folderRef);
    	String parentPath = path.toDisplayPath(nodeService, permissionService);
    	log.info("parentPath:"+parentPath);
    	
    	int pos = parentPath.indexOf("/", 1);
    	
    	FileInfo fileInfo = fileFolderService.getFileInfo(folderRef);
    	
    	String fullPath = parentPath.substring(pos) + "/" + fileInfo.getName();
    	
    	return URLEncoder.encode(fullPath, StandardCharsets.UTF_8.toString()); 
    }
    
    public void deleteFileFolder(String nodeRefStr) {
		
    	if (nodeRefStr!=null && !nodeRefStr.equals("")) {
	    	final NodeRef nodeRef = new NodeRef(nodeRefStr);
			AuthenticationUtil.runAs(new RunAsWork<String>()
		    {
				public String doWork() throws Exception
				{
					fileFolderService.delete(nodeRef);
					return null;
				}
		    }, AuthenticationUtil.getAdminUserName());
    	}
	}
    
	public NodeRef getWorkingCopyNodeRef(String srcNodeRef){
		log.info("srcNodeRef="+srcNodeRef);
		Map<QName,Serializable> propss = null;
//		Map<QName,Serializable> propss = nodeService.getProperties(new NodeRef(srcNodeRef));
//		for(Entry<QName, Serializable> e:propss.entrySet()) {
//			log.info("  src : "+e.getValue()+"  :  "+e.getKey());
//		}
		
//		List<ChildAssociationRef> nodes = nodeService.getChildAssocs(new NodeRef(srcNodeRef));
//		for (ChildAssociationRef element : nodes) {
//			log.info("  asso : "+element.getChildRef());
//			propss = nodeService.getProperties(element.getChildRef());
//			for(Entry<QName, Serializable> e:propss.entrySet()) {
//				log.info("         "+e.getValue()+"  :  "+e.getKey());
//			}
//		}
//		
//		log.info("****** source asso ****");
//		List<AssociationRef> assos = nodeService.getSourceAssocs(new NodeRef(srcNodeRef), ContentModel.ASSOC_WORKING_COPY_LINK);
//		for (AssociationRef element : assos) {
//			log.info("  t asso s : "+element.getSourceRef());
//			propss = nodeService.getProperties(element.getSourceRef());
//			for(Entry<QName, Serializable> e:propss.entrySet()) {
//				log.info("         "+e.getValue()+"  :  "+e.getKey());
//			}
//			log.info("  t asso t : "+element.getTargetRef());
//			propss = nodeService.getProperties(element.getTargetRef());
//			for(Entry<QName, Serializable> e:propss.entrySet()) {
//				log.info("         "+e.getValue()+"  :  "+e.getKey());
//			}
//		}
//		
		NodeRef nodeRef = null;
		
		log.info("****** target asso ****");
		List<AssociationRef> assos = nodeService.getTargetAssocs(new NodeRef(srcNodeRef), ContentModel.ASSOC_WORKING_COPY_LINK);
		for (AssociationRef element : assos) {
			log.info("  t asso s : "+element.getSourceRef());
			propss = nodeService.getProperties(element.getSourceRef());
			for(Entry<QName, Serializable> e:propss.entrySet()) {
				log.info("         "+e.getValue()+"  :  "+e.getKey());
			}
			log.info("  t asso t : "+element.getTargetRef());
			propss = nodeService.getProperties(element.getTargetRef());
			for(Entry<QName, Serializable> e:propss.entrySet()) {
				log.info("         "+e.getValue()+"  :  "+e.getKey());
			}
			nodeRef = element.getTargetRef();
		}
		
		return nodeRef;
	}
	
	public String getDocExt(String name) {
		int pos = name.lastIndexOf(".");
		return name.substring(pos+1);
	}
	
	public void downloadContent(NodeRef nodeRef, String fileName) throws Exception {
		ContentReader contentReader = contentService.getReader(nodeRef, ContentModel.PROP_CONTENT);
	    InputStream is = contentReader.getContentInputStream();

		BufferedInputStream in = null;
        FileOutputStream fout = null;
        try
        {
            in = new BufferedInputStream(is);
            fout = new FileOutputStream(fileName, false);

            byte data[] = new byte[1024];
            int count;
            while ((count = in.read(data, 0, 1024)) != -1)
            {
                fout.write(data, 0, count);
            }
        }
        catch (Exception ex) {
        	log.error("",ex);
        }
        finally
        {
            if (in != null)
                in.close();
            if (fout != null)
                fout.close();
        }
	}
	
	public void convertOfficeToPdf(String fileName, String pdfPath) {
		String oooExe = CommonUtil.getGlobalProperty("ooo.exe");
		
		String[] cmd = {oooExe, "--headless", "--convert-to", "pdf", "--outdir", pdfPath, fileName};
		try {
			Process process = Runtime.getRuntime().exec(cmd);
			process.waitFor();
			
		} catch (Exception ex) {
			ex.printStackTrace(System.out);
		}
	}

	public String getVersionLabel(NodeRef nodeRef) {
		final Version version = versionService.getCurrentVersion(nodeRef);
		return AuthenticationUtil.runAs(new RunAsWork<String>()
			    {
					public String doWork() throws Exception
					{
						return version!=null ? version.getVersionLabel() : null;
					}
			    }, AuthenticationUtil.getAdminUserName());
	}
	
	public void cancelCheckout(NodeRef docRef) {
    	log.info("  is checked out:"+docRef.toString());
	    if (checkOutCheckInService.isCheckedOut(docRef)) {
	    	log.info("    true");
	    	final NodeRef wNodeRef = getWorkingCopyNodeRef(docRef.toString());
	    	log.info("    cancel check out:"+wNodeRef);
			AuthenticationUtil.runAs(new RunAsWork<String>()
			{
				public String doWork() throws Exception
				{

			    	checkOutCheckInService.cancelCheckout(wNodeRef);
					return null;
				}
			}, AuthenticationUtil.getAdminUserName());
	    }
	    else {
	    	log.info("    false");
	    }
	}
	
	public List<FileModel> listFile(final NodeRef folderNodeRef, final String lang) throws Exception {

		List<FileModel> fileList = AuthenticationUtil.runAs(new RunAsWork<List<FileModel>>()
	    {
			public List<FileModel> doWork() throws Exception
			{
				List<FileModel> files = new ArrayList<FileModel>();
				
		    	Set<QName> qnames = new HashSet<QName>();
		    	qnames.add(ContentModel.TYPE_CONTENT);
		    	qnames.add(ApplicationModel.TYPE_FILELINK);
		    	List<ChildAssociationRef> docs = nodeService.getChildAssocs(folderNodeRef, qnames);
		    	for(ChildAssociationRef doc : docs) {
		    		log.info("doc:"+doc.toString());
		    		log.info("   childRef:"+doc.getChildRef().toString());
		    		log.info("   qname:"+doc.getQName().getLocalName());
		    		
		    		Map<QName,Serializable> props = nodeService.getProperties(doc.getChildRef());
//		    		for(Entry e : props.entrySet()) {
//		    			log.info("-- " + e.getKey()+":"+e.getValue());
//		    		}
		    		
	    			FileModel fileModel = new FileModel();
	    			fileModel.setName(doc.getQName().getLocalName());
	    			String desc = (String)nodeService.getProperty(doc.getChildRef(), ContentModel.PROP_DESCRIPTION);
	    			fileModel.setDesc(desc);
	    			fileModel.setNodeRef(doc.getChildRef().toString());
	    			fileModel.setAction("V");
	    			String by = (String)nodeService.getProperty(doc.getChildRef(), ContentModel.PROP_CREATOR);
	    			Map<String, Object> emp = adminHrEmployeeService.getWithDtl(by, null);
	    			String lang_suffix = (lang!=null && lang.startsWith("th") ? "_th" : "");
	    			fileModel.setBy(emp!=null ? (String)emp.get("first_name"+lang_suffix) : "");
	    			Date time = (Date)nodeService.getProperty(doc.getChildRef(), ContentModel.PROP_CREATED);
	    			fileModel.setTime(CommonDateTimeUtil.convertToGridDateTime(new Timestamp(time.getTime())));
	    			
		    		NodeRef linkDest = (NodeRef)nodeService.getProperty(doc.getChildRef(), ContentModel.PROP_LINK_DESTINATION);		    		
		    		if (linkDest!=null) {
		    			fileModel.setNodeRef(linkDest.toString());
		    		}
	    			
	    			files.add(fileModel);
		    	}
		    	return files;
			}
	    }, AuthenticationUtil.getAdminUserName());
		
		return fileList;
	}
}
