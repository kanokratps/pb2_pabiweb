Ext.define('PBPcm.view.committee.cond2Tab', {
    extend: 'Ext.panel.Panel',
    alias:'widget.pcmReqCmtCond2Tab',

    layout:'anchor',
    autoScroll:true,

	initComponent: function(config) {
		var me = this;
		
		Ext.applyIf(me, {
			items:[{
				xtype:'hidden',
				name:'methodCond2Rule',
				value:me.preCond2
			},{
				xtype:'combo',
				name:'methodCond2',
				fieldLabel:me.preCond2,
		    	displayField:'name',
		    	valueField:'id',
		        emptyText : "โปรดเลือก",
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
				}			    	
		    },{
		    	xtype:'textarea',
		    	name:'methodCond2Dtl',
		    	fieldLabel:'รายละเอียดเพิ่มเติม (ให้ต่อเนื่องกับเงื่อนไขที่เลือก)',
		    	labelWidth:160,
		    	anchor:"-10 -35",
		    	margin:'5 0 0 10'
		    }]
		});		
		
	    this.callParent(arguments);
	}
    
});