package pb.repo.pcm.wscript;

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
import pb.repo.pcm.service.PcmOrdService;
import pb.repo.pcm.service.PcmOrdWorkflowService;

import com.github.dynamicextensionsalfresco.webscripts.annotations.RequestParam;
import com.github.dynamicextensionsalfresco.webscripts.annotations.Uri;
import com.github.dynamicextensionsalfresco.webscripts.annotations.WebScript;

@Component
@WebScript
public class PcmOrdWorkflowWebScript {
	
	private static Logger log = Logger.getLogger(PcmOrdWorkflowWebScript.class);
	
	private static final String URI_PREFIX = CommonConstant.GLOBAL_URI_PREFIX + "/pcm/ord/wf";
	
	@Autowired
	PcmOrdWorkflowService workflowService;
	
	@Autowired
	PcmOrdService pcmOrdService;
	
	@Autowired
	DataSource dataSource;
	/*
	 * id = pd id
	 */
	@Uri(URI_PREFIX + "/assignee/list")
	public void handleAssigneeList(@RequestParam final String id,
								   @RequestParam(required=false) final String lang
								, final WebScriptResponse response)
								throws Exception {

		String json = null;

		SqlSession session = DbConnectionFactory.getSqlSessionFactory(dataSource).openSession();
		try {
			workflowService.setModuleService(pcmOrdService);
			JSONArray jsArr = workflowService.listAssignee(session, id,lang);
			
			json = CommonUtil.jsonSuccess(jsArr);
		} catch (Exception ex) {
			log.error("", ex);
			json = CommonUtil.jsonFail(ex.toString());
			throw ex;
		} finally {
			session.close();
			CommonUtil.responseWrite(response, json);
		}
	}
	
	/*
	 * id = memo id
	 */
	@Uri(URI_PREFIX + "/task/list")
	public void handleTaskList(@RequestParam final String id,
							   @RequestParam final String lang,
							   final WebScriptResponse response)
								throws Exception {

		String json = null;

		try {
			workflowService.setModuleService(pcmOrdService);
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
	 * id = memo id
	 */
	@Uri(URI_PREFIX + "/dtl/list")
	public void handleDetailList(@RequestParam final String id,
								 @RequestParam(required=false) final String lang
								, final WebScriptResponse response)
								throws Exception {

		String json = null;

		try {
			workflowService.setModuleService(pcmOrdService);
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
