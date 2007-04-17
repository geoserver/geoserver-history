<html>

  <head>
     <style type="text/css">
      #map {
        width: ${request.width}px;
        height: ${request.height}px;
        border: 1px solid black;
      }
     </style>
     
     <script src="${request.baseUrl}/openlayers/lib/OpenLayers.js"></script>
     <script type="text/javascript">
     
       function init(){
          var map = new OpenLayers.Map('map',{'controls': [], 'maxZoomLevel': 17} );
          
<#list request.layers as layer>
  <#assign layerName = "layer" + layer_index>
          //var ${layerName} = new OpenLayers.Layer.WMS.Untiled(
          var ${layerName} = new OpenLayers.Layer.WMS(
            "${layer.name}", "${request.baseUrl}/wms",
            {layers: '${layer.name}', styles: '${request.styles[layer_index].name}', format: 'image/png' }
          );
          map.addLayer( ${layerName} );
</#list>

          map.addControl(new OpenLayers.Control.PanZoomBar());
          map.addControl(new OpenLayers.Control.LayerSwitcher());
          map.addControl(new OpenLayers.Control.MouseDefaults());
          map.addControl(new OpenLayers.Control.MousePosition());
         
          var bounds = new OpenLayers.Bounds(${request.bbox.minX},${request.bbox.minY},${request.bbox.maxX},${request.bbox.maxY});
          map.zoomToExtent( bounds );
       }

    </script>

  </head>
  
  <body onload="init()">
     <div id="map"></div>

  </body>
  
</html>