mapbuilder.loadScript(baseDir+"/widget/WidgetBaseXSL.js");
function MapScaleText(widgetNode,model){
WidgetBaseXSL.apply(this,new Array(widgetNode,model));
this.submitForm=function(){
var newScale=this.mapScaleTextForm.mapScale.value;
this.model.extent.setScale(newScale.split(",").join(""));
return false;}
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
return false
}
}
this.showScale=function(objRef){
if(objRef.mapScaleTextForm){
var newScale=Math.round(objRef.model.extent.getScale());
var parts=new Array();
while(newScale>=1000.0){
var newPart=newScale/1000.0;
newScale=Math.floor(newPart);
var strPart=leadingZeros(Math.round((newPart-newScale)*1000).toString(),3);
parts.unshift(strPart);
}
parts.unshift(newScale);
objRef.mapScaleTextForm.mapScale.value=parts.join(",");
}
}
this.model.addListener("bbox",this.showScale,this);
this.model.addListener("refresh",this.showScale,this);
this.prePaint=function(objRef){
var mapScale=objRef.model.extent.getScale();
this.stylesheet.setParameter("mapScale",mapScale);
}
this.postPaint=function(objRef){
objRef.mapScaleTextForm=document.getElementById(objRef.formName);
objRef.mapScaleTextForm.parentWidget=objRef;
objRef.mapScaleTextForm.onkeypress=objRef.handleKeyPress;
objRef.showScale(objRef);
}
this.formName="MapScaleText_"+mbIds.getId();
this.stylesheet.setParameter("formName",this.formName);
}
