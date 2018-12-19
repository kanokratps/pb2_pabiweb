Ext.define('PBAdmin.view.main.testSystem.Main', {
    extend: 'Ext.panel.Panel',
    alias:'widget.adminMainTestSystemMain',

	initComponent: function(config) {
		var me = this;
		
		Ext.applyIf(me, {
			
			layout:'fit',
			
			items:[{
				xtype:'textarea'
			}],
			
			tbar : [{
                text: "Current User",
                iconCls: "icon_ok",                
                action: "currentUser"
			},{
                text: "Version",
                iconCls: "icon_ok",                
                action: "version"
            }]
				
		});		
		
	    this.callParent(arguments);
		
	}
    
});
