Ext.define('PBAdmin.controller.main.Settings', {
    extend: 'Ext.app.Controller',

    refs:[{
        ref:'grid',
        selector: 'adminMainSettingsMain'
	},{
	    ref:'cmbType',
	    selector: 'adminMainSettingsMain combo[name=type]'
	},{
		ref:'btnAdd',
		selector: 'adminMainSettingsMain button[action=addSettings]'
	},{
		ref:'btnBack',
		selector: 'adminMainSettingsMain button[action=back]'
	},{
    	ref:'txtSearch',
		selector:'adminMainSettingsMain [itemId=txtSearchSettings]'
	}],
    
    init:function() {
		var me = this;
		
		Ext.Ajax.request({
		     url:ALF_CONTEXT+'/admin/main/master/showSystem',
		     method: "GET",
		     success: function(res){
		    	 
		    	 var json = Ext.decode(res.responseText);
		      	  
		    	 if(json.success){
		    		 
		    	    me.showSystem = json.data.showSystem;
		    	  
		    	 }else{
		    		 PB.Dlg.error('ERR_'+me.MSG_KEY, MODULE_ADMIN);
		    	 }
		    	 
		     },
		     failure: function(response, opts){
		    	 PB.Dlg.error('ERR_'+me.MSG_KEY, MODULE_ADMIN);
		     },
		     headers: getAlfHeader(),
		     async:false
		});        	
		
		me.control({
			'adminMainSettingsMain [action=searchSettings]': {
				click : me.search
			},
			'adminMainSettingsMain [action=addSettings]': {
				click : me.addSettings
			},
   			'adminMainSettingsMain':{
   				edit : me.editSettings,
   				del : me.deleteSettings,
   				selectType : me.selectType,
   				dblClick : me.loadChildren,
   				search : me.search
   			}
		});

	},
	
	MSG_KEY : 'DELETE_MASTER',
	URL : ALF_CONTEXT+'/admin/main/master',
	
	search:function() {
    	var me = this;
    	var store = me.getGrid().getStore();
    	
    	store.getProxy().extraParams = {
    		t : me.getCmbType().getValue(),
 	   		s : me.getTxtSearch().getValue()
 	   	};
		if (!me.showSystem) {
			store.getProxy().extraParams.system = false; 
		}
		store.currentPage = 1;
    	store.load();
	},

	selectType:function(combo, newValue, oldValue){
		var me = this;
		me.getBtnAdd().setDisabled(false);
		var store = me.getGrid().getStore();
		store.getProxy().extraParams = {
    		t : newValue,
 	   		s : me.getTxtSearch().getValue()
 	   	};
		
		if (!me.showSystem) {
			store.getProxy().extraParams.system = false; 
		}
		
		store.currentPage = 1;
		store.load();
		me.getBtnBack().setVisible(false);
		
		me.parentCode = combo.getValue();
		me.parentName = combo.getRawValue();
	},
	
	loadChildren:function(record){
		if (!this.getBtnBack().isVisible()) {
	    	this.getBtnAdd().setDisabled(false); 
	    	this.getGrid().getStore().load({params:{t:record.data.code}});
	    	
	    	this.getBtnBack().setText(record.data.name);
	    	this.getBtnBack().setVisible(true);
	    	
	    	this.parentCode = record.data.code;
	    	this.parentName = record.data.name;
		}
	},
	
	deleteSettings : function(r) {
		this.getGrid().getView().getSelectionModel().select(r);
		this.selectedRec = r;
		PB.Dlg.confirm('CONFIRM_'+this.MSG_KEY,this,'doDeleteSettings', MODULE_ADMIN);
	},
	
	doDeleteSettings : function(){
		var me = this;
		
		Ext.Ajax.request({
		     url:me.URL+'/delete',
		     method: "POST",
		     params: {
		         id: me.selectedRec.get("id")
		     },
		     success: function(res){
		    	 
		    	 var json = Ext.decode(res.responseText);
		      	  
		    	 if(json.success){
		    	  
		    		PB.Dlg.alert('SUCC_'+me.MSG_KEY, MODULE_ADMIN);
		    		
		        	var store = me.getGrid().getStore();
		        	
		  	    	store.getProxy().extraParams = {
		  	    		t : me.parentCode,
	  	    			s : me.getTxtSearch().getValue()  	    			  
		  	 	   	};
		  	    	store.load();
		    		 
		    	 }else{
		    		 PB.Dlg.error('ERR_'+me.MSG_KEY, MODULE_ADMIN);
		    	 }
		    	 
		     },
		     failure: function(response, opts){
		    	 PB.Dlg.error('ERR_'+me.MSG_KEY, MODULE_ADMIN);
		     },
		     headers: getAlfHeader()
		});        	
	 
	},
	
	addSettings : function() {
		this.createDlg('เพิ่มค่า').show();
	},
	
	editSettings : function(rec) {
		var me = this;
	
		me.getGrid().getView().getSelectionModel().select(rec);
	
		var dialog = me.createDlg('แก้ไขค่า');
		
		Ext.Ajax.request({
		      url:me.URL+'/get',
		      method: "GET",
		      params: {
		    	  id : rec.get("id")
		      },
		      success: function(response){
		    	  
		    	var json = Ext.decode(response.responseText);
		
		    	var form = dialog.down('form');
		
		    	form.down('field[name=id]').setValue(rec.get("id"));
		    	form.down('field[name=code]').setValue(json.data[0].code);
		    	form.down('field[name=name]').setValue(json.data[0].name);
		    	form.down('field[name=flag1]').setValue(json.data[0].flag1=="null" ? "" : json.data[0].flag1);
		    	form.down('field[name=flag2]').setValue(json.data[0].flag2=="null" ? "" : json.data[0].flag2);
		    	form.down('field[name=flag3]').setValue(json.data[0].flag3=="null" ? "" : json.data[0].flag3);
		    	form.down('field[name=flag4]').setValue(json.data[0].flag4=="null" ? "" : json.data[0].flag4);
		    	form.down('field[name=flag5]').setValue(json.data[0].flag5=="null" ? "" : json.data[0].flag5);
		    	form.down('field[name=active]').setValue(json.data[0].active=="null" ? "" : json.data[0].active);
		    	form.down('field[name=system]').setValue(json.data[0].system=="null" ? "" : json.data[0].system);
		        
		      },
		      failure: function(response, opts){
		          alert("failed");
		      },
		      headers: getAlfHeader(),
		      async:false
		}); 
		
		dialog.show();
	},
	
	createDlg:function(title) {
	
		var me = this;
		
		var dialog = Ext.create('PBAdmin.view.main.settings.Dlg', {
		    title : title
		});
		
		var form = dialog.down('form');

		form.add({
			xtype: 'hidden',
			name: 'id'
		 },{
			xtype: 'hidden',
			name: 'type',
			value: me.parentCode
		 },{
		    xtype: 'textfield',
		    fieldLabel : 'ประเภท', 
		    labelWidth: 70,
		    anchor:"-10",
		    hideTrigger:true,
		    name : 'typeDesc',
		    msgTarget: 'side',
		    margin: '10 0 0 10',
		    readOnly : true,
		    value : me.parentName
		},{
		     xtype: 'textfield',
		     fieldLabel : 'รหัส', 
		     labelWidth: 70,
		     anchor:"-10",
		     hideTrigger:true,
		     name : 'code',
		     msgTarget: 'side',
		     margin: '10 0 0 10',
		     allowBlank : false
		 },{
		     xtype: 'textfield',
		     fieldLabel : 'ชื่อ', 
		     labelWidth: 70,
		     anchor:"-10",
		     hideTrigger:true,
		     name : 'name',
		     msgTarget: 'side',
		     margin: '10 0 0 10',
		     allowBlank : false
		 },{
		     xtype     : 'textfield',
		     fieldLabel : 'ค่า (1)', 
		     labelWidth: 70,
		     anchor:"-10",
		     hideTrigger:true,
		     name : 'flag1',
		     msgTarget: 'side',
		     margin: '10 0 0 10',
		     allowBlank : true
		 },{
		     xtype     : 'textfield',
		     fieldLabel : 'ค่า (2)', 
		     labelWidth: 70,
		     anchor:"-10",
		     hideTrigger:true,
		     name : 'flag2',
		     msgTarget: 'side',
		     margin: '10 0 0 10',
		     allowBlank : true
		 },{
		     xtype     : 'textfield',
		     fieldLabel : 'ค่า (3)', 
		     labelWidth: 70,
		     anchor:"-10",
		     hideTrigger:true,
		     name : 'flag3',
		     msgTarget: 'side',
		     margin: '10 0 0 10',
		     allowBlank : true
		 },{
		     xtype     : 'textfield',
		     fieldLabel : 'ค่า (4)', 
		     labelWidth: 70,
		     anchor:"-10",
		     hideTrigger:true,
		     name : 'flag4',
		     msgTarget: 'side',
		     margin: '10 0 0 10',
		     allowBlank : true
		 },{
		     xtype     : 'textfield',
		     fieldLabel : 'ค่า (5)', 
		     labelWidth: 70,
		     anchor:"-10",
		     hideTrigger:true,
		     name : 'flag5',
		     msgTarget: 'side',
		     margin: '10 0 0 10',
		     allowBlank : true
		 },{
		     xtype     : 'checkbox',
		     grow      : true,
		     fieldLabel : 'Active', 
		     labelWidth: 70,
		     width : 360,
		     name : 'active',
		     msgTarget: 'side',
		     margin: '10 0 0 10'
		 },{
		     xtype     : 'checkbox',
		     grow      : true,
		     fieldLabel : 'System', 
		     labelWidth: 70,
		     width : 360,
		     name : 'system',
		     msgTarget: 'side',
		     margin: '10 0 0 10',
		     hidden:!me.showSystem
		 });
		
		return dialog;
	}

});
