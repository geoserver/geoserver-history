.. _wms_basics:

WMS basics
==========

GeoServer provides support for Open Geospatial Consortium (OGC) Web Map Service (WMS) version 1.1.1.  This is a standard for generating maps on the web - it is how all the visual mapping that GeoServer does is produced.  Using a compliant WMS makes it possible for clients to overlay maps from several different sources in a seamless way.

GeoServer's implementation fully supports most every part of the standard, and is certified compliant against the OGC's test suite.  It includes a wide variety of rendering and labeling options, and is one of the fastest WMS Servers for both raster and vector data.  

The WMS implementation of GeoServer also supports reprojection in to any reference system in the EPSG database, and it is also possible to add additional projections if the Well Known Text is known.  It also fully supports the Styled Layer Descriptor (SLD) standard, and indeed uses SLD files as its native rendering rules.  For more information on how to style GeoServer data in the WMS see the section :ref:`styling`

