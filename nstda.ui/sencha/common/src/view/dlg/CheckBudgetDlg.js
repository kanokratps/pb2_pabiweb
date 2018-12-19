Ext.define('PB.view.common.CheckBudgetDlg', {
	extend : 'Ext.window.Window',
    alias:'widget.checkBudgetDlg',
    
	initComponent: function(config) {
		var me = this;
		
		var lbw = 180;
		
		Ext.applyIf(me, {
	        renderTo : Ext.getBody(),
	        modal: true,
	        width: 700,
	        height: 200,
	        resizable: false,
	        layout:'fit',
	        items : [{
	        	xtype : 'form',
		        border : 0,
		        items:[{
		        	xtype:'textfield',
		        	itemId:'name',
		        	fieldLabel:PB.Label.b.name,
		        	labelWidth:lbw,
		        	anchor:"-5",
		        	margin:'5 5 5 5',
					readOnly:true,
				    fieldStyle:'background-color: #ddd; background-image:none;',
				    value:me.rec.name
		        },{
		        	xtype:'textfield',
		        	itemId:'balance',
		        	fieldLabel:PB.Label.cb.balance,
		        	labelWidth:lbw,
		        	anchor:"-5",
		        	margin:'5 5 5 5',
					readOnly:true,
					fieldStyle:'background-color: #ddd; background-image:none;text-align:right;',
				    value:me.rec.balance
		        },{
		        	xtype:'textfield',
		        	itemId:'preAppBudget',
		        	fieldLabel:PB.Label.cb.preAppBudget,
		        	labelWidth:lbw,
		        	anchor:"-5",
		        	margin:'5 5 5 5',
					readOnly:true,
				    fieldStyle:'background-color: #ddd; background-image:none;text-align:right;',
				    value:me.rec.preAppBudget
		        },{
		        	xtype:'textfield',
		        	itemId:'expBalance',
		        	fieldLabel:PB.Label.cb.expBalance,
		        	labelWidth:lbw,
		        	anchor:"-5",
		        	margin:'5 5 5 5',
					readOnly:true,
					fieldStyle:'background-color: #ddd; background-image:none;text-align:right;',
				    value:me.rec.expBalance
		        }]
	        }],
	        buttons : [{
	          text: PB.Label.m.ok,
	          iconCls:'icon_ok',
	          handler:this.destroy,
	          scope:this
	        }]
		});
		
        this.callParent(arguments);
		
	}
});