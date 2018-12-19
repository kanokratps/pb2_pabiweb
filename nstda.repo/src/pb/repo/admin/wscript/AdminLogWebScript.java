package pb.repo.admin.wscript;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.text.DecimalFormat;

import org.alfresco.repo.security.authority.UnknownAuthorityException;
import org.alfresco.service.cmr.security.AuthorityService;
import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.extensions.webscripts.WebScriptResponse;
import org.springframework.stereotype.Component;

import pb.common.constant.CommonConstant;
import pb.common.util.CommonUtil;

import com.github.dynamicextensionsalfresco.webscripts.annotations.Authentication;
import com.github.dynamicextensionsalfresco.webscripts.annotations.AuthenticationType;
import com.github.dynamicextensionsalfresco.webscripts.annotations.HttpMethod;
import com.github.dynamicextensionsalfresco.webscripts.annotations.RequestParam;
import com.github.dynamicextensionsalfresco.webscripts.annotations.Uri;
import com.github.dynamicextensionsalfresco.webscripts.annotations.WebScript;

@Component
@WebScript
@Authentication(value=AuthenticationType.ADMIN)
public class AdminLogWebScript {
	
	private static Logger log = Logger.getLogger(AdminLogWebScript.class);
	
	private static final String URI_PREFIX = CommonConstant.GLOBAL_URI_PREFIX + "/admin/log";
	private static final String FILE_NAME = "/opt/alfresco/tomcat/logs/catalina.out";
	
	@Autowired
	private AuthorityService authorityService;
	
	  @Uri(method=HttpMethod.POST, value=URI_PREFIX+"/range")
	  public void handleRange(@RequestParam(required=false) Integer from,
			  				 @RequestParam(required=false) Integer to,
			  			final WebScriptResponse response)  throws Exception {
	    
		  	JSONObject jobj = new JSONObject();
		  	String json = null;
		  	
			try {
				
				StringBuffer msg = new StringBuffer();
				
				FileInputStream fs= new FileInputStream(FILE_NAME);
				BufferedReader br = new BufferedReader(new InputStreamReader(fs));

				int i = 0;
				for(; i < from-1; ++i)
				  br.readLine();
				String line = null;
				while((line = br.readLine())!=null && i<to) {
					msg.append(line+"\n");
					i++;
				}
		  	
				jobj.put("message", msg);
			  	jobj.put("success", true);
			  	jobj.put("info", getInfo());
			
				json = jobj.toString();
				
			} catch (Exception ex) {
				log.error("", ex);
				json = CommonUtil.jsonFail(ex.toString());
				throw ex;
				
			} finally {
				CommonUtil.responseWrite(response, json);
			}
	    
	  }
	

  @Uri(method=HttpMethod.POST, value=URI_PREFIX+"/last")
  public void handleLast(@RequestParam(required=false) Integer line,
		  			final WebScriptResponse response)  throws Exception {
    
	  	JSONObject jobj = new JSONObject();
	  	String json = null;
	  	
		try {
			
			File f = new File(FILE_NAME);
			
			String msg = tail(f, line);
	  	
			jobj.put("message", msg);
		  	jobj.put("success", true);
		  	jobj.put("info", getInfo());
		
			json = jobj.toString();
			
		} catch (Exception ex) {
			log.error("", ex);
			json = CommonUtil.jsonFail(ex.toString());
			throw ex;
			
		} finally {
			CommonUtil.responseWrite(response, json);
		}
    
  }
	
	public String tail( File file, int lines) {
	    java.io.RandomAccessFile fileHandler = null;
	    try {
	        fileHandler = 
	            new java.io.RandomAccessFile( file, "r" );
	        long fileLength = fileHandler.length() - 1;
	        StringBuilder sb = new StringBuilder();
	        int line = 0;

	        for(long filePointer = fileLength; filePointer != -1; filePointer--){
	            fileHandler.seek( filePointer );
	            int readByte = fileHandler.readByte();

	             if( readByte == 0xA ) {
	                if (filePointer < fileLength) {
	                    line = line + 1;
	                }
	            } else if( readByte == 0xD ) {
	                if (filePointer < fileLength-1) {
	                    line = line + 1;
	                }
	            }
	            if (line >= lines) {
	                break;
	            }
	            sb.append( ( char ) readByte );
	        }

	        String lastLine = sb.reverse().toString();
	        return lastLine;
	    } catch( java.io.FileNotFoundException e ) {
	        e.printStackTrace();
	        return null;
	    } catch( java.io.IOException e ) {
	        e.printStackTrace();
	        return null;
	    }
	    finally {
	        if (fileHandler != null )
	            try {
	                fileHandler.close();
	            } catch (IOException e) {
	            }
	    }
	}
  
	  @Uri(method=HttpMethod.POST, value=URI_PREFIX+"/info")
	  public void handleInfo(final WebScriptResponse response)  throws Exception {
	    
		  	JSONObject jobj = new JSONObject();
		  	String json = null;
		  	
			try {
				String msg = getInfo();
		  	
				jobj.put("message", msg);
			  	jobj.put("success", true);
			
				json = jobj.toString();
				
			} catch (Exception ex) {
				log.error("", ex);
				json = CommonUtil.jsonFail(ex.toString());
				throw ex;
				
			} finally {
				CommonUtil.responseWrite(response, json);
			}
	    
	  }
	  
	  private String getInfo() throws Exception {
			DecimalFormat df = new DecimalFormat("#,##0");
			
			File f = new File(FILE_NAME);
			
			FileReader       input = new FileReader(f);
			LineNumberReader count = new LineNumberReader(input);
			
			while (count.skip(Long.MAX_VALUE) > 0)
		    {
		      // Loop just in case the file is > Long.MAX_VALUE or skip() decides to not read the entire file
		    }
			
			String msg = "Size:"+df.format(f.length())+"  Line Count:"+df.format(count.getLineNumber()+1);

			return msg;
	  }
	
}
