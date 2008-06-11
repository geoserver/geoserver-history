function WpsCapabilities(modelNode,parent){
ModelBase.apply(this,new Array(modelNode,parent));
this.namespace="xmlns:wps='http://www.opengis.net/wps' xmlns:ows='http://www.opengis.net/ows' xmlns:xlink='http://www.w3.org/1999/xlink'";
this.getServerUrl=function(requestName,method,feature){
var requestParts=requestName.split(':');
var xpath="/wps:Capabilities/ows:OperationsMetadata/ows:Operation[@name='"+requestParts[1]+"']";
if(method.toLowerCase()=="post"){
xpath+="/ows:DCP/ows:HTTP/ows:Post";
}else{
xpath+="/ows:DCP/ows:HTTP/ows:Get";
}
var urlNode=this.doc.selectSingleNode(xpath);
if(urlNode){
return urlNode.getAttribute("xlink:href");
}else{
return null;
}
}
this.getVersion=function(){
var xpath="/wps:Capabilities";
return this.doc.selectSingleNode(xpath).getAttribute("version");
}
this.getMethod=function(){
return this.method;
}
this.getFeatureNode=function(featureName){
return this.doc.selectSingleNode(this.nodeSelectXpath+"[wps:Name='"+featureName+"']");
}
}
