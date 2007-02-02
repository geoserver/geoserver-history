mapbuilder.loadScript(baseDir+"/widget/EditButtonBase.js");
function EditPolygon(widgetNode,model){
EditButtonBase.apply(this,new Array(widgetNode,model));
this.doAction=function(objRef,targetNode){
if(objRef.enabled){
var point=objRef.mouseHandler.model.extent.getXY(targetNode.evpl);
var old=objRef.targetModel.getXpathValue(objRef.targetModel,objRef.featureXpath);
var thePolygon="";
if(!old){
thePolygon=point[0]+","+point[1];
}
else{
var collect=old.split(" "); 
if(collect.length<2){ 
thePolygon=old+" "+point[0]+","+point[1];
}
else if(collect.length==2){
thePolygon=old+" "+point[0]+","+point[1]+" "+collect[0];
}
else if(collect.length>2){
for(var i=0;i<collect.length-1;++i){
thePolygon=thePolygon+collect[i]+" ";
}
thePolygon=thePolygon+point[0]+","+point[1]+" "+collect[0];
}
}
sucess=objRef.targetModel.setXpathValue(objRef.targetModel,objRef.featureXpath,thePolygon);
if(!sucess){
alert("EditPolygon: invalid featureXpath in config: "+objRef.featureXpath);
}
}
}
}
