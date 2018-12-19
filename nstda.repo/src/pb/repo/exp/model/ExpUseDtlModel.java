package pb.repo.exp.model;

import java.sql.Timestamp;

import pb.common.model.BaseDataModel;

public class ExpUseDtlModel extends BaseDataModel {
	
	Long id;
	String masterId;
	String activity;
	Integer actId;
	String actName;
	Integer actGrpId;
	String actGrpName;
	Integer assetRuleId;
	String assetName;
	String condition1;
	String condition2;
	String position;
	String uom;
	Double amount;
	String specialWorkflow;
	
	Timestamp createdTime;
	String createdBy;
	Timestamp updatedTime;
	String updatedBy;

	private Long totalRowCount;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public String getMasterId() {
		return masterId;
	}

	public void setMasterId(String masterId) {
		this.masterId = masterId;
	}
	
	public String getActivity() {
		return activity;
	}

	public void setActivity(String activity) {
		this.activity = activity;
	}

	public Integer getActId() {
		return actId;
	}

	public void setActId(Integer actId) {
		this.actId = actId;
	}

	public String getActName() {
		return actName;
	}

	public void setActName(String actName) {
		this.actName = actName;
	}

	public Integer getActGrpId() {
		return actGrpId;
	}

	public void setActGrpId(Integer actGrpId) {
		this.actGrpId = actGrpId;
	}

	public String getActGrpName() {
		return actGrpName;
	}

	public void setActGrpName(String actGrpName) {
		this.actGrpName = actGrpName;
	}

	public String getCondition1() {
		return condition1;
	}

	public void setCondition1(String condition1) {
		this.condition1 = condition1;
	}

	public String getCondition2() {
		return condition2;
	}

	public void setCondition2(String condition2) {
		this.condition2 = condition2;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public String getUom() {
		return uom;
	}

	public void setUom(String uom) {
		this.uom = uom;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}
	
	public String getSpecialWorkflow() {
		return specialWorkflow;
	}

	public void setSpecialWorkflow(String specialWorkflow) {
		this.specialWorkflow = specialWorkflow;
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

	public String getAssetName() {
		return assetName;
	}

	public void setAssetName(String assetName) {
		this.assetName = assetName;
	}

	public Integer getAssetRuleId() {
		return assetRuleId;
	}

	public void setAssetRuleId(Integer assetRuleId) {
		this.assetRuleId = assetRuleId;
	}

}
