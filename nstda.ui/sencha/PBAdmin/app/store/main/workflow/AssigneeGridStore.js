Ext.define('PBAdmin.store.main.workflow.AssigneeGridStore', {
    extend: 'Ext.data.Store',
    model: 'PBAdmin.model.main.workflow.AssigneeGridModel',
    autoLoad:false,
    pageSize:PAGE_SIZE,

    proxy: {
        type: 'ajax',
        timeout: GRID_TIME_OUT,
        api: {
        	read: ALF_CONTEXT+'/admin/main/wf/assignee/list'
        },
        reader: {
            type: 'json',
            root: 'data',
            successProperty: 'success'
        }
    }
});