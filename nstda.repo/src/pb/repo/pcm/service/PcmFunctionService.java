package pb.repo.pcm.service;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRMapCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;

import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pb.repo.admin.constant.MainMasterConstant;
import pb.repo.admin.service.AdminMasterService;
import pb.repo.pcm.dao.PcmFunctionDAO;
import pb.repo.pcm.util.PcmUtil;

@Service
public class PcmFunctionService {

	private static Logger log = Logger.getLogger(PcmFunctionService.class);
	
	@Autowired
	DataSource dataSource;
	
	@Autowired
	AdminMasterService masterService;

	
	public List<Map<String,Object>> listType(Map<String, Object> params) throws Exception {
		
		List<Map<String,Object>> list = null;
		
		SqlSession session = PcmUtil.openSession(dataSource);
		
        try {
        	
        	PcmFunctionDAO memoFunctionDAO = session.getMapper(PcmFunctionDAO.class);
          
    		String data = memoFunctionDAO.listType(params);
    		list = jsonToMap(data);
    		
        } catch (Exception ex) {
			log.error("", ex);
        	throw ex;
        } finally {
        	session.close();
        }
        
        return list;
	}
	
	public List<Map<String,Object>> listTeam(Map<String, Object> params) throws Exception {
		
		List<Map<String,Object>> list = null;
		
		SqlSession session = PcmUtil.openSession(dataSource);
		
        try {
        	
        	PcmFunctionDAO memoFunctionDAO = session.getMapper(PcmFunctionDAO.class);
          
    		String data = memoFunctionDAO.listTeam(params);
    		list = jsonToMap(data);
    		
        } catch (Exception ex) {
			log.error("", ex);
        	throw ex;
        } finally {
        	session.close();
        }
        
        return list;
	}
	
	public List<Map<String,Object>> listLevel(Map<String, Object> params) throws Exception {
		
		List<Map<String,Object>> list = null;
		
		SqlSession session = PcmUtil.openSession(dataSource);
		
        try {
        	
        	PcmFunctionDAO memoFunctionDAO = session.getMapper(PcmFunctionDAO.class);
          
    		String data = memoFunctionDAO.listLevel(params);
    		list = jsonToMap(data);
    		
        } catch (Exception ex) {
			log.error("", ex);
        	throw ex;
        } finally {
        	session.close();
        }
        
        return list;
	}
	
	public List<Map<String,Object>> listHour(Map<String, Object> params) throws Exception {
		
		List<Map<String,Object>> list = null;
		
		SqlSession session = PcmUtil.openSession(dataSource);
		
        try {
        	
        	PcmFunctionDAO memoFunctionDAO = session.getMapper(PcmFunctionDAO.class);
          
    		String data = memoFunctionDAO.listHour(params);
    		list = jsonToMap(data);
    		
        } catch (Exception ex) {
			log.error("", ex);
        	throw ex;
        } finally {
        	session.close();
        }
        
        return list;
	}
	
	public List<Map<String,Object>> listTypeChange(Map<String, Object> params) throws Exception {
		
		List<Map<String,Object>> list = null;
		
		SqlSession session = PcmUtil.openSession(dataSource);
		
        try {
        	
        	PcmFunctionDAO memoFunctionDAO = session.getMapper(PcmFunctionDAO.class);
          
    		String data = memoFunctionDAO.listTypeChange(params);
    		list = jsonToMap(data);
    		
        } catch (Exception ex) {
			log.error("", ex);
        	throw ex;
        } finally {
        	session.close();
        }
        
        return list;
	}
	
	public List<Map<String,Object>> listRequestDay(Map<String, Object> params) throws Exception {
		
		List<Map<String,Object>> list = null;
		
		SqlSession session = PcmUtil.openSession(dataSource);
		
        try {
        	
        	PcmFunctionDAO memoFunctionDAO = session.getMapper(PcmFunctionDAO.class);
          
    		String data = memoFunctionDAO.listRequestDay(params);
    		list = jsonToMap(data);
    		
        } catch (Exception ex) {
			log.error("", ex);
        	throw ex;
        } finally {
        	session.close();
        }
        
        return list;
	}
	
	public List<Map<String,Object>> listRequestEffect(Map<String, Object> params) throws Exception {
		
		List<Map<String,Object>> list = null;
		
		SqlSession session = PcmUtil.openSession(dataSource);
		
        try {
        	
        	PcmFunctionDAO memoFunctionDAO = session.getMapper(PcmFunctionDAO.class);
          
    		String data = memoFunctionDAO.listRequestEffect(params);
    		list = jsonToMap(data);
    		
        } catch (Exception ex) {
			log.error("", ex);
        	throw ex;
        } finally {
        	session.close();
        }
        
        return list;
	}
	
	public List<Map<String,Object>> listChange(Map<String, Object> params) throws Exception {
		
		List<Map<String,Object>> list = null;
		
		SqlSession session = PcmUtil.openSession(dataSource);
		
        try {
        	
        	PcmFunctionDAO memoFunctionDAO = session.getMapper(PcmFunctionDAO.class);
          
    		String data = memoFunctionDAO.listChange(params);
    		list = jsonToMap(data);
    		
        } catch (Exception ex) {
			log.error("", ex);
        	throw ex;
        } finally {
        	session.close();
        }
        
        return list;
	}
	
	public List<Map<String,Object>> listTrain(Map<String, Object> params) throws Exception {
		
		List<Map<String,Object>> list = null;
		
		SqlSession session = PcmUtil.openSession(dataSource);
		
        try {
        	
        	PcmFunctionDAO memoFunctionDAO = session.getMapper(PcmFunctionDAO.class);
          
    		String data = memoFunctionDAO.listTrain(params);
    		list = jsonToMap(data);
    		
        } catch (Exception ex) {
			log.error("", ex);
        	throw ex;
        } finally {
        	session.close();
        }
        
        return list;
	}
	
	public JasperPrint genReport(List<Map<String,Object>> list, String name, String from, String to, String type, String typeName, String criteria, String fileName) throws Exception {
		
		List<Map<String, Object>> rptConfigList = masterService.listByType(MainMasterConstant.TYPE_REPORT, type , true, null, null, null);
		Map<String, Object> rptConfigModel = rptConfigList.get(0); 
		
		 final String basePath = System.getProperty("catalina.base");
		  String rptName = basePath+"/webapps/alfresco/report/"+name;
		  String logoPath = basePath+"/webapps/alfresco/images/rpt/logo.png";
		  Map<String, Object> map = new HashMap<>();
		  List<Map<String, Object>> listData = new ArrayList<Map<String,Object>>();
		  map.put("data", list);
		  map.put("fromDate", from);
		  map.put("toDate", to);
		  map.put("type", type);
		  map.put("typeName", rptConfigModel.get(MainMasterConstant.TFN_FLAG2));
		  map.put("criteria", criteria);
		  listData.add(map);
		  
		  Map parameters = new HashMap();
		  //System.out.println("BaseDir"+reportFile.getParentFile());
		  parameters.put("logo", logoPath);
			
		  Collection<Map<String, ?>> collection = new ArrayList<Map<String,?>>(listData);
		  JRMapCollectionDataSource data = new JRMapCollectionDataSource(collection);
		  
		  JasperCompileManager.compileReportToFile(rptName+".jrxml");
			File reportFile = new File(rptName+".jasper");
			if (!reportFile.exists())
				throw new JRRuntimeException("File WebappReport.jasper not found. The report design must be compiled first.");
			
			
			JasperReport jasperReport = (JasperReport)JRLoader.loadObjectFromFile(reportFile.getPath());
			JasperPrint jasperPrint = 
					JasperFillManager.fillReport(
						jasperReport, 
						parameters, 
						data
						);	
			
			
			
			return jasperPrint;
		
	}
	
	public static void main(String args[]) throws JSONException {
		//System.out.println(jsonToMap("[{\"a\":\"ss\"},{\"a\":\"ss\"}]"));
	}
	
	public static List<Map<String,Object>> jsonToMap(String data) throws JSONException {
	
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();

		if(data!=null) {
			Map<String,Object> map = null;
			JSONArray a = new JSONArray(data);
			for(int i=0; i<a.length();i++) {
				JSONObject row = new JSONObject(a.get(i).toString());
				Iterator<String> keys = row.keys();
				map = new HashMap<String, Object>();
				while(keys.hasNext()) {
					String key = keys.next();
					map.put(key, row.get(key));
				}
				list.add(map);
			}
		}

		
		return list;
		
	}
	
}
