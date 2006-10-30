mapbuilder.loadScript(baseDir+"/widget/ButtonBase.js");
function GetFeatureInfo(toolNode,model){
ButtonBase.apply(this,new Array(toolNode,model));
this.xsl=new XslProcessor(baseDir+"/tool/GetFeatureInfo.xsl");
this.infoFormat="application/vnd.ogc.gml";
this.doAction=function(objRef,targetNode){
if(objRef.enabled){
objRef.targetModel.deleteTemplates();
var selectedLayer=objRef.context.getParam("selectedLayer");
if(selectedLayer==null){
var queryList=objRef.context.getQueryableLayers();
if(queryList.length==0){
alert("There are no queryable layers available, please add a queryable layer to the context.");
return;
}
else{
for(var i=0;i<queryList.length;++i){
var layerNode=queryList[i];
var layerName=layerNode.firstChild.data;
var hidden=objRef.context.getHidden(layerName);
if(hidden==0){objRef.xsl.setParameter("queryLayer",layerName);
objRef.xsl.setParameter("xCoord",targetNode.evpl[0]);
objRef.xsl.setParameter("yCoord",targetNode.evpl[1]);
objRef.xsl.setParameter("infoFormat",objRef.infoFormat);
objRef.xsl.setParameter("featureCount","1");
urlNode=objRef.xsl.transformNodeToObject(objRef.context.doc);
url=urlNode.documentElement.firstChild.nodeValue;
httpPayload=new Object();
httpPayload.url=url;
httpPayload.method="get";
httpPayload.postData=null;
objRef.targetModel.newRequest(objRef.targetModel,httpPayload);
}
}
}
}
else{
objRef.xsl.setParameter("queryLayer",selectedLayer);
objRef.xsl.setParameter("xCoord",targetNode.evpl[0]);
objRef.xsl.setParameter("yCoord",targetNode.evpl[1]);
objRef.xsl.setParameter("infoFormat",objRef.infoFormat);
objRef.xsl.setParameter("featureCount","1");
var urlNode=objRef.xsl.transformNodeToObject(objRef.context.doc);
var url=urlNode.documentElement.firstChild.nodeValue;
if(objRef.infoFormat=="text/html"){
window.open(url,'queryWin','height=200,width=300,scrollbars=yes');
}else{
var httpPayload=new Object();
httpPayload.url=url;
httpPayload.method="get";
httpPayload.postData=null;
objRef.targetModel.newRequest(objRef.targetModel,httpPayload);
}
}
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
