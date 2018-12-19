(function(){var g=YAHOO.util.Dom,m=YAHOO.util.Event,b=YAHOO.util.Selector;var e=Alfresco.util.encodeHTML,h=Alfresco.util.hasEventInterest,f=Alfresco.util.siteURL;Alfresco.component.TaskEditHeader=function l(p){Alfresco.component.TaskEditHeader.superclass.constructor.call(this,p,["button"]);this.name="Alfresco.component.TaskEditHeader";this.options=YAHOO.lang.merge(this.options,Alfresco.component.TaskEditHeader.superclass.options);Alfresco.util.ComponentManager.reregister(this);this.isRunning=false;this.taskId=null;YAHOO.Bubbling.on("taskDetailedData",this.onTaskDetailedData,this);return this};YAHOO.extend(Alfresco.component.TaskEditHeader,Alfresco.component.ShareFormManager,{isRunning:false,taskId:null,referrerValue:null,onReady:function a(){Alfresco.util.Ajax.request({url:Alfresco.constants.URL_SERVICECONTEXT+"components/people-finder/people-finder",dataObj:{htmlid:this.id+"-peopleFinder"},successCallback:{fn:this.onPeopleFinderLoaded,scope:this},failureMessage:"Could not load People Finder component",execScripts:true})},onPeopleFinderLoaded:function c(q){var p=g.get(this.id+"-peopleFinder");p.innerHTML=q.serverResponse.responseText;this.widgets.reassignPanel=Alfresco.util.createYUIPanel(this.id+"-reassignPanel");this.widgets.peopleFinder=Alfresco.util.ComponentManager.get(this.id+"-peopleFinder");this.widgets.peopleFinder.setOptions({singleSelectMode:true,addButtonLabel:this.msg("button.select")});YAHOO.Bubbling.on("personSelected",this.onPersonSelected,this)},onPersonSelected:function o(q,p){if(h(this.widgets.peopleFinder,p)){this.widgets.reassignPanel.hide();this._updateTaskProperties({cm_owner:p[1].userName},"reassign")}},onTaskDetailedData:function k(r,q){var p=q[1];this.taskId=p.id;this.referrerValue=Alfresco.util.getQueryStringParameter("referrer");
var vv = YAHOO.util.Cookie.get("alf_share_locale");
vv = vv ? vv : "en";
var isThai = vv.indexOf("th")==0;

var title = p.title;
if(title.indexOf("Preparer") > -1){
	title = isThai ? "ผู้บันทึก" : "Preparer";
}
else if(title.indexOf("Requester") > -1){
	title = isThai ? "ผู้ขอ" : "Requester";
}
else if(title.indexOf("Approver") > -1){
	var ppp = title.indexOf("Approver");
	var suf = title.substring(ppp+8);
	title = isThai ? "ผู้อนุมัติขั้นที่" : "Approver";
	title += suf;
}
else if(title.indexOf("Consultant") > -1){
	title = isThai ? "ที่ปรึกษา" : "Consultant";
}
b.query("h1 span",this.id,true).innerHTML=e(title);if(!p.isEditable){Alfresco.util.PopupManager.displayMessage({text:this.msg("message.task.completed"),displayTime:2});YAHOO.lang.later(2000,this,function(){if(this.referrerValue){if(referrerValue=="tasks"){document.location.href=f("my-tasks")}else{if(referrerValue="workflows"){document.location.href=f("my-workflows")}}}else{document.location.href=this.getSiteDefaultUrl()||Alfresco.constants.URL_CONTEXT}},[])}if(p.isReassignable){this.widgets.reassignButton=Alfresco.util.createYUIButton(this,"reassign",this.onReassignButtonClick);g.removeClass(b.query(".actions .reassign",this.id),"hidden")}if(p.isClaimable){this.widgets.claimButton=Alfresco.util.createYUIButton(this,"claim",this.onClaimButtonClick);g.removeClass(b.query(".actions .claim",this.id),"hidden");g.removeClass(b.query(".unassigned-message",this.id),"hidden")}if(p.isReleasable){this.widgets.releaseButton=Alfresco.util.createYUIButton(this,"release",this.onReleaseButtonClick);g.removeClass(b.query(".actions .release",this.id),"hidden")}},onReleaseButtonClick:function n(q,p){this._updateTaskProperties({cm_owner:null},"release")},onClaimButtonClick:function j(q,p){this._updateTaskProperties({cm_owner:Alfresco.constants.USERNAME},"claim")},onReassignButtonClick:function d(q,p){this.widgets.peopleFinder.clearResults();this.widgets.reassignPanel.show()},_updateTaskProperties:function i(p,q){this._disableActionButtons(true);YAHOO.lang.later(2000,this,function(){if(this.isRunning){if(!this.widgets.feedbackMessage){this.widgets.feedbackMessage=Alfresco.util.PopupManager.displayMessage({text:this.msg("message."+q),spanClass:"wait",displayTime:0})}else{if(!this.widgets.feedbackMessage.cfg.getProperty("visible")){this.widgets.feedbackMessage.show()}}}},[]);if(!this.isRunning){this.isRunning=true;Alfresco.util.Ajax.jsonPut({url:Alfresco.constants.PROXY_URI_RELATIVE+"api/task-instances/"+this.taskId,dataObj:p,successCallback:{fn:function(r,t){this.isRunning=false;var s=r.json.data;if(s){Alfresco.util.PopupManager.displayMessage({text:this.msg("message."+t+".success")});YAHOO.lang.later(3000,this,function(u){if(u.owner&&u.owner.userName==Alfresco.constants.USERNAME){document.location.reload()}else{if(this.referrerValue){this.navigateForward(true)}else{document.location.href=this.getSiteDefaultUrl()||Alfresco.constants.URL_CONTEXT}}},s)}},obj:q,scope:this},failureCallback:{fn:function(r){this.isRunning=false;this._disableActionButtons(false);Alfresco.util.PopupManager.displayPrompt({title:this.msg("message.failure"),text:this.msg("message."+q+".failure")})},scope:this}})}},_disableActionButtons:function(p){if(this.widgets.reassignButton){this.widgets.reassignButton.set("disabled",p)}if(this.widgets.releaseButton){this.widgets.releaseButton.set("disabled",p)}if(this.widgets.claimButton){this.widgets.claimButton.set("disabled",p)}}})})();