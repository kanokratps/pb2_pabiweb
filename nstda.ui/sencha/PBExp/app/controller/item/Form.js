Ext.define('PBExp.controller.item.Form', {
    extend: 'Ext.app.Controller',

    refs:[{
        ref: 'main',
        selector: 'expBrwMain'
    },{
        ref: 'mainForm',
        selector: 'expBrwMainForm'
    },{
        ref: 'dlg',
        selector: 'expBrwItemDtlDlg'
    },{
        ref: 'form',
        selector: 'expBrwItemDtlDlg [itemId=formDetail]'
	},{
    	ref:'grid',
		selector:'expBrwItemTab grid[itemId=itemGrid]'
    },{
        ref: 'hidTotal',     
        selector:'expBrwItemTab field[name=total]'
    },{
        ref: 'lblNetAmt',     
        selector:'expBrwItemTab label[name=netAmt]'
    },{
        ref: 'txtReason',
        selector:'expBrwItemTab field[name=reason]'
    },{
        ref: 'hidId',     
        selector:'expBrwItemDtlDlg field[name=id]'
    },{
        ref: 'cmbActGrpId',
        selector:'expBrwItemDtlDlg field[name=actGrpId]'
    },{
        ref: 'cmbActId',
        selector:'expBrwItemDtlDlg field[name=actId]'
    },{
        ref: 'cmbAssetRuleId',
        selector:'expBrwItemDtlDlg field[name=assetRuleId]'
    },{
        ref: 'cmbCondition1',
        selector:'expBrwItemDtlDlg field[name=condition1]'
    },{
        ref: 'txtActivity',
        selector:'expBrwItemDtlDlg field[name=activity]'
    },{
        ref: 'txtAmount',     
        selector:'expBrwItemDtlDlg field[name=amount]'
    },{
        ref: 'gridAct',
        selector:'expBrwItemDtlDlg grid'
    },{
        ref: 'hidPrTotal',
        selector:'expBrwInfoTab field[name=prTotal]'
    },{
        ref: 'hidFundId',
        selector:'expBrwInfoTab field[name=fundId]'
    },{
        ref: 'hidBudgetCcType',
        selector:'expBrwInfoTab field[name=budgetCcType]'
    },{
        ref: 'hidBudgetCc',
        selector:'expBrwInfoTab field[name=budgetCc]'
    },{
        ref: 'chkSmallAmount',
        selector:'expBrwInfoTab field[name=isSmallAmount]'
    }],
    
    init:function() {
		var me = this;
		
		me.control({
			'expBrwItemDtlDlg [action=ok]': {
				click : me.ok
			},
			'expBrwItemDtlDlg': {
				selectActivityGroup : me.selectActivityGroup,
				selectActivity : me.selectActivity,
				selectCond1 : me.selectCond1,
				cond1Load : me.cond1Load,
				assetStoreLoad:me.assetStoreLoad
			},
			'expBrwItemTab [action=addItem]': {
				click : me.add
			},
			'expBrwItemTab grid[itemId=itemGrid]':{
				edit:me.edit,
				del:me.del,
				copy:me.copy
			},
			'expBrwItemTab': {
				itemStoreLoad:me.calSummary,
				isReason:me.isReason
			}
		});

	},
	
	COPY_MSG_KEY : 'COPY_EXP_BRW_ITEM',
	DEL_MSG_KEY : 'DELETE_EXP_BRW_ITEM',
    URL : ALF_CONTEXT+'/exp/brw/item',
    MSG_URL : ALF_CONTEXT+'/exp/message',

    validForm:function() {
		var result = true;

		var me = this;
		
		if (String(me.getChkSmallAmount().getValue()).toLowerCase()==="true") {
			var total = 0;
			
			var store = me.getGrid().getStore();
			store.each(function(rec){
				if (rec.get("actGrpId")!=me.getCmbActGrpId().getValue() || rec.get("actId")!=me.getCmbActId().getValue()) {
					total += rec.data.amount;
				}
			});
			
			total += me.getTxtAmount().getValue()
			
			if (total>me.getHidPrTotal().getValue()) {
//				alert("Total more than PR");
				PB.Dlg.error('',MODULE_EXP,{msg:PBExp.Label.i.totalExceedPR+" ("+Ext.util.Format.number(me.getHidPrTotal().getValue(), DEFAULT_MONEY_FORMAT)+")"});
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
					
					rec = Ext.create('PBExp.model.ItemGridModel',{
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
					}
				});
				rec.set("assetRuleId",me.getCmbAssetRuleId().getValue() ? me.getCmbAssetRuleId().getValue() : 0);
				me.getCmbAssetRuleId().getStore().each(function(r){
					if (r.get("id")==me.getCmbAssetRuleId().getValue()) {
						rec.set("assetName", r.get("name"));
					}
				});
				rec.set("condition1",me.getCmbCondition1().getValue() ? me.getCmbCondition1().getValue() : "");
				rec.set("activity",me.getTxtActivity().getValue());
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
	
		this.createDlg('เพิ่ม').show();
	},
	
	createDlg:function(title, rec) {
		
		var me = this;
		
		var dialog = Ext.create('PBExp.view.item.DtlDlg', {
		    title : title,
		    rec : rec,
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
	
		var dialog = me.createDlg('แก้ไข',rec);
		
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
		
		var rec = Ext.create('PBExp.model.ItemGridModel',{
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
		rec.set("condition1", oitem.data['condition1']);
		rec.set("activity", oitem.data['activity']);
		rec.set("amount", oitem.data['amount']);
		
		rec.commit();
		store.add(rec);
		
		me.calSummary();
	},
	
	getTotal:function() {
		var me = this;
		
		var total = 0;
		
		var store = me.getGrid().getStore();
		store.each(function(rec){
			total += rec.data.amount;
		});
		
		return total;
	},

	calSummary:function() {
		var me = this;
		var total = me.getTotal();
		
		me.getLblNetAmt().setText(Ext.util.Format.number(total, DEFAULT_MONEY_FORMAT));
		me.getHidTotal().setValue(total);
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
		var me = this;
		
	//	console.log("id:"+rec[0].data.id);
	
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
		
		store = me.getCmbActGrpId().getStore();
		store.getProxy().extraParams = {
			query:getLang()+" ",
		    actId:rec[0].data.id
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
		
	//	console.log("cond1:"+rec[0].data.id);
	
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
