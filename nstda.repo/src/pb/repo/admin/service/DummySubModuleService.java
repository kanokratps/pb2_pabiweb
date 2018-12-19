package pb.repo.admin.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.alfresco.service.cmr.repository.NodeRef;
import org.alfresco.service.namespace.QName;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pb.common.constant.CommonConstant;
import pb.repo.admin.constant.ExpBrwConstant;
import pb.repo.admin.constant.ExpUseConstant;
import pb.repo.admin.constant.MainWorkflowConstant;
import pb.repo.admin.constant.PcmReqConstant;
import pb.repo.admin.dao.AdminWorkflowDAO;
import pb.repo.admin.dao.MainWorkflowNextActorDAO;
import pb.repo.admin.model.MainWorkflowHistoryModel;
import pb.repo.admin.model.MainWorkflowNextActorModel;
import pb.repo.admin.model.SubModuleModel;
import pb.repo.common.mybatis.DbConnectionFactory;

@Service
public class DummySubModuleService implements SubModuleService {
	
	private static Logger log = Logger.getLogger(DummySubModuleService.class);
	
	@Autowired
	DataSource dataSource;
	
	@Autowired
	MainWorkflowService mainWorkflowService;
	
	@Autowired
	AdminHrEmployeeService adminHrEmployeeService;

	@Override
	public List<Map<String, Object>> listWorkflowPath(String id, String lang) {
		List<Map<String, Object>> list = mainWorkflowService.listWorkflowPath(id, lang);
		
		Map<String,Object> model = (Map<String, Object>)getForWfPath(id, null);

		Map<String, Object> map = new HashMap<String, Object>();
		
		String prefix = id.substring(0, 2);
		
		if (prefix.equals(CommonConstant.SUB_MODULE_PREFIX_EXP_BRW)) { // AV
			if (!model.get("req_by").equals(model.get("created_by"))) {
				/*
				 * Add Accepter
				 */
				map = new HashMap<String, Object>();
				map.put("LEVEL", mainWorkflowService.getTaskCaption(MainWorkflowConstant.TN_REQUESTER, lang, null));
				map.put("U", model.get("req_by"));
				map.put("G", "");
				map.put("IRA", false);
				map.put("C", "1");
				
				list.add(0, map);
			}
		}
		else
		if (prefix.equals(CommonConstant.SUB_MODULE_PREFIX_EXP_BRW)) { // PD
			map = new HashMap<String, Object>();
			map.put("LEVEL", mainWorkflowService.getTaskCaption(MainWorkflowConstant.TN_SUPERVISOR, lang, null));
			map.put("U", model.get("app_by"));
			map.put("G", "");
			map.put("IRA", false);
			map.put("C", "0");
			
			list.add(0, map);
		}

		/*
		 * Add Preparer
		 */
		map = new HashMap<String, Object>();
		map.put("LEVEL", mainWorkflowService.getTaskCaption(MainWorkflowConstant.TN_PREPARER, lang, null));
		map.put("U", model.get("created_by"));
		map.put("G", "");
		map.put("IRA", false);
		map.put("C", "0");
		
		list.add(0, map);
		
		if(prefix.equals(CommonConstant.SUB_MODULE_PREFIX_EXP_BRW)
				|| prefix.equals(CommonConstant.SUB_MODULE_PREFIX_EXP_USE)
				|| prefix.equals(CommonConstant.SUB_MODULE_PREFIX_PCM_REQ)) { // PR AV EX 
			/*
			 * Add Requester
			 */
			map = new HashMap<String, Object>();
			map.put("LEVEL", mainWorkflowService.getTaskCaption(MainWorkflowConstant.TN_REQUESTER, lang, null));
			map.put("U", model.get("req_by"));
			map.put("G", "");
			map.put("IRA", false);
			map.put("C", "0");
			
			list.add(0, map);
		}
		
		/*
		 * Add Next Actor
		 */
		List<Map<String, Object>> actorList = null;
		SqlSession session = DbConnectionFactory.getSqlSessionFactory(dataSource).openSession();
        try {
            
            MainWorkflowNextActorDAO dao = session.getMapper(MainWorkflowNextActorDAO.class);

    		actorList = dao.listWorkflowPath(id);
    		
    		for(Map<String, Object> actor : actorList) {
    			actor.put("LEVEL", mainWorkflowService.getTaskCaption((String)actor.get("LEVEL"),lang,null));
    		}
    		
    		list.addAll(actorList);
            
        } catch (Exception ex) {
			log.error("", ex);
        } finally {
        	session.close();
        }
		
        /*
         * Convert Employeee Code to Name
         */
        lang = (lang!=null && lang.startsWith("th") ? "_th" : "");
        
        List<String> codes = new ArrayList<String>();
		for(Map<String, Object> rec:list) {
			codes.add((String)rec.get("U"));
		}
		if (codes.size()>0) {
			List<Map<String, Object>> empList = adminHrEmployeeService.listInSet(codes);
			for(Map<String, Object> rec:list) {
				boolean found = false;
				for (Map<String, Object> empModel : empList) {
					if (empModel.get("code").equals(rec.get("U"))) {
						rec.put("U", empModel.get("code") + " - " + empModel.get("first_name"+lang));
						found = true;
						break;
					}
				}
				if(!found) {
					rec.put("U", "");
				}
			}
		}
		
		return list;
	}

	@Override
	public void update(SqlSession session, SubModuleModel model) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public Object get(String id, String lang) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getForWfPath(String id, String lang) {
		
		Map<String,Object> model = null;
		
		lang = lang!=null && lang.startsWith("th") ? "_th" : "";
		
		SqlSession session = DbConnectionFactory.getSqlSessionFactory(dataSource).openSession();
        try {
        	AdminWorkflowDAO dao = session.getMapper(AdminWorkflowDAO.class);

            Map<String,Object> params = new HashMap<String,Object>();
            
            params.put("id", id);
            params.put("lang", lang);
            params.put("table", CommonConstant.SUB_MODULE_TABLE_NAMES.get(id.substring(0, 2)));
            
    		model = dao.getForWfPath(params);
//    		model.setTotalRowCount(1l);
        } catch (Exception ex) {
			log.error("", ex);
        } finally {
        	session.close();
        }
        
        return model;
	}

	@Override
	public String getWorkflowName() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getWorkflowDescription(SubModuleModel paramModel)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getWorkflowDescriptionEn(SubModuleModel paramModel)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, Object> convertToMap(SubModuleModel model) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getSubModuleType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setWorkflowParameters(Map<QName, Serializable> parameters,
			SubModuleModel model, List<NodeRef> docList,
			List<NodeRef> attachDocList) {
		// TODO Auto-generated method stub

	}

	@Override
	public String getActionCaption(String action, String lang) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<MainWorkflowNextActorModel> listNextActor(SubModuleModel model) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> listRelatedUser(SubModuleModel model) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getFirstComment(SubModuleModel model) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getFirstStatus() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getNextActionInfo(Object obj, String lang) {
		
		Map<String, Object> model = (Map<String,Object>)obj;
		
		String prefix = ((String)model.get("id")).substring(0, 2);
		
		if (prefix.equals(CommonConstant.SUB_MODULE_PREFIX_PCM_REQ)) {
			if (model!=null 
					&& (model.get("status").equals(PcmReqConstant.ST_CANCEL_BY_PCM) 
					|| model.get("status").equals(PcmReqConstant.ST_CANCEL_BY_REQ))
				) {
				return "";
			} else {
				return mainWorkflowService.getTaskCaption(MainWorkflowConstant.TN_PROCUREMENT,lang,null);
			}
		}
		else
		if (prefix.equals(CommonConstant.SUB_MODULE_PREFIX_PCM_ORD)) {
			return mainWorkflowService.getTaskCaption(MainWorkflowConstant.TN_PROCUREMENT,lang,null);
		}
		else
		if (prefix.equals(CommonConstant.SUB_MODULE_PREFIX_EXP_BRW)) {
			if (model!=null 
					&& (model.get("status").equals(ExpBrwConstant.ST_CANCEL_BY_FIN) 
					|| model.get("status").equals(ExpBrwConstant.ST_CANCEL_BY_REQ))
				) {
				return "";
			} else {
				return mainWorkflowService.getTaskCaption(MainWorkflowConstant.TN_FINANCE,lang,null);
			}
		}
		else
		if (prefix.equals(CommonConstant.SUB_MODULE_PREFIX_EXP_USE)) {
			if (model!=null 
					&& (model.get("status").equals(ExpUseConstant.ST_CANCEL_BY_FIN) 
					|| model.get("status").equals(ExpUseConstant.ST_CANCEL_BY_REQ))
				) {
				return "";
			} else {
				if (model.get("pay_type").equals("3")) { // 3=internal charge
					return mainWorkflowService.getTaskCaption(MainWorkflowConstant.TN_SERVICE_UNIT,lang,null);
				} else {
					return mainWorkflowService.getTaskCaption(MainWorkflowConstant.TN_FINANCE,lang,null);
				}
			}
		}
		else
		if (prefix.equals(CommonConstant.SUB_MODULE_PREFIX_HR_SAL)) {
			return mainWorkflowService.getTaskCaption(MainWorkflowConstant.TN_HUMAN_RESOURCES,lang,null);
		}
		
		return "";
	}

	@Override
	public String getModelUri() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getWfUri() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getModelPrefix() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getDocDesc() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MainWorkflowHistoryModel getReqByWorkflowHistory(SubModuleModel model) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MainWorkflowHistoryModel getAppByWorkflowHistory(SubModuleModel model) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> getSpecialUserForAddPermission(SubModuleModel model) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> getSpecialGroupForAddPermission() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public QName getPropNextReviewers() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public QName getPropDescEn() {
		// TODO Auto-generated method stub
		return null;
	}

//	@Override
//	public Map<String, String> getBossMap(String docType, SubModuleModel model)
//			throws Exception {
//		// TODO Auto-generated method stub
//		return null;
//	}

	@Override
	public Boolean addPermissionToAttached() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setFirstTaskAssignee(Map<QName, Serializable> parameters,
			SubModuleModel model) {
		// TODO Auto-generated method stub

	}

	@Override
	public void prepareModelForWfDesc(SubModuleModel model, String lang) {
		// TODO Auto-generated method stub

	}

	@Override
	public Map<String, Object> getWorkflowPathParamters(SubModuleModel model) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Double getWorkflowPathParamterTotalForProject(SubModuleModel model) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Double getWorkflowPathParamterTotalForSection(SubModuleModel model) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getMessage(String code) {
		// TODO Auto-generated method stub
		return null;
	}

}
