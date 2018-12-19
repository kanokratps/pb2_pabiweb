/*
 * DtlDlg
 * 	- AssigneeGrid
 * 	- DtlGrid
 */
Ext.define('PBExp.view.workflow.DtlDlg', {
	extend : 'Ext.window.Window',
	alias : 'widget.expWfDtlDlg',
	initComponent: function(config) {
		
		var me = this;
		var assigneeStore = Ext.create('PBExp.store.workflow.AssigneeGridStore');
		var store = Ext.create('PBExp.store.workflow.DtlGridStore');
		
		Ext.applyIf(me, {
	            modal: true,
	            width: 950,
	            height: 570,
	            layout: 'border',
	            resizable: true,
	            title:'Workflow Detail',
		        buttons:[{
			          text: 'Close',
			          handler:this.destroy,
			          scope:this
		        }],
	            items : [{
	            	region:'west',
	            	layout:'border',
	            	width : 300,
	            	items:[{
		            	title:'Current Task',
		            	region:'north',
		            	height:57,
		            	items:[{
		            		xtype:'label',
		            		html:'',
		            		style: 'display:inline-block;text-align:center',
		            		margin:'5 0 0 5'
		            	}]
		            },{
		            	title : 'Path',
		            	region:'center',
		            	split:true,
		    			xtype : 'expWfAssigneeGrid',
		    			actionType : 'related',
		            	store : assigneeStore
		            }]
	            },{
	            	title : 'History',
	            	region:'center',
        			xtype : 'expWfDtlGrid',
        			actionType : 'related',
	            	store : store
	            }]
		});
		
        this.callParent(arguments);
	}
});		