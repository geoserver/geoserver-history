mapbuilder.loadScript(baseDir+"/widget/WidgetBase.js");
function AoiBoxDHTML(widgetNode,model){
WidgetBase.apply(this,new Array(widgetNode,model));
this.lineWidth=widgetNode.selectSingleNode("mb:lineWidth").firstChild.nodeValue;this.lineColor=widgetNode.selectSingleNode("mb:lineColor").firstChild.nodeValue;this.crossSize=parseInt(widgetNode.selectSingleNode("mb:crossSize").firstChild.nodeValue);
this.paint=function(objRef){
var aoiBox=objRef.model.getParam("aoi");
if(aoiBox){
var ul=objRef.model.extent.getPL(aoiBox[0]);
var lr=objRef.model.extent.getPL(aoiBox[1]);
if((Math.abs(ul[0]-lr[0])<objRef.crossSize)&& 
(Math.abs(ul[1]-lr[1])<objRef.crossSize)){
objRef.drawCross(new Array((ul[0]+lr[0])/2,(ul[1]+lr[1])/2));
}else{
objRef.drawBox(ul,lr);
}
}
}
model.addListener("aoi",this.paint,this);
MapContainerBase.apply(this,new Array(widgetNode,model));
this.setVis=function(vis){
var visibility="hidden";
if(vis)visibility="visible";
this.Top.style.visibility=visibility;
this.Left.style.visibility=visibility;
this.Right.style.visibility=visibility;
this.Bottom.style.visibility=visibility;
}
this.clear=function(objRef){
objRef.setVis(false);
}
this.containerModel.addListener("bbox",this.clear,this);
this.drawBox=function(ul,lr){
this.Top.style.left=ul[0]+'px';
this.Top.style.top=ul[1]+'px';
this.Top.style.width=lr[0]-ul[0]+'px';
this.Top.style.height=this.lineWidth+'px';
this.Left.style.left=ul[0]+'px';
this.Left.style.top=ul[1]+'px';
this.Left.style.width=this.lineWidth+'px';
this.Left.style.height=lr[1]-ul[1]+'px';
this.Right.style.left=lr[0]-this.lineWidth+'px';
this.Right.style.top=ul[1]+'px';
this.Right.style.width=this.lineWidth+'px';
this.Right.style.height=lr[1]-ul[1]+'px';
this.Bottom.style.left=ul[0]+'px';
this.Bottom.style.top=lr[1]-this.lineWidth+'px';
this.Bottom.style.width=lr[0]-ul[0]+'px';
this.Bottom.style.height=this.lineWidth+'px';
this.setVis(true);
}
this.drawCross=function(center){
this.Top.style.left=Math.floor(center[0]-this.crossSize/2)+'px';
this.Top.style.top=Math.floor(center[1]-this.lineWidth/2)+'px';
this.Top.style.width=this.crossSize+'px';
this.Top.style.height=this.lineWidth+'px';
this.Top.style.visibility="visible";
this.Left.style.left=Math.floor(center[0]-this.lineWidth/2)+'px';
this.Left.style.top=Math.floor(center[1]-this.crossSize/2)+'px';
this.Left.style.width=this.lineWidth+'px';
this.Left.style.height=this.crossSize+'px';
this.Left.style.visibility="visible";
this.Right.style.visibility="hidden";
this.Bottom.style.visibility="hidden";
}
this.getImageDiv=function(){
var newDiv=document.createElement("div");
newDiv.innerHTML="<img src='"+config.skinDir+"/images/Spacer.gif' width='1px' height='1px'/>";
newDiv.style.position="absolute";
newDiv.style.backgroundColor=this.lineColor;
newDiv.style.visibility="hidden";
newDiv.style.zIndex=300;
this.node.appendChild(newDiv);
return newDiv;
}
this.loadAoiBox=function(objRef){
objRef.Top=objRef.getImageDiv();
objRef.Bottom=objRef.getImageDiv();
objRef.Left=objRef.getImageDiv();
objRef.Right=objRef.getImageDiv();
objRef.paint(objRef);
}
this.loadAoiBox(this);
}
