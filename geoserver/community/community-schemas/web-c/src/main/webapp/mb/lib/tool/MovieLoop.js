mapbuilder.loadScript(baseDir+"/tool/ToolBase.js");
function MovieLoop(toolNode,model){
ToolBase.apply(this,new Array(toolNode,model));
this.frameIncrement=1;
this.model.setParam("firstFrame",0);
this.timestampIndex=0;
window.movieLoop=this;
var framesPerSecond=toolNode.selectSingleNode("mb:framesPerSecond");
if(framesPerSecond){
this.delay=1000/framesPerSecond.firstChild.nodeValue;
}else{
this.delay=1000/10;}
this.maxFrames=30;
var maxFrames=toolNode.selectSingleNode("mb:maxFrames");
if(maxFrames)this.maxFrames=maxFrames.firstChild.nodeValue;
this.setFrame=function(index){
var timestampList=this.model.timestampList;
if(this.timestampIndex!=null){
var timestamp=timestampList.childNodes[this.timestampIndex];
timestamp.setAttribute("current","0");
this.model.setParam("timestamp",this.timestampIndex);
}
var firstFrame=this.model.getParam("firstFrame");
var lastFrame=this.model.getParam("lastFrame");
if(index>lastFrame)index=firstFrame;
if(index<firstFrame)index=lastFrame;
this.timestampIndex=index;
timestamp=timestampList.childNodes[this.timestampIndex];
timestamp.setAttribute("current","1");
this.model.setParam("timestamp",this.timestampIndex);
}
this.nextFrame=function(step){
var objRef=window.movieLoop;
var increment=objRef.frameIncrement;
if(step)increment=step;objRef.setFrame(objRef.timestampIndex+increment);
}
this.setFrameLimits=function(objRef){
var timestampList=objRef.model.timestampList;
var firstFrame=objRef.model.getParam("firstFrame");
var lastFrame=firstFrame+objRef.maxFrames;
if(lastFrame>timestampList.childNodes.length)lastFrame=timestampList.childNodes.length-1;
objRef.model.setParam("lastFrame",lastFrame);
timestampList.childNodes[firstFrame].setAttribute("current","1");
}
this.model.addFirstListener("refresh",this.setFrameLimits,this);
this.model.addListener("firstFrame",this.setFrameLimits,this);
this.reset=function(objRef){
objRef.pause();
objRef.setFrame(objRef.model.getParam("firstFrame"));
}
this.model.addListener("loadModel",this.reset,this);
this.model.addListener("bbox",this.reset,this);
this.play=function(){
this.movieTimer=setInterval('window.movieLoop.nextFrame()',this.delay);
}
this.pause=function(){
clearInterval(this.movieTimer);
}
this.stop=function(){
this.pause();
this.reset(this);
}
this.stopListener=function(objRef){
objRef.stop();
}
this.model.addListener("stopLoop",this.stopListener,this);
}
