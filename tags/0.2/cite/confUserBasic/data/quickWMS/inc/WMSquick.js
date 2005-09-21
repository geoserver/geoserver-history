
/*
* Project quickWMS: Generic JavaScript WMS Client
* File : WMSquick.js
* Author : Pedro Pereira Gonçalves
* email : pedro.goncalves@esa.int or pedro@inovagis.org
* Version : 0.1
* Description: Adds mouse events for selection and zoom to the mapWMS class
*                    Please read coderules.txt before making any change
* Tested on : Netscape 7.0, IE 5.5, IE 6.0
* Last Change : 2003-05-02
* Dependencies : WMSbrowsers.js, WMSmap.js, WMSlayers.js, WMStools.js
* Future Developments :
* License : OpenSource (check license.txt in the same directory)
* History :
    2003-04-23 : Documentation added
    2003-03-20 : File Created
* Objects : dynamicWMS
* Functions : none
*/

function quickWMS(x1, y1, x2, y2, srs) {
	this.inheritFrom = mapWMS;
	this.inheritFrom(x1, y1, x2, y2, srs);
	this.toolMode="";
	this.slBox=new Array(0, 0, 0, 0);	 // array variable for the selected area
	this.zBox=new Array(0, 0, 0, 0);	// private zoom array variable
	this.prevBBox = new Array;
	this.prevBBox[this.prevBBox.length]=new Array(this.BBox[0], this.BBox[1], this.BBox[2], this.BBox[3]);

	this.indexBBox=0;

	/// events
	this.onMouseMove=null;
	this.onMouseClick=null;
	this.onMouseUp=null;
	this.onMouseDown=null;

	// write the html for the selection
	this.writeHTML[this.writeHTML.length]= function (objRef){
		openLayer(objRef.name+"Selection","selectionLayer","visibility:inherit;",0, 0, objRef.width, objRef.height, true);
		if (browser.isOldNS) document.write('<table border="2" width="100%" height="100%"><tr height=5 bgcolor="black"><td colspan="3"></td></tr><tr><td bgcolor="black" width="5"></td><td></td><td bgcolor="black" width="5"></td></tr></table>');
		closeLayer();
		hideLayer(objRef.name+"Selection");

		openLayer(objRef.name+"zoom","zoomSelectionLayer","visibility:inherit;",0, 0, objRef.width, objRef.height, true);
		if (browser.isOldNS) document.write('<table border="2" width="100%" height="100%"><tr height=5 bgcolor="black"><td colspan="3">ola</td></tr><tr><td bgcolor="black" width="5"></td><td></td><td bgcolor="black" width="5"></td></tr></table>');
		closeLayer();
		hideLayer(objRef.name+"zoom");
		}

	// functions
	// this function is called from the writeDOM function in its parent (mapWMS)
	this.initialize = function () {
			assignMouseEvent(this.name,"mousemove",WMS_mouseMove, true);
			assignMouseEvent(this.name,"mouseup",WMS_mouseUp, true);
			assignMouseEvent(this.name,"mousedown",WMS_mouseDown, true);
		}

	this.mouseToolManager=WMS_mouseToolManager;

	this.setSelectionXY= function(x1, y1, x2, y2){
		if (x1)	this.slBox[0]=x1;
		if (y1) this.slBox[1]=y1;
		if (x2) this.slBox[2]=x2;
		if (y2) this.slBox[3]=y2;
		var pX1=geo2MouseX(this,this.slBox[0]);
		var pY1=geo2MouseY(this,this.slBox[3]);
		var pX2=geo2MouseX(this,this.slBox[2]);
		var pY2=geo2MouseY(this,this.slBox[1]);
		if (browser.isOldNS) layer = getLayer(this.name+"Selection").clip;//style
		else layer = getLayer(this.name+"Selection").style;
		if (pX1>pX2){layer.left=pX2;layer.width=pX1-pX2;}
		else {layer.left=pX1;layer.width=pX2-pX1;}
		if (pY1>pY2){layer.top=pY2;layer.height=pY1-pY2;}
		else {layer.top=pY1;layer.height=pY2-pY1;}
		if (parseInt(layer.width)>this.width) layer.width=this.width;
		if (parseInt(layer.left)<0) layer.left=0;
		if (parseInt(layer.height)>this.height) layer.height=this.height;
		if (parseInt(layer.top)<0) layer.top=0;
		}

	this.setSelection = function (x1, y1, x2, y2){
		if (x1)	{var pX1=x1;this.slBox[0]=mouse2GeoX(this,x1)}
		else var pX1=geo2MouseX(this,this.slBox[0]);
		if (y1)	{var pY1=y1;this.slBox[3]=mouse2GeoY(this,y1)}
		else var pY1=geo2MouseY(this,this.slBox[3]);
		if (x2)	{var pX2=x2;this.slBox[2]=mouse2GeoX(this,x2)}
		else var pX2=geo2MouseX(this,this.slBox[2]);
		if (y2)	{var pY2=y2;this.slBox[1]=mouse2GeoY(this,y2)}
		else var pY2=geo2MouseY(this,this.slBox[1]);

		if (browser.isOldNS) {layer = getLayer(this.name+"Selection").clip;}//style
		else layer = getLayer(this.name+"Selection").style;

		// check the correct position in the slBox array
		if (this.slBox[1]>this.slBox[3]){
			var tVal=this.slBox[1];
			this.slBox[1]=this.slBox[3];
			this.slBox[3]=tVal;
			tVal=pY1;
			pY1=pY2;
			pY2=tVal;
		}
		if (this.slBox[0]>this.slBox[2]){
			var tVal=this.slBox[0];
			this.slBox[0]=this.slBox[2];
			this.slBox[2]=tVal;
			tVal=pX1;
			pX1=pX2;
			pX2=tVal;
		}

		//getLayer(this.name+"Selection").bgColor="#CC00EE";
		if (pX1>pX2){layer.left=pX2;layer.width=pX1-pX2;}
		else {layer.left=pX1;layer.width=pX2-pX1;}
		if (pY1>pY2){layer.top=pY2;layer.height=pY1-pY2;}
		else {layer.top=pY1;layer.height=pY2-pY1;}
		if (parseInt(layer.width)>this.width) layer.width=this.width;
		if (parseInt(layer.left)<0) layer.left=0;
		if (parseInt(layer.height)>this.height) layer.height=this.height;
		if (parseInt(layer.top)<0) layer.top=0;
		}

	this.setZoomBox = function (x1, y1, x2, y2){
		if (x1)	{var pX1=x1;this.zBox[0]=mouse2GeoX(this,x1)}
		else var pX1=geo2MouseX(this,this.zBox[0]);
		if (y1)	{var pY1=y1;this.zBox[3]=mouse2GeoY(this,y1)}
		else var pY1=geo2MouseY(this,this.zBox[3]);
		if (x2)	{var pX2=x2;this.zBox[2]=mouse2GeoX(this,x2)}
		else var pX2=geo2MouseX(this,this.zBox[2]);
		if (y2)	{var pY2=y2;this.zBox[1]=mouse2GeoY(this,y2)}
		else var pY2=geo2MouseY(this,this.zBox[1]);

		if (browser.isOldNS){ layer = getLayer(this.name+"zoom").clip;}//alert(layer)}//style
		else layer = getLayer(this.name+"zoom").style;

		if (pX1>pX2){layer.left=pX2;layer.width=pX1-pX2;}
		else {layer.left=pX1;layer.width=pX2-pX1;}

		if (pY1>pY2){layer.top=pY2;layer.height=pY1-pY2;}
		else {layer.top=pY1;layer.height=pY2-pY1;}
		}

	// this class will not use the inherited updateBBox method because it keeps a list of previous requests
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
		this.refresh();
		}
	// this function is also overrided
	this.cancelRequest = function(){
		hideLayer(this.name+"Status");
		clearInterval(this.waitFunction);
		if (this.indexBBox>0) {
			this.prevBBox.length--
			this.indexBBox--;
			}
		copy(this.lastBBox,this.BBox);
		this.refresh();
		}
	this.back=function(){
		if (this.indexBBox>0){
			this.indexBBox-=1;
			this.BBox[0]=this.prevBBox[this.indexBBox][0];
			this.BBox[1]=this.prevBBox[this.indexBBox][1];
			this.BBox[2]=this.prevBBox[this.indexBBox][2];
			this.BBox[3]=this.prevBBox[this.indexBBox][3];
			this.refresh();
			return true;
			}
		else return false;
		}

	this.home=function(){
		this.updateBBox(this.initBBox[0], this.initBBox[1],this.initBBox[2],this.initBBox[3]);
		}

	this.forward=function(){
		if (this.indexBBox<this.prevBBox.length-1){
			this.indexBBox+=1;
			this.BBox[0]=this.prevBBox[this.indexBBox][0];
			this.BBox[1]=this.prevBBox[this.indexBBox][1];
			this.BBox[2]=this.prevBBox[this.indexBBox][2];
			this.BBox[3]=this.prevBBox[this.indexBBox][3];
			this.refresh();
			}
		}

	}