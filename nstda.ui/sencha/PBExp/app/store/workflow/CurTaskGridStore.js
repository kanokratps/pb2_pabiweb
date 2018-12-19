Ext.define('PBExp.store.workflow.CurTaskGridStore', {
    extend: 'Ext.data.Store',
    model: 'PBExp.model.workflow.CurTaskGridModel',
    autoLoad:false,
    pageSize:PAGE_SIZE,

    proxy: {
        type: 'ajax',
        timeout: GRID_TIME_OUT,
        api: {
        	read: ALF_CONTEXT+'/exp/wf/task/list'
        },
        reader: {
            type: 'json',
            root: 'data',
            successProperty: 'success'
        }
    }
});