mapbuilder.loadScript(baseDir+"/tool/ToolBase.js");
mapbuilder.loadScript(baseDir+"/model/Proj.js");
function ZoomToAoi(toolNode,model){
ToolBase.apply(this,new Array(toolNode,model));
this.initProj=function(toolRef){
if(toolRef.model.doc&&toolRef.targetModel.doc){
if(toolRef.model.getSRS()!=toolRef.targetModel.getSRS()){
toolRef.model.proj=new Proj(toolRef.model.getSRS());
toolRef.targetModel.proj=new Proj(toolRef.targetModel.getSRS());
}
}
}
this.setListeners=function(toolRef){
toolRef.model.addListener("loadModel",toolRef.initProj,toolRef);
toolRef.targetModel.addListener("loadModel",toolRef.initProj,toolRef);
toolRef.initProj(toolRef);
}
this.model.addListener("loadModel",this.setListeners,this);
this.showTargetAoi=function(tool){
if(tool.targetModel.doc){
var bbox=tool.targetModel.getBoundingBox(); 
var ul=new Array(bbox[0],bbox[3]);
var lr=new Array(bbox[2],bbox[1]);
if(tool.model.getSRS()!=tool.targetModel.getSRS()){
ul=tool.targetModel.proj.Inverse(ul);lr=tool.targetModel.proj.Inverse(lr);
if(ul[0]>lr[0])ul[0]=ul[0]-360.0;ul=tool.model.proj.Forward(ul);lr=tool.model.proj.Forward(lr);
}
tool.model.setParam("aoi",new Array(ul,lr));
}
}
this.firstInit=function(tool){
tool.targetModel.addListener("loadModel",tool.showTargetAoi,tool);
tool.targetModel.addListener("bbox",tool.showTargetAoi,tool);
tool.showTargetAoi(tool);
}
this.model.addListener("loadModel",this.firstInit,this);
this.mouseUpHandler=function(tool,targetNode){
var bbox=tool.model.getParam("aoi");
var ul=bbox[0];
var lr=bbox[1];
if(tool.model.getSRS()!=tool.targetModel.getSRS()){
ul=tool.targetModel.proj.Forward(ul);lr=tool.targetModel.proj.Forward(lr);
}
if((ul[0]==lr[0])&&(ul[1]==lr[1])){
tool.targetModel.extent.centerAt(ul,tool.targetModel.extent.res[0]);
}else{
tool.targetModel.extent.zoomToBox(ul,lr);
}
}
this.model.addListener('mouseup',this.mouseUpHandler,this);
}
