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
 * 		- AttendeeTab
 * 		- FileTab
 */
Ext.define('PBExp.view.Main', {
    extend: 'Ext.tab.Panel',
    requires:[
        'PBExp.view.MainGrid',
        'PBExp.view.MainForm'
    ],
    
    alias : 'widget.expBrwMain',

	initComponent: function(config) {
		var me = this;
		
		var items = [];
		
		var store = Ext.create('PBExp.store.GridStore',{
			storeId:'expBrwGridStore',
			autoLoad:false
		});
		
		store.getProxy().extraParams = {
			lang : getLang()
		}
		
		if (!ID) {
			items.push({
				xtype:'expBrwMainGrid',
				title:PB.Label.m.search,
				store:store
			});
			
//			if (me.tasks.expBrwRptTab) {
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