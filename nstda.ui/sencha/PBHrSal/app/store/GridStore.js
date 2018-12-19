Ext.define('PBHrSal.store.GridStore', {
    extend: 'Ext.data.Store',
    model: 'PBHrSal.model.GridModel',
    autoLoad:true,
    pageSize:PAGE_SIZE,
    remoteSort:true,

    proxy: {
        type: 'ajax',
        timeout: GRID_TIME_OUT,
        api: {
        	read: ALF_CONTEXT+'/hr/sal/list'
        },
        reader: {
            type: 'json',
            root: 'data',
            successProperty: 'success'
        }
    }
});