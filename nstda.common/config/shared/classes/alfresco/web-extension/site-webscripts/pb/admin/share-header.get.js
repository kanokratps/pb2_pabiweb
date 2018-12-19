var headerBar = widgetUtils.findObject(model.jsonModel, "id", "HEADER_APP_MENU_BAR");
if (headerBar != null)
{
	var widgets = [];
	if (user.isAdmin) {
		widgets.push({
              name: "alfresco/header/AlfMenuItem",
              config:
              {
			      id: "HEADER_PB_ADMIN",
			      label: "header.menu.pb-admin.label",
			      iconClass: "alf-pb-admin-icon",
			      targetUrl: "admin"
              }
		});
	}
	
	widgets.push({
	  	  name: "alfresco/header/AlfMenuItem",
	      config:
	      {
		      label: "header.menu.pb-pcm.label",
		      iconClass: "alf-pb-pcm-icon",
		      targetUrl: "pcm"
	      }
    });
	
	widgets.push({
	  	  name: "alfresco/header/AlfMenuItem",
	      config:
	      {
		      label: "header.menu.pb-exp.label",
		      iconClass: "alf-pb-exp-icon",
		      targetUrl: "exp"
	      }
    });
	
	widgets.push({
	  	  name: "alfresco/header/AlfMenuItem",
	      config:
	      {
		      label: "header.menu.pb-hr.label",
		      iconClass: "alf-pb-hr-icon",
		      targetUrl: "hr"
	      }
    });
	
   headerBar.config.widgets.splice(1, 0, {
       id: "HEADER_PB",
	   name: "alfresco/header/AlfMenuBarPopup",
	   config: {
	       label: "header.menu.pb.label",
	       widgets: [{   
	             name: "alfresco/menus/AlfMenuGroup",
	             config: {
	                widgets: widgets
	             }
	       }]
	    }
	});
   
}