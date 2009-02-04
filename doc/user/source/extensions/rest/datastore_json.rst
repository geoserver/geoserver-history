.. _datastore_json:

.. code-block:: javascript

   {
     dataStore: {
       name: "states_shapefile", 
       enabled: true, 
       connectionParameters: {
         url: {
           string: "file:data/shapefiles/states.shp"
         }, 
         namespace: {
           string: "http://www.openplans.org/topp"
         }
       }, 
       workspace: "topp"
     }
   }
   
