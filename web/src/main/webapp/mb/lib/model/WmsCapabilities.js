function WmsCapabilities(modelNode,parent){
ModelBase.apply(this,new Array(modelNode,parent));
this.namespace="xmlns:wms='http://www.opengis.net/wms' xmlns:xlink='http://www.w3.org/1999/xlink'";
this.getServerUrl=function(requestName,method,feature){
var version=this.getVersion();
if(version=="1.0.0"){
requestName=requestName.substring(3);var xpath="/WMT_MS_Capabilities/Capability/Request/"+requestName;
if(method.toLowerCase()=="post"){
xpath+="/DCPType/HTTP/Post";
}else{
xpath+="/DCPType/HTTP/Get";
}
return this.doc.selectSingleNode(xpath).getAttribute("onlineResource");
}else{
var xpath="/WMT_MS_Capabilities/Capability/Request/"+requestName;
if(method.toLowerCase()=="post"){
xpath+="/DCPType/HTTP/Post/OnlineResource";
}else{
xpath+="/DCPType/HTTP/Get/OnlineResource";
}
return this.doc.selectSingleNode(xpath).getAttribute("xlink:href");
}
}
this.getVersion=function(){
var xpath="/WMT_MS_Capabilities";
return this.doc.selectSingleNode(xpath).getAttribute("version");
}
this.getServerTitle=function(){
var xpath="/WMT_MS_Capabilities/Service/Title";
var node=this.doc.selectSingleNode(xpath);
return node.firstChild.nodeValue;
}
this.getImageFormat=function(){
var version=this.getVersion();
if(version=="1.0.0"){
var xpath="/WMT_MS_Capabilities/Capability/Request/Map/Format";var node=this.doc.selectSingleNode(xpath);
if(_SARISSA_IS_IE){
return "image/"+node.firstChild.baseName.toLowerCase();
}
else{
return "image/"+node.firstChild.localName.toLowerCase();
}
}else{
var xpath="/WMT_MS_Capabilities/Capability/Request/GetMap/Format";
var node=this.doc.selectSingleNode(xpath);
return node.firstChild.nodeValue;
}
}
this.getServiceName=function(){
var xpath="/WMT_MS_Capabilities/Service/Name";
var node=this.doc.selectSingleNode(xpath);
return node.firstChild.nodeValue;
}
this.getFeatureNode=function(featureName){
return this.doc.selectSingleNode(this.nodeSelectXpath+"[Name='"+featureName+"']");
}
}
