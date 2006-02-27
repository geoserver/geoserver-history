/* ###################################################################################################
* KDVZ - Erweiterungen
* 2004-01-16 kdvzWMS definiert, erbt von quickWMS
*
* Stand 2003-09-03
* layerBar definiert, hiermit koennen Layer sichtbar/unsichtbar geschaltet werden
* Funktioniert z.Z. nur für den 1. WMS
* ####################################################################################################
*/

/* ###################################################################################################
* KDVZ - Erweiterung
* 2004-01-16 kdvzMapWMS
*
* ####################################################################################################
*/

function kdvzMapWMS(x1, y1, x2, y2, srs) {

	this.inheritFrom = quickWMS;
	this.inheritFrom(x1, y1, x2, y2, srs);

	// layerArray mit init-Parametern
	this.layerArray=new Array();

	this.aScale=0.0;

	// neu definiert - ruft jetzt kdvzLayerWMS() auf
	this.addLayer = function(url, layer, version, caption){
		this.layers[this.layers.length] = new kdvzLayerWMS(this, url, layer, caption, version);
		return true;
		}

	// function wird ueberschrieben, da layerbar.updateLayers() hinzugefuegt
	this.updateBBox = function (x1, y1, x2, y2){
		if (this.onBeforeChange){this.onBeforeChange(this,x1,y1,x2,y2);}
		copy(this.BBox,this.lastBBox);
		var nI=this.prevBBox.length;
		for (var i=this.indexBBox+1;i<nI;i++) {this.prevBBox.length--}
		this.BBox[0]=x1/1;this.BBox[2]=x2/1;
		this.BBox[1]=y1/1;this.BBox[3]=y2/1;
		this.indexBBox+=1;
		geoConstraints(this);
		this.prevBBox[this.prevBBox.length]=new Array(this.BBox[0],this.BBox[1],this.BBox[2],this.BBox[3]);

		// aScale wird berechnet
		var aScale = 0.0;
		deltaX = this.BBox[2]-this.BBox[0];
		this.aScale = Math.round(Math.sqrt(Math.pow(deltaX,2)*2)/this.width/0.00028);
		// Scale aktualisieren
		scaleForm = getForm('','ScaleForm');
    	scaleForm.aScale.value=this.aScale;

  		// BBox wird in Fenster geschrieben
		bboxForm = getForm('','BBoxForm');
    	bboxForm.aBBox.value = Math.round(this.BBox[0])+','+Math.round(this.BBox[1])+','+Math.round(this.BBox[2])+','+Math.round(this.BBox[3]);

		// updateLayers ohne refresh()
		if (this.layerbar) {this.layerbar.updateLayers(false);}
		this.refresh();
		}

	// homeBBox definiert
	this.homeBBox = new Array(this.BBox[0], this.BBox[1], this.BBox[2], this.BBox[3]);

	// Methode neu definiert, home ist jetzt nicht mehr ident. mit initBBox
	this.home=function(){
		if (this.homeBBox[0]) {
			this.updateBBox(this.homeBBox[0], this.homeBBox[1],this.homeBBox[2],this.homeBBox[3]);
			}
		else {
			this.updateBBox(this.initBBox[0], this.initBBox[1],this.initBBox[2],this.initBBox[3]);
			}
		}

	// neue Methode setHome
	this.setHome=function(x1, y1, x2, y2){
		this.homeBBox[0] = x1;
		this.homeBBox[1] = y1;
		this.homeBBox[2] = x2;
		this.homeBBox[3] = y2;
		}

	this.writeDOM = function (name, left, top, width, height, style){
		this.name = name;
		this.width = width;
    	this.height = height;
		this.top = top;
		this.left = left;
		document.open();
		if (style) openLayer(this.name,"",style, this.left, this.top, width, height, true);
		else openLayer(this.name,"","background-color:white;", this.left, this.top, width, height, true);
		for(i=0; i<this.layers.length; i++){
					this.layers[i].writeDOM(this.name+i,0,0,this.width,this.height,"");
					// we have to remove the onload function (defined in the WMSlayer.js) of the transportIMG
					// because here we use the wait function
					if (!this.directLoading) 	this.layers[i].transportIMG.onload=function (){this.loading=false;if (this.parent.parent) {this.parent.parent.lastLoaded=this.parent.caption}}
					else	this.layers[i].DOMref.onload = function (){if (this.src.indexOf(empty_IMG)<0) {this.loading=false;if (this.parent.parent) {this.parent.parent.lastLoaded=this.parent.caption}}}

					}
		for(i=0; i<this.writeHTML.length; i++) this.writeHTML[i](this);
		this.topImage=writeIMG(name+"MapTop",empty_IMG,0,0,this.width,this.height,"");
		this.topImage.parent=this;

		//status layer
		// 2003-10-13 : change this from inherit to visible (ns71 problems)
		openLayer(this.name+"Status","","visibility:visible;",0, 0, this.width, this.height, true);
		if (browser.isOldNS) writeIMG(this.name+"Status_",empty_IMG,0,0,this.width,this.height,"")
		else writeIMG(this.name+"Status_",white_IMG,0,0,this.width,this.height,"filter:alpha(opacity="+statusThreshold+"); -moz-opacity:"+(statusThreshold/100)+";");
		//statusTXT
		openLayer(this.name+"StatusTXT","","position: absolute;visibility:inherit",0, 0, this.width, this.height/2, true);
		closeLayer();//statusTXT
		//statusIMG
		openLayer(this.name+"StatusIMG","","visibility:inherit;",0, this.height/2, this.width, this.height/2, true);
		//style="cursor:hour;"position:absolute;top:0,left:0;

		var content='<table border="0" width='+this.width+' height='+this.height/2 +'><tr align="center" valign="top"><td>'
		content+="<img src='"+waiting_IMG+"'>";
		content+="<BR><span class='cancelText' onclick=javascript:getLayer('"+this.name+"').reference.cancelRequest();return false;";
		content+=" style='cursor:pointer'>Loading...</span>";
		content+="</td></tr></table>";
		document.write(content);
		closeLayer();//statusIMG
		closeLayer();//status
		closeLayer();//name

		// BBox-Anzeige
		openLayer(this.name+"BBox","","",this.left,this.top+this.height+5,250,0,true);
         content  = '<form action="" method="post" name="BBoxForm" id="BBoxForm" style="border:0;">';
		 content += '<span class="text8">BBox:&#160;<input type="text" name="aBBox" value="" readonly style="border:0; background-color: #BFBFBF; width:200px; font-size:8pt; text-align:center;"></span></form>';
		 document.write(content);
		closeLayer();

		// Scale-Anzeige
		openLayer(this.name+"Scale","","",this.left+this.width-120,this.top+this.height+5,250,0,true);
        content  = '<form action="" method="post" name="ScaleForm" id="ScaleForm" style="border:0;">';
	 	content += '<span class="text8">Scale 1:&#160;<input type="text" name="aScale" value="" readonly style="border:0; background-color: #BFBFBF; width:50px; font-size:8pt; text-align:center;"></span></form>';
		document.write(content);
		closeLayer();

		//closeLayer();//name teste 2003-09-25
		document.writeln();
		// retrieve the reference of the layer tag to add to the object

		if (browser.isOldNS){this.DOMref= findLayer(name,document);}
		else this.DOMref=document.getElementById(name);
		// set a reference to this object on the layer tag
		this.DOMref.reference=this;


		document.close();
		// if any of its descendents needs to initialize something we call this function
		if (this.initialize) this.initialize();
		return true;
		}


	// Unterstuetzng von featureInfo
	this.onSelection = function (wmsLayer,x1,y1,x2,y2){

		var aRef = this.layers[0]
		// die selektierbaren Layer werden in
		// additionalParam
		for (i=0; i<this.layers.length; i++) {
			if (this.layers[i].queryLayers != "") {
				aRef = this.layers[i];
			}
		}
        var pUrl = aRef.wmsUrl;
        if (pUrl.indexOf("?")<0) pUrl += "?";
        else {pUrl += "&" }
		//var pUrl ="http://"+ aRef.wmsUrl;
        pUrl += "VERSION=1.1.0&REQUEST=GetFeatureInfo";
        if (aRef.wmsLayers) pUrl += "&LAYERS=" + aRef.wmsLayers;

        if (aRef.styles!="") pUrl += "&STYLES=" + aRef.styles;

        pUrl += "&SRS=" +aRef.srs;

        if (parent){
            pUrl += "&BBOX=" + aRef.parent.BBox[0] + "," + aRef.parent.BBox[1] + "," + aRef.parent.BBox[2] + "," + aRef.parent.BBox[3]
            }
        else {
            pUrl += "&BBOX=" + BBox[0] + "," + BBox[1] + "," + BBox[2] + "," + BBox[3];
            }

        pUrl += "&WIDTH=" +aRef.width;
        pUrl += "&HEIGHT="+aRef.height;

        if (aRef.format!="") {
            pUrl += "&FORMAT=" + aRef.format;
            }

        if (aRef.transparent) pUrl += "&TRANSPARENT=TRUE";

		// KDVZ 2004-04-23, alle sichtbaren Layer werden abgefragt
		//pUrl += "&QUERY_LAYERS="+aRef.queryLayers;
		var deegree = aRef.wmsUrl.match(/deegreewms/);
		if (deegree)
			pUrl += "&QUERY_LAYERS="+aRef.queryLayers;
		else
			pUrl += "&QUERY_LAYERS=" + aRef.wmsLayers;
			//pUrl += "&QUERY_LAYERS="+aRef.queryLayers;

        if (x1>x2){
        	tx=x1;
        	x1=x2;
        	x2=tx;
        }

        if (y1>y2){
        	ty=y1;
        	y1=y2;
        	y2=ty;
        }

        dX = (x2-x1)/2;
        dY = (y2-y1)/2;

		mouseX = geo2MouseX(wmsLayer,x1+dX);
		mouseY = geo2MouseY(wmsLayer,y1+dY);
		//alert(mouseX+" , "+mouseY);

        pUrl += "&X="+mouseX;
        pUrl += "&Y="+mouseY;
        pUrl += "&INFO_FORMAT=text/html";
        pUrl += "&FEATURE_COUNT=5";
        //prompt("",pUrl);

		featureWin = window.open("about:blank","FeatureInfo","dependent=yes,scrollbars=yes,statusbar=yes,resizable=yes,locationbar=yes,width=600,height=400");
		//,"width=600,height=400,scrollbars=yes,statusbar=yes,resizable=yes,locationbar=yes"
		featureWin.focus();
		featureWin.location.href =pUrl;
		}

	// Unterstuetzng von printLayer
	this.printLayer = function (){
		printWin = window.open(path + "print.jsp","printWin","dependent=yes,scrollbars=yes,statusbar=yes,resizable=yes,locationbar=yes,toolbar=yes,width=680,height=600");
		printWin.focus();
		}
}

/* ###################################################################################################
* KDVZ - Erweiterung
* 2004-01-16 kdvzLayerWMS
* queryLayers definiert die Layer fuer FeatureInfo
* Funktioniert nur fuer jeweils einen WMS!!
* ####################################################################################################
*/

function kdvzLayerWMS(parent, serverUrl, layer, caption, version) {

	this.inheritFrom = layerWMS;
	this.inheritFrom(parent, serverUrl, layer, caption, version);
	this.queryLayers = "";

	this.url = function (BBox)
	 {
		if (!this.visible) {
			var pUrl =empty_IMG;
			// KDVZ 2004-01-22
			if (this.directLoading){
                this.DOMref.loading=false;
                this.DOMref.src =pUrl;
                }
			}
		else{
			// (*) changed 2003-09-09 : was 	var pUrl ="http://"+ this.wmsUrl;
			var pUrl = this.wmsUrl;
			if (pUrl.indexOf("?")<0) pUrl += "?"+this.request
			else {pUrl += "&"+this.request }
			pUrl += "&SRS=" +this.srs;
			pUrl += "&WIDTH=" +this.width;
    		pUrl += "&HEIGHT="+this.height;
    		// Mod fuer SIAS, &Layers ist nicht bekannt
    		if (this.wmsLayers && this.additionalParam=="") pUrl += "&LAYERS=" + this.wmsLayers;
    		if (this.wmsLayers && this.additionalParam!="") pUrl += "&swldy_themes=" + this.wmsLayers;

			if (parent && !BBox){
				pUrl += "&BBOX=" + this.parent.BBox[0] + "," + this.parent.BBox[1] + "," + this.parent.BBox[2] + "," + this.parent.BBox[3];
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

}
/* ###################################################################################################
* KDVZ - Erweiterungen
* 2004-01-22 layerbar definiert
* Zu jedem WMS koennen die Layer in Themen gruppiert werden
* Der eigentliche Request wird dann aus den einzelnen Themen zusammengesetzt
* Funktion initLayer(caption, title, layer, visible) definiert die Themen
* z.B. initLayer("Stadtplan", "ALK - Raster", "stadtplan", "visible")
*
* ####################################################################################################
*/
function layerBar(obRefName){

	this.map=obRefName;

	this.layerArray=new Array();

	this.writeDOM = function ( x, y, horiz) {

		var aLayerArray = this.layerArray;
		var content = "";
		var selectedLayers = "";
		var lbWidth = 250;
		var lbHeight = 280;

		content += "<form name='LayerForm' action='' method=''>";
        // table beginnen
        content += "<table border='0' class='themes'>";
		for (i = 0; i<aLayerArray.length; i++){
				// WMS-Caption
				aCaption = aLayerArray[i][0];
				// Thema (einzelner oder Gruppe von Layern)
				aTheme = aLayerArray[i][1];
				// layer für request
				queryLayers = aLayerArray[i][2];
				// rendern des Themas
				doRender = aLayerArray[i][3];

				if (aTheme != "" && doRender=="visible") {
					content+="<tr><td class='themes'><input name='"+queryLayers+"' type='checkbox' value='"+aCaption+"' checked='true'> "+aTheme+"</a></td></tr>";
				}
				if (aTheme != "" && doRender=="") {
					content+="<tr><td class='themes'><input name='"+queryLayers+"' type='checkbox' value='"+aCaption+"'> "+aTheme+"</td></tr>";
				}
		}
		content+="</table>";
		content+="</form>";

		// Header
		contenth ="";
		contenth += "<span class='text11'><b>Layers</b></br></span>";

		// Footer
		contentf = "";
		contentf +="<span><button name='updateLayers' type='button' value='' onClick='map.layerbar.updateLayers(true);' style='height:22px; width:150px;' class='text10'><b>Update Layers</b></button><button name='promptUrl' type='button' value='' onClick='map.layerbar.promptUrl();' style='height:22px; width:90px;' class='text10'><b>URLs</b></button></span>";

		openLayer(this.map.name+"LayerBar","text11","z-index:10;", x, y, lbWidth, lbHeight, true);
		document.write("<div>");
		document.write(contenth);
		closeLayer();
		document.write("<div class='formular' style='overflow:auto;height:"+(lbHeight-50)+";'>");
		document.write(content);
		closeLayer();
		document.write("<div>");
		document.write(contentf);
		closeLayer();
		closeLayer();

	}

	// initLayer neu definiert
	this.initLayer = function(caption, title, layer, visible) {
		lcount = this.layerArray.length

		this.layerArray[lcount] = new Array(6);
		this.layerArray[lcount][0] = caption;
		this.layerArray[lcount][1] = title;
		this.layerArray[lcount][2] = layer;
		this.layerArray[lcount][3] = visible;
		}
	// Helper
	this.promptUrl = function() {
		// Schleife ueber alle Layer aus map.addLayer(...)
		for (i=0; i<this.map.layers.length; i++) {
			prompt(this.map.layers[i].caption,this.map.layers[i].url());
		}
	}

	this.updateLayers = function(refresh) {

    	aForm  = getForm('','LayerForm');
    	var selectedLayers ='';
    	var layersArray = aForm.elements;
    	var selectedLayers = new Array();
        // aScale wird fuer den Themenbaum beruecksichtigt
        var aScale = this.map.aScale;

		// Schleife ueber alle Layer aus map.addLayer(...)
		for (i=0; i<this.map.layers.length; i++) {
    		for (j = 0; j<layersArray.length; j++){
		    	if (layersArray[i].checked && layersArray[j].value == this.map.layers[i].caption) {
		    		this.map.layers[i].visible=true;
		    		break;
		    	} else if (!layersArray[i].checked && layersArray[j].value == this.map.layers[i].caption) {
		    		this.map.layers[i].visible=false;
		    		break;
		    	}
		    }
    	}

    	if (refresh==true) {
    		this.map.refresh();
    	}
    	else {return;}
	}

}


/* ###################################################################################################
* KDVZ - Erweiterung
* 2004-06-14 navigationWMS modifiziert
*
*
* ####################################################################################################
*/
function kdvzNavigationWMS(x1, y1, x2, y2, srs) {
	this.inheritFrom =kdvzMapWMS;
	this.inheritFrom(x1,y1,x2,y2,srs);
	this.hlBox=new Array(0,0,0,0);

	// write the html for the selection
	this.writeHTML[this.writeHTML.length]= function (objRef){
		{
			openLayer(objRef.name+"Highlight","","visibility:inherit",0, 0, objRef.width, objRef.height, true);
			//HighlightTop
			openLayer(objRef.name+"hlTop","","visibility:inherit",0, 0, objRef.width, objRef.height, true);
			var content="<table bgcolor='silver' border=0 style='filter:alpha(opacity=" + highLightThreshold + "); -moz-opacity:"+ (highLightThreshold/100)+";position:absolute;top:0;left:0' width=100% height=100%><tr><td></td></tr></table>"
			document.write(content);closeLayer();//HighlightTop
			//HighlightBottom
			openLayer(objRef.name+"hlBottom","","visibility:inherit",0, 0, objRef.width, objRef.height, true);
			document.write(content);closeLayer();//HighlightBottom
			//HighlightRigth
			openLayer(objRef.name+"hlRight","","visibility:inherit",0, 0, objRef.width, objRef.height, true);
			document.write(content);
			closeLayer();//HighlightRight
			//HighlightLeft
			openLayer(objRef.name+"hlLeft","","visibility:inherit",0, 0, objRef.width, objRef.height, true);
			document.write(content);closeLayer();//HighlightLeft
			//borderHighlight
			openLayer(objRef.name+"hlBorder","","border-style:solid;border-width:1;border-color:black;visibility:inherit",0, 0, objRef.width, objRef.height, true);
			var content="<table border=0 style='position:absolute;top:0;left:0' width=100% height=100%><tr><td></td></tr></table>"
			document.write(content);closeLayer();//borderHighlight
			closeLayer();//Highlight
			}
		}

	this.notify= function(mapRef,eventType){
		// this class only has one type of events ...
		// so no parsing is done on the eventType
		// future descendents can define other events
		if (mapRef.BBox) this.setHighLightXY(mapRef.BBox[0],mapRef.BBox[1],mapRef.BBox[2],mapRef.BBox[3]);
		}


	// the values in geographical coords and can be null
	this.setHighLightXY = function (x1, y1, x2, y2){
		if (areEqual(Array(x1, y1, x2, y2),this.BBox)){
			hideLayer(this.name+"Highlight");
			return false
			}
		else{
			showLayer(this.name+"Highlight");
			if (x1||x1==0)	{var pX1=geo2MouseX(this,x1);this.hlBox[0]=x1}
			else var pX1=geo2MouseX(this,this.hlBox[0]);

			if (y2||y2==0)	{var pY1=geo2MouseY(this,y2);this.hlBox[3]=y2}
			else var pY1=geo2MouseY(this,this.hlBox[3]);

			if (x2||x2==0)	{var pX2=geo2MouseX(this,x2);this.hlBox[2]=x2}
			else var pX2=geo2MouseX(this,this.hlBox[2]);

			if (y1||y1==0)	{var pY2=geo2MouseY(this,y1);this.hlBox[1]=y1}
			else {var pY2=geo2MouseY(this,this.hlBox[1]);	}

			//	if (y1==0) alert (pY2);
			var ratioX=(pX2-pX1)/this.width;
			var ratioY=(pY2-pY1)/this.height;
			if (ratioX>ratioY) var ratio=ratioX
			else	var ratio=ratioY;

			// KDVZ 2004-01-16
			// check if BBox is valid
			// new function refreshNavBox()
			if (ratio>0.6 || ratio<0.1) {refreshNavBBox(this,x1,y1,x2,y2);}
			else if (x1<this.BBox[0] || y1<this.BBox[1]) {refreshNavBBox(this,x1,y1,x2,y2);}
			else if (x2>this.BBox[2] || y2>this.BBox[3]) {refreshNavBBox(this,x1,y1,x2,y2);}
			else {refreshHighLight(this,pX1,pY1,pX2,pY2);}
			}

	// the values in screen coords and can be null
	this.setHighLight=function (x1, y1, x2, y2){
		// Note: mouse coordinate Y2 is equivalent hlBox[1]
		// (because screen coordinates have an inverse Y-axis

		// first check if the coordinates are correct
		var tmp;
		if (x1>x2){tmp=x1;x1=x2;x2=tmp}
		if (y1>y2){tmp=y1;y1=y2;y2=tmp}

		if (x1)	{var pX1=x1;this.hlBox[0]=mouse2GeoX(this,x1)}
		else var pX1=geo2MouseX(this,this.hlBox[0]);

		if (y1)	{var pY1=y1;this.hlBox[3]=mouse2GeoY(this,y1)}
		else var pY1=geo2MouseY(this,this.hlBox[3]);

		if (x2)	{var pX2=x2;this.hlBox[2]=mouse2GeoX(this,x2)}
		else var pX2=geo2MouseX(this,this.hlBox[2]);

		if (y2)	{var pY2=y2;this.hlBox[1]=mouse2GeoY(this,y2)}
		else var pY2=geo2MouseY(this,this.hlBox[1]);

		// refreshHighLight excepts the values in pixels (screen coordiantes)
		refreshHighLight(this,pX1,pY1,pX2,pY2)
		}


	}

}

/* ###################################################################################################
* KDVZ - Erweiterungen, Hilfsfunktionen
* 2004-01-22 getForm(), getCurrentWinWidth(), getCurrentWinHeight()
*
* ####################################################################################################
*/

function getForm (aLayerName, aFormName)
{
 if (browser.isOldNS)
  {
      if (aLayerName == '') {
         return window.document.forms[aFormName];
      }
      else {
         var aLayer = getLayer(aLayerName);
         return aLayer.document.forms[aFormName];
      }
  }
  else if(document.getElementById && document.getElementsByName)
  {
    if(document.getElementById(aFormName))                      //First try to find by id
       return document.getElementById(aFormName);
    else if (document.getElementsByName(aFormName))             //Then if that fails by name
           return document.getElementsByName(aFormName)[0];
  }
  else if (browser.isOldIE) {
    var elt = eval('document.all.' + aFormName);
    return(elt);
  }
}

function getCurrentWinWidth()
{ if (browser.isOldNS)     return(window.innerWidth);
  else if (browser.isDOM) return(document.body.clientWidth);
  else if (browser.isOldIE) return(document.body.clientWidth);
  //else if (browser.isDOM) return(window.innerWidth);
}

function getCurrentWinHeight()
{ if (browser.isOldNS)     return(window.innerHeight);
  else if (browser.isDOM) return(document.body.clientHeight);
  else if (browser.isOldIE) return(document.body.clientHeight);
  //else if (browser.isDOM) return(window.innerHeight);

}

function searchFeature() {
		showLayer("SearchBar");
		document.getElementById("Strassen").style.setAttribute("backgroundColor","#BFBFBF","false");
		document.getElementById("Strassen").style.setAttribute("color","#000066","false");
		myFrame = document.getElementById("StrasseRequest");
		myFrame.src = "/demo/examples/request_deegree.php";
}

function switchThemes() {
		showLayer("myMapLayerBar");
		document.getElementById("Themen").style.setAttribute("backgroundColor","#BFBFBF","false");
		document.getElementById("Themen").style.setAttribute("Color","#000066","false");
}

function switchTool(current,future) {
	hideLayer(current);
	document.getElementById("Themen").style.setAttribute("backgroundColor","BFBFFF","false");
	document.getElementById("Themen").style.setAttribute("Color","#FFFFFF","false");
	document.getElementById("Strassen").style.setAttribute("backgroundColor","BFBFFF","false");
	document.getElementById("Strassen").style.setAttribute("color","#FFFFFF","false");
	document.getElementById("Adressen").style.setAttribute("backgroundColor","BFBFFF","false");
	document.getElementById("Adressen").style.setAttribute("color","#FFFFFF","false");

	if (future == "SearchBar") searchFeature();
	if (future == "myMapLayerBar") switchThemes();
	else showLayer(future);
}