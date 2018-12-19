Ext.define('PBExpUse.store.OldGridStore', {
    extend: 'Ext.data.Store',
    model: 'PBExpUse.model.OldGridModel',
    autoLoad:false,
    pageSize:PAGE_SIZE,

    proxy: {
        type: 'ajax',
        timeout: GRID_TIME_OUT,
        api: {
        	read: ALF_CONTEXT+'/exp/brw/old/list'
        },
        reader: {
            type: 'json',
            root: 'data',
            successProperty: 'success'
        }
    }
});