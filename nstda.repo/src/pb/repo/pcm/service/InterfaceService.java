package pb.repo.pcm.service;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import javax.sql.DataSource;

import org.alfresco.repo.forms.FormException;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pb.common.constant.CommonConstant;
import pb.common.model.FileModel;
import pb.common.util.CommonDateTimeUtil;
import pb.common.util.CommonUtil;
import pb.common.util.InterfaceUtil;
import pb.common.util.NodeUtil;
import pb.repo.admin.constant.MainBudgetSrcConstant;
import pb.repo.admin.constant.MainMasterConstant;
import pb.repo.admin.constant.MainWorkflowConstant;
import pb.repo.admin.dao.MainWorkflowDAO;
import pb.repo.admin.dao.MainWorkflowHistoryDAO;
import pb.repo.admin.model.MainMasterModel;
import pb.repo.admin.model.MainWorkflowHistoryModel;
import pb.repo.admin.model.MainWorkflowModel;
import pb.repo.admin.service.AdminAssetService;
import pb.repo.admin.service.AdminConstructionService;
import pb.repo.admin.service.AdminMasterService;
import pb.repo.admin.service.AdminSectionService;
import pb.repo.admin.service.AdminUserService;
import pb.repo.common.mybatis.DbConnectionFactory;
import pb.repo.pcm.model.PcmOrdModel;
import pb.repo.pcm.model.PcmReqModel;
import redstone.xmlrpc.XmlRpcArray;
import redstone.xmlrpc.XmlRpcClient;
import redstone.xmlrpc.XmlRpcException;
import redstone.xmlrpc.XmlRpcStruct;

@Service
public class InterfaceService {

	private static Logger log = Logger.getLogger(InterfaceService.class);

	@Autowired
	DataSource dataSource;
	
	@Autowired
	PcmReqService pcmReqService;
	
	@Autowired
	PcmOrdService pcmOrdService;
	
	@Autowired
	AdminMasterService masterService;
	
	@Autowired
	AdminSectionService sectionService;
	
	@Autowired
	AdminAssetService assetService;

	@Autowired
	AdminConstructionService constructionService;
	
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
	
	public String createPR(PcmReqModel model) throws Exception {
		log.info("interface : createPR");
		
		Boolean success = false;
		String msgs = null;
		Map<String, Object> map = new HashMap<String, Object>();
		
		final Properties gProp = CommonUtil.getGlobalProperties();
		String odooPassword = gProp.getProperty(CommonConstant.GP_ODOO_ADMIN_PASSWORD);
		
		try {
			Map<String, Object> cfg = getConnectionConfig(odooPassword);
			XmlRpcClient client = getXmlRpcClient(cfg);
			
			List<Map<String, Object>> list = pcmReqService.listForInf(model.getId());
			Map<String, Object> data = list.size() > 0 ? list.get(0) : null;
			
			List args = getInitArgs(cfg);
			args.add("purchase.request"); // Remote Object
			args.add("generate_purchase_request");
			
			List a = new ArrayList();
			
			/*
			 * Header
			 */
	        map.put("name", data.get("req_id"));
	        map.put("requested_by",data.get("req_by"));
	        map.put("responsible_uid",data.get("rp_id")!=null ? data.get("rp_id") : "");
	        map.put("assigned_to",model.getUpdatedBy());
	        map.put("date_approve",CommonDateTimeUtil.convertToOdooFieldDate(new Timestamp(Calendar.getInstance().getTimeInMillis())));
	        map.put("total_budget_value",data.get("total"));
	        map.put("prototype_type",data.get("prototype_type"));
	        map.put("purchase_type_id.id", data.get("method_type")!=null ? data.get("method_type") : "");
	        map.put("purchase_method_id.id",data.get("method_id")!=null ? data.get("method_id") : "");
	        map.put("purchase_unit_id.id",data.get("pcm_section_id"));
	        map.put("description",data.get("reason"));
	        map.put("objective",data.get("objective"));
	        map.put("currency_id.id",data.get("currency_id"));
	        map.put("currency_rate",data.get("currency_rate"));
	        map.put("delivery_address", data.get("location"));
	        map.put("date_start",CommonDateTimeUtil.convertToOdooFieldDate((Timestamp) data.get("requested_time")));
//	        map.put("operating_unit_id.id",data.get("operating_unit_id"));
	        map.put("org_id",data.get("pur_org_id"));
	        map.put("request_ref_id", data.get("ref_id"));
	        map.put("purchase_price_range_id.id", data.get("price_range_id")!=null ? data.get("price_range_id") : "");
	        map.put("purchase_condition_id.id", data.get("condition_id")!=null ? data.get("condition_id") : "");
	        map.put("purchase_condition_detail_id.id", data.get("method_cond2")!=null ? data.get("method_cond2") : "");
	        map.put("purchase_condition_detail", data.get("method_cond2_dtl"));
	        map.put("is_small_amount",data.get("is_small_amount").equals("1") ? "True" : "False");
	        
//	        for(String key : map.keySet()) {
//	        	log.info(" - "+key+":"+map.get(key));
//	        }
	        
	        for(Entry<String, Object> m:map.entrySet()) {
	        	if (m.getValue()==null) {
	        		log.info("**************************** "+m.getKey()+":"+m.getValue());
	        	}
	        }
	        
	        /*
	         * Line Item
	         */
			list = pcmReqService.listDtlForInf(model.getId());
	        
	        List orderLine = new ArrayList();
	        
	        String fundId = data.get("fund_id")!=null ? data.get("fund_id").toString() : "";
	        String contractDate = CommonDateTimeUtil.convertToOdooFieldDate((Timestamp) data.get("contract_date"));
	        
	        for(Map<String, Object> dtl:list) {
		        Map<String, Object> line = new HashMap<String, Object>();
		        line.put("product_id.id","");
		        line.put("activity_rpt_id.id",dtl.get("act_id"));
		        line.put("activity_group_id.id",dtl.get("act_grp_id"));
		        line.put("name",dtl.get("description")!=null ? dtl.get("description") : ""); 
		        line.put("product_qty",dtl.get("quantity")); 
		        line.put("price_unit",dtl.get("price")); ////////
		        line.put("fiscalyear_id",(Integer)dtl.get("fiscal_year") != 0 ? dtl.get("fiscal_year").toString() : ""); ////////
		        line.put("product_uom_id.id",dtl.get("unit_id")); 
		        line.put("date_required", contractDate);
		        if (dtl.get("budget_cc_type").equals(MainBudgetSrcConstant.TYPE_UNIT)) {
		        	line.put("section_id.id",dtl.get("budget_cc"));
		        } else 
		        if (dtl.get("budget_cc_type").equals(MainBudgetSrcConstant.TYPE_PROJECT)) {
		        	line.put("project_id.id",dtl.get("budget_cc"));
		        } else 
		        if (dtl.get("budget_cc_type").equals(MainBudgetSrcConstant.TYPE_ASSET)) {
		        	line.put("invest_asset_id.id",dtl.get("budget_cc"));
		        } else 
		        if (dtl.get("budget_cc_type").equals(MainBudgetSrcConstant.TYPE_CONSTRUCTION)) {
		        	line.put("invest_construction_phase_id.id",dtl.get("budget_cc"));
		        }
		        line.put("cost_control_id.id",dtl.get("cost_control_id")!=null ? dtl.get("cost_control_id") : "");
		        line.put("fixed_asset","False");
		        line.put("tax_ids",dtl.get("vat_name")!=null ? dtl.get("vat_name") : "");
		        line.put("fund_id.id", fundId);
		        
		        orderLine.add(line);
		        
//		        for(String key : line.keySet()) {
//		        	log.info(" -- "+key+":"+line.get(key));
//		        }
		        for(Entry<String, Object> m:line.entrySet()) {
		        	if (m.getValue()==null) {
		        		log.info("**************************** "+m.getKey()+":"+m.getValue());
		        	}
		        }
	        }
	        
	        map.put("line_ids", orderLine);
	
	        /*
	         * Committee
	         */
			list = pcmReqService.listCmtForInf(model.getId());
			
	        List committee = new ArrayList();
	
	        int seq = 1;
	        for(Map<String, Object> cmtData:list) {
		        Map<String, Object> cmt = new HashMap<String, Object>();
		        String title = cmtData.get("title") !=null ? (String)cmtData.get("title") : null;
		        cmt.put("name",(title != null && !title.equals("null") && !title.equals("") ? title+" " : "")+cmtData.get("first_name")+" "+cmtData.get("last_name")); 
		        cmt.put("position",cmtData.get("position"));
		        cmt.put("committee_type_id",cmtData.get("committee_id"));
		        cmt.put("sequence", String.valueOf(seq++));
		        committee.add(cmt);
		        
//		        for(String key : cmt.keySet()) {
//		        	log.info(" --- "+key+":"+cmt.get(key));
//		        }
//		        
		        for(Entry<String, Object> m:cmt.entrySet()) {
		        	if (m.getValue()==null) {
		        		log.info("**************************** "+m.getKey()+":"+m.getValue());
		        	}
		        }
		        
	        }
	        
	        map.put("committee_ids", committee);
	
	        /*
	         * Attachment
	         */
	        List<FileModel> fileList = pcmReqService.listFile(model.getId(), true);
	        log.info("fileList.size()="+fileList.size());
	        
	        List attachment = new ArrayList();
	        
	        for(FileModel file:fileList) {
	// detail : http://localhost:18080/share/page/document-details?nodeRef=workspace://SpacesStore/5190b027-00c6-4322-98c3-1be01314bdd9
	//download format url : http://localhost:18080/share/proxy/alfresco/api/node/content/workspace/SpacesStore/<url>/<name>?a=true
	//example url : http://localhost:18080/share/proxy/alfresco/api/node/content/workspace/SpacesStore/260cdb8d-5a9c-43af-9c8e-178c75f1fe38/PR16000050.pdf?a=true
	        	
		        Map<String, Object> att = new HashMap<String, Object>();
		        att.put("name", file.getName()); 
		        att.put("description", file.getDesc()!=null ? file.getDesc() : ""); 
		        att.put("file_url",NodeUtil.trimNodeRef(file.getNodeRef().toString()));
		        att.put("attach_by", file.getBy());
		        attachment.add(att);
		        
//		        for(String key : att.keySet()) {
//		        	log.info(" ---- "+key+":"+att.get(key));
//		        }
//		        
		        for(Entry<String, Object> m:att.entrySet()) {
		        	if (m.getValue()==null) {
		        		log.info("**************************** "+m.getKey()+":"+m.getValue());
		        	}
		        }
		        
	        }
	        
	        map.put("attachment_ids", attachment);
	        
			a.add(map);
			
			args.add(a);
			
			InterfaceUtil.logInfo(args, log);

			Object obj = client.invoke("execute_kw", args);
			
			XmlRpcStruct strc = (XmlRpcStruct)obj;
			
			for(Object k : strc.keySet()) {
				Object v = strc.get(k);
	
				log.info(" - "+k+" : "+v+":"+v.getClass().getName());
			}
			
			success = (Boolean)strc.get("is_success");
			msgs = strc.get("messages")!=null ? strc.get("messages").toString() : "";
		}
		catch (Exception ex) {
			ex.printStackTrace();
			log.error(ex);
			return ex.toString()+":"+map.toString();
		}
		
		return success ? "OK" : msgs+":"+map.toString();
	}
	
	public String updateStatusPD(PcmOrdModel model, String action, String user, String comment, String login) throws Exception {
		log.info("interface : updateStatusPD");
		
		MainMasterModel cfgModel = masterService.getSystemConfig(MainMasterConstant.SCC_MAIN_INF_PD_UPDATE_STATUS);
		
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
				args.add("purchase.requisition"); // Remote Object
				args.add("done_order");
				
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
		        List<FileModel> fileList = pcmOrdService.listFile(model.getId());
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
	
					if(k.equals("attachments")) {
						log.info(" - "+k+" : "+v.getClass().getName());
						
						XmlRpcArray arr = (XmlRpcArray)v;
						Map<String, Object> doc = (Map<String, Object>)arr.get(0);

						log.info("   "+doc.get("name"));
						
						pcmOrdService.updateFinalDoc(model, doc);
					} else {
						log.info(" - "+k+" : "+v+":"+v.getClass().getName());
					}
				}
				
				if (strc.size()>0) {
					success = (Boolean)strc.get("is_success");
					msgs = strc.get("messages")!=null ? strc.get("messages").toString() : "";
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
