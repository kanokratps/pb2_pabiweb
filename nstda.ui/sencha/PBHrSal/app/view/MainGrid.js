Ext.define('PBHrSal.view.MainGrid', {
    extend: 'Ext.grid.Panel',
    alias:'widget.hrSalMainGrid',
    
	initComponent: function(config) {
		var me = this;
		
		me.lang = getLang().split("_")[0].toLowerCase();
	
		var columns = [
		      {
	        	xtype: 'actioncolumn',
	        	dataIndex: 'action',
	        	text: '', 
	            width: 80,
	            sortable:false,
	            items: [{
	                tooltip: 'Detail', 
	                action : 'viewDetail',
	        	    getClass: function(v) {
	        	    	return getActionIcon(v, "V", 'view');
	                }
	            }, {
	                tooltip: 'Goto Folder', 
	                action : 'gotoFolder',
	        	    getClass: function(v) {
	        	    	return getActionIcon(v, "F", 'detail');
	                }
	            }]
	          }
//			  { text: 'PD NO.', dataIndex:'id', width:150}
		];
		
		Ext.Ajax.request({
		      url:ALF_CONTEXT+"/hr/sal/gridfield/list",
		      method: "GET",
		      success: function(response){
		    	  
		    	var data = Ext.decode(response.responseText).data;
		    	
		    	for(var i=0; i<data.length; i++) {
					var d = data[i];
					
					var col = {
						text: PBHrSal.Label.m[d.label],  
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
		
		//columns.push({ text: 'Requested Time',  dataIndex: 'created_time_show', width:130});
		columns.push({ text: PBHrSal.Label.m.status,  dataIndex: 'wfstatus', width:300,
		  	  renderer: function (v, m, r) {
			
					if (r.get("overDue")) {
						m.style = "background-color:#FFFF66";
					}
					
					var status = r.get("status");
        			if (status != "D") {
			            var id = Ext.id();
			            var id1 = Ext.id();
			            Ext.defer(function () {
			            	me.createIconViewHistory(id, v, r);
			            }, 50);
			            return Ext.String.format('<div><div style="float:left;margin:0;padding:0;width:12px;height:20px" class="icon_bar_{2}" id="{1}"></div><div id="{0}"></div></div>', id, id1, PBHrSal.Label.s[status].color);
                    } else {
			            var id = Ext.id();
			            return Ext.String.format('<div style="float:left;margin:0;padding:0;width:12px;height:20px" class="icon_bar_{1}" id="{0}"></div>', id, PBHrSal.Label.s[status].color);
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
                text: PB.Label.m.search,
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
	},
	
//	indicator:{
//		"W1":{color:"yellow", text_th:"รอการอนุมัติ", text_en:"Wait for Approval"},
//		"W2":{color:"red", text_th:"ไม่อนุมัติ", text_en:"Rejected"},
//		"C1":{color:"green", text_th:"อนุมัติ", text_en:"Approved"},
//		"X1":{color:"darkgray", text_th:"ฝ่ายบุคคลยกเลิก", text_en:"Cancelled By HR"},
//		"S":{color:"purple", text_th:"ขอคำปรึกษา", text_en:"Consulting"}
//	},
	
	createIconViewHistory:function(id, v, r) {
		var me = this;
		
//		var s = eval('me.indicator[r.get("status")].text_' +me.lang);
		var s = eval('PBHrSal.Label.s[r.get("status")].text_' +me.lang);

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
