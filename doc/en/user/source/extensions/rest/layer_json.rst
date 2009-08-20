.. _layer_json:

.. code-block:: javascript

   {
      "layer":{
         "name":"states",
         "path":"\/",
         "type":"VECTOR",
         "defaultStyle":{
            "name":"population",
            "href":"http:\/\/localhost:8080\/geoserver\/rest\/styles\/population.json"
         },
         "styles":{
            "style":{
               "name":"population",
               "href":"http:\/\/localhost:8080\/geoserver\/rest\/styles\/population.json"
            }
         },
         "enabled":true,
         "resource":{
            "name":"states",
            "href":"http:\/\/localhost:8080\/geoserver\/rest\/workspaces\/topp\/datastores\/states_shapefile\/featuretypes\/states.json"
         }
      }
   }
