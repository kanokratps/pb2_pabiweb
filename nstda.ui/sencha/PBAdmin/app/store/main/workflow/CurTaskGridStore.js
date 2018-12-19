Ext.define('PBAdmin.store.main.workflow.CurTaskGridStore', {
    extend: 'Ext.data.Store',
    model: 'PBAdmin.model.main.workflow.CurTaskGridModel',
    autoLoad:false,
    pageSize:PAGE_SIZE,

    proxy: {
        type: 'ajax',
        timeout: GRID_TIME_OUT,
        api: {
        	read: ALF_CONTEXT+'/admin/wf/task/list'
        },
        reader: {
            type: 'json',
            root: 'data',
            successProperty: 'success'
        }
    }
});