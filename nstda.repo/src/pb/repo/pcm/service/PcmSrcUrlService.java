package pb.repo.pcm.service;

import static org.apache.ibatis.jdbc.SelectBuilder.BEGIN;
import static org.apache.ibatis.jdbc.SelectBuilder.FROM;
import static org.apache.ibatis.jdbc.SelectBuilder.GROUP_BY;
import static org.apache.ibatis.jdbc.SelectBuilder.ORDER_BY;
import static org.apache.ibatis.jdbc.SelectBuilder.SELECT;
import static org.apache.ibatis.jdbc.SelectBuilder.SQL;
import static org.apache.ibatis.jdbc.SelectBuilder.WHERE;

import java.sql.Connection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.alfresco.service.cmr.security.AuthenticationService;
import org.apache.ibatis.builder.xml.dynamic.DynamicSqlSource;
import org.apache.ibatis.builder.xml.dynamic.TextSqlNode;
import org.apache.ibatis.jdbc.SqlRunner;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pb.common.constant.CommonConstant;
import pb.common.util.CommonUtil;
import pb.repo.admin.constant.MainMasterConstant;
import pb.repo.admin.constant.PcmReqConstant;
import pb.repo.admin.service.AdminUserGroupService;
import pb.repo.common.mybatis.DbConnectionFactory;
import pb.repo.pcm.constant.PcmReqDtlConstant;
import pb.repo.pcm.dao.PcmReqDAO;
import pb.repo.pcm.model.PcmReqModel;
import pb.repo.pcm.util.PcmUtil;

@Service
public class PcmSrcUrlService {

	private static Logger log = Logger.getLogger(PcmSrcUrlService.class);

	@Autowired
	DataSource dataSource;
	
	@Autowired
	AuthenticationService authService;
	
	@Autowired
	AdminUserGroupService userGroupService;
	
	private String selectMainMasterSql(String cond, String curUser, String userRoles, String orderBy, String joinCond, boolean forRequester, String formMode) {
		BEGIN();
		
		StringBuffer fields = new StringBuffer();
		fields.append("M."+MainMasterConstant.TFN_ID);
		fields.append(",M."+MainMasterConstant.TFN_TYPE);
		fields.append(",M."+MainMasterConstant.TFN_CODE);
		fields.append(",M."+MainMasterConstant.TFN_NAME);
		fields.append(",M."+MainMasterConstant.TFN_FLAG1);
		fields.append(",M."+MainMasterConstant.TFN_FLAG2);
		fields.append(",M."+MainMasterConstant.TFN_FLAG3);
		fields.append(",M."+MainMasterConstant.TFN_FLAG4);
		fields.append(",M."+MainMasterConstant.TFN_FLAG5);
		
		SELECT(fields.toString()
				+ ",COUNT(*) OVER() "+CommonConstant.TFN_TOTAL_ROW_COUNT
		);
		FROM(MainMasterConstant.TABLE_NAME+" M");

		if (joinCond!=null && !joinCond.trim().equals("")) {
			String usrCond = " like '%,"+curUser+",%'";
//			String amSql = "(select AM.* from "+MainApprovalMatrixConstant.TABLE_NAME+" AM,"
//					+ MainApprovalMatrixDtlConstant.TABLE_NAME+" AD"
//					+ " where AM.ID=AD.MASTER_ID"
//					+ " and ("+MainApprovalMatrixConstant.TFN_REQUESTER_USER+usrCond
//					;
//			
//			if (!forRequester) {
//				amSql += " or "+MainApprovalMatrixConstant.TFN_ACTION_USER+usrCond
//					  + " or "+MainApprovalMatrixConstant.TFN_RELATED_USER+usrCond
//					  + " or "+MainApprovalMatrixDtlConstant.TFN_REVIEWER_USER+usrCond
//					  ;
//			}
//			else
//			if (formMode.equals("E")) {
//				amSql += " or "+MainApprovalMatrixDtlConstant.TFN_REVIEWER_USER+usrCond;
//			}
//			
//			userRoles = userRoles.replace("'", "");
//			String[] groups = userRoles.split(",");
//			
//			for(int i=0; i<groups.length; i++) {
//				if (!groups[i].equals("")) {
//					String grpCond = " like '%,"+groups[i].replace("GROUP_", "")+",%'";
//					amSql += " or "+MainApprovalMatrixConstant.TFN_REQUESTER_GROUP+grpCond;
//					
//					if (!forRequester) {
//						amSql += " or "+MainApprovalMatrixConstant.TFN_ACTION_GROUP+grpCond
//							  + " or "+MainApprovalMatrixConstant.TFN_RELATED_GROUP+grpCond
//							  + " or "+MainApprovalMatrixDtlConstant.TFN_REVIEWER_GROUP+grpCond
//							  ;
//					}
//					else
//					if (formMode.equals("E")) {
//						amSql += " or "+MainApprovalMatrixDtlConstant.TFN_REVIEWER_GROUP+grpCond;
//					}
//				}
//			}
//					
//			amSql += ")) A";
//			
//			FROM(amSql);
//			WHERE(joinCond);
		}
		
		StringBuffer newCond = new StringBuffer();
		String[] clauses = cond.split(" ");
		for (String clause : clauses) {
			newCond.append(clause.indexOf("=") >= 0 ? "M."+clause : clause);
			newCond.append(" ");
		}
		
		WHERE("M.IS_ACTIVE=true");
		WHERE(newCond.toString());
		
		
		GROUP_BY(fields.toString());
		
		if (orderBy == null) {
			orderBy = MainMasterConstant.TFN_CODE;
		}
		ORDER_BY("M."+orderBy);
		
		return SQL();
	}

	public Map<String, Object> listMainMaster(String cond, String orderBy, Boolean all, String joinCond, String codeValue, boolean forRequester, String formMode) throws Exception {
		
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		Connection conn = dataSource.getConnection();
		
        try {
        	String curUser = authService.getCurrentUserName();
        	String userRoles = userGroupService.getAuthoritiesForUser(curUser);
//        	log.info(authService.getCurrentUserName()+":roles:"+userRoles);
        	
    		SqlRunner sqlRunner = new SqlRunner(conn);
    		TextSqlNode node = new TextSqlNode(selectMainMasterSql(cond, curUser, userRoles, orderBy, joinCond, forRequester, formMode));
    		DynamicSqlSource s = new DynamicSqlSource(DbConnectionFactory.getSqlSessionFactory(dataSource).getConfiguration(), node);
    		BoundSql sql = s.getBoundSql(null);
    		
    		log.info("SQL="+sql.getSql());
    		
    		List<Map<String,Object>> tmpList = sqlRunner.selectAll(sql.getSql());
    		
    		if (all!=null && all) {
    			map.put("", "== ทั้งหมด ==");
    		}
    		
    		String codeField = (codeValue!=null) && (codeValue.toLowerCase().equals("n")) ? MainMasterConstant.TFN_NAME : MainMasterConstant.TFN_CODE;
    		for(Map<String,Object> tmpMap : tmpList) {
	    		map.put((String)tmpMap.get(codeField), (String)tmpMap.get(MainMasterConstant.TFN_NAME));
    		}

        } catch (Exception ex) {
			log.error("", ex);
        } finally {
        	conn.close();
        }
        
        return map;
	}
	
	public Map<String, Object> getMainMasterField(String cond, String resultField, String joinCond, boolean forRequester, String formMode) throws Exception {
		
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		Connection conn = dataSource.getConnection();
		
        try {
        	String curUser = authService.getCurrentUserName();
        	String userRoles = userGroupService.getAuthoritiesForUser(curUser);
        	
    		SqlRunner sqlRunner = new SqlRunner(conn);
    		TextSqlNode node = new TextSqlNode(selectMainMasterSql(cond, curUser, userRoles, null, joinCond, forRequester, formMode));
    		DynamicSqlSource s = new DynamicSqlSource(DbConnectionFactory.getSqlSessionFactory(dataSource).getConfiguration(), node);
    		BoundSql sql = s.getBoundSql(null);
    		
    		log.info("SQL="+sql.getSql());
    		
    		Map<String,Object> tmpMap = sqlRunner.selectOne(sql.getSql());
    		
	    	map.put((String)tmpMap.get(resultField.toUpperCase()), "");
        } catch (Exception ex) {
			log.error("", ex);
        } finally {
        	conn.close();
        }
        
        return map;
	}
	
	private String selectMemoSql(String cond, String curUser, String userRoles, String orderBy, String joinCond, boolean forRequester, String formMode) {
		BEGIN();
		
		StringBuffer fields = new StringBuffer();
		fields.append("M."+PcmReqConstant.TFN_ID);
		fields.append(",M."+PcmReqConstant.TFN_TOTAL);
		fields.append(",M."+PcmReqConstant.TFN_WAITING_LEVEL);
		fields.append(",M."+PcmReqConstant.TFN_STATUS);
		fields.append(",M."+PcmReqConstant.TFN_WORKFLOW_INS_ID);
		fields.append(",M."+PcmReqConstant.TFN_DOC_REF);
		fields.append(",M."+PcmReqConstant.TFN_FOLDER_REF);
		fields.append(",M."+PcmReqConstant.TFN_CREATED_TIME);
		fields.append(",M."+PcmReqConstant.TFN_CREATED_BY);
		fields.append(",M."+PcmReqConstant.TFN_UPDATED_TIME);
		fields.append(",M."+PcmReqConstant.TFN_UPDATED_BY);

		
		SELECT(fields.toString()
				+ ",COUNT(*) OVER() "+CommonConstant.TFN_TOTAL_ROW_COUNT
		);
		FROM(PcmReqConstant.TABLE_NAME+" M");

		if (joinCond!=null && !joinCond.trim().equals("")) {
//			String usrCond = " like '%,"+curUser+",%'";
//			String amSql = "(select AM.* from "+MainApprovalMatrixConstant.TABLE_NAME+" AM,"
//					+ MainApprovalMatrixDtlConstant.TABLE_NAME+" AD"
//					+ " where AM.ID=AD.MASTER_ID"
//					+ " and ("+MainApprovalMatrixConstant.TFN_REQUESTER_USER+usrCond
//					;
//			
//			if (!forRequester) {
//				amSql += " or "+MainApprovalMatrixConstant.TFN_ACTION_USER+usrCond
//					  + " or "+MainApprovalMatrixConstant.TFN_RELATED_USER+usrCond
//					  + " or "+MainApprovalMatrixDtlConstant.TFN_REVIEWER_USER+usrCond
//					  ;
//			}
//			else
//			if (formMode.equals("E")) {
//				amSql += " or "+MainApprovalMatrixDtlConstant.TFN_REVIEWER_USER+usrCond;
//			}
//			
//			userRoles = userRoles.replace("'", "");
//			String[] groups = userRoles.split(",");
//			
//			for(int i=0; i<groups.length; i++) {
//				if (!groups[i].equals("")) {
//					String grpCond = " like '%,"+groups[i].replace("GROUP_", "")+",%'";
//					amSql += " or "+MainApprovalMatrixConstant.TFN_REQUESTER_GROUP+grpCond;
//					
//					if (!forRequester) {
//						amSql += " or "+MainApprovalMatrixConstant.TFN_ACTION_GROUP+grpCond
//							  + " or "+MainApprovalMatrixConstant.TFN_RELATED_GROUP+grpCond
//							  + " or "+MainApprovalMatrixDtlConstant.TFN_REVIEWER_GROUP+grpCond
//							  ;
//					}
//					else
//					if (formMode.equals("E")) {
//						amSql += " or "+MainApprovalMatrixDtlConstant.TFN_REVIEWER_GROUP+grpCond;
//					}
//				}
//			}
//					
//			amSql += ")) A";
//			
//			FROM(amSql);
//			WHERE(joinCond);
		}
		
		StringBuffer newCond = new StringBuffer();
		String[] clauses = cond.split(" ");
		for (String clause : clauses) {
			newCond.append((clause.indexOf("!") >= 0 
					|| clause.indexOf("=") >= 0 
					|| clause.indexOf(">") >= 0 
					|| clause.indexOf("<") >= 0) ? 
					(clause.startsWith("M.") || clause.startsWith("A.") ? clause : "M."+clause) : clause);
			newCond.append(" ");
		}
		
		WHERE(newCond.toString());
		
		
		GROUP_BY(fields.toString());
		
		if (orderBy == null) {
			orderBy = PcmReqConstant.TFN_ID;
		}
		ORDER_BY("M."+orderBy);
		
		return SQL();
	}
	
	private String selectViewMemoSql(String cond, String curUser, String userRoles, String orderBy, String joinCond, boolean forRequester, String formMode) {
		BEGIN();
		
		StringBuffer fields = new StringBuffer();
		fields.append("M."+PcmReqConstant.TFN_ID);
		fields.append(",M."+PcmReqConstant.TFN_TOTAL);
		fields.append(",M."+PcmReqConstant.TFN_WAITING_LEVEL);
		fields.append(",M."+PcmReqConstant.TFN_STATUS);
		fields.append(",M."+PcmReqConstant.TFN_WORKFLOW_INS_ID);
		fields.append(",M."+PcmReqConstant.TFN_DOC_REF);
		fields.append(",M."+PcmReqConstant.TFN_FOLDER_REF);
		fields.append(",M."+PcmReqConstant.TFN_CREATED_TIME);
		fields.append(",M."+PcmReqConstant.TFN_CREATED_BY);
		fields.append(",M."+PcmReqConstant.TFN_UPDATED_TIME);
		fields.append(",M."+PcmReqConstant.TFN_UPDATED_BY);
		
		fields.append(",D."+PcmReqDtlConstant.TFN_DESCRIPTION);
		fields.append(",D."+PcmReqDtlConstant.TFN_QUANTITY);
		fields.append(",D."+PcmReqDtlConstant.TFN_UNIT_ID);
		fields.append(",D."+PcmReqDtlConstant.TFN_PRICE);
		fields.append(",D."+PcmReqDtlConstant.TFN_TOTAL);
		
		SELECT(fields.toString()
				+ ",COUNT(*) OVER() "+CommonConstant.TFN_TOTAL_ROW_COUNT
		);
		FROM(PcmReqConstant.TABLE_NAME+" M");
		
		FROM(PcmReqDtlConstant.TABLE_NAME+" D");
		
		WHERE("M."+PcmReqDtlConstant.TFN_ID+"=D."+PcmReqDtlConstant.TFN_MASTER_ID);

		if (joinCond!=null && !joinCond.trim().equals("")) {
//			String usrCond = " like '%,"+curUser+",%'";
//			String amSql = "(select AM.* from "+MainApprovalMatrixConstant.TABLE_NAME+" AM,"
//					+ MainApprovalMatrixDtlConstant.TABLE_NAME+" AD"
//					+ " where AM.ID=AD.MASTER_ID"
//					+ " and ("+MainApprovalMatrixConstant.TFN_REQUESTER_USER+usrCond
//					;
//			
//			if (!forRequester) {
//				amSql += " or "+MainApprovalMatrixConstant.TFN_ACTION_USER+usrCond
//					  + " or "+MainApprovalMatrixConstant.TFN_RELATED_USER+usrCond
//					  + " or "+MainApprovalMatrixDtlConstant.TFN_REVIEWER_USER+usrCond
//					  ;
//			}
//			else
//			if (formMode.equals("E")) {
//				amSql += " or "+MainApprovalMatrixDtlConstant.TFN_REVIEWER_USER+usrCond;
//			}
//			
//			userRoles = userRoles.replace("'", "");
//			String[] groups = userRoles.split(",");
//			
//			for(int i=0; i<groups.length; i++) {
//				if (!groups[i].equals("")) {
//					String grpCond = " like '%,"+groups[i].replace("GROUP_", "")+",%'";
//					amSql += " or "+MainApprovalMatrixConstant.TFN_REQUESTER_GROUP+grpCond;
//					
//					if (!forRequester) {
//						amSql += " or "+MainApprovalMatrixConstant.TFN_ACTION_GROUP+grpCond
//							  + " or "+MainApprovalMatrixConstant.TFN_RELATED_GROUP+grpCond
//							  + " or "+MainApprovalMatrixDtlConstant.TFN_REVIEWER_GROUP+grpCond
//							  ;
//					}
//					else
//					if (formMode.equals("E")) {
//						amSql += " or "+MainApprovalMatrixDtlConstant.TFN_REVIEWER_GROUP+grpCond;
//					}
//				}
//			}
//					
//			amSql += ")) A";
//			
//			FROM(amSql);
//			WHERE(joinCond);
		}
		
		StringBuffer newCond = new StringBuffer();
		String[] clauses = cond.split(" ");
		for (String clause : clauses) {
			newCond.append((clause.indexOf("!") >= 0 
					|| clause.indexOf("=") >= 0 
					|| clause.indexOf(">") >= 0 
					|| clause.indexOf("<") >= 0) ? 
					(clause.startsWith("M.") || clause.startsWith("D.") || clause.startsWith("A.") ? clause : "M."+clause) : clause);
			newCond.append(" ");
		}
		
		WHERE(newCond.toString());
		
		
		GROUP_BY(fields.toString());
		
		if (orderBy == null) {
			orderBy = PcmReqConstant.TFN_ID;
		}
		ORDER_BY("M."+orderBy);
		
		return SQL();
	}

	public Map<String, Object> listMemo(String cond, String orderBy, Boolean all, String joinCond, String codeValue, boolean forRequester, String formMode) throws Exception {
		
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		Connection conn = dataSource.getConnection();
		
        try {
        	String curUser = authService.getCurrentUserName();
        	String userRoles = userGroupService.getAuthoritiesForUser(curUser);
        	
    		SqlRunner sqlRunner = new SqlRunner(conn);
    		TextSqlNode node = new TextSqlNode(selectMemoSql(cond, curUser, userRoles, orderBy, joinCond, forRequester, formMode));
    		DynamicSqlSource s = new DynamicSqlSource(DbConnectionFactory.getSqlSessionFactory(dataSource).getConfiguration(), node);
    		BoundSql sql = s.getBoundSql(null);
    		
    		log.info("SQL="+sql.getSql());
    		
    		List<Map<String,Object>> tmpList = sqlRunner.selectAll(sql.getSql());
    		
    		if (all!=null && all) {
    			map.put("", "== ทั้งหมด ==");
    		}
    		
    		String codeField = PcmReqConstant.TFN_ID;
    		for(Map<String,Object> tmpMap : tmpList) {
	    		map.put((String)tmpMap.get(codeField), (String)tmpMap.get(PcmReqConstant.TFN_ID));
    		}
        } catch (Exception ex) {
			log.error("", ex);
        } finally {
        	conn.close();
        }
        
        return map;
	}
	
	public Map<String, Object> listViewMemo(String cond, String orderBy, Boolean all, String joinCond, String codeValue, boolean forRequester, String formMode) throws Exception {
		
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		Connection conn = dataSource.getConnection();
		
        try {
        	String curUser = authService.getCurrentUserName();
        	String userRoles = userGroupService.getAuthoritiesForUser(curUser);
        	
    		SqlRunner sqlRunner = new SqlRunner(conn);
    		TextSqlNode node = new TextSqlNode(selectViewMemoSql(cond, curUser, userRoles, orderBy, joinCond, forRequester, formMode));
    		DynamicSqlSource s = new DynamicSqlSource(DbConnectionFactory.getSqlSessionFactory(dataSource).getConfiguration(), node);
    		BoundSql sql = s.getBoundSql(null);
    		
    		log.info("SQL="+sql.getSql());
    		
    		List<Map<String,Object>> tmpList = sqlRunner.selectAll(sql.getSql());
    		
    		if (all!=null && all) {
    			map.put("", "== All ==");
    		}
    		
    		String codeField = PcmReqConstant.TFN_ID;
    		for(Map<String,Object> tmpMap : tmpList) {
	    		map.put((String)tmpMap.get(codeField), (String)tmpMap.get(PcmReqConstant.TFN_ID)+" - "+(String)tmpMap.get(PcmReqDtlConstant.TFN_DESCRIPTION));
    		}
        } catch (Exception ex) {
			log.error("", ex);
        } finally {
        	conn.close();
        }
        
        return map;
	}
	
	private String selectMemoDtlSql(String cond) {
		BEGIN();
		
		StringBuffer fields = new StringBuffer();
		fields.append("D."+PcmReqDtlConstant.TFN_ID);
		fields.append(",D."+PcmReqDtlConstant.TFN_DESCRIPTION);
		fields.append(",D."+PcmReqDtlConstant.TFN_QUANTITY);
		fields.append(",D."+PcmReqDtlConstant.TFN_UNIT_ID);
		fields.append(",D."+PcmReqDtlConstant.TFN_PRICE);
		fields.append(",D."+PcmReqDtlConstant.TFN_TOTAL);
		
		SELECT(fields.toString()
				+ ",COUNT(*) OVER() "+CommonConstant.TFN_TOTAL_ROW_COUNT
		);
		FROM(PcmReqDtlConstant.TABLE_NAME+" D");
		
		WHERE(cond);
		
		return SQL();
	}

	public Map<String, Object> getMemoDtlField(String cond) throws Exception {
		
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		Connection conn = dataSource.getConnection();
		
        try {
    		SqlRunner sqlRunner = new SqlRunner(conn);
    		TextSqlNode node = new TextSqlNode(selectMemoDtlSql(cond));
    		DynamicSqlSource s = new DynamicSqlSource(DbConnectionFactory.getSqlSessionFactory(dataSource).getConfiguration(), node);
    		BoundSql sql = s.getBoundSql(null);
    		
    		log.info("SQL="+sql.getSql());
    		
    		Map<String,Object> tmpMap = sqlRunner.selectOne(sql.getSql());
    		
	    	map.put((String)tmpMap.get(PcmReqDtlConstant.TFN_DESCRIPTION), "");
        } catch (Exception ex) {
			log.error("", ex);
        } finally {
        	conn.close();
        }
        
        return map;
	}
	
	public Map<String, Object> getMemoFieldTotal(String id, String lang) throws Exception {
		
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		List<PcmReqModel> list = null;
		
		SqlSession session = PcmUtil.openSession(dataSource);
        try {
        	Map<String, Object> params = new HashMap<String,Object>();
        	params.put("id", id);
        	params.put("lang", lang);
        	
            PcmReqDAO dao = session.getMapper(PcmReqDAO.class);
    		PcmReqModel model = dao.get(params);
    		
    		
            
    		map.put("data", CommonUtil.formatMoney(model.getTotal())+" "+model.getCurrency());
        } catch (Exception ex) {
			log.error("", ex);
        	throw ex;
        } finally {
        	session.close();
        }
        
        return map;
	}	
	
}
