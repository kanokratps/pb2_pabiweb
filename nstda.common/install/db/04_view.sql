CREATE OR REPLACE VIEW public.pb2_activity_group_view AS 
 SELECT a.id,
    a.name,
    COALESCE(ir.value, a.name::text) AS name_th,
    a.budget_method,
    a.special_workflow_emotion,
    ic.internal_charge,
    a.description
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
  WHERE a.budget_method::text = 'expense'::text AND a.active = true AND (a.no_display IS FALSE OR a.no_display IS NULL);

ALTER TABLE public.pb2_activity_group_view
  OWNER TO alfresco;
GRANT ALL ON TABLE public.pb2_activity_group_view TO alfresco;
GRANT SELECT ON TABLE public.pb2_activity_group_view TO etl_pabi2;

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
GRANT ALL ON TABLE public.pb2_activity_view TO alfresco;
GRANT SELECT ON TABLE public.pb2_activity_view TO etl_pabi2;

CREATE OR REPLACE VIEW public.pb2_av_outstanding_view WITH (security_barrier=false) AS 
 SELECT a.id,
    a.number,
    a.amount_advanced AS waitamt,
    a.cleared_amount,
    a.amount_advanced - a.cleared_amount::double precision AS balance,
    a.employee_code,
    a.name,
    brw.id AS af_number,
    brw.is_small_amount
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
          WHERE exp.is_employee_advance = true AND exp.state::text <> 'cancelled'::text) a
     LEFT JOIN pb2_exp_brw brw ON a.number::text = brw.id::text
  WHERE (a.amount_advanced - a.cleared_amount::double precision) > 0::double precision
  ORDER BY a.number;

ALTER TABLE public.pb2_av_outstanding_view
  OWNER TO alfresco;
GRANT ALL ON TABLE public.pb2_av_outstanding_view TO alfresco;
GRANT SELECT ON TABLE public.pb2_av_outstanding_view TO etl_pabi2;

CREATE OR REPLACE VIEW public.pb2_employee_info_view WITH (security_barrier=false) AS 
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
    e.org_id,
    u.active
   FROM pb2_ext_hr_employee e
     JOIN pb2_ext_res_users u ON e.employee_code::text = u.login::text
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
GRANT ALL ON TABLE public.pb2_employee_info_view TO alfresco;
GRANT SELECT ON TABLE public.pb2_employee_info_view TO etl_pabi2;

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
    req.requested_time,
    req.is_small_amount,
    req.status AS status_code
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
            pb2_exp_brw.requested_time,
            pb2_exp_brw.is_small_amount
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
            brw.requested_time,
            brw.is_small_amount
           FROM pb2_exp_brw brw
             LEFT JOIN ( SELECT w_1.master_id,
                    w_1.workflow_ins_id,
                    w_1.status,
                    w_1.status_th,
                    COALESCE(e.first_name, w_1.by) AS by,
                    COALESCE(ir_fname.value, w_1.by::text) AS by_th,
                    w_1.by_time,
                    w_1.task_id
                   FROM pb2_main_workflow w_1
                     LEFT JOIN pb2_ext_hr_employee e ON w_1.by::text = e.employee_code::text
                     LEFT JOIN ( SELECT pb2_ext_ir_translation.res_id,
                            pb2_ext_ir_translation.value
                           FROM pb2_ext_ir_translation
                          WHERE pb2_ext_ir_translation.name::text = 'hr.employee,first_name'::text AND pb2_ext_ir_translation.type::text = 'model'::text AND pb2_ext_ir_translation.lang::text = 'th_TH'::text) ir_fname ON e.id = ir_fname.res_id
                  WHERE w_1.type::text = 'EXP_BRW'::text) w ON brw.id::text = w.master_id::text
          WHERE brw.status::text <> 'D'::text) req
     LEFT JOIN ( SELECT e.employee_code,
            e.first_name,
            ir_fname.value AS first_name_th
           FROM pb2_ext_hr_employee e
             LEFT JOIN ( SELECT pb2_ext_ir_translation.res_id,
                    pb2_ext_ir_translation.value
                   FROM pb2_ext_ir_translation
                  WHERE pb2_ext_ir_translation.name::text = 'hr.employee,first_name'::text AND pb2_ext_ir_translation.type::text = 'model'::text AND pb2_ext_ir_translation.lang::text = 'th_TH'::text) ir_fname ON e.id = ir_fname.res_id) v_created_by ON req.created_by::text = v_created_by.employee_code::text
     LEFT JOIN ( SELECT e.employee_code,
            e.first_name,
            ir_fname.value AS first_name_th
           FROM pb2_ext_hr_employee e
             LEFT JOIN ( SELECT pb2_ext_ir_translation.res_id,
                    pb2_ext_ir_translation.value
                   FROM pb2_ext_ir_translation
                  WHERE pb2_ext_ir_translation.name::text = 'hr.employee,first_name'::text AND pb2_ext_ir_translation.type::text = 'model'::text AND pb2_ext_ir_translation.lang::text = 'th_TH'::text) ir_fname ON e.id = ir_fname.res_id) v_req_by ON req.req_by::text = v_req_by.employee_code::text
     LEFT JOIN ( SELECT pb2_main_master.code,
            pb2_main_master.name AS objective_type_name_th,
            pb2_main_master.flag2 AS objective_type_name
           FROM pb2_main_master
          WHERE pb2_main_master.type::text = 'BRW_TYPE'::text) mm ON req.objective_type::text = mm.code::text
     LEFT JOIN ( SELECT 'U'::text AS _type,
            s.id,
            concat('[', btrim(s.code::text), '] ', s.name) AS name,
            concat('[', btrim(s.code::text), '] ', COALESCE(irs.value, s.name::text)) AS name_th
           FROM pb2_ext_res_section s
             LEFT JOIN ( SELECT pb2_ext_ir_translation.res_id,
                    pb2_ext_ir_translation.value
                   FROM pb2_ext_ir_translation
                  WHERE pb2_ext_ir_translation.name::text = 'res.section,name'::text AND pb2_ext_ir_translation.type::text = 'model'::text AND pb2_ext_ir_translation.lang::text = 'th_TH'::text) irs ON s.id = irs.res_id
          WHERE s.active = true
        UNION
         SELECT 'P'::text AS _type,
            p.id,
            concat('[', btrim(p.code::text), '] ', p.name) AS name,
            concat('[', btrim(p.code::text), '] ', COALESCE(irp.value, p.name::text)) AS name_th
           FROM pb2_ext_res_project p
             LEFT JOIN ( SELECT pb2_ext_ir_translation.res_id,
                    pb2_ext_ir_translation.value
                   FROM pb2_ext_ir_translation
                  WHERE pb2_ext_ir_translation.name::text = 'res.project,name'::text AND pb2_ext_ir_translation.type::text = 'model'::text AND pb2_ext_ir_translation.lang::text = 'th_TH'::text) irp ON p.id = irp.res_id
          WHERE p.active = true
        UNION
         SELECT 'A'::text AS _type,
            i.id,
            concat('[', btrim(i.code::text), '] ', i.name) AS asset,
            concat('[', btrim(i.code::text), '] ', COALESCE(ira.value, i.name::text)) AS asset_th
           FROM pb2_ext_res_invest_asset i
             LEFT JOIN ( SELECT pb2_ext_ir_translation.res_id,
                    pb2_ext_ir_translation.value
                   FROM pb2_ext_ir_translation
                  WHERE pb2_ext_ir_translation.name::text = 'res.invest.asset,name'::text AND pb2_ext_ir_translation.type::text = 'model'::text AND pb2_ext_ir_translation.lang::text = 'th_TH'::text) ira ON i.id = ira.res_id
          WHERE i.active IS TRUE
        UNION
         SELECT 'C'::text AS _type,
            cp.id,
            concat('[', btrim(cp.code::text), '] ', cp.name) AS name,
            concat('[', btrim(cp.code::text), '] ', COALESCE(ira.value, cp.name::text)) AS name_th
           FROM pb2_ext_res_invest_construction_phase cp
             LEFT JOIN ( SELECT pb2_ext_ir_translation.res_id,
                    pb2_ext_ir_translation.value
                   FROM pb2_ext_ir_translation
                  WHERE pb2_ext_ir_translation.name::text = 'res.invest.construction.phase,name'::text AND pb2_ext_ir_translation.type::text = 'model'::text AND pb2_ext_ir_translation.lang::text = 'th_TH'::text) ira ON cp.id = ira.res_id
             LEFT JOIN pb2_ext_res_invest_construction ct ON ct.id = cp.invest_construction_id
          WHERE cp.active IS TRUE) b ON req.budget_cc_type::text = b._type AND req.budget_cc = b.id
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

GRANT ALL ON TABLE public.pb2_exp_brw_view TO alfresco;
GRANT SELECT ON TABLE public.pb2_exp_brw_view TO etl_pabi2;
  
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
    req.emotion,
    req.is_small_amount,
    req.pay_type
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
                 LIMIT 1) AS emotion,
            pb2_exp_use.is_small_amount
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
                 LIMIT 1) AS emotion,
            exp.is_small_amount
           FROM pb2_exp_use exp
             LEFT JOIN ( SELECT w_1.master_id,
                    w_1.workflow_ins_id,
                    w_1.status,
                    w_1.status_th,
                    COALESCE(e.first_name, w_1.by) AS by,
                    COALESCE(ir_fname.value, w_1.by::text) AS by_th,
                    w_1.by_time,
                    w_1.task_id
                   FROM pb2_main_workflow w_1
                     LEFT JOIN pb2_ext_hr_employee e ON w_1.by::text = e.employee_code::text
                     LEFT JOIN ( SELECT pb2_ext_ir_translation.res_id,
                            pb2_ext_ir_translation.value
                           FROM pb2_ext_ir_translation
                          WHERE pb2_ext_ir_translation.name::text = 'hr.employee,first_name'::text AND pb2_ext_ir_translation.type::text = 'model'::text AND pb2_ext_ir_translation.lang::text = 'th_TH'::text) ir_fname ON e.id = ir_fname.res_id
                  WHERE w_1.type::text = 'EXP_USE'::text) w ON exp.id::text = w.master_id::text
          WHERE exp.status::text <> 'D'::text) req
     LEFT JOIN ( SELECT e.employee_code,
            e.first_name,
            ir_fname.value AS first_name_th
           FROM pb2_ext_hr_employee e
             LEFT JOIN ( SELECT pb2_ext_ir_translation.res_id,
                    pb2_ext_ir_translation.value
                   FROM pb2_ext_ir_translation
                  WHERE pb2_ext_ir_translation.name::text = 'hr.employee,first_name'::text AND pb2_ext_ir_translation.type::text = 'model'::text AND pb2_ext_ir_translation.lang::text = 'th_TH'::text) ir_fname ON e.id = ir_fname.res_id) v_created_by ON req.created_by::text = v_created_by.employee_code::text
     LEFT JOIN ( SELECT e.employee_code,
            e.first_name,
            ir_fname.value AS first_name_th
           FROM pb2_ext_hr_employee e
             LEFT JOIN ( SELECT pb2_ext_ir_translation.res_id,
                    pb2_ext_ir_translation.value
                   FROM pb2_ext_ir_translation
                  WHERE pb2_ext_ir_translation.name::text = 'hr.employee,first_name'::text AND pb2_ext_ir_translation.type::text = 'model'::text AND pb2_ext_ir_translation.lang::text = 'th_TH'::text) ir_fname ON e.id = ir_fname.res_id) v_req_by ON req.req_by::text = v_req_by.employee_code::text
     LEFT JOIN ( SELECT pb2_main_master.code,
            pb2_main_master.name AS pay_type_name_th,
            pb2_main_master.flag2 AS pay_type_name
           FROM pb2_main_master
          WHERE pb2_main_master.type::text = 'EXP_TYPE'::text) mm ON req.pay_type::text = mm.code::text
     LEFT JOIN ( SELECT 'U'::text AS _type,
            s.id,
            concat('[', btrim(s.code::text), '] ', s.name) AS name,
            concat('[', btrim(s.code::text), '] ', COALESCE(irs.value, s.name::text)) AS name_th
           FROM pb2_ext_res_section s
             LEFT JOIN ( SELECT pb2_ext_ir_translation.res_id,
                    pb2_ext_ir_translation.value
                   FROM pb2_ext_ir_translation
                  WHERE pb2_ext_ir_translation.name::text = 'res.section,name'::text AND pb2_ext_ir_translation.type::text = 'model'::text AND pb2_ext_ir_translation.lang::text = 'th_TH'::text) irs ON s.id = irs.res_id
          WHERE s.active = true
        UNION
         SELECT 'P'::text AS _type,
            p.id,
            concat('[', btrim(p.code::text), '] ', p.name) AS name,
            concat('[', btrim(p.code::text), '] ', COALESCE(irp.value, p.name::text)) AS name_th
           FROM pb2_ext_res_project p
             LEFT JOIN ( SELECT pb2_ext_ir_translation.res_id,
                    pb2_ext_ir_translation.value
                   FROM pb2_ext_ir_translation
                  WHERE pb2_ext_ir_translation.name::text = 'res.project,name'::text AND pb2_ext_ir_translation.type::text = 'model'::text AND pb2_ext_ir_translation.lang::text = 'th_TH'::text) irp ON p.id = irp.res_id
          WHERE p.active = true
        UNION
         SELECT 'A'::text AS _type,
            i.id,
            concat('[', btrim(i.code::text), '] ', i.name) AS asset,
            concat('[', btrim(i.code::text), '] ', COALESCE(ira.value, i.name::text)) AS asset_th
           FROM pb2_ext_res_invest_asset i
             LEFT JOIN ( SELECT pb2_ext_ir_translation.res_id,
                    pb2_ext_ir_translation.value
                   FROM pb2_ext_ir_translation
                  WHERE pb2_ext_ir_translation.name::text = 'res.invest.asset,name'::text AND pb2_ext_ir_translation.type::text = 'model'::text AND pb2_ext_ir_translation.lang::text = 'th_TH'::text) ira ON i.id = ira.res_id
          WHERE i.active IS TRUE
        UNION
         SELECT 'C'::text AS _type,
            cp.id,
            concat('[', btrim(cp.code::text), '] ', cp.name) AS name,
            concat('[', btrim(cp.code::text), '] ', COALESCE(ira.value, cp.name::text)) AS name_th
           FROM pb2_ext_res_invest_construction_phase cp
             LEFT JOIN ( SELECT pb2_ext_ir_translation.res_id,
                    pb2_ext_ir_translation.value
                   FROM pb2_ext_ir_translation
                  WHERE pb2_ext_ir_translation.name::text = 'res.invest.construction.phase,name'::text AND pb2_ext_ir_translation.type::text = 'model'::text AND pb2_ext_ir_translation.lang::text = 'th_TH'::text) ira ON cp.id = ira.res_id
             LEFT JOIN pb2_ext_res_invest_construction ct ON ct.id = cp.invest_construction_id
          WHERE cp.active IS TRUE) b ON req.budget_cc_type::text = b._type AND req.budget_cc = b.id
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
GRANT ALL ON TABLE public.pb2_exp_use_view TO alfresco;
GRANT SELECT ON TABLE public.pb2_exp_use_view TO etl_pabi2;

CREATE OR REPLACE VIEW public.pb2_hr_employee_view WITH (security_barrier=false) AS 
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
    COALESCE(ir_org.org_name_th, ir_org.org_name::text) AS org_name_th,
    ir_division.division,
    ir_division.division_th,
    u.active
   FROM pb2_ext_res_users u
     JOIN pb2_ext_hr_employee e ON u.login::text = e.employee_code::text
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
            ir.value AS section_th,
            s.division_id
           FROM pb2_ext_res_section s
             LEFT JOIN ( SELECT pb2_ext_ir_translation.res_id,
                    pb2_ext_ir_translation.value
                   FROM pb2_ext_ir_translation
                  WHERE pb2_ext_ir_translation.name::text = 'res.section,name'::text AND pb2_ext_ir_translation.type::text = 'model'::text AND pb2_ext_ir_translation.lang::text = 'th_TH'::text) ir ON s.id = ir.res_id) ir_section ON e.section_id = ir_section.id
     LEFT JOIN ( SELECT d.id,
            concat('[', btrim(d.code::text), '] ', COALESCE(d.name_short, d.name)) AS division,
            concat('[', btrim(d.code::text), '] ', COALESCE(d.name_short, COALESCE(ir.value, d.name::text)::character varying)) AS division_th
           FROM pb2_ext_res_division d
             LEFT JOIN ( SELECT pb2_ext_ir_translation.res_id,
                    pb2_ext_ir_translation.value
                   FROM pb2_ext_ir_translation
                  WHERE pb2_ext_ir_translation.name::text = 'res.division,name'::text AND pb2_ext_ir_translation.type::text = 'model'::text AND pb2_ext_ir_translation.lang::text = 'th_TH'::text) ir ON d.id = ir.res_id) ir_division ON ir_section.division_id = ir_division.id
  WHERE e.id <> 1;

ALTER TABLE public.pb2_hr_employee_view
  OWNER TO alfresco;
GRANT ALL ON TABLE public.pb2_hr_employee_view TO alfresco;
GRANT SELECT ON TABLE public.pb2_hr_employee_view TO etl_pabi2;

CREATE OR REPLACE VIEW public.pb2_hr_salary_view AS 
 SELECT sal.id,
    sal.total,
    sal.objective,
    sal.section_id,
    sal.doc_type,
    sal.status,
    sal.workflow_ins_id,
    w.task_id,
    sal.updated_time,
    sal.created_time,
    sal.updated_by,
    sal.folder_ref,
    sal.doc_ref,
    w.by AS wf_by,
    w.by_th AS wf_by_th,
    w.by_time AS wf_by_time,
    w.status AS wf_status,
    w.status_th AS wf_status_th,
        CASE
            WHEN sal.status::text = 'D'::text THEN '1'::character varying
            WHEN sal.status::text = 'W2'::text THEN '5'::character varying
            WHEN sal.status::text = 'W1'::text THEN '2'::character varying
            WHEN sal.status::text = 'S'::text THEN '3'::character varying
            WHEN sal.status::text = 'C1'::text THEN '4'::character varying
            WHEN sal.status::text = 'X1'::text THEN '6'::character varying
            ELSE sal.status
        END AS order_field,
    COALESCE(v_created_by.first_name, sal.created_by) AS created_by,
    COALESCE(v_created_by.first_name_th, sal.created_by::text) AS created_by_th,
    v_section.org_name,
    v_section.org_name_th,
    all_rev.all_rev,
    sal.created_by AS created_by_code,
    d.description AS method_name
   FROM pb2_hr_salary sal
     LEFT JOIN ( SELECT w_1.master_id,
            w_1.workflow_ins_id,
            w_1.status,
            w_1.status_th,
            COALESCE(e.first_name, w_1.by) AS by,
            COALESCE(ir_fname.value, w_1.by::text) AS by_th,
            w_1.by_time,
            w_1.task_id
           FROM pb2_main_workflow w_1
             LEFT JOIN pb2_ext_hr_employee e ON w_1.by::text = e.employee_code::text
             LEFT JOIN ( SELECT pb2_ext_ir_translation.res_id,
                    pb2_ext_ir_translation.value
                   FROM pb2_ext_ir_translation
                  WHERE pb2_ext_ir_translation.name::text = 'hr.employee,first_name'::text AND pb2_ext_ir_translation.type::text = 'model'::text AND pb2_ext_ir_translation.lang::text = 'th_TH'::text) ir_fname ON e.id = ir_fname.res_id
          WHERE w_1.type::text = 'HR_SAL'::text) w ON sal.id::text = w.master_id::text
     LEFT JOIN ( SELECT e.employee_code,
            e.first_name,
            ir_fname.value AS first_name_th
           FROM pb2_ext_hr_employee e
             LEFT JOIN ( SELECT pb2_ext_ir_translation.res_id,
                    pb2_ext_ir_translation.value
                   FROM pb2_ext_ir_translation
                  WHERE pb2_ext_ir_translation.name::text = 'hr.employee,first_name'::text AND pb2_ext_ir_translation.type::text = 'model'::text AND pb2_ext_ir_translation.lang::text = 'th_TH'::text) ir_fname ON e.id = ir_fname.res_id) v_created_by ON sal.created_by::text = v_created_by.employee_code::text
     LEFT JOIN ( SELECT pb2_section_view.id,
            pb2_section_view.description AS org_name,
            pb2_section_view.description_th AS org_name_th
           FROM pb2_section_view) v_section ON sal.section_id = v_section.id
     LEFT JOIN ( SELECT e.master_id,
            string_agg(e.all_rev::text, ' '::text) AS all_rev
           FROM ( SELECT pb2_main_workflow_reviewer.master_id,
                    pb2_main_workflow_reviewer.reviewer_user AS all_rev
                   FROM pb2_main_workflow_reviewer
                  WHERE pb2_main_workflow_reviewer.master_id::text ~~ 'SL%'::text
                UNION
                 SELECT pb2_main_workflow_next_actor.master_id,
                    pb2_main_workflow_next_actor.actor_user AS all_rev
                   FROM pb2_main_workflow_next_actor
                  WHERE pb2_main_workflow_next_actor.master_id::text ~~ 'SL%'::text) e
          GROUP BY e.master_id) all_rev ON sal.id::text = all_rev.master_id::text
     LEFT JOIN pb2_ext_wkf_config_doctype d ON sal.doc_type::text = d.name::text;

ALTER TABLE public.pb2_hr_salary_view
  OWNER TO alfresco;
GRANT ALL ON TABLE public.pb2_hr_salary_view TO alfresco;
GRANT SELECT ON TABLE public.pb2_hr_salary_view TO etl_pabi2;

CREATE OR REPLACE VIEW public.pb2_invest_asset_view AS 
 SELECT o.name_short AS org,
    COALESCE(iro.value, o.name_short::text) AS org_th,
    concat('[', btrim(i.code::text), '] ', i.name) AS asset,
    concat('[', btrim(i.code::text), '] ', COALESCE(ira.value, i.name::text)) AS asset_th,
    c.code AS costcenter,
    i.reason_purchase AS objective,
    i.owner_section_id AS section_id,
    i.id
   FROM pb2_ext_res_invest_asset i
     LEFT JOIN pb2_ext_res_org o ON i.org_id = o.id
     LEFT JOIN pb2_ext_res_costcenter c ON i.costcenter_id = c.id
     LEFT JOIN ( SELECT pb2_ext_ir_translation.res_id,
            pb2_ext_ir_translation.value
           FROM pb2_ext_ir_translation
          WHERE pb2_ext_ir_translation.name::text = 'res.org,name_short'::text AND pb2_ext_ir_translation.type::text = 'model'::text AND pb2_ext_ir_translation.lang::text = 'th_TH'::text) iro ON i.org_id = iro.res_id
     LEFT JOIN ( SELECT pb2_ext_ir_translation.res_id,
            pb2_ext_ir_translation.value
           FROM pb2_ext_ir_translation
          WHERE pb2_ext_ir_translation.name::text = 'res.invest.asset,name'::text AND pb2_ext_ir_translation.type::text = 'model'::text AND pb2_ext_ir_translation.lang::text = 'th_TH'::text) ira ON i.id = ira.res_id
     JOIN ( SELECT pb2_ext_account_fiscalyear.id
           FROM pb2_ext_account_fiscalyear
          WHERE now() >= pb2_ext_account_fiscalyear.date_start AND now() <= pb2_ext_account_fiscalyear.date_stop) f ON i.fiscalyear_id = f.id
  WHERE i.active IS TRUE;

ALTER TABLE public.pb2_invest_asset_view
  OWNER TO alfresco;
GRANT ALL ON TABLE public.pb2_invest_asset_view TO alfresco;
GRANT SELECT ON TABLE public.pb2_invest_asset_view TO etl_pabi2;

CREATE OR REPLACE VIEW public.pb2_invest_construction_phase_view AS 
 SELECT o.name_short AS org,
    COALESCE(iro.value, o.name_short::text) AS org_th,
    concat('[', btrim(cp.code::text), '] ', cp.name) AS construction,
    concat('[', btrim(cp.code::text), '] ', COALESCE(ira.value, cp.name::text)) AS construction_th,
    c.code AS costcenter,
    ct.pm_section_id AS section_id,
    cp.id
   FROM pb2_ext_res_invest_construction_phase cp
     LEFT JOIN pb2_ext_res_org o ON cp.org_id = o.id
     LEFT JOIN pb2_ext_res_costcenter c ON cp.costcenter_id = c.id
     LEFT JOIN ( SELECT pb2_ext_ir_translation.res_id,
            pb2_ext_ir_translation.value
           FROM pb2_ext_ir_translation
          WHERE pb2_ext_ir_translation.name::text = 'res.org,name_short'::text AND pb2_ext_ir_translation.type::text = 'model'::text AND pb2_ext_ir_translation.lang::text = 'th_TH'::text) iro ON cp.org_id = iro.res_id
     LEFT JOIN ( SELECT pb2_ext_ir_translation.res_id,
            pb2_ext_ir_translation.value
           FROM pb2_ext_ir_translation
          WHERE pb2_ext_ir_translation.name::text = 'res.invest.construction.phase,name'::text AND pb2_ext_ir_translation.type::text = 'model'::text AND pb2_ext_ir_translation.lang::text = 'th_TH'::text) ira ON cp.id = ira.res_id
     LEFT JOIN pb2_ext_res_invest_construction ct ON ct.id = cp.invest_construction_id
  WHERE cp.active IS TRUE;

ALTER TABLE public.pb2_invest_construction_phase_view
  OWNER TO alfresco;
GRANT ALL ON TABLE public.pb2_invest_construction_phase_view TO alfresco;
GRANT SELECT ON TABLE public.pb2_invest_construction_phase_view TO etl_pabi2;

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
    d.description AS method_name,
    ord.status AS status_code
   FROM pb2_pcm_ord ord
     LEFT JOIN ( SELECT w_1.master_id,
            w_1.workflow_ins_id,
            w_1.status,
            w_1.status_th,
            COALESCE(e.first_name, w_1.by) AS by,
            COALESCE(ir_fname.value, w_1.by::text) AS by_th,
            w_1.by_time,
            w_1.task_id
           FROM pb2_main_workflow w_1
             LEFT JOIN pb2_ext_hr_employee e ON w_1.by::text = e.employee_code::text
             LEFT JOIN ( SELECT pb2_ext_ir_translation.res_id,
                    pb2_ext_ir_translation.value
                   FROM pb2_ext_ir_translation
                  WHERE pb2_ext_ir_translation.name::text = 'hr.employee,first_name'::text AND pb2_ext_ir_translation.type::text = 'model'::text AND pb2_ext_ir_translation.lang::text = 'th_TH'::text) ir_fname ON e.id = ir_fname.res_id
          WHERE w_1.type::text = 'PCM_ORD'::text) w ON ord.id::text = w.master_id::text
     LEFT JOIN ( SELECT e.employee_code,
            e.first_name,
            ir_fname.value AS first_name_th
           FROM pb2_ext_hr_employee e
             LEFT JOIN ( SELECT pb2_ext_ir_translation.res_id,
                    pb2_ext_ir_translation.value
                   FROM pb2_ext_ir_translation
                  WHERE pb2_ext_ir_translation.name::text = 'hr.employee,first_name'::text AND pb2_ext_ir_translation.type::text = 'model'::text AND pb2_ext_ir_translation.lang::text = 'th_TH'::text) ir_fname ON e.id = ir_fname.res_id) v_created_by ON ord.created_by::text = v_created_by.employee_code::text
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
GRANT ALL ON TABLE public.pb2_pcm_ord_view TO alfresco;
GRANT SELECT ON TABLE public.pb2_pcm_ord_view TO etl_pabi2;

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
            WHEN req.status::text = 'C3'::text THEN '7'::character varying
            WHEN req.status::text = 'X1'::text THEN '8'::character varying
            WHEN req.status::text = 'X2'::text THEN '9'::character varying
            ELSE req.status
        END AS order_field,
    all_rev.all_rev,
    req.created_by AS created_by_code,
    req.req_by AS req_by_code,
    req.requested_time,
    req.is_small_amount,
    req.status AS status_code
   FROM ( SELECT pb2_pcm_req.id,
            pb2_pcm_req.is_small_amount,
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
            pr.is_small_amount,
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
                    COALESCE(ir_fname.value, w_1.by::text) AS by_th,
                    w_1.by_time,
                    w_1.task_id
                   FROM pb2_main_workflow w_1
                     LEFT JOIN pb2_ext_hr_employee e ON w_1.by::text = e.employee_code::text
                     LEFT JOIN ( SELECT pb2_ext_ir_translation.res_id,
                            pb2_ext_ir_translation.value
                           FROM pb2_ext_ir_translation
                          WHERE pb2_ext_ir_translation.name::text = 'hr.employee,first_name'::text AND pb2_ext_ir_translation.type::text = 'model'::text AND pb2_ext_ir_translation.lang::text = 'th_TH'::text) ir_fname ON e.id = ir_fname.res_id
                  WHERE w_1.type::text = 'PCM_REQ'::text) w ON pr.id::text = w.master_id::text
          WHERE pr.status::text <> 'D'::text) req
     LEFT JOIN ( SELECT e.employee_code,
            e.first_name,
            ir_fname.value AS first_name_th
           FROM pb2_ext_hr_employee e
             LEFT JOIN ( SELECT pb2_ext_ir_translation.res_id,
                    pb2_ext_ir_translation.value
                   FROM pb2_ext_ir_translation
                  WHERE pb2_ext_ir_translation.name::text = 'hr.employee,first_name'::text AND pb2_ext_ir_translation.type::text = 'model'::text AND pb2_ext_ir_translation.lang::text = 'th_TH'::text) ir_fname ON e.id = ir_fname.res_id) v_created_by ON req.created_by::text = v_created_by.employee_code::text
     LEFT JOIN ( SELECT e.employee_code,
            e.first_name,
            ir_fname.value AS first_name_th
           FROM pb2_ext_hr_employee e
             LEFT JOIN ( SELECT pb2_ext_ir_translation.res_id,
                    pb2_ext_ir_translation.value
                   FROM pb2_ext_ir_translation
                  WHERE pb2_ext_ir_translation.name::text = 'hr.employee,first_name'::text AND pb2_ext_ir_translation.type::text = 'model'::text AND pb2_ext_ir_translation.lang::text = 'th_TH'::text) ir_fname ON e.id = ir_fname.res_id) v_req_by ON req.req_by::text = v_req_by.employee_code::text
     LEFT JOIN ( SELECT t.id,
            t.name AS objective_type_name,
            t.sequence,
            COALESCE(ir.value, t.name::text) AS objective_type_name_th
           FROM pb2_ext_purchase_type t
             LEFT JOIN ( SELECT pb2_ext_ir_translation.res_id,
                    pb2_ext_ir_translation.value
                   FROM pb2_ext_ir_translation
                  WHERE pb2_ext_ir_translation.name::text = 'purchase_type,name'::text AND pb2_ext_ir_translation.type::text = 'model'::text AND pb2_ext_ir_translation.lang::text = 'th_TH'::text) ir ON t.id = ir.res_id) mm ON req.objective_type = mm.id
     LEFT JOIN ( SELECT 'U'::text AS _type,
            s.id,
            concat('[', btrim(s.code::text), '] ', s.name) AS name,
            concat('[', btrim(s.code::text), '] ', COALESCE(irs.value, s.name::text)) AS name_th
           FROM pb2_ext_res_section s
             LEFT JOIN ( SELECT pb2_ext_ir_translation.res_id,
                    pb2_ext_ir_translation.value
                   FROM pb2_ext_ir_translation
                  WHERE pb2_ext_ir_translation.name::text = 'res.section,name'::text AND pb2_ext_ir_translation.type::text = 'model'::text AND pb2_ext_ir_translation.lang::text = 'th_TH'::text) irs ON s.id = irs.res_id
          WHERE s.active = true
        UNION
         SELECT 'P'::text AS _type,
            p.id,
            concat('[', btrim(p.code::text), '] ', p.name) AS name,
            concat('[', btrim(p.code::text), '] ', COALESCE(irp.value, p.name::text)) AS name_th
           FROM pb2_ext_res_project p
             LEFT JOIN ( SELECT pb2_ext_ir_translation.res_id,
                    pb2_ext_ir_translation.value
                   FROM pb2_ext_ir_translation
                  WHERE pb2_ext_ir_translation.name::text = 'res.project,name'::text AND pb2_ext_ir_translation.type::text = 'model'::text AND pb2_ext_ir_translation.lang::text = 'th_TH'::text) irp ON p.id = irp.res_id
          WHERE p.active = true
        UNION
         SELECT 'A'::text AS _type,
            i.id,
            concat('[', btrim(i.code::text), '] ', i.name) AS asset,
            concat('[', btrim(i.code::text), '] ', COALESCE(ira.value, i.name::text)) AS asset_th
           FROM pb2_ext_res_invest_asset i
             LEFT JOIN ( SELECT pb2_ext_ir_translation.res_id,
                    pb2_ext_ir_translation.value
                   FROM pb2_ext_ir_translation
                  WHERE pb2_ext_ir_translation.name::text = 'res.invest.asset,name'::text AND pb2_ext_ir_translation.type::text = 'model'::text AND pb2_ext_ir_translation.lang::text = 'th_TH'::text) ira ON i.id = ira.res_id
          WHERE i.active IS TRUE
        UNION
         SELECT 'C'::text AS _type,
            cp.id,
            concat('[', btrim(cp.code::text), '] ', cp.name) AS name,
            concat('[', btrim(cp.code::text), '] ', COALESCE(ira.value, cp.name::text)) AS name_th
           FROM pb2_ext_res_invest_construction_phase cp
             LEFT JOIN ( SELECT pb2_ext_ir_translation.res_id,
                    pb2_ext_ir_translation.value
                   FROM pb2_ext_ir_translation
                  WHERE pb2_ext_ir_translation.name::text = 'res.invest.construction.phase,name'::text AND pb2_ext_ir_translation.type::text = 'model'::text AND pb2_ext_ir_translation.lang::text = 'th_TH'::text) ira ON cp.id = ira.res_id
             LEFT JOIN pb2_ext_res_invest_construction ct ON ct.id = cp.invest_construction_id
          WHERE cp.active IS TRUE) b ON req.budget_cc_type::text = b._type AND req.budget_cc = b.id
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
GRANT ALL ON TABLE public.pb2_pcm_req_view TO alfresco;

CREATE OR REPLACE VIEW public.pb2_pr_method_committee_view AS 
 SELECT c.id,
    r.sequence AS seq,
    c.name AS title,
    r.number_committee AS amount_min,
    r.method_id
   FROM pb2_ext_purchase_committee_type_prweb_method r,
    pb2_ext_purchase_committee_type c
  WHERE r.committee_type_id = c.id AND c.prweb_only IS TRUE
  ORDER BY r.method_id, r.sequence, c.name;

ALTER TABLE public.pb2_pr_method_committee_view
  OWNER TO alfresco;
GRANT ALL ON TABLE public.pb2_pr_method_committee_view TO alfresco;
GRANT SELECT ON TABLE public.pb2_pr_method_committee_view TO etl_pabi2;

GRANT SELECT ON TABLE public.pb2_pcm_req_view TO etl_pabi2;

CREATE OR REPLACE VIEW public.pb2_pr_method_view AS 
 SELECT p.id,
    t.name AS obj,
    p.name AS method,
    pr.name AS cond1,
    ''::text AS cond2,
    ''::text AS doc_type,
    p.price_range_id,
    ''::text AS condition_id,
    ''::text AS doctype_id,
    ''::text AS method_id,
    pr.price_from,
    pr.price_to,
    p.type_id
   FROM pb2_ext_prweb_purchase_method p
     LEFT JOIN pb2_ext_purchase_type t ON p.type_id = t.id
     LEFT JOIN pb2_ext_purchase_price_range pr ON p.price_range_id = pr.id
  ORDER BY t.name, p.name;

ALTER TABLE public.pb2_pr_method_view
  OWNER TO alfresco;
GRANT ALL ON TABLE public.pb2_pr_method_view TO alfresco;
GRANT SELECT ON TABLE public.pb2_pr_method_view TO etl_pabi2;

CREATE OR REPLACE VIEW public.pb2_project_view WITH (security_barrier=false) AS 
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
    concat('[', btrim(p.code::text), '] ', COALESCE(irp.value, p.name::text)) AS description_th,
    p.state,
    p.date_start,
    p.date_end
   FROM pb2_ext_res_project p
     LEFT JOIN pb2_ext_res_org o ON p.org_id = o.id
     LEFT JOIN pb2_ext_hr_employee e ON p.pm_employee_id = e.id
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
GRANT ALL ON TABLE public.pb2_project_view TO alfresco;
GRANT SELECT ON TABLE public.pb2_project_view TO etl_pabi2;

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
    s.name_short AS section_name_short,
    s.internal_charge
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
GRANT ALL ON TABLE public.pb2_section_view TO alfresco;
GRANT SELECT ON TABLE public.pb2_section_view TO etl_pabi2;

CREATE OR REPLACE VIEW public.pb2_workflow_view AS 
 SELECT t.activititask,
    t.activitiworkflow,
    t.docid,
    t.description_,
    t.assignee_,
    t.name_,
    pr.id,
    pr.objective,
    pr.preparer,
    pr.requester,
    pr.preparer_code,
    pr.requester_code,
    pr.status,
    pr.action,
    pr.reqdate,
    pr.all_rev,
    pr.workflow_ins_id,
    pr.folder_ref,
    t.assignee_code,
    t.create_time_
   FROM ( SELECT concat('activiti$', act_ru_task.id_) AS activititask,
            concat('activiti$', act_ru_task.proc_inst_id_) AS activitiworkflow,
            btrim("substring"(act_ru_task.description_::text, 1, 10)) AS docid,
            act_ru_task.description_,
            act_ru_task.assignee_ AS assignee_code,
            e.first_name AS assignee_,
            act_ru_task.name_,
            act_ru_task.create_time_
           FROM act_ru_task
             LEFT JOIN ( SELECT pb2_ext_hr_employee.employee_code,
                    pb2_ext_hr_employee.first_name
                   FROM pb2_ext_hr_employee) e ON act_ru_task.assignee_::text = e.employee_code::text
          WHERE act_ru_task.description_::text ~~ 'PR%'::text) t
     LEFT JOIN ( SELECT pr_1.id,
            pr_1.objective,
            vc.first_name AS preparer,
            vr.first_name AS requester,
            pr_1.created_by AS preparer_code,
            pr_1.req_by AS requester_code,
            pr_1.status,
            w.status AS action,
            pr_1.requested_time AS reqdate,
            all_rev.all_rev,
            pr_1.workflow_ins_id,
            pr_1.folder_ref
           FROM pb2_pcm_req pr_1
             LEFT JOIN pb2_main_workflow w ON pr_1.id::text = w.master_id::text
             JOIN pb2_ext_hr_employee vr ON pr_1.req_by::text = vr.employee_code::text
             LEFT JOIN pb2_ext_hr_employee vc ON pr_1.created_by::text = vc.employee_code::text
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
                  GROUP BY e.master_id) all_rev ON pr_1.id::text = all_rev.master_id::text) pr ON t.docid = pr.id::text
UNION
 SELECT t.activititask,
    t.activitiworkflow,
    t.docid,
    t.description_,
    t.assignee_,
    t.name_,
    pd.id,
    pd.objective,
    pd.preparer,
    pd.requester,
    pd.preparer_code,
    pd.requester_code,
    pd.status,
    pd.action,
    pd.reqdate,
    pd.all_rev,
    pd.workflow_ins_id,
    pd.folder_ref,
    t.assignee_code,
    t.create_time_
   FROM ( SELECT concat('activiti$', act_ru_task.id_) AS activititask,
            concat('activiti$', act_ru_task.proc_inst_id_) AS activitiworkflow,
            btrim("substring"(act_ru_task.description_::text, 1, 10)) AS docid,
            act_ru_task.description_,
            act_ru_task.assignee_ AS assignee_code,
            e.first_name AS assignee_,
            act_ru_task.name_,
            act_ru_task.create_time_
           FROM act_ru_task
             LEFT JOIN ( SELECT pb2_ext_hr_employee.employee_code,
                    pb2_ext_hr_employee.first_name
                   FROM pb2_ext_hr_employee) e ON act_ru_task.assignee_::text = e.employee_code::text
          WHERE act_ru_task.description_::text ~~ 'PD%'::text) t
     LEFT JOIN ( SELECT pd_1.id,
            pd_1.objective,
            vc.first_name AS preparer,
            vc.first_name AS requester,
            pd_1.created_by AS preparer_code,
            pd_1.created_by AS requester_code,
            pd_1.status,
            w.status AS action,
            pd_1.created_time AS reqdate,
            all_rev.all_rev,
            pd_1.workflow_ins_id,
            pd_1.folder_ref
           FROM pb2_pcm_ord pd_1
             LEFT JOIN pb2_main_workflow w ON pd_1.id::text = w.master_id::text
             LEFT JOIN pb2_ext_hr_employee vc ON pd_1.created_by::text = vc.employee_code::text
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
                  GROUP BY e.master_id) all_rev ON pd_1.id::text = all_rev.master_id::text) pd ON t.docid = pd.id::text
UNION
 SELECT t.activititask,
    t.activitiworkflow,
    t.docid,
    t.description_,
    t.assignee_,
    t.name_,
    ex.id,
    ex.objective,
    ex.preparer,
    ex.requester,
    ex.preparer_code,
    ex.requester_code,
    ex.status,
    ex.action,
    ex.reqdate,
    ex.all_rev,
    ex.workflow_ins_id,
    ex.folder_ref,
    t.assignee_code,
    t.create_time_
   FROM ( SELECT concat('activiti$', act_ru_task.id_) AS activititask,
            concat('activiti$', act_ru_task.proc_inst_id_) AS activitiworkflow,
            btrim("substring"(act_ru_task.description_::text, 1, 10)) AS docid,
            act_ru_task.description_,
            act_ru_task.assignee_ AS assignee_code,
            e.first_name AS assignee_,
            act_ru_task.name_,
            act_ru_task.create_time_
           FROM act_ru_task
             LEFT JOIN ( SELECT pb2_ext_hr_employee.employee_code,
                    pb2_ext_hr_employee.first_name
                   FROM pb2_ext_hr_employee) e ON act_ru_task.assignee_::text = e.employee_code::text
          WHERE act_ru_task.description_::text ~~ 'EX%'::text) t
     LEFT JOIN ( SELECT ex_1.id,
            ex_1.objective,
            vc.first_name AS preparer,
            vr.first_name AS requester,
            ex_1.created_by AS preparer_code,
            ex_1.req_by AS requester_code,
            ex_1.status,
            w.status AS action,
            ex_1.requested_time AS reqdate,
            all_rev.all_rev,
            ex_1.workflow_ins_id,
            ex_1.folder_ref
           FROM pb2_exp_use ex_1
             LEFT JOIN pb2_main_workflow w ON ex_1.id::text = w.master_id::text
             JOIN pb2_ext_hr_employee vr ON ex_1.req_by::text = vr.employee_code::text
             LEFT JOIN pb2_ext_hr_employee vc ON ex_1.created_by::text = vc.employee_code::text
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
                  GROUP BY e.master_id) all_rev ON ex_1.id::text = all_rev.master_id::text) ex ON t.docid = ex.id::text
UNION
 SELECT t.activititask,
    t.activitiworkflow,
    t.docid,
    t.description_,
    t.assignee_,
    t.name_,
    av.id,
    av.objective,
    av.preparer,
    av.requester,
    av.preparer_code,
    av.requester_code,
    av.status,
    av.action,
    av.reqdate,
    av.all_rev,
    av.workflow_ins_id,
    av.folder_ref,
    t.assignee_code,
    t.create_time_
   FROM ( SELECT concat('activiti$', act_ru_task.id_) AS activititask,
            concat('activiti$', act_ru_task.proc_inst_id_) AS activitiworkflow,
            btrim("substring"(act_ru_task.description_::text, 1, 10)) AS docid,
            act_ru_task.description_,
            act_ru_task.assignee_ AS assignee_code,
            e.first_name AS assignee_,
            act_ru_task.name_,
            act_ru_task.create_time_
           FROM act_ru_task
             LEFT JOIN ( SELECT pb2_ext_hr_employee.employee_code,
                    pb2_ext_hr_employee.first_name
                   FROM pb2_ext_hr_employee) e ON act_ru_task.assignee_::text = e.employee_code::text
          WHERE act_ru_task.description_::text ~~ 'AV%'::text) t
     LEFT JOIN ( SELECT av_1.id,
            av_1.objective,
            vc.first_name AS preparer,
            vr.first_name AS requester,
            av_1.created_by AS preparer_code,
            av_1.req_by AS requester_code,
            av_1.status,
            w.status AS action,
            av_1.requested_time AS reqdate,
            all_rev.all_rev,
            av_1.workflow_ins_id,
            av_1.folder_ref
           FROM pb2_exp_brw av_1
             LEFT JOIN pb2_main_workflow w ON av_1.id::text = w.master_id::text
             JOIN pb2_ext_hr_employee vr ON av_1.req_by::text = vr.employee_code::text
             LEFT JOIN pb2_ext_hr_employee vc ON av_1.created_by::text = vc.employee_code::text
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
                  GROUP BY e.master_id) all_rev ON av_1.id::text = all_rev.master_id::text) av ON t.docid = av.id::text
UNION
 SELECT t.activititask,
    t.activitiworkflow,
    t.docid,
    t.description_,
    t.assignee_,
    t.name_,
    sl.id,
    sl.objective,
    sl.preparer,
    sl.requester,
    sl.preparer_code,
    sl.requester_code,
    sl.status,
    sl.action,
    sl.reqdate,
    sl.all_rev,
    sl.workflow_ins_id,
    sl.folder_ref,
    t.assignee_code,
    t.create_time_
   FROM ( SELECT concat('activiti$', act_ru_task.id_) AS activititask,
            concat('activiti$', act_ru_task.proc_inst_id_) AS activitiworkflow,
            btrim("substring"(act_ru_task.description_::text, 1, 10)) AS docid,
            act_ru_task.description_,
            act_ru_task.assignee_ AS assignee_code,
            e.first_name AS assignee_,
            act_ru_task.name_,
            act_ru_task.create_time_
           FROM act_ru_task
             LEFT JOIN ( SELECT pb2_ext_hr_employee.employee_code,
                    pb2_ext_hr_employee.first_name
                   FROM pb2_ext_hr_employee) e ON act_ru_task.assignee_::text = e.employee_code::text
          WHERE act_ru_task.description_::text ~~ 'SL%'::text) t
     LEFT JOIN ( SELECT sl_1.id,
            sl_1.objective,
            vc.first_name AS preparer,
            vc.first_name AS requester,
            sl_1.created_by AS preparer_code,
            sl_1.created_by AS requester_code,
            sl_1.status,
            w.status AS action,
            sl_1.created_time AS reqdate,
            all_rev.all_rev,
            sl_1.workflow_ins_id,
            sl_1.folder_ref
           FROM pb2_hr_salary sl_1
             LEFT JOIN pb2_main_workflow w ON sl_1.id::text = w.master_id::text
             LEFT JOIN pb2_ext_hr_employee vc ON sl_1.created_by::text = vc.employee_code::text
             LEFT JOIN ( SELECT e.master_id,
                    string_agg(e.all_rev::text, ' '::text) AS all_rev
                   FROM ( SELECT pb2_main_workflow_reviewer.master_id,
                            pb2_main_workflow_reviewer.reviewer_user AS all_rev
                           FROM pb2_main_workflow_reviewer
                          WHERE pb2_main_workflow_reviewer.master_id::text ~~ 'SL%'::text
                        UNION
                         SELECT pb2_main_workflow_next_actor.master_id,
                            pb2_main_workflow_next_actor.actor_user AS all_rev
                           FROM pb2_main_workflow_next_actor
                          WHERE pb2_main_workflow_next_actor.master_id::text ~~ 'SL%'::text) e
                  GROUP BY e.master_id) all_rev ON sl_1.id::text = all_rev.master_id::text) sl ON t.docid = sl.id::text
UNION
 SELECT ''::text AS activititask,
    concat('activiti$', e.proc_inst_id_) AS activitiworkflow,
    "substring"(v.text_::text, 1, 10) AS docid,
    v.text_ AS description_,
    ''::character varying AS assignee_,
    ''::character varying AS name_,
    pd.id,
    pd.objective,
    pd.preparer,
    pd.requester,
    pd.preparer_code,
    pd.requester_code,
    pd.status,
    pd.action,
    pd.reqdate,
    pd.all_rev,
    pd.workflow_ins_id,
    pd.folder_ref,
    ''::character varying AS assignee_code,
    NULL::timestamp without time zone AS create_time_
   FROM ( SELECT act_ru_execution.id_,
            act_ru_execution.rev_,
            act_ru_execution.proc_inst_id_,
            act_ru_execution.business_key_,
            act_ru_execution.parent_id_,
            act_ru_execution.proc_def_id_,
            act_ru_execution.super_exec_,
            act_ru_execution.act_id_,
            act_ru_execution.is_active_,
            act_ru_execution.is_concurrent_,
            act_ru_execution.is_scope_,
            act_ru_execution.is_event_scope_,
            act_ru_execution.suspension_state_,
            act_ru_execution.cached_ent_state_
           FROM act_ru_execution
          WHERE act_ru_execution.proc_def_id_::text ~~ 'NSTDAPcmPD%'::text AND act_ru_execution.act_id_::text = 'RequesterRemote'::text) e
     LEFT JOIN ( SELECT act_ru_variable.id_,
            act_ru_variable.rev_,
            act_ru_variable.type_,
            act_ru_variable.name_,
            act_ru_variable.execution_id_,
            act_ru_variable.proc_inst_id_,
            act_ru_variable.task_id_,
            act_ru_variable.bytearray_id_,
            act_ru_variable.double_,
            act_ru_variable.long_,
            act_ru_variable.text_,
            act_ru_variable.text2_
           FROM act_ru_variable
          WHERE act_ru_variable.name_::text = 'pcmordwf_description'::text) v ON e.proc_inst_id_::text = v.proc_inst_id_::text
     LEFT JOIN ( SELECT pd_1.id,
            pd_1.objective,
            vc.first_name AS preparer,
            vc.first_name AS requester,
            pd_1.created_by AS preparer_code,
            pd_1.created_by AS requester_code,
            pd_1.status,
            w.status AS action,
            pd_1.created_time AS reqdate,
            all_rev.all_rev,
            pd_1.workflow_ins_id,
            pd_1.folder_ref
           FROM pb2_pcm_ord pd_1
             LEFT JOIN pb2_main_workflow w ON pd_1.id::text = w.master_id::text
             LEFT JOIN pb2_ext_hr_employee vc ON pd_1.created_by::text = vc.employee_code::text
             LEFT JOIN ( SELECT e_1.master_id,
                    string_agg(e_1.all_rev::text, ' '::text) AS all_rev
                   FROM ( SELECT pb2_main_workflow_reviewer.master_id,
                            pb2_main_workflow_reviewer.reviewer_user AS all_rev
                           FROM pb2_main_workflow_reviewer
                          WHERE pb2_main_workflow_reviewer.master_id::text ~~ 'PD%'::text
                        UNION
                         SELECT pb2_main_workflow_next_actor.master_id,
                            pb2_main_workflow_next_actor.actor_user AS all_rev
                           FROM pb2_main_workflow_next_actor
                          WHERE pb2_main_workflow_next_actor.master_id::text ~~ 'PD%'::text) e_1
                  GROUP BY e_1.master_id) all_rev ON pd_1.id::text = all_rev.master_id::text) pd ON concat('activiti$', e.proc_inst_id_) = pd.workflow_ins_id::text;

ALTER TABLE public.pb2_workflow_view
  OWNER TO alfresco;
GRANT ALL ON TABLE public.pb2_workflow_view TO alfresco;
GRANT SELECT ON TABLE public.pb2_workflow_view TO intraymon;
GRANT SELECT ON TABLE public.pb2_workflow_view TO etl_pabi2;
COMMENT ON VIEW public.pb2_workflow_view
  IS '- ใช้กับหน้า Search ของ Workflow Tool, และเช็คเพื่อป้องกันการ Start Workflow ซ้ำ
- union query ชุดสุดท้าย เป็นการเอาข้อมูลใบ PD ที่ผู้อนุมัติ reject กลับไปให้คนทาง Odoo แก้ไขและส่งกลับมาขออนุมัติใหม่ได้ เป็น Workflow Task ที่ไม่มี Assignee';