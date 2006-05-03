function Config(url){
this.doc=Sarissa.getDomDocument();
this.doc.async=false;
this.doc.validateOnParse=false;this.doc.load(url);
if(this.doc.parseError<0){
alert("error loading config document: "+url);}
this.url=url;
this.namespace="xmlns:mb='"+mbNsUrl+"'";
this.doc.setProperty("SelectionLanguage","XPath");
Sarissa.setXpathNamespaces(this.doc,this.namespace);
var configDoc=Sarissa.getDomDocument();
configDoc.async=false;
configDoc.validateOnParse=false;configDoc.load(baseDir+"/"+mbServerConfig);
if(configDoc.parseError<0){
}else{
configDoc.setProperty("SelectionLanguage","XPath");
Sarissa.setXpathNamespaces(configDoc,this.namespace);
var node=configDoc.selectSingleNode("/mb:MapbuilderConfig/mb:proxyUrl");
if(node)this.proxyUrl=node.firstChild.nodeValue;
node=configDoc.selectSingleNode("/mb:MapbuilderConfig/mb:serializeUrl");
if(node)this.serializeUrl=node.firstChild.nodeValue;
}
configDoc=null;
this.loadConfigScripts=function(){
mapbuilder.loadScriptsFromXpath(this.doc.selectNodes("//mb:models/*"),"model/");
mapbuilder.loadScriptsFromXpath(this.doc.selectNodes("//mb:widgets/*"),"widget/");
mapbuilder.loadScriptsFromXpath(this.doc.selectNodes("//mb:tools/*"),"tool/");
var scriptFileNodes=this.doc.selectNodes("//mb:scriptFile");
for(var i=0;i<scriptFileNodes.length;i++){
scriptFile=scriptFileNodes[i].firstChild.nodeValue;
mapbuilder.loadScript(scriptFile);
}
}
this.lang="en";
if(window.cgiArgs["language"]){
this.lang=window.cgiArgs["language"];
}else if(window.language){
this.lang=window.language;
}
var modelNode=this.doc.documentElement;
this.skinDir=modelNode.selectSingleNode("mb:skinDir").firstChild.nodeValue;
var proxyUrl=modelNode.selectSingleNode("mb:proxyUrl");
if(proxyUrl)this.proxyUrl=proxyUrl.firstChild.nodeValue;
var serializeUrl=modelNode.selectSingleNode("mb:serializeUrl");
if(serializeUrl)this.serializeUrl=serializeUrl.firstChild.nodeValue;
var widgetText=modelNode.selectSingleNode("mb:widgetTextUrl");
if(widgetText){
var widgetTextUrl=this.skinDir+"/"+this.lang+"/"+widgetText.firstChild.nodeValue;
this.widgetText=Sarissa.getDomDocument();
this.widgetText.async=false;
this.widgetText.validateOnParse=false;this.widgetText.load(widgetTextUrl);
if(this.widgetText.parseError<0){
alert("error loading widgetText document: "+widgetTextUrl);}
this.widgetText.setProperty("SelectionLanguage","XPath");
Sarissa.setXpathNamespaces(this.widgetText,this.namespace);
}
this.objects=new Object();
ModelBase.apply(this,new Array(modelNode));
this.loadModel=function(modelId,modelUrl){
var model=this.objects[modelId];
if(model&&modelUrl){
var httpPayload=new Object();
httpPayload.method=model.method;
httpPayload.url=modelUrl;
model.newRequest(model,httpPayload);
}else{
alert("config loadModel error:"+modelId+":"+modelUrl);
}
}
this.paintWidget=function(widget){
if(widget){
widget.paint(widget,true);
}else{
alert("config paintWidget error: widget does not exist");
}
}
}
if(document.readyState==null){
mapbuilder.setLoadState(MB_LOAD_CONFIG);
config=new Config(mbConfigUrl);
config[config.id]=config;
config.loadConfigScripts();
}
