function Logger(modelNode,parent){
ModelBase.apply(this,new Array(modelNode,parent));
this.namespace="xmlns:mb='http://mapbuilder.sourceforge.net/mapbuilder'";
this.doc=Sarissa.getDomDocument("http://mapbuilder.sourceforge.net/mapbuilder","mb:Logger");Sarissa.setXpathNamespaces(this.doc,this.namespace);
this.doc.async=false;
this.doc.validateOnParse=false; 
this.logEvent=function(eventType,listenerId,targetId,paramValue){
var eventLog=this.doc.createElement("event");
eventLog.setAttribute("time",new Date().getTime());
eventLog.setAttribute("listener",listenerId);
eventLog.setAttribute("target",targetId);
if(paramValue)eventLog.setAttribute("param",paramValue);
eventLog.appendChild(this.doc.createTextNode(eventType));
this.doc.documentElement.appendChild(eventLog);
}
this.clearLog=function(){
while(this.doc.documentElement.hasChildNodes()){
this.doc.documentElement.removeChild(this.doc.documentElement.firstChild);
}
this.callListeners("refresh");
}
this.saveLog=function(){
if(config.serializeUrl){
var tempDoc=postLoad(config.serializeUrl,logger.doc);
tempDoc.setProperty("SelectionLanguage","XPath");
Sarissa.setXpathNamespaces(tempDoc,"xmlns:xlink='http://www.w3.org/1999/xlink'");
var onlineResource=tempDoc.selectSingleNode("//OnlineResource");
var fileUrl=onlineResource.attributes.getNamedItem("xlink:href").nodeValue;
alert("event log saved as:"+fileUrl);
}else{
alert("unable to save event log; provide a serializeUrl property in config");
}
}
this.refreshLog=function(objRef){
objRef.callListeners("refresh");
}
if(parent)parent.addListener("refresh",this.refreshLog,this);
window.onunload=this.saveLog;window.logger=this;}
