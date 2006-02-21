mapbuilder.loadScript(baseDir+"/widget/WidgetBase.js");
function FeatureList(widgetNode,model){
WidgetBaseXSL.apply(this,new Array(widgetNode,model));
this.setAttr=function(objRef,xpath,value){
objRef.model.setXpathValue(objRef.model,xpath,value);
}
}
