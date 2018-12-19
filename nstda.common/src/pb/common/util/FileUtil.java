package pb.common.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.DatatypeConverter;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.util.FileCopyUtils;

import pb.common.constant.FileConstant;
import pb.common.constant.JsonConstant;
import pb.common.model.FileModel;

public class FileUtil {
	
	public static JSONObject convertToJSONObject(FileModel model) throws Exception {
		if (model == null) {
			return null;
		}
		JSONObject jsObj = new JSONObject();
		
		jsObj.put(FileConstant.JFN_NAME, model.getName());
		jsObj.put(FileConstant.JFN_DESC, model.getDesc());
		jsObj.put(FileConstant.JFN_PATH, model.getPath());
		jsObj.put(FileConstant.JFN_NODE_REF, model.getNodeRef());
		jsObj.put(FileConstant.JFN_ACTION, "ED");
		
		return jsObj;
	}
	
	public static JSONArray convertToJSONArray(List<FileModel> inList) throws Exception {
		
		List<JSONObject> outList = new ArrayList<JSONObject>();
		
		JSONObject jsObj = null;
		for(FileModel model : inList) {
			jsObj = convertToJSONObject(model);
			
			outList.add(jsObj);
		}
		
		return new JSONArray(outList);
	}
	
	
	public static String jsonSuccess(List<FileModel> list) throws Exception {
		
		JSONObject jsObj = new JSONObject();

		jsObj.put(JsonConstant.SUCCESS,  true);
		jsObj.put(JsonConstant.TOTAL,  list.size());
		jsObj.put(JsonConstant.DATA, convertToJSONArray(list));
		
		return jsObj.toString();
	}
	
	public InputStream base64InputStream(String base64String)throws IOException {
	    if (base64String!=null) {
	        return new ByteArrayInputStream(DatatypeConverter.parseBase64Binary(base64String));
	    }

	    return null;
	}
	
	public String inputStreamToBase64(InputStream inputStream) throws IOException{
	    if (inputStream!=null) {
	        ByteArrayOutputStream output = new ByteArrayOutputStream();
	        FileCopyUtils.copy(inputStream, output);
	        //TODO retrieve content type from file, & replace png below with it
	        return "data:image/png;base64," + DatatypeConverter.printBase64Binary(output.toByteArray());
	    }

	    return null;
	}
	
	public static void validateFileName(String fileName) throws Exception {
		if (fileName!=null) {
//			if (fileName.matches("(.*[\"\*\\\>\<\?\/\:\|]+.*)|(.*[\.]?.*[\.]+$)|(.*[ ]+$)")) {
			if (fileName.matches("(.*[\\\"\\*\\\\\\>\\<\\?\\/\\:\\|]+.*)|(.*[\\.]?.*[\\.]+$)|(.*[ ]+$)")) {
				throw new Exception("Invalid File Name (Invalid characters:\" * < > ? / : |):"+fileName);
			}
		}
	}

}
