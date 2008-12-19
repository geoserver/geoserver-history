.. _wfs: 

Web Feature Service
=================== 

Introduction
------------ 

The `Web Feature Service <http://www.opengeospatial.org/standards/wfs>`_ 
(WFS) is a standard created by the OGC that refers to the 
sending and receiving of geospatial data through the medium of the World 
Wide Web. WFS encodes and transfer information in Geography Markup 
Language, a subset of XML. The current version of WFS is 1.1.0. 
GeoServer supports both version 1.1.0 (the default since GeoServer 
1.6.0) and version 1.0.0. There are differences between these two 
formats, some more subtle than others, and this will be noted where 
differences arise. The current version of WFS is 1.1. WFS version 1.0 is 
still used in places, and we will note where there are differences. 
However, the syntax will often remain the same. 

An important distinction must be made between WFS and Web Map Service 
(WMS), which refers to the sending and receiving of geographic 
information after it has been rendered as a digital image. 

Benefits of WFS
--------------- 

One can think of WFS as the "source code" to the maps that one would 
ordinarily view (via WMS). WFS leads to greater transparency and 
openness in mapping applications. Instead of merely being able to look 
at a picture of the map, as the provider wants the user to see, the 
power is in the hands of the user to determine how to visualize (style) 
the raw geographic and attribute data. The data can also be downloaded, 
further analyzed, and combined with other data. The transactional 
capabilities of WFS allow for collaborative mapping applications. In 
short, WFS is what enables open spatial data. 

Operations
---------- 

WFS can perform the following operations: 

	#. **GetCapabilities** - Determine a server's datasets, operations, and services
	#. **DescribeFeatureType** - Receive information and attributes about a particular dataset
	#. **GetFeature** - Receive the actual data, including geometry and attribute values
	#. **GetGMLObject** *(Version 1.1.0 only)* - Retrieve element instances by traversing XLinks that refer to their XML IDs.
	#. **LockFeature** - Prevent a feature type from being edited
	#. **Transaction** - Make edits to existing feature types, such as creating, updating, and deleting. 

A WFS server that supports Transactions is sometimes known as a WFS-T. 
GeoServer fully supports transactions. 

Differences between WFS versions
-------------------------------- 

The major differences between the WFS versions are: 

* WFS 1.1.0 returns GML3 as the default GML. In WFS 1.0.0 the default was GML2. (GeoServer still supports requests in both GML3 and GML2 formats.) GML3 has slightly different ways of specifying a geometry. 
* In WFS 1.1.0, the way to specify the SRS (Spatial Reference System, aka projection) is *urn:x-ogc:def:crs:EPSG:XXXX*, whereas in version 1.0.0 the specification was *http://www.opengis.net/gml/srs/epsg.xml#XXXX*. This change has implications on the axis order of the returned data (see below LINK). 
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

	* EPSG:xxxx - longitude/latitude
	* http://www.opengis.net/gml/srs/epsg.xml#xxxx - longitude/latitude
	* urn:x-ogc:def:crs:EPSG:xxxx - latitude/longitude 


GetCapabilities
---------------

The **GetCapabilities** operation is a request to a WFS server for a list of what operations and services ("capabilities") are being offered by that server.

A typical GetCapabilities request would look like this:

Using a GET request (standard HTTP):

.. code-block:: html 
http://www.example.com/wfs?
    service=wfs&
    version=1.1.0&
    request=GetCapabilities
	  
The equivalent using POST (at URL http://www.example.com/wfs):
	
.. code-block:: xml 
<GetCapabilities
    service="WFS"
    xmlns="http://www.opengis.net/wfs"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xsi:schemaLocation="http://www.opengis.net/wfs 			
    http://schemas.opengis.net/wfs/1.1.0/wfs.xsd"/>
	
GET requests are simplest to decode, so we will discuss them in detail, but the POST requests are analogous.  (The actual requests would be all on one line, with no line breaks, but our convention here is to break lines for clarity.)  Here there are three parameters being passed to our WFS server, *"service=wfs"*, *"version=1.1.0"*, and *"request=GetCapabilities."*  At a bare minimum, it is required that a WFS request have these three parameters (service, version, and request).  GeoServer relaxes these requirements (setting the default version if omitted), but "officially" they are mandatory, so they should always be included.  The *service* key tells the WFS server that a WFS request is forthcoming.  The *version* key refers to which version of WFS is being requested.  Note that there are only two version numbers officially supported:  "1.0.0" and "1.1.0".  Supplying a value like "1" or "1.1" will likely return an error.  The *request* key is where the actual GetCapabilities operation is specified.

The Capabilities document that is returned is a long and complex chunk of XML, but very important, and so it is worth taking a closer look.  (The 1.0.0 Capabilities document is very different from the 1.1.0 document discussed here, so beware.)  There are five main components we will be discussing (other components are beyond the scope of this document.):

	* **ServiceIdentification** - This section contains basic "header" information such as the Name and ServiceType.  The ServiceType mentions which version(s) of WFS are supported.
	* **ServiceProvider** - This section provides contact information about the company behind the WFS server, including telephone, website, and email.
	* **OperationsMetadata** - This section describes the operations that the WFS server recognizes and the parameters for each operation.  A WFS server can be set up not to respond to all aforementioned operations.
	* **FeatureTypeList** - This section lists the available FeatureTypes.  They are listed in the form "namespace:featuretype".  Also, the default projection of the FeatureType is listed here, along with the resultant bounding box for the data in that projection.
	* **Filter_Capabilities** - This section lists filters available in which to request the data.  SpatialOperators (Equals, Touches), ComparisonOperators (LessThan, GreaterThan), and other functions are all listed here.  These filters are not defined in the Capabilities document, but most of them (like the ones mentioned here) are self-evident.

DescribeFeatureType
-------------------

The purpose of the operation entitled DescribeFeatureType is to request information about an individual FeatureType before requesting the actual data.  Specifically, DescribeFeatureType will request a list of features and attributes for the given FeatureType, or list the FeatureTypes available.

Let's say we want a list of FeatureTypes.  The appropriate GET request would be:

.. code-block:: html 
    http://www.example.com/wfsserver?
       service=wfs&
       version=1.1.0&
       request=DescribeFeatureType

Note again the three required fields (service, version, and request).  This will return the list of FeatureTypes, sorted by namespace.

If we wanted information about a specific FeatureType, the GET request would be:

.. code-block:: html 
    http://www.example.com/wfsserver?
       service=wfs&
       version=1.1.0&
       request=DescribeFeatureType&
       typeName=namespace:featuretype

The only difference between the two requests is the addition of "typeName=namespace:featuretype" where featuretype is the name of the FeatureType and namespace is the name of the namespace that FeatureType is contained in.
