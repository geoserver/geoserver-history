<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
    <head>
        <title>OpenLayers map preview</title>
        <!-- Import OL CSS, auto import does not work with our minified OL.js build -->
        <link rel="stylesheet" type="text/css" href="${baseUrl}/openlayers/theme/default/style.css"/>
        <!-- Basic CSS definitions -->
        <style type="text/css">
            /* General settings */
            body {
                font-family: Verdana, Geneva, Arial, Helvetica, sans-serif;
                font-size: small;
            }
            /* Toolbar styles */
            #toolbar {
                position: relative;
                padding-bottom: 0.5em;
                display: none;
            }
            
            #toolbar ul {
                list-style: none;
                padding: 0;
                margin: 0;
            }
            
            #toolbar ul li {
                float: left;
                padding-right: 1em;
                padding-bottom: 0.5em;
            }
            
            #toolbar ul li a {
                font-weight: bold;
                font-size: smaller;
                vertical-align: middle;
                color: black;
                text-decoration: none;
            }

            #toolbar ul li a:hover {
                text-decoration: underline;
            }
            
            #toolbar ul li * {
                vertical-align: middle;
            }

            /* The map and the location bar */
            #map {
                clear: both;
                position: relative;
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
            
            #options {
                position: absolute;
                left: 13px;
                top: 7px;
                z-index: 3000;
            }

            /* Styles used by the default GetFeatureInfo output, added to make IE happy */
            table.featureInfo, table.featureInfo td, table.featureInfo th {
                border: 1px solid #ddd;
                border-collapse: collapse;
                margin: 0;
                padding: 0;
                font-size: 90%;
                padding: .2em .1em;
            }
            
            table.featureInfo th {
                padding: .2em .2em;
                text-transform: uppercase;
                font-weight: bold;
                background: #eee;
            }
            
            table.featureInfo td {
                background: #fff;
            }
            
            table.featureInfo tr.odd td {
                background: #eee;
            }
            
            table.featureInfo caption {
                text-align: left;
                font-size: 100%;
                font-weight: bold;
                text-transform: uppercase;
                padding: .2em .2em;
            }
        </style>
        <!-- Import OpenLayers, reduced, wms read only version -->
        <script src="${baseUrl}/openlayers/OpenLayers.js" type="text/javascript">
        </script>
        <script defer="defer" type="text/javascript">
            var map;
            var untiled;
            var tiled;
            var pureCoverage = ${pureCoverage?string};
            // pink tile avoidance
            OpenLayers.IMAGE_RELOAD_ATTEMPTS = 5;
            // make OL compute scale according to WMS spec
            OpenLayers.DOTS_PER_INCH = 25.4 / 0.28;
        
            function init(){
                // if this is just a coverage or a group of them, disable a few items,
                // and default to jpeg format
                format = 'image/png';
                if(pureCoverage) {
                    document.getElementById('filterType').disabled = true;
                    document.getElementById('filter').disabled = true;
                    document.getElementById('antialiasSelector').disabled = true;
                    document.getElementById('updateFilterButton').disabled = true;
                    document.getElementById('resetFilterButton').disabled = true;
                    document.getElementById('jpeg').selected = true;
                    format = "image/jpeg";
                }
            
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
                        format: format,
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
                        format: format
                    },
                    {singleTile: true, ratio: 1} 
                );
        
                map.addLayers([untiled, tiled]);

                // build up all controls            
                map.addControl(new OpenLayers.Control.PanZoomBar({
                    position: new OpenLayers.Pixel(2, 15)
                }));
                map.addControl(new OpenLayers.Control.Navigation());
                map.addControl(new OpenLayers.Control.Scale($('scale')));
                map.addControl(new OpenLayers.Control.MousePosition({element: $('location')}));
                map.zoomToExtent(bounds);
                
                // wire up the option button
                var options = document.getElementById("options");
                options.onclick = toggleControlPanel;
                
                // support GetFeatureInfo
                map.events.register('click', map, function (e) {
                    document.getElementById('nodelist').innerHTML = "Loading... please wait...";
                    var params = {
                        REQUEST: "GetFeatureInfo",
                        EXCEPTIONS: "application/vnd.ogc.se_xml",
                        BBOX: map.getExtent().toBBOX(),
                        X: e.xy.x,
                        Y: e.xy.y,
                        INFO_FORMAT: 'text/html',
                        QUERY_LAYERS: map.layers[0].params.LAYERS,
                        FEATURE_COUNT: 50,
                        <#assign skipped=["request","bbox","width","height","format"]>
                        <#list parameters as param>            
                        <#if !(skipped?seq_contains(param.name?lower_case))>
                        ${param.name?capitalize}: '${param.value?js_string}',
                        </#if>
                        </#list>
                        WIDTH: map.size.w,
                        HEIGHT: map.size.h,
                        format: format};
                    OpenLayers.loadURL("${baseUrl}/wms", params, this, setHTML, setHTML);
                    OpenLayers.Event.stop(e);
                });
            }
            
            // sets the HTML provided into the nodelist element
            function setHTML(response){
                document.getElementById('nodelist').innerHTML = response.responseText;
            };
            
            // shows/hide the control panel
            function toggleControlPanel(event){
                var toolbar = document.getElementById("toolbar");
                if (toolbar.style.display == "none") {
                    toolbar.style.display = "block";
                }
                else {
                    toolbar.style.display = "none";
                }
                event.stopPropagation();
                map.updateSize()
            }
            
            // Tiling mode, can be 'tiled' or 'untiled'            
            function setTileMode(tilingMode){
                if (tilingMode == 'tiled') {
                    untiled.setVisibility(false);
                    tiled.setVisibility(true);
                    map.setBaseLayer(tiled);
                }
                else {
                    untiled.setVisibility(true);
                    tiled.setVisibility(false);
                    map.setBaseLayer(untiled);
                }
            }
            
            // changes the current tile format
            function setImageFormat(mime){
                // we may be switching format on setup
                if(tiled == null)
                  return;
                  
                tiled.mergeNewParams({
                    format: mime
                });
                untiled.mergeNewParams({
                    format: mime
                });
                /*
                var paletteSelector = document.getElementById('paletteSelector')
                if (mime == 'image/jpeg') {
                    paletteSelector.selectedIndex = 0;
                    setPalette('');
                    paletteSelector.disabled = true;
                }
                else {
                    paletteSelector.disabled = false;
                }
                */
            }
            
            function setAntialiasMode(mode){
                tiled.mergeNewParams({
                    format_options: 'antialias:' + mode
                });
                untiled.mergeNewParams({
                    format_options: 'antialias:' + mode
                });
            }
            
            function setPalette(mode){
                if (mode == '') {
                    tiled.mergeNewParams({
                        palette: null
                    });
                    untiled.mergeNewParams({
                        palette: null
                    });
                }
                else {
                    tiled.mergeNewParams({
                        palette: mode
                    });
                    untiled.mergeNewParams({
                        palette: mode
                    });
                }
            }
            
            function setWidth(size){
                var mapDiv = document.getElementById('map');
                var wrapper = document.getElementById('wrapper');
                
                if (size == "auto") {
                    // reset back to the default value
                    mapDiv.style.width = null;
                    wrapper.style.width = null;
                }
                else {
                    mapDiv.style.width = size + "px";
                    wrapper.style.width = size + "px";
                }
                // notify OL that we changed the size of the map div
                map.updateSize();
            }
            
            function setHeight(size){
                var mapDiv = document.getElementById('map');
                
                if (size == "auto") {
                    // reset back to the default value
                    mapDiv.style.height = null;
                }
                else {
                    mapDiv.style.height = size + "px";
                }
                // notify OL that we changed the size of the map div
                map.updateSize();
            }
            
            function updateFilter(){
                if(pureCoverage)
                  return;
            
                var filterType = document.getElementById('filterType').value;
                var filter = document.getElementById('filter').value;
                
                // by default, reset all filters                
                var filterParams = {
                    filter: null,
                    cql_filter: null,
                    featureId: null
                };
                if (OpenLayers.String.trim(filter) != "") {
                    if (filterType == "cql") 
                        filterParams["cql_filter"] = filter;
                    if (filterType == "ogc") 
                        filterParams["filter"] = filter;
                    if (filterType == "fid") 
                        filterParams["featureId"] = filter;
                }
                // merge the new filter definitions
                mergeNewParams(filterParams);
            }
            
            function resetFilter() {
                if(pureCoverage)
                  return;
            
                document.getElementById('filter').value = "";
                updateFilter();
            }
            
            function mergeNewParams(params){
                tiled.mergeNewParams(params);
                untiled.mergeNewParams(params);
            }
        </script>
    </head>
    <body onload="init()">
        <div id="toolbar" style="display: none;">
            <ul>
                <li>
                    <a>Tiling:</a>
                    <select id="tilingModeSelector" onchange="setTileMode(value)">
                        <option value="untiled">Single tile</option>
                        <option value="tiled">Tiled</option>
                    </select>
                </li>
                <li>
                    <a>Antialias:</a>
                    <select id="antialiasSelector" onchange="setAntialiasMode(value)">
                        <option value="full">Full</option>
                        <option value="text">Text only</option>
                        <option value="none">Disabled</option>
                    </select>
                </li>
                <li>
                    <a>Format:</a>
                    <select id="imageFormatSelector" onchange="setImageFormat(value)">
                        <option value="image/png">PNG 24bit</option>
                        <option value="image/png8">PNG 8bit</option>
                        <option value="image/gif">GIF</option>
                        <option id="jpeg" value="image/jpeg">JPEG</option>
                    </select>
                </li>
                <!-- Commented out for the moment, some code needs to be extended in 
                     order to list the available palettes
                <li>
                    <a>Palette:</a>
                    <select id="paletteSelector" onchange="setPalette(value)">
                        <option value="">None</option>
                        <option value="safe">Web safe</option>
                    </select>
                </li>
                -->
                <li>
                    <a>Width/Height:</a>
                    <select id="widthSelector" onchange="setWidth(value)">
                        <!--
                        These values come from a statistics of the viewable area given a certain screen area
                        (but have been adapted a litte, simplified numbers, added some resolutions for wide screen)
                        You can find them here: http://www.evolt.org/article/Real_World_Browser_Size_Stats_Part_II/20/2297/
                        --><option value="auto">Auto</option>
                        <option value="600">600</option>
                        <option value="750">750</option>
                        <option value="950">950</option>
                        <option value="1000">1000</option>
                        <option value="1200">1200</option>
                        <option value="1400">1400</option>
                        <option value="1600">1600</option>
                        <option value="1900">1900</option>
                    </select>
                    <select id="heigthSelector" onchange="setHeight(value)">
                        <option value="auto">Auto</option>
                        <option value="300">300</option>
                        <option value="400">400</option>
                        <option value="500">500</option>
                        <option value="600">600</option>
                        <option value="700">700</option>
                        <option value="800">800</option>
                        <option value="900">900</option>
                        <option value="1000">1000</option>
                    </select>
                </li>
                <li>
                    <a>Filter:</a>
                    <select id="filterType">
                        <option value="cql">CQL</option>
                        <option value="ogc">OGC</option>
                        <option value="fid">FeatureID</option>
                    </select>
                    <input type="text" size="80" id="filter"/>
                    <img id="updateFilterButton" src="${baseUrl}/openlayers/img/east-mini.png" onClick="updateFilter()" title="Apply filter"/>
                    <img id="resetFilterButton" src="${baseUrl}/openlayers/img/cancel.png" onClick="resetFilter()" title="Reset filter"/>
                </li>
            </ul>
        </div>
        <div id="map">
            <img id="options" title="Toggle options toolbar" src="${baseUrl}/options.png"/>
        </div>
        <div id="wrapper">
            <div id="location">location</div>
            <div id="scale">
            </div>
        </div>
        <div id="nodelist">
            <em>Click on the map to get feature info</em>
        </div>
    </body>
</html>
