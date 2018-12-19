package pb.repo.hr.constant;

import java.util.LinkedHashMap;
import java.util.Map;

import pb.repo.admin.constant.MainWorkflowConstant;
import pb.repo.admin.constant.SubModuleConstant;


public class HrSalConstant extends SubModuleConstant {

	public static final String TABLE_NAME = "pb2_hr_salary";
	
    /*
     * Table Field Name
     */
	public static final String TFN_OBJECTIVE = "OBJECTIVE";
	public static final String TFN_SECTION_ID = "SECTION_ID";
	public static final String TFN_APP_BY = "APP_BY";
	public static final String TFN_DOC_TYPE = "DOC_TYPE";
	
	public static final String TFN_TOTAL = "TOTAL";
	
	public static final String TFN_WF_BY = "WF_BY";
	public static final String TFN_WF_STATUS = "WF_STATUS";
	public static final String TFN_WF_BY_TIME = "WF_BY_TIME";
	
	public static final String TFN_ORG_NAME = "ORG_NAME";
	
    /*
     * JSON Field Name
     */
	public static final String JFN_OBJECTIVE = "objective";
	public static final String JFN_SECTION_ID = "section_id";
	
	public static final String JFN_APP_BY = "app_by";
	public static final String JFN_REQ_BY_NAME = "req_by_name";
	
	public static final String JFN_DOC_TYPE = "doc_type";
	public static final String JFN_ORG_NAME = "org_name";
	
	public static final String JFN_TOTAL = "total";
	public static final String JFN_TOTAL_SHOW = "total_show"; 
	public static final String JFN_WF_STATUS = "wfStatus";
	public static final String JFN_ACTION = "action";
	
	public static final String JFN_FISCAL_YEAR = "fiscal_year";
	
	public static final String JFN_CREATED_TIME_SHOW = "created_time_show";


	/*
	 * Status
	 */
	public static final String ST_WAITING = "W1";
	public static final String ST_WAITING_REJECT = "W2";
	public static final String ST_CLOSED_BY_ACT = "C1";
	public static final String ST_CANCEL_BY_PCM = "X1";
	public static final String ST_CONSULT = "S";
	
	public static final Map<String, Object> DISPLAY_TYPES = new LinkedHashMap<String, Object>();
	
    static {
    	DISPLAY_TYPES.put(ST_WAITING, "Waiting Approver");
    	DISPLAY_TYPES.put(ST_WAITING_REJECT, "Approver Reject");
    	DISPLAY_TYPES.put(ST_CLOSED_BY_ACT, "PO");
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
    	
    	WF_TASK_ACTIONS.put(MainWorkflowConstant.TA_COMPLETE, "PO");
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
    	
    	WF_TASK_ACTIONS_TH.put(MainWorkflowConstant.TA_COMPLETE, "PO");
    }    
    
    /*
     * Task Caption
     */
	public static final Map<String, String> WF_TASK_CAPTIONS = new LinkedHashMap<String, String>();
    static {
    	WF_TASK_CAPTIONS.put(MainWorkflowConstant.TA_START, "Start");
    }
    
	public static final Map<String, String> WF_TASK_CAPTIONS_TH = new LinkedHashMap<String, String>();
    static {
    	WF_TASK_CAPTIONS_TH.put(MainWorkflowConstant.TA_START, "ขออนุมัติ");
    }    

    /*
     * ACTION
     */
    public static final String ACTION_SHOW_DIAGRAM = "S";
    public static final String ACTION_GOTO_FOLDER = "F";
    public static final String ACTION_SHOW_HISTORY = "H";
    public static final String ACTION_SHOW_DETAIL = "V";
    
    /*
     * Workflow Name
     */
    public static final String WF_NAME = "NSTDAHrSal";

}
