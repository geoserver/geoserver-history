/*
Author:       Mike Adair mike.adairATccrs.nrcan.gc.ca
License:      LGPL as per: http://www.gnu.org/copyleft/lesser.html

$Id: OWSCatSearchForm.js 2076 2006-04-11 19:48:08Z madair $
*/

// Ensure this object's dependancies are loaded.
mapbuilder.loadScript(baseDir+"/widget/WidgetBaseXSL.js");

/**
 * Widget to display a form for input of parameters to generate a web service 
 * request.  This JS object handles the form submit via HTTP Get by appending 
 * a query string to the form's action URL.  The query string is created from
 * all input elements and their values.
 * The target model is then loaded from the URL created.
 * A stylehseet must be specified as a property in config for this widget.  
 * See widget/NtsForm.xsl for an example. 
 *
 * @constructor
 * @base WidgetBase
 * @param widgetNode This widget's object node from the configuration document.
 * @param model The model that this widget is a view of.
 */

function OWSCatSearchForm(widgetNode, model) {
  WidgetBaseXSL.apply(this, new Array(widgetNode, model));

  this.filter = Sarissa.getDomDocument();
  this.filter.async = false; 
  this.filter.load("OWSCatResourceFilter.xml");
  this.filter.setProperty("SelectionLanguage", "XPath");


  /**
   * Refreshes the form onblur handlers when this widget is painted.
   * @param objRef Pointer to this CurorTrack object.
   */
  this.postPaint = function(objRef) {
    objRef.searchForm = document.getElementById(objRef.formName);
    objRef.searchForm.parentWidget = objRef;

    objRef.searchForm.westCoord.onblur = objRef.setAoi;
    objRef.searchForm.northCoord.onblur = objRef.setAoi;
    objRef.searchForm.eastCoord.onblur = objRef.setAoi;
    objRef.searchForm.southCoord.onblur = objRef.setAoi;
    objRef.searchForm.westCoord.model = objRef.model;
    objRef.searchForm.northCoord.model = objRef.model;
    objRef.searchForm.eastCoord.model = objRef.model;
    objRef.searchForm.southCoord.model = objRef.model;

    objRef.searchForm.onkeypress = objRef.handleKeyPress;
    objRef.searchForm.onsubmit = objRef.submitForm;
    //objRef.searchForm.mapsheet.onblur = objRef.setMapsheet;
  }

  /**
   * Output the AOI coordinates to the associated form input elements.  This
   * method is registered as an AOI listener on the context doc.
   * @param objRef Pointer to this searchForm object.
   */
  this.displayAoiCoords = function(objRef) {
    //objRef.searchForm = document.getElementById(objRef.formName);
    var aoi = objRef.model.getParam("aoi");
    objRef.searchForm.westCoord.value = aoi[0][0];
    objRef.searchForm.northCoord.value = aoi[0][1];
    objRef.searchForm.eastCoord.value = aoi[1][0];
    objRef.searchForm.southCoord.value = aoi[1][1];
  }
  this.model.addListener('aoi', this.displayAoiCoords, this);

  /**
   * Handles user input from the form element.  This is an onblur handler for 
   * the input elements.
   */
  this.setAoi = function() {
    var aoi = this.model.getParam("aoi");
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
      this.model.setParam("aoi",new Array(ul,lr) );
    }
  }

/**
 * Change the AOI coordinates from select box choice of prefined locations
 * @param bbox the bbox value of the location keyword chosen
 */
  this.setLocation = function(bbox) {
    var bboxArray = new Array();
    bboxArray     = bbox.split(",");
    var ul = new Array(parseFloat(bboxArray[0]),parseFloat(bboxArray[2]));
    var lr = new Array(parseFloat(bboxArray[1]),parseFloat(bboxArray[3]));
    this.model.setParam("aoi",new Array(ul,lr));

    //convert this.model XY to latlong
    //convert latlong to targetmodel XY
    //extent.setAoi takes XY as input
    //this.targetModel.setParam("aoi", new Array(ul,lr));
    //this.targetModel.setParam("mouseup",this);
  }


  /**
   * Handles submission of the form (via javascript in an <a> tag)
   */
  this.submitForm = function() {
    var thisWidget = this.parentWidget;
    thisWidget.createFilter(thisWidget);
    thisWidget.targetModel.setParam("wfs_GetFeature","service_resources");
    return false;
  }

  /**
   * creates the filter expression
   */
  this.createFilter = function(objRef) {
    objRef.searchForm = document.getElementById(objRef.formName);
    var filter = config.objects["filterExpression"];    //TBD get this ID from config

    var aoi = this.model.getParam("aoi");
    var bboxStr = "";
    if (aoi) {
      bboxStr = aoi[0][0]+","+aoi[1][1]+" "+aoi[1][0]+","+aoi[0][1];
    } else {
      var bbox = this.model.getBoundingBox();
      bboxStr = bbox[0]+","+bbox[1]+" "+bbox[2]+","+bbox[3];
    }
    filter.setXpathValue(filter,"/Filter/And/BBOX/Box/coordinates",bboxStr);
    var keywords = "*"+objRef.searchForm.keywords.value+"*";
    filter.setXpathValue(filter,"/Filter/And/Or/Or/PropertyIsLike[PropertyName='title']/Literal",keywords);
    filter.setXpathValue(filter,"/Filter/And/Or/Or/PropertyIsLike[PropertyName='abstract']/Literal",keywords);
    filter.setXpathValue(filter,"/Filter/And/Or/PropertyIsLike[PropertyName='keywords']/Literal",keywords);
  }


  /**
   * handles keypress events to filter out everything except "enter".  
   * Pressing the "enter" key will trigger a form submit
   * @param event  the event object passed in for Mozilla; IE uses window.event
   */
  this.handleKeyPress = function(event) {
    var keycode;
    var target;
    if (event){
      //Mozilla
      keycode=event.which;
      target=event.currentTarget;
    }else{
      //IE
      keycode=window.event.keyCode;
      target=window.event.srcElement.form;
    }

    if (keycode == 13) {    //enter key
      return true;
    }
  }

  var RUC_Window=null;
  this.openRucWindow = function( rucType ) { 
    if ( RUC_Window == null || RUC_Window.closed ) { 
      var baseUrl;
      var params;
      switch(rucType) {
        case "placename":
          baseURL = "/rucs/placeName.html?language=" + config.lang + "&formName=" + this.formName;
          params = "width=290,height=480,scrollbars=0,toolbar=0,location=0,directories=0,status=0,menubar=0,resizable=0";
          break;
        case "postalCode":
          baseURL = "/rucs/postalCode.html?language=" + config.lang + "&formName=" + this.formName;
          params = "width=280,height=180,scrollbars=0,toolbar=0,location=0,directories=0,status=0,menubar=0,resizable=0";
          break;
        default:
          alert("unkown RUC type");
          break;
      }
      RUC_Window = open( baseURL, "RUCWindow", params );
    }
    RUC_Window.focus();
    return false;
  } 
  function RUC_closeRUCWindow() { 
    if ( RUC_Window != null && !RUC_Window.closed ) { 
      RUC_Window.close();
    } 
  } 

  //set some properties for the form output
  this.formName = "WebServiceForm_" + mbIds.getId();
  this.stylesheet.setParameter("formName", this.formName);
}

  /**
   */
  SetAoiCoords = function(aoiBox) {
    config.objects.mainMap.setParam("aoi",aoiBox );
  }

