Ext.define('PBExpUse.store.ItemGridStore', {
    extend: 'Ext.data.Store',
    model: 'PBExpUse.model.ItemGridModel',
    autoLoad:false,
    pageSize:PAGE_SIZE,

    proxy: {
        type: 'ajax',
        timeout: GRID_TIME_OUT,
        api: {
        	read: ALF_CONTEXT+'/exp/use/item/list'
        },
        reader: {
            type: 'json',
            root: 'data',
            successProperty: 'success'
        }
    }
});