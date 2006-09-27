/*
Author:  Patrice G. Cappelaere patATcappelaere.com
License: LGPL as per: http://www.gnu.org/copyleft/lesser.html
$Id: FeaturePointFactory.js 1670 2005-09-19 14:55:53Z cappelaere $
*/
 

/**
  * Point Feature Factory
  * @param widgetNode  The widget's XML object node from the configuration document.
  * @param model       The model object that this widget belongs to.
  */
function FeaturePointFactory(widgetNode, model) {
  // initialize from model
  this.normalImage = widgetNode.selectSingleNode("mb:normalImage").firstChild.nodeValue; 
  this.highlightImage = widgetNode.selectSingleNode("mb:highlightImage").firstChild.nodeValue;
}

/**
  * Clear all created features
  */
FeaturePointFactory.prototype.clearPointFeatures = function( ) {
  // we need to clear all the div's first
  var divs = document.getElementsByTagName("div");
  for (var i= divs.length-1; i>= 0; i--) {
    var div = divs[i];
    if( div.id.indexOf("RSS_Item") > -1 ) {
        var img = divs[i].firstChild;
        img.onmouseover = null;
        img.onmouseout = null;
        divs[i].parentNode.removeChild( divs[i] );
    }
  }
}

/**
  * Create Point Feature
  * 
  * @param objRef a pointer to this widget object
  * @param geometry array of necessary coordinates for that type of geometry
  * @param itemId feature Id
  * @param title title of feature
  * @param papupStr popup to display on mouseover
  */
FeaturePointFactory.prototype.createPointFeature = function( objRef, geometry, itemId, title, popupStr ) {
  //add in the normalImage
  var normalImageDiv = document.createElement("DIV");
  normalImageDiv.setAttribute("id",itemId+"_normal");
  normalImageDiv.style.position = "absolute";
  normalImageDiv.style.visibility = "visible";
  normalImageDiv.style.zIndex = 300;
          
  var newImage = document.createElement("IMG");
  newImage.src = config.skinDir+ this.normalImage;
  newImage.title = title;
 
  normalImageDiv.appendChild(newImage);
  objRef.node.appendChild( normalImageDiv );
 
  // install handlers
  this.install( newImage, itemId, popupStr );
                     
  //add in the highlightImage
  var highlightImageDiv = document.createElement("DIV");
  highlightImageDiv.setAttribute("id",itemId+"_highlight");
  highlightImageDiv.style.position = "absolute";
  highlightImageDiv.style.visibility = "hidden";
  highlightImageDiv.style.zIndex = 301;   //all highlight images are on top of others
  var newImage = document.createElement("IMG");
  newImage.src = config.skinDir+ this.highlightImage;
  newImage.title = title;
  highlightImageDiv.appendChild(newImage);
  objRef.node.appendChild( highlightImageDiv );
  
  normalImageDiv.style.left = geometry[0];
  normalImageDiv.style.top = geometry[1];
  highlightImageDiv.style.left = geometry[0];
  highlightImageDiv.style.top = geometry[1];
}

/**
  * MouseOver Handler
  *
  * Note: "This" points to the feature
  */
FeaturePointFactory.prototype.mouseOverHandler = function(ev) {    
  // get the enclosing div to get the current position
  var normalImageDiv = document.getElementById(this.itemId+"_normal");
  var topPx = new String(normalImageDiv.style.top);
  var leftPx = new String(normalImageDiv.style.left);
  var offx = normalImageDiv.offsetParent.offsetLeft;
  var offy = normalImageDiv.offsetParent.offsetTop;
  var top = parseInt(topPx.replace("px","")) + 20 + offy;
  var left = parseInt(leftPx.replace("px","")) + 20 + offx;

      
  // hilite the marker
  normalImageDiv.style.visibility = "hidden";
  var highlightImageDiv = document.getElementById(this.itemId+"_highlight");
  highlightImageDiv.style.visibility = "visible";
    
  // set the popup text with stylesheet output
  var popupStr = this.overlib;
  if( popupStr == undefined ) {
    popupStr = "Feature under construction.  Stay tuned!";
  }

  overlib( popupStr, WIDTH, 50, STICKY, FIXX, left, FIXY, top, CAPTION, 'Caption');
  return true;
}
  
/*
 * Mouseout handler
 */
FeaturePointFactory.prototype.mouseOutHandler = function(ev) {
  var highlightImageDiv = document.getElementById(this.itemId+"_highlight");
  highlightImageDiv.style.visibility = "hidden";
  var normalImageDiv = document.getElementById(this.itemId+"_normal");
  normalImageDiv.style.visibility = "visible";
  return nd();
}

/**
  * install mouse handlers and params in div element
  * 
  */
FeaturePointFactory.prototype.install = function( feature, itemId, popupStr ) {
  feature.itemId = itemId;
  feature.overlib = popupStr;
      
  feature.onmouseover = this.mouseOverHandler; 
  feature.onmouseout  = this.mouseOutHandler;
}
