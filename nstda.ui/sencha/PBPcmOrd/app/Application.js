Ext.define('PBPcmOrd.Application', {
    name: 'PBPcmOrd',

    extend: 'Ext.app.Application',
    
    requires: [
        'Ext.util.Cookies',
        'Ext.grid.Panel',
        'Ext.grid.column.Number',
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
        'Ext.layout.container.Border',
        'Ext.form.field.Hidden',
        'Ext.form.field.Text',
        'Ext.form.field.TextArea',
        'Ext.form.field.Date',
        'Ext.tab.Panel',
        'Ext.overrides.grid.column.Action',
        'Ext.ux.form.NumericField',
        'Ext.ux.form.TimePickerField',
        'Ext.ux.DateTimePicker',
        'Ext.ux.form.DateTimeField',
        'Ext.ux.DateTimeMenu',
        
        'PB.Util',
        'PB.Dlg',
        'PB.Label',
        'PB.button.LinkButton',
        'PB.store.common.ComboBoxStore',
        'PB.model.common.ComboBoxModel',
        'PB.vtype.Validation',

        'PBPcmOrd.view.Main',
        
        'PBPcmOrd.controller.Main',
        'PBPcmOrd.controller.common.FolderDtl',

        'PBPcmOrd.Label'
	],

    views: [
        'Main'
    ],

    controllers: [
        'Main',
        'common.FolderDtl'
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
		      url:ALF_CONTEXT+"/pcm/userTask",
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
		      url:ALF_CONTEXT+"/pcm/message/ord",
		      method: "GET",
		      params:{
				 lang:getLang()
			  },
		      success: function(response){
		    	  
		    	var data = Ext.decode(response.responseText);
			 	Ext.apply(PBPcmOrd.Label, data);
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
		
	 	view = Ext.create('PBPcmOrd.view.Main',{
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
