Ext.define('PBAdmin.view.main.util.Main', {
    extend: 'Ext.panel.Panel',
    alias:'widget.adminMainUtilMain',

	initComponent: function(config) {
		var me = this;
		
		Ext.applyIf(me, {
			
			layout:'fit',
			
			items:[{
				xtype:'textarea'
			}],
			
			tbar : [{
                text: "Add User to Group",
                iconCls: "icon_ok",                
                action: "addUserToGroup"
            }]
				
		});		
		
	    this.callParent(arguments);
		
	}
    
});
