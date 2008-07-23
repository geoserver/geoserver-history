mapbuilder.loadScript(baseDir+"/widget/WidgetBaseXSL.js");
mapbuilder.loadScript(baseDir+"/widget/MapContainerBase.js");
function TimeSeries(widgetNode,model){
WidgetBaseXSL.apply(this,new Array(widgetNode,model));
MapContainerBase.apply(this,new Array(widgetNode,model));
this.hiddenListener=function(objRef,layerName){
var vis="visible";
if(objRef.model.getHidden(layerName)=="1"){
vis="hidden";
}
var layerId=objRef.model.id+"_"+objRef.id+"_"+layerName;
if(objRef.model.timestampList&&objRef.model.timestampList.getAttribute("layerName")==layerName){ 
var timestampIndex=objRef.model.getParam("timestamp");
var timestamp=objRef.model.timestampList.childNodes[timestampIndex];
layerId+="_"+timestamp.firstChild.nodeValue;
}
var layer=document.getElementById(layerId);
if(layer){
layer.style.visibility=vis;
}else{
alert("error finding layerId:"+layerId);
}
}
this.model.addListener("hidden",this.hiddenListener,this);
this.timestampListener=function(objRef,timestampIndex){
var layerName=objRef.model.timestampList.getAttribute("layerName");
var timestamp=objRef.model.timestampList.childNodes[timestampIndex];
var vis=(timestamp.getAttribute("current")=="1")?"visible":"hidden";
var layerId=objRef.model.id+"_"+objRef.id+"_"+layerName+"_"+timestamp.firstChild.nodeValue;
var layer=document.getElementById(layerId);
if(layer){
layer.style.visibility=vis;
}else{
alert("error finding layerId:"+layerId);
}
}
this.model.addListener("timestamp",this.timestampListener,this);
this.prePaint=function(objRef){
var timelist="";
var timestampList=objRef.model.timestampList;
if(timestampList){
for(var i=objRef.model.getParam("firstFrame");i<=objRef.model.getParam("lastFrame");++i){
timelist+=timestampList.childNodes[i].firstChild.nodeValue+",";
}
objRef.stylesheet.setParameter("timeList",timelist.substring(0,timelist.length-1));}
}
}
