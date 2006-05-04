mapbuilder.loadScript(baseDir+"/model/Proj.js");
mapbuilder.loadScript(baseDir+"/widget/MapContainerBase.js");
function GmlRendererWKT(widgetNode,model){
var base=new MapContainerBase(this,widgetNode,model);
this.paintMethod="xsl2js";
this.coordXsl=new XslProcessor(baseDir+"/widget/GmlCooordinates2Coord.xsl");
this.prePaint=function(objRef){
objRef.model.setParam("modelStatus","preparing coordinates");
objRef.stylesheet.setParameter("targetElement",objRef.containerModel.getWindowWidth());
objRef.resultDoc=objRef.coordXsl.transformNodeToObject(objRef.resultDoc);
}
this.hiddenListener=function(objRef,layerName){
var vis="visible";
if(objRef.model.getHidden(layerName)){
vis="hidden";
}
var outputNode=document.getElementById(objRef.outputNodeId)
for(var i=0;i<outputNode.childNodes.length;++i){
outputNode.childNodes[i].style.visibility=vis;
}
}
this.model.addListener("hidden",this.hiddenListener,this);
}
