package pb.repo.exp.model;

import java.sql.Timestamp;
import java.util.List;

import pb.repo.admin.model.SubModuleModel;

public class ExpUseModel extends SubModuleModel {
	
	String reqBy;
	String reqByName;
	
	String objective;
	String isReason;
	String reason;
	String note;
	
	String isSmallAmount;
	String brwIsSmallAmount;
	
	Integer budgetCc;
	String budgetCcType;
	String budgetCcName;
	String budgetCcTypeName;
	
	Integer fundId;
	String fundName;
	
	Integer costControlTypeId;
	Integer costControlId;
	String costControl;
	Timestamp costControlFrom;
	Timestamp costControlTo;
	String costControlName;
	String costControlTypeName;

	String bankType;
	Integer bank;

	String payType;
	String payTypeName;
	String payDtl1;
	String payDtl2;
	String payDtl3;
	
	String ichargeName;
	String ichargeTypeName;
//	String ichargeActivity;
	
	Double total;
	
	Integer rewarning;
	Integer waitingDay;
	String wfBy;
	String wfByTime;
	String wfStatus;
	
	Double prTotal;
	
	Timestamp requestedTime;
	
	Integer emotion;
	Long infId;
	
	List<ExpUseAttendeeModel> attendeeList;
	List<ExpUseDtlModel> dtlList;
	
	public String getWfBy() {
		return wfBy;
	}
	public void setWfBy(String wfBy) {
		this.wfBy = wfBy;
	}
	public String getWfByTime() {
		return wfByTime;
	}
	public void setWfByTime(String wfByTime) {
		this.wfByTime = wfByTime;
	}
	public String getWfStatus() {
		return wfStatus;
	}
	public void setWfStatus(String wfStatus) {
		this.wfStatus = wfStatus;
	}
	public Integer getRewarning() {
		return rewarning;
	}
	public void setRewarning(Integer rewarning) {
		this.rewarning = rewarning;
	}
	public Integer getWaitingDay() {
		return waitingDay;
	}
	public void setWaitingDay(Integer waitingDay) {
		this.waitingDay = waitingDay;
	}
	public Boolean getOverDue() {
		return (rewarning!=null) && (rewarning > 0) && (waitingDay > rewarning);
	}
	public String getReqBy() {
		return reqBy;
	}
	public void setReqBy(String reqBy) {
		this.reqBy = reqBy;
	}
	public String getObjective() {
		return objective;
	}
	public void setObjective(String objective) {
		this.objective = objective;
	}
	public String getBudgetCcType() {
		return budgetCcType;
	}
	public void setBudgetCcType(String budgetCcType) {
		this.budgetCcType = budgetCcType;
	}
	public Integer getBudgetCc() {
		return budgetCc;
	}
	public void setBudgetCc(Integer budgetCc) {
		this.budgetCc = budgetCc;
	}
	public String getBudgetCcName() {
		return budgetCcName;
	}
	public void setBudgetCcName(String budgetCcName) {
		this.budgetCcName = budgetCcName;
	}
	public String getBudgetCcTypeName() {
		return budgetCcTypeName;
	}
	public void setBudgetCcTypeName(String budgetCcTypeName) {
		this.budgetCcTypeName = budgetCcTypeName;
	}
	public Integer getFundId() {
		return fundId;
	}
	public void setFundId(Integer fundId) {
		this.fundId = fundId;
	}
	public String getFundName() {
		return fundName;
	}
	public void setFundName(String fundName) {
		this.fundName = fundName;
	}
	public Double getTotal() {
		return total;
	}
	public void setTotal(Double total) {
		this.total = total;
	}
	public Integer getCostControlTypeId() {
		return costControlTypeId;
	}
	public void setCostControlTypeId(Integer costControlTypeId) {
		this.costControlTypeId = costControlTypeId;
	}
	public Integer getCostControlId() {
		return costControlId;
	}
	public void setCostControlId(Integer costControlId) {
		this.costControlId = costControlId;
	}
	public List<ExpUseAttendeeModel> getAttendeeList() {
		return attendeeList;
	}
	public void setAttendeeList(List<ExpUseAttendeeModel> attendeeList) {
		this.attendeeList = attendeeList;
	}
	public String getCostControl() {
		return costControl;
	}
	public void setCostControl(String costControl) {
		this.costControl = costControl;
	}
	public Timestamp getCostControlFrom() {
		return costControlFrom;
	}
	public void setCostControlFrom(Timestamp costControlFrom) {
		this.costControlFrom = costControlFrom;
	}
	public Timestamp getCostControlTo() {
		return costControlTo;
	}
	public void setCostControlTo(Timestamp costControlTo) {
		this.costControlTo = costControlTo;
	}
	public String getBankType() {
		return bankType;
	}
	public void setBankType(String bankType) {
		this.bankType = bankType;
	}
	public Integer getBank() {
		return bank;
	}
	public void setBank(Integer bank) {
		this.bank = bank;
	}
	public String getPayType() {
		return payType;
	}
	public void setPayType(String payType) {
		this.payType = payType;
	}
	public String getPayTypeName() {
		return payTypeName;
	}
	public void setPayTypeName(String payTypeName) {
		this.payTypeName = payTypeName;
	}
	public String getPayDtl1() {
		return payDtl1;
	}
	public void setPayDtl1(String payDtl1) {
		this.payDtl1 = payDtl1;
	}
	public String getPayDtl2() {
		return payDtl2;
	}
	public void setPayDtl2(String payDtl2) {
		this.payDtl2 = payDtl2;
	}
	public String getPayDtl3() {
		return payDtl3;
	}
	public void setPayDtl3(String payDtl3) {
		this.payDtl3 = payDtl3;
	}
	public String getIchargeName() {
		return ichargeName;
	}
	public void setIchargeName(String ichargeName) {
		this.ichargeName = ichargeName;
	}
	public String getIchargeTypeName() {
		return ichargeTypeName;
	}
	public void setIchargeTypeName(String ichargeTypeName) {
		this.ichargeTypeName = ichargeTypeName;
	}
//	public String getIchargeActivity() {
//		return ichargeActivity;
//	}
//	public void setIchargeActivity(String ichargeActivity) {
//		this.ichargeActivity = ichargeActivity;
//	}
	public List<ExpUseDtlModel> getDtlList() {
		return dtlList;
	}
	public void setDtlList(List<ExpUseDtlModel> dtlList) {
		this.dtlList = dtlList;
	}
	public String getCostControlName() {
		return costControlName;
	}
	public void setCostControlName(String costControlName) {
		this.costControlName = costControlName;
	}
	public String getCostControlTypeName() {
		return costControlTypeName;
	}
	public void setCostControlTypeName(String costControlTypeName) {
		this.costControlTypeName = costControlTypeName;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public Timestamp getRequestedTime() {
		return requestedTime;
	}
	public void setRequestedTime(Timestamp requestedTime) {
		this.requestedTime = requestedTime;
	}
	public Integer getEmotion() {
		return emotion;
	}
	public void setEmotion(Integer emotion) {
		this.emotion = emotion;
	}
	public String getReqByName() {
		return reqByName;
	}
	public void setReqByName(String reqByName) {
		this.reqByName = reqByName;
	}
	public String getIsReason() {
		return isReason;
	}
	public void setIsReason(String isReason) {
		this.isReason = isReason;
	}
	public String getIsSmallAmount() {
		return isSmallAmount;
	}
	public void setIsSmallAmount(String isSmallAmount) {
		this.isSmallAmount = isSmallAmount;
	}
	public Long getInfId() {
		return infId;
	}
	public void setInfId(Long infId) {
		this.infId = infId;
	}
	public String getBrwIsSmallAmount() {
		return brwIsSmallAmount;
	}
	public void setBrwIsSmallAmount(String brwIsSmallAmount) {
		this.brwIsSmallAmount = brwIsSmallAmount;
	}
	public Double getPrTotal() {
		return prTotal;
	}
	public void setPrTotal(Double prTotal) {
		this.prTotal = prTotal;
	}
	
}
