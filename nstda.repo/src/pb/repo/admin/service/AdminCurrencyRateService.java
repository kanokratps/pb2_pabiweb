package pb.repo.admin.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.alfresco.service.cmr.security.AuthenticationService;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pb.repo.admin.dao.MainCurrencyRateDAO;
import pb.repo.admin.model.MainCurrencyRateModel;
import pb.repo.common.mybatis.DbConnectionFactory;

@Service
public class AdminCurrencyRateService {
	
	private static Logger log = Logger.getLogger(AdminCurrencyRateService.class);

	@Autowired
	DataSource dataSource;
	
	@Autowired
	AuthenticationService authService;
	
	public List<MainCurrencyRateModel> list(String name) {
		
		List<MainCurrencyRateModel> list = null;
		
        SqlSession session = DbConnectionFactory.getSqlSessionFactory(dataSource).openSession();
        try {
        	MainCurrencyRateDAO dao = session.getMapper(MainCurrencyRateDAO.class);
            
        	Map<String, Object> params = new HashMap<String,Object>();
        	
            list = dao.list(name);
        } finally {
        	session.close();
        }
		
		return list;
	}
}
