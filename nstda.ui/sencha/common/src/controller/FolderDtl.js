Ext.define('PB.controller.common.FolderDtl', {
    extend: 'Ext.app.Controller',

    refs:[{
    	ref:'dlg',
    	selector:'folderDtlDlg'
    }],
    
    init:function() {
		var me = this;
		
		me.control({
			'folderDtlDlg grid':{
				view:me.view
			}
		});
	},
	
	getGrid:function(sender) {
		return sender.up("window").down("grid");
	},
	
	view:function(r) {
		var grid = this.getDlg().down("grid");
		console.log(grid);
		window.open(Alfresco.constants.PROXY_URI_RELATIVE+"api/node/content/"+nodeRef2Url(r.get("nodeRef"))+"/"+r.get("name"),"_new");
	}

});