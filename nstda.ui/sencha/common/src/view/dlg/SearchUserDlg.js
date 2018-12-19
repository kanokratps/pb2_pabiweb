Ext.define('PB.view.common.SearchUserDlg', {
	extend : 'Ext.window.Window',
    alias:'widget.searchUserDlg',
    
	initComponent: function(config) {
		var me = this;
		
		var lbw = 100;
		
//		var store = Ext.create('PB.store.common.UserStore');
		var store = Ext.create('PB.store.common.EmployeeUserStore',{autoLoad:false});
		store.getProxy().api.read = ALF_CONTEXT+'/admin/main/employee/list';

		var selModel;
    	var columns = []

    	if (me.multiSelect) { 
			selModel = Ext.create('Ext.selection.CheckboxModel',{
				mode:"MULTI",
				showHeaderCheckbox:true
			});
		} else {
			selModel = Ext.create('Ext.selection.RowModel',{
				mode:"SINGLE"
			});
			columns.push({ 
				text:'', dataIndex: 'id', width: 50, align:'center', renderer:
		    	function(v) {
	 	    		return '<input type="radio" name="id" value="'+v+'"/>'; 
	 	    	}
	 	    });
		}
		
    	columns.push(
//	        	     { text:PB.Label.m.org, dataIndex: 'org_name', width: 50 },
//	        	     { text:PB.Label.m.section, dataIndex: 'section_name', flex:1, renderer:function(v,m,r){ return '['+r.get('section_code')+"] "+v;}},
//	        	     { text:PB.Label.m.ecode, dataIndex: 'emp_id', width: 100 },
//	        	     { text:PB.Label.m.fullname, dataIndex: 'first_name', flex:1, renderer:function(v,m,r){ return r.get('title')+" "+v+" "+r.get('last_name');}},
//	        	     { text:PB.Label.m.pos, dataIndex: 'pos_name', width: 150 }
    	     { text:PB.Label.m.org, dataIndex: 'org', width: 50 },
    	     { text:PB.Label.m.division, dataIndex: 'div_name', flex:1},
    	     { text:PB.Label.m.section, dataIndex: 'utype', flex:1},
    	     { text:PB.Label.m.ecode, dataIndex: 'code', width: 100 },
    	     { text:PB.Label.m.fullname, dataIndex: 'fname', flex:1, renderer:function(v,m,r){ return r.get('title')+" "+v+" "+r.get('lname');}},
    	     { text:PB.Label.m.pos, dataIndex: 'position', width: 150 }
    	);
		
		
		Ext.applyIf(me, {
	        renderTo : Ext.getBody(),
	        modal: true,
	        width: 790,
	        height: 500,
	        layout: 'border',
	        resizable: false,
	        items : [{
	        	region:'north',
	        	height:40,
	        	xtype : 'form',
		        itemId : 'searchUser',
		        border : 0,
		        items:[{
		        	xtype:'container',
		        	margin:'5 0 0 10',
		        	layout:{
						type:'hbox',
						align:'middle'
					},
					items:[{
			        	xtype:'textfield',
			        	itemId:'searchTerm',
			        	fieldLabel:PB.Label.m.searchTerm,
			        	labelWidth:lbw,
			        	width:400,
			        	margin:'5 0 0 0',
		            	enableKeyEvents: true,
		            	hasfocus:true,
			           	listeners: {
			 	 			specialkey: function(field, e){
			 	 				if(e.getKey() == e.ENTER){
			 	 					this.fireEvent("searchUser",this);
			 	 				}
			 	 			},
			 	 			afterrender: function(txt) {
								Ext.defer(function(){
									txt.focus();
								},100);
								this.setHeight(22);
			 	 			}
			           	}			        		
					},{
			        	xtype:'button',
			        	text:PB.Label.m.btnSearch,
			        	iconCls:'icon_search',
			        	margin:'5 0 0 10',
		                listeners: {
		                    click: function(){
		                    	this.fireEvent("searchUser", this);
		                    }
		                }
					}]
		        }]
	        },{
	        	region:'center',
	        	xtype:'grid',
	        	margin:'5 0 0 0',
	        	columns:columns,
	        	store:store,
	        	selModel:selModel
	        }],
	        buttons : [{
	          text: PB.Label.m.btnConfirm, 
	          action : 'ok',
	          iconCls:'icon_ok',
	          listeners: {
	               click: function(){
	                	this.fireEvent("confirmUser", this);
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