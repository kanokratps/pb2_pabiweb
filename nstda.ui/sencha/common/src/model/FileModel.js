Ext.define('PB.model.common.FileModel', {
    extend: 'Ext.data.Model',
    fields : [
    		   {name : 'name'}
    		 , {name : 'desc'}    		   
    		 , {name : 'path'}
    		 , {name : 'nodeRef'}
    		 , {name : 'action'}
    ],
    idProperty:'name'
});