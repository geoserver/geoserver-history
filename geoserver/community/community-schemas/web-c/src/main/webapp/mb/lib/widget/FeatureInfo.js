mapbuilder.loadScript(baseDir+"/widget/WidgetBaseXSL.js");
function FeatureInfo(widgetNode,model){
WidgetBaseXSL.apply(this,new Array(widgetNode,model));
this.setAttr=function(objRef,xpath,value){
objRef.model.setXpathValue(objRef.model,xpath,value);
}
}
