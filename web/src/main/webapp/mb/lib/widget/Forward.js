mapbuilder.loadScript(baseDir+"/widget/ButtonBase.js");
function Forward(widgetNode,model){
ButtonBase.apply(this,new Array(widgetNode,model));
this.doSelect=function(selected,objRef){
if(selected){
this.targetModel.setParam("historyForward");
var nextExtent=objRef.targetModel.nextExtent;
if(nextExtent){
this.targetModel.setParam("historyStop");
objRef.targetModel.extent.zoomToBox(nextExtent[0],nextExtent[1]);
this.targetModel.setParam("historyStart");
}
}
}
}
