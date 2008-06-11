/*
Author:       Cameron Shorter cameronATshorter.net
License:      LGPL as per: http://www.gnu.org/copyleft/lesser.html

$Id: GmlPointRenderer.js 1670 2005-09-19 14:55:53Z cappelaere $
*/

// Ensure this object's dependancies are loaded.
mapbuilder.loadScript(baseDir+"/model/Proj.js");
mapbuilder.loadScript(baseDir+"/widget/MapContainerBase.js");
mapbuilder.loadScript(baseDir+"/widget/Popup.js");
mapbuilder.loadScript(baseDir+"/widget/FeatureFactory.js");

// Resource: http://www.bazon.net/mishoo/articles.epl?art_id=824

/**
 * Render GML point geometery into HTML.  This is a MapContainer widget.
 * Other Geometries could be handled if there was some way to get a point 
 * out of it (e.g. polygon centroid).
 * This widget places an image at the specified point on the map.
 * It also places a highlight image at the same spot and registers a 
 * hihglightFeature event on the model, where the featureId is set as the model param.
 * Models using this widget must implement getFeatureNodes(), 
 * @constructor
 * @base MapContainerBase
 * @param widgetNode  The widget's XML object node from the configuration document.
 * @param model       The model object that this widget belongs to.
 */

function GmlPointRenderer(widgetNode, model) {

  //this.normalImage = widgetNode.selectSingleNode("mb:normalImage").firstChild.nodeValue; 
  	//this.highlightImage = widgetNode.selectSingleNode("mb:highlightImage").firstChild.nodeValue;
  this.popup = new Popup( widgetNode, model );
  this.featureFactory = new FeatureFactory(widgetNode, model);
  
  /**
    * Clear all markers
    */
  this.clearWidget = function(objRef) {
    // call feature factory since we do not know the implementation
    this.featureFactory.clearFeatures();
  }
  
  /** draw the points by putting the image at the point
    * @param objRef a pointer to this widget object
    */
  this.paint = function(objRef) {
    
    // cleanup first
    objRef.clearWidget(objRef );
       	
    if (objRef.model.doc && objRef.node) {
      var containerProj = new Proj(objRef.containerModel.getSRS());
      var features = objRef.model.getFeatureNodes();
      for (var i=0; i<features.length; ++i) {
        var feature = features[i];
        var title = objRef.model.getFeatureName(feature);
        var itemId = objRef.model.getFeatureId(feature);   //or feature id's for feature collections?
        var point = objRef.model.getFeaturePoint(feature);
        
        if( (point[0] == 0) && (point[1] == 0 )) {
        		return;
        	}
        	
        point = containerProj.Forward(point);
        point = objRef.containerModel.extent.getPL(point);

        // get the popup info from stylesheet
        var popupStr = objRef.popup.transform( objRef.popup, feature );
        
        // create a point feature
        objRef.featureFactory.createFeature( objRef, 'POINT', point, itemId, title, popupStr );
      }
    }
  }
    
  this.stylesheet = new XslProcessor(baseDir+"/widget/Null.xsl");
  var base = new MapContainerBase(this,widgetNode,model);
 
  /** highlights the selected feature by switching to the highlight image
    * @param objRef a pointer to this widget object
    */
  this.highlight = function(objRef, featureId) {
    var normalImageDiv = document.getElementById(featureId+"_normal");
    normalImageDiv.style.visibility = "hidden";
    var highlightImageDiv = document.getElementById(featureId+"_highlight");
    highlightImageDiv.style.visibility = "visible";
  }
  this.model.addListener("highlightFeature",this.highlight, this);

  /** highlights the selected feature by switching to the highlight image
    * @param objRef a pointer to this widget object
    */
  this.dehighlight = function(objRef, featureId) {
    var normalImageDiv = document.getElementById(featureId+"_normal");
    normalImageDiv.style.visibility = "visible";
    var highlightImageDiv = document.getElementById(featureId+"_highlight");
    highlightImageDiv.style.visibility = "hidden";
  }
  this.model.addListener("dehighlightFeature",this.dehighlight, this);

}

