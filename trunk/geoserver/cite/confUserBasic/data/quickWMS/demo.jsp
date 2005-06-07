<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 3.2 Final//EN">
<html><head>
<!--
/*
* Project quickWMS: Generic JavaScript WMS Client 
* quickWMS - multiple layer request according to OpenGIS Web Mapping Specification with auxiliary window for map navigation
* Author : Pedro Pereira Gon�alves (pedro@inovagis.org)
* File : map.htm
* Version : 0.01
* Last Change : 2003-10-27
* Dependencies : WMSbrowsers.js, WMSlayer.js, WMSmap.js, WMStools.js, WMSquick.js, WMSnavigation.js, 
* Future Developments : 
* Tested on : Netscape 7.0, IE 6.0
* History :
    2003-10-27 : File Created
*/
-->
<link rel="stylesheet" href="help.css">

<link rel="stylesheet" href="inc/quickWMS.css">

<script language='Javascript'>
  var path = "/geoserver/data/quickWMS/";
  var path_skin = path + "blue_skin/";
</script>
<script language="Javascript" src="inc/WMSbrowsers.js"></script>
<script language="Javascript" src="inc/WMSlayer.js"></script>
<script language="Javascript" src="inc/WMSmap.js"></script>
<script language="Javascript" src="inc/WMStools.js"></script>
<script language="Javascript" src="inc/WMSquick.js"></script>
<script language="Javascript" src="inc/WMSnavigation.js"></script>
<script language="Javascript" src="inc/KDVZadds.js"></script>

<title>WMS Client</title>
</head>
<body>

<script language='Javascript'>
// WMS servers - list here other OGC compliant map servers
// Changes:	2003-10-24 : Added http:// to the DEMIS and ESRIN server urls
	var geoserverWMS = "<%="http://" + request.getServerName() + ":" + request.getServerPort() + "/geoserver/wms?"%>";
	var esaWMS = "http://mapserv2.esrin.esa.it/cubestor/cubeserv/cubeserv.cgi";

	// dynamische Kartengroesse
	// Beginn
    var offsetTop = 180;
    var offsetLeft = 180;
    var winWidth = getCurrentWinWidth()-offsetLeft;
    var winHeight = getCurrentWinHeight()-offsetTop;
	winWidth = Math.ceil(winWidth/2)*2;
	winHeight = Math.ceil(winHeight/2)*2;
	if (winWidth < 250) winWidth = 250;
	if (winHeight < 250) winHeight = 250;

	if (winWidth > winHeight) {winWidth = winHeight;}
	else if (winHeight > winWidth) {winHeight = winWidth;}
	var mapWidth = winWidth;
	var mapHeight = winHeight;
	// dynamische Kartengroesse
	// Ende

	// statische Kartengroesse
	var mapWidth = 650;
	var mapHeight = 420;
	// max. Ausdehnung
	var map = new kdvzMapWMS(-132, 20, -60, 55, 'EPSG:4326');

	function writeMaps(){

		// Schema addLayer(url, layer, version, caption)
		map.addLayer(geoserverWMS, "Img_Sample", "1.1.1", "Rasters1");
		map.layers[0].format="image/gif";
		map.layers[0].transparent = false;
		map.layers[0].styles="raster";
		map.layers[0].opacity = 1.0;
		map.layers[0].queryLayers = ""
		map.layers[0].visible=true;

		// Schema addLayer(url, layer, version, caption)
		map.addLayer(geoserverWMS, "topp:states", "1.1.1", "Vectors1");
		map.layers[1].format="image/gif";
		map.layers[1].transparent = true;
		map.layers[1].styles="population";
		map.layers[1].opacity = 1.0;
		map.layers[1].queryLayers = "topp:states"
		map.layers[1].visible=true;

		map.writeDOM("myMap", 100, 150, mapWidth, mapHeight, "background-color:white; border-width:2px; border-color:#000066; border-style:solid;");
		//hideLayer("myMapScale");
		//hideLayer("myMapBBox");

		// add toolbar
		map.toolbar = new toolBar(map);
		map.toolbar.writeDOM(map.left-30,map.top+0,false);

		// add a navigation window
		/*var nav = new navigationWMS(-132, 20, -60, 55, 'EPSG:4326');
		nav.addLayer(geoserverWMS, "Img_Sample", "1.1.1");
		nav.layers[0].format="image/png";
		nav.layers[0].styles="raster";
		nav.layers[0].visible=true;*/
		var nav = new navigationWMS(-180, -90, 180, 90);
		nav.addLayer(esaWMS, "WORLD_MODIS_1KM:MapAdmin", "1.0.0", "Modis Mosaic");		
		nav.onError = function (layer){alert("error")}
		nav.layers[0].format="JPEG";			


		nav.writeDOM("myNavMap", map.left+mapWidth+30, map.top+mapHeight-130, 230, 130, "background-color:white; border-width:2px; border-color:#000066; border-style:solid;");
		nav.addListener(map);
		nav.refresh();

		// add quickWMS-Logo
		//createLayer("quickwms", map.left+mapWidth-105, map.top+mapHeight+5,110,15, true, '<span class="text8">based on <a href="/qwms/license.txt" target="quickwms"><span style="font-weight:bold;">quickWMS</span></a></span>');

		// Startansicht
		map.updateBBox(-132, 20, -60, 55);
		// Home definiert, optional
		map.setHome(-132, 20, -60, 55);

		// add layerBar
		map.layerbar = new layerBar(map);
		map.layerbar.initLayer("Rasters1", "Rasters Plane 1", "Img_Sample", "visible");
		map.layerbar.initLayer("Vectors1", "Vectors Plane 1", "topp:states", "visible");
		map.layerbar.writeDOM(map.left+mapWidth+25,map.top+0,false);
		}
writeMaps();

</script>

</body></html>
