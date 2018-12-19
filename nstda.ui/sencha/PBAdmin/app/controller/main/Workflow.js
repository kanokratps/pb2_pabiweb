Ext.define('PBAdmin.controller.main.Workflow', {
    extend: 'Ext.app.Controller',

    refs:[{
        ref:'main',
        selector: 'adminMainWorkflowMain'
	},{
        ref:'grid',
        selector: 'adminMainWorkflowMain grid[itemId=wfGrid]'
	},{
        ref:'agrid',
        selector: 'adminMainWorkflowMain grid[itemId=wfaGrid]'
	},{
    	ref:'txtSearch',
		selector: 'adminMainWorkflowMain [itemId=txtSearch]'
	},{
	    ref:'cmbWfType',
	    selector: 'adminMainWorkflowMain combo[name=wfType]'
	},{
	    ref:'txtEmployee',
	    selector: 'adminMainWorkflowMain [name=employee]'
	},{
	    ref:'chkIsMyTask',
	    selector: 'adminMainWorkflowMain [name=isMyTask]'
	},{
	    ref:'cmbEType',
	    selector: 'adminMainWorkflowMain [name=eType]'
	},{
	    ref:'txtAssignee',
	    selector: 'adminMainWorkflowMain [name=assignee]'
	},{
	    ref:'txtAssigneeName',
	    selector: 'adminMainWorkflowMain [name=assigneeName]'
	},{
	    ref:'txtSrcAssignee',
	    selector: 'adminMainWorkflowMain [name=srcAssignee]'
	},{
	    ref:'txtSrcAssigneeName',
	    selector: 'adminMainWorkflowMain [name=srcAssigneeName]'
	},{
	    ref:'txtReason',
	    selector: 'adminMainWorkflowMain [name=reason]'
	}],
    
    init:function() {
		var me = this;
		
		me.control({
			'adminMainWorkflowMain': {
				selectEmployee : me.selectEmployee,
				clearEmployee : me.clearEmployee,
				selectSrcAssignee : me.selectSrcAssignee,
				clearSrcAssignee : me.clearSrcAssignee,
				selectAssignee : me.selectAssignee,
				clearAssignee : me.clearAssignee,
   				search : me.search
			},
			'adminMainWorkflowMain [action=search]': {
				click : me.search
			},
			'adminMainWorkflowMain [action=cancel]': {
				click : me.cancel
			},
			'adminMainWorkflowMain [action=fixDoc]': {
				click : me.fixDoc
			},
			'adminMainWorkflowMain [action=assign]': {
				click : me.assign
			},
			'adminMainWorkflowMain grid[itemId=wfGrid]': {
				cellclick : me.wfcellclick,
				viewHistory: me.viewHistory
			},
			'adminMainWorkflowMain grid[itemId=wfaGrid]': {
				active : me.assigneeActive
   			}
		});

	},
	
	MSG_KEY : 'WORKFLOW',
	URL : ALF_CONTEXT+'/admin/main/workflow',
	
	search:function() {
		var me = this;
		var store = me.getGrid().getStore();
		
		store.getProxy().extraParams = {
		    e : me.getTxtEmployee().getValue(),
//		    m : me.getChkIsMyTask().getValue(),
		    et : me.getCmbEType().getValue(),
			t : me.getCmbWfType().getValue(),
	   		s : me.getTxtSearch().getValue()
		};
		store.currentPage = 1;
		store.load();
	},
	
	cancel:function() {
		PB.Dlg.confirm('CONFIRM_CANCEL_'+this.MSG_KEY,this,'doCancel', MODULE_ADMIN);
	},
	
	doCancel : function(){
		var me = this;
		
		var me = this;
		var items = me.getGrid().getSelectionModel().selected.items;
		if (items.length==0) {
			PB.Dlg.warn("", MODULE_ADMIN, {msg:"Select any tasks to cancel."});
		} else {
			var ids = [];
			var cancel = true;

			var notAllow = "";

			for(var a in items) {
				var item = items[a];
				
				if (item.data.no.startsWith("PD") || item.data.no.startsWith("SAL")) {
					PB.Dlg.alert('CANNOT_CANCEL_'+me.MSG_KEY, MODULE_ADMIN);
					cancel = false;
					break;
				} else 
				if (item.data.status.indexOf("(W1)")<0 && item.data.status.indexOf("(W2)")<0 && item.data.status.indexOf("(S)")<0) {
					cancel = false;
					if (notAllow != "") {
						notAllow += "<br/>";
					}
					
					notAllow += item.data.no;
				}

				var it = {};
				it.id = item.data.id;
				
				ids.push(it);
			}
			
			if (notAllow != "") {
				PB.Dlg.warn('', MODULE_ADMIN,{msg:'Cannot cancel the following document because status is not W1/W2/S : <br/>'+notAllow});
			}
			
			if (cancel) {
				
		    	var myMask = new Ext.LoadMask({
		    		target:me.getMain(),
		    		msg:"Please wait..."
		    	});
		    	myMask.show();
			
				Ext.Ajax.request({
				     url:me.URL+'/cancel',
				     method: "POST",
				     params: {
				         i: Ext.JSON.encode(ids)
				     },
				     success: function(res){
				    	 
				    	 var json = Ext.decode(res.responseText);
				      	  
				    	 if(json.success){
				    	  
				    		 PB.Dlg.alert('SUCC_CANCEL_'+me.MSG_KEY, MODULE_ADMIN);
				    		
			  	    		 var store = me.getGrid().getStore();
			  	    		 store.currentPage = 1;
			  	    		 store.load();
			  	    		 
			  	    		 store = me.getAgrid().getStore();
				  	   		 store.getProxy().extraParams = {
				  	   			 id : 0
   		               		 };
			  	    		 store.currentPage = 1;
			  	    		 store.load();
				    		
				    	 }else{
				    		 PB.Dlg.error('ERR_CANCEL_'+me.MSG_KEY, MODULE_ADMIN);
				    	 }
	    	   	    	 myMask.hide();
				     },
				     failure: function(response, opts){
				    	 PB.Dlg.error('ERR_CANCEL_'+me.MSG_KEY, MODULE_ADMIN);
			  	    	 myMask.hide();
				     },
				     headers: getAlfHeader()
				});
			
			}
		}
	},	
	
	assign:function() {
		PB.Dlg.confirm('CONFIRM_ASSIGN_'+this.MSG_KEY,this,'doAssign', MODULE_ADMIN);
	},
	
	doAssign : function(){
		var me = this;
		
		var me = this;
		var items = me.getGrid().getSelectionModel().selected.items;
		if (items.length==0) {
			PB.Dlg.warn("", MODULE_ADMIN, {msg:"Select any tasks to assign."});
		} else {
			if (!me.getTxtAssignee().getValue()) {
				PB.Dlg.warn("", MODULE_ADMIN, {msg:"Select any employees to assign."});
			} else {
				if (!me.getTxtReason().getValue() || !me.getTxtReason().getValue().trim()) {
					PB.Dlg.warn("", MODULE_ADMIN, {msg:"Enter reason to assign."});
				} else {
					
			    	var myMask = new Ext.LoadMask({
			    		target:me.getMain(),
			    		msg:"Please wait..."
			    	});
			    	myMask.show();
					
					var ids = [];
					
					for(var a in items) {
						var it = {};
						var item = items[a];
						
						it.id = item.data.id;
						
						ids.push(it);
					}
					
					Ext.Ajax.request({
					     url:me.URL+'/assign',
					     method: "POST",
					     params: {
					         i: Ext.JSON.encode(ids),
					         s: me.getTxtSrcAssignee().getValue(),
					         a: me.getTxtAssignee().getValue(),
					         r: me.getTxtReason().getValue()
					     },
					     success: function(res){
					    	 
					    	 var json = Ext.decode(res.responseText);
					      	  
					    	 if(json.success){
					    		 PB.Dlg.alert('SUCC_ASSIGN_'+me.MSG_KEY, MODULE_ADMIN);
					    		 
				  	    		 var store = me.getGrid().getStore();
				  	    		 store.currentPage = 1;
				  	    		 store.load();			    		
					    		 
				  	    		 store = me.getAgrid().getStore();
				  	    		 store.currentPage = 1;
				  	    		 store.load();
				  	    		 
					    		 me.getTxtSrcAssignee().setValue(null);
					    		 me.getTxtSrcAssigneeName().setValue(null);
					    		 me.getTxtAssignee().setValue(null);
					    		 me.getTxtAssigneeName().setValue(null);
					    		 me.getTxtReason().setValue(null);
					    		
					    	 }else{
//					    		 PB.Dlg.error('ERR_ASSIGN_'+me.MSG_KEY, MODULE_ADMIN);
					    		 PB.Dlg.error('', MODULE_ADMIN,{msg:json.message});
					    	 }
					    	 myMask.hide();
					     },
					     failure: function(response, opts){
					    	 PB.Dlg.error('ERR_ASSIGN_'+me.MSG_KEY, MODULE_ADMIN);
				  	    	 myMask.hide();
					     },
					     headers: getAlfHeader()
					});
		
				}
			}
		}
	},	
	
	selectEmployee:function() {
		var me = this;
		
		var dlg = Ext.create("PB.view.common.SearchUserDlg",{
			title:PB.Label.m.search,
			targetPanel:this.getMain(),
			callback:this.selectEmployeeCallBack
		});
		dlg.show();
	},
	
	selectEmployeeCallBack:function(id, rec) {
		var tab = this.targetPanel;
		setValue(tab, 'employee', rec.get('code'));
		setValue(tab, 'employeeName', rec.get('title') + ' ' + rec.get('fname') + ' ' + rec.get('lname'));
	},

	clearEmployee:function() {
		var tab = this.getMain();
		setValue(tab, 'employee', null);
		setValue(tab, 'employeeName', '');
		setValue(tab, 'isMyTask', null);
	},

	selectAssignee:function() {
		var me = this;
		
		var dlg = Ext.create("PB.view.common.SearchUserDlg",{
			title:PB.Label.m.search,
			targetPanel:this.getMain(),
			callback:this.selectAssigneeCallBack
		});
		dlg.show();
	},
	
	selectAssigneeCallBack:function(id, rec) {
		var tab = this.targetPanel;
		setValue(tab, 'assignee', rec.get('code'));
		setValue(tab, 'assigneeName', rec.get('title') + ' ' + rec.get('fname') + ' ' + rec.get('lname'));
	},

	clearAssignee:function() {
		var tab = this.getMain();
		setValue(tab, 'assignee', null);
		setValue(tab, 'assigneeName', '');
	},
	
	selectSrcAssignee:function() {
		var me = this;
		
		var dlg = Ext.create("PB.view.common.SearchUserDlg",{
			title:PB.Label.m.search,
			targetPanel:this.getMain(),
			callback:this.selectSrcAssigneeCallBack
		});
		dlg.show();
	},
	
	selectSrcAssigneeCallBack:function(id, rec) {
		var tab = this.targetPanel;
		setValue(tab, 'srcAssignee', rec.get('code'));
		setValue(tab, 'srcAssigneeName', rec.get('title') + ' ' + rec.get('fname') + ' ' + rec.get('lname'));
	},

	clearSrcAssignee:function() {
		var tab = this.getMain();
		setValue(tab, 'srcAssignee', null);
		setValue(tab, 'srcAssigneeName', '');
	},
	
	wfcellclick:function(grid, td, cidx, rec, tr, ridx) {
		var me = this;
		var store = me.getAgrid().getStore();
		
		me.rec = rec;

		store.getProxy().extraParams = {
		    id : rec.get("wfid")
		};
		store.currentPage = 1;
		store.load();
	},
	
	assigneeActive:function(r) {
		var me = this;
		
		Ext.Ajax.request({
		     url:me.URL+'/assignee/active',
		     method: "POST",
		     params: {
		         id: r.get('id'),
		         a: r.get('active')
		     },
		     success: function(res){
		    	 
		    	 var json = Ext.decode(res.responseText);
		      	  
		    	 if(json.success){
		    	  
					 var store = me.getAgrid().getStore();
					
					 store.getProxy().extraParams = {
					    id : me.rec.get("wfid")
					 };
					 store.currentPage = 1;
					 store.load();
		    		
		    	 }else{
		    		 PB.Dlg.error('', MODULE_ADMIN, {msg:'fail'});
		    	 }
		    	 
		     },
		     failure: function(response, opts){
		    	 PB.Dlg.error('', MODULE_ADMIN, {msg:'fail'});
		     },
		     headers: getAlfHeader()
		});
	},
	
	viewHistory:function(r) {
		var me = this;
		
		var dlg = Ext.create("PBAdmin.view.main.workflow.DtlDlg");
		var id = r.get("no");
	
		// Current Task
		Ext.Ajax.request({
		      url:ALF_CONTEXT+'/admin/main/wf/task/list',
		      method: "GET",
		      params: {
		    	  id : id,
		    	  lang:getLang()
		      },
		      success: function(response) {
				  var data = Ext.decode(response.responseText).data[0];
				  var curTask;
				  if (data) {
					  curTask = data.type+(data.assignedTo ? " : " : "")+data.assignedTo;
				  } else {
					  curTask = "-";
				  }
				  dlg.items.items[0].items.items[0].items.items[0].setText('<font color="blue">'+curTask+'</font>',false);
				  
			  },
		      failure: function(response, opts){
		          alert("failed");
		      },
		      headers: getAlfHeader()
		});
		
		
		// Path
		var store = dlg.items.items[0].items.items[1].getStore(); 
		store.getProxy().extraParams = {
			id : id,
			lang : getLang()
		}
		store.load();
		
		// History
		store = dlg.items.items[1].getStore();
		store.getProxy().extraParams = {
		   	id : id,
		   	lang : getLang()
		};
		store.load();
		
		// Show
		dlg.show();
		
	},

	fixDoc:function() {
		PB.Dlg.confirm('CONFIRM_FIXDOC_'+this.MSG_KEY,this,'doFixDoc', MODULE_ADMIN);
	},
	
	doFixDoc : function(){
		var me = this;
		
		var me = this;
		var items = me.getGrid().getSelectionModel().selected.items;
		if (items.length==0) {
			PB.Dlg.warn("", MODULE_ADMIN, {msg:"Select any tasks to fix."});
		} else {
			var ids = [];
			var fix = true;
	
			var notAllow = "";
	
			for(var a in items) {
				var item = items[a];
				
//				if (item.data.no.startsWith("PD") || item.data.no.startsWith("SAL")) {
//					PB.Dlg.alert('CANNOT_FIXDOC_'+me.MSG_KEY, MODULE_ADMIN);
//					fix = false;
//					break;
//				} else 
//				if (item.data.status.indexOf("(W1)")<0 && item.data.status.indexOf("(W2)")<0) {
//					cancel = false;
//					if (notAllow != "") {
//						notAllow += "<br/>";
//					}
//					
//					notAllow += item.data.no;
//				}
	
				var it = {};
				it.id = item.data.id;
				
				ids.push(it);
			}
			
			if (notAllow != "") {
				PB.Dlg.warn('', MODULE_ADMIN,{msg:'Cannot fix the following document because status is not W1/W2 : <br/>'+notAllow});
			}
			
			if (fix) {
				
		    	var myMask = new Ext.LoadMask({
		    		target:me.getMain(),
		    		msg:"Please wait..."
		    	});
		    	myMask.show();
			
				Ext.Ajax.request({
				     url:me.URL+'/fixDoc',
				     method: "POST",
				     params: {
				         i: Ext.JSON.encode(ids)
				     },
				     success: function(res){
				    	 
				    	 var json = Ext.decode(res.responseText);
				      	  
				    	 if(json.success){
				    	  
				    		 PB.Dlg.alert('SUCC_FIXDOC_'+me.MSG_KEY, MODULE_ADMIN);
				    		
			  	    		 var store = me.getGrid().getStore();
			  	    		 store.currentPage = 1;
			  	    		 store.load();
			  	    		 
			  	    		 store = me.getAgrid().getStore();
				  	   		 store.getProxy().extraParams = {
				  	   			 id : 0
			                 };
			  	    		 store.currentPage = 1;
			  	    		 store.load();
				    		
				    	 }else{
				    		 PB.Dlg.error('ERR_FIXDOC_'+me.MSG_KEY, MODULE_ADMIN);
				    	 }
	    	   	    	 myMask.hide();
				     },
				     failure: function(response, opts){
				    	 PB.Dlg.error('ERR_FIXDOC_'+me.MSG_KEY, MODULE_ADMIN);
			  	    	 myMask.hide();
				     },
				     headers: getAlfHeader()
				});
			
			}
		}
	}	

});
