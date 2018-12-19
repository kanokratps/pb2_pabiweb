(function()
{
   /**
    * YUI Library aliases
    */
   var Dom = YAHOO.util.Dom,
       Event = YAHOO.util.Event;

   /**
    * Alfresco Slingshot aliases
    */
   var $html = Alfresco.util.encodeHTML;
   
   /**
    * UserDataTable constructor.
    * 
    * @param {String} htmlId The HTML id of the parent element
    * @param {String} currentValueHtmlId The HTML id of the parent element
    * @return {Alfresco.UserDataTable} The new UserDataTable instance
    * @constructor
    */
   Alfresco.UserDataTable = function Alfresco_UserDataTable(htmlId, currentValueHtmlId, dsUrl, dsSuffix)
   {
      Alfresco.UserDataTable.superclass.constructor.call(this, "Alfresco.UserDataTable", htmlId, ["button", "container", "datasource", "datatable"]);
      this.currentValueHtmlId = currentValueHtmlId;
      this.dsUrl = dsUrl;
      this.dsSuffix = dsSuffix;
      
      return this;
   };
   
   YAHOO.extend(Alfresco.UserDataTable, Alfresco.component.Base,
   {
      /**
       * Object container for initialization options
       *
       * @property options
       * @type object
       */
      options: {},
      
      /**
       * Fired by YUILoaderHelper when required component script files have
       * been loaded into the browser.
       *
       * @method onComponentsLoaded
       */
      onComponentsLoaded: function UserDataTable_onComponentsLoaded()
      {
         if (this.id !== "null")
         {
        	 Event.onContentReady(this.id, this.onReady, this, true);
        	 //this.onReady();
         }
      },
      
      /**
       * Fired by YUI when parent element is available for scripting.
       * Component initialisation, including instantiation of YUI widgets and event listener binding.
       *
       * @method onReady
       */
      onReady: function UserDataTable_onReady()
      {
          // Variables DataTable
          // columns
          var me = this;
			var variablesColumnDefs = [
			     {
			         key:"time", label: this.msg("form.control.details.column.timeColumnHeader"), sortable:true,resizeable:true,
			         formatter:function(elCell, oRecord, oColumn, oData)
			         {
			            elCell.innerHTML = oData;
			         }
			     },{
			        key:"by", label: this.msg("form.control.details.column.userColumnHeader"), sortable:true,resizeable:true,
			        formatter:function(elCell, oRecord, oColumn, oData)
			        {
			           // instead of "hardcoding" the link to the user profile page, use uri templates
			           // <uri-templates> are loaded from share-config.xml / share-config-custom.xml.
			           // profile page is one of the default predefined uri templates.
			           var template = Alfresco.constants.URI_TEMPLATES["userprofilepage"];
			           var HTMLed_username=(oData+"");
			           var unmarked_username=(oData+"");
			
			           // don't display the link if username is null (could be an audited failed login), if the template could not be found
			           // or in portlet mode
			           if ( ! (YAHOO.lang.isUndefined(template) || template.length === 0 || Alfresco.constants.PORTLET || unmarked_username == "null"))
			           {
			              uri = Alfresco.util.uriTemplate("userprofilepage",{ userid: unmarked_username});
			              elCell.innerHTML = "<a href='" + uri + "' class='theme-color-1'>" + HTMLed_username + "</a>";
			           }
			           else
			              elCell.innerHTML = HTMLed_username;
			
			        }
			     },{
			         key:"task", label: this.msg("form.control.details.column.stateColumnHeader"), sortable:true,resizeable:true,
			         formatter:function(elCell, oRecord, oColumn, oData)
			         {
			            elCell.innerHTML = oData;
			         }
			     },{
			         key:"status", label: this.msg("form.control.details.column.actionColumnHeader"), sortable:true,resizeable:true,
			         formatter:function(elCell, oRecord, oColumn, oData)
			         {
			            elCell.innerHTML = oData;
			         }
			     },{
			         key:"comment", label: this.msg("form.control.details.column.commentColumnHeader"), sortable:true,resizeable:true,
			         formatter:function(elCell, oRecord, oColumn, oData)
			         {
			            elCell.innerHTML = oData;
			         }
			     }
			   ];          
          
          var dsFieldValue = Dom.get(this.currentValueHtmlId) ? Dom.get(this.currentValueHtmlId).value : document.getElementsByName(this.currentValueHtmlId)[0].value;
          var url = Alfresco.constants.PROXY_URI_RELATIVE + this.dsUrl + dsFieldValue + this.dsSuffix;
		  var myCallback = {
			  success: function(o) {
			        var fieldValue = o.responseText;
			        //console.log("fieldValue="+fieldValue);

					fieldValue = fieldValue.replace("'","\u0027");
					
					  var tmpData = YAHOO.lang.JSON.parse(fieldValue);
					  var grdData = tmpData.data;
					  
						  //FieldValue: array
						  me.widgets.variablesDataSource = new YAHOO.util.LocalDataSource(grdData);
						  me.widgets.variablesDataSource.responseType = YAHOO.util.XHRDataSource.TYPE_JSARRAY;
						  me.widgets.variablesDataSource.responseSchema = {
					      fields:
					      [
					         { key: "by" },
					         { key: "time" },
							 { key: "task"},
					         { key: "status"},
							 { key: "comment"},
					      ]
					   };
					
					   var dtOptions =
					   {
							/*
					      paginator: new YAHOO.widget.Paginator(
					      {
					         rowsPerPage : 100,
					         firstPageLinkLabel : this.msg("form.control.details.page.firstPageLinkLabel"),
					         previousPageLinkLabel : this.msg("form.control.details.page.previousPageLinkLabel"),
					         nextPageLinkLabel : this.msg("form.control.details.page.nextPageLinkLabel"),
					         lastPageLinkLabel : this.msg("form.control.details.page.lastPageLinkLabel"),
					         pageReportTemplate : this.msg("audit.dashlet.pageReportTemplate")
					      }),
							*/
					      //draggableColumns: true, // (optional)
					      MSG_EMPTY: me.msg("form.control.details.message.noDetails"),
					      MSG_ERROR: me.msg("form.control.details.message.detailsError")
					      
					//      caption:me.msg("form.control.details.title")
					   };
						  
						  // table
						  me.widgets.variablesDataTable = new YAHOO.widget.DataTable(me.id + "-details", 
						  variablesColumnDefs, me.widgets.variablesDataSource, dtOptions);
						  // MSG
						  me.widgets.variablesDataTable.set("MSG_EMPTY", me.msg("form.control.details.message.noDetails"));
						  me.widgets.variablesDataTable.set("MSG_ERROR", me.msg("form.control.details.message.detailsError"));
						  
						  // render the table              
						  me.widgets.variablesDataTable.render();
								
		  	  },
			  failure: function(o) { console.log("fail:"+o);},
		  };
		  var transaction = YAHOO.util.Connect.asyncRequest('GET', url, myCallback);          

      }
           
   });
})();