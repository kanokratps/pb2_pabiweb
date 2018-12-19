<@markup id="css" >
   <#-- CSS Dependencies -->
		<@link rel="stylesheet" type="text/css" href="${url.context}/res/page/PBAdmin/resources/PBAdmin-all.css" />
		<@link rel="stylesheet" type="text/css" href="${url.context}/res/page/css/app_share.css" />
	
</@>
<@markup id="js">
   <#-- JavaScript Dependencies -->
	<@script type="text/javascript" src="${url.context}/res/page/common/constant.js"></@script>
	<@script type="text/javascript" src="${url.context}/res/page/common/util.js"></@script>
	<@script type="text/javascript" src="${url.context}/res/page/PBAdmin/app.js"></@script>
	<@script type="text/javascript" src="${url.context}/res/js/common/consultant.js"></@script>
</@>
<#-- Renders a hidden form field for edit and create modes only -->
<#assign fieldValue = "">
<#if field.control.params.contextProperty??>
   <#if context.properties[field.control.params.contextProperty]??>
      <#assign fieldValue = context.properties[field.control.params.contextProperty]>
   <#elseif args[field.control.params.contextProperty]??>
      <#assign fieldValue = args[field.control.params.contextProperty]>
   </#if>
<#elseif context.properties[field.name]??>
   <#assign fieldValue = context.properties[field.name]>
<#else>
   <#assign fieldValue = field.value>
</#if>

<#if form.mode == "edit" || form.mode == "create">
	<div class="form-field">
		<span id="${field.control.params.fieldName}"></span>
	</div>
   <input type="hidden" name="${field.name}" 
          <#if field.value?is_number>value="${fieldValue?c}"<#else>value="${fieldValue?html}"</#if> />
</#if>

<script type="text/javascript">

	YAHOO.util.Event.onDOMReady(function(){
	
		renderConsultant("${field.control.params.fieldName}","${fieldValue?html}","${field.name}","${field.control.params.width}","");	
		
	});

</script>
