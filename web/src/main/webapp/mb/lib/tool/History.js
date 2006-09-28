mapbuilder.loadScript(baseDir+"/tool/ToolBase.js");
function History(toolNode,model){
ToolBase.apply(this,new Array(toolNode,model));
var doAdd=new Boolean();
this.init=function(objRef){
place=-1;
list=new Array();
var bbox=objRef.targetModel.getBoundingBox();
newExtent=new Array();
newExtent[0]=new Array(bbox[0],bbox[3]);
newExtent[1]=new Array(bbox[2],bbox[1]);
list.push(newExtent); 
place=place+1; 
objRef.model.active=place;
objRef.model.historyList=list;
}
this.add=function(objRef){
if(objRef.model.active!=null){
var place=objRef.model.active;
var list=objRef.model.historyList;
newExtent=new Array();
newExtent[0]=objRef.model.extent.ul;
newExtent[1]=objRef.model.extent.lr;
if(place==(list.length-1)){list.push(newExtent); 
place=place+1; 
}
else{place=place+1;
list=list.slice(0,place);
list.push(newExtent);
}
objRef.model.active=place;
objRef.model.historyList=list;
}
}
this.back=function(objRef){
place=objRef.model.active;
if(place<1){
objRef.model.previousExtent=null;
alert("You can't go further back");
}
else{
place=place-1;
objRef.model.active=place;
objRef.model.previousExtent=objRef.model.historyList[place];
}
}
this.forward=function(objRef){
place=objRef.model.active;
if(place<(objRef.model.historyList.length-1)){
place=place+1;
objRef.model.active=place;
objRef.model.nextExtent=objRef.model.historyList[place];
}
else{
objRef.model.nextExtent=null;
alert("You can't go further forward");
}
}
this.stop=function(objRef){
objRef.model.removeListener("bbox",objRef.add,objRef);
}
this.start=function(objRef){
objRef.model.addListener("bbox",objRef.add,objRef);
}
this.initReset=function(objRef){
objRef.targetModel.addListener("bbox",objRef.add,objRef);
objRef.targetModel.addListener("loadModel",objRef.init,objRef);
}
this.model.addListener("historyBack",this.back,this);
this.model.addListener("historyForward",this.forward,this);
this.model.addListener("historyStart",this.start,this);
this.model.addListener("historyStop",this.stop,this);
this.model.addListener("init",this.initReset,this);
}
