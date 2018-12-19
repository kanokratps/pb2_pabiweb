package pb.repo.admin.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.alfresco.service.cmr.security.AuthenticationService;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pb.common.constant.JsonConstant;
import pb.repo.admin.constant.MainHrExpenseRuleConstant;
import pb.repo.admin.dao.MainHrExpenseRuleDAO;
import pb.repo.admin.model.MainHrExpenseRuleModel;
import pb.repo.common.mybatis.DbConnectionFactory;

@Service
public class AdminHrExpenseRuleService {
	
	private static Logger log = Logger.getLogger(AdminHrExpenseRuleService.class);

	@Autowired
	DataSource dataSource;
	
	@Autowired
	AuthenticationService authService;

	public MainHrExpenseRuleModel get(String id) {
			
		MainHrExpenseRuleModel model = null;
		
		SqlSession session = DbConnectionFactory.getSqlSessionFactory(dataSource).openSession();
        try {
        	MainHrExpenseRuleDAO dao = session.getMapper(MainHrExpenseRuleDAO.class);
    		model = dao.get(id);
    		if (model!=null) {
    			model.setTotalRowCount(1l);
    		}
            
        } finally {
        	session.close();
        }
        
        return model;
	}
	
	public List<Map<String, Object>> list(Integer id, String cond) {
		
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		
        SqlSession session = DbConnectionFactory.getSqlSessionFactory(dataSource).openSession();
        try {
        	MainHrExpenseRuleDAO dao = session.getMapper(MainHrExpenseRuleDAO.class);
            
        	Map<String, Object> params = new HashMap<String, Object>();

        	params.put("id", id);
        	params.put("cond", cond);
        	
    		list = dao.list(params);
        	
        } catch (Exception ex) {
        	log.error(ex);
        } finally {
        	session.close();
        }
		
		return list;
	}	
	
	public List<Map<String, Object>> listDistinct(Integer id) {
		
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		
        SqlSession session = DbConnectionFactory.getSqlSessionFactory(dataSource).openSession();
        try {
        	MainHrExpenseRuleDAO dao = session.getMapper(MainHrExpenseRuleDAO.class);
            
        	Map<String, Object> params = new HashMap<String, Object>();

        	params.put("id", id);
        	
    		List<Map<String, Object>> tmpList = dao.listDistinct(params);
    		
    		int i = 0;
    		for(Map<String,Object> tmpMap : tmpList) {
    			Map<String, Object> map = new HashMap<String, Object>();
	    		map.put(JsonConstant.COMBOBOX_ID, i++);
	    		map.put(JsonConstant.COMBOBOX_NAME, (String)tmpMap.get(MainHrExpenseRuleConstant.TFN_CONDITION_1));
	    		map.put(JsonConstant.COMBOBOX_DATA, tmpMap);
	    		
	    		list.add(map);
    		}
        	
        } catch (Exception ex) {
        	log.error(ex);
        } finally {
        	session.close();
        }
		
		return list;
	}	
}
