mapbuilder.loadScript(baseDir+"/tool/ButtonBase.js");
function WfsGetFeature(widgetNode,model){
ButtonBase.apply(this,new Array(widgetNode,model));
this.tolerance=widgetNode.selectSingleNode('mb:tolerance').firstChild.nodeValue;
this.typeName=widgetNode.selectSingleNode('mb:typeName').firstChild.nodeValue;
this.webServiceUrl=widgetNode.selectSingleNode('mb:webServiceUrl').firstChild.nodeValue;
this.httpPayload=new Object();
this.httpPayload.method="get";
this.httpPayload.postData=null;
this.trm=widgetNode.selectSingleNode("mb:transactionResponseModel").firstChild.nodeValue;
this.cursor="pointer"; 
this.doAction=function(objRef,targetNode){
if(objRef.enabled){
extent=objRef.targetModel.extent;
point=extent.getXY(targetNode.evpl);
xPixel=extent.res[0]*objRef.tolerance;
yPixel=extent.res[1]*objRef.tolerance;
bbox=(point[0]-xPixel)+","+(point[1]-yPixel)+","+(point[0]+xPixel)+","+(point[1]+yPixel);
objRef.httpPayload.url=objRef.webServiceUrl+"?request=GetFeature&typeName="+objRef.typeName+"&bbox="+bbox;
if(!objRef.transactionResponseModel){
objRef.transactionResponseModel=eval("config.objects."+objRef.trm);
}
objRef.transactionResponseModel.newRequest(objRef.transactionResponseModel,objRef.httpPayload);
}
}
this.setMouseListener=function(objRef){
if(objRef.mouseHandler){
objRef.mouseHandler.model.addListener('mouseup',objRef.doAction,objRef);
}
objRef.context=objRef.widgetNode.selectSingleNode("mb:context");
if(objRef.context){
objRef.context=eval("config.objects."+objRef.context.firstChild.nodeValue);
}
}
config.addListener("loadModel",this.setMouseListener,this);
}
