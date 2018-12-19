Ext.define('PBExp.store.AttendeeGridStore', {
    extend: 'Ext.data.Store',
    model: 'PBExp.model.AttendeeGridModel',
    autoLoad:false,
    pageSize:PAGE_SIZE,

    proxy: {
        type: 'ajax',
        timeout: GRID_TIME_OUT,
        api: {
        	read: ALF_CONTEXT+'/exp/brw/attendee/list'
        },
        reader: {
            type: 'json',
            root: 'data',
            successProperty: 'success'
        }
    }
});