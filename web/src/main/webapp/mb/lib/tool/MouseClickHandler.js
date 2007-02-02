mapbuilder.loadScript(baseDir+"/tool/ToolBase.js");
function MouseClickHandler(toolNode,model){
ToolBase.apply(this,new Array(toolNode,model));
this.clickHandler=function(objRef,targetNode){
objRef.model.setParam("clickPoint",targetNode.evpl);
}
model.addListener('mouseup',this.clickHandler,this);
}
