Ext.define('PB.controller.common.User', {
    extend: 'Ext.app.Controller',

    refs:[{
    	ref:'dlg',
    	selector:'searchUserDlg'
    }],
    
    init:function() {
		var me = this;
		
		me.control({
			'searchUserDlg button': {
				searchUser : me.search
			},
			'searchUserDlg textfield': {
				searchUser : me.search
			},
			'searchUserDlg button[action=ok]':{
				confirmUser:me.ok
			}
			
		});
	},
	
	getGrid:function(sender) {
		return sender.up("window").down("grid");
	},
	
	getSearchTerm:function(sender) {
		return sender.up("container").down("field[itemId=searchTerm]");
	},
	
    search : function(sender) {
		var me = this;
	
		var store = me.getGrid(sender).getStore();
		
		store.getProxy().extraParams = {
			s:me.getSearchTerm(sender).getValue(),
			lang:getLang()
		}
		
		store.load();
	},
	
	ok:function(btn) {
		var me = this;
		
		if (me.getDlg().multiSelect) {
			
			var grid = me.getGrid(btn);
			var selModel = grid.getSelectionModel();
			var selected = selModel.selected;
			
			if (!me.getDlg().validateCallback || me.getDlg().validateCallback(selected.items)) {
				me.getDlg().callback(selected.items);
				me.getDlg().close();
			}
			
		} else {
		
			var id = getRadioValue("id");
			
			var store = me.getGrid(btn).getStore();
			var rec;
			
			for(var i=0; i<store.getCount(); i++) {
				var r = store.getAt(i);
				if (r.get('id') == id) {
					rec = r;
				}
			}
			
			me.getDlg().callback(id, rec);
			me.getDlg().close();
		}
	}

});