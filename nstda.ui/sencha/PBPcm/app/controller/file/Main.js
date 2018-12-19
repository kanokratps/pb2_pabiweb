Ext.define('PBPcm.controller.file.Main', {
    extend: 'Ext.app.Controller',
    
	refs:[{
	    ref: 'main',
	    selector:'pcmReqMain'
	},{
	    ref: 'cmbMp',
	    selector:'pcmReqFileTab uploadGrid field[name=mp]'
	}],
	
	init:function() {
		var me = this;
		
		me.control({
			'grid [action=download]': {
				click : me.download
			},
			'combo': {
				selectMediumPrice : me.selectMediumPrice
			}
		});
	
	},

	download:function() {
		var me = this;

		var nodeRef = me.rec.data.FLAG1;
		var pos = nodeRef.lastIndexOf("/");
		var id = nodeRef.substring(pos+1);
		window.open(Alfresco.constants.PROXY_URI_RELATIVE+"api/node/content/workspace/SpacesStore/"+id+"/a.odt?a=true");
	},
	
	selectMediumPrice:function(cmb, rec) {
		this.rec = rec[0].data;
	}

});
