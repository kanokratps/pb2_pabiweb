Ext.define('PB.model.common.FolderDtlModel', {
    extend: 'Ext.data.Model',
    fields : [
    		   {name : 'name'}
    		 , {name : 'desc'}
    		 , {name : 'path'}
    		 , {name : 'nodeRef'}
    		 , {name : 'by'}
    		 , {name : 'time'}
    		 , {name : 'action'}
    ],
    idProperty:'name'
});