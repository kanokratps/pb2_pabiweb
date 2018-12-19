Ext.define('PBExp.model.item.ActivityGridModel', {
    extend: 'Ext.data.Model',
    fields : [ {name : 'id'}
    		 , {name : 'activity_id'}
    		 , {name : 'activity_name'}
    		 , {name : 'condition_1'}
    		 , {name : 'condition_2'}
    		 , {name : 'position'}
    		 , {name : 'uom'}
    		 , {name : 'amount'}
    		 , {name : 'action'}
    ]
});