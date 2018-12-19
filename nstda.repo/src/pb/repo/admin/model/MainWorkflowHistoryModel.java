package pb.repo.admin.model;

import java.sql.Timestamp;

import org.json.JSONObject;

public class MainWorkflowHistoryModel {

	Long id;
	Long masterId;
	Timestamp time;
	String by;
	String action;
	String task;
	String actionTh;
	String taskTh;
	String comment;
	Integer level;
	String status;
	
    public MainWorkflowHistoryModel() {
    }
	
    public MainWorkflowHistoryModel(JSONObject jobj) throws Exception {
        time = (Timestamp)jobj.get("time");
        by = jobj.getString("by");
        action = jobj.getString("action");
        task = jobj.getString("task");
        comment = jobj.getString("comment");
        status = jobj.getString("status");
    }
    
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getMasterId() {
		return masterId;
	}
	public void setMasterId(Long masterId) {
		this.masterId = masterId;
	}
	public Timestamp getTime() {
		return time;
	}
	public void setTime(Timestamp time) {
		this.time = time;
	}
	public String getBy() {
		return by;
	}
	public void setBy(String by) {
		this.by = by;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public String getTask() {
		return task;
	}
	public void setTask(String task) {
		this.task = task;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public Integer getLevel() {
		return level;
	}
	public void setLevel(Integer level) {
		this.level = level;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}

	public String getActionTh() {
		return actionTh;
	}

	public void setActionTh(String actionTh) {
		this.actionTh = actionTh;
	}

	public String getTaskTh() {
		return taskTh;
	}

	public void setTaskTh(String taskTh) {
		this.taskTh = taskTh;
	}
	
}
