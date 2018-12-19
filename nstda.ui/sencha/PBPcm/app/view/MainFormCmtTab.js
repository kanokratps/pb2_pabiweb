/*
 * 	- CmtTab
 * 		- committee.DtlDlg
 * 		- committee.Cond2Tab
 */
Ext.define('PBPcm.view.MainFormCmtTab', {
    extend: 'Ext.panel.Panel',
    alias:'widget.pcmReqCmtTab',

    layout:'fit',
    autoScroll:true,
    
	initComponent: function(config) {
		var me = this;

		var methodStore = Ext.create('PB.store.common.ComboBoxStore',{autoLoad:false});
		methodStore.getProxy().api.read = ALF_CONTEXT+'/pcm/req/cmt/list';
		methodStore.getProxy().extraParams = {
			objType : me.rec.objective_type ? me.rec.objective_type : 0
		}

//		if (!replaceIfNull(me.rec.is_across_budget, "0") == "1") { // not across budget
			if (replaceIfNull(me.rec.total, 0)) {
				methodStore.getProxy().extraParams.total = replaceIfNull(me.rec.total, 0); 
			}
//		} else {
//			if (replaceIfNull(me.rec.across_budget, null)) {
//				methodStore.getProxy().extraParams.total = replaceIfNull(me.rec.across_budget, null);
//			}
//		}
		
		methodStore.load(function() {
			if (me.rec.prweb_method_id) {
				var rec = methodStore.getById(parseInt(me.rec.prweb_method_id));
				me.fireEvent("selectCmb", me, [rec], me.rec);
			} else {
				var id = 0;
				methodStore.each(function(r){
					id = r.get('id');
				});
				var rec = methodStore.getById(parseInt(id));
				me.fireEvent("selectCmb", me, [rec], me.rec);
			}
		});
		
		var lbw = parseInt(PBPcm.Label.c.lbw);
		
		Ext.applyIf(me, {
			tbar : [{
				xtype:'combo',
				name:'method',
				fieldLabel:mandatoryLabel(PBPcm.Label.c.pcmMethod),
		    	displayField:'name',
		    	valueField:'id',
		        emptyText : PB.Label.m.select,
		        store: methodStore,
		        queryMode: 'local',
		        typeAhead:true,
		        multiSelect:false,
		        forceSelection:true,
				labelWidth:lbw,
				width:800,
				margin:'0 0 0 10',
				allowBlank:false,
		        listConfig : {
			    	resizable:true,
			    	minWidth:800,
			    	width:800,
				    getInnerTpl: function () {
						return '<div>{name}</div>';
				        //return '<div>{name}<tpl if="id != \'\'"> ({id})</tpl></div>';
				    }
				},
		        listeners:{
					beforequery : function(qe) {
						qe.query = new RegExp(qe.query, 'i');
		//				qe.forceAll = true;
					}
				},
				value:replaceIfNull((me.rec.prweb_method_id ? parseInt(me.rec.prweb_method_id) : null), null),
				readOnly:me.rec.viewing,
				fieldStyle:me.rec.viewing ? READ_ONLY : "",
				disabled:me.disabled
		    }],
			items:[{
				xtype:'tabpanel',
				itemId:'cmtTab'
			}]			
		});		
		
	    me.callParent(arguments);
	}
    
});
