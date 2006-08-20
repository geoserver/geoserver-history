mapbuilder.loadScript(baseDir+"/widget/WidgetBase.js");
function Loading2(widgetNode,model){
WidgetBase.apply(this,new Array(widgetNode,model));
var imageSrc=widgetNode.selectSingleNode("mb:imageSrc");
if(imageSrc){
this.imageSrc=config.skinDir+imageSrc.firstChild.nodeValue;
}else{
this.imageSrc=config.skinDir+"/images/Loading.gif";
}
var textMessage=widgetNode.selectSingleNode("mb:textMessage");
if(textMessage){
this.textMessage=textMessage.firstChild.nodeValue;
}else{
this.textMessage="Document loading, please wait...";
}
this.model.addListener("newModel",this.paint,this);
this.model.addListener("loadModel",this.clear,this);
}
Loading2.prototype.paint=function(objRef){
if(objRef.node){
var outputNode=document.getElementById(objRef.outputNodeId+"_loading");
if(!outputNode){
outputNode=document.createElement("div");
outputNode.setAttribute("id",objRef.outputNodeId+"_loading");
if(objRef.imageSrc){
var loadingImg=document.createElement("img");
loadingImg.src=objRef.imageSrc;
outputNode.appendChild(loadingImg);
}
if(objRef.textMessage){
var loadingMsg=document.createElement("p");
loadingMsg.innerHTML=objRef.textMessage;
outputNode.appendChild(loadingMsg);
}
objRef.node.appendChild(outputNode);
}
}
}
Loading2.prototype.clear=function(objRef){
var outputNode=document.getElementById(objRef.outputNodeId+"_loading");
if(outputNode)objRef.node.removeChild(outputNode);
}
