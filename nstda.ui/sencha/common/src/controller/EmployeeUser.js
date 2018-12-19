Ext.define('PB.controller.common.EmployeeUser', {
    extend: 'Ext.app.Controller',

    refs:[{
    	ref:'dlg',
    	selector:'searchEmployeeUserDlg'
    }],
    
    init:function() {
		var me = this;
		
		me.control({
			'searchEmployeeUserDlg': {
				selectRadio : me.selectRadio,
				selectDestination : me.selectDestination
			},
			'searchEmployeeUserDlg button': {
				searchUser : me.search
			},
			'searchEmployeeUserDlg textfield': {
				searchUser : me.search
			},
			'searchEmployeeUserDlg button[action=ok]':{
				confirm:me.ok
			}
			
		});
	},
	
	getGrid:function(sender) {
		return sender.up("window").down("grid");
	},
	
	getType:function(sender) {
		return sender.up("window").down("field[name=type]");
	},	
	
	getCode:function(sender) {
		return sender.up("window").down("field[itemId=code]");
	},
	
	getSearchTerm:function(sender) {
		return sender.up("container").down("field[itemId=searchTerm]");
	},
	
	getDestination:function(sender) {
		return sender.up("window").down("field[name=destination]");
	},
	
	getRoute:function(sender) {
		return sender.up("window").down("field[name=route]");
	},
	
	getAmount:function(sender) {
		return sender.up("window").down("field[name=amount]");
	},
	
	getDepart:function(sender) {
		return sender.up("window").down("field[name=depart]");
	},
	
	getArrive:function(sender) {
		return sender.up("window").down("field[name=arrive]");
	},
	
	getCls:function(sender) {
		return sender.up("window").down("field[name=cls]");
	},
	
	getDestType:function(sender) {
		return sender.up("window").down("field[name=destType]");
	},
	
	getDestination:function(sender) {
		return sender.up("window").down("field[itemId=destination]");
	},
	
    search : function(sender) {
		var me = this;
	
		var store = me.getGrid(sender).getStore();
		
//		var c = me.getCode(sender).getValue();
//		var pos = c.indexOf("|");
		
		store.getProxy().extraParams = {
//            t:me.getType(sender).getGroupValue(),
//            c:c.substring(0,pos),
			s:me.getSearchTerm(sender).getValue(),
			lang:getLang()
		}
		
		store.load();
	},	
	
	ok:function(btn) {
		var me = this;
		
		var grid = me.getGrid(btn);
		var selModel = grid.getSelectionModel();
		var selected = selModel.selected;
		
		if (me.getDlg().needFootPrint) {
			for(var a in selected.items) {
				var item = selected.items[a];
				
				item.data.desttype = me.getDestType(btn).getGroupValue();
				item.data.destination = me.getDestination(btn).getValue();
				item.data.route = me.getRoute(btn).getValue();
				item.data.depart = me.getDepart(btn).getValue();
				item.data.arrive = me.getArrive(btn).getValue();
				item.data.cls = me.getCls(btn).getValue();
				item.data.amount = me.getAmount(btn).getValue();
			}
		}
		
		if (!me.getDlg().validateCallback || me.getDlg().validateCallback(selected.items)) {
			me.getDlg().callback(selected.items);
			me.getDlg().close();
		}
	},
	
	selectRadio:function(rad, v) {
		var me = this;
		var type = v ? "U" : "P";

		var store = me.getCode(rad).getStore();
		store.getProxy().extraParams = {
			t:type
		}
		store.load();
	},

	selectDestination:function(rad, v) {
		var me = this;
		var type = v ? "D" : "I";

		var store = me.getDestination(rad).getStore();
		store.getProxy().extraParams = {
			t:type
		}
		store.load();
	}

});