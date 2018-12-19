/*
 * - Dlg
 */
Ext.define('PBAdmin.view.main.settings.Main', {
    extend: 'Ext.grid.Panel',
    alias:'widget.adminMainSettingsMain',
    requires: [
        'PBAdmin.store.main.SettingsTypeCmbStore',
        'Ext.toolbar.Toolbar',
        'Ext.toolbar.Fill',
        'Ext.toolbar.Paging',
        'Ext.button.Button',
        'Ext.form.field.Text',
        'Ext.form.field.ComboBox'
    ],

	initComponent: function(config) {
		var me = this;
		
		var settingsTypeStore = Ext.create('PB.store.common.ComboBoxStore',{storeId:'settingsTypeStore'});
		settingsTypeStore.getProxy().api.read = ALF_CONTEXT+'/admin/main/master/listType';
		settingsTypeStore.getProxy().extraParams = {
    		t : 'A'
 	   	};
		
		if (!me.showSystem) {
			settingsTypeStore.getProxy().extraParams.system = false;
		}
		
		settingsTypeStore.load();
		
		var columns = [
	          {
	        	dataIndex: 'action',
		        text: 'Action', 
		        width: 80,
		        renderer: me.action
	          },
			  { text: '#',  dataIndex: 'id', width:50}, 
	          { text: 'รหัส',  dataIndex: 'code', width:130}, 
	          { text: 'ชื่อ',  dataIndex: 'name', flex:1}, 
	          { text: 'ค่า (1)',  dataIndex: 'flag1', flex:1}, 
	          { text: 'ค่า (2)',  dataIndex: 'flag2', flex:1},
	          { text: 'ค่า (3)',  dataIndex: 'flag3', flex:1},
	          { text: 'ค่า (4)',  dataIndex: 'flag4', flex:1},
	          { text: 'ค่า (5)',  dataIndex: 'flag5', flex:1},
	          { text: 'Active',  dataIndex: 'is_active', width:60, renderer:me.renderStatus},
	          { text: 'System',  dataIndex: 'is_system', width:60, renderer:me.renderStatus, hidden:!me.showSystem}
		];
	
		Ext.applyIf(me, {
			
			plain:true,
			
			columns : columns,
				
			tbar : [{
               xtype: 'combo',
               fieldLabel : 'ประเภท',
               margin: '0 0 0 10',
               labelWidth: 50,
               name : 'type',
               allowBlank : false,
               emptyText : EMTY_TEXT_COMBO,
               store: settingsTypeStore,
               queryMode: 'local',
               displayField: 'name',
               valueField: 'id',
               width: 450,
		       listConfig : {
					width:500,
					resizable: true,
				    getInnerTpl: function () {
				        return '<div>{name}<tpl if="id != \'\'"> ({id})</tpl></div>';
				    }
			   },
	 	       listeners: {
				   beforequery : function(qe) {
						qe.query = new RegExp(qe.query, 'i');
		//				qe.forceAll = true;
				   },
		       	   change : function(combo, newValue, oldValue, e){
		       		   me.fireEvent("selectType",combo, newValue, oldValue);
		       	   }
	           }
			},{
			   xtype:'button',
			   hidden:true,
			   action:'back'
			},{
            	xtype: 'textfield',
            	itemId:'txtSearchSettings',
            	enableKeyEvents: true,
            	margin:'0 0 0 3',
	           	listeners: {
	           		
	 	 			specialkey: function(field, e){
	 	 				if(e.getKey() == e.ENTER){
	 	 					me.fireEvent("search");
	 	 				}
	 	 			}
			
	           	}
            },{
            	xtype: 'button',
                text: "ค้นหา",
                iconCls: "icon_search",                
                action: "searchSettings"
            },
                "->"
            ,{ 
            	xtype: 'button',
                text: "เพิ่มค่า",
                iconCls: "icon_add",
                action: "addSettings",
                disabled: true
            }],
            
		 	bbar : {
				  xtype:'pagingtoolbar',
			      displayInfo    : true,
			      pageSize       : ADMIN_PAGE_SIZE,
			      store          : me.store
//			},
//				
//		 	listeners : {
//		 	    itemdblclick: function(dv, record, item, index, e) {
//		 	        me.fireEvent("dblClick",record);
//		 	    }
		 	}
		
		});		
		
	    this.callParent(arguments);
	    
	    Ext.apply(me.store, {pageSize:ADMIN_PAGE_SIZE});
	},

	action : function(vv, m, r, i, c, s, view){
	
		var me = view.up('grid');
	
		if(vv=='ED'){
			var id = Ext.id();
	        Ext.defer(function () {
	        	if(Ext.getElementById(id) != undefined){
		            Ext.widget('linkbutton', {
		                renderTo: id,
		                tooltip:'แก้ไข',
		                iconCls:'icon_edit',
		                width: 20,
		                handler: function () {
		                	me.fireEvent("edit",r);
		                }
		            });
	        	}
	        }, 50);
	        
	        var id1 = Ext.id();
	        Ext.defer(function () {
	        	if(Ext.getElementById(id1) != undefined){
		            Ext.widget('linkbutton', {
		                renderTo: id1, 
		                tooltip:'ลบ',
		                iconCls:'icon_delete',
		                width: 20,
		                handler: function () {
		                	me.fireEvent("del",r);
		                }
		            });
	        	}
	        }, 50);
        
        
	        return Ext.String.format('<div><div id="{0}" style="float:left;margin-right:5px"></div><div id="{1}" style="float:left;margin-right:5px"></div></div>', id, id1);
		}
		
	},
	
	renderStatus : function(v) {
		if (v) {
			return '<div style="width:16px;height:16px;background-image: url('+MAIN_CONTEXT+'/res/page/img/icon/ok.png) !important;">&nbsp;</div>';
		}
		else {
			return '<div style="width:16px;height:16px;background-image: url('+MAIN_CONTEXT+'/res/page/img/icon/no.png) !important;">&nbsp;</div>';
		}
	}
    
});
