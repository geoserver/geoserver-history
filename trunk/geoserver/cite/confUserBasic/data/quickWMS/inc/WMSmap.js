/*
* Project : Generic JavaScript WMS Client
* File : WMSmap.js
* Author : Pedro Pereira Gonçalves
* email : pedro.goncalves@esa.int or pedro@inovagis.org
* Version : 0.2
* Description: Simple wms map client with a list of layers originating from
*                     different servers. No mouse support present in this class
*                      (check quickWMS class at WMSquick.js)
* Tested on : Netscape 7.0, IE 5.5, IE 6.0
* Last Change : 2003-06-20
* Dependencies : WMSlayer.js, WMSbrowsers.js
* Future Developments :
* License : OpenSource (check license.txt in the same directory)
* History :
	2003-10-24 : Added the option to select direct loading without using the transport image
                          by default is false in all browsers except in IE 6 (because this browsers will always recall the image even if it has an object in memory)
    2003-04-28 : Write img of each layer passed to the layerWMS writeDOM method
    2003-04-23 : Documentation added
    2003-03-20 : File Created
* Objects : mapWMS
* Functions :
             function mouse2GeoX(obRef,mX)
             function mouse2GeoY(obRef,mY)
             function geo2MouseX(obRef,cX)
             function geo2MouseY(obRef,cY)
             function geoConstraints(obRef)

*/

function mapWMS(x1, y1, x2, y2, srs){

    this.caption = "";
    this.BBox = new Array(x1,y1,x2,y2);
	this.initBBox = new Array(x1,y1,x2,y2);
	this.lastBBox = new Array(x1,y1,x2,y2);
	this.lastLoaded = ""; //works for the status captions
	// default value for srs is EPSG:4326
	if (srs) this.srs=srs;
	else this.srs="EPSG:4326";

	// list of layers requests
	this.layers=new Array;
	this.directLoading=false; // if directLoading is true then it will not use a temporary image while receiving the requests
	// in IE60 this is true by default
	if (browser.isIE60) this.directLoading=true;

	// additional HTML lines to add to the main layer
	this.writeHTML=new Array;

	//	geographical listeners events
 	this.listeners = new Array;
	// private features - dont thouch :)
	// ***** none

	// not documented features
	// ***** none

	/// events
	this.onChange=null; // it must be defined as function(layer){}
	this.onBeforeChange=null; //it must be defined as function (wmsLayer,x1,y1,x2,y2)
	this.onError = null; // it must be defined as function(layer){}
	this.onSelecting = null;//it must be defined as function (wmsLayer,x1,y1,x2,y2)
	this.onSelection = null;//it must be defined as function (wmsLayer,x1,y1,x2,y2)

	// functions
	this.refresh = function (){
		window.status="";
		showLayer(this.name+"Status");
		this.setStatus(TXT_OnRequestingData);
		for(i=0; i<this.layers.length; i++) this.layers[i].refresh();
		this.waitFunction=null;
		this.waitFunction=setTimeout('waitfunction("'+ this.name + '")',200);
		return true;
		}

	this.addLayer = function(url, layer, version, caption){
		this.layers[this.layers.length] = new layerWMS(this, url, layer, caption, version);
		return true;
		}

	this.updateBBox = function (x1, y1, x2, y2){
		if (this.onBeforeChange){this.onBeforeChange(this,x1,y1,x2,y2);}
		copy(this.BBox,this.lastBBox);
		this.BBox[0]=x1/1;this.BBox[1]=y1/1;
		this.BBox[2]=x2/1;this.BBox[3]=y2/1;
		geoConstraints(this);
		this.refresh();
		}

	this.cancelRequest = function(){
		hideLayer(this.name+"Status");
		clearInterval(this.waitFunction);
		copy(this.lastBBox,this.BBox);
		this.refresh();
		}

	this.zoomTo = function (x,y,zoomFactor){
		var sizeX=(this.BBox[2]-this.BBox[0])/zoomFactor;
		var sizeY=(this.BBox[3]-this.BBox[1])/zoomFactor;
		this.updateBBox(x-sizeX/2,y-sizeY/2,x+sizeX/2,y+sizeY/2);
		}

	this.notify= function(mapRef){
		}

	this.addListener = function(mapRef){
		mapRef.listeners[mapRef.listeners.length]=this;
		}


	this.setStatus = function (value){
		if (browser.isOldNS) {
			// TO-DO : set the status for netscape 4.7
			}
		else{
			var content="<table style='position:absolute;top:0;left:0' width=100% height=100%><tr align='center' valign='bottom'><td class='statusText'>"
			content+= "<span style='cursor:wait'>"+value+"</span>";
			content+="</td></tr></table>";
			setLayerHTML(this.name+"StatusTXT", content);
			}
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
		content+=" style='cursor:pointer'>Waiting...</span>";
		content+="</td></tr></table>";
		document.write(content);
		closeLayer();//statusIMG
		closeLayer();//status
		closeLayer();//name

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
	}




// ******* geoConstraints(obRef) *************
// Check for the geographical and proportions constrains of the bounding box
function geoConstraints(obRef)
{
  // check min and max values
  if (obRef.BBox[0]>obRef.BBox[2]){
  	var tmpX1=obRef.BBox[0];
  	obRef.BBox[0]=obRef.BBox[2];
	obRef.BBox[2]=tmpX1;
	}
  if (obRef.BBox[1]>obRef.BBox[3]){
  	var tmpY1=obRef.BBox[1];
  	obRef.BBox[1]=obRef.BBox[3];
	obRef.BBox[3]=tmpY1;
	}
  // check first if current BBox obeys the initial proportions
  var initPropor = (obRef.initBBox[2] - obRef.initBBox[0]) / (obRef.initBBox[3] - obRef.initBBox[1]);
  var Propor = (obRef.BBox[2] - obRef.BBox[0]) / (obRef.BBox[3] - obRef.BBox[1]);
  if (initPropor!=Propor)
  	{
		var sizeX = obRef.BBox[2] - obRef.BBox[0];
		var sizeY = obRef.BBox[3] - obRef.BBox[1];
		if (sizeY<sizeX){
			var midY= obRef.BBox[1] + (sizeY)/2;
			var newSizeY= (sizeX)/initPropor;
			obRef.BBox[1]= midY - newSizeY / 2;
			obRef.BBox[3]= midY + newSizeY / 2;
			}
		else{
			var midX= obRef.BBox[0] + (sizeX)/2;
			var newSizeX= (sizeY)*initPropor;
			obRef.BBox[0]= midX - newSizeX / 2;
			obRef.BBox[2]= midX + newSizeX / 2;
			}
	}

  // check if both BBox axis are inside the initial BBox
  // check X-axis
  var SizeX = obRef.BBox[2] - obRef.BBox[0];
  if (obRef.BBox[0]<obRef.initBBox[0])
     {
	      obRef.BBox[0] = obRef.initBBox[0];
          obRef.BBox[2] = obRef.BBox[0] + SizeX;
          if (obRef.BBox[2]>obRef.initBBox[2]) { obRef.BBox[2]=obRef.initBBox[2]}
     }
  else
	{
       if (obRef.BBox[2]>obRef.initBBox[2])
          {
            obRef.BBox[2] = obRef.initBBox[2];
            obRef.BBox[0] = obRef.BBox[2] - SizeX;
          }
     }
  // check Y-axis
  var SizeY = (obRef.BBox[3] - obRef.BBox[1]);

  if (obRef.BBox[1]<obRef.initBBox[1])
     {
       obRef.BBox[1] = obRef.initBBox[1];
       obRef.BBox[3] = obRef.BBox[1] + SizeY;
       if (obRef.BBox[3]>obRef.initBBox[3]) {obRef.BBox[3]=obRef.initBBox[3]}
     }
  else
     {
       if (obRef.BBox[3]>obRef.initBBox[3])
          {
            obRef.BBox[3]= obRef.initBBox[3];
            obRef.BBox[1]= obRef.BBox[3] - SizeY;
          }
     }
/* to do : arrows should me made invisible
  if (document.getElementById(obRef.name+"Arrows")) {
  if ((obRef.Y2== obRef.initY2) && (obRef.Y1== obRef.initY1) &&
  	(obRef.X2== obRef.initX2) && (obRef.X1== obRef.initX1))
	{hideArrows(obRef,true)}
else
	{hideArrows(obRef,false)}
	}
	*/
}

// *** Mouse to geographical conversion function
function mouse2GeoX(obRef,mX)
{return obRef.BBox[0] + mX * (obRef.BBox[2] - obRef.BBox[0])/obRef.width;}
function mouse2GeoY(obRef,mY)
{return obRef.BBox[3] - mY * (obRef.BBox[3] - obRef.BBox[1])/ obRef.height;}

// ***  Geographical to mouse conversion function
function geo2MouseX(obRef,cX)
{return Math.round(obRef.width * (cX - obRef.BBox[0])/ (obRef.BBox[2] - obRef.BBox[0]) )}
function geo2MouseY(obRef,cY)
{return Math.round( obRef.height * (obRef.BBox[3] - cY) / ( obRef.BBox[3] - obRef.BBox[1] )); }



// ************* waitfunction(mapName) ****************
// function that waits for all the images to arrive
// you should NOT call this function from your code
function waitfunction(mapName){
	var missingName="";
	var layerRef=getLayer(mapName);

	if (layerRef.reference){
		var mapRef=layerRef.reference;
		var stillMissing = 0;
		for (i=0; i<mapRef.layers.length; i++){
			// if there is an image still loading
			if (!mapRef.directLoading) {
				if (mapRef.layers[i].transportIMG.loading){
					stillMissing+=1;
					missingName =mapRef.layers[i].caption;
					}
				}else{
				//alert(mapRef.layers[i].DOMref.loading);
				if (mapRef.layers[i].DOMref.loading){
					stillMissing+=1;
					missingName =mapRef.layers[i].caption;

					}
				}
			}
		if (stillMissing>0){
			if (stillMissing==1 && mapRef.layers.length>1) mapRef.setStatus(layerRef.reference.lastLoaded + " " + TXT_ReceivedLayer +"\n<br>" +TXT_WaitingFor +" " + missingName )//stillMissing + TXT_Request)
			else
					if (stillMissing==mapRef.layers.length) mapRef.setStatus( TXT_WaitingFor + stillMissing + TXT_Request);
					else mapRef.setStatus( layerRef.reference.lastLoaded + " " + TXT_ReceivedLayer +"\n<br>" +TXT_WaitingFor + stillMissing + TXT_Request);
			mapRef.waitFunction=setTimeout('waitfunction("'+ mapRef.name + '")',1000);
			}
		else{

			clearInterval(mapRef.waitFunction);
			mapRef.setStatus(TXT_LayerUpdated);
			if (!mapRef.directLoading) {
				for (i=0; i<mapRef.layers.length; i++)
					{mapRef.layers[i].DOMref.src=mapRef.layers[i].transportIMG.src;};
			}
			hideLayer(mapName+"Status");
			if(mapRef.onChange){mapRef.onChange(mapRef)};
			if(mapRef.listeners){
				for (i=0;i<mapRef.listeners.length;i++){mapRef.listeners[i].notify(mapRef, TXT_onChange)};
				}
			if (mapRef.setSelectionXY){
				if (isVisible(mapRef.name+"Selection")){
					mapRef.setSelectionXY();
					}
				}

			return true;
			}
		}
	}