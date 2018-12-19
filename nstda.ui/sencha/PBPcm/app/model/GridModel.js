Ext.define('PBPcm.model.GridModel', {
    extend: 'Ext.data.Model',
    fields : [ {name : 'id'}
    		 , {name : 'req_by'}
    		 , {name : 'req_ou'}
    		 , {name : 'objective_type_name'}
    		 , {name : 'objective_type'}
    		 , {name : 'objective'}
    		 , {name : 'currency'}
    		 , {name : 'total'}
    		 , {name : 'workflow_ins_id'}
    		 , {name : 'created_time_show'}
    		 , {name : 'requested_time_show'}
    		 , {name : 'is_small_amount'}
    		 , {name : 'budget_cc'}
    		 , {name : 'budget_cc_name'}
    		 , {name : 'fund_id'}
    		 , {name : 'fund_name'}
    		 , {name : 'doc_ref'}
    		 , {name : 'file_name'}
    		 , {name : 'folder_ref'}
    		 , {name : 'status'}
    		 , {name : 'wfstatus'}
    		 , {name : 'created_by'}
    		 , {name : 'updated_by'}
    		 , {name : 'updated_time'}
    		 , {name : 'overdue'}
    		 , {name : 'action'}
    ]
});