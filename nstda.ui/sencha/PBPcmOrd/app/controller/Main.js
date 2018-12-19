Ext.define('PBPcmOrd.controller.Main', {
    extend: 'Ext.app.Controller',
    
	refs:[{
	    ref:'main',
	    selector:'pcmOrdMain'
	},{
	    ref:'mainGrid',
	    selector:'pcmOrdMainGrid'
	},{
		ref:'txtSearch',
		selector:'pcmOrdMainGrid [itemId=txtSearch]'
	}],
	
	init:function() {
		var me = this;

		me.control({
			'pcmOrdMain [action=add]': {
				click : me.add
			},
			'pcmOrdMain [action=search]': {
				click : me.search
			},
			'pcmOrdMainGrid':{
				search : me.search,
   				gotoFolder : me.gotoFolder,
   				viewDetail : me.viewDetail,
   				viewHistory : me.viewHistory
			}
		});
	
	},
	
	SEND_MSG_KEY : 'SEND_PCM_ORD',
	MSG_KEY : 'DELETE_ORD',
    URL : ALF_CONTEXT+'/pcm/ord',
    ADMIN_URL : ALF_CONTEXT+'/admin/pcm/ord',
    MSG_URL : ALF_CONTEXT+'/pcm/message',
	
	onLaunch:function(app) {
		var me = this;
		
		var mainGrid = me.getMainGrid();
		if (mainGrid) {
			var tbar = me.getMainGrid().getDockedComponent(1);
			
			Ext.Ajax.request({
			      url:me.URL+"/criteria/list",
			      method: "GET",
			      success: function(response){
			    	  
			    	var data = Ext.decode(response.responseText).data;
			    	
					for(var i=data.length-1; i>=0; i--) {
						
						var d = data[i];
						
						var store = Ext.create('PB.store.common.ComboBoxStore');
						
						store.getProxy().api.read = ALF_CONTEXT+'/srcUrl/'+PB.Util.getSrcUrl(d.url);
						if (d.param) {
							var params = d.param.split(",");
							
							store.getProxy().extraParams = {
								p1 : params[0],
								orderBy : 'code',
								all : true,
								lang : getLang()
							}
							if (params.length>1) {
								store.getProxy().extraParams.p2 = params[1];
							}
							
						}
						store.load();
						
						var c = {
							xtype:'combo',
							itemId:'cri'+i,
							bgData:d,
							hideLabel:true,
					    	displayField:'name',
					    	valueField:'id',
					        emptyText : d.emptyText,
					        store: store,
					        queryMode: 'local',
					        typeAhead:true,
					        multiSelect:false,
					        forceSelection:true,
					        listConfig : {
						    	resizable:true,
							    getInnerTpl: function () {
									return '<div>{name}</div>';
							        //return '<div>{name}<tpl if="id != \'\'"> ({id})</tpl></div>';
							    }
							},
					        listeners:{
								beforequery : function(qe) {
									qe.query = new RegExp(qe.query, 'i');
					//				qe.forceAll = true;
								}
							}
						}
						
						if (d.listWidth) {
					        c.matchFieldWidth = false;
							c.listConfig.width = d.listWidth;
						}
						
						if (d.width) {
							c.width = d.width;
						}
						
						tbar.insert(0, c);
	
					}
					
			      },
			      failure: function(response, opts){
			          alert("failed");
			      },
			      headers: getAlfHeader()
			}); 
		}
		
		if (ID) {
			
			Ext.Ajax.request({
			      url:me.URL+"/get",
			      method: "GET",
			      params: {
			    	  id : ID,
			    	  lang : getLang()
			      },
			      success: function(response){
			    	  
			    	var data = Ext.decode(response.responseText).data;
			    	
			    	var rec = Ext.create("PBPcmOrd.model.GridModel",data[0]);

					me.edit(rec);
					
			      },
			      failure: function(response, opts){
			          alert("failed");
			      },
			      headers: getAlfHeader()
			});

		}
	},
	
	search:function() {
		var me = this;
		
		PB.Util.checkSession(this, me.MSG_URL+"/get", function() {

			var store = me.getMainGrid().getStore();
			
			var params = {
				s : me.getTxtSearch().getValue(),
				lang:getLang()
			}
			
			var fields = {};
			
			var tbar = me.getMainGrid().getDockedComponent(1);
			for(var i=0; i<10; i++) {
				var c = tbar.down("[itemId=cri"+i+"]");
				if (c) {
					var d = c.bgData;
					fields[d.field] = c.getValue();
				}
			}
			
			params.fields = Ext.JSON.encode(fields);
			
			store.getProxy().extraParams = params;
			store.currentPage = 1;
			store.load();
		
		});
	},
	
	add:function() {
		var me = this;
		
		PB.Util.checkSession(this, me.MSG_URL+"/get", function() {
		
			var grid = me.getMainGrid();
			
			var myMask = new Ext.LoadMask({
				target:me.getMain(), 
				msg:"Please wait..."
			});
			
			myMask.show();
			
			var params = {
					total:224700,
					requested_time:'2016-03-20T12:00:00',
					files:Ext.JSON.encode([]),
					reqOu:'105015'
			};
			
			Ext.Ajax.request({
			    url:me.URL+"/send",
			    method: "POST",
			    params: params,
			    success: function(response){
			  	  
				  	var json = Ext.decode(response.responseText);
					  
				   	if (json.success) {
				   		PB.Dlg.info('SUCC_'+me.SEND_MSG_KEY, MODULE_PCM, {msg:'ID:'+json.data.id, fn:me.closeForm, scope:me});
				   	} else {
				   		if (json.data.valid != undefined && !json.data.valid) {
				   			
				   			var msg;
				   			
				   			if (json.data.msg) {
				   				msg = json.data.msg;
					   		}
					   		
				   			PB.Dlg.error(null, MODULE_PCM, {msg:msg});
				   		}
				   		else {
					   		PB.Dlg.error('ERR_'+me.SEND_MSG_KEY, MODULE_PCM);
				   		}
				   	}
				   	 
			   	 	myMask.hide();
			
			    },
			    failure: function(response, opts){
			    	try {
			    		var json = Ext.decode(response.responseText);
				    	PB.Dlg.error('ERR_'+me.SEND_MSG_KEY+" ("+json.message+")", MODULE_PCM);
			    	}
			    	catch (err) {
				    	alert(response.responseText);
			    	}
				    myMask.hide();
			    },
			    headers: getAlfHeader()
			});
		
		});
	},

	closeForm:function() {
		this.getMainGrid().getStore().load();
	},
	
	gotoFolder : function(r) {
		var me = this;
		
		PB.Util.checkSession(this, me.MSG_URL+"/get", function() {
	
			var dlg = Ext.create("PB.view.common.FolderDtlDlg",{
				rec : r
			});
			dlg.show();
		
		});
	},
	
	_gotoFolder:function(r) {
		Ext.Ajax.request({
	        url:ALF_CONTEXT+"/util/getFolderName",
	        async : false,
	        method: "POST",
	        params: {
	        	n : r.get('folder_ref')
	        },
	        success: function(response){
	        	
	        	var json = Ext.decode(response.responseText);  
	        	
	        	if(json.success){
	        		
	//            	window.open(MAIN_CONTEXT+"/page/repository#filter=path%7C%2FSites%2F"+CURRENT_SITE+"%2FdocumentLibrary%2F"+json.folderName+"%7C", "_new");
	//            	window.open(MAIN_CONTEXT+"/page/site/"+CURRENT_SITE+"/documentlibrary#filter=path%7C"+ json.data.name + "%7C", "_new");
	            	window.open(MAIN_CONTEXT+"/page/repository#filter=path%7C"+json.data.name+"%7C", "_new");
	        		
	        	}
	        	
	        },
	        failure: function(response, opts){
	        	
	        	//console.log("fail");
	        },
	        headers: getAlfHeader()
	    });
	},
	
	viewDetail:function(r) {
		var me = this;
		
		var win = window.open("", "_new");
		
		PB.Util.checkSession(this, me.MSG_URL+"/get", function() {

//			window.open(Alfresco.constants.PROXY_URI_RELATIVE+"api/node/content/"+nodeRef2Url(r.get("doc_ref")),"_new");
			win.location.href = Alfresco.constants.PROXY_URI_RELATIVE+"api/node/content/"+nodeRef2Url(r.get("doc_ref"));
		});
	},
	
	viewHistory:function(r) {
		var me = this;
		
		PB.Util.checkSession(this, me.MSG_URL+"/get", function() {

			var dlg = Ext.create("PBPcmOrd.view.workflow.DtlDlg");
			var id = r.get("id");
		
			// Current Task
			Ext.Ajax.request({
			      url:me.URL+'/wf/task/list',
			      method: "GET",
			      params: {
			    	  id : id,
			    	  lang:getLang()
			      },
			      success: function(response) {
					  var data = Ext.decode(response.responseText).data[0];
					  var curTask;
					  if (data && r.get("status")!="X1") {
						  curTask = data.type+(data.assignedTo ? " : " : "")+data.assignedTo;
					  } else {
						  curTask = "";
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
		
		});
	}

});
