mapbuilder.loadScript(baseDir+"/widget/WidgetBaseXSL.js");
function ModelSwitcher(widgetNode,model){
WidgetBase.apply(this,new Array(widgetNode,model));
this.switchMap=function(targetModel,modelUrl){
this.bbox=config.objects.mainMap.getBoundingBox();
this.targetModel.addListener("contextLoaded",this.setExtent,this);
window.cgiArgs["bbox"]=""+this.bbox[0]+","+this.bbox[1]+","+this.bbox[2]+","+this.bbox[3];
config.loadModel(targetModel,modelUrl);
}
this.setExtent=function(objRef){
}
}
