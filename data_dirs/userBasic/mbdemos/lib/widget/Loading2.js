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
this.updateMessage=this.textMessage;
var mapContainerNode=widgetNode.selectSingleNode("mb:mapContainerId");
if(mapContainerNode){
this.containerNodeId=mapContainerNode.firstChild.nodeValue;
this.node=document.getElementById(this.containerNodeId);
}
this.model.addListener("newModel",this.paint,this);
this.model.addListener("loadModel",this.clear,this);
this.model.addListener("bbox",this.paint,this);
this.model.addListener("refresh",this.paint,this);
this.model.addListener("modelStatus",this.update,this);
}
Loading2.prototype.paint=function(objRef){
if(objRef.node){
var outputNode=document.getElementById(objRef.outputNodeId+"_loading");
if(!outputNode){
outputNode=document.createElement("div");
outputNode.setAttribute("id",objRef.outputNodeId+"_loading");
objRef.node.appendChild(outputNode);
}
outputNode.className="loadingIndicator";
outputNode.style.zindex=1000;
outputNode.style.position="absolute";
outputNode.style.left='0px';
outputNode.style.top='0px';
if(objRef.imageSrc){
var imageNode=document.getElementById(objRef.outputNodeId+"_imageNode");
if(!imageNode){
imageNode=document.createElement("img");
imageNode.setAttribute("id",objRef.outputNodeId+"_imageNode");
outputNode.appendChild(imageNode);
imageNode.style.zindex=1000;
}
imageNode.src=objRef.imageSrc;
}
if(objRef.updateMessage){
var messageNode=document.getElementById(objRef.outputNodeId+"_messageNode");
if(!messageNode){
messageNode=document.createElement("p");
messageNode.setAttribute("id",objRef.outputNodeId+"_messageNode");
outputNode.appendChild(messageNode);
}
messageNode.innerHTML=objRef.updateMessage;
}
}
}
Loading2.prototype.clear=function(objRef){
var outputNode=document.getElementById(objRef.outputNodeId+"_loading");
if(outputNode)objRef.node.removeChild(outputNode);
}
Loading2.prototype.update=function(objRef,message){
if(message){
objRef.updateMessage=message;
objRef.paint(objRef);
}else{
objRef.clear(objRef);
}
}
