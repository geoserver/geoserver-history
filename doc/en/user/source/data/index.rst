.. _data:

Working with Data
=================

This section discusses the data sources that can GeoServer can read and access.

GeoServer allows the loading and serving of the following data formats by default:

* Vector data formats
   * Shapefiles (including directories of shapefiles)
   * PostGIS databases (with or without JNDI)
   * External WFS layers
   * Java Properties files
* Raster data formats
   * ArcGrid
   * GeoTIFF
   * Gtopo30
   * ImageMosaic
   * WorldImage


Other data sources require the use of GeoServer extensions, extra downloads that add functionality to GeoServer.  These extensions are always available on the `GeoServer download page <http://geoserver.org/display/GEOS/Download>`_.

.. warning:: Make sure to match the version of the extension to the version of the GeoServer instance!


.. toctree::
   :maxdepth: 2

   shapefile/
   postgis/
   directory/
   wfs/
   properties/
   arcgrid/
   geotiff/
   gtopo30/
   imagemosaic/
   worldimage/
   arcsde/
   gml/
   db2/
   h2/
   mysql/
   featurepregen/
   oracle/
   sqlserver/
   vpf/
   gdal/
   imagepyramid/
   imagemosaicjdbc/
   oraclegeoraster/
   customjdbcaccess/
   connection-pooling/
   app-schema/index

   
