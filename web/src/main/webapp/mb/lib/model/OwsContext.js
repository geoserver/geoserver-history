function OwsContext(modelNode,parent){
ModelBase.apply(this,new Array(modelNode,parent));
this.namespace="xmlns:wmc='http://www.opengis.net/context' xmlns:ows='http://www.opengis.net/ows' xmlns:ogc='http://www.opengis.net/ogc' xmlns:xsl='http://www.w3.org/1999/XSL/Transform' xmlns:xlink='http://www.w3.org/1999/xlink'";
this.setHidden=function(layerName,hidden){
var hiddenValue="0";
if(hidden)hiddenValue="1";
var layer=this.getFeatureNode(layerName);
layer.setAttribute("hidden",hiddenValue);
this.callListeners("hidden",layerName);
}
this.getHidden=function(layerName){
var hidden=1;
var layer=this.getFeatureNode(layerName)
return layer.getAttribute("hidden");
}
this.getBoundingBox=function(){
var lowerLeft=this.doc.selectSingleNode("/wmc:OWSContext/wmc:General/ows:BoundingBox/ows:LowerCorner");
var upperRight=this.doc.selectSingleNode("/wmc:OWSContext/wmc:General/ows:BoundingBox/ows:UpperCorner");
var strBbox=new String(lowerLeft.firstChild.nodeValue+" "+upperRight.firstChild.nodeValue).split(" ");
var bbox=new Array();
for(i=0;i<strBbox.length;++i){
bbox[i]=parseFloat(strBbox[i]);
}
return bbox;
}
this.setBoundingBox=function(boundingBox){
var lowerLeft=this.doc.selectSingleNode("/wmc:OWSContext/wmc:General/ows:BoundingBox/ows:LowerCorner");
lowerLeft.firstChild.nodeValue=boundingBox[0]+" "+boundingBox[1];
var upperRight=this.doc.selectSingleNode("/wmc:OWSContext/wmc:General/ows:BoundingBox/ows:UpperCorner");
upperRight.firstChild.nodeValue=boundingBox[2]+" "+boundingBox[3];
this.callListeners("bbox");
}
this.setSRS=function(srs){
var bbox=this.doc.selectSingleNode("/wmc:OWSContext/wmc:General/ows:BoundingBox");
bbox.setAttribute("crs",srs);
this.callListeners("srs");
}
this.getSRS=function(){
var bbox=this.doc.selectSingleNode("/wmc:OWSContext/wmc:General/ows:BoundingBox");
srs=bbox.getAttribute("crs");
return srs;
}
this.getWindowWidth=function(){
var win=this.doc.selectSingleNode("/wmc:OWSContext/wmc:General/wmc:Window");
width=win.getAttribute("width");
return width;
}
this.setWindowWidth=function(width){
var win=this.doc.selectSingleNode("/wmc:OWSContext/wmc:General/wmc:Window");
win.setAttribute("width",width);
this.callListeners("resize");
}
this.getWindowHeight=function(){
var win=this.doc.selectSingleNode("/wmc:OWSContext/wmc:General/wmc:Window");
height=win.getAttribute("height");
return height;
}
this.setWindowHeight=function(height){
var win=this.doc.selectSingleNode("/wmc:OWSContext/wmc:General/wmc:Window");
win.setAttribute("height",height);
this.callListeners("resize");
}
this.getServerUrl=function(requestName,method,feature){
return feature.selectSingleNode("wmc:Server/wmc:OnlineResource").getAttribute("xlink:href");
}
this.getVersion=function(feature){ 
return feature.selectSingleNode("wmc:Server").getAttribute("version");
}
this.getMethod=function(feature){
return feature.selectSingleNode("wmc:Server/wmc:OnlineResource").getAttribute("wmc:method");
}
this.getFeatureNode=function(featureName){
return this.doc.selectSingleNode(this.nodeSelectXpath+"/*[wmc:Name='"+featureName+"']");
}
this.loadFeatures=function(objRef){
var nodeSelectXpath=objRef.nodeSelectXpath+"/wmc:FeatureType[wmc:Server/@service='OGC:WFS']/wmc:Name";
var featureList=objRef.doc.selectNodes(nodeSelectXpath);
for(var i=0;i<featureList.length;i++){
var featureName=featureList[i].firstChild.nodeValue;
objRef.setParam('wfs_GetFeature',featureName);
}
}
this.addListener("loadModel",this.loadFeatures,this);
this.setRequestParameters=function(featureName,requestStylesheet){
var feature=this.getFeatureNode(featureName);
if(feature.selectSingleNode("ogc:Filter")){
requestStylesheet.setParameter("filter",escape(Sarissa.serialize(feature.selectSingleNode("ogc:Filter"))));
}
}
this.getQueryableLayers=function(){
var listNodeArray=this.doc.selectNodes("/wmc:OWSContext/wmc:ResourceList/wmc:Layer[attribute::queryable='1']/wmc:Name");
return listNodeArray;
}
this.getAllLayers=function(){
var listNodeArray=this.doc.selectNodes("/wmc:OWSContext/wmc:ResourceList/wmc:Layer");
return listNodeArray;
}
this.getLayer=function(layerName){
var layer=this.doc.selectSingleNode("/wmc:OWSContext/wmc:ResourceList/wmc:Layer[wmc:Name='"+layerName+"']");
return layer;
}
}
