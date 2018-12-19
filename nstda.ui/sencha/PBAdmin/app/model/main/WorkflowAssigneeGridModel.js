Ext.define('PBAdmin.model.main.WorkflowAssigneeGridModel', {
    extend: 'Ext.data.Model',
    fields : [ {name : 'id'}
			 , {name : 'src'}
			 , {name : 'dest'}
			 , {name : 'assignedTime'}
    		 , {name : 'active'}
    ]
});