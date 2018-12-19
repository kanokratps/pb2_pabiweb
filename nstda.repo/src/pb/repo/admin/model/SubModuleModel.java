package pb.repo.admin.model;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class SubModuleModel {
	String id;
	
	String docRef;
	String folderRef;
	String fileName;
	
	Integer waitingLevel;
	String workflowInsId;
	
	String isRefId;
	String refId;
	
	Timestamp createdTime;
	String createdBy;
	Timestamp updatedTime;
	String updatedBy;
	
	String status;
	
	List<String> listAttachDoc;

	private Long totalRowCount;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getDocRef() {
		return docRef;
	}
	public void setDocRef(String docRef) {
		this.docRef = docRef;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getFolderRef() {
		return folderRef;
	}
	public void setFolderRef(String folderRef) {
		this.folderRef = folderRef;
	}
	public Integer getWaitingLevel() {
		return waitingLevel;
	}
	public void setWaitingLevel(Integer waitingLevel) {
		this.waitingLevel = waitingLevel;
	}
	public String getWorkflowInsId() {
		return workflowInsId;
	}
	public void setWorkflowInsId(String workflowInsId) {
		this.workflowInsId = workflowInsId;
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
	public Timestamp getUpdatedTime() {
		return updatedTime;
	}
	public void setUpdatedTime(Timestamp updatedTime) {
		this.updatedTime = updatedTime;
	}
	public String getUpdatedBy() {
		return updatedBy;
	}
	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}
	public Long getTotalRowCount() {
		return totalRowCount;
	}
	public void setTotalRowCount(Long totalRowCount) {
		this.totalRowCount = totalRowCount;
	}
	public List<String> getListAttachDoc() {
		return listAttachDoc;
	}
	public void setListAttachDoc(List<String> listAttachDoc) {
		this.listAttachDoc = listAttachDoc;
	}
	public void setAttachDoc(String doc) {
		if(this.listAttachDoc == null) {
			this.listAttachDoc = new ArrayList<String>();
		}
		this.listAttachDoc.add(doc);
	}
	public String getIsRefId() {
		return isRefId;
	}
	public void setIsRefId(String isRefId) {
		this.isRefId = isRefId;
	}
	public String getRefId() {
		return refId;
	}
	public void setRefId(String refId) {
		this.refId = refId;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
}
