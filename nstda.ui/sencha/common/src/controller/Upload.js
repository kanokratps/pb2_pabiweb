Ext.define('PB.controller.common.Upload', {
    extend: 'Ext.app.Controller',
    alias:'controller.commonUpload',
    
    refs:[{
    	ref:'grid',
    	selector:'uploadGrid'
    }],

    init:function() {
		var me = this;
		
		me.control({
   			'uploadGrid': {
				uploadFile :  me.uploadFile,
				deleteFile :  me.deleteFile,
				editFile :  me.editFile
			}
		});

	},
	
	uploadFile : function(form,grid){
		var me = this;
		   
		if(form.isValid()){
		    form.submit({
		    	method : 'POST',
		        url: grid.uploadUrl+'?'+ALF_TOKEN+'='+encodeURIComponent(Ext.util.Cookies.get(ALF_TOKEN)),
		        waitMsg: 'Uploading file...',
		        success: function(fp, o) {
		        	
		    		if (o.result.success) {
		    			var data = o.result.data;
		    			for(var i=0; i<data.length; i++) {
				        	var fileModel = new Ext.create('PB.model.common.FileModel',{
				        		name : data[i].name,
				        		path : data[i].path,
				        		desc : data[i].desc,
				        		action   : 'ED'
				        	});
				        	grid.getStore().add(fileModel);
			        	}
			        	
		    			Ext.MessageBox.buttonText = {ok: 'OK', yes: 'Yes', no: 'No', cancel: 'Cancel'};
			        	Ext.Msg.alert('Success', 'Success');
			        	
//			        	me.getGrid().down("[name=desc]").setValue(null);
		        	}
		    		
		        	me.getGrid().down("[name=file]").fileInputEl.set({
		                multiple:true
		            });
		        }
		    });
		}
	},
	
	deleteFile : function(grid, rowIndex, colIndex){
	
		var rec = grid.getStore().getAt(rowIndex);
	
		Ext.Ajax.request({
		    url : grid.deleteUrl,
		    method: "POST",
		    params: {
		    	name : rec.get('name'),
		    	path: rec.get('path')
		    },
		    success: function(response){
		    	
		    	var json = Ext.decode(response.responseText);  
		    	
		    	if(json.success){
		    		
		    		grid.getStore().remove(rec);
		    		
		    	}
		    	
		    },
		    failure: function(response, opts){
		    	
		    	
		    },
		    headers: getAlfHeader()
		});
	
	},
	
	editFileCallback:function(grid,rec) {
		console.log(rec.get('name')+","+rec.get('desc'));
		
		Ext.Ajax.request({
		    url : grid.editUrl,
		    method: "POST",
		    params: {
		    	name : rec.get('name'),
		    	path: rec.get('path'),
		    	nodeRef: rec.get('nodeRef'),
		    	desc: rec.get('desc')
		    },
		    success: function(response){
		    	
		    	var json = Ext.decode(response.responseText);  
		    	
		//		var r = grid.getStore().getById(rec.get('name'));
		//		r.set("desc", rec.desc);
		//		r.commit();
				grid.getView().refresh();
		    },
		    failure: function(response, opts){
		    	
		    	
		    },
		    headers: getAlfHeader()
		});		
		
	},
	
	editFile : function(grid, rowIndex, colIndex){
		
		var rec = grid.getStore().getAt(rowIndex);
		
		dlg = Ext.create('PB.view.common.EditFileDlg',{rec:rec, callback:this.editFileCallback, grid:grid});
		dlg.show();
	}


});
