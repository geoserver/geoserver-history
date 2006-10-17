mapbuilder.loadScript(baseDir+"/widget/WidgetBase.js");
function WidgetBaseWZ(widgetNode,model){
WidgetBase.apply(this,new Array(widgetNode,model));
if(!widget.stylesheet){
var styleNode=widgetNode.selectSingleNode("mb:stylesheet");
if(styleNode){
widget.stylesheet=new XslProcessor(styleNode.firstChild.nodeValue,model.namespace);
}else{
widget.stylesheet=new XslProcessor(baseDir+"/widget/"+widgetNode.nodeName+".xsl",model.namespace);
}
}
for(var j=0;j<widgetNode.childNodes.length;j++){
if(widgetNode.childNodes[j].firstChild
&&widgetNode.childNodes[j].firstChild.nodeValue)
{
widget.stylesheet.setParameter(
widgetNode.childNodes[j].nodeName,
widgetNode.childNodes[j].firstChild.nodeValue);
}
}
if(config.widgetText){
var textNodeXpath="/mb:WidgetText/mb:widgets/mb:"+widgetNode.nodeName;
var textParams=config.widgetText.selectNodes(textNodeXpath+"/*");
for(var j=0;j<textParams.length;j++){
widget.stylesheet.setParameter(textParams[j].nodeName,textParams[j].firstChild.nodeValue);
}
}
widget.stylesheet.setParameter("modelId",widget.model.id);
widget.stylesheet.setParameter("modelTitle",widget.model.title);
widget.stylesheet.setParameter("widgetId",widget.id);
widget.stylesheet.setParameter("skinDir",config.skinDir);
widget.stylesheet.setParameter("lang",config.lang);
this.paint=function(objRef){
if(objRef.model.doc&&objRef.node){
objRef.stylesheet.setParameter("modelUrl",objRef.model.url);
objRef.stylesheet.setParameter("targetModelId",objRef.targetModel.id);
objRef.resultDoc=objRef.model.doc;objRef.prePaint(objRef);
if(objRef.debug)alert("prepaint:"+Sarissa.serialize(objRef.resultDoc));
if(objRef.debug)alert("stylesheet:"+Sarissa.serialize(objRef.stylesheet.xslDom));
var outputNode=document.getElementById(objRef.outputNodeId);
var tempNode=document.createElement("DIV");
var s=objRef.stylesheet.transformNodeToString(objRef.resultDoc);
if(config.serializeUrl&&objRef.debug)postLoad(config.serializeUrl,s);
if(objRef.debug)alert("painting:"+objRef.id+":"+s);
tempNode.innerHTML=s;
tempNode.firstChild.setAttribute("id",objRef.outputNodeId);
if(outputNode){
objRef.node.replaceChild(tempNode.firstChild,outputNode);
}else{
objRef.node.appendChild(tempNode.firstChild);
}
objRef.postPaint(objRef);
}
}
this.clearWidget=function(objRef){
var outputNode=document.getElementById(objRef.outputNodeId);
if(outputNode)objRef.node.removeChild(outputNode);
}
widget.model.addListener("refresh",widget.paint,widget);
widget.model.addListener("newModel",widget.clearWidget,widget);
}
