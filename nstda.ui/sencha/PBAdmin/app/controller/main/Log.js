Ext.define('PBAdmin.controller.main.Log', {
    extend: 'Ext.app.Controller',

    refs:[{
        ref:'main',
        selector: 'adminMainLogMain'
	},{
	    ref:'textArea',
	    selector: 'adminMainLogMain textarea'
	},{
	    ref:'txtLine',
	    selector: 'adminMainLogMain [name=line]'
	},{
	    ref:'lblInfo',
	    selector: 'adminMainLogMain [name=info]'
	},{
	    ref:'txtFrom',
	    selector: 'adminMainLogMain [name=from]'
	},{
	    ref:'txtTo',
	    selector: 'adminMainLogMain [name=to]'
	}],
    
    init:function() {
		var me = this;
		
		me.control({
			'adminMainLogMain [action=range]': {
				click : me.range
			},
			'adminMainLogMain [action=last]': {
				click : me.last
   			},
			'adminMainLogMain [action=info]': {
				click : me.info
   			}
		});

	},
	
	MSG_KEY : 'LOG',
	URL : ALF_CONTEXT+'/admin/log',
	
	range:function(){
		var me = this;
	
		var myMask = new Ext.LoadMask({
			target:me.getMain(),
			msg:"Please wait..."
		});
		myMask.show();
		
		var params = {
			from:me.getTxtFrom().getValue(),
			to:me.getTxtTo().getValue()
		}
		
		Ext.Ajax.request({
		    url:me.URL+'/range',
		    method: "POST",
		    params:params,
		    success: function(response){
		  	  
		  	  var json = Ext.decode(response.responseText);
			  
		  	  if(json.success){
		   		 me.getTextArea().setValue(json.message);
		   		 me.getLblInfo().setText(json.info);
		
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
	},

	last:function(){
		var me = this;
	
		var myMask = new Ext.LoadMask({
			target:me.getMain(),
			msg:"Please wait..."
		});
		myMask.show();
		
		var params = {
			line:me.getTxtLine().getValue()
		}
		
		Ext.Ajax.request({
		    url:me.URL+'/last',
		    method: "POST",
		    params:params,
		    success: function(response){
		  	  
		  	  var json = Ext.decode(response.responseText);
			  
		  	  if(json.success){
		   		 me.getTextArea().setValue(json.message);
		   		 me.getLblInfo().setText(json.info);
		
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
	},
	
	info:function() {
		var me = this;
		
		var params = {
		}
		
		Ext.Ajax.request({
		    url:me.URL+'/info',
		    method: "POST",
		    params:params,
		    success: function(response){
		  	  
		  	  var json = Ext.decode(response.responseText);
			  
		  	  if(json.success){
		  		  me.getLblInfo().setText(json.message);
		
		  	  } else {
		  		  PB.Dlg.error('', MODULE_ADMIN, {msg:json.message});
		  	  }
		    },
		    failure: function(response, opts){
		  	  PB.Dlg.error('ERR_'+me.MSG_KEY, MODULE_ADMIN);
		    },
		    headers: getAlfHeader()
		});	

		
	}
	
});
