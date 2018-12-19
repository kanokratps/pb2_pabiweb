package pb.repo.pcm.model;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class PcmReqCmtHdrModel {
	
	Long id;
	String pcmReqId;
	String committee;
	Integer committeeId;
	
	Timestamp createdTime;
	String createdBy;
	Timestamp updatedTime;
	String updatedBy;
	
	List<PcmReqCmtDtlModel> dtlList;

	private Long totalRowCount;

	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getPcmReqId() {
		return pcmReqId;
	}
	public void setPcmReqId(String pcmReqId) {
		this.pcmReqId = pcmReqId;
	}
	public String getCommittee() {
		return committee;
	}
	public void setCommittee(String committee) {
		this.committee = committee;
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
	public List<PcmReqCmtDtlModel> getDtlList() {
		if (dtlList==null) dtlList = new ArrayList<PcmReqCmtDtlModel>();
		return dtlList;
	}
	public void setDtlList(List<PcmReqCmtDtlModel> dtlList) {
		this.dtlList = dtlList;
	}
	public Integer getCommitteeId() {
		return committeeId;
	}
	public void setCommitteeId(Integer committeeId) {
		this.committeeId = committeeId;
	}
	
}
