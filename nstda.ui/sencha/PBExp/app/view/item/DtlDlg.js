Ext.define('PBExp.view.item.DtlDlg', {
	extend : 'Ext.window.Window',
	alias : 'widget.expBrwItemDtlDlg',
	
	initComponent: function(config) {
		var me = this;
		
		var actGrpId = me.rec ? me.rec.get("actGrpId") : null;
		
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
		astore.load({params:{actGrpId:actGrpId}});
		
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
			query:getLang()+' ',
			actId:me.rec ? me.rec.get("actId") : 0
		}
		agstore.load({params:{id:actGrpId}});
		
		var cstore = Ext.create('PB.store.common.ComboBoxStore',{
			autoLoad:false,
			listeners:{
				load:function(st, recs) {
					me.fireEvent("cond1Load", recs);
				}
			}
		});
		cstore.getProxy().api.read = ALF_CONTEXT+'/admin/main/expenserule/listDistinct';
		if (me.rec) {
			cstore.getProxy().extraParams = {
				id:me.rec.get("actId")
			}
			cstore.load();
		}

		var store = Ext.create('PBExp.store.item.ActivityGridStore',{autoLoad:false});
		if (me.rec) {
			store.getProxy().extraParams = {
				id:me.rec.get("actId"),
				cond:me.rec.get("condition1")
			}
			store.load();
		}
		
		var lbw = 140;
		
		console.log("isSmallAmount:"+me.trec.isSmallAmount);
		
		Ext.applyIf(me, {
		        renderTo : Ext.getBody(),
	            modal: true,
	            width: 800,
	            height: 530,
	            layout: 'border',
	            resizable: false,
		        items : [{
		        	region:'north',
		        	xtype : 'form',
			        itemId : 'formDetail',
			        border : 0,
			        height:200,
			        items:[{
						xtype: 'hidden',
						name: 'id',
						value : me.rec ? me.rec.get("id") : null
					},{
						xtype:'combo',
						name:'actId',
						fieldLabel:mandatoryLabel(PBExp.Label.i.act),
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
		    	       	    },
							afterrender:function(cmb) {
								Ext.defer(function(){
									cmb.focus();
								},100);
		    	       	    },
		    	       	    change:function(combo,newV,oldV) {
		    	       	    	me.fireEvent("changeActivity",combo, newV, oldV);
		    	       	    }
						},
						value:me.rec ? me.rec.get("actId") : null,
						disabled:me.trec ? me.trec.isSmallAmount : false
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
							fieldLabel:mandatoryLabel(PBExp.Label.i.actGrp),
					    	displayField:'name',
					    	valueField:'id',
					        emptyText : PB.Label.m.select,
					        store: agstore,
			//		        queryMode: 'local',
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
			    	       	    }
							},
							value:me.rec ? me.rec.get("actGrpId") : null,
							flex:1,
							disabled:me.trec ? me.trec.isSmallAmount : false
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
						xtype:'textfield',
						name:'activity',
						fieldLabel:mandatoryLabel(PBExp.Label.i.desc),
				        anchor:"-10",
						labelWidth:lbw,
						margin: '10 0 0 10',
						allowBlank:false,
						value:me.rec ? me.rec.get("activity") : null,
						maxLength:255,
						disabled:me.trec ? me.trec.isSmallAmount : false
					},{
						xtype:'combo',
						name:'assetRuleId',
						fieldLabel:PBExp.Label.i.asset,
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
						value:me.rec ? me.rec.get("assetRuleId") : null,
						disabled:me.trec ? me.trec.isSmallAmount : false
					},{
						xtype:'combo',
						name:'condition1',
						fieldLabel:mandatoryLabel(PBExp.Label.i.cond),
				    	displayField:'name',
				    	valueField:'name',
				        emptyText : PB.Label.m.select,
				        store: cstore,
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
							},
							select : function(combo, rec){
		    	       		    me.fireEvent("selectCond1",combo, rec);
		    	       	    }
						},
						value:me.rec ? me.rec.get("condition1") : null,
						disabled:me.trec ? me.trec.isSmallAmount : false
					},{
					    xtype: 'numericfield',
					    fieldLabel : mandatoryLabel(PBExp.Label.i.amt), 
					    labelWidth: lbw,
		//			    anchor:"-10",
						width:300,
					    hideTrigger:true,
					    name : 'amount',
					    msgTarget: 'side',
					    margin: '10 0 0 10',
					    allowBlank:false,
					    minValue:0,
						value : me.rec ? me.rec.get("amount") : null
					}]
		        },{
		        	region:'center',
		        	xtype:'grid',
		        	margin:'5 0 0 0',
		        	columns:[
		        	     { text:PBExp.Label.i.desc, dataIndex: 'activity_name', flex:1 },
		        	     { text:PBExp.Label.i.cond, dataIndex: 'condition_1', width:120},
		        	     { text:PBExp.Label.i.pos, dataIndex: 'position', flex: 1 },
		        	     { text:PBExp.Label.i.amtAllow, dataIndex: 'amount', width:110, align:'right', xtype:"numbercolumn", format:DEFAULT_MONEY_FORMAT }
		        	],
		        	store:store
		        }],
		        buttons : [{
		          text: PB.Label.m.ok, 
	//	          disabled : true,
		          action : 'ok',
		          itemId: 'okButton',
		          iconCls:'icon_ok'
		        },{
		          text: PB.Label.m.cancel,
		          handler:this.destroy,
		          scope:this,
		          iconCls:'icon_no'
		        }]
		});
		
        this.callParent(arguments);
	}
});		