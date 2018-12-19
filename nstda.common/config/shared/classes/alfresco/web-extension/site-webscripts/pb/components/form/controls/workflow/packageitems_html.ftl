
<#include "/pb/components/form/controls/association.ftl" />
<#macro setPackageItemOptions field>

   <#local documentLinkResolver>function(item){ return Alfresco.constants.PROXY_URI_RELATIVE+"api/node/content/" + item.nodeRef.replace("://","/")+"/"+item.name; }</#local>
   <#local allowAddAction = false>
   <#local allowRemoveAllAction = false>
   <#-- qs -->
   <#local allowRemoveAction = false>
   <#local allowUploadAction = false>
   <#-- qs - end -->
   <#local allowUploadNewAction = false>
   <#local allowWatermarkAction = false>
   <#local allowDownloadAction = false>
   <#local allowGenDocAction = false>
   <#local allowEditDescAction = false>

   <#assign buttons="">
   <#if field.control.params.buttons??>
	<#local buttons = field.control.params.buttons>	
   </#if>
   
   <#assign moreAction="">
   <#if field.control.params.moreAction??>
	<#local moreAction = field.control.params.moreAction>
   </#if>   
   
   <#assign targetFolder="">
   <#if form.data['prop_pcmreqwf_folderRef']??>
   		<#assign targetFolder=form.fields["prop_pcmreqwf_folderRef"].value>
   </#if>
   <#if form.data['prop_pcmordwf_folderRef']??>
   		<#assign targetFolder=form.fields["prop_pcmordwf_folderRef"].value>
   </#if>
   <#if form.data['prop_expbrwwf_folderRef']??>
   		<#assign targetFolder=form.fields["prop_expbrwwf_folderRef"].value>
   </#if>
   <#if form.data['prop_expusewf_folderRef']??>
   		<#assign targetFolder=form.fields["prop_expusewf_folderRef"].value>
   </#if>
   <#if form.data['prop_hrsalwf_folderRef']??>
   		<#assign targetFolder=form.fields["prop_hrsalwf_folderRef"].value>
   </#if>
   
   <#assign fieldID="">
   <#if field.control.params.fieldID??>
	<#assign fieldID = field.control.params.fieldID>	
   </#if>
   
   
   <#assign docname="">
   <#if form.data['prop_sd_projectName']??>
	<#assign docname=form.fields["prop_sd_projectName"].value>
   </#if>
   
   <#assign pdFolder="">
   <#if form.data['prop_sd_pdFolderNodeRef']??>
	<#assign pdFolder=form.fields["prop_sd_pdFolderNodeRef"].value>
   </#if>
   
   <#assign srcNodeRef="">
   <#if form.data["${fieldID}"]??>
	<#assign srcNodeRef=form.fields["${fieldID}"].value>
   </#if>
	
   <#if form.data['prop_bpm_packageActionGroup']?? && form.data['prop_bpm_packageActionGroup']?is_string && form.data['prop_bpm_packageActionGroup']?length &gt; 0>
      <#local allowAddAction = true>
   </#if>

   <#if buttons?contains("u")>
   		<#local allowUploadAction = true>
   </#if>
   
   <#if buttons?contains("!u")>
   		<#local allowUploadAction = false>
   </#if>
   <#if buttons?contains("n")>
   		<#local allowUploadNewAction = true>
   </#if>
   
   <#if buttons?contains("w")>
   		<#local allowWatermarkAction = true>
   </#if>
   
   <#if buttons?contains("d")>
   		<#local allowDownloadAction = true>
   </#if>
   
   <#if buttons?contains("g")>
   		<#local allowGenDocAction = true>
   </#if>
   
   <#if buttons?contains("e")>
   		<#local allowEditDescAction = true>
   </#if>
   
   <#local actions = []>
   
   <#assign removeAction="">
   <#if field.control.params.removeAction??>
	 <#local allowRemoveAction = field.control.params.removeAction>
   </#if>
   
   <#if form.data['prop_bpm_packageItemActionGroup']?? && form.data['prop_bpm_packageItemActionGroup']?is_string && form.data['prop_bpm_packageItemActionGroup']?length &gt; 0>
      <#local packageItemActionGroup = form.data['prop_bpm_packageItemActionGroup']>
      <#if moreAction=="true">
      	<#local viewMoreAction = [{ "name": "view_more_actions", "label": "form.control.object-picker.workflow.view_more_actions", "link": documentLinkResolver }]>
      <#else>
      	<#local viewMoreAction = []>
      </#if>
      <#if packageItemActionGroup == "read_package_item_actions" || packageItemActionGroup == "edit_package_item_actions">
         <#local actions = actions + viewMoreAction>
      <#elseif packageItemActionGroup == "remove_package_item_actions" || packageItemActionGroup == "start_package_item_actions" || packageItemActionGroup == "edit_and_remove_package_item_actions">
         <#local actions = actions + viewMoreAction>
         <#local allowRemoveAllAction = true>
         <#local allowRemoveAction = true>
      <#elseif packageItemActionGroup >
      <#else>
         <#local actions = actions + viewMoreAction>      
      </#if>
   </#if>

   <#-- Additional item actions -->

   <script type="text/javascript">//<![CDATA[
   (function()
   {
      <#-- Modify the properties on the object finder created by association control-->
      var picker = Alfresco.util.ComponentManager.get("${controlId}");

      picker.setOptions(
      {
         showLinkToTarget: true,
         targetLinkTemplate: ${documentLinkResolver},
      <#if form.mode == "create" && form.destination?? && form.destination?length &gt; 0>
         startLocation: "${form.destination?js_string}",
      <#elseif field.control.params.startLocation??>
         startLocation: "${field.control.params.startLocation?js_string}",
      </#if>
         itemType: "cm:content",
         displayMode: "${field.control.params.displayMode!"list"}",
         listItemActions: [
         <#list actions as action>
         {
            name: "${action.name}",
            <#if action.link??>
            link: ${action.link},
            <#elseif action.event>
            event: "${action.event}", 
            </#if>
            label: "${action.label}"
         }<#if action_has_next>,</#if>
         </#list>],
         allowRemoveAction: ${allowRemoveAction?string},
         allowRemoveAllAction: ${allowRemoveAllAction?string},
         allowSelectAction: ${allowAddAction?string},
	 <#-- qs -->
	 allowUploadAction: ${allowUploadAction?string},
	 allowUploadNewAction: ${allowUploadNewAction?string},
	 allowWatermarkAction: ${allowWatermarkAction?string},
	 allowDownloadAction: ${allowDownloadAction?string},
	 allowGenDocAction: ${allowGenDocAction?string},
	 allowEditDescAction: ${allowEditDescAction?string},
         selectActionLabel: "${field.control.params.selectActionLabel!msg("button.add")}",
		 pdFolder:"${pdFolder}",
		 srcNodeRef:"${srcNodeRef}",
	 <#if field.control.params.targetFolder??>
		targetFolder:"${field.control.params.targetFolder}",
	 </#if>
	 <#if targetFolder??>
		targetFolder:"${targetFolder}",
		destination:"${targetFolder}",
	 </#if>
		 controlId:"${fieldHtmlId}"

      });
   })();
   //]]></script>
   
<#-- qs -->
<#if allowUploadAction == true || allowUploadNewAction == true || allowWatermarkAction == true>
	<#-- qs <#assign el=args.htmlid?html> -->
	<#-- qs -->
	<#assign el=fieldHtmlId>
	<#-- qs - end -->
	
	<#assign contentTypes = [{"id":"cm:content","value":"cm_content"}]>
	
	<div id="${el}-dialog" class="html-upload hidden">
         <div class="hd">
            <span id="${el}-title-span"></span>
         </div>
         <div class="bd" id="${el}-bd1">
            <p id="${el}-singleUploadTip-span">${msg("label.singleUploadTip")}</p>
            <p id="${el}-singleUpdateTip-span">${msg("label.singleUpdateTip")}</p>
            <script type="text/javascript">
               <#assign callback>onAlfrescoHtmlUploadComponent_${el?replace("-", "__")}</#assign>
               var ${callback}_success = function(response)
               {
                  var component = Alfresco.util.ComponentManager.get('${el}');
                  component.onUploadSuccess.call(component, response)
               };
               var ${callback}_failure= function(response)
               {
                  var component = Alfresco.util.ComponentManager.get('${el}');
                  component.onUploadFailure.call(component, response)
               };
            </script>
            <form id="${el}-htmlupload-form"
                  method="post" enctype="multipart/form-data" accept-charset="utf-8"
                  action="${url.context}/proxy/alfresco/api/upload.html">
               <fieldset id="${el}-fs">
               <input type="hidden" id="${el}-siteId-hidden" name="siteId" value=""/>
               <input type="hidden" id="${el}-containerId-hidden" name="containerId" value=""/>
               <input type="hidden" id="${el}-destination-hidden" name="destination" value=""/>
               <input type="hidden" id="${el}-username-hidden" name="username" value=""/>
               <input type="hidden" id="${el}-updateNodeRef-hidden" name="updateNodeRef" value=""/>
               <input type="hidden" id="${el}-uploadDirectory-hidden" name="uploadDirectory" value=""/>
               <input type="hidden" id="${el}-overwrite-hidden" name="overwrite" value=""/>
               <input type="hidden" id="${el}-thumbnails-hidden" name="thumbnails" value=""/>
               <input type="hidden" name="success" value="window.parent.${callback}_success"/>
               <input type="hidden" name="failure" value="window.parent.${callback}_failure"/>
               <div>
                  <div class="yui-g">
                     <h2>${msg("section.file")}</h2>
                  </div>
                  <div class="yui-gd <#if (contentTypes?size == 1)>hidden</#if>">
                     <div class="yui-u first">
                        <label for="${el}-contentType-select">${msg("label.contentType")}</label>
                     </div>
                     <div class="yui-u">
                        <select id="${el}-contentType-select" name="contentType" tabindex="0">
                           <#if (contentTypes?size > 0)>
                              <#list contentTypes as contentType>
                                 <option value="${contentType.id}">${msg(contentType.value)}</option>
                              </#list>
                           </#if>
                        </select>
                     </div>
                  </div>
                  <div class="yui-gd">
                     <div class="yui-u first">
                        <label for="${el}-filedata-file">${msg("label.file")}</label>
                     </div>
                     <div class="yui-u">
                        <input type="file" id="${el}-filedata-file" name="filedata" tabindex="0" />
                     </div>
                  </div>
               </div>
               <div id="${el}-versionSection-div">
                  <div class="yui-g">
                     <h2>${msg("section.version")}</h2>
                  </div>
                  <div class="yui-gd">
                     <div class="yui-u first">
                        <span>${msg("label.version")}</span>
                     </div> 
                     <div class="yui-u">
                        <input id="${el}-minorVersion-radioButton" type="radio" name="majorVersion" checked="checked" value="false" tabindex="0" />
                        <label for="${el}-minorVersion-radioButton" id="${el}-minorVersion">${msg("label.minorVersion")}</label>
                     </div>
                  </div>
                  <div class="yui-gd">
                     <div class="yui-u first">&nbsp;
                     </div>
                     <div class="yui-u">
                        <input id="${el}-majorVersion-radioButton" type="radio" name="majorVersion" value="true" tabindex="0" />
                        <label for="${el}-majorVersion-radioButton" id="${el}-majorVersion">${msg("label.majorVersion")}</label>
                     </div>
                  </div>
                  <div class="yui-gd">
                     <div class="yui-u first">
                        <label for="${el}-description-textarea">${msg("label.comments")}</label>
                     </div>
                     <div class="yui-u">
                        <textarea id="${el}-description-textarea" name="description" cols="80" rows="4" tabindex="0"></textarea>
                     </div>
                  </div>
               </div>
               <div class="bdft">
                  <input id="${el}-upload-button" type="button" value="${msg("button.upload")}" tabindex="0" />
                  <input id="${el}-cancel-button" type="button" value="${msg("button.cancel")}" tabindex="0" />
               </div>
               </fieldset>
               </form>
         </div>
      </div>
		
	<script type="text/javascript">//<![CDATA[
	new Alfresco.HtmlUpload("${el}").setMessages(
	   ${messages}
	);
	//]]></script>
</#if>
<#-- qs - end -->

</#macro>

<@setPackageItemOptions field />
