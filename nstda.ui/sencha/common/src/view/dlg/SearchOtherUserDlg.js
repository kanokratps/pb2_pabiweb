Ext.define('PB.view.common.SearchOtherUserDlg', {
	extend : 'Ext.window.Window',
	alias : 'widget.searchOtherUserDlg',
	
	initComponent: function(config) {
		var me = this;
		
		var lbw = 100;
		var lbwcb = 230;
		
		var destStore = Ext.create('PB.store.common.ComboBoxStore',{
			autoLoad:false,
			sorters: [
		        {
		            property : 'name',
		            direction: 'ASC'
		        }
		    ]
		});
		destStore.getProxy().api.read = ALF_CONTEXT+'/admin/main/destination/list';
		destStore.getProxy().extraParams = {
			t:"D"
		}
		destStore.load();
		
		var tstore = Ext.create('PB.store.common.ComboBoxStore',{
			autoLoad:false,
			sorters: [{
		         property: 'o',
		         direction: 'ASC'
		    }]
		});
		tstore.getProxy().api.read = ALF_CONTEXT+'/admin/main/partner/title/list';
		tstore.getProxy().extraParams = {
			lang:getLang()
		}
		tstore.load();
		
		var items = [{
        	region:'center',
	    	xtype : 'form',
	        itemId : 'formDetail',
	        border : 0,
	        items:[{
				xtype:'combo',
				name:'title',
				fieldLabel:PB.Label.m.title,
		    	displayField:'name',
		    	valueField:'id',
		        emptyText : PB.Label.m.select,
		        store: tstore,
//		        queryMode: 'local',
		        typeAhead:true,
		        multiSelect:false,
		        forceSelection:true,
		        anchor:"-10",
				labelWidth:lbw,
				margin: '10 0 0 10',
				allowBlank:true,
		        listConfig : {
				    getInnerTpl: function () {
						return '<div>{name}</div>';
				        //return '<div>{name}<tpl if="id != \'\'"> ({id})</tpl></div>';
				    }
				},
		        listeners:{
					beforequery : function(qe) {
						qe.query = getLang()+" "+qe.query;
					},
					select : function(combo, rec){
    	       		    //me.fireEvent("selectActivity",combo, rec);
    	       	    },
					afterrender:function(cmb) {
						Ext.defer(function(){
							cmb.focus();
						},100);
					}
				},
			    value:me.rec ? replaceIfNull(me.rec.get("tid"), 0) : 0,
				maxLength:255
//						
//						
//			    xtype: 'textfield',
//			    fieldLabel : mandatoryLabel(PB.Label.m.title), 
//			    labelWidth: lbw,
//			    anchor:"-10",
//			    hideTrigger:true,
//			    name : 'title',
//			    msgTarget: 'side',
//			    margin: '10 0 0 10',
//			    allowBlank:false,
//			    listeners:{
//					afterrender:function(txt) {
//						Ext.defer(function(){
//							txt.focus();
//						},100);
//					}
//				},
//			    value:me.rec ? replaceIfNull(me.rec.get("title"), null) : null,
//				maxLength:255
			},{
			    xtype: 'textfield',
			    fieldLabel : mandatoryLabel(PB.Label.m.fname), 
			    labelWidth: lbw,
			    anchor:"-10",
			    hideTrigger:true,
			    name : 'fname',
			    msgTarget: 'side',
			    margin: '10 0 0 10',
			    allowBlank:false,
			    value:me.rec ? replaceIfNull(me.rec.get("fname"), null) : null,
				maxLength:255
			},{
			    xtype: 'textfield',
			    fieldLabel : mandatoryLabel(PB.Label.m.lname), 
			    labelWidth: lbw,
			    anchor:"-10",
			    hideTrigger:true,
			    name : 'lname',
			    msgTarget: 'side',
			    margin: '10 0 0 10',
			    allowBlank:false,
			    value:me.rec ? replaceIfNull(me.rec.get("lname"), null) : null,
				maxLength:255
			}]
		}]
		
		if (me.needPosition) {
			items[0].items.push({
			    xtype: 'textfield',
			    fieldLabel : mandatoryLabel(PB.Label.m.sectionNonemp), 
			    labelWidth: lbw,
			    anchor:"-10",
			    hideTrigger:true,
			    name : 'section',
			    msgTarget: 'side',
			    margin: '10 0 0 10',
			    allowBlank:false,
			    value:me.rec ? replaceIfNull(me.rec.get("utype"), null) : null,
				maxLength:255
			});
			
			items[0].items.push({
			    xtype: 'textfield',
			    fieldLabel : mandatoryLabel(PB.Label.m.pos), 
			    labelWidth: lbw,
			    anchor:"-10",
			    hideTrigger:true,
			    name : 'position',
			    msgTarget: 'side',
			    margin: '10 0 0 10',
			    allowBlank:false,
			    value:me.rec ? replaceIfNull(me.rec.get("position"), null) : null,
				maxLength:255
			});
		}
		
		if (me.needFootPrint) {
			items.push({
				region:'south',
				xtype:'form',
				border:0,
				title:'Carbon Footprint',
				collapsible:true,
				collapsed:true,
				items:[{
					xtype:'container',
					margin:'5 5 0 5',
					layout:{
						type:'hbox',
						align:'middle'
					},
					items:[{
						xtype:'radio',
						name:'destType',
						boxLabel:'ในประเทศ',
						inputValue:'D',
						margin:'0 15 0 5',
						checked:true,
						listeners:{
							change:function(rad, newV, oldV) {
								me.fireEvent("selectDestination",rad,newV);
							}
						}
					},{
						xtype:'radio',
						name:'destType',
						boxLabel:'ต่างประเทศ',
						inputValue:'I',
						margin:'0 15 0 5',
						checked:false
					},{
						xtype:'combo',
						itemId:'destination',
						hideLabel:true,
				    	displayField:'name',
				    	valueField:'name',
				        emptyText : PB.Label.m.select,
				        store:destStore,
				        queryMode: 'local',
				        typeAhead:true,
				        multiSelect:false,
				        forceSelection:true,
						flex:1,
			    		margin:'5 10 0 40',
			    		anchor:'-10',
						editable:true,
						allowBlank:false,
						listeners:{
							beforequery : function(qe) {
								qe.query = new RegExp(qe.query, 'i');
				//				qe.forceAll = true;
							},
							select : function(combo, rec){
				       		   me.fireEvent("selectDestination",combo, rec);
				       	    }
						}
			    	}]
				},{
					xtype:'textfield',
					fieldLabel:'เส้นทางการบิน',
					labelWidth:lbwcb,
					name:'route',
					margin:'5 5 0 10',
					anchor:'-10'
				},{
					xtype:'container',
					margin:'5 5 0 10',
					anchor:'-10',
					layout:{
						type:'hbox',
						align:'middle'
					},
					items:[{
			    		xtype:'datefield',
			    		fieldLabel:'วันที่เริ่มต้น - สิ้นสุด',
			    		labelWidth:lbwcb,
			    		name:'depart',
			    		itemId:'depart',
			    		width:lbwcb+120,
			    		vtype: 'daterange',
						endDateField: 'arrive',
						listeners : {
							change : function(d, newV, oldV) {
				                var end = d.up('form').down('#' + d.endDateField);
				                end.setMinValue(newV);
				                end.validate();
				                d.dateRangeMin = newV;
							}
						}
					},{
						xtype:'label',
						text:' - ',
						width:15,
						margin:'0 0 0 5'
					},{
			    		xtype:'datefield',
			    		hideLabel:true,
			    		name:'arrive',
			    		itemId:'arrive',
			    		width:120,
			    		vtype: 'daterange',
						startDateField: 'depart',
						listeners : {
							change : function(d, newV, oldV) {
				                var start = d.up('form').down('#' + d.startDateField);
				                start.setMaxValue(newV);
				                start.validate();
				                d.dateRangeMax = newV;
							}
						}
					}]
				},{
					xtype:'textfield',
					fieldLabel:'Class',
					labelWidth:lbwcb,
					name:'cls',
					margin:'5 5 0 10',
					anchor:'-10'
				},{
					xtype:'numberfield',
					fieldLabel:'จำนวนเงิน',
					labelWidth:lbwcb,
					name:'amount',
					hideTrigger:true,
					margin:'5 5 5 10',
					anchor:'-10'
				}]	            	
			});
		}		
		
		Ext.applyIf(me, {
	        renderTo : Ext.getBody(),
	        modal: true,
	        width: 550,
	        height: 350,
	        layout: 'border',
	        resizable: false,
	        items : items,
	        buttons : [{
	          text: PB.Label.m.btnConfirm, 
	          action : 'ok',
	          iconCls:'icon_ok',
	          listeners: {
	               click: function(){
	                	this.fireEvent("confirm", this);
	               }
	          }
	        },{
	          text: PB.Label.m.cancel,
	          handler:this.destroy,
	          scope:this,
	          iconCls:'icon_no'
	        }]		
		});
		
        this.callParent(arguments);
	}
});		