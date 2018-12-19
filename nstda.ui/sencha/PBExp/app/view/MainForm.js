Ext.define('PBExp.view.MainForm', {
    extend: 'Ext.form.Panel',
    alias:'widget.expBrwMainForm',
    requires: [
        'PBExp.view.MainFormUserTab',
        'PBExp.view.MainFormInfoTab',
        'PBExp.view.MainFormItemTab',
        'PBExp.view.MainFormAttendeeTab',
        'PBExp.view.MainFormFileTab'
    ],
    
	layout:'fit',
    items:[{
		xtype : 'tabpanel',
		tabBar : {
			items : [{
				xtype : 'tbfill'
			},{
				xtype : 'hiddenfield',
				name : 'id'
			},{
				xtype : 'hiddenfield',
				name : 'hId'
			},{
				xtype : 'hiddenfield',
				name : 'status'
			},{
	            xtype: 'button',
	            text: "",
	            action: "approvalMatrix",
	            iconCls: "icon_branch",
	            hidden:true
			},{
	            xtype: 'button',
	            text: "Preview",
	            action: "preview",
	            iconCls: "icon_view",
	            margin:'0 2 0 0'
			},{
	            xtype: 'button',
	            text: "Save Draft",
	            action: "saveDraft",
	            iconCls: "icon_save",
	            margin:'0 2 0 0'
			},{
	            xtype: 'button',
	            text: "Send for Approval",
	            action: "send",
	            iconCls: "icon_send",
	            margin:'0 2 0 0'
			},{
				xtype : 'button',
				text : 'Close',
				action : 'cancel',
				iconCls : 'icon_no',
	            margin:'0 2 0 0'
			},{
	            xtype: 'button',
	            text: "Finish",
	            action: "finish",
	            iconCls: "icon_send",
	            hidden:true,
	            margin:'0 2 0 0'
			},{
				xtype : 'button',
				text : 'Close',
				action : 'cancelEdit',
				iconCls : 'icon_no',
	            hidden:true,
	            margin:'0 2 0 0'
			},{
				xtype : 'button',
				text : 'Close',
				action : 'close',
				iconCls : 'icon_no',
	            hidden:true,
	            margin:'0 2 0 0'
			}]
		}
    }]
    
});
