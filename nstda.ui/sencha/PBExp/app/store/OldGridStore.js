Ext.define('PBExp.store.OldGridStore', {
    extend: 'Ext.data.Store',
    model: 'PBExp.model.OldGridModel',
    autoLoad:true,
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