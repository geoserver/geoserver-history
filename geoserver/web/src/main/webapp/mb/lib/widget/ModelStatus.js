mapbuilder.loadScript(baseDir+"/widget/WidgetBaseXSL.js");
function ModelStatus(widgetNode,model){
WidgetBaseXSL.apply(this,new Array(widgetNode,model));
this.prePaint=function(objRef){
objRef.stylesheet.setParameter("statusMessage",objRef.targetModel.getParam("modelStatus"));
}
this.model.addListener("modelStatus",this.paint,this);
}
