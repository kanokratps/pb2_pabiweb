Ext.define('PB.view.common.EditFileDlg', {
	extend : 'Ext.window.Window',
    alias:'widget.editFileDlg',
    
	initComponent: function(config) {
		var me = this;
		
		var lbw = 100;
		
		Ext.applyIf(me, {
	        renderTo : Ext.getBody(),
	        modal: true,
	        width: 490,
	        height: 200,
	        resizable: false,
	        layout:'fit',
	        items : [{
	        	xtype : 'form',
		        border : 0,
		        items:[{
			        	xtype:'textfield',
			        	itemId:'desc',
			        	fieldLabel:PB.Label.u.desc,
			        	labelWidth:lbw,
			        	anchor:"-5",
			        	margin:'5 5 5 5',
		            	enableKeyEvents: true,
		            	hasfocus:true,
			           	listeners: {
			 	 			specialkey: function(field, e){
			 	 				if(e.getKey() == e.ENTER){
			 	 					this.fireEvent("confirmFile",this, me.rec, me.grid);
			 	 				}
			 	 			},
			 	 			afterrender: function(txt) {
								Ext.defer(function(){
									txt.focus();
								},100);
								this.setHeight(22);
			 	 			}
			           	},
			           	value:me.rec.get('desc'),
						maxLength:255
		        }]
	        }],
	        buttons : [{
	          text: PB.Label.m.btnConfirm, 
	          action : 'ok',
	          iconCls:'icon_ok',
	          listeners: {
	               click: function(){
	                	this.fireEvent("confirmFile", this, me.rec, me.grid);
	               }
	          }
	        },{
	          text: PB.Label.m.cancel,
	          iconCls:'icon_no',
	          handler:this.destroy,
	          scope:this
	        }]
		});
		
        this.callParent(arguments);
		
	}
});