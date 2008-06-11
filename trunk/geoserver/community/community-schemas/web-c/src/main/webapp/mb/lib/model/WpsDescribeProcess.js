function WpsDescribeProcess(modelNode,parent){
ModelBase.apply(this,new Array(modelNode,parent));
this.namespace="xmlns:wps='http://www.opengis.net/wps' xmlns:ows='http://www.opengis.net/ows' xmlns:xlink='http://www.w3.org/1999/xlink'";
this.getServerUrl=function(requestName,method,feature){
return this.parentModel.getServerUrl(requestName,method,feature);
}
this.getVersion=function(){
var xpath="/wps:ProcessDescription";
return this.doc.selectSingleNode(xpath).getAttribute("version");
}
this.getMethod=function(){
return this.method;
}
this.getFeatureNode=function(featureName){
return this.doc.selectSingleNode(this.nodeSelectXpath+"[wps:Name='"+featureName+"']");
}
}
