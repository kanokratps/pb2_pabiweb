Ext.define('PBPcm.view.MainFormUserTab', {
    extend: 'Ext.form.Panel',
    alias:'widget.pcmReqUserTab',
    
    autoScroll:true,

	initComponent: function(config) {
		var me = this;
	
		var lbw = parseInt(PBPcm.Label.u.lbw);

		Ext.applyIf(me, {
			items:[{
				xtype:'container',
				layout:'hbox',
				anchor:"-10",
				items:[{
					xtype:'trigger',
					name:'reqBy',
					fieldLabel:PBPcm.Label.u.reqBy,
					labelWidth:lbw,
					margin:"5 0 0 10",
					width:250,
					triggerCls:'x-form-search-trigger',
					onTriggerClick:function(evt) {
						me.fireEvent("selectReqBy");
					},
					listeners: {
						specialkey: function(field, e){
		                    // e.HOME, e.END, e.PAGE_UP, e.PAGE_DOWN,
		                    // e.TAB, e.ESC, arrow keys: e.LEFT, e.RIGHT, e.UP, e.DOWN
		                    if (e.getKey() == e.ESC) {
		                    	this.spckey = 0;
		                    	me.fireEvent("hideAM");
		                    }
		                    else
		                    if (e.getKey() == e.LEFT && (this.spckey==0 || !this.spckey)) {
		                    	this.spckey=1;
		                    }
		                    else
		                    if (e.getKey() == e.UP && this.spckey==1) {
		                    	this.spckey=2;
		                    }
		                    else
		                    if (e.getKey() == e.RIGHT && this.spckey==2) {
		                    	this.spckey=3;
		                    }
		                    else
		                    if (e.getKey() == e.DOWN && this.spckey==3) {
		                    	this.spckey=4;
		                    }
		                    else
		                    if (e.getKey() == e.TAB && this.spckey==4) {
		                    	this.spckey=99;
		                    } else {
		                    	this.spckey=0;
		                    }
		                    
		                    if (this.spckey==99) {
		                    	me.fireEvent("showAM");
		                    	this.spckey = 0;
		                    }
						}
	                },
					value:replaceIfNull(me.rec.req_by, null),
					editable:false,
					readOnly:me.rec.viewing,
					fieldStyle:me.rec.viewing ? READ_ONLY : ""
				},{
					xtype:'textfield',
					name:'reqByName',
					hideLabel:true,
					margin:"5 0 0 10",
					flex:1,
					value:replaceIfNull(me.rec.req_by_name, null),
					readOnly:true,
					fieldStyle:READ_ONLY
				},{
					xtype:'textfield',
					name:'reqTelNo',
					hideLabel:true,
//					fieldLabel:'โทรศัพท์',
//					labelWidth:lbw,
					margin:"5 0 0 10",
					width:300,
					value:replaceIfNull(me.rec.req_tel_no, null),
					readOnly:true,
					fieldStyle:READ_ONLY
				},{
					xtype:'textfield',
					name:'reqByDept',
					hideLabel:true,
					margin:"5 0 0 10",
					flex:1,
					value:replaceIfNull(me.rec.req_by_dept, null),
					readOnly:true,
					fieldStyle:READ_ONLY
				}]
			},{
				xtype:'container',
				layout:'hbox',
				anchor:"-10",
				items:[{
					xtype:'textfield',
					name:'reqBu',
					fieldLabel:PBPcm.Label.u.reqBu,
					labelWidth:lbw,
					margin:"5 0 0 10",
					flex:1,
					value:replaceIfNull(me.rec.req_bu, null),
					readOnly:true,
					fieldStyle:READ_ONLY
				}]
			},{
				xtype:'container',
				layout:'hbox',
				anchor:"-10",
				items:[{
					xtype:'textfield',
					name:'reqOuName',
					fieldLabel:PBPcm.Label.u.reqOu,
					labelWidth:lbw,
					margin:"5 0 0 10",
					flex:1,
					value:replaceIfNull(me.rec.req_ou_name, null),
					readOnly:true,
					fieldStyle:READ_ONLY
				},{
					xtype:'hidden',
					name:'reqOu',
					value:replaceIfNull(me.rec.req_ou, null)
				}]
			},{
				xtype:'datefield',
				name:'createdTime',
				fieldLabel:PBPcm.Label.u.createdTime,
				labelWidth:lbw,
				margin:"5 0 0 10",
				width:250,
				format:'d/m/Y',
				value:me.rec.created_time ? new Date(me.rec.created_time) : new Date(),
				readOnly:true,
				fieldStyle:READ_ONLY
			},{
				xtype:'hidden',
				name:'createdBy',
				value:replaceIfNull(me.rec.created_by, null)
			},{
				xtype:'textfield',
				name:'createdByShow',
				fieldLabel:PBPcm.Label.u.createdBy,
				labelWidth:lbw,
				margin:"5 0 0 10",
				width:600,
				value:replaceIfNull(me.rec.created_by_show, null),
				readOnly:true,
				fieldStyle:READ_ONLY
			},{
				xtype:'textfield',
				name:'telNo',
				fieldLabel:PBPcm.Label.u.telNo,
				labelWidth:lbw,
				margin:"5 0 0 10",
				width:600,
				value:replaceIfNull(me.rec.tel_no, null),
				readOnly:true,
				fieldStyle:READ_ONLY
			}]
		});		
		
	    this.callParent(arguments);

	}
    
});
