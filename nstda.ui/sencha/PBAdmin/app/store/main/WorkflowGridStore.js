Ext.define('PBAdmin.store.main.WorkflowGridStore', {
    extend: 'Ext.data.Store',
    model: 'PBAdmin.model.main.WorkflowGridModel',
    autoLoad:false,
    pageSize:PAGE_SIZE,
    remoteSort:true,

    proxy: {
        type: 'ajax',
        timeout: GRID_TIME_OUT,
        api: {
        	read: ALF_CONTEXT+'/admin/main/workflow/list'
        },
        reader: {
            type: 'json',
            root: 'data',
            successProperty: 'success'
        }
    }
});