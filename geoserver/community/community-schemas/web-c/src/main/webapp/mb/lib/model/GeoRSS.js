function GeoRSS(modelNode,parent){
ModelBase.apply(this,new Array(modelNode,parent));
this.initItems=function(objRef){
var items=objRef.doc.selectNodes("rdf:RDF/rss:item");
if(items.length==0){
items=objRef.doc.selectNodes("atom:feed/atom:entry");
}
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
if(labelNode==null)
labelNode=featureNode.selectSingleNode("atom:title");
return labelNode?labelNode.firstChild.nodeValue:"No RSS title";
}
this.getFeatureId=function(featureNode){
var id=featureNode.getAttribute("id")
if(id)
return id;
id=featureNode.getAttribute("atom:id")
if(id)
return id;
return "no_id";
}
this.getFeaturePoint=function(featureNode){
if(featureNode.selectSingleNode("geo:long")){
var pointX=featureNode.selectSingleNode("geo:long").firstChild.nodeValue;
var pointY=featureNode.selectSingleNode("geo:lat").firstChild.nodeValue;
return new Array(pointX,pointY);
}else{
var pos=featureNode.selectSingleNode("georss:where/gml:Point/gml:pos")
if(pos!=null){
var coordstr=pos.firstChild.nodeValue
var coords=coordstr.split(" ")
var pointX=coords[0]
var pointY=coords[1]
return new Array(pointX,pointY);
}else{
return new Array(0,0);}
}
}
this.getFeatureIcon=function(featureNode){
if(featureNode==null)
return null;
var icon=featureNode.selectSingleNode("atom:icon");
if(icon!=undefined){
return icon.firstChild.nodeValue;
}else{
return null;
}
}
}
