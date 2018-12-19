Ext.define('PB.controller.common.BudgetSrc', {
    extend: 'Ext.app.Controller',

    refs:[{
    	ref:'dlg',
    	selector:'searchBudgetSrcDlg'
    }],
    
    init:function() {
		var me = this;
		
		me.control({
			'searchBudgetSrcDlg button': {
				searchBudgetSrc : me.search
			},
			'searchBudgetSrcDlg textfield': {
				searchBudgetSrc : me.search
			},
			'searchBudgetSrcDlg button[action=ok]':{
				confirmBudgetSrc:me.ok
			},
			'searchBudgetSrcDlg grid':{
				select:me.selectRow
			},
			'searchBudgetSrcDlg':{
				selectRadio:me.selectRadio,
				loadFund:me.loadFund
			}
		});
		

	},
	
	getGrid:function(sender) {
		return sender.up("window").down("grid");
	},
	
	getSearchTerm:function(sender) {
		return sender.up("window").down("field[itemId=searchTerm]");
	},
	
	getType:function(sender) {
		return sender.up("window").down("field[name=type]");
	},
	
	getFund:function(sender) {
		return sender.up("window").down("field[name=fund]");
	},
	
    search : function(sender, v) {
		var me = this;
	
		var store = me.getGrid(sender).getStore();
		
		store.getProxy().extraParams = {
			s:me.getSearchTerm(sender).getValue(),
			t:v ? v : me.getType(sender).getGroupValue(),
			lang:getLang(),
			onlySectProj:sender.up("window").onlySectProj
		}
		
		store.load();
	},
	
	ok:function(btn) {
		var me = this;
		
		var type = me.getType(btn).getGroupValue();

		if (validForm(me.getDlg().down("panel[itemId=southPanel]"))) {
			var ids = getRadioValue("id").split("|");
			var t = {"U":PB.Label.m.section,"P":PB.Label.m.project,"A":PB.Label.b.invAsset,"C":PB.Label.b.constr};
			var typeName = t[type];
			var fund = me.getFund(btn).getValue();
			var fundName = me.getFund(btn).getRawValue();
			
			me.getDlg().callback(ids, type, typeName, fund, fundName);
			me.getDlg().close();
		}
	},
	
	selectRadio:function(rad, v) {
		var grid = this.getDlg().down("grid");
		var store = grid.getStore();
		var columns;
		if(v=="U") {
//			grid.headerCt.getHeaderAtIndex(1).setText("ศูนย์");
//			grid.headerCt.getHeaderAtIndex(2).setText("หน่วยงาน");
			
			columns = [
	        	     { text:'', dataIndex: 'id', width: 50, align:'center', renderer:
	        	    	 function(v) {
	        	    		return '<input type="radio" name="id" value="'+v+'"/>'; 
	        	    	 }
	        	     },
	        	     { text:PB.Label.m.org, dataIndex: 'org', width: 70 },
	        	     { text:PB.Label.b.sectShort, dataIndex: 'type', width: 85 },
	        	     { text:PB.Label.b.sectName, dataIndex: 'name', flex:1 }
	        ];
			
	    }
	    else
		if (v=="P") {
//			grid.headerCt.getHeaderAtIndex(1).setText("รหัส");
//			grid.headerCt.getHeaderAtIndex(2).setText("ชื่อ");
			columns = [
	        	     { text:'', dataIndex: 'id', width: 50, align:'center', renderer:
	        	    	 function(v) {
	        	    		return '<input type="radio" name="id" value="'+v+'"/>'; 
	        	    	 }
	        	     },
	        	     { text:PB.Label.m.org, dataIndex: 'org', width: 70 },
	        	     { text:PB.Label.b.projName, dataIndex: 'name', flex:1 },
	        	     { text:PB.Label.b.pm, dataIndex: 'cc', flex:1 }
	        ];
		}
	    else
		if (v=="A") {
			columns = [
	        	     { text:'', dataIndex: 'id', width: 50, align:'center', renderer:
	        	    	 function(v) {
	        	    		return '<input type="radio" name="id" value="'+v+'"/>'; 
	        	    	 }
	        	     },
	        	     { text:PB.Label.m.org, dataIndex: 'org', width: 100 },
	        	     { text:PB.Label.b.asset, dataIndex: 'name', flex:1 },
	        	     { text:PB.Label.b.costcenter, dataIndex: 'cc', width: 100 },
	        	     { text:PB.Label.b.objective, dataIndex: 'type', flex:1 }
	        ];
		}
	    else
		if (v=="C") {
			columns = [
	        	     { text:'', dataIndex: 'id', width: 50, align:'center', renderer:
	        	    	 function(v) {
	        	    		return '<input type="radio" name="id" value="'+v+'"/>'; 
	        	    	 }
	        	     },
	        	     { text:PB.Label.m.org, dataIndex: 'org', width: 100 },
	        	     { text:PB.Label.b.cpnp, dataIndex: 'name', flex:1 },
	        	     { text:PB.Label.b.costcenter, dataIndex: 'cc', width:100 }
	        ];
		}
		
		grid.reconfigure(undefined,columns);
		this.search(grid, "Z");
		
		var cmbFund = this.getDlg().down("field[name=fund]");
		cmbFund.setValue(null);
		var store = cmbFund.getStore();
		store.load();
	},
	
	selectRow:function(grid, rec) {
		var me = this;
	
		var id = rec.data.id.split("|")[0];
		
		var type = me.getDlg().down("field[name=type]").getGroupValue();
		var store = me.getDlg().down("field[name=fund]").getStore();
		
		store.load({params:{
			t:type,
			id:id
		}});
	},
	
	loadFund:function(store, rs) {
		var cmbFund = this.getDlg().down("field[name=fund]");
	
		if (rs.length == 1) {
			cmbFund.setValue(store.first().data.id);
		}
	}

});