<html xmlns="http://www.w3.org/1999/xhtml">
  <#-- 
     All numerical values use the ?c "computer audience" built-in to avoid 
     freemarker inserting thousands separators
   -->
  <head>
     <style type="text/css">
      #map {
        width: ${request.width?c}px;
        height: ${request.height?c}px;
        border: 1px solid black;
      }
     </style>
     
     <script src="${baseUrl}/openlayers/OpenLayers.js"></script>
     <script defer="defer" type="text/javascript">
       function init(){
          var map = new OpenLayers.Map($('map'), {controls:[]} );
          OpenLayers.IMAGE_RELOAD_ATTEMPTS = 5;
          
          var bounds = new OpenLayers.Bounds(${request.bbox.minX?c},${request.bbox.minY?c},${request.bbox.maxX?c},${request.bbox.maxY?c})
          //var wmsLayer = new OpenLayers.Layer.WMS.Untiled(
          var wmsLayer = new OpenLayers.Layer.WMS(
            "${layerName}", "${baseUrl}/wms",
            {
<#list parameters as param>            
              ${param.name}: '${param.value}',
</#list>
              format: 'image/png', tiled: 'true', tilesOrigin : "${request.bbox.minX?c},${request.bbox.minY?c}"
            },
            {maxExtent: bounds, maxResolution: ${maxResolution?c}, projection: "${request.SRS}"} 
          );
          map.addLayer(wmsLayer);

		  map.addControl(new OpenLayers.Control.PanZoomBar({div:$('nav')}));
          map.addControl(new OpenLayers.Control.LayerSwitcher());
          map.addControl(new OpenLayers.Control.MouseDefaults());
          map.addControl(new OpenLayers.Control.Scale($('scale')));
          map.addControl(new OpenLayers.Control.MousePosition({element: $('position')}));
          map.addControl(new OpenLayers.Control.OverviewMap());
          map.zoomToExtent(bounds);
       }
     </script>
  </head>
  <body onload="init()">
     <table>
       <tr>
         <td width="40" valign="center"><div id="nav"></div></td>
         <td colspan="3"><div id="map"></div></td>
       </tr>
       <tr>
         <td/>
         <td><div id="scale"></div></td>
         <td/>
         <td align="right"><div id="position"></div></td>
       </tr>
     </table>
  </body>
</html>