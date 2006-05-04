mapbuilder.loadScript(baseDir+"/widget/ButtonBase.js");
function Reset(widgetNode,model){
ButtonBase.apply(this,new Array(widgetNode,model));
this.cursor="default"; 
this.initExtent=function(objRef){
objRef.originalExtent=new Extent(objRef.targetModel); 
objRef.originalExtent.init(objRef.originalExtent);
objRef.originalExtent.setResolution(new Array(objRef.targetModel.getWindowWidth(),objRef.targetModel.getWindowHeight()));
}
this.initReset=function(objRef){
objRef.targetModel.addListener("loadModel",objRef.initExtent,objRef);
}
this.model.addListener("init",this.initReset,this);
this.doSelect=function(selected,objRef){
if(selected){
var originalExtent=objRef.originalExtent;
objRef.targetModel.extent.centerAt(originalExtent.getCenter(),originalExtent.res[0]);
}
}
}
