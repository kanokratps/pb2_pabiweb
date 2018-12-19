Ext.define('PBExpUse.view.workflow.DtlGrid', {
	extend : 'Ext.grid.Panel',
	alias : 'widget.expWfDtlGrid',
	anchor : '100%',
	initComponent: function(config) {
		
		var me = this;

		Ext.applyIf(me, {
			
			   columns: [ 
			    { text: 'Date-Time',width: 120, dataIndex: 'time'},
			    { text: 'By',width: 110, dataIndex: 'by'},
			    { text: 'Task',width: 100, dataIndex: 'task'},
			    { text: 'Status',width: 80, dataIndex: 'status'},
			    { text: 'Comment', dataIndex: 'comment', tdCls: 'wrap',flex: 1}
			   ]
		});		
				
        this.callParent(arguments);
	}

});		