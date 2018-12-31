/**
 * Copyright (C) 2005-2013 Alfresco Software Limited.
 *
 * This file is part of Alfresco
 *
 * Alfresco is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Alfresco is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Alfresco. If not, see <http://www.gnu.org/licenses/>.
 */

/**
 * @module alfresco/header/AppMenu
 * @extends module:alfresco/menus/AlfMenuBarPopup
 * @mixes module:alfresco/core/CoreXhr
 * @author Thongchai Jiansampimon
 */
define(["dojo/_base/declare",
        "alfresco/header/AlfMenuBarPopup",
        "alfresco/core/CoreXhr",
        "dojo/_base/lang",
        "dojo/_base/array",
        "dojo/aspect",
        "dijit/registry",
        "alfresco/menus/AlfMenuGroup",
        "alfresco/header/AlfMenuItem",
        "alfresco/header/AlfCascadingMenu",
        "dojo/dom-style",
        "dijit/popup"], 
        function(declare, AlfMenuBarPopup, AlfXhr, lang, array, aspect, registry, AlfMenuGroup, AlfMenuItem, AlfCascadingMenu, domStyle, popup) {
   
   /**
    * This extends "alfresco/header/AlfMenuBarPopup" to add additional subscriptions for site data changes. Most
    * notably when site favourites are added and removed.
    */
   return declare([AlfMenuBarPopup, AlfXhr], {
      
      /**
       * An array of the i18n files to use with this widget.
       * 
       * @instance
       * @type {{i18nFile: string}[]}
       * @default [{i18nFile: "./i18n/AlfSitesMenu.properties"}]
       */
      i18nRequirements: [{i18nFile: "./i18n/AppMenu.properties"}],
      
      /**
       * This should be set with the id of the current user. By default it is set to null which indicates
       * and if not overridden will result in the add/remove favourite sites menu items NOT being displayed.
       * 
       * @instance
       * @type {string}
       * @default null
       */
      currentUser: null,
      
      /**
       * This should be set with the short name of the current site. By default it is set to the null which
       * indicates that the Sites Menu is not being displayed on a page related to a specific site. In which case
       * the "Add" and "Remove" favourite options will NOT be added.
       * @instance
       * @type {string}
       * @default null
       */
      currentSite: null,

      /**
       * Extend the default constructor to add subscriptions for handling favourites being added and removed.
       * 
       * @instance
       */
      constructor: function alfresco_header_AppMenu__constructor() {
      },
      
      /**
       * This defines the default widgets to display in the menu - just a loading image.
       * 
       * @instance
       * @type {object}
       */
      widgets: [
         {
            name: "alfresco/header/AlfMenuItem",
            config: {
               iconClass: "alf-loading-icon",
               label: "loading.label"
            }
         }
      ],
      
      /**
       * Extend the default postCreate function to setup handlers for adding the 'Useful' group once all the other
       * menu groups and items have been processed.
       *  
       * @instance
       */
      postCreate: function alf_menus_header_AppMenu__postCreate() {
         if (!this.label)
         {
            this.set("label", this.message("header.menu.pb.label"));
         }
         this.inherited(arguments);
         if (this.popup)
         {
            // The "opOpen" function of the associated AlfMenuGroups widget is not defined by default so we're 
            // going to "link" it to the popupFocused function so that the first time the user clicks on the 
            // AlfSitesMenu in the menu bar we can asynchronously load the required data.
            this.popup.onOpen = dojo.hitch(this, "loadMenu");
         }
         else
         {
            this.alfLog("log", "No App Menu popup - something has gone wrong!");
         }
      },
      
      /**
       * A URL to override the default. Primarily provided for the test harness.
       * @instance
       * @type {string} 
       */
      _menuUrl: null,
      
      /**
       * Indicates whether or not the menu has been loaded yet.
       * @instance
       * @type {boolean} 
       * @default false
       */
      _menuLoaded: false,
      
      /**
       * This function is called when the associated AlfMenuGroups popup is opened. It asynchronously loads the
       * recently visited sites information.
       * 
       * @instance
       */
      loadMenu: function alfresco_header_AppMenu__loadMenu() {
         if (this._menuLoaded)
         {
            this.alfLog("log", "Menu already loaded");
         }
         else
         {
            this.alfLog("log", "Loading menu");
            var url = this._menuUrl;
            if (url == null)
            {
               if (this.id == "HEADER_PB_BUDGET") {
            	   url = Alfresco.constants.PROXY_URI_RELATIVE + "pb/admin/menu/widgets-budget";
               } else {
            	   url = Alfresco.constants.PROXY_URI_RELATIVE + "pb/admin/menu/widgets";
               }
               
               if (this.currentSite)
               {
                  url = url + "/site/" + this.currentSite;
               }
            }
            this.serviceXhr({url : url,
                             method: "GET",
                             query:"lang="+this.getLang(),
                             successCallback: this._menuDataLoaded,
                             failureCallback: this._menuDataLoadFailed,
                             callbackScope: this});
         }
      },
      
      /**
       * 
       * @instance
       * @param {object} response The response from the request
       * @param {object} originalRequestConfig The configuration passed on the original request
       */
      _menuDataLoaded: function alfresco_header_AppMenu___menuDataLoaded(response, originalRequestConfig) {
         this.alfLog("log", "Menu data loaded successfully", response);
         this._menuLoaded = true;
         
         // Check for keyboard access by seeing if the first child is focused...
         var focusFirstChild = (this.popup && this.popup.getChildren().length > 0 && this.popup.getChildren()[0].focused);
         
         // Remove the loading menu item...
         var _this = this;
         array.forEach(this.popup.getChildren(), function(widget, index) {
            _this.popup.removeChild(widget);
         });
         
         if (response.widgets) {
        	 this.addWidgets(response.widgets);
         }
         
         if (focusFirstChild)
         {
            // Focus something after the async load completes to ensure that when the user has opened the 
            // menu with the keyboard they have something selected...
            if (_this.popup)
            {
               _this.popup.focusFirstChild();
            }  
         }
			
//         var iframe = document.getElementsByClassName("dijitBackgroundIframe");
//         for(var a in iframe) {
//        	 console.log(a+":"+iframe[a]);
//         }
//		 for(var i=0;i<iframe.length;i++){
//			 iframe[i].remove();
//         }
         
//		var label = document.querySelectorAll("td.dijitMenuItemLabel");
//////		var label = document.getElementsByClassName("dijitMenuItemLabel");
//		console.log(label)
//		for(var i=0;i<label.length;i++){
//			if(label[i].textContent=='Finance') {
//				label[i].style.width = '100px';
//			}
//		}
		
		var nodes = YAHOO.util.Selector.query('td.dijitMenuItemLabel[colspan=2]','HEADER_PB_dropdown');
//		console.log("node:"+nodes);
		var maxW = 0;
		for(var i=0;i<nodes.length;i++){
//			console.log(nodes[i].textContent+":"+nodes[i].offsetWidth);
			if (nodes[i].offsetWidth > maxW) {
				maxW = nodes[i].offsetWidth;
			}
		}
		for(var i=0;i<nodes.length;i++){
			nodes[i].style.width = maxW+'px';
		}
      },
      
      /**
       * Adds the "widgets" to the AlfAppMenuGroups popup.
       * 
       * @instance
       * @param {array} widgets An array of the recently visited site menu item widget configurations
       */
      addWidgets: function alf_header_AppMenu__addWidgets(widgets) {
         if (this.popup)
         {
        	 for(var ww in widgets) {
        		 var w = widgets[ww];
        		 //console.log(" - "+w.name);
        		 
        		 var widget = null;
        		 
        		 if (w.type == "G") {
        			 widget = new AlfMenuGroup({
        	               label: w.config.label,
        	               widgets:w.config.widgets
        	         });
        		 }
        		 else
        		 if (w.type == "I") {
        			 widget = new AlfMenuItem(w.config);
        		 }
        		 else
        		 if (w.type == "M") {
        			 widget = new AlfCascadingMenu(w.config);
        		 }

            	 this.popup.addChild(widget);
             }
         }
      },
      
      /**
       * 
       * @instance
       * @param {object} response The response from the request
       * @param {object} originalRequestConfig The configuration passed on the original request
       */
      _menuDataLoadFailed: function alfresco_header_AppMenu___menuDataLoadFailed(response, originalRequestConfig) {
         this.alfLog("error", "Could not load app menu items", response);
         var _this = this;
         array.forEach(this.popup.getChildren(), function(widget, index) {
            _this.popup.removeChild(widget);
         });
         this.addMenuFailMessageItem()
      },
      
      /**
       * This is a reference to any favourites message item that gets created (typically when either
       * favourites could not be loaded or there are no favourites to display).
       * 
       * @instance
       * @type {object}
       * @default null
       */
      _menuMessageItem: null,
      
      /**
       * Adds a non-functional message menu item to the favourites menu. This is used when needing to 
       * indicate that the favourites could not be loaded. The most likely cause of this would be when
       * connectivity has been lost, authentication timed out or the repository has gone down since
       * loading the page.
       * 
       * @instance
       */
      addMenuFailMessageItem: function alf_header_AppMenu__addMenuFailMessageItem() {
         this._menuMessageItem = new AlfMenuItem({
            label: "menu.load.error"
         });
         this.popup.addChild(this._menuMessageItem);
      },
       
	  getLang:function alf_header_appMenu_getLang() {
		  var v = YAHOO.util.Cookie.get("alf_share_locale");
		  return v ? v : "en";
	  }
    
   });
});