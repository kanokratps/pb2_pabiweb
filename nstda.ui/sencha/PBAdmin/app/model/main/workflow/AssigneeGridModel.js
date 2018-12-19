Ext.define('PBAdmin.model.main.workflow.AssigneeGridModel', {
    extend: 'Ext.data.Model',
    fields : [ {name : 'id'}
    		 , {name : 'assignee'}
    		 , {name : 'user'}
    		 , {name : 'current'}
    		 , {name : 'color'}
    ]
});