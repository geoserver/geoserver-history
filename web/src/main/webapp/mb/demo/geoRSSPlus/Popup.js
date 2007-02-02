/*
Author:       Patrice G Cappelaere patATcappelaere.com
License:      LGPL as per: http://www.gnu.org/copyleft/lesser.html

$Id: Popup.js 1670 2005-09-19 14:55:53Z cappelaere $
*/

// Ensure this object's dependancies are loaded.
// References: http://www.bosrup.com/web/overlib/?Command_Reference
// Plugins: 
//		http://overlib.boughner.us/plugins/bubble_commands.html
//		http://www.bosrup.com/web/overlib/?Unofficial_Plugins

mapbuilder.loadScript(baseDir+"/model/Proj.js");
mapbuilder.loadScript(baseDir+"/widget/WidgetBase.js");
mapbuilder.loadScript(baseDir+"/util/overlib/overlib.js");
// does not work with IE mapbuilder.loadScript(baseDir+"/util/overlib/overlib_shadow.js");
//mapbuilder.loadScript(baseDir+"/util/overlib/overlib_bubble/overlib_bubble.js");

/**
 * Popup uses overlib http://www.bosrup.com/web/overlib/
 * @constructor
 * @base MapContainerBase
 * @param widgetNode  The widget's XML object node from the configuration document.
 * @param model       The model object that this widget belongs to.
 */
function Popup( widgetNode, model) {

  // get configuration options   
  var styleNode = widgetNode.selectSingleNode("mb:stylesheet");
  if (styleNode ) {
  	  var xslt = styleNode.firstChild.nodeValue;
      this.stylesheet = new XslProcessor(xslt, model.namespace);
  } 
  
  // apply stylesheet transform to the entry and return results
  this.transform = function( objRef, entry ) {
   	if( objRef.stylesheet != null ) {
  		var resultNode = objRef.stylesheet.transformNodeToObject( entry );
        var result = Sarissa.serialize(resultNode.documentElement);
    		//alert("transforming:"+result);
  		return result;
  	}
  }
}
