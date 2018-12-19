function getActionIcon(v, a, i) { 
	return v.indexOf(a) < 0 ? HIDE_CSS : 'icon_'+i
}
function getAlfHeader() { 
	return { 'Alfresco-CSRFToken': Ext.util.Cookies.get(ALF_TOKEN) }
}
function validForm(form){
	return form.getForm().isValid();
}
function validateFormAdmin(form) {
	
	var fields = form.getForm().getFields();
	
	var fieldCount = fields.getCount();
	
	var valid = true;
	
	for(var index=0; index<fieldCount; index++){
		field = fields.get(index);
		if(field.getXType() == "datefield"){
			value = field.getRawValue();
		}
		else{
			value = field.getValue();
		}
		
		if (SEARCH_RE_ADMIN.test(value)) {
			valid = false;
	
			if(field.getXType()=="combobox" || field.getXType()=="combo"){
				field.reset();
			}
			
			break;
		}
		
	}
	
	return valid;	
}

Date.prototype.format = function(format) //author: meizz
{
  var o = {
    "M+" : this.getMonth()+1, //month
    "d+" : this.getDate(),    //day
    "h+" : this.getHours(),   //hour
    "m+" : this.getMinutes(), //minute
    "s+" : this.getSeconds(), //second
    "q+" : Math.floor((this.getMonth()+3)/3),  //quarter
    "S" : this.getMilliseconds() //millisecond
  }

  if(/(y+)/.test(format)) format=format.replace(RegExp.$1,
    (this.getFullYear()+"").substr(4 - RegExp.$1.length));
  for(var k in o)if(new RegExp("("+ k +")").test(format))
    format = format.replace(RegExp.$1,
      RegExp.$1.length==1 ? o[k] :
        ("00"+ o[k]).substr((""+ o[k]).length));
  return format;
}

function loadingStore(store,fn){
	if(store!=null&&store!=undefined){
		var callback ;
		if(fn!=null && fn!='' && fn!="" && fn!=undefined){
			callback = {
					callback : fn
			}
		}
		
		store.load(callback);
	}
}

function htmlToLineSeparator(value){
	if(value!=null&&value!=undefined){
		
		while(value.indexOf("<br/>") != -1){
			value = value.replace("<br/>","\n");
		}
		
		return value;
	}
}

function compareDate(from,to){
	
	var dateFrom = new Date(from.getValue());
	var dateTo = new Date(to.getValue());
	
    if(to.getValue() == null){
		to.setMinValue(dateFrom);
		to.validate();
	}else{
		to.setMinValue(dateFrom);
		to.validate();
	}
}

function getLanguage() {
	var req = new XMLHttpRequest();
	req.open('GET', document.location, false);
	req.send(null);
	var headers = req.getAllResponseHeaders().toLowerCase();
	var contentLanguage = headers.match( /^content-language\:(.*)$/gm );
	if(contentLanguage[0]) {
	    return contentLanguage[0].split(":")[1].trim().toUpperCase();
	}	    
}
function getCookie(name) {
  match = document.cookie.match(new RegExp(name + '=([^;]+)'));
  if (match) return match[1];
}
function getLang() {
	var v = getCookie(ALF_LANG_COOKIE);
	return v ? v : "en";
}
function mandatoryLabel(lbl) {
	return lbl + '<font color="red">*</font>';
}
function replaceIfNull(v, dv) {
	return (v ? v : dv);
}
function getRadioValue(name) {
    var el = document.getElementsByName(name);
    for (var i=0, l=el.length; i<l; i++)
    {
        if (el[i].checked)
        {
            return el[i].value;
        }
    }
}
function setValue(f,n,v) {
	f.down('field[name='+n+']').setValue(v);
}
function nodeRef2Url(n) {
	return n.replace("://","/");
}
function getScrSize() {
    var winW = 0, winH = 0;
    if (document.body && document.body.offsetWidth) {
        winW = document.body.offsetWidth;
        winH = document.body.offsetHeight;
    }
    if (document.compatMode=='CSS1Compat' && document.documentElement && document.documentElement.offsetWidth ) {
        winW = document.documentElement.offsetWidth;
        winH = document.documentElement.offsetHeight;
    }
    if (window.innerWidth && window.innerHeight) {
        winW = window.innerWidth;
        winH = window.innerHeight;
    }   
    return { width: winW, height: winH };
}
function cvtDateValue(v) {
	if (v) {
		return Ext.Date.parse(v.split("T").join(" "), "Y-m-d H:i:s");
	} else {
		return null;
	}
}
function alertInvalidSession() {
	var lang = getLang();
	var msg = lang.indexOf("th") >= 0 ? INVALID_SES_TH : INVALID_SES;
	Ext.MessageBox.alert("ERROR", msg);
}