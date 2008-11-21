<html>
<#assign wmsUrl="../../../wms?request=GetMap&version=1.1.1" 
         wfsUrl="../../../wfs?SERVICE=WFS&VERSION=1.1.0&REQUEST=GetFeature"
         layersParam = "&layers="+name
         nameParam = "&typeName="+name
         kmlUrl="../../../wms/kml?superoverlay=true"
         gml2="GML2"
         gml2gzip="GML2-GZIP"
         gml3="gml3"
         json="json"
         PDFParam="&Format=application/pdf"
         bboxParam="&bbox="+bbox
         dimParams="&width="+width+"&height="+height
         srsParam="&srs="+srs
         imgUrl="../../../images/"
>
  <head>
    <title>${title} - Powered by GeoServer </title>
    <link rel="stylesheet" type="text/css" href="../../../openlayers/theme/default/style.css"/>
    <!-- Basic CSS definitions -->
    <style type="text/css">
        /* General settings */
        body {
            font-family: Verdana, Geneva, Arial, Helvetica, sans-serif;
            font-size: small;
        }
        
        /* The map and the location bar */
        #map {
            clear: both;
            position: relative;
            width: ${width}px;
            height: ${height}px;
            border: 1px solid black;
        }            
    </style>

  <style type="text/css">
/*-----------------------
General styles
-----------------------*/
body {
background: #fff;
color: #222;
font-family: Tahoma, "Lucida Sans Unicode", "Lucida Grande", Verdana, sans-serif;

}

h1, 
h2, 
h3, 
h4, 
h5, 
h6 {
color: #0082b6;
font-family: Tahoma, "Lucida Sans", "Lucida Sans Unicode", "Lucida Grande", Verdana, sans-serif;
margin: 0.5em 0;
}

h1 {
font-size: 1.7em;
margin: 0 0 0.5em;
}

h2 {
font-size: 1.3em;
border-bottom: 2px solid #000;
}

strong, 
em,
dt, 
b, 
i {
font-family: Tahoma, "Lucida Sans", "Lucida Sans Unicode", "Lucida Grande", Verdana, sans-serif;
}

a {
color: #0076a1;
text-decoration: none;
outline: none !important; /* avoid ugly dotted border for Firefox */
}

a:hover {
color: #cc6d00;
text-decoration: underline;
}

a img {
border: none; 
}

div {
outline: none !important; /* avoid ugly dotted border for Firefox, when listeners are added via js */
}


/*-----------------------
Utility Classes
-----------------------*/
.selfclear:after {
content: ".";
display: block;
height: 0;
clear: both;
visibility: hidden;
}

.selfclear {
display: inline-block; /* IE 7 */
}

.selfclear {
display: block;
}

* html .selfclear {
height: 1px; /* IE < 7 */
}

.leftwise {
float:left;
}

.rightwise {
float:right;
}

.nobreak {
white-space: nowrap;
}

.noshow {
display: none;
}

/*-*/
#geoserver-logo {
float: right;
width: 100px;
height: 30px;
overflow: hidden;
text-indent: -9999em;
background: url(${imgUrl}powered-by-geoserver-100x30.png) no-repeat; 
}

/*-*/
#info {
color: #333;
font-size: 0.95em;
}

/*-*/
#view-data,
#download-data {
margin: 0 0 0.75em;
}

#view-data ul,
#view-data p,
#download-data ul,
#download-data p {
margin: 0;
padding: 0; 
float: left;
}

#view-data ul li,
#download-data ul li {
list-style-type: none; 
float: left;
margin: 0 0.55em 0 0.3em;
padding: 0;
}

#view-data ul li a,
#download-data ul li a {
padding: 0 0 0 20px;
background: url(${imgUrl}page_white_text.png) 0 50% no-repeat;
}

#view-data ul li a.pdf,
#download-data ul li a.pdf {
background: url(${imgUrl}page_white_acrobat.png) 0 50% no-repeat; 
}

#view-data ul li a.kml,
#download-data ul li a.kml {
background: url(${imgUrl}kml-16x16.png) 0 50% no-repeat; 
}

#view-data ul li a.shapefile,
#download-data ul li a.shapefile {
background: url(${imgUrl}page_white_zip.png) 0 50% no-repeat; 
}

#view-data ul li a.json,
#download-data ul li a.json {
background: url(${imgUrl}page_white_text.png) 0 50% no-repeat; 
}

#view-data ul li a.gml2,
#download-data ul li a.gml2 {
background: url(${imgUrl}page_white_vector.png) 0 50% no-repeat; 
}

#view-data ul li a.gml3,
#download-data ul li a.gml3 {
background: url(${imgUrl}page_white_vector.png) 0 50% no-repeat; 
}

#view-data ul li a.georss,
#download-data ul li a.georss {
background: url(${imgUrl}feed.png) 0 50% no-repeat; 
}

/*-*/
#map {
clear: both;
position: relative;
width: 800px;
height: 317px;
border: 1px solid black;
}

/*-*/

#metadata table {
border-collapse: collapse;
border-spacing: 0;
border: 1px solid #000; 
}

#metadata th {
white-space: nowrap;
}

#metadata th,
#metadata td {
padding: 0.2em;
border: 1px solid #666;
}

  </style>

    <script src="../../../openlayers/OpenLayers.js" type="text/javascript">
    </script>
    <script defer="defer" type="text/javascript">

    var format = 'image/png';
    var untiled;
    var map;

    function init(){
        var bounds = new OpenLayers.Bounds(${bbox});

        var options = {
            controls: [],
            maxExtent: bounds,
            maxResolution: ${maxResolution},
            projection: '${srs}',
            units: 'degrees'
        };

        map = new OpenLayers.Map('map', options);
        
        // setup tiled layer
        tiled = new OpenLayers.Layer.WMS(
            "${name} - Tiled", "../../../wms",
            {
                height: '${height}',
                width: '${width}',
                layers: '${name}',
                styles: '',
                srs: '${srs}',
                format: format,
                tiled: 'true',
                tilesOrigin : "${tilesOrigin}"
            },
            {buffer: 0} 
        );

        // setup single tiled layer
        untiled = new OpenLayers.Layer.WMS(
            "${name} - Untiled", "../../../wms",
            {
                height: '${height}',
                width: '${width}',
                layers: '${name}',
                styles: '',
                srs: '${srs}',
                format: format
            },
            {singleTile: true, ratio: 1} 
        );

        map.addLayers([untiled, tiled]);

        // build up all controls            
        map.addControl(new OpenLayers.Control.PanZoom());
        map.addControl(new OpenLayers.Control.Navigation());
        map.addControl(new OpenLayers.Control.Scale($('scale')));
        map.addControl(new OpenLayers.Control.MousePosition({element: $('location')}));
        map.zoomToExtent(bounds);

    }
    </script>
  </head>
  <body onload="init()">
    <a id="geoserver-logo" title="Powered by GeoServer" href="http://geoserver.org">Powered by GeoServer</a>
    <h1>${title}</h1>
    <p id="info">${abstract}</p>

    <h2>Formats</h2>
    <div id="view-data" class="selfclear">
      <p>View the data as:</p>
      <ul>
        <li>
          <a class="pdf" href="${wmsUrl + PDFParam + layersParam + bboxParam + '&styles=' + srsParam + dimParams }">PDF</a>
        </li>
        <li>
          <a class="google-earth" href="${kmlUrl + layersParam}">Google Earth</a>
        </li>
      </ul><!-- /#view-data -->
    </div>
    <div id="download-data" class="selfclear">
      <p>Download the data as:</p>
      <ul>
        <li><a class="kml" href="${kmlUrl + layersParam + '&mode=flat'}">KML</a></li>
        <li><a class="shapefile" href="${wfsUrl + nameParam + '&outputFormat=' + 'SHAPE-ZIP'}">Shapefile</a></li>   
        <li><a class="json" href="${wfsUrl + nameParam + '&outputFormat=' + json}">JSON</a></li>
        <li><a class="gml2" href="${wfsUrl + nameParam + '&outputFormat=' + gml2}">GML2</a></li>
        <li><a class="gml3" href="${wfsUrl + nameParam + '&outputFormat=' + gml3}">GML3</a></li>
        <!-- 
           Problem: browser tries to up zip file as XML.
        <li><a href="${wfsUrl + nameParam + '&outputFormat=' + gml2gzip}">GML2</a> gZipped</li>
        -->
      </ul>
    </div><!-- /#download-data -->

    <h2>Map</h2>
    <div id="map"></div>

    <div id="Metadata">
      <h2>Metadata</h2>
      <table border="1">
        <tr>
          <th scope="row">Keywords</td>
          <td><#list keywords as keyword>${keyword}, </#list></td>
        </tr>
        <tr>
          <th scope="row">Native CRS</th>
          <td>${nativeCRS}</td>
        </tr>
        <tr>
          <th scope="row">Declared CRS</th>
          <td>${declaredCRS}</td>
        </tr>
        <tr>
          <th scope="row">Extent</th>
          <td>${bbox}</td>
        </tr>
        <tr>
          <th scope="row">SRS</th>
          <td>${srs}</td>
        </tr>
        <!--
        <#if metadataLinks?size != 0>
        <tr>
          <th scope="row">Metadata Links</th>
          <td><#list metadataLinks as link >${link}, </#list></td>
        </tr>
        </#if>
        -->
    </table>
    </div><!-- /#metadata -->    
    
    <#if gwc == "true">
    <h2>Access tiles</h2>
    Access the tiles through these API's:
    <dl>
<!--  <li>OpenLayers/WMS-Caching</li> -->
      <dt>Google Maps API</dt>
      <dd>
       <a href="http://geowebcache.org/trac/wiki/google_maps">Use GeoWebCache tiles in GoogleMaps</a> with the following code snippet:
        <code>tileUrlTemplate: '${gwcLink}service/gmaps?layers=${name}&zoom={Z}&x={X}&y={Y}',</code>
      </dd>
      <dt>Virtual Earth API</dt>
      <dd>
        <a href="http://geowebcache.org/trac/wiki/virtual_earth">Use GeoWebCache tiles in Virtual Earth</a> with the following code snippet:
        <code>var tileLayerURL = '${gwcLink}service/ve?quadkey=%4&format=image/png&layers=${name}';</code>
      </dd>
    </dl>
    </#if>
    
  </body>
</html>
