<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "DTD/xhtml1-strict.dtd">

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
     </style>
     
     <script src="${baseUrl}/openlayers/OpenLayers.js" type="text/javascript">
     </script>
     <script defer="defer" type="text/javascript">
       var map;
       var untiled;
       var tiled;
       function setHTML(response) { 
        OpenLayers.Util.getElement('nodelist').innerHTML = response.responseText;
       };
       
       function init(){
          map = new OpenLayers.Map('map', {controls:[], 'projection': '${request.SRS}', 'units':'${units}'}); 
          
          OpenLayers.IMAGE_RELOAD_ATTEMPTS = 5;
          
          // setup tiled layer
          var bounds = new OpenLayers.Bounds(${request.bbox.minX?c},${request.bbox.minY?c},${request.bbox.maxX?c},${request.bbox.maxY?c})
          tiled = new OpenLayers.Layer.WMS(
            "${layerName}", "${baseUrl}/wms",
            {
<#list parameters as param>            
              ${param.name}: '${param.value}',
</#list>
              format: 'image/png', tiled: 'true', tilesOrigin : "${request.bbox.minX?c},${request.bbox.minY?c}"
            },
            {maxExtent: bounds, maxResolution: ${maxResolution?c}, projection: "${request.SRS}", buffer: 0} 
          );
          //map.addLayer(tiled);
          
          // setup untiled layer
          untiled = new OpenLayers.Layer.WMS.Untiled(
            "${layerName}", "${baseUrl}/wms",
            {
<#list parameters as param>            
              ${param.name}: '${param.value}',
</#list>
              format: 'image/png'
            },
            {maxExtent: bounds, maxResolution: ${maxResolution?c}, projection: "${request.SRS}"} 
          );
          untiled.ratio=1;
          untiled.setVisibility(false, false);
          map.addLayer(untiled);

          // setup controls and initial zooms
	  map.addControl(new OpenLayers.Control.PanZoomBar({div:$('nav')}));
          map.addControl(new OpenLayers.Control.LayerSwitcher());
          map.addControl(new OpenLayers.Control.MouseDefaults());
          map.addControl(new OpenLayers.Control.Scale($('scale')));
          map.addControl(new OpenLayers.Control.MousePosition({element: $('position')}));
          //map.addControl(new OpenLayers.Control.OverviewMap());
          map.zoomToExtent(bounds);
          
          // support GetFeatureInfo
          map.events.register('click', map, function (e) {
            OpenLayers.Util.getElement('nodelist').innerHTML = "Loading... please wait...";
            var url =  map.layers[0].getFullRequestString({
                            REQUEST: "GetFeatureInfo",
                            EXCEPTIONS: "application/vnd.ogc.se_xml",
                            BBOX: map.getExtent().toBBOX(),
                            X: e.xy.x,
                            Y: e.xy.y,
                            INFO_FORMAT: 'text/html',
                            QUERY_LAYERS: map.layers[0].params.LAYERS,
                            FEATURE_COUNT: 50,
<#assign skipped=["request","bbox","service","version","format","width","height"]>
<#list parameters as param>            
  <#if !(skipped?seq_contains(param.name?lower_case))>
                            ${param.name}: '${param.value}',
  </#if>
</#list>
                            WIDTH: map.size.w,
                            HEIGHT: map.size.h},
                            "${baseUrl}/wms"
                            );
            OpenLayers.loadURL(url, '', this, setHTML, setHTML);
            Event.stop(e);
      });
      }
      </script>
  </head>
  
  <body onload="init()">
     <table>
       <tr>
         <td style="width:40px" valign="middle" rowspan="3"><div id="nav"></div></td>
         <td colspan="3" align="right">
           <!-- Switch layers when links are pressed -->
           <a id="untiledLink" href="#" onclick="map.removeLayer(tiled);map.addLayer(untiled)">Untiled</a>
           <a id="tiledLink" href="#" onclick="map.removeLayer(untiled);map.addLayer(tiled);">Tiled</a>
           <a id="tiledLink" href="${baseUrl}/wms/kml_reflect?<#list parameters as param>${param.name}=${param.value}&</#list>">KML</a>
         </td>
       </tr>
       <tr>
         <td colspan="3"><div id="map"></div></td>
       </tr>
       <tr>
         <td><div id="scale"></div></td>
         <td/>
         <td align="right"><div id="position"></div></td>
       </tr>
     </table>
     <div id="nodelist">Click on the map to get feature infos</div>
  </body>
</html>
