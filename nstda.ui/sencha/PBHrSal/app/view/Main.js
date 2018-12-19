/*
 * - MainGrid
 */
Ext.define('PBHrSal.view.Main', {
    extend: 'Ext.tab.Panel',
    requires:[
        'PBHrSal.view.MainGrid'
    ],
    
    alias : 'widget.hrSalMain',

	initComponent: function(config) {
		var me = this;
		
		var items = [];
		
		var store = Ext.create('PBHrSal.store.GridStore',{
			storeId:'hrSalGridStore',
			autoLoad:false
		});
	
		store.getProxy().extraParams = {
			lang : getLang()
		}
		
		if (!ID) {
			items.push({
				xtype:'hrSalMainGrid',
				title:PB.Label.m.search,
				store:store
			});
			/*
			items.push({
				xtype:'hrSalReportForm',
				title:'Report'
			});
			*/
		}
		
		Ext.applyIf(me, {
			
			border : 0,
	
			items:items
				
		});		
		
	    this.callParent(arguments);
	}

});