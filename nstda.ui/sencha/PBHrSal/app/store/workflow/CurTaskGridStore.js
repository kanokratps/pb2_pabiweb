Ext.define('PBHrSal.store.workflow.CurTaskGridStore', {
    extend: 'Ext.data.Store',
    model: 'PBHrSal.model.workflow.CurTaskGridModel',
    autoLoad:false,
    pageSize:PAGE_SIZE,

    proxy: {
        type: 'ajax',
        timeout: GRID_TIME_OUT,
        api: {
        	read: ALF_CONTEXT+'/hr/sal/wf/task/list'
        },
        reader: {
            type: 'json',
            root: 'data',
            successProperty: 'success'
        }
    }
});