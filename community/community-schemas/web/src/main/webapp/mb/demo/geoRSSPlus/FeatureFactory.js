/*
License: LGPL as per: http://www.gnu.org/copyleft/lesser.html
$Id: FeatureFactory.js 1670 2005-09-19 14:55:53Z cappelaere $
*/
mapbuilder.loadScript(baseDir+"/widget/Popup.js");
mapbuilder.loadScript(baseDir+"/widget/FeaturePointFactory.js");

/**
  * Feature Facctoty constructor
  * @param widgetNode  The widget's XML object node from the configuration document.
  * @param model       The model object that this widget belongs to.
  */
function FeatureFactory(widgetNode, model) {
  // initialize popu capability
  this.popup = new Popup( widgetNode, model );
 
  // init the derived factories
  this.featurePointFactory = new FeaturePointFactory( widgetNode, model );
}

/**
  * clear all created features
  */
FeatureFactory.prototype.clearFeatures = function( ) {
  // that's all we have  implemented so far
  this.featurePointFactory.clearPointFeatures();
}

/**
  * Feature Factory that will create a feature of proper type
  * @param objRef a pointer to this widget object
  * @param type Feature type to render: POINT, LINE, CURVE, POLY
  * @param geometry array of necessary coordinates for that type of geometry
  * @param itemId feature Id
  * @param title title of feature
  * @param papupStr popup to display on mouseover
  */
FeatureFactory.prototype.createFeature = function( objRef, type, geometry, itemId, title, popupStr ) {
  if( type == 'POINT' ) {
    this.featurePointFactory.createPointFeature( objRef, geometry, itemId, title, popupStr );
  } else if( type == 'LINE' ) {
    // this will probably require a round trip to server to generate the line
  } else if( type == 'CURVE' ) {
   // this will probably require a round trip to server to generate the bezier curve  
  } else if( type == 'POLY' ) {
   // this will probably require a round trip to server to generate the poly
  } else {
    alert( 'feature:'+type+' is not supported');
  }
}