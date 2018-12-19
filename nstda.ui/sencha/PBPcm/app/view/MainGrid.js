Ext.define('PBPcm.view.MainGrid', {
    extend: 'Ext.grid.Panel',
    alias:'widget.pcmReqMainGrid',
    
	initComponent: function(config) {
		var me = this;
		
		me.lang = getLang().split("_")[0].toLowerCase();
	
		var columns = [
		      {
	        	xtype: 'actioncolumn',
	        	dataIndex: 'action',
	        	text: '', 
	            width: 115,
	            sortable:false,
	            items: [{
	                tooltip: 'Copy', 
	                action : 'copy',
	        	    getClass: function(v) {
	        	    	return getActionIcon(v, "C", 'copy');
                	}
//	        	}, {
//	                tooltip: 'Diagram', 
//	                action : 'showDiagram',
//	        	    getClass: function(v) {
//	        	    	return getActionIcon(v, "S", 'diagram');
//	                }
	            }, {
	                tooltip: 'Detail', 
	                action : 'viewDetail',
	        	    getClass: function(v) {
	        	    	return getActionIcon(v, "V", 'view');
	        	    }
                },{
	                tooltip: 'Edit', 
	                action : 'edit',
	        	    getClass: function(v) {
	        	    	return getActionIcon(v, "E", 'edit');
	                }
	            }, {
	                tooltip: 'Goto Folder', 
	                action : 'gotoFolder',
	        	    getClass: function(v) {
	        	    	return getActionIcon(v, "F", 'detail');
	                }
	            }, {
	                tooltip: 'Delete', 
	                action : 'del',
	        	    getClass: function(v) {
	        	    	return getActionIcon(v, "D", 'delete');
	        	    }
	            }, {
	                tooltip: 'View', 
	                action : 'view',
	        	    getClass: function(v) {
	        	    	return getActionIcon(v, "W", 'view_mode');
	        	    }
	            }]
	          }
		];
		
		Ext.Ajax.request({
		      url:ALF_CONTEXT+"/pcm/req/gridfield/list",
		      method: "GET",
		      success: function(response){
		    	  
		    	var data = Ext.decode(response.responseText).data;
		    	
				for(var i=0; i<data.length; i++) {
					var d = data[i];
					
					var col = {
						text: PBPcm.Label.m[d.label],  
						dataIndex: d.field,
						align:d.align
					}
					
					if (d.wrap) {
						col.tdCls = 'wrap';
					}
					
					if (d.width) {
						col.width = parseInt(d.width);
					} else {
						col.flex = 1;
					}
					
					if (d.type) {
						col.xtype = d.type+"column";
						col.format = DEFAULT_MONEY_FORMAT;
					}
					
					columns.push(col); 
				}
    				
		      },
		      failure: function(response, opts){
		          alert("failed");
		      },
		      headers: getAlfHeader(),
		      async:false
		}); 
		
		columns.push({ text: PBPcm.Label.m.status,  dataIndex: 'wfstatus', width:370,
		  	  renderer: function (v, m, r) {
			
					if (r.get("overdue")) {
						m.style = "background-color:#FFFF66";
					}
					
					var status = r.get("status");
        			if (status != "D") {
			            var id = Ext.id();
			            var id1 = Ext.id();
			            Ext.defer(function () {
			            	me.createIconViewHistory(id, v, r);
			            }, 50);
			            return Ext.String.format('<div><div style="float:left;margin:0;padding:0;width:12px;height:20px" class="icon_bar_{2}" id="{1}"></div><div id="{0}"></div></div>', id, id1, PBPcm.Label.s[status].color);
                    } else {
			            var id = Ext.id();
			            return Ext.String.format('<div style="float:left;margin:0;padding:0;width:12px;height:20px" class="icon_bar_{1}" id="{0}"></div>', id, PBPcm.Label.s[status].color);
                    }
		      }
		 });
		
		Ext.applyIf(me, {
			
			columns : columns,
				
			tbar : [{
            	xtype: 'textfield',
            	itemId:'txtSearch',
            	width:120,
            	enableKeyEvents: true,
	           	listeners: {
	           		
	 	 			specialkey: function(field, e){
	 	 				if(e.getKey() == e.ENTER){
	 	 					me.fireEvent("search");
	 	 				}
	 	 			}
			
	           	}
            },{
            	xtype: 'button',
                text: PB.Label.m.btnSearch,
                iconCls: "icon_search",                
                action: "search"
            }],
            
			bbar : {
				xtype:'pagingtoolbar',
			    displayInfo    : true,
			    pageSize       : PAGE_SIZE,
			    store          : me.store
			}
				
		});		
		
	    this.callParent(arguments);
	    
		Ext.apply(me.store, {pageSize:PAGE_SIZE});
		me.store.load();
		
		me.addDocked({
			xtype:'toolbar',
			dock:'top',
			items:[{
            	xtype: 'button',
	            text: PB.Label.m.btnNew,
	            iconCls: "icon_add",
	            action: "add"
			}]
		});

	},
	
//	indicator:{
//		"D":{color:"gray", text_th:"Draft", text_en:"Draft"},
//		"W1":{color:"yellow", text_th:"รอการอนุมัติ", text_en:"Wait for Approval"},
//		"W2":{color:"red", text_th:"ไม่อนุมัติ", text_en:"Rejected"},
//		"C1":{color:"green", text_th:"รอพัสดุรับงาน", text_en:"Wait for Acceptance"},
//		"C2":{color:"green", text_th:"พัสดุรับงาน", text_en:"Accepted"},
//		"C3":{color:"green", text_th:"พัสดุเห็นชอบให้ดำเนินการเอง", text_en:"Purchased by yourself"},
//		"X1":{color:"darkgray", text_th:"ผู้ขอยกเลิก", text_en:"Cancelled By Requester"},
//		"X2":{color:"darkgray", text_th:"พัสดุยกเลิก", text_en:"Cancelled By Procurement"},
//		"S":{color:"purple", text_th:"ขอคำปรึกษา", text_en:"Consulting"}
//	},
	
	createIconViewHistory:function(id, v, r) {
		var me = this;
		
//		var s = eval('me.indicator[r.get("status")].text_' +me.lang);
		var s = eval('PBPcm.Label.s[r.get("status")].text_' +me.lang);
		
		if (Ext.get(id)) {
            Ext.widget('linkbutton', {
                renderTo: id,
                text: s+' (' + r.get("wfstatus") + ')',
                iconCls:'icon_postpone',
                width: 75,
                handler: function () { 
                	me.fireEvent("viewHistory",r);
                },
                tooltip: 'Workflow Detail'
            });
	    }
	    else {
		    Ext.defer(function () {
		    	me.createIconViewHistory(id, v, r);
		    }, 50);
	    }
	}
	
});
