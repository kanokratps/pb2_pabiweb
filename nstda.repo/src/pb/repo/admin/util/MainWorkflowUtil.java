package pb.repo.admin.util;

import org.activiti.engine.delegate.DelegateTask;
import org.apache.log4j.Logger;
import org.json.JSONObject;

import pb.repo.admin.constant.MainWorkflowConstant;

public class MainWorkflowUtil {
	
	private static Logger log = Logger.getLogger(MainWorkflowUtil.class);
	
	public static JSONObject createAssigneeGridModel(int id, String assignee, String user, String group, Boolean isRqAp, String color)  throws Exception {
		JSONObject jsObj = new JSONObject();
		
		jsObj.put("id", id);
		jsObj.put("assignee", assignee);
		jsObj.put("group", MainUtil.trimComma(group));
		
		user = (MainUtil.trimComma(user)!=null ? MainUtil.trimComma(user) : "");
		
		jsObj.put("user", ((isRqAp!=null && isRqAp) ? "[Requester]"+(!user.equals("") ? "," : "") : "") + user);
		
		jsObj.put("color", color);
		
		return jsObj;
	}
	
	public static String getFinalTaskKey(String taskKey,Integer level) {
		return taskKey + (taskKey.equals(MainWorkflowConstant.TN_REVIEWER) ? (" #"+level) : "");
	}
	
	public static String appendTaskKey(String taskHistory, String taskKey, Integer level) {
		String finalTaskHistory = taskHistory + "|" + getFinalTaskKey(taskKey, level);
		log.info("  taskHistory:" + finalTaskHistory);
		return finalTaskHistory;
	}
	
	public static void logTask(Logger log, DelegateTask task) {
		log.info("  task.definitionKey:" + task.getTaskDefinitionKey()
				+", id:"+task.getId()
				+", Desc:"+task.getDescription()
				+", Event:"+task.getEventName()
				+", Name:"+task.getName()
				+", Owner:"+task.getOwner());
	}
}
