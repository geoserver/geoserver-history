mapbuilder.loadScript(baseDir+"/widget/WidgetBaseXSL.js");
function Timestamp(widgetNode,model){
WidgetBaseXSL.apply(this,new Array(widgetNode,model));
this.updateTimestamp=function(objRef,timestamp){
var inputEl=document.getElementById("timestampValue");
inputEl.value=objRef.model.timestampList.childNodes[timestamp].firstChild.nodeValue;
}
this.model.addListener("timestamp",this.updateTimestamp,this);
}
