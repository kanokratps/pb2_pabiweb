Ext.define('PBExpUse.store.OldComboStore', {
    extend: 'Ext.data.Store',
    model: 'PBExpUse.model.OldComboModel',
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