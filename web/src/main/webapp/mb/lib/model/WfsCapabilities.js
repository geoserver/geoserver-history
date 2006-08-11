function WfsCapabilities(modelNode,parent){
ModelBase.apply(this,new Array(modelNode,parent));
this.namespace="xmlns:wfs='http://www.opengis.net/wfs'";
this.getServerUrl=function(requestName,method,feature){
var xpath="/wfs:WFS_Capabilities/wfs:Capability/wfs:Request/"+requestName;
if(method.toLowerCase()=="post"){
xpath+="/wfs:DCPType/wfs:HTTP/wfs:Post";
}else{
xpath+="/wfs:DCPType/wfs:HTTP/wfs:Get";
}
return this.doc.selectSingleNode(xpath).getAttribute("onlineResource");
}
this.getVersion=function(){
var xpath="/wfs:WFS_Capabilities";
return this.doc.selectSingleNode(xpath).getAttribute("version");
}
this.getMethod=function(){
return this.method;
}
this.getFeatureNode=function(featureName){
return this.doc.selectSingleNode(this.nodeSelectXpath+"[wfs:Name='"+featureName+"']");
}
}
