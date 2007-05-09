mapbuilder.loadScript(baseDir+"/model/Proj.js");
mapbuilder.loadScript(baseDir+"/widget/MapContainerBase.js");
mapbuilder.loadScript(baseDir+"/util/wz_jsgraphics/wz_jsgraphics.js");
function GmlRendererWZ(widgetNode,model){
WidgetBase.apply(this,new Array(widgetNode,model));
var styleNode=widgetNode.selectSingleNode("mb:stylesheet");
if(styleNode){
this.stylesheet=new XslProcessor(styleNode.firstChild.nodeValue,model.namespace);
}else{
this.stylesheet=new XslProcessor(baseDir+"/widget/"+widgetNode.nodeName+".xsl",model.namespace);
}
this.paint=function(objRef){
if(objRef.model.doc&&objRef.node&&objRef.containerModel&&objRef.containerModel.doc){
objRef.stylesheet.setParameter("modelUrl",objRef.model.url);
objRef.resultDoc=objRef.model.doc;objRef.prePaint(objRef);
if(objRef.debug)alert("prepaint:"+Sarissa.serialize(objRef.resultDoc));
if(objRef.debug)alert("stylesheet:"+Sarissa.serialize(objRef.stylesheet.xslDom));
var outputNode=document.getElementById(objRef.outputNodeId);
var tempNode=document.createElement("DIV");
tempNode.style.position="absolute";
tempNode.style.top=0;
tempNode.style.left=0;
tempNode.style.zindex=300;
tempNode.setAttribute("id",objRef.outputNodeId);
if(outputNode){
objRef.node.replaceChild(tempNode,outputNode);
}else{
objRef.node.appendChild(tempNode);
}
jsNode=objRef.stylesheet.transformNodeToObject(objRef.resultDoc);
js=jsNode.selectSingleNode("js").firstChild.nodeValue;
if(objRef.debug)alert("javascript eval:"+js);
objRef.model.setParam("modelStatus","rendering");
eval(js);
objRef.postPaint(objRef);
}
}
this.model.addListener("refresh",this.paint,this);
MapContainerBase.apply(this,new Array(widgetNode,model));
for(var j=0;j<widgetNode.childNodes.length;j++){
if(widgetNode.childNodes[j].firstChild
&&widgetNode.childNodes[j].firstChild.nodeValue)
{
this.stylesheet.setParameter(
widgetNode.childNodes[j].nodeName,
widgetNode.childNodes[j].firstChild.nodeValue);
}
}
if(config.widgetText){
var textNodeXpath="/mb:WidgetText/mb:widgets/mb:"+widgetNode.nodeName;
var textParams=config.widgetText.selectNodes(textNodeXpath+"/*");
for(var j=0;j<textParams.length;j++){
this.stylesheet.setParameter(textParams[j].nodeName,textParams[j].firstChild.nodeValue);
}
}
this.stylesheet.setParameter("modelId",this.model.id);
this.stylesheet.setParameter("modelTitle",this.model.title);
this.stylesheet.setParameter("widgetId",this.id);
this.stylesheet.setParameter("skinDir",config.skinDir);
this.stylesheet.setParameter("lang",config.lang);
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
if(!document.getElementById(objRef.outputNodeId)){
}
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
