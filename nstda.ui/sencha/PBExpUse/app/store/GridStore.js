Ext.define('PBExpUse.store.GridStore', {
    extend: 'Ext.data.Store',
    model: 'PBExpUse.model.GridModel',
    autoLoad:true,
    pageSize:PAGE_SIZE,
    remoteSort:true,

    proxy: {
        type: 'ajax',
        timeout: GRID_TIME_OUT,
        api: {
        	read: ALF_CONTEXT+'/exp/use/list'
        },
        reader: {
            type: 'json',
            root: 'data',
            successProperty: 'success'
        }
    }
});