function Context(modelNode,parent){
ModelBase.apply(this,new Array(modelNode,parent));
this.namespace="xmlns:mb='http://mapbuilder.sourceforge.net/mapbuilder' xmlns:wmc='http://www.opengis.net/context' xmlns:xsl='http://www.w3.org/1999/XSL/Transform'";
this.setHidden=function(layerName,hidden){
var hiddenValue="0";
if(hidden)hiddenValue="1";
var layer=this.doc.selectSingleNode("/wmc:ViewContext/wmc:LayerList/wmc:Layer[wmc:Name='"+layerName+"']");
if(layer)layer.setAttribute("hidden",hiddenValue);
this.callListeners("hidden",layerName);
}
this.getHidden=function(layerName){
var hidden=1;
var layer=this.doc.selectSingleNode("/wmc:ViewContext/wmc:LayerList/wmc:Layer[wmc:Name='"+layerName+"']");
if(layer)hidden=layer.getAttribute("hidden");
return hidden;
}
this.getBoundingBox=function(){
var boundingBox=this.doc.selectSingleNode("/wmc:ViewContext/wmc:General/wmc:BoundingBox");
bbox=new Array();
bbox[0]=parseFloat(boundingBox.getAttribute("minx"));
bbox[1]=parseFloat(boundingBox.getAttribute("miny"));
bbox[2]=parseFloat(boundingBox.getAttribute("maxx"));
bbox[3]=parseFloat(boundingBox.getAttribute("maxy"));
return bbox;
}
this.setBoundingBox=function(boundingBox){
var bbox=this.doc.selectSingleNode("/wmc:ViewContext/wmc:General/wmc:BoundingBox");
bbox.setAttribute("minx",boundingBox[0]);
bbox.setAttribute("miny",boundingBox[1]);
bbox.setAttribute("maxx",boundingBox[2]);
bbox.setAttribute("maxy",boundingBox[3]);
this.callListeners("bbox",boundingBox);
}
this.initBbox=function(objRef){
if(window.cgiArgs["bbox"]){var bbox=window.cgiArgs["bbox"].split(',');
var ul=new Array(parseFloat(bbox[0]),parseFloat(bbox[3]));
var lr=new Array(parseFloat(bbox[2]),parseFloat(bbox[1]));
objRef.extent.zoomToBox(ul,lr);
}
}
this.addListener("contextLoaded",this.initBbox,this);
this.initAoi=function(objRef){
if(window.cgiArgs["aoi"]){var aoi=window.cgiArgs["aoi"].split(',');
objRef.setParam("aoi",new Array(new Array(aoi[0],aoi[3]),new Array(aoi[2],aoi[1])));
}
}
this.addListener("loadModel",this.initAoi,this);
this.setSRS=function(srs){
var bbox=this.doc.selectSingleNode("/wmc:ViewContext/wmc:General/wmc:BoundingBox");
bbox.setAttribute("SRS",srs);
this.callListeners("srs");
}
this.getSRS=function(){
var bbox=this.doc.selectSingleNode("/wmc:ViewContext/wmc:General/wmc:BoundingBox");
srs=bbox.getAttribute("SRS");
return srs;
}
this.getWindowWidth=function(){
var win=this.doc.selectSingleNode("/wmc:ViewContext/wmc:General/wmc:Window");
return win.getAttribute("width");
}
this.setWindowWidth=function(width){
var win=this.doc.selectSingleNode("/wmc:ViewContext/wmc:General/wmc:Window");
win.setAttribute("width",width);
this.callListeners("resize");
}
this.getWindowHeight=function(){
var win=this.doc.selectSingleNode("/wmc:ViewContext/wmc:General/wmc:Window");
return win.getAttribute("height");
}
this.setWindowHeight=function(height){
var win=this.doc.selectSingleNode("/wmc:ViewContext/wmc:General/wmc:Window");
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
this.getQueryableLayers=function(){
var listNodeArray=this.doc.selectNodes("/wmc:ViewContext/wmc:LayerList/wmc:Layer[attribute::queryable='1']/wmc:Name");
return listNodeArray;
}
this.getAllLayers=function(){
var listNodeArray=this.doc.selectNodes("/wmc:ViewContext/wmc:LayerList/wmc:Layer");
return listNodeArray;
}
this.getLayer=function(layerName){
var layer=this.doc.selectSingleNode("/wmc:ViewContext/wmc:LayerList/wmc:Layer[wmc:Name='"+layerName+"']");
return layer;
}
this.addLayer=function(objRef,layerNode){
var parentNode=objRef.doc.selectSingleNode("/wmc:ViewContext/wmc:LayerList");
parentNode.appendChild(layerNode.cloneNode(true));
objRef.modified=true;
}
this.addFirstListener("addLayer",this.addLayer,this);
this.deleteLayer=function(objRef,layerName){
var deletedNode=objRef.getLayer(layerName);
if(!deletedNode){
alert("node note found; unable to delete node:"+layerName);
return;
}
deletedNode.parentNode.removeChild(deletedNode);
objRef.modified=true;
}
this.addFirstListener("deleteLayer",this.deleteLayer,this);
this.moveLayerUp=function(objRef,layerName){
var movedNode=objRef.getLayer(layerName);
var sibNode=movedNode.selectSingleNode("following-sibling::*");
if(!sibNode){
alert("can't move node past beginning of list:"+layerName);
return;
}
movedNode.parentNode.insertBefore(sibNode,movedNode);
objRef.modified=true;
}
this.addFirstListener("moveLayerUp",this.moveLayerUp,this);
this.moveLayerDown=function(objRef,layerName){
var movedNode=objRef.getLayer(layerName);
var listNodeArray=movedNode.selectNodes("preceding-sibling::*");var sibNode=listNodeArray[listNodeArray.length-1];
if(!sibNode){
alert("can't move node past beginning of list:"+layerName);
return;
}
movedNode.parentNode.insertBefore(movedNode,sibNode);
objRef.modified=true;
}
this.addFirstListener("moveLayerDown",this.moveLayerDown,this);
this.setExtension=function(extensionNode){
var extension=this.doc.selectSingleNode("/wmc:ViewContext/wmc:General/wmc:Extension");
if(!extension){
var general=this.doc.selectSingleNode("/wmc:ViewContext/wmc:General");
var newChild=createElementWithNS(this.doc,"Extension",'http://www.opengis.net/context');
extension=general.appendChild(newChild);
}
return extension.appendChild(extensionNode);
}
this.getExtension=function(){
return this.doc.selectSingleNode("/wmc:ViewContext/wmc:General/wmc:Extension");
}
this.initTimeExtent=function(objRef){
var timeNodes=objRef.doc.selectNodes("//wmc:Dimension[@name='time']");
for(var i=0;i<timeNodes.length;++i){
var extentNode=timeNodes[i];
objRef.timestampList=createElementWithNS(objRef.doc,"TimestampList",mbNsUrl);
var layerName=extentNode.parentNode.parentNode.selectSingleNode("wmc:Name").firstChild.nodeValue;
objRef.timestampList.setAttribute("layerName",layerName);
var times=extentNode.firstChild.nodeValue.split(",");for(var j=0;j<times.length;++j){
var params=times[j].split("/");if(params.length==3){
var start=setISODate(params[0]);
var stop=setISODate(params[1]);
var period=params[2];
var parts=period.match(/^P((\d*)Y)?((\d*)M)?((\d*)D)?T?((\d*)H)?((\d*)M)?((.*)S)?/);
for(var i=1;i<parts.length;++i){
if(!parts[i])parts[i]=0;
}
do{
var timestamp=createElementWithNS(objRef.doc,"Timestamp",mbNsUrl);
timestamp.appendChild(objRef.doc.createTextNode(getISODate(start)));
objRef.timestampList.appendChild(timestamp);
start.setFullYear(start.getFullYear()+parseInt(parts[2],10));
start.setMonth(start.getMonth()+parseInt(parts[4],10));
start.setDate(start.getDate()+parseInt(parts[6],10));
start.setHours(start.getHours()+parseInt(parts[8],10));
start.setMinutes(start.getMinutes()+parseInt(parts[10],10));
start.setSeconds(start.getSeconds()+parseFloat(parts[12]));
}while(start.getTime()<=stop.getTime());
}else{
var timestamp=createElementWithNS(objRef.doc,"Timestamp",mbNsUrl);
timestamp.appendChild(objRef.doc.createTextNode(times[j]));
objRef.timestampList.appendChild(timestamp);
}
}
objRef.setExtension(objRef.timestampList); 
}
}
this.addFirstListener("loadModel",this.initTimeExtent,this);
this.getCurrentTimestamp=function(layerName){
var index=this.getParam("timestamp");
return this.timestampList.childNodes[index].firstChild.nodeValue;
}
}
