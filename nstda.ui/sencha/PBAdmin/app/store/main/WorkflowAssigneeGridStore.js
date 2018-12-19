Ext.define('PBAdmin.store.main.WorkflowAssigneeGridStore', {
    extend: 'Ext.data.Store',
    model: 'PBAdmin.model.main.WorkflowAssigneeGridModel',
    autoLoad:false,
    pageSize:PAGE_SIZE,
    remoteSort:true,

    proxy: {
        type: 'ajax',
        timeout: GRID_TIME_OUT,
        api: {
        	read: ALF_CONTEXT+'/admin/main/workflow/assignee/list'
        },
        reader: {
            type: 'json',
            root: 'data',
            successProperty: 'success'
        }
    }
});