mapbuilder.loadScript(baseDir+"/widget/WidgetBaseXSL.js");
function Widget(widgetNode,model){
WidgetBaseXSL.apply(this,new Array(widgetNode,model));
}
