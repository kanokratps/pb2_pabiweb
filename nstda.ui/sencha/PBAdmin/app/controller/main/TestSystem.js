Ext.define('PBAdmin.controller.main.TestSystem', {
    extend: 'Ext.app.Controller',

    refs:[{
        ref:'main',
        selector: 'adminMainTestSystemMain'
	},{
	    ref:'textArea',
	    selector: 'adminMainTestSystemMain textarea'
	}],
    
    init:function() {
		var me = this;
		
		me.control({
			'adminMainTestSystemMain [action=currentUser]': {
				click : me.currentUser
			},
			'adminMainTestSystemMain [action=version]': {
				click : me.version
   			}
		});

	},
	
	MSG_KEY : 'TEST_SYSTEM',
	URL : ALF_CONTEXT+'/admin/main/testSystem',
	
	testUserSignature:function(){
		var me = this;
	
		var myMask = new Ext.LoadMask({
			target:me.getMain(),
			msg:"Please wait..."
		});
		myMask.show();
		
		Ext.Ajax.request({
		    url:me.URL+'/userSignature',
		    method: "GET",
		    success: function(response){
		  	  
		  	  var json = Ext.decode(response.responseText);
			  
		  	  if(json.success){
		  		 var msg = "";
		   		 if (json.data.valid) {
		   			 msg = "OK";
		   		 }
		   		 else {
		   			 msg = "This process checks only the users in Approval Matrix"
		   				 + "\nSignature Folder : "+decodeURIComponent(json.data.folder)
		   				 + "\nNo-image users : "+json.data.users
		   				 + "\nInvalid user groups : "+json.data.groups;
		   		 }
		   		 
		   		 me.getTextArea().setValue(msg);
		
		  	  } else {
		  		  PB.Dlg.error('ERR_'+me.MSG_KEY, MODULE_ADMIN);
		  	  }
		  	  
		  	  myMask.hide();	
		    },
		    failure: function(response, opts){
		  	  PB.Dlg.error('ERR_'+me.MSG_KEY, MODULE_ADMIN);
		  	  myMask.hide();
		    },
		    headers: getAlfHeader()
		});	
	},
	
	currentUser:function(){
		var me = this;
	
		var myMask = new Ext.LoadMask({
			target:me.getMain(),
			msg:"Please wait..."
		});
		myMask.show();
		
		Ext.Ajax.request({
		    url:me.URL+'/currentUser',
		    method: "GET",
		    success: function(response){
		  	  
		  	  var json = Ext.decode(response.responseText);
			  
		  	  if(json.success){
		  		 var msg = "Count : " + json.data.cnt
		   				 + "\nUsers : "+json.data.users;
		   		 
		   		 me.getTextArea().setValue(msg);
		
		  	  } else {
		  		  PB.Dlg.error('ERR_'+me.MSG_KEY, MODULE_ADMIN);
		  	  }
		  	  
		  	  myMask.hide();	
		    },
		    failure: function(response, opts){
		  	  PB.Dlg.error('ERR_'+me.MSG_KEY, MODULE_ADMIN);
		  	  myMask.hide();
		    },
		    headers: getAlfHeader()
		});	
	},	

	version:function(){
		var me = this;
	
		var myMask = new Ext.LoadMask({
			target:me.getMain(),
			msg:"Please wait..."
		});
		myMask.show();
		
		Ext.Ajax.request({
		    url:me.URL+'/version',
		    method: "GET",
		    success: function(response){
		  	  
		  	  var json = Ext.decode(response.responseText);
			  
		  	  if(json.success){
		  		 var msg = json.data.msg;
		   		 me.getTextArea().setValue(msg);
		  	  } else {
		  		 PB.Dlg.error('ERR_'+me.MSG_KEY, MODULE_ADMIN);
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
