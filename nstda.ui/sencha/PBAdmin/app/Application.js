Ext.define('PBAdmin.Application', {
    name: 'PBAdmin',

    extend: 'Ext.app.Application',

    requires: [
        'Ext.util.Cookies',
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
        'Ext.layout.container.HBox',
        'Ext.form.field.Hidden',
        'Ext.form.field.Text',
        'Ext.form.field.Trigger',
        'Ext.tab.Panel',
        'Ext.grid.column.CheckColumn',
        'Ext.form.field.Checkbox',
        'Ext.form.field.Date',
        'Ext.overrides.grid.column.Action',
        'Ext.ux.form.MultiFile',
        
        'PB.Util',
        'PB.Dlg',
        'PB.Label',
        'PB.button.LinkButton',
        'PB.model.common.UserModel',
        'PB.store.common.UserStore',
        'PB.view.common.SearchUserDlg',
        
        'PBAdmin.view.Main',
        'PBAdmin.view.main.workflow.Main',
        'PBAdmin.view.main.settings.Main',
        'PBAdmin.view.main.testSystem.Main',
        'PBAdmin.view.main.util.Main',
        'PBAdmin.view.main.log.Main',
        
        'PBAdmin.controller.Main',
        'PBAdmin.controller.common.User',
        'PBAdmin.controller.main.Settings',
        'PBAdmin.controller.main.SettingsForm',
        'PBAdmin.controller.main.TestSystem',
        'PBAdmin.controller.main.Util',
        'PBAdmin.controller.main.Workflow',
        'PBAdmin.controller.main.Log',

        'PBAdmin.store.main.SettingsGridStore',
        'PBAdmin.store.main.AuthMasterTypeCmbStore',
        'PBAdmin.store.main.ModuleCmbStore'
	],

    views: [
        'Main'
    ],

    controllers: [
        'common.User',
        'Main',
        'main.Settings',
        'main.SettingsForm',
        'main.Group',
        'main.TestSystem',
        'main.Util',
        'main.Workflow',
        'main.Log'
    ],

    stores: [
        'main.SettingsGridStore',
        'main.AuthMasterTypeCmbStore'
    ],
    
	launch: function () {
		Ext.apply(Ext.QuickTips.getQuickTip(), {
		    dismissDelay: 0
		});
	
		if (typeof TASKS !== 'undefined') {
			HEIGHT = Ext.get(HTML_ID).getHeight();
			WIDTH = Ext.get(HTML_ID).getWidth();
			
		 	view = Ext.create('PBAdmin.view.Main',{
			  renderTo: Ext.Element.get(MAIN_ID),
		 	  height:(HEIGHT-H_OFFSET)+'px',
		 	  width:(WIDTH-W_OFFSET)+'px',
		 	  tasks:TASKS
		 	});
		 	
		 	Ext.Ajax.request({
			      url:ALF_CONTEXT+"/util/getPageSize",
			      method: "GET",
			      success: function(response){
			    	  
			    	var data = Ext.decode(response.responseText).data;
			    	
			    	ADMIN_PAGE_SIZE = data.pageSize;
			    	
			      },
			      failure: function(response, opts){
			          // do nothing
			      },
			      headers: getAlfHeader(),
			      async:false
			});	 	
		 	
		 	Ext.Ajax.request({
			      url:ALF_CONTEXT+"/admin/module/list",
			      method: "GET",
			      success: function(response){
			    	  
			    	var system = Ext.decode(response.responseText);
			    	
				 	modules = system.modules;
					
					for(var i=0; i<modules.length; i++) {
						view.addMenu(modules[i].name, modules[i].iconCls, i==0);
					}
					view.setMenuItems(modules[0].items);
	
			      },
			      failure: function(response, opts){
			          alert("failed");
			      },
			      headers: getAlfHeader()
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
		 	
		}

	}
//test
});
