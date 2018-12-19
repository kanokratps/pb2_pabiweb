package pb.repo.hr.service;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

import javax.sql.DataSource;

import org.alfresco.repo.forms.FormException;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pb.common.constant.CommonConstant;
import pb.common.model.FileModel;
import pb.common.util.CommonUtil;
import pb.common.util.InterfaceUtil;
import pb.common.util.NodeUtil;
import pb.repo.admin.constant.MainMasterConstant;
import pb.repo.admin.constant.MainWorkflowConstant;
import pb.repo.admin.dao.MainWorkflowDAO;
import pb.repo.admin.dao.MainWorkflowHistoryDAO;
import pb.repo.admin.model.MainMasterModel;
import pb.repo.admin.model.MainWorkflowHistoryModel;
import pb.repo.admin.model.MainWorkflowModel;
import pb.repo.admin.service.AdminMasterService;
import pb.repo.admin.service.AdminSectionService;
import pb.repo.admin.service.AdminUserService;
import pb.repo.common.mybatis.DbConnectionFactory;
import pb.repo.hr.model.HrSalModel;
import redstone.xmlrpc.XmlRpcClient;
import redstone.xmlrpc.XmlRpcException;
import redstone.xmlrpc.XmlRpcStruct;

@Service
public class InterfaceService {

	private static Logger log = Logger.getLogger(InterfaceService.class);

	@Autowired
	DataSource dataSource;
	
	@Autowired
	HrSalService hrSalService;
	
	@Autowired
	AdminMasterService masterService;
	
	@Autowired
	AdminSectionService sectionService;
	
	@Autowired
	AdminUserService userService;
	
	private List<Object> getInitArgs(Map<String, Object> cfg) {
		List<Object> args = new ArrayList();
		args.add(cfg.get("db")); // db name
		args.add(cfg.get("usr")); // uid 1='admin'
		args.add(cfg.get("pwd")); // password
		
		return args;
	}

	private Map<String, Object> getConnectionConfig(String password) {
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		MainMasterModel sysCfgModel = masterService.getSystemConfig(MainMasterConstant.SCC_MAIN_ODOO_URL,false);
		String host = sysCfgModel.getFlag1();
		
		sysCfgModel = masterService.getSystemConfig(MainMasterConstant.SCC_MAIN_ODOO_DB,false);
		String db = sysCfgModel.getFlag1();
		
		sysCfgModel = masterService.getSystemConfig(MainMasterConstant.SCC_MAIN_ODOO_USER,false);
		String login = sysCfgModel.getFlag1();
		
		Map<String,Object> user = userService.getByLogin(login);
		
		Integer usr = (Integer)user.get("id"); // uid 1='admin'
		String pwd = password;
		
		log.info("host:"+host);
//		log.info("db:"+db);
//		log.info("usr:"+usr);
		
		map.put("host", host);
		map.put("db", db);
		map.put("usr", usr);
		map.put("pwd", pwd);

		return map;
	}
	
	private XmlRpcClient getXmlRpcClient(Map<String, Object> cfg) throws Exception {
		return new XmlRpcClient(cfg.get("host")+CommonConstant.EXT_XMLRPC_URL, false);
	}
	
	public String updateStatusSalary(HrSalModel model, String action, String user, String comment, String login) throws Exception {
		log.info("interface : updateStatusSalary");
		
		MainMasterModel cfgModel = masterService.getSystemConfig(MainMasterConstant.SCC_MAIN_INF_SAL_UPDATE_STATUS);
		
		Boolean success = false;
		String msgs = null;
		Map<String, Object> map = new HashMap<String, Object>();
		
		if (cfgModel.getFlag1().equals(CommonConstant.V_ENABLE)) { 
		
			try {
				
				final Properties gProp = CommonUtil.getGlobalProperties();
				String odooPassword = gProp.getProperty(CommonConstant.GP_ODOO_ADMIN_PASSWORD);
				
				Map<String, Object> cfg = getConnectionConfig(odooPassword);
				XmlRpcClient client = getXmlRpcClient(cfg);
				
				List args = getInitArgs(cfg);
				args.add("hr.salary.expense"); // Remote Object
				args.add("done_salary");
				
				List a = new ArrayList();
				
		        map.put("name", model.getId());
		        map.put("approve_uid", user);
		        map.put("action", action.equals(MainWorkflowConstant.TA_APPROVE) ? "C1" : "W2");
		        map.put("file_name",model.getId()+".pdf");
		        map.put("file_url",NodeUtil.trimNodeRef(model.getDocRef()));
		        map.put("comment",comment);
		        
		        List<Map<String, Object>> atts = new ArrayList<Map<String,Object>>();
		        
		        MainWorkflowHistoryModel wfHisModel = null;
				SqlSession session = DbConnectionFactory.getSqlSessionFactory(dataSource).openSession();
				try {
		            MainWorkflowDAO wfDao = session.getMapper(MainWorkflowDAO.class);
		            MainWorkflowHistoryDAO dao = session.getMapper(MainWorkflowHistoryDAO.class);
		            
		            MainWorkflowModel wfModel = new MainWorkflowModel();
					wfModel.setMasterId(model.getId());
					wfModel = wfDao.getLastWorkflow(wfModel);
		            
					Map<String, Object> params = new HashMap<String, Object>();
		            params.put("masterId", wfModel.getId());
		            
			        wfHisModel = dao.getLastInf(params);
		            
		        } catch (Exception ex) {
					log.error("", ex);
		        } finally {
		        	session.close();
		        }
				
		        /*
		         * Attachment
		         */
		        List<FileModel> fileList = hrSalService.listFile(model.getId());
		        log.info("fileList.size()="+fileList.size());
		        
		        List attachment = new ArrayList();
		        
		        for(FileModel file:fileList) {
		        	if (wfHisModel.getTime().before(file.getTimestamp())) {
				        Map<String,Object> att = new HashMap<String, Object>();
				        att.put("file_name", file.getName());
				        att.put("file_url", NodeUtil.trimNodeRef(file.getNodeRef().toString()));
				        att.put("description", file.getDesc());
				        att.put("attach_by", file.getBy());
				        
				        atts.add(att);
		        	}
		        }
		        
		        map.put("attachments", atts);		        
//		        log.info("map="+map);
		        
				a.add(map);
				args.add(a);
				
				InterfaceUtil.logInfo(args, log);
		//		arguments.add(map);
	
				Object obj = client.invoke("execute_kw", args);
				
				XmlRpcStruct strc = (XmlRpcStruct)obj;
				
				log.info("result.size() : "+strc.keySet().size());
				for(Object k : strc.keySet()) {
					Object v = strc.get(k);
	
					log.info(" - "+k+" : "+v+":"+v.getClass().getName());
				}
				
				if (strc.size()>0) {
					success = (Boolean)strc.get("is_success");
					msgs = (String)strc.get("messages");
				}
			} catch (XmlRpcException ex) {
				ex.printStackTrace();
				log.error(ex);
				throw new FormException(CommonConstant.FORM_ERR+ex.toString());
			} catch (Exception ex) {
				ex.printStackTrace();
				log.error(ex);
				return ex.toString()+":"+map.toString();
			}
		} else {
			success = true;
		}
		
		return success ? "OK" : msgs+":"+map.toString();
	}
	
	public Map<String, Object> getBudgetControlLevel(Timestamp time, String login) throws Exception {
		log.info("sub interface : getBudgetControlLevel");
		
		log.info("  time:"+time.toString());
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		final Properties gProp = CommonUtil.getGlobalProperties();
		String odooPassword = gProp.getProperty(CommonConstant.GP_ODOO_ADMIN_PASSWORD);
		
		Map<String, Object> cfg = getConnectionConfig(odooPassword);
		XmlRpcClient client = getXmlRpcClient(cfg);
		
		List args = getInitArgs(cfg);
		args.add("account.budget"); // Remote Object
		args.add("get_fiscal_and_budget_level");
		
		List a = new ArrayList();
//		Map<String, Object> map = new HashMap<String, Object>();
//		a.add(map);
		
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd",Locale.US);
		
		a.add(df.format(time));
		
		args.add(a);
		
		InterfaceUtil.logInfo(args, log);

		Object obj = client.invoke("execute_kw", args);
		
		
		XmlRpcStruct strc = (XmlRpcStruct)obj;
		
		for(Object k : strc.keySet()) {
			Object v = strc.get(k);

			log.info(" - "+k+" : "+v+":"+v.getClass().getName());
			
			map.put((String)k, v);
		}

		
		return map;
	}
	
	private Map<String, Object> checkBudget(Integer fiscalId,String budgetType,String budgetLevel, Integer resourceId,Double amount, String login) throws Exception {
		log.info("sub interface : checkBudget");
		
		log.info("  fiscalId:"+fiscalId);
		log.info("  budgetType:"+budgetType);
		log.info("  budgetLevel:"+budgetLevel);
		log.info("  resourceId:"+resourceId);
		log.info("  amount:"+amount);
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		final Properties gProp = CommonUtil.getGlobalProperties();
		String odooPassword = gProp.getProperty(CommonConstant.GP_ODOO_ADMIN_PASSWORD);
		
		Map<String, Object> cfg = getConnectionConfig(odooPassword);
		XmlRpcClient client = getXmlRpcClient(cfg);
		
		List args = getInitArgs(cfg);
		args.add("account.budget"); // Remote Object
		args.add("check_budget");
		
		List a = new ArrayList();
//		Map<String, Object> map = new HashMap<String, Object>();
//		a.add(map);
//		
//		map.put("", fiscalId);
//		map.put("", budgetType);
//		map.put("", budgetLevel);
//		map.put("", resourceId);
//		map.put("", amount);
		
		a.add(fiscalId);
		a.add(budgetType);
		a.add(budgetLevel);
		a.add(resourceId);
		a.add(amount);
		
		args.add(a);
		
		InterfaceUtil.logInfo(args, log);

		Object obj = client.invoke("execute_kw", args);
		
		
		XmlRpcStruct strc = (XmlRpcStruct)obj;
		
		for(Object k : strc.keySet()) {
			Object v = strc.get(k);

			log.info(" - "+k+" : "+v+":"+v.getClass().getName());
			
			map.put((String)k, v);
		}
		
		return map;
	}	
	
	public Map<String, Object> checkBudget(String budgetCcType, Integer budgetCc, Double amount, String login) throws Exception {
		log.info("interface : checkBudget");
		log.info(" - budgetCcType : "+budgetCcType);
		log.info(" - budgetCc : "+budgetCc);
		log.info(" - amount : "+amount);
		
		Map<String, Object> budgetLevel = getBudgetControlLevel(new Timestamp(Calendar.getInstance(Locale.US).getTimeInMillis()), login);
//		for(String k : budgetLevel.keySet()) {
//			log.info(" -- "+k+":"+budgetLevel.get(k));
//		}
		
		String budgetType = budgetCcType.equals("P") ? "project_base" : "unit_base";
		Integer resourceId = null;
		if (budgetCcType.equals("P")) {
			budgetType = "project_base";
			resourceId = budgetCc;
		} else {
			budgetType = "unit_base";
			Map<String, Object> section = sectionService.get(budgetCc);
			resourceId = (Integer)section.get((String)budgetLevel.get(budgetType));
		}
		
		log.info(" --- budgetType : "+budgetType);
		log.info(" --- resouceId : "+resourceId);
		
		Map<String, Object> budget = checkBudget(
				(Integer)budgetLevel.get("fiscal_id"),
				budgetType,
				(String)budgetLevel.get(budgetType),
				resourceId,
				amount,
				login
		);
		
		return budget;
	}
}
