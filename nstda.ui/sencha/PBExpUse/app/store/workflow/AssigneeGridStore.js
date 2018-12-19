Ext.define('PBExpUse.store.workflow.AssigneeGridStore', {
    extend: 'Ext.data.Store',
    model: 'PBExpUse.model.workflow.AssigneeGridModel',
    autoLoad:false,
    pageSize:PAGE_SIZE,

    proxy: {
        type: 'ajax',
        timeout: GRID_TIME_OUT,
        api: {
        	read: ALF_CONTEXT+'/exp/use/wf/assignee/list'
        },
        reader: {
            type: 'json',
            root: 'data',
            successProperty: 'success'
        }
    }
});