Ext.define('PBPcm.model.workflow.CurTaskGridModel', {
    extend: 'Ext.data.Model',
    fields : [ {name : 'id'}
    		 , {name : 'type'}
    		 , {name : 'assignedTo'}
    		 , {name : 'status'}
    ]
});