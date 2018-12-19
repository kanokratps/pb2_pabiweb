<#escape x as jsonUtils.encodeJSONString(x)>
{
   <#if document.nodeRef??>
	   "nodeRef": "${document.nodeRef}",
	   "fileName": "${document.name}",
   <#else>
	   <#-- qs -->
	   "nodeRef":"${custom.nodeRef}",
	   "fileName": "${custom.fileName}",
	   "creator": "${custom.creator}",
	   "modified": "${xmldate(custom.modified)}",
	   "modifier": "${custom.modifier}",
	   "displayPath": "${custom.displayPath}",
	   "isContainer": ${custom.isContainer?string},
	   "type": "${custom.type}",
	   "title": "${custom.title}",
	   "description": "${custom.description}",
	   "selectable": ${custom.selectable?string},
	   <#-- qs - end -->	
   </#if>

   
   
   "status":
   {
      "code": 200,
      "name": "OK",
      "description": "File uploaded successfully"
   }
}
</#escape>