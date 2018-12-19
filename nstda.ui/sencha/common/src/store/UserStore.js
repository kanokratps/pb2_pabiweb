Ext.define('PB.store.common.UserStore', {
    extend: 'Ext.data.Store',
    model: 'PB.model.common.UserModel',
    autoLoad:false,
    pageSize:PAGE_SIZE,

    proxy: {
        type: 'ajax',
        timeout: GRID_TIME_OUT,
        api: {
        	read : ALF_CONTEXT + "/admin/main/user/list"
        },
        reader: {
            type: 'json',
            root: 'data',
            successProperty: 'success'
        }
    }
});