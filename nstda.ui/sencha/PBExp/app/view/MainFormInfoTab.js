Ext.define('PBExp.view.MainFormInfoTab', {
    extend: 'Ext.form.Panel',
    alias:'widget.expBrwInfoTab',
    
    autoScroll:true,

	initComponent: function(config) {
		var me = this;
		
		var store = Ext.create('PB.store.common.ComboBoxStore');
		store.getProxy().api.read = ALF_CONTEXT+'/srcUrl/main/master?all=false';
		store.getProxy().extraParams = {
			p1 : "type='BRW_TYPE'",
			orderBy : 'flag1',
			all : true,
			lang : getLang()
		}
		store.load();
		
//		var typeStore = Ext.create('PB.store.common.ComboBoxStore');
//		typeStore.getProxy().api.read = ALF_CONTEXT+'/admin/main/activity/list';
//		typeStore.getProxy().extraParams = {
//		    name : '',
//			all : true
//		}
//		typeStore.load();

		var prStore = Ext.create('PBExp.store.OldComboStore');
		prStore.getProxy().api.read = ALF_CONTEXT+'/pcm/req/old/list';
		prStore.getProxy().extraParams = {
			r : replaceIfNull(me.rec.req_by, null),
			bg : replaceIfNull(me.rec.budget_cc, 0),
			bgt : replaceIfNull(me.rec.budget_cc_type, null),
			f : replaceIfNull(me.rec.fund_id, 0),
			avid : replaceIfNull(me.rec.id, 0)
		}
		prStore.load();
		
		var bankStore = Ext.create('PB.store.common.ComboBoxStore');
		bankStore.getProxy().api.read = ALF_CONTEXT+'/admin/main/bank/list';
		bankStore.getProxy().extraParams = {
			lang:getLang()
		}
		bankStore.load();
		
		var ogridStore = Ext.create('PBExp.store.OldGridStore',{storeId:'oldExpBrwGridStore',autoLoad:false});
		
		var lbw = 140;
		
		var ocolumns = [{
	        dataIndex: 'ord',
	    	text: PBExp.Label.n.oweOrder, 
	    	width:80,
	    	align:'center'
	    },{
	        dataIndex: 'number',
        	text: PBExp.Label.n.oweDocNo, 
        	width:115,
        	align:'center'
	    },{
	        dataIndex: 'name',
        	text: PBExp.Label.n.oweName, 
        	flex:1,
        	align:'left'
	    },{
	    	xtype:'numbercolumn',
	        dataIndex: 'waitamt',
        	text: PBExp.Label.n.oweWait, 
        	width:100,
        	align:'right',
        	format:DEFAULT_MONEY_FORMAT        		
	    },{
	    	xtype:'numbercolumn',
	        dataIndex: 'balance',
        	text: PBExp.Label.n.oweBalance, 
        	width:100,
        	align:'right',
        	format:DEFAULT_MONEY_FORMAT
	    }];
		
/*		
		var columns = [{
	        		xtype: 'actioncolumn',
		        	dataIndex: 'action',
		        	text: '',
		        	align:'center',
		            width: 80,
		            items: [{
		                tooltip: 'Edit', 
		                action : 'edit',
		        	    getClass: function(v) {
		        	    	return getActionIcon(v, "E", 'edit');
			            }
		            }, {
		                tooltip: 'Delete', 
		                action : 'del',
		        	    getClass: function(v) {
		        	    	return getActionIcon(v, "D", 'delete');
		        	    }
		            }]
	        	},			
				{ text: PBExp.Label.i.actGrp,  dataIndex: 'actGrpName', flex:1},
				{ text: PBExp.Label.i.act,  dataIndex: 'actName', flex:1},
				{ text: PBExp.Label.i.desc,  dataIndex: 'activity', flex:1},
				{ text: PBExp.Label.i.cond,  dataIndex: 'condition1', flex:1, renderer:function(v){return (v ? v :"")}},
				{ text: PBExp.Label.n.amount,  dataIndex: 'amount', width:180, align:'right', xtype: 'numbercolumn', format:'0,000.00'}
		];
*/		
		
		Ext.applyIf(me, {
			items:[{
				xtype:'container',
				layout:'hbox',
				anchor:'-10',
				items:[{
					xtype:'hidden',
					name:'prTotal',
					value:replaceIfNull(me.rec.pr_total, null)
				},{
					xtype:'hidden',
					name:'fundId',
					value:replaceIfNull(me.rec.fund_id, null)
				},{
					xtype:'hidden',
					name:'budgetCcType',
					value:replaceIfNull(me.rec.budget_cc_type, null)
				},{
					xtype:'hidden',
					name:'budgetCc',
					value:replaceIfNull(me.rec.budget_cc, null)
				},{
					xtype:'trigger',
					name:'budgetCcTypeName',
					fieldLabel:mandatoryLabel(PB.Label.b.name),
					width:lbw+200,
					labelWidth:lbw,
					margin:"5 0 0 10",
					triggerCls:'x-form-search-trigger',
					editable:false,
					onTriggerClick:function(evt) {
						me.fireEvent("selectBudgetCc");
					},
					value:replaceIfNull(me.rec.budget_cc_type_name, ''),
					allowBlank:false,
					readOnly:me.rec.viewing,
					fieldStyle:me.rec.viewing ? READ_ONLY : ""
				},{
					xtype:'button',
					hidden:replaceIfNull(me.rec.budget_cc_type, null) == null,
					iconCls:'icon_money',
					margin:"5 0 0 10",
					text:'',
					action:'showBudget',
					disabled:me.rec.viewing
				},{
					xtype:'textfield',
					name:'budgetCcName',
					hideLabel:true,
					flex:1,
					margin:'5 0 0 10',
					value:replaceIfNull(me.rec.budget_cc_name, ''),
					readOnly:true,
					fieldStyle:READ_ONLY
				},{
					xtype:'textfield',
					name:'fundName',
					hideLabel:true,
					flex:1,
					margin:'5 0 0 10',
					value:replaceIfNull(me.rec.fund_name, ''),
					readOnly:true,
					fieldStyle:READ_ONLY
				}]
			},{
				xtype:'container',
				layout:'hbox',
				margin:"5 0 0 10",
				anchor:"-10",
				items:[{
					xtype:'combo',
					name:'objectiveType',
					fieldLabel:mandatoryLabel(PBExp.Label.n.objType),
					errLabel:PBExp.Label.n.err_objType,
			    	displayField:'name',
			    	valueField:'id',
			        emptyText : PB.Label.m.select,
			        store: store,
			        queryMode: 'local',
			        typeAhead:true,
			        multiSelect:false,
			        forceSelection:true,
					width:lbw+200,
					labelWidth:lbw,
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
	    	       	    change : function(combo, newValue, oldValue, e){
	    	       		   me.fireEvent("selectObjectiveType",combo, newValue, oldValue);
	    	       	    }
					},
					value:replaceIfNull(me.rec.objective_type, null),
					readOnly:me.rec.viewing,
					fieldStyle:me.rec.viewing ? READ_ONLY : ""
				},{
					xtype:'textfield',
					name:'objective',
					fieldLabel:mandatoryLabel(PBExp.Label.n.obj),
					labelWidth:100,
					margin:"0 0 0 15",
					flex:1,
					allowBlank:false,
					value:replaceIfNull(me.rec.objective, null),
					maxLength:255,
					readOnly:me.rec.viewing,
					fieldStyle:me.rec.viewing ? READ_ONLY : ""
				},{
					xtype:'datefield',
					name:'dateBack',
					fieldLabel:mandatoryLabel(PBExp.Label.n.dateBack),
					labelWidth:90,
					margin:"0 0 0 15",
					width:lbw+80,
					format:'d/m/Y',
					value:me.rec.date_back ? new Date(me.rec.date_back) : null,
					allowBlank:false,
					hidden:me.rec.date_back ? false : true,
					disabled:me.rec.date_back ? false : true,
					readOnly:me.rec.viewing,
					fieldStyle:me.rec.viewing ? READ_ONLY : ""
				}]
			},{
				xtype:'container',
				layout:'hbox',
				anchor:'-10',
				margin:'5 0 0 10',
				items:[{
					xtype:'hidden',
					name:'costControlId',
					value:replaceIfNull(me.rec.cost_control_id, null)
				},{
					xtype:'hidden',
					name:'costControlTypeId',
					value:replaceIfNull(me.rec.cost_control_type_id, null)
				},{
					xtype:'trigger',
					name:'costControlTypeName',
					fieldLabel:PBExp.Label.n.cc,
					width:lbw+200,
					labelWidth:lbw,
					trigger1Cls: 'x-form-clear-trigger',
				    trigger2Cls: 'x-form-search-trigger',
				    hideTrigger1: true,
				    hasSearch : false,
					editable:false,
					onTrigger1Click:function(evt) {
						this.triggerEl.item(0).dom.parentNode.style.width = "0px";
						me.fireEvent("clearCostControl");
					},
					onTrigger2Click:function(evt) {
						me.fireEvent("selectCostControl");
					},
					listeners:{
						afterrender:function() {
							var w = this.getValue() ? "17" : "0";
							this.triggerEl.item(0).dom.parentNode.style.width = w+"px";
						},
						change:function(trigger, newV, oldV) {
							var w = newV ? "17" : "0";
							this.triggerEl.item(0).dom.parentNode.style.width = w+"px";
						}
					},
					value:replaceIfNull(me.rec.cost_control_type_name, null),
					readOnly:me.rec.viewing,
					fieldStyle:me.rec.viewing ? READ_ONLY : ""
				},{
					xtype:'textfield',
					name:'costControlName',
					hideLabel:true,
					flex:1,
					margin:'0 0 0 15',
					value:replaceIfNull(me.rec.cost_control_name, null),
					readOnly:true,
					fieldStyle:READ_ONLY
				}]
			},{
				xtype:'container',
				layout:'hbox',
//				anchor:'-10',
			    width : lbw + 560,
				margin:'5 0 0 10',
				items:[{
					xtype:'checkbox',
					name:'isSmallAmount',
					boxLabel:PBExp.Label.n.isSA,
					inputValue:'1',
//					width:lbw+260,
//				    flex:0.55,				
					checked:replaceIfNull(me.rec.is_small_amount, "0") == "1",
					listeners:{
						change:function(chk, newV) {
							me.fireEvent("isSmallAmount",chk, newV);
						}
					},
					readOnly:me.rec.viewing,
					disabled:replaceIfNull(me.rec.budget_cc_type, null)=='C' || replaceIfNull(me.rec.budget_cc_type, null)=='A'
				},{
					xtype:'combo',
					name:'originPrNumber',
					margin:'0 0 0 10',
					hideLabel:true,
					fieldLabel:PBExp.Label.n.prNumber,					
					labelWidth:150,
					store:prStore,
			    	displayField:'id', 
			    	valueField:'id', 
			        queryMode: 'local',
//			        typeAhead:true,
			        multiSelect:false,
			        forceSelection:true,
			        flex:1,
					allowBlank:false,
					editable:false,
			        listConfig : {
						minWidth:500,
					    getInnerTpl: function () {
							return '<div>[{id}] {objective_type_name} {objective} ({total_cnv:number("0,000.00")})</div>';
					        //return '<div>{name}<tpl if="id != \'\'"> ({id})</tpl></div>';
					    }
					},
			        listeners:{
						beforequery : function(qe) {
							qe.query = new RegExp(qe.query, 'i');
			//				qe.forceAll = true;
	    	       	    },
	    	       	    change : function(cmb, newV, oldV, e){
		    	       		me.fireEvent("selectOldPr", cmb, newV, oldV);
		    	       	}
					},
					value:replaceIfNull(me.rec.pay_dtl2, null),
					disabled:replaceIfNull(me.rec.is_small_amount, "0") == "0" || replaceIfNull(me.rec.budget_cc_type, null)=='C' || replaceIfNull(me.rec.budget_cc_type, null)=='A',
					readOnly:me.rec.viewing,
					fieldStyle:me.rec.viewing ? READ_ONLY : ""
				}]
			},{
				xtype:'panel',
				title:PBExp.Label.n.oweTitle,
				layout:'border',
				margin:'5 0 0 0',
				height:140,
				items:[{
					region:'center',
					itemId:'grdOldAV',
					xtype:'grid',
					columns:ocolumns,
					store:ogridStore,
					disabled:me.rec.viewing
				},{
					region:'east',
					xtype:'panel',
					itemId:'oldAV',
					width:500,
					layout:'fit',
					split:true,
					collapsible:true,
					title:mandatoryLabel(PBExp.Label.n.avRemark),
					items:[{
						xtype:'textarea',
						name:'avRemark',
						hideLabel:true,
						fieldLabel:PBExp.Label.n.avRemark,
						margin:"0 0 0 0",
//						fieldLabel:,
//						labelWidth:130,
//						margin:"5 5 5 10",
						value:replaceIfNull(me.rec.av_remark, null),
						allowBlank:false,
						maxLength:255,
						readOnly:me.rec.viewing,
						disabled:me.rec.viewing,
						fieldStyle:me.rec.viewing ? READ_ONLY : ""
					}]
				}]
			},{
				xtype:'panel',
				title:PBExp.Label.n.methodTitle,
				margin:'0 0 0 0',
				border:0,
				items:[{
					xtype:'radio',
					name:'bankType',
					boxLabel:PBExp.Label.n.bankBbl,
					inputValue:'0',
					margin:'5 0 0 10',
					checked:replaceIfNull(me.rec.bank_type, "0") == "0", 
					listeners:{
						change:function(rad, newV, oldV) {
							if (newV) {
								me.fireEvent("selectMainBank",rad, "0");
							}
						}
					},
					readOnly:me.rec.viewing
				},{
					xtype:'container',
					layout:'hbox',
					margin:"0 0 5 5",
					anchor:"-10",
					items:[{
						xtype:'radio',
						name:'bankType',
						boxLabel:PBExp.Label.n.bank,
						inputValue:'1',
						margin:'5 0 0 5',
						checked:replaceIfNull(me.rec.bank_type, "0") == "1",
						listeners:{
							change:function(rad, newV, oldV) {
								if (newV) {
									me.fireEvent("selectMainBank",rad, "1");
								}
							}
						},
						readOnly:me.rec.viewing
					},{
						xtype:'combo',
						name:'bank',
						margin:'5 10 0 5',
						hideLabel:true,
				    	displayField:'name',
				    	valueField:'id',
				        emptyText : PB.Label.m.select,
				        store: bankStore,
//				        queryMode: 'local',
				        typeAhead:true,
				        multiSelect:false,
				        forceSelection:true,
						flex:1,
						labelWidth:lbw,
						allowBlank:true,
				        listConfig : {
						    getInnerTpl: function () {
								return '<div>{name}</div>';
						        //return '<div>{name}<tpl if="id != \'\'"> ({id})</tpl></div>';
						    }
						},
//				        listeners:{
//							beforequery : function(qe) {
//								qe.query = new RegExp(qe.query, 'i');
//				//				qe.forceAll = true;
//		    	       	    }
//						},
						value:replaceIfNull(me.rec.bank, null),
						readOnly:me.rec.viewing,
						fieldStyle:me.rec.viewing ? READ_ONLY : ""
					}]
				},{
					xtype:'label',
					html:'<font color="red">'+PBExp.Label.n.bankWarn+'</font>',
					margin:'5 10 5 15',
					anchor:"-10"
				}]
			}]
		});		
		
	    this.callParent(arguments);

		Ext.apply(ogridStore, {pageSize:PAGE_SIZE});
		ogridStore.load({
			params:{
				r:replaceIfNull(me.rec.req_by, null)
			},
			callback:function(r) {
			    if(!me.rec.viewing) {
			    	me.fireEvent("oldStoreLoad",r);
				}
			}
		});
	},
	
	listeners:{
		added:function() {
			var me = this;
			
			me.fireEvent("selectMainBank", me.down("radio[name=bankType]"), replaceIfNull(me.rec.bank_type, "0"));
			me.fireEvent("isSmallAmount", me.down("field[name=isSmallAmount]"), replaceIfNull(me.rec.is_small_amount, "0")=="1", true);

		}
	}	
    
});
