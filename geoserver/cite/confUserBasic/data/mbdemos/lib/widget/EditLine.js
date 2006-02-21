mapbuilder.loadScript(baseDir+"/widget/EditButtonBase.js");
function EditLine(widgetNode,model){
EditButtonBase.apply(this,new Array(widgetNode,model));
this.doAction=function(objRef,targetNode){
if(objRef.enabled){
point=objRef.mouseHandler.model.extent.getXY(targetNode.evpl);
old=objRef.targetModel.getXpathValue(
objRef.targetModel,
objRef.featureXpath);
if(!old){old=""};
sucess=objRef.targetModel.setXpathValue(
objRef.targetModel,
objRef.featureXpath,
old+" "+point[0]+","+point[1]);
if(!sucess){
alert("EditLine: invalid featureXpath in config: "+objRef.featureXpath);
}
}
}
}
