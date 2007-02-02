mapbuilder.loadScript(baseDir+"/widget/ButtonBase.js");
function EditButtonBase(widgetNode,model){
ButtonBase.apply(this,new Array(widgetNode,model));
this.cursor="crosshair"; 
this.trm=widgetNode.selectSingleNode("mb:transactionResponseModel");
if(this.trm)this.trm=this.trm.firstChild.nodeValue;
this.defaultModelUrl=widgetNode.selectSingleNode("mb:defaultModelUrl").firstChild.nodeValue;
this.featureXpath=widgetNode.selectSingleNode("mb:featureXpath").firstChild.nodeValue;
this.doSelect=function(selected,objRef){
if(objRef.trm&&!objRef.transactionResponseModel){
objRef.transactionResponseModel=eval("config.objects."+objRef.trm);
}
if(objRef.enabled&&selected&&objRef.targetModel.url!=objRef.defaultModelUrl){
objRef.loadDefaultModel(objRef);
}
if(!selected&&objRef.transactionResponseModel){
objRef.transactionResponseModel.setModel(objRef.transactionResponseModel,null);
}
}
this.loadDefaultModel=function(objRef){
objRef.targetModel.url=objRef.defaultModelUrl;
var httpPayload=new Object();
httpPayload.url=objRef.defaultModelUrl;
httpPayload.method="get";
httpPayload.postData=null;
objRef.targetModel.newRequest(objRef.targetModel,httpPayload);
}
this.setMouseListener=function(objRef){
if(objRef.mouseHandler){
objRef.mouseHandler.model.addListener('mouseup',objRef.doAction,objRef);
}
}
this.initButton=function(objRef){
objRef.targetModel.addListener("loadModel",objRef.setMouseListener,objRef);
}
this.model.addListener("init",this.initButton,this);
}
