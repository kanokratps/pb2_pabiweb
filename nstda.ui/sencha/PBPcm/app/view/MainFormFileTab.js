Ext.define('PBPcm.view.MainFormFileTab', {
    extend: 'Ext.container.Container',
    alias:'widget.pcmReqFileTab',

	initComponent: function(config) {
		var me = this;
		
		var fileStore = Ext.create('PB.store.common.FileStore');
		
		if (me.rec.id) {
			fileStore.getProxy().api.read = ALF_CONTEXT + "/pcm/file/list";
			fileStore.getProxy().extraParams = {
			    id:me.rec.id
			}
			fileStore.load();
		}
	
		Ext.applyIf(me, {
			layout:'border',
			items:[{
				region:'center',
				xtype:'form',
				height:(HEIGHT-100) / 2,
	            border:0,
	            layout:'fit',
	            items:[{
	            	xtype:'uploadGrid',
	            	border:0,
	            	uploadUrl:ALF_CONTEXT+"/pcm/file/upload",
	            	deleteUrl:ALF_CONTEXT+"/pcm/file/delete",
	            	editUrl:ALF_CONTEXT+"/pcm/file/edit",
	            	store:fileStore,
	            	editMode:me.editMode,
	            	viewing:me.rec.viewing
	            }]
			}]
		});
		
	    this.callParent(arguments);
	}
    
});
