Ext.define('PBPcm.controller.Main', {
    extend: 'Ext.app.Controller',
    
	refs:[{
	    ref: 'main',
	    selector:'pcmReqMain'
	},{
	    ref: 'mainGrid',
	    selector:'pcmReqMainGrid'
	},{
		ref:'txtSearch',
		selector:'pcmReqMainGrid [itemId=txtSearch]'
	},{
		ref:'mainForm',
		selector:'pcmReqMainForm'
	},{
    	ref:'userTab',
		selector:'pcmReqUserTab'
	},{
    	ref:'infoTab',
		selector:'pcmReqInfoTab'
	},{
    	ref:'fileTab',
		selector:'pcmReqFileTab'
	},{
    	ref:'rptTab',
		selector:'pcmReqRptTab'
    },{
        ref: 'hidId',     
        selector: 'pcmReqMainForm field[name=id]'
    },{
        ref: 'hidHId',     
        selector: 'pcmReqMainForm field[name=hId]'
    },{
        ref: 'hidStatus',     
        selector: 'pcmReqMainForm field[name=status]'
    },{
        ref: 'btnApprovalMatrix',     
        selector: 'pcmReqMainForm button[action=approvalMatrix]'
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
        ref: 'btnAdd',
        selector:'pcmReqMain [action=add]'
	}],
	
	init:function() {
		var me = this;

		me.control({
			'pcmReqMain [action=add]': {
				click : me.add
			},
			'pcmReqMain [action=search]': {
				click : me.search
			},
			'pcmReqMainGrid':{
				copy : me.copy,
				edit : me.edit,
				del : me.del,
				search : me.search,
   				showDiagram : me.showDiagram,
   				gotoFolder : me.gotoFolder,
   				viewDetail : me.viewDetail,
   				viewHistory : me.viewHistory,
   				view : me.view
			}
		});
	
	},
	
	COPY_MSG_KEY : 'COPY_PCM_REQ',
	MSG_KEY : 'DELETE_PCM_REQ',
    URL : ALF_CONTEXT+'/pcm/req',
    ADMIN_URL : ALF_CONTEXT+'/admin/pcm/req',
    MSG_URL : ALF_CONTEXT+'/pcm/message',
	
	onLaunch:function(app) {
		var me = this;
	
		var mainGrid = me.getMainGrid();
		if (mainGrid) {
			var tbar = me.getMainGrid().getDockedComponent(1);
			
			Ext.Ajax.request({
			      url:me.URL+"/criteria/list",
			      method: "GET",
			      success: function(response){
			    	  
			    	var data = Ext.decode(response.responseText).data;
			    	
					for(var i=data.length-1; i>=0; i--) {
						
						var d = data[i];
						
						var store = Ext.create('PB.store.common.ComboBoxStore');
						
						store.getProxy().api.read = ALF_CONTEXT+'/srcUrl/'+PB.Util.getSrcUrl(d.url);
						if (d.param) {
							var params = d.param.split(",");
							
							store.getProxy().extraParams = {
								p1 : params[0],
								orderBy : 'code',
								all : true,
								lang : getLang()
							}
							if (params.length>1) {
								store.getProxy().extraParams.p2 = params[1];
							}
							
						}
						store.load();
						
						var c = {
							xtype:'combo',
							itemId:'cri'+i,
							bgData:d,
							hideLabel:true,
					    	displayField:'name',
					    	valueField:'id',
					        emptyText : d.emptyText,
					        store: store,
					        queryMode: 'local',
					        typeAhead:true,
					        multiSelect:false,
					        forceSelection:true,
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
								}
							}
						}
						
						if (d.listWidth) {
					        c.matchFieldWidth = false;
							c.listConfig.width = d.listWidth;
						}
						
						if (d.width) {
							c.width = d.width;
						}
						
						tbar.insert(0, c);
	
					}
					
			      },
			      failure: function(response, opts){
			          alert("failed");
			      },
			      headers: getAlfHeader()
			}); 
		}
		
		if (ID) {
			
			Ext.Ajax.request({
			      url:me.URL+"/get",
			      method: "GET",
			      params: {
			    	  id : ID,
			    	  lang : getLang()
			      },
			      success: function(response){
			    	  
			    	var data = Ext.decode(response.responseText).data;
			    	
			    	var rec = Ext.create("PBPcm.model.GridModel",data[0]);

					me.edit(rec);
					
			      },
			      failure: function(response, opts){
			          alert("failed");
			      },
			      headers: getAlfHeader()
			});

		}
	},
	
	search:function() {
		var me = this;
		
		PB.Util.checkSession(this, me.MSG_URL+"/get", function() {

			var store = me.getMainGrid().getStore();
			
			var params = {
				s : me.getTxtSearch().getValue(),
				lang : getLang()
			}
			
			var fields = {};
			
			var tbar = me.getMainGrid().getDockedComponent(1);
			for(var i=0; i<10; i++) {
				var c = tbar.down("[itemId=cri"+i+"]");
				if (c) {
					var d = c.bgData;
					fields[d.field] = c.getValue();
				}
			}
			
			params.fields = Ext.JSON.encode(fields);
			
			store.getProxy().extraParams = params;
			store.currentPage = 1;
			store.load();
		
		});
	},
	
	activateForm:function() {
		this.getMain().getLayout().setActiveItem(1);
	},
	
	createForm:function(title, rec) {
		var me = this;
	
		me.getMain().insert(1, {
			xtype:'pcmReqMainForm',
			title:title
		})

		var form = me.getMainForm().down("tabpanel");

		form.removeAll(true);
		
//		form.down("field[name=hId]").setValue(header.id);
		Ext.Ajax.request({
		      url:me.MSG_URL+"/list",
		      method: "GET",
		      params: {
		    	  keys : "TAB_TITLE_USER,TAB_TITLE_INFO,TAB_TITLE_ITEM,TAB_TITLE_CMT,TAB_TITLE_FILE",
		    	  lang:getLang()
		      },
		      success: function(response){
		    	  
		    	var data = Ext.decode(response.responseText).data;
		    	
		    	var firstTab = form.add({ xtype:'pcmReqUserTab', title:data[0].message, rec:rec });
		    	form.setActiveTab(firstTab);
		    	
		    	form.add({ xtype:'pcmReqInfoTab', title:data[1].message, rec:rec });
    			form.add({ xtype:'pcmReqItemTab', title:data[2].message, rec:rec });
    			form.add({ xtype:'pcmReqCmtTab', title:data[3].message, rec:rec , disabled:rec.is_small_amount == "1"});
    			form.add({ xtype:'pcmReqFileTab', title:data[4].message, rec:rec, editMode:me.isEditMode() });
    			
    			Ext.defer(function() {
				   validForm(me.getMainForm());
					var f = me.getRaPrototype();
					if (!f.getGroupValue()) {
					   me.getRaPrototypeYes().markInvalid("Choose either Yes or No");
					   me.getRaPrototypeNo().markInvalid("Choose either Yes or No");
					}
				},  1000) ;
		      },
		      failure: function(response, opts){
		          alert("failed");
		      },
		      headers: getAlfHeader()
		}); 

	},
	
	getInitField:function(d, prefix, settings) {
	
		var mand = (this.requires && this.requires.indexOf(prefix+'_'+d.fname)>=0);

		var field = {
			fieldLabel:d.dname,
			id:prefix+'_'+d.fname,
		    name:prefix+'_'+d.fname,
		    margin:'5 0 0 10',
		    labelWidth: settings.lblWidth,
		    width: parseInt(d.dwidth),
	        allowBlank : !(d.mandat == "true") && !mand,
	        bgData:d,
	        hSaveField:d.saveField,
	        maxLength:d.flength,
	        enforceMaxLength:true,
	        value:d.v
		};
		
		return field;
	},
	
	showMandatoryLabel:function(field) {
		if (field.bgData.dtype != "R") {
			if (!(field.bgData.dtype == "A" && field.bgData.pname && field.bgData.pname!="null")) {
				if (!field.allowBlank) {
					field.fieldLabel += " <font color=\"red\">*</font>";
				}
			}
		}
	},
	
	mergeTextField:function(field, d) {
	
		var m;
	
		if (d.ftype == 'I' || d.ftype == 'F') {
			m = {
				xtype : 'numericfield',
				hideTrigger : true
			}
			
			if (d.dformat) {
				var format = d.dformat.split("|");
				
				m.alwaysDisplayDecimals = true;
				
				if (format[0] == "-") {
					m.allowNegative = false;
				}
				
				if (format.length > 1) {
					
					m.useThousandSeparator = format[1].trim() != "";
					if (m.useThousandSeparator) {
						m.thousandSeparator = format[1];
					}
					
					if (format.length > 2) {
						m.allowDecimals = (format[2].trim() != "0") && (format[2].trim() != "");
						if (m.allowDecimals) {
							m.decimalPrecision = parseInt(format[2].trim());
							m.decimalSeparator = '.';
						}
						
						if (format.length > 3) {
							if (format[3]) {
								m.currencySymbol = format[3];
							}
						}
						
					}
					
				}
			}
		}
		else {
			m = {
				xtype : 'textfield'
			}
		}
		
		Ext.merge(field, m);
		
		if (d.dheight && d.dheight!="null") {
			field.height = parseInt(d.dheight)
		}

		return field;
	},
	
	mergeCalendarField:function(field, d) {
		var me = this;
	
		if (d.ftype == "D") { // Date
			Ext.merge(field, {
				    xtype:'datefield',
				    format:d.dformat,
				    invalidText:'Invalid Format'
			});
		}
		else 
		if (d.ftype == "E") { // DateTime
			Ext.merge(field, {
				    xtype:'datetimefield',
				    format:d.dformat,
				    invalidText:'Invalid Format'
			});
		}
		else 
		if (d.ftype == "T") { // Time
			Ext.merge(field, {
				    xtype:'uxtimepicker',
				    format:d.dformat,
				    invalidText:'Invalid Format',
				    showSecond:d.dformat ? d.dformat.indexOf('s')>=0 : false
			});
		}
		
		if (d.spcParam) {
			var sparam = d.spcParam;
			var kw = "endField=";
			var pos = sparam.indexOf(kw);
			if (pos >= 0) {
				var fname = me.paramValue(sparam, ";", pos+kw.length);
				fname = me.absName(fname);
				
				Ext.merge(field, {
					vtype: 'daterange',
					endDateField: fname,
					listeners : {
						change : function(d, newV, oldV) {
			                var end = d.up('form').down('#' + d.endDateField);
			                end.setMinValue(newV);
			                end.validate();
			                d.dateRangeMin = newV;
						}
					}
				});
			}
			
			kw = "startField=";
			pos = sparam.indexOf(kw);
			if (pos >= 0) {
				var fname = me.paramValue(sparam, ";", pos+kw.length);
				fname = me.absName(fname); 
				
				Ext.merge(field, {
					vtype: 'daterange',
					startDateField: fname,
					listeners : {
						change : function(d, newV, oldV) {
			                var start = d.up('form').down('#' + d.startDateField);
			                start.setMaxValue(newV);
			                start.validate();
			                d.dateRangeMax = newV;
						}
					}
				});
			}
		}
	
		if (d.v) {
			if (d.v == "now") {
				field.value = new Date();
			}
			else
			if (d.v.indexOf("T") >= 0) {
				var t = d.v.split("T");
				field.value = Ext.Date.parse(t.join(" "), "Y-m-d H:i:s");
			}
			else {
				field.value = Ext.Date.parse(d.v, "Y-m-d H:i:s.u");
			}
		}
		
		return field;
	},
	
	paramValue:function(param, sep, pos1) {
		var pos2 = param.indexOf(sep,pos1);
		return (pos2>=0) ? param.substring(pos1, pos2) : param.substring(pos1);
	},
	
	absName:function(n) {
		return (n.indexOf("H_") == 0 || n.indexOf("F_") == 0) ? n : "F_" + n;
	},
	
	mergeTextAreaField:function(field, d) {
		Ext.merge(field, {
			xtype:'textarea'
		});
		
		if (d.dheight && d.dheight!="null") {
			field.height = parseInt(d.dheight);
		}
		
		return field;
	},
	
	mergeLabelField:function(field, d) {
		Ext.merge(field, {
			xtype:'textarea',
			readOnly:true,
		    fieldStyle:'background-color: #ddd; background-image:none;'
		});
		
		if (d.dheight && d.dheight!="null") {
			field.height = parseInt(d.dheight);
		}
		
		return field;
	},
	
	mergeHiddenField:function(field) {
		field.xtype = 'hiddenfield';
		
		return field;
	},
	
	mergeCheckBoxField:function(field, d) {
		field.xtype = 'checkbox';
		field.hideLabel = true;
		field.boxLabel = d.dname;
		field.inputValue = 'true';
		field.uncheckedValue = 'false';
		if (field.value == 'true') {
			field.checked = true;
		}
		
		return field;
	},
	
	mergeRadioBoxField:function(field, d) {
		var me = this;
		
		Ext.merge(field, {
			xtype : 'radiogroup',
			defaultType : 'radiofield',
			defaults:{flex:1},
        	layout : d.dformat && (d.dformat == "v") ? "vbox" : "hbox",
        	items:[],
        	invalidCls:'x-radio-invalid'
		});
		
		if (!d.srcUrl || (d.srcUrl == "null")) {
			PB.Dlg.error(null, MODULE_PCM, "Invalid Source Url : "+d.srcUrl+  ", Radio Box Field Name : "+d.fname);
		} else {
			
			var extraParams = {};
			if (d.srcParam && d.srcParam.indexOf("${") < 0) { // srcParam don't need value from other field
				var params = d.srcParam.split(",");
				
				for(var pi=0; pi<params.length; pi++) {
					
					var v = params[pi];
		
					extraParams["p"+(pi+1)] = v;
				}
				
				extraParams["formMode"] = (ID ? "E" : "C");
			}			
			
			Ext.Ajax.request({
			      url:ALF_CONTEXT+'/srcUrl/'+PB.Util.getSrcUrl(d.srcUrl),
			      method: "GET",
			      params:extraParams,
		      	  async: false,
			      success: function(response){
			    	  
			    	var data = Ext.decode(response.responseText).data;
			    	
					for(var i=0; i<data.length; i++) {
						
						var da = data[i];
						
						var r = {
					        boxLabel  : da.name,
		                    name      : field.name,
		                    inputValue: da.id,
		                    checked : d.v == da.id,
		                    readOnly : field.readOnly,
			    			listeners:{
		    					change: function(radio, nv, ov) {
		    						me.handleTriggerFieldEvent(field.bgData);
		    					}
		    				}
						}
						
						field.items.push(r);
					}
					
			      },
			      failure: function(response, opts){
			          alert("failed");
			      },
			      headers: getAlfHeader()
			}); 

		}
		
		return field;
	},
	
	mergePanel:function(field, d) {
		field.xtype = 'panel';
		field.title = d.dname;
		
		if (d.dheight && d.dheight!="null") {
			field.height = parseInt(d.dheight);
		}
		
		if (d.dformat && d.dformat=="h") {
			field.layout = "hbox";
		}
		
		return field;
	},
	
	mergeListBoxField:function(field, d) {
		var me = this;
		var store = Ext.create("PB.store.common.ComboBoxStore",{
			listeners:{
				load : {
					fn:me.onListBoxLoad,
					scope:me
				}
			}
		});
		store.oname=field.name;
		
		if (!d.srcUrl || (d.srcUrl == "null")) {
			PB.Dlg.error(null, MODULE_PCM, "Invalid Source Url : "+d.srcUrl+  ", Check List Box Field Name : "+d.fname);
		} else {
			store.getProxy().api.read = ALF_CONTEXT+"/srcUrl/" + PB.Util.getSrcUrl(d.srcUrl);
			
			if (d.srcParam && d.srcParam.indexOf("${") < 0) { // srcParam don't need value from other field
				var extraParams = {};
				var params = d.srcParam.split(",");
				
				for(var pi=0; pi<params.length; pi++) {
					
					var v = params[pi];
		
					extraParams["p"+(pi+1)] = v;
				}
				
				extraParams["formMode"] = (ID ? "E" : "C");
				store.getProxy().extraParams = extraParams;
				store.load();
			}
			else
			if (!d.srcParam) {
				store.load();
			}
			else {
				store.load();
			}
		}
		
		Ext.merge(field, {
			xtype:'combo',
	    	displayField:'name',
	    	valueField:'id',
	        emptyText : EMTY_TEXT_COMBO,
	        store: store,
	        queryMode: 'local',
	        typeAhead:true,
	        multiSelect:false,
	        forceSelection:true,
	        listeners:{
				beforequery : function(qe) {
					qe.query = new RegExp(qe.query, 'i');
	//				qe.forceAll = true;
				}
			}
		});
		
		return field;
	},
	
	checkReadOnlyModeHdr:function(d) {
		var me = this;
	
		d.crDis = "";
		if (d.spcParam) {
			var kw = "readOnly=";
			var pos = d.spcParam.indexOf(kw);
			
			if (pos >= 0) {
				var v = me.paramValue(d.spcParam, ";", pos+kw.length);
				
				if (v.indexOf("C") >= 0) {
					d.crDis = "true";
				}
				if (v.indexOf("E") >= 0) {
					d.edDis = "true";
				}
			}
		}
	
		return d;
	},
	
	checkReadOnlyModeFmt:function(d) {
		var me = this;
	
		d.edDis = "";
		if (d.spcParam) {
			var kw = "readOnly=";
			var pos = d.spcParam.indexOf(kw);
			
			if (pos >= 0) {
				var v = me.paramValue(d.spcParam, ";", pos+kw.length);
				
				d.edDis = ","+v+",";
			}
		}
		
		return d;
	},
	
	checkMandatoryMode:function(d) {
		var me = this;
		
		d.mandatory = "";
		if (d.spcParam) {
			var kw = "mandatory=";
			var pos = d.spcParam.indexOf(kw);
			
			if (pos >= 0) {
				var v = me.paramValue(d.spcParam, ";", pos+kw.length);
				
				d.mandatory = ","+v+",";
			}
		}
	
		return d;
	},
	
	handleTriggerFieldEvent:function(d) {
		var me = this;
		
		if (d.tgrField) {
			
			var parent = me.getMainForm();
		
			var tgrNames = d.tgrField.split(",");
			
			for(var ti=0; ti<tgrNames.length; ti++) {
				
				var tName = tgrNames[ti];
				var prefix = tName.substring(0,2);
				if ((prefix != "H_") && (prefix != "F_")) {
					tName = "F_" + tName;
					prefix = "F_";
				}
				
				var field = parent.down("field[name="+tName+"]");
				if (!field) {
					var errMsg = "Trigger Not Found : "+tName;
					if (prefix == "H_") {
						alert(errMsg);
					} else {
						console.log(errMsg);
					}
					
					return;
				}
				
				var params = field.bgData.srcParam.split(",");
				
				if (params.length > 0) {
					
					var extraParams = me.getStoreExtraParams(params, parent);
					
					if (field.alias.join().indexOf("combo") >= 0) {
						var store = field.getStore();
						store.ov = field.getValue();
						store.getProxy().extraParams = extraParams;
						store.load();
					}
					else {
						var srcUrl = PB.Util.getSrcUrl(field.bgData.srcUrl);
						
						if (srcUrl && srcUrl.substring(0,2) == "${") {
							var pos2 = srcUrl.indexOf("}");
							var srcName = srcUrl.substring(2,srcUrl.length-1);
							
							var pfx = srcName.substring(0,2);
							if ((pfx != "H_") && (pfx != "F_")) {
								srcName = "F_" + srcName;
							}

							var f = parent.query("field[name="+srcName.trim()+"]");
							var fv = "";
							if (!f) {
								alert("Field Not Found : "+srcName.trim());
							}
							else {
								if (f instanceof Array) {
									for(var i=0; i < f.length; i++) {
										var fi = f[i];
										if (fi.inputType == "radio") {
											if (fi.checked) {
												fv = fi.inputValue;
												break;
											}
										}
										else {
											fv = fi.getValue();
										}
									}
								} else {
									fv = f.getValue();
								}
							}							

							field.setValue(fv);
						}
						else {
							Ext.Ajax.request({
							      url:ALF_CONTEXT + "/srcUrl/" + srcUrl,
							      method: "GET",
							      params: extraParams,
							      success: function(response){
									var dataArray = Ext.decode(response.responseText).data;
									if (dataArray.length > 0) {
								    	if (prefix == "H_" && (field.hSaveField == "approval_matrix_id")) {
								    		if (dataArray.length > 1) {
								    			PB.Dlg.error(null, MODULE_PCM, {msg:"Found "+dataArray.length+" Approval Matrixes! Please contact administrator"});
								    			field.setValue(null);
									    		me.getBtnApprovalMatrix().hide();
								    			return;
								    		}
								    		
								    		field.setValue(dataArray[0].id);
								    		me.getBtnApprovalMatrix().show();
								    		me.getBtnApprovalMatrix().fid = field.getValue();
								    		me.loadDefaultUsers(field.getValue());
										}
										else {
											field.setValue(dataArray[0].id);
										}
							      	}
							      	else {
										field.setValue(null);
								    	if (prefix == "H_" && (field.hSaveField == "approval_matrix_id")) {
								    		me.getBtnApprovalMatrix().hide();
								    		me.getBtnApprovalMatrix().fid = null;
								    	}
							      	}
	
							      },
							      failure: function(response, opts){
							          alert("failed");
							      },
							      headers: getAlfHeader(),
							      async:false
							});
						}
					}
				}
			}
		}
	},

	enableAddBtn:function(me) {
		Ext.defer(function () {
			me.getBtnAdd().enable();
	    }, 800);
	},

	add:function() {
		var me = this;
		
		if (!me.getBtnAdd().isDisabled()) {

			me.getBtnAdd().disable();
			
			PB.Util.checkSession(this, me.MSG_URL+"/get", function() {
			
				if (me.getMainForm()) {
					me.getMainForm().destroy();
				}
				
				delete me.data; // Form Add Mode
		
				Ext.Ajax.request({
				      url:me.URL+"/userDtl",
				      method: "GET",
				      params:{
						m:'c',
						lang:getLang()
					  },
				      success: function(response){
				    	  
				    	var json = Ext.decode(response.responseText);
				    	
				    	var data = json.data[0];
				    	data.created_time = new Date();
				    	
						me.createForm(PB.Label.m.create, data);
						
						me.activateForm();
						
						me.getHidId().setValue(null);
						me.getHidStatus().setValue(null);
						
				      },
				      failure: function(response, opts){
				          alert("failed");
				      },
				      headers: getAlfHeader()
				});		
	
			});
		
		}
	},
	
	edit: function(rec) {
		var me = this;
		
		if (!me.editing) {
			
			me.editing = true;
		
			PB.Util.checkSession(this, me.MSG_URL+"/get", function() {
	
				if (!ID) {
					if (me.getMainForm()) {
						me.activateForm();
						me.editing = false;
						return;
					}
			
					me.getMainGrid().getView().getSelectionModel().select(rec);
				}
				
				Ext.Ajax.request({
				      url:me.URL+"/get",
				      method: "GET",
				      params: {
				    	  id : rec.get("id"),
				    	  lang:getLang()
				      },
				      success: function(response){
				    	  
				    	var json = Ext.decode(response.responseText);
				    	
				    	me.data = json.data[0]; // Form Edit Mode
				    	me.data.items = json.items;
				    	
				    	Ext.Ajax.request({
					      url:me.URL+"/userDtl",
					      method: "GET",
					      params:{
				    		 r:me.data.req_by,
				    	     c:me.data.created_by,
				    	     m:'e',
				    	     lang:getLang()
				    	  },
					      success: function(response){
					    	  
					    	var json = Ext.decode(response.responseText);
					    	
					    	var data = json.data[0];
					    	
					    	Ext.merge(me.data, data);
					    	
							me.createForm(PB.Label.m.edit+' : <font color="red">'+rec.get("id")+"</font>", me.data);
							if (ID) {
								var form = me.getMainForm(); 
								form.down("button[action=send]").hide();
								form.down("button[action=saveDraft]").hide();
								form.down("button[action=cancel]").hide();
								form.down("button[action=finish]").show();
								form.down("button[action=cancelEdit]").show();
							}
							else {
								me.activateForm();
							}
							
							me.getHidId().setValue(rec.get("id"));
							me.getHidStatus().setValue(rec.get("status"));
							
							me.editing = false;
					      },
					      failure: function(response, opts){
					          alert("failed");
							  me.editing = false;
					      },
					      headers: getAlfHeader()
				    	});	
						
				      },
				      failure: function(response, opts){
				          alert("failed");
						  me.editing = false;
				      },
				      headers: getAlfHeader()
				});
			
			});
		
		}
	},
	
	isEditMode : function() {
		return (this.data != null);
	},
	
	getHeaderValue : function(d) {
		var v = d.value;
		
		if (d.dtype!="B") {
			v = this.isEditMode() ? this.data[d.saveField] : d.value;
		} else {
			v = d.value.replace(/\\n/g,"\n");
		}
		
		return v;
	},

	getFieldValue : function(d) {
		var v = d.value;
	
		if (d.dtype!="B") {
			v = this.isEditMode() ? this.data.dtls[d.fname] : d.value;
		} else {
			v = d.value.replace(/\\n/g,"\n");
		}

		return v;
	},

	getStoreExtraParams:function(params, parent) {
		var me = this;
	
		var extraParams = {
			m:"PCM",
			formMode:(ID ? "E" : "C")
		};
		
		for(var pi=0; pi<params.length; pi++) {
			
			var v = params[pi];
			
			var pos = v.indexOf("${");
		
			while (pos >= 0) {
				var pos2 = v.indexOf("}", pos+2);
				if (pos2 >= 0) {
					var fname = v.substring(pos+2, pos2);
					
					var fnameSearch = fname;
					var prefix = fname.substring(0,2);
					if ((prefix != "H_") && (prefix != "F_")) {
						fnameSearch = "F_" + fname;
					}
					
					var f = parent.query("field[name="+fnameSearch.trim()+"]");
					var fv = "";
					if (!f) {
						alert("Field Not Found : "+fnameSearch.trim());
					}
					else {
						if (f instanceof Array) {
							for(var i=0; i < f.length; i++) {
								var fi = f[i];
								if (fi.inputType == "radio") {
									if (fi.checked) {
										fv = fi.inputValue;
										break;
									}
								}
								else {
									fv = fi.getValue();
								}
							}
						} else {
							fv = f.getValue();
						}
					}
					
					v = v.replace("${"+fname+"}",fv);
				}
				else {
					alert("Invalid Params : "+v);
				}
				
				pos = v.indexOf("${");
			}
		
			extraParams["p"+(pi+1)] = v;
		}
		
		return extraParams;
	},
	
	loadComboBoxStore:function(parent, prefix) {
		var me = this;
		var fields = parent.query("combo[name^="+prefix+"_]");
		
		for(var i=0; i<fields.length; i++) {
			var f = fields[i];
			
			var store = f.getStore();
			store.f = f;
			
			var srcParam = f.bgData.srcParam;
			
			if((srcParam.indexOf("${")>=0) && (srcParam.indexOf("}")>=0)) {
				var params = srcParam.split(",");
//				store.removeListener("load",me.onListBoxLoad);
//				store.addListener("load",me.onEditModeStoreLoad,me);
				store.getProxy().extraParams = me.getStoreExtraParams(params, parent);
				store.load();
			}
		}
		
		if (!me.isEditMode()) {
			fields = parent.query("textfield[name^="+prefix+"_]");
			
			for(var i=0; i<fields.length; i++) {
				var f = fields[i];
				var srcParam = f.bgData.srcParam;
				if(f.xtype=="textfield" && f.bgData.srcUrl && srcParam && srcParam.indexOf("${")<0) {
					var params = srcParam.split(",");
					if (params.length > 0) {
						var extraParams = me.getStoreExtraParams(params, parent);
						
						Ext.Ajax.request({
						      url:ALF_CONTEXT + "/srcUrl/" + f.bgData.srcUrl,
						      method: "GET",
						      params: extraParams,
						      success: function(response){
								var dataArray = Ext.decode(response.responseText).data;
								if (dataArray.length > 0) {
									f.setValue(dataArray[0].id);
						      	}
						      	else {
									f.setValue(null);
						      	}
	
						      },
						      failure: function(response, opts){
						          alert("failed");
						      },
						      headers: getAlfHeader(),
						      async:false
						});					
						
					}
				}
			}
		}
	},
//	
//	onEditModeStoreLoad:function(store, rs) {
//		var me = this;
//	
//		Ext.each(rs, function(r, i) {
//			if (r.data.id == store.f.bgData.v) {
//				store.f.setValue(r.data.id);
//				store.removeListener("load", me.onEditModeStoreLoad);
//				store.addListener("load", me.onListBoxLoad, me);
//				return false;
//			}
//		}, store);
//	},

	onListBoxLoad:function(store, rs) {
		var me = this;
		if (rs!=null) {
			var f = me.getMainForm().down("combo[name="+store.oname+"]");
			if (!me.isEditMode()) {
				if (!f.readOnly) {
					if (rs.length == 1) {
							f.setValue(store.first().data.id);
							f.fireEvent('select', f, store.first());
					} else 
					if (rs.length > 1 && store.first().data.id.indexOf(",")>=0) {
						if (!f.getValue()) {
							f.setValue(store.first().data.id);
							f.fireEvent('select', f, store.first());
						}
					} else
					if (rs.length > 1) {
						var dvalue = store.ov ? store.ov : store.f.bgData.v;
						
						store.each(function(rec, id){
							if (dvalue == rec.get('id')) {
								f.setValue(dvalue);
								f.fireEvent('select', f, rec);
							}
						});
						
					}
				}
			} else {
				if (store.ov) {
					store.each(function(rec, id){
						if (store.ov == rec.get('id')) {
							f.setValue(store.ov);
							f.fireEvent('select', f, rec);
						}
					});
				} else {
					if (store.f) {
						Ext.each(rs, function(r, i) {
//								console.log("  r.id:"+r.data.id+" , d.v:"+store.f.bgData.v);
							if (r.data.id == store.f.bgData.v) {
//								console.log("   matched");
//								console.log("   name:"+store.f.name);
								store.f.setValue(r.data.id);
							}
						}, store);
					}
				}
			}
		}
	},

	showDiagram : function(r){
		if (r.data.status.indexOf('-ปิดงาน-') >= 0) {
			Ext.Msg.alert("Information","ปิดงานไปแล้ว  ไม่สามารถดู workflow ได้");
		}else if (r.data.status.indexOf('-ยกเลิก-') >= 0) {
			Ext.Msg.alert("Information","ยกเลิกไปแล้ว  ไม่สามารถดู workflow ได้");
		} else {
			window.open(Alfresco.constants.PROXY_URI + "api/workflow-instances/" + r.get('workflow_ins_id') + '/diagram',"_new");
		}
	},
	
	gotoFolder : function(r) {
		var me = this;
		
		PB.Util.checkSession(this, me.MSG_URL+"/get", function() {

			var dlg = Ext.create("PB.view.common.FolderDtlDlg",{
				rec : r
			});
			dlg.show();
		
		});
	},

	_gotoFolder : function(r){
		Ext.Ajax.request({
	        url:ALF_CONTEXT+"/util/getFolderName",
	        async : false,
	        method: "POST",
	        params: {
	        	n : r.get('folder_ref'),
	        	lang : getLang()
	        },
	        success: function(response){
	        	
	        	var json = Ext.decode(response.responseText);  
	        	
	        	if(json.success){
	        		
	//            	window.open(MAIN_CONTEXT+"/page/repository#filter=path%7C%2FSites%2F"+CURRENT_SITE+"%2FdocumentLibrary%2F"+json.folderName+"%7C", "_new");
//	            	window.open(MAIN_CONTEXT+"/page/site/"+CURRENT_SITE+"/documentlibrary#filter=path%7C"+ json.data.name + "%7C", "_new");
                	window.open(MAIN_CONTEXT+"/page/repository#filter=path%7C"+json.data.name+"%7C", "_new");
	        		
	        	}
	        	
	        },
	        failure: function(response, opts){
	        	
	        	//console.log("fail");
	        },
	        headers: getAlfHeader()
	    });
	
	},
	
	viewDetail : function(r){
		var me = this;
		
		var id = "_new";
	  	var win = window.open("", id);
		
		PB.Util.checkSession(this, me.MSG_URL+"/get", function() {

	//	    window.open(MAIN_CONTEXT+"/page/document-details?nodeRef="+r.get("doc_ref"),"_new");
//		    window.open(Alfresco.constants.PROXY_URI_RELATIVE+"api/node/content/"+nodeRef2Url(r.get("doc_ref"))+"/"+r.get("file_name"),"_new");
		    win.location.href = Alfresco.constants.PROXY_URI_RELATIVE+"api/node/content/"+nodeRef2Url(r.get("doc_ref"))+"/"+r.get("file_name");
		});
	},
	
	viewHistory : function(r){
		var me = this;
		
		PB.Util.checkSession(this, me.MSG_URL+"/get", function() {
	
			var dlg = Ext.create("PBPcm.view.workflow.DtlDlg");
			var id = r.get("id");
	
			// Current Task
			Ext.Ajax.request({
			      url:ALF_CONTEXT+'/pcm/wf/task/list',
			      method: "GET",
			      params: {
			    	  id : id,
			    	  lang:getLang()
			      },
			      success: function(response) {
					  var data = Ext.decode(response.responseText).data[0];
					  var curTask;
					  if (data) {
						  curTask = data.type+(data.assignedTo ? " : " : "")+data.assignedTo;
					  } else {
						  curTask = "-";
					  }
					  dlg.items.items[0].items.items[0].items.items[0].setText('<font color="blue">'+curTask+'</font>',false);
					  
				  },
			      failure: function(response, opts){
			          alert("failed");
			      },
			      headers: getAlfHeader()
			});
			
			
			// Path
			var store = dlg.items.items[0].items.items[1].getStore(); 
			store.getProxy().extraParams = {
				id : id,
				lang : getLang()
			}
			store.load();
			
			// History
			store = dlg.items.items[1].getStore();
			store.getProxy().extraParams = {
			   	id : id,
			   	lang : getLang()
			};
			store.load();
			
			// Show
			dlg.show();
		
		});
	},

	del : function(r) {
		var me = this;
		
		PB.Util.checkSession(this, me.MSG_URL+"/get", function() {
		
			me.getMainGrid().getView().getSelectionModel().select(r);
			
			me.selectedRec = r;
			PB.Dlg.confirm('CONFIRM_'+me.MSG_KEY, me,'doDel', MODULE_PCM);
		
		});
	},
	
	doDel : function(){
		var me = this;
		
		Ext.Ajax.request({
		     url:me.URL+"/delete",
		     method: "POST",
		     params: {
		         id: me.selectedRec.get("id")
		     },
		     success: function(res){
		    	 
		    	 var json = Ext.decode(res.responseText);
		      	  
		    	 if(json.success){
		    	  
		    		PB.Dlg.alert('SUCC_'+me.MSG_KEY, MODULE_PCM);
		    		
		        	var store = me.getMainGrid().getStore();
		  	    	store.load();
		    		 
		    	 }else{
		    		 PB.Dlg.error('ERR_'+me.MSG_KEY, MODULE_PCM);
		    	 }
		    	 
		     },
		     failure: function(response, opts){
		    	 PB.Dlg.error('ERR_'+me.MSG_KEY, MODULE_PCM);
		     },
		     headers: getAlfHeader()
		});        	
	
	},

	copy : function(r) {
		var me = this;
		
		PB.Util.checkSession(this, me.MSG_URL+"/get", function() {

			me.getMainGrid().getView().getSelectionModel().select(r);
			
			me.selectedRec = r;
			PB.Dlg.confirm('CONFIRM_'+me.COPY_MSG_KEY,me,'doCopy', MODULE_PCM);
		
		});
	},
	
	doCopy : function(){
		var me = this;
		
		Ext.Ajax.request({
		     url:me.URL+"/copy",
		     method: "POST",
		     params: {
		         id: me.selectedRec.get("id")
		     },
		     success: function(res){
		    	 
		    	 var json = Ext.decode(res.responseText);
		      	  
		    	 if(json.success){
			   		var id = json.data;
			   		PB.Dlg.info('SUCC_'+me.COPY_MSG_KEY, MODULE_PCM, {msg:'ID:'+id, fn:me.closeForm, scope:me});
		    	 }else{
		    		PB.Dlg.error('ERR_'+me.COPY_MSG_KEY, MODULE_PCM);
		    	 }
		    	 
		     },
		     failure: function(response, opts){
		    	 PB.Dlg.error('ERR_'+me.COPY_MSG_KEY, MODULE_PCM);
		     },
		     headers: getAlfHeader()
		});        	
	
	},
	
	closeForm:function() {
		this.getMainGrid().getStore().load();
	},
	
	view:function(rec) {
		var me = this;
		
		if (!me.getMainForm()) {
		
			if (!me.viewing) {
				
				me.viewing = true;
			
				PB.Util.checkSession(this, me.MSG_URL+"/get", function() {
			
					Ext.Ajax.request({
					      url:me.URL+"/get",
					      method: "GET",
					      params: {
					    	  id : rec.get("id"),
					    	  lang:getLang()
					      },
					      success: function(response){
					    	  
					    	var json = Ext.decode(response.responseText);
					    	
					    	me.data = json.data[0]; // Form Edit Mode
					    	me.data.items = json.items;
					    	
					    	Ext.Ajax.request({
						      url:me.URL+"/userDtl",
						      method: "GET",
						      params:{
					    		 r:me.data.req_by,
					    	     c:me.data.created_by,
					    	     m:'v',
					    	     lang:getLang()
					    	  },
						      success: function(response){
						    	  
						    	var json = Ext.decode(response.responseText);
						    	
						    	var data = json.data[0];
						    	
						    	me.data.viewing = true;
	
						    	Ext.merge(me.data, data);
						    	
								me.createForm(PB.Label.m.view+' : <font color="red">'+rec.get("id")+"</font>", me.data);
								
								var form = me.getMainForm(); 
								form.down("button[action=send]").hide();
								form.down("button[action=saveDraft]").hide();
								form.down("button[action=cancel]").hide();
								form.down("button[action=preview]").hide();
								form.down("button[action=close]").show();
								
								me.activateForm();
								
								me.getHidId().setValue(rec.get("id"));
								me.getHidStatus().setValue(rec.get("status"));
								
								me.viewing = false;
						      },
						      failure: function(response, opts){
						          alert("failed");
								  me.viewing = false;
						      },
						      headers: getAlfHeader()
					    	});	
							
					      },
					      failure: function(response, opts){
					          alert("failed");
							  me.viewing = false;
					      },
					      headers: getAlfHeader()
					});
				
				});
			
			}	
		
		}
	}
	
});
