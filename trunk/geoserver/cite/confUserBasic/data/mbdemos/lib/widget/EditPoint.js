mapbuilder.loadScript(baseDir+"/widget/EditButtonBase.js");
function EditPoint(widgetNode,model){
EditButtonBase.apply(this,new Array(widgetNode,model));
this.doAction=function(objRef,targetNode){
if(objRef.enabled){
point=objRef.mouseHandler.model.extent.getXY(targetNode.evpl);
sucess=objRef.targetModel.setXpathValue(
objRef.targetModel,
objRef.featureXpath,point[0]+","+point[1]);
if(!sucess){
alert("EditPoint: invalid featureXpath in config: "+objRef.featureXpath);
}
}
}
}
