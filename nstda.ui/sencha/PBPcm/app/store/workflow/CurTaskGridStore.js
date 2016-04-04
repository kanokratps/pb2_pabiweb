Ext.define('PBPcm.store.workflow.CurTaskGridStore', {
    extend: 'Ext.data.Store',
    model: 'PBPcm.model.workflow.CurTaskGridModel',
    autoLoad:false,
    pageSize:PAGE_SIZE,

    proxy: {
        type: 'ajax',
        timeout: GRID_TIME_OUT,
        api: {
        	read: ALF_CONTEXT+'/pcm/wf/task/list'
        },
        reader: {
            type: 'json',
            root: 'data',
            successProperty: 'success'
        }
    }
});