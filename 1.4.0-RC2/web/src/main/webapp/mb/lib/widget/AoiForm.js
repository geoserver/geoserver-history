mapbuilder.loadScript(baseDir+"/widget/WidgetBaseXSL.js");
mapbuilder.loadScript(baseDir+"/model/Proj.js");
function AoiForm(widgetNode,model){
WidgetBaseXSL.apply(this,new Array(widgetNode,model));
this.displayAoiCoords=function(objRef,targetNode){
objRef.aoiForm=document.getElementById(objRef.formName);
var aoi=objRef.model.getParam("aoi");
if(aoi&&objRef.aoiForm){
objRef.aoiForm.westCoord.value=aoi[0][0];
objRef.aoiForm.northCoord.value=aoi[0][1];
objRef.aoiForm.eastCoord.value=aoi[1][0];
objRef.aoiForm.southCoord.value=aoi[1][1];
}
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
this.postPaint=function(objRef){
objRef.aoiForm=document.getElementById(objRef.formName);
objRef.aoiForm.westCoord.onblur=objRef.setAoi;
objRef.aoiForm.northCoord.onblur=objRef.setAoi;
objRef.aoiForm.eastCoord.onblur=objRef.setAoi;
objRef.aoiForm.southCoord.onblur=objRef.setAoi;
objRef.aoiForm.westCoord.model=objRef.model;
objRef.aoiForm.northCoord.model=objRef.model;
objRef.aoiForm.eastCoord.model=objRef.model;
objRef.aoiForm.southCoord.model=objRef.model;
}
var formNameNode=widgetNode.selectSingleNode("mb:formName");
if(formNameNode){
this.formName=formNameNode.firstChild.nodeValue;
}else{
this.formName="AoiForm_"+mbIds.getId();
}
this.stylesheet.setParameter("formName",this.formName);
}
