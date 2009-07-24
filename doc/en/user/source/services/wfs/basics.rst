.. _wfs_basics:

WFS basics
==========

.. warning:: Add intro here

Differences between WFS versions
-------------------------------- 

The major differences between the WFS versions are: 

* WFS 1.1.0 returns GML3 as the default GML. In WFS 1.0.0 the default was GML2. (GeoServer still supports requests in both GML3 and GML2 formats.) GML3 has slightly different ways of specifying a geometry. 
* In WFS 1.1.0, the way to specify the SRS (Spatial Reference System, aka projection) is ``urn:x-ogc:def:crs:EPSG:XXXX``, whereas in version 1.0.0 the specification was ``http://www.opengis.net/gml/srs/epsg.xml#XXXX``. This change has implications on the axis order of the returned data. 
* WFS 1.1.0 supports on-the-fly reprojection of data, which allows for data to be returned in a SRS other than the native. 

Axis ordering
------------- 

WFS 1.0.0 servers return geographic coordinates in longitude/latitude 
(x/y) order. This is the most common way to distribute data as well (for 
example, most shapefiles adopt this order by default). 

However, the traditional axis order for geographic and cartographic 
systems is latitude/longitude (y/x), the opposite and WFS 1.1.0 
specification respects this. This can cause difficulties when switching 
between servers with different WFS versions, or when upgading your WFS. 

To sum up, the defaults are as follows: 

* WFS 1.1.0 request = latitude/longitude
* WMS 1.0.0 request = longitude/latitude 

GeoServer, however, in an attempt to minimize confusion and increase 
interoperability, has adopted the following conventions when specifying 
projections in the follow formats: 

* ``EPSG:xxxx`` - longitude/latitude
* ``http://www.opengis.net/gml/srs/epsg.xml#xxxx`` - longitude/latitude
* ``urn:x-ogc:def:crs:EPSG:xxxx`` - latitude/longitude 
