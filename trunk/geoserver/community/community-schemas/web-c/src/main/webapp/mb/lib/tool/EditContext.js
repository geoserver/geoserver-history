mapbuilder.loadScript(baseDir+"/tool/ToolBase.js");
function EditContext(toolNode,model){
ToolBase.apply(this,new Array(toolNode,model));
var styleUrl=baseDir+"/tool/xsl/wmc_AddResource.xsl";this.stylesheet=new XslProcessor(styleUrl);
for(var j=0;j<toolNode.childNodes.length;j++){
if(toolNode.childNodes[j].firstChild&&toolNode.childNodes[j].firstChild.nodeValue){
this.stylesheet.setParameter(toolNode.childNodes[j].nodeName,toolNode.childNodes[j].firstChild.nodeValue);
}
}
this.addNodeToModel=function(featureName){
var feature=this.model.getFeatureNode(featureName);
this.stylesheet.setParameter("version",this.model.getVersion());
this.stylesheet.setParameter("serverUrl",this.model.getServerUrl("GetMap","get"));
this.stylesheet.setParameter("serverTitle",this.model.getServerTitle());
this.stylesheet.setParameter("serviceName","wms");this.stylesheet.setParameter("format",this.model.getImageFormat());
var newNode=this.stylesheet.transformNodeToObject(feature);
Sarissa.setXpathNamespaces(newNode,this.targetModel.namespace);
if(this.debug)alert(newNode.xml);
this.targetModel.setParam('addLayer',newNode.documentElement);
}
this.addLayerFromCat=function(featureName){
var feature=this.model.getFeatureNode(featureName);
var newNode=this.stylesheet.transformNodeToObject(feature);
Sarissa.setXpathNamespaces(newNode,this.targetModel.namespace);
if(this.debug)alert(newNode.xml);
this.targetModel.setParam('addLayer',newNode.documentElement);
}
this.moveNode=function(objRef,featureName){
}
this.model.addListener("MoveNode",this.addNodeToModel,this);
this.deleteNode=function(objRef,featureName){
}
this.model.addListener("DeleteNode",this.addNodeToModel,this);
}
