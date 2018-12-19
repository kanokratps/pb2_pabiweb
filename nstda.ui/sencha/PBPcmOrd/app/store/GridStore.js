Ext.define('PBPcmOrd.store.GridStore', {
    extend: 'Ext.data.Store',
    model: 'PBPcmOrd.model.GridModel',
    autoLoad:true,
    pageSize:PAGE_SIZE,
    remoteSort:true,

    proxy: {
        type: 'ajax',
        timeout: GRID_TIME_OUT,
        api: {
        	read: ALF_CONTEXT+'/pcm/ord/list'
        },
        reader: {
            type: 'json',
            root: 'data',
            successProperty: 'success'
        }
    }
});