mapbuilder.loadScript(baseDir+"/widget/MapContainerBase.js");
function MapImage(widgetNode,model){
WidgetBase.apply(this,new Array(widgetNode,model));
this.paint=function(objRef){
if(objRef.model.doc&&objRef.node){
objRef.prePaint(objRef);
var outputNode=document.getElementById(objRef.outputNodeId);
var tempNode=document.createElement("DIV");
tempNode.style.position="absolute";
tempNode.style.top=0;
tempNode.style.left=0;
tempNode.appendChild(objRef.model.doc);tempNode.setAttribute("id",objRef.outputNodeId);
if(outputNode){
objRef.node.replaceChild(tempNode,outputNode);
}else{
objRef.node.appendChild(tempNode);
}
objRef.postPaint(objRef);
}
}
this.model.addListener("refresh",this.paint,this);
MapContainerBase.apply(this,new Array(widgetNode,model));
this.prePaint=function(objRef){
objRef.model.doc.width=objRef.containerModel.getWindowWidth();
objRef.model.doc.height=objRef.containerModel.getWindowHeight();
}
}
