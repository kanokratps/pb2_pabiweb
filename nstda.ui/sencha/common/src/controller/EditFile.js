Ext.define('PB.controller.common.EditFile', {
    extend: 'Ext.app.Controller',

    refs:[{
    	ref:'dlg',
    	selector:'editFileDlg'
    },{
    	ref:'form',
    	selector:'editFileDlg form'
    }],
    
    init:function() {
		var me = this;
		
		me.control({
			'editFileDlg textfield': {
				confirmFile : me.ok
			},
			'editFileDlg button[action=ok]':{
				confirmFile : me.ok
			}
			
		});
	},
	
	getDesc:function(sender) {
		return sender.up("window").down("field[itemId=desc]");
	},
	
	ok:function(btn, rec, grid) {
		var me = this;
		
		var form = me.getForm();		

		if(validForm(form)) {
		
			rec.set('desc', me.getDesc(btn).getValue());
			rec.commit();
			
			me.getDlg().callback(grid,rec);
			me.getDlg().close();
		
		}
	},
	
	validForm:function() {
		
	}

});