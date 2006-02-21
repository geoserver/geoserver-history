var baseDir;
function Mapbuilder(){
this.loadScript=function(url){
}
this.loadScriptsFromXpath=function(xPath,dir){
var nodes=this.doc.selectNodes(xPath);
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
var index=src.indexOf("/MapbuilderServerLoad.js");
if(index>=0){
baseDir=src.substring(0,index);
}
}
}
}
var mapbuilder=new Mapbuilder();
function mbDoLoad(){
config.init(config);
var mbTimerStop=new Date();
config.callListeners("loadModel");
}
