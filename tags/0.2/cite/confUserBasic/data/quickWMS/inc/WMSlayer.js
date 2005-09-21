/*
* Project quickWMS: Generic JavaScript WMS Client
* File : WMSlayer.js
* Author : Pedro Pereira Gonçalves
* email : pedro@inovagis.org
* Version : 0.3
* Description: Translates the WMS 1.1 OpenGIS specification to a javascript class
*                     Includes methods visibility methods and URL construct
*                     For more complex behavior his class needs a parent control where is
*                     going to be displayed (e.g. mapWMS or quickWMS)
*                     Please read coderules.txt before making any change
* Tested on : Netscape 7.0, IE 5.5, IE 6.0
* Last Change : 2003-10-27
* Dependencies : WMSbrowsers.js
* Future Developments : styles,
* License : OpenSource (check license.txt)
* History :
	2003-10-27 : When in directLoading mode the image source is first cleared using the
                          empty_img and only then gos to the wms url (once again thanks to Bart for this suggestion)
	2003-10-24 : Added the option to select direct loading without using the transport image
                          by default is false in all browsers except in IE 6 (because this browsers will always recall the image even if it has a object in memory)
	2003-10-10 : In url function forces the use of the BBox input parameter if exists
	2003-09-09 : Change the add layer method so that accepts relative urls  (*)
	2003-06-25 : Documentation added
    2003-04-28 : writeDOM method added to layerWMS
                          added the onerror event
    2003-04-23 : Documentation added
    2003-03-20 : File Created
* Objects : layerWMS
* Functions : none
*/

function layerWMS(parent, serverUrl, layer, caption, version)
{
	if (parent){
		this.layerID = parent.name + parent.layers.length;
		this.parent = parent;
		this.srs = parent.srs;
		this.directLoading=parent.directLoading;
		}
	else{
		this.layerID = "";
		this.parent = parent;
		this.srs = 	"EPSG:4326";
		this.directLoading=false; // if directLoading is true then it will not use a temporary image while receiving the requests
		// in IE60 this is true by default
		if (browser.isIE60) this.directLoading=true;
		}
	this.wmsUrl = serverUrl;
	this.wmsLayers = layer;
	this.caption = layer;
	if (caption) this.caption = caption;
	// default format is GIF TO-DO
	this.format = "GIF";
	this.styles = "";
	this.visible = true;
	this.transparent = true;

	// Specific Parameters for GIServer (inovagis web map server)
	this.maxValue=null;
	this.minValue=null;
	this.style=null;
	this.additionalParam = "";

	// TO-DO: legend linkage to object
	this.legend=null;

	/*
	Opacity Paremeters
	this is still an undocumented function
	TO-DO: netscape support and more testing
	*/
	this.opacity_priv=1;
	this.opacity = function(value){;
			if (value){
				this.opacity_priv=value;
				this.DOMref.style.filter="alpha(opacity="+this.opacity_priv+")";
			}
			return this.opacity_priv;
			};

	// version=1.1 is the default value
	if (version) {this.version=version}
	else {this.version="1.1"}

	// set the tags version and request according to the version number (0.9 to 1.1 are considered)
	if (this.version.substring(0,3)=="1.0" || this.version.substring(0,3)=="0.9"){this.request="WMTVER="+this.version +"&REQUEST=map"}
	else{this.request="VERSION="+this.version +"&REQUEST=getmap"}

	this.DOMref=null;

	// BBox is an optional parameter when a parent exists
	// if nothinsg then is going to use the parent value
	this.url = function (BBox)
	 {
		if (!this.visible) {
			var pUrl =empty_IMG;
			}
		else{
			// (*) changed 2003-09-09 : was 	var pUrl ="http://"+ this.wmsUrl;
			var pUrl = this.wmsUrl;
			if (pUrl.indexOf("?")<0) pUrl += "?"+this.request
			else {pUrl += "&"+this.request }
			pUrl += "&SRS=" +this.srs;
			pUrl += "&WIDTH=" +this.width;
    		pUrl += "&HEIGHT="+this.height;
			pUrl += "&LAYERS=" + this.wmsLayers;

			if (parent && !BBox){
				pUrl += "&BBOX=" + this.parent.BBox[0] + "," + this.parent.BBox[1] + "," + this.parent.BBox[2] + "," + this.parent.BBox[3]
				}
			else {
				pUrl += "&BBOX=" + BBox[0] + "," + BBox[1] + "," + BBox[2] + "," + BBox[3];
				}
			if (this.version.substring(0,3)=="1.0" || this.version.substring(0,3)=="0.9") pUrl += "&EXCEPTIONS=INIMAGE"
			else pUrl += "&EXCEPTIONS=application/vnd.ogc.se_inimage"

			if (this.transparent) pUrl += "&TRANSPARENT=TRUE";

			pUrl += "&FORMAT=" + this.format;

			if (this.styles!="") pUrl += "&STYLES=" + this.styles;
			if (this.additionalParam!="") pUrl += "&" + this.additionalParam;

			// specific tags for GIServer compliant
			if (this.maxValue) pUrl+= "&"+this.wmsLayers+".maxvalue="+this.maxValue;
			if (this.minValue) pUrl+= "&"+this.wmsLayers+".minvalue="+this.minValue;
			if (this.style) pUrl+= "&"+this.wmsLayers+".style="+this.style;
		}
		//prompt("",pUrl);
		return pUrl
	}
	// to-do still needs implement the style tag
	this.writeDOM = function (name, left, top, width, height, style){
		if (!this.width) this.width=width;
		if (!this.height) this.height=height;
		this.DOMref = writeIMG (name, empty_IMG, left, top, width, height, "filter:alpha(opacity="+this.opacity*100+");-moz-opacity:"+this.opacity+";");

		if (this.directLoading){
				this.DOMref.parent=this;
				this.DOMref.loading=true;
				this.DOMref.onload=function (){
					this.loading=false;
					//alert(this.parent.DOMref.src);
					//alert(this.src);
					//this.parent.DOMref.src=this.src;
					}
				this.DOMref.onerror=function (){
									// launch the error event to the parent - the layerWMS -
									//if this event does exist .. it will try the parent
									if (this.parent.onError) this.parent.onError(this.parent);
									else
											if (this.parent.parent) if (this.parent.parent.onError) this.parent.parent.onError(this.parent);
									this.loading=false;
									this.src=empty_IMG;
									};
			}
		return this.DOMref;
		/*
		this.transportIMG.parent=this;
		this.transportIMG.parent.DOMref=this.DOMref;
		   */
		}
	// BBox is an optional parameter .. if nothing them is going to use the parent value
	this.refresh = function(BBox){
		if (this.directLoading){
			this.DOMref.src=empty_IMG;
			this.DOMref.loading=true;
			this.DOMref.src =this.url(BBox);
			}
		else {
			this.transportIMG.loading=true;
			this.transportIMG.src = this.url(BBox);
			}
		}

	// this must be recoded .. the logic is when hiding a layer the request changes to empty.gif
	this.show = function ()
			{showLayer(this.layerID);}
	this.hide = function ()
			{hideLayer(this.layerID);}
	this.isVisible = function ()
			{return isVisible(this.layerID)}


	/// private properties for download control ... do not TOUCH!! :)
	if (!this.directLoading){
		this.transportIMG = new Image();
		this.transportIMG.parent=this;
		this.transportIMG.onload=function (){
				this.loading=false;
				//alert(this.parent.DOMref.src);
				//alert(this.src);
				this.parent.DOMref.src=this.src;
				}
		this.transportIMG.onerror=function (){
									// launch the error event to the parent - the layerWMS -
									//if this event does exist .. it will try the parent
									if (this.parent.onError) this.parent.onError(this.parent);
									else
											if (this.parent.parent)
											{
												if (this.parent.parent.onError) this.parent.parent.onError(this.parent);
											}
									this.loading=false;
									this.src=empty_IMG;
									}
	}

}