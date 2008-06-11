function GetMap(modelNode,parent){
ModelBase.apply(this,new Array(modelNode,parent));
this.namespace="xmlns:gml='http://www.opengis.net/gml' xmlns:wfs='http://www.opengis.net/wfs'";
this.prePaint=function(objRef){
objRef.stylesheet.setParameter("width",objRef.containerModel.getWindowWidth());
objRef.stylesheet.setParameter("height",objRef.containerModel.getWindowHeight());
bBox=objRef.containerModel.getBoundingBox();
var bboxStr=bBox[0]+","+bBox[1]+","+bBox[2]+","+bBox[3];
objRef.stylesheet.setParameter("bbox",bboxStr);
objRef.stylesheet.setParameter("srs",objRef.containerModel.getSRS());
objRef.stylesheet.setParameter("version",objRef.model.getVersion(objRef.featureNode));
objRef.stylesheet.setParameter("baseUrl",objRef.model.getServerUrl("GetMap"));
objRef.stylesheet.setParameter("mbId",objRef.featureNode.getAttribute("id"));
objRef.resultDoc=objRef.featureNode;
}
this.loadLayer=function(objRef,feature){
objRef.featureNode=feature;
objRef.paint(objRef);
}
this.addListener("GetMap",this.loadLayer,this);
}
