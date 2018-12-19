(function(){var g=YAHOO.util.Dom,l=YAHOO.util.Event,a=YAHOO.util.Selector;var d=Alfresco.util.encodeHTML,f=Alfresco.util.siteURL;Alfresco.dashlet.MyTasks=function e(m){Alfresco.dashlet.MyTasks.superclass.constructor.call(this,"Alfresco.dashlet.MyTasks",m,["button","container","datasource","datatable","paginator","history","animation"]);this.services.preferences=new Alfresco.service.Preferences();return this};YAHOO.extend(Alfresco.dashlet.MyTasks,Alfresco.component.Base);YAHOO.lang.augmentProto(Alfresco.dashlet.MyTasks,Alfresco.action.WorkflowActions);YAHOO.lang.augmentObject(Alfresco.dashlet.MyTasks.prototype,{PREFERENCES_TASKS_DASHLET:"",PREFERENCES_TASKS_DASHLET_FILTER:"",options:{hiddenTaskTypes:[],maxItems:50,filters:{}},onReady:function k(){this.widgets.filterMenuButton=Alfresco.util.createYUIButton(this,"filters",this.onFilterSelected,{type:"menu",menu:"filters-menu",lazyloadmenu:false});this.widgets.searchButton=Alfresco.util.createYUIButton(this,"search",this.onSearchPressed,{});this.PREFERENCES_TASKS_DASHLET=this.services.preferences.getDashletId(this,"tasks");this.PREFERENCES_TASKS_DASHLET_FILTER=this.PREFERENCES_TASKS_DASHLET+".filter";var x=this.services.preferences.get();var n=Alfresco.util.findValueByDotNotation(x,this.PREFERENCES_TASKS_DASHLET_FILTER,"activeTasks");n=this.options.filters.hasOwnProperty(n)?n:"activeTasks";this.widgets.filterMenuButton.set("label",this.msg("filter."+n));this.widgets.filterMenuButton.value=n;g.removeClass(a.query(".toolbar div",this.id,true),"hidden");this.widgets.filterMenuButton.set("disabled",true);var q=a.query(".toolbar span",this.id,false);for(var u in q){if(q[u]["className"]=="first-child"&&q[u]["innerHTML"].indexOf("my-task")>=0){g.addClass(q[u],"hidden")}else{if(q[u]["innerHTML"]=="|"){g.addClass(q[u],"hidden")}}}var p=YAHOO.lang.substitute("api/task-instances?authority={authority}&properties={properties}&exclude={exclude}",{authority:encodeURIComponent(Alfresco.constants.USERNAME),properties:["bpm_priority","bpm_status","bpm_dueDate","bpm_description","pcmreqwf_description","pcmordwf_description","expbrwwf_description","expusewf_description","hrsalwf_description"].join(","),exclude:this.options.hiddenTaskTypes.join(",")});this.widgets.alfrescoDataTable=new Alfresco.util.DataTable({dataSource:{url:Alfresco.constants.PROXY_URI+p,filterResolver:this.bind(function(){var y=this.widgets.filterMenuButton.value;var z=this.options.filters[y];return this.substituteParameters(z)||""})},dataTable:{container:this.id+"-tasks",columnDefinitions:[{key:"isPooled",sortable:false,formatter:this.bind(this.renderCellIcons),width:24},{key:"title",sortable:false,formatter:this.bind(this.renderCellTaskInfo)}],config:{MSG_EMPTY:this.msg("message.noTasks")}},paginator:{history:false,hide:false,config:{containers:[this.id+"-paginator"],template:"{FirstPageLink} {PreviousPageLink} {CurrentPageReport} {NextPageLink} {LastPageLink}",firstPageLinkLabel:"&lt;&lt;",previousPageLinkLabel:"&lt;",nextPageLinkLabel:"&gt;",lastPageLinkLabel:"&gt;&gt;",pageReportTemplate:this.msg("pagination.template.page-report"),rowsPerPage:this.options.maxItems}}});var m=Alfresco.constants.PROXY_URI_RELATIVE+"pb/util/flashMsg";var w={success:function(B){var A=JSON.parse(B.responseText);if(A.data.msg){mySimpleDialog=new YAHOO.widget.SimpleDialog("dlg",{width:"20em",height:"10em",effect:{effect:YAHOO.widget.ContainerEffect.FADE,duration:0.25},fixedcenter:true,modal:true,visible:false,draggable:false,close:false});mySimpleDialog.setHeader("Warning!");mySimpleDialog.setBody(A.data.msg);var z=function(){this.hide()};var y=[{text:"OK",handler:z}];mySimpleDialog.cfg.queueProperty("buttons",y);mySimpleDialog.render(document.body);mySimpleDialog.show()}},failure:function(y){alert("failure")},};/*var r=YAHOO.util.Connect.asyncRequest("GET",m,w);*/var t=this,o=this.widgets.alfrescoDataTable.getDataTable(),s=o.doBeforeLoadData;o.doBeforeLoadData=function v(y,z,A){if(z.results.length===0){g.addClass(this.configs.paginator.getContainerNodes(),"hidden")}else{g.removeClass(this.configs.paginator.getContainerNodes(),"hidden")}if(z.results.length===0){z.results.unshift({isInfo:true,title:t.msg("empty.title"),description:t.msg("empty.description")})}return s.apply(this,arguments)}},onSearchPressed:function h(o,n){var p=this.widgets.filterMenuButton.value;var q=this.substituteParameters(this.options.filters[p],{});var m=document.getElementById("searchTermTask");q+="&name="+m.value;this.widgets.alfrescoDataTable.loadDataTable(q)},onFilterSelected:function i(n,m){var o=m[1];if(o){this.widgets.alfrescoDataTable.currentSkipCount=0;this.widgets.filterMenuButton.set("label",o.cfg.getProperty("text"));this.widgets.filterMenuButton.value=o.value;var p=this.substituteParameters(this.options.filters[o.value],{});this.widgets.alfrescoDataTable.loadDataTable(p);this.services.preferences.set(this.PREFERENCES_TASKS_DASHLET_FILTER,o.value)}},renderCellIcons:function c(v,w,s,m){var p=w.getData(),r="";if(p.isInfo){s.width=52;g.setStyle(v,"width",s.width+"px");g.setStyle(v.parentNode,"width",s.width+"px");r='<img src="'+Alfresco.constants.URL_RESCONTEXT+'components/images/help-task-bw-32.png" />'}else{var u=p.properties.bpm_priority,t={"1":"high","2":"medium","3":"low"},o=t[u+""],q=p.isPooled;r='<img src="'+Alfresco.constants.URL_RESCONTEXT+"components/images/priority-"+o+'-16.png" title="'+this.msg("label.priority",this.msg("priority."+o))+'"/>';if(q){r+='<br/><img src="'+Alfresco.constants.URL_RESCONTEXT+'components/images/pooled-task-16.png" title="'+this.msg("label.pooledTask")+'"/>'}var n=p.properties.bpm_status;if(n){if(n.indexOf("REJECT")>-1){r+='<img src="'+Alfresco.constants.URL_RESCONTEXT+'components/images/reject.png" />'}else{if(n.indexOf("RESUBMIT")>-1){r+='<img src="'+Alfresco.constants.URL_RESCONTEXT+'components/images/revise.png" />'}else{if(n.indexOf("WAPPR")>-1){r+='<img src="'+Alfresco.constants.URL_RESCONTEXT+'components/images/wappr.png" />'}else{if(n.indexOf("CONSULT")>-1){r+='<img src="'+Alfresco.constants.URL_RESCONTEXT+'components/images/consult.png" />'}}}}}}v.innerHTML=r},renderCellTaskInfo:function j(s,J,u,C){var L=J.getData(),F="";if(L.isInfo){F+='<div class="empty"><h3>'+L.title+"</h3>";F+="<span>"+L.description+"</span></div>"}else{var n=L.id,w=L.properties.bpm_description,A=L.properties.bpm_dueDate,K=A?Alfresco.util.fromISO8601(A):null,H=new Date(),q=L.title,B=L.properties.bpm_status,I=L.owner;if(L.propertyLabels&&Alfresco.util.isValueSet(L.propertyLabels.bpm_status,false)){B=L.propertyLabels.bpm_status}var x=YAHOO.util.Cookie.get("alf_share_locale");x=x?x:"en";var z=x.indexOf("th")==0;if(!z){w=L.properties.pcmreqwf_description;if(!w){w=L.properties.pcmordwf_description;if(!w){w=L.properties.expbrwwf_description;if(!w){w=L.properties.expusewf_description;if(!w){w=L.properties.hrsalwf_description}}}}}if(q){if(q.indexOf("Preparer")>-1){q=z?"ผู้บันทึก":"Preparer"}else{if(q.indexOf("Requester")>-1){q=z?"ผู้ขอ":"Requester"}else{if(q.indexOf("Approver")>-1){var y=q.indexOf("Approver");var r=q.substring(y+8);q=z?"ผู้อนุมัติขั้นที่":"Approver";q+=r}else{if(q.indexOf("Consultant")>-1){q=z?"ที่ปรึกษา":"Consultant"}}}}}if(B){if(B.indexOf("REJECT")>-1){B=z?"ไม่อนุมัติ":"Rejected"}else{if(B.indexOf("RESUBMIT")>-1){B=z?"รอการอนุมัติ":"Waiting for Approval"}else{if(B.indexOf("WAPPR")>-1){B=z?"รอการอนุมัติ":"Waiting for Approval"}else{if(B.indexOf("CONSULT")>-1){B=z?"ขอคำปรึกษา":"Consulting"}}}}}B="<b>"+(z?"สถานะ":"Status")+":&nbsp;</b>"+B;if(w==q){w=this.msg("workflow.no_message")}var D;if(L.isEditable){D=f("task-edit?taskId="+n+"&referrer=tasks")+'" class="theme-color-1" title="'+this.msg("title.editTask")}else{D=f("task-details?taskId="+n+"&referrer=tasks")+'" class="theme-color-1" title="'+this.msg("title.viewTask")}var o='<h3><a href="'+D+'">'+d(w)+"</a></h3>",E=K?'<h4><span class="'+(H>K?"task-delayed":"")+'" title="'+this.msg("title.dueOn",Alfresco.util.formatDate(K,"longDate"))+'">'+Alfresco.util.formatDate(K,"longDate")+"</span></h4>":"",m='<div title="'+this.msg("title.taskSummary",q,B)+'">'+this.msg("label.taskSummary",q,B)+"</div>",G="",t="";if(K){G='<div><img src="'+Alfresco.constants.URL_RESCONTEXT+'components/images/help-task-bw-32.png"></div>'}if(!I||!I.userName){t='<span class="theme-bg-color-5 theme-color-5 unassigned-task">'+this.msg("label.unassignedTask")+"</span>"}F=o+E+m+G+t+G}s.innerHTML=F},renderCellActions:function b(o,m,p,r){var n=m.getData(),q="";if(n.isInfo){p.width=0;g.setStyle(o,"width",p.width+"px");g.setStyle(o.parentNode,"width",p.width+"px")}else{if(n.isEditable){q+='<a href="'+f("task-edit?taskId="+n.id+"&referrer=tasks")+'" class="edit-task" title="'+this.msg("title.editTask")+'">&nbsp;</a>'}q+='<a href="'+f("task-details?taskId="+n.id+"&referrer=tasks")+'" class="view-task" title="'+this.msg("title.viewTask")+'">&nbsp;</a>';q+='<a href="" onClick="showLightbox({ src: \''+Alfresco.constants.PROXY_URI+"api/workflow-instances/"+n.workflowInstance.id+'/diagram\'}); return false;" class="view-diagram" title="'+this.msg("title.viewDiagram")+'">&nbsp;</a>'}o.innerHTML=q}})})();