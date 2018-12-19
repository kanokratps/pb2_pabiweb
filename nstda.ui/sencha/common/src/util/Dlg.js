Ext.define('PB.Dlg', {
	statics: {
		confirm:function(key,f,fn,module,n_fn,sc){
			var me = this;
		
			Ext.Ajax.request({
		        url:ALF_CONTEXT+"/"+module+"/message/get",
		        method: "GET",
		        params: {
		            key: key,
		            lang:getLang()
		        },
		        success: function(response){
		        	
		        	var json = Ext.decode(response.responseText);
		        	
		        	if (json.success) {
		        	
			        	Ext.MessageBox.buttonText = {ok: 'OK', yes: 'Yes', no: 'No', cancel: 'Cancel'};
			        	
			        	Ext.MessageBox.confirm("Confirmation("+json.data[0].code+")", json.data[0].message , function(btn){
			        	
			        		if(btn=="yes"){
			        			f[fn](sc);
			        		}else{
			        			
			        			if(n_fn!=null && n_fn!=undefined){
			        				f[n_fn](sc);
			        			}
			        			
			        		}
			        	});
		        	
		        	} else {
		        		alertInvalidSession();
		        	}
		        	
		        },
		        failure: function(response, opts){
		        	alertInvalidSession();
		        },
		        headers: getAlfHeader(),
		        async:false
		    });
			
		},
		
		doConfirm:function(key,f,fn,module,n_fn) {

		},
		
		/*
		 * opts :
		 * 	- msg : extra message
		 *  - icon : Ext.MessageBox.INFO, WARNING, ERROR, QUESTION
		 *  - modal : true/false
		 *  - fn : callback function
		 */
		show:function(key,module,opts) {
			 
			var me = this;
		
	
			if (!opts.icon) {
				icon = Ext.MessageBox.INFO;
			}
		
			Ext.Ajax.request({
		        url:ALF_CONTEXT+"/"+module+"/message/get",
		        method: "GET",
		        params: {
		            key: key,
		            lang:getLang()
		        },
		        success: function(response){
		        	
		        	var json = Ext.decode(response.responseText);
		        	
		        	if (json.success) {
			        	var msg = json.data[0].message.trim();
			        	if (opts.msg) {
			        		if (msg && msg!="") {
			        			msg += "<br/><br/>" ;
			        		}
			        		
			        		msg += opts.msg+"<br/>&nbsp;";
			        		
			        	}
			        	
		        		if (opts.val) {
		        			msg = Ext.String.format(msg, opts.val[0],opts.val[1],opts.val[2]);
		        		}
			        	
			        	if (opts.buttonText) {
			        		Ext.MessageBox.buttonText = opts.buttonText;
			        	} else {
			        		Ext.MessageBox.buttonText = {ok: 'OK', yes: 'Yes', no: 'No', cancel: 'Cancel'};
			        	}
			        	
			        	var title = opts.iconName ? opts.iconName + " (" + json.data[0].code + ")" : json.data[0].code;
			        	
			        	var config = {
			        		title:title, 
			        		msg:msg,
			        		icon: opts.icon,
			        		buttons:(opts.buttons ? opts.buttons : Ext.MessageBox.OK),
			        		modal:opts.modal,
			        		fn:opts.fn,
			        		scope:opts.scope,
			        		animateTarget:opts.animateTarget
			        	}			        	
			        	
			        	if (opts.scope && msg.indexOf("<br/>")) {
			        		config.width = Ext.util.TextMetrics.measure(
			        						  opts.scope.getMain().getEl().dom,
			        						msg).width+140;
			        	}
//			        	console.log("width:"+(config.width-120));

			        	Ext.MessageBox.show(config);
		        	} else {
		        		alertInvalidSession();
		        	}
		        },
		        failure: function(response, opts){
		        	alertInvalidSession();
				},
		        headers: getAlfHeader(),
		        async:false
		    });

		},
		
		info:function(key,module, opts) {
			if (!key) {
				key = "INFO";
			}
			
			if (!opts) {
				opts = {};
			}
			
			opts.icon = Ext.MessageBox.INFO;
			opts.iconName = "Information";
			this.show(key,module,opts);
		},

		warn:function(key,module, opts) {
			if (!key) {
				key = "WARNING";
			}
		
			if (!opts) {
				opts = {};
			}
			
			opts.icon = Ext.MessageBox.WARNING;
			opts.iconName = "Warning";
			this.show(key,module,opts);
		},

		error:function(key,module, opts) {
			if (!key) {
				key = "ERROR";
			}
			
			if (!opts) {
				opts = {};
			}
			
			opts.icon = Ext.MessageBox.ERROR;
			opts.iconName = "Error";
			this.show(key,module,opts);
		},

		alert:function(key,module){
			
			Ext.Ajax.request({
		        url:ALF_CONTEXT+"/"+module+"/message/get",
		        method: "GET",
		        params: {
		            key: key
		        },
		        success: function(response){
		        	
		        	var json = Ext.decode(response.responseText);
		        	
	        		Ext.MessageBox.buttonText = {ok: 'OK', yes: 'Yes', no: 'No', cancel: 'Cancel'};
		        	Ext.MessageBox.alert(json.data[0].code, json.data[0].message);
		        	
		        },
		        failure: function(response, opts){
		            //console.log("failed");
		        },
		        headers: getAlfHeader(),
		        async:false
		    });        	
		
			
		},
		
		prompt:function (key,f,fn,module){
		
			Ext.Ajax.request({
		        url:ALF_CONTEXT+"/"+module+"/message/get",
		        method: "GET",
		        params: {
		            key: key
		        },
		        success: function(response){
		        	
		        	var json = Ext.decode(response.responseText);
		        	
		        	var dlg = Ext.create('PB.view.common.prompt.PromptDlg',{
		        		title : json.data[0].code
		        	});
		        	
		        	dlg.down('[itemId=msg]').setText(json.data[0].message);
		        	dlg.show();
		        	
		        	var btn = dlg.down('[itemId=btnPrompt]');
		        	var txt = dlg.down('[itemId=promptTxt]');
		        	btn.handler = function(){
		        		
		        		if(txt.isValid()){
		        			
		        			f[fn](txt.getValue());
		        			dlg.destroy();
		        			
		        		}else{
		        			
		        			return false;
		        			
		        		}
		        		
		        	}
		        
		        },
		        failure: function(response, opts){
		            //console.log("failed");
		        },
		        headers: getAlfHeader()
		    });   
			
		}
		
	}
});