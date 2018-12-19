Ext.define('PBAdmin.view.main.workflow.Main', {
    extend: 'Ext.panel.Panel',
    alias:'widget.adminMainWorkflowMain',

	initComponent: function(config) {
		var me = this;
		
		var lbw = 70 + (getLang().indexOf("th")>=0 ? 0 : 70);
		
		var store = Ext.create('PBAdmin.store.main.WorkflowGridStore');
		
		var wfStore = Ext.create('PB.store.common.ComboBoxStore');
		wfStore.getProxy().api.read = ALF_CONTEXT+'/srcUrl/main/master?all=true';
		wfStore.getProxy().extraParams = {
			p1 : "type='DOC_TYPE'",
			orderBy : 'code',
			all : true,
			lang : getLang()
		}
		wfStore.load();
		
		var columns = []
		columns.push(
				{ text: PB.Label.w.wfid,  dataIndex: 'wfid', width:110, hidden:true},
				{ text: PB.Label.w.id,  dataIndex: 'id', width:110, hidden:true},
				{ text: PB.Label.w.no,  dataIndex: 'no', width:100},
				{ text: PB.Label.w.desc,  dataIndex: 'desc', flex:1},
				{ text: PB.Label.w.assignee,  dataIndex: 'assignee', width:100},
				{ text: PB.Label.w.requester,  dataIndex: 'requester', width:100},
				{ text: PB.Label.w.preparer,  dataIndex: 'preparer', width:100},
				{ text: PB.Label.w.reqdate,  dataIndex: 'reqdate', width:150},
				{ text: PB.Label.w.status,  dataIndex: 'status', width:150, renderer: me.statusRenderer}
		);
		
		var selModel = Ext.create('Ext.selection.CheckboxModel',{
			mode:"MULTI",
			showHeaderCheckbox:true,
			renderer: function(value, metaData, record, rowIndex, colIndex, store, view) {
	            var baseCSSPrefix = Ext.baseCSSPrefix;
	            metaData.tdCls = baseCSSPrefix + 'grid-cell-special ' + baseCSSPrefix + 'grid-cell-row-checker';
	            return record.get('assignee') ? '<div class="' + baseCSSPrefix + 'grid-row-checker"> </div>' : '';
	        }
		});
		
		var acolumns = []
		acolumns.push(
				{ text: PB.Label.w.id,  dataIndex: 'id', width:110, hidden:true},
				{ text: PB.Label.w.srcAssignee,  dataIndex: 'src', width:150},
				{ text: PB.Label.w.destAssignee,  dataIndex: 'dest', width:150},
				{ text: PB.Label.w.assignedTime,  dataIndex: 'assignedTime', width:150},
				{ text: PB.Label.w.active,  dataIndex: 'active', width:80, renderer:me.renderActive, hidden:true}
		);

		var astore = Ext.create('PBAdmin.store.main.WorkflowAssigneeGridStore');
		
		var etypes = Ext.create('Ext.data.Store', {
	        fields: ['id', 'name'],
	        data : [
	            {"id":"0", "name":"Related"},
	            {"id":"1", "name":"My Task"},
	            {"id":"2", "name":"Assigned"}
	            //...
	        ]
	    });
		
		Ext.applyIf(me, {
			layout:'border',
			items:[{
				region:'center',
				xtype:'grid',
				itemId:'wfGrid',
				columns : columns,
				store:store,
				selModel:selModel,
				tbar : [{
					xtype:'trigger',
					name:'employee',
					fieldLabel:PB.Label.m.emp,
					width:230,
					labelWidth:70,
					margin:"5 0 0 10",
					trigger1Cls: 'x-form-clear-trigger',
				    trigger2Cls: 'x-form-search-trigger',
				    hideTrigger1: true,
				    hasSearch : false,
					editable:false,
					onTrigger1Click:function(evt) {
						this.triggerEl.item(0).dom.parentNode.style.width = "0px";
						me.fireEvent("clearEmployee");
					},
					onTrigger2Click:function(evt) {
						me.fireEvent("selectEmployee");
					},
					listeners:{
						afterrender:function() {
							var w = this.getValue() ? "17" : "0";
							this.triggerEl.item(0).dom.parentNode.style.width = w+"px";
						},
						change:function(trigger, newV, oldV) {
							var w = newV ? "17" : "0";
							this.triggerEl.item(0).dom.parentNode.style.width = w+"px";
						}
					}
				},{
					xtype:'textfield',
					name:'employeeName',
					hideLabel:true,
					width:200,
					margin:'5 0 0 10',
					readOnly:true,
					fieldStyle:READ_ONLY
				},{
					xtype:'combo',
					name:'eType',
					hideLabel:true,
			    	displayField:'name',
			    	valueField:'id',
			        emptyText : PB.Label.m.select,
			        store: etypes,
			        queryMode: 'local',
			        multiSelect:false,
			        forceSelection:true,
					width:100,
					allowBlank:false,
					editable:false,
					margin:'5 0 0 10'
				},{
					xtype:'combo',
					name:'wfType',
					fieldLabel:PB.Label.w.wfType,
			    	displayField:'name',
			    	valueField:'id',
			        emptyText : PB.Label.m.select,
			        store: wfStore,
			        queryMode: 'local',
			        typeAhead:true,
			        multiSelect:false,
			        forceSelection:true,
					width:180,
					labelWidth:70,
					allowBlank:true,
			        listConfig : {
					    getInnerTpl: function () {
							return '<div>{name}</div>';
					        //return '<div>{name}<tpl if="id != \'\'"> ({id})</tpl></div>';
					    }
					},
			        listeners:{
						beforequery : function(qe) {
							qe.query = new RegExp(qe.query, 'i');
			//				qe.forceAll = true;
						},
	    	       	    change : function(combo, newValue, oldValue, e){
	    	       		   me.fireEvent("selectWfType",combo, newValue, oldValue);
	    	       	    }
					},
					margin:'5 0 0 10'
				},{
					xtype: 'textfield',
					itemId:'txtSearch',
					width:150,
					enableKeyEvents: true,
				   	listeners: {
				   		
							specialkey: function(field, e){
								if(e.getKey() == e.ENTER){
									me.fireEvent("search");
								}
							}
				
				   	}
				},{
					xtype: 'button',
				    text: PB.Label.m.btnSearch,
				    iconCls: "icon_search",                
				    action: "search"
				},'->',{
					xtype: 'button',
				    text: PB.Label.m.btnFixDoc,
				    iconCls: "icon_view",                
				    action: "fixDoc"
				},{
					xtype: 'button',
				    text: PB.Label.m.btnCancelWf,
				    iconCls: "icon_no",                
				    action: "cancel"
				}],
				
				bbar : {
					xtype:'pagingtoolbar',
				    displayInfo    : true,
				    pageSize       : PAGE_SIZE,
				    store          : store
				}
			},{
				region:'south',
				height:200,
				xtype:'panel',
				title:'Assign',
				border:false,
				layout:'border',
				items:[{
					region:'west',
					xtype:'panel',
					layout:'vbox',
					frame:false,
					width:lbw+450,
					items:[{
						xtype:'container',
						layout:'hbox',
						items:[{
							xtype:'trigger',
							name:'srcAssignee',
							fieldLabel:PB.Label.w.srcAssignee,
							width:lbw+160,
							labelWidth:lbw+20,
							margin:"5 0 0 10",
							trigger1Cls: 'x-form-clear-trigger',
						    trigger2Cls: 'x-form-search-trigger',
						    hideTrigger1: true,
						    hasSearch : false,
							editable:false,
							onTrigger1Click:function(evt) {
								this.triggerEl.item(0).dom.parentNode.style.width = "0px";
								me.fireEvent("clearSrcAssignee");
							},
							onTrigger2Click:function(evt) {
								me.fireEvent("selectSrcAssignee");
							},
							listeners:{
								afterrender:function() {
									var w = this.getValue() ? "17" : "0";
									this.triggerEl.item(0).dom.parentNode.style.width = w+"px";
								},
								change:function(trigger, newV, oldV) {
									var w = newV ? "17" : "0";
									this.triggerEl.item(0).dom.parentNode.style.width = w+"px";
								}
							}
						},{
							xtype:'textfield',
							name:'srcAssigneeName',
							hideLabel:true,
							width:260,
							margin:'5 0 0 10',
							readOnly:true,
							fieldStyle:READ_ONLY
						}]
					},{
						xtype:'container',
						layout:'hbox',
						items:[{
							xtype:'trigger',
							name:'assignee',
							fieldLabel:PB.Label.w.destAssignee,
							width:lbw+160,
							labelWidth:lbw+20,
							margin:"5 0 0 10",
							trigger1Cls: 'x-form-clear-trigger',
						    trigger2Cls: 'x-form-search-trigger',
						    hideTrigger1: true,
						    hasSearch : false,
							editable:false,
							onTrigger1Click:function(evt) {
								this.triggerEl.item(0).dom.parentNode.style.width = "0px";
								me.fireEvent("clearAssignee");
							},
							onTrigger2Click:function(evt) {
								me.fireEvent("selectAssignee");
							},
							listeners:{
								afterrender:function() {
									var w = this.getValue() ? "17" : "0";
									this.triggerEl.item(0).dom.parentNode.style.width = w+"px";
								},
								change:function(trigger, newV, oldV) {
									var w = newV ? "17" : "0";
									this.triggerEl.item(0).dom.parentNode.style.width = w+"px";
								}
							}
						},{
							xtype:'textfield',
							name:'assigneeName',
							hideLabel:true,
							width:260,
							margin:'5 0 0 10',
							readOnly:true,
							fieldStyle:READ_ONLY
						}]
					},{
						xtype:'textarea',
						name:'reason',
						margin:'5 0 0 10',
						fieldLabel:PB.Label.w.remark,
						labelWidth:lbw+20,
						width:lbw+430
					}]
				},{
					region:'center',
					items:[{
						xtype: 'button',
					    text: PB.Label.m.btnAssign,
					    iconCls: "icon_assign",                
					    action: "assign",
						margin:'5 0 0 10',
						height:30
					}]
				},{
					region:'east',
					xtype:'grid',
					itemId:'wfaGrid',
					columns:acolumns,
					store:astore,
					width:WIDTH-lbw-680
				}]
			}]
				
		});		
		
	    this.callParent(arguments);

	},
	
	renderActive : function(vv, m, r, i, c, s, view) {
		var me = view.up('grid');
	
		var id = Ext.id();
        Ext.defer(function () {
        	if(Ext.getElementById(id) != undefined){
	            Ext.widget('linkbutton', {
	                renderTo: id,
//	                tooltip:'แก้ไข',
	                iconCls:'icon_' + (vv=="1" ? "ok" : "no"),
	                width: 20,
	                handler: function () {
	                	me.fireEvent("active",r);
	                }
	            });
        	}
        }, 50);
        
        return Ext.String.format('<div><div id="{0}" style="float:left;margin-right:5px"></div></div>', id);
	},
	
	statusRenderer : function(vv, m, r, i, c, s, view){
		
		var me = view.up('grid');
		
		var id = Ext.id();
	    Ext.defer(function () {
	    	if(Ext.getElementById(id) != undefined){
	            Ext.widget('linkbutton', {
	                renderTo: id,
	                tooltip:'History',
	                iconCls:'icon_postpone',
	                width: 20,
	                handler: function () {
	                	me.fireEvent("viewHistory",r);
	                }
	            });
	    	}
	    }, 50);
	    
	    return Ext.String.format('<div><div id="{0}" style="float:left;margin-right:5px"></div></div>{1}', id, vv);
	}
    
});
