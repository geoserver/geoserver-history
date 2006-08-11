function GeoRSS(modelNode,parent){
ModelBase.apply(this,new Array(modelNode,parent));
this.initItems=function(objRef){
var items=objRef.doc.selectNodes("rdf:RDF/rss:item");
for(var i=0;i<items.length;++i){
var item=items[i];
item.setAttribute("id","RSS_Item_"+mbIds.getId());
}
}
this.addFirstListener("loadModel",this.initItems,this);
this.getFeatureNodes=function(){
return this.doc.selectNodes(this.nodeSelectXpath);
}
this.getFeatureName=function(featureNode){
var labelNode=featureNode.selectSingleNode("rss:title");
return labelNode?labelNode.firstChild.nodeValue:"No RSS title";
}
this.getFeatureId=function(featureNode){
return featureNode.getAttribute("id")?featureNode.getAttribute("id"):"no_id";
}
this.getFeaturePoint=function(featureNode){
if(featureNode.selectSingleNode("geo:long")){
var pointX=featureNode.selectSingleNode("geo:long").firstChild.nodeValue;
var pointY=featureNode.selectSingleNode("geo:lat").firstChild.nodeValue;
return new Array(pointX,pointY);
}else{
return new Array(0,0);}
}
}
