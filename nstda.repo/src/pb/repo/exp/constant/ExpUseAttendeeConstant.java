package pb.repo.exp.constant;

public class ExpUseAttendeeConstant {
	
	public static final String TABLE_NAME = "pb2_exp_use_attendee";
	
    /*
     * Table Field Name
     */
	public static final String TFN_ID = "ID";
	public static final String TFN_MASTER_ID = "MASTER_ID";
	public static final String TFN_TYPE = "TYPE";
	public static final String TFN_CODE = "CODE";
	public static final String TFN_TITLE = "TITLE";
	public static final String TFN_FNAME = "FNAME";
	public static final String TFN_LNAME = "LNAME";
	public static final String TFN_UNIT_TYPE = "UNIT_TYPE";
	public static final String TFN_POSITION = "POSITION";
	public static final String TFN_POSITION_ID = "POSITION_ID";
	public static final String TFN_TOTAL = "TOTAL";
	
	
    /*
     * JSON Field Name
     */
	public static final String JFN_ID = "id";
	public static final String JFN_MASTER_ID = "masterId";
	public static final String JFN_TYPE = "type";
	public static final String JFN_CODE = "code";
	public static final String JFN_TID = "tid";
	public static final String JFN_TITLE = "title";
	public static final String JFN_FNAME = "fname";
	public static final String JFN_LNAME = "lname";
	public static final String JFN_UNIT_TYPE = "utype";
	public static final String JFN_POSITION = "position";
	public static final String JFN_POSITION_ID = "positionId";
	public static final String JFN_CREATED_TIME = "createdTime";
	public static final String JFN_CREATED_BY = "createdBy";
	public static final String JFN_UPDATED_TIME = "updatedTime";
	public static final String JFN_UPDATED_BY = "updatedBy";
	public static final String JFN_ACTION = "action";

	/*
	 * TYPE
	 */
	public static final String T_EMPLOYEE = "E";
	public static final String T_OTHER = "O";
	
	/*
	 * UNIT TYPE
	 */
	public static final String UT_UNIT = "U";
	public static final String UT_PROJECT = "P";
}
