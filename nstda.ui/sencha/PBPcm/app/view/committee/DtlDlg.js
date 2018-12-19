Ext.define('PBPcm.view.committee.DtlDlg', {
	extend : 'Ext.window.Window',
	alias : 'widget.pcmCommitteeDtlDlg',
	
	initComponent: function(config) {
		var me = this;
		
		var lbw = 70;
		
		var store = Ext.create('PB.store.common.UserStore');
		
		var tstore = Ext.create('PB.store.common.ComboBoxStore',{
			autoLoad:false,
			sorters: [{
		         property: 'name',
		         direction: 'ASC'
		    }]
		});
		tstore.getProxy().api.read = ALF_CONTEXT+'/admin/main/partner/title/list';
		tstore.load();
		
		Ext.applyIf(me, {
		        renderTo : Ext.getBody(),
	            modal: true,
	            width: 650,
	            height: 500,
	            layout: 'border',
	            resizable: false,
	            items : [{
	            	region:'center',
		        	xtype : 'form',
			        itemId : 'formDetail',
			        border : 0,
			        items:[{
						xtype: 'hidden',
						name: 'id'
					},{
						xtype:'combo',
						name:'title',
						fieldLabel:mandatoryLabel(PB.Label.m.title),
				    	displayField:'name',
				    	valueField:'id',
				        emptyText : PB.Label.m.select,
				        store: tstore,
		//		        queryMode: 'local',
				        typeAhead:true,
				        multiSelect:false,
				        forceSelection:true,
				        anchor:"-10",
						labelWidth:70,
						margin: '10 0 0 10',
						allowBlank:false,
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
		    	       		    me.fireEvent("selectActivity",combo, rec);
		    	       	    },
							afterrender:function(cmb) {
								Ext.defer(function(){
									cmb.focus();
								},100);
							}
						},
						value:me.rec ? me.rec.get("title") : null
					},{
					    xtype: 'textfield',
					    fieldLabel : mandatoryLabel(PB.Label.m.fname), 
					    labelWidth: 70,
					    anchor:"-10",
					    name : 'first_name',
					    msgTarget: 'side',
					    margin: '10 0 0 10'
					},{
					    xtype: 'textfield',
					    fieldLabel : mandatoryLabel(PB.Label.m.lname), 
					    labelWidth: 70,
					    anchor:"-10",
					    name : 'last_name',
					    msgTarget: 'side',
					    margin: '10 0 0 10'
					}]
	            },{
	            	region:'south',
//	            	collapsible:true,
//	            	collapsed:true,
	            	title:'ค้นหาพนักงาน',
	            	layout:'border',
	            	height:350,
			        items : [{
			        	region:'north',
			        	height:40,
			        	xtype : 'form',
				        itemId : 'searchUser',
				        border : 0,
				        items:[{
				        	xtype:'container',
				        	margin:'5 0 0 10',
				        	layout:{
								type:'hbox',
								align:'middle'
							},
							items:[{
					        	xtype:'textfield',
					        	itemId:'searchTerm',
					        	fieldLabel:'คำค้นหา',
					        	labelWidth:lbw,
					        	width:400,
					        	margin:'5 0 0 0',
				            	enableKeyEvents: true,
				            	hasfocus:true,
					           	listeners: {
					 	 			specialkey: function(field, e){
					 	 				if(e.getKey() == e.ENTER){
					 	 					this.fireEvent("searchUser",this);
					 	 				}
					 	 			},
					 	 			afterrender: function(txt) {
										Ext.defer(function(){
											txt.focus();
										},100);
										this.setHeight(22);
					 	 			}
					           	}			        		
							},{
					        	xtype:'button',
					        	text:'ค้นหา',
					        	iconCls:'icon_search',
					        	margin:'5 0 0 10',
				                listeners: {
				                    click: function(){
				                    	this.fireEvent("searchUser", this);
				                    }
				                }
							}]
				        }]
			        },{
			        	region:'center',
			        	xtype:'grid',
			        	margin:'5 0 0 0',
			        	columns:[
			        	     { text:'ศูนย์', dataIndex: 'org_name', width: 100 },
			        	     { text:'หน่วยงาน', dataIndex: 'section_name', flex:1 },
			        	     { text:'รหัสพนักงาน', dataIndex: 'emp_id', width: 100 },
			        	     { text:'ชื่อ-นามสกุล', dataIndex: 'first_name', flex:1, renderer:function(v,m,r){ return v+" "+r.get('last_name');}}
			        	],
			        	store:store
			        }]
	            }],
		        buttons : [{
		          text: 'บันทึก', 
	//	          disabled : true,
		          action : 'ok',
		          itemId: 'okButton',
		          iconCls:'icon_ok'
		        },{
		          text: 'ยกเลิก',
		          handler:this.destroy,
		          scope:this,
		          iconCls:'icon_no'
		        }]
		});
		
        this.callParent(arguments);
	}
});		