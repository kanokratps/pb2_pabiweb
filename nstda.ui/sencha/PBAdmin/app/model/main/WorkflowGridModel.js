Ext.define('PBAdmin.model.main.WorkflowGridModel', {
    extend: 'Ext.data.Model',
    fields : [ {name : 'id'}
			 , {name : 'wfid'}
			 , {name : 'no'}
			 , {name : 'desc'}
			 , {name : 'assignee'}
    		 , {name : 'requester'}
    		 , {name : 'preparer'}
    		 , {name : 'reqdate'}
    		 , {name : 'remark'}
    		 , {name : 'status'}
    ]
});