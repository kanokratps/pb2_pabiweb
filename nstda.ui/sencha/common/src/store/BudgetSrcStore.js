Ext.define('PB.store.common.BudgetSrcStore', {
    extend: 'Ext.data.Store',
    model: 'PB.model.common.BudgetSrcModel',
    autoLoad:false,
    pageSize:PAGE_SIZE,

    proxy: {
        type: 'ajax',
        timeout: GRID_TIME_OUT,
        api: {
        	read : ALF_CONTEXT + "/admin/main/budgetSrc/list"
        },
        reader: {
            type: 'json',
            root: 'data',
            successProperty: 'success'
        }
    }
});