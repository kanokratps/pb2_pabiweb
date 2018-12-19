Ext.define('PB.view.common.FolderDtlDlg', {
	extend : 'Ext.window.Window',
    alias:'widget.folderDtlDlg',
    
	initComponent: function(config) {
		var me = this;
		
		var lbw = 140;
		
		var store = Ext.create('PB.store.common.FolderDtlStore');
		store.getProxy().extraParams = {
			n:me.rec.get("folder_ref"),
			lang:getLang()
		}
		store.load();
		
		Ext.applyIf(me, {
	        renderTo : Ext.getBody(),
	        title:PB.Label.m.doc,
	        modal: true,
	        width: 720,
	        height: 500,
	        layout: 'fit',
	        resizable: false,
	        items : [{
	        	xtype:'grid',
	        	margin:'5 0 0 0',
	        	columns:[
	        	     { 	xtype: 'actioncolumn',
		 	        	dataIndex: 'action',
		 	        	text: '', 
		 	            width: 50,
		 	            align:'center',
		 	            items: [{
		 	                tooltip: 'Detail', 
		 	                action : 'view',
		 	        	    getClass: function(v) {
		 	        	    	return getActionIcon(v, "V", 'view');
		                 	}
		 	        	}]
	        	     },
	        	     { text:PB.Label.f.name, dataIndex: 'name', width: 150 },
	        	     { text:PB.Label.f.desc, dataIndex: 'desc', flex:1 },
	        	     { text:PB.Label.f.by, dataIndex: 'by', width:150},
	        	     { text:PB.Label.f.time, dataIndex: 'time', width:150}
	        	],
	        	store:store
	        }],
	        buttons : [{
	          text: PB.Label.m.ok,
	          iconCls:'icon_ok',
	          handler:this.destroy,
	          scope:this
	        }]
		});
		
        this.callParent(arguments);
	}
});