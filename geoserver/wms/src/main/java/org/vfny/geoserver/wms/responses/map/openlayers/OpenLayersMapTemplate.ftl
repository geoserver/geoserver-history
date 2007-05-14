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
     
     <script src="${openLayersLocation}"></script>
     <script defer="defer" type="text/javascript">
       function init(){
          var map = new OpenLayers.Map($('map'), {controls:[]} );
          OpenLayers.IMAGE_RELOAD_ATTEMPTS = 3;
          
          var bounds = new OpenLayers.Bounds(${request.bbox.minX?c},${request.bbox.minY?c},${request.bbox.maxX?c},${request.bbox.maxY?c})
<#list request.layers as layer>
  <#assign layerName = "layer" + layer_index>
          var ${layerName} = new OpenLayers.Layer.WMS.Untiled(
          //var ${layerName} = new OpenLayers.Layer.WMS(
            "${layer.name}", "${request.baseUrl}/wms",
            {layers: '${layer.name}', styles: '${request.styles[layer_index].name}', format: 'image/png'},
            {maxExtent: bounds, maxResolution: ${maxResolution?c}, projection: "${request.SRS}" } 
          );
          map.addLayer( ${layerName} );
</#list>

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