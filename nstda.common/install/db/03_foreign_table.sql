CREATE FOREIGN TABLE public.pb2_ext_account_activity
   (id integer NOT NULL,
    name character varying ,
    active boolean ,
    search_keywords character varying ,
    budget_method character varying ,
    special_workflow character varying ,
    internal_charge boolean )
   SERVER foreign_server
   OPTIONS (table_name 'account_activity');
ALTER FOREIGN TABLE public.pb2_ext_account_activity
  OWNER TO alfresco;

CREATE FOREIGN TABLE public.pb2_ext_account_activity_group
   (id integer NOT NULL,
    parent_left integer ,
    parent_right integer ,
    parent_id integer ,
    name character varying ,
    active boolean ,
    budget_method character varying ,
    special_workflow_emotion boolean )
   SERVER foreign_server
   OPTIONS (table_name 'account_activity_group');
ALTER FOREIGN TABLE public.pb2_ext_account_activity_group
  OWNER TO alfresco;
  
CREATE FOREIGN TABLE public.pb2_ext_account_fiscalyear
   (id integer ,
    name character varying ,
    state character varying )
   SERVER foreign_server
   OPTIONS (table_name 'account_fiscalyear');
ALTER FOREIGN TABLE public.pb2_ext_account_fiscalyear
  OWNER TO alfresco;
  
CREATE FOREIGN TABLE public.pb2_ext_account_tax
   (id integer NOT NULL,
    tax_code_id integer ,
    type character varying ,
    name character varying ,
    amount numeric ,
    type_tax_use character varying ,
    active boolean ,
    is_undue_tax boolean ,
    is_wht boolean )
   SERVER foreign_server
   OPTIONS (table_name 'account_tax');
ALTER FOREIGN TABLE public.pb2_ext_account_tax
  OWNER TO alfresco;
  
CREATE FOREIGN TABLE public.pb2_ext_activity_group_activity_rel
   (activity_group_id integer ,
    activity_id integer )
   SERVER foreign_server
   OPTIONS (table_name 'activity_group_activity_rel');
ALTER FOREIGN TABLE public.pb2_ext_activity_group_activity_rel
  OWNER TO alfresco;

CREATE FOREIGN TABLE public.pb2_ext_cost_control
   (id integer ,
    cost_control_type_id integer ,
    description text ,
    name character varying ,
    active boolean ,
    sector_id integer ,
    subsector_id integer ,
    division_id integer ,
    section_id integer ,
    org_id integer ,
    owner_level character varying ,
    public boolean )
   SERVER foreign_server
   OPTIONS (table_name 'cost_control');
ALTER FOREIGN TABLE public.pb2_ext_cost_control
  OWNER TO alfresco;

CREATE FOREIGN TABLE public.pb2_ext_cost_control_type
   (id integer ,
    description text ,
    name character varying ,
    active boolean )
   SERVER foreign_server
   OPTIONS (table_name 'cost_control_type');
ALTER FOREIGN TABLE public.pb2_ext_cost_control_type
  OWNER TO alfresco;

CREATE FOREIGN TABLE public.pb2_ext_hr_employee
   (id integer NOT NULL,
    employee_code character varying ,
    title_id integer ,
    first_name character varying ,
    last_name character varying ,
    gender character varying ,
    mobile_phone character varying ,
    work_email character varying ,
    work_phone character varying ,
    work_location character varying ,
    org_id integer ,
    costcenter_id integer ,
    section_id integer ,
    department_id integer ,
    image bytea ,
    image_small bytea ,
    name_related character varying ,
    position_id integer ,
    position_management_id integer ,
    is_management boolean )
   SERVER foreign_server
   OPTIONS (table_name 'hr_employee');
ALTER FOREIGN TABLE public.pb2_ext_hr_employee
  OWNER TO alfresco;

CREATE FOREIGN TABLE public.pb2_ext_hr_expense_clearing
   (id integer ,
    date timestamp without time zone ,
    advance_expense_id integer ,
    expense_id integer ,
    invoice_id integer ,
    expense_amount numeric ,
    clearing_amount numeric ,
    invoiced_amount numeric )
   SERVER foreign_server
   OPTIONS (table_name 'hr_expense_clearing');
ALTER FOREIGN TABLE public.pb2_ext_hr_expense_clearing
  OWNER TO alfresco;

CREATE FOREIGN TABLE public.pb2_ext_hr_expense_expense
   (id integer NOT NULL,
    employee_id integer NOT NULL,
    amount numeric ,
    "number" character varying NOT NULL,
    advance_expense_id integer ,
    is_employee_advance boolean ,
    is_advance_clearing boolean ,
    amount_advanced double precision ,
    name character varying )
   SERVER foreign_server
   OPTIONS (table_name 'hr_expense_expense');
ALTER FOREIGN TABLE public.pb2_ext_hr_expense_expense
  OWNER TO alfresco;

CREATE FOREIGN TABLE public.pb2_ext_hr_expense_rule
   (id integer NOT NULL,
    activity_id integer NOT NULL,
    amount double precision NOT NULL,
    condition_1 character varying ,
    condition_2 character varying ,
    "position" character varying ,
    uom character varying )
   SERVER foreign_server
   OPTIONS (table_name 'hr_expense_rule');
ALTER FOREIGN TABLE public.pb2_ext_hr_expense_rule
  OWNER TO alfresco;

CREATE FOREIGN TABLE public.pb2_ext_hr_position
   (id integer NOT NULL,
    description text ,
    name character varying NOT NULL)
   SERVER foreign_server
   OPTIONS (table_name 'hr_position');
ALTER FOREIGN TABLE public.pb2_ext_hr_position
  OWNER TO alfresco;

CREATE FOREIGN TABLE public.pb2_ext_ir_model_data
   (id integer ,
    module character varying ,
    name character varying ,
    res_id integer )
   SERVER foreign_server
   OPTIONS (table_name 'ir_model_data');
ALTER FOREIGN TABLE public.pb2_ext_ir_model_data
  OWNER TO alfresco;

CREATE FOREIGN TABLE public.pb2_ext_ir_translation
   (id integer NOT NULL,
    lang character varying ,
    src text ,
    name character varying NOT NULL,
    res_id integer ,
    module character varying ,
    state character varying ,
    comments text ,
    value text ,
    type character varying )
   SERVER foreign_server
   OPTIONS (table_name 'ir_translation');
ALTER FOREIGN TABLE public.pb2_ext_ir_translation
  OWNER TO alfresco;

CREATE FOREIGN TABLE public.pb2_ext_product_uom
   (id integer NOT NULL,
    name character varying ,
    active boolean )
   SERVER foreign_server
   OPTIONS (table_name 'product_uom');
ALTER FOREIGN TABLE public.pb2_ext_product_uom
  OWNER TO alfresco;

CREATE FOREIGN TABLE public.pb2_ext_prweb_purchase_method
   (id integer ,
    method_id integer ,
    type_id integer ,
    price_range_id integer ,
    write_uid integer ,
    doctype_id integer ,
    condition_id integer )
   SERVER foreign_server
   OPTIONS (table_name 'prweb_purchase_method');
ALTER FOREIGN TABLE public.pb2_ext_prweb_purchase_method
  OWNER TO alfresco;

CREATE FOREIGN TABLE public.pb2_ext_prweb_purchase_method_rel
   (committee_type_id integer ,
    method_id integer )
   SERVER foreign_server
   OPTIONS (table_name 'prweb_purchase_method_rel');
ALTER FOREIGN TABLE public.pb2_ext_prweb_purchase_method_rel
  OWNER TO alfresco;

CREATE FOREIGN TABLE public.pb2_ext_purchase_committee_type
   (id integer ,
    name character varying )
   SERVER foreign_server
   OPTIONS (table_name 'purchase_committee_type');
ALTER FOREIGN TABLE public.pb2_ext_purchase_committee_type
  OWNER TO alfresco;

CREATE FOREIGN TABLE public.pb2_ext_purchase_condition
   (id integer ,
    name character varying )
   SERVER foreign_server
   OPTIONS (table_name 'purchase_condition');
ALTER FOREIGN TABLE public.pb2_ext_purchase_condition
  OWNER TO alfresco;

CREATE FOREIGN TABLE public.pb2_ext_purchase_condition_detail
   (id integer NOT NULL,
    name character varying )
   SERVER foreign_server
   OPTIONS (table_name 'purchase_condition_detail');
ALTER FOREIGN TABLE public.pb2_ext_purchase_condition_detail
  OWNER TO alfresco;

CREATE FOREIGN TABLE public.pb2_ext_purchase_condition_rel
   (condition_id integer NOT NULL,
    condition_detail_id integer NOT NULL)
   SERVER foreign_server
   OPTIONS (table_name 'purchase_condition_rel');
ALTER FOREIGN TABLE public.pb2_ext_purchase_condition_rel
  OWNER TO alfresco;

CREATE FOREIGN TABLE public.pb2_ext_purchase_method
   (id integer ,
    name character varying )
   SERVER foreign_server
   OPTIONS (table_name 'purchase_method');
ALTER FOREIGN TABLE public.pb2_ext_purchase_method
  OWNER TO alfresco;

CREATE FOREIGN TABLE public.pb2_ext_purchase_price_range
   (id integer ,
    name character varying ,
    price_from double precision ,
    price_to double precision )
   SERVER foreign_server
   OPTIONS (table_name 'purchase_price_range');
ALTER FOREIGN TABLE public.pb2_ext_purchase_price_range
  OWNER TO alfresco;

CREATE FOREIGN TABLE public.pb2_ext_purchase_type
   (id integer ,
    name character varying )
   SERVER foreign_server
   OPTIONS (table_name 'purchase_type');
ALTER FOREIGN TABLE public.pb2_ext_purchase_type
  OWNER TO alfresco;

CREATE FOREIGN TABLE public.pb2_ext_purchasingunit_section_rel
   (purchasing_unit_id integer NOT NULL,
    section_id integer )
   SERVER foreign_server
   OPTIONS (table_name 'purchasingunit_section_rel');
ALTER FOREIGN TABLE public.pb2_ext_purchasingunit_section_rel
  OWNER TO alfresco;

CREATE FOREIGN TABLE public.pb2_ext_res_bank
   (id integer NOT NULL,
    code character varying ,
    name character varying ,
    active boolean ,
    abbrev character varying )
   SERVER foreign_server
   OPTIONS (table_name 'res_bank');
ALTER FOREIGN TABLE public.pb2_ext_res_bank
  OWNER TO alfresco;

CREATE FOREIGN TABLE public.pb2_ext_res_costcenter
   (id integer NOT NULL,
    code character varying ,
    name character varying )
   SERVER foreign_server
   OPTIONS (table_name 'res_costcenter');
ALTER FOREIGN TABLE public.pb2_ext_res_costcenter
  OWNER TO alfresco;

CREATE FOREIGN TABLE public.pb2_ext_res_currency
   (id integer NOT NULL,
    name character varying ,
    active boolean )
   SERVER foreign_server
   OPTIONS (table_name 'res_currency');
ALTER FOREIGN TABLE public.pb2_ext_res_currency
  OWNER TO alfresco;

CREATE FOREIGN TABLE public.pb2_ext_res_currency_rate
   (id integer NOT NULL,
    currency_id integer ,
    rate numeric ,
    write_date timestamp without time zone )
   SERVER foreign_server
   OPTIONS (table_name 'res_currency_rate');
ALTER FOREIGN TABLE public.pb2_ext_res_currency_rate
  OWNER TO alfresco;

CREATE FOREIGN TABLE public.pb2_ext_res_division
   (id integer NOT NULL,
    code character varying ,
    name character varying ,
    org_id integer ,
    active boolean )
   SERVER foreign_server
   OPTIONS (table_name 'res_division');
ALTER FOREIGN TABLE public.pb2_ext_res_division
  OWNER TO alfresco;

CREATE FOREIGN TABLE public.pb2_ext_res_fund
   (id integer NOT NULL,
    name character varying ,
    active boolean )
   SERVER foreign_server
   OPTIONS (table_name 'res_fund');
ALTER FOREIGN TABLE public.pb2_ext_res_fund
  OWNER TO alfresco;

CREATE FOREIGN TABLE public.pb2_ext_res_fund_project_rel
   (fund_id integer ,
    project_id integer )
   SERVER foreign_server
   OPTIONS (table_name 'res_fund_project_rel');
ALTER FOREIGN TABLE public.pb2_ext_res_fund_project_rel
  OWNER TO alfresco;

CREATE FOREIGN TABLE public.pb2_ext_res_fund_section_rel
   (fund_id integer ,
    section_id integer )
   SERVER foreign_server
   OPTIONS (table_name 'res_fund_section_rel');
ALTER FOREIGN TABLE public.pb2_ext_res_fund_section_rel
  OWNER TO alfresco;

CREATE FOREIGN TABLE public.pb2_ext_res_groups_users_rel
   (gid integer ,
    uid integer )
   SERVER foreign_server
   OPTIONS (table_name 'res_groups_users_rel');
ALTER FOREIGN TABLE public.pb2_ext_res_groups_users_rel
  OWNER TO alfresco;

CREATE FOREIGN TABLE public.pb2_ext_res_org
   (id integer NOT NULL,
    code character varying ,
    name character varying ,
    operating_unit_id integer ,
    name_short character varying ,
    active boolean )
   SERVER foreign_server
   OPTIONS (table_name 'res_org');
ALTER FOREIGN TABLE public.pb2_ext_res_org
  OWNER TO alfresco;

CREATE FOREIGN TABLE public.pb2_ext_res_partner_title
   (id integer NOT NULL,
    domain character varying ,
    name character varying ,
    shortcut character varying )
   SERVER foreign_server
   OPTIONS (table_name 'res_partner_title');
ALTER FOREIGN TABLE public.pb2_ext_res_partner_title
  OWNER TO alfresco;

CREATE FOREIGN TABLE public.pb2_ext_res_project
   (id integer NOT NULL,
    code character varying ,
    name character varying ,
    org_id integer ,
    costcenter_id integer ,
    active boolean )
   SERVER foreign_server
   OPTIONS (table_name 'res_project');
ALTER FOREIGN TABLE public.pb2_ext_res_project
  OWNER TO alfresco;

CREATE FOREIGN TABLE public.pb2_ext_res_project_member
   (id integer NOT NULL,
    project_id integer ,
    project_position character varying ,
    employee_id integer )
   SERVER foreign_server
   OPTIONS (table_name 'res_project_member');
ALTER FOREIGN TABLE public.pb2_ext_res_project_member
  OWNER TO alfresco;

CREATE FOREIGN TABLE public.pb2_ext_res_project_prototype
   (id integer NOT NULL,
    code character varying ,
    name character varying ,
    prototype_type character varying ,
    project_id integer )
   SERVER foreign_server
   OPTIONS (table_name 'res_project_prototype');
ALTER FOREIGN TABLE public.pb2_ext_res_project_prototype
  OWNER TO alfresco;

CREATE FOREIGN TABLE public.pb2_ext_res_section
   (id integer NOT NULL,
    code character varying ,
    name character varying ,
    division_id integer ,
    sector_id integer ,
    org_id integer ,
    costcenter_id integer ,
    active boolean ,
    name_short character varying ,
    subsector_id integer )
   SERVER foreign_server
   OPTIONS (table_name 'res_section');
ALTER FOREIGN TABLE public.pb2_ext_res_section
  OWNER TO alfresco;

CREATE FOREIGN TABLE public.pb2_ext_res_users
   (id integer ,
    active boolean ,
    login character varying(64) ,
    company_id integer ,
    default_operating_unit_id integer ,
    default_section_id integer )
   SERVER foreign_server
   OPTIONS (table_name 'res_users');
ALTER FOREIGN TABLE public.pb2_ext_res_users
  OWNER TO alfresco;

CREATE FOREIGN TABLE public.pb2_ext_wf_emotion_activity_group_section_rel
   (section_id integer ,
    activity_group_id integer )
   SERVER foreign_server
   OPTIONS (table_name 'wf_emotion_activity_group_section_rel');
ALTER FOREIGN TABLE public.pb2_ext_wf_emotion_activity_group_section_rel
  OWNER TO alfresco;

CREATE FOREIGN TABLE public.pb2_ext_wkf_cmd_approval_amount
   (id integer NOT NULL,
    level integer ,
    org_id integer ,
    amount_min double precision ,
    amount_max double precision ,
    doctype_id integer )
   SERVER foreign_server
   OPTIONS (table_name 'wkf_cmd_approval_amount');
ALTER FOREIGN TABLE public.pb2_ext_wkf_cmd_approval_amount
  OWNER TO alfresco;

CREATE FOREIGN TABLE public.pb2_ext_wkf_cmd_boss_level_approval
   (id integer NOT NULL,
    level integer ,
    employee_id integer ,
    section_id integer ,
    org_id integer )
   SERVER foreign_server
   OPTIONS (table_name 'wkf_cmd_boss_level_approval');
ALTER FOREIGN TABLE public.pb2_ext_wkf_cmd_boss_level_approval
  OWNER TO alfresco;

CREATE FOREIGN TABLE public.pb2_ext_wkf_cmd_boss_special_level
   (id integer NOT NULL,
    employee_id integer ,
    special_level integer ,
    section_id integer )
   SERVER foreign_server
   OPTIONS (table_name 'wkf_cmd_boss_special_level');
ALTER FOREIGN TABLE public.pb2_ext_wkf_cmd_boss_special_level
  OWNER TO alfresco;

CREATE FOREIGN TABLE public.pb2_ext_wkf_cmd_level
   (id integer NOT NULL,
    description text ,
    name character varying )
   SERVER foreign_server
   OPTIONS (table_name 'wkf_cmd_level');
ALTER FOREIGN TABLE public.pb2_ext_wkf_cmd_level
  OWNER TO alfresco;

CREATE FOREIGN TABLE public.pb2_ext_wkf_cmd_section_assign
   (id integer NOT NULL,
    employee_id integer NOT NULL,
    section_id integer NOT NULL)
   SERVER foreign_server
   OPTIONS (table_name 'wkf_cmd_section_assign');
ALTER FOREIGN TABLE public.pb2_ext_wkf_cmd_section_assign
  OWNER TO alfresco;

CREATE FOREIGN TABLE public.pb2_ext_wkf_cmd_special_amount_project_approval
   (id integer NOT NULL,
    amount_min double precision ,
    amount_max double precision ,
    doctype_id integer ,
    employee_id integer )
   SERVER foreign_server
   OPTIONS (table_name 'wkf_cmd_special_amount_project_approval');
ALTER FOREIGN TABLE public.pb2_ext_wkf_cmd_special_amount_project_approval
  OWNER TO alfresco;

CREATE FOREIGN TABLE public.pb2_ext_wkf_config_doctype
   (id integer NOT NULL,
    description text ,
    name character varying ,
    module character varying )
   SERVER foreign_server
   OPTIONS (table_name 'wkf_config_doctype');
ALTER FOREIGN TABLE public.pb2_ext_wkf_config_doctype
  OWNER TO alfresco;

CREATE FOREIGN TABLE public.pb2_ext_wkf_config_purchase_unit
   (id integer NOT NULL,
    description text ,
    name character varying ,
    org_id integer ,
    doctype_id integer )
   SERVER foreign_server
   OPTIONS (table_name 'wkf_config_purchase_unit');
ALTER FOREIGN TABLE public.pb2_ext_wkf_config_purchase_unit
  OWNER TO alfresco;

CREATE FOREIGN TABLE public.pb2_ext_wkf_config_purchase_unit_responsible
   (id integer NOT NULL,
    level character varying ,
    employee_id integer ,
    purchasing_unit_id integer )
   SERVER foreign_server
   OPTIONS (table_name 'wkf_config_purchase_unit_responsible');
ALTER FOREIGN TABLE public.pb2_ext_wkf_config_purchase_unit_responsible
  OWNER TO alfresco;
