Ext.define('PBExpUse.Application', {
    name: 'PBExpUse',

    extend: 'Ext.app.Application',

    requires: [
        'Ext.util.Cookies',
        'Ext.grid.Panel',
        'Ext.grid.column.Number',
        'Ext.grid.column.CheckColumn',
        'Ext.MessageBox',
        'Ext.tip.QuickTipManager',
        'Ext.picker.Color',
        'Ext.Toolbar.Item',
        'Ext.toolbar.Toolbar',
        'Ext.Toolbar.Separator',
        'Ext.Toolbar.Spacer',
        'Ext.util.Format',
        'Ext.layout.container.Card',
        'Ext.layout.container.Form',
        'Ext.form.field.Hidden',
        'Ext.form.field.Text',
        'Ext.form.field.TextArea',
        'Ext.form.field.Date',
        'Ext.tab.Panel',
        'Ext.overrides.grid.column.Action',
        'Ext.ux.form.MultiFile',
        'Ext.ux.form.NumericField',
        'Ext.ux.form.TimePickerField',
        'Ext.ux.DateTimePicker',
        'Ext.ux.form.DateTimeField',
        'Ext.ux.DateTimeMenu',
        'Ext.EventManager',
        
        'PB.Util',
        'PB.Dlg',
        'PB.Label',
        'PB.button.LinkButton',
        'PB.store.common.UserStore',
        'PB.model.common.UserModel',
        'PB.store.common.CostControlStore',
        'PB.model.common.CostControlModel',
        'PB.store.common.ComboBoxStore',
        'PB.model.common.ComboBoxModel',
        'PB.vtype.Validation',

        'PBExpUse.view.Main',
        
        'PBExpUse.store.OldComboStore',
        'PBExpUse.model.OldComboModel',
        'PBExpUse.store.OldGridStore',
        'PBExpUse.model.OldGridModel',

        'PBExpUse.controller.Main',
        'PBExpUse.controller.Form',
        'PBExpUse.controller.common.User',
        'PBExpUse.controller.common.BudgetSrc',
        'PBExpUse.controller.common.EmployeeUser',
        'PBExpUse.controller.common.OtherUser',
        'PBExpUse.controller.common.CostControl',
        'PBExpUse.controller.common.Upload',
        'PBExpUse.controller.common.EditFile',
        'PBExpUse.controller.common.FolderDtl',
        'PBExpUse.controller.attendee.Form',
        'PBExpUse.controller.item.Form',
        'PBExpUse.controller.file.Main',
        
        'PBExpUse.Label'
	],
	
    views: [
        'Main'
    ],

    controllers: [
        'Main',
        'Form',
        'common.User',
        'common.BudgetSrc',
        'common.EmployeeUser',
        'common.OtherUser',
        'common.CostControl',
        'common.Upload',
        'common.EditFile',
        'common.FolderDtl',
        'attendee.Form',
        'item.Form',
        'file.Main'    
    ],

    stores: [
        // TODO: add stores here
    ],
    
	launch: function () {
		Ext.apply(Ext.QuickTips.getQuickTip(), {
		    dismissDelay: 0
		});

	 	Ext.Ajax.request({
		      url:ALF_CONTEXT+"/util/getPageSize",
		      method: "GET",
		      success: function(response){
		    	  
		    	var data = Ext.decode(response.responseText).data;
		    	
		    	PAGE_SIZE = data.pageSize;
		    	
		      },
		      failure: function(response, opts){
		          // do nothing
		      },
		      headers: getAlfHeader(),
		      async:false
		});
	 	
	 	Ext.Ajax.request({
		      url:ALF_CONTEXT+"/exp/userTask",
		      method: "GET",
		      success: function(response){
		    	  
		    	var data = Ext.decode(response.responseText).data;
		    	
		    	TASKS = data.tasks;
		    	
		      },
		      failure: function(response, opts){
		          // do nothing
		      },
		      headers: getAlfHeader(),
		      async:false
		});
	 	
	 	Ext.Ajax.request({
		      url:ALF_CONTEXT+"/exp/message/use",
		      method: "GET",
		      params:{
				 lang:getLang()
			  },
		      success: function(response){
		    	  
		    	var data = Ext.decode(response.responseText);
			 	Ext.apply(PBExpUse.Label, data);
			 	//alert(PBExpUse.Label.a);
		      },
		      failure: function(response, opts){
		          // do nothing
		      },
		      headers: getAlfHeader(),
		      async:false
		});
	 	
	 	Ext.Ajax.request({
		      url:ALF_CONTEXT+"/admin/message/lbl",
		      method: "GET",
		      params:{
	 			 lang:getLang()
	 		  },
		      success: function(response){
		    	  
		    	var data = Ext.decode(response.responseText);
			 	Ext.apply(PB.Label, data);
			 	//alert(PBPcm.Label.a);
		      },
		      failure: function(response, opts){
		          // do nothing
		      },
		      headers: getAlfHeader(),
		      async:false
		});	 	
	
		HEIGHT = Ext.get(HTML_ID).getHeight();
		WIDTH = Ext.get(HTML_ID).getWidth();
		
	 	view = Ext.create('PBExpUse.view.Main',{
		  renderTo: Ext.Element.get(MAIN_ID),
	 	  height:(HEIGHT-H_OFFSET)+'px',
	 	  width:(WIDTH-W_OFFSET)+'px',
	 	  tasks:TASKS
	 	});
	 	
	 	Ext.EventManager.onWindowResize(function() {
			HEIGHT = Ext.get(HTML_ID).getHeight();
			WIDTH = Ext.get(HTML_ID).getWidth();
	 		view.setHeight(HEIGHT-H_OFFSET);
	 		view.setWidth(WIDTH-W_OFFSET);
	 		view.doLayout();
	 	});
	
	}
});
