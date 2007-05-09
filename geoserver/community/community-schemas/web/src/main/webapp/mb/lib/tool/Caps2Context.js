function Caps2Context(toolNode,model){
ToolBase.apply(this,new Array(toolNode,model));
var styleUrl=baseDir+"/tool/xsl/Caps2Context.xsl";
this.stylesheet=new XslProcessor(styleUrl,model.namespace);
for(var j=0;j<toolNode.childNodes.length;j++){
if(toolNode.childNodes[j].firstChild&&toolNode.childNodes[j].firstChild.nodeValue){
this.stylesheet.setParameter(toolNode.childNodes[j].nodeName,toolNode.childNodes[j].firstChild.nodeValue);
}
}
this.mapAllLayers=function(objRef){
objRef.stylesheet.setParameter("selectedLayer",'');
var newContext=objRef.stylesheet.transformNodeToObject(objRef.model.doc);
objRef.targetModel.setParam("newModel",null);
objRef.targetModel.url='';
objRef.targetModel.doc=newContext;
objRef.targetModel.finishLoading();
}
this.model.addListener("mapAllLayers",this.mapAllLayers,this);
this.mapSingleLayer=function(objRef,layerName){
objRef.stylesheet.setParameter("selectedLayer",layerName);
var newContext=objRef.stylesheet.transformNodeToObject(objRef.model.doc);
objRef.targetModel.setParam("newModel",null);
objRef.targetModel.url='';
objRef.targetModel.doc=newContext;
objRef.targetModel.finishLoading();
}
this.model.addListener("mapLayer",this.mapSingleLayer,this);
}
