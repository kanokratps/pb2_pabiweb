package pb.repo.pcm.util;

import javax.sql.DataSource;

import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.log4j.Logger;

import pb.repo.common.mybatis.DbConnectionFactory;
import pb.repo.pcm.dao.PcmFunctionDAO;
import pb.repo.pcm.dao.PcmOrdDAO;
import pb.repo.pcm.dao.PcmReqCmtDtlDAO;
import pb.repo.pcm.dao.PcmReqCmtHdrDAO;
import pb.repo.pcm.dao.PcmReqDAO;
import pb.repo.pcm.dao.PcmReqDtlDAO;
import pb.repo.pcm.dao.PcmReqMethodCommitteeDAO;
import pb.repo.pcm.dao.PcmReqMethodDAO;
import pb.repo.pcm.model.PcmOrdModel;
import pb.repo.pcm.model.PcmReqCmtDtlModel;
import pb.repo.pcm.model.PcmReqCmtHdrModel;
import pb.repo.pcm.model.PcmReqDtlModel;
import pb.repo.pcm.model.PcmReqMethodCommitteeModel;
import pb.repo.pcm.model.PcmReqMethodModel;
import pb.repo.pcm.model.PcmReqModel;

public class PcmUtil {
	
	private static Logger log = Logger.getLogger(PcmUtil.class);
	
	public static SqlSession openSession(DataSource dataSource) {
		
		SqlSessionFactory sqlSessionFactory = DbConnectionFactory.getSqlSessionFactory(dataSource);

		Configuration config = sqlSessionFactory.getConfiguration();
		if (!config.hasMapper(PcmReqDAO.class)) {
	        config.getTypeAliasRegistry().registerAlias("pcmReqModel", PcmReqModel.class);
	        config.getTypeAliasRegistry().registerAlias("pcmReqDtlModel", PcmReqDtlModel.class);
	        config.getTypeAliasRegistry().registerAlias("pcmReqMethodModel", PcmReqMethodModel.class);
	        config.getTypeAliasRegistry().registerAlias("pcmReqMethodCommitteeModel", PcmReqMethodCommitteeModel.class);
	        config.getTypeAliasRegistry().registerAlias("pcmReqCmtHdrModel", PcmReqCmtHdrModel.class);
	        config.getTypeAliasRegistry().registerAlias("pcmReqCmtDtlModel", PcmReqCmtDtlModel.class);
	        
	        config.addMapper(PcmReqDAO.class);
	        config.addMapper(PcmReqDtlDAO.class);
	        config.addMapper(PcmReqMethodDAO.class);
	        config.addMapper(PcmReqMethodCommitteeDAO.class);
	        config.addMapper(PcmReqCmtHdrDAO.class);
	        config.addMapper(PcmReqCmtDtlDAO.class);
		}
		
		if (!config.hasMapper(PcmFunctionDAO.class)) {
	        //config.getTypeAliasRegistry().registerAlias("memoWorkflowModel", MemoWorkflowModel.class);
	        
	        config.addMapper(PcmFunctionDAO.class);
		}
		
		if (!config.hasMapper(PcmOrdDAO.class)) {
	        config.getTypeAliasRegistry().registerAlias("pcmOrdModel", PcmOrdModel.class);
	        
	        config.addMapper(PcmOrdDAO.class);
		}		
		
        return sqlSessionFactory.openSession();
	}
	
}
