<link rel="stylesheet" type="text/css" href="${url.context}/yui/datatable/assets/skins/sam/datatable.css" />
<@markup id="css" >
   <#-- CSS Dependencies -->
		<@link rel="stylesheet" type="text/css" href="${url.context}/res/page/PBAdmin/resources/PBAdmin-all.css" />
	
</@>
<@markup id="js">
   <#-- JavaScript Dependencies -->
	<@script type="text/javascript" src="${url.context}/res/page/common/constant.js"></@script>
	<@script type="text/javascript" src="${url.context}/res/page/common/util.js"></@script>
	<@script type="text/javascript" src="${url.context}/res/page/PBAdmin/app.js"></@script>
</@>
<style>
#oImg:hover {
    cursor: pointer;
}
</style>
<#if (RequestParameters.taskId?has_content)>
<#assign taskId = RequestParameters.taskId/>
</#if>
<#assign controlId = fieldHtmlId + "-cntrl"/>
<#if field.control.params.ds?exists><#assign cols=field.control.params.cols><#else><#assign cols=''></#if>
<#if field.control.params.dsUrl?exists><#assign dsUrl=field.control.params.dsUrl><#else><#assign dsUrl=''></#if>

<div class="form-field">
   <#if form.mode == "view">
      <div class="viewmode-field">
         <#if field.mandatory && !(field.value?is_number) && field.value == "">
            <span class="incomplete-warning"><img src="${url.context}/res/components/form/images/warning-16.png" title="${msg("form.field.incomplete")}" /><span>
         </#if>
         <span class="viewmode-label">${field.label?html}:</span>
         <#if field.control.params.activateLinks?? && field.control.params.activateLinks == "true">
            <#assign fieldValue=field.value?html?replace("((http|ftp|https):\\/\\/[\\w\\-_]+(\\.[\\w\\-_]+)+([\\w\\-\\.,@?\\^=%&:\\/~\\+#]*[\\w\\-\\@?\\^=%&\\/~\\+#])?)", "<a href=\"$1\" target=\"_blank\">$1</a>", "r")>
         <#else>
            <#if field.value?is_number>
               <#assign fieldValue=field.value?c>
            <#else>
               <#assign fieldValue=field.value?html>
            </#if>
         </#if>
         <span class="viewmode-value"><#if fieldValue == "">${msg("form.control.novalue")}<#else>
		      <div class="details form-field" id="${controlId}">
			  <div class="details-datatable" id="${controlId}-details">
			  </div>
			  <input id="${fieldHtmlId}-h" name="${field.name}" type="hidden" value="${fieldValue?html}"/>
		      </div>
         </#if></span>
      </div>
   <#else>
      <label for="${fieldHtmlId}">${field.label?html}:<#if field.mandatory><span class="mandatory-indicator">${msg("form.required.fields.marker")}</span></#if></label>
      <#if field.value == ""><!--empty--><#else>
      <span class="viewmode-value">
		      <div class="details form-field" id="${controlId}">
			  <div class="details-datatable" id="${controlId}-details">
			  </div>
			  <input id="${fieldHtmlId}-h" name="${field.name}" type="hidden" value="${field.value?html}"/>
		      </div>
      </#if></span>
      <@formLib.renderFieldHelp field=field />
   </#if>
</div>

<script type="text/javascript">

YAHOO.util.Event.onDOMReady(function(){
   new Alfresco.UserDataTable("${controlId}", "${fieldHtmlId}-h", "${dsUrl}").setMessages(${messages});
   			
    	var div = document.getElementById('ext-edit-btn');
    	var oImg=document.createElement("div");
		oImg.setAttribute('id','oImg');
		oImg.setAttribute('alt', 'edit');
		oImg.style.height= '15px';
		oImg.style.width= '15px';
		oImg.style.backgroundImage = "url('../res/page/img/icon/edit.png')";
    	div.appendChild(oImg);
    	
    	Ext.widget('linkbutton', {
                renderTo: 'oImg',
                handler: function () { 
                	var hid = document.getElementsByName("prop_pcmreqwf_id")[0];
//					if (!hid) alert("Test");
					var pf = hid.value.substring(0,2);
					var u = {
						"PR":"pcm-req",
						"PD":"pcm-ord",
						"AV":"exp-brw",
						"AP":"exp-use",
						"HR":"hr-sal"
					}
                	window.location = "${url.context}/page/"+u[pf]+"?id="+hid.value+"&tid=${taskId}";
                }
        });
		
   		var myReferrer = window.location.origin+Alfresco.constants.URL_PAGECONTEXT+"user/"+Alfresco.constants.USERNAME+"/dashboard";
		window.document.__defineGetter__('referrer', function () {
			return myReferrer;
		});
			   		
});

</script>