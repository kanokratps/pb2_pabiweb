package pb.repo.hr.wscript;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.extensions.webscripts.WebScriptResponse;
import org.springframework.stereotype.Component;

import pb.common.constant.CommonConstant;
import pb.common.util.CommonUtil;
import pb.repo.common.mybatis.DbConnectionFactory;
import pb.repo.hr.service.HrSalService;
import pb.repo.hr.service.HrSalWorkflowService;

import com.github.dynamicextensionsalfresco.webscripts.annotations.RequestParam;
import com.github.dynamicextensionsalfresco.webscripts.annotations.Uri;
import com.github.dynamicextensionsalfresco.webscripts.annotations.WebScript;

@Component
@WebScript
public class HrSalWorkflowWebScript {
	
	private static Logger log = Logger.getLogger(HrSalWorkflowWebScript.class);
	
	private static final String URI_PREFIX = CommonConstant.GLOBAL_URI_PREFIX + "/hr/sal/wf";
	
	@Autowired
	HrSalWorkflowService workflowService;
	
	@Autowired
	HrSalService hrSalService;
	
	@Autowired
	DataSource dataSource;
	
	/*
	 * id = pr id
	 */
	@Uri(URI_PREFIX + "/assignee/list")
	public void handleAssigneeList(@RequestParam final String id,
								   @RequestParam(required=false) final String lang
								, final WebScriptResponse response)
								throws Exception {

		String json = null;

		SqlSession session = DbConnectionFactory.getSqlSessionFactory(dataSource).openSession();

		try {
			workflowService.setModuleService(hrSalService);
			JSONArray jsArr = workflowService.listAssignee(session, id, lang);
			
			json = CommonUtil.jsonSuccess(jsArr);
		} catch (Exception ex) {
			log.error("", ex);
			json = CommonUtil.jsonFail(ex.toString());
			throw ex;
		} finally {
			CommonUtil.responseWrite(response, json);
		}
	}
	
	/*
	 * id = pr id
	 */
	@Uri(URI_PREFIX + "/task/list")
	public void handleTaskList(@RequestParam final String id,
							   @RequestParam final String lang,
							   final WebScriptResponse response)
								throws Exception {

		String json = null;

		try {
			workflowService.setModuleService(hrSalService);
			JSONArray jsArr = workflowService.listTask(id, lang);
			json = CommonUtil.jsonSuccess(jsArr);

		} catch (Exception ex) {
			log.error("", ex);
			json = CommonUtil.jsonFail(ex.toString());
			throw ex;

		} finally {
			CommonUtil.responseWrite(response, json);

		}
	}
	
	/*
	 * id = pr id
	 */
	@Uri(URI_PREFIX + "/dtl/list")
	public void handleDetailList(@RequestParam final String id,
								 @RequestParam(required=false) final String lang
								, final WebScriptResponse response)
								throws Exception {

		String json = null;

		try {
			workflowService.setModuleService(hrSalService);
			JSONArray jsArr = workflowService.listDetail(id, lang);
			json = CommonUtil.jsonSuccess(jsArr);

		} catch (Exception ex) {
			log.error("", ex);
			json = CommonUtil.jsonFail(ex.toString());
			throw ex;

		} finally {
			CommonUtil.responseWrite(response, json);

		}
	}
	
}
