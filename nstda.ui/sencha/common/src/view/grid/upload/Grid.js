Ext.define('PB.view.common.upload.Grid', {
	extend : 'Ext.grid.Panel',
	alias : 'widget.uploadGrid',
	
	initComponent: function(config) {
		var me = this;
		
		Ext.applyIf(me, {
			
			   columns: [ 
			     {
			    	xtype: 'actioncolumn',
		        	dataIndex: 'action',
		        	itemId : 'actionGrid',
		        	text: '',
		            width: 70,
		            align: 'center',
		            items: [{
		                tooltip: 'Edit Description',
		        	    getClass: function(v) {
		        	    	return getActionIcon(v, "E", 'edit');
		                }, 
		                handler: function(grid, rowIndex, colIndex) {
		                    me.fireEvent('editFile',me, rowIndex, colIndex);
			            },
			            disabled:me.viewing
		            },{
		                tooltip: 'Delete',
		        	    getClass: function(v) {
		        	    	return getActionIcon(v, "D", 'delete');
		                }, 
		                handler: function(grid, rowIndex, colIndex) {
		                    me.fireEvent('deleteFile',me, rowIndex, colIndex);
			            },
			            disabled:me.viewing
		            }]
			     },
			     {text: PB.Label.u.number, xtype: 'rownumberer', width:60},
			     {text: PB.Label.u.name, dataIndex: 'name', flex: 0.5, renderer:me.fileNameRenderer},
			     {text: PB.Label.u.desc, dataIndex: 'desc', flex: 1}
			    ],
			    tbar:[{
		    	    xtype:'label',
		    	    html:"&nbsp;&nbsp;"+PB.Label.u.file+":"
//		        },{
//            		xtype: 'filefield',
//	            	padding: '0px 0px 0px 5px',
//	                name: 'file',
//	                msgTarget: 'side',
//	                width : 335,
//	                buttonConfig: {
//	                    text: PB.Label.u.browse,
//                        iconCls: "icon_search"               
//	                }
		        },{
            		xtype: 'multifilefield',
	            	padding: '0px 0px 0px 5px',
	                name: 'file',
	                msgTarget: 'side',
	                flex : 1,
	                buttonConfig: {
	                    text: PB.Label.u.browse,
                        iconCls: "icon_search"               
	                },
	                disabled:me.viewing
//            	},{
//            		xtype:'textfield',
//            		fieldLabel:PB.Label.u.desc,
//            		labelWidth:80,
//            		name:'desc',
//            		width:335,
//            		maxLength:100,
//            		margin:'0 0 0 20'
            	},{
            		xtype: 'button',
	                text: PB.Label.u.upload,
	                iconCls: "icon_upload",
	                listeners: {
	                    click: function(){
	                    	var file = me.down('[name=file]').getValue();			                    	
	                    	if(file != null && file != undefined && file != ''){
	                    		me.fireEvent('uploadFile',me.up('form').getForm(),me);	
	                    	}
	                    	
	                    }
	                },
	                disabled:me.viewing
            	},{
             	    xtype:'hiddenfield',
            	    name:'uuid'
               	}]
		
		});		
				
        this.callParent(arguments);
	},
	
	fileNameRenderer:function(v, m, r) {
		var me = this;
		var val = v;
		
		if(me.editMode) {
//			val = '<a href="'+MAIN_CONTEXT+"/page/document-details?nodeRef="+r.get("nodeRef")+'" target="_new">'+v+'</a>';
			var nodeRef = r.get("nodeRef").replace(":/","");
			if (nodeRef) {
				val = '<a href="'+MAIN_CONTEXT+"/proxy/alfresco/api/node/content/"+nodeRef+'/'+encodeURI(r.get("name")).replace("#","")+'" target="_new">'+v+'</a>';
			}
		}

		return val;
	}

});		