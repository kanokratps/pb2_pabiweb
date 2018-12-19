package pb.repo.admin.service;

import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.alfresco.service.cmr.security.AuthenticationService;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pb.repo.admin.dao.MainWkfConfigDocTypeDAO;
import pb.repo.admin.dao.MainWkfConfigPurchaseUnitDAO;
import pb.repo.admin.dao.MainWkfConfigPurchaseUnitResponsibleDAO;
import pb.repo.admin.model.MainWkfConfigDocTypeModel;
import pb.repo.common.mybatis.DbConnectionFactory;

@Service
public class AdminWkfConfigService {
	
	private static Logger log = Logger.getLogger(AdminWkfConfigService.class);

	@Autowired
	DataSource dataSource;
	
	@Autowired
	AuthenticationService authService;

	public List<Map<String, Object>> listSupervisor(Integer id) {
		
		List<Map<String, Object>> list = null;
		
        SqlSession session = DbConnectionFactory.getSqlSessionFactory(dataSource).openSession();
        try {
        	MainWkfConfigPurchaseUnitResponsibleDAO dao = session.getMapper(MainWkfConfigPurchaseUnitResponsibleDAO.class);
            
    		list = dao.listSupervisor(id);
            
        } finally {
        	session.close();
        }
        
        return list;
	}
	
	public List<Map<String, Object>> listPurchasingUnit(Integer sectionId) {
		
		List<Map<String, Object>> list = null;
		
        SqlSession session = DbConnectionFactory.getSqlSessionFactory(dataSource).openSession();
        try {
        	MainWkfConfigPurchaseUnitDAO dao = session.getMapper(MainWkfConfigPurchaseUnitDAO.class);
            
    		list = dao.listPurchasingUnitBySectionId(sectionId);
            
        } finally {
        	session.close();
        }
        
        return list;
	}
	
	public Map<String, Object> getDocType(String name) {
		Map<String, Object> map = null;
		
        SqlSession session = DbConnectionFactory.getSqlSessionFactory(dataSource).openSession();
        try {
        	MainWkfConfigDocTypeDAO dao = session.getMapper(MainWkfConfigDocTypeDAO.class);
            
    		map = dao.getByName(name);
            
        } finally {
        	session.close();
        }
        
        return map;
	}
	
	public List<MainWkfConfigDocTypeModel> listDocType(Map<String, Object> params) {
		
		List<MainWkfConfigDocTypeModel> list = null;
		
        SqlSession session = DbConnectionFactory.getSqlSessionFactory(dataSource).openSession();
        try {
        	MainWkfConfigDocTypeDAO dao = session.getMapper(MainWkfConfigDocTypeDAO.class);
            
    		list = dao.list(params);
            
        } finally {
        	session.close();
        }
        
        return list;
	}
}
