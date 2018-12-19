function renderConsultant(Id,v,n,width,vv){
	
	 if (!PB.Label.m) {
		 Ext.Ajax.request({
		      url:ALF_CONTEXT+"/admin/message/lbl",
		      method: "GET",
		      params:{
	 			 lang:getLang()
	 		  },
		      success: function(response){
		    	  
		    	var data = Ext.decode(response.responseText);
			 	Ext.apply(PB.Label, data);
			 	//alert(PBPcm.Label.a);
		      },
		      failure: function(response, opts){
		          // do nothing
		      },
		      headers: getAlfHeader(),
		      async:false
		});	
	}
	 
	
	var w = parseInt(width);
	
	var field = document.getElementsByName(n)[0];
	
	var ww;
	var setPanel = Ext.query('div[class=set-panel-heading]');
	for(var a in setPanel) {
		var positionInfo = setPanel[a].getBoundingClientRect();
		ww = positionInfo.width;
		break;
	}
	
	var panel = Ext.create('Ext.container.Container',{
		layout:'hbox',
		width:ww-5,
		renderTo: Ext.Element.get(Id),
		items:[{
			xtype:'trigger',
			name:Id+"-tgr",
			itemId:Id+"-tgr",
			hideLabel:true,
			width:w+40,
			margin:"5 0 0 0",
			trigger1Cls: 'x-form-clear-trigger',
		    trigger2Cls: 'x-form-search-trigger',
		    hideTrigger1: true,
		    hasSearch : false,
			editable:false,
			onTrigger1Click:function(evt) {
				this.triggerEl.item(0).dom.parentNode.style.width = "0px";
				setValue(panel, Id+'-tgr', null);
				setValue(panel, Id+'-desc', '');
			},
			onTrigger2Click:function(evt) {
				Ext.create("PB.view.common.SearchUserDlg",{
					title:PB.Label.m.search,
					targetPanel:panel,
					callback:this.selectCallBack
				}).show();
				
			},
			selectCallBack:function(id, rec) {
				setValue(this.targetPanel, Id+'-tgr', rec.get('code'));
				setValue(this.targetPanel, Id+'-desc', 
					rec.get('title') 
					+ ' ' + rec.get('fname')
					+ ' ' + rec.get('lname')
					+ ' ' + rec.get('position')
//					+ ' ' + rec.get('org_desc')
//					+ ' ' + rec.get('section_desc')
				);
				field.value = rec.get('code');
			},
			listeners:{
				boxready:function() {
					var cw = this.getValue() ? "17" : "0";
					this.triggerEl.item(0).dom.parentNode.style.width = cw+"px";
					
					var tw = this.getValue() ? w : w+17;
					this.inputEl.dom.style.width = tw+"px";
					
					return true;
				},			
				change:function(trigger, newV, oldV) {
//					console.log("change:"+this.inputEl.dom.style.width);
					var cw = newV ? "17" : "0";
					this.triggerEl.item(0).dom.parentNode.style.width = cw+"px";
					
					var tw = this.getValue() ? w : w+17;
					this.inputEl.dom.style.width = tw+"px";
				}
			},
			value:replaceIfNull(v, null)
		},{
			xtype:'textfield',
			name : Id+"-desc",
			itemId : Id+"-desc",
			value : vv,
			hideLabel:true,
			flex:1,
			margin:'5 0 0 10',
			readOnly:true,
			fieldStyle:'color:red;border:0px solid;box-shadow:none'
		}]
	});

}