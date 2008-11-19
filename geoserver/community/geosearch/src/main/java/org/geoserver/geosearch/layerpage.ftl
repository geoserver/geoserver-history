<html>
<#assign wmsUrl="../../../wms?request=GetMap&version=1.1.1" 
         wfsUrl="../../../wfs?SERVICE=WFS&VERSION=1.1.0&REQUEST=GetFeature"
         layersParam = "&layers="+name
         nameParam = "&typeName="+name
         kmlUrl="../../../wms/kml?mode=flat&superoverlay=true"
         gml2="GML2"
         gml2gzip="GML2-GZIP"
         gml3="gml3"
         json="json"
         PDFParam="&Format=application/pdf"
         bboxParam="&bbox="+bbox
         dimParams="&width="+width+"&height="+height
         srsParam="&srs="+srs
>
  <head>
    <title>${title} - Powered by GeoServer </title>
    <link rel="stylesheet" type="text/css" href="http://localhost:8080/geoserver/openlayers/theme/default/style.css"/>
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
    <h1>${title}</h1>
    <p>${abstract}</p>
    
    <div id="map"></div>

    <h2>Metadata</h2>
    <table border="1">
      <tr>
        <td>Keywords</td>
        <td><#list keywords as keyword>${keyword}, </#list></td>
      </tr>
      <tr><td>Native CRS</td><td>${nativeCRS}</td><td>
      <tr><td>Declared CRS</td><td>${declaredCRS}</td><td>
    </table>

    <h2>View Data</h2>
    View the data as
    <ul>
      <li>
        <a href="${wmsUrl + PDFParam + layersParam + bboxParam + '&styles=' + srsParam + dimParams }">PDF</a>
      </li>
    </ul>

    <h2>Download Data</h2>
    Download the data as:
    <ul>
      <li><a href="${kmlUrl + layersParam}">KML</a></li>
      <li><a href="${wfsUrl + nameParam + '&outputFormat=' + 'SHAPE-ZIP'}">Shapefile</a></li>   
      <li><a href="${wfsUrl + nameParam + '&outputFormat=' + json}">JSON</a></li>
      <li><a href="${wfsUrl + nameParam + '&outputFormat=' + gml2}">GML2</a></li>
      <li><a href="${wfsUrl + nameParam + '&outputFormat=' + gml3}">GML3</a></li>
      <!-- 
         Problem: browser tries to up zip file as XML.
         <li><a href="${wfsUrl + nameParam + '&outputFormat=' + gml2gzip}">GML2</a> gZipped</li>
      -->
    </ul>
  </body>
</html>
