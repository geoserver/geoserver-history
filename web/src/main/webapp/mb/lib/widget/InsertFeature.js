mapbuilder.loadScript(baseDir+"/widget/ButtonBase.js");
function InsertFeature(widgetNode,model){
this.cursor="default"; 
ButtonBase.apply(this,new Array(widgetNode,model));
this.trm=widgetNode.selectSingleNode("mb:transactionResponseModel").firstChild.nodeValue;
this.tm=widgetNode.selectSingleNode("mb:targetModel").firstChild.nodeValue;
this.tc=widgetNode.selectSingleNode("mb:targetContext").firstChild.nodeValue;
this.httpPayload=new Object();
this.httpPayload.url=widgetNode.selectSingleNode("mb:webServiceUrl").firstChild.nodeValue;
this.httpPayload.method="post";
this.insertXsl=new XslProcessor(baseDir+"/tool/xsl/wfs_Insert.xsl");
this.updateXsl=new XslProcessor(baseDir+"/tool/xsl/wfs_Update.xsl");
this.doSelect=function(selected,objRef){
if(selected){
if(!objRef.transactionResponseModel){
objRef.transactionResponseModel=eval("config.objects."+objRef.trm);
objRef.transactionResponseModel.addListener("loadModel",objRef.handleResponse,objRef);
}
if(!objRef.targetModel){
objRef.targetModel=eval("config.objects."+objRef.tm);
}
if(!objRef.targetContext){
objRef.targetContext=eval("config.objects."+objRef.tc);
}
fid=objRef.targetModel.getXpathValue(objRef.targetModel,"//@fid");
if(objRef.targetModel.doc){
if(fid){
s=objRef.updateXsl.transformNodeToObject(objRef.targetModel.doc);
}else{
s=objRef.insertXsl.transformNodeToObject(objRef.targetModel.doc);
}
objRef.httpPayload.postData=s;
objRef.transactionResponseModel.newRequest(objRef.transactionResponseModel,objRef.httpPayload);
}else alert("No feature available to insert");
}
}
this.handleResponse=function(objRef){
sucess=objRef.transactionResponseModel.doc.selectSingleNode("//wfs:TransactionResult/wfs:Status/wfs:SUCCESS");
if(sucess){
objRef.targetModel.setModel(objRef.targetModel,null);
objRef.targetContext.callListeners("refreshWmsLayers");
}
}
}
