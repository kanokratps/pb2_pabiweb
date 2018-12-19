Ext.define('PBExp.store.GridStore', {
    extend: 'Ext.data.Store',
    model: 'PBExp.model.GridModel',
    autoLoad:true,
    pageSize:PAGE_SIZE,
    remoteSort:true,

    proxy: {
        type: 'ajax',
        timeout: GRID_TIME_OUT,
        api: {
        	read: ALF_CONTEXT+'/exp/brw/list'
        },
        reader: {
            type: 'json',
            root: 'data',
            successProperty: 'success'
        }
    }
});