.. _featuretype_json:

.. code-block:: javascript 

   {
     featureType: {
       name: "states", 
       nativeName: "states", 
       alias: "", 
       title: "USA Population", 
       abstract: "This is some census data on the states.", 
       keywords: {
         string: [
           "census", 
           "united", 
           "boundaries", 
           "state", 
           "states"
         ]
       }, 
       metadataLinks: "", 
       srs: "EPSG:4326", 
       nativeBoundingBox: {
         minx: -124.73142200000001, 
         maxx: -66.969849, 
         miny: 24.955967, 
         maxy: 49.371735, 
         crs: "EPSG:4326"
       }, 
       latLonBoundingBox: {
         minx: -124.731422, 
         maxx: -66.969849, 
         miny: 24.955967, 
         maxy: 49.371735, 
         crs: "EPSG:4326"
       }, 
       projectionPolicy: "FORCE_DECLARED", 
       enabled: true, 
       attributes: {
         attribute: [
           {
             name: "the_geom", 
             minOccurs: 0, 
             maxOccurs: 1, 
             nillable: false, 
             metadata: ""
           }, 
           {
             name: "STATE_NAME", 
             minOccurs: 0, 
             maxOccurs: 1, 
             nillable: false, 
             metadata: ""
           }, 
           {
             name: "STATE_FIPS", 
             minOccurs: 0, 
             maxOccurs: 1, 
             nillable: false, 
             metadata: ""
           }, 
           {
             name: "SUB_REGION", 
             minOccurs: 0, 
             maxOccurs: 1, 
             nillable: false, 
             metadata: ""
           }, 
           {
             name: "STATE_ABBR", 
             minOccurs: 0, 
             maxOccurs: 1, 
             nillable: false, 
             metadata: ""
           }, 
           {
             name: "LAND_KM", 
             minOccurs: 0, 
             maxOccurs: 1, 
             nillable: false, 
             metadata: ""
           }, 
           {
             name: "WATER_KM", 
             minOccurs: 0, 
             maxOccurs: 1, 
             nillable: false, 
             metadata: ""
           }, 
           {
             name: "PERSONS", 
             minOccurs: 0, 
             maxOccurs: 1, 
             nillable: false, 
             metadata: ""
           }, 
           {
             name: "FAMILIES", 
             minOccurs: 0, 
             maxOccurs: 1, 
             nillable: false, 
             metadata: ""
           }, 
           {
             name: "HOUSHOLD", 
             minOccurs: 0, 
             maxOccurs: 1, 
             nillable: false, 
             metadata: ""
           }, 
           {
             name: "MALE", 
             minOccurs: 0, 
             maxOccurs: 1, 
             nillable: false, 
             metadata: ""
           }, 
           {
             name: "FEMALE", 
             minOccurs: 0, 
             maxOccurs: 1, 
             nillable: false, 
             metadata: ""
           }, 
           {
             name: "WORKERS", 
             minOccurs: 0, 
             maxOccurs: 1, 
             nillable: false, 
             metadata: ""
           }, 
           {
             name: "DRVALONE", 
             minOccurs: 0, 
             maxOccurs: 1, 
             nillable: false, 
             metadata: ""
           }, 
           {
             name: "CARPOOL", 
             minOccurs: 0, 
             maxOccurs: 1, 
             nillable: false, 
             metadata: ""
           }, 
           {
             name: "PUBTRANS", 
             minOccurs: 0, 
             maxOccurs: 1, 
             nillable: false, 
             metadata: ""
           }, 
           {
             name: "EMPLOYED", 
             minOccurs: 0, 
             maxOccurs: 1, 
             nillable: false, 
             metadata: ""
           }, 
           {
             name: "UNEMPLOY", 
             minOccurs: 0, 
             maxOccurs: 1, 
             nillable: false, 
             metadata: ""
           }, 
           {
             name: "SERVICE", 
             minOccurs: 0, 
             maxOccurs: 1, 
             nillable: false, 
             metadata: ""
           }, 
           {
             name: "MANUAL", 
             minOccurs: 0, 
             maxOccurs: 1, 
             nillable: false, 
             metadata: ""
           }, 
           {
             name: "P_MALE", 
             minOccurs: 0, 
             maxOccurs: 1, 
             nillable: false, 
             metadata: ""
           }, 
           {
             name: "P_FEMALE", 
             minOccurs: 0, 
             maxOccurs: 1, 
             nillable: false, 
             metadata: ""
           }, 
           {
             name: "SAMP_POP", 
             minOccurs: 0, 
             maxOccurs: 1, 
             nillable: false, 
             metadata: ""
           }
         ]
       }, 
       maxFeatures: 0, 
       numDecimals: 0, 
       nativeCRS: "GEOGCS["GCS_WGS_1984", 
     DATUM["WGS_1984", 
       SPHEROID["WGS_1984", 6378137.0, 298.257223563]], 
     PRIMEM["Greenwich", 0.0], 
     UNIT["degree", 0.017453292519943295], 
     AXIS["Longitude", EAST], 
     AXIS["Latitude", NORTH]]", 
       store: "states_shapefile", 
       namespace: "topp", 
       metadata: {
         kml.regionateAttribute: {
           string: ""
         }, 
         cacheAgeMax: {
           string: 3600
         }, 
         indexingEnabled: {
           boolean: false
         }, 
         cachingEnabled: {
           boolean: true
         }, 
         kml.regionateStrategy: {
           string: ""
         }, 
         dirName: {
           string: "states"
         }, 
         kml.regionateFeatureLimit: {
           int: 10
         }
       }
     }
   }
