Ext.define('PBAdmin.controller.main.Util', {
    extend: 'Ext.app.Controller',

    refs:[{
        ref:'main',
        selector: 'adminMainUtilMain'
	},{
	    ref:'textArea',
	    selector: 'adminMainUtilMain textarea'
	}],
    
    init:function() {
		var me = this;
		
		me.control({
			'adminMainUtilMain [action=addUserToGroup]': {
				click : me.addUserToGroup
   			}
		});

	},
	
	MSG_KEY : 'UTIL',
	URL : ALF_CONTEXT+'/admin/util',
	
	addUserToGroup:function(){
		var me = this;
	
		var myMask = new Ext.LoadMask({
			target:me.getMain(),
			msg:"Please wait..."
		});
		myMask.show();
		
		var p = me.getTextArea().getValue().split("\n");
		var params = {};
		for(var a in p) {
			if (p[a].indexOf("u=")==0) {
				var q = p[a].split("=");
				params.u = q[1];
			}
			else
			if (p[a].indexOf("g=")==0) {
				var q = p[a].split("=");
				params.g = q[1];
			}
		}
		
		Ext.Ajax.request({
		    url:me.URL+'/u2g',
		    method: "POST",
		    params:params,
		    success: function(response){
		  	  
		  	  var json = Ext.decode(response.responseText);
			  
		  	  if(json.success){
		  		 var msg = "OK";
		   		 
		   		 me.getTextArea().setValue(me.getTextArea().getValue()+"\n\nResult:"+msg);
		
		  	  } else {
		  		  PB.Dlg.error('', MODULE_ADMIN, {msg:json.message});
		  	  }
		  	  
		  	  myMask.hide();	
		    },
		    failure: function(response, opts){
		  	  PB.Dlg.error('ERR_'+me.MSG_KEY, MODULE_ADMIN);
		  	  myMask.hide();
		    },
		    headers: getAlfHeader()
		});	
	}
	
});
