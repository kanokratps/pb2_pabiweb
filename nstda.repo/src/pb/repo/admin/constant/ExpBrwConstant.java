package pb.repo.admin.constant;

import java.util.LinkedHashMap;
import java.util.Map;

public class ExpBrwConstant extends SubModuleConstant {

	public static final String TABLE_NAME = "pb2_exp_brw";
	
    /*
     * Table Field Name
     */
	public static final String TFN_TOTAL = "TOTAL";
	
	public static final String TFN_REQ_BY = "REQ_BY";

	public static final String TFN_OBJECTIVE_TYPE = "OBJECTIVE_TYPE";
	public static final String TFN_OBJECTIVE_TYPE_NAME = "OBJECTIVE_TYPE_NAME";
	public static final String TFN_OBJECTIVE = "OBJECTIVE";
	public static final String TFN_AV_REMARK = "AV_REMARK";
	public static final String TFN_REASON = "REASON";
	public static final String TFN_NOTE = "NOTE";
	
	public static final String TFN_IS_SMALL_AMOUNT = "IS_SMALL_AMOUNT";
	public static final String TFN_ORIGIN_PR_NUMBER = "ORIGIN_PR_NUMBER";
	
	public static final String TFN_IS_REASON = "IS_REASON";
	
	public static final String TFN_BUDGET_CC = "BUDGET_CC";
	public static final String TFN_BUDGET_CC_NAME = "BUDGET_CC_NAME";
	public static final String TFN_BUDGET_CC_TYPE = "BUDGET_CC_TYPE";
	public static final String TFN_FUND_ID = "FUND_ID";
	
	public static final String TFN_COST_CONTROL_TYPE_ID = "COST_CONTROL_TYPE_ID";
	public static final String TFN_COST_CONTROL_ID = "COST_CONTROL_ID";
	
	public static final String TFN_DATE_BACK = "DATE_BACK";
	
	public static final String TFN_BANK_TYPE = "BANK_TYPE";
	public static final String TFN_BANK = "BANK";
	
	public static final String TFN_WF_BY = "WF_BY";
	public static final String TFN_WF_STATUS = "WF_STATUS";
	public static final String TFN_WF_BY_TIME = "WF_BY_TIME";
	
	public static final String TFN_REQUESTED_TIME = "REQUESTED_TIME";
	
    /*
     * JSON Field Name
     */
	public static final String JFN_ID = "id";
	
	public static final String JFN_TOTAL = "total";
	public static final String JFN_TOTAL_SHOW = "total_show";
	public static final String JFN_WF_STATUS = "wfStatus";
	public static final String JFN_OVER_DUE = "overDue";
	
	public static final String JFN_REQ_BY = "req_by"; 
	public static final String JFN_REQ_BY_NAME = "req_by_name";
	public static final String JFN_REQ_TEL_NO = "req_tel_no";
	public static final String JFN_REQ_BY_DEPT = "req_by_dept"; 
	
	public static final String JFN_REQ_BU = "req_bu"; 
	
	public static final String JFN_REQ_SECTION_ID = "req_ou"; 
	public static final String JFN_REQ_SECTION_NAME = "req_ou_name";

	public static final String JFN_OBJECTIVE_TYPE = "objective_type";
	public static final String JFN_OBJECTIVE_TYPE_NAME = "objective_type_name";
	public static final String JFN_OBJECTIVE = "objective";
	public static final String JFN_AV_REMARK = "av_remark";
	public static final String JFN_REASON = "reason";
	public static final String JFN_NOTE = "note";
	
	public static final String JFN_BRW_IS_SMALL_AMOUNT = "brw_is_small_amount";
	public static final String JFN_IS_SMALL_AMOUNT = "is_small_amount";
	public static final String JFN_ORIGIN_PR_NUMBER = "origin_pr_number";
	
	public static final String JFN_IS_REASON = "is_reason";
	
	public static final String JFN_BUDGET_CC = "budget_cc";
	public static final String JFN_BUDGET_CC_NAME = "budget_cc_name";
	public static final String JFN_BUDGET_CC_TYPE = "budget_cc_type";
	public static final String JFN_BUDGET_CC_TYPE_NAME = "budget_cc_type_name";
	
	public static final String JFN_FUND_ID = "fund_id";
	public static final String JFN_FUND_NAME = "fund_name";
	
	public static final String JFN_COST_CONTROL_TYPE_ID = "cost_control_type_id";
	public static final String JFN_COST_CONTROL_ID = "cost_control_id";
	public static final String JFN_COST_CONTROL_TYPE_NAME = "cost_control_type_name";
	public static final String JFN_COST_CONTROL_NAME = "cost_control_name";
	
	public static final String JFN_DATE_BACK = "date_back";
	
	public static final String JFN_BANK_TYPE = "bank_type";
	public static final String JFN_BANK = "bank";
	
	public static final String JFN_PR_TOTAL = "pr_total";

	public static final String JFN_ACTION = "action";
	public static final String JFN_RUNNING_NO = "running_no";
	public static final String JFN_FISCAL_YEAR = "fiscal_year";
	
	public static final String JFN_CREATED_BY_SHOW = "created_by_show";
	public static final String JFN_CREATED_TIME_SHOW = "created_time_show";
	public static final String JFN_TEL_NO = "tel_no";
	
	public static final String JFN_REQUESTED_TIME = "requested_time";
	public static final String JFN_REQUESTED_TIME_SHOW = "requested_time_show";

	
	/*
	 * Status
	 */
	public static final String ST_DRAFT = "D";
	public static final String ST_WAITING = "W1";
	public static final String ST_WAITING_REJECT = "W2";
	public static final String ST_CLOSED_BY_ACT = "C1";
	public static final String ST_CLOSED_BY_FIN = "C2";
	public static final String ST_PAID_BY_FIN = "C3";
	public static final String ST_CANCEL_BY_REQ = "X1";
	public static final String ST_CANCEL_BY_FIN = "X2";
	public static final String ST_CONSULT = "S";
	
	public static final Map<String, Object> DISPLAY_TYPES = new LinkedHashMap<String, Object>();
	
    static {
    	DISPLAY_TYPES.put(ST_DRAFT, "Draft");
    	DISPLAY_TYPES.put(ST_WAITING, "Waiting Approver");
    	DISPLAY_TYPES.put(ST_WAITING_REJECT, "Approver Reject");
    	DISPLAY_TYPES.put(ST_CLOSED_BY_ACT, "Waiting Accounting");
    	DISPLAY_TYPES.put(ST_CLOSED_BY_FIN, "Finance Accepted");
    	DISPLAY_TYPES.put(ST_PAID_BY_FIN, "Finance Paid");
    	DISPLAY_TYPES.put(ST_CANCEL_BY_REQ, "Cancel by Requester");
    	DISPLAY_TYPES.put(ST_CANCEL_BY_FIN, "Cancel by Finance");
    	DISPLAY_TYPES.put(ST_CONSULT, "Consult");
    }
    
    /*
     * Task Action
     */
	public static final Map<String, String> WF_TASK_ACTIONS = new LinkedHashMap<String, String>();
    static {
    	WF_TASK_ACTIONS.put(MainWorkflowConstant.TA_START, "Started");
		
    	WF_TASK_ACTIONS.put(MainWorkflowConstant.TA_APPROVE, "Approved");
    	WF_TASK_ACTIONS.put(MainWorkflowConstant.TA_REJECT, "Rejected");
    	WF_TASK_ACTIONS.put(MainWorkflowConstant.TA_CONSULT, "Consulted");
    	
    	WF_TASK_ACTIONS.put(MainWorkflowConstant.TA_COMMENT, "Commented");
    	
    	WF_TASK_ACTIONS.put(MainWorkflowConstant.TA_RESUBMIT, "Resubmitted");
    	WF_TASK_ACTIONS.put(MainWorkflowConstant.TA_CANCEL, "Cancelled");
    	
    	WF_TASK_ACTIONS.put(MainWorkflowConstant.TA_COMPLETE, "Finance");
    	
    	WF_TASK_ACTIONS.put(MainWorkflowConstant.TA_PAID, "Paid");
    }
    
	public static final Map<String, String> WF_TASK_ACTIONS_TH = new LinkedHashMap<String, String>();
    static {
    	WF_TASK_ACTIONS_TH.put(MainWorkflowConstant.TA_START, "ขออนุมัติ");
		
    	WF_TASK_ACTIONS_TH.put(MainWorkflowConstant.TA_APPROVE, "อนุมัติ");
    	WF_TASK_ACTIONS_TH.put(MainWorkflowConstant.TA_REJECT, "ไม่อนุมัติ");
    	WF_TASK_ACTIONS_TH.put(MainWorkflowConstant.TA_CONSULT, "ขอคำปรึกษา");
    	
    	WF_TASK_ACTIONS_TH.put(MainWorkflowConstant.TA_COMMENT, "ให้ความเห็น");
    	
    	WF_TASK_ACTIONS_TH.put(MainWorkflowConstant.TA_RESUBMIT, "ขออนุมัติใหม่");
    	WF_TASK_ACTIONS_TH.put(MainWorkflowConstant.TA_CANCEL, "ยกเลิก");
    	
    	WF_TASK_ACTIONS_TH.put(MainWorkflowConstant.TA_COMPLETE, "ส่งงานให้การเงิน");
    	
    	WF_TASK_ACTIONS_TH.put(MainWorkflowConstant.TA_PAID, "จ่ายเงิน");
    	
    }    
    
    /*
     * ACTION
     */
    public static final String ACTION_COPY = "C";
    public static final String ACTION_EDIT = "E";
    public static final String ACTION_DELETE = "D";
    public static final String ACTION_SHOW_DIAGRAM = "S";
    public static final String ACTION_GOTO_FOLDER = "F";
    public static final String ACTION_SHOW_HISTORY = "H";
    public static final String ACTION_SHOW_DETAIL = "V";
    public static final String ACTION_VIEW = "W";

    /*
     * Workflow Name
     */
    public static final String WF_NAME = "NSTDAExpAV";
    
    /*
     * Next Actor
     */
//    public static final String NA_OFFICER = "เจ้าหน้าที่การเงิน";
    
    /*
     * Budget CC Type
     */
//    public static final String BCCT_UNIT = "U";
//    public static final String BCCT_PROJECT = "P";
    
    /*
     * Jasper Report
     */
    public static final String JR_AV = "av";
}
