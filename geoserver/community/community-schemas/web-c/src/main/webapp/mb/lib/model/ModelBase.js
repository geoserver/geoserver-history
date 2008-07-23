function ModelBase(modelNode,parentModel){
Listener.apply(this);
this.async=true;this.contentType="text/xml";
this.modelNode=modelNode;
var idAttr=modelNode.attributes.getNamedItem("id");
if(idAttr){
this.id=idAttr.nodeValue;
}else{
this.id="MbModel_"+mbIds.getId();
}
var titleNode=modelNode.selectSingleNode("mb:title");
if(titleNode){
this.title=titleNode.firstChild.nodeValue;
}else{
this.title=this.id;
}
if(modelNode.selectSingleNode("mb:debug"))this.debug="true";
if(window.cgiArgs[this.id]){ 
this.url=window.cgiArgs[this.id];
}else if(window[this.id]){ 
this.url=window[this.id];
}else if(modelNode.url){ 
this.url=modelNode.url;
}else{
var defaultModel=modelNode.selectSingleNode("mb:defaultModelUrl");
if(defaultModel)this.url=defaultModel.firstChild.nodeValue;
}
var method=modelNode.selectSingleNode("mb:method");
if(method){
this.method=method.firstChild.nodeValue;
}else{
this.method="get";
}
var namespace=modelNode.selectSingleNode("mb:namespace");
if(namespace){
this.namespace=namespace.firstChild.nodeValue;
}
var templateAttr=modelNode.attributes.getNamedItem("template");
if(templateAttr){
this.template=(templateAttr.nodeValue=="true")?true:false;
this.modelNode.removeAttribute("template");
}
var nodeSelectXpath=modelNode.selectSingleNode("mb:nodeSelectXpath");
if(nodeSelectXpath){
this.nodeSelectXpath=nodeSelectXpath.firstChild.nodeValue;
}
this.getXpathValue=function(objRef,xpath){
if(!objRef.doc)return null; 
node=objRef.doc.selectSingleNode(xpath);
if(node&&node.firstChild){
return node.firstChild.nodeValue;
}else{
return null;
}
}
this.setXpathValue=function(objRef,xpath,value,refresh){
if(refresh==null)refresh=true;
var node=objRef.doc.selectSingleNode(xpath);
if(node){
if(node.firstChild){
node.firstChild.nodeValue=value;
}else{
dom=Sarissa.getDomDocument();
v=dom.createTextNode(value);
node.appendChild(v);
}
if(refresh)objRef.setParam("refresh");
return true;
}else{
return false;
}
}
this.loadModelDoc=function(objRef){
if(objRef.url){
objRef.callListeners("newModel");
objRef.setParam("modelStatus","loading");
if(objRef.contentType=="image"){
objRef.doc=new Image();
objRef.doc.src=objRef.url;
}else{
var xmlHttp=new XMLHttpRequest();
var sUri=objRef.url;
if(sUri.indexOf("http://")==0){
if(objRef.method=="get"){
sUri=getProxyPlusUrl(sUri);
}else{
sUri=config.proxyUrl;
}
}
xmlHttp.open(objRef.method,sUri,objRef.async);
if(objRef.method=="post"){
xmlHttp.setRequestHeader("content-type",objRef.contentType);
xmlHttp.setRequestHeader("serverUrl",objRef.url);
}
xmlHttp.onreadystatechange=function(){
objRef.setParam("modelStatus",httpStatusMsg[xmlHttp.readyState]);
if(xmlHttp.readyState==4){
if(xmlHttp.status>=400){var errorMsg="error loading document: "+sUri+" - "+xmlHttp.statusText+"-"+xmlHttp.responseText;
alert(errorMsg);
objRef.setParam("modelStatus",errorMsg);
return;
}else{
if(null==xmlHttp.responseXML){
alert("null XML response:"+xmlHttp.responseText);
}else{
objRef.doc=xmlHttp.responseXML;
}
objRef.finishLoading();
}
}
}
xmlHttp.send(objRef.postData);
if(!objRef.async){
if(xmlHttp.status>=400){var errorMsg="error loading document: "+sUri+" - "+xmlHttp.statusText+"-"+xmlHttp.responseText;
alert(errorMsg);
this.objRef.setParam("modelStatus",errorMsg);
return;
}else{
if(null==xmlHttp.responseXML)alert("null XML response:"+xmlHttp.responseText);
objRef.doc=xmlHttp.responseXML;
objRef.finishLoading();
}
}
}
}
}
this.setModel=function(objRef,newModel){
objRef.callListeners("newModel");
objRef.doc=newModel;
if((newModel==null)&&objRef.url){
objRef.url=null;
}
objRef.finishLoading();
}
this.finishLoading=function(){
if(this.doc){
this.doc.setProperty("SelectionLanguage","XPath");
if(this.namespace)Sarissa.setXpathNamespaces(this.doc,this.namespace);
if(this.debug)alert("Loading Model:"+this.id+" "+Sarissa.serialize(this.doc));
this.callListeners("contextLoaded");this.callListeners("loadModel");
}
}
this.newRequest=function(objRef,httpPayload){
var model=objRef;
if(objRef.template){
var parentNode=objRef.modelNode.parentNode;
var newConfigNode;
if(_SARISSA_IS_IE){
newConfigNode=parentNode.appendChild(modelNode.cloneNode(true));
}else{
newConfigNode=parentNode.appendChild(objRef.modelNode.ownerDocument.importNode(objRef.modelNode,true));
}
newConfigNode.removeAttribute("id");model=objRef.createObject(newConfigNode);
model.callListeners("init");
if(!objRef.templates)objRef.templates=new Array();
objRef.templates.push(model);
}
model.url=httpPayload.url;
if(!model.url)model.doc=null;
model.method=httpPayload.method;
model.postData=httpPayload.postData;
model.loadModelDoc(model);
}
this.deleteTemplates=function(){
if(this.templates){
while(model=this.templates.pop()){
model.setParam("newModel");
var parentNode=this.modelNode.parentNode;
parentNode.removeChild(model.modelNode);
}
}
}
this.saveModel=function(objRef){
if(config.serializeUrl){
var response=postLoad(config.serializeUrl,objRef.doc);
response.setProperty("SelectionLanguage","XPath");
Sarissa.setXpathNamespaces(response,"xmlns:xlink='http://www.w3.org/1999/xlink'");
var onlineResource=response.selectSingleNode("//OnlineResource");
var fileUrl=onlineResource.attributes.getNamedItem("xlink:href").nodeValue;
objRef.setParam("modelSaved",fileUrl);
}else{
alert("serializeUrl must be specified in config to save a model");
}
}
this.createObject=function(configNode){
var objectType=configNode.nodeName;
var evalStr="new "+objectType+"(configNode,this);";
var newObject=eval(evalStr);
if(newObject){
config.objects[newObject.id]=newObject;
return newObject;
}else{ 
alert("error creating object:"+objectType);
}
}
this.loadObjects=function(objectXpath){
var configObjects=this.modelNode.selectNodes(objectXpath);
for(var i=0;i<configObjects.length;i++){
this.createObject(configObjects[i]);
}
}
this.parseConfig=function(objRef){
objRef.loadObjects("mb:widgets/*");
objRef.loadObjects("mb:tools/*");
objRef.loadObjects("mb:models/*");
}
this.refresh=function(objRef){
objRef.setParam("refresh",true);
}
this.addListener("loadModel",this.refresh,this);
this.init=function(objRef){
objRef.callListeners("init");
}
this.clearModel=function(objRef){
objRef.doc=null;
}
if(parentModel){
this.parentModel=parentModel;
this.parentModel.addListener("init",this.init,this);
this.parentModel.addListener("loadModel",this.loadModelDoc,this);
this.parentModel.addListener("newModel",this.clearModel,this);
this.parseConfig(this);
}
}
var httpStatusMsg=['uninitialized','loading','loaded','interactive','completed'];
