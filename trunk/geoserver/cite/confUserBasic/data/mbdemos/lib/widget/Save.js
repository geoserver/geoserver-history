mapbuilder.loadScript(baseDir+"/widget/ButtonBase.js");
function Save(widgetNode,model){
ButtonBase.apply(this,new Array(widgetNode,model));
this.doSelect=function(selected,objRef){
if(selected){
objRef.targetModel.saveModel(objRef.targetModel);
}
}
this.savedModelPopup=function(objRef,fileUrl){
window.open(fileUrl,this.popupWindowName);
}
this.initReset=function(objRef){
objRef.targetModel.addListener("modelSaved",objRef.savedModelPopup,objRef);
}
var popupWindowName=widgetNode.selectSingleNode("mb:popupWindowName");
if(popupWindowName){
this.popupWindowName=popupWindowName.firstChild.nodeValue;
this.model.addListener("init",this.initReset,this);
}
}
