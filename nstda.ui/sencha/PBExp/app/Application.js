Ext.define('PBExp.Application', {
    name: 'PBExp',

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

        'PBExp.view.Main',
        
        'PBExp.store.OldComboStore',
        'PBExp.model.OldComboModel',

        'PBExp.controller.Main',
        'PBExp.controller.Form',
        'PBExp.controller.common.User',
        'PBExp.controller.common.BudgetSrc',
        'PBExp.controller.common.EmployeeUser',
        'PBExp.controller.common.OtherUser',
        'PBExp.controller.common.CostControl',
        'PBExp.controller.common.Upload',
        'PBExp.controller.common.EditFile',
        'PBExp.controller.common.FolderDtl',
        'PBExp.controller.item.Form',
        'PBExp.controller.attendee.Form',
        'PBExp.controller.file.Main',
        
        'PBExp.Label'
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
        'item.Form',
        'attendee.Form',
        'file.Main'    
    ],

    stores: [
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
		      url:ALF_CONTEXT+"/exp/message/brw",
		      method: "GET",
		      params:{
				 lang:getLang()
			  },
		      success: function(response){
		    	  
		    	var data = Ext.decode(response.responseText);
			 	Ext.apply(PBExp.Label, data);
			 	//alert(PBExp.Label.a);
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
		
	 	view = Ext.create('PBExp.view.Main',{
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
