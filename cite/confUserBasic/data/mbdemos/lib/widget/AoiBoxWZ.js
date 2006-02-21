mapbuilder.loadScript(baseDir+"/widget/MapContainerBase.js");
mapbuilder.loadScript(baseDir+"/util/wz_jsgraphics/wz_jsgraphics.js");
function AoiBoxWZ(widgetNode,model){
WidgetBase.apply(this,new Array(widgetNode,model));
this.lineWidth=widgetNode.selectSingleNode("mb:lineWidth").firstChild.nodeValue;
this.lineColor=widgetNode.selectSingleNode("mb:lineColor").firstChild.nodeValue;
this.crossSize=widgetNode.selectSingleNode("mb:crossSize").firstChild.nodeValue;
this.paint=function(objRef){
var outputNode=document.getElementById(objRef.outputNodeId);
if(!outputNode){
outputNode=document.createElement("DIV");
outputNode.setAttribute("id",objRef.outputNodeId);
outputNode.style.position="relative";
objRef.node.appendChild(outputNode);
}
outputNode.style.left=0;
outputNode.style.top=0;
if(!objRef.jg){
objRef.jg=new jsGraphics(objRef.outputNodeId);
objRef.jg.setColor(objRef.lineColor);
objRef.jg.setStroke(parseInt(objRef.lineWidth));
}
var aoiBox=objRef.model.getParam("aoi");
if(aoiBox){
var ul=objRef.model.extent.getPL(aoiBox[0]);
var lr=objRef.model.extent.getPL(aoiBox[1]);
var width=lr[0]-ul[0];
var height=lr[1]-ul[1];
objRef.jg.clear();
if((width<objRef.crossSize)&&(height<objRef.crossSize)){
var x=(lr[0]+ul[0])/2;
var y=(lr[1]+ul[1])/2;
var c=objRef.crossSize/2;
objRef.jg.drawLine(x+c,y,x-c,y);
objRef.jg.drawLine(x,y+c,x,y-c);
}else{
objRef.jg.drawRect(ul[0],ul[1],width,height);
}
objRef.jg.paint();
}
}
this.model.addListener("aoi",this.paint,this);
MapContainerBase.apply(this,new Array(widgetNode,model));
this.clearAoiBox=function(objRef){
if(objRef.jg)objRef.jg.clear();
}
this.model.addListener("bbox",this.clearAoiBox,this);
this.refresh=function(objRef){
objRef.clearAoiBox(objRef);
objRef.jg=null;
}
this.model.addListener("newModel",this.refresh,this);
}
