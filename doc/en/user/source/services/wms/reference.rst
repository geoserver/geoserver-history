.. _wms_reference: 

WMS reference
============= 

The `Web Map Service <http://www.opengeospatial.org/standards/wms>`_ (WMS) is a standard created by the OGC that refers to the sending and receiving of geographically registered map images through HTTP.  GeoServer supports WMS version 1.1.1, and so the information below will refer to this specific version unless otherwise noted.

WMS can perform the following operations:

.. list-table::
   :widths: 20 80

   * - **Operation**
     - **Description**
   * - ``GetCapabilities``
     - Retrieves a list of the server's data, as well as valid WMS operations and parameters
   * - ``GetMap``
     - Retrieves a map image based on geographic data 
   * - ``GetFeatureInfo``
     - Retrieves information about particular features as shown on a map image
   * - ``DescribeLayer``
     - Retrieves an XML description of a map layer


.. _wms_getcap:

GetCapabilities
---------------

.. warning: Add info on service, version, request, updatesequence(?).

.. _wms_getmap:

GetMap
------

.. warning: Add info on service, version, request, layers, styles, srs, bbox, width, height, format, transparent, bgcolor, exceptions, time, elevation, sld, etc...

.. _wms_getfeatureinfo:

GetFeatureInfo
--------------

.. warning: Add info on service, version, request, query_layers, info_format, feature_count, x, y, exceptions (?), etc...

.. _wms_describelayer:

DescribeLayer
-------------