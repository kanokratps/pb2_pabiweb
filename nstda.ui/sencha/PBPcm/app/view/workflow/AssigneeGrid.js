Ext.define('PBPcm.view.workflow.AssigneeGrid', {
	extend : 'Ext.grid.Panel',
	alias : 'widget.pcmWfAssigneeGrid',
	anchor : '100%',
	initComponent: function(config) {
		
		var me = this;

		Ext.applyIf(me, {
			
			   columns: [ 
			      { text: 'Task', dataIndex: 'assignee', width:120, renderer:me.taskRenderer},
			      { text: 'User', dataIndex: 'user', flex:1, renderer:me.userRenderer, tdCls:'wrap'}
			   ]
		});
				
        this.callParent(arguments);
	},
	
	taskRenderer:function(v, m, r) {
		var color = r.get("color");
		if (color == "0") {
			v = '<font color="black">' + v + '</font>';
		} else {
			v = '<font color="blue">' + v + '</font>';
		}
		
		return v; 
	},
	
	userRenderer:function(v, m, r) {
		var pos = v.indexOf("(");
		if (pos >= 0) {
			v = v.substring(0, pos) + '<br/><font color="red">' + v.substring(pos)+'</font>';
		}
		
		var color = r.get("color");
		if (color == "0") {
			v = '<font color="black">' + v + '</font>';
		} else {
			v = '<font color="blue">' + v + '</font>';
		}
		
		return v; 
	}

});		