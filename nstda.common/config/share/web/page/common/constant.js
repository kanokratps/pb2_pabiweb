var MAIN_ID="mainClient"
,HTML_ID="Share"
,H_OFFSET=100
,W_OFFSET=18
,HEIGHT=0
,WIDTH=0
,GRID_TIME_OUT=30000
,HIDE_CSS="x-hide-display"
,ALF_TOKEN='Alfresco-CSRFToken'
,ALF_LANG_COOKIE='alf_share_locale'	
,MAIN_CONTEXT="/share"
,CONTEXT=MAIN_CONTEXT+"/service"
,ALF_CONTEXT=MAIN_CONTEXT+"/proxy/alfresco/pb"
,APP_FOLDER=MAIN_CONTEXT+'/res/app'
,SEARCH_RE_ADMIN = /[{}?\[\]<>=%*|'"]/
,TEXT_REGX_ADMIN = /^[^\{\}\?\[\]\<\>\=\%\*\'\"\|]?[^\{\}\?\[\]\<\>\=\%\*\'\"\|]+$/
,TEXT_REGX_ADMIN_JOB = /^[^\{\}\[\]\<\>\=\%\'\"\|]?[^\{\}\[\]\<\>\=\%\'\"\|]+$/
,TEXT_REGX_TEXT_ADMIN = "Not allowed special characters: { } ? [ ] < > = % * ' \" |"
,EMTY_TEXT_COMBO = '--- Please Select ---'
,PAGE_SIZE=20
,ADMIN_PAGE_SIZE=20
,TEXT_REGX= /^[^\'\"\,]?[^\'\"\,]+$/
,TEXT_REGX_TEXT="Not allowed special characters: ' \" \,"	
,REGEX = /^((?![']).)*$/
,REGEX_TEXT = "Not allowed special characters: '"	
,MAX_APPROVER = 15
,DEFAULT_MONEY_FORMAT='0,000.00'
,DEFAULT_DATE_TIME_FORMAT='d/m/Y H:i'
,DEFAULT_DATE_FORMAT='d/m/Y'
,MODULE_ADMIN="admin"
,MODULE_PCM="pcm"
,MODULE_EXP="exp"
,READ_ONLY='background-color:#ddd;background-image:none'
,INVALID_SES="Invalid Session! Please login again."
,INVALID_SES_TH="Invalid Session! กรุณา login อีกครั้ง"
,CHAIRMAN="ประธานกรรมการ"
,COMMITTEE="กรรมการ"
;