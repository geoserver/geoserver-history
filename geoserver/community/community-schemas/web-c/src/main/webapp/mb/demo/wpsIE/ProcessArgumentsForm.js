/*
Author:       Mike Adair mike.adairATccrs.nrcan.gc.ca
License:      LGPL as per: http://www.gnu.org/copyleft/lesser.html

$Id: ProcessArgumentsForm.js 1672 2005-09-20 02:42:46Z madair1 $
*/

// Ensure this object's dependancies are loaded.
mapbuilder.loadScript(baseDir+"/widget/WidgetBaseXSL.js");
mapbuilder.loadScript(baseDir+"/model/Proj.js");

/**
 * Widget to display the AOI box coordinates in a form.
 *
 * @constructor
 * @base WidgetBaseXSL
 * @param widgetNode This widget's object node from the configuration document.
 * @param model The model that this widget is a view of.
 */

function ProcessArgumentsForm(widgetNode, model) {
  WidgetBaseXSL.apply(this, new Array(widgetNode, model));

  this.executeProcess = function(processName) {
    var webServiceUrl = this.argsForm.action
    if (this.argsForm.action.indexOf("?")<0) webServiceUrl += "?";
    var queryString = "";
    for (var i=0; i<this.argsForm.elements.length; ++i) {
      var element = this.argsForm.elements[i];
      if (element.name.indexOf("_dataSelector")>0) continue;
      queryString += element.name + "=" + encodeURIComponent(element.value) + "&";
    }
    if (this.targetModel.method == "get") {
      webServiceUrl += queryString;
      this.targetModel.postData = null;
      if (this.debug) alert(webServiceUrl);
    } else {
      this.targetModel.postData = queryString;
      if (this.debug) alert(webServiceUrl+" posting:"+queryString);
    }
    config.loadModel( this.targetModel.id, webServiceUrl);
  }

  /**
   * Initialize dynamic properties.
   * @param toolRef Pointer to this object.
   */
  this.initMapModel = function(objRef) {
    //set the map model
    var mapModel = objRef.widgetNode.selectSingleNode("mb:mapModel");
    if (mapModel) {
      objRef.mapModel = eval("config.objects."+mapModel.firstChild.nodeValue);
      if ( !objRef.mapModel ) {
        alert("error finding mapModel:" + mapModel.firstChild.nodeValue + " for:" + objRef.id);
      }
    }
    objRef.stylesheet.setParameter("mapModelId", objRef.mapModel.id );
  }
  this.model.addListener("init", this.initMapModel, this);

  /**
   * Output the AOI coordinates to the associated form input elements.  This
   * method is registered as an AOI listener on the context doc.
   * @param objRef Pointer to this argsForm object.
   */
  this.displayAoiCoords = function(objRef) {
    objRef.argsForm = document.getElementById(objRef.formName);
    var aoi = objRef.mapModel.getParam("aoi");
    objRef.argsForm.westCoord.value = aoi[0][0];
    objRef.argsForm.northCoord.value = aoi[0][1];
    objRef.argsForm.eastCoord.value = aoi[1][0];
    objRef.argsForm.southCoord.value = aoi[1][1];
  }

  /**
   * Set the argument value as a param on the model
   * @param objRef Pointer to this argsForm object.
   */
  this.setArgument = function(event) {
    this.model.setParam(this.name,this.value);
  }

  /**
   * Handles user input from the form element.  This is an onblur handler for 
   * the input elements.
   * @param objRef Pointer to this CurorTrack object.
   */
  this.setAoi = function(event) {
    var aoi = this.mapModel.getParam("aoi");
    if (aoi) {
      var ul = aoi[0];
      var lr = aoi[1];
      switch(this.name) {
        case 'westCoord':
          ul[0] = this.value;
          break;
        case 'northCoord':
          ul[1] = this.value;
          break;
        case 'eastCoord':
          lr[0] = this.value;
          break;
        case 'southCoord':
          lr[1] = this.value;
          break;
      }
      this.mapModel.setParam("aoi",new Array(ul,lr) );
    }
  }

  /**
   * Refreshes the form onblur handlers when this widget is painted.
   * @param objRef Pointer to this argsForm object.
   */
  this.prePaint = function(objRef) {
    var serviceUrl = objRef.model.parentModel.getServerUrl("wps:Execute", "get");
    if (serviceUrl) {
      objRef.stylesheet.setParameter("webServiceUrl",serviceUrl);
      objRef.stylesheet.setParameter("webServiceMethod","get");
      objRef.targetModel.method = "get";
    } else {
      serviceUrl = objRef.model.parentModel.getServerUrl("wps:Execute", "post");
      objRef.stylesheet.setParameter("webServiceUrl",serviceUrl);
      objRef.stylesheet.setParameter("webServiceMethod","post");
      objRef.targetModel.method = "post";
    }
  }

  /**
   * Refreshes the form onblur handlers when this widget is painted.
   * @param objRef Pointer to this argsForm object.
   */
  this.postPaint = function(objRef) {
    objRef.argsForm = document.getElementById(objRef.formName);
    var argsArray = objRef.model.doc.selectNodes("/wps:ProcessDescription/wps:ProcessMember/wps:Process/wps:Input");
    var selectorIndex=0;
    for (var i=0; i<argsArray.length; ++i) {
      var input= argsArray[i];
      var argType = input.selectSingleNode("wps:Parameter/wps:Datatype/*").nodeName;
      var argName = input.selectSingleNode("wps:Parameter/wps:Name").firstChild.nodeValue;
      switch (argType) {
        case "Reference":
        case "wps:Reference":
          if (selectorIndex<2) {
            var selector = config.objects["dataSelector"+selectorIndex];
            ++selectorIndex;
            selector.stylesheet.setParameter("selectName",objRef.id+"_"+argName+"_dataSelector");
            selector.node = document.getElementById(objRef.id+"_"+argName+"_dataSelectorWidget");
            selector.outputNodeId = objRef.id+"_"+argName+"_dataSelectorWidget_outputNode";
            selector.paint(selector);
            selector.targetInput = objRef.argsForm[argName];
          }
          objRef.argsForm[argName].model = objRef.model;
          objRef.argsForm[argName].onblur = objRef.setArgument;
          objRef.argsForm[argName].onchange = objRef.setArgument;
          if (objRef.model.getParam(argName)) objRef.argsForm[argName].value = objRef.model.getParam(argName);
          break;
        case "BoundingBox":
        case "wps:BoundingBox":
          objRef.argsForm.westCoord.onblur = objRef.setAoi;
          objRef.argsForm.northCoord.onblur = objRef.setAoi;
          objRef.argsForm.eastCoord.onblur = objRef.setAoi;
          objRef.argsForm.southCoord.onblur = objRef.setAoi;
          objRef.argsForm.westCoord.model = objRef.model;
          objRef.argsForm.northCoord.model = objRef.model;
          objRef.argsForm.eastCoord.model = objRef.model;
          objRef.argsForm.southCoord.model = objRef.model;
          if (objRef.mapModel) objRef.mapModel.addListener('aoi', objRef.displayAoiCoords, objRef);
          break;
        case "LiteralValue":
        case "wps:LiteralValue":
          objRef.argsForm[argName].model = objRef.model;
          objRef.argsForm[argName].onblur = objRef.setArgument;
          if (objRef.model.getParam(argName)) objRef.argsForm[argName].value = objRef.model.getParam(argName);
          break;
        case "ComplexValue":
        case "wps:ComplexValue":
          objRef.argsForm[argName].model = objRef.model;
          objRef.argsForm[argName].onblur = objRef.setArgument;
          if (objRef.model.getParam(argName)) objRef.argsForm[argName].value = objRef.model.getParam(argName);
          break;
        default:
          alert("invalid argument datatype:"+argType);
          break;
      }
    }
  }

  //set some properties for the form output
  this.formName = "ProcessArgsForm_";// + mbIds.getId();
  this.stylesheet.setParameter("formName", this.formName);
}

