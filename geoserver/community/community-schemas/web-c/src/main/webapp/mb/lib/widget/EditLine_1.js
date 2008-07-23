mapbuilder.loadScript(baseDir+"/widget/ButtonBase.js");
function EditLine_1(widgetNode,model){
var base=new ButtonBase(this,widgetNode,model);
this.featureXpath=widgetNode.selectSingleNode("mb:featureXpath").firstChild.nodeValue;
this.doAction=function(objRef,targetNode){
if(objRef.enabled){
point=objRef.targetModel.containerModel.extent.getXY(targetNode.evpl);
old=objRef.targetModel.getXpathValue(
objRef.targetModel,
objRef.targetModel.nodeSelectXpath+objRef.featureXpath);
if(!old){old=""};
sucess=objRef.targetModel.setXpathValue(
objRef.targetModel,
objRef.targetModel.nodeSelectXpath+objRef.featureXpath,
old+" "+point[0]+","+point[1]);
if(!sucess){
alert("EditLine: invalid featureXpath in config: "+objRef.targetModel.nodeSelectXpath+objRef.featureXpath);
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
