mapbuilder.loadScript(baseDir+"/widget/WidgetBaseXSL.js");
function SelectTimeFrame(widgetNode,model){
WidgetBaseXSL.apply(this,new Array(widgetNode,model));
this.setFirstFrame=function(index){
this.model.setParam("stopLoop");
this.model.setParam("firstFrame",index);
}
this.setLastFrame=function(index){
var timestampList=this.model.timestampList;
if(index>timestampList.firstFrame){
timestampList.lastFrame=index;
}else{
alert("last frame must be after the first frame");
}
this.model.setParam("stopLoop");
}
}
