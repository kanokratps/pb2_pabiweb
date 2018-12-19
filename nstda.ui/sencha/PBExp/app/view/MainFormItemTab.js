Ext.define('PBExp.view.MainFormItemTab', {
    extend: 'Ext.panel.Panel',
    alias:'widget.expBrwItemTab',

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
	        	},			
				{ text: PBExp.Label.i.actGrp,  dataIndex: 'actGrpName', flex:1},
				{ text: PBExp.Label.i.act,  dataIndex: 'actName', flex:1},
				{ text: PBExp.Label.i.desc,  dataIndex: 'activity', flex:1},
				{ text: PBExp.Label.i.asset,  dataIndex: 'assetName', flex:0.75},
				{ text: PBExp.Label.i.cond,  dataIndex: 'condition1', flex:1, renderer:function(v){return (v ? v :"")}},
				{ text: PBExp.Label.i.amt,  dataIndex: 'amount', width:180, align:'right', xtype: 'numbercolumn', format:'0,000.00'}
		);
		
//		var vatStore = Ext.create('PB.store.common.ComboBoxStore');
//		vatStore.getProxy().api.read = ALF_CONTEXT+'/admin/main/tax/list';
//		vatStore.getProxy().extraParams = {
//		}
//		vatStore.load();
		
//		var store = Ext.create('PBExpUse.store.ItemGridStore');
		var store = Ext.create('PBExp.store.ItemGridStore',{storeId:'expBrwItemGridStore',autoLoad:false});
		store.getProxy().extraParams = {
			id : me.rec.id
		}
		
		var lbw = parseInt(PBExp.Label.i.lbw);
		
		Ext.applyIf(me, {
			items:[{
				xtype:'container',
				layout:'border',
				border:0,
				items:[{
					region:'north',
					xtype:'form',
					height:140,
					items:[{
						xtype:'checkbox',
						name:'isReason',
						boxLabel:PBExp.Label.i.isReason,
						inputValue:'1',
						margin:"5 0 0 10",
						//width:lbw+80,
						checked:replaceIfNull(me.rec.is_reason, "0") == "1",
						listeners:{
							change:function(chk, newV) {
								me.fireEvent("isReason",chk, newV);
							}
						},
						readOnly:me.rec.viewing
					},{
						xtype:'textfield',
						name:'reason',
						fieldLabel:PBExp.Label.i.reason,
						labelWidth:lbw,
						margin:"5 0 0 10",
						width:WIDTH-40,
						allowBlank:false,
						value:replaceIfNull(me.rec.reason, null),
						maxLength:255,
						readOnly:me.rec.viewing,
						fieldStyle:me.rec.viewing ? READ_ONLY : "",
						disabled:replaceIfNull(me.rec.is_reason, "0") != "1"
					},{
						xtype:'textareafield',
						name:'note',
						fieldLabel:PBExp.Label.i.note,
						labelWidth:lbw,
						margin:"5 0 0 10",
						width:WIDTH-40,
						height:70,
						allowBlank:true,
						maxLength:255,
						value:replaceIfNull(me.rec.note, null),
						readOnly:me.rec.viewing,
						fieldStyle:me.rec.viewing ? READ_ONLY : ""
					}]
				},{
					region:'center',
					margin:'0 0 0 0',
					xtype:'grid',
					itemId:'itemGrid',
					columns:columns,
					store:store,
					tbar:[{
		        		xtype: 'button',
		                text: PB.Label.m.btnAdd,
		                iconCls: "icon_add",
		                action:'addItem',
		                disabled:me.rec.viewing || me.rec.is_small_amount=="1"
					}]
//				    header:{
//						titlePosition:1,
//						items:[{
////					    	xtype:'tbfill'
////					    },{
//			        		xtype: 'button',
//			                text: PB.Label.m.btnAdd,
//			                iconCls: "icon_add",
//			                action:'addItem',
//			                disabled:me.rec.viewing
//			        	}]
//				    }
				},{
					xtype:'container',
					region:'south',
					layout:'border',
					height:30,
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
//							xtype:'container',
//							layout:'hbox',
//							border:0,
//							margin:'5 5 0 5',
//							items:[{
//								xtype:'label',
//								text:PBExpUse.Label.i.gross,
//								width:114
//							},{
//								xtype:'label',
//								name:'grossAmt',
//								style:'text-align:right;',
//								width:211
//							}]
//						},{
//							xtype:'container',
//							layout:'hbox',
//							border:0,
//							margin:'4 5 0 5',
//							items:[{
//								xtype:'hidden',
//								name:'vat',
//								value:replaceIfNull(me.rec.vat, 0)
//							},{
//								xtype:'combo',
//								name:'vatId',
//								fieldLabel:mandatoryLabel(PBExp.Label.i.vat),
//								labelWidth:100,
//						    	displayField:'name',
//						    	valueField:'id',
//						        emptyText : PB.Label.m.select,
//						        store: vatStore,
//						        queryMode: 'local',
//						        multiSelect:false,
//						        forceSelection:true,
//								width:220,
//								margin:"3 0 0 0",
//								editable:false,
//								allowBlank:false,
//								listeners:{
//									select : function(combo, rec){
//				    	       		   me.fireEvent("selectVat",combo, rec);
//				    	       	    }
//								},
//								value:me.rec.vat_id!=null ? me.rec.vat_id : null
//							},{
//								xtype:'label',
//								name:'vatAmt',
//								style:'text-align:right;',
//								width:100,
//								margin:'3 0 0 10'
//							}]
//						},{
							xtype:'container',
							layout:'hbox',
							border:0,
							margin:'5 5 0 5',
							items:[{
								xtype:'label',
								text:PBExp.Label.i.total,
								width:110
							},{
								xtype:'label',
								name:'netAmt',
								style:'text-align:right;',
								width:215
							},{
								xtype:'hiddenfield',
								name:'total',
								value:replaceIfNull(me.rec.total, 0)
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
