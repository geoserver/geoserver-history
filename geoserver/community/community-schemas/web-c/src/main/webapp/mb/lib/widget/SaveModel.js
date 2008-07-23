mapbuilder.loadScript(baseDir+"/widget/WidgetBaseXSL.js");
function SaveModel(widgetNode,model){
WidgetBaseXSL.apply(this,new Array(widgetNode,model));
this.saveLink=function(objRef,fileUrl){
var modelAnchor=document.getElementById(objRef.model.id+"."+objRef.id+".modelUrl");
modelAnchor.href=fileUrl;
}
this.model.addListener("modelSaved",this.saveLink,this);
}
