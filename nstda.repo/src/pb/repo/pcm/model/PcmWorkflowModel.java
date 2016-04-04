package pb.repo.pcm.model;

import java.sql.Timestamp;

public class PcmWorkflowModel {

	Long id;
	String type;
	String masterId;
	String workflowInsId;
	String status;
	String assignee;
	String by;
	Timestamp byTime;
	String taskId;
	Timestamp createdTime;
	String createdBy;
	
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getMasterId() {
		return masterId;
	}
	public void setMasterId(String masterId) {
		this.masterId = masterId;
	}
	public String getWorkflowInsId() {
		return workflowInsId;
	}
	public void setWorkflowInsId(String workflowInsId) {
		this.workflowInsId = workflowInsId;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getAssignee() {
		return assignee;
	}
	public void setAssignee(String assignee) {
		this.assignee = assignee;
	}
	public String getBy() {
		return by;
	}
	public void setBy(String by) {
		this.by = by;
	}
	public Timestamp getByTime() {
		return byTime;
	}
	public void setByTime(Timestamp byTime) {
		this.byTime = byTime;
	}
	public String getTaskId() {
		return taskId;
	}
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	public Timestamp getCreatedTime() {
		return createdTime;
	}
	public void setCreatedTime(Timestamp createdTime) {
		this.createdTime = createdTime;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	
	
	
	
}
