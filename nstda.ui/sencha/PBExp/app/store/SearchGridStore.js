Ext.define('PBExp.store.SearchGridStore', {
    extend: 'Ext.data.Store',
    model: 'PBExp.model.GridModel',
    autoLoad:true,
    pageSize:PAGE_SIZE,

    proxy: {
        type: 'ajax',
        timeout: GRID_TIME_OUT,
        api: {
        	read: ALF_CONTEXT+'/exp/brw/listForSearch'
        },
        reader: {
            type: 'json',
            root: 'data',
            successProperty: 'success'
        }
    }
});