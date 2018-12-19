/*
 * - MainGrid
 * 		- workflow
 * 			- DtlDlg
 * 				- AssigneeGrid
 * 				- CurTaskGrid
 * 				- DtlGrid
 * - MainForm
 * 		- UserTab
 * 		- InfoTab
 * 		- ItemTab
 * 		- FileTab
 * 		- CmtTab
 */
Ext.define('PBPcm.view.Main', {
    extend: 'Ext.tab.Panel',
    requires:[
        'PBPcm.view.MainGrid',
        'PBPcm.view.MainForm'
    ],

    alias : 'widget.pcmReqMain',

	initComponent: function(config) {
		var me = this;
		
		var items = [];
		
		var store = Ext.create('PBPcm.store.GridStore',{
			storeId:'pcmReqGridStore',
			autoLoad:false
		});

		store.getProxy().extraParams = {
			lang : getLang()
		}
		
		if (!ID) {
			items.push({
				xtype:'pcmReqMainGrid',
				title:PB.Label.m.search,
				store:store
			});
			
//			if (me.tasks.pcmReqRptTab) {
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