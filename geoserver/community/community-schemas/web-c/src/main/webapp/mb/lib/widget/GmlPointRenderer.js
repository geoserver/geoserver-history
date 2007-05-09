mapbuilder.loadScript(baseDir+"/model/Proj.js");
mapbuilder.loadScript(baseDir+"/widget/MapContainerBase.js");
function GmlPointRenderer(widgetNode,model){
WidgetBase.apply(this,new Array(widgetNode,model));
MapContainerBase.apply(this,new Array(widgetNode,model));
this.normalImage=widgetNode.selectSingleNode("mb:normalImage").firstChild.nodeValue; 
this.highlightImage=widgetNode.selectSingleNode("mb:highlightImage").firstChild.nodeValue; 
this.model.addListener("refresh",this.paint,this); 
this.highlight=function(objRef,featureId){
var normalImageDiv=document.getElementById(featureId+"_normal");
normalImageDiv.style.visibility="hidden";
var highlightImageDiv=document.getElementById(featureId+"_highlight");
highlightImageDiv.style.visibility="visible";
}
this.model.addListener("highlightFeature",this.highlight,this);
this.dehighlight=function(objRef,featureId){
var normalImageDiv=document.getElementById(featureId+"_normal");
normalImageDiv.style.visibility="visible";
var highlightImageDiv=document.getElementById(featureId+"_highlight");
highlightImageDiv.style.visibility="hidden";
}
this.clearFeatures=function(){
var features=this.model.getFeatureNodes();
for(var i=0;i<features.length;++i){
var feature=features[i];
var itemId=this.model.getFeatureId(feature);var normalImageDiv=document.getElementById(itemId+"_normal");
var highlightImageDiv=document.getElementById(itemId+"_highlight");
if(normalImageDiv)
this.node.removeChild(normalImageDiv);
if(highlightImageDiv)
this.node.removeChild(highlightImageDiv);
}
}
this.model.addListener("dehighlightFeature",this.dehighlight,this);
}
GmlPointRenderer.prototype.paint=function(objRef){
if(objRef.model.doc&&objRef.node&&objRef.containerModel.doc){
var containerProj=new Proj(objRef.containerModel.getSRS());
objRef.clearFeatures();
var features=objRef.model.getFeatureNodes();
for(var i=0;i<features.length;++i){
var feature=features[i];
var title=objRef.model.getFeatureName(feature);
var itemId=objRef.model.getFeatureId(feature);var point=objRef.model.getFeaturePoint(feature);
point=containerProj.Forward(point);
point=objRef.containerModel.extent.getPL(point);
var normalImageDiv=document.getElementById(itemId+"_normal");
var highlightImageDiv=document.getElementById(itemId+"_highlight");
if(!normalImageDiv){
normalImageDiv=document.createElement("DIV");
normalImageDiv.setAttribute("id",itemId+"_normal");
normalImageDiv.style.position="absolute";
normalImageDiv.style.visibility="visible";
normalImageDiv.style.zIndex=300;
var newImage=document.createElement("IMG");
newImage.src=config.skinDir+objRef.normalImage;
newImage.title=title;
normalImageDiv.appendChild(newImage);
objRef.node.appendChild(normalImageDiv);
highlightImageDiv=document.createElement("DIV");
highlightImageDiv.setAttribute("id",itemId+"_highlight");
highlightImageDiv.style.position="absolute";
highlightImageDiv.style.visibility="hidden";
highlightImageDiv.style.zIndex=301;var newImage=document.createElement("IMG");
newImage.src=config.skinDir+objRef.highlightImage;
newImage.title=title;
highlightImageDiv.appendChild(newImage);
objRef.node.appendChild(highlightImageDiv);
}
normalImageDiv.style.left=point[0];
normalImageDiv.style.top=point[1];
highlightImageDiv.style.left=point[0];
highlightImageDiv.style.top=point[1];
}
}
}
