package pb.repo.admin.service;

import java.util.List;

import javax.sql.DataSource;

import org.alfresco.service.cmr.security.AuthenticationService;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pb.repo.admin.dao.MainCurrencyDAO;
import pb.repo.admin.model.MainCurrencyModel;
import pb.repo.common.mybatis.DbConnectionFactory;

@Service
public class AdminCurrencyService {
	
	private static Logger log = Logger.getLogger(AdminCurrencyService.class);

	@Autowired
	DataSource dataSource;
	
	@Autowired
	AuthenticationService authService;

	public List<MainCurrencyModel> list() {
		
		List<MainCurrencyModel> list = null;
		
        SqlSession session = DbConnectionFactory.getSqlSessionFactory(dataSource).openSession();
        try {
        	MainCurrencyDAO dao = session.getMapper(MainCurrencyDAO.class);
            
            list = dao.list();
            
        } finally {
        	session.close();
        }
		
		return list;
	}
}
