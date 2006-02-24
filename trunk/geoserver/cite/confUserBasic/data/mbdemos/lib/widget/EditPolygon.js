mapbuilder.loadScript(baseDir+"/widget/EditButtonBase.js");
function EditPolygon(widgetNode,model){
EditButtonBase.apply(this,new Array(widgetNode,model));
this.doAction=function(objRef,targetNode){
if(objRef.enabled){
point=objRef.mouseHandler.model.extent.getXY(targetNode.evpl);
old=objRef.targetModel.getXpathValue(
objRef.targetModel,
objRef.featureXpath);
if(!old){old=""}
sucess=objRef.targetModel.setXpathValue(
objRef.targetModel,
objRef.featureXpath,
old+" "+point[0]+","+point[1]);
if(!sucess){
alert("EditPolygon: invalid featureXpath in config: "+objRef.featureXpath);
}
}
}
}
