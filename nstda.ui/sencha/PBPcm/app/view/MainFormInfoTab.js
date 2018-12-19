Ext.define('PBPcm.view.MainFormInfoTab', {
    extend: 'Ext.form.Panel',
    alias:'widget.pcmReqInfoTab',
    
    autoScroll:true,

	initComponent: function(config) {
		var me = this;
		
		var reason = me.rec.reason;
		var reasonOth = null;
		if (reason && reason.indexOf("อื่นๆ")==0) {
			var pos = reason.indexOf(" ");
			reasonOth = reason.substring(pos+1);
			reason = "อื่นๆ";
		}
		
		var store = Ext.create('PB.store.common.ComboBoxStore');
		store.getProxy().api.read = ALF_CONTEXT+'/srcUrl/pcm/objType/list';
		store.getProxy().extraParams = {
			all : false,
			lang : getLang()
		}
		store.load();
		
		var currencyStore = Ext.create('PB.store.common.ComboBoxStore');
		currencyStore.getProxy().api.read = ALF_CONTEXT+'/admin/main/currency/list';
		currencyStore.getProxy().extraParams = {
		}
		currencyStore.load();
		
//		var stockStore = Ext.create('PB.store.common.ComboBoxStore');
//		stockStore.getProxy().api.read = ALF_CONTEXT+'/srcUrl/main/master?all=false';
//		stockStore.getProxy().extraParams = {
//			p1 : "type='STOCK'",
//			orderBy : 'code',
//			all : true
//		}
//		stockStore.load();
		
		var purUnitStore = Ext.create('PB.store.common.ComboBoxStore');
		purUnitStore.getProxy().api.read = ALF_CONTEXT+'/srcUrl/main/master?all=false';
		purUnitStore.getProxy().extraParams = {
			p1 : "type='PU'",
			orderBy : 'code',
			all : true
		}
		purUnitStore.load();
		
		var prototypeTypeStore = Ext.create('PB.store.common.ComboBoxStore');
		prototypeTypeStore.getProxy().api.read = ALF_CONTEXT+'/srcUrl/main/master?all=false';
		prototypeTypeStore.getProxy().extraParams = {
			p1 : "type='PTT'",
			orderBy : 'code',
			all : true,
			lang : getLang()
		}
		prototypeTypeStore.load();

//		var prototypeStore = Ext.create('PB.store.common.ComboBoxStore',{autoLoad:false});
//		prototypeStore.getProxy().api.read = ALF_CONTEXT+'/admin/main/prototype/list';
//		prototypeStore.getProxy().extraParams = {
//			lang : getLang()
//		}
//		var pttParams = {};
//		if (me.rec.budget_cc_type == "P") {
//			pttParams.t = me.rec.prototype_type;
//			pttParams.p = me.rec.budget_cc;
//		}
//		prototypeStore.load({params:pttParams});
		
		var reasonStore = Ext.create('PB.store.common.ComboBoxStore');
		reasonStore.getProxy().api.read = ALF_CONTEXT+'/srcUrl/main/master?all=false';
		reasonStore.getProxy().extraParams = {
			p1 : "type='PR_RSN'",
			orderBy : 'id',
			all : true,
			lang : getLang()
		}
		reasonStore.load();
		
		var lbw = parseInt(PBPcm.Label.n.lbw);
		
		Ext.applyIf(me, {
			items:[{
				xtype:'container',
				layout:'hbox',
				anchor:'-10',
				items:[{
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
					width:lbw+110,
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
	//		},{
	//			xtype:'container',
	//			margin:'5 0 0 10',
	//			layout:{
	//				type:'hbox',
	//				align:'middle'
	//			},
	//			items:[{
	//				xtype:'label',
	//				text:'ประเภทการซื้อ:',
	//				width:lbw
	//			},{
	//				xtype:'radio',
	//				name:'isStock',
	//				boxLabel:'เข้า  Stock',
	//				inputValue:'1',
	//				margin:'0 0 0 5',
	//				checked:replaceIfNull(me.rec.is_stock, "0") == "1" 
	//			},{
	//				xtype:'combo',
	//				name:'stockOu',
	//				hideLabel:true,
	//		    	displayField:'name',
	//		    	valueField:'id',
	//		        emptyText : "",
	//		        store: stockStore,
	//		        queryMode: 'local',
	//		        typeAhead:true,
	//		        multiSelect:false,
	//		        forceSelection:true,
	//				width:320,
	//				margin:"5 0 0 10",
	//				value:replaceIfNull(me.rec.stock_ou, null),
	//				readOnly:true
	//			},{
	//				xtype:'radio',
	//				name:'isStock',
	//				boxLabel:'ไม่เข้า Stock',
	//				inputValue:'0',
	//				margin:'0 0 0 20',
	//				checked:replaceIfNull(me.rec.is_stock, "0") == "0",
	//				listeners:{
	//					change:function(rad, newV) {
	//						if (newV) {
	//							me.fireEvent("notStock",rad);
	//						}
	//					}
	//				}
	//			}]
			},{
				xtype:'container',
				layout:'hbox',
				margin:"5 0 0 10",
				anchor:"-10",
				items:[{
					xtype:'combo',
					name:'objectiveType',
					fieldLabel:mandatoryLabel(PBPcm.Label.n.objType),
					errLabel:PBPcm.Label.n.err_objType, 
			    	displayField:'name',
			    	valueField:'id',
			        emptyText : PB.Label.m.select,
			        store: store,
			        queryMode: 'local',
			        typeAhead:true,
			        multiSelect:false,
			        forceSelection:true,
					width:lbw+275,
					labelWidth:lbw,
					allowBlank:false,
			        listConfig : {
			    		resizable:true,
				    	minWidth:250,
				    	width:250,
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
	    	       		   me.fireEvent("selectObjective",combo, newValue, oldValue);
	    	       	    }
					},
					value:replaceIfNull(me.rec.objective_type, null),
					readOnly:me.rec.viewing,
					fieldStyle:me.rec.viewing ? READ_ONLY : ""
				},{
					xtype:'textfield',
					name:'objective',
					fieldLabel:mandatoryLabel(PBPcm.Label.n.obj),
					labelWidth:160,
					margin:"0 0 0 15",
					flex:1,
					allowBlank:false,
					value:replaceIfNull(me.rec.objective, null),
					maxLength:255,
					readOnly:me.rec.viewing,
					fieldStyle:me.rec.viewing ? READ_ONLY : ""
//					,afterLabelTextTpl: ' <img src="../res/page/img/icon/question.png" class="info_image" data-qtip="ระบุรายการ จำนวน เพื่ออะไร"></img>'
				}]
			},{
				xtype:'container',
				layout:'hbox',
				margin:"5 0 0 10",
				anchor:"-10",
				items:[{
					xtype:'combo',
					name:'reason',
					fieldLabel:mandatoryLabel(PBPcm.Label.n.reason),
			    	displayField:'name',
			    	valueField:'id',
			        emptyText : PB.Label.m.select,
			        store: reasonStore,
			        queryMode: 'local',
			        typeAhead:true,
			        multiSelect:false,
			        forceSelection:true,
					width:lbw+275,
					labelWidth:lbw,
					allowBlank:false,
	     	        listeners: {
	    	       	   change : function(combo, newValue, oldValue, e){
							me.fireEvent("selectReason",combo, newValue, oldValue);
	    	       	   }
	                },
					value:replaceIfNull(reason, ""),
					readOnly:me.rec.viewing,
					fieldStyle:me.rec.viewing ? READ_ONLY : ""
				},{
					xtype:'textfield',
					name:'reasonOth',
					hideLabel:true,
					fieldLabel:mandatoryLabel(PBPcm.Label.n.reasonOth),
					flex:1,
					margin:"0 0 0 15",
					allowBlank:false,
					value:replaceIfNull(reasonOth, null),
					disabled:replaceIfNull(reason, "") != "อื่นๆ",
					hideTrigger:true,
					maxLength:255,
					readOnly:me.rec.viewing,
					fieldStyle:me.rec.viewing ? READ_ONLY : ""
				}]
			},{
				xtype:'container',
				border:0,
				layout:'hbox',
				items:[{
					xtype:'combo',
					name:'currency',
					fieldLabel:mandatoryLabel(PBPcm.Label.n.currency),
			    	displayField:'name',
			    	valueField:'id',
			        emptyText : PB.Label.m.select,
			        store: currencyStore,
			        queryMode: 'local',
			        typeAhead:true,
			        multiSelect:false,
			        forceSelection:true,
					width:lbw+110,
					labelWidth:lbw,
					margin:"5 0 0 10",
					allowBlank:false,
	     	        listeners: {
	    	       	   change : function(combo, newValue, oldValue, e){
							if (newValue && newValue.length == 3) {
								me.fireEvent("selectCurrency",combo, newValue.toUpperCase(), oldValue);
							}
	    	       	   }
	                },
					value:replaceIfNull(me.rec.currency, "THB"),
					readOnly:me.rec.viewing,
					fieldStyle:me.rec.viewing ? READ_ONLY : ""
				},{
					xtype:'numberfield',
					name:'currencyRate',
					fieldLabel:mandatoryLabel(PBPcm.Label.n.currencyRate),
					labelWidth:160,
					width:250,
					margin:"5 0 0 15",
					allowBlank:false,
					value:replaceIfNull(me.rec.currency_rate, "1"),
					disabled:replaceIfNull(me.rec.currency, "THB") == "THB",
					hideTrigger:true,
					listeners: {
	    	       	   blur : function(txt, e){
							me.fireEvent("changeRate", txt);
	    	       	   }
	                },
					readOnly:me.rec.viewing,
					fieldStyle:me.rec.viewing ? READ_ONLY : ""
				},{
					xtype:'label',
					itemId:'baht',
					text:PB.Label.m.baht,
					disabledCls:'lbl_disabled',
					disabled:replaceIfNull(me.rec.currency, "THB") == "THB",
					margin:"7 0 0 5"
				}]
			},{
				xtype:'container',
				layout:'hbox',
				items:[{
					xtype:'checkbox',
					name:'isSmallAmount',
					boxLabel:PBPcm.Label.n.isSA,
					inputValue:'1',
					margin:'5 0 0 10',
					width:lbw + 200,
					checked:replaceIfNull(me.rec.is_small_amount, "0") == "1",
					value:replaceIfNull(me.rec.is_small_amount, "0") == "1",
					listeners:{
						change:function(chk, newV) {
							me.fireEvent("isSmallAmount",chk, newV);
						}
					},
					readOnly:me.rec.viewing,
					disabled:replaceIfNull(me.rec.budget_cc_type, null)=='C' || replaceIfNull(me.rec.budget_cc_type, null)=='A' || replaceIfNull(me.rec.is_across_budget, "0") == "1"
				}]
			},{
				xtype:'container',
				layout:'hbox',
				items:[{
					xtype:'checkbox',
					name:'isAcrossBudget',
					boxLabel:PBPcm.Label.n.isAB,
					inputValue:'1',
					margin:'5 0 0 10',
					width:lbw + 200,
					checked:replaceIfNull(me.rec.is_across_budget, "0") == "1",
					listeners:{
						change:function(chk, newV) {
							me.fireEvent("isAcrossBudget",chk, newV);
						}
					},
					readOnly:me.rec.viewing,
					disabled:replaceIfNull(me.rec.is_small_amount, "0") == "1"
//				},{
//					xtype:'numericfield',
//					fieldLabel:mandatoryLabel(PBPcm.Label.n.total),
//					errLabel:PBPcm.Label.n.err_total,
//					labelWidth:110,
//					margin:"5 0 0 5",
//					width:315,
//					name:'acrossBudget',
//					value:replaceIfNull(me.rec.across_budget, null),
//					allowBlank:false,
//					disabled:replaceIfNull(me.rec.is_across_budget, "0") != "1",
//					hideTrigger:true,
//					listeners:{
//						blur:function(txt) {
//							me.fireEvent("acrossBudgetBlur",txt);
//						}
//					}
				}]
			},{
				xtype:'container',
				layout:'hbox',
				items:[{
					xtype:'checkbox',
					name:'isRefId',
					boxLabel:PBPcm.Label.n.isRefId,
					inputValue:'1',
					margin:"5 0 0 10",
					width:lbw,
					checked:replaceIfNull(me.rec.is_ref_id, "0") == "1",
					listeners:{
						change:function(chk, newV) {
							me.fireEvent("isRefId",chk, newV);
						}
					},
					readOnly:me.rec.viewing
				},{
					xtype:'trigger',
					name:'refId',
					fieldLabel:mandatoryLabel(PBPcm.Label.n.refId),
					errLabel:PBPcm.Label.n.err_refId,
					labelWidth:110,
					margin:"5 0 0 5",
					width:315,
					triggerCls:'x-form-search-trigger',
					onTriggerClick:function(evt) {
						me.fireEvent("selectPR");
					},
					value:replaceIfNull(me.rec.ref_id, null),
					allowBlank:false,
					editable:false,
					disabled:replaceIfNull(me.rec.is_ref_id, "0") != "1",
					readOnly:me.rec.viewing,
					fieldStyle:me.rec.viewing ? READ_ONLY : ""
				}]
			},{
				xtype:'container',
				margin:'5 0 0 10',
				layout:{
					type:'hbox',
					align:'middle'
				},
				items:[{
					xtype:'label',
					html:mandatoryLabel(PBPcm.Label.n.isPtt),
					width:lbw
				},{
					xtype:'radio',
					itemId:'isPrototypeNo',
					name:'isPrototype',
					boxLabel:PB.Label.m.no,
					inputValue:'0',
					margin:'0 0 0 0',
//					fieldLabel:mandatoryLabel('การจัดซื้อ / จัดจ้างครั้งนี้เป็นรายการครุภัณฑ์ต้นแบบ'),
					errLabel:PBPcm.Label.n.err_isPtt,
					hideLabel:true,
					checked:replaceIfNull(me.rec.is_prototype, null) == "0",
					listeners:{
						change:function(rad, newV) {
							if (newV) {
								me.fireEvent('selectIsPrototype', rad, false);
							}
						}
					},
					invalidCls:'x-radio-invalid',
					readOnly:me.rec.viewing
				},{
					xtype:'radio',
					itemId:'isPrototypeYes',
					name:'isPrototype',
					boxLabel:PB.Label.m.yes,
					inputValue:'1',
					margin:'0 0 0 5',
					checked:replaceIfNull(me.rec.is_prototype, "0") == "1",
					listeners:{
						change:function(rad, newV) {
							if (newV) {
								me.fireEvent('selectIsPrototype', rad, true);
							}
						}
					},
					invalidCls:'x-radio-invalid',
					readOnly:me.rec.viewing
				},{
					xtype:'combo',
					name:'prototypeType',
					hideLabel:true,
					fieldLabel:mandatoryLabel(PBPcm.Label.n.prototypeType),
			    	displayField:'name',
			    	valueField:'id',
			        emptyText : PB.Label.m.select,
			        store: prototypeTypeStore,
			        queryMode: 'local',
			        editable:false,
			        multiSelect:false,
			        forceSelection:true,
					width:155,
					margin:"5 0 0 10",
					listeners:{
						change:function(cmd, newV, oldV) {
							me.fireEvent('selectPrototypeType', cmd, newV, oldV);
						}
					},
					value:replaceIfNull(me.rec.prototype_type, null),
					allowBlank:false,
					disabled:!(replaceIfNull(me.rec.is_prototype, "0") == "1"),
					readOnly:me.rec.viewing,
					fieldStyle:me.rec.viewing ? READ_ONLY : ""
//				},{
//					xtype:'combo',
//					name:'prototypeNo',
//					fieldLabel:mandatoryLabel(PBPcm.Label.n.prototypeNo),
//					labelWidth:90,
//					errLabel:PBPcm.Label.n.err_prototype_no,
//			    	displayField:'name',
//			    	valueField:'id',
//			        emptyText : PB.Label.m.select,
//			        store: prototypeStore,
//			        queryMode: 'local',
//			        editable:false,
//			        multiSelect:false,
//			        forceSelection:true,
//					flex:1,
//					margin:"5 10 0 10",
//					value:replaceIfNull(me.rec.prototype_no, null) ? parseInt(me.rec.prototype_no) : null,
//					allowBlank:false,
//					disabled:!(replaceIfNull(me.rec.is_prototype, "0") == "1")
				}]
			},{
				xtype:'container',
				layout:'hbox',
				items:[{
					xtype:'label',
					text:'',
					width:lbw
				},{
					xtype:'label',
					html:'<font color="blue">ครุภัณฑ์ต้นแบบ/ส่งมอบ คือ ครุภัณฑ์ที่นำมาดัดแปลงหรือประกอบเป็นต้นแบบตามแผนงานในโครงการที่ได้รับอนุมัติ<br/>หรือ ครุภัณฑ์ที่จัดหามาเพื่อดำเนินการส่งมอบให้กับหน่วยงานอื่นๆตามเอกสารหรือสัญญาที่สำนักงานได้ลงนามไว้</font>',
					anchor:'-10',
					margin:'5 0 0 10'
				}]
			},{
				xtype:'container',
				layout:'hbox',
				anchor:'-10',
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
					fieldLabel:PBPcm.Label.n.cc,
					width:lbw+160,
					labelWidth:lbw,
					margin:"5 0 0 10",
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
					margin:'5 0 0 10',
					value:replaceIfNull(me.rec.cost_control_name, null),
					readOnly:true,
					fieldStyle:READ_ONLY
				}]
			},{
				xtype:'hidden',
				name:'pcmOu',
				value:replaceIfNull(me.rec.pcm_ou, null)
//			}{
//				xtype:'combo',
//				name:'pcmOu',
//				fieldLabel:'หน่วยงานที่จัดซื้อ/จัดจ้าง',
//		    	displayField:'name',
//		    	valueField:'id',
//		        emptyText : PB.Label.m.select,
//		        store: purUnitStore,
//		        queryMode: 'local',
//		        typeAhead:true,
//		        multiSelect:false,
//		        forceSelection:true,
//				width:554,
//				margin:"5 0 0 10",
//				labelWidth:lbw,
//				value:replaceIfNull(me.rec.pcm_ou, null)
			},{
				xtype:'datefield',
				name:'contractDate',
				fieldLabel:mandatoryLabel(PBPcm.Label.n.contractDate),
				labelWidth:lbw,
				margin:"5 0 0 10",
				width:lbw+160,
				format:'d/m/Y',
				value:me.rec.contract_date ? new Date(me.rec.contract_date) : null,
				allowBlank:false,
				readOnly:me.rec.viewing,
				fieldStyle:me.rec.viewing ? READ_ONLY : ""
			},{
				xtype:'textarea',
				name:'location',
				fieldLabel:PBPcm.Label.n.loc,
				labelWidth:lbw,
				anchor:'-10',
				height:60,
				margin:"5 0 0 10",
				value:replaceIfNull(me.rec.location, null),
				maxLength:255,
				readOnly:me.rec.viewing,
				fieldStyle:me.rec.viewing ? READ_ONLY : ""
			},{
				xtype:'label',
				name:'rid',
				style:'color:white',
				text:me.rec.rid
			}]
		});
		
	    this.callParent(arguments);
	},
	
	budgetSrcTypeName:function(budgetSrcType) {
		switch(budgetSrcType) {
			case 'U':return PB.Label.m.section;break;
			case 'P':return PB.Label.m.project;break;
			case 'A':return PB.Label.m.asset;break;
			case 'C':return PB.Label.m.construction;break;
			default:
				return '';
		}
	}
    
});
