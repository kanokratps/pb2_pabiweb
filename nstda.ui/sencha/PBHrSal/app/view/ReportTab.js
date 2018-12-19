Ext.define('PBHrSal.view.ReportTab', {
    extend: 'Ext.panel.Panel',
    alias:'widget.hrSalReportTab',
    requires: [
        'Ext.grid.Panel'
    ],
    
    layout:'border',
   
    
	initComponent: function(config) {
	
		var me = this;
		var w = Ext.get(HTML_ID).getWidth();
		var cw = w/1.2;
		
		//var cmbModel = Ext.create('PB.model.common.ComboBoxModel',
		//	{id: null, code:null ,name: '( ไม่ระบุ )',select:null,flag2:null});
		var cmb =  Ext.create('PB.store.common.ComboBoxStore',{storeId:'reportTypeStore',autoLoad:false});
		cmb.getProxy().api.read = ALF_CONTEXT+'/admin/main/master/list';
		cmb.getProxy().extraParams = {
      		t : 'R'
		};
		cmb.load({callback:function(){
			//cmb.insert(0,cmbModel);
		}});
		
		var inoutModel = Ext.create('PB.model.common.ComboBoxModel',
			{id: null, code:null ,name: '( ในประเทศ/นอกประเทศ )',select:null,flag2:null});

		
		var inoutStore =  Ext.create('PB.store.common.ComboBoxStore',{storeId:'inoutStore',autoLoad:false});
		inoutStore.getProxy().api.read = ALF_CONTEXT+'/admin/main/master/list';
		inoutStore.getProxy().extraParams = {
      		t : 'COT'
		};
		inoutStore.load({callback:function(){
			inoutStore.insert(0,inoutModel);
		}});


		var sortStore =  Ext.create('PB.store.common.ComboBoxStore',{storeId:'sortStore',autoLoad:false});
		
		var reqModel = Ext.create('PB.model.common.ComboBoxModel',
			{id: '', code:'' ,name: '( ทั้งหมด )',select:null,flag2:''});
		var requestStore =  Ext.create('PB.store.common.ComboBoxStore',{storeId:'requestStore',autoLoad:false});
		
		var pmModel = Ext.create('PB.model.common.ComboBoxModel',
			{id: '', code:'' ,name: '( ทั้งหมด )',select:null,flag2:null});
		var pmStore =  Ext.create('PB.store.common.ComboBoxStore',{storeId:'pmStore',autoLoad:false});
		pmStore.getProxy().api.read = ALF_CONTEXT+'/admin/main/master/list';
		pmStore.getProxy().extraParams = {
      		t : 'OP2'
		};
		pmStore.load({callback:function(){
			pmStore.insert(0,pmModel);
		}});

		var lwidth = 115;
		var C_R01 = [ { text: '', dataIndex:'', flex:1}, { text: '', dataIndex:'', flex:1}  ];
		Ext.applyIf(me, {
			items:[{
				region:'north',
				height:115,
				itemId : 'northPanel',
				xtype:'panel',
				autoScroll : true,
				layout: {
			        align: 'center',
			        type: 'vbox'
	        	},
				items : [{
					xtype : 'container',
					itemId : 'p_Type',
					margin : '10 0 0 0',
					border : 0,
					width : cw,
					layout: {
				        align: 'center',
				        type: 'vbox'
		        	},
					items : [{
						xtype : 'combo',
				    	allowBlank : true,
				    	editable : false,
					    fieldLabel: '\u0E1B\u0E23\u0E30\u0E40\u0E20\u0E17\u0E23\u0E32\u0E22\u0E07\u0E32\u0E19',
					    labelWidth: lwidth,
				    	name : 'reportType',
				    	itemId : 'reportType',
				    	emptyText : '( \u0E1B\u0E23\u0E30\u0E40\u0E20\u0E17\u0E23\u0E32\u0E22\u0E07\u0E32\u0E19 )',
				    	store : cmb,
					    queryMode: 'local',
				       	displayField: 'name',
				       	width : cw,
				       	value : '',
				       	valueField: 'code'
					}]
				},{
					xtype : 'container',
					border : 0,
					itemId : 'p_Date',
					width : cw,
					layout: {
					 	type: 'table',
					 	columns: 3
					},
					items : [{
						xtype: 'datefield',
						editable : true,
						itemId : 'fromDate',
						width : cw/2.5,
						format:'d/m/Y',
						fieldLabel: '\u0E27\u0E31\u0E19\u0E17\u0E35\u0E48',
						labelWidth: lwidth,
//						name: '',
		                maskRe:/[0-9\/]+/,
		                vtype: 'daterange',
		                endDateField: 'toDate', // id of the end date field
						listeners : {
							change : function(d, newV, oldV) {
				                var end = d.up('form').down('#' + d.endDateField);
				                end.setMinValue(newV);
				                end.validate();
				                d.dateRangeMin = newV;
							}
						}
					},{
						xtype : 'label',
						text : '-',
						margin : '0 5 0 5',
						width : 30
					},{
						xtype: 'datefield',
						editable : true,
						itemId : 'toDate',
						width : cw/2.5,
						format:'d/m/Y',
						fieldLabel: '',
//						name: '',
		                maskRe:/[0-9\/]+/,
		                vtype: 'daterange',
		                startDateField: 'fromDate', // id of the start date field
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
					xtype : 'panel',
					hidden : true,
					itemId : 'permanentDate',
					border : 0,
					width : cw,
					layout: {
				        align: 'center',
				        type: 'vbox'
		        	},
					items : [{
						xtype : 'combo',
						width : cw,
						editable : false,
				    	allowBlank : true,
					    fieldLabel: 'วันที่แก้ไขถาวร',
					    labelWidth: lwidth,
				    	//name : 'cmbSort',
				    	itemId : 'cmbPermanentDate',
				    	emptyText : '( วันที่แก้ไขถาวร )',
				    	store : pmStore,
					    queryMode: 'local',
				       	displayField: 'name',
	//			       	value : '',
				       	valueField: 'code'
					}]
				},{
					xtype : 'panel',
					hidden : true,
					itemId : 'requestType',
					border : 0,
					width : cw,
					layout: {
				        align: 'center',
				        type: 'vbox'
		        	},
					items : [{
						xtype : 'combo',
						width : cw,
						editable : false,
				    	allowBlank : true,
					    fieldLabel: 'ประเภทการร้องขอ',
					    labelWidth: lwidth,
				    	//name : 'cmbSort',
				    	itemId : 'cmbRequestType',
				    	emptyText : '( ประเภทการร้องขอ )',
				    	store : requestStore,
					    queryMode: 'local',
				       	displayField: 'name',
				    	//valueNotFoundText : '%',
	//			       	value : '',
				       	valueField: 'code',
				       	listeners : {
							afterrender : function(t ,e){
								requestStore.getProxy().api.read = ALF_CONTEXT+'/admin/main/master/list';
								requestStore.getProxy().extraParams = {
						      		t : 'CRT'
								};
								requestStore.load({callback:function(){
									requestStore.insert(0,reqModel);
									t.select(reqModel);
								}});
		
							}
	
						}
	
					}]
			},{
					xtype : 'panel',
					hidden : true,
					itemId : 'p_R08',
					border : 0,
					width : cw,
					layout: {
				        align: 'center',
				        type: 'vbox'
		        	},
					items : [{
						xtype : 'combo',
						width : cw,
						editable : false,
				    	allowBlank : true,
					    fieldLabel: '\u0E40\u0E23\u0E35\u0E22\u0E07\u0E25\u0E33\u0E14\u0E31\u0E1A',
					    labelWidth: lwidth,
				    	//name : 'cmbSort',
				    	itemId : 'cmbSort',
				    	emptyText : '( \u0E40\u0E23\u0E35\u0E22\u0E07\u0E25\u0E33\u0E14\u0E31\u0E1A )',
				    	store : sortStore,
					    queryMode: 'local',
				       	displayField: 'name',
//				       	value : '',
				       	valueField: 'flag2'
					}]
				},{
					xtype : 'panel',
					hidden : true,
					itemId : 'p_R09',
					border : 0,
					width : cw,
					layout: {
				        align: 'center',
				        type: 'vbox'
		        	},
					items : [{
						xtype : 'textfield',
						fieldLabel: '\u0E1B\u0E35\u0E07\u0E1A\u0E1B\u0E23\u0E30\u0E21\u0E32\u0E13',
						labelWidth: lwidth,
						name : 'yearBudget',
				    	itemId : 'yearBudget',
				    	width : cw
					},{
						xtype : 'combo',
						width : cw,
						editable : false,
				    	allowBlank : true,
					    fieldLabel: '\u0E43\u0E19/\u0E19\u0E2D\u0E01\u0E1B\u0E23\u0E30\u0E40\u0E17\u0E28',
					    labelWidth: lwidth,
				    	name : 'inout',
				    	itemId : 'inout',
				    	emptyText : '( \u0E43\u0E19\u0E1B\u0E23\u0E30\u0E40\u0E17\u0E28/\u0E19\u0E2D\u0E01\u0E1B\u0E23\u0E30\u0E40\u0E17\u0E28 )',
				    	store : inoutStore,
					    queryMode: 'local',
				       	displayField: 'name',
				       	value : '',
				       	valueField: 'flag2'
					},{
						xtype : 'textfield',
						fieldLabel: '\u0E0A\u0E37\u0E48\u0E2D\u0E1C\u0E39\u0E49\u0E2D\u0E1A\u0E23\u0E21',
						labelWidth: lwidth,
						name : 'nameTrain',
				    	itemId : 'nameTrain',
				    	width : cw
					}]
				}]
			},{
				region:'center',
				xtype:'panel',
				items : [{
					xtype : 'grid',
					columns : C_R01,
					itemId : 'reportGrid'
					//title : 'CAR',
					//tbar : []
			
				}]
				
			}]
		});		
		
	    this.callParent(arguments);
	}
    
});
