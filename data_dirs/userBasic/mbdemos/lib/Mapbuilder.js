var mbTimerStart=new Date();
var config;
var baseDir;
var mbServerConfig="mapbuilderConfig.xml";
var mbNsUrl="http://mapbuilder.sourceforge.net/mapbuilder";
var MB_UNLOADED=0;var MB_LOAD_CORE=1;var MB_LOAD_CONFIG=2;var MB_LOADED=3; 
function Mapbuilder(){
this.loadState=MB_UNLOADED;
this.loadingScripts=new Array();
this.scriptsTimer=null;
this.checkScriptsLoaded=function(){
if(document.readyState!=null){
while(this.loadingScripts.length>0
&&(this.loadingScripts[0].readyState=="loaded"
||this.loadingScripts[0].readyState=="complete"
||this.loadingScripts[0].readyState==null))
{
this.loadingScripts.shift();
}
if(this.loadingScripts.length==0){
this.setLoadState(this.loadState+1);
}
}
else{
if(this.loadState==MB_LOAD_CORE&&config!=null){
this.setLoadState(MB_LOAD_CONFIG);
}
}
}
this.setLoadState=function(newState){
this.loadState=newState;
switch(newState){
case MB_LOAD_CORE:
this.loadScript(baseDir+"/util/sarissa/Sarissa.js");
this.loadScript(baseDir+"/util/sarissa/sarissa_ieemu_xpath.js");
this.loadScript(baseDir+"/util/Util.js");
this.loadScript(baseDir+"/util/Listener.js");
this.loadScript(baseDir+"/model/ModelBase.js");
this.loadScript(baseDir+"/model/Config.js");
break;
case MB_LOAD_CONFIG:
if(document.readyState){
config=new Config(mbConfigUrl);
config.loadConfigScripts();
}else{
this.setLoadState(MB_LOADED);
}
break;
case MB_LOADED:
clearInterval(this.scriptsTimer);
break;
}
}
this.loadScript=function(url){
if(!document.getElementById(url)){
var script=document.createElement('script');
script.defer=false;script.type="text/javascript";
script.src=url;
script.id=url;
document.getElementsByTagName('head')[0].appendChild(script);
this.loadingScripts.push(script);
}
}
this.loadScriptsFromXpath=function(nodes,dir){
for(var i=0;i<nodes.length;i++){
if(nodes[i].selectSingleNode("mb:scriptFile")==null){
scriptFile=baseDir+"/"+dir+nodes[i].nodeName+".js";
this.loadScript(scriptFile);
}
}
}
var head=document.getElementsByTagName('head')[0];
var nodes=head.childNodes;
for(var i=0;i<nodes.length;++i){
var src=nodes.item(i).src;
if(src){
var index=src.indexOf("/Mapbuilder.js");
if(index>=0){
baseDir=src.substring(0,index);
}
}
}
this.setLoadState(MB_LOAD_CORE);
this.scriptsTimer=setInterval('mapbuilder.checkScriptsLoaded()',100);
}
function checkIESecurity()
{
var testPrefixes=["Msxml2.DOMDocument.5.0","Msxml2.DOMDocument.4.0","Msxml2.DOMDocument.3.0","MSXML2.DOMDocument","MSXML.DOMDocument","Microsoft.XMLDOM"];
var bFound=false;
for(var i=0;i<testPrefixes.length&&!bFound;i++){
try{
var oDoc=new ActiveXObject(testPrefixes[i]);
bFound=true;
}
catch(e){
}
}
if(!bFound)window.open("/mapbuilder/docs/help/IESetup.html");}
if(navigator.userAgent.toLowerCase().indexOf("msie")>-1)checkIESecurity();
var mapbuilder=new Mapbuilder();
function mapbuilderInit(){
if(mapbuilder&&mapbuilder.loadState==MB_LOADED){
clearInterval(mbTimerId);
config.parseConfig(config);
config.callListeners("init");
var mbTimerStop=new Date();
if(window.mbInit)window.mbInit();
config.callListeners("loadModel");
}
}
var mbTimerId;
function mbDoLoad(initFunction){
mbTimerId=setInterval('mapbuilderInit()',100);
if(initFunction)window.mbInit=initFunction;
}
