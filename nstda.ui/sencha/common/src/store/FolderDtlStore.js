Ext.define('PB.store.common.FolderDtlStore', {
    extend: 'Ext.data.Store',
    model: 'PB.model.common.FolderDtlModel',
    autoLoad:false,
    pageSize:PAGE_SIZE,

    proxy: {
        type: 'ajax',
        timeout: GRID_TIME_OUT,
        api: {
        	read: ALF_CONTEXT+"/util/getFolderDtl"
        },
        reader: {
            type: 'json',
            root: 'data',
            successProperty: 'success'
        }
    }
});