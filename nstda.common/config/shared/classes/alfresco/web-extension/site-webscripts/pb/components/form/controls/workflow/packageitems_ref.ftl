
<#include "/pb/components/form/controls/association.ftl" />
<#macro setPackageItemOptions field>

   <!--local documentLinkResolver function(item){ return Alfresco.util.siteURL("document-details?nodeRef=" + item.nodeRef, { site: item.site }); }  local-->
   <#local documentLinkResolver>function(item){ return Alfresco.constants.PROXY_URI_RELATIVE+"api/node/content/" + item.nodeRef.replace("://","/")+"/"+item.name; }</#local>
   <#local allowAddAction = false>
   <#local allowRemoveAllAction = false>
   <#-- qs
   <#local allowRemoveAction = false>
   -->
   <#-- qs -->
   <#local allowRemoveAction = false>
   <#local allowUploadAction = false>
   <#-- qs - end -->
   <#local allowUploadNewAction = false>
   <#local allowWatermarkAction = false>
   <#local allowDownloadAction = false>
   <#local allowGenDocAction = false>

   <#assign buttons="">
   <#if field.control.params.buttons??>
	<#local buttons = field.control.params.buttons>	
   </#if>
   
   <#assign targetFolder="">
   <#if form.data['prop_pcmreqwf_folderRef']??>
   		<#assign targetFolder=form.fields["prop_pcmreqwf_folderRef"].value>
   </#if>
   <#if form.data['prop_pcmordwf_folderRef']??>
   		<#assign targetFolder=form.fields["prop_pcmordwf_folderRef"].value>
   </#if>
   <#if form.data['prop_cpwf_folderRef']??>
   		<#assign targetFolder=form.fields["prop_cpwf_folderRef"].value>
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
   
   <#assign urdFolder="">
   <#if form.data['prop_sd_urdFolderNodeRef']??>
	<#assign urdFolder=form.fields["prop_sd_urdFolderNodeRef"].value>
   </#if>

   <#assign designFolder="">
   <#if form.data['prop_sd_designFolderNodeRef']??>
	<#assign designFolder=form.fields["prop_sd_designFolderNodeRef"].value>
   </#if>
   
   <#assign constructionFolder="">
   <#if form.data['prop_sd_constructionFolderNodeRef']??>
	<#assign constructionFolder=form.fields["prop_sd_constructionFolderNodeRef"].value>
   </#if>
   
   <#assign trainingFolder="">
   <#if form.data['prop_sd_trainingFolderNodeRef']??>
	<#assign trainingFolder=form.fields["prop_sd_trainingFolderNodeRef"].value>
   </#if>
	
   <#assign uatFolder="">
   <#if form.data['prop_sd_uatFolderNodeRef']??>
	<#assign uatFolder=form.fields["prop_sd_uatFolderNodeRef"].value>
   </#if>
   
   <#assign deploymentFolder="">
   <#if form.data['prop_sd_deploymentFolderNodeRef']??>
	<#assign deploymentFolder=form.fields["prop_sd_deploymentFolderNodeRef"].value>
   </#if>

   <#assign goliveFolder="">
   <#if form.data['prop_sd_goliveFolderNodeRef']??>
	<#assign goliveFolder=form.fields["prop_sd_goliveFolderNodeRef"].value>
   </#if>

   <#assign postImpFolder="">
   <#if form.data['prop_sd_postFolderNodeRef']??>
	<#assign postImpFolder=form.fields["prop_sd_postFolderNodeRef"].value>
   </#if>

   <#assign srcNodeRef="">
   <#if form.data["${fieldID}"]??>
	<#assign srcNodeRef=form.fields["${fieldID}"].value>
   </#if>
	
   <#assign pdfTempFolderNodeRef="">
   <#if form.data['prop_isowf_pdfNodeRef']??>
	<#assign pdfTempFolderNodeRef=form.fields["prop_isowf_pdfNodeRef"].value>
   </#if>
	
   <#assign docNumber="">
   <#if form.data['prop_isowf_docNumber']??>
	<#assign docNumber=form.fields["prop_isowf_docNumber"].value>
   </#if>
   
   <#assign effectiveDate="">
   <#if form.data['prop_isowf_effectiveDate']??>
	<#assign effectiveDate=form.fields["prop_isowf_effectiveDate"].value>
   </#if>
   

   <#local actions = []>

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
   
   <#assign removeAction="">
   <#if field.control.params.removeAction??>
	 <#local allowRemoveAction = field.control.params.removeAction>
   </#if>
   
   <#if form.data['prop_bpm_packageItemActionGroup']?? && form.data['prop_bpm_packageItemActionGroup']?is_string && form.data['prop_bpm_packageItemActionGroup']?length &gt; 0>
      <#local packageItemActionGroup = form.data['prop_bpm_packageItemActionGroup']>
      <#local viewMoreAction = { "name": "view_more_actions", "label": "form.control.object-picker.workflow.view_more_actions", "link": documentLinkResolver }>
      <#if packageItemActionGroup == "read_package_item_actions" || packageItemActionGroup == "edit_package_item_actions">
         <#local actions = actions + [viewMoreAction]>
      <#elseif packageItemActionGroup == "remove_package_item_actions" || packageItemActionGroup == "start_package_item_actions" || packageItemActionGroup == "edit_and_remove_package_item_actions">
         <#local actions = actions + [viewMoreAction]>
         <#local allowRemoveAllAction = true>
         <#local allowRemoveAction = true>
      <#elseif packageItemActionGroup >
      <#else>
         <#local actions = actions + [viewMoreAction]>      
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
         selectActionLabel: "${field.control.params.selectActionLabel!msg("button.add")}",
		 controlId:"${fieldHtmlId}",
		 pdFolder:"${pdFolder}",
		 urdFolder:"${urdFolder}",
		 designFolder:"${designFolder}",
		 constructionFolder:"${constructionFolder}",
		 trainingFolder:"${trainingFolder}",
		 uatFolder:"${uatFolder}",
		 deploymentFolder:"${deploymentFolder}",
		 goliveFolder:"${goliveFolder}",
		 srcNodeRef:"${srcNodeRef}",
		 pdfTempFolderNodeRef:"${pdfTempFolderNodeRef}",
		 docNumber:"${docNumber}",
		 effectiveDate:"${effectiveDate}",
	 <#if field.control.params.targetFolder??>
		targetFolder:"${field.control.params.targetFolder}",
	 </#if>
	 <#if targetFolder??>
		targetFolder:"${targetFolder}",
	 </#if>

	 postImpFolder:"${postImpFolder}"

      });
      
      	var v = '${field.value}';
   		var c = document.getElementById("${controlId}");
   		if (!v) {
	   		var set = c.parentNode.parentNode.parentNode.parentNode;
	   		set.style.display = 'none';
   		}
      
   })();
   //]]></script>
   
<#-- qs -->
<#if allowUploadAction == true || allowUploadNewAction == true || allowWatermarkAction == true>
	<#-- qs <#assign el=args.htmlid?html> -->
	<#-- qs -->
	<#assign el=fieldHtmlId>
	<#-- qs - end -->
	
	<#-- lt added -->
	<#assign contentTypes = [{"id":"cm:content","value":"cm_content"}]>

	<div id="${el}-dialog" class="flash-upload hidden">
	   <div class="hd">
		  <span id="${el}-title-span"></span>
	   </div>
	   <div class="bd">
		  <div class="browse-wrapper">
			 <div class="center">
				<div id="${el}-flashuploader-div" class="browse"></div>
				<div class="label">Select File to Upload</div>
			 </div>
		  </div>
		  <div class="tip-wrapper">
			 <span id="${el}-multiUploadTip-span">${msg("label.multiUploadTip")}</span>
			 <span id="${el}-singleUpdateTip-span">${msg("label.singleUpdateTip")}</span>
		  </div>

		  <div id="${el}-filelist-table" class="fileUpload-filelist-table"></div>

		  <div class="status-wrapper">
			 <span id="${el}-status-span" class="status"></span>
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
				   <input id="${el}-minorVersion-radioButton" type="radio" name="majorVersion" checked="checked" tabindex="0"/>
				   <label for="${el}-minorVersion-radioButton" id="${el}-minorVersion">${msg("label.minorVersion")}</label>
				</div>
			 </div>
			 <div class="yui-gd">
				<div class="yui-u first">&nbsp;
				</div>
				<div class="yui-u">
				   <input id="${el}-majorVersion-radioButton" type="radio" name="majorVersion" tabindex="0"/>
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

		  <!-- Templates for a file row -->
		  <div style="display:none">
			 <div id="${el}-left-div" class="fileupload-left-div">
				<span class="fileupload-percentage-span hidden">&nbsp;</span>
				<#if (contentTypes?size == 1)>
				<input class="fileupload-contentType-input" type="hidden" value="${contentTypes[0].id}"/>
				<#elseif (contentTypes?size > 1)>
				<select class="fileupload-contentType-select" tabindex="0">
				   <#if (contentTypes?size > 0)>
					  <#list contentTypes as contentType>
						 <option value="${contentType.id}">${msg(contentType.value)}</option>
					  </#list>
				   </#if>
				</select>
				</#if>
			 </div>
			 <div id="${el}-center-div" class="fileupload-center-div">
				<span class="fileupload-progressSuccess-span">&nbsp;</span>
				<img src="${url.context}/res/components/images/generic-file-32.png" class="fileupload-docImage-img" alt="file" />
				<span class="fileupload-progressInfo-span"></span>
			 </div>
			 <div id="${el}-right-div" class="fileupload-right-div">
				<span class="fileupload-fileButton-span">
				   <button class="fileupload-file-button" value="DDDDDDDDDD" disabled="true" tabindex="0">${msg("button.remove")}</button>
				</span>
			 </div>
		  </div>
			 <div class="bdft">
				<input id="${el}-upload-button" type="button" value="AAAAA1" tabindex="0"/>
				<input id="${el}-cancelOk-button" type="button" value="BBBBB2" tabindex="0"/>
			 </div>
	   </div>
	</div>
	<script type="text/javascript">//<![CDATA[
	new Alfresco.FlashUpload("${el}").setMessages(
	   ${messages}
	);
	//]]></script>
</#if>
<#-- qs - end -->

</#macro>

<@setPackageItemOptions field />
