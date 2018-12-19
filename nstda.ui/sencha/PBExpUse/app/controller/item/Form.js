Ext.define('PBExpUse.controller.item.Form', {
    extend: 'Ext.app.Controller',

    refs:[{
        ref: 'main',
        selector: 'expUseMain'
    },{
        ref: 'mainForm',
        selector: 'expUseMainForm'
    },{
        ref: 'dlg',
        selector: 'expUseItemDtlDlg'
    },{
        ref: 'form',
        selector: 'expUseItemDtlDlg [itemId=formDetail]'
	},{
    	ref:'uploadGrid',
		selector:'expUseFileTab uploadGrid'
	},{
    	ref:'grid',
		selector:'expUseItemTab grid'
    },{
        ref: 'txtReason',
        selector:'expUseItemTab field[name=reason]'
	},{
	    ref: 'hidEmotion',
	    selector: 'expUseMainForm field[name=emotion]'
    },{
        ref: 'hidId',     
        selector:'expUseItemDtlDlg field[name=id]'
    },{
        ref: 'cmbActGrpId',
        selector:'expUseItemDtlDlg field[name=actGrpId]'
    },{
        ref: 'cmbActId',
        selector:'expUseItemDtlDlg field[name=actId]'
    },{
        ref: 'txtActivity',
        selector:'expUseItemDtlDlg field[name=activity]'
    },{
        ref: 'cmbAssetRuleId',
        selector:'expUseItemDtlDlg field[name=assetRuleId]'
    },{
        ref: 'cmbCondition1',
        selector:'expUseItemDtlDlg field[name=condition1]'
    },{
        ref: 'txtAmount',     
        selector:'expUseItemDtlDlg field[name=amount]'
    },{
        ref: 'gridAct',
        selector:'expUseItemDtlDlg grid'
    },{
        ref: 'hidPrTotal',
        selector:'expUseInfoTab field[name=prTotal]'

//    },{
//        ref: 'lblGrossAmt',
//        selector:'expUseItemTab label[name=grossAmt]'
//    },{
//        ref: 'lblVatAmt',
//        selector:'expUseItemTab label[name=vatAmt]'
    },{
        ref: 'lblNetAmt',
        selector:'expUseItemTab label[name=netAmt]'
//    },{
//        ref: 'hidVat',     
//        selector:'expUseItemTab field[name=vat]'
    },{
        ref: 'hidTotal',     
        selector:'expUseItemTab field[name=total]'
	},{
	    ref: 'radPayType',
	    selector: 'expUseMainForm field[name=payType]'
    },{
        ref: 'hidFundId',
        selector:'expUseInfoTab field[name=fundId]'
    },{
        ref: 'hidBudgetCcType',
        selector:'expUseInfoTab field[name=budgetCcType]'
    },{
        ref: 'hidBudgetCc',
        selector:'expUseInfoTab field[name=budgetCc]'
    },{
        ref: 'chkSmallAmount',
        selector:'expUseInfoTab field[name=isSmallAmount]'
    }],
    
    init:function() {
		var me = this;
		
		me.control({
			'expUseItemDtlDlg [action=ok]': {
				click : me.ok
			},
			'expUseItemDtlDlg': {
				selectActivityGroup : me.selectActivityGroup,
				selectActivity : me.selectActivity,
				selectCond1 : me.selectCond1,
				cond1Load : me.cond1Load,
				assetStoreLoad:me.assetStoreLoad
			},
			'expUseItemTab grid [action=addItem]': {
				click : me.add
			},
			'expUseItemTab grid':{
				edit:me.edit,
				del:me.del,
				copy:me.copy
			},
			'expUseItemTab': {
//				selectVat : me.selectVat,
				itemStoreLoad:me.calSummary,
				isReason:me.isReason
			}
		});

	},
	
	COPY_MSG_KEY : 'COPY_EXP_USE_ITEM',
	DEL_MSG_KEY : 'DELETE_EXP_USE_ITEM',
    URL : ALF_CONTEXT+'/exp/use/item',
    MSG_URL : ALF_CONTEXT+'/exp/message',

    validForm:function() {
		var result = true;

		var me = this;
		
		if (String(me.getChkSmallAmount().getValue()).toLowerCase()==="true") {
			var total = 0;
			
			var store = me.getGrid().getStore();
			store.each(function(rec){
				if (rec.get("actGrpId")!=me.getCmbActGrpId().getValue() || rec.get("actId")!=me.getCmbActId().getValue()) {
//					console.log("add:"+rec.data.amount);
					total += rec.data.amount;
				}
			});
			
			total += me.getTxtAmount().getValue()
			
			if (total>me.getHidPrTotal().getValue()) {
				PB.Dlg.error('',MODULE_EXP,{msg:PBExpUse.Label.i.totalExceedPR+" ("+Ext.util.Format.number(me.getHidPrTotal().getValue(), DEFAULT_MONEY_FORMAT)+")"});
				result = false;
			}
		}
		
		return result;
	},
	
    ok:function() {
	
		var me = this;
		
		if (validForm(me.getForm())) {
			if (me.validForm()) {
			
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
					
					rec = Ext.create('PBExpUse.model.ItemGridModel',{
			    		id : maxId+1,
			    		action : 'CED' 
			    	});
					
				} else {
					rec = me.selectedRec;
				}
				rec.set("actGrpId",me.getCmbActGrpId().getValue());
				me.getCmbActGrpId().getStore().each(function(r){
					if (r.get("id")==me.getCmbActGrpId().getValue()) {
						rec.set("actGrpName", r.get("name"));
					}
				});
				rec.set("actId",me.getCmbActId().getValue());
				me.getCmbActId().getStore().each(function(r){
					if (r.get("id")==me.getCmbActId().getValue()) {
						rec.set("actName", r.get("name"));
						rec.set("specialWorkflow", r.get("data").special_workflow ? r.get("data").special_workflow : "");
					}
				});
				rec.set("assetRuleId",me.getCmbAssetRuleId().getValue() ? me.getCmbAssetRuleId().getValue() : 0);
				me.getCmbAssetRuleId().getStore().each(function(r){
					if (r.get("id")==me.getCmbAssetRuleId().getValue()) {
						rec.set("assetName", r.get("name"));
					}
				});
				rec.set("activity", me.getTxtActivity().getValue());
				rec.set("position", "");
				rec.set("condition2", "");
				rec.set("uom" ,"");
				rec.set("condition1",me.getCmbCondition1().getValue());
				rec.set("amount",me.getTxtAmount().getValue());
				
				if (!id) {
					rec.commit();
					store.add(rec);
				} else {
					me.getGrid().getView().refresh();
				}
				
				me.getDlg().destroy();
				
				me.calSummary();
			}
		} // validForm
	}, // method
	
	add:function() {
		var me = this;
		
		this.createDlg(PB.Label.m.add
						,null
						,me.getHidEmotion().getValue()
						,me.getRadPayType().getGroupValue() == "3"
		).show();
	},
	
	createDlg:function(title, rec, emotion, icharge) {
		
		var me = this;
		
		var dialog = Ext.create('PBExpUse.view.item.DtlDlg', {
		    title : title,
		    rec : rec,
		    emotion : emotion,
		    icharge : icharge,
		    trec : {
				fundId:me.getHidFundId().getValue(),
				budgetCcType:me.getHidBudgetCcType().getValue(),
				budgetCc:me.getHidBudgetCc().getValue(),
				isSmallAmount:me.getChkSmallAmount().getValue()
		    }
		});
		
		return dialog;
	},
	
	edit:function(rec) {
		var me = this;
		
		me.getGrid().getView().getSelectionModel().select(rec);
		me.selectedRec = rec;		
	
		var dialog = me.createDlg(PB.Label.m.edit
								,rec
								,me.getHidEmotion().getValue()
								,me.getRadPayType().getGroupValue() == "3"
		);
		
//		me.getHidId().setValue(rec.get("id"));
//		me.getCmbActivityId().setValue(rec.get("activityId"));
//		me.getCmbCondition1().setValue(rec.get("condition1"));
//		me.getTxtAmount().setValue(rec.get("amount"));
		
		dialog.show();
	},
	
	del:function(rec) {
		this.getGrid().getView().getSelectionModel().select(rec);
		
		this.selectedRec = rec;
	
		PB.Dlg.confirm('CONFIRM_'+this.DEL_MSG_KEY,this,'doDel', MODULE_EXP);
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
			PB.Dlg.confirm('CONFIRM_'+me.COPY_MSG_KEY,me,'doCopy', MODULE_EXP);
		
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
		
		var rec = Ext.create('PBExpUse.model.ItemGridModel',{
			id : maxId+1,
			action : 'CED' 
		});
		
		var oitem = me.selectedRec;
		
		rec.set("activity", oitem.data['activity']);
		rec.set("actGrpId", oitem.data['actGrpId']);
		rec.set("actGrpName", oitem.data['actGrpName']);
		rec.set("actId", oitem.data['actId']);
		rec.set("actName", oitem.data['actName']);
		rec.set("assetRuleId", oitem.data['assetRuleId']);
		rec.set("assetName", oitem.data['assetName']);
		rec.set("condition1", oitem.data['condition1']);
		rec.set("condition2", oitem.data['condition2']);
		rec.set("position", oitem.data['position']);
		rec.set("uom", oitem.data['uom']);
		rec.set("amount", oitem.data['amount']);
		rec.set("specialWorkflow", oitem.data['specialWorkflow']);
		
		rec.commit();
		store.add(rec);
		
		me.calSummary();
	},
	
	calSummary:function() {
		var me = this;
		
		var grossAmt = 0;
		
		var store = me.getGrid().getStore();
		store.each(function(rec){
			grossAmt += rec.data.amount;
		});
		
//		var vat = parseFloat(me.getHidVat().getValue());
//		var vatAmt = grossAmt * vat;
//		var netAmt = grossAmt+vatAmt;
		var netAmt = grossAmt;
		
//		me.getLblGrossAmt().setText(Ext.util.Format.number(grossAmt, DEFAULT_MONEY_FORMAT));
//		me.getLblVatAmt().setText(Ext.util.Format.number(vatAmt, DEFAULT_MONEY_FORMAT));
		me.getLblNetAmt().setText(Ext.util.Format.number(netAmt, DEFAULT_MONEY_FORMAT));
		
		me.getHidTotal().setValue(netAmt);
	},
	
//	selectVat:function(cmb, rec) {
//		var me = this;
//		
//		if (rec[0].data.data.amount) {
//			me.getHidVat().setValue(rec[0].data.data.amount);
//		} else {
//			me.getHidVat().setValue(0);
//		}
//		
//		me.calSummary();
//	},
	
	selectActivityGroup:function(cmb, rec) {
		var me = this;
		
//		console.log("id:"+rec[0].data.id);

//		var payType = me.getRadPayType().getGroupValue();
//
//		var store = me.getCmbActId().getStore();
//		store.getProxy().extraParams = {
//			actGrpId:rec[0].data.id,
//			query:getLang()+" ",
//			icharge:payType==3
//		}
//		store.load();
		
		var helpAG = document.getElementById("help-ag");
		helpAG.setAttribute("data-qtip",rec[0].data.data.description);

	},

	selectActivity:function(cmb, rec) {
		var me = this;
		
//		console.log("id:"+rec[0].data.id);

		var store = me.getCmbCondition1().getStore();
		store.getProxy().extraParams = {
			id:rec[0].data.id
		}
		store.load();
		
		store = me.getGridAct().getStore();
		store.getProxy().extraParams = {
			id:0,
			cond:0
		}
		store.load();		
		
		var payType = me.getRadPayType().getGroupValue();

		store = me.getCmbActGrpId().getStore();
		store.getProxy().extraParams = {
			query:getLang()+" ",
			actId:rec[0].data.id,
			emotion:me.getHidEmotion().getValue()
//			,icharge:payType==3
		}
		store.load({
			callback:function(recs) {
//				me.fireEvent("agStoreLoad", recs);
				
				me.agStoreLoad(recs);
			}
		});
		
//		var r = [{
//			data:{id:0}
//		}]
//		me.selectActivity(cmb, r);

	},

	selectCond1:function(cmb, rec) {
		var me = this;
		
//		console.log("cond1:"+rec[0].data.id);

		var store = me.getGridAct().getStore();
		store.getProxy().extraParams = {
			id:rec[0].data.data.activity_id,
			cond:rec[0].data.data.condition_1
		}
		store.load();
	},
	
	cond1Load:function(recs) {
		var me = this;
		
		var cmbCond1 = me.getCmbCondition1(); 
		
		cmbCond1.setDisabled(recs.length<=0);
	},
	
	agStoreLoad:function(recs) {
		var me = this;
		if (recs && recs.length==1) {
			me.getCmbActGrpId().setValue(recs[0].get("id"));
		}
	},

	isReason:function(chk,v) {
		var me = this;
		me.getTxtReason().setDisabled(!v);
		if (!v) {
			me.getTxtReason().setValue(null);
		}
	},
	
	assetStoreLoad:function(len) {
		this.getCmbAssetRuleId().setDisabled(len<=1);
	}
});
