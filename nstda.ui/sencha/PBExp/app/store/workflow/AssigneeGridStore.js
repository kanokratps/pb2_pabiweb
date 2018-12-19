Ext.define('PBExp.store.workflow.AssigneeGridStore', {
    extend: 'Ext.data.Store',
    model: 'PBExp.model.workflow.AssigneeGridModel',
    autoLoad:false,
    pageSize:PAGE_SIZE,

    proxy: {
        type: 'ajax',
        timeout: GRID_TIME_OUT,
        api: {
        	read: ALF_CONTEXT+'/exp/wf/assignee/list'
        },
        reader: {
            type: 'json',
            root: 'data',
            successProperty: 'success'
        }
    }
});