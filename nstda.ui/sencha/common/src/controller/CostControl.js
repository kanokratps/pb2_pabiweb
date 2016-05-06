Ext.define('PB.controller.common.CostControl', {
    extend: 'Ext.app.Controller',

    refs:[{
    	ref:'dlg',
    	selector:'searchCostControlDlg'
    }],

    init:function() {
		var me = this;
		
		this.control({
			'button': {
				searchCostControl : me.search
			},
			'textfield': {
				searchCostControl : me.search
			},
			'searchCostControlDlg button[action=ok]':{
				confirmCostControl: me.ok
			}
		});
	},
	
	getGrid:function(sender) {
		return sender.up("window").down("grid");
	},
	
	getSearchTerm:function(sender) {
		return sender.up("container").down("field[itemId=searchTerm]");
	},
	
	getType:function(sender) {
		return sender.up("window").down("field[name=type]");
	},
	
    search : function(sender) {
		var me = this;
	
    	var store = me.getGrid(sender).getStore();
    	
    	store.getProxy().extraParams = {
    			s:me.getSearchTerm(sender).getValue(),
    			t:me.getType(sender).getValue()
    	}
    	
    	store.load();
	},
	
	ok:function(btn) {
		var me = this;
		
		var ids = getRadioValue("id").split("|");
		
		var cmbType = me.getType(btn); 
		var type = cmbType.getValue();
		var typeName;
		cmbType.getStore().each(function(rec){
			if (rec.get("id")==type) {
				typeName = rec.get("name");
			}
		});
		
		me.getDlg().callback(ids, type, typeName);
		me.getDlg().close();
	}

});