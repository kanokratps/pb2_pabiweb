CREATE OR REPLACE VIEW public.pb2_activity_group_view AS 
 SELECT a.id,
    a.name,
    COALESCE(ir.value, a.name::text) AS name_th,
    a.budget_method,
    a.special_workflow_emotion,
    ic.internal_charge
   FROM pb2_ext_account_activity_group a
     LEFT JOIN ( SELECT pb2_ext_ir_translation.res_id,
            pb2_ext_ir_translation.value
           FROM pb2_ext_ir_translation
          WHERE pb2_ext_ir_translation.name::text = 'account.activity.group,name'::text AND pb2_ext_ir_translation.type::text = 'model'::text AND pb2_ext_ir_translation.lang::text = 'th_TH'::text) ir ON a.id = ir.res_id
     LEFT JOIN ( SELECT r.activity_group_id,
            true AS internal_charge
           FROM pb2_ext_account_activity a_1,
            pb2_ext_activity_group_activity_rel r
          WHERE a_1.internal_charge IS TRUE AND a_1.id = r.activity_id
          GROUP BY r.activity_group_id) ic ON ic.activity_group_id = a.id
  WHERE a.budget_method::text = 'expense'::text AND a.active = true;

ALTER TABLE public.pb2_activity_group_view
  OWNER TO alfresco;

CREATE OR REPLACE VIEW public.pb2_activity_view AS 
 SELECT a.id,
    a.name,
    COALESCE(ir.value, a.name::text) AS name_th,
    r.activity_group_id,
    a.search_keywords,
    a.budget_method,
    a.special_workflow,
    a.internal_charge
   FROM pb2_ext_account_activity a
     JOIN pb2_ext_activity_group_activity_rel r ON a.id = r.activity_id
     LEFT JOIN ( SELECT pb2_ext_ir_translation.res_id,
            pb2_ext_ir_translation.value
           FROM pb2_ext_ir_translation
          WHERE pb2_ext_ir_translation.name::text = 'account_activity,name'::text AND pb2_ext_ir_translation.type::text = 'model'::text AND pb2_ext_ir_translation.lang::text = 'th_TH'::text) ir ON a.id = ir.res_id
  WHERE a.budget_method::text = 'expense'::text AND a.active = true;

ALTER TABLE public.pb2_activity_view
  OWNER TO alfresco;

CREATE OR REPLACE VIEW public.pb2_av_outstanding_view AS 
 SELECT a.id,
    a.number,
    a.amount_advanced AS waitamt,
    a.cleared_amount,
    a.amount_advanced - a.cleared_amount::double precision AS balance,
    a.employee_code,
    a.name
   FROM ( SELECT exp.id,
            exp.number,
            COALESCE(adv.amount_advanced, exp.amount_advanced) AS amount_advanced,
            COALESCE(adv.cleared_amount, 0.0) AS cleared_amount,
            emp.employee_code,
            exp.name
           FROM pb2_ext_hr_expense_expense exp
             LEFT JOIN ( SELECT exp_1.id,
                    exp_1.number,
                    exp_1.amount_advanced,
                    clr.cleared_amount
                   FROM pb2_ext_hr_expense_expense exp_1
                     JOIN ( SELECT pb2_ext_hr_expense_clearing.advance_expense_id,
                            sum(pb2_ext_hr_expense_clearing.clearing_amount) AS cleared_amount
                           FROM pb2_ext_hr_expense_clearing
                          GROUP BY pb2_ext_hr_expense_clearing.advance_expense_id) clr ON clr.advance_expense_id = exp_1.id
                  WHERE exp_1.is_employee_advance = true) adv ON adv.id = exp.id
             LEFT JOIN pb2_ext_hr_employee emp ON exp.employee_id = emp.id
          WHERE exp.is_employee_advance = true) a
     LEFT JOIN pb2_exp_brw brw ON a.number::text = brw.id::text
  WHERE (a.amount_advanced - a.cleared_amount::double precision) > 0::double precision AND brw.id IS NOT NULL
  ORDER BY a.number;

ALTER TABLE public.pb2_av_outstanding_view
  OWNER TO alfresco;

CREATE OR REPLACE VIEW public.pb2_boss_emotion_view AS 
 SELECT pb2_boss_view.org_id,
    pb2_boss_view.section_id,
    pb2_boss_view.employee_id,
    pb2_boss_view.lvl,
        CASE
            WHEN pb2_boss_view.lvl::text = 'L01'::text OR pb2_boss_view.lvl::text = 'L07'::text THEN 0.01::double precision
            ELSE pb2_boss_view.amount_min
        END AS amount_min,
        CASE
            WHEN pb2_boss_view.lvl::text = 'L01'::text THEN 0.01::double precision
            ELSE pb2_boss_view.amount_max
        END AS amount_max,
    pb2_boss_view.first_name,
    pb2_boss_view.last_name,
    pb2_boss_view.doc_type,
    pb2_boss_view.is_special,
    pb2_boss_view.employee_code
   FROM pb2_boss_view
  WHERE pb2_boss_view.doc_type::text = 'EX'::text;

ALTER TABLE public.pb2_boss_emotion_view
  OWNER TO alfresco;
  
CREATE OR REPLACE VIEW public.pb2_boss_view AS 
 SELECT a.org_id,
    a.section_id,
    a.employee_id,
    a._level AS lvl,
    a.amount_min,
    a.amount_max,
    a.first_name,
    a.last_name,
    a.doc_type,
    '0'::text AS is_special,
    a.employee_code
   FROM ( SELECT b.org_id,
            b.section_id,
            b.employee_id,
            l.name AS _level,
            a_1.amount_min,
            a_1.amount_max,
            h.employee_code,
            h.first_name,
            h.last_name,
            d.name AS doc_type,
            '0' AS is_special,
            l.id AS level_id
           FROM pb2_ext_wkf_cmd_level l,
            pb2_ext_wkf_cmd_boss_level_approval b,
            pb2_ext_wkf_cmd_approval_amount a_1,
            pb2_ext_hr_employee h,
            pb2_ext_wkf_config_doctype d
          WHERE l.id = b.level AND a_1.org_id = b.org_id AND a_1.level = b.level AND a_1.doctype_id = d.id AND b.employee_id = h.id AND a_1.amount_min > 0::double precision AND a_1.amount_max > 0::double precision) a
     LEFT JOIN pb2_ext_wkf_cmd_boss_special_level s ON a.section_id = s.section_id AND s.special_level = a.level_id
  WHERE s.id IS NULL
UNION
 SELECT s.org_id,
    sl.section_id,
    sl.employee_id,
    l.name AS lvl,
    a.amount_min,
    a.amount_max,
    h.first_name,
    h.last_name,
    d.name AS doc_type,
    '1'::text AS is_special,
    h.employee_code
   FROM pb2_ext_wkf_cmd_level l,
    pb2_ext_wkf_cmd_boss_special_level sl,
    pb2_ext_res_section s,
    pb2_ext_wkf_cmd_approval_amount a,
    pb2_ext_hr_employee h,
    pb2_ext_wkf_config_doctype d
  WHERE l.id = sl.special_level AND sl.section_id = s.id AND a.org_id = s.org_id AND a.level = sl.special_level AND a.doctype_id = d.id AND sl.employee_id = h.id AND a.amount_min > 0::double precision AND a.amount_max > 0::double precision
  ORDER BY 5, 6, 10 DESC;

ALTER TABLE public.pb2_boss_view
  OWNER TO alfresco;
COMMENT ON VIEW public.pb2_boss_view
  IS 'select * from
(สายอนุมัติ ทั้งหมดที่มีการตัดข้อมูลที่มีใน Special Level ( pb2_ext_wkf_cmd_boss_special_level ) ออก)
union
(สายอนุมัติ ที่เป็น Special Level ( pb2_ext_wkf_cmd_boss_special_level ))
';

CREATE OR REPLACE VIEW public.pb2_employee_info_view AS 
 SELECT e.id,
    e.employee_code,
    e.first_name,
    t.name AS title,
    e.last_name,
    e.work_phone,
    e.mobile_phone,
    o.name AS org_desc,
    s.id AS section_id,
    concat('[', btrim(s.code::text), '] ', s.name) AS section_desc,
    d.name AS div_name,
    COALESCE(irt.value, t.name::text) AS title_th,
    COALESCE(irf.value, e.first_name::text) AS first_name_th,
    COALESCE(irl.value, e.last_name::text) AS last_name_th,
    COALESCE(iro.value, o.name::text) AS org_desc_th,
    concat('[', btrim(s.code::text), '] ', COALESCE(irs.value, s.name::text)) AS section_desc_th,
    COALESCE(ird.value, d.name::text) AS div_name_th,
    po.name AS "position",
    COALESCE(irp.value, po.name::text) AS position_th,
    e.org_id
   FROM pb2_ext_hr_employee e
     JOIN pb2_ext_res_users u ON e.employee_code::text = u.login::text AND u.active = true
     LEFT JOIN pb2_ext_res_org o ON e.org_id = o.id
     LEFT JOIN pb2_ext_hr_position po ON e.position_id = po.id
     LEFT JOIN pb2_ext_res_partner_title t ON e.title_id = t.id
     LEFT JOIN pb2_ext_res_section s ON e.section_id = s.id
     LEFT JOIN pb2_ext_res_division d ON d.id = s.division_id
     LEFT JOIN ( SELECT pb2_ext_ir_translation.res_id,
            pb2_ext_ir_translation.value
           FROM pb2_ext_ir_translation
          WHERE pb2_ext_ir_translation.name::text = 'res.partner.title,name'::text AND pb2_ext_ir_translation.type::text = 'model'::text AND pb2_ext_ir_translation.lang::text = 'th_TH'::text) irt ON t.id = irt.res_id
     LEFT JOIN ( SELECT pb2_ext_ir_translation.res_id,
            pb2_ext_ir_translation.value
           FROM pb2_ext_ir_translation
          WHERE pb2_ext_ir_translation.name::text = 'hr.employee,first_name'::text AND pb2_ext_ir_translation.type::text = 'model'::text AND pb2_ext_ir_translation.lang::text = 'th_TH'::text) irf ON e.id = irf.res_id
     LEFT JOIN ( SELECT pb2_ext_ir_translation.res_id,
            pb2_ext_ir_translation.value
           FROM pb2_ext_ir_translation
          WHERE pb2_ext_ir_translation.name::text = 'hr.employee,last_name'::text AND pb2_ext_ir_translation.type::text = 'model'::text AND pb2_ext_ir_translation.lang::text = 'th_TH'::text) irl ON e.id = irl.res_id
     LEFT JOIN ( SELECT pb2_ext_ir_translation.res_id,
            pb2_ext_ir_translation.value
           FROM pb2_ext_ir_translation
          WHERE pb2_ext_ir_translation.name::text = 'res.org,name'::text AND pb2_ext_ir_translation.type::text = 'model'::text AND pb2_ext_ir_translation.lang::text = 'th_TH'::text) iro ON o.id = iro.res_id
     LEFT JOIN ( SELECT pb2_ext_ir_translation.res_id,
            pb2_ext_ir_translation.value
           FROM pb2_ext_ir_translation
          WHERE pb2_ext_ir_translation.name::text = 'res.section,name'::text AND pb2_ext_ir_translation.type::text = 'model'::text AND pb2_ext_ir_translation.lang::text = 'th_TH'::text) irs ON s.id = irs.res_id
     LEFT JOIN ( SELECT pb2_ext_ir_translation.res_id,
            pb2_ext_ir_translation.value
           FROM pb2_ext_ir_translation
          WHERE pb2_ext_ir_translation.name::text = 'res.division,name'::text AND pb2_ext_ir_translation.type::text = 'model'::text AND pb2_ext_ir_translation.lang::text = 'th_TH'::text) ird ON d.id = ird.res_id
     LEFT JOIN ( SELECT pb2_ext_ir_translation.res_id,
            pb2_ext_ir_translation.value
           FROM pb2_ext_ir_translation
          WHERE pb2_ext_ir_translation.name::text = 'hr.position,name'::text AND pb2_ext_ir_translation.type::text = 'model'::text AND pb2_ext_ir_translation.lang::text = 'th_TH'::text) irp ON po.id = irp.res_id;

ALTER TABLE public.pb2_employee_info_view
  OWNER TO alfresco;

CREATE OR REPLACE VIEW public.pb2_exp_brw_view AS 
 SELECT req.id,
    req.total,
    req.objective_type,
    mm.objective_type_name,
    mm.objective_type_name_th,
    req.objective,
    req.budget_cc,
    req.budget_cc_type,
    b.name AS budget_cc_name,
    b.name_th AS budget_cc_name_th,
    req.wf_by,
    req.wf_by_th,
    req.wf_by_time,
    req.wf_status,
    req.wf_status_th,
    req.status,
    req.reason,
    req.cost_control_id,
    req.cost_control_type_id,
    req.workflow_ins_id,
    req.task_id,
    req.updated_time,
    req.updated_by,
    req.created_time,
    req.folder_ref,
    req.doc_ref,
        CASE
            WHEN req.status::text = 'D'::text THEN '1'::character varying
            WHEN req.status::text = 'W2'::text THEN '2'::character varying
            WHEN req.status::text = 'W1'::text THEN '3'::character varying
            WHEN req.status::text = 'S'::text THEN '4'::character varying
            WHEN req.status::text = 'C1'::text THEN '5'::character varying
            WHEN req.status::text = 'C2'::text THEN '6'::character varying
            WHEN req.status::text = 'X1'::text THEN '7'::character varying
            WHEN req.status::text = 'X2'::text THEN '8'::character varying
            ELSE req.status
        END AS order_field,
    COALESCE(v_created_by.first_name, req.created_by) AS created_by,
    COALESCE(v_created_by.first_name_th, req.created_by::text) AS created_by_th,
    COALESCE(v_req_by.first_name, req.req_by) AS req_by,
    COALESCE(v_req_by.first_name_th, req.req_by::text) AS req_by_th,
    all_rev.all_rev,
    req.created_by AS created_by_code,
    req.req_by AS req_by_code,
    req.remark,
    req.av_reason,
    req.requested_time
   FROM ( SELECT pb2_exp_brw.id,
            pb2_exp_brw.total,
            pb2_exp_brw.objective_type,
            pb2_exp_brw.objective,
            pb2_exp_brw.budget_cc,
            pb2_exp_brw.budget_cc_type,
            NULL::character varying AS wf_by,
            NULL::text AS wf_by_th,
            NULL::timestamp with time zone AS wf_by_time,
            NULL::character varying AS wf_status,
            NULL::character varying AS wf_status_th,
            NULL::character varying AS task_id,
            pb2_exp_brw.workflow_ins_id,
            pb2_exp_brw.status,
            pb2_exp_brw.reason,
            pb2_exp_brw.cost_control_id,
            pb2_exp_brw.cost_control_type_id,
            pb2_exp_brw.folder_ref,
            pb2_exp_brw.doc_ref,
            pb2_exp_brw.created_by,
            pb2_exp_brw.req_by,
            pb2_exp_brw.updated_time,
            pb2_exp_brw.created_time,
            pb2_exp_brw.updated_by,
            pb2_exp_brw.note AS remark,
            pb2_exp_brw.av_remark AS av_reason,
            pb2_exp_brw.requested_time
           FROM pb2_exp_brw
          WHERE pb2_exp_brw.status::text = 'D'::text
        UNION
         SELECT brw.id,
            brw.total,
            brw.objective_type,
            brw.objective,
            brw.budget_cc,
            brw.budget_cc_type,
            w.by,
            w.by_th,
            w.by_time,
            w.status AS wf_status,
            w.status_th AS wf_status_th,
            w.task_id,
            brw.workflow_ins_id,
            brw.status,
            brw.reason,
            brw.cost_control_id,
            brw.cost_control_type_id,
            brw.folder_ref,
            brw.doc_ref,
            brw.created_by,
            brw.req_by,
            brw.updated_time,
            brw.created_time,
            brw.updated_by,
            brw.note AS remark,
            brw.av_remark AS av_reason,
            brw.requested_time
           FROM pb2_exp_brw brw
             LEFT JOIN ( SELECT w_1.master_id,
                    w_1.workflow_ins_id,
                    w_1.status,
                    w_1.status_th,
                    COALESCE(e.first_name, w_1.by) AS by,
                    COALESCE(e.first_name_th, w_1.by::text) AS by_th,
                    w_1.by_time,
                    w_1.task_id
                   FROM pb2_main_workflow w_1
                     LEFT JOIN pb2_hr_employee_view e ON w_1.by::text = e.employee_code::text
                  WHERE w_1.type::text = 'EXP_BRW'::text) w ON brw.id::text = w.master_id::text
          WHERE brw.status::text <> 'D'::text) req
     LEFT JOIN ( SELECT pb2_hr_employee_view.employee_code,
            pb2_hr_employee_view.first_name,
            pb2_hr_employee_view.first_name_th
           FROM pb2_hr_employee_view) v_created_by ON req.created_by::text = v_created_by.employee_code::text
     LEFT JOIN ( SELECT pb2_hr_employee_view.employee_code,
            pb2_hr_employee_view.first_name,
            pb2_hr_employee_view.first_name_th
           FROM pb2_hr_employee_view) v_req_by ON req.req_by::text = v_req_by.employee_code::text
     LEFT JOIN ( SELECT pb2_main_master.code,
            pb2_main_master.name AS objective_type_name_th,
            pb2_main_master.flag2 AS objective_type_name
           FROM pb2_main_master
          WHERE pb2_main_master.type::text = 'BRW_TYPE'::text) mm ON req.objective_type::text = mm.code::text
     LEFT JOIN ( SELECT 'U'::text AS _type,
            pb2_section_view.id,
            pb2_section_view.description AS name,
            pb2_section_view.description_th AS name_th
           FROM pb2_section_view
        UNION
         SELECT 'P'::text AS _type,
            pb2_project_view.id,
            pb2_project_view.description AS name,
            pb2_project_view.description_th AS name_th
           FROM pb2_project_view) b ON req.budget_cc_type::text = b._type AND req.budget_cc = b.id
     LEFT JOIN ( SELECT e.master_id,
            string_agg(e.all_rev::text, ' '::text) AS all_rev
           FROM ( SELECT pb2_main_workflow_reviewer.master_id,
                    pb2_main_workflow_reviewer.reviewer_user AS all_rev
                   FROM pb2_main_workflow_reviewer
                  WHERE pb2_main_workflow_reviewer.master_id::text ~~ 'AV%'::text
                UNION
                 SELECT pb2_main_workflow_next_actor.master_id,
                    pb2_main_workflow_next_actor.actor_user AS all_rev
                   FROM pb2_main_workflow_next_actor
                  WHERE pb2_main_workflow_next_actor.master_id::text ~~ 'AV%'::text) e
          GROUP BY e.master_id) all_rev ON req.id::text = all_rev.master_id::text;

ALTER TABLE public.pb2_exp_brw_view
  OWNER TO alfresco;

CREATE OR REPLACE VIEW public.pb2_exp_use_view AS 
 SELECT req.id,
    req.status,
    req.total,
    req.objective,
    req.reason,
    req.budget_cc,
    req.budget_cc_type,
    req.cost_control_id,
    req.cost_control_type_id,
    req.cost_control,
    req.cost_control_from,
    req.cost_control_to,
    req.bank,
    req.bank_type,
    req.workflow_ins_id,
    req.updated_time,
    req.updated_by,
    req.created_time,
    req.folder_ref,
    req.doc_ref,
        CASE
            WHEN req.status::text = 'D'::text THEN '1'::character varying
            WHEN req.status::text = 'W2'::text THEN '2'::character varying
            WHEN req.status::text = 'W1'::text THEN '3'::character varying
            WHEN req.status::text = 'S'::text THEN '4'::character varying
            WHEN req.status::text = 'C1'::text THEN '5'::character varying
            WHEN req.status::text = 'C2'::text THEN '6'::character varying
            WHEN req.status::text = 'X1'::text THEN '7'::character varying
            WHEN req.status::text = 'X2'::text THEN '8'::character varying
            ELSE req.status
        END AS order_field,
    b.name AS budget_cc_name,
    b.name_th AS budget_cc_name_th,
    req.wf_by,
    req.wf_by_th,
    req.wf_by_time,
    req.wf_status,
    req.wf_status_th,
    req.task_id,
    COALESCE(v_created_by.first_name, req.created_by) AS created_by,
    COALESCE(v_created_by.first_name_th, req.created_by::text) AS created_by_th,
    COALESCE(v_req_by.first_name, req.req_by) AS req_by,
    COALESCE(v_req_by.first_name_th, req.req_by::text) AS req_by_th,
    mm.pay_type_name,
    mm.pay_type_name_th,
    all_rev.all_rev,
    req.created_by AS created_by_code,
    req.req_by AS req_by_code,
    req.remark,
    req.requested_time,
    req.emotion
   FROM ( SELECT pb2_exp_use.id,
            pb2_exp_use.status,
            pb2_exp_use.total,
            pb2_exp_use.objective,
            pb2_exp_use.reason,
            pb2_exp_use.budget_cc,
            pb2_exp_use.budget_cc_type,
            pb2_exp_use.cost_control_id,
            pb2_exp_use.cost_control_type_id,
            pb2_exp_use.cost_control,
            pb2_exp_use.cost_control_from,
            pb2_exp_use.cost_control_to,
            pb2_exp_use.bank,
            pb2_exp_use.bank_type,
            pb2_exp_use.workflow_ins_id,
            pb2_exp_use.updated_time,
            pb2_exp_use.updated_by,
            pb2_exp_use.created_time,
            pb2_exp_use.folder_ref,
            pb2_exp_use.doc_ref,
            NULL::character varying AS wf_by,
            NULL::text AS wf_by_th,
            NULL::timestamp with time zone AS wf_by_time,
            NULL::character varying AS wf_status,
            NULL::character varying AS wf_status_th,
            NULL::character varying AS task_id,
            pb2_exp_use.created_by,
            pb2_exp_use.req_by,
            pb2_exp_use.pay_type,
            pb2_exp_use.note AS remark,
            pb2_exp_use.requested_time,
            ( SELECT 1
                   FROM pb2_ext_wf_emotion_activity_group_section_rel e
                  WHERE e.section_id = pb2_exp_use.budget_cc
                 LIMIT 1) AS emotion
           FROM pb2_exp_use
          WHERE pb2_exp_use.status::text = 'D'::text
        UNION
         SELECT exp.id,
            exp.status,
            exp.total,
            exp.objective,
            exp.reason,
            exp.budget_cc,
            exp.budget_cc_type,
            exp.cost_control_id,
            exp.cost_control_type_id,
            exp.cost_control,
            exp.cost_control_from,
            exp.cost_control_to,
            exp.bank,
            exp.bank_type,
            exp.workflow_ins_id,
            exp.updated_time,
            exp.updated_by,
            exp.created_time,
            exp.folder_ref,
            exp.doc_ref,
            w.by,
            w.by_th,
            w.by_time,
            w.status AS wf_status,
            w.status_th AS wf_status_th,
            w.task_id,
            exp.created_by,
            exp.req_by,
            exp.pay_type,
            exp.note AS remark,
            exp.requested_time,
            ( SELECT 1
                   FROM pb2_ext_wf_emotion_activity_group_section_rel ee
                  WHERE ee.section_id = exp.budget_cc
                 LIMIT 1) AS emotion
           FROM pb2_exp_use exp
             LEFT JOIN ( SELECT w_1.master_id,
                    w_1.workflow_ins_id,
                    w_1.status,
                    w_1.status_th,
                    COALESCE(e.first_name, w_1.by) AS by,
                    COALESCE(e.first_name_th, w_1.by::text) AS by_th,
                    w_1.by_time,
                    w_1.task_id
                   FROM pb2_main_workflow w_1
                     LEFT JOIN pb2_hr_employee_view e ON w_1.by::text = e.employee_code::text
                  WHERE w_1.type::text = 'EXP_USE'::text) w ON exp.id::text = w.master_id::text
          WHERE exp.status::text <> 'D'::text) req
     LEFT JOIN ( SELECT pb2_hr_employee_view.employee_code,
            pb2_hr_employee_view.first_name,
            pb2_hr_employee_view.first_name_th
           FROM pb2_hr_employee_view) v_created_by ON req.created_by::text = v_created_by.employee_code::text
     LEFT JOIN ( SELECT pb2_hr_employee_view.employee_code,
            pb2_hr_employee_view.first_name,
            pb2_hr_employee_view.first_name_th
           FROM pb2_hr_employee_view) v_req_by ON req.req_by::text = v_req_by.employee_code::text
     LEFT JOIN ( SELECT pb2_main_master.code,
            pb2_main_master.name AS pay_type_name_th,
            pb2_main_master.flag2 AS pay_type_name
           FROM pb2_main_master
          WHERE pb2_main_master.type::text = 'EXP_TYPE'::text) mm ON req.pay_type::text = mm.code::text
     LEFT JOIN ( SELECT 'U'::text AS _type,
            pb2_section_view.id,
            pb2_section_view.description AS name,
            pb2_section_view.description_th AS name_th
           FROM pb2_section_view
        UNION
         SELECT 'P'::text AS _type,
            pb2_project_view.id,
            pb2_project_view.description AS name,
            pb2_project_view.description_th AS name_th
           FROM pb2_project_view) b ON req.budget_cc_type::text = b._type AND req.budget_cc = b.id
     LEFT JOIN ( SELECT e.master_id,
            string_agg(e.all_rev::text, ' '::text) AS all_rev
           FROM ( SELECT pb2_main_workflow_reviewer.master_id,
                    pb2_main_workflow_reviewer.reviewer_user AS all_rev
                   FROM pb2_main_workflow_reviewer
                  WHERE pb2_main_workflow_reviewer.master_id::text ~~ 'EX%'::text
                UNION
                 SELECT pb2_main_workflow_next_actor.master_id,
                    pb2_main_workflow_next_actor.actor_user AS all_rev
                   FROM pb2_main_workflow_next_actor
                  WHERE pb2_main_workflow_next_actor.master_id::text ~~ 'EX%'::text) e
          GROUP BY e.master_id) all_rev ON req.id::text = all_rev.master_id::text;

ALTER TABLE public.pb2_exp_use_view
  OWNER TO alfresco;
  
CREATE OR REPLACE VIEW public.pb2_hr_employee_view AS 
 SELECT e.employee_code,
    e.first_name,
    e.last_name,
    COALESCE(ir_fname.value, e.first_name::text) AS first_name_th,
    COALESCE(ir_lname.value, e.last_name::text) AS last_name_th,
    ir_title.title,
    COALESCE(ir_title.title_th, ir_title.title::text) AS title_th,
    ir_position."position",
    COALESCE(ir_position.position_th, ir_position."position"::text) AS position_th,
    ir_org.org_name_short,
    COALESCE(ir_org.org_name_short_th, ir_org.org_name_short::text) AS org_name_short_th,
    concat('[', btrim(ir_section.section_code::text), '] ', ir_section.section) AS utype,
    concat('[', btrim(ir_section.section_code::text), '] ', COALESCE(ir_section.section_th, ir_section.section::text)) AS utype_th,
    e.position_id,
    e.work_phone,
    e.mobile_phone,
    ir_org.org_name,
    COALESCE(ir_org.org_name_th, ir_org.org_name::text) AS org_name_th
   FROM pb2_ext_res_users u
     JOIN pb2_ext_hr_employee e ON u.active = true AND u.login::text = e.employee_code::text
     LEFT JOIN ( SELECT pb2_ext_ir_translation.res_id,
            pb2_ext_ir_translation.value
           FROM pb2_ext_ir_translation
          WHERE pb2_ext_ir_translation.name::text = 'hr.employee,first_name'::text AND pb2_ext_ir_translation.type::text = 'model'::text AND pb2_ext_ir_translation.lang::text = 'th_TH'::text) ir_fname ON e.id = ir_fname.res_id
     LEFT JOIN ( SELECT pb2_ext_ir_translation.res_id,
            pb2_ext_ir_translation.value
           FROM pb2_ext_ir_translation
          WHERE pb2_ext_ir_translation.name::text = 'hr.employee,last_name'::text AND pb2_ext_ir_translation.type::text = 'model'::text AND pb2_ext_ir_translation.lang::text = 'th_TH'::text) ir_lname ON e.id = ir_lname.res_id
     LEFT JOIN ( SELECT t.id,
            t.name AS title,
            ir.value AS title_th
           FROM pb2_ext_res_partner_title t
             LEFT JOIN ( SELECT pb2_ext_ir_translation.res_id,
                    pb2_ext_ir_translation.value
                   FROM pb2_ext_ir_translation
                  WHERE pb2_ext_ir_translation.name::text = 'res.partner.title,name'::text AND pb2_ext_ir_translation.type::text = 'model'::text AND pb2_ext_ir_translation.lang::text = 'th_TH'::text) ir ON t.id = ir.res_id) ir_title ON e.title_id = ir_title.id
     LEFT JOIN ( SELECT p.id,
            p.name AS "position",
            ir.value AS position_th
           FROM pb2_ext_hr_position p
             LEFT JOIN ( SELECT pb2_ext_ir_translation.res_id,
                    pb2_ext_ir_translation.value
                   FROM pb2_ext_ir_translation
                  WHERE pb2_ext_ir_translation.name::text = 'hr.position,name'::text AND pb2_ext_ir_translation.type::text = 'model'::text AND pb2_ext_ir_translation.lang::text = 'th_TH'::text) ir ON p.id = ir.res_id) ir_position ON e.position_id = ir_position.id
     LEFT JOIN ( SELECT o.id,
            o.name_short AS org_name_short,
            ir.value AS org_name_short_th,
            o.name AS org_name,
            ir2.value AS org_name_th
           FROM pb2_ext_res_org o
             LEFT JOIN ( SELECT pb2_ext_ir_translation.res_id,
                    pb2_ext_ir_translation.value
                   FROM pb2_ext_ir_translation
                  WHERE pb2_ext_ir_translation.name::text = 'res.org,name_short'::text AND pb2_ext_ir_translation.type::text = 'model'::text AND pb2_ext_ir_translation.lang::text = 'th_TH'::text) ir ON o.id = ir.res_id
             LEFT JOIN ( SELECT pb2_ext_ir_translation.res_id,
                    pb2_ext_ir_translation.value
                   FROM pb2_ext_ir_translation
                  WHERE pb2_ext_ir_translation.name::text = 'res.org,name'::text AND pb2_ext_ir_translation.type::text = 'model'::text AND pb2_ext_ir_translation.lang::text = 'th_TH'::text) ir2 ON o.id = ir2.res_id) ir_org ON e.org_id = ir_org.id
     LEFT JOIN ( SELECT s.id,
            s.code AS section_code,
            s.name AS section,
            ir.value AS section_th
           FROM pb2_ext_res_section s
             LEFT JOIN ( SELECT pb2_ext_ir_translation.res_id,
                    pb2_ext_ir_translation.value
                   FROM pb2_ext_ir_translation
                  WHERE pb2_ext_ir_translation.name::text = 'res.section,name'::text AND pb2_ext_ir_translation.type::text = 'model'::text AND pb2_ext_ir_translation.lang::text = 'th_TH'::text) ir ON s.id = ir.res_id) ir_section ON e.section_id = ir_section.id
  WHERE e.id <> 1;

ALTER TABLE public.pb2_hr_employee_view
  OWNER TO alfresco;
  
CREATE OR REPLACE VIEW public.pb2_pcm_ord_view AS 
 SELECT ord.id,
    ord.total,
    ord.objective,
    ord.section_id,
    ord.pr_id,
    ord.doc_type,
    ord.app_by,
    ord.status,
    ord.workflow_ins_id,
    w.task_id,
    ord.updated_time,
    ord.created_time,
    ord.updated_by,
    ord.folder_ref,
    ord.doc_ref,
    w.by AS wf_by,
    w.by_th AS wf_by_th,
    w.by_time AS wf_by_time,
    w.status AS wf_status,
    w.status_th AS wf_status_th,
        CASE
            WHEN ord.status::text = 'D'::text THEN '1'::character varying
            WHEN ord.status::text = 'W2'::text THEN '2'::character varying
            WHEN ord.status::text = 'W1'::text THEN '3'::character varying
            WHEN ord.status::text = 'S'::text THEN '4'::character varying
            WHEN ord.status::text = 'C1'::text THEN '5'::character varying
            WHEN ord.status::text = 'X1'::text THEN '6'::character varying
            ELSE ord.status
        END AS order_field,
    COALESCE(v_created_by.first_name, ord.created_by) AS created_by,
    COALESCE(v_created_by.first_name_th, ord.created_by::text) AS created_by_th,
    v_section.org_name,
    v_section.org_name_th,
    all_rev.all_rev,
    ord.created_by AS created_by_code,
    d.description AS method_name
   FROM pb2_pcm_ord ord
     LEFT JOIN ( SELECT w_1.master_id,
            w_1.workflow_ins_id,
            w_1.status,
            w_1.status_th,
            COALESCE(e.first_name, w_1.by) AS by,
            COALESCE(e.first_name_th, w_1.by::text) AS by_th,
            w_1.by_time,
            w_1.task_id
           FROM pb2_main_workflow w_1
             LEFT JOIN pb2_hr_employee_view e ON w_1.by::text = e.employee_code::text
          WHERE w_1.type::text = 'PCM_ORD'::text) w ON ord.id::text = w.master_id::text
     LEFT JOIN ( SELECT pb2_hr_employee_view.employee_code,
            pb2_hr_employee_view.first_name,
            pb2_hr_employee_view.first_name_th
           FROM pb2_hr_employee_view) v_created_by ON ord.created_by::text = v_created_by.employee_code::text
     LEFT JOIN ( SELECT pb2_section_view.id,
            pb2_section_view.name AS org_name,
            pb2_section_view.name_th AS org_name_th
           FROM pb2_section_view) v_section ON ord.section_id = v_section.id
     LEFT JOIN ( SELECT e.master_id,
            string_agg(e.all_rev::text, ' '::text) AS all_rev
           FROM ( SELECT pb2_main_workflow_reviewer.master_id,
                    pb2_main_workflow_reviewer.reviewer_user AS all_rev
                   FROM pb2_main_workflow_reviewer
                  WHERE pb2_main_workflow_reviewer.master_id::text ~~ 'PD%'::text
                UNION
                 SELECT pb2_main_workflow_next_actor.master_id,
                    pb2_main_workflow_next_actor.actor_user AS all_rev
                   FROM pb2_main_workflow_next_actor
                  WHERE pb2_main_workflow_next_actor.master_id::text ~~ 'PD%'::text) e
          GROUP BY e.master_id) all_rev ON ord.id::text = all_rev.master_id::text
     LEFT JOIN pb2_ext_wkf_config_doctype d ON ord.doc_type::text = d.name::text;

ALTER TABLE public.pb2_pcm_ord_view
  OWNER TO alfresco;
  
CREATE OR REPLACE VIEW public.pb2_pcm_req_view AS 
 SELECT req.id,
    req.total,
    req.total_cnv,
    req.objective_type,
    mm.objective_type_name,
    mm.objective_type_name_th,
    req.objective,
    req.budget_cc,
    req.budget_cc_type,
    req.reason,
    req.currency,
    req.cost_control_id,
    req.cost_control_type_id,
    req.location,
    req.prweb_method_id,
    req.method_cond2,
    req.method_cond2_rule,
    req.method_cond2_dtl,
    req.workflow_ins_id,
    req.folder_ref,
    req.doc_ref,
    req.contract_date,
    b.name AS budget_cc_name,
    b.name_th AS budget_cc_name_th,
    req.wf_by,
    req.wf_by_th,
    req.wf_by_time,
    req.wf_status,
    req.wf_status_th,
    req.task_id,
    req.status,
    req.updated_time,
    req.created_time,
    req.updated_by,
    COALESCE(v_created_by.first_name, req.created_by) AS created_by,
    COALESCE(v_created_by.first_name_th, req.created_by::text) AS created_by_th,
    COALESCE(v_req_by.first_name, req.req_by) AS req_by,
    COALESCE(v_req_by.first_name_th, req.req_by::text) AS req_by_th,
        CASE
            WHEN req.status::text = 'D'::text THEN '1'::character varying
            WHEN req.status::text = 'W2'::text THEN '2'::character varying
            WHEN req.status::text = 'W1'::text THEN '3'::character varying
            WHEN req.status::text = 'S'::text THEN '4'::character varying
            WHEN req.status::text = 'C1'::text THEN '5'::character varying
            WHEN req.status::text = 'C2'::text THEN '6'::character varying
            WHEN req.status::text = 'X1'::text THEN '7'::character varying
            WHEN req.status::text = 'X2'::text THEN '8'::character varying
            ELSE req.status
        END AS order_field,
    all_rev.all_rev,
    req.created_by AS created_by_code,
    req.req_by AS req_by_code,
    req.requested_time
   FROM ( SELECT pb2_pcm_req.id,
            pb2_pcm_req.status,
            pb2_pcm_req.total,
            pb2_pcm_req.total_cnv,
            pb2_pcm_req.objective_type,
            pb2_pcm_req.objective,
            pb2_pcm_req.budget_cc,
            pb2_pcm_req.budget_cc_type,
            pb2_pcm_req.reason,
            pb2_pcm_req.currency,
            pb2_pcm_req.cost_control_id,
            pb2_pcm_req.cost_control_type_id,
            pb2_pcm_req.location,
            pb2_pcm_req.prweb_method_id,
            pb2_pcm_req.method_cond2,
            pb2_pcm_req.method_cond2_rule,
            pb2_pcm_req.method_cond2_dtl,
            pb2_pcm_req.folder_ref,
            pb2_pcm_req.doc_ref,
            pb2_pcm_req.contract_date,
            NULL::character varying AS wf_by,
            NULL::text AS wf_by_th,
            NULL::timestamp with time zone AS wf_by_time,
            NULL::character varying AS wf_status,
            NULL::character varying AS wf_status_th,
            NULL::character varying AS task_id,
            pb2_pcm_req.workflow_ins_id,
            pb2_pcm_req.created_by,
            pb2_pcm_req.req_by,
            pb2_pcm_req.updated_time,
            pb2_pcm_req.created_time,
            pb2_pcm_req.updated_by,
            pb2_pcm_req.requested_time
           FROM pb2_pcm_req
          WHERE pb2_pcm_req.status::text = 'D'::text
        UNION
         SELECT pr.id,
            pr.status,
            pr.total,
            pr.total_cnv,
            pr.objective_type,
            pr.objective,
            pr.budget_cc,
            pr.budget_cc_type,
            pr.reason,
            pr.currency,
            pr.cost_control_id,
            pr.cost_control_type_id,
            pr.location,
            pr.prweb_method_id,
            pr.method_cond2,
            pr.method_cond2_rule,
            pr.method_cond2_dtl,
            pr.folder_ref,
            pr.doc_ref,
            pr.contract_date,
            w.by,
            w.by_th,
            w.by_time,
            w.status AS wf_status,
            w.status_th AS wf_status_th,
            w.task_id,
            pr.workflow_ins_id,
            pr.created_by,
            pr.req_by,
            pr.updated_time,
            pr.created_time,
            pr.updated_by,
            pr.requested_time
           FROM pb2_pcm_req pr
             LEFT JOIN ( SELECT w_1.master_id,
                    w_1.workflow_ins_id,
                    w_1.status,
                    w_1.status_th,
                    COALESCE(e.first_name, w_1.by) AS by,
                    COALESCE(e.first_name_th, w_1.by::text) AS by_th,
                    w_1.by_time,
                    w_1.task_id
                   FROM pb2_main_workflow w_1
                     LEFT JOIN pb2_hr_employee_view e ON w_1.by::text = e.employee_code::text
                  WHERE w_1.type::text = 'PCM_REQ'::text) w ON pr.id::text = w.master_id::text
          WHERE pr.status::text <> 'D'::text) req
     LEFT JOIN ( SELECT pb2_hr_employee_view.employee_code,
            pb2_hr_employee_view.first_name,
            pb2_hr_employee_view.first_name_th
           FROM pb2_hr_employee_view) v_created_by ON req.created_by::text = v_created_by.employee_code::text
     LEFT JOIN ( SELECT pb2_hr_employee_view.employee_code,
            pb2_hr_employee_view.first_name,
            pb2_hr_employee_view.first_name_th
           FROM pb2_hr_employee_view) v_req_by ON req.req_by::text = v_req_by.employee_code::text
     LEFT JOIN ( SELECT pb2_main_master.code,
            pb2_main_master.name AS objective_type_name_th,
            pb2_main_master.flag2 AS objective_type_name
           FROM pb2_main_master
          WHERE pb2_main_master.type::text = 'PC'::text) mm ON req.objective_type::text = mm.code::text
     LEFT JOIN ( SELECT 'U'::text AS _type,
            pb2_section_view.id,
            pb2_section_view.description AS name,
            pb2_section_view.description_th AS name_th
           FROM pb2_section_view
        UNION
         SELECT 'P'::text AS _type,
            pb2_project_view.id,
            pb2_project_view.description AS name,
            pb2_project_view.description_th AS name_th
           FROM pb2_project_view) b ON req.budget_cc_type::text = b._type AND req.budget_cc = b.id
     LEFT JOIN ( SELECT e.master_id,
            string_agg(e.all_rev::text, ' '::text) AS all_rev
           FROM ( SELECT pb2_main_workflow_reviewer.master_id,
                    pb2_main_workflow_reviewer.reviewer_user AS all_rev
                   FROM pb2_main_workflow_reviewer
                  WHERE pb2_main_workflow_reviewer.master_id::text ~~ 'PR%'::text
                UNION
                 SELECT pb2_main_workflow_next_actor.master_id,
                    pb2_main_workflow_next_actor.actor_user AS all_rev
                   FROM pb2_main_workflow_next_actor
                  WHERE pb2_main_workflow_next_actor.master_id::text ~~ 'PR%'::text) e
          GROUP BY e.master_id) all_rev ON req.id::text = all_rev.master_id::text;

ALTER TABLE public.pb2_pcm_req_view
  OWNER TO alfresco;
  
CREATE OR REPLACE VIEW public.pb2_pr_method_committee_view AS 
 SELECT c.id,
    1 AS seq,
    c.name AS title,
    1 AS amount_min,
    r.method_id
   FROM pb2_ext_prweb_purchase_method_rel r,
    pb2_ext_purchase_committee_type c
  WHERE r.committee_type_id = c.id;

ALTER TABLE public.pb2_pr_method_committee_view
  OWNER TO alfresco;
  
CREATE OR REPLACE VIEW public.pb2_pr_method_view AS 
 SELECT p.id,
    t.name AS obj,
    m.name AS method,
    pr.name AS cond1,
    c.name AS cond2,
    d.name AS doc_type,
    p.price_range_id,
    p.condition_id,
    p.doctype_id,
    p.method_id,
    pr.price_from,
    pr.price_to
   FROM pb2_ext_prweb_purchase_method p
     LEFT JOIN pb2_ext_purchase_type t ON p.type_id = t.id
     LEFT JOIN pb2_ext_purchase_method m ON p.method_id = m.id
     LEFT JOIN pb2_ext_purchase_price_range pr ON p.price_range_id = pr.id
     LEFT JOIN pb2_ext_purchase_condition c ON p.condition_id = c.id
     LEFT JOIN pb2_ext_wkf_config_doctype d ON p.doctype_id = d.id
  ORDER BY t.name, m.name;

ALTER TABLE public.pb2_pr_method_view
  OWNER TO alfresco;
  
CREATE OR REPLACE VIEW public.pb2_project_view AS 
 SELECT p.id,
    p.name,
    p.code,
    COALESCE(irp.value, p.name::text) AS name_th,
    o.name AS org_name,
    COALESCE(iro.value, o.name::text) AS org_name_th,
    o.name_short AS org_name_short,
    COALESCE(iro_short.value, o.name_short::text) AS org_name_short_th,
    e.employee_code AS pm_code,
    (((t.name::text || ' '::text) || e.first_name::text) || ' '::text) || e.last_name::text AS pm_name,
    (((COALESCE(irt.value, t.name::text) || ' '::text) || COALESCE(irf.value, e.first_name::text)) || ' '::text) || COALESCE(irl.value, e.last_name::text) AS pm_name_th,
    concat('[', btrim(p.code::text), '] ', p.name) AS description,
    concat('[', btrim(p.code::text), '] ', COALESCE(irp.value, p.name::text)) AS description_th
   FROM pb2_ext_res_project p
     LEFT JOIN pb2_ext_res_org o ON p.org_id = o.id
     LEFT JOIN pb2_ext_res_project_member m ON m.project_id = p.id AND m.project_position::text = 'manager'::text
     LEFT JOIN pb2_ext_hr_employee e ON m.employee_id = e.id
     LEFT JOIN pb2_ext_res_partner_title t ON e.title_id = t.id
     LEFT JOIN ( SELECT pb2_ext_ir_translation.res_id,
            pb2_ext_ir_translation.value
           FROM pb2_ext_ir_translation
          WHERE pb2_ext_ir_translation.name::text = 'res.project,name'::text AND pb2_ext_ir_translation.type::text = 'model'::text AND pb2_ext_ir_translation.lang::text = 'th_TH'::text) irp ON p.id = irp.res_id
     LEFT JOIN ( SELECT pb2_ext_ir_translation.res_id,
            pb2_ext_ir_translation.value
           FROM pb2_ext_ir_translation
          WHERE pb2_ext_ir_translation.name::text = 'res.org,name'::text AND pb2_ext_ir_translation.type::text = 'model'::text AND pb2_ext_ir_translation.lang::text = 'th_TH'::text) iro ON p.org_id = iro.res_id
     LEFT JOIN ( SELECT pb2_ext_ir_translation.res_id,
            pb2_ext_ir_translation.value
           FROM pb2_ext_ir_translation
          WHERE pb2_ext_ir_translation.name::text = 'res.org,name_short'::text AND pb2_ext_ir_translation.type::text = 'model'::text AND pb2_ext_ir_translation.lang::text = 'th_TH'::text) iro_short ON p.org_id = iro_short.res_id
     LEFT JOIN ( SELECT pb2_ext_ir_translation.res_id,
            pb2_ext_ir_translation.value
           FROM pb2_ext_ir_translation
          WHERE pb2_ext_ir_translation.name::text = 'res.partner.title,name'::text AND pb2_ext_ir_translation.type::text = 'model'::text AND pb2_ext_ir_translation.lang::text = 'th_TH'::text) irt ON e.title_id = irt.res_id
     LEFT JOIN ( SELECT pb2_ext_ir_translation.res_id,
            pb2_ext_ir_translation.value
           FROM pb2_ext_ir_translation
          WHERE pb2_ext_ir_translation.name::text = 'hr.employee,first_name'::text AND pb2_ext_ir_translation.type::text = 'model'::text AND pb2_ext_ir_translation.lang::text = 'th_TH'::text) irf ON e.id = irf.res_id
     LEFT JOIN ( SELECT pb2_ext_ir_translation.res_id,
            pb2_ext_ir_translation.value
           FROM pb2_ext_ir_translation
          WHERE pb2_ext_ir_translation.name::text = 'hr.employee,last_name'::text AND pb2_ext_ir_translation.type::text = 'model'::text AND pb2_ext_ir_translation.lang::text = 'th_TH'::text) irl ON e.id = irl.res_id
  WHERE p.active = true AND e.employee_code::text <> ''::text;

ALTER TABLE public.pb2_project_view
  OWNER TO alfresco;
  
CREATE OR REPLACE VIEW public.pb2_section_view AS 
 SELECT s.id,
    concat('[', btrim(s.code::text), '] ', s.name) AS description,
    concat('[', btrim(s.code::text), '] ', COALESCE(irs.value, s.name::text)) AS description_th,
    o.name,
    COALESCE(iro.value, o.name::text) AS name_th,
    o.name_short,
    COALESCE(iro_short.value, o.name_short::text) AS name_short_th,
    concat('[', btrim(c.code::text), '] ', c.name) AS costcenter,
    concat('[', btrim(c.code::text), '] ', COALESCE(irc.value, c.name::text)) AS costcenter_th,
    s.name_short AS section_name_short
   FROM pb2_ext_res_section s
     LEFT JOIN pb2_ext_res_org o ON s.org_id = o.id
     LEFT JOIN pb2_ext_res_costcenter c ON s.costcenter_id = c.id
     LEFT JOIN ( SELECT pb2_ext_ir_translation.res_id,
            pb2_ext_ir_translation.value
           FROM pb2_ext_ir_translation
          WHERE pb2_ext_ir_translation.name::text = 'res.section,name'::text AND pb2_ext_ir_translation.type::text = 'model'::text AND pb2_ext_ir_translation.lang::text = 'th_TH'::text) irs ON s.id = irs.res_id
     LEFT JOIN ( SELECT pb2_ext_ir_translation.res_id,
            pb2_ext_ir_translation.value
           FROM pb2_ext_ir_translation
          WHERE pb2_ext_ir_translation.name::text = 'res.org,name'::text AND pb2_ext_ir_translation.type::text = 'model'::text AND pb2_ext_ir_translation.lang::text = 'th_TH'::text) iro ON s.org_id = iro.res_id
     LEFT JOIN ( SELECT pb2_ext_ir_translation.res_id,
            pb2_ext_ir_translation.value
           FROM pb2_ext_ir_translation
          WHERE pb2_ext_ir_translation.name::text = 'res.org,name_short'::text AND pb2_ext_ir_translation.type::text = 'model'::text AND pb2_ext_ir_translation.lang::text = 'th_TH'::text) iro_short ON s.org_id = iro_short.res_id
     LEFT JOIN ( SELECT pb2_ext_ir_translation.res_id,
            pb2_ext_ir_translation.value
           FROM pb2_ext_ir_translation
          WHERE pb2_ext_ir_translation.name::text = 'res.costcenter,name'::text AND pb2_ext_ir_translation.type::text = 'model'::text AND pb2_ext_ir_translation.lang::text = 'th_TH'::text) irc ON s.costcenter_id = irc.res_id
  WHERE s.active = true;

ALTER TABLE public.pb2_section_view
  OWNER TO alfresco;