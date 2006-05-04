mapbuilder.loadScript(baseDir+"/widget/WidgetBase.js");
mapbuilder.loadScript(baseDir+"/util/wz_jsgraphics/wz_jsgraphics.js");
function MapScaleBar(widgetNode,model){
WidgetBase.apply(this,new Array(widgetNode,model));
this.width=100;
this.height=3;
this.intervals=5;
this.showScale=function(objRef){
if(objRef.mapScaleTextForm){
var newScale=Math.round(objRef.model.extent.getScale());
var parts=new Array();
while(newScale>=1000.0){
var newPart=newScale/1000.0;
newScale=Math.floor(newPart);
var strPart=leadingZeros(Math.round((newPart-newScale)*1000).toString(),3);
parts.unshift(strPart);
}
parts.unshift(newScale);
objRef.mapScaleTextForm.mapScale.value=parts.join(",");
}
}
this.model.addListener("bbox",this.paint,this);
this.model.addListener("refresh",this.paint,this);
}
MapScaleBar.prototype.paint=function(objRef){
if(objRef.model.doc&&objRef.node){
var outputNode=document.getElementById(objRef.outputNodeId);
if(!outputNode){
outputNode=document.createElement("DIV");
outputNode.setAttribute("id",objRef.outputNodeId);
outputNode.style.position="relative";
outputNode.style.width=objRef.width;
outputNode.style.height=objRef.height*4;
objRef.node.appendChild(outputNode);
}
if(!objRef.jg){
objRef.jg=new jsGraphics(objRef.outputNodeId);
}
objRef.jg.clear();
var HMARGIN=3;
var X_STEP_SIZE=5;
var dsx=objRef.width-2*HMARGIN;
var res=objRef.model.extent.res[0];
objRef.model.units=3;objRef.units=4;do{
var msx=(res*dsx)/(InchesPerUnit(objRef.units,0)/InchesPerUnit(objRef.model.units,0));
var i=roundInterval(msx/objRef.intervals);
var isx=Math.round((i/(InchesPerUnit(objRef.model.units,0)/InchesPerUnit(objRef.units,0)))/res); 
var sx=(objRef.intervals*isx); 
if(sx<=(objRef.width-2*HMARGIN))break; 
dsx-=X_STEP_SIZE; 
}while(1);
var subBar=isx;
objRef.jg.fillRect(0,0,subBar,objRef.height+1);
objRef.jg.drawRect(subBar,0,subBar,objRef.height);
objRef.jg.fillRect(subBar*2,0,subBar,objRef.height+1);
objRef.jg.drawRect(subBar*3,0,subBar,objRef.height);
objRef.jg.fillRect(subBar*4,0,subBar,objRef.height+1);
var newScale=Math.round(isx*res*objRef.intervals);
objRef.jg.drawString(newScale.toString(),0,3+1,"right");
objRef.jg.paint();
}
}
function roundInterval(d)
{
if(d<.001)
return(Math.round(d*10000)/10000.0);
if(d<.01)
return(Math.round(d*1000)/1000.0);
if(d<.1)
return(Math.round(d*100)/100.0);
if(d<1)
return(Math.round(d*10)/10.0);
if(d<100)
return(Math.round(d));
if(d<1000)
return(Math.round(d/10)*10);
if(d<10000)
return(Math.round(d/100)*100);
if(d<100000)
return(Math.round(d/1000)*1000);
if(d<1000000)
return(Math.round(d/10000)*10000);
return(-1);
}
function InchesPerUnit(units,center_lat){
var lat_adj=1.0;
var ipu=1.0;
switch(units){
case 4:
case 3:
ipu=inchesPerUnit[units]; 
break;
case 5:
if(center_lat!=0.0){
var cos_lat=Math.cos(Math.PI*center_lat/180.0);
lat_adj=Math.sqrt(1+cos_lat*cos_lat)/Math.sqrt(2.0);
}
ipu=inchesPerUnit[units]*lat_adj;
break;
default:
break;
}
return ipu;
}
var unitText=["in","ft","mi","m","km"];
var inchesPerUnit=[1,12,63360.0,39.3701,39370.1,4374754];
