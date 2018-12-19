package pb.repo.exp.wscript;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.alfresco.model.ContentModel;
import org.alfresco.service.cmr.repository.NodeRef;
import org.alfresco.service.cmr.repository.NodeService;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.extensions.webscripts.WebScriptRequest;
import org.springframework.extensions.webscripts.WebScriptResponse;
import org.springframework.extensions.webscripts.WebScriptSession;
import org.springframework.extensions.webscripts.servlet.FormData;
import org.springframework.extensions.webscripts.servlet.FormData.FormField;
import org.springframework.stereotype.Component;

import pb.common.constant.CommonConstant;
import pb.common.model.FileModel;
import pb.common.util.CommonUtil;
import pb.common.util.FileUtil;
import pb.common.util.FolderUtil;

import com.github.dynamicextensionsalfresco.webscripts.annotations.HttpMethod;
import com.github.dynamicextensionsalfresco.webscripts.annotations.RequestParam;
import com.github.dynamicextensionsalfresco.webscripts.annotations.Uri;
import com.github.dynamicextensionsalfresco.webscripts.annotations.WebScript;

@Component
@WebScript
public class ExpFileWebScript {
	
	private static Logger log = Logger.getLogger(ExpFileWebScript.class);
	
	private static final String URI_PREFIX = CommonConstant.GLOBAL_URI_PREFIX + "/exp/file";
	
	@Autowired
	NodeService nodeService;

  @Uri(method=HttpMethod.POST, value=URI_PREFIX+"/upload")
  public void handleUpload(final WebScriptRequest request, final WebScriptResponse response)  throws Exception {
    
	String json = null;

	try {
		log.info("/upload");
		
		WebScriptSession session = request.getRuntime().getSession();
		
		String uuid =  null;
		
		if (session.getValue(CommonConstant.SESSION_PAGE_UUID)!=null) {
			uuid = (String) session.getValue(CommonConstant.SESSION_PAGE_UUID); 
		} else {
			uuid = UUID.randomUUID().toString();
			session.setValue(CommonConstant.SESSION_PAGE_UUID, uuid);
		}
		 
	    FormData fd = (FormData) request.parseContent();

        FormField[] fields = fd.getFields();
        
        String sep = File.separator;
        
        String desc = null;
        
		String path = "alf_"+uuid.toString();
		
        String fullPath = FolderUtil.getTmpDir() + sep + path;
		log.info(fullPath);
		new File(fullPath).mkdirs();
		
		List<FileModel> files = new ArrayList<FileModel>();
        
        for (FormField field : fields) {
        	if (field.getIsFile()) {
        		String name = field.getFilename();
				log.info(" - "+name);
        			
				byte[] fileContent = IOUtils.toByteArray(field.getInputStream());
				
				OutputStream out = new FileOutputStream(fullPath + sep + name);
				out.write(fileContent);
				out.close();
				
				FileModel fileModel = new FileModel();
				fileModel.setName(name);
				fileModel.setDesc(desc);
				fileModel.setPath(path);
				files.add(fileModel);
        	}
        	else {
        		if (field.getName().equals("desc")) {
        			desc = field.getValue();
        		}
        	}
        }
		 
		json = FileUtil.jsonSuccess(files);
		
	} catch (Exception ex) {
		log.error("", ex);
		json = CommonUtil.jsonFail(ex.toString());
		throw ex;
		
	} finally {
		CommonUtil.responseWriteHtml(response, json);
	}
    
  }
  
  @Uri(method=HttpMethod.POST, value=URI_PREFIX+"/delete")
  public void handleDelete(@RequestParam(required=false) final String name
		  				,@RequestParam final String path
		  				,final WebScriptResponse response) throws Exception {
	  
	log.info("-----handleDelete("+path+"/"+name+")-----");
	String json = null;
	
	try {
		String sep = File.separator;

		String fullName = FolderUtil.getTmpDir() + sep + path + sep + name;
		log.info(fullName);
		File file = new File(fullName);
		file.delete();
		
		json = CommonUtil.jsonSuccess();
		
	} catch (Exception ex) {
		log.error("", ex);
		json = CommonUtil.jsonFail(ex.toString());
		throw ex;
	} finally {
		CommonUtil.responseWrite(response, json);
	}
	  
  }
  
  @Uri(method=HttpMethod.POST, value=URI_PREFIX+"/edit")
  public void handleEdit(@RequestParam(required=false) final String name
		  				,@RequestParam final String path
		  				,@RequestParam final String nodeRef
		  				,@RequestParam final String desc
		  				,final WebScriptResponse response) throws Exception {
	  
	log.info("-----ExpFileWebScript.handleEdit("+path+"/"+name+"/"+desc+")-----");
	String json = null;
	
	try {
		if (nodeRef!=null && !nodeRef.equals("")) {
			nodeService.setProperty(new NodeRef(nodeRef), ContentModel.PROP_DESCRIPTION, desc);
		}
		
		json = CommonUtil.jsonSuccess();
		
	} catch (Exception ex) {
		log.error("", ex);
		json = CommonUtil.jsonFail(ex.toString());
		throw ex;
	} finally {
		CommonUtil.responseWrite(response, json);
	}
	  
  }
  

}
