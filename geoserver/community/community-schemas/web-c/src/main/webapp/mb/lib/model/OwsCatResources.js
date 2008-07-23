mapbuilder.loadScript(baseDir+"/model/FeatureCollection.js");
function OwsCatResources(modelNode,parent){
FeatureCollection.apply(this,new Array(modelNode,parent));
this.namespace="xmlns:owscat='http://www.ec.gc.ca/owscat' xmlns:gml='http://www.opengis.net/gml' xmlns:wfs='http://www.opengis.net/wfs'";
}
OwsCatResources.prototype.getFeatureNode=function(featureName){
return this.doc.selectSingleNode(this.nodeSelectXpath+"[owscat:name='"+featureName+"']");
}
