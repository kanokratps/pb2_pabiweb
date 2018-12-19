Ext.define('PB.Util', {
	statics: {
	    getSrcUrl:function(srcUrl) {
			if (srcUrl == "mainMaster") {
				return "memo/master";
			}
			else
			if (srcUrl == "mainMasterField") {
				return "memo/masterField";
			}
			else
			if (srcUrl == "format") {
				return "memo/format";
			}
			else
			if (srcUrl == "approvalMatrix") {
				return "main/approvalMatrix";
			}
			else
			if (srcUrl == "userProfile") {
				return "main/userProfile";
			}
			
			return srcUrl;
		},
		
		checkSession:function(scope, url, fn) {
			Ext.Ajax.request({
		        url:url,
		        method: "GET",
		        params: {
		            key: "INFO",
		            lang:getLang()
		        },
		        success: function(response){
		        	
		        	var json = Ext.decode(response.responseText);
		        	
		        	if (json.success) {
		        		fn(null);
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
	    }
	}
});
