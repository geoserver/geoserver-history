mapbuilder.loadScript(baseDir+"/tool/Extent.js");
function MapContainerBase(widgetNode,model){
var mapContainerNode=widgetNode.selectSingleNode("mb:mapContainerId");
if(mapContainerNode){
this.containerNodeId=mapContainerNode.firstChild.nodeValue;
}else{
alert("MapContainerBase: required property mapContainerId missing in config:"+this.id);
}
var containerNode=document.getElementById(this.containerNodeId);
if(containerNode){
this.containerModel=containerNode.containerModel;
model.containerModel=containerNode.containerModel;
this.setContainerWidth=function(objRef){
objRef.node.style.width=objRef.containerModel.getWindowWidth()+'px';
objRef.node.style.height=objRef.containerModel.getWindowHeight()+'px';
if(this.stylesheet){
this.stylesheet.setParameter("width",objRef.containerModel.getWindowWidth());
this.stylesheet.setParameter("height",objRef.containerModel.getWindowHeight());
}
}
}else{
containerNode=document.createElement("DIV");
containerNode.setAttribute("id",this.containerNodeId);
containerNode.id=this.containerNodeId;
containerNode.style.position="relative";
containerNode.style.overflow="hidden";
containerNode.containerModel=this.model;
this.containerModel=this.model;
model.containerModel=containerNode.containerModel; 
this.setContainerWidth=function(objRef){
var fixedWidth=widgetNode.selectSingleNode("mb:fixedWidth");
if(fixedWidth){
fixedWidth=fixedWidth.firstChild.nodeValue;
var aspectRatio=objRef.containerModel.getWindowHeight()/objRef.containerModel.getWindowWidth();
var newHeight=Math.round(aspectRatio*fixedWidth);
objRef.containerModel.setWindowWidth(fixedWidth);
objRef.containerModel.setWindowHeight(newHeight);
}
objRef.node.style.width=objRef.containerModel.getWindowWidth()+'px';
objRef.node.style.height=objRef.containerModel.getWindowHeight()+'px';
if(this.stylesheet){
this.stylesheet.setParameter("width",objRef.containerModel.getWindowWidth());
this.stylesheet.setParameter("height",objRef.containerModel.getWindowHeight());
}
}
this.containerModel.extent=new Extent(this.containerModel);
this.containerModel.addListener("contextLoaded",this.containerModel.extent.firstInit,this.containerModel.extent);
this.containerModel.addListener("bbox",this.containerModel.extent.init,this.containerModel.extent);
this.containerModel.addListener("resize",this.containerModel.extent.init,this.containerModel.extent);
this.setTooltip=function(objRef,tooltip){
}
this.containerModel.addListener("tooltip",this.setTooltip,this);
this.eventHandler=function(ev){
if(window.event){
var p=window.event.clientX-getOffsetLeft(this)+document.documentElement.scrollLeft+document.body.scrollLeft;
var l=window.event.clientY-getOffsetTop(this)+document.documentElement.scrollTop+document.body.scrollTop;
this.evpl=new Array(p,l);
this.eventTarget=window.event.srcElement;
this.eventType=window.event.type;
this.altKey=window.event.altKey;
this.ctrlKey=window.event.ctrlKey;
this.shiftKey=window.event.shiftKey;
window.event.returnValue=false;
window.event.cancelBubble=true;
}else{
var p=ev.clientX+window.scrollX-getOffsetLeft(this);
var l=ev.clientY+window.scrollY-getOffsetTop(this);
this.evpl=new Array(p,l);
this.eventTarget=ev.target;
this.eventType=ev.type;
this.altKey=ev.altKey;
this.ctrlKey=ev.ctrlKey;
this.shiftKey=ev.shiftKey;
ev.stopPropagation();
}
this.containerModel.setParam(this.eventType,this);
return false;
}
this.eventHandler=this.eventHandler;
containerNode.onmousemove=this.eventHandler;
containerNode.onmouseout=this.eventHandler;
containerNode.onmouseover=this.eventHandler;
containerNode.onmousedown=this.eventHandler;
containerNode.onmouseup=this.eventHandler;
this.node.appendChild(containerNode);
}
this.node=document.getElementById(this.containerNodeId);
this.setContainerWidth=this.setContainerWidth;
this.containerModel.addFirstListener("loadModel",this.setContainerWidth,this);
this.containerModel.addListener("bbox",this.paint,this);
}
function getOffsetLeft(node){
var offsetLeft=0;
if(node==null){
return offsetLeft;
} 
else{
if(node.offsetLeft){
offsetLeft=node.offsetLeft+getOffsetLeft(node.offsetParent);
}
else{
offsetLeft=getOffsetLeft(node.offsetParent);
}
return offsetLeft;
}
} 
function getOffsetTop(node){
var offsetTop=0;
if(node==null){
return offsetTop;
}
else{
if(node.offsetTop){
offsetTop=node.offsetTop+getOffsetTop(node.offsetParent);
}
else{
offsetTop=getOffsetTop(node.offsetParent);
}
return offsetTop;
}
}
