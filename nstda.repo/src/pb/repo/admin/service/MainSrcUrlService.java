package pb.repo.admin.service;

import static org.apache.ibatis.jdbc.SelectBuilder.BEGIN;
import static org.apache.ibatis.jdbc.SelectBuilder.FROM;
import static org.apache.ibatis.jdbc.SelectBuilder.ORDER_BY;
import static org.apache.ibatis.jdbc.SelectBuilder.SELECT;
import static org.apache.ibatis.jdbc.SelectBuilder.SQL;
import static org.apache.ibatis.jdbc.SelectBuilder.WHERE;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.sql.DataSource;

import org.alfresco.service.cmr.repository.NodeService;
import org.alfresco.service.cmr.security.AuthenticationService;
import org.alfresco.service.cmr.security.AuthorityService;
import org.alfresco.service.cmr.security.AuthorityType;
import org.alfresco.service.cmr.security.PersonService;
import org.apache.ibatis.builder.xml.dynamic.DynamicSqlSource;
import org.apache.ibatis.builder.xml.dynamic.TextSqlNode;
import org.apache.ibatis.jdbc.SqlRunner;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pb.common.constant.CommonConstant;
import pb.common.constant.JsonConstant;
import pb.common.util.PersonUtil;
import pb.repo.admin.constant.MainMasterConstant;
import pb.repo.admin.model.MainMasterModel;
import pb.repo.common.mybatis.DbConnectionFactory;

@Service
public class MainSrcUrlService {

	private static Logger log = Logger.getLogger(MainSrcUrlService.class);

	@Autowired
	DataSource dataSource;
	
	@Autowired
	AuthenticationService authService;
	
	@Autowired
	AdminUserGroupService userGroupService;
	
	@Autowired
	AdminMasterService masterService;
	
	@Autowired
	AuthorityService authorityService;
	
	@Autowired
	PersonService personService;
	
	@Autowired
	NodeService nodeService;
	
	@Autowired
	AdminHrEmployeeService employeeService;

	private String selectMainMasterSql(String cond, String orderBy) {
		BEGIN();
		
		StringBuffer fields = new StringBuffer();
		fields.append(MainMasterConstant.TFN_ID);
		fields.append(","+MainMasterConstant.TFN_TYPE);
		fields.append(","+MainMasterConstant.TFN_CODE);
		fields.append(","+MainMasterConstant.TFN_NAME);
		fields.append(","+MainMasterConstant.TFN_FLAG1);
		fields.append(","+MainMasterConstant.TFN_FLAG2);
		fields.append(","+MainMasterConstant.TFN_FLAG3);
		fields.append(","+MainMasterConstant.TFN_FLAG4);
		fields.append(","+MainMasterConstant.TFN_FLAG5);
		
		SELECT(fields.toString()
				+ ",COUNT(*) OVER() "+CommonConstant.TFN_TOTAL_ROW_COUNT
		);
		FROM(MainMasterConstant.TABLE_NAME);
		
		WHERE("IS_ACTIVE=true");
		WHERE(cond);
		
		if (orderBy == null) {
			orderBy = MainMasterConstant.TFN_NAME;
		}
		ORDER_BY(orderBy);
		
		return SQL();
	}
	
	public Map<String, Object> listMainMaster(String cond, String codeValue, String orderBy, Boolean all, String lang) throws Exception {
		
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		Connection conn = dataSource.getConnection();
		
        try {
        	
    		SqlRunner sqlRunner = new SqlRunner(conn);
    		TextSqlNode node = new TextSqlNode(selectMainMasterSql(cond, orderBy));
    		DynamicSqlSource s = new DynamicSqlSource(DbConnectionFactory.getSqlSessionFactory(dataSource).getConfiguration(), node);
    		BoundSql sql = s.getBoundSql(null);
    		
//    		log.info("SQL="+sql.getSql());
    		
    		List<Map<String,Object>> tmpList = sqlRunner.selectAll(sql.getSql());
    		
    		String allLbl = null;
    		String name = null;
    		if (lang!=null && lang.startsWith("th")) {
    			name = MainMasterConstant.TFN_NAME;
    			allLbl = "ทั้งหมด";
    		} else {
    			name = MainMasterConstant.TFN_FLAG2;
    			allLbl = "All";
    		}
//    		log.info("lang name:"+name);
    		
    		if (all!=null && all) {
    			map.put("", "== "+allLbl+" ==");
    		}
    		
    		String codeField = (codeValue!=null) && (codeValue.toLowerCase().equals("n")) ? MainMasterConstant.TFN_NAME : MainMasterConstant.TFN_CODE;
    		for(Map<String,Object> tmpMap : tmpList) {
	    		map.put((String)tmpMap.get(codeField), (String)tmpMap.get(name));
    		}

        } finally {
        	conn.close();
        }
        
        return map;
	}
	
	public Map<String, Object> listMainMaster3(String cond, String codeValue, String orderBy, Boolean all, String lang) throws Exception {
		
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		Connection conn = dataSource.getConnection();
		
        try {
        	
    		SqlRunner sqlRunner = new SqlRunner(conn);
    		TextSqlNode node = new TextSqlNode(selectMainMasterSql(cond, orderBy));
    		DynamicSqlSource s = new DynamicSqlSource(DbConnectionFactory.getSqlSessionFactory(dataSource).getConfiguration(), node);
    		BoundSql sql = s.getBoundSql(null);
    		
//    		log.info("SQL="+sql.getSql());
    		
    		List<Map<String,Object>> tmpList = sqlRunner.selectAll(sql.getSql());
    		
    		String allLbl = null;
    		String name = null;
    		String name2 = null;
    		if (lang!=null && lang.startsWith("th")) {
    			name = MainMasterConstant.TFN_NAME;
    			name2 = MainMasterConstant.TFN_FLAG4;
    			allLbl = "ทั้งหมด";
    		} else {
    			name = MainMasterConstant.TFN_FLAG2;
    			name2 = MainMasterConstant.TFN_FLAG5;
    			allLbl = "All";
    		}
//    		log.info("lang name:"+name);
    		
    		if (all!=null && all) {
    			map.put("", "== "+allLbl+" ==");
    		}
    		
    		String codeField = (codeValue!=null) && (codeValue.toLowerCase().equals("n")) ? MainMasterConstant.TFN_NAME : MainMasterConstant.TFN_CODE;
    		for(Map<String,Object> tmpMap : tmpList) {
    			StringBuffer nameVal = new StringBuffer(); 
    			nameVal.append((String)tmpMap.get(name));
    			if (tmpMap.get(name2)!=null && !tmpMap.get(name2).equals("")) {
    				nameVal.append(" / ");
    				nameVal.append(tmpMap.get(name2));
    			}
	    		map.put((String)tmpMap.get(codeField), nameVal.toString());
    		}

        } finally {
        	conn.close();
        }
        
        return map;
	}	
	
	public List<Map<String, Object>> listMainMaster2(String cond, String codeValue, String orderBy, Boolean all) throws Exception {
		
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Connection conn = dataSource.getConnection();
		
        try {
        	
    		SqlRunner sqlRunner = new SqlRunner(conn);
    		TextSqlNode node = new TextSqlNode(selectMainMasterSql(cond, orderBy));
    		DynamicSqlSource s = new DynamicSqlSource(DbConnectionFactory.getSqlSessionFactory(dataSource).getConfiguration(), node);
    		BoundSql sql = s.getBoundSql(null);
    		
    		List<Map<String, Object>> tmpList = sqlRunner.selectAll(sql.getSql());
    		
    		String codeField = (codeValue!=null) && (codeValue.toLowerCase().equals("n")) ? MainMasterConstant.TFN_NAME : MainMasterConstant.TFN_CODE;
    		for(Map<String,Object> tmpMap : tmpList) {
    			Map<String, Object> map = new HashMap<String, Object>();
	    		map.put(JsonConstant.COMBOBOX_ID, (String)tmpMap.get(codeField));
	    		map.put(JsonConstant.COMBOBOX_NAME, (String)tmpMap.get(MainMasterConstant.TFN_NAME));
	    		map.put(JsonConstant.COMBOBOX_DATA, tmpMap);
	    		
	    		list.add(map);
    		}
    		
    		if (all!=null && all) {
    			Map<String,Object> map = new HashMap<String,Object>(); 
    			map.put(MainMasterConstant.JFN_ID, "");
    			map.put(MainMasterConstant.JFN_NAME, "== ทั้งหมด ==");
    			list.add(0,map);
    		}

        } finally {
        	conn.close();
        }
        
        return list;
	}	

	public Map<String, Object> getMainMasterField(String cond, String resultField) throws Exception {
		
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		Connection conn = dataSource.getConnection();
		
        try {
//        	String userRoles = userGroupService.getAuthoritiesForUser(authService.getCurrentUserName());
        	
    		SqlRunner sqlRunner = new SqlRunner(conn);
    		TextSqlNode node = new TextSqlNode(selectMainMasterSql(cond, null));
    		DynamicSqlSource s = new DynamicSqlSource(DbConnectionFactory.getSqlSessionFactory(dataSource).getConfiguration(), node);
    		BoundSql sql = s.getBoundSql(null);
    		
    		log.info("SQL="+sql.getSql());
    		
    		Map<String,Object> tmpMap = sqlRunner.selectOne(sql.getSql());
    		
	    	map.put((String)tmpMap.get(resultField.toUpperCase()), "");

        } finally {
        	conn.close();
        }
        
        return map;
	}
	
	public Map<String, Object> getMainMasterValue(String type, String code, String lang) throws Exception {
		
    	MainMasterModel model = masterService.getByTypeAndCode(type, code);
    	
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		
		if (model==null) {
			map.put("data", code);
		} else {
	    	if (lang!=null && lang.startsWith("th")) {
	    		map.put("data",model.getName());
	    	} else {
	    		map.put("data",model.getFlag2());
	    	}
		}
        
        return map;
	}
	
	public Map<String, Object> getUserName(String code, String lang) throws Exception {
		
    	Map<String, Object> user = employeeService.getWithDtl(code, null);
    	
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		
		lang = lang!=null && lang.startsWith("th") ? "_th" : "";
		
    	map.put("data", user.get("title"+lang) + " " + user.get("first_name"+lang) + " " + user.get("last_name"+lang));
        
        return map;
	}
	
	public Map<String,Object> listUser(String groupNames, String codeValue, String includeEmpty) throws Exception {

		Map<String,Object> userMap = new LinkedHashMap<String, Object>();
		
        try {
        	Set<String> userSet = null;
        	
        	PersonUtil personUtil = new PersonUtil();
        	personUtil.setNodeService(nodeService);
        	
        	if (groupNames!=null && groupNames.length()>0) {
        		
            	userSet = new HashSet<String>();
            	
	        	String[] groups = groupNames.split(";");
	        	for(String g : groups) {
	        		if (g!=null && g.trim().length()>0) {
		        		Set<String> member = userGroupService.getGroupMember("GROUP_"+g.trim());
		        		userSet.addAll(member);
	        		}
	        	}
        	}
        	else {
        		userSet = authorityService.getAllAuthorities(AuthorityType.USER);
        	}
        	
    		List<String> userList = new ArrayList<String>();
    		userList.addAll(userSet);
    		Collections.sort(userList);
    		
        	/*
        	 * Fill userMap from userSet
        	 */
        	if (includeEmpty!=null && includeEmpty.equals("1")) {
        		userMap.put("", "\u00A0");
        	}
        	
        	for(String u : userList) {
        		userMap.put(u, personUtil.getFullName(PersonUtil.getPerson(u, personService)));
        	}
        	
        	
        } catch (Exception ex) {
			log.error("", ex);
        } finally {
        }
        
        return userMap;
	}
}
