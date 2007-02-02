function ToolBase(toolNode,model){
this.model=model;
this.toolNode=toolNode;
var id=toolNode.selectSingleNode("@id");
if(id){
this.id=id.firstChild.nodeValue;
}else{
this.id="MbTool_"+mbIds.getId();
}
this.initTargetModel=function(toolRef){
var targetModel=toolRef.toolNode.selectSingleNode("mb:targetModel");
if(targetModel){
var targetModelName=targetModel.firstChild.nodeValue;
toolRef.targetModel=eval("config.objects."+targetModelName);
if(!toolRef.targetModel)alert("error finding targetModel:"+targetModelName+" for tool:"+toolRef.id);
}else{
toolRef.targetModel=toolRef.model;
}
}
this.model.addListener("init",this.initTargetModel,this);
this.initMouseHandler=function(toolRef){
var mouseHandler=toolRef.toolNode.selectSingleNode("mb:mouseHandler");
if(mouseHandler){
toolRef.mouseHandler=eval("config.objects."+mouseHandler.firstChild.nodeValue);
if(!toolRef.mouseHandler){
alert("error finding mouseHandler:"+mouseHandler.firstChild.nodeValue+" for tool:"+toolRef.id);
}
}
}
this.model.addListener("init",this.initMouseHandler,this);
this.enabled=true;
var enabled=toolNode.selectSingleNode("mb:enabled");
if(enabled)this.enabled=eval(enabled.firstChild.nodeValue);
}
