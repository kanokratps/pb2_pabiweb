Ext.define('PB.store.common.EmployeeUserStore', {
    extend: 'Ext.data.Store',
    model: 'PB.model.common.EmployeeUserModel',
    autoLoad:false,
    pageSize:PAGE_SIZE,

    proxy: {
        type: 'ajax',
        timeout: GRID_TIME_OUT,
        api: {
        	read : ALF_CONTEXT + "/admin/main/employee/list"
        },
        reader: {
            type: 'json',
            root: 'data',
            successProperty: 'success'
        }
    }
});