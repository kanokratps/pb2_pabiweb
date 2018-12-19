Ext.define('PBPcm.view.item.DtlDlg', {
	extend : 'Ext.window.Window',
	alias : 'widget.pcmItemDtlDlg',
	
	initComponent: function(config) {
		var me = this;
		
		var actGrpId = me.rec ? me.rec.get("actGrpId") : null;
		
		var fstore = Ext.create('PB.store.common.ComboBoxStore',{
			autoLoad:false,
			sorters: [{
			    property: 'name',
			    direction: 'ASC'
			}]
		});
		fstore.getProxy().api.read = ALF_CONTEXT+'/admin/main/fiscalyear/list';
		fstore.load();		
		
		var assetStore = Ext.create('PB.store.common.ComboBoxStore',{
			autoLoad:false,
			sorters: [{
		         property: 'name',
		         direction: 'ASC'
		    }]
		});
		assetStore.getProxy().api.read = ALF_CONTEXT+'/admin/main/asset/rule/list';
		assetStore.getProxy().extraParams = {
			query:getLang()+' '
		}
		if (me.trec.budgetCcType == 'P') {
			assetStore.getProxy().extraParams.fundId = me.trec.fundId;
			assetStore.getProxy().extraParams.projectId = me.trec.budgetCc;
		}
		assetStore.load(function(recs) {
			me.fireEvent("assetStoreLoad", recs.length);
		});
		
		var astore = Ext.create('PB.store.common.ComboBoxStore',{
			autoLoad:false,
			sorters: [{
		         property: 'name',
		         direction: 'ASC'
		    }]
		});
		astore.getProxy().api.read = ALF_CONTEXT+'/admin/main/activity/list';
		astore.getProxy().extraParams = {
			query:getLang()+' '
		}
		astore.load();
		
		var agstore = Ext.create('PB.store.common.ComboBoxStore',{
			autoLoad:false,
			sorters: [{
		         property: 'name',
		         direction: 'ASC'
		    }],
		    listeners: {
		        load: {
		            fn: function(cmb,recs){
		        		var rec = null;
		                for(var a in recs) {
		                	if(recs[a].data.id == actGrpId) {
		                		rec = recs[a];
		                	}
		                }
		                
		                if (rec) {
							var helpAG = document.getElementById("help-ag");
							helpAG.setAttribute("data-qtip",rec.data.data.description);
		                }
		            }
		        },
		        exception: function(misc) {
		            console.log("fail!");
		        }
		    }
		});
		agstore.getProxy().api.read = ALF_CONTEXT+'/admin/main/activity/group/list';
		agstore.getProxy().extraParams = {
			query : getLang()+' '
		}
		agstore.load();
		
		var store = Ext.create('PB.store.common.ComboBoxStore',{autoLoad:false});
		store.getProxy().api.read = ALF_CONTEXT+'/admin/main/product/uom/list';
		store.getProxy().extraParams = {
		}
		store.load();
		
		var lbw = 170;
		
		Ext.applyIf(me, {
		        renderTo : Ext.getBody(),
	            modal: true,
	            width: 520,
	            height: 370,
	            layout: 'fit',
	            resizable: false,
	            items : [{
		        	xtype : 'form',
			        itemId : 'formDetail',
			        border : 0,
			        items:[{
						xtype: 'hidden',
						name: 'id'
					 },{
						 xtype:'hidden',
						 name:'isEqmt',
						 value:'0'
//					 },{
//					    xtype: 'radiogroup',
//					    itemId:'isEqmt',
//					    fieldLabel : 'ครุภัณฑ์',
//					    columns:2,
//					    labelWidth: lbw,
//					    msgTarget: 'side',
//					    margin: '10 0 0 10',
//					    anchor:"-10",
//					    items:[
//					           {name:'isEqmt',boxLabel:'ไม่ใช่',inputValue:'0',checked:true},
//					           {name:'isEqmt',boxLabel:'ใช่',inputValue:'1'}
//					    ]
					},{
						xtype:'combo',
						name:'fiscalYear',
						fieldLabel:mandatoryLabel(PBPcm.Label.t.fiscalYear),
				    	displayField:'name',
				    	valueField:'name',
				        emptyText : PB.Label.m.select,
				        store: fstore,
//				        queryMode: 'local',
				        typeAhead:true,
				        multiSelect:false,
				        forceSelection:true,
				        anchor:"-10",
						labelWidth:lbw,
						margin: '10 0 0 10',
						allowBlank:false,
				        listConfig : {
						    getInnerTpl: function () {
								return '<div>{name}</div>';
						        //return '<div>{name}<tpl if="id != \'\'"> ({id})</tpl></div>';
						    }
						},
				        listeners:{
							beforequery : function(qe) {
								qe.query = getLang()+" "+qe.query;
							},
							afterrender:function(cmb) {
								Ext.defer(function(){
									cmb.focus();
								},300);
							}
						},
					    hidden:!me.acrossBudget,
					    disabled:!me.acrossBudget
					},{
						xtype:'combo',
						name:'actId',
						fieldLabel:mandatoryLabel(PBPcm.Label.t.act),
				    	displayField:'name',
				    	valueField:'id',
				        emptyText : PB.Label.m.select,
				        store: astore,
		//		        queryMode: 'local',
				        typeAhead:true,
				        multiSelect:false,
				        forceSelection:true,
				        anchor:"-10",
						labelWidth:lbw,
						margin: '10 0 0 10',
						allowBlank:false,
				        listConfig : {
					    	resizable:true,
					    	minWidth:lbw+100,
					    	width:lbw+200,
						    getInnerTpl: function () {
								return '<div>{name}</div>';
						        //return '<div>{name}<tpl if="id != \'\'"> ({id})</tpl></div>';
						    }
						},
				        listeners:{
							beforequery : function(qe) {
								qe.query = getLang()+" "+qe.query;
							},
							select : function(combo, rec){
		    	       		    me.fireEvent("selectActivity",combo, rec);
		    	       	    }
						},
						value:me.rec ? me.rec.get("actId") : null
					},{
						xtype:'container',
						layout: {
					        type: 'hbox',
					        align: 'stretch'
					    },
					    anchor:"-10",
						items:[{
							xtype:'combo',
							name:'actGrpId',
							fieldLabel:mandatoryLabel(PBPcm.Label.t.actGrp),
					    	displayField:'name',
					    	valueField:'id',
					        emptyText : PB.Label.m.select,
					        store: agstore,
	//				        queryMode: 'local',
					        typeAhead:true,
					        multiSelect:false,
					        forceSelection:true,
					        anchor:"-10",
							labelWidth:lbw,
							margin: '10 0 0 10',
							allowBlank:false,
					        listConfig : {
							    getInnerTpl: function () {
									return '<div>{name}</div>';
							        //return '<div>{name}<tpl if="id != \'\'"> ({id})</tpl></div>';
							    }
							},
					        listeners:{
								beforequery : function(qe) {
									qe.query = getLang()+" "+qe.query;
								},
								select : function(combo, rec){
			    	       		    me.fireEvent("selectActivityGroup",combo, rec);
			    	       	    },
								afterrender:function(cmb) {
									Ext.defer(function(){
										cmb.focus();
									},100);
								}
							},
							flex:1
//						,value:me.rec ? me.rec.get("actGrpId") : null
						},{
							xtype:'displayfield',
							fieldLabel:' ',
							labelSeparator:'',
							labelWidth:30,
							margin: '8 0 0 4',
							afterLabelTpl: '<img id="help-ag" src="../res/page/img/icon/question.png" class="info_image" data-qtip="คำอธิบาย Activity Group"></img>',
							value:'',
							width:20
						}]
					},{
					    xtype: 'textfield',
					    fieldLabel : mandatoryLabel(PBPcm.Label.t.name), 
					    labelWidth: lbw,
					    anchor:"-10",
					    hideTrigger:true,
					    name : 'desc',
					    msgTarget: 'side',
					    margin: '10 0 0 10',
					    allowBlank:false,
					    maxLength:255
					},{
						xtype:'combo',
						name:'assetRuleId',
						fieldLabel:PBPcm.Label.t.asset,
				    	displayField:'name',
				    	valueField:'id',
				        emptyText : PB.Label.m.select,
				        store: assetStore,
		//		        queryMode: 'local',
				        typeAhead:true,
				        multiSelect:false,
				        forceSelection:true,
				        anchor:"-10",
						labelWidth:lbw,
						margin: '10 0 0 10',
						allowBlank:true,
				        listConfig : {
						    getInnerTpl: function () {
								return '<div>{name}&nbsp;</div>';
						        //return '<div>{name}<tpl if="id != \'\'"> ({id})</tpl></div>';
						    }
						},
				        listeners:{
							beforequery : function(qe) {
								qe.query = getLang()+" "+qe.query;
							},
							select : function(combo, rec){
		    	       		    me.fireEvent("selectAsset",combo, rec);
		    	       	    }
						},
						value:me.rec ? me.rec.get("assetRuleId") : null
					},{
					    xtype: 'numericfield',
					    fieldLabel : mandatoryLabel(PBPcm.Label.t.qty), 
					    labelWidth: lbw,
					    anchor:"-10",
					    hideTrigger:true,
					    name : 'qty',
					    msgTarget: 'side',
					    margin: '10 0 0 10',
					    allowBlank:false,
					    minValue:0
					},{
						xtype:'combo',
						name:'unit',
						fieldLabel:mandatoryLabel(PBPcm.Label.t.uom),
				    	displayField:'name',
				    	valueField:'id',
				        emptyText : PB.Label.m.select,
				        store: store,
				        queryMode: 'local',
				        typeAhead:true,
				        multiSelect:false,
				        forceSelection:true,
				        anchor:"-10",
						labelWidth:lbw,
						margin: '10 0 0 10',
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
					},{
					    xtype: 'numericfield',
					    fieldLabel : mandatoryLabel(PBPcm.Label.t.dlg_prc), 
					    labelWidth: lbw,
					    anchor:"-10",
					    hideTrigger:true,
					    name : 'prc',
					    msgTarget: 'side',
					    margin: '10 0 0 10',
					    allowBlank:false,
					    maxLength:255,
					    minValue:0
					}],
			        buttons : [{
			          text: PB.Label.m.save, 
//			          disabled : true,
			          action : 'ok',
			          itemId: 'okButton',
			          iconCls:'icon_ok'
			        },{
			          text: PB.Label.m.cancel,
			          handler:this.destroy,
			          scope:this,
			          iconCls:'icon_no'
			        }]
	            }]
		});
		
        this.callParent(arguments);
	}
});		