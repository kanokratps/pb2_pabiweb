package pb.repo.admin.constant;

import java.util.LinkedHashMap;
import java.util.Map;


public class PcmReqConstant extends SubModuleConstant {

	public static final String TABLE_NAME = "pb2_pcm_req";
	
    /*
     * Table Field Name
     */
	public static final String TFN_TOTAL = "TOTAL";
	public static final String TFN_TOTAL_CNV = "TOTAL_CNV";
	
	public static final String TFN_REQ_BY = "REQ_BY"; 
	public static final String TFN_REQ_SECTION_ID = "REQ_SECTION_ID"; 

	public static final String TFN_OBJECTIVE_TYPE = "OBJECTIVE_TYPE"; // buy, employ, hire
	public static final String TFN_OBJECTIVE_TYPE_NAME = "OBJECTIVE_TYPE_NAME"; // buy, employ, hire
	public static final String TFN_OBJECTIVE = "OBJECTIVE";
	public static final String TFN_REASON = "REASON";
	
	public static final String TFN_CURRENCY = "CURRENCY"; // THB, USD, EUR, JPY, CNY
	public static final String TFN_CURRENCY_RATE = "CURRENCY_RATE";
	
	public static final String TFN_BUDGET_CC = "BUDGET_CC";
	public static final String TFN_BUDGET_CC_NAME = "BUDGET_CC_NAME";
	public static final String TFN_BUDGET_CC_TYPE = "BUDGET_CC_TYPE";
	public static final String TFN_FUND_ID = "FUND_ID";
	
	public static final String TFN_IS_STOCK = "IS_STOCK";
	public static final String TFN_STOCK_SECTION_ID = "STOCK_SECTION_ID";

	public static final String TFN_IS_PROTOTYPE = "IS_PROTOTYPE";
	public static final String TFN_PROTOTYPE_TYPE = "PROTOTYPE_TYPE";
	public static final String TFN_PROTOTYPE_NO = "PROTOTYPE_NO";
	
	public static final String TFN_COST_CONTROL_TYPE_ID = "COST_CONTROL_TYPE_ID";
	public static final String TFN_COST_CONTROL_ID = "COST_CONTROL_ID";
	
	public static final String TFN_PCM_SECTION_ID = "PCM_SECTION_ID";
	public static final String TFN_LOCATION = "LOCATION";
	
	public static final String TFN_CONTRACT_DATE = "CONTRACT_DATE";
	
	public static final String TFN_IS_SMALL_AMOUNT = "IS_SMALL_AMOUNT";
	
	public static final String TFN_IS_ACROSS_BUDGET = "IS_ACROSS_BUDGET";
	public static final String TFN_ACROSS_BUDGET = "ACROSS_BUDGET";
	
	public static final String TFN_IS_REF_ID = "IS_REF_ID";
	public static final String TFN_REF_ID = "REF_ID";
	
	public static final String TFN_PRWEB_METHOD_ID = "PRWEB_METHOD_ID";
	public static final String TFN_METHOD_COND2_RULE = "METHOD_COND2_RULE";
	public static final String TFN_METHOD_COND2 = "METHOD_COND2";
	public static final String TFN_METHOD_COND2_DTL = "METHOD_COND2_DTL";
	
	public static final String TFN_VAT = "VAT";
	public static final String TFN_VAT_ID = "VAT_ID";
	
	public static final String TFN_WF_BY = "WF_BY";
	public static final String TFN_WF_STATUS = "WF_STATUS";
	public static final String TFN_WF_BY_TIME = "WF_BY_TIME";
	
	public static final String TFN_REQUESTED_TIME = "REQUESTED_TIME";
	
    /*
     * JSON Field Name
     */
	public static final String JFN_ID = "id";
	
	public static final String JFN_TOTAL = "total";
	public static final String JFN_TOTAL_CNV = "totalcnv";
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

	public static final String JFN_OBJECTIVE_TYPE = "objective_type"; // buy, employ, hire
	public static final String JFN_OBJECTIVE_TYPE_NAME = "objective_type_name"; // buy, employ, hire
	public static final String JFN_OBJECTIVE = "objective";
	public static final String JFN_REASON = "reason";
	public static final String JFN_REASON_OTH = "reasonOth";
	
	public static final String JFN_CURRENCY = "currency"; // THB, USD, EUR, JPY, CNY
	public static final String JFN_CURRENCY_RATE = "currency_rate";
	
	public static final String JFN_BUDGET_CC = "budget_cc";
	public static final String JFN_BUDGET_CC_NAME = "budget_cc_name";
	public static final String JFN_BUDGET_CC_TYPE = "budget_cc_type";
	public static final String JFN_BUDGET_CC_TYPE_NAME = "budget_cc_type_name";
	public static final String JFN_FUND_ID = "fund_id";
	public static final String JFN_FUND_NAME = "fund_name";
	
	public static final String JFN_IS_STOCK = "is_stock";
	public static final String JFN_STOCK_SECTION_ID = "stock_ou";

	public static final String JFN_IS_PROTOTYPE = "is_prototype";
	public static final String JFN_PROTOTYPE_TYPE = "prototype_type";
	public static final String JFN_PROTOTYPE_NO = "prototype_no";
	
	public static final String JFN_COST_CONTROL_TYPE_ID = "cost_control_type_id";
	public static final String JFN_COST_CONTROL_TYPE_NAME = "cost_control_type_name";
	public static final String JFN_COST_CONTROL_ID = "cost_control_id";
	public static final String JFN_COST_CONTROL_NAME = "cost_control_name";
	
	public static final String JFN_PCM_SECTION_ID = "pcm_ou";
	public static final String JFN_LOCATION = "location";
	
	public static final String JFN_CONTRACT_DATE = "contract_date";
	
	public static final String JFN_IS_SMALL_AMOUNT = "is_small_amount";
	
	public static final String JFN_IS_ACROSS_BUDGET = "is_across_budget";
	public static final String JFN_ACROSS_BUDGET = "across_budget";
	
	public static final String JFN_IS_REF_ID = "is_ref_id";
	public static final String JFN_REF_ID = "ref_id";
	
	public static final String JFN_RID = "rid";
	
	public static final String JFN_PRWEB_METHOD_ID = "prweb_method_id";
	public static final String JFN_METHOD_COND2_RULE = "method_cond2_rule";
	public static final String JFN_METHOD_COND2 = "method_cond2";
	public static final String JFN_METHOD_COND2_DTL = "method_cond2_dtl";
	
	public static final String JFN_VAT = "vat";
	public static final String JFN_VAT_ID = "vat_id";
	public static final String JFN_PRICE_INCLUDE = "price_include";
	
	public static final String JFN_ACTION = "action";
	public static final String JFN_RUNNING_NO = "running_no";
	public static final String JFN_FISCAL_YEAR = "fiscal_year";
	
	public static final String JFN_CREATED_BY_SHOW = "created_by_show";
	public static final String JFN_CREATED_TIME_SHOW = "created_time_show";
	public static final String JFN_TEL_NO = "tel_no";

	public static final String JFN_FILE_NAME = "file_name";
	
	public static final String JFN_REQUESTED_TIME = "requested_time";
	public static final String JFN_REQUESTED_TIME_SHOW = "requested_time_show";
	
	/*
	 * Status
	 */
	public static final String ST_DRAFT = "D";
	public static final String ST_WAITING = "W1";
	public static final String ST_WAITING_REJECT = "W2";
	public static final String ST_CLOSED_BY_ACT = "C1";
	public static final String ST_CLOSED_BY_PCM = "C2";
	public static final String ST_CLOSED_BY_PCM_DIY = "C3";
	public static final String ST_CANCEL_BY_REQ = "X1";
	public static final String ST_CANCEL_BY_PCM = "X2";
	public static final String ST_CONSULT = "S";
	
	public static final Map<String, Object> DISPLAY_TYPES = new LinkedHashMap<String, Object>();
	
    static {
    	DISPLAY_TYPES.put(ST_DRAFT, "Draft");
    	DISPLAY_TYPES.put(ST_WAITING, "Waiting Approver");
    	DISPLAY_TYPES.put(ST_WAITING_REJECT, "Approver Reject");
    	DISPLAY_TYPES.put(ST_CLOSED_BY_ACT, "Waiting Procurement");
    	DISPLAY_TYPES.put(ST_CLOSED_BY_PCM, "Procurement Accepted");
    	DISPLAY_TYPES.put(ST_CANCEL_BY_REQ, "Cancel by Requester");
    	DISPLAY_TYPES.put(ST_CANCEL_BY_PCM, "Cancel by Procurement");
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
    	
    	WF_TASK_ACTIONS.put(MainWorkflowConstant.TA_COMPLETE, "Procurement");
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
    	
    	WF_TASK_ACTIONS_TH.put(MainWorkflowConstant.TA_COMPLETE, "ส่งงานให้พัสดุ");
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
    public static final String WF_NAME = "NSTDAPcmPR";
    
    /*
     * Next Actor
     */
//    public static final String NA_BOSS = "หัวหน้าพัสดุ";
//    public static final String NA_OFFICER = "เจ้าหน้าที่พัสดุ";
    
    /*
     * Budget CC Type
     */
//    public static final String BCCT_UNIT = "U";
//    public static final String BCCT_PROJECT = "P";
    
    /*
     * Jasper Report
     */
    public static final String JR_PR = "pr";
    
}
