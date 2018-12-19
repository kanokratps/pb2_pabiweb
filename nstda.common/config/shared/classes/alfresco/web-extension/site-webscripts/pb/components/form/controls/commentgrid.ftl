<link rel="stylesheet" type="text/css" href="${url.context}/yui/datatable/assets/skins/sam/datatable.css" />
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
<#if field.control.params.pbmodule?exists><#assign pbmodule=field.control.params.pbmodule><#else><#assign pbmodule=''></#if>
<#if field.control.params.dsFieldValue?exists><#assign dsFieldValue=field.control.params.dsFieldValue><#else><#assign dsFieldValue=''></#if>

<div class="form-field">
   <#if form.mode == "view">
      <div class="viewmode-field">
         <#if field.mandatory && !(field.value?is_number) && field.value == "">
            <span class="incomplete-warning"><img src="${url.context}/res/components/form/images/warning-16.png" title="${msg("form.field.incomplete")}" /><span>
         </#if>
         <#if field.control.params.showLabel?? && field.control.params.showLabel == "true">
         	<span class="viewmode-label">${field.label?html}:</span>
         </#if>
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
			  <!--input id="${fieldHtmlId}-h" name="${field.name}" type="hidden" value="${fieldValue?html}"/-->
		      </div>
         </#if></span>
      </div>
   <#else>
      <#if field.control.params.showLabel?? && field.control.params.showLabel == "true">
      	<label for="${fieldHtmlId}">${field.label?html}:<#if field.mandatory><span class="mandatory-indicator">${msg("form.required.fields.marker")}</span></#if></label>
      </#if>
      <#if field.value == ""><!--empty--><#else>
      <span class="viewmode-value">
		      <div class="details form-field" id="${controlId}">
			  <div class="details-datatable" id="${controlId}-details">
			  </div>
			  <!--input id="${fieldHtmlId}-h" name="${field.name}" type="hidden" value="${field.value?html}"/-->
		      </div>
      </#if></span>
      <@formLib.renderFieldHelp field=field />
   </#if>
</div>

<script type="text/javascript">

YAHOO.util.Event.onDOMReady(function(){
   new Alfresco.UserDataTable("${controlId}", "${dsFieldValue}", "${dsUrl}", "&lang="+getLang()).setMessages(${messages});
   			/*
   		var divs = document.getElementsByTagName("div");
    	for(var i = 0; i < divs.length; i++){
    		if(divs[i].className.indexOf('ext-edit-btn') >-1){
    			var element = document.createElement("input");
    			element.type = "button";
				element.value = "Edit";
				//element.appendChild(document.createTextNode('The man who mistook his wife for a hat'));
				//divs[i].getElementsByClassName("bar")[0]
				//divs[i].style.width = "50%";
				divs[i].appendChild(element);
    			//divs[i].style.width=(WIDTH-W_OFFSET)+'px';
    			break;
    		}
    	}
    	*/
    	
   		var myReferrer = window.location.origin+Alfresco.constants.URL_PAGECONTEXT+"user/"+Alfresco.constants.USERNAME+"/dashboard";
		window.document.__defineGetter__('referrer', function () {
			return myReferrer;
		});
    	
    	var div = document.getElementById('ext-edit-btn');
    	if (div) {
	    	var oImg=document.createElement("img");
			oImg.setAttribute('src', '../res/page/img/icon/edit.png');
			oImg.setAttribute('id','oImg');
			oImg.setAttribute('alt', 'edit');
			oImg.style.height= '15px';
			oImg.style.width= '15px';
			oImg.onclick = function () {
				var pbmodule = "${pbmodule}";
				console.log("pbmodule:"+pbmodule);
				var pbmodule2 = pbmodule.replace("-","");
				console.log("pbmodule2:"+pbmodule2);
			
				var hid = document.getElementsByName("prop_"+pbmodule2+"wf_id")[0];
				if (!hid) alert("Test");
//				var pf = hid.value.substring(0,2);
//				var u = {
//					"PR":"pcm-req",
//					"PD":"pcm-ord",
//					"AV":"exp-brw",
//					"AP":"exp-use"
//				}
	    		window.location = "${url.context}/page/"+pbmodule+"?id="+hid.value+"&tid=${taskId}";
			};
			div.appendChild(oImg);                    // Append <button> to <body>
		}
});

</script>