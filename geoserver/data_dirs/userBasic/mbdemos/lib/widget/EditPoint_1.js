mapbuilder.loadScript(baseDir+"/widget/ButtonBase.js");
function EditPoint_1(widgetNode,model){
var base=new ButtonBase(this,widgetNode,model);
this.doAction=function(objRef,targetNode){
if(objRef.enabled){
point=objRef.targetModel.containerModel.extent.getXY(targetNode.evpl);
sucess=objRef.targetModel.setXpathValue(
objRef.targetModel,
objRef.targetModel.nodeSelectXpath,point[0]+","+point[1]);
if(!sucess){
alert("EditPoint: invalid featureXpath in config: "+objRef.targetModel.nodeSelectXpath);
}
}
}
this.setMouseListener=function(objRef){
if(objRef.targetModel.containerModel){
objRef.targetModel.containerModel.addListener('mouseup',objRef.doAction,objRef);
}
}
config.addListener("loadModel",this.setMouseListener,this);
}
