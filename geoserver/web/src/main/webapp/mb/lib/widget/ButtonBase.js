mapbuilder.loadScript(baseDir+"/widget/WidgetBaseXSL.js");
function ButtonBase(widgetNode,model){
this.stylesheet=new XslProcessor(baseDir+"/widget/Button.xsl");
var buttonBarNode=widgetNode.selectSingleNode("mb:buttonBar");
if(buttonBarNode){
this.htmlTagId=buttonBarNode.firstChild.nodeValue;
}else{
alert("buttonBar property required for object:"+widgetNode.nodeName);
}
WidgetBaseXSL.apply(this,new Array(widgetNode,model));
this.buttonType=widgetNode.selectSingleNode("mb:class").firstChild.nodeValue;
if(this.buttonType=="RadioButton")this.enabled=false;
var disabledImage=widgetNode.selectSingleNode("mb:disabledSrc");
if(disabledImage){
this.disabledImage=document.createElement("img");
this.disabledImage.src=config.skinDir+disabledImage.firstChild.nodeValue;
}
var enabledImage=widgetNode.selectSingleNode("mb:enabledSrc");
if(enabledImage){
this.enabledImage=document.createElement("img");
this.enabledImage.src=config.skinDir+enabledImage.firstChild.nodeValue;
}
var cursorNode=this.widgetNode.selectSingleNode("mb:cursor");
if(cursorNode!=null){
var cursor=cursorNode.firstChild.nodeValue;
this.cursor=cursor;
}else{
this.cursor="default";}
this.prePaint=function(objRef){
objRef.resultDoc=objRef.widgetNode;
}
this.doAction=function(){}
this.select=function(){
var a=document.getElementById("mainMapContainer");
if(a!=null){
a.style.cursor=this.cursor;
} 
if(this.buttonType=="RadioButton"){
if(this.node.selectedRadioButton){
with(this.node.selectedRadioButton){
if(disabledImage)image.src=disabledImage.src;
enabled=false;
if(mouseHandler)mouseHandler.enabled=false;
link.className="mbButton";
doSelect(false,this);
}
}
this.node.selectedRadioButton=this;
if(this.enabledImage)this.image.src=this.enabledImage.src;
this.link.className="mbButtonSelected";
}
this.enabled=true;
if(this.mouseHandler)this.mouseHandler.enabled=true;
this.doSelect(true,this);
}
this.doSelect=function(selected,objRef){
}
var selected=widgetNode.selectSingleNode("mb:selected");
if(selected&&selected.firstChild.nodeValue)this.selected=true;
this.initMouseHandler=function(objRef){
var mouseHandler=objRef.widgetNode.selectSingleNode("mb:mouseHandler");
if(mouseHandler){
objRef.mouseHandler=eval("config.objects."+mouseHandler.firstChild.nodeValue);
if(!objRef.mouseHandler){
alert("error finding mouseHandler:"+mouseHandler.firstChild.nodeValue+" for button:"+objRef.id);
}
}else{
objRef.mouseHandler=null;
}
}
this.buttonInit=function(objRef){
objRef.image=document.getElementById(objRef.id+"_image");
objRef.link=document.getElementById(objRef.outputNodeId);
if(objRef.selected)objRef.select();
}
this.model.addListener("refresh",this.buttonInit,this);
this.model.addListener("init",this.initMouseHandler,this);
}
