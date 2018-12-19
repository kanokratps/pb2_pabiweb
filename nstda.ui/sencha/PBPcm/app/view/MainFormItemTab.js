Ext.define('PBPcm.view.MainFormItemTab', {
    extend: 'Ext.panel.Panel',
    alias:'widget.pcmReqItemTab',

    layout:'fit',
    autoScroll:true,

	initComponent: function(config) {
		var me = this;
		
		var columns = []
		columns.push(
	        	{
	        		xtype: 'actioncolumn',
		        	dataIndex: 'action',
		        	text: '', 
		            width: 80,
		            align:'center',
		            items: [{
		                tooltip: 'Copy', 
		                action : 'copy',
		        	    getClass: function(v) {
	        	    		return getActionIcon(v, "C", 'copy');
		        	    },
		        	    disabled:me.rec.viewing
	            	}, {
		                tooltip: 'Edit',
		                action : 'edit',
		        	    getClass: function(v) {
		        	    	return getActionIcon(v, "E", 'edit');
		        	    },
		        	    disabled:me.rec.viewing
		            }, {
		                tooltip: 'Delete', 
		                action : 'del',
		        	    getClass: function(v) {
		        	    	return getActionIcon(v, "D", 'delete');
		        	    },
		        	    disabled:me.rec.viewing
		            }]
	        	}
	    );
		
		if (replaceIfNull(me.rec.is_across_budget, "0") == "1") {
			columns.push(
				{ text: PBPcm.Label.t.fiscalYear,  dataIndex: 'fiscalYear', width:80}
			);
		}
		
		columns.push(
				{ text: PBPcm.Label.t.actGrp,  dataIndex: 'actGrpName', flex:0.75},
				{ text: PBPcm.Label.t.act,  dataIndex: 'actName', flex:0.75},
				{ text: PBPcm.Label.t.name,  dataIndex: 'description', flex:1},
				{ text: PBPcm.Label.t.asset,  dataIndex: 'assetName', flex:0.75},
				{ text: PBPcm.Label.t.qty,  dataIndex: 'quantity', width:80, align:'right', xtype: 'numbercolumn', format:'0,000'},
				{ text: PBPcm.Label.t.uom,  dataIndex: 'unit', width:110, align:'center'},
				{ text: PBPcm.Label.t.prc,  dataIndex: 'price', width:100, align:'right', xtype: 'numbercolumn', format:'0,000.00'},
				{ text: PBPcm.Label.t.subtotal,  dataIndex: 'total', width:180, align:'right', xtype: 'numbercolumn', format:'0,000.00'}
		);
		
		var vatStore = Ext.create('PB.store.common.ComboBoxStore');
		vatStore.getProxy().api.read = ALF_CONTEXT+'/admin/main/tax/list';
		vatStore.getProxy().extraParams = {
		}
		vatStore.load();
		
		var store = Ext.create('PBPcm.store.ItemGridStore');
		
		var lbw = parseInt(PBPcm.Label.t.lbw);
		var fw = 205;

		Ext.applyIf(me, {
			items:[{
				xtype:'container',
				layout:'border',
				border:0,
				items:[{
					xtype:'grid',
					region:'center',
					columns : columns,
					store: store,
				    tbar:[{
//				    	xtype:'tbfill'
//				    },{
		        		xtype: 'button',
		                text: PB.Label.m.btnAdd,
		                iconCls: "icon_add",
		                action:'addItem',
		        	    disabled:me.rec.viewing
		        	}]

				},{
					xtype:'container',
					itemId:'totalPanel',
					region:'south',
					layout:'border',
					height:replaceIfNull(me.rec.currency, "THB") == "THB" ? 88 : 115,
					style:'background-color:white',
					items:[{
						xtype:'container',
						region:'center',
						border:0
					},{
						xtype:'container',
						region:'east',
						layout:'vbox',
						border:0,
						items:[{
							xtype:'container',
							layout:'hbox',
							border:0,
							margin:'5 5 0 5',
							items:[{
								xtype:'label',
								text:PBPcm.Label.t.gross,
								width:lbw+100
							},{
								xtype:'label',
								name:'grossAmt',
								style:'text-align:right;',
								width:fw
							}]
						},{
							xtype:'container',
							layout:'hbox',
							border:0,
							margin:'4 5 0 5',
							items:[{
								xtype:'checkbox',
								name:'calcVat',
								boxLabel:PBPcm.Label.t.calcVat,
								inputValue:'1',
								margin:'0 0 0 0',
								width:lbw+20,
								checked:replaceIfNull(me.rec.vat_id, "0") != "0",
								listeners:{
									change:function(chk, newV) {
										me.fireEvent("calcVat",chk, newV);
									}
								},
								readOnly:me.rec.viewing
							},{
								xtype:'hidden',
								name:'priceInclude',
								value:replaceIfNull(me.rec.price_include, 0)
							},{
								xtype:'hidden',
								name:'vat',
								value:Number(replaceIfNull(me.rec.vat, 0).toFixed(5))
							},{
								xtype:'combo',
								name:'vatId',
								hideLabel:true,
//								fieldLabel:mandatoryLabel(PBPcm.Label.t.vat),
//								labelWidth:lbw,
						    	displayField:'name',
						    	valueField:'id',
						        emptyText : PB.Label.m.select,
						        store: vatStore,
						        queryMode: 'local',
						        multiSelect:false,
						        forceSelection:true,
								width:140,
								margin:"3 0 0 0",
								editable:false,
								allowBlank:false,
								listeners:{
									select : function(combo, rec){
				    	       		   me.fireEvent("selectVat",combo, rec);
				    	       	    }
								},
								value:me.rec.vat_id!=null ? me.rec.vat_id : null,
								readOnly:me.rec.viewing,
								fieldStyle:me.rec.viewing ? READ_ONLY : "",
								disabled:replaceIfNull(me.rec.vat_id, "0") == "0"
							},{
								xtype:'label',
								name:'vatAmt',
								style:'text-align:right;',
								width:fw-65,
								margin:'3 0 0 10'
							}]
						},{
							xtype:'container',
							layout:'hbox',
							border:0,
							margin:'5 5 0 5',
							items:[{
								xtype:'label',
								itemId:'lblTotal',
								text:PBPcm.Label.t.total + (replaceIfNull(me.rec.currency, "THB")=="THB" ? "" : " ("+ me.rec.currency +")"),
								width:lbw+100
							},{
								xtype:'label',
								name:'netAmt',
								style:'text-align:right;',
								width:fw
							},{
								xtype:'hiddenfield',
								name:'total',
								value:replaceIfNull(me.rec.total, 0)
							}]
						},{
							xtype:'container',
							layout:'hbox',
							border:0,
							margin:'5 5 0 5',
							items:[{
								xtype:'label',
								itemId:'lblTotalCnv',
								text:PBPcm.Label.t.total + ' (x'+Ext.util.Format.number(replaceIfNull(me.rec.currency_rate, 1), DEFAULT_MONEY_FORMAT)+' THB)',
								width:lbw+100
							},{
								xtype:'label',
								name:'netAmtCnv',
								style:'text-align:right;',
								width:fw
							},{
								xtype:'hiddenfield',
								name:'totalCnv'
//									,
//								value:replaceIfNull(me.rec.totalCnv, 0)
							}]
						}]
					}]
					
				}]	
			}]
		});		
		
	    this.callParent(arguments);
	    
		Ext.apply(store, {pageSize:PAGE_SIZE});
		store.load({
			params:{id:me.rec.id},
			callback:function() {
				me.fireEvent("itemStoreLoad");
			}
		});
	}
    
});
