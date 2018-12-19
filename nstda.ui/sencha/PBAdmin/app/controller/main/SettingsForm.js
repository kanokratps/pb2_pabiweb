Ext.define('PBAdmin.controller.main.SettingsForm', {
    extend: 'Ext.app.Controller',
   
    refs: [{
        ref: 'grid',
        selector: 'adminMainSettingsMain'
    },{
        ref: 'dlg',
        selector: 'adminMainSettingsDlg'
    },{
    	ref: 'formSettings',
        selector: 'adminMainSettingsDlg #formSettings'
    },{
        ref: 'hidId',
        selector: '#formSettings> field[name=id]'
    },{
        ref: 'hidType',
        selector: '#formSettings> field[name=type]'
    },{
        ref: 'txtCode',
        selector: '#formSettings> field[name=code]'
    },{
        ref: 'txtName',
        selector: '#formSettings> field[name=name]'
    },{
        ref: 'txtFlag1',
        selector: '#formSettings> field[name=flag1]'
    },{
        ref: 'txtFlag2',
        selector: '#formSettings> field[name=flag2]'
    },{
        ref: 'txtFlag3',
        selector: '#formSettings> field[name=flag3]'
    },{
        ref: 'txtFlag4',
        selector: '#formSettings> field[name=flag4]'
    },{
        ref: 'txtFlag5',
        selector: '#formSettings> field[name=flag5]'
    },{
        ref: 'chkActive',     
        selector: '#formSettings> field[name=active]'
    },{
        ref: 'chkSystem',     
        selector: '#formSettings> field[name=system]'
 	},{
    	ref:'txtSearch',
		selector:'adminMainSettingsMain [itemId=txtSearchSettings]'
   }],
 
    init : function() {
	
    	var me = this;
    	
    	me.control({     
          'adminMainSettingsDlg button[action=ok]': {
        	  click : me.ok
          }
        });
    },
    
    MSG_KEY : 'SAVE_MASTER',
    URL : ALF_CONTEXT+'/admin/main/master',
    
    ok: function(btn) {
    	
    	var form = this.getFormSettings();
    	
    	if(validForm(form)){
	    	PB.Dlg.confirm('CONFIRM_'+this.MSG_KEY,this,'doOk', MODULE_ADMIN);
    	}
    	
    },
    
    doOk: function(){
    	
    	var me = this;
    	
    	var dlg = me.getDlg();
    	
    	var myMask = new Ext.LoadMask({
    		target:dlg,
    		msg:"Please wait..."
    	});
    	myMask.show();
    
    	Ext.Ajax.request({
  	      url:me.URL+'/save',
  	      method: "POST",
  	      params: {
  	    	  id:me.getHidId().getValue(),
  	    	  type:me.getHidType().getValue(),
  	    	  code:me.getTxtCode().getValue(),
  	    	  name:me.getTxtName().getValue(),
  	    	  flag1:me.getTxtFlag1().getValue(),
  	    	  flag2:me.getTxtFlag2().getValue(),
	    	  flag3:me.getTxtFlag3().getValue(),
	    	  flag4:me.getTxtFlag4().getValue(),
	    	  flag5:me.getTxtFlag5().getValue(),
  	    	  active:me.getChkActive().getValue(),
  	    	  system:me.getChkSystem().getValue()
  	      },
  	      success: function(response){
  	    	  
  	    	  var json = Ext.decode(response.responseText);
       	  
  	    	  if(json.success){
           		 
  	    		  PB.Dlg.alert('SUCC_'+me.MSG_KEY, MODULE_ADMIN);
           		 
  	    		  var store = me.getGrid().getStore();
            	
  	    		  store.getProxy().extraParams = {
  	    			  t : me.getHidType().getValue(),
  	    			  s : me.getTxtSearch().getValue()  	    			  
  	    		  };
  	    		  store.load();
  	    	  } else {
  	    		  PB.Dlg.error('ERR_'+me.MSG_KEY, MODULE_ADMIN);
  	    	  }
  	    	  
  	    	  dlg.destroy();
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