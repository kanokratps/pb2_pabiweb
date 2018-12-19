Ext.define('PBPcm.view.committee.cond2Tab', {
    extend: 'Ext.panel.Panel',
    alias:'widget.pcmReqCmtCond2Tab',

    layout:'anchor',
    autoScroll:true,

	initComponent: function(config) {
		var me = this;
		
		var rec = me.rec ? me.rec : {};
		
		Ext.applyIf(me, {
			items:[{
				xtype:'hidden',
				name:'methodCond2Rule',
		    	value:replaceIfNull(rec.method_cond2_rule, me.preCond2) 
			},{
				xtype:'combo',
				name:'methodCond2',
				fieldLabel:me.preCond2,
		    	displayField:'name',
		    	valueField:'id',
		        emptyText : PB.Label.m.select,
		        store: me.store,
		        queryMode: 'local',
		        typeAhead:true,
		        multiSelect:false,
		        forceSelection:true,
				labelWidth:160,
				anchor:"-10",
				margin:'5 0 0 10',
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
		    	value:replaceIfNull(rec.method_cond2, null),
		    	readOnly:rec.viewing,
		    	fieldStyle:rec.viewing ? READ_ONLY : ""
		    },{
		    	xtype:'textarea',
		    	name:'methodCond2Dtl',
		    	fieldLabel:'รายละเอียดเพิ่มเติม (ให้ต่อเนื่องกับเงื่อนไขที่เลือก)',
		    	labelWidth:160,
		    	anchor:"-10 -35",
		    	margin:'5 0 0 10',
		    	value:replaceIfNull(rec.method_cond2_dtl, null),
		    	maxLength:255,
		    	readOnly:rec.viewing,
		    	fieldStyle:rec.viewing ? READ_ONLY : ""
		    }]
		});		
		
	    this.callParent(arguments);
	}
    
});
