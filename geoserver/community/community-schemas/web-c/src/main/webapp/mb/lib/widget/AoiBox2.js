mapbuilder.loadScript(baseDir+"/widget/GmlRenderer.js");
function AoiBox2(widgetNode,model){
var base=new GmlRenderer(widgetNode,model);
for(sProperty in base){ 
this[sProperty]=base[sProperty]; 
} 
this.stylesheet=new XslProcessor(baseDir+"/widget/GmlRenderer.xsl");
this.prePaint=function(objRef){
objRef.stylesheet.setParameter("width",objRef.targetModel.getWindowWidth());
objRef.stylesheet.setParameter("height",objRef.targetModel.getWindowHeight());
bBox=objRef.targetModel.getBoundingBox();
objRef.stylesheet.setParameter("bBoxMinX",bBox[0]);
objRef.stylesheet.setParameter("bBoxMinY",bBox[1]);
objRef.stylesheet.setParameter("bBoxMaxX",bBox[2]);
objRef.stylesheet.setParameter("bBoxMaxY",bBox[3]);
objRef.stylesheet.setParameter("color","#FF0000");
objRef.stylesheet.setParameter("crossSize","15");
objRef.stylesheet.setParameter("lineWidth","1");
aoiBox=objRef.model.getParam("aoi");
gml='<?xml version="1.0" encoding="utf-8" standalone="no"?>';
if(aoiBox){
ul=objRef.model.extent.getPL(aoiBox[0]);
lr=objRef.model.extent.getPL(aoiBox[1]);
gml=gml+'<Aoi version="1.0.0" xmlns:gml="http://www.opengis.net/gml">';
gml=gml+'<gml:Envelope>';
gml=gml+'<gml:coord>';
gml=gml+'<gml:X>'+aoiBox[0][0]+'</gml:X>';
gml=gml+'<gml:Y>'+aoiBox[0][1]+'</gml:Y>';
gml=gml+'</gml:coord>';
gml=gml+'<gml:coord>';
gml=gml+'<gml:X>'+aoiBox[1][0]+'</gml:X>';
gml=gml+'<gml:Y>'+aoiBox[1][1]+'</gml:Y>';
gml=gml+'</gml:coord>';
gml=gml+'</gml:Envelope>';
gml=gml+'</Aoi>';
}else{
gml=gml+"<null/>";
}
objRef.resultDoc=Sarissa.getDomDocument();
objRef.resultDoc.loadXML(gml);
}
this.aoiListener=function(objRef){
objRef.paint(objRef);
}
model.addListener("aoi",this.aoiListener,this);
}
