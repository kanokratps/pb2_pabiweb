Ext.define('PBExpUse.store.SearchGridStore', {
    extend: 'Ext.data.Store',
    model: 'PBExpUse.model.GridModel',
    autoLoad:true,
    pageSize:PAGE_SIZE,

    proxy: {
        type: 'ajax',
        timeout: GRID_TIME_OUT,
        api: {
        	read: ALF_CONTEXT+'/exp/use/listForSearch'
        },
        reader: {
            type: 'json',
            root: 'data',
            successProperty: 'success'
        }
    }
});