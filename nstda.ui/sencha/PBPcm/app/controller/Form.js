Ext.define('PBPcm.controller.Form', {
    extend: 'Ext.app.Controller',

    refs:[{
        ref: 'main',
        selector: 'pcmReqMain'
    },{
        ref: 'mainForm',
        selector: 'pcmReqMainForm'
    },{
        ref: 'mainGrid',
        selector: 'pcmReqMainGrid'
    },{
        ref: 'hidId',     
        selector: 'pcmReqMainForm field[name=id]'
    },{
        ref: 'hidStatus',
        selector: 'pcmReqMainForm field[name=status]'
    },{
        ref: 'txtReqBy',     
        selector: 'pcmReqMainForm field[name=reqBy]'
    },{
        ref: 'txtReqOu',     
        selector: 'pcmReqMainForm field[name=reqOu]'
    },{
        ref: 'cmbObjectiveType',     
        selector: 'pcmReqMainForm field[name=objectiveType]'
    },{
        ref: 'txtObjective',
        selector: 'pcmReqMainForm field[name=objective]'
    },{
        ref: 'cmbReason',
        selector: 'pcmReqMainForm field[name=reason]'
    },{
        ref: 'txtReasonOth',
        selector: 'pcmReqMainForm field[name=reasonOth]'
    },{
        ref: 'cmbCurrency',
        selector: 'pcmReqMainForm field[name=currency]'
    },{
        ref: 'txtCurrencyRate',
        selector: 'pcmReqMainForm field[name=currencyRate]'
    },{
        ref: 'lblBaht',
        selector: 'pcmReqMainForm label[itemId=baht]'
    },{
        ref: 'hidBudgetCcType',
        selector: 'pcmReqMainForm field[name=budgetCcType]'
    },{
        ref: 'hidBudgetCc',
        selector: 'pcmReqMainForm field[name=budgetCc]'
    },{
        ref: 'hidFundId',
        selector: 'pcmReqMainForm field[name=fundId]'
    },{
        ref: 'raStock',
        selector: 'pcmReqMainForm field[name=isStock]'
    },{
        ref: 'cmbStockOu',
        selector: 'pcmReqMainForm field[name=stockOu]'
    },{
        ref: 'raPrototype',
        selector: 'pcmReqMainForm field[name=isPrototype]'
    },{
        ref: 'raPrototypeYes',
        selector: 'pcmReqMainForm field[itemId=isPrototypeYes]'
    },{
        ref: 'raPrototypeNo',
        selector: 'pcmReqMainForm field[itemId=isPrototypeNo]'
    },{
        ref: 'cmbPrototypeType',
        selector: 'pcmReqMainForm field[name=prototypeType]'
//    },{
//        ref: 'cmbPrototypeNo',
//        selector: 'pcmReqMainForm field[name=prototypeNo]'
    },{
        ref: 'hidCostControlTypeId',
        selector: 'pcmReqMainForm field[name=costControlTypeId]'
    },{
        ref: 'hidCostControlId',
        selector: 'pcmReqMainForm field[name=costControlId]'
    },{
        ref: 'cmbPcmOu',
        selector: 'pcmReqMainForm field[name=pcmOu]'
    },{
        ref: 'txtContractDate',
        selector: 'pcmReqMainForm field[name=contractDate]'
    },{
        ref: 'txtLocation',
        selector: 'pcmReqMainForm field[name=location]'
    },{
        ref: 'chkSmallAmount',
        selector: 'pcmReqMainForm field[name=isSmallAmount]'
    },{
        ref: 'chkAcrossBudget',
        selector: 'pcmReqMainForm field[name=isAcrossBudget]'
    },{
        ref: 'txtAcrossBudget',
        selector: 'pcmReqMainForm field[name=acrossBudget]'
    },{
        ref: 'chkRefId',
        selector: 'pcmReqMainForm field[name=isRefId]'
    },{
        ref: 'txtRefId',
        selector: 'pcmReqMainForm field[name=refId]'
    },{
        ref: 'hidTotal',     
        selector: 'pcmReqMainForm field[name=total]'
    },{
        ref: 'hidTotalCnv',     
        selector: 'pcmReqMainForm field[name=totalCnv]'
    },{
        ref: 'cmbMethod',     
        selector: 'pcmReqMainForm field[name=method]'
    },{
        ref: 'hidMethodCond2Rule',     
        selector: 'pcmReqMainForm field[name=methodCond2Rule]'
    },{
        ref: 'cmbMethodCond2',     
        selector: 'pcmReqMainForm field[name=methodCond2]'
    },{
        ref: 'txtMethodCond2Dtl',     
        selector: 'pcmReqMainForm field[name=methodCond2Dtl]'
    },{
        ref: 'cmbVatId',
        selector: 'pcmReqMainForm field[name=vatId]'
    },{
        ref: 'hidVat',
        selector: 'pcmReqMainForm field[name=vat]'
    },{
        ref: 'hidPriceInclude',
        selector: 'pcmReqMainForm field[name=priceInclude]'
	},{
    	ref:'fileTab',
		selector:'pcmReqFileTab'
	},{
    	ref:'uploadGrid',
		selector:'pcmReqFileTab uploadGrid'
    },{
        ref: 'btnApprovalMatrix',     
        selector: 'pcmReqMainForm button[action=approvalMatrix]'
	},{
    	ref:'itemGrid',
    	selector:'pcmReqItemTab grid'
	},{
    	ref:'totalPanel',
    	selector:'pcmReqItemTab [itemId=totalPanel]'
	},{
    	ref:'lblTotal',
    	selector:'pcmReqItemTab [itemId=lblTotal]'
	},{
    	ref:'lblTotalCnv',
    	selector:'pcmReqItemTab [itemId=lblTotalCnv]'
    },{
        ref: 'lblNetAmtCnv',
        selector:'pcmReqItemTab label[name=netAmtCnv]'
    },{
    	ref:'mainCmtTab',
    	selector:'pcmReqCmtTab'
    },{
    	ref:'cmtTab',
    	selector:'pcmReqCmtTab tabpanel'
	},{
    	ref:'infoTab',
    	selector:'pcmReqInfoTab'
	},{
    	ref:'userTab',
    	selector:'pcmReqUserTab'
    },{
        ref: 'btnSend',
        selector:'pcmReqMainForm [action=send]'
    },{
        ref: 'btnAdd',
        selector:'pcmReqMain [action=add]'
    }],

    init:function() {
		var me = this;
		
		me.control({
			'pcmReqMainForm [action=finish]': {
				click : me.finish
			},
			'pcmReqMainForm [action=cancelEdit]': {
				click : me.cancelEdit
			},
			'pcmReqMainForm [action=send]': {
				click : me.send
			},
			'pcmReqMainForm [action=close]': {
				click : me.closeForm
			},
			'pcmReqMainForm [action=cancel]': {
				click : me.cancel
			},
			'pcmReqMainForm [action=saveDraft]': {
				click : me.saveDraft
			},
			'pcmReqMainForm [action=preview]': {
				click : me.preview
			},
			'pcmReqMainForm [action=approvalMatrix]': {
				click : me.showApprovalMatrix
			},
			'pcmReqUserTab':{
				selectReqBy:me.selectReqBy,
				showAM : me.showAM,
				hideAM : me.hideAM
			},
			'pcmReqInfoTab [action=showBudget]':{
				click : me.showBudget
			},
			'pcmReqInfoTab':{
				selectObjective:me.selectObjective,
				selectCurrency:me.selectCurrency,
				selectBudgetCc:me.selectBudgetCc,
				selectCostControl:me.selectCostControl,
				selectPR:me.selectPR,
				selectReason:me.selectReason,
				clearCostControl:me.clearCostControl,
				selectPrototypeType:me.selectPrototypeType,
				notStock:me.notStock,
				selectIsPrototype:me.selectIsPrototype,
				isAcrossBudget:me.isAcrossBudget,
				isRefId:me.isRefId,
				acrossBudgetBlur:me.acrossBudgetBlur,
				changeRate:me.changeRate,
				isSmallAmount:me.isSmallAmount
			},
			'button': {
				searchPR : me.searchPR
			},
			'textfield': {
				searchPR : me.searchPR
			},
			'pcmReqSearchDlg button[action=ok]':{
				confirmPR : me.confirmPR
			},
			'pcmReqSearchDlg grid':{
   				viewDetail : me.viewPRDetail
			}
		});

	},
	
	CURRENCY_RATE_MSG_KEY : 'GET_CURRENCY_RATE',
	PREVIEW_MSG_KEY : 'PREVIEW',
	SEND_MSG_KEY : 'SEND_PCM_REQ',
	MSG_KEY : 'SAVE_PCM_REQ',
    URL : ALF_CONTEXT+'/pcm',
    MSG_URL : ALF_CONTEXT+'/pcm/message',
    
    validForm:function(saveDraft) {
		var me = this;
		
		var form = me.getMainForm();
	
		var msg = "";

		if (!saveDraft) {
			var i = 1;
			if(!validForm(form) || !me.getRaPrototype().getGroupValue()) {
				var r = me.listInvalidField(form);
				msg = r.msg;
				i = r.i;
			}
		
			if (me.getItemGrid().getStore().getCount() == 0) {
				if (msg) {
					msg += "<br/>";
				}
				
				var tab = me.getItemGrid().up("panel");
				msg += i+".["+tab.title+"] "+tab.title;
				i++;
			} 
			else
			if (me.getHidTotal().getValue()==0) {
				if (msg) {
					msg += "<br/>";
				}
				
				var tab = me.getItemGrid().up("panel");
				msg += i+".["+tab.title+"] "+PBPcm.Label.t.totalZero;
				i++;
			}

			if (me.getChkAcrossBudget().getValue()) {
				var validFY = true;
				me.getItemGrid().getStore().each(function(record,id){
					if (!record.get("fiscalYear") || record.get("fiscalYear")==0) {
						validFY = false;
					}
				});
				
				if (!validFY) {
					if (msg) {
						msg += "<br/>";
					}
					
					var tab = me.getItemGrid().up("panel");
					msg += i+".["+tab.title+"] "+PBPcm.Label.t.noFiscalYear;
					i++;
				}
			}
			// check total with across budget
//			if (me.getChkAcrossBudget().getValue()) {
//				var acbg = parseFloat(me.getTxtAcrossBudget().getValue());
//				if (acbg > 0) {
//					var total = me.getHidTotal().getValue(); 
//					if (total > acbg) {
//						if (msg) {
//							msg +="<br/>";
//						}
//						msg += i+".จำนวนเงินรวมในรายการมากกว่าจำนวนเงินงบประมาณข้ามปี ("+total + '>' + acbg+")";
//						i++;
//					}
//				}
//			}

			if (me.getChkSmallAmount().getValue()!="1") {
				// check committee
				var panel = me.getCmtTab();
				
				panel.items.each(function(grid){
					if (grid.xtype == 'grid') {
					
						var cmtStore = grid.getStore();
						console.log("grid.cmt.amount_min:"+grid.cmt.amount_min);
						var minCmt = grid.cmt.amount_min;
						if (cmtStore.getCount() < minCmt) {
							if (msg) {
								msg += "<br/>";
							}
							
							var tab = me.getMainCmtTab();
							msg += i+".["+tab.title+"] "+Ext.String.format(PBPcm.Label.t.noCmtMember,grid.title,minCmt);
							i++;
						}
					}
				});
			}

//			if (me.getChkSmallAmount().getValue()!="1") {
//				Ext.Ajax.request({
//				    url:ALF_CONTEXT+"/srcUrl/main/masterField",
//				    method: "GET",
//				    params: {p1:"type='SYSTEM' and code='PCM_REQ_CMT_CHECK_PARAMS'",p2:'flag1'},
//				    async:false,
//				    success: function(response){
//					
//						var json = Ext.decode(response.responseText);
//						
//						var cfg = json.data[0].id.split(",");
//						var b = parseInt(cfg[0]);
//						var l = parseInt(cfg[1]);
//						var u = parseInt(cfg[2]);
//	
//						// check committee
//						var panel = me.getCmtTab();
//						
//						var total = me.getHidTotalCnv().getValue();
//						var minCmt = total>b ? u : l;
//						
//						panel.items.each(function(grid){
//							if (grid.xtype == 'grid') {
//							
//								var cmtStore = grid.getStore();
//			//					console.log("grid.cmt.amount_min:"+grid.cmt.amount_min);
//								if (cmtStore.getCount() < minCmt) {
//									if (msg) {
//										msg += "<br/>";
//									}
//									
//									var tab = me.getMainCmtTab();
//									msg += i+".["+tab.title+"] "+Ext.String.format(PBPcm.Label.t.noCmtMember,grid.title,minCmt);
//									i++;
//								}
//							}
//						});
//						
//						
//	//					if (msg) {
//	//						PB.Dlg.show('ERR_NOT_ENOUGH_CMT', MODULE_PCM, {msg:msg});
//	//						result = false;
//	//					}
//					}
//				});
//			}
		}
		
		return msg;
	},
	
	validForm2:function(fn) {
		var me = this;
		var result = true;
		// check file
		if (me.getFileTab().down("uploadGrid").getStore().getCount()<=0) {
			var errCode = me.getHidTotalCnv().getValue()>=100000 ? "ERR_NO_FILE_MP" : "ERR_NO_FILE";
			
			PB.Dlg.show(errCode, MODULE_PCM, 
				{
					icon:Ext.MessageBox.WARNING,
					modal:true,
					fn:function(btn) {
						if (btn=='yes') {
							me[fn](null);
						}
					},
					scope:me,
//								animateTarget:me.getFileTab().down("uploadGrid"),
					buttonText:{yes:PB.Label.u.noAttachYes,no:PB.Label.u.noAttachNo},
					buttons:Ext.MessageBox.YESNO
				}
			);
			result = false;
		}
		
		return result;
	},
	
	validFormRefId:function(fn) {
		var me = this;
		var result = true;
		
		if (me.getChkRefId().getValue()) {
			Ext.Ajax.request({
			    url:me.URL+"/req/checkRefPR",
			    method: "POST",
			    params: {
					refId:me.getTxtRefId().getValue(),
					total:me.getHidTotalCnv().getValue()
			    },
			    async:false,
			    success: function(response){
				
					var json = Ext.decode(response.responseText);
					
					if (json.success && !json.data.ok) {
						PB.Dlg.warn('ERR_REF_ID_TOTAL_OVER', MODULE_PCM, {val:[json.data.percent]});
						result = false;
					}
				},
			    headers: getAlfHeader()
			});
		}
				
		return result;
	},
	
	listInvalidField:function(form) {
		var ifield = form.query("field{isValid()==false}");
		var msg = "";
		var i = 1;
		
		var f = this.getRaPrototype();
//		f.validateValue("2");
		if (!f.getGroupValue()) {
			if (msg) {
				msg +="<br/>";
			}
			var lbl = f.errLabel;
			if (!lbl) {
				lbl = f.getFieldLabel();
			}
			var pos = lbl.indexOf("<font");
			if(pos<0) {
				pos = lbl.length;
			}
			
			var tab = f.up("panel");
			msg += i+".["+tab.title+"] "+lbl.substring(0,pos);
			i++;
			this.getRaPrototypeYes().markInvalid("Choose either Yes or No");
			this.getRaPrototypeNo().markInvalid("Choose either Yes or No");
		}

		for(var a in ifield) {
			if (msg) {
				msg +="<br/>";
			}
			var lbl = ifield[a].errLabel;
			if (!lbl) {
				lbl = ifield[a].getFieldLabel();
			}
			var pos = lbl.indexOf("<font");
			if(pos<0) {
				pos = lbl.length;
			}
			var tab = ifield[a].up("panel");
			msg += i+".["+tab.title+"] "+lbl.substring(0,pos);
			i++;
		}

		return {msg:msg,i:i};
	},
	
	send:function() {
		var me = this;
		
		me.getBtnSend().disable();
		
		PB.Util.checkSession(this, me.MSG_URL+"/get", function() {

	//		var form = me.getMainForm();
			
			var msg = me.validForm(false);
			if (!msg) {
//				if (me.validFormRefId()) {
					if (me.validForm2('doSend')) {
						PB.Dlg.confirm('CONFIRM_'+me.SEND_MSG_KEY, me,'doSend', MODULE_PCM, 'enableSendBtn', me);
					} else {
						me.enableSendBtn(me);
					}
//				} else {
//					me.enableSendBtn(me);
//				}
			} else {
				PB.Dlg.warn('INVALID_INPUT_'+me.MSG_KEY, MODULE_PCM, {msg:msg, scope:me});
				me.enableSendBtn(me);
				return;
			}
		
		});
	},
	
	enableSendBtn:function(me) {
		Ext.defer(function () {
			me.getBtnSend().enable();
	    }, 800);
	},
	
	enableAddBtn:function(me) {
		Ext.defer(function () {
			me.getBtnAdd().enable();
	    }, 800);
	},
	
	doSend: function(){
	
		var me = this;
		
		var grid = me.getMainGrid();
		
		var myMask = new Ext.LoadMask({
			target:me.getMain(), 
			msg:"Please wait..."
		});
		
		myMask.show();
		
		var params;
		
		try {
			params = me.prepareParams();
  		} catch (err) {
  			console.error(err);
  			myMask.hide();
  			return;
  		}
		
		Ext.Ajax.request({
		    url:me.URL+"/send",
		    method: "POST",
		    params: params,
		    success: function(response){
		  	  
				me.enableSendBtn(me);

				var json = Ext.decode(response.responseText);
				  
			   	if (json.success) {
			   		PB.Dlg.info('SUCC_'+me.SEND_MSG_KEY, MODULE_PCM, {msg:'ID:'+json.data.id, fn:me.closeForm, scope:me});
	   				me.enableAddBtn(me);
			   	} else {
			   		if (json.data != undefined && json.data.valid != undefined && !json.data.valid) {
			   			
			   			var msg;
			   			
			   			if (json.data.msg) {
			   				msg = json.data.msg;
			   			}
			   			else {
			   				msg = "";
			   				
					   		if (json.data.users) {
				   				msg += "Invalid Users ==> " + json.data.users;
					   		}
					   		
					   		if (json.data.groups) {
					   			if (msg) {
					   				msg += "<br/><br/>";
					   			}
					   			
				   				msg += "Invalid Groups ==> " + json.data.groups;
					   		}
					   		
					   		if (msg) {
				   				msg += "<br/><br/>Please check whether group exists and has member.";
					   		}
				   		}
				   		
				   		var opt;
			   			if (json.data.close) {
			   				opt = {msg:msg, fn:me.closeForm, scope:me}
			   			} else {
			   				opt = {msg:msg}
			   			}
			   			
			   			PB.Dlg.warn(null, MODULE_PCM, opt);
			   		}
			   		else {
				   		PB.Dlg.error('ERR_'+me.SEND_MSG_KEY, MODULE_PCM,{msg:json.message});
			   		}
			   	}
			   	 
		   	 	myMask.hide();
		
		    },
		    failure: function(response, opts){
		    	try {
		    		var json = Ext.decode(response.responseText);
			    	PB.Dlg.error('ERR_'+me.SEND_MSG_KEY+" ("+json.message+")", MODULE_PCM);
		    	}
		    	catch (err) {
			    	alert(response.responseText);
		    	}
			    myMask.hide();
				me.enableSendBtn(me);
		    },
		    headers: getAlfHeader()
		});
	},
	
	prepareParams : function() {
		var me = this;
		
		var reason = me.getCmbReason().getValue();
		if (reason == "อื่นๆ") {
			reason = "อื่นๆ " + me.getTxtReasonOth().getValue();
		}

		var params = {
  			id:me.getHidId().getValue(),
  			status:me.getHidStatus().getValue(),
  			
  			reqBy:me.getTxtReqBy().getValue(),
			reqOu:me.getTxtReqOu().getValue(),
  			
			objectiveType:me.getCmbObjectiveType().getValue(),
			objective:me.getTxtObjective().getValue(),
			reason:reason,
			lang:getLang()
  		};
		
		params.currency = me.getCmbCurrency().getValue();
		params.currencyRate = me.getTxtCurrencyRate().getValue();
		
		params.budgetCcType = me.getHidBudgetCcType().getValue();
		params.budgetCc = me.getHidBudgetCc().getValue();
		
		params.fundId = me.getHidFundId().getValue();
		
//		params.isStock = (me.getRaStock().getValue() ? "1" : "0");
//		params.stockOu = me.getCmbStockOu().getValue();
		
		params.isPrototype = (me.getRaPrototypeYes().getValue() ? "1" : "0");
		params.prototypeType = me.getCmbPrototypeType().getValue();
//		params.prototypeNo = me.getCmbPrototypeNo().getValue();
		
		params.costControlTypeId = me.getHidCostControlTypeId().getValue();
		params.costControlId = me.getHidCostControlId().getValue();

		params.contractDate = me.getTxtContractDate().getValue();
		
		params.pcmOu = me.getCmbPcmOu().getValue();
		params.location = me.getTxtLocation().getValue();
		
		params.isSmallAmount = (me.getChkSmallAmount().getValue() ? "1" : "0");
		
		params.isAcrossBudget = (me.getChkAcrossBudget().getValue() ? "1" : "0");
//		params.acrossBudget = me.getTxtAcrossBudget().getValue();
		
		params.isRefId = (me.getChkRefId().getValue() ? "1" : "0");
		params.refId = me.getTxtRefId().getValue();

		params.method = me.getCmbMethod().getValue();
		params.methodCond2Rule = me.getHidMethodCond2Rule() ? me.getHidMethodCond2Rule().getValue() : "";
		params.methodCond2 = me.getCmbMethodCond2() ? me.getCmbMethodCond2().getValue() : "";
		params.methodCond2Dtl = me.getTxtMethodCond2Dtl() ? me.getTxtMethodCond2Dtl().getValue() : "";
		
		params.vatId = me.getCmbVatId().getValue();
		params.vat = me.getHidVat().getValue();
		params.priceInclude = me.getHidPriceInclude().getValue();
		
		params.total = me.getHidTotal().getValue();
		params.totalCnv = me.getHidTotalCnv().getValue();
		
		params.items = me.getItems();
		params.files = me.listFiles();
		params.cmts = me.getCmts();
		
		return params;
	},
	
	cancel:function() {
		PB.Dlg.confirm('CONFIRM_CANCEL_PCM_REQ',this,'closeForm', MODULE_PCM);
	},
	
	closeForm:function() {
		this.getMainForm().destroy();
		this.refreshGrid();
		this.enableAddBtn(this);
	},
	
	refreshGrid:function() {
		this.getMainGrid().getStore().load();
	},
	
	saveDraft:function() {
		var me = this;
		
		PB.Util.checkSession(this, me.MSG_URL+"/get", function() {
		
//			var msg = me.validForm(true);
//			if (msg) {
//				PB.Dlg.warn('INVALID_INPUT_'+me.MSG_KEY, MODULE_PCM, {msg:msg});
//				return;
//			}
			
			var grid = me.getMainGrid();
			
			var myMask = new Ext.LoadMask({
				target:me.getMain(), 
				msg:"Please wait..."
			});
			
			myMask.show();
			
			var params;
			try {
				params = me.prepareParams();
	  		} catch (err) {
	  			console.error(err);
	  			myMask.hide();
	  			return;
	  		}
			
			Ext.Ajax.request({
			    url:me.URL+"/save",
			    method: "POST",
			    params: params,
			    success: function(response){
				
				  	var json = Ext.decode(response.responseText);
					  
				   	if (json.success) {
				   		var id = json.data.id;
				   		me.getHidId().setValue(id);
				   		me.getHidStatus().setValue(json.data.status);
				   		me.getMainForm().setTitle(PB.Label.m.edit+' : <font color="red">'+id+"</font>");
				   		
				   		me.getUploadGrid().editMode = true;
				   		var fileStore = me.getUploadGrid().getStore();
						fileStore.getProxy().api.read = ALF_CONTEXT + "/pcm/file/list";
						fileStore.getProxy().extraParams = {
						    id:id
						}
						fileStore.load();
				   		
				   		me.refreshGrid();
				   		PB.Dlg.info('SUCC_'+me.MSG_KEY, MODULE_PCM, {msg:'ID:'+id, scope:me});
				   	} else {
				   		
				   		if (json.data != undefined && json.data.valid != undefined && !json.data.valid) {
			   			
				   			var msg;
				   			
				   			if (json.data.msg) {
				   				msg = json.data.msg;
				   			}
	
					   		var opt;
				   			if (json.data.close) {
				   				opt = {msg:msg, fn:me.closeForm, scope:me}
				   			} else {
				   				opt = {msg:msg}
				   			}
				   			
				   			PB.Dlg.warn(null, MODULE_PCM, opt);
				   		} else {
				   			PB.Dlg.error('ERR_'+me.MSG_KEY, MODULE_PCM,{msg:json.message});
				   		}
				   	}
				   	 
			   	 	myMask.hide();
			
			    },
			    failure: function(response, opts){
			    	try {
			    		var json = Ext.decode(response.responseText);
				    	PB.Dlg.error('ERR_'+me.MSG_KEY+" ("+json.message+")", MODULE_PCM);
			    	}
			    	catch (err) {
				    	alert(response.responseText);
			    	}
				    myMask.hide();
			    },
			    headers: getAlfHeader()
			});
		
		});
		
	},
	
	getHSaveFieldValue:function(saveField, req) {
		var field = this.getMainForm().down("field[hSaveField="+saveField+"]");
		var v;
		if(field) {
			v = field.getValue();
		}
		else {
			if (req) {
				alert("Field Not Found : "+saveField);
				throw "fnf";
			}
		}
		return v;
	},
	
	getItems:function() {
	
		var me = this;
	
		var itemStore = me.getItemGrid().getStore();
		
		var data = [];
		itemStore.each(function(rec){
		   data.push(rec.data);
		});
		
		return Ext.JSON.encode(data);
	},
	
	getCmts:function() {
	
		var me = this;
		
		var panel = me.getCmtTab();
		
		var data = [];
		panel.items.each(function(grid){
			if (grid.xtype == 'grid') {
			
				var cmtStore = grid.getStore();
				
				var cmt = [];
				cmtStore.each(function(rec){
				   cmt.push(rec.data);
				});
				
				data.push({
					cmt:grid.title,
					cmtId:grid.cmtId,
					cmts:cmt
				});
				
			}
		});
	
		return Ext.JSON.encode(data);
	},	
	
	listFiles:function() {
		var values = [];
	
		var store = this.getFileTab().down("uploadGrid").getStore();
		
		store.each(function(rec){
			values.push({
				name:rec.get("name"),
				desc:rec.get("desc"),
				path:rec.get("path"),
				nodeRef:rec.get("nodeRef")
			});
		});

		return Ext.JSON.encode(values);
	},
	
	activateRptTab:function() {
		this.getMainForm().down("tabpanel").getLayout().setActiveItem(this.getRptTab());
	},

	finish:function() {
		var me = this;
		var msg = me.validForm(false);
		if (!msg) {
//			if (me.validFormRefId()) {
//				if(me.validForm2('doFinish')) {
//					PB.Dlg.confirm('CONFIRM_'+this.SEND_MSG_KEY,this,'doFinish', MODULE_PCM);
//				}
//			}
			
			if (me.validFormRefId()) {
				if (me.validForm2('doFinish')) {
					PB.Dlg.confirm('CONFIRM_'+me.SEND_MSG_KEY, me,'doFinish', MODULE_PCM);
				}
			}
			
		} else {
			PB.Dlg.warn('INVALID_INPUT_'+this.MSG_KEY, MODULE_PCM, {msg:msg});
			return;
		}
	},
	
	doFinish: function(){
		var me = this;
		
		var myMask = new Ext.LoadMask({
			target:me.getMain(), 
			msg:"Please wait..."
		});
		
		myMask.show();
		
		var params;
		
		try {
			params = me.prepareParams();
  		} catch (err) {
  			myMask.hide();
  			return;
  		}
		
		Ext.Ajax.request({
		    url:me.URL+"/finish",
		    method: "POST",
		    params: params,
		    success: function(response){
		  	  
			  	var json = Ext.decode(response.responseText);
				  
			   	if (json.success) {
			   		 
			   		PB.Dlg.info('SUCC_'+me.MSG_KEY, MODULE_PCM, {msg:'ID:'+json.data.id, fn:me.backToWfForm, scope:me});
			   		
			   	} else {
			   		if (json.data != undefined && json.data.valid != undefined && !json.data.valid) {
			   			
						var msg;
						
						if (json.data.msg) {
							msg = json.data.msg;
						}
						else {
							msg = "";
							
							if (json.data.users) {
								msg += "Invalid Users ==> " + json.data.users;
							}
							
							if (json.data.groups) {
								if (msg) {
									msg += "<br/><br/>";
								}
								
								msg += "Invalid Groups ==> " + json.data.groups;
							}
							
							if (msg) {
								msg += "<br/><br/>Please check whether group exists and has member.";
							}
						}
						
						PB.Dlg.warn(null, MODULE_PCM, {msg:msg});
					}
					else {
				   		PB.Dlg.error('ERR_'+me.MSG_KEY, MODULE_PCM,{msg:json.message});
					}

			   	}
			   	 
		   	 	myMask.hide();
		
		    },
		    failure: function(response, opts){
		    	try {
		    		var json = Ext.decode(response.responseText);
			    	PB.Dlg.error('ERR_'+me.MSG_KEY+" ("+json.message+")", MODULE_PCM);
		    	}
		    	catch (err) {
			    	alert(response.responseText);
		    	}
			    myMask.hide();
		    },
		    headers: getAlfHeader()
		});
	},
	
	cancelEdit:function() {
		PB.Dlg.confirm('CONFIRM_CANCEL_PCM_REQ',this,'backToWfForm', MODULE_PCM);
	},

	backToWfForm:function() {
		window.location = appContext+"/page/task-edit?taskId="+TASK_ID+"&referrer=tasks";
	},
	
	showAM:function() {
		var me = this;
		me.getBtnApprovalMatrix().show();
	},

	hideAM:function() {
		var me = this;
		me.getBtnApprovalMatrix().hide();
	},

	showApprovalMatrix:function() {
		var me = this;

//		var msg = me.validForm(false);
//		if (!msg) {
			me.doShowApprovalMatrix();
//			if (me.validForm2('doShowApprovalMatrix')) {
//				PB.Dlg.confirm('CONFIRM_'+me.SEND_MSG_KEY, me,'doShowApprovalMatrix', MODULE_PCM, 'enableSendBtn', me);
//			}
//		} else {
//			PB.Dlg.warn('INVALID_INPUT_'+me.MSG_KEY, MODULE_PCM, {msg:msg, scope:me});
//			return;
//		}
	},
	
	doShowApprovalMatrix:function() {
		var me = this;
		
		var grid = me.getMainGrid();
		
		var myMask = new Ext.LoadMask({
			target:me.getMain(), 
			msg:"Please wait..."
		});
		
		myMask.show();
		
		var params;
		
		try {
			params = me.prepareParams();
			} catch (err) {
				console.error(err);
				myMask.hide();
				return;
			}
		
		Ext.Ajax.request({
		    url:me.URL+"/am",
		    method: "POST",
		    params: params,
		    success: function(response){
		  	  
				var json = Ext.decode(response.responseText);
				  
			   	if (json.success) {
			   		PB.Dlg.info(null, MODULE_PCM, {msg:json.data, scope:me});
			   	} else {
			   		if (json.data != undefined && json.data.valid != undefined && !json.data.valid) {
			   			
			   			var msg;
			   			
			   			if (json.data.msg) {
			   				msg = json.data.msg;
			   			}
			   			else {
			   				msg = "";
			   				
					   		if (json.data.users) {
				   				msg += "Invalid Users ==> " + json.data.users;
					   		}
					   		
					   		if (json.data.groups) {
					   			if (msg) {
					   				msg += "<br/><br/>";
					   			}
					   			
				   				msg += "Invalid Groups ==> " + json.data.groups;
					   		}
					   		
					   		if (msg) {
				   				msg += "<br/><br/>Please check whether group exists and has member.";
					   		}
				   		}
				   		
				   		var opt;
			   				opt = {msg:msg}
			   			
			   			PB.Dlg.warn(null, MODULE_PCM, opt);
			   		}
			   		else {
				   		PB.Dlg.error(null, MODULE_PCM,{msg:json.message});
			   		}
			   	}
			   	 
		   	 	myMask.hide();
		
		    },
		    failure: function(response, opts){
		    	try {
		    		var json = Ext.decode(response.responseText);
			    	PB.Dlg.error('ERR_'+me.SEND_MSG_KEY+" ("+json.message+")", MODULE_PCM);
		    	}
		    	catch (err) {
			    	alert(response.responseText);
		    	}
			    myMask.hide();
		    },
		    headers: getAlfHeader()
		});
	},
	
	filterMethod:function(objType) {
		var me = this;
		var methodStore = me.getCmbMethod().getStore();
		methodStore.getProxy().extraParams = {
			objType : objType
		}
		
//		if (!me.getChkAcrossBudget().getValue()) { // not across budget
			if (me.getHidTotalCnv().getValue()) {
				methodStore.getProxy().extraParams.total = me.getHidTotalCnv().getValue(); 
			}
//		} else {
//			if (me.getTxtAcrossBudget().getValue()) {
//				methodStore.getProxy().extraParams.total = me.getTxtAcrossBudget().getValue();
//			}
//		}
		
		methodStore.load(function(s, recs) {
			if (me.getCmbMethod().getValue()) {
				var rec = methodStore.getById(parseInt(me.getCmbMethod().getValue()));
				if (!rec) {
					me.getCmbMethod().setValue(null);
					me.getCmtTab().removeAll(true);
				} else {
					me.getCmtTab().fireEvent("selectCmb", me, [rec], me.rec);
				}
			} else {
				var id = 0;
				methodStore.each(function(r){
					id = r.get('id');
				});
				me.getCmbMethod().setValue(id);
				var rec = methodStore.getById(id);
				me.getCmtTab().fireEvent("selectCmb", me, [rec], me.rec);
			}
		});
	},
	
	selectObjective:function(cmb, newV) {
		var me = this;
		
		me.filterMethod(newV);
	},
	
	selectCurrency:function(cmb, newV, oldV) {
		var me = this;
		
		if (newV == "THB") {
			me.getTxtCurrencyRate().setValue(1);
			me.getTxtCurrencyRate().disable();
			me.getLblBaht().disable();
			
			me.getTotalPanel().setHeight(88);
			
			me.getLblTotal().setText(PBPcm.Label.t.total);
			
			me.getHidTotalCnv().setValue(me.getHidTotal().getValue());
			
			me.getItemGrid().fireEvent("calSummary", me);
		}
		else {
			
			me.getTxtCurrencyRate().enable();
			me.getLblBaht().enable();
		
			Ext.Ajax.request({
			     url:ALF_CONTEXT+'/admin/main/currency/rate/get',
			     method: "GET",
			     params: {
					id: newV
			     },
			     success: function(res){
			    	 
			    	 var json = Ext.decode(res.responseText);
			      	  
			    	 if(json.success){
			    	  
			    		 me.getTxtCurrencyRate().setValue(json.data);
			    		 
			 			 me.getLblTotalCnv().setText(PBPcm.Label.t.total+" (x"+ me.getTxtCurrencyRate().getValue() +" THB)");
			 			 
			 			 me.getHidTotalCnv().setValue(me.getHidTotal().getValue() * json.data);

			 			 me.getLblNetAmtCnv().setText(Ext.util.Format.number(me.getHidTotalCnv().getValue(), DEFAULT_MONEY_FORMAT));
			 			 
			 			 me.getItemGrid().fireEvent("calSummary", me);
			 			 
			    	 }else{
			    		 PB.Dlg.error('ERR_'+me.CURRENCY_RATE_MSG_KEY, MODULE_PCM);
			    	 }
			    	 
			     },
			     failure: function(response, opts){
			    	 PB.Dlg.error('ERR_'+me.CURRENCY_RATE_MSG_KEY, MODULE_PCM);
			     },
			     headers: getAlfHeader()
			});
			
			me.getTotalPanel().setHeight(115);
			
			me.getLblTotal().setText(PBPcm.Label.t.total+" ("+ newV +")");
		}
		
		
//		me.getItemTab().doLayout();

	},
	
	selectPrototypeType:function(cmb, newV, oldV) {
		var me = this;
		
//		if (me.getHidBudgetCcType().getValue() == "P") {
//		
//			me.getCmbPrototypeNo().getStore().load({
//				params:{
//					p:me.getHidBudgetCc().getValue(),
//					t:me.getCmbPrototypeType().getValue(),
//					lang:getLang()
//				}
//			});
//		
//		} else {
//			console.log("budget cc type<>P");
//		}
	},	
	
	preview:function() {
		
		var me = this;
		
		PB.Util.checkSession(this, me.MSG_URL+"/get", function() {
		
			var msg = me.validForm(false);
			if (msg) {
				PB.Dlg.warn('INVALID_INPUT_'+me.MSG_KEY, MODULE_PCM, {msg:msg});
				return;
			}
			
			var grid = me.getMainGrid();
			
			var myMask = new Ext.LoadMask({
				target:me.getMain(), 
				msg:"Please wait..."
			});
			
			myMask.show();
			
			var params;
			
			try {
				params = me.prepareParams();
	  		} catch (err) {
	  			console.log(err);
	  			myMask.hide();
	  			return;
	  		}
	  		
	  		var win = window.open("", "_new");
				
			Ext.Ajax.request({
			    url:me.URL+"/preview",
			    method: "POST",
			    params: params,
			    success: function(response){
			  	  
				  	var json = Ext.decode(response.responseText);
					  
				   	if (json.success) {
//						win.open(me.URL+"/preview?id="+json.data[0].id,"_blank");
						win.location.href = me.URL+"/preview?id="+json.data[0].id;
				   	} else {
				   		PB.Dlg.error('ERR_'+me.PREVIEW_MSG_KEY, MODULE_PCM,{msg:json.message});
				   		win.close();
				   	}
				   	 
			   	 	myMask.hide();
			
			    },
			    failure: function(response, opts){
			    	try {
			    		var json = Ext.decode(response.responseText);
				    	PB.Dlg.error('ERR_'+me.PREVIEW_MSG_KEY+" ("+json.message+")", MODULE_PCM);
			    	}
			    	catch (err) {
				    	alert(response.responseText);
			    	}
				    myMask.hide();
				    win.close();
			    },
			    headers: getAlfHeader()
			});
	
		});
	},
	
	notStock:function() {
		var me = this;
		
		me.getCmbStockOu().clearValue();
	},
	
	selectIsPrototype:function(rad, v) {
		var me = this;
		
		if (v) {
			me.getCmbPrototypeType().setDisabled(false);
//			me.getCmbPrototypeNo().setDisabled(false);
		} else {
			me.getCmbPrototypeType().setDisabled(true);
			me.getCmbPrototypeType().clearValue();
//			me.getCmbPrototypeNo().setDisabled(true);
//			me.getCmbPrototypeNo().clearValue();
		}
		me.getRaPrototypeYes().clearInvalid();
		me.getRaPrototypeNo().clearInvalid();
	},
	
	selectBudgetCcCallBack:function(ids, type, typeName, fund, fundName) {
		var tab = this.targetPanel;
		
		setValue(tab, 'budgetCc', ids[0]);
		setValue(tab, 'budgetCcName', ids[1]);
		setValue(tab, 'budgetCcType', type);
		setValue(tab, 'budgetCcTypeName', typeName);
		
		setValue(tab, 'fundId', fund);
		setValue(tab, 'fundName', fundName);
		
		tab.down("button[action='showBudget']").show();
		
		var sc = this.sc;
		var saDisabled = type=="A" || type=="C";
		sc.getChkSmallAmount().setDisabled(saDisabled);
		if(saDisabled) {
			sc.getChkSmallAmount().setValue(null);
		}
	},
	
	selectBudgetCc:function() {
		var me = this;

		var dlg = Ext.create("PB.view.common.SearchBudgetSrcDlg",{
			title:PB.Label.m.search,
			targetPanel:this.getInfoTab(),
			callback:this.selectBudgetCcCallBack,
			sc:me
		});
		dlg.show();
	},
	
	selectReqByCallBack:function(id, rec) {
		var tab = this.targetPanel;
		setValue(tab, 'reqBy', rec.get('code'));
		setValue(tab, 'reqByName', rec.get('title') + ' ' + rec.get('fname') + ' ' + rec.get('lname'));

		var mphone = rec.get("mphone")!=null ? rec.get("mphone") : "";
		var wphone = rec.get("wphone")!=null ? rec.get("wphone") : "";
		var comma = (mphone!="" && wphone!="") ? "," : "";
		
		setValue(tab, 'reqTelNo', wphone+comma+mphone);
		setValue(tab, 'reqByDept', rec.get('position'));
		setValue(tab, 'reqBu', rec.get('org_name'));
		setValue(tab, 'reqOuName', rec.get('utype'));
	},	

	selectReqBy:function() {
		var me = this;

		var dlg = Ext.create("PB.view.common.SearchUserDlg",{
			title:PB.Label.m.search,
			targetPanel:this.getUserTab(),
			callback:this.selectReqByCallBack
		});
		dlg.show();
	},
	
	selectCostControlCallBack:function(id, name, type, typeName) {
		var tab = this.targetPanel;
		setValue(tab, 'costControlId', id);
		setValue(tab, 'costControlName', name);
		setValue(tab, 'costControlTypeId', type);
		setValue(tab, 'costControlTypeName', typeName);
	},
	
	selectCostControl:function() {
		Ext.create("PB.view.common.SearchCostControlDlg",{
			title:PB.Label.m.search,
			targetPanel:this.getInfoTab(),
			sectionId:this.getInfoTab().down("field[name=budgetCc]").getValue(),
			callback:this.selectCostControlCallBack
		}).show();
	},
	
	clearCostControl:function() {
		var tab = this.getInfoTab();
		setValue(tab, 'costControlId', null);
		setValue(tab, 'costControlName', '');
		setValue(tab, 'costControlTypeId', null);
		setValue(tab, 'costControlTypeName', '');
	},
	
	acrossBudgetBlur:function(txt) {
		var me = this;
		me.filterMethod(me.getCmbObjectiveType().getValue());
	},
	
	isAcrossBudget:function(chk, v) {
		var me = this;
		var columns = [];
		
		columns.push(
	        	{
	        		xtype: 'actioncolumn',
		        	dataIndex: 'action',
		        	text: '', 
		            width: 80,
		            align:'center',
		            items: [{
		                tooltip: 'Copy', 
		                action : 'copy',
		        	    getClass: function(v) {
	        	    		return getActionIcon(v, "C", 'copy');
		        	    }
	            	}, {
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
	        	}
	    );		
//		var txt = this.getInfoTab().down("[name=acrossBudget]");
		
		me.getChkSmallAmount().setDisabled(v || me.getHidBudgetCcType().getValue()=="C" || me.getHidBudgetCcType().getValue()=="A");

		if (v) {
//			txt.enable();
			me.getChkSmallAmount().setValue(null);
			
			me.getChkRefId().setValue(false);
					
			columns.push(
				{ text: PBPcm.Label.t.fiscalYear,  dataIndex: 'fiscalYear', width:80}
			);
		} else {
//			txt.disable();
//			txt.setValue(null);
		}
		
		me.filterMethod(me.getCmbObjectiveType().getValue());
		
		columns.push(
				{ text: PBPcm.Label.t.actGrp,  dataIndex: 'actGrpName', flex:0.75},
				{ text: PBPcm.Label.t.act,  dataIndex: 'actName', flex:0.75},
				{ text: PBPcm.Label.t.name,  dataIndex: 'description', flex:1},
				{ text: PBPcm.Label.t.asset,  dataIndex: 'assetName', flex:0.75},
				{ text: PBPcm.Label.t.qty,  dataIndex: 'quantity', width:80, align:'right', xtype: 'numbercolumn', format:'0,000'},
				{ text: PBPcm.Label.t.uom,  dataIndex: 'unit', width:110, align:'center'},
				{ text: PBPcm.Label.t.prc,  dataIndex: 'price', width:100, align:'right', xtype: 'numbercolumn', format:'0,000.00'},
				{ text: PBPcm.Label.t.subtotal,  dataIndex: 'total', width:180, align:'right', xtype: 'numbercolumn', format:'0,000.00'}
		);
		
		me.getItemGrid().reconfigure(undefined,columns);
	},
	
	isRefId:function(chk, v) {
		var me = this;
		var txt = this.getInfoTab().down("[name=refId]");
		if (v) {
			txt.enable();
			me.getChkAcrossBudget().setValue(false);
		} else {
			txt.disable();
			txt.setValue(null);
		}
	},
	
	selectPRCallBack:function(sender, id, rec) {
		var tab = sender.up("window").targetPanel;
		setValue(tab, 'refId', id);
	},

	selectPR:function() {
		Ext.create("PBPcm.view.pr.SearchDlg",{
			title:'ค้นหา',
			targetPanel:this.getInfoTab(),
			callback:this.selectPRCallBack
		}).show();
	},
	
	searchPR:function(sender) {
		var me = this;
		
		PB.Util.checkSession(this, me.MSG_URL+"/get", function() {
		
			if (me.getCmbObjectiveType().getValue()) {
				var store = sender.up("window").down("grid").getStore();
				
				store.getProxy().extraParams = {
					s:sender.up("container").down("field[itemId=searchTerm]").getValue(),
					fields:"{objective_type:'"+me.getCmbObjectiveType().getValue()+"'}",
					lang:getLang()
				}
				
				store.load();
			} else {
				PB.Dlg.warn('ERR_NOT_CHOOSE_OBJECTIVE_TYPE', MODULE_PCM);
			}
		
		});
	},
	
	confirmPR:function(sender) {
		var me = this;
		
		var id = getRadioValue("id");
		
		var store = sender.up("window").down("grid").getStore();
		var rec;
		
		for(var i=0; i<store.getCount(); i++) {
			var r = store.getAt(i);
			if (r.get('id') == id) {
				rec = r;
			}
		}
		
		sender.up("window").callback(sender, id, rec);
		sender.up("window").close();
	},
	
	viewPRDetail:function(r) {
		var me = this;
		
	  	var win = window.open("", "_new");

		PB.Util.checkSession(this, me.MSG_URL+"/get", function() {

			win.location.href = Alfresco.constants.PROXY_URI_RELATIVE+"api/node/content/"+nodeRef2Url(r.get("doc_ref"));
//			window.open(Alfresco.constants.PROXY_URI_RELATIVE+"api/node/content/"+nodeRef2Url(r.get("doc_ref")),"_new");
		
		});
	},
	
	selectReason:function(cmb, newV, oldV) {
		var me = this;
		
		me.getTxtReasonOth().setDisabled(newV != "อื่นๆ");
		if (newV != "อื่นๆ") {
			me.getTxtReasonOth().setValue(null);
		}
	},
	
	showBudget:function() {
		var me = this;
		
		PB.Util.checkSession(this, me.MSG_URL+"/get", function() {
		
			Ext.Ajax.request({
			    url:ALF_CONTEXT+"/admin/main/totalPreBudget",
			    method: "GET",
			    params: {
					budgetCc:me.getHidBudgetCc().getValue(), 
					budgetCcType:me.getHidBudgetCcType().getValue(),
					fundId:me.getHidFundId().getValue()
				},
			    async:false,
			    success: function(response){
				
					var json = Ext.decode(response.responseText);
					
					var tab = me.getInfoTab();
					var rec = {
							name : tab.down("field[name=budgetCcTypeName]").getValue()+" "+tab.down("field[name=budgetCcName]").getValue()+" ("+tab.down("field[name=fundName]").getValue()+")",
							balance : json.data.balance,
							preAppBudget : json.data.pre,
							expBalance : json.data.ebalance
					}
				
					Ext.create("PB.view.common.CheckBudgetDlg",{
						title:'งบประมาณ',
						rec:rec
					}).show();
			    }
			});
		
		});
	},
	
	changeRate:function() {
		var me = this;
		var rate = me.getTxtCurrencyRate().getValue();
		me.getLblTotalCnv().setText(PBPcm.Label.t.total+" (x"+ rate +" THB)");
		me.getHidTotalCnv().setValue(me.getHidTotal().getValue() * rate);
		me.getLblNetAmtCnv().setText(Ext.util.Format.number(me.getHidTotalCnv().getValue(), DEFAULT_MONEY_FORMAT));
		
		me.getItemGrid().fireEvent("calSummary", me);	
	},
	
	isSmallAmount:function(chk, v) {
		var me = this;
		me.getMainCmtTab().setDisabled(v);
		me.getCmbMethod().setDisabled(v);

		me.getChkAcrossBudget().setDisabled(v);

		if(v) {
			me.getCmbMethod().setValue(null);
			me.getMainCmtTab().fireEvent("selectCmb", me, [], me.getMainCmtTab().rec);
			
			me.getHidMethodCond2Rule().setValue(null);
			me.getCmbMethodCond2().setValue(null);
			me.getTxtMethodCond2Dtl().getValue(null);
			
			me.getChkAcrossBudget().setValue(null);
		}
	}

});
