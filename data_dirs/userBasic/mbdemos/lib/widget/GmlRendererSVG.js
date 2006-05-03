mapbuilder.loadScript(baseDir+"/model/Proj.js");
mapbuilder.loadScript(baseDir+"/widget/MapContainerBase.js");
function GmlRendererSVG(widgetNode,model){
var base=new MapContainerBase(this,widgetNode,model);
this.paintMethod="xsl2html";
this.coordXsl=new XslProcessor(baseDir+"/widget/GmlCooordinates2Coord.xsl");
this.prePaint=function(objRef){
objRef.model.setParam("modelStatus","preparing coordinates");
objRef.stylesheet.setParameter("width",objRef.containerModel.getWindowWidth());
objRef.stylesheet.setParameter("height",objRef.containerModel.getWindowHeight());
bBox=objRef.containerModel.getBoundingBox();
objRef.stylesheet.setParameter("bBoxMinX",bBox[0]);
objRef.stylesheet.setParameter("bBoxMinY",bBox[1]);
objRef.stylesheet.setParameter("bBoxMaxX",bBox[2]);
objRef.stylesheet.setParameter("bBoxMaxY",bBox[3]);
objRef.stylesheet.setParameter("color","#FF0000");
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
