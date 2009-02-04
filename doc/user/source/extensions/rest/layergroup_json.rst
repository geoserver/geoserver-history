.. _layergroup_json:

.. code-block:: javascript

   {
     layerGroup: {
       name: "tasmania", 
       bounds: {
         minx: 143.83482400000003, 
         maxx: 148.47914100000003, 
         miny: -43.648056, 
         maxy: -39.573891, 
         crs: "EPSG:4326"
       }, 
       layers: {
         layer: [
           "tasmania_state_boundaries", 
           "tasmania_water_bodies", 
           "tasmania_roads", 
           "tasmania_cities"
         ]
       }, 
       styles: {
         style: [
           "green", 
           "cite_lakes", 
           "simple_roads", 
           "capitals"
         ]
       }, 
       metadata: {
         rawStyleList: {
           string: ""
         }
       }
     }
   }
   
