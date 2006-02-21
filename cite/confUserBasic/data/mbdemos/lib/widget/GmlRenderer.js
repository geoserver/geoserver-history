mapbuilder.loadScript(baseDir+"/widget/MapContainerBase.js");
function GmlRenderer(widgetNode,model){
var base=new MapContainerBase(this,widgetNode,model)
this.coordXsl=new XslProcessor(baseDir+"/widget/GmlCooordinates2Coord.xsl");
this.prePaint=function(objRef){
objRef.stylesheet.setParameter("width",objRef.containerModel.getWindowWidth());
objRef.stylesheet.setParameter("height",objRef.containerModel.getWindowHeight());
bBox=objRef.containerModel.getBoundingBox();
objRef.stylesheet.setParameter("bBoxMinX",bBox[0]);
objRef.stylesheet.setParameter("bBoxMinY",bBox[1]);
objRef.stylesheet.setParameter("bBoxMaxX",bBox[2]);
objRef.stylesheet.setParameter("bBoxMaxY",bBox[3]);
objRef.stylesheet.setParameter("color","#FF0000");
objRef.resultDoc=objRef.coordXsl.transformNodeToObject(objRef.resultDoc);
}
}
