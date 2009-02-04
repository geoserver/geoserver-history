.. _coverage_json:   

.. code-block:: javascript

   {
     coverage: {
       name: "Arc_Sample", 
       nativeName: "Arc_Sample", 
       alias: "", 
       title: "Global annual rainfall", 
       description: "Global annual rainfall in ArcGrid format", 
       keywords: {
         string: [
           "WCS", 
           "arcGridSample", 
           "arcGridSample_Coverage"
         ]
       }, 
       metadataLinks: "", 
       srs: "EPSG:4326", 
       nativeBoundingBox: {
         minx: -180, 
         maxx: 180, 
         miny: -90, 
         maxy: 90, 
         crs: "EPSG:4326"
       }, 
       latLonBoundingBox: {
         minx: -180, 
         maxx: 180, 
         miny: -90, 
         maxy: 90, 
         crs: "EPSG:4326"
       }, 
       enabled: true, 
       nativeFormat: "ArcGrid", 
       grid: {
         @dimension: "2", 
         range: {
           low: "0 0", 
           high: "720 360"
         }, 
         transform: {
           scaleX: 0.5, 
           scaleY: -0.5, 
           shearX: [
             0, 
             0, 
             -179.75, 
             89.75
           ]
         }, 
         crs: "EPSG:4326"
       }, 
       supportedFormats: {
         string: [
           "ARCGRID", 
           "ARCGRID-GZIP", 
           "GEOTIFF", 
           "PNG", 
           "GIF", 
           "TIFF"
         ]
       }, 
       interpolationMethods: {
         string: "nearest neighbor"
       }, 
       defaultInterpolationMethod: "nearest neighbor", 
       dimensions: {
         coverageDimension: {
           name: "precip30min", 
           description: "GridSampleDimension[-9999.0,8849.000000000002]", 
           range: {
             elementClass: "java.lang.Double", 
             minValue: {
               @class: "double", 
               $: "-9999.0"
             }, 
             maxValue: {
               @class: "double", 
               $: "8849.0"
             }, 
             isMinIncluded: true, 
             isMaxIncluded: true
           }, 
           nullValues: ""
         }
       }, 
       requestSRS: {
         string: "EPSG:4326"
       }, 
       responseSRS: {
         string: "EPSG:4326"
       }, 
       parameters: "", 
       nativeCRS: "GEOGCS["WGS 84", 
     DATUM["World Geodetic System 1984", 
       SPHEROID["WGS 84", 6378137.0, 298.257223563, AUTHORITY["EPSG","7030"]], 
       AUTHORITY["EPSG","6326"]], 
     PRIMEM["Greenwich", 0.0, AUTHORITY["EPSG","8901"]], 
     UNIT["degree", 0.017453292519943295], 
     AXIS["Geodetic longitude", EAST], 
     AXIS["Geodetic latitude", NORTH], 
     AUTHORITY["EPSG","4326"]]", 
       store: "arcGridSample", 
       namespace: "nurc", 
       metadata: {
         dirName: {
           string: "arcGridSample_Arc_Sample"
         }
       }
     }
   }
   
