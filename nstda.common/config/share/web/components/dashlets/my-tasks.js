/**
 * Copyright (C) 2005-2010 Alfresco Software Limited.
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
 * Dashboard MyTasks component.
 *
 * @namespace Alfresco.dashlet
 * @class Alfresco.dashlet.MyTasks
 */
(function()
{
   /**
    * YUI Library aliases
    */
   var Dom = YAHOO.util.Dom,
      Event = YAHOO.util.Event,
      Selector = YAHOO.util.Selector;

   /**
    * Alfresco Slingshot aliases
    */
   var $html = Alfresco.util.encodeHTML,
      $siteURL = Alfresco.util.siteURL;

   /**
    * Dashboard MyTasks constructor.
    *
    * @param {String} htmlId The HTML id of the parent element
    * @return {Alfresco.dashlet.MyTasks} The new component instance
    * @constructor
    */
   Alfresco.dashlet.MyTasks = function MyTasks_constructor(htmlId)
   {
      Alfresco.dashlet.MyTasks.superclass.constructor.call(this, "Alfresco.dashlet.MyTasks", htmlId, ["button", "container", "datasource", "datatable", "paginator", "history", "animation"]);

      // Services
      this.services.preferences = new Alfresco.service.Preferences();

      return this;
   };

   /**
    * Extend from Alfresco.component.Base
    */
   YAHOO.extend(Alfresco.dashlet.MyTasks, Alfresco.component.Base);

   /**
    * Augment prototype with Common Workflow actions to reuse createFilterURLParameters
    */
   YAHOO.lang.augmentProto(Alfresco.dashlet.MyTasks, Alfresco.action.WorkflowActions);

   /**
    * Augment prototype with main class implementation, ensuring overwrite is enabled
    */
   YAHOO.lang.augmentObject(Alfresco.dashlet.MyTasks.prototype,
   {
      /**
       * Preferences
       */
      PREFERENCES_TASKS_DASHLET: "",
      PREFERENCES_TASKS_DASHLET_FILTER: "",
      /**
       * Object container for initialization options
       *
       * @property options
       * @type object
       */
      options:
      {
         /**
          * Task types not to display
          *
          * @property hiddenTaskTypes
          * @type object
          * @default []
          */
         hiddenTaskTypes: [],

         /**
          * Maximum number of tasks to display in the dashlet.
          *
          * @property maxItems
          * @type int
          * @default 50
          */
         maxItems: 50,

         /**
          * Filter look-up: type to display value and query value
          *
          * @property filters
          * @type Object
          */
         filters: {}
      },

      /**
       * Fired by YUI when parent element is available for scripting
       * @method onReady
       */
      onReady: function MyTasks_onReady()
      {
         // Create filter menu
         this.widgets.filterMenuButton = Alfresco.util.createYUIButton(this, "filters", this.onFilterSelected,
         {
            type: "menu",
            menu: "filters-menu",
            lazyloadmenu: false
         });
         
         this.widgets.searchButton = Alfresco.util.createYUIButton(this, "search", this.onSearchPressed,
         {
         });

         // Load preferences (after which the appropriate tasks will be displayed)
         this.PREFERENCES_TASKS_DASHLET = this.services.preferences.getDashletId(this, "tasks");
         this.PREFERENCES_TASKS_DASHLET_FILTER = this.PREFERENCES_TASKS_DASHLET + ".filter";
         var prefs = this.services.preferences.get();
         
         // Select the preferred filter in the ui
         var filter = Alfresco.util.findValueByDotNotation(prefs, this.PREFERENCES_TASKS_DASHLET_FILTER, "activeTasks");
         filter = this.options.filters.hasOwnProperty(filter) ? filter : "activeTasks";
         this.widgets.filterMenuButton.set("label", this.msg("filter." + filter));
         this.widgets.filterMenuButton.value = filter;
         
         // Display the toolbar now that we have selected the filter
         Dom.removeClass(Selector.query(".toolbar div", this.id, true), "hidden");
         
         // Doy+ {
         this.widgets.filterMenuButton.set("disabled",true);
         
         var ds = Selector.query(".toolbar span", this.id, false);
         for(var a in ds) {
//        	 console.log(a+":"+ds[a])
    		 if (ds[a]["className"] == "first-child" && ds[a]["innerHTML"].indexOf("my-task")>=0) {
//    			 console.log("  hidden");
        		 Dom.addClass(ds[a], "hidden");
        	 }
        	 else
    		 if (ds[a]["innerHTML"] == "|") {
//    			 console.log("  hidden");
        		 Dom.addClass(ds[a], "hidden");
        	 }
//        	 else {
//    			 for(var b in ds[a]) {
//	    			 console.log("  "+b+":"+ds[a][b]);
//	    		 }
//        	 }
         }
         // Doy+ }

         // Prepare webscript url to task instances
         var webscript = YAHOO.lang.substitute("api/task-instances?authority={authority}&properties={properties}&exclude={exclude}",
         {
            authority: encodeURIComponent(Alfresco.constants.USERNAME),
            properties: ["bpm_priority", "bpm_status", "bpm_dueDate", "bpm_description","pcmreqwf_description","pcmordwf_description","expbrwwf_description","expusewf_description","hrsalwf_description"].join(","),
            exclude: this.options.hiddenTaskTypes.join(",")
         });

         /**
          * Create datatable with a simple pagination that only displays number of results.
          * The pagination is handled in the "base" data source url and can't be changed in the dashlet
          */
         this.widgets.alfrescoDataTable = new Alfresco.util.DataTable(
         {
            dataSource:
            {
               url: Alfresco.constants.PROXY_URI + webscript,
               filterResolver: this.bind(function()
               {
                  // Reuse method form WorkflowActions
                  var filter = this.widgets.filterMenuButton.value;
                  var filterParameters = this.options.filters[filter];
//                  console.log("datasource.filerResolver : filter :"+filter);
//                  console.log("datasource.filerResolver : filterParameters :"+filterParameters);
                  return this.substituteParameters(filterParameters) || "";
               })
            },
            dataTable:
            {
               container: this.id + "-tasks",
               columnDefinitions:
               [
                  { key: "isPooled", sortable: false, formatter: this.bind(this.renderCellIcons), width: 24 },
                  { key: "title", sortable: false, formatter: this.bind(this.renderCellTaskInfo) }
//                  ,
//                  { key: "name", sortable: false, formatter: this.bind(this.renderCellActions), width: 65 }
               ],
               config:
               {
                  MSG_EMPTY: this.msg("message.noTasks")
               }
            },
            paginator:
            {
               history: false,
               hide: false,
               config:
               {
                  containers: [this.id + "-paginator"],
                  template: "{FirstPageLink} {PreviousPageLink} {CurrentPageReport} {NextPageLink} {LastPageLink}",
                  firstPageLinkLabel: "&lt;&lt;",
                  previousPageLinkLabel: "&lt;",
                  nextPageLinkLabel: "&gt;",
                  lastPageLinkLabel: "&gt;&gt;",
                  pageReportTemplate: this.msg("pagination.template.page-report"),
                  rowsPerPage: this.options.maxItems
               }               
            }
         });

         var url = Alfresco.constants.PROXY_URI_RELATIVE+"pb/util/flashMsg";
         var myCallback = {
           success: function(o) {var r=JSON.parse(o.responseText);if(r.data.msg){
	       		mySimpleDialog = new YAHOO.widget.SimpleDialog("dlg", {
	    		    width: "20em",
	    		    height: "10em",
	    		    effect:{
	    		        effect: YAHOO.widget.ContainerEffect.FADE,
	    		        duration: 0.25
	    		    },
	    		    fixedcenter: true,
	    		    modal: true,
	    		    visible: false,
	    		    draggable: false,
	    		    close: false
	    		});
	    		 
	    		mySimpleDialog.setHeader("Warning!");
	    		mySimpleDialog.setBody(r.data.msg);
	//    		mySimpleDialog.cfg.setProperty("icon", YAHOO.widget.SimpleDialog.ICON_WARN);
	    		
	    		var handleYes = function() {
	    		    //user confirms the deletion of this item;
	    		    //this method would perform that deletion;
	    		    //when ready, hide the SimpleDialog:
	    		    this.hide();
	    		};
	    		var myButtons = [
	    		    { text: "OK", handler: handleYes }
	    		];
	    		 
	    		mySimpleDialog.cfg.queueProperty("buttons", myButtons);
	    		mySimpleDialog.render(document.body);
	    		mySimpleDialog.show();     
    	   }},
           failure: function(o) {alert("failure")},
           /* ... */
         };
         //var transaction = YAHOO.util.Connect.asyncRequest('GET', url, myCallback);
         
         // Override DataTable function to set custom empty message
         var me = this,
            dataTable = this.widgets.alfrescoDataTable.getDataTable(),
            original_doBeforeLoadData = dataTable.doBeforeLoadData;

         dataTable.doBeforeLoadData = function MyTasks_doBeforeLoadData(sRequest, oResponse, oPayload)
         {
            // Hide the paginator if there are fewer rows than would cause pagination
            if (oResponse.results.length === 0)
            {
               Dom.addClass(this.configs.paginator.getContainerNodes(), "hidden");
            }
            else
            {
               Dom.removeClass(this.configs.paginator.getContainerNodes(), "hidden");
            }

            if (oResponse.results.length === 0)
            {
               oResponse.results.unshift(
               {
                  isInfo: true,
                  title: me.msg("empty.title"),
                  description: me.msg("empty.description")
               });
            }

            return original_doBeforeLoadData.apply(this, arguments);
         };
         
      },
      
      // Doy+ {
      onSearchPressed: function MyTasks_onSearchPressed(p_sType, p_aArgs)
      {
    	  var menuItem = this.widgets.filterMenuButton.value;
//   	      console.log("onSearchPressed : menuItem : "+menuItem);
//   	      console.log("onSearchPressed : this.options.filters[menuItem] : "+this.options.filters[menuItem]);
          var parameters = this.substituteParameters(this.options.filters[menuItem], {});
//   	      console.log("onSearchPressed : parameters : "+parameters);
   	      
   	      var searchTerm = document.getElementById("searchTermTask");
   	      parameters += "&name=" + searchTerm.value;
   	      
          this.widgets.alfrescoDataTable.loadDataTable(parameters);

//    	  console.log("Search Pressed");
      },
      // Doy+ }

      /**
       * Reloads the list with the new filter and updates the filter menu button's label
       *
       * @param p_sType {string} The event
       * @param p_aArgs {array} Event arguments
       */
      onFilterSelected: function MyTasks_onFilterSelected(p_sType, p_aArgs)
      {
         var menuItem = p_aArgs[1];
         
         if (menuItem)
         {
            // Set the currentSkipCount to 0 so after changing the filter the paging starts from the first page
            // Otherwise an error can occur if the previous taks page has more pages than the new page
            this.widgets.alfrescoDataTable.currentSkipCount = 0;
            
            this.widgets.filterMenuButton.set("label", menuItem.cfg.getProperty("text"));
            this.widgets.filterMenuButton.value = menuItem.value;
            
//     	    console.log("onFilterSelected : menuItem.value : "+menuItem.value);
//     	    console.log("onFilterSelected : this.options.filters[menuItem.value] : "+this.options.filters[menuItem.value]);
            var parameters = this.substituteParameters(this.options.filters[menuItem.value], {});
//     	    console.log("onFilterSelected : parameters : "+parameters);
            this.widgets.alfrescoDataTable.loadDataTable(parameters);

            // Save preferences
            this.services.preferences.set(this.PREFERENCES_TASKS_DASHLET_FILTER, menuItem.value);
         }
      },

      /**
       * Priority & pooled icons custom datacell formatter
       */
      renderCellIcons: function MyTasks_onReady_renderCellIcons(elCell, oRecord, oColumn, oData)
      {
         var data = oRecord.getData(),
            desc = "";

         if (data.isInfo)
         {
            oColumn.width = 52;
            Dom.setStyle(elCell, "width", oColumn.width + "px");
            Dom.setStyle(elCell.parentNode, "width", oColumn.width + "px");

            desc = '<img src="' + Alfresco.constants.URL_RESCONTEXT + 'components/images/help-task-bw-32.png" />';
         }
         else
         {
        	 
            var priority = data.properties["bpm_priority"],
               priorityMap = { "1": "high", "2": "medium", "3": "low" },
               priorityKey = priorityMap[priority + ""],
               pooledTask = data.isPooled;
            
            desc = '<img src="' + Alfresco.constants.URL_RESCONTEXT + 'components/images/priority-' + priorityKey + '-16.png" title="' + this.msg("label.priority", this.msg("priority." + priorityKey)) + '"/>';
            if (pooledTask)
            {
               desc += '<br/><img src="' + Alfresco.constants.URL_RESCONTEXT + 'components/images/pooled-task-16.png" title="' + this.msg("label.pooledTask") + '"/>';
            }
            
            //Doy+ {
//            console.log("data.title::::" + data.title);
//            for(var a in data) {
//            	console.log(a+":"+data[a]);
//            }
//            for(var a in data.properties) {
//            	console.log("p:"+a+":"+data.properties[a]);
//            }
            
            var status = data.properties["bpm_status"];
            if (status) {
	            if(status.indexOf("REJECT") > -1){
	            	desc += '<img src="' + Alfresco.constants.URL_RESCONTEXT + 'components/images/reject.png" />'
	            }
	            else if(status.indexOf("RESUBMIT") > -1){
	            	desc += '<img src="' + Alfresco.constants.URL_RESCONTEXT + 'components/images/revise.png" />'
	            }
	            else if(status.indexOf("WAPPR") > -1){
	            	desc += '<img src="' + Alfresco.constants.URL_RESCONTEXT + 'components/images/wappr.png" />'
	            }
	            else if(status.indexOf("CONSULT") > -1){
	            	desc += '<img src="' + Alfresco.constants.URL_RESCONTEXT + 'components/images/consult.png" />'
	            }
            }
            
            
            //Doy+ }
         }

         elCell.innerHTML = desc;
      },

      /**
       * Task info custom datacell formatter
       */
      renderCellTaskInfo: function MyTasks_onReady_renderCellTaskInfo(elCell, oRecord, oColumn, oData)
      {
         var data = oRecord.getData(),
            desc = "";

         if (data.isInfo)
         {
            desc += '<div class="empty"><h3>' + data.title + '</h3>';
            desc += '<span>' + data.description + '</span></div>';
         }
         else
         {
            var taskId = data.id,
               message = data.properties["bpm_description"],
               dueDateStr = data.properties["bpm_dueDate"],
               dueDate = dueDateStr ? Alfresco.util.fromISO8601(dueDateStr) : null,
               today = new Date(),
               type = data.title,
               status = data.properties["bpm_status"],
               assignee = data.owner;
               
            // if there is a property label available for the status use that instead
            if (data.propertyLabels && Alfresco.util.isValueSet(data.propertyLabels["bpm_status"], false))
            {
               status = data.propertyLabels["bpm_status"];
            }
            
            var v = YAHOO.util.Cookie.get("alf_share_locale");
            v = v ? v : "en";
            var isThai = v.indexOf("th")==0;
            
            if (!isThai) {
            	message = data.properties["pcmreqwf_description"];
            	if (!message) {
                	message = data.properties["pcmordwf_description"];
                	if (!message) {
                    	message = data.properties["expbrwwf_description"];
                    	if (!message) {
                        	message = data.properties["expusewf_description"];
                        	if (!message) {
                            	message = data.properties["hrsalwf_description"];
                        	}
                    	}
                	}
            	}
            }
            
            if (type) {
	            if(type.indexOf("Preparer") > -1){
	            	type = isThai ? "ผู้บันทึก" : "Preparer";
	            }
	            else if(type.indexOf("Requester") > -1){
	            	type = isThai ? "ผู้ขอ" : "Requester";
	            }
	            else if(type.indexOf("Approver") > -1){
	            	var p = type.indexOf("Approver");
	            	var suf = type.substring(p+8);
	            	type = isThai ? "ผู้อนุมัติขั้นที่" : "Approver";
	            	type += suf;
	            }
	            else if(type.indexOf("Consultant") > -1){
	            	type = isThai ? "ที่ปรึกษา" : "Consultant";
	            }
            }
            
            if (status) {
	            if(status.indexOf("REJECT") > -1){
	            	status = isThai ? "ไม่อนุมัติ" : "Rejected";
	            }
	            else if(status.indexOf("RESUBMIT") > -1){
	            	status = isThai ? "รอการอนุมัติ" : "Waiting for Approval";
	            }
	            else if(status.indexOf("WAPPR") > -1){
	            	status = isThai ? "รอการอนุมัติ" : "Waiting for Approval";
	            }
	            else if(status.indexOf("CONSULT") > -1){
	            	status = isThai ? "ขอคำปรึกษา" : "Consulting";
	            }
            }
            
			status ="<b>"+(isThai ? "สถานะ" : "Status")+":&nbsp;</b>"+ status;
            
            // if message is the same as the task type show the <no message> label
            if (message == type)
            {
               message = this.msg("workflow.no_message");
            }

            var href;
            if (data.isEditable)
            {
               href = $siteURL('task-edit?taskId=' + taskId + '&referrer=tasks') + '" class="theme-color-1" title="' + this.msg("title.editTask");
            }
            else
            {
               href = $siteURL('task-details?taskId=' + taskId + '&referrer=tasks') + '" class="theme-color-1" title="' + this.msg("title.viewTask");
            }

            var messageDesc = '<h3><a href="' + href + '">' + $html(message) + '</a></h3>',
               dateDesc = dueDate ? '<h4><span class="' + (today > dueDate ? "task-delayed" : "") + '" title="' + 
                          this.msg("title.dueOn", Alfresco.util.formatDate(dueDate, "longDate")) + '">' + Alfresco.util.formatDate(dueDate, "longDate") + '</span></h4>' : "",
               statusDesc = '<div title="' + this.msg("title.taskSummary", type, status) + '">' + this.msg("label.taskSummary", type, status) + '</div>',
               rejectDesc = '',
               unassignedDesc = '';
                          
            // Doy+ {
            if (dueDate) {
               rejectDesc = '<div><img src="' + Alfresco.constants.URL_RESCONTEXT + 'components/images/help-task-bw-32.png"></div>';
            }
            // Doy+ }
            
            if (!assignee || !assignee.userName)
            {
               unassignedDesc = '<span class="theme-bg-color-5 theme-color-5 unassigned-task">' + this.msg("label.unassignedTask") + '</span>';
            }
            
            // Doy+ {
            desc = messageDesc + dateDesc + statusDesc + rejectDesc + unassignedDesc + rejectDesc;
            // Doy+ }
            
//            console.log("message:" + desc);
         }
         
         elCell.innerHTML = desc;
      },

      /**
       * Actions custom datacell formatter
       */
      renderCellActions:function MyTasks_onReady_renderCellActions(elCell, oRecord, oColumn, oData)
      {
         var data = oRecord.getData(),
            desc = "";

         if (data.isInfo)
         {
            oColumn.width = 0;
            Dom.setStyle(elCell, "width", oColumn.width + "px");
            Dom.setStyle(elCell.parentNode, "width", oColumn.width + "px");
         }
         else
         {
            if (data.isEditable)
            {
               desc += '<a href="' + $siteURL('task-edit?taskId=' + data.id + '&referrer=tasks') + '" class="edit-task" title="' + this.msg("title.editTask") + '">&nbsp;</a>';
            }
            desc += '<a href="' + $siteURL('task-details?taskId=' + data.id + '&referrer=tasks') + '" class="view-task" title="' + this.msg("title.viewTask") + '">&nbsp;</a>';

            desc += '<a href="" onClick="showLightbox({ src: \'' + Alfresco.constants.PROXY_URI + "api/workflow-instances/" + data.workflowInstance.id + '/diagram\'}); return false;" class="view-diagram" title="' + this.msg("title.viewDiagram") + '">&nbsp;</a>';
         }

         elCell.innerHTML = desc;
      }

   });
})();
