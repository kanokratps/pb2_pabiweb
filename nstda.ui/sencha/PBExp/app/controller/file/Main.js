Ext.define('PBExp.controller.file.Main', {
    extend: 'Ext.app.Controller',
    
	refs:[{
	    ref: 'main',
	    selector:'expBrwMain'
	},{
	    ref: 'cmbMp',
	    selector:'expBrwFileTab uploadGrid field[name=mp]'
	}],
	
	init:function() {
		var me = this;
		
		me.control({
			'grid [action=download]': {
				click : me.download
			}
		});
	
	},

	MSG_KEY : 'DELETE_REQ_DETAIL',
	URL : ALF_CONTEXT+'/exp/brw/dtl',
	ADMIN_URL : ALF_CONTEXT+'/admin/exp/dtl',
	MSG_URL : ALF_CONTEXT+'/exp/message',
	
	download:function() {
		var me = this;

		var nodeRef = me.rec.data.FLAG1;
		var pos = nodeRef.lastIndexOf("/");
		var id = nodeRef.substring(pos+1);
		window.open(Alfresco.constants.PROXY_URI_RELATIVE+"api/node/content/workspace/SpacesStore/"+id+"/a.odt?a=true");
	}
	
});
