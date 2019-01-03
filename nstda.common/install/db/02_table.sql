--
-- PostgreSQL database dump
--

-- Dumped from database version 9.5.13
-- Dumped by pg_dump version 9.5.5

-- Started on 2019-01-03 09:55:39

SET statement_timeout = 0;
SET lock_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;
SET row_security = off;

SET search_path = public, pg_catalog;

SET default_tablespace = '';

SET default_with_oids = false;

--
-- TOC entry 376 (class 1259 OID 26723)
-- Name: pb2_exp_brw; Type: TABLE; Schema: public; Owner: alfresco
--

CREATE TABLE pb2_exp_brw (
    id character varying(50) NOT NULL,
    total double precision,
    folder_ref character varying(255),
    doc_ref character varying(255),
    workflow_ins_id character varying(50),
    waiting_level integer,
    status character varying(2),
    req_by character varying(20),
    created_time timestamp with time zone DEFAULT statement_timestamp(),
    created_by character varying(20),
    updated_time timestamp with time zone DEFAULT statement_timestamp(),
    updated_by character varying(20),
    objective_type character varying,
    objective character varying(1000),
    reason character varying(1000),
    budget_cc_type character varying(1),
    budget_cc integer,
    cost_control_type_id integer,
    cost_control_id integer,
    bank_type character varying,
    date_back timestamp with time zone,
    bank integer,
    fund_id integer,
    note character varying,
    av_remark character varying,
    requested_time timestamp with time zone,
    is_reason character varying(1),
    is_small_amount character varying(1),
    origin_pr_number character varying
);


ALTER TABLE pb2_exp_brw OWNER TO alfresco;

--
-- TOC entry 434 (class 1259 OID 27155)
-- Name: pb2_exp_brw_attendee; Type: TABLE; Schema: public; Owner: alfresco
--

CREATE TABLE pb2_exp_brw_attendee (
    id bigint NOT NULL,
    master_id character varying(50) NOT NULL,
    code character varying,
    created_time timestamp with time zone DEFAULT statement_timestamp(),
    created_by character varying(20),
    updated_time timestamp with time zone DEFAULT statement_timestamp(),
    updated_by character varying(20),
    fname character varying,
    unit_type character varying,
    "position" character varying,
    type character varying,
    position_id integer,
    lname character varying,
    title character varying
);


ALTER TABLE pb2_exp_brw_attendee OWNER TO alfresco;

--
-- TOC entry 433 (class 1259 OID 27153)
-- Name: pb2_exp_brw_attendee_id_seq; Type: SEQUENCE; Schema: public; Owner: alfresco
--

CREATE SEQUENCE pb2_exp_brw_attendee_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE pb2_exp_brw_attendee_id_seq OWNER TO alfresco;

--
-- TOC entry 3059 (class 0 OID 0)
-- Dependencies: 433
-- Name: pb2_exp_brw_attendee_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: alfresco
--

ALTER SEQUENCE pb2_exp_brw_attendee_id_seq OWNED BY pb2_exp_brw_attendee.id;


--
-- TOC entry 377 (class 1259 OID 26739)
-- Name: pb2_exp_brw_dtl; Type: TABLE; Schema: public; Owner: alfresco
--

CREATE TABLE pb2_exp_brw_dtl (
    id bigint NOT NULL,
    master_id character varying(50) NOT NULL,
    activity character varying,
    amount double precision,
    created_time timestamp with time zone DEFAULT statement_timestamp(),
    created_by character varying(20),
    updated_time timestamp with time zone DEFAULT statement_timestamp(),
    updated_by character varying(20),
    act_grp_id integer,
    act_id integer,
    condition_1 character varying,
    asset_rule_id integer
);


ALTER TABLE pb2_exp_brw_dtl OWNER TO alfresco;

--
-- TOC entry 378 (class 1259 OID 26747)
-- Name: pb2_exp_brw_dtl_id_seq; Type: SEQUENCE; Schema: public; Owner: alfresco
--

CREATE SEQUENCE pb2_exp_brw_dtl_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE pb2_exp_brw_dtl_id_seq OWNER TO alfresco;

--
-- TOC entry 3061 (class 0 OID 0)
-- Dependencies: 378
-- Name: pb2_exp_brw_dtl_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: alfresco
--

ALTER SEQUENCE pb2_exp_brw_dtl_id_seq OWNED BY pb2_exp_brw_dtl.id;


--
-- TOC entry 379 (class 1259 OID 26749)
-- Name: pb2_main_master; Type: TABLE; Schema: public; Owner: alfresco
--

CREATE TABLE pb2_main_master (
    id bigint NOT NULL,
    type character varying(10),
    code character varying(40),
    name character varying(255),
    is_active boolean,
    flag1 character varying(255),
    flag2 character varying(255),
    flag3 character varying(255),
    flag4 character varying(255),
    flag5 character varying(255),
    created_time timestamp with time zone DEFAULT statement_timestamp(),
    created_by character varying(20),
    updated_time timestamp with time zone DEFAULT statement_timestamp(),
    updated_by character varying(20),
    is_system boolean
);


ALTER TABLE pb2_main_master OWNER TO alfresco;

--
-- TOC entry 380 (class 1259 OID 26757)
-- Name: pb2_main_workflow; Type: TABLE; Schema: public; Owner: alfresco
--

CREATE TABLE pb2_main_workflow (
    id bigint NOT NULL,
    type character varying(10),
    master_id character varying(50) NOT NULL,
    workflow_ins_id character varying(255),
    status character varying(255),
    by character varying(20),
    by_time timestamp with time zone,
    task_id character varying(255),
    created_time timestamp with time zone,
    created_by character varying(20),
    assignee character varying(20),
    execution_id character varying,
    status_th character varying
);


ALTER TABLE pb2_main_workflow OWNER TO alfresco;

--
-- TOC entry 381 (class 1259 OID 26763)
-- Name: pb2_main_workflow_next_actor; Type: TABLE; Schema: public; Owner: alfresco
--

CREATE TABLE pb2_main_workflow_next_actor (
    id bigint NOT NULL,
    master_id character varying(20),
    level integer,
    actor_group character varying(500),
    actor_user character varying(500),
    created_time timestamp with time zone,
    created_by character varying(20),
    actor character varying
);


ALTER TABLE pb2_main_workflow_next_actor OWNER TO alfresco;

--
-- TOC entry 382 (class 1259 OID 26769)
-- Name: pb2_main_workflow_reviewer; Type: TABLE; Schema: public; Owner: alfresco
--

CREATE TABLE pb2_main_workflow_reviewer (
    id bigint NOT NULL,
    master_id character varying(20),
    level integer,
    reviewer_group character varying(500),
    reviewer_user character varying(500),
    percent double precision,
    rewarning integer,
    hint character varying(255),
    created_time timestamp with time zone,
    created_by character varying(20)
);


ALTER TABLE pb2_main_workflow_reviewer OWNER TO alfresco;

--
-- TOC entry 383 (class 1259 OID 26775)
-- Name: pb2_exp_use; Type: TABLE; Schema: public; Owner: alfresco
--

CREATE TABLE pb2_exp_use (
    id character varying(50) NOT NULL,
    total double precision,
    folder_ref character varying(255),
    doc_ref character varying(255),
    workflow_ins_id character varying(50),
    waiting_level integer,
    status character varying(2),
    req_by character varying(20),
    created_time timestamp with time zone DEFAULT statement_timestamp(),
    created_by character varying(20),
    updated_time timestamp with time zone DEFAULT statement_timestamp(),
    updated_by character varying(20),
    objective character varying(1000),
    budget_cc_type character varying(1),
    budget_cc integer,
    cost_control_type_id integer,
    cost_control_id integer,
    cost_control character varying,
    cost_control_from timestamp with time zone,
    cost_control_to timestamp with time zone,
    bank_type character varying,
    bank integer,
    pay_type character varying,
    pay_dtl1 character varying,
    pay_dtl2 character varying,
    pay_dtl3 character varying,
    fund_id integer,
    reason character varying,
    note character varying,
    requested_time timestamp with time zone,
    is_reason character varying(1),
    is_small_amount character varying(1),
    inf_id bigint
);


ALTER TABLE pb2_exp_use OWNER TO alfresco;

--
-- TOC entry 439 (class 1259 OID 27180)
-- Name: pb2_exp_use_attendee; Type: TABLE; Schema: public; Owner: alfresco
--

CREATE TABLE pb2_exp_use_attendee (
    id bigint NOT NULL,
    master_id character varying(50) NOT NULL,
    code character varying,
    fname character varying,
    unit_type character varying,
    "position" character varying,
    type character varying,
    created_time timestamp with time zone DEFAULT statement_timestamp(),
    created_by character varying(20),
    updated_time timestamp with time zone DEFAULT statement_timestamp(),
    updated_by character varying(20),
    position_id integer,
    lname character varying,
    title character varying
);


ALTER TABLE pb2_exp_use_attendee OWNER TO alfresco;

--
-- TOC entry 438 (class 1259 OID 27178)
-- Name: pb2_exp_use_attendee_id_seq; Type: SEQUENCE; Schema: public; Owner: alfresco
--

CREATE SEQUENCE pb2_exp_use_attendee_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE pb2_exp_use_attendee_id_seq OWNER TO alfresco;

--
-- TOC entry 3068 (class 0 OID 0)
-- Dependencies: 438
-- Name: pb2_exp_use_attendee_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: alfresco
--

ALTER SEQUENCE pb2_exp_use_attendee_id_seq OWNED BY pb2_exp_use_attendee.id;


--
-- TOC entry 384 (class 1259 OID 26791)
-- Name: pb2_exp_use_dtl; Type: TABLE; Schema: public; Owner: alfresco
--

CREATE TABLE pb2_exp_use_dtl (
    id bigint NOT NULL,
    master_id character varying(50) NOT NULL,
    created_time timestamp with time zone DEFAULT statement_timestamp(),
    created_by character varying(20),
    updated_time timestamp with time zone DEFAULT statement_timestamp(),
    updated_by character varying(20),
    condition_1 character varying,
    amount double precision,
    condition_2 character varying,
    "position" character varying,
    uom character varying,
    act_id integer,
    act_grp_id integer,
    activity character varying,
    asset_rule_id integer
);


ALTER TABLE pb2_exp_use_dtl OWNER TO alfresco;

--
-- TOC entry 329 (class 1259 OID 26528)
-- Name: pb2_exp_use_dtl_id_seq; Type: SEQUENCE; Schema: public; Owner: alfresco
--

CREATE SEQUENCE pb2_exp_use_dtl_id_seq
    START WITH 269
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE pb2_exp_use_dtl_id_seq OWNER TO alfresco;

--
-- TOC entry 3070 (class 0 OID 0)
-- Dependencies: 329
-- Name: pb2_exp_use_dtl_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: alfresco
--

ALTER SEQUENCE pb2_exp_use_dtl_id_seq OWNED BY pb2_exp_use_dtl.id;


--
-- TOC entry 419 (class 1259 OID 27079)
-- Name: pb2_hr_salary; Type: TABLE; Schema: public; Owner: alfresco
--

CREATE TABLE pb2_hr_salary (
    id character varying(50) NOT NULL,
    total double precision,
    folder_ref character varying(255),
    doc_ref character varying(255),
    status character varying(2),
    created_time timestamp with time zone DEFAULT statement_timestamp(),
    created_by character varying(20),
    updated_time timestamp with time zone DEFAULT statement_timestamp(),
    updated_by character varying(20),
    waiting_level integer,
    workflow_ins_id character varying(50),
    objective character varying,
    section_id integer,
    doc_type character varying
);


ALTER TABLE pb2_hr_salary OWNER TO alfresco;

--
-- TOC entry 385 (class 1259 OID 26799)
-- Name: pb2_main_complete_notification; Type: TABLE; Schema: public; Owner: alfresco
--

CREATE TABLE pb2_main_complete_notification (
    id bigint NOT NULL,
    receiver character varying(255),
    task_id character varying(100),
    template character(1),
    created_time timestamp with time zone,
    created_by character varying(20)
);


ALTER TABLE pb2_main_complete_notification OWNER TO alfresco;

--
-- TOC entry 386 (class 1259 OID 26802)
-- Name: pb2_main_complete_notification_id_seq; Type: SEQUENCE; Schema: public; Owner: alfresco
--

CREATE SEQUENCE pb2_main_complete_notification_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE pb2_main_complete_notification_id_seq OWNER TO alfresco;

--
-- TOC entry 3073 (class 0 OID 0)
-- Dependencies: 386
-- Name: pb2_main_complete_notification_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: alfresco
--

ALTER SEQUENCE pb2_main_complete_notification_id_seq OWNED BY pb2_main_complete_notification.id;


--
-- TOC entry 432 (class 1259 OID 27128)
-- Name: pb2_main_interface; Type: TABLE; Schema: public; Owner: alfresco
--

CREATE TABLE pb2_main_interface (
    id bigint NOT NULL,
    source character varying(500),
    method character varying(500),
    params character varying,
    created_time timestamp with time zone DEFAULT statement_timestamp()
);


ALTER TABLE pb2_main_interface OWNER TO alfresco;

--
-- TOC entry 431 (class 1259 OID 27126)
-- Name: pb2_main_interface_id_seq; Type: SEQUENCE; Schema: public; Owner: alfresco
--

CREATE SEQUENCE pb2_main_interface_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE pb2_main_interface_id_seq OWNER TO alfresco;

--
-- TOC entry 3075 (class 0 OID 0)
-- Dependencies: 431
-- Name: pb2_main_interface_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: alfresco
--

ALTER SEQUENCE pb2_main_interface_id_seq OWNED BY pb2_main_interface.id;


--
-- TOC entry 387 (class 1259 OID 26804)
-- Name: pb2_main_master_id_seq; Type: SEQUENCE; Schema: public; Owner: alfresco
--

CREATE SEQUENCE pb2_main_master_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE pb2_main_master_id_seq OWNER TO alfresco;

--
-- TOC entry 3076 (class 0 OID 0)
-- Dependencies: 387
-- Name: pb2_main_master_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: alfresco
--

ALTER SEQUENCE pb2_main_master_id_seq OWNED BY pb2_main_master.id;


--
-- TOC entry 388 (class 1259 OID 26806)
-- Name: pb2_main_msg; Type: TABLE; Schema: public; Owner: alfresco
--

CREATE TABLE pb2_main_msg (
    user_ character varying(100),
    msg character varying(255)
);


ALTER TABLE pb2_main_msg OWNER TO alfresco;

--
-- TOC entry 446 (class 1259 OID 93074)
-- Name: pb2_main_workflow_assignee; Type: TABLE; Schema: public; Owner: alfresco
--

CREATE TABLE pb2_main_workflow_assignee (
    id bigint DEFAULT nextval('pb2_main_workflow_assignee_id_seq'::regclass) NOT NULL,
    workflow_ins_id character varying,
    src_user character varying,
    dest_user character varying,
    active character varying(1),
    created_by character varying,
    created_time timestamp with time zone
);


ALTER TABLE pb2_main_workflow_assignee OWNER TO alfresco;

--
-- TOC entry 389 (class 1259 OID 26809)
-- Name: pb2_main_workflow_history; Type: TABLE; Schema: public; Owner: alfresco
--

CREATE TABLE pb2_main_workflow_history (
    id bigint NOT NULL,
    master_id bigint,
    "time" timestamp with time zone,
    by character varying(50),
    action character varying(255),
    task character varying(255),
    comment character varying,
    level integer,
    status character varying,
    action_th character varying,
    task_th character varying
);


ALTER TABLE pb2_main_workflow_history OWNER TO alfresco;

--
-- TOC entry 390 (class 1259 OID 26815)
-- Name: pb2_main_workflow_history_id_seq; Type: SEQUENCE; Schema: public; Owner: alfresco
--

CREATE SEQUENCE pb2_main_workflow_history_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE pb2_main_workflow_history_id_seq OWNER TO alfresco;

--
-- TOC entry 3080 (class 0 OID 0)
-- Dependencies: 390
-- Name: pb2_main_workflow_history_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: alfresco
--

ALTER SEQUENCE pb2_main_workflow_history_id_seq OWNED BY pb2_main_workflow_history.id;


--
-- TOC entry 391 (class 1259 OID 26817)
-- Name: pb2_main_workflow_id_seq; Type: SEQUENCE; Schema: public; Owner: alfresco
--

CREATE SEQUENCE pb2_main_workflow_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE pb2_main_workflow_id_seq OWNER TO alfresco;

--
-- TOC entry 3081 (class 0 OID 0)
-- Dependencies: 391
-- Name: pb2_main_workflow_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: alfresco
--

ALTER SEQUENCE pb2_main_workflow_id_seq OWNED BY pb2_main_workflow.id;


--
-- TOC entry 392 (class 1259 OID 26819)
-- Name: pb2_main_workflow_next_actor_id_seq; Type: SEQUENCE; Schema: public; Owner: alfresco
--

CREATE SEQUENCE pb2_main_workflow_next_actor_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE pb2_main_workflow_next_actor_id_seq OWNER TO alfresco;

--
-- TOC entry 3082 (class 0 OID 0)
-- Dependencies: 392
-- Name: pb2_main_workflow_next_actor_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: alfresco
--

ALTER SEQUENCE pb2_main_workflow_next_actor_id_seq OWNED BY pb2_main_workflow_next_actor.id;


--
-- TOC entry 330 (class 1259 OID 26534)
-- Name: pb2_main_workflow_reviewer_id_seq; Type: SEQUENCE; Schema: public; Owner: alfresco
--

CREATE SEQUENCE pb2_main_workflow_reviewer_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE pb2_main_workflow_reviewer_id_seq OWNER TO alfresco;

--
-- TOC entry 3083 (class 0 OID 0)
-- Dependencies: 330
-- Name: pb2_main_workflow_reviewer_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: alfresco
--

ALTER SEQUENCE pb2_main_workflow_reviewer_id_seq OWNED BY pb2_main_workflow_reviewer.id;


--
-- TOC entry 393 (class 1259 OID 26821)
-- Name: pb2_pcm_ord; Type: TABLE; Schema: public; Owner: alfresco
--

CREATE TABLE pb2_pcm_ord (
    id character varying(50) NOT NULL,
    total double precision,
    folder_ref character varying(255),
    doc_ref character varying(255),
    status character varying(2),
    created_time timestamp with time zone DEFAULT statement_timestamp(),
    created_by character varying(20),
    updated_time timestamp with time zone DEFAULT statement_timestamp(),
    updated_by character varying(20),
    waiting_level integer,
    workflow_ins_id character varying(50),
    objective character varying,
    section_id integer,
    pr_id character varying,
    doc_type character varying,
    app_by character varying
);


ALTER TABLE pb2_pcm_ord OWNER TO alfresco;

--
-- TOC entry 394 (class 1259 OID 26829)
-- Name: pb2_pcm_req; Type: TABLE; Schema: public; Owner: alfresco
--

CREATE TABLE pb2_pcm_req (
    id character varying(50) NOT NULL,
    total double precision,
    folder_ref character varying(255),
    doc_ref character varying(255),
    workflow_ins_id character varying(50),
    waiting_level integer,
    status character varying(2),
    req_by character varying(20),
    created_time timestamp with time zone DEFAULT statement_timestamp(),
    created_by character varying(20),
    updated_time timestamp with time zone DEFAULT statement_timestamp(),
    updated_by character varying(20),
    _objective_type character varying(10),
    objective character varying(1000),
    reason character varying(1000),
    currency character varying(10),
    currency_rate real,
    prototype_type character varying(30),
    location character varying(1000),
    across_budget double precision,
    ref_id character varying(50),
    method_cond2_rule character varying(255),
    method_cond2 character varying(255),
    method_cond2_dtl character varying(1000),
    vat real,
    is_stock character varying(1),
    is_prototype character varying(1),
    is_across_budget character varying(1),
    is_ref_id character varying(1),
    vat_id integer,
    prototype_no character varying(50),
    budget_cc_type character varying(1),
    budget_cc integer,
    pcm_section_id integer,
    req_section_id integer,
    stock_section_id integer,
    cost_control_type_id integer,
    cost_control_id integer,
    prweb_method_id bigint,
    ref_doc_ref character varying,
    contract_date timestamp with time zone,
    total_cnv double precision,
    fund_id integer,
    requested_time timestamp with time zone,
    is_small_amount character varying(1),
    price_include boolean,
    objective_type integer
);


ALTER TABLE pb2_pcm_req OWNER TO alfresco;

--
-- TOC entry 3085 (class 0 OID 0)
-- Dependencies: 394
-- Name: COLUMN pb2_pcm_req.total; Type: COMMENT; Schema: public; Owner: alfresco
--

COMMENT ON COLUMN pb2_pcm_req.total IS 'Total';


--
-- TOC entry 3086 (class 0 OID 0)
-- Dependencies: 394
-- Name: COLUMN pb2_pcm_req.folder_ref; Type: COMMENT; Schema: public; Owner: alfresco
--

COMMENT ON COLUMN pb2_pcm_req.folder_ref IS 'Folder Node Ref.';


--
-- TOC entry 3087 (class 0 OID 0)
-- Dependencies: 394
-- Name: COLUMN pb2_pcm_req.doc_ref; Type: COMMENT; Schema: public; Owner: alfresco
--

COMMENT ON COLUMN pb2_pcm_req.doc_ref IS 'Document Node Ref.';


--
-- TOC entry 3088 (class 0 OID 0)
-- Dependencies: 394
-- Name: COLUMN pb2_pcm_req.workflow_ins_id; Type: COMMENT; Schema: public; Owner: alfresco
--

COMMENT ON COLUMN pb2_pcm_req.workflow_ins_id IS 'Workflow Instance ID';


--
-- TOC entry 3089 (class 0 OID 0)
-- Dependencies: 394
-- Name: COLUMN pb2_pcm_req.waiting_level; Type: COMMENT; Schema: public; Owner: alfresco
--

COMMENT ON COLUMN pb2_pcm_req.waiting_level IS 'Workflow Waiting Level';


--
-- TOC entry 3090 (class 0 OID 0)
-- Dependencies: 394
-- Name: COLUMN pb2_pcm_req.status; Type: COMMENT; Schema: public; Owner: alfresco
--

COMMENT ON COLUMN pb2_pcm_req.status IS 'PR Status';


--
-- TOC entry 3091 (class 0 OID 0)
-- Dependencies: 394
-- Name: COLUMN pb2_pcm_req.req_by; Type: COMMENT; Schema: public; Owner: alfresco
--

COMMENT ON COLUMN pb2_pcm_req.req_by IS 'Requeted by';


--
-- TOC entry 3092 (class 0 OID 0)
-- Dependencies: 394
-- Name: COLUMN pb2_pcm_req.created_time; Type: COMMENT; Schema: public; Owner: alfresco
--

COMMENT ON COLUMN pb2_pcm_req.created_time IS 'Created on';


--
-- TOC entry 3093 (class 0 OID 0)
-- Dependencies: 394
-- Name: COLUMN pb2_pcm_req.created_by; Type: COMMENT; Schema: public; Owner: alfresco
--

COMMENT ON COLUMN pb2_pcm_req.created_by IS 'Created by';


--
-- TOC entry 3094 (class 0 OID 0)
-- Dependencies: 394
-- Name: COLUMN pb2_pcm_req.updated_time; Type: COMMENT; Schema: public; Owner: alfresco
--

COMMENT ON COLUMN pb2_pcm_req.updated_time IS 'Last Updated on';


--
-- TOC entry 3095 (class 0 OID 0)
-- Dependencies: 394
-- Name: COLUMN pb2_pcm_req.updated_by; Type: COMMENT; Schema: public; Owner: alfresco
--

COMMENT ON COLUMN pb2_pcm_req.updated_by IS 'Last Updated by';


--
-- TOC entry 3096 (class 0 OID 0)
-- Dependencies: 394
-- Name: COLUMN pb2_pcm_req._objective_type; Type: COMMENT; Schema: public; Owner: alfresco
--

COMMENT ON COLUMN pb2_pcm_req._objective_type IS 'Objective Type';


--
-- TOC entry 3097 (class 0 OID 0)
-- Dependencies: 394
-- Name: COLUMN pb2_pcm_req.objective; Type: COMMENT; Schema: public; Owner: alfresco
--

COMMENT ON COLUMN pb2_pcm_req.objective IS 'Objective';


--
-- TOC entry 3098 (class 0 OID 0)
-- Dependencies: 394
-- Name: COLUMN pb2_pcm_req.reason; Type: COMMENT; Schema: public; Owner: alfresco
--

COMMENT ON COLUMN pb2_pcm_req.reason IS 'Reason';


--
-- TOC entry 3099 (class 0 OID 0)
-- Dependencies: 394
-- Name: COLUMN pb2_pcm_req.currency; Type: COMMENT; Schema: public; Owner: alfresco
--

COMMENT ON COLUMN pb2_pcm_req.currency IS 'Currency Unit';


--
-- TOC entry 3100 (class 0 OID 0)
-- Dependencies: 394
-- Name: COLUMN pb2_pcm_req.currency_rate; Type: COMMENT; Schema: public; Owner: alfresco
--

COMMENT ON COLUMN pb2_pcm_req.currency_rate IS 'Currency Rate';


--
-- TOC entry 3101 (class 0 OID 0)
-- Dependencies: 394
-- Name: COLUMN pb2_pcm_req.prototype_type; Type: COMMENT; Schema: public; Owner: alfresco
--

COMMENT ON COLUMN pb2_pcm_req.prototype_type IS 'Prototype';


--
-- TOC entry 3102 (class 0 OID 0)
-- Dependencies: 394
-- Name: COLUMN pb2_pcm_req.location; Type: COMMENT; Schema: public; Owner: alfresco
--

COMMENT ON COLUMN pb2_pcm_req.location IS 'Delivery Address';


--
-- TOC entry 3103 (class 0 OID 0)
-- Dependencies: 394
-- Name: COLUMN pb2_pcm_req.across_budget; Type: COMMENT; Schema: public; Owner: alfresco
--

COMMENT ON COLUMN pb2_pcm_req.across_budget IS 'Across Budget';


--
-- TOC entry 3104 (class 0 OID 0)
-- Dependencies: 394
-- Name: COLUMN pb2_pcm_req.ref_id; Type: COMMENT; Schema: public; Owner: alfresco
--

COMMENT ON COLUMN pb2_pcm_req.ref_id IS 'Reference ID';


--
-- TOC entry 3105 (class 0 OID 0)
-- Dependencies: 394
-- Name: COLUMN pb2_pcm_req.method_cond2_rule; Type: COMMENT; Schema: public; Owner: alfresco
--

COMMENT ON COLUMN pb2_pcm_req.method_cond2_rule IS 'Purchase Method Condition Rule';


--
-- TOC entry 3106 (class 0 OID 0)
-- Dependencies: 394
-- Name: COLUMN pb2_pcm_req.method_cond2; Type: COMMENT; Schema: public; Owner: alfresco
--

COMMENT ON COLUMN pb2_pcm_req.method_cond2 IS 'Purchase Method Condition';


--
-- TOC entry 3107 (class 0 OID 0)
-- Dependencies: 394
-- Name: COLUMN pb2_pcm_req.method_cond2_dtl; Type: COMMENT; Schema: public; Owner: alfresco
--

COMMENT ON COLUMN pb2_pcm_req.method_cond2_dtl IS 'Purchase Method Condition Detail';


--
-- TOC entry 3108 (class 0 OID 0)
-- Dependencies: 394
-- Name: COLUMN pb2_pcm_req.vat; Type: COMMENT; Schema: public; Owner: alfresco
--

COMMENT ON COLUMN pb2_pcm_req.vat IS 'Vat';


--
-- TOC entry 3109 (class 0 OID 0)
-- Dependencies: 394
-- Name: COLUMN pb2_pcm_req.vat_id; Type: COMMENT; Schema: public; Owner: alfresco
--

COMMENT ON COLUMN pb2_pcm_req.vat_id IS 'VAT ID';


--
-- TOC entry 3110 (class 0 OID 0)
-- Dependencies: 394
-- Name: COLUMN pb2_pcm_req.prototype_no; Type: COMMENT; Schema: public; Owner: alfresco
--

COMMENT ON COLUMN pb2_pcm_req.prototype_no IS 'Prototype Contract No.';


--
-- TOC entry 3111 (class 0 OID 0)
-- Dependencies: 394
-- Name: COLUMN pb2_pcm_req.budget_cc_type; Type: COMMENT; Schema: public; Owner: alfresco
--

COMMENT ON COLUMN pb2_pcm_req.budget_cc_type IS 'Type of budget (Unit/Project)';


--
-- TOC entry 3112 (class 0 OID 0)
-- Dependencies: 394
-- Name: COLUMN pb2_pcm_req.budget_cc; Type: COMMENT; Schema: public; Owner: alfresco
--

COMMENT ON COLUMN pb2_pcm_req.budget_cc IS 'Budget Section ID';


--
-- TOC entry 3113 (class 0 OID 0)
-- Dependencies: 394
-- Name: COLUMN pb2_pcm_req.pcm_section_id; Type: COMMENT; Schema: public; Owner: alfresco
--

COMMENT ON COLUMN pb2_pcm_req.pcm_section_id IS 'Procuement Section ID';


--
-- TOC entry 3114 (class 0 OID 0)
-- Dependencies: 394
-- Name: COLUMN pb2_pcm_req.req_section_id; Type: COMMENT; Schema: public; Owner: alfresco
--

COMMENT ON COLUMN pb2_pcm_req.req_section_id IS 'Requester Section ID';


--
-- TOC entry 3115 (class 0 OID 0)
-- Dependencies: 394
-- Name: COLUMN pb2_pcm_req.stock_section_id; Type: COMMENT; Schema: public; Owner: alfresco
--

COMMENT ON COLUMN pb2_pcm_req.stock_section_id IS 'Stock Section ID';


--
-- TOC entry 3116 (class 0 OID 0)
-- Dependencies: 394
-- Name: COLUMN pb2_pcm_req.cost_control_type_id; Type: COMMENT; Schema: public; Owner: alfresco
--

COMMENT ON COLUMN pb2_pcm_req.cost_control_type_id IS 'Cost Control Type';


--
-- TOC entry 3117 (class 0 OID 0)
-- Dependencies: 394
-- Name: COLUMN pb2_pcm_req.cost_control_id; Type: COMMENT; Schema: public; Owner: alfresco
--

COMMENT ON COLUMN pb2_pcm_req.cost_control_id IS 'Cost Control';


--
-- TOC entry 395 (class 1259 OID 26837)
-- Name: pb2_pcm_req_committee_dtl; Type: TABLE; Schema: public; Owner: alfresco
--

CREATE TABLE pb2_pcm_req_committee_dtl (
    id bigint NOT NULL,
    master_id bigint NOT NULL,
    first_name character varying,
    "position" character varying,
    created_time timestamp with time zone DEFAULT statement_timestamp(),
    created_by character varying(20),
    updated_time timestamp with time zone DEFAULT statement_timestamp(),
    updated_by character varying(20),
    last_name character varying,
    employee_code character varying,
    title character varying
);


ALTER TABLE pb2_pcm_req_committee_dtl OWNER TO alfresco;

--
-- TOC entry 3119 (class 0 OID 0)
-- Dependencies: 395
-- Name: COLUMN pb2_pcm_req_committee_dtl.master_id; Type: COMMENT; Schema: public; Owner: alfresco
--

COMMENT ON COLUMN pb2_pcm_req_committee_dtl.master_id IS 'Committee Header ID';


--
-- TOC entry 3120 (class 0 OID 0)
-- Dependencies: 395
-- Name: COLUMN pb2_pcm_req_committee_dtl.first_name; Type: COMMENT; Schema: public; Owner: alfresco
--

COMMENT ON COLUMN pb2_pcm_req_committee_dtl.first_name IS 'First Name';


--
-- TOC entry 3121 (class 0 OID 0)
-- Dependencies: 395
-- Name: COLUMN pb2_pcm_req_committee_dtl."position"; Type: COMMENT; Schema: public; Owner: alfresco
--

COMMENT ON COLUMN pb2_pcm_req_committee_dtl."position" IS 'Position';


--
-- TOC entry 3122 (class 0 OID 0)
-- Dependencies: 395
-- Name: COLUMN pb2_pcm_req_committee_dtl.created_time; Type: COMMENT; Schema: public; Owner: alfresco
--

COMMENT ON COLUMN pb2_pcm_req_committee_dtl.created_time IS 'Created On';


--
-- TOC entry 3123 (class 0 OID 0)
-- Dependencies: 395
-- Name: COLUMN pb2_pcm_req_committee_dtl.created_by; Type: COMMENT; Schema: public; Owner: alfresco
--

COMMENT ON COLUMN pb2_pcm_req_committee_dtl.created_by IS 'Created By';


--
-- TOC entry 3124 (class 0 OID 0)
-- Dependencies: 395
-- Name: COLUMN pb2_pcm_req_committee_dtl.updated_time; Type: COMMENT; Schema: public; Owner: alfresco
--

COMMENT ON COLUMN pb2_pcm_req_committee_dtl.updated_time IS 'Last Updated On';


--
-- TOC entry 3125 (class 0 OID 0)
-- Dependencies: 395
-- Name: COLUMN pb2_pcm_req_committee_dtl.updated_by; Type: COMMENT; Schema: public; Owner: alfresco
--

COMMENT ON COLUMN pb2_pcm_req_committee_dtl.updated_by IS 'Last Updated By';


--
-- TOC entry 3126 (class 0 OID 0)
-- Dependencies: 395
-- Name: COLUMN pb2_pcm_req_committee_dtl.last_name; Type: COMMENT; Schema: public; Owner: alfresco
--

COMMENT ON COLUMN pb2_pcm_req_committee_dtl.last_name IS 'Last Name';


--
-- TOC entry 396 (class 1259 OID 26845)
-- Name: pb2_pcm_req_committee_dtl_id_seq; Type: SEQUENCE; Schema: public; Owner: alfresco
--

CREATE SEQUENCE pb2_pcm_req_committee_dtl_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE pb2_pcm_req_committee_dtl_id_seq OWNER TO alfresco;

--
-- TOC entry 3128 (class 0 OID 0)
-- Dependencies: 396
-- Name: pb2_pcm_req_committee_dtl_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: alfresco
--

ALTER SEQUENCE pb2_pcm_req_committee_dtl_id_seq OWNED BY pb2_pcm_req_committee_dtl.id;


--
-- TOC entry 397 (class 1259 OID 26847)
-- Name: pb2_pcm_req_committee_hdr; Type: TABLE; Schema: public; Owner: alfresco
--

CREATE TABLE pb2_pcm_req_committee_hdr (
    id bigint NOT NULL,
    pcm_req_id character varying(50) NOT NULL,
    committee character varying(255),
    created_time timestamp with time zone DEFAULT statement_timestamp(),
    created_by character varying(20),
    updated_time timestamp with time zone DEFAULT statement_timestamp(),
    updated_by character varying(20),
    committee_id integer
);


ALTER TABLE pb2_pcm_req_committee_hdr OWNER TO alfresco;

--
-- TOC entry 3129 (class 0 OID 0)
-- Dependencies: 397
-- Name: COLUMN pb2_pcm_req_committee_hdr.pcm_req_id; Type: COMMENT; Schema: public; Owner: alfresco
--

COMMENT ON COLUMN pb2_pcm_req_committee_hdr.pcm_req_id IS 'PR Number';


--
-- TOC entry 3130 (class 0 OID 0)
-- Dependencies: 397
-- Name: COLUMN pb2_pcm_req_committee_hdr.committee; Type: COMMENT; Schema: public; Owner: alfresco
--

COMMENT ON COLUMN pb2_pcm_req_committee_hdr.committee IS 'Committee Name';


--
-- TOC entry 3131 (class 0 OID 0)
-- Dependencies: 397
-- Name: COLUMN pb2_pcm_req_committee_hdr.created_time; Type: COMMENT; Schema: public; Owner: alfresco
--

COMMENT ON COLUMN pb2_pcm_req_committee_hdr.created_time IS 'Created on';


--
-- TOC entry 3132 (class 0 OID 0)
-- Dependencies: 397
-- Name: COLUMN pb2_pcm_req_committee_hdr.created_by; Type: COMMENT; Schema: public; Owner: alfresco
--

COMMENT ON COLUMN pb2_pcm_req_committee_hdr.created_by IS 'Created by';


--
-- TOC entry 3133 (class 0 OID 0)
-- Dependencies: 397
-- Name: COLUMN pb2_pcm_req_committee_hdr.updated_time; Type: COMMENT; Schema: public; Owner: alfresco
--

COMMENT ON COLUMN pb2_pcm_req_committee_hdr.updated_time IS 'Last Updated on';


--
-- TOC entry 3134 (class 0 OID 0)
-- Dependencies: 397
-- Name: COLUMN pb2_pcm_req_committee_hdr.updated_by; Type: COMMENT; Schema: public; Owner: alfresco
--

COMMENT ON COLUMN pb2_pcm_req_committee_hdr.updated_by IS 'Last Updated by';


--
-- TOC entry 398 (class 1259 OID 26852)
-- Name: pb2_pcm_req_committee_hdr_id_seq; Type: SEQUENCE; Schema: public; Owner: alfresco
--

CREATE SEQUENCE pb2_pcm_req_committee_hdr_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE pb2_pcm_req_committee_hdr_id_seq OWNER TO alfresco;

--
-- TOC entry 3136 (class 0 OID 0)
-- Dependencies: 398
-- Name: pb2_pcm_req_committee_hdr_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: alfresco
--

ALTER SEQUENCE pb2_pcm_req_committee_hdr_id_seq OWNED BY pb2_pcm_req_committee_hdr.id;


--
-- TOC entry 399 (class 1259 OID 26854)
-- Name: pb2_pcm_req_dtl; Type: TABLE; Schema: public; Owner: alfresco
--

CREATE TABLE pb2_pcm_req_dtl (
    id bigint NOT NULL,
    master_id character varying(50) NOT NULL,
    description text,
    quantity double precision,
    created_time timestamp with time zone DEFAULT statement_timestamp(),
    created_by character varying(20),
    updated_time timestamp with time zone DEFAULT statement_timestamp(),
    updated_by character varying(20),
    price double precision,
    total double precision,
    unit_id integer,
    act_grp_id integer,
    fiscal_year integer,
    act_id integer,
    asset_rule_id integer
);


ALTER TABLE pb2_pcm_req_dtl OWNER TO alfresco;

--
-- TOC entry 3137 (class 0 OID 0)
-- Dependencies: 399
-- Name: COLUMN pb2_pcm_req_dtl.master_id; Type: COMMENT; Schema: public; Owner: alfresco
--

COMMENT ON COLUMN pb2_pcm_req_dtl.master_id IS 'PR Number';


--
-- TOC entry 3138 (class 0 OID 0)
-- Dependencies: 399
-- Name: COLUMN pb2_pcm_req_dtl.description; Type: COMMENT; Schema: public; Owner: alfresco
--

COMMENT ON COLUMN pb2_pcm_req_dtl.description IS 'Product';


--
-- TOC entry 3139 (class 0 OID 0)
-- Dependencies: 399
-- Name: COLUMN pb2_pcm_req_dtl.quantity; Type: COMMENT; Schema: public; Owner: alfresco
--

COMMENT ON COLUMN pb2_pcm_req_dtl.quantity IS 'Quantity';


--
-- TOC entry 3140 (class 0 OID 0)
-- Dependencies: 399
-- Name: COLUMN pb2_pcm_req_dtl.created_time; Type: COMMENT; Schema: public; Owner: alfresco
--

COMMENT ON COLUMN pb2_pcm_req_dtl.created_time IS 'Created on';


--
-- TOC entry 3141 (class 0 OID 0)
-- Dependencies: 399
-- Name: COLUMN pb2_pcm_req_dtl.created_by; Type: COMMENT; Schema: public; Owner: alfresco
--

COMMENT ON COLUMN pb2_pcm_req_dtl.created_by IS 'Created by';


--
-- TOC entry 3142 (class 0 OID 0)
-- Dependencies: 399
-- Name: COLUMN pb2_pcm_req_dtl.updated_time; Type: COMMENT; Schema: public; Owner: alfresco
--

COMMENT ON COLUMN pb2_pcm_req_dtl.updated_time IS 'Last Updated on';


--
-- TOC entry 3143 (class 0 OID 0)
-- Dependencies: 399
-- Name: COLUMN pb2_pcm_req_dtl.updated_by; Type: COMMENT; Schema: public; Owner: alfresco
--

COMMENT ON COLUMN pb2_pcm_req_dtl.updated_by IS 'Last Updated by';


--
-- TOC entry 3144 (class 0 OID 0)
-- Dependencies: 399
-- Name: COLUMN pb2_pcm_req_dtl.price; Type: COMMENT; Schema: public; Owner: alfresco
--

COMMENT ON COLUMN pb2_pcm_req_dtl.price IS 'Price';


--
-- TOC entry 3145 (class 0 OID 0)
-- Dependencies: 399
-- Name: COLUMN pb2_pcm_req_dtl.total; Type: COMMENT; Schema: public; Owner: alfresco
--

COMMENT ON COLUMN pb2_pcm_req_dtl.total IS 'Total';


--
-- TOC entry 400 (class 1259 OID 26862)
-- Name: pb2_pcm_req_dtl_id_seq; Type: SEQUENCE; Schema: public; Owner: alfresco
--

CREATE SEQUENCE pb2_pcm_req_dtl_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE pb2_pcm_req_dtl_id_seq OWNER TO alfresco;

--
-- TOC entry 3147 (class 0 OID 0)
-- Dependencies: 400
-- Name: pb2_pcm_req_dtl_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: alfresco
--

ALTER SEQUENCE pb2_pcm_req_dtl_id_seq OWNED BY pb2_pcm_req_dtl.id;


--
-- TOC entry 2836 (class 2604 OID 27158)
-- Name: id; Type: DEFAULT; Schema: public; Owner: alfresco
--

ALTER TABLE ONLY pb2_exp_brw_attendee ALTER COLUMN id SET DEFAULT nextval('pb2_exp_brw_attendee_id_seq'::regclass);


--
-- TOC entry 2805 (class 2604 OID 26864)
-- Name: id; Type: DEFAULT; Schema: public; Owner: alfresco
--

ALTER TABLE ONLY pb2_exp_brw_dtl ALTER COLUMN id SET DEFAULT nextval('pb2_exp_brw_dtl_id_seq'::regclass);


--
-- TOC entry 2839 (class 2604 OID 27183)
-- Name: id; Type: DEFAULT; Schema: public; Owner: alfresco
--

ALTER TABLE ONLY pb2_exp_use_attendee ALTER COLUMN id SET DEFAULT nextval('pb2_exp_use_attendee_id_seq'::regclass);


--
-- TOC entry 2816 (class 2604 OID 26865)
-- Name: id; Type: DEFAULT; Schema: public; Owner: alfresco
--

ALTER TABLE ONLY pb2_exp_use_dtl ALTER COLUMN id SET DEFAULT nextval('pb2_exp_use_dtl_id_seq'::regclass);


--
-- TOC entry 2817 (class 2604 OID 26866)
-- Name: id; Type: DEFAULT; Schema: public; Owner: alfresco
--

ALTER TABLE ONLY pb2_main_complete_notification ALTER COLUMN id SET DEFAULT nextval('pb2_main_complete_notification_id_seq'::regclass);


--
-- TOC entry 2834 (class 2604 OID 27131)
-- Name: id; Type: DEFAULT; Schema: public; Owner: alfresco
--

ALTER TABLE ONLY pb2_main_interface ALTER COLUMN id SET DEFAULT nextval('pb2_main_interface_id_seq'::regclass);


--
-- TOC entry 2808 (class 2604 OID 26867)
-- Name: id; Type: DEFAULT; Schema: public; Owner: alfresco
--

ALTER TABLE ONLY pb2_main_master ALTER COLUMN id SET DEFAULT nextval('pb2_main_master_id_seq'::regclass);


--
-- TOC entry 2809 (class 2604 OID 26868)
-- Name: id; Type: DEFAULT; Schema: public; Owner: alfresco
--

ALTER TABLE ONLY pb2_main_workflow ALTER COLUMN id SET DEFAULT nextval('pb2_main_workflow_id_seq'::regclass);


--
-- TOC entry 2818 (class 2604 OID 26869)
-- Name: id; Type: DEFAULT; Schema: public; Owner: alfresco
--

ALTER TABLE ONLY pb2_main_workflow_history ALTER COLUMN id SET DEFAULT nextval('pb2_main_workflow_history_id_seq'::regclass);


--
-- TOC entry 2810 (class 2604 OID 26870)
-- Name: id; Type: DEFAULT; Schema: public; Owner: alfresco
--

ALTER TABLE ONLY pb2_main_workflow_next_actor ALTER COLUMN id SET DEFAULT nextval('pb2_main_workflow_next_actor_id_seq'::regclass);


--
-- TOC entry 2811 (class 2604 OID 26871)
-- Name: id; Type: DEFAULT; Schema: public; Owner: alfresco
--

ALTER TABLE ONLY pb2_main_workflow_reviewer ALTER COLUMN id SET DEFAULT nextval('pb2_main_workflow_reviewer_id_seq'::regclass);


--
-- TOC entry 2825 (class 2604 OID 26872)
-- Name: id; Type: DEFAULT; Schema: public; Owner: alfresco
--

ALTER TABLE ONLY pb2_pcm_req_committee_dtl ALTER COLUMN id SET DEFAULT nextval('pb2_pcm_req_committee_dtl_id_seq'::regclass);


--
-- TOC entry 2828 (class 2604 OID 26873)
-- Name: id; Type: DEFAULT; Schema: public; Owner: alfresco
--

ALTER TABLE ONLY pb2_pcm_req_committee_hdr ALTER COLUMN id SET DEFAULT nextval('pb2_pcm_req_committee_hdr_id_seq'::regclass);


--
-- TOC entry 2831 (class 2604 OID 26874)
-- Name: id; Type: DEFAULT; Schema: public; Owner: alfresco
--

ALTER TABLE ONLY pb2_pcm_req_dtl ALTER COLUMN id SET DEFAULT nextval('pb2_pcm_req_dtl_id_seq'::regclass);


--
-- TOC entry 2912 (class 2606 OID 93082)
-- Name: pb2_main_workflow_assignee_pkey; Type: CONSTRAINT; Schema: public; Owner: alfresco
--

ALTER TABLE ONLY pb2_main_workflow_assignee
    ADD CONSTRAINT pb2_main_workflow_assignee_pkey PRIMARY KEY (id);


--
-- TOC entry 2850 (class 2606 OID 26878)
-- Name: pk_pb2_exp_brw; Type: CONSTRAINT; Schema: public; Owner: alfresco
--

ALTER TABLE ONLY pb2_exp_brw
    ADD CONSTRAINT pk_pb2_exp_brw PRIMARY KEY (id);


--
-- TOC entry 2908 (class 2606 OID 27165)
-- Name: pk_pb2_exp_brw_attendee; Type: CONSTRAINT; Schema: public; Owner: alfresco
--

ALTER TABLE ONLY pb2_exp_brw_attendee
    ADD CONSTRAINT pk_pb2_exp_brw_attendee PRIMARY KEY (id);


--
-- TOC entry 2852 (class 2606 OID 60028)
-- Name: pk_pb2_exp_brw_dtl; Type: CONSTRAINT; Schema: public; Owner: alfresco
--

ALTER TABLE ONLY pb2_exp_brw_dtl
    ADD CONSTRAINT pk_pb2_exp_brw_dtl PRIMARY KEY (id);


--
-- TOC entry 2876 (class 2606 OID 26882)
-- Name: pk_pb2_exp_use; Type: CONSTRAINT; Schema: public; Owner: alfresco
--

ALTER TABLE ONLY pb2_exp_use
    ADD CONSTRAINT pk_pb2_exp_use PRIMARY KEY (id);


--
-- TOC entry 2910 (class 2606 OID 27190)
-- Name: pk_pb2_exp_use_attendee; Type: CONSTRAINT; Schema: public; Owner: alfresco
--

ALTER TABLE ONLY pb2_exp_use_attendee
    ADD CONSTRAINT pk_pb2_exp_use_attendee PRIMARY KEY (id);


--
-- TOC entry 2878 (class 2606 OID 26884)
-- Name: pk_pb2_exp_use_dtl; Type: CONSTRAINT; Schema: public; Owner: alfresco
--

ALTER TABLE ONLY pb2_exp_use_dtl
    ADD CONSTRAINT pk_pb2_exp_use_dtl PRIMARY KEY (id);


--
-- TOC entry 2880 (class 2606 OID 26888)
-- Name: pk_pb2_main_complete_notification; Type: CONSTRAINT; Schema: public; Owner: alfresco
--

ALTER TABLE ONLY pb2_main_complete_notification
    ADD CONSTRAINT pk_pb2_main_complete_notification PRIMARY KEY (id);


--
-- TOC entry 2906 (class 2606 OID 27137)
-- Name: pk_pb2_main_interface; Type: CONSTRAINT; Schema: public; Owner: alfresco
--

ALTER TABLE ONLY pb2_main_interface
    ADD CONSTRAINT pk_pb2_main_interface PRIMARY KEY (id);


--
-- TOC entry 2856 (class 2606 OID 26876)
-- Name: pk_pb2_main_master; Type: CONSTRAINT; Schema: public; Owner: alfresco
--

ALTER TABLE ONLY pb2_main_master
    ADD CONSTRAINT pk_pb2_main_master PRIMARY KEY (id);


--
-- TOC entry 2862 (class 2606 OID 26904)
-- Name: pk_pb2_main_workflow; Type: CONSTRAINT; Schema: public; Owner: alfresco
--

ALTER TABLE ONLY pb2_main_workflow
    ADD CONSTRAINT pk_pb2_main_workflow PRIMARY KEY (id);


--
-- TOC entry 2882 (class 2606 OID 26906)
-- Name: pk_pb2_main_workflow_history; Type: CONSTRAINT; Schema: public; Owner: alfresco
--

ALTER TABLE ONLY pb2_main_workflow_history
    ADD CONSTRAINT pk_pb2_main_workflow_history PRIMARY KEY (id);


--
-- TOC entry 2865 (class 2606 OID 26890)
-- Name: pk_pb2_main_workflow_next_actor; Type: CONSTRAINT; Schema: public; Owner: alfresco
--

ALTER TABLE ONLY pb2_main_workflow_next_actor
    ADD CONSTRAINT pk_pb2_main_workflow_next_actor PRIMARY KEY (id);


--
-- TOC entry 2868 (class 2606 OID 26902)
-- Name: pk_pb2_main_workflow_reviewer; Type: CONSTRAINT; Schema: public; Owner: alfresco
--

ALTER TABLE ONLY pb2_main_workflow_reviewer
    ADD CONSTRAINT pk_pb2_main_workflow_reviewer PRIMARY KEY (id);


--
-- TOC entry 2887 (class 2606 OID 26892)
-- Name: pk_pb2_pcm_ord; Type: CONSTRAINT; Schema: public; Owner: alfresco
--

ALTER TABLE ONLY pb2_pcm_ord
    ADD CONSTRAINT pk_pb2_pcm_ord PRIMARY KEY (id);


--
-- TOC entry 2895 (class 2606 OID 26894)
-- Name: pk_pb2_pcm_req; Type: CONSTRAINT; Schema: public; Owner: alfresco
--

ALTER TABLE ONLY pb2_pcm_req
    ADD CONSTRAINT pk_pb2_pcm_req PRIMARY KEY (id);


--
-- TOC entry 2897 (class 2606 OID 26896)
-- Name: pk_pb2_pcm_req_committee_dtl; Type: CONSTRAINT; Schema: public; Owner: alfresco
--

ALTER TABLE ONLY pb2_pcm_req_committee_dtl
    ADD CONSTRAINT pk_pb2_pcm_req_committee_dtl PRIMARY KEY (id);


--
-- TOC entry 2899 (class 2606 OID 26898)
-- Name: pk_pb2_pcm_req_committee_hdr; Type: CONSTRAINT; Schema: public; Owner: alfresco
--

ALTER TABLE ONLY pb2_pcm_req_committee_hdr
    ADD CONSTRAINT pk_pb2_pcm_req_committee_hdr PRIMARY KEY (id);


--
-- TOC entry 2901 (class 2606 OID 26900)
-- Name: pk_pb2_pcm_req_dtl; Type: CONSTRAINT; Schema: public; Owner: alfresco
--

ALTER TABLE ONLY pb2_pcm_req_dtl
    ADD CONSTRAINT pk_pb2_pcm_req_dtl PRIMARY KEY (id);


--
-- TOC entry 2858 (class 2606 OID 26908)
-- Name: uq_pb2_main_master; Type: CONSTRAINT; Schema: public; Owner: alfresco
--

ALTER TABLE ONLY pb2_main_master
    ADD CONSTRAINT uq_pb2_main_master UNIQUE (type, code);


--
-- TOC entry 2843 (class 1259 OID 60015)
-- Name: inx_pb2_exp_brw_budget_cc; Type: INDEX; Schema: public; Owner: alfresco
--

CREATE INDEX inx_pb2_exp_brw_budget_cc ON public.pb2_exp_brw USING btree (budget_cc);


--
-- TOC entry 2844 (class 1259 OID 60016)
-- Name: inx_pb2_exp_brw_budget_cc_type; Type: INDEX; Schema: public; Owner: alfresco
--

CREATE INDEX inx_pb2_exp_brw_budget_cc_type ON public.pb2_exp_brw USING btree (budget_cc_type);


--
-- TOC entry 2845 (class 1259 OID 60017)
-- Name: inx_pb2_exp_brw_created_by; Type: INDEX; Schema: public; Owner: alfresco
--

CREATE INDEX inx_pb2_exp_brw_created_by ON public.pb2_exp_brw USING btree (created_by);


--
-- TOC entry 2846 (class 1259 OID 60018)
-- Name: inx_pb2_exp_brw_objective_type; Type: INDEX; Schema: public; Owner: alfresco
--

CREATE INDEX inx_pb2_exp_brw_objective_type ON public.pb2_exp_brw USING btree (objective_type);


--
-- TOC entry 2847 (class 1259 OID 60019)
-- Name: inx_pb2_exp_brw_req_by; Type: INDEX; Schema: public; Owner: alfresco
--

CREATE INDEX inx_pb2_exp_brw_req_by ON public.pb2_exp_brw USING btree (req_by);


--
-- TOC entry 2848 (class 1259 OID 60020)
-- Name: inx_pb2_exp_brw_status; Type: INDEX; Schema: public; Owner: alfresco
--

CREATE INDEX inx_pb2_exp_brw_status ON public.pb2_exp_brw USING btree (status);


--
-- TOC entry 2869 (class 1259 OID 60009)
-- Name: inx_pb2_exp_use_budget_cc; Type: INDEX; Schema: public; Owner: alfresco
--

CREATE INDEX inx_pb2_exp_use_budget_cc ON public.pb2_exp_use USING btree (budget_cc);


--
-- TOC entry 2870 (class 1259 OID 60010)
-- Name: inx_pb2_exp_use_budget_cc_type; Type: INDEX; Schema: public; Owner: alfresco
--

CREATE INDEX inx_pb2_exp_use_budget_cc_type ON public.pb2_exp_use USING btree (budget_cc_type);


--
-- TOC entry 2871 (class 1259 OID 60011)
-- Name: inx_pb2_exp_use_created_by; Type: INDEX; Schema: public; Owner: alfresco
--

CREATE INDEX inx_pb2_exp_use_created_by ON public.pb2_exp_use USING btree (created_by);


--
-- TOC entry 2872 (class 1259 OID 60012)
-- Name: inx_pb2_exp_use_pay_type; Type: INDEX; Schema: public; Owner: alfresco
--

CREATE INDEX inx_pb2_exp_use_pay_type ON public.pb2_exp_use USING btree (pay_type);


--
-- TOC entry 2873 (class 1259 OID 60013)
-- Name: inx_pb2_exp_use_req_by; Type: INDEX; Schema: public; Owner: alfresco
--

CREATE INDEX inx_pb2_exp_use_req_by ON public.pb2_exp_use USING btree (req_by);


--
-- TOC entry 2874 (class 1259 OID 60014)
-- Name: inx_pb2_exp_use_status; Type: INDEX; Schema: public; Owner: alfresco
--

CREATE INDEX inx_pb2_exp_use_status ON public.pb2_exp_use USING btree (status);


--
-- TOC entry 2902 (class 1259 OID 60025)
-- Name: inx_pb2_hr_salary_created_by; Type: INDEX; Schema: public; Owner: alfresco
--

CREATE INDEX inx_pb2_hr_salary_created_by ON public.pb2_hr_salary USING btree (created_by);


--
-- TOC entry 2903 (class 1259 OID 60024)
-- Name: inx_pb2_hr_salary_section_id; Type: INDEX; Schema: public; Owner: alfresco
--

CREATE INDEX inx_pb2_hr_salary_section_id ON public.pb2_hr_salary USING btree (section_id);


--
-- TOC entry 2904 (class 1259 OID 60026)
-- Name: inx_pb2_hr_salary_status; Type: INDEX; Schema: public; Owner: alfresco
--

CREATE INDEX inx_pb2_hr_salary_status ON public.pb2_hr_salary USING btree (status);


--
-- TOC entry 2853 (class 1259 OID 52032)
-- Name: inx_pb2_main_master_code; Type: INDEX; Schema: public; Owner: alfresco
--

CREATE INDEX inx_pb2_main_master_code ON public.pb2_main_master USING btree (code);


--
-- TOC entry 2854 (class 1259 OID 52033)
-- Name: inx_pb2_main_master_type; Type: INDEX; Schema: public; Owner: alfresco
--

CREATE INDEX inx_pb2_main_master_type ON public.pb2_main_master USING btree (type);


--
-- TOC entry 2859 (class 1259 OID 109270)
-- Name: inx_pb2_main_workflow_by; Type: INDEX; Schema: public; Owner: alfresco
--

CREATE INDEX inx_pb2_main_workflow_by ON public.pb2_main_workflow USING btree (by);


--
-- TOC entry 2860 (class 1259 OID 52029)
-- Name: inx_pb2_main_workflow_master_id; Type: INDEX; Schema: public; Owner: alfresco
--

CREATE INDEX inx_pb2_main_workflow_master_id ON public.pb2_main_workflow USING btree (master_id);


--
-- TOC entry 2863 (class 1259 OID 52034)
-- Name: inx_pb2_main_workflow_next_actor_master_id; Type: INDEX; Schema: public; Owner: alfresco
--

CREATE INDEX inx_pb2_main_workflow_next_actor_master_id ON public.pb2_main_workflow_next_actor USING btree (master_id);


--
-- TOC entry 2866 (class 1259 OID 52031)
-- Name: inx_pb2_main_workflow_reviewer_master_id; Type: INDEX; Schema: public; Owner: alfresco
--

CREATE INDEX inx_pb2_main_workflow_reviewer_master_id ON public.pb2_main_workflow_reviewer USING btree (master_id);


--
-- TOC entry 2883 (class 1259 OID 60022)
-- Name: inx_pb2_pcm_ord_created_by; Type: INDEX; Schema: public; Owner: alfresco
--

CREATE INDEX inx_pb2_pcm_ord_created_by ON public.pb2_pcm_ord USING btree (created_by);


--
-- TOC entry 2884 (class 1259 OID 60021)
-- Name: inx_pb2_pcm_ord_section_id; Type: INDEX; Schema: public; Owner: alfresco
--

CREATE INDEX inx_pb2_pcm_ord_section_id ON public.pb2_pcm_ord USING btree (section_id);


--
-- TOC entry 2885 (class 1259 OID 60023)
-- Name: inx_pb2_pcm_ord_status; Type: INDEX; Schema: public; Owner: alfresco
--

CREATE INDEX inx_pb2_pcm_ord_status ON public.pb2_pcm_ord USING btree (status);


--
-- TOC entry 2888 (class 1259 OID 52043)
-- Name: inx_pb2_pcm_req_budget_cc; Type: INDEX; Schema: public; Owner: alfresco
--

CREATE INDEX inx_pb2_pcm_req_budget_cc ON public.pb2_pcm_req USING btree (budget_cc);


--
-- TOC entry 2889 (class 1259 OID 52044)
-- Name: inx_pb2_pcm_req_budget_cc_type; Type: INDEX; Schema: public; Owner: alfresco
--

CREATE INDEX inx_pb2_pcm_req_budget_cc_type ON public.pb2_pcm_req USING btree (budget_cc_type);


--
-- TOC entry 2890 (class 1259 OID 52035)
-- Name: inx_pb2_pcm_req_created_by; Type: INDEX; Schema: public; Owner: alfresco
--

CREATE INDEX inx_pb2_pcm_req_created_by ON public.pb2_pcm_req USING btree (created_by);


--
-- TOC entry 2891 (class 1259 OID 52037)
-- Name: inx_pb2_pcm_req_objective_type; Type: INDEX; Schema: public; Owner: alfresco
--

CREATE INDEX inx_pb2_pcm_req_objective_type ON public.pb2_pcm_req USING btree (_objective_type);


--
-- TOC entry 2892 (class 1259 OID 52036)
-- Name: inx_pb2_pcm_req_req_by; Type: INDEX; Schema: public; Owner: alfresco
--

CREATE INDEX inx_pb2_pcm_req_req_by ON public.pb2_pcm_req USING btree (req_by);


--
-- TOC entry 2893 (class 1259 OID 52038)
-- Name: inx_pb2_pcm_req_status; Type: INDEX; Schema: public; Owner: alfresco
--

CREATE INDEX inx_pb2_pcm_req_status ON public.pb2_pcm_req USING btree (status);


--
-- TOC entry 2917 (class 2606 OID 27166)
-- Name: exp_brw_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: alfresco
--

ALTER TABLE ONLY pb2_exp_brw_attendee
    ADD CONSTRAINT exp_brw_id_fkey FOREIGN KEY (master_id) REFERENCES pb2_exp_brw(id);


--
-- TOC entry 2913 (class 2606 OID 26914)
-- Name: exp_use_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: alfresco
--

ALTER TABLE ONLY pb2_exp_use_dtl
    ADD CONSTRAINT exp_use_id_fkey FOREIGN KEY (master_id) REFERENCES pb2_exp_use(id);


--
-- TOC entry 2918 (class 2606 OID 27191)
-- Name: fk_pb2_exp_use_attendee_master_id; Type: FK CONSTRAINT; Schema: public; Owner: alfresco
--

ALTER TABLE ONLY pb2_exp_use_attendee
    ADD CONSTRAINT fk_pb2_exp_use_attendee_master_id FOREIGN KEY (master_id) REFERENCES pb2_exp_use(id);


--
-- TOC entry 2914 (class 2606 OID 26924)
-- Name: pcm_req_committee_hdr_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: alfresco
--

ALTER TABLE ONLY pb2_pcm_req_committee_dtl
    ADD CONSTRAINT pcm_req_committee_hdr_id_fkey FOREIGN KEY (master_id) REFERENCES pb2_pcm_req_committee_hdr(id);


--
-- TOC entry 2916 (class 2606 OID 26929)
-- Name: pcm_req_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: alfresco
--

ALTER TABLE ONLY pb2_pcm_req_dtl
    ADD CONSTRAINT pcm_req_id_fkey FOREIGN KEY (master_id) REFERENCES pb2_pcm_req(id);


--
-- TOC entry 2915 (class 2606 OID 26934)
-- Name: pcm_req_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: alfresco
--

ALTER TABLE ONLY pb2_pcm_req_committee_hdr
    ADD CONSTRAINT pcm_req_id_fkey FOREIGN KEY (pcm_req_id) REFERENCES pb2_pcm_req(id);


--
-- TOC entry 3057 (class 0 OID 0)
-- Dependencies: 376
-- Name: pb2_exp_brw; Type: ACL; Schema: public; Owner: alfresco
--

REVOKE ALL ON TABLE pb2_exp_brw FROM PUBLIC;
REVOKE ALL ON TABLE pb2_exp_brw FROM alfresco;
GRANT ALL ON TABLE pb2_exp_brw TO alfresco;
GRANT SELECT ON TABLE pb2_exp_brw TO etl_pabi2;


--
-- TOC entry 3058 (class 0 OID 0)
-- Dependencies: 434
-- Name: pb2_exp_brw_attendee; Type: ACL; Schema: public; Owner: alfresco
--

REVOKE ALL ON TABLE pb2_exp_brw_attendee FROM PUBLIC;
REVOKE ALL ON TABLE pb2_exp_brw_attendee FROM alfresco;
GRANT ALL ON TABLE pb2_exp_brw_attendee TO alfresco;
GRANT SELECT ON TABLE pb2_exp_brw_attendee TO etl_pabi2;


--
-- TOC entry 3060 (class 0 OID 0)
-- Dependencies: 377
-- Name: pb2_exp_brw_dtl; Type: ACL; Schema: public; Owner: alfresco
--

REVOKE ALL ON TABLE pb2_exp_brw_dtl FROM PUBLIC;
REVOKE ALL ON TABLE pb2_exp_brw_dtl FROM alfresco;
GRANT ALL ON TABLE pb2_exp_brw_dtl TO alfresco;
GRANT SELECT ON TABLE pb2_exp_brw_dtl TO etl_pabi2;


--
-- TOC entry 3062 (class 0 OID 0)
-- Dependencies: 379
-- Name: pb2_main_master; Type: ACL; Schema: public; Owner: alfresco
--

REVOKE ALL ON TABLE pb2_main_master FROM PUBLIC;
REVOKE ALL ON TABLE pb2_main_master FROM alfresco;
GRANT ALL ON TABLE pb2_main_master TO alfresco;
GRANT SELECT ON TABLE pb2_main_master TO etl_pabi2;


--
-- TOC entry 3063 (class 0 OID 0)
-- Dependencies: 380
-- Name: pb2_main_workflow; Type: ACL; Schema: public; Owner: alfresco
--

REVOKE ALL ON TABLE pb2_main_workflow FROM PUBLIC;
REVOKE ALL ON TABLE pb2_main_workflow FROM alfresco;
GRANT ALL ON TABLE pb2_main_workflow TO alfresco;
GRANT SELECT ON TABLE pb2_main_workflow TO intraymon;
GRANT SELECT ON TABLE pb2_main_workflow TO etl_pabi2;


--
-- TOC entry 3064 (class 0 OID 0)
-- Dependencies: 381
-- Name: pb2_main_workflow_next_actor; Type: ACL; Schema: public; Owner: alfresco
--

REVOKE ALL ON TABLE pb2_main_workflow_next_actor FROM PUBLIC;
REVOKE ALL ON TABLE pb2_main_workflow_next_actor FROM alfresco;
GRANT ALL ON TABLE pb2_main_workflow_next_actor TO alfresco;
GRANT SELECT ON TABLE pb2_main_workflow_next_actor TO etl_pabi2;


--
-- TOC entry 3065 (class 0 OID 0)
-- Dependencies: 382
-- Name: pb2_main_workflow_reviewer; Type: ACL; Schema: public; Owner: alfresco
--

REVOKE ALL ON TABLE pb2_main_workflow_reviewer FROM PUBLIC;
REVOKE ALL ON TABLE pb2_main_workflow_reviewer FROM alfresco;
GRANT ALL ON TABLE pb2_main_workflow_reviewer TO alfresco;
GRANT SELECT ON TABLE pb2_main_workflow_reviewer TO etl_pabi2;


--
-- TOC entry 3066 (class 0 OID 0)
-- Dependencies: 383
-- Name: pb2_exp_use; Type: ACL; Schema: public; Owner: alfresco
--

REVOKE ALL ON TABLE pb2_exp_use FROM PUBLIC;
REVOKE ALL ON TABLE pb2_exp_use FROM alfresco;
GRANT ALL ON TABLE pb2_exp_use TO alfresco;
GRANT SELECT ON TABLE pb2_exp_use TO etl_pabi2;


--
-- TOC entry 3067 (class 0 OID 0)
-- Dependencies: 439
-- Name: pb2_exp_use_attendee; Type: ACL; Schema: public; Owner: alfresco
--

REVOKE ALL ON TABLE pb2_exp_use_attendee FROM PUBLIC;
REVOKE ALL ON TABLE pb2_exp_use_attendee FROM alfresco;
GRANT ALL ON TABLE pb2_exp_use_attendee TO alfresco;
GRANT SELECT ON TABLE pb2_exp_use_attendee TO etl_pabi2;


--
-- TOC entry 3069 (class 0 OID 0)
-- Dependencies: 384
-- Name: pb2_exp_use_dtl; Type: ACL; Schema: public; Owner: alfresco
--

REVOKE ALL ON TABLE pb2_exp_use_dtl FROM PUBLIC;
REVOKE ALL ON TABLE pb2_exp_use_dtl FROM alfresco;
GRANT ALL ON TABLE pb2_exp_use_dtl TO alfresco;
GRANT SELECT ON TABLE pb2_exp_use_dtl TO etl_pabi2;


--
-- TOC entry 3071 (class 0 OID 0)
-- Dependencies: 419
-- Name: pb2_hr_salary; Type: ACL; Schema: public; Owner: alfresco
--

REVOKE ALL ON TABLE pb2_hr_salary FROM PUBLIC;
REVOKE ALL ON TABLE pb2_hr_salary FROM alfresco;
GRANT ALL ON TABLE pb2_hr_salary TO alfresco;
GRANT SELECT ON TABLE pb2_hr_salary TO etl_pabi2;


--
-- TOC entry 3072 (class 0 OID 0)
-- Dependencies: 385
-- Name: pb2_main_complete_notification; Type: ACL; Schema: public; Owner: alfresco
--

REVOKE ALL ON TABLE pb2_main_complete_notification FROM PUBLIC;
REVOKE ALL ON TABLE pb2_main_complete_notification FROM alfresco;
GRANT ALL ON TABLE pb2_main_complete_notification TO alfresco;
GRANT SELECT ON TABLE pb2_main_complete_notification TO etl_pabi2;


--
-- TOC entry 3074 (class 0 OID 0)
-- Dependencies: 432
-- Name: pb2_main_interface; Type: ACL; Schema: public; Owner: alfresco
--

REVOKE ALL ON TABLE pb2_main_interface FROM PUBLIC;
REVOKE ALL ON TABLE pb2_main_interface FROM alfresco;
GRANT ALL ON TABLE pb2_main_interface TO alfresco;
GRANT SELECT ON TABLE pb2_main_interface TO etl_pabi2;


--
-- TOC entry 3077 (class 0 OID 0)
-- Dependencies: 388
-- Name: pb2_main_msg; Type: ACL; Schema: public; Owner: alfresco
--

REVOKE ALL ON TABLE pb2_main_msg FROM PUBLIC;
REVOKE ALL ON TABLE pb2_main_msg FROM alfresco;
GRANT ALL ON TABLE pb2_main_msg TO alfresco;
GRANT SELECT ON TABLE pb2_main_msg TO etl_pabi2;


--
-- TOC entry 3078 (class 0 OID 0)
-- Dependencies: 446
-- Name: pb2_main_workflow_assignee; Type: ACL; Schema: public; Owner: alfresco
--

REVOKE ALL ON TABLE pb2_main_workflow_assignee FROM PUBLIC;
REVOKE ALL ON TABLE pb2_main_workflow_assignee FROM alfresco;
GRANT ALL ON TABLE pb2_main_workflow_assignee TO alfresco;
GRANT SELECT ON TABLE pb2_main_workflow_assignee TO etl_pabi2;


--
-- TOC entry 3079 (class 0 OID 0)
-- Dependencies: 389
-- Name: pb2_main_workflow_history; Type: ACL; Schema: public; Owner: alfresco
--

REVOKE ALL ON TABLE pb2_main_workflow_history FROM PUBLIC;
REVOKE ALL ON TABLE pb2_main_workflow_history FROM alfresco;
GRANT ALL ON TABLE pb2_main_workflow_history TO alfresco;
GRANT SELECT ON TABLE pb2_main_workflow_history TO intraymon;
GRANT SELECT ON TABLE pb2_main_workflow_history TO etl_pabi2;


--
-- TOC entry 3084 (class 0 OID 0)
-- Dependencies: 393
-- Name: pb2_pcm_ord; Type: ACL; Schema: public; Owner: alfresco
--

REVOKE ALL ON TABLE pb2_pcm_ord FROM PUBLIC;
REVOKE ALL ON TABLE pb2_pcm_ord FROM alfresco;
GRANT ALL ON TABLE pb2_pcm_ord TO alfresco;
GRANT SELECT ON TABLE pb2_pcm_ord TO etl_pabi2;


--
-- TOC entry 3118 (class 0 OID 0)
-- Dependencies: 394
-- Name: pb2_pcm_req; Type: ACL; Schema: public; Owner: alfresco
--

REVOKE ALL ON TABLE pb2_pcm_req FROM PUBLIC;
REVOKE ALL ON TABLE pb2_pcm_req FROM alfresco;
GRANT ALL ON TABLE pb2_pcm_req TO alfresco;
GRANT SELECT ON TABLE pb2_pcm_req TO etl_pabi2;


--
-- TOC entry 3127 (class 0 OID 0)
-- Dependencies: 395
-- Name: pb2_pcm_req_committee_dtl; Type: ACL; Schema: public; Owner: alfresco
--

REVOKE ALL ON TABLE pb2_pcm_req_committee_dtl FROM PUBLIC;
REVOKE ALL ON TABLE pb2_pcm_req_committee_dtl FROM alfresco;
GRANT ALL ON TABLE pb2_pcm_req_committee_dtl TO alfresco;
GRANT SELECT ON TABLE pb2_pcm_req_committee_dtl TO etl_pabi2;


--
-- TOC entry 3135 (class 0 OID 0)
-- Dependencies: 397
-- Name: pb2_pcm_req_committee_hdr; Type: ACL; Schema: public; Owner: alfresco
--

REVOKE ALL ON TABLE pb2_pcm_req_committee_hdr FROM PUBLIC;
REVOKE ALL ON TABLE pb2_pcm_req_committee_hdr FROM alfresco;
GRANT ALL ON TABLE pb2_pcm_req_committee_hdr TO alfresco;
GRANT SELECT ON TABLE pb2_pcm_req_committee_hdr TO etl_pabi2;


--
-- TOC entry 3146 (class 0 OID 0)
-- Dependencies: 399
-- Name: pb2_pcm_req_dtl; Type: ACL; Schema: public; Owner: alfresco
--

REVOKE ALL ON TABLE pb2_pcm_req_dtl FROM PUBLIC;
REVOKE ALL ON TABLE pb2_pcm_req_dtl FROM alfresco;
GRANT ALL ON TABLE pb2_pcm_req_dtl TO alfresco;
GRANT SELECT ON TABLE pb2_pcm_req_dtl TO etl_pabi2;


-- Completed on 2019-01-03 09:55:44

CREATE SEQUENCE public.pb2_exp_brw_id_seq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;
ALTER TABLE public.pb2_exp_brw_id_seq
  OWNER TO alfresco;

CREATE SEQUENCE public.pb2_exp_use_id_seq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;
ALTER TABLE public.pb2_exp_use_id_seq
  OWNER TO alfresco;

  
CREATE SEQUENCE public.pb2_pcm_req_id_seq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;
ALTER TABLE public.pb2_pcm_req_id_seq
  OWNER TO alfresco;
--
-- PostgreSQL database dump complete
--

