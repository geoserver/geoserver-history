mapbuilder.loadScript(baseDir+"/widget/WidgetBaseXSL.js");
function Legend(widgetNode,model){
WidgetBaseXSL.apply(this,new Array(widgetNode,model));
this.prePaint=function(objRef){
if(objRef.model.featureName){
objRef.stylesheet.setParameter("featureName",objRef.model.featureName);
objRef.stylesheet.setParameter("hidden",objRef.model.getHidden(objRef.model.featureName).toString());
}
}
this.selectLayer=function(objRef,layer){
objRef.model.setParam('selectedLayer',layer);
}
this.model.addListener("deleteLayer",this.paint,this);
this.model.addListener("moveLayerUp",this.paint,this);
this.model.addListener("moveLayerDown",this.paint,this);
this.model.addListener("addLayer",this.paint,this);
}
