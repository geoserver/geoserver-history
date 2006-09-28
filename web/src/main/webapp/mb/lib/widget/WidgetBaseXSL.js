mapbuilder.loadScript(baseDir+"/widget/WidgetBase.js");
function WidgetBaseXSL(widgetNode,model){
WidgetBase.apply(this,new Array(widgetNode,model));
if(!this.stylesheet){
var styleNode=widgetNode.selectSingleNode("mb:stylesheet");
if(styleNode){
this.stylesheet=new XslProcessor(styleNode.firstChild.nodeValue,model.namespace);
}else{
this.stylesheet=new XslProcessor(baseDir+"/widget/"+widgetNode.nodeName+".xsl",model.namespace);
}
}
if(config.widgetText){
var textNodeXpath="/mb:WidgetText/mb:widgets/mb:"+widgetNode.nodeName;
var textParams=config.widgetText.selectNodes(textNodeXpath+"/*");
for(var j=0;j<textParams.length;j++){
this.stylesheet.setParameter(textParams[j].nodeName,textParams[j].firstChild.nodeValue);
}
}
for(var j=0;j<widgetNode.childNodes.length;j++){
if(widgetNode.childNodes[j].firstChild
&&widgetNode.childNodes[j].firstChild.nodeValue)
{
this.stylesheet.setParameter(
widgetNode.childNodes[j].nodeName,
widgetNode.childNodes[j].firstChild.nodeValue);
}
}
this.stylesheet.setParameter("modelId",this.model.id);
this.stylesheet.setParameter("modelTitle",this.model.title);
this.stylesheet.setParameter("widgetId",this.id);
this.stylesheet.setParameter("skinDir",config.skinDir);
this.stylesheet.setParameter("lang",config.lang);
this.paint=function(objRef,refresh){
if(objRef.model.doc&&objRef.node&&(objRef.autoRefresh||refresh)){
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
if(tempNode.firstChild!=null){tempNode.firstChild.setAttribute("id",objRef.outputNodeId);
if(outputNode){
objRef.node.replaceChild(tempNode.firstChild,outputNode);
}else{
objRef.node.appendChild(tempNode.firstChild);
}
}
objRef.postPaint(objRef);
}
}
this.model.addListener("refresh",this.paint,this);
this.clearWidget=function(objRef){
var outputNode=document.getElementById(objRef.outputNodeId);
if(outputNode)objRef.node.removeChild(outputNode);
}
this.model.addListener("newModel",this.clearWidget,this);
}
