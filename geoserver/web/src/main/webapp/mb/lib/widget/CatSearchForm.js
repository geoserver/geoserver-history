mapbuilder.loadScript(baseDir+"/widget/WidgetBaseXSL.js");
function OWSCatSearchForm(widgetNode,model){
WidgetBaseXSL.apply(this,new Array(widgetNode,model));
this.postPaint=function(objRef){
objRef.searchForm=document.getElementById(objRef.formName);
objRef.searchForm.parentWidget=objRef;
objRef.searchForm.westCoord.onblur=objRef.setAoi;
objRef.searchForm.northCoord.onblur=objRef.setAoi;
objRef.searchForm.eastCoord.onblur=objRef.setAoi;
objRef.searchForm.southCoord.onblur=objRef.setAoi;
objRef.searchForm.westCoord.model=objRef.model;
objRef.searchForm.northCoord.model=objRef.model;
objRef.searchForm.eastCoord.model=objRef.model;
objRef.searchForm.southCoord.model=objRef.model;
objRef.searchForm.onkeypress=objRef.handleKeyPress;
objRef.searchForm.onsubmit=objRef.submitForm;
}
this.displayAoiCoords=function(objRef){
objRef.searchForm=document.getElementById(objRef.formName);
var aoi=objRef.model.getParam("aoi");
objRef.searchForm.westCoord.value=aoi[0][0];
objRef.searchForm.northCoord.value=aoi[0][1];
objRef.searchForm.eastCoord.value=aoi[1][0];
objRef.searchForm.southCoord.value=aoi[1][1];
}
this.model.addListener('aoi',this.displayAoiCoords,this);
this.setAoi=function(){
var aoi=this.model.getParam("aoi");
if(aoi){
var ul=aoi[0];
var lr=aoi[1];
switch(this.name){
case 'westCoord':
ul[0]=this.value;
break;
case 'northCoord':
ul[1]=this.value;
break;
case 'eastCoord':
lr[0]=this.value;
break;
case 'southCoord':
lr[1]=this.value;
break;
}
this.model.setParam("aoi",new Array(ul,lr));
}
}
this.setLocation=function(bbox){
var bboxArray=new Array();
bboxArray=bbox.split(",");
var ul=new Array(parseFloat(bboxArray[0]),parseFloat(bboxArray[2]));
var lr=new Array(parseFloat(bboxArray[1]),parseFloat(bboxArray[3]));
this.model.setParam("aoi",new Array(ul,lr));
}
this.submitForm=function(){
thisWidget=this.parentWidget;
thisWidget.webServiceForm=document.getElementById(thisWidget.formName);
thisWidget.targetModel.setParam("wfs_GetFeature","service_resources");
return false;
}
this.handleKeyPress=function(event){
var keycode;
var target;
if(event){
keycode=event.which;
target=event.currentTarget;
}else{
keycode=window.event.keyCode;
target=window.event.srcElement.form;
}
if(keycode==13){target.parentWidget.submitForm();
return false;
}
}
var RUC_Window=null;
this.openRucWindow=function(rucType){ 
if(RUC_Window==null||RUC_Window.closed){ 
var baseUrl;
var params;
switch(rucType){
case "placename":
baseURL="/rucs/placeName.html?language="+config.lang+"&formName="+this.formName;
params="width=290,height=480,scrollbars=0,toolbar=0,location=0,directories=0,status=0,menubar=0,resizable=0";
break;
case "postalCode":
baseURL="/rucs/postalCode.html?language="+config.lang+"&formName="+this.formName;
params="width=280,height=180,scrollbars=0,toolbar=0,location=0,directories=0,status=0,menubar=0,resizable=0";
break;
default:
alert("unkown RUC type");
break;
}
RUC_Window=open(baseURL,"RUCWindow",params);
}
RUC_Window.focus();
return false;
} 
function RUC_closeRUCWindow(){ 
if(RUC_Window!=null&&!RUC_Window.closed){ 
RUC_Window.close();
} 
} 
this.formName="WebServiceForm_"+mbIds.getId();
this.stylesheet.setParameter("formName",this.formName);
}
SetAoiCoords=function(aoiBox){
config.objects.mainMap.setParam("aoi",aoiBox);
}
