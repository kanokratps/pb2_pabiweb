
<#include "/pb/components/form/controls/association.ftl" />
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
	<#-- qs -->
	<#assign el=fieldHtmlId>
	<#-- qs - end -->
	
	<div id="${el}-dialog" class="html-upload hidden">
         <div class="hd">
            <span id="${el}-title-span"></span>
         </div>
         <div class="bd" id="${el}-bd1">
            <p id="${el}-singleUploadTip-span">&nbsp;Click "Browse" button to select file(s) to upload.<br/>&nbsp;Use CTRL or SHIFT to select multiple file.</p>
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
               <div>
                  <div class="yui-g">
	                  <div id="${el}-file" style="width:370px;height:40px">
	                  </div>
                  </div>
               </div>
               <div class="bdft">
                  <input id="${el}-upload-button" type="button" value="${msg("button.upload")}" tabindex="0" />
                  <input id="${el}-cancel-button" type="button" value="Cancel" tabindex="0" />
               </div>
         </div>
    </div>
		
	<script type="text/javascript">//<![CDATA[
	new Alfresco.ExtUpload("${el}").setMessages(
	   ${messages}
	);

	//]]></script>
</#if>
<#-- qs - end -->

</#macro>

<@setPackageItemOptions field />