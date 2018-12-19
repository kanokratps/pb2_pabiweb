Ext.define('PBExpUse.view.Main', {
    extend: 'Ext.tab.Panel',
    requires:[
        'PBExpUse.view.MainGrid',
        'PBExpUse.view.MainForm'
    ],
    
    alias : 'widget.expUseMain',

	initComponent: function(config) {
		var me = this;
		
		var items = [];
		
		var store = Ext.create('PBExpUse.store.GridStore',{
			storeId:'expUseGridStore',
			autoLoad:false
		});
		
		store.getProxy().extraParams = {
			lang : getLang()
		}
		
		if (!ID) {
			items.push({
				xtype:'expUseMainGrid',
				title:PB.Label.m.search,
				store:store
			});
			
//			if (me.tasks.expUseRptTab) {
//				items.push({
//					xtype:'reportForm',
//					title:'Report'
//				});
//			}
		}
		
		Ext.applyIf(me, {
			
			border : 0,
	
			items:items
				
		});		
		
	    this.callParent(arguments);
	}
});