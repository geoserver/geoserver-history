.. _reference: 

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

.. toctree::
   :maxdepth: 2

   get_capabilities/capabilities
   get_feature_info/featureinfo
   get_map/map
   describe_layer/describelayer
   get_legend_graphic/legendgraphic
  
