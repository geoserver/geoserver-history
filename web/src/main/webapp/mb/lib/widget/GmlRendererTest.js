mapbuilder.loadScript(baseDir+"/widget/MapContainerBase.js");
function GmlRendererTest(widgetNode,model){
this.paint=function(objRef){
var features=objRef.model.doc.selectNodes("//gml:featureMember");
alert("pretending to paint:"+features.length+" features"+Sarissa.serialize(objRef.model.doc));
}
var base=new MapContainerBase(this,widgetNode,model)
}
