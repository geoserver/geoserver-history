function DragPanHandler(toolNode,model){
ToolBase.apply(this,new Array(toolNode,model));
this.mouseUpHandler=function(objRef,targetNode){
if(objRef.enabled){
if(objRef.dragging){
objRef.dragging=false;
if((objRef.deltaP==0)&&(objRef.deltaL==0))return;
var width=objRef.model.getWindowWidth();
var height=objRef.model.getWindowHeight();
var ul=objRef.model.extent.getXY(new Array(-objRef.deltaP,-objRef.deltaL));var lr=objRef.model.extent.getXY(new Array(width-objRef.deltaP,height-objRef.deltaL));objRef.model.setParam("aoi",new Array(ul,lr));
}
}
}
this.mouseDownHandler=function(objRef,targetNode){
if(objRef.enabled){
objRef.dragging=true;
objRef.anchorPoint=targetNode.evpl;
objRef.deltaP=0;
objRef.deltaL=0;
var images=targetNode.childNodes;
objRef.oldPos=new Array(images.length);
for(var i=0;i<images.length;i++){
var img=images.item(i);
var P=img.style.left;
var L=img.style.top;
if(P&&L)
objRef.oldPos[i]=new Array(parseInt(P),parseInt(L));
else
objRef.oldPos[i]=new Array(0,0);
}
}
}
this.mouseMoveHandler=function(objRef,targetNode){
if(objRef.enabled){
if(objRef.dragging){
objRef.deltaP=targetNode.evpl[0]-objRef.anchorPoint[0];
objRef.deltaL=targetNode.evpl[1]-objRef.anchorPoint[1];
var images=targetNode.childNodes;
for(var i=0;i<images.length;i++){
var img=images.item(i);
img.style.left=objRef.deltaP+objRef.oldPos[i][0]+'px';
img.style.top=objRef.deltaL+objRef.oldPos[i][1]+'px';
}
}
}
}
this.model.addListener('mousedown',this.mouseDownHandler,this);
this.model.addListener('mousemove',this.mouseMoveHandler,this);
this.model.addListener('mouseup',this.mouseUpHandler,this);
}
