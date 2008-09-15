<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
    <#-- 
    All numerical values use the ?c "computer audience" built-in to avoid 
    freemarker inserting thousands separators
    -->
    <head>
        <title>OpenLayers map preview</title>
        <style type="text/css">
            #map {
                width: ${request.width?c}px;
                height: ${request.height?c}px;
                border: 1px solid black;
            }
            #wrapper {
                width: ${request.width?c}px;
            }
            #location {
                float: right;
            }
        </style>
        <script src="${baseUrl}/openlayers/OpenLayers.js" type="text/javascript">
        </script>
        <script defer="defer" type="text/javascript">
        var map;
        var untiled;
        var tiled;
        function setHTML(response) { 
            document.getElementById('nodelist').innerHTML = response.responseText;
        };
        
        OpenLayers.IMAGE_RELOAD_ATTEMPTS = 5;
        OpenLayers.DOTS_PER_INCH = 25.4 / 0.28;
        
        function init(){
            var bounds = new OpenLayers.Bounds(
                ${request.bbox.minX?c}, ${request.bbox.minY?c},
                ${request.bbox.maxX?c}, ${request.bbox.maxY?c}
            );
            var options = {
                controls: [],
                maxExtent: bounds,
                maxResolution: ${maxResolution?c},
                projection: "${request.SRS?js_string}",
                units: '${units?js_string}'
            };
            map = new OpenLayers.Map('map', options);
            
            
            // setup tiled layer
            tiled = new OpenLayers.Layer.WMS(
                "${layerName} - Tiled", "${baseUrl}/wms",
                {
                    <#list parameters as param>            
                    ${param.name}: '${param.value?js_string}',
                    </#list>
                    format: 'image/png',
                    tiled: 'true',
                    tilesOrigin : "${request.bbox.minX?c},${request.bbox.minY?c}"
                },
                {buffer: 0} 
            );
            
            // setup single tiled layer
            untiled = new OpenLayers.Layer.WMS(
                "${layerName} - Untiled", "${baseUrl}/wms",
                {
                    <#list parameters as param>            
                    ${param.name}: '${param.value?js_string}',
                    </#list>
                    format: 'image/png'
                },
                {singleTile: true, ratio: 1} 
            );
        
            map.addLayers([untiled, tiled]);
            
            // setup controls and initial zooms
            map.addControl(new OpenLayers.Control.PanZoomBar());
            map.addControl(new OpenLayers.Control.Navigation());
            map.addControl(new OpenLayers.Control.Scale($('scale')));
            map.addControl(new OpenLayers.Control.MousePosition({element: $('location')}));
            map.addControl(new OpenLayers.Control.LayerSwitcher());
            //map.addControl(new OpenLayers.Control.OverviewMap());
            map.zoomToExtent(bounds);
            
            // support GetFeatureInfo
            map.events.register('click', map, function (e) {
                document.getElementById('nodelist').innerHTML = "Loading... please wait...";
                var url =  map.layers[0].getFullRequestString(
                    {
                        request: "GetFeatureInfo",
                        EXCEPTIONS: "application/vnd.ogc.se_xml",
                        BBOX: map.getExtent().toBBOX(),
                        X: e.xy.x,
                        Y: e.xy.y,
                        INFO_FORMAT: 'text/html',
                        QUERY_LAYERS: map.layers[0].params.LAYERS,
                        FEATURE_COUNT: 50,
                        <#assign skipped=["request","bbox","service","version","format","width","height","layers","styles","srs"]>
                        <#list parameters as param>            
                        <#if !(skipped?seq_contains(param.name?lower_case))>
                        ${param.name}: '${param.value?js_string}',
                        </#if>
                        </#list>
                        WIDTH: map.size.w,
                        HEIGHT: map.size.h
                    },
                    "${baseUrl}/wms"
                );
                OpenLayers.loadURL(url, '', this, setHTML, setHTML);
                OpenLayers.Event.stop(e);
            });
        }
        </script>
    </head>
    <body onload="init()">
        <div id="map"></div>
        <div id="wrapper">
            <div id="location"></div>
            <div id="scale"></div>
        </div>
        <div id="nodelist">Click on the map to get feature info</div>
    </body>
</html>
