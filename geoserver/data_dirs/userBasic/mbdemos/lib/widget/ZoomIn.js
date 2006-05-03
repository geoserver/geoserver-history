mapbuilder.loadScript(baseDir+"/widget/ButtonBase.js");
function ZoomIn(widgetNode,model){
ButtonBase.apply(this,new Array(widgetNode,model));
this.zoomBy=4;
this.doAction=function(objRef,targetNode){
if(objRef.enabled){
var bbox=objRef.targetModel.getParam("aoi");
if(bbox!=null){
var extent=objRef.targetModel.extent;
var ul=bbox[0];
var lr=bbox[1];
if((ul[0]==lr[0])&&(ul[1]==lr[1])){
extent.centerAt(ul,extent.res[0]/objRef.zoomBy);
}else{
extent.zoomToBox(ul,lr);
}
}
}
}
this.setMouseListener=function(objRef){
if(objRef.mouseHandler){
objRef.mouseHandler.model.addListener('mouseup',objRef.doAction,objRef);
}
}
this.model.addListener("loadModel",this.setMouseListener,this);
}
