mapbuilder.loadScript(baseDir+"/widget/WidgetBase.js");
mapbuilder.loadScript(baseDir+"/widget/MapContainerBase.js");
function MapPane(widgetNode,model){
WidgetBase.apply(this,new Array(widgetNode,model));
MapContainerBase.apply(this,new Array(widgetNode,model));
if(!this.stylesheet){
var styleNode=widgetNode.selectSingleNode("mb:stylesheet");
if(styleNode){
this.stylesheet=new XslProcessor(styleNode.firstChild.nodeValue,model.namespace);
}else{
this.stylesheet=new XslProcessor(baseDir+"/widget/"+widgetNode.nodeName+".xsl",model.namespace);
}
}
var loadingSrc=widgetNode.selectSingleNode("mb:loadingSrc");
if(loadingSrc){
this.loadingSrc=config.skinDir+loadingSrc.firstChild.nodeValue;
}else{
this.loadingSrc=config.skinDir+"/images/Loading.gif";
}
for(var j=0;j<widgetNode.childNodes.length;j++){
if(widgetNode.childNodes[j].firstChild
&&widgetNode.childNodes[j].firstChild.nodeValue)
{
this.stylesheet.setParameter(
widgetNode.childNodes[j].nodeName,
widgetNode.childNodes[j].firstChild.nodeValue);
}
}
this.stylesheet.setParameter("modelId",this.model.id);
this.stylesheet.setParameter("modelTitle",this.model.title);
this.stylesheet.setParameter("widgetId",this.id);
this.stylesheet.setParameter("skinDir",config.skinDir);
this.stylesheet.setParameter("lang",config.lang);
this.hiddenListener=function(objRef,layerName){
var vis="visible";
if(objRef.model.getHidden(layerName)=="1"){
vis="hidden";
}
var layerId=objRef.model.id+"_"+objRef.id+"_"+layerName;
var layer=document.getElementById(layerId);
if(layer){
layer.style.visibility=vis;
imgId="real"+layer.imgId;
img=document.getElementById(imgId);if(img)img.style.visibility=vis;
}
}
this.model.addListener("hidden",this.hiddenListener,this);
this.refreshWmsLayers=function(objRef){
objRef.d=new Date();
objRef.stylesheet.setParameter("uniqueId",objRef.d.getTime());
objRef.paint(objRef);
}
this.model.addListener("refreshWmsLayers",this.refreshWmsLayers,this);
this.model.addListener("refresh",this.paint,this);
this.model.addListener("addLayer",this.addLayer,this);
this.model.addListener("deleteLayer",this.deleteLayer,this);
this.model.addListener("moveLayerUp",this.moveLayerUp,this);
this.model.addListener("moveLayerDown",this.moveLayerDown,this);
}
MapPane.prototype.paint=function(objRef,refresh){
if(objRef.model.doc&&objRef.node&&(objRef.autoRefresh||refresh)){
objRef.stylesheet.setParameter("width",objRef.model.getWindowWidth());
objRef.stylesheet.setParameter("height",objRef.model.getWindowHeight());
objRef.stylesheet.setParameter("bbox",objRef.model.getBoundingBox().join(","));
objRef.stylesheet.setParameter("srs",objRef.model.getSRS());
if(objRef.debug)alert("painting:"+Sarissa.serialize(objRef.model.doc));
if(objRef.debug)alert("stylesheet:"+Sarissa.serialize(objRef.stylesheet.xslDom));
var tempDom=objRef.stylesheet.transformNodeToObject(objRef.model.doc);
var tempNodeList=tempDom.selectNodes("//img");
if(objRef.debug){
alert("painting:"+objRef.id+":"+s);
if(config.serializeUrl)postLoad(config.serializeUrl,s);
}
var outputNode=document.getElementById(objRef.outputNodeId);
if(!outputNode){
outputNode=document.createElement("div");
outputNode.setAttribute("id",objRef.outputNodeId);
outputNode.style.position="absolute"; 
objRef.node.appendChild(outputNode);
outputNode.style.left='0px';
outputNode.style.top='0px';
} 
var layers=objRef.model.getAllLayers();
if(!objRef.imageStack)objRef.imageStack=new Array(layers.length);
objRef.firstImageLoaded=false;
objRef.layerCount=layers.length;
for(var i=0;i<layers.length;i++){
if(!objRef.imageStack[i]){
objRef.imageStack[i]=new Image();
objRef.imageStack[i].objRef=objRef;
}
var newSrc=tempNodeList[i].getAttribute("src");
objRef.loadImgDiv(layers[i],newSrc,objRef.imageStack[i]);
}
var message="loading "+objRef.layerCount+" map layer"+((objRef.layerCount>1)?"s":"");
objRef.model.setParam("modelStatus",message);
}
}
MapPane.prototype.getLayerDivId=function(layerName){
return this.model.id+"_"+this.id+"_"+layerName;}
MapPane.prototype.addLayer=function(objRef,layerNode){
++objRef.layerCount;
var message="loading "+objRef.layerCount+" map layer"+((objRef.layerCount>1)?"s":"");
objRef.model.setParam("modelStatus",message);
objRef.stylesheet.setParameter("width",objRef.model.getWindowWidth());
objRef.stylesheet.setParameter("height",objRef.model.getWindowHeight());
objRef.stylesheet.setParameter("bbox",objRef.model.getBoundingBox().join(","));
objRef.stylesheet.setParameter("srs",objRef.model.getSRS());
var s=objRef.stylesheet.transformNodeToString(layerNode);
var tempNode=document.createElement("div");
tempNode.innerHTML=s;
var newSrc=tempNode.firstChild.firstChild.getAttribute("src"); 
objRef.imageStack.push(new Image());
objRef.imageStack[objRef.imageStack.length-1].objRef=objRef;
objRef.firstImageLoaded=true;
objRef.loadImgDiv(layerNode,newSrc,objRef.imageStack[objRef.imageStack.length-1]);
}
MapPane.prototype.deleteLayer=function(objRef,layerName){
var imgDivId=objRef.getLayerDivId(layerName); 
var imgDiv=document.getElementById(imgDivId);
var outputNode=document.getElementById(objRef.outputNodeId);
outputNode.removeChild(imgDiv);
}
MapPane.prototype.moveLayerUp=function(objRef,layerName){
var outputNode=document.getElementById(objRef.outputNodeId);
var imgDivId=objRef.getLayerDivId(layerName); 
var movedNode=document.getElementById(imgDivId);
var sibNode=movedNode.nextSibling;
if(!sibNode){
alert("can't move node past beginning of list:"+layerName);
return;
}
outputNode.insertBefore(sibNode,movedNode);
}
MapPane.prototype.moveLayerDown=function(objRef,layerName){
var outputNode=document.getElementById(objRef.outputNodeId);
var imgDivId=objRef.getLayerDivId(layerName); 
var movedNode=document.getElementById(imgDivId);
var sibNode=movedNode.previousSibling;
if(!sibNode){
alert("can't move node past end of list:"+layerName);
return;
}
outputNode.insertBefore(movedNode,sibNode);
}
MapPane.prototype.loadImgDiv=function(layerNode,newSrc,newImg){
var outputNode=document.getElementById(this.outputNodeId);
var layerName=layerNode.selectSingleNode("wmc:Name").firstChild.nodeValue; 
var layerHidden=(layerNode.getAttribute("hidden")==1)?true:false; 
var imageFormat="image/gif";
var imageFormatNode=layerNode.selectSingleNode("wmc:FormatList/wmc:Format[@current='1']"); 
if(imageFormatNode)imageFormat=imageFormatNode.firstChild.nodeValue; 
var imgDivId=this.getLayerDivId(layerName); 
var imgDiv=document.getElementById(imgDivId);
if(!imgDiv){
imgDiv=document.createElement("div");
imgDiv.setAttribute("id",imgDivId);
imgDiv.style.position="absolute"; 
imgDiv.style.visibility=(layerHidden)?"hidden":"visible";
imgDiv.style.top='0px'; 
imgDiv.style.left='0px';
imgDiv.imgId=Math.random().toString(); 
var domImg=document.createElement("img");
domImg.id="real"+imgDiv.imgId;
domImg.src=config.skinDir+"/images/Spacer.gif";
domImg.layerHidden=layerHidden;
imgDiv.appendChild(domImg);
outputNode.appendChild(imgDiv);
}
newImg.id=imgDiv.imgId;
newImg.hidden=layerHidden;
newImg.fixPng=false;
if(_SARISSA_IS_IE&&imageFormat=="image/png")newImg.fixPng=true;
newImg.onload=MapImgLoadHandler;
newImg.src=newSrc;
}
function MapImgLoadHandler(){
var oldImg=document.getElementById("real"+this.id);
if(!this.objRef.firstImageLoaded){
this.objRef.firstImageLoaded=true;
var outputNode=document.getElementById(this.objRef.outputNodeId);
var siblingImageDivs=outputNode.childNodes;
for(var i=0;i<siblingImageDivs.length;++i){
var sibImg=siblingImageDivs[i].firstChild;
sibImg.parentNode.style.visibility="hidden";
sibImg.style.visibility="hidden";if(_SARISSA_IS_IE)sibImg.src=config.skinDir+"/images/Spacer.gif";
}
if(_SARISSA_IS_IE)siblingImageDivs[0].firstChild.parentNode.parentNode.style.visibility="hidden";
outputNode.style.left='0px';
outputNode.style.top='0px';
}
--this.objRef.layerCount;
if(this.objRef.layerCount>0){
var message="loading "+this.objRef.layerCount+" map layers"
this.objRef.model.setParam("modelStatus",message);
}else{
this.objRef.model.setParam("modelStatus");
}
if(_SARISSA_IS_IE)oldImg.parentNode.parentNode.style.visibility="visible";
if(this.fixPng){
var vis=oldImg.layerHidden?"hidden":"visible";
oldImg.outerHTML=fixPNG(this,"real"+this.id);
if(!this.hidden){
fixImg=document.getElementById("real"+this.id);fixImg.style.visibility="visible"
}
}else{
oldImg.src=this.src;
oldImg.width=this.objRef.model.getWindowWidth();
oldImg.height=this.objRef.model.getWindowHeight();
if(!this.hidden){
oldImg.parentNode.style.visibility="visible";
oldImg.style.visibility="visible";}
}
}
