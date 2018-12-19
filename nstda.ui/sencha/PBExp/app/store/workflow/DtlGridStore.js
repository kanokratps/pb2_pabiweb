Ext.define('PBExp.store.workflow.DtlGridStore', {
    extend: 'Ext.data.Store',
    model: 'PBExp.model.workflow.DtlGridModel',
    autoLoad:false,
    pageSize:PAGE_SIZE,

    proxy: {
        type: 'ajax',
        timeout: GRID_TIME_OUT,
        api: {
        	read: ALF_CONTEXT+'/exp/wf/dtl/list'
        },
        reader: {
            type: 'json',
            root: 'data',
            successProperty: 'success'
        }
    }
});