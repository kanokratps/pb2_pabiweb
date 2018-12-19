Ext.define('PB.view.common.SearchEmployeeUserDlg', {
	extend : 'Ext.window.Window',
    alias:'widget.searchEmployeeUserDlg',
    
	initComponent: function(config) {
		var me = this;
		
		var lbw = 100;
		var lbwcb = 230;
		
		var destStore = Ext.create('PB.store.common.ComboBoxStore',{
			autoLoad:false,
			sorters: [
		        {
		            property : 'name',
		            direction: 'ASC'
		        }
		    ]
		});
		destStore.getProxy().api.read = ALF_CONTEXT+'/admin/main/destination/list';
		destStore.getProxy().extraParams = {
			t:"D"
		}
		destStore.load();
		
		var store = Ext.create('PB.store.common.EmployeeUserStore',{autoLoad:false});
		store.getProxy().api.read = ALF_CONTEXT+'/admin/main/employee/list';

		var selModel = Ext.create('Ext.selection.CheckboxModel',{
			mode:"MULTI",
			showHeaderCheckbox:true
		});
		
		var items = [{
        	region:'north',
	    	height:40,
	    	xtype : 'form',
	        itemId : 'searchEmployeeUser',
	        border : 0,
	        items:[{
	//        	xtype:'container',
	//        	margin:'5 0 0 10',
	//        	layout:{
	//				type:'hbox',
	//				align:'middle'
	//			},
	//			items:[{
	//				xtype:'label',
	//				text:'ประเภท:',
	//				width:lbw
	//			},{
	//				xtype:'radio',
	//				name:'type',
	//				boxLabel:'หน่วยงาน',
	//				inputValue:'U',
	//				margin:'0 0 0 5',
	//				checked:true,
	//				listeners:{
	//					change:function(rad, newV, oldV) {
	//						me.fireEvent("selectRadio",rad,newV);
	//					}
	//				}
	//			},{
	//				xtype:'radio',
	//				name:'type',
	//				boxLabel:'โครงการ',
	//				inputValue:'P',
	//				margin:'0 0 0 5',
	//				checked:false
	//			},{
	//				xtype:'combo',
	//				itemId:'code',
	//				hideLabel:true,
	//		    	displayField:'name',
	//		    	valueField:'id',
	//		        emptyText : PB.Label.m.select,
	//		        store: cmbStore,
	//		        queryMode: 'local',
	//		        typeAhead:true,
	//		        multiSelect:false,
	//		        forceSelection:true,
	//				flex:1,
	//				margin:'0 10 0 10',
	//				editable:true,
	//				allowBlank:false,
	//				listeners:{
	//					beforequery : function(qe) {
	//						qe.query = new RegExp(qe.query, 'i');
	//		//				qe.forceAll = true;
	//					}
	//				}
	//			}]
	//        },{
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
		        	flex:1,
		        	margin:'5 0 0 0',
	            	enableKeyEvents: true,
		           	listeners: {
		 	 			specialkey: function(field, e){
		 	 				if(e.getKey() == e.ENTER){
		 	 					this.fireEvent("searchUser",this);
		 	 				}
		 	 			},
						afterrender:function(txt) {
							Ext.defer(function(){
								txt.focus();
							},100);
						}
		           	}			        		
				},{
		        	xtype:'button',
		        	text:PB.Label.m.btnSearch,
		        	iconCls:'icon_search',
		        	margin:'5 10 0 10',
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
	    	columns:[
	    	     { text:PB.Label.m.org, dataIndex: 'org', width:60},
	    	     { text:PB.Label.m.division, dataIndex: 'div_name', flex: 1 },
	    	     { text:PB.Label.m.section, dataIndex: 'utype', flex: 1 },
	    	     { text:PB.Label.m.ecode, dataIndex: 'code', width: 100 },
	    	     { text:PB.Label.m.fullname, dataIndex: 'fname', flex: 1, renderer:function(v,m,r){return r.get('title')+' '+r.get('fname')+' '+r.get('lname')}},
	    	     { text:PB.Label.m.pos, dataIndex: 'position', flex:1 }
	    	],
	    	store:store,
	    	selModel:selModel
	    }]
		
		if (me.needFootPrint) {
			items.push({
		    	region:'south',
		    	xtype:'form',
		    	border:0,
		    	title:'Carbon Footprint',
		    	collapsible:true,
		    	collapsed:true,
		    	items:[{
		    		xtype:'container',
		    		margin:'5 5 0 5',
		    		layout:{
						type:'hbox',
						align:'middle'
		    		},
		    		items:[{
						xtype:'radio',
						name:'destType',
						boxLabel:'ในประเทศ',
						inputValue:'D',
						margin:'0 15 0 5',
						checked:true,
						listeners:{
							change:function(rad, newV, oldV) {
								me.fireEvent("selectDestination",rad,newV);
							}
						}
					},{
						xtype:'radio',
						name:'destType',
						boxLabel:'ต่างประเทศ',
						inputValue:'I',
						margin:'0 15 0 5',
						checked:false
		    		},{
						xtype:'combo',
						itemId:'destination',
						hideLabel:true,
				    	displayField:'name',
				    	valueField:'name',
				        emptyText : PB.Label.m.select,
				        store:destStore,
				        queryMode: 'local',
				        typeAhead:true,
				        multiSelect:false,
				        forceSelection:true,
						flex:1,
		        		margin:'5 10 0 40',
		        		anchor:'-10',
						editable:true,
						allowBlank:false,
						listeners:{
							beforequery : function(qe) {
								qe.query = new RegExp(qe.query, 'i');
				//				qe.forceAll = true;
							},
							select : function(combo, rec){
		    	       		   me.fireEvent("selectDestination",combo, rec);
		    	       	    }
						}
		        	}]
		    	},{
		    		xtype:'textfield',
		    		fieldLabel:'เส้นทางการบิน',
		    		labelWidth:lbwcb,
		    		name:'route',
		    		margin:'5 5 0 10',
		    		anchor:'-10'
		    	},{
		    		xtype:'container',
		    		margin:'5 5 0 10',
		    		anchor:'-10',
		    		layout:{
						type:'hbox',
						align:'middle'
		    		},
		    		items:[{
		        		xtype:'datefield',
		        		fieldLabel:'วันที่เริ่มต้น - สิ้นสุด',
		        		labelWidth:lbwcb,
		        		name:'depart',
		        		itemId:'depart',
		        		width:lbwcb+120,
		        		vtype: 'daterange',
						endDateField: 'arrive',
						listeners : {
							change : function(d, newV, oldV) {
				                var end = d.up('form').down('#' + d.endDateField);
				                end.setMinValue(newV);
				                end.validate();
				                d.dateRangeMin = newV;
							}
						}
		    		},{
		    			xtype:'label',
		    			text:' - ',
		    			width:15,
		    			margin:'0 0 0 5'
		    		},{
		        		xtype:'datefield',
		        		hideLabel:true,
		        		name:'arrive',
		        		itemId:'arrive',
		        		width:120,
		        		vtype: 'daterange',
						startDateField: 'depart',
						listeners : {
							change : function(d, newV, oldV) {
				                var start = d.up('form').down('#' + d.startDateField);
				                start.setMaxValue(newV);
				                start.validate();
				                d.dateRangeMax = newV;
							}
						}
		    		}]
		    	},{
		    		xtype:'textfield',
		    		fieldLabel:'Class',
		    		labelWidth:lbwcb,
		    		name:'cls',
		    		margin:'5 5 0 10',
		    		anchor:'-10'
		    	},{
		    		xtype:'numberfield',
		    		fieldLabel:'จำนวนเงิน',
		    		labelWidth:lbwcb,
		    		name:'amount',
		    		hideTrigger:true,
		    		margin:'5 5 5 10',
		    		anchor:'-10'
		    	}]
		    });
		}
		
		Ext.applyIf(me, {
	        renderTo : Ext.getBody(),
	        modal: true,
	        width: 750,
	        height: 500,
	        layout: 'border',
	        resizable: false,
	        items : items,
	        buttons : [{
	          text: PB.Label.m.btnConfirm, 
	          action : 'ok',
	          iconCls:'icon_ok',
	          listeners: {
	               click: function(){
	                	this.fireEvent("confirm", this);
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