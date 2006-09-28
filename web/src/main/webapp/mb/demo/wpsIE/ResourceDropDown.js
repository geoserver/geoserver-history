/*
Author:       Tom Kralidis
License:      LGPL as per: http://www.gnu.org/copyleft/lesser.html

$Id: ResourceDropDown.js 1672 2005-09-20 02:42:46Z madair1 $
*/

// Ensure this object's dependancies are loaded.
mapbuilder.loadScript(baseDir+"/widget/WidgetBaseXSL.js");

/**
 * Widget to display a list of resources in a context doc to be selected for use
 * as input to another form.
 * @constructor
 * @base WidgetBaseXSL
 * @param widgetNode This widget's object node from the configuration document.
 * @param model The model that this widget is a view of.
 */


function ResourceDropDown(widgetNode, model) {
  WidgetBaseXSL.apply(this, new Array(widgetNode, model));

  /**
   * Change the AOI coordinates from select box choice of prefined locations
   * @param bbox the bbox value of the location keyword chosen
   */

  this.selectResource = function(selectedOption) {
    if (selectedOption.value.length>0) {
      var httpPayload = null;
      var feature = this.model.getFeatureNode(selectedOption.value);
      var resourceType = selectedOption.getAttribute("resourceType");
      switch(resourceType) {
        case "OGC:WFS":
          httpPayload = config.objects.wfsController.createHttpPayload(feature);//TBD: remove hard-coded ID
          break;
        case "OGC:WCS":
          httpPayload = config.objects.wcsController.createHttpPayload(feature);//TBD: remove hard-coded ID
          break;
        case "wms":
          alert("not implemented yet; this will populate the URI input box with the URI to:"+featureName);
          break;
        case "OGC:GML":
          httpPayload = new Object();
          httpPayload.url = feature.selectSingleNode("wmc:Server/wmc:OnlineResource/@xlink:href").nodeValue;
          break;
      }
      this.targetInput.value = httpPayload.url;
      this.targetInput.focus();
    }
  }

  this.setTargetListener = function(objRef) {
    objRef.targetModel.addListener("refresh",objRef.paint,objRef);
  }
  this.model.addListener("loadModel",this.setTargetListener,this);
}
