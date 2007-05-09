function FeatureCollection(modelNode,parent){
ModelBase.apply(this,new Array(modelNode,parent));
if(!this.namespace){
this.namespace="xmlns:gml='http://www.opengis.net/gml' xmlns:wfs='http://www.opengis.net/wfs'";
}
this.convertCoords=function(objRef){
if(objRef.doc&&objRef.containerModel&&objRef.containerModel.doc){
var coordNodes=objRef.doc.selectNodes("//gml:coordinates");
if(coordNodes.length>0&&objRef.containerModel){
var srsNode=coordNodes[0].selectSingleNode("ancestor-or-self::*/@srsName");
if(srsNode.nodeValue.toUpperCase()!=objRef.containerModel.getSRS().toUpperCase()){
var sourceProj=new Proj(srsNode.nodeValue);
objRef.setParam("modelStatus","converting coordinates");
var containerProj=new Proj(objRef.containerModel.getSRS());
for(var i=0;i<coordNodes.length;++i){
var coords=coordNodes[i].firstChild.nodeValue;
var coordsArray=coords.split(' ');
var newCoords='';
for(var j=0;j<coordsArray.length;++j){
var xy=coordsArray[j].split(',');
if(xy.length==2){
var llTemp=sourceProj.Inverse(xy);
xy=containerProj.Forward(llTemp);
newCoords+=xy.join(',')+' ';
}
}
coordNodes[i].firstChild.nodeValue=newCoords;
}
}
}
}
}
this.addFirstListener("loadModel",this.convertCoords,this);
if(this.containerModel)this.containerModel.addListener("loadModel",this.convertCoords,this);
this.setHidden=function(featureName,hidden){
this.hidden=hidden;
this.callListeners("hidden",featureName);
}
this.getHidden=function(layerName){
return this.hidden;
}
this.hidden=false;
this.getFeatureNodes=function(){
return this.doc.selectNodes(this.nodeSelectXpath);
}
this.getFeatureName=function(featureNode){
var labelNode=featureNode.selectSingleNode("topp:CITY_NAME");return labelNode?labelNode.firstChild.nodeValue:"No RSS title";
}
this.getFeatureId=function(featureNode){
return featureNode.getAttribute("fid")?featureNode.getAttribute("fid"):"no_id";
}
this.getFeaturePoint=function(featureNode){
var coordSelectXpath="topp:the_geom/gml:MultiPoint/gml:pointMember/gml:Point/gml:coordinates";var coords=featureNode.selectSingleNode(coordSelectXpath);
if(coords){
var point=coords.firstChild.nodeValue.split(',');
return point
}else{
return new Array(0,0);}
}
}
