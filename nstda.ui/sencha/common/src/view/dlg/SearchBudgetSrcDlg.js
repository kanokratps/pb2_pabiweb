Ext.define('PB.view.common.SearchBudgetSrcDlg', {
	extend : 'Ext.window.Window',
    alias:'widget.searchBudgetSrcDlg',
    
	initComponent: function(config) {
		var me = this;
		
		var lbw = 140;
		
		var fundStore = Ext.create('PB.store.common.ComboBoxStore',{
			autoLoad:false,
			listeners:{
				load:function(store, rec) {
					me.fireEvent("loadFund", store, rec);
				}
			}
		});
		fundStore.getProxy().api.read = ALF_CONTEXT+'/admin/main/fund/list';
		fundStore.getProxy().extraParams = {
			lang:getLang()
		}
//		fundStore.load();
		
		var store = Ext.create('PB.store.common.BudgetSrcStore');
		
		Ext.applyIf(me, {
	        renderTo : Ext.getBody(),
	        modal: true,
	        width: 800,
	        height: 500,
	        layout: 'border',
	        resizable: false,
	        items : [{
	        	region:'north',
	        	height:70,
	        	xtype : 'form',
		        itemId : 'searchBudgetCc',
		        border : 0,
		        items:[{
		        	xtype:'container',
		        	margin:'5 0 0 10',
		        	layout:{
						type:'hbox',
						align:'middle'
					},
					items:[{
						xtype:'label',
						text:(me.firstLabel ? me.firstLabel : PB.Label.b.name)+":",
						width:lbw
					},{
						xtype:'radio',
						name:'type',
						boxLabel:PB.Label.m.section,
						inputValue:'U',
						margin:'0 0 0 5',
						checked:true,
						listeners:{
							change:function(rad, newV, oldV) {
								if (newV) {
									me.fireEvent("selectRadio",rad, "U");
								}
							}
						}
					},{
						xtype:'radio',
						name:'type',
						boxLabel:PB.Label.m.project,
						inputValue:'P',
						margin:'0 0 0 5',
						checked:false,
						listeners:{
							change:function(rad, newV, oldV) {
								if (newV) {
									me.fireEvent("selectRadio",rad, "P");
								}
							}
						}
					},{
						xtype:'radio',
						name:'type',
						boxLabel:PB.Label.b.invAsset,
						inputValue:'A',
						margin:'0 15 0 5',
						checked:false,
						listeners:{
							change:function(rad, newV, oldV) {
								if (newV) {
									me.fireEvent("selectRadio",rad, "A");
								}
							}
						},
						afterBoxLabelTextTpl: ' <img src="../res/page/img/icon/question.png" class="info_image" data-qtip="ครุภัณฑ์ประจำปีที่ผ่านการพิจารณาอนุมัติจากคณะกรรมการพิจารณากลั่นกรองครุภัณฑ์วิทยาศาสตร์"></img>',
						hidden:me.onlySectProj
					},{
						xtype:'radio',
						name:'type',
						boxLabel:PB.Label.b.constr,
						inputValue:'C',
						margin:'0 0 0 5',
						checked:false,
						listeners:{
							change:function(rad, newV, oldV) {
								if (newV) {
									me.fireEvent("selectRadio",rad, "C");
								}
							}
						},
						hidden:me.onlySectProj
					}]
		        },{
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
			           	listeners: {
			 	 			specialkey: function(field, e){
			 	 				if(e.getKey() == e.ENTER){
			 	 					this.fireEvent("searchBudgetSrc",this);
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
			        	action:'searchBudgetSrc',
			        	text:PB.Label.m.btnSearch,
			        	iconCls:'icon_search',
			        	margin:'5 0 0 10',
		                listeners: {
		                    click: function(){
		                    	this.fireEvent("searchBudgetSrc", this);
		                    }
		                }
					}]
		        }]
	        },{
	        	region:'center',
	        	xtype:'grid',
	        	margin:'5 0 0 0',
	        	columns:[
	        	     { text:'', dataIndex: 'id', width: 50, align:'center', renderer:
	        	    	 function(v) {
	        	    		return '<input type="radio" name="id" value="'+v+'"/>'; 
	        	    	 }
	        	     },
	        	     { text:PB.Label.m.org, dataIndex: 'org', width: 70 },
	        	     { text:PB.Label.b.sectShort, dataIndex: 'type', width: 85 },
	        	     { text:PB.Label.b.sectName, dataIndex: 'name', flex:1 }
	        	],
	        	store:store
	        },{
	        	region:'south',
	        	itemId:'southPanel',
	        	xtype:'form',
	        	items:[{
					xtype:'combo',
					name:'fund',
					fieldLabel:mandatoryLabel(PB.Label.b.fund),
			    	displayField:'name',
			    	valueField:'id',
			        emptyText : PB.Label.m.select,
			        store: fundStore,
			        queryMode: 'local',
			        typeAhead:true,
			        multiSelect:false,
			        forceSelection:true,
			        anchor:"-10",
					labelWidth:lbw,
					margin: '10 0 10 10',
					allowBlank:false,
			        listConfig : {
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
				}],
				hidden:me.onlySectProj
	        }],
	        buttons : [{
	          text: PB.Label.m.btnConfirm, 
	          action : 'ok',
	          iconCls:'icon_ok',
	          listeners: {
	               click: function(){
	                	this.fireEvent("confirmBudgetSrc", this);
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