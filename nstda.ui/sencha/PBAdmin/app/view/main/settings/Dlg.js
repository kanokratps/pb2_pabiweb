Ext.define('PBAdmin.view.main.settings.Dlg', {
	extend : 'Ext.window.Window',
	alias : 'widget.adminMainSettingsDlg',
	
	initComponent: function(config) {
		var me = this;
		
		Ext.applyIf(me, {
		        renderTo : Ext.getBody(),
	            modal: true,
	            width: 450,
	            height: 400,
	            layout: 'fit',
	            resizable: false,
	            items : [{
		        	xtype : 'form',
			        itemId : 'formSettings',
			        border : 0,
			        layout:'anchor',
			        buttons : [{
			          text: 'บันทึก', 
//			          disabled : true,
			          action : 'ok',
			          itemId: 'okButton'
			        },{
			          text: 'ยกเลิก',
			          handler:this.destroy,
			          scope:this
			        }]	            
			    }]
		});
		
        this.callParent(arguments);
	}
});		