mapbuilder.loadScript(baseDir+"/widget/WidgetBaseXSL.js");
function WmsCapabilitiesImport(widgetNode,model){
WidgetBaseXSL.apply(this,new Array(widgetNode,model));
this.onKeyPress=function(e){
var url;
var keycode;
if(e.which){
keycode=e.which;
url=e.currentTarget.value;
}else{
keycode=window.event.keyCode;
url=window.event.srcElement.value;
}
if(keycode==13){
capabilities=Sarissa.getDomDocument();
capabilities.async=false;
capabilities.load(url);
alert("capabilities="+capabilities.xml);
xsl=Sarissa.getDomDocument();
xsl.async=false;
xsl.load(baseDir+"/widget/wms/WMSCapabilities2Context.xsl");
alert("xsl="+xsl.xml);
context=Sarissa.getDomDocument();
capabilities.transformNodeToObject(xsl,context);
alert("context="+context.xml);
this.model.loadModelNode(context);
}
}
}
