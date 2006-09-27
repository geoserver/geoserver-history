mapbuilder.loadScript(baseDir+"/widget/ButtonBase.js");
function Back(widgetNode,model){
ButtonBase.apply(this,new Array(widgetNode,model));
this.doSelect=function(selected,objRef){
if(selected){
this.targetModel.setParam("historyBack");
var previousExtent=objRef.targetModel.previousExtent;
if(previousExtent){
this.targetModel.setParam("historyStop");
objRef.targetModel.extent.zoomToBox(previousExtent[0],previousExtent[1]);
this.targetModel.setParam("historyStart");
}
}
}
}
