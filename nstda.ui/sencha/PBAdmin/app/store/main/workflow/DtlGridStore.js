Ext.define('PBAdmin.store.main.workflow.DtlGridStore', {
    extend: 'Ext.data.Store',
    model: 'PBAdmin.model.main.workflow.DtlGridModel',
    autoLoad:false,
    pageSize:PAGE_SIZE,

    proxy: {
        type: 'ajax',
        timeout: GRID_TIME_OUT,
        api: {
        	read: ALF_CONTEXT+'/admin/main/wf/dtl/list'
        },
        reader: {
            type: 'json',
            root: 'data',
            successProperty: 'success'
        }
    }
});