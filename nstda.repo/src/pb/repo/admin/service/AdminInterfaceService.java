package pb.repo.admin.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pb.repo.admin.dao.MainInterfaceDAO;
import pb.repo.common.mybatis.DbConnectionFactory;

@Service
public class AdminInterfaceService {
	
	private static Logger log = Logger.getLogger(AdminInterfaceService.class);
	
	@Autowired
	DataSource dataSource;
	
	public Long add(String _source, String _method, String _params) {
		
		Long id = null;
		SqlSession session = DbConnectionFactory.getSqlSessionFactory(dataSource).openSession();
		
		try {
			Map<String, Object> params = new HashMap<String, Object>(); 
			
			params.put("source", _source);
			params.put("method", _method);
			params.put("params", _params);
			
            MainInterfaceDAO dao = session.getMapper(MainInterfaceDAO.class);
            
            dao.add(params);
            id = (Long)params.get("id");
            
            session.commit();
            
        } catch (Exception ex) {
			log.error("", ex);
        	session.rollback();
        } finally {
        	session.close();
        }
		
		return id;
	}
	
}
