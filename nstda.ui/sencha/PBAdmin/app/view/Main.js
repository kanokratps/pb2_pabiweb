Ext.define('PBAdmin.view.Main', {
    extend: 'Ext.container.Container',
    requires:[
        'Ext.layout.container.Border'
    ],
    
    alias : 'widget.adminView',

    layout: {
        type: 'border'
    },

    items: [{
        xtype: 'panel',
	    title: 'Modules',
	    region: 'west',
	    frame:true,
	    width: 120,
	    collapsible: true,
	    layout:'fit',
	    items:{
    		xtype:'toolbar',
    		itemId:'menu',
    		vertical:true,
    		layout:'anchor'
    	}
	},{
	    region: 'center',
	    xtype: 'tabpanel'
    }],
    
	addMenu:function(name, iconCls, pressed) {
		var menu = this.down('[itemId=menu]');
		
		var newBtn = {
			text: name,
			pressed: pressed,
			xtype: 'button',
			action: 'menu',
			toggleGroup:'menu',
			width:"100%",
			iconCls:iconCls,
			scale:'large'
		};
		
		menu.add(newBtn);
	},

	setMenuItems:function(items) {
		var tabPanel = this.down("tabpanel");
		tabPanel.removeAll(true);
		
		var language = getLang()
		
		var firstTab;
		
		for(var i=0; i<items.length; i++) {
			
			var oitem = items[i];
			
			var item = {};
			
			for(var a in oitem) {
				item[a] = oitem[a];
			}
			
			if (language.toUpperCase() == "TH") {
				item['title'] = item['title_th'];
			}

			var newTab = tabPanel.add(item);
			
			if (i==0) {
				tabPanel.setActiveTab(newTab);
			}
			
		}
		
	}

});