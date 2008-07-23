mapbuilder.loadScript(baseDir+"/widget/WidgetBase.js");
function Loading(widgetNode,model){
WidgetBase.apply(this,new Array(widgetNode,model));
this.paint=function(objRef){
while(objRef.node.childNodes.length>0){
objRef.node.removeChild(objRef.node.childNodes[0]);
}
}
this.model.addListener("refresh",this.paint,this);
}
