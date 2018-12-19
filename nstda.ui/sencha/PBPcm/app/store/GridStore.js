Ext.define('PBPcm.store.GridStore', {
    extend: 'Ext.data.Store',
    model: 'PBPcm.model.GridModel',
    autoLoad:true,
    pageSize:PAGE_SIZE,
    remoteSort:true,

    proxy: {
        type: 'ajax',
        timeout: GRID_TIME_OUT,
        api: {
        	read: ALF_CONTEXT+'/pcm/req/list'
        },
        reader: {
            type: 'json',
            root: 'data',
            successProperty: 'success'
        }
    }
});