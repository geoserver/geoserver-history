
/*
* Project quickWMS: Generic JavaScript WMS Client
* File : WMSnavigation.js
* Author : Pedro Pereira Gonçalves
* email : pedro.goncalves@esa.int or pedro@inovagis.org
* Version : 0.2
* Description: Defines a descendent of mapWMS that draws a highlight box over
*                    a defined geographical region. It can be linked to another mapWMS
*                    so that automatically updates the highlighted box for the visible region
*                    Please read coderules.txt before making any change
* Last Change : 2003-11-18
* Dependencies : WMSlayer.js, WMSmap.js, WMSbrowsers.js
* Future Developments :
* License : OpenSource  (check license.txt in the same directory)
* History :
    2003-11-18 : Code documentation added
    2003-03-20 : File Created
*/



function navigationWMS(x1, y1, x2, y2, srs) {
	this.inheritFrom =mapWMS;
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

// general private function
// in screen coordinates
function refreshHighLight(mapRef,x1,y1,x2,y2)
{
	if (x2<(x1+5)) x2=x1+10;
	if (y2<(y1+5)) y2=y1+10;
	setLayerPos(mapRef.name+"hlTop",x1,0,x2-x1,y1);
	setLayerPos(mapRef.name+"hlBottom",x1,y2,x2-x1,mapRef.height-y2);
	setLayerPos(mapRef.name+"hlLeft",0,0,x1,mapRef.height);
	setLayerPos(mapRef.name+"hlRight",x2,0,mapRef.width-x2,mapRef.height);
	//if (browser.isNS7)setLayerPos(mapRef.name+"hlBorder",x1,y1,x2-x1-2,y2-y1-2)
	//else
	setLayerPos(mapRef.name+"hlBorder",x1,y1,x2-x1-2,y2-y1-2)
}

// KDVZ 2004-01-16
// new function refreshNavBox()
function refreshNavBBox(mapRef,x1,y1,x2,y2)
{
    var newX1=x1-(x2-x1)/1;//change this number if you want to change the size of the hightlighted box
    var newX2=x2+(x2-x1)/1;//a bigger number (like 2 or 3) will make it bigger
    var newY1=y1-(y2-y1)/1;// attention not to use  0.5
    var newY2=y2+(y2-y1)/1;
    // TODO check if the BBox is still valid

    mapRef.updateBBox(newX1, newY1, newX2, newY2);
    if (areEqual(mapRef.initBBox,mapRef.BBox)){
            pX1=geo2MouseX(mapRef,x1);
            pY1=geo2MouseY(mapRef,y2);
            pX2=geo2MouseX(mapRef,x2);
            pY2=geo2MouseY(mapRef,y1);
            //alert("new bbox " + newX1 + ", " + newY1 + ", " + newX2  + ", "  + newY2);
            if (areEqual(Array(x1, y1, x2, y2),mapRef.BBox)){
                hideLayer(mapRef.name+"Highlight");
                return false
                }
            else {refreshHighLight(mapRef,pX1,pY1,pX2,pY2)};
            }
    else{
            if (areEqual(mapRef.hlBox,mapRef.BBox)){hideLayer(mapRef.name+"Highlight");return false}
                else {mapRef.setHighLightXY(x1, y1, x2, y2);}
            }
}