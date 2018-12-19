Ext.define('PBAdmin.view.main.log.Main', {
    extend: 'Ext.panel.Panel',
    alias:'widget.adminMainLogMain',

	initComponent: function(config) {
		var me = this;
		
		Ext.applyIf(me, {
			
			layout:'fit',
			
			items:[{
				xtype:'textarea'
			}],
			
			tbar : [{
				xtype:'numberfield',
				fieldLabel:'Range',
				labelWidth:50,
				name:'from',
				value:'1',
				width:140,
				margin:'0 0 0 5'
			},{
				xtype:'numberfield',
				fieldLabel:' - ',
				labelSeparator:'',
				labelWidth:10,
				name:'to',
				value:'100',
				width:100
			},{
                text: "Refresh",
	            iconCls: "icon_ok",                
	            action: "range"
			},{
				xtype:'numberfield',
				fieldLabel:'Last Line Count',
				name:'line',
				value:100,
				width:180,
				margin:'0 0 0 10'
			},{
                text: "Refresh",
                iconCls: "icon_ok",                
                action: "last"
			},{
                text: "Info",
                iconCls: "icon_ok",                
                action: "info"
            },{
				xtype:'label',
				name:'info',
				margin:'12 0 0 5'
            }]
            
		});		
		
	    this.callParent(arguments);
		
	}
    
});
