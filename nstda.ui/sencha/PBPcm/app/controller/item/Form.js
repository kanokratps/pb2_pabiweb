Ext.define('PBPcm.controller.item.Form', {
    extend: 'Ext.app.Controller',

    refs:[{
        ref: 'main',
        selector: 'pcmReqMain'
    },{
        ref: 'mainForm',
        selector: 'pcmReqMainForm'
    },{
        ref: 'dlg',
        selector: 'pcmItemDtlDlg'
    },{
        ref: 'form',
        selector: 'pcmItemDtlDlg [itemId=formDetail]'
    },{
    	ref:'uploadGrid',
		selector:'pcmReqFileTab uploadGrid'
	},{
    	ref:'grid',
		selector:'pcmReqItemTab grid'
    },{
        ref: 'hidId',     
        selector:'pcmItemDtlDlg field[name=id]'
//	},{
//        ref: 'raIsEqmt',
//        selector:'pcmItemDtlDlg [itemId=isEqmt]'
	},{
        ref: 'hidIsEqmt',
        selector:'pcmItemDtlDlg [name=isEqmt]'
    },{
        ref: 'cmbFiscalYear',
        selector:'pcmItemDtlDlg field[name=fiscalYear]'
    },{
        ref: 'cmbActId',
        selector:'pcmItemDtlDlg field[name=actId]'
    },{
        ref: 'cmbActGrpId',
        selector:'pcmItemDtlDlg field[name=actGrpId]'
    },{
        ref: 'cmbAssetRuleId',
        selector:'pcmItemDtlDlg field[name=assetRuleId]'
    },{
        ref: 'txtDesc',
        selector:'pcmItemDtlDlg field[name=desc]'
    },{
        ref: 'cmbUnit',
        selector:'pcmItemDtlDlg field[name=unit]'
    },{
        ref: 'txtQty',
        selector:'pcmItemDtlDlg field[name=qty]'
    },{
        ref: 'txtPrc',
        selector:'pcmItemDtlDlg field[name=prc]'
    },{
        ref: 'txtCurrencyRate',
        selector:'pcmReqInfoTab field[name=currencyRate]'
    },{
        ref: 'hidFundId',
        selector:'pcmReqInfoTab field[name=fundId]'
    },{
        ref: 'hidBudgetCcType',
        selector:'pcmReqInfoTab field[name=budgetCcType]'
    },{
        ref: 'hidBudgetCc',
        selector:'pcmReqInfoTab field[name=budgetCc]'
    },{
        ref: 'lblGrossAmt',
        selector:'pcmReqItemTab label[name=grossAmt]'
    },{
        ref: 'lblVatAmt',
        selector:'pcmReqItemTab label[name=vatAmt]'
    },{
        ref: 'lblNetAmt',
        selector:'pcmReqItemTab label[name=netAmt]'
    },{
        ref: 'lblNetAmtCnv',
        selector:'pcmReqItemTab label[name=netAmtCnv]'
    },{
        ref: 'hidPriceInclude',     
        selector:'pcmReqItemTab field[name=priceInclude]'
    },{
        ref: 'hidVat',     
        selector:'pcmReqItemTab field[name=vat]'
    },{
        ref: 'cmbVatId',     
        selector:'pcmReqItemTab field[name=vatId]'
    },{
        ref: 'hidTotal',     
        selector:'pcmReqItemTab field[name=total]'
    },{
        ref: 'hidTotalCnv',     
        selector:'pcmReqItemTab field[name=totalCnv]'
    },{
        ref: 'cmbObjectiveType',     
        selector: 'pcmReqMainForm field[name=objectiveType]'
    },{
        ref: 'cmbMethod',     
        selector: 'pcmReqMainForm field[name=method]'
	},{
	    ref: 'cmtTab',
	    selector:'pcmReqCmtTab [itemId=cmtTab]'
	},{
	    ref: 'mainCmtTab',
	    selector:'pcmReqCmtTab'
    },{
        ref: 'chkAcrossBudget',
        selector: 'pcmReqMainForm field[name=isAcrossBudget]'
    },{
        ref: 'txtAcrossBudget',
        selector: 'pcmReqMainForm field[name=acrossBudget]'
    }],
    
    init:function() {
		var me = this;
		
		me.control({
			'pcmItemDtlDlg [action=ok]': {
				click : me.ok
			},
			'pcmItemDtlDlg': {
				selectActivityGroup : me.selectActivityGroup,
				selectActivity : me.selectActivity,
				assetStoreLoad : me.assetStoreLoad
			},
			'pcmReqItemTab grid [action=addItem]': {
				click : me.add
			},
			'pcmReqItemTab grid':{
				edit:me.edit,
				del:me.del,
				copy:me.copy,
				calSummary:me.calSummary
			},
			'pcmReqItemTab': {
				selectVat : me.selectVat,
				itemStoreLoad:me.calSummary,
				calcVat:me.calcVat
			}
		});

	},
	
	COPY_MSG_KEY : 'COPY_PCM_REQ_ITEM',
	DEL_MSG_KEY : 'DELETE_PCM_REQ_ITEM',
    URL : ALF_CONTEXT+'/pcm/item',
    MSG_URL : ALF_CONTEXT+'/pcm/message',

    ok:function() {
	
		var me = this;
		
		if (validForm(me.getForm())) {
			
			var rec; 
			
			var id = me.getHidId().getValue();
			if (!id) {

				var store = me.getGrid().getStore();
		
				var maxId = 0;
				if (store.getCount() > 0)
				{
				  maxId = store.getAt(0).get('id');
				  store.each(function(rec)
				  {
				    maxId = Math.max(maxId, rec.get('id'));
				  });
				}
				
				rec = Ext.create('PBPcm.model.ItemGridModel',{
		    		id : maxId+1,
		    		action : 'CED' 
		    	});
				
			} else {
				rec = me.selectedRec;
			}
			
			var curRate = parseFloat(me.getTxtCurrencyRate().getValue());

			var prc = parseFloat(me.getTxtPrc().getValue());
//			var prcCnv = prc * curRate;
			var qty = parseFloat(me.getTxtQty().getValue());
			var total = prc * qty;
			
			rec.set("actName",me.getCmbActId().getRawValue());
			rec.set("actId",me.getCmbActId().getValue()); 
			rec.set("actGrpName",me.getCmbActGrpId().getRawValue());
			rec.set("actGrpId",me.getCmbActGrpId().getValue()); 
			rec.set("assetName",me.getCmbAssetRuleId().getRawValue());
			rec.set("assetRuleId",me.getCmbAssetRuleId().getValue() ? me.getCmbAssetRuleId().getValue() : 0); 
			rec.set("description",me.getTxtDesc().getValue()); 
			rec.set("quantity",qty); 
			rec.set("unit",me.getCmbUnit().getRawValue());
			rec.set("unitId",me.getCmbUnit().getValue());
			rec.set("price",prc);
			rec.set("fiscalYear",me.getCmbFiscalYear().getValue() ? me.getCmbFiscalYear().getValue() : 0);
			rec.set("total",total);
			
			if (!id) {
				rec.commit();
				store.add(rec);
			} else {
				me.getGrid().getView().refresh();
			}
			
			me.getDlg().destroy();
			
			me.calSummary();
		} // validForm
	}, // method
	
	add:function() {
		var me = this;
	
		this.createDlg(PB.Label.m.add).show();

	},
	
	createDlg:function(title, rec) {
		
		var me = this;
		
		var dialog = Ext.create('PBPcm.view.item.DtlDlg', {
		    title : title,
		    rec : rec,
		    acrossBudget : me.getChkAcrossBudget().getValue(),
		    trec : {
				fundId:me.getHidFundId().getValue(),
				budgetCcType:me.getHidBudgetCcType().getValue(),
				budgetCc:me.getHidBudgetCc().getValue()
		    }
		});
		
		return dialog;
	},
	
	edit:function(rec) {
		var me = this;
		
		me.getGrid().getView().getSelectionModel().select(rec);
		me.selectedRec = rec;		
	
		var dialog = me.createDlg(PB.Label.m.edit, rec);
		
		me.getHidId().setValue(rec.get("id"));
		me.getCmbFiscalYear().setValue(rec.get("fiscalYear").toString());
		me.getCmbActId().setValue(rec.get("actId"));
		me.getCmbActGrpId().setValue(rec.get("actGrpId"));
		me.getTxtDesc().setValue(rec.get("description"));
		me.getTxtQty().setValue(rec.get("quantity"));
		me.getCmbUnit().setValue(rec.get("unitId"));
		me.getTxtPrc().setValue(rec.get("price"));
		
		dialog.show();
	},
	
	del:function(rec) {
		this.getGrid().getView().getSelectionModel().select(rec);
		
		this.selectedRec = rec;
	
		PB.Dlg.confirm('CONFIRM_'+this.DEL_MSG_KEY,this,'doDel', MODULE_PCM);
	},
	
	doDel:function() {
		var me = this;
		me.getGrid().getStore().remove(this.selectedRec);
		me.calSummary();
	},
	
	copy : function(r) {
		var me = this;
		
		PB.Util.checkSession(this, me.MSG_URL+"/get", function() {
	
			me.getGrid().getView().getSelectionModel().select(r);
			
			me.selectedRec = r;
			PB.Dlg.confirm('CONFIRM_'+me.COPY_MSG_KEY,me,'doCopy', MODULE_PCM);
		
		});
	},
	
	doCopy : function(){
		var me = this;

		var store = me.getGrid().getStore();
		
		var maxId = 0;
		
		if (store.getCount() > 0)
		{
		  maxId = store.getAt(0).get('id');
		  store.each(function(rec)
		  {
		    maxId = Math.max(maxId, rec.get('id'));
		  });
		}
		
		var rec = Ext.create('PBPcm.model.ItemGridModel',{
			id : maxId+1,
			action : 'CED' 
		});
		
		var oitem = me.selectedRec;
		
		rec.set("actGrpId", oitem.data['actGrpId']);
		rec.set("actGrpName", oitem.data['actGrpName']);
		rec.set("actId", oitem.data['actId']);
		rec.set("actName", oitem.data['actName']);
		rec.set("assetRuleId", oitem.data['assetRuleId']);
		rec.set("assetName", oitem.data['assetName']);
		rec.set("description", oitem.data['description']);
		rec.set("quantity", oitem.data['quantity']);
		rec.set("unit", oitem.data['unit']);
		rec.set("unitId", oitem.data['unitId']);
		rec.set("price", oitem.data['price']);
		rec.set("fiscalYear", oitem.data['fiscalYear']);
		rec.set("total", oitem.data['total']);
		
		rec.commit();
		store.add(rec);
		me.calSummary();
	},
	
	calSummary:function() {
		var me = this;
		
		var grossAmt = 0;
		
		var store = me.getGrid().getStore();
		store.each(function(rec){
			grossAmt += rec.data.total;
		});
		
		var curRate = parseFloat(me.getTxtCurrencyRate().getValue());
		
		var vat = parseFloat(me.getHidVat().getValue());
		var vatAmt = 0;
		var netAmt = 0;
		var netAmtCnv = 0;
		if (String(me.getHidPriceInclude().getValue()).toLowerCase() === "true") {
			vat = vat*100;
			vatAmt = Math.round(grossAmt * vat / (100+vat) * 100) / 100; // decimal 2 digits
			vatAmt = Number(vatAmt.toFixed(2));
			netAmt = Number(grossAmt.toFixed(2));
			grossAmt = netAmt - vatAmt;
		} else {
			grossAmt = Number(grossAmt.toFixed(2));
			vatAmt = Number((grossAmt * vat).toFixed(2));
			netAmt = grossAmt + vatAmt;
		}
		netAmtCnv = netAmt * curRate;
		
//		console.log("grossAmt:"+grossAmt);
//		console.log("vatAmt:"+vatAmt);
//		console.log("netAmt:"+netAmt);
//		console.log("netAmtCnv:"+netAmtCnv);
		
		me.getLblGrossAmt().setText(Ext.util.Format.number(grossAmt, DEFAULT_MONEY_FORMAT));
		me.getLblVatAmt().setText(Ext.util.Format.number(vatAmt, DEFAULT_MONEY_FORMAT));
		me.getLblNetAmt().setText(Ext.util.Format.number(netAmt, DEFAULT_MONEY_FORMAT));
		me.getLblNetAmtCnv().setText(Ext.util.Format.number(netAmtCnv, DEFAULT_MONEY_FORMAT));
		
		me.getHidTotal().setValue(Number(netAmt.toFixed(2)));
		me.getHidTotalCnv().setValue(Number(netAmtCnv.toFixed(2)));
		
//		if (!me.getChkAcrossBudget().getValue()) { // not across budget
			var methodStore = me.getCmbMethod().getStore();
			methodStore.getProxy().extraParams = {
				objType : me.getCmbObjectiveType().getValue() ? me.getCmbObjectiveType().getValue() : 0 
			}
			if (me.getHidTotal().getValue()) {
				methodStore.getProxy().extraParams.total = me.getHidTotalCnv().getValue(); 
			}
			methodStore.load(function(s, recs) {

				if (me.getCmbMethod().getValue()) {
					var rec = methodStore.getById(parseInt(me.getCmbMethod().getValue()));
					if (!rec) {
						me.getCmbMethod().setValue(null);
						me.getCmtTab().removeAll(true);
					}
				} else {
					var id = 0;
					methodStore.each(function(r){
						id = r.get('id');
					});
					me.getCmbMethod().setValue(id);
					var rec = methodStore.getById(id);
					me.getMainCmtTab().fireEvent("selectCmb", me, [rec], me.rec);
				}
			});
//		}
		
		if (netAmtCnv >= 100000) {
			var tbar = me.getUploadGrid().getDockedComponent(1);
			var dlBtn = tbar.down("button[action=download]");

			if (!dlBtn) {
						
				var store = Ext.create('PB.store.common.ComboBoxStore');
				store.getProxy().api.read = ALF_CONTEXT+'/srcUrl/main/master2?all=false';
				store.getProxy().extraParams = {
					p1 : "type='MP'",
					orderBy : 'code',
					all : true
				}
				store.load();
				
				tbar.add({
					xtype:'tbfill'
				},{
					xtype:'combo',
					name:'mp',
					fieldLabel:'แบบฟอร์มราคากลาง',
			    	displayField:'name',
			    	valueField:'id',
			        emptyText : PB.Label.m.select,
			        store: store,
			        queryMode: 'local',
			        typeAhead:true,
			        multiSelect:false,
			        forceSelection:true,
					width:420,
					labelWidth:130,
			        listConfig : {
				    	resizable:true,
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
			       	    select : function(combo, records, e){
			       		   	combo.fireEvent("selectMediumPrice",combo, records, e);
			       	    }
					}			
				},{
					xtype:'button',
					text:'Download',
					iconCls:'icon_download',
					action:'download',
					margin:'0 0 0 5'
				});
			}
		} else {
			var tbar = me.getUploadGrid().getDockedComponent(1);
			var dlBtn = tbar.down("button[action=download]");

			if (dlBtn) {
				var items = tbar.items;
				while(items.getCount()>4) {
					tbar.remove(items.getAt(4),true);
				}
			}
		}
		
	},
	
	selectVat:function(cmb, rec) {
		var me = this;
		
		if (rec[0].data.data.amount) {
			me.getHidVat().setValue(Number(rec[0].data.data.amount.toFixed(5)));
		} else {
			me.getHidVat().setValue(0);
		}
		
		me.getHidPriceInclude().setValue(rec[0].data.data.price_include);
		
		me.calSummary();
	},
	
	selectActivityGroup:function(cmb, rec) {
		var me = this;
		
	//	console.log("id:"+rec[0].data.id);
	
//		var store = me.getCmbActId().getStore();
//		store.getProxy().extraParams = {
//			actGrpId:rec[0].data.id,
//			query:getLang()+" "
//		}
//		store.load();
		
		var helpAG = document.getElementById("help-ag");
		helpAG.setAttribute("data-qtip",rec[0].data.data.description);
	},
	
	selectActivity:function(cmb, rec) {
//		var me = this;
//		
//		store = me.getGridAct().getStore();
//		store.getProxy().extraParams = {
//			id:0,
//			cond:0
//		}
//		store.load();		
		var me = this;
		
		var store = me.getCmbActGrpId().getStore();
		store.getProxy().extraParams = {
		    query : getLang()+' ',
		    actId:rec[0].data.id
		}
		store.load({
			callback:function(recs) {
//				me.fireEvent("agStoreLoad", recs);
				
				me.agStoreLoad(recs);
			}
		});
	},
	
	agStoreLoad:function(recs) {
		var me = this;
		if (recs && recs.length==1) {
			me.getCmbActGrpId().setValue(recs[0].get("id"));
		}
	},
	
	calcVat:function(chk, newV) {
		var me = this;
		me.getCmbVatId().setDisabled(!newV);
		
		if (!newV) {
			me.getHidVat().setValue(0);
			me.getCmbVatId().setValue(null);
			me.calSummary();
		}
	},
	
	assetStoreLoad:function(len) {
		this.getCmbAssetRuleId().setDisabled(len<=1);
	}

});
