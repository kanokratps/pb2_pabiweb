package pb.repo.admin.constant;

import java.util.HashMap;
import java.util.Map;

public class MainWorkflowConstant {
  
    /*
     * Task Name
     */
    public static final String TN_ACCEPTER = "Accepter";
    public static final String TN_REQUESTER = "Requester";
    public static final String TN_PREPARER = "Preparer";
    public static final String TN_SUPERVISOR = "Supervisor";
    public static final String TN_PREPARER_RESUBMIT = "Preparer Resubmit";
    public static final String TN_REVIEWER = "Reviewer";
    public static final String TN_CONSULTANT = "Consultant";
    
    public static final String TN_PROCUREMENT = "Procurement";
    public static final String TN_PROCUREMENT_SUPERVISOR = "Procurement Supervisor";
    public static final String TN_FINANCE = "Finance";
    public static final String TN_FINANCE_OFFICER = "Finance Officer";
    
    public static final String TN_SERVICE_UNIT = "Service Unit";
    
    public static final String TN_HUMAN_RESOURCES = "Human Resources";

    public static final String TN_ADMIN = "Administrator";

	public static final Map<String, String> WF_TASK_NAMES = new HashMap<String, String>();
    static {
    	WF_TASK_NAMES.put(TN_ACCEPTER, "Requester");
    	WF_TASK_NAMES.put(TN_REQUESTER, "Requester");
    	WF_TASK_NAMES.put(TN_PREPARER, "Preparer");
    	WF_TASK_NAMES.put(TN_SUPERVISOR, "Supervisor");
    	WF_TASK_NAMES.put(TN_REVIEWER, "Approver");
    	WF_TASK_NAMES.put(TN_PREPARER_RESUBMIT, "Preparer Resubmit");
    	WF_TASK_NAMES.put(TN_CONSULTANT, "Consultant");
    	
    	WF_TASK_NAMES.put(TN_PROCUREMENT, "Procurement");
    	WF_TASK_NAMES.put(TN_PROCUREMENT_SUPERVISOR, "Procurement Supervisor");
    	
    	WF_TASK_NAMES.put(TN_FINANCE, "Finance");
    	WF_TASK_NAMES.put(TN_FINANCE_OFFICER, "Finance Officer");
    	
    	WF_TASK_NAMES.put(TN_SERVICE_UNIT, "Service Unit");
    	
    	WF_TASK_NAMES.put(TN_HUMAN_RESOURCES, "Human Resources");
    	
    	WF_TASK_NAMES.put(TN_ADMIN, "Administrator");
    }
    
	public static final Map<String, String> WF_TASK_NAMES_TH = new HashMap<String, String>();
    static {
    	WF_TASK_NAMES_TH.put(TN_ACCEPTER, "ผู้ขอ");
    	WF_TASK_NAMES_TH.put(TN_REQUESTER, "ผู้ขอ");
    	WF_TASK_NAMES_TH.put(TN_PREPARER, "ผู้บันทึก");
    	WF_TASK_NAMES_TH.put(TN_SUPERVISOR, "ผู้ตรวจสอบ");
    	WF_TASK_NAMES_TH.put(TN_REVIEWER, "ผู้อนุมัติขั้นที่");
    	WF_TASK_NAMES_TH.put(TN_PREPARER_RESUBMIT, "ผู้บันทึก ทบทวนอีกครั้ง");
    	WF_TASK_NAMES_TH.put(TN_CONSULTANT, "ที่ปรึกษาให้ความเห็น");
    	
    	WF_TASK_NAMES_TH.put(TN_PROCUREMENT, "ฝ่ายพัสดุ");
    	WF_TASK_NAMES_TH.put(TN_PROCUREMENT_SUPERVISOR, "หัวหน้าพัสดุ");
    	
    	WF_TASK_NAMES_TH.put(TN_FINANCE, "การเงิน");
    	WF_TASK_NAMES_TH.put(TN_FINANCE_OFFICER, "เจ้าหน้าที่การเงิน");
    	
    	WF_TASK_NAMES_TH.put(TN_SERVICE_UNIT, "หน่วยงานที่ให้บริการ");
    	
    	WF_TASK_NAMES_TH.put(TN_HUMAN_RESOURCES, "ฝ่ายบุคคล");
    	
    	WF_TASK_NAMES_TH.put(TN_ADMIN, "ผู้ดูแลระบบ");
    }
    
    /*
     * Task Outcome
     */
    public static final String TO_REVIEW = "reviewOutcome";
    public static final String TO_RESUBMIT = "reSubmitOutcome";
    public static final String TO_CONSULT = "consultOutcome";
    public static final String TO_ACCEPT = "acceptOutcome";
    
    
    /*
     * Task Action
     */
    public static final String TA_START = "Start";
    
    public static final String TA_APPROVE = "Approve"; // TO_REVIEW
    public static final String TA_REJECT = "Reject";
    public static final String TA_CONSULT = "Consult";
    
    public static final String TA_RESUBMIT = "Resubmit"; // TO_RESUBMIT   
    public static final String TA_CANCEL = "Cancel"; 
    
    public static final String TA_COMMENT = "Comment"; // TO_CONSULT
    
    public static final String TA_COMPLETE = "Complete";

    public static final String TA_PAID = "Paid";
    
    /*
     * Workflow Path parameter
     */
    public static final String WPP_DOC_TYPE = "docType";
    public static final String WPP_BGT_SRC_TYPE = "bgtSrcType";
    public static final String WPP_BGT_SRC = "bgtSrc";
    public static final String WPP_REQ_BY = "reqBy";
    public static final String WPP_EMOTION = "emotion";
    public static final String WPP_TOTAL = "total";
}
