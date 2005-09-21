/*
* Project quickWMS: Generic JavaScript WMS Client
* File : WMStools.js
* Author : Pedro Pereira Gonçalves
* email : pedro.goncalves@esa.int or pedro@inovagis.org
* Version : 0.1
* Description: Deals with mouse functions and basic WMS tools and toolbars
*                     Please read coderules.txt before making any change
* Tested on : Netscape 7.0, IE 5.5, IE 6.0
* Last Change : 2003-10-24
* Dependencies : WMSbrowsers.js
* Future Developments : Pan buttons (left, right .. and so on )
* License : OpenSource (check license.txt in the same directory)
* History :
   	2003-10-24 : When in select mode clicking again in the selection button will disable the tool and hide the selection layer
	2003-04-23 : Documentation added
    2003-03-20 : File Created
* Objects :
* Functions :
*/


function toolBar(obRefName){
	//obRefName.toolBar=this;
	this.map=obRefName;
	// KDVZ 2004-01-16, new buttons recentre,print and german text
	this.images = new Array ('back.gif', 'forward.gif', 'home.gif', 'zsel.gif', 'zminus.gif', 'recentre.gif', 'print.gif', 'select.gif');
	this.help = new Array ('back to previous view', 'jump to next view', 'go to first to home view','select area to zoom', 'zoom out', 'recentre tool', 'print', 'select tool');
	//this.images = new Array ('back.gif', 'forward.gif', 'home.gif', 'zsel.gif', 'zminus.gif', 'recentre.gif', 'print.gif', 'select.gif');
	//this.help = new Array ('zurück', 'vorwärts', 'Startansicht','Ausschnit vergößern', 'Ausschnitt verkleinern', 'Zentrieren', 'Drucken', 'Auswählen');
	//this.images = new Array ('back.gif', 'forward.gif', 'home.gif', 'zsel.gif', 'zminus.gif', 'recentre.gif', 'print.gif');
	//this.help = new Array ('zurück', 'vorwärts', 'Startansicht','Ausschnit vergößern', 'Ausschnitt verkleinern', 'Zentrieren', 'Drucken');
	this.events = new Array;
	this.events[0]= function (map) {map.back();}
	this.events[1]= function (map) {map.forward();}
	this.events[2]= function (map) {map.home();}
	this.events[3]= function (map) {map.toolMode='selzoom';map.toolbar.reset();map.toolbar.imgs[3].src=path_skin+'h_'+map.toolbar.images[3]}
	this.events[4]= function (map) {map.toolMode='zoomout';map.toolbar.reset();map.toolbar.imgs[4].src=path_skin+'h_'+map.toolbar.images[4]}
	this.events[5]= function (map) {map.toolMode='pan';map.toolbar.reset();map.toolbar.imgs[5].src=path_skin+'h_'+map.toolbar.images[5]}
	this.events[6]= function (map) {map.printLayer();}
	this.events[7]= function (map) {if (map.toolMode=='selection') {
                                            map.toolMode='';
                                            hideLayer(map.name+"Selection");
                                            map.toolbar.reset();
                                            }
									else {
                                            map.toolMode='selection';
                                            map.toolbar.reset();
                                            map.toolbar.imgs[7].src=path_skin+'h_'+map.toolbar.images[7];
                                         }
									}
	this.reset = function () {
			for(i=0; i<this.images.length; i++){
				this.imgs[i].src=path_skin+this.images[i];
			}
		}

	this.writeDOM = function ( x, y, horiz)	{
			var content="";
			if (browser.isOldNS){var style=""}
			else {var style=" style='cursor:pointer;cursor:hand'"}
			for(i=0; i<this.images.length; i++){
				content+="<img src='"+path_skin+this.images[i]+"'"+style+" width=24 height=24 border=0 alt='"+this.help[i]+"' onmouseup=getLayer('"+this.map.name+"ToolBar').reference.events["+i+"](getLayer('"+this.map.name+"ToolBar').reference.map)>";
			}
			if (horiz) {openLayer(this.map.name+"ToolBar","","", x, y, 25*this.images.length, 25, true);}
			else {openLayer(this.map.name+"ToolBar","","", x, y, 25, 25*this.images.length, true);}
			document.write(content);
			closeLayer();

			// 2003-10-24 : Note .. for the tools to be compatible with ns47 we should make a special case for it because it doesn't works with getElementXXX methods
			if (browser.isNS47x)	{
				getLayer(this.map.name+"ToolBar").reference=this;
				this.imgs=getLayer(this.map.name+"ToolBar").document.images;
			}
			else {
				//getLayer(this.map.name+"ToolBar").reference=this;
				//this.imgs=getLayer(this.map.name+"ToolBar").getElementsByTagName("img");
				// 2003-10-13 : changed this because of ns71
				document.getElementById(this.map.name+"ToolBar").reference=this;
				this.imgs=document.getElementById(this.map.name+"ToolBar").getElementsByTagName("img");
			}

		}
}


function getMouseXY(e)
{

	var mouseX;
	var mouseY;
	if (browser.isIE50 || browser.isIE55 || browser.isIE60){e = window.event;}


	if (e.layerX)
	{
		mouseX =e.layerX;
		mouseY =e.layerY;
	}
	else
	if (browser.isOldNS)
  	{
		mouseX=e.pageX;
		mouseY=e.pageY;
	}
  else
  {
		mouseX=e.clientX;
		mouseY=e.clientY;
  		mouseX=e.offsetX;
		mouseY=e.offsetY;
	}
  	mouseX++;
	mouseY++;

	mouseX=Math.round(mouseX/mouseGrid)*mouseGrid;
	mouseY=Math.round(mouseY/mouseGrid)*mouseGrid;

	if (e.target){
		if (e.target.parent) return new layerPoint(e.target.parent,mouseX,mouseY);
		}
	else{
		if (e.srcElement){
			if (e.srcElement.parentElement){
				if (e.srcElement.parentElement.reference){
					return new layerPoint(e.srcElement.parentElement.reference,mouseX,mouseY)
					}
				}
			}
		else if (e.currentTarget) return new layerPoint(e.currentTarget.reference,mouseX,mouseY);

		}

}



function WMS_zoomTool(obRef, eventType, X, Y){
	if (eventType=="mouseMove" && obRef.toolMode=="zooming"){
		showLayer(obRef.name+"zoom");
		obRef.setZoomBox(null,null,X,Y);
		return true;
		}
	else
	if (eventType=="mouseMove" && obRef.toolMode=="selecting"){
		showLayer(obRef.name+"Selection");
		if (obRef.onSelecting){
				var tX2=mouse2GeoX(obRef,X);
				var tY1=mouse2GeoY(obRef,Y);
				var tX1=obRef.slBox[0];
				var tY2=obRef.slBox[3];
				if (tX2<obRef.slBox[0]){
					tX1=tX2;
					tX2=obRef.slBox[0];
					}
				/*if (tY2<obRef.slBox[1]){
					tY1=tY2;
					tY2=obRef.slBox[1];
					}
					*/
				if (obRef.onSelecting(obRef,tX1,tY1,tX2,tY2)){
					obRef.setSelection(null,null,X,Y);
				}
			}
		else{
			obRef.setSelection(null,null,X,Y);
		}

		return true;
		}
	else
	if (eventType=="mouseDown") {
		if (obRef.toolMode=="selzoom"){
			if(obRef.setZoomBox){
				if (obRef.toolMode=="zooming"){
					obRef.setZoomBox(null,null,X,Y);
					}
				else{
					obRef.setZoomBox(X,Y,X,Y);
					obRef.toolMode="zooming";
					//showLayer(obRef.name+"Selection");
					}
				}
			}
		if (obRef.toolMode=="selection"){
			if(obRef.setSelection){
				if (obRef.toolMode=="selecting"){
					obRef.setSelection(null,null,X,Y);
					}
				else{
					obRef.setSelection(X,Y,X,Y);
					obRef.toolMode="selecting";
					//showLayer(obRef.name+"Selection");
					}
				}
			if (obRef.onSelecting){obRef.onSelecting(obRef,X,Y,X,Y)}
			}
		// KDVZ 2004-01-16, recentre
		if (obRef.toolMode=="pan") {
				obRef.setZoomBox(X,Y,null,null);
			}

		return true
		}
	else
	if (eventType=="mouseUp") {
		if (obRef.toolMode=="zooming"){
			if(obRef.setZoomBox){
				if (isVisible(obRef.name+"zoom")){
					obRef.setZoomBox(null,null,X,Y);
					if (  X-geo2MouseX(obRef,obRef.zBox[0])<5 && Y-geo2MouseY(obRef,obRef.zBox[1])<5) {
						obRef.zoomTo(mouse2GeoX(obRef,X),mouse2GeoY(obRef,Y),2);
						}
					else obRef.updateBBox( obRef.zBox[0], obRef.zBox[1],obRef.zBox[2],obRef.zBox[3]);
					hideLayer(obRef.name+"zoom");
					}
				else{
					obRef.zoomTo(mouse2GeoX(obRef,X),mouse2GeoY(obRef,Y),2);
					hideLayer(obRef.name+"zoom");
					}
				}
			else{

				}
			obRef.toolMode="selzoom";
			return true;
			}
		if (obRef.toolMode=="zoomout"){
			obRef.zoomTo(mouse2GeoX(obRef,X),mouse2GeoY(obRef,Y),0.5);
			return true;
			}
		if (obRef.toolMode=="selecting"){
			obRef.toolMode="selection";
			// KDVZ Mods 2004-01-16
			// return true;
			if (obRef.onSelection){obRef.onSelection(obRef,obRef.slBox[0],obRef.slBox[1],obRef.slBox[2],obRef.slBox[3])}
			hideLayer(obRef.name+"Selection");
			return true;
			}
		// KDVZ 2004-01-16, recentre
		if (obRef.toolMode=="pan"){
			obRef.zoomTo(mouse2GeoX(obRef,X),mouse2GeoY(obRef,Y),1.0);
			return true;
			}
		}
	else
		return false
	}

function WMS_mouseToolManager(eventType, X, Y){
	if (true)//(this.toolMode=="selzoom" || this.toolMode=="zooming" || this.toolMode=="zoomout")
		{return WMS_zoomTool(this,eventType, X, Y);}
	else
		return false;
	}

function WMS_mouseUp(e){
	mouse=getMouseXY(e);

	if (mouse){
		if (!mouse.layer.mouseToolManager("mouseUp",mouse.X,mouse.Y)){
			if (mouse.layer.onMouseUp)
				{mouse.layer.onMouseUp(mouse.layer, mouse.X,mouse.Y);}
			}
		}
	// Returning true on a mouse event insures that window.status will not be
	//   overwritten (browser convention)
	return false;
	}
function WMS_mouseDown(e){
	mouse=getMouseXY(e);
	//window.status+=mouse.X;
	if (mouse){
		if (!mouse.layer.mouseToolManager("mouseDown",mouse.X,mouse.Y)){
			if (mouse.layer.onMouseUp){
				mouse.layer.onMouseUp(mouse.layer, mouse.X,mouse.Y);
				}
			}
		}
	// Returning true on a mouse event insures that window.status will not be
	//   overwritten (browser convention)
	stopEventPropagation(e);
	//return false;
	  return true;
	}

function WMS_mouseMove(e){
	if (getMouseXY){
			mouse=getMouseXY(e);
	if (mouse){
		if (!mouse.layer.mouseToolManager("mouseMove",mouse.X,mouse.Y)){
			if (mouse.layer.onMouseMove){
				mouse.layer.onMouseMove(mouse.layer,mouse2GeoX(mouse.layer,mouse.X),mouse2GeoY(mouse.layer,mouse.Y));
				}
			}
		}
	stopEventPropagation(e);
	return false;//true;
		}
	}

function changeBBox(obRef,value)
{
	var box=value.split(',');
	obRef.updateBBox (box[0],box[1],box[2],box[3]);
}



//undocumented functions
/*

function writeNavArrows(x,y, obRef){
	openLayer(obRef.name+"Arrows","","filter:alpha(opacity=80);-moz-opacity:0.80;border-style:solid;border-width:1;background-color:white;", x, y, 64,67, true);

	document.write("<img onclick=moveBy('"+obRef.name+"') style='position:absolute;top:5;left:5' src='"+path_IMG+"nw.gif' width=24 height=24 border=0 alt=''>");
	document.write("<img onclick=moveBy('"+obRef.name+"') style='position:absolute;top:2;left:20' src='"+path_IMG+"n.gif' width=24 height=24 border=0 alt=''>");
	document.write("<img onclick=moveBy('"+obRef.name+"') style='position:absolute;top:5;left:35' src='"+path_IMG+"ne.gif' width=24 height=24 border=0 alt=''>");

	document.write("<img onclick=moveBy('"+obRef.name+"') style='position:absolute;top:21;left:5' src='"+path_IMG+"w.gif' width=24 height=24 border=0 alt=''>");
	document.write("<img onclick=moveBy('"+obRef.name+"') style='position:absolute;top:21;left:37' src='"+path_IMG+"e.gif' width=24 height=24 border=0 alt=''>");

	document.write("<img onclick=moveBy('"+obRef.name+"') style='position:absolute;top:38;left:5' src='"+path_IMG+"sw.gif' width=24 height=24 border=0 alt=''>");
	document.write("<img onclick=moveBy('"+obRef.name+"') style='position:absolute;top:41;left:20' src='"+path_IMG+"s.gif' width=24 height=24 border=0 alt=''>");
	document.write("<img onclick=moveBy('"+obRef.name+"') style='position:absolute;top:38;left:35' src='"+path_IMG+"se.gif' width=24 height=24 border=0 alt=''>");
	}
*/