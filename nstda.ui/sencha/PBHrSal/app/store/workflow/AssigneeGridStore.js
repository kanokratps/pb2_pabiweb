Ext.define('PBHrSal.store.workflow.AssigneeGridStore', {
    extend: 'Ext.data.Store',
    model: 'PBHrSal.model.workflow.AssigneeGridModel',
    autoLoad:false,
    pageSize:PAGE_SIZE,

    proxy: {
        type: 'ajax',
        timeout: GRID_TIME_OUT,
        api: {
        	read: ALF_CONTEXT+'/hr/sal/wf/assignee/list'
        },
        reader: {
            type: 'json',
            root: 'data',
            successProperty: 'success'
        }
    }
});