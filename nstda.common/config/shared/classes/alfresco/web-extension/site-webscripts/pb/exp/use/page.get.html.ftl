<#if result == "T">
<@markup id="css" >
   <#-- CSS Dependencies -->
	<@link rel="stylesheet" type="text/css" href="${url.context}/res/page/PBExpUse/resources/PBExpUse-all.css" />
	<@link rel="stylesheet" type="text/css" href="${url.context}/res/page/css/app.css" />
</@>

<@markup id="js">
   <#-- JavaScript Dependencies -->
    <script type="text/javascript">
		var TASKS = '${tasks}'
			,CURRENT_SITE = Alfresco.constants.SITE
			,ID = '${page.url.args.id?if_exists}'
			,TASK_ID = '${page.url.args.tid?if_exists}'
			;
	</script>
	<@script type="text/javascript" src="${url.context}/res/page/common/constant.js"></@script>
	<@script type="text/javascript" src="${url.context}/res/page/common/util.js"></@script>
	<@script type="text/javascript" src="${url.context}/res/page/PBExpUse/app.js"></@script>
</@>

<@markup id="widgets">
   <@createWidgets group="expUse"/>
</@>

<@markup id="html">
<div id="mainClient"></div>
</@>
</#if>
