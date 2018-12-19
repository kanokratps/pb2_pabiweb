package pb.repo.admin.constant;

public class AdminConstant {
	public static final StringBuffer CHANGE_LOG = new StringBuffer();
	
    static {
//		CHANGE_LOG.append("  - Interface : For Odoo : Manage Member in Accounting Group\n");
//    	CHANGE_LOG.append("  - EX : Interface : Load Test\n");

//    	CHANGE_LOG.append("  - Workflow Path : Section : Start from first level\n");

    	CHANGE_LOG.append("\nV.1.0.242 --- (18/12/2018)\n");
    	CHANGE_LOG.append("  - HR : Approver signature not show\n");
    	
    	CHANGE_LOG.append("\nV.1.0.241 --- (17/12/2018)\n");
    	CHANGE_LOG.append("  - PD,HR : Interface : updateStatusPD : Add Attachment Parameter\n");
    	
    	CHANGE_LOG.append("\nV.1.0.240 --- (11/12/2018)\n");
    	CHANGE_LOG.append("  - PD,HR : Resubmit : Upload attached file cause error\n");
    	
    	CHANGE_LOG.append("\nV.1.0.239 --- (30/11/2018)\n");
    	CHANGE_LOG.append("  - PR : Across Year Budget : Fiscal Year Combo Box show from current year\n");
    	CHANGE_LOG.append("  - PD,HR : Interface : Check invalid file name character\n");
    	
    	CHANGE_LOG.append("\nV.1.0.238 --- (26/11/2018)\n");
    	CHANGE_LOG.append("  - PD : Interface : Add Attached Filed Description Parameter\n");
    	
    	CHANGE_LOG.append("\nV.1.0.237 --- (21/11/2018)\n");
    	CHANGE_LOG.append("  - PR : Interface : Create PR : parameter date_start is got from requested_time\n");
    	CHANGE_LOG.append("  - PR,EX,AV : Requester and Preparer Time change from createdTime to requestedTime\n");
    	
    	CHANGE_LOG.append("\nV.1.0.236 --- (19/11/2018)\n");
    	CHANGE_LOG.append("  - EX : Interface : Create : Add createdBy parameter\n");
    	
    	CHANGE_LOG.append("\nV.1.0.235 --- (16/11/2018)\n");
    	CHANGE_LOG.append("  - Sort Attached Files\n");

    	CHANGE_LOG.append("\nV.1.0.234 --- (15/11/2018)\n");
    	CHANGE_LOG.append("  - Admin : Main : Hide Log Tab\n");
    	CHANGE_LOG.append("  - Workflow : Show Error if doc_ref and level is null\n");
    	
    	CHANGE_LOG.append("\nV.1.0.233 --- (14/11/2018)\n");
    	CHANGE_LOG.append("  - EX : Interface : Resubmit task owner is the interface user\n");
    	CHANGE_LOG.append("  - Workflow History : Current Task : Show blank if workflow_ins_id is null\n");
    	
    	CHANGE_LOG.append("\nV.1.0.232 --- (13/11/2018)\n");
    	CHANGE_LOG.append("  - PR,EX,AV : Warning if total=0\n");
    	
    	CHANGE_LOG.append("\nV.1.0.231 --- (12/11/2018)\n");
    	CHANGE_LOG.append("  - EX : Emotion Workflow : Fixed\n");
    	CHANGE_LOG.append("  - EX : View : Old PR\n");
    	
    	CHANGE_LOG.append("\nV.1.0.230 --- (10/11/2018)\n");
    	CHANGE_LOG.append("  - PR : Warn if purchasing unit officer not found in workflow path\n");
    	
    	CHANGE_LOG.append("\nV.1.0.229 --- (07/11/2018)\n");
    	CHANGE_LOG.append("  - EX : Interface : CreateEX (O->A) : Cost Control Values not required\n");
    	
    	CHANGE_LOG.append("\nV.1.0.228 --- (06/11/2018)\n");
    	CHANGE_LOG.append("  - PD : Sort by create time : Error\n");
    	
    	CHANGE_LOG.append("\nV.1.0.227 --- (05/11/2018)\n");
    	CHANGE_LOG.append("  - Clean Log\n");
    	CHANGE_LOG.append("  - PR,EX,AV : Check Budget Button : Waiting Approval Amount : Include consulting Status\n");
    	CHANGE_LOG.append("  - PR : Total and Total_cnv : Set from shown text\n");
    	
    	CHANGE_LOG.append("\nV.1.0.226 --- (02/11/2018)\n");
    	CHANGE_LOG.append("  - Clean Log\n");
    	CHANGE_LOG.append("  - View pb2_employee_info_view, pb2_hr_employee_view : Remove u.active=true\n");
    	CHANGE_LOG.append("  - PR,AV,EX : Save Form : Warn user if attached file name is <doc id>.pdf\n");

    	CHANGE_LOG.append("\nV.1.0.225 --- (01/11/2018)\n");
    	CHANGE_LOG.append("  - System Config : MAIN_DEBUG\n");
    	
    	CHANGE_LOG.append("\nV.1.0.224 --- (31/10/2018)\n");
    	CHANGE_LOG.append("  - Interface : Hide Password in log\n");
    	
    	CHANGE_LOG.append("\nV.1.0.223 --- (30/10/2018)\n");
    	CHANGE_LOG.append("  - Query : Project : change command from between to greater or equal and less than or equal\n");
    	
    	CHANGE_LOG.append("\nV.1.0.222 --- (29/10/2018)\n");
    	CHANGE_LOG.append("  - PR,EX,AV : Backup Draft Data when delete\n");
    	
    	CHANGE_LOG.append("\nV.1.0.221 --- (27/10/2018)\n");
    	CHANGE_LOG.append("  - Admin : Current User\n");
    	
    	CHANGE_LOG.append("\nV.1.0.220 --- (26/10/2018)\n");
    	CHANGE_LOG.append("  - Use PM Section ID from pb2_ext_res_project instead of hr_employee\n");
    	CHANGE_LOG.append("  - PR,EX,AV : Remove user validation from Send for Approval button\n");
    	CHANGE_LOG.append("  - Merge Workflow Path Code to Main Module\n");

    	CHANGE_LOG.append("\nV.1.0.219 --- (25/10/2018)\n");
    	CHANGE_LOG.append("  - Trim Log\n");
    	CHANGE_LOG.append("  - EX : Gen Doc : Replace content with old doc ref\n");
    	CHANGE_LOG.append("  - Form : Add workflow path button\n");

    	CHANGE_LOG.append("\nV.1.0.218 --- (24/10/2018)\n");
    	CHANGE_LOG.append("  - New Menu : Redmine Issue #2519\n");
    	CHANGE_LOG.append("  - EX,AV : Old PR Combo box should not type character\n");
    	CHANGE_LOG.append("  - Workflow Path : Remove requester in SQL Query\n");
    	
    	CHANGE_LOG.append("\nV.1.0.217 --- (23/10/2018)\n");
    	CHANGE_LOG.append("  - PR,EX,AV : Form,PDF : Change Small Amount Checkbox Label\n");
    	
    	CHANGE_LOG.append("\nV.1.0.216 --- (23/10/2018)\n");
    	CHANGE_LOG.append("  - Workflow : Current Task : If employee not found show blank at first name position\n");
    	
    	CHANGE_LOG.append("\nV.1.0.215 --- (22/10/2018)\n");
    	CHANGE_LOG.append("  - EX : Get : Do not join section or project if pay_dtl2 is string\n");
    	CHANGE_LOG.append("  - Project Boss List : find boss list by PM instead of requester\n");
    	
    	CHANGE_LOG.append("\nV.1.0.214 --- (21/10/2018)\n");
    	CHANGE_LOG.append("  - EX : Internal Charge : Show only internal charge section\n");
    	
    	CHANGE_LOG.append("\nV.1.0.213 --- (19/10/2018)\n");
    	CHANGE_LOG.append("  - EX : Interface : Create EX : Attendee parameter not required\n");
    	
    	CHANGE_LOG.append("\nV.1.0.212 --- (26/09/2018)\n");
    	CHANGE_LOG.append("  - Workflow : Form : Upload without flash player\n");
    	
    	CHANGE_LOG.append("\nV.1.0.211 --- (24/09/2018)\n");
    	CHANGE_LOG.append("  - Admin : Workflow : Fix missing document and attached files\n");
    	
    	CHANGE_LOG.append("\nV.1.0.210 --- (20/09/2018)\n");
    	CHANGE_LOG.append("  - Workflow : New Attached File Uploader : Not Use Flash Player\n");
    	
    	CHANGE_LOG.append("\nV.1.0.209 --- (18/09/2018)\n");
    	CHANGE_LOG.append("  - EX : Payment Type non internal charge : Show All Activities including internal charge type\n");
    	
    	CHANGE_LOG.append("\nV.1.0.208 --- (14/09/2018)\n");
    	CHANGE_LOG.append("  - Menu Stock Request : Change Thai Label\n");
    	
    	CHANGE_LOG.append("\nV.1.0.207 --- (12/09/2018)\n");
    	CHANGE_LOG.append("  - Search Budget Source : Project : Add condition 'approve' and active\n");

    	CHANGE_LOG.append("\nV.1.0.206 --- (10/09/2018)\n");
    	CHANGE_LOG.append("  - EX : Interface : Create EX : Rollback if found error\n");
    	
    	CHANGE_LOG.append("\nV.1.0.205 --- (09/09/2018)\n");
    	CHANGE_LOG.append("  - EX : Internal Charge Payment Type : Do not check Budget\n");
    	
    	CHANGE_LOG.append("\nV.1.0.204 --- (08/09/2018)\n");
    	CHANGE_LOG.append("  - PR,EX,AV : Check Budget Button : Ignore Internal charge type\n");
    	
    	CHANGE_LOG.append("\nV.1.0.203 --- (07/09/2018)\n");
    	CHANGE_LOG.append("  - EX : Interface : Create : Commit DB\n");
    	
    	CHANGE_LOG.append("\nV.1.0.202 --- (06/09/2018)\n");
    	CHANGE_LOG.append("  - PR,EX,AV : Non Employee Title : Not Required\n");
    	CHANGE_LOG.append("  - PR,EX,AV : Non Employee Title : Display both languange\n");

    	CHANGE_LOG.append("\nV.1.0.201 --- (03/09/2018)\n");
    	CHANGE_LOG.append("  - Workflow History : Add Close Button on the bottom\n");
    	CHANGE_LOG.append("  - PR : Form : Item : Fiscal year combo box item show from last fiscal year\n");
    	CHANGE_LOG.append("  - PR : Additional : Not Limit Additional Amount\n");
    	
    	CHANGE_LOG.append("\nV.1.0.200 --- (01/09/2018)\n");
    	CHANGE_LOG.append("  - PR : Mission Signature After Purchasing Accept Work\n");
    	
    	CHANGE_LOG.append("\nV.1.0.199 --- (31/08/2018)\n");
    	CHANGE_LOG.append("  - PR,EX,AV : View save draft atteched files\n");
    	CHANGE_LOG.append("  - PR,EX,AV : Interface from Odoo : Check ID existence\n");

    	CHANGE_LOG.append("\nV.1.0.198 --- (29/08/2018)\n");
    	CHANGE_LOG.append("  - PR,EX,AV : Old PR : Objective Type show id instead of name\n");
    	CHANGE_LOG.append("  - PR : Form : Show Refering EX/AV with white font\n");
    	
    	CHANGE_LOG.append("\nV.1.0.197 --- (24/08/2018)\n");
    	CHANGE_LOG.append("  - Workflow History : Wrong Current Task\n");
    	
    	CHANGE_LOG.append("\nV.1.0.196 --- (23/08/2018)\n");
    	CHANGE_LOG.append("  - Admin : Workflow : Reassign or Cancel show waiting dialog while processing\n");
    	CHANGE_LOG.append("  - PR,AV,EX : Budget Source Name show only English when click icon edit\n");
    	
    	CHANGE_LOG.append("\nV.1.0.195 --- (22/08/2018)\n");
    	CHANGE_LOG.append("  - Admin : Workflow : Not Allow to cancel except status W1/W2\n");
    	CHANGE_LOG.append("  - Admin : Utility : Add User : if user has already been in group do not show error\n");
    	
    	CHANGE_LOG.append("\nV.1.0.194 --- (21/08/2018)\n");
    	CHANGE_LOG.append("  - EX : Select New Budget Src : Clear Small Amount Check Box and Old PR Combo Box\n");
    	
    	CHANGE_LOG.append("\nV.1.0.193 --- (20/08/2018)\n");
    	CHANGE_LOG.append("  - PR,AV,EX : Interface : Fixed : Send Title 'null'\n");
    	CHANGE_LOG.append("  - Admin : Workflow : Select Type 'assigned' : Not show result\n");
    	CHANGE_LOG.append("  - PR : Send for Approval : Show Wrong Minimum Committee number\n");
    	CHANGE_LOG.append("  - PR : Fixed : Search by Purchase Type\n");
    	CHANGE_LOG.append("  - Admin : Add System Config : MAIN_ODOO_STOCK_REQUEST_URL\n");
    	
    	CHANGE_LOG.append("\nV.1.0.192 --- (19/08/2018)\n");
    	CHANGE_LOG.append("  - Admin : Workflow : Change Label : 'Type' to 'Document'\n");
    	CHANGE_LOG.append("  - Admin : Workflow : Refresh Assignee Panel after Cancel\n");
    	
    	CHANGE_LOG.append("\nV.1.0.191 --- (17/08/2018)\n");
    	CHANGE_LOG.append("  - Admin : Workflow : Reassign requester as reviewer\n");
    	CHANGE_LOG.append("  - PR : Show Required Comittee Number\n");
    	CHANGE_LOG.append("  - Admin : Change Tab Title : 'Test System' to 'System'\n");
    	CHANGE_LOG.append("  - PR : Search Criteria : PR Type Combo Box : Change Lookup Table\n");
    	
    	CHANGE_LOG.append("\nV.1.0.190 --- (16/08/2018)\n");
    	CHANGE_LOG.append("  - PR,AV,EX : Item Dialog : Disable Assets Combo Box if no item\n");
    	CHANGE_LOG.append("  - System Config : Remove : PCM_ORD_SIGN_POS, PCM_ORD_SIGN_FONT_SIZE, PCM_ORD_SIGN_DATE_OFFSET\n");
    	CHANGE_LOG.append("  - System Config : Remove : PCM_ODOO_URL, PCM_ODOO_DB, EXP_ODOO_URL, EXP_ODOO_DB\n");
    	CHANGE_LOG.append("  - System Config : Add : MAIN_ODOO_URL, MAIN_ODOO_DB, MAIN_ODOO_USER\n");
    	
    	CHANGE_LOG.append("\nV.1.0.189 --- (15/08/2018)\n");
    	CHANGE_LOG.append("  - EX : Form : Hide Pretty Cash Option\n");
    	CHANGE_LOG.append("  - Disable Flash Message\n");
    	CHANGE_LOG.append("  - Admin : Main Master : Add Field is_system\n");
    	CHANGE_LOG.append("  - Admin : System Config : MAIN_SHOW_SYSTEM_VALUE\n");

    	CHANGE_LOG.append("\nV.1.0.188 --- (09/08/2018)\n");
    	CHANGE_LOG.append("  - Admin : Workflow : PD,HR : Resubmit : Replace Original Reviewer to Assigned Reviewer\n");
    	CHANGE_LOG.append("  - Menu : Show/Hide Menu by User Group\n");
    	CHANGE_LOG.append("  - PR : Do not add special user for add permission\n");
    	
    	CHANGE_LOG.append("\nV.1.0.187 --- (06/08/2018)\n");
    	CHANGE_LOG.append("  - Admin : Workflow : Fixed : Assign Current User\n");
    	CHANGE_LOG.append("  - Admin : Workflow : Search Workflow with Assiged User\n");
    	CHANGE_LOG.append("  - Admin : Workflow : History Icon\n");
    	
    	CHANGE_LOG.append("\nV.1.0.186 --- (03/08/2018)\n");
    	CHANGE_LOG.append("  - Admin : Workflow : Assignee Panel\n");
    	
    	CHANGE_LOG.append("\nV.1.0.185 --- (02/08/2018)\n");
    	CHANGE_LOG.append("  - Interface : Odoo Login by admin\n");
    	
    	CHANGE_LOG.append("\nV.1.0.184 --- (28/07/2018)\n");
    	CHANGE_LOG.append("  - Admin : Workflow : Add Source Assignee Field\n");
    	
    	CHANGE_LOG.append("\nV.1.0.183 --- (26/07/2018)\n");
    	CHANGE_LOG.append("  - PR : Interface : pabiweb_check_budget : VAT price included : Send VAT price excluded Amount\n");
    	CHANGE_LOG.append("  - Query pb2_ext_budget_asset_rule_line : Add pb2_ext_budget_fund_rule.state='confirmed'\n");
    	
    	CHANGE_LOG.append("\nV.1.0.182 --- (25/07/2018)\n");
    	CHANGE_LOG.append("  - PR,EX,AV : UI & Report : Hide Title if null\n");
    	CHANGE_LOG.append("  - Interface : Change Login user to admin\n");
    	CHANGE_LOG.append("  - Resubmit Workflow : Replace with assignee from pb2_main_workflow_assignee\n");
    	CHANGE_LOG.append("  - Boss View : Remove amount_min field\n");
    	CHANGE_LOG.append("  - Boss View : Use Odoo View\n");
    	
    	CHANGE_LOG.append("\nV.1.0.181 --- (24/07/2018)\n");
    	CHANGE_LOG.append("  - Admin : Workflow : Reassign : Save assignee for future assignment on assigned workflow\n");
    	CHANGE_LOG.append("  - PR : Copy : Not Copy price_included field\n");
    	CHANGE_LOG.append("  - PR,EX,AV : Save : Title field : Save '' instead of null\n");
    	CHANGE_LOG.append("  - PR,EX,AV : Edit, Click Search Tab, Edit and Close then cannot Edit again\n");
    	
    	CHANGE_LOG.append("\nV.1.0.180 --- (21/07/2018)\n");
    	CHANGE_LOG.append("  - EX : Special Workflow Condition : Section, Activity, Activity Group is Special Workflow\n");
    	CHANGE_LOG.append("  - Admin : Workflow : Add Table pb2_main_workflow_assignee\n");
    	
    	CHANGE_LOG.append("\nV.1.0.179 --- (17/07/2018)\n");
    	CHANGE_LOG.append("  - Admin : Workflow : Show Assignee column\n");
    	CHANGE_LOG.append("  - Admin : Workflow : Remove consult from query\n");
    	
    	CHANGE_LOG.append("\nV.1.0.178 --- (16/07/2018)\n");
    	CHANGE_LOG.append("  - EX : View : Ref. PR not show\n");
    	CHANGE_LOG.append("  - AV : View : Ref. AV not show\n");
    	
    	CHANGE_LOG.append("\nV.1.0.177 --- (07/07/2018)\n");
    	CHANGE_LOG.append("  - PR : New PR Method Operation\n");
    	CHANGE_LOG.append("  - PD : Query module='purchase_pd'\n");
    	
    	CHANGE_LOG.append("\nV.1.0.176 --- (01/07/2018)\n");
    	CHANGE_LOG.append("  - PR,AV,EX : Non-Employee title : not translate\n");
    	
    	CHANGE_LOG.append("\nV.1.0.175 --- (28/06/2018)\n");
    	CHANGE_LOG.append("  - Admin : Workflow : Sort\n");
    	CHANGE_LOG.append("  - PD : Approve : Update Document Signature\n");
    	
    	CHANGE_LOG.append("\nV.1.0.174 --- (27/06/2018)\n");
    	CHANGE_LOG.append("  - EX : Internal Charge : Approve : Not Check Budget\n");
    	CHANGE_LOG.append("  - PR : Additional PR : Change Query\n");
    	CHANGE_LOG.append("  - All : Sort\n");
    	CHANGE_LOG.append("  - Admin : Workflow : Add Columns Assignee\n");
//    	CHANGE_LOG.append("  - EX : Interface : Load Test\n");
    	
    	CHANGE_LOG.append("\nV.1.0.173 --- (26/06/2018)\n");
    	CHANGE_LOG.append("  - Admin : Workflow : Cancel and Reassign not function\n");
    	CHANGE_LOG.append("  - EX : Start Workflow : Check Budget if budgetCc is Project\n");
    	CHANGE_LOG.append("  - EX : simple_check_budget : add parameter internal_charge=True\n");
    	
    	CHANGE_LOG.append("\nV.1.0.172 --- (24/06/2018)\n");
    	CHANGE_LOG.append("  - PR,EX,AV : Start Workflow : Prevent Duplicated Workflow\n");
    	CHANGE_LOG.append("  - PR,EX,AV : Form View very slow\n");
    	CHANGE_LOG.append("  - Admin : Workflow : Query By API\n");
    	CHANGE_LOG.append("  - Admin : Workflow : Paging not function\n");
    	
    	CHANGE_LOG.append("\nV.1.0.171 --- (22/06/2018)\n");
    	CHANGE_LOG.append("  - Interface : Save Error Log\n");
    	CHANGE_LOG.append("  - EX : Add Internal Charge Status\n");
    	
    	CHANGE_LOG.append("\nV.1.0.170 --- (21/06/2018)\n");
    	CHANGE_LOG.append("  - EX : Show Referred AV by Label instead of Combo Box\n");
    	
    	CHANGE_LOG.append("\nV.1.0.169 --- (20/06/2018)\n");
    	CHANGE_LOG.append("  - All : Workflow Status lookup from main_master\n");
    	CHANGE_LOG.append("  - UOM : Always Show Label Thai(English)\n");
    	
    	CHANGE_LOG.append("\nV.1.0.168 --- (14/06/2018)\n");
    	CHANGE_LOG.append("  - PR,EX,AV : Workflow History : Optimize get() method\n");
    	CHANGE_LOG.append("  - All : Workflow : Save 1 Record per workflow\n");
    	CHANGE_LOG.append("  - EX,AV : Edit : Not show budget source\n");
    	CHANGE_LOG.append("  - PD : Method : Use pb2_ext_wkf_config_doctype\n");
    	
    	CHANGE_LOG.append("\nV.1.0.167 --- (13/06/2018)\n");
    	CHANGE_LOG.append("  - HR : Workflow Upload File Bug\n");
    	CHANGE_LOG.append("  - EX : Emotion Activity Group not show\n");
    	
    	CHANGE_LOG.append("\nV.1.0.166 --- (12/06/2018)\n");
    	CHANGE_LOG.append("  - Admin : Stop Scheduler\n");
    	
    	CHANGE_LOG.append("\nV.1.0.165 --- (11/06/2018)\n");
    	CHANGE_LOG.append("  - Admin : Log\n");
    	CHANGE_LOG.append("  - Admin : Utility : Add User to Group : Show Invalid User\n");
    	CHANGE_LOG.append("  - Admin : Purchase Condition order by name\n");
    	
    	CHANGE_LOG.append("\nV.1.0.164 --- (06/06/2018)\n");
    	CHANGE_LOG.append("  - Admin : Workflow : PD,HR : Should not be cancelled\n");
    	CHANGE_LOG.append("  - Admin : Workflow : Hide Reason Column\n");
    	CHANGE_LOG.append("  - Admin : Workflow : Uncheck My Task after clear employee field\n");
    	CHANGE_LOG.append("  - Admin : Hide Tab PCM,EXP\n");
    	CHANGE_LOG.append("  - Admin : Hide Test Signature User Button\n");
    	CHANGE_LOG.append("  - Admin : Util : Check User Existing before add to Group\n");
    	
    	CHANGE_LOG.append("\nV.1.0.163 --- (05/06/2018)\n");
    	CHANGE_LOG.append("  - Admin : PD,HR : Change Query : Search Consult\n");
    	CHANGE_LOG.append("  - Admin : PD : Show Requester Name\n");
    	
    	CHANGE_LOG.append("\nV.1.0.162 --- (28/05/2018)\n");
    	CHANGE_LOG.append("  - Admin : Change Query\n");
    	
    	CHANGE_LOG.append("\nV.1.0.161 --- (25/05/2018)\n");
    	CHANGE_LOG.append("  - HR : Remove Wrong Workflow History\n");
    	CHANGE_LOG.append("  - HR : Workflow Form does not show Thai\n");
    	CHANGE_LOG.append("  - PD : Cancel : Show blank at current task panel\n");
    	
    	CHANGE_LOG.append("\nV.1.0.160 --- (21/05/2018)\n");
    	CHANGE_LOG.append("  - Admin : Workflow : Cancel\n");
    	CHANGE_LOG.append("  - Admin : Workflow : Reassign\n");
    	CHANGE_LOG.append("  - AV : Report : Is Small Amount Fields do not move depence on band height\n");
    	CHANGE_LOG.append("  - Interface : Login with hard code password\n");
    	
    	CHANGE_LOG.append("\nV.1.0.159 --- (17/05/2018)\n");
    	CHANGE_LOG.append("  - Admin : Workflow : Search\n");
    	CHANGE_LOG.append("  - Admin : Settings : Combo Box Type : Search\n");
    	
    	CHANGE_LOG.append("\nV.1.0.158 --- (10/05/2018)\n");
    	CHANGE_LOG.append("  - PR,AV,EX : Cancel Workflow : Ask for Comment\n");
    	CHANGE_LOG.append("  - PR,AV,EX : Make Finish Button function like Send for Approval Button\n");
    	CHANGE_LOG.append("  - PR : Missing Error Message\n");
    	CHANGE_LOG.append("  - EX : Search by Method field\n");
    	CHANGE_LOG.append("  - PR : Reviewer cannot see Ref PR Doc in WF Form\n");
    	CHANGE_LOG.append("  - PD : Consult cannot see Attached Doc\n");
    	
    	CHANGE_LOG.append("\nV.1.0.157 --- (09/05/2018)\n");
    	CHANGE_LOG.append("  - Admin : Add Workflow Tab\n");
    	CHANGE_LOG.append("  - PR,EX,AV : Cancel Button : Change to Close Button\n");
    	CHANGE_LOG.append("  - EX,AV : Copy Icon : Not Allow if it is Small Amount type\n");
    	
    	CHANGE_LOG.append("\nV.1.0.156 --- (08/05/2018)\n");
    	CHANGE_LOG.append("  - AV,EX : Edit : Small Amount : PR not shown\n");
    	
    	CHANGE_LOG.append("\nV.1.0.155 --- (07/05/2018)\n");
    	CHANGE_LOG.append("  - PR,AV : A & AG : No Internal Charge Condition\n");
    	CHANGE_LOG.append("  - PR : Additional PR : Some Columns do not show Thai\n");
    	
    	CHANGE_LOG.append("\nV.1.0.154 --- (26/04/2018)\n");
    	CHANGE_LOG.append("  - EX,AV : Small Amount : Copy Item From PR : Not Allow Add and Remove Item\n");
    	CHANGE_LOG.append("  - EX,AV : Small Amount : Copy Item From PR : Not Allow Total more than PR Total\n");
    	CHANGE_LOG.append("  - PR : Exclusive select small amount checkbox and across budget checkbox\n");
    	CHANGE_LOG.append("  - EX : Select Old AV : if AV refer Small Amount PR then Not Allow to change EX Items\n");
    	CHANGE_LOG.append("  - PR : Add Field price_include\n");
    	CHANGE_LOG.append("  - EX,AV : Small Amount : Select PR only once\n");
    	CHANGE_LOG.append("  - EX,AV : Copy : Small Amount : Not Copy Origin PR\n");
    	
    	CHANGE_LOG.append("\nV.1.0.153 --- (24/04/2018)\n");
    	CHANGE_LOG.append("  - EX,AV : Small Amount : Select PR : Copy Objective from PR to EX,AV\n");
    	CHANGE_LOG.append("  - EX : Internal Charge : Hide Source of Funds Field in Source of Budget Dialog\n");
    	CHANGE_LOG.append("  - PR,AV,EX : Budget Source : Construction and Asset : Disable Small Amount Check Box\n");
    	CHANGE_LOG.append("  - Main : Add Table pb2_main_interface\n");
    	CHANGE_LOG.append("  - EX : Add Field pb2_exp_use.inf_id\n");
    	
    	CHANGE_LOG.append("\nV.1.0.152 --- (20/04/2018)\n");
    	CHANGE_LOG.append("  - EX : Interface : Create EX : Add Field isReason and isSmallAmount\n");
    	
    	CHANGE_LOG.append("\nV.1.0.151 --- (18/04/2018)\n");
    	CHANGE_LOG.append("  - EX : Do not validate clearing AV if AV data exists in Odoo only\n");
    	
    	CHANGE_LOG.append("\nV.1.0.150 --- (16/04/2018)\n");
    	CHANGE_LOG.append("  - HR : End Workflow after Reject\n");
    	
    	CHANGE_LOG.append("\nV.1.0.149 --- (10/04/2018)\n");
    	CHANGE_LOG.append("  - PR : Commitee Tab Bug Fixed\n");
    	CHANGE_LOG.append("  - EX : Add Petty Cash Label\n");
    	
    	CHANGE_LOG.append("\nV.1.0.148 --- (09/04/2018)\n");
    	CHANGE_LOG.append("  - Interface : Create EX : Payment Type Petty Cash\n");
    	CHANGE_LOG.append("  - Interface : Check Budget : Call New Method : pabiweb_check_budget()\n");
    	CHANGE_LOG.append("  - EX : Form : Add Petty Cash Combo Box\n");
    	
    	CHANGE_LOG.append("\nV.1.0.147 --- (28/03/2018)\n");
    	CHANGE_LOG.append("  - PR,EX,AV : Add Asset Name Field\n");
    	
    	CHANGE_LOG.append("\nV.1.0.146 --- (28/02/2018)\n");
    	CHANGE_LOG.append("  - HR : Signature : Add Title\n");
    	CHANGE_LOG.append("  - HR : Signature : Change signed Date to BE\n");
    	
    	CHANGE_LOG.append("\nV.1.0.146 --- (28/02/2018)\n");
    	CHANGE_LOG.append("  - HR : Test Signature Position\n");
    	
    	CHANGE_LOG.append("\nV.1.0.145 --- (23/02/2018)\n");
    	CHANGE_LOG.append("  - Admin : Delete table wkf_cmd_section_assign\n");
    	
    	CHANGE_LOG.append("\nV.1.0.144 --- (22/02/2018)\n");
    	CHANGE_LOG.append("  - PR,EX,AV : Change English small amount labels\n");
    	CHANGE_LOG.append("  - HR : Change Closed Task status labels\n");

    	CHANGE_LOG.append("\nV.1.0.143 --- (20/02/2018)\n");
    	CHANGE_LOG.append("  - HR : History : Current Task : Change from procurement to HR\n");
    	
    	CHANGE_LOG.append("\nV.1.0.142 --- (19/02/2018)\n");
    	CHANGE_LOG.append("  - All : Change Invest Construction Query\n");
    	CHANGE_LOG.append("  - All : Budget Source Dialog : Extend Construction Name Column Width\n");
    	
    	CHANGE_LOG.append("\nV.1.0.141 --- (18/02/2018)\n");
    	CHANGE_LOG.append("  - EX : Old AV Label not show\n");
    	
    	CHANGE_LOG.append("\nV.1.0.140 --- (16/02/2018)\n");
    	CHANGE_LOG.append("  - PR : Choose Small Amount : Clear Procurement Method Tab data\n");
    	
    	CHANGE_LOG.append("\nV.1.0.139 --- (06/02/2018)\n");
    	CHANGE_LOG.append("  - PR,EX,AV : Switch Activity and Activity Group Field Positions\n");
    	CHANGE_LOG.append("  - PR : Add Vat Checkbox\n");
    	CHANGE_LOG.append("  - PR,EX,AV : Source of Budget : Asset,Construction\n");
    	
    	CHANGE_LOG.append("\nV.1.0.138 --- (24/08/2017)\n");
    	CHANGE_LOG.append("  - PR : Method Selected but Committee Tabs do not show\n");
    	CHANGE_LOG.append("  - Search User : Add Order by Division in SQL\n");
    	CHANGE_LOG.append("  - PR : Currency Rate : Order Descending\n");
    	CHANGE_LOG.append("  - PR : Currency Rate : Get rate value : field rate -> rate_input\n");
    	
    	CHANGE_LOG.append("\nV.1.0.137 --- (07/08/2017)\n");
    	CHANGE_LOG.append("  - PR,EX,AV : Form View Mode\n");
    	CHANGE_LOG.append("  - PR,EX,AV : Change Button label to English\n");
    	CHANGE_LOG.append("  - EX,AV : Pdf : Signature Section separated\n");
    	
    	CHANGE_LOG.append("\nV.1.0.136 --- (04/08/2017)\n");
    	CHANGE_LOG.append("  - PR,EX,AV : Change Button label to English\n");
    	CHANGE_LOG.append("  - EX,AV : Add English Warning\n");
    	CHANGE_LOG.append("  - EX : Copy Button : Reason for bypass procurement is not copied from the original one\n");
    	
    	CHANGE_LOG.append("\nV.1.0.135 --- (03/08/2017)\n");
    	CHANGE_LOG.append("  - PR,EX,AV Pdf : Change Field Background Color\n");
    	CHANGE_LOG.append("  - Search Employee : Add Division column\n");
    	
    	CHANGE_LOG.append("\nV.1.0.134 --- (02/08/2017)\n");
    	CHANGE_LOG.append("  - PR,EX,AV : Copy Button : Wrong Total\n");
    	CHANGE_LOG.append("  - PR,EX,AV Pdf : Bigger Font for ID\n");
    	CHANGE_LOG.append("  - PR,EX,AV Pdf : Change Report Header to Page Header\n");
    	CHANGE_LOG.append("  - PR,EX,AV Pdf : Change Report Footer to Report Data\n");
    	CHANGE_LOG.append("  - PR : Add Committee : Show Wrong Icon\n");
    	CHANGE_LOG.append("  - EX,AV : Check Duplicated Attendee\n");
    	
    	CHANGE_LOG.append("\nV.1.0.133 --- (24/07/2017)\n");
    	CHANGE_LOG.append("  - EX : Download Document Button\n");
    	CHANGE_LOG.append("  - EX,AV Pdf : Change Thai Attendee Label\n");
    	CHANGE_LOG.append("  - PR : Hide Meduim Price Combox Box\n");
    	CHANGE_LOG.append("  - PR : Change Currecy Rate to THB, show Wrong Method\n");
    	CHANGE_LOG.append("  - PR,AV,EX : Change Warning Messages\n");
    	CHANGE_LOG.append("  - PR,AV,EX : Copy Item Button\n");
    	CHANGE_LOG.append("  - PR,AV,EX : Edit Button shows form more than 1\n");
    	
    	CHANGE_LOG.append("\nV.1.0.132 --- (03/07/2017)\n");
    	CHANGE_LOG.append("  - Add Salary Module\n");
    	CHANGE_LOG.append("  - AV,EX : Change Attendee Tab Caption\n");
    	CHANGE_LOG.append("  - PR,AV,EX : Align Add Button to Left side\n");
    	CHANGE_LOG.append("  - PR : Exchange Rate : Add label 'Baht'\n");
    	CHANGE_LOG.append("  - PR : Change 'No VAT' label\n");
    	CHANGE_LOG.append("  - PR : Move 2 Check Boxes to below Currency Field\n");
    	CHANGE_LOG.append("  - AV,EX : Change Remark\n");
    	CHANGE_LOG.append("  - PR,AV,EX : Activity Group Description Icon\n");
    	CHANGE_LOG.append("  - PR : Move Committee Position\n");
    	CHANGE_LOG.append("  - PR,AV,EX : Select Other User Title by Combo Box\n");
    	
    	CHANGE_LOG.append("\nV.1.0.131 --- (26/06/2017)\n");
    	CHANGE_LOG.append("  - Change Thai 'Home' Caption\n");
    	
    	CHANGE_LOG.append("\nV.1.0.130 --- (30/06/2017)\n");
    	CHANGE_LOG.append("  - Admin : Odoo Password Configurable\n");

    	CHANGE_LOG.append("\nV.1.0.129 --- (31/05/2017)\n");
    	CHANGE_LOG.append("  - PR,AV,EX : Preview : Avoid Chrome Pop-up Blocker\n");
    	
    	CHANGE_LOG.append("\nV.1.0.128 --- (31/05/2017)\n");
    	CHANGE_LOG.append("  - EX : Attendee Tab : Add Button : 2 Languages\n");
    	
    	CHANGE_LOG.append("\nV.1.0.127 --- (29/05/2017)\n");
    	CHANGE_LOG.append("  - Update Installation files\n");
    	CHANGE_LOG.append("  - PR,AV,EX : Budget Source : Section : Remove name_short in front of project code\n");
    	CHANGE_LOG.append("  - AV,EX : Select Activity Group : Clear Condition\n");
    	
    	CHANGE_LOG.append("\nV.1.0.126 --- (26/05/2017)\n");
    	CHANGE_LOG.append("  - PR,AV,EX : Budget Source : Project : Remove name_short in front of project code\n");
    	CHANGE_LOG.append("  - PR,EX : Check Budget : user balance instead of balance-preapproved\n");

    	CHANGE_LOG.append("\nV.1.0.125 --- (26/05/2017)\n");
    	CHANGE_LOG.append("  - PR,AV,EX : Click add button, open 2 tabs\n");

    	CHANGE_LOG.append("\nV.1.0.124 --- (26/05/2017)\n");
    	CHANGE_LOG.append("  - AV,EX : Next Actor : XXXXXX\n");

    	CHANGE_LOG.append("\nV.1.0.123 --- (24/05/2017)\n");
    	CHANGE_LOG.append("  - PR,PD,AV,EX : Warning Dialog show incomplete message\n");
    	
    	CHANGE_LOG.append("\nV.1.0.122 --- (24/05/2017)\n");
    	CHANGE_LOG.append("  - PR,PD,AV,EX : Work flow History : Change Query String\n");
    	
    	CHANGE_LOG.append("\nV.1.0.121 --- (24/05/2017)\n");
    	CHANGE_LOG.append("  - EX : Pdf : Below data Not Float\n");
    	
    	CHANGE_LOG.append("\nV.1.0.120 --- (23/05/2017)\n");
    	CHANGE_LOG.append("  - EX : Interface : Create Internal Charge\n");
    	CHANGE_LOG.append("  - PR,AV,EX : Send Button : Disable after click\n");
    	CHANGE_LOG.append("  - PR,AV,EX : Add Button : Disable after click\n");
    	CHANGE_LOG.append("  - EX : Old AV : Change Caption : Add Balance field\n");
    	CHANGE_LOG.append("  - EX : Paging not work\n");

    	CHANGE_LOG.append("\nV.1.0.119 --- (23/05/2017)\n");
    	CHANGE_LOG.append("  - EX : Save Draft : Files is not String, is Object\n");
    	
    	CHANGE_LOG.append("\nV.1.0.118 --- (22/05/2017)\n");
    	CHANGE_LOG.append("  - PR,PD,AV,EX : System Config : MAIN_MONITOR_USER\n");
    	CHANGE_LOG.append("  - PR,AV,EX : Requester : Change Thai Caption\n");
    	CHANGE_LOG.append("  - PR : Across Budget : Use value from totalCnv\n");
    	
    	CHANGE_LOG.append("\nV.1.0.117 --- (17/05/2017)\n");
    	CHANGE_LOG.append("  - My Task : IE : Not Show List\n");
    	CHANGE_LOG.append("  - My Task : Thai Preparer Caption\n");
    	
    	CHANGE_LOG.append("\nV.1.0.116 --- (16/05/2017)\n");
    	CHANGE_LOG.append("  - PR,AV,EX : IE : Upload files\n");
    	CHANGE_LOG.append("  - EX : Non-Employee Dialog : Change Section Caption to Organization\n");
    	CHANGE_LOG.append("  - PR,AV,EX : IE : Edit : Tabs Missing\n");
    	CHANGE_LOG.append("  - PR,AV,EX : IE : Commitee Tab not show\n");
    	
    	CHANGE_LOG.append("\nV.1.0.115 --- (15/05/2017)\n");
    	CHANGE_LOG.append("  - PR,AV,EX : Workflow Form : Edit Description show fail dialog\n");
    	CHANGE_LOG.append("  - PR,AV,EX : View Uploaded Document\n");
    	CHANGE_LOG.append("  - PR : Across Year Budget : Check budget with first item\n");
    	
    	CHANGE_LOG.append("\nV.1.0.114 --- (12/05/2017)\n");
    	CHANGE_LOG.append("  - PR,AV,EX : Workflow Form : Upload multiple file then Edit Description cause File Missing\n");
    	
    	CHANGE_LOG.append("\nV.1.0.113 --- (11/05/2017)\n");
    	CHANGE_LOG.append("  - PR,PD,AV,EX : Workflow Form : Resubmit,Consult : Missing Attached Files\n");
    	CHANGE_LOG.append("  - PR,AV,EX : Create Form : Change Requester Division to Position\n");
    	CHANGE_LOG.append("  - PR : Form : Currency in item tab not equals which in info tab\n");
    	
    	CHANGE_LOG.append("\nV.1.0.112 --- (10/05/2017)\n");
    	CHANGE_LOG.append("  - PR : Workflow Form : Resubmit : Missing Attached Files\n");
    	
    	CHANGE_LOG.append("\nV.1.0.111 --- (08/05/2017)\n");
    	CHANGE_LOG.append("  - Login : IE Support\n");
    	CHANGE_LOG.append("  - PR,AV,EX : Item : Forbidded to input negative value\n");
    	CHANGE_LOG.append("  - AV : Workflow Form : Change Accepter Thai Caption\n");
    	
    	CHANGE_LOG.append("\nV.1.0.110 --- (03/05/2017)\n");
    	CHANGE_LOG.append("  - PR : Workflow Form : Total with Currency\n");
    	CHANGE_LOG.append("  - PR,AV,EX : Pdf : Missing Border\n");
    	CHANGE_LOG.append("  - AV : Workflow Form : Total with Currency\n");
    	
    	CHANGE_LOG.append("\nV.1.0.109 --- (02/05/2017)\n");
    	CHANGE_LOG.append("  - PR : Committee : English Caption\n");
    	
    	CHANGE_LOG.append("\nV.1.0.108 --- (02/05/2017)\n");
    	CHANGE_LOG.append("  - AV : My Task : Correct Budget Source Project Name\n");
    	CHANGE_LOG.append("  - EX,AV,PR : Pdf : Adjust\n");
    	
    	CHANGE_LOG.append("\nV.1.0.107 --- (28/04/2017)\n");
    	CHANGE_LOG.append("  - PR : Pdf : Adjust\n");
    	
    	CHANGE_LOG.append("\nV.1.0.106 --- (27/04/2017)\n");
    	CHANGE_LOG.append("  - PR,EX : Check Budget : Exclude Current ID\n");
		CHANGE_LOG.append("  - EX : Pdf : Stretch Field\n");
		
    	CHANGE_LOG.append("\nV.1.0.105 --- (26/04/2017)\n");
		CHANGE_LOG.append("  - PR,EX,AV : Cost Control Dialog : Query by Section ID\n");
		
    	CHANGE_LOG.append("\nV.1.0.104 --- (25/04/2017)\n");
		CHANGE_LOG.append("  - PR,EX : Start Workflow : Check Budget\n");
		CHANGE_LOG.append("  - PR,EX : Workflow Form : Approve Button : Check Budget\n");
		
    	CHANGE_LOG.append("\nV.1.0.103 --- (24/04/2017)\n");
		CHANGE_LOG.append("  - PR : Interface : Send activity_rpt_id.id\n");
		CHANGE_LOG.append("  - PR,AV,EX : Check Budget Icon\n");
		
    	CHANGE_LOG.append("\nV.1.0.102 --- (20/04/2017)\n");
		CHANGE_LOG.append("  - PR,AV,EX : Pdf : Job Control : Show '-' if no value\n");
		CHANGE_LOG.append("  - EX : Pdf : Payment Type and Recieve Type : Show only Selected Value\n");
		
    	CHANGE_LOG.append("\nV.1.0.101 --- (16/04/2017)\n");
		CHANGE_LOG.append("  - PR : Add Activity Field\n");
		CHANGE_LOG.append("  - PR,AV,EX : Requester should not see Preparer's Draft Requests\n");
		CHANGE_LOG.append("  - AV,EX : Participant Tab : Section -> organization\n");
		CHANGE_LOG.append("  - Main : Change table name pb2_ext_bank_master -> pb2_ext_res_bank\n");
		CHANGE_LOG.append("  - PR,PD,AV,EX : Change Folder Detail Columns' Caption\n");
		CHANGE_LOG.append("  - AV,EX : Workflow Form : Upload attached file and Edit Description\n");

    	CHANGE_LOG.append("\nV.1.0.100 --- (05/01/2017)\n");
		CHANGE_LOG.append("  - PR,PD,EX : My Task Description Format\n");
		CHANGE_LOG.append("  - Workflow Form : Date Format\n");
    	CHANGE_LOG.append("\nV.1.0.99 --- (21/12/2016)\n");
		CHANGE_LOG.append("  - EX: Edit : Show Other Attendee Section\n");
		CHANGE_LOG.append("  - EX: Old AV : Show Only Waiting Amount, not show balance\n");
		CHANGE_LOG.append("  - PR,AV,EX: Budget Src : Show Short Name of Section\n");
		CHANGE_LOG.append("  - PR,AV,EX: Workflow Form : No Comment Warning Message\n");
		CHANGE_LOG.append("  - PR,AV,EX: Interface : File : add field attach_by\n");
		CHANGE_LOG.append("  - EX : Select Old AV : Replace Budget Src\n");
		
    	CHANGE_LOG.append("\nV.1.0.98 --- (15/12/2016)\n");
		CHANGE_LOG.append("  - Search Employee : Remove title field from search condition\n");
		CHANGE_LOG.append("  - EX : Payment Doc : Show Thai Attendees\n");
		CHANGE_LOG.append("  - PR Item : Change Label : Withholding Tax to VAT\n");
		CHANGE_LOG.append("  - AV,EX Interface : Send approver_code\n");
		CHANGE_LOG.append("  - AV,EX Attendee : Not Get Input from field Section\n");
		
    	CHANGE_LOG.append("\nV.1.0.97 --- (09/12/2016)\n");
		CHANGE_LOG.append("  - Workflow Upload Dialog : Change Label 'Select PD File' to 'Select File'\n");
		CHANGE_LOG.append("  - Workflow : Change Status Label from verb to noun\n");
		CHANGE_LOG.append("  - EX : Old AV : Change List Format\n");
		CHANGE_LOG.append("  - Change Label : 'Reviewer' to 'Approver'\n");
		CHANGE_LOG.append("  - PR,AV,EX : Save Draft : Not Validate\n");
		CHANGE_LOG.append("  - AV : My Task : Change Title Format\n");
		CHANGE_LOG.append("  - PR : Remove Prototype NO. Field\n");
		CHANGE_LOG.append("  - PR,AV,EX : Check Folder Permission\n");
		
    	CHANGE_LOG.append("\nV.1.0.96 --- (08/12/2016)\n");
		CHANGE_LOG.append("  - PR,AV,EX : Requester Resubmit : Warn if no comment\n");
		CHANGE_LOG.append("  - AV : Change Confirm Message if Requester=Preparer\n");
		CHANGE_LOG.append("  - AV : Prevent submit 500,000 baht\n");
		
		CHANGE_LOG.append("\nV.1.0.95 --- (06/12/2016)\n");
		CHANGE_LOG.append("  - PR : Add Prototype List\n");
		CHANGE_LOG.append("  - PR : Change Field name : prototype -> prototype_type\n");
		CHANGE_LOG.append("  - PR : Change Field name : prototype_contract_no -> prototype_no\n");
		CHANGE_LOG.append("  - Main Menu : Swap AV and EX Position\n");
		CHANGE_LOG.append("  - AV : Change Label Reason to Reason for Advance\n");
    	CHANGE_LOG.append("\nV.1.0.94 --- (02/12/2016)\n");
		CHANGE_LOG.append("  - EX : Internal Charge : Remove Activity Field\n");
		CHANGE_LOG.append("  - EX : Change source of fund or payment type : Show Warning\n");
    	CHANGE_LOG.append("\nV.1.0.93 --- (23/11/2016)\n");
		CHANGE_LOG.append("  - EX : Internal Charge Field : 2 Languages\n");
		CHANGE_LOG.append("  - EX : Emotion Workflow\n");
    	CHANGE_LOG.append("\nV.1.0.92 --- (22/11/2016)\n");
		CHANGE_LOG.append("  - PR : Move File Tab to the Last position\n");
		CHANGE_LOG.append("  - PD : Workflow History : Blue font Reviewer\n");
		CHANGE_LOG.append("  - EX : Internal Charge\n");
    	CHANGE_LOG.append("\nV.1.0.91 --- (18/11/2016)\n");
		CHANGE_LOG.append("  - UI : Check Session\n");
    	CHANGE_LOG.append("\nV.1.0.90 --- (17/11/2016)\n");
		CHANGE_LOG.append("  - Cost Center : Change Label\n");
		CHANGE_LOG.append("  - PD : Resubmit : Recalculate Reviewer\n");
		CHANGE_LOG.append("  - AV Item : Change warning message\n");
		CHANGE_LOG.append("  - Workflow History : Reviewer is blue\n");
    	CHANGE_LOG.append("\nV.1.0.89 --- (14/11/2016)\n");
		CHANGE_LOG.append("  - AV Workflow Form : Add Real Requester in Approval Matrix\n");
		CHANGE_LOG.append("  - Approval Matrix : Special Level\n");
    	CHANGE_LOG.append("\nV.1.0.88 --- (11/11/2016)\n");
		CHANGE_LOG.append("  - EX,AV : Change field names : av_reason->av_remark,reamrk->note\n");
    	CHANGE_LOG.append("\nV.1.0.87 --- (10/11/2016)\n");
		CHANGE_LOG.append("  - EX Pdf : Label mixed up\n");
		CHANGE_LOG.append("  - AV,EX : Seach Activity by field search_keyword\n");
    	CHANGE_LOG.append("\nV.1.0.86 --- (09/11/2016)\n");
		CHANGE_LOG.append("  - AV : Change AV Type Label\n");
		CHANGE_LOG.append("  - EX : Move file Reason to Item Tab\n");
		CHANGE_LOG.append("  - EX : Add Field Remark to Item Tab\n");
		CHANGE_LOG.append("  - AV : Add Item Tab\n");
		CHANGE_LOG.append("  - AV : Add Field Reason and Remark to Item Tab\n");
		CHANGE_LOG.append("  - AV : Add Field av_reason\n");
		CHANGE_LOG.append("  - PR,AV,EX : Add Field requested_time\n");
    	CHANGE_LOG.append("\nV.1.0.85 --- (26/10/2016)\n");
		CHANGE_LOG.append("  - AV,EX : Hide Carbon Footprint section\n");
		CHANGE_LOG.append("  - EX : Hide Import Attendee Button\n");
		CHANGE_LOG.append("  - EX : Add Field Description and Change Label\n");
		CHANGE_LOG.append("  - AV : Change Label\n");
		CHANGE_LOG.append("  - EX : Clear AV : Get Data from AV\n");
    	CHANGE_LOG.append("\nV.1.0.84 --- (26/10/2016)\n");
		CHANGE_LOG.append("  - PD : Change Attachment Description\n");
    	CHANGE_LOG.append("\nV.1.0.83 --- (25/10/2016)\n");
		CHANGE_LOG.append("  - Optimize Code\n");
		CHANGE_LOG.append("  - EX : Change Column AP Type to EX Type\n");
		CHANGE_LOG.append("  - PD Workflow Form : Remove Requester Field\n");
    	CHANGE_LOG.append("\nV.1.0.82 --- (17/10/2016)\n");
		CHANGE_LOG.append("  - Workflow Form : 2 Languages\n");
		CHANGE_LOG.append("  - My Task : 2 Languages\n");
		CHANGE_LOG.append("  - PR Pdf : Adjust\n");
		CHANGE_LOG.append("  - EX : Payment Type : Add Petty Cash\n");
    	CHANGE_LOG.append("\nV.1.0.82 --- (14/10/2016)\n");
		CHANGE_LOG.append("  - Approval Matrix : Project : Wrong Result\n");
    	CHANGE_LOG.append("\nV.1.0.81 --- (11/10/2016)\n");
		CHANGE_LOG.append("  - AV,EX : External Attendee : Add Field section\n");
		CHANGE_LOG.append("  - EX : Add Field reason for bypass procurement\n");
		CHANGE_LOG.append("  - PR,AV,EX Pdf : Adjust Org Name Length\n");
		CHANGE_LOG.append("  - PR Form : Change Fiscal Year Field to Combobox\n");
		CHANGE_LOG.append("  - AV Item : Add Fields\n");
		CHANGE_LOG.append("  - EX Share UI : Edit Attachment Description\n");
    	CHANGE_LOG.append("\nV.1.0.80 --- (06/10/2016)\n");
		CHANGE_LOG.append("  - PR,AV,EX Pdf : Budget Source\n");
		CHANGE_LOG.append("  - AV,EX Interface : Parameter Approve Date = now\n");
		CHANGE_LOG.append("  - AV,EX Pdf : Old AV : Add Field Objective\n");
		CHANGE_LOG.append("  - PR,AV,EX Pdf : Bold Label\n");
		CHANGE_LOG.append("  - AV,EX Pdf : Sign Last Approver\n");
    	CHANGE_LOG.append("\nV.1.0.79 --- (05/10/2016)\n");
		CHANGE_LOG.append("  - EX Interface : Change parameter advance_expense_id.id to advance_expense_number\n");
		CHANGE_LOG.append("  - PR Interface : Add History\n");
    	CHANGE_LOG.append("\nV.1.0.78 --- (04/10/2016)\n");
		CHANGE_LOG.append("  - EX : Payment Document\n");
    	CHANGE_LOG.append("\nV.1.0.77 --- (03/10/2016)\n");
		CHANGE_LOG.append("  - AV : Old AV : Clear Reason Field if change Requester\n");
		CHANGE_LOG.append("  - EX : Item : Select Activity Group before show Activity List\n");
		CHANGE_LOG.append("  - Workflow History : 2 Languages\n");
		CHANGE_LOG.append("  - EX : Duplicated Reviewer\n");
		CHANGE_LOG.append("  - EX : Warn : No Detail\n");
    	CHANGE_LOG.append("\nV.1.0.76 --- (30/09/2016)\n");
		CHANGE_LOG.append("  - System Config : PCM_REQ_TOP_GROUP, PCM_ORD_TOP_GROUP,EXP_BRW_TOP_GROUP, EXP_USE_TOP_GROUP\n");
		CHANGE_LOG.append("  - Workflow : Add Fields status_th\n");
		CHANGE_LOG.append("  - Workflow History : Add Fields action_th and task_th\n");
		CHANGE_LOG.append("  - Folder Path Name : Use A.D. instead of B.E.\n");
		CHANGE_LOG.append("  - AV,EX : Old AV : Query view pb2_av_outstanding_view\n");
    	CHANGE_LOG.append("\nV.1.0.75 --- (29/09/2016)\n");
		CHANGE_LOG.append("  - PR,AV,EX : Cannot Change Requester\n");
		CHANGE_LOG.append("  - Workflow History Interface\n");
		CHANGE_LOG.append("  - EX Pdf : Adjust\n");
    	CHANGE_LOG.append("\nV.1.0.74 --- (28/09/2016)\n");
		CHANGE_LOG.append("  - AV,EX : Copy : Missing fund_id\n");
		CHANGE_LOG.append("  - EX Interface : Create EX : not send bank if pay_type is 0 or 2\n");
		CHANGE_LOG.append("  - EX Pdf : not show bank radio value if pay_type is 0 or 2\n");
		CHANGE_LOG.append("  - EX Form : not enable bank controls if pay_type is 0 or 2\n");
		CHANGE_LOG.append("  - AV : Change Old AV Columns' Labels\n");
		CHANGE_LOG.append("  - AV Pdf : Before and After Save Draft are difference\n");
		CHANGE_LOG.append("  - AV : Change Old AV Columns' Labels\n");
		CHANGE_LOG.append("  - EX Pdf : not show supplier name\n");
		CHANGE_LOG.append("  - AV,EX Form : Set Status Field after Save Draft\n");
		CHANGE_LOG.append("  - Budget Source Dialog : Remove Columne Cost Center\n");
		CHANGE_LOG.append("  - AV Interface : Wrong date_back value\n");
    	CHANGE_LOG.append("\nV.1.0.73 --- (22/09/2016)\n");
		CHANGE_LOG.append("  - EX : Workflow Form : Pay Type Label\n");
		CHANGE_LOG.append("  - EX : Edit Attached Files Description\n");
		CHANGE_LOG.append("  - AV : Gen Pdf\n");
		CHANGE_LOG.append("  - EX : Gen Pdf\n");
		CHANGE_LOG.append("  - My Task : Hide Icon\n");
		CHANGE_LOG.append("  - AV : Search Error\n");
    	CHANGE_LOG.append("\nV.1.0.72 --- (20/09/2016)\n");
		CHANGE_LOG.append("  - EX : Copy : No Attendee\n");
		CHANGE_LOG.append("  - EX : Supplier Name not show\n");
		CHANGE_LOG.append("  - EX : Source of Fund\n");
		CHANGE_LOG.append("  - EX : Other bank not show\n");
		CHANGE_LOG.append("  - EX : Add Activity Group Column in grid\n");
    	CHANGE_LOG.append("\nV.1.0.71 --- (19/09/2016)\n");
		CHANGE_LOG.append("  - AV,EX : Smart Search Bank Name\n");
		CHANGE_LOG.append("  - PR,AV,EX : Improve Cost Control Query\n");
		CHANGE_LOG.append("  - EX : Save Wrong Pay Type\n");
    	CHANGE_LOG.append("\nV.1.0.70 --- (14/09/2016)\n");
		CHANGE_LOG.append("  - AV Interface : Add Parameters\n");
		CHANGE_LOG.append("  - EX Workflow Form : Edit Rejected EX\n");
		CHANGE_LOG.append("  - EX Interface : Add Parameters\n");
		CHANGE_LOG.append("  - EX Item : Add Field act_grp_id\n");
		CHANGE_LOG.append("  - EX Item : Change Field activity_id to act_id\n");
		CHANGE_LOG.append("  - EX Form : Line Item : Remove Field vat\n");
		CHANGE_LOG.append("  - EX : Remove Field vat, vat_id, req_section_id\n");
		CHANGE_LOG.append("  - EX Form : Payment Method : Remove one option\n");
		CHANGE_LOG.append("  - AV Form : Choose Advance Type than show date_back\n");
		CHANGE_LOG.append("  - Change view name : pb2_ext_pr_method_committee_view to pb2_pr_method_committee_view\n");
		CHANGE_LOG.append("  - Change view name : pb2_ext_pr_method_view to pb2_pr_method_view\n");
		CHANGE_LOG.append("  - AV : Change field name : cost_control_to to date_back\n");
		CHANGE_LOG.append("  - AV : Remove field : req_section_id,cost_control, cost_control_from\n");
		CHANGE_LOG.append("  - PR : Add Multiple Committee\n");
    	CHANGE_LOG.append("\nV.1.0.69 --- (13/09/2016)\n");
		CHANGE_LOG.append("  - Odoo : Query Active Currency\n");
		CHANGE_LOG.append("  - Odoo : Query Active Account Tax\n");
		CHANGE_LOG.append("  - Odoo : Query Active User\n");
		CHANGE_LOG.append("  - EX Workflow Form : Adjust Fields\n");
		CHANGE_LOG.append("  - EX Form : Attendee Tab : 2 Languages\n");
		CHANGE_LOG.append("  - AV PDF : Add Field Cost Control\n");
		CHANGE_LOG.append("  - PR Interface : Create PR : change fiscal_year_id to fiscalyear_id\n");
    	CHANGE_LOG.append("\nV.1.0.68 --- (11/09/2016)\n");
		CHANGE_LOG.append("  - PR PDF : show fund_id\n");
		CHANGE_LOG.append("  - AP Form : Attendee Tab : 2 Languages\n");
		CHANGE_LOG.append("  - AP : Edit : Budget Source show [code]\n");
		CHANGE_LOG.append("  - AP : Requester : Show Title\n");
		CHANGE_LOG.append("  - AV : Add Field Cost Control\n");
		CHANGE_LOG.append("  - AV,AP : Interface\n");
		CHANGE_LOG.append("  - AP : Start Workflow\n");
    	CHANGE_LOG.append("\nV.1.0.67 --- (08/09/2016)\n");
		CHANGE_LOG.append("  - AV,AP : Edit : Show fund_id\n");
		CHANGE_LOG.append("  - AV Form : Participant : Show Title\n");
		CHANGE_LOG.append("  - AV,AP Attendee : Add Field title\n");
		CHANGE_LOG.append("  - PR : Search Project without PM\n");
    	CHANGE_LOG.append("\nV.1.0.66 --- (07/09/2016)\n");
		CHANGE_LOG.append("  - AV : Add System Config : MAIN_INF_AV_CREATE_AV\n");
		CHANGE_LOG.append("  - AV Workflow Form : Change Field Label\n");
		CHANGE_LOG.append("  - AV UI : Support 2 Languages\n");
		CHANGE_LOG.append("  - AV Workflow History : Missing ReqBy in Path\n");
		CHANGE_LOG.append("  - AV Form : Validate on Create Event\n");
		CHANGE_LOG.append("  - PR Interface : Create PR : Add Field fund_id\n");
		CHANGE_LOG.append("  - AV,EX : Add Field fund_id\n");
    	CHANGE_LOG.append("\nV.1.0.65 --- (05/09/2016)\n");
		CHANGE_LOG.append("  - PR : Add Field fund_id\n");
		CHANGE_LOG.append("  - Share UI : Hide Customize Dashboard Button\n");
    	CHANGE_LOG.append("\nV.1.0.64 --- (02/09/2016)\n");
		CHANGE_LOG.append("  - Folder Icon : Use Ext JS Dialog\n");
    	CHANGE_LOG.append("\nV.1.0.63 --- (01/09/2016)\n");
		CHANGE_LOG.append("  - PR Pdf : Remove border of total field\n");
		CHANGE_LOG.append("  - PR Form : ReqBy Dialog : Search short_name field\n");
		CHANGE_LOG.append("  - Workflow Form : Make Consult Chooser dialog look like ReqBy Dialog\n");
    	CHANGE_LOG.append("\nV.1.0.62 --- (30/08/2016)\n");
		CHANGE_LOG.append("  - PR Form : Validate Fiscal Year field in line item\n");
		CHANGE_LOG.append("  - PD Interface : Add Permission to Attached PR Folder\n");
		CHANGE_LOG.append("  - PR Form : Change ReqBy Dialog Column Format\n");
		CHANGE_LOG.append("  - PR Form : Change Employee Committee Dialog as same as ReqBy Dialog\n");
    	CHANGE_LOG.append("\nV.1.0.61 --- (29/08/2016)\n");
		CHANGE_LOG.append("  - PR Form : Add Field TotalCnv\n");
		CHANGE_LOG.append("  - PR Detail : Remove Field PriceCnv, isEquipment\n");
    	CHANGE_LOG.append("\nV.1.0.60 --- (28/08/2016)\n");
		CHANGE_LOG.append("  - PR Form : Validate Fields on Create Event\n");
		CHANGE_LOG.append("  - PR Form : Add title to requester and preparer\n");
    	CHANGE_LOG.append("\nV.1.0.59 --- (27/08/2016)\n");
		CHANGE_LOG.append("  - PR,AV,AP Form : Move Field Budget Source to the first position\n");
		CHANGE_LOG.append("  - PD Interface : Create PD : Attache File Size 0\n");
    	CHANGE_LOG.append("\nV.1.0.58 --- (25/08/2016)\n");
		CHANGE_LOG.append("  - PR Interface : Login by admin\n");
		CHANGE_LOG.append("  - PR Pdf : Adjust Fields\n");
    	CHANGE_LOG.append("\nV.1.0.57 --- (24/08/2016)\n");
		CHANGE_LOG.append("  - PR Line Item : Add fiscal_year field\n");
		CHANGE_LOG.append("  - PR Pdf : Adjust Fields\n");
		CHANGE_LOG.append("  - AV Search : Workflow History\n");
		CHANGE_LOG.append("  - Table workflow history : Add Field status\n");
		CHANGE_LOG.append("  - AV Workflow : Add Expense Officer into Path\n");
    	CHANGE_LOG.append("\nV.1.0.55 --- (22/08/2016)\n");
		CHANGE_LOG.append("  - AV : Start Workflow\n");
    	CHANGE_LOG.append("\nV.1.0.54 --- (20/08/2016)\n");
		CHANGE_LOG.append("  - PR Form : Change Field Labels\n");
    	CHANGE_LOG.append("\nV.1.0.53 --- (18/08/2016)\n");
		CHANGE_LOG.append("  - PR Form : Add Contract Date Field\n");
    	CHANGE_LOG.append("\nV.1.0.52 --- (17/08/2016)\n");
		CHANGE_LOG.append("  - PR Form : Budget Status UI\n");
		CHANGE_LOG.append("  - PD Workflow Form : Add Consult Button\n");
    	CHANGE_LOG.append("\nV.1.0.51 --- (16/08/2016)\n");
		CHANGE_LOG.append("  - Search Employee : Ignore 'admin'\n");
		CHANGE_LOG.append("  - PR Form : Edit : Budget Source : Wrong Language\n");
		CHANGE_LOG.append("  - PR Form : Edit : Committee : Wrong Language\n");
		CHANGE_LOG.append("  - PR Search : Thai Columns\n");
		CHANGE_LOG.append("  - PR Interface : Change user from admin to current user\n");
    	CHANGE_LOG.append("\nV.1.0.50 --- (15/08/2016)\n");
		CHANGE_LOG.append("  - PR Form : User Tab : 2 Languages\n");
		CHANGE_LOG.append("  - PR Form : Search User : 2 Languages\n");
		CHANGE_LOG.append("  - PR Form : Committee Tab : 2 Languages\n");
		CHANGE_LOG.append("  - PR Committee : Add Field Title\n");
		CHANGE_LOG.append("  - PR Form : Search Committee : 2 Languages\n");
		CHANGE_LOG.append("  - PR Form : Objective Type and Reason : 2 Languages\n");
		CHANGE_LOG.append("  - PR Workflow : Add Procument Org Member Folder Permission\n");
		CHANGE_LOG.append("  - Workflow Form : View in browser\n");
		CHANGE_LOG.append("  - PR Pdf : Thai Fields\n");
    	CHANGE_LOG.append("\nV.1.0.49 --- (08/08/2016)\n");
		CHANGE_LOG.append("  - PR Pdf : Add Activity Group\n");
		CHANGE_LOG.append("  - PR Line Item : Edit Activity Group\n");
		CHANGE_LOG.append("  - Search Budget Source : 2 Languages\n");
		CHANGE_LOG.append("  - PR Form : Clear Across Budget when check Additional PR\n");
    	CHANGE_LOG.append("\nV.1.0.48 --- (08/08/2016)\n");
		CHANGE_LOG.append("  - PR Workflow Form : Edit File Description\n");
		CHANGE_LOG.append("  - AP Activity Combo Box : Google Search Style\n");
		CHANGE_LOG.append("  - Cost Control Combo Box : 2 Languages\n");
		CHANGE_LOG.append("  - PR Line Item : Add Activity Group Field\n");
    	CHANGE_LOG.append("\nV.1.0.47 --- (03/08/2016)\n");
		CHANGE_LOG.append("  - PR Workflow : Resubmit create multiple next action record\n");
		CHANGE_LOG.append("  - PR Form : Additional PR : Search PR that got same purchasing type\n");
		CHANGE_LOG.append("  - PR,AV,AP Form : Multiple file upload\n");
		CHANGE_LOG.append("  - PR Budget Source : Change Hint Message\n");
		CHANGE_LOG.append("  - PR Objective : Add Hint Message\n");
		CHANGE_LOG.append("  - Odoo : Change DB to V.5\n");
    	CHANGE_LOG.append("\nV.1.0.46 --- (01/08/2016)\n");
		CHANGE_LOG.append("  - PR Form : Reason field : Change from text to combobox\n");
		CHANGE_LOG.append("  - PR Form : File Description : Edit Button\n");
		CHANGE_LOG.append("  - PR Form : Additional PR : Search PR that not referenced by Other PR\n");
		CHANGE_LOG.append("  - PR Form : VAT : Change SQL\n");
    	CHANGE_LOG.append("\nV.1.0.45 --- (28/07/2016)\n");
		CHANGE_LOG.append("  - Workflow Error Msg : Change by current language\n");
    	CHANGE_LOG.append("\nV.1.0.44 --- (28/07/2016)\n");
		CHANGE_LOG.append("  - PR Interface : Change Parameter objective_type to method_type\n");
		CHANGE_LOG.append("  - PR Pdf : Adjust Layout\n");
		CHANGE_LOG.append("  - PD Resubmit : Missing Approver in History\n");
    	CHANGE_LOG.append("\nV.1.0.43 --- (27/07/2016)\n");
		CHANGE_LOG.append("  - PR Form : Committee : Delete until 1 committee left, App shows wrong position\n");
		CHANGE_LOG.append("  - Budget Source : Project : Show PM's name after selected language\n");
		CHANGE_LOG.append("  - Workflow History : Show User name after selected language\n");
		CHANGE_LOG.append("  - View in browser : Add file name after URL\n");
		CHANGE_LOG.append("  - Interface PR : Budget Source : Project : Send Wrong Value\n");
		CHANGE_LOG.append("  - PR : Resubmit with difference total, don't get new approval matrix\n");
		CHANGE_LOG.append("  - Workflow Share UI : Search Consult Button not work\n");
		CHANGE_LOG.append("  - PR,PD Pdf : Thai Name Signatures\n");
    	CHANGE_LOG.append("\nV.1.0.42 --- (21/07/2016)\n");
		CHANGE_LOG.append("  - PR Approval Matrix : Project Type\n");
		CHANGE_LOG.append("  - PR Form : Committee : Chairman Label\n");
		CHANGE_LOG.append("  - PR Form : Edit : Enable Currency Rate field if it is not 'THB'\n");
		CHANGE_LOG.append("  - PR Form : Additional PR : Clear Ref PR No. field if uncheck the checkbox\n");
    	CHANGE_LOG.append("\nV.1.0.41 --- (20/07/2016)\n");
		CHANGE_LOG.append("  - PD Interface : Action=1 : Check duplicated data\n");
		CHANGE_LOG.append("  - PD Interface : Action=3 : Remove Approver from History\n");
		CHANGE_LOG.append("  - Workflow : Tell the reason for rejecting in the comment field\n");
		CHANGE_LOG.append("  - PR Interface : Send Price instead of Converted Price\n");
		CHANGE_LOG.append("  - PD Workflow : Last Approver signature\n");
		CHANGE_LOG.append("  - PD Interface : Add Approver Permission to PD Folder\n");
    	CHANGE_LOG.append("\nV.1.0.40 --- (15/07/2016)\n");
		CHANGE_LOG.append("  - PR Form : User Tab : Multiple Language Label\n");
		CHANGE_LOG.append("  - PD Workflow : Add Approver into history\n");
    	CHANGE_LOG.append("\nV.1.0.39 --- (13/07/2016)\n");
		CHANGE_LOG.append("  - PR Form : Check Duplicated Committee\n");
		CHANGE_LOG.append("  - AV Interface : Fill Parameters from UI Input\n");
		CHANGE_LOG.append("  - UI : Multiple Languages\n");
    	CHANGE_LOG.append("\nV.1.0.38 --- (12/07/2016)\n");
		CHANGE_LOG.append("  - PR Form : Error Msg\n");
    	CHANGE_LOG.append("\nV.1.0.37 --- (12/07/2016)\n");
		CHANGE_LOG.append("  - AV Form : List Old AV\n");
		CHANGE_LOG.append("  - PD Interface : Change method from create to action\n");
    	CHANGE_LOG.append("\nV.1.0.36 --- (12/07/2016)\n");
		CHANGE_LOG.append("  - PR Form : Check Mandatory Equiment field\n");
		CHANGE_LOG.append("  - PR Form : Update Error Messages\n");
		CHANGE_LOG.append("  - PD Interface : Add Comment Field\n");
    	CHANGE_LOG.append("\nV.1.0.35 --- (11/07/2016)\n");
		CHANGE_LOG.append("  - PR Interface : Add Reason field\n");
		CHANGE_LOG.append("  - AV Form : Update table detail\n");
		CHANGE_LOG.append("  - AV : Remove field activity\n");
    	CHANGE_LOG.append("\nV.1.0.34 --- (08/07/2016)\n");
		CHANGE_LOG.append("  - PR,AV,AP Form : Change Budget Source Label\n");
		CHANGE_LOG.append("  - PR Form : Change Across Budget Label\n");
		CHANGE_LOG.append("  - PR Form : Change Location Label\n");
		CHANGE_LOG.append("  - PR Form : Change Old PR Label\n");
		CHANGE_LOG.append("  - PR Form : Change Price Label\n");
		CHANGE_LOG.append("  - PR,AV,AP Form : Budget Source : Main Equipment Explanation icon\n");
		CHANGE_LOG.append("  - AV : Add table detail\n");
    	CHANGE_LOG.append("\nV.1.0.33 --- (07/07/2016)\n");
		CHANGE_LOG.append("  - AV Form : Change Activity Combo Box to Text Area\n");
		CHANGE_LOG.append("  - AV Table : Change Activity Field Type from integer to varchar\n");
		CHANGE_LOG.append("  - PD Workflow : Comment History : Remove Doc Type from Comment\n");
		CHANGE_LOG.append("  - PR Form : Cross Year Budget : Filter Purchase Method\n");
    	CHANGE_LOG.append("\nV.1.0.32 --- (06/07/2016)\n");
		CHANGE_LOG.append("  - Main : Change MAIN_ODOO_URL and MAIN_ODOO_DB to PCM_* and EXP_*\n");
		CHANGE_LOG.append("  - PR Form : Line Item : Remove 'Type' Column\n");
		CHANGE_LOG.append("  - Interface : Create AV\n");
		CHANGE_LOG.append("  - AV,AP : Add Column employee position id for Interface\n");
		CHANGE_LOG.append("  - PR Interface : delete parameter purchase_confidential_id.id\n");
		CHANGE_LOG.append("  - PR Interface : change parameter confidential_detail to purchase_condition_detail\n");
    	CHANGE_LOG.append("\nV.1.0.31 --- (05/07/2016)\n");
		CHANGE_LOG.append("  - PD Interface : Resubmit and cancel from Odoo\n");
		CHANGE_LOG.append("  - PR Form : Read Method Cond2 from table\n");
    	CHANGE_LOG.append("\nV.1.0.30 --- (30/06/2016)\n");
		CHANGE_LOG.append("  - Workflow Table : add field 'execution_id'\n");
		CHANGE_LOG.append("  - Admin : System Config : MAIN_INF_PD_UPDATE_STATUS\n");
		CHANGE_LOG.append("  - AP : Copy : Clear Employee AV : Clear AV Id\n");
		CHANGE_LOG.append("  - AV,AP Form : Add Requester Tel NO. field\n");
    	CHANGE_LOG.append("\nV.1.0.29 --- (27/06/2016)\n");
		CHANGE_LOG.append("  - PD : Search Approval Matrix\n");
    	CHANGE_LOG.append("\nV.1.0.28 --- (22/06/2016)\n");
		CHANGE_LOG.append("  - Consult : Comment Required\n");
		CHANGE_LOG.append("  - PR : Send for Approval : Missing Attached Files\n");
		CHANGE_LOG.append("  - PR Form : Add Field Request Tel NO.\n");
		CHANGE_LOG.append("  - PR Pdf : Add Field Request Tel NO.\n");
    	CHANGE_LOG.append("\nV.1.0.27 --- (21/06/2016)\n");
		CHANGE_LOG.append("  - PR,AV,AP Form : Search Section : Add Column Cost Center\n");
		CHANGE_LOG.append("  - AP Form : Change comply to meeting requirement\n");
		CHANGE_LOG.append("  - Search : Google Style\n");
		CHANGE_LOG.append("  - PR : Change Committee Input Dialog\n");
    	CHANGE_LOG.append("\nV.1.0.26 --- (14/06/2016)\n");
		CHANGE_LOG.append("  - AP Form : Preview\n");
		CHANGE_LOG.append("  - AV Form : Preview\n");
		CHANGE_LOG.append("  - Main Menu : Change Labels\n");
		CHANGE_LOG.append("  - Interface : Create PR Interface : Wrong Assignee\n");
    	CHANGE_LOG.append("\nV.1.0.25 --- (13/06/2016)\n");
		CHANGE_LOG.append("  - AP Form : Activity Detail is not mandatory if no choice to select\n");
		CHANGE_LOG.append("  - AP Form : Edit Activity\n");
    	CHANGE_LOG.append("\nV.1.0.24 --- (10/06/2016)\n");
		CHANGE_LOG.append("  - AP Form : Tab Attendee : Show Cost Control\n");
		CHANGE_LOG.append("  - AP Form : Tab Info : Combo Boxes don't show Bank list\n");
    	CHANGE_LOG.append("\nV.1.0.23 --- (09/06/2016)\n");
		CHANGE_LOG.append("  - AP Form : Save Draft\n");
		CHANGE_LOG.append("  - AV Form : Copy AV Button\n");
    	CHANGE_LOG.append("\nV.1.0.22 --- (07/06/2016)\n");
		CHANGE_LOG.append("  - AV Form : Save Draft\n");
		CHANGE_LOG.append("  - Workflow Form : Reject : Error if no comment\n");
    	CHANGE_LOG.append("\nV.1.0.21 --- (02/06/2016)\n");
		CHANGE_LOG.append("  - PR Workflow : Consult Process\n");
		CHANGE_LOG.append("  - Scheduler : Reset Sequence\n");
		CHANGE_LOG.append("  - AP UI : Initial\n");
		CHANGE_LOG.append("  - AV UI : Initial\n");
		CHANGE_LOG.append("  - PR Workflow : Show Real Requester\n");
		CHANGE_LOG.append("  - PR : Budget CC : Show Project Code\n");
		CHANGE_LOG.append("  - PR Workflow : Fill PR Doc Description\n");
		CHANGE_LOG.append("  - PR & PD : Change Date Format to 'dd/mm/yyyy'\n");
		CHANGE_LOG.append("  - Workflow History : Sort Ascending\n");
		CHANGE_LOG.append("  - Interface : Check Budget : Change Paramters\n");
    	CHANGE_LOG.append("\nV.1.0.20 --- (25/05/2016)\n");
		CHANGE_LOG.append("  - Workflow Service : Separate service for PR and PD\n");
		CHANGE_LOG.append("  - PR Workflow : Reviewer check Budget every level\n");
    	CHANGE_LOG.append("\nV.1.0.19 --- (24/05/2016)\n");
		CHANGE_LOG.append("  - PR Unit : Query only Active Unit\n");
		CHANGE_LOG.append("  - PD Boss : Only Employee 001509\n");
		CHANGE_LOG.append("  - PR Workflow : Consult Button\n");
    	CHANGE_LOG.append("\nV.1.0.18 --- (23/05/2016)\n");
		CHANGE_LOG.append("  - PR Form : Check Budget : Show Error if budget is not enough\n");
		CHANGE_LOG.append("  - Interface : Create PR : Change parameter\n");
		CHANGE_LOG.append("  - PR Form : Upload File : Add Description field\n");
    	CHANGE_LOG.append("\nV.1.0.17 --- (22/05/2016)\n");
		CHANGE_LOG.append("  - PR Workflow : Convert Employee Code with '0' padding\n");
		CHANGE_LOG.append("  - PD Worfklow  : Show Error Dialog box with xmlrpc parameter\n");
		CHANGE_LOG.append("  - PD Form  : Search Additional PR\n");
		CHANGE_LOG.append("  - PD Form  : Check Across Budget with total\n");
    	CHANGE_LOG.append("\nV.1.0.16 --- (19/05/2016)\n");
		CHANGE_LOG.append("  - PR Form : Set Focus in Dialog\n");
		CHANGE_LOG.append("  - PD Interface : Update Status\n");
		CHANGE_LOG.append("  - Interface : Check Budget\n");
		CHANGE_LOG.append("  - PR Form : Choose Budget Cc : change grid column header\n");
		CHANGE_LOG.append("  - PR Form : Limit Max Length for some text fields\n");
		CHANGE_LOG.append("  - WF Approved : Last Approver Signature\n");
		CHANGE_LOG.append("  - Interface : Create PR : if error then rollback\n");
		CHANGE_LOG.append("  - Doc Folder : Add Permission to Procurement Section\n");
    	CHANGE_LOG.append("\nV.1.0.15 --- (16/05/2016)\n");
		CHANGE_LOG.append("  - DB : Change field name : pcm_req.method -> pcm_req.prweb_method_id\n");
		CHANGE_LOG.append("  - PR Form : List mandatory fields\n");
    	CHANGE_LOG.append("\nV.1.0.14 --- (15/05/2016)\n");
		CHANGE_LOG.append("  - PR Form : Check file count if total >= 100,000\n");
		CHANGE_LOG.append("  - PR Form : Save Draft don't close form\n");
		CHANGE_LOG.append("  - PR Form : Resize grid when window resize\n");
		CHANGE_LOG.append("  - PD Interface : Accept Thai Characters\n");
    	CHANGE_LOG.append("\nV.1.0.13 --- (13/05/2016)\n");
		CHANGE_LOG.append("  - PR Workflow : Gen PR Pdf at the last approval\n");
		CHANGE_LOG.append("  - DB : Drop table pb2_pcm_ord_dtl\n");
		CHANGE_LOG.append("  - PR Pdf : Fill with data from the PR Form\n");
		CHANGE_LOG.append("  - PD Grid : Show Section Name\n");
		CHANGE_LOG.append("  - Preview Pdf : Change URL\n");
		CHANGE_LOG.append("  - PD Form : Show Workflow History\n");
    	CHANGE_LOG.append("\nV.1.0.12 --- (08/05/2016)\n");
		CHANGE_LOG.append("  - PR Workflow : Find Path\n");
		CHANGE_LOG.append("  - PR Form : Validate Procurement Method and Committee\n");
    	CHANGE_LOG.append("\nV.1.0.11 --- (06/05/2016)\n");
		CHANGE_LOG.append("  - SQL : change LIKE to ILIKE for case insensitive search\n");
		CHANGE_LOG.append("  - PR Form : Change Requester\n");
		CHANGE_LOG.append("  - PR Form : If select THB currency, disable currency rate text field\n");
		CHANGE_LOG.append("  - PR Form : Fixed Bug : Cannot Delete PR if ECM Folder does not exist\n");
		CHANGE_LOG.append("  - PR Form : Get Currency Rate Error Message\n");
		CHANGE_LOG.append("  - PR Form : Across Budget,Ref ID Check Box : Hide/Show Text Field\n");
		CHANGE_LOG.append("\nV.1.0.10 --- (05/05/2016)\n");
		CHANGE_LOG.append("  - WF Form : Update Task Share UI Data from Edited Data\n");
		CHANGE_LOG.append("  - Interface : Create PR\n");
		CHANGE_LOG.append("  - Interface : Update PR Status\n");
		CHANGE_LOG.append("  - Interface : Create PD\n");
		CHANGE_LOG.append("  - Main Menu : Change Caption\n");
		CHANGE_LOG.append("\nV.1.0.9 --- (30/04/2016)\n");
		CHANGE_LOG.append("  - WF Form : Workflow History : Currenct Task shows 'Procurement' instead of '-'\n");
		CHANGE_LOG.append("  - WF Form : Update My Task Title from Edited Data\n");
		CHANGE_LOG.append("  - WF Form : Hide Equipment Radio on Line Item Dialog\n");
		CHANGE_LOG.append("  - WF Form : Hide Stock Location\n");
		CHANGE_LOG.append("  - WF Form : Event Clear Button\n");
		CHANGE_LOG.append("\nV.1.0.8 --- (28/04/2016)\n");
		CHANGE_LOG.append("  - WF Form : Search Event Dialog\n");
		CHANGE_LOG.append("  - WF Form : Search Section and Project Dialog\n");
		CHANGE_LOG.append("\nV.1.0.7 --- (25/04/2016)\n");
		CHANGE_LOG.append("  - WF Share UI : Edit Button\n");
		CHANGE_LOG.append("  - PR Workflow : Find Procurement Boss\n");
		CHANGE_LOG.append("  - PR Form : Remove Field : 'Stock','Pcm Ou'\n");
		CHANGE_LOG.append("  - PR Form : Tab User : Fill User Information\n");
		CHANGE_LOG.append("\nV.1.0.6 --- (21/04/2016)\n");
		CHANGE_LOG.append("  - PR Grid : Workflow History\n");
		CHANGE_LOG.append("  - PR Form : Add Committee\n");
		CHANGE_LOG.append("  - PR : Gen ID from DB sequence and Fiscal Year\n");
		CHANGE_LOG.append("  - PR Form : Add Line Item\n");
		CHANGE_LOG.append("  - PR Form : Add Field prototype_contract_no\n");
		CHANGE_LOG.append("  - PR Form : Download Middle Price Document\n");
		CHANGE_LOG.append("  - PR Search : Show Grid Field Value\n");
		CHANGE_LOG.append("  - PR Form : Prototype Combo Box\n");
		CHANGE_LOG.append("  - WF Share UI : Remove 'Reassign' Button\n");
		CHANGE_LOG.append("  - PR Form : Tab Committee\n");
		CHANGE_LOG.append("  - PR Form : Currency Combo Box\n");
		CHANGE_LOG.append("  - PR Form : Line Item : Product Uom Combo Box\n");
		CHANGE_LOG.append("  - New Table : pb2_ext_product_uom, pb2_ext_res_currency, pb2_ext_res_currency_rate\n");
		CHANGE_LOG.append("  - Delete Approval Matrix Code and Tables\n");
		CHANGE_LOG.append("  - Change Code Prefix 'NSTDA' to 'PB'\n");
		CHANGE_LOG.append("  - Make Ext JS Theme Font larger\n");
		CHANGE_LOG.append("\nV.1.0.5 --- (14/03/2016)\n");
		CHANGE_LOG.append("  - Add Procurement Order Page\n");
		CHANGE_LOG.append("\nV.1.0.4 --- (03/03/2016)\n");
		CHANGE_LOG.append("  - Add Approval Feature\n");
		CHANGE_LOG.append("\nV.1.0.3 --- (29/02/2016)\n");
		CHANGE_LOG.append("  - Update PR Form : Add Detail, Committee\n");
		CHANGE_LOG.append("\nV.1.0.2 --- (24/02/2016)\n");
		CHANGE_LOG.append("  - Update PR Form\n");
		CHANGE_LOG.append("  - Create XML RPC URL for Odoo\n");
		CHANGE_LOG.append("\nV.1.0.1 --- (23/02/2016)\n");
		CHANGE_LOG.append("  - Update PR UI\n");
		CHANGE_LOG.append("\nV.1.0.0 --- (17/02/2016)\n");
		CHANGE_LOG.append("  - Initial");
    }
    
    public static final String TABLE_PREFIX = "pb2_";
    public static final String TABLE_EXT_PREFIX = TABLE_PREFIX + "ext_";
}
