Ext.define('PBPcm.view.workflow.AssigneeGrid', {
	extend : 'Ext.grid.Panel',
	alias : 'widget.pcmWfAssigneeGrid',
	anchor : '100%',
	initComponent: function(config) {
		
		var me = this;

		Ext.applyIf(me, {
			
			   columns: [ 
			      { text: 'Task', dataIndex: 'assignee', width:120},
			      { text: 'User', dataIndex: 'user', flex:1}
			   ]
		});
				
        this.callParent(arguments);
	}

});		