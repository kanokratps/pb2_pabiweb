package pb.common.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.alfresco.model.ContentModel;
import org.alfresco.service.cmr.model.FileFolderService;
import org.alfresco.service.cmr.model.FileInfo;
import org.alfresco.service.cmr.repository.NodeRef;
import org.alfresco.service.cmr.repository.NodeService;
import org.alfresco.service.cmr.repository.StoreRef;
import org.alfresco.service.cmr.search.ResultSet;
import org.alfresco.service.cmr.search.SearchService;
import org.apache.log4j.Logger;

import pb.common.constant.AlfrescoConstant;
import pb.common.exception.FolderNoPermissionException;

public class FolderUtil {

	private static Logger log = Logger.getLogger(FolderUtil.class);

	protected NodeService nodeService;
    private FileFolderService fileFolderService;
    private SearchService searchService;
	
	public void setNodeService(NodeService nodeService) {this.nodeService = nodeService;}
	public void setFileFolderService(FileFolderService fileFolderService) {this.fileFolderService = fileFolderService;}
	public void setSearchService(SearchService searchService) {this.searchService = searchService;}
	
	public NodeRef getFolderNodeRefByXPath(String xPath){
		NodeRef nodeRef = null;
		StoreRef storeRef = new StoreRef(StoreRef.PROTOCOL_WORKSPACE, "SpacesStore");
		ResultSet rs = null;
		try {
			rs = searchService.query(storeRef, SearchService.LANGUAGE_XPATH, xPath);
			if(rs.length() > 0){
				for(int i = 0; i < rs.length(); i++){
					if(!rs.getNodeRef(i).getId().equalsIgnoreCase("missing")){
						return rs.getNodeRef(i); 
					}
				}
			}
		}
		catch (Throwable e) {
			System.err.println(e.getMessage());
		}
		finally {
		if (rs != null){
			  rs.close();
		  }
		}
		return nodeRef;
	}
	
	public NodeRef createFolderStructure(List<Object> paths, String site) throws Exception {
		
		NodeRef f = null;
		
		try {
			NodeRef documentLibary = getSiteDocLib(site);
			
			//NodeRef documentLibary = new NodeRef("workspace://SpacesStore/d564bfd4-ba77-4023-b686-44338b136a18");
			log.info("documentLibary :: " +documentLibary.toString());
			log.info("paths :: " +paths.toString());
			f = documentLibary;
			
			for(Object path : paths){
				
				if (path instanceof Map) {
					
					Map<String, Object> pathMap = (Map<String, Object>)path;
					
					NodeRef search = fileFolderService.searchSimple(f, (String)pathMap.get("name"));
					
					if(search == null){
					
						FileInfo folder = fileFolderService.create(f, (String)pathMap.get("name"), ContentModel.TYPE_FOLDER);
						f = folder.getNodeRef();
						
						String description = (String)pathMap.get("desc");
						if (description != null) {
							nodeService.setProperty(f, ContentModel.PROP_DESCRIPTION, description);
						}
						
					}else{
						
						f = search;
						
					}
				}
				else
				if(path instanceof String){
					NodeRef search = fileFolderService.searchSimple(f, (String)path);
					
					if(search == null){
					
						FileInfo folder = fileFolderService.create(f, (String)path, ContentModel.TYPE_FOLDER);
						f = folder.getNodeRef();
						
					}else{
						
						f = search;
						
					}
				}else if(path instanceof ArrayList<?>){
					
					FileInfo folder = null;
					NodeRef firstPath = null;
					int i = 0;
					for(Object subFolder : (ArrayList<?>)path){
						
						NodeRef search = fileFolderService.searchSimple(f, subFolder.toString());
						if(search == null){
							
							folder = fileFolderService.create(f, subFolder.toString(), ContentModel.TYPE_FOLDER);
						   
						}
						if(i==0 && search!=null){
							firstPath = search;
						}else if(i==0 && search==null){
							firstPath = folder.getNodeRef();
						}				
						
						i++;
					}
					f = firstPath;
					
				}
				
				
			}
		
		} catch (Exception ex) {
			String msg = "Not Found documentLibrary folder in site";
			if (ex.getMessage().startsWith(msg)) {
				String siteName = ex.getMessage().substring(msg.length()); 
				FolderNoPermissionException e = new FolderNoPermissionException("Please check Permission for the user");
				throw e;
			} else {
				throw ex;
			}
		}
		
		return f;
	}
	
	public String getSitePath(String site) {
		return "/app:company_home/st:sites/cm:"+site+"/cm:documentLibrary";
	}
	
	private NodeRef getSiteDocLib(String site) throws Exception {
		String docLibPath = getSitePath(site);
		
		log.info("docLibPath:"+docLibPath);
		
		NodeRef documentLibrary = getFolderNodeRefByXPath(docLibPath);

		if(documentLibrary == null){
			log.info("Cannot find documentLibrary folder in site '"+site+"'");
			throw new Exception("Not Found documentLibrary folder in site '"+site+"'");
		} else {
			log.info("Found documentLibrary folder in site '"+site+"'");
		}
		
		return documentLibrary;
	}
	
	public NodeRef getFolder(List<Object> paths, String site) throws Exception{
		
		NodeRef f = getSiteDocLib(site);
		for(Object path : paths){
			
			if(path instanceof String){
				NodeRef search = fileFolderService.searchSimple(f, (String)path);
				
				if(search == null){
				
					return null;
					
				}else{
					
					f = search;
					
				}
				
			}else if(path instanceof ArrayList<?>){
				
				NodeRef firstPath = null;
				int i = 0;
				for(Object subFolder : (ArrayList<?>)path){
					
					NodeRef search = fileFolderService.searchSimple(f, subFolder.toString());
					if(search == null){

						return null;
					   
					}
					if(i==0 && search!=null){
						firstPath = search;
					}
					
					i++;
				}
				f = firstPath;
			}
		}
		
		return f;
	}
	
	public NodeRef renameFolder(List<Object> paths, String newName, String site) throws Exception {
		NodeRef oldFolder = getFolder(paths, site);
		if(oldFolder != null) {
			fileFolderService.rename(oldFolder,  newName);
		} else {
			StringBuffer sb = new StringBuffer();
			for(Object folder:paths) {
				if (sb.length() > 0) {
					sb.append("/");
				}
				sb.append(folder);
			}
			log.info("Old Folder '"+sb.toString()+"' to rename is not exists!");
			throw new Exception("folder01");
		}
		return oldFolder;
	}
	
	public static String getValidFolderName(String oldName) {
		String newName = oldName;
		
		for(char c : AlfrescoConstant.INVALID_FOLDER_CHAR.toCharArray()) {
			newName = newName.replace(c, '_');
		}
		
		return newName;
	}

	public static String getTmpDir() {
		return System.getProperty("java.io.tmpdir");
	}
	
	public NodeRef createInBackupFolder(String startPath, String folderPrefix, String name) {;
		String backupPath = startPath+"/cm:Deleted"+(folderPrefix!=null ? "/cm:"+folderPrefix : "");
		NodeRef backupRef = getFolderNodeRefByXPath(backupPath);
		
		FileInfo folder = fileFolderService.create(backupRef, name, ContentModel.TYPE_FOLDER);
		return folder.getNodeRef();		
	}
	
	public void moveToDeletedFolder(String startPath, NodeRef folderRef, String folderPrefix) throws Exception {
		String backupPath = startPath+"/cm:Deleted"+(folderPrefix!=null ? "/cm:"+folderPrefix : "");
		
		log.info("backupPath:"+backupPath);
		
		NodeRef backupRef = getFolderNodeRefByXPath(backupPath);
		
		log.info("backupRef:"+backupRef);
		
		FileInfo folderInfo = fileFolderService.getFileInfo(folderRef);
		
		String newName = folderInfo.getName()+getBackupFileSuffix();
		
		log.info("newName:"+newName);
		
		fileFolderService.move(folderRef, backupRef, newName);
	
	}
	
	public String getBackupFileSuffix() {
		DateFormat df = new SimpleDateFormat("YYYYMMddHHmm");
		
		return "_"+df.format(Calendar.getInstance(Locale.US).getTime());
	}
}
