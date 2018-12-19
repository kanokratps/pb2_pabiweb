Ext.define('PBPcm.store.SearchGridStore', {
    extend: 'Ext.data.Store',
    model: 'PBPcm.model.GridModel',
    autoLoad:true,
    pageSize:PAGE_SIZE,

    proxy: {
        type: 'ajax',
        timeout: GRID_TIME_OUT,
        api: {
        	read: ALF_CONTEXT+'/pcm/req/listForSearch'
        },
        reader: {
            type: 'json',
            root: 'data',
            successProperty: 'success'
        }
    }
});