Ext.define('PBExp.model.workflow.DtlGridModel', {
    extend: 'Ext.data.Model',
    fields : [ {name : 'id'}
    		 , {name : 'time'}
    		 , {name : 'by'}
    		 , {name : 'status'}
    		 , {name : 'task'}
    		 , {name : 'comment'}
    ]
});