mapbuilder.loadScript(baseDir+"/widget/WidgetBaseXSL.js");
function Locations(widgetNode,model){
WidgetBaseXSL.apply(this,new Array(widgetNode,model));
this.model.getSRS=function(){return "EPSG:4326";}
this.setAoi=function(bbox,targetModel){
var bboxArray=new Array();
bboxArray=bbox.split(",");
var ul=new Array(parseFloat(bboxArray[0]),parseFloat(bboxArray[3]));
var lr=new Array(parseFloat(bboxArray[2]),parseFloat(bboxArray[1]));
this.model.setParam("aoi",new Array(ul,lr));
this.targetModel.extent.zoomToBox(ul,lr);
}
}
