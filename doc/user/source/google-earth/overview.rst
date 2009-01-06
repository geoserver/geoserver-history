.. _google-earth-overview:

Google Earth Overview
=====================

Introduction
------------

GeoServer is an open source server whose function is to enable the 
`Geospatial web <http://en.wikipedia.org/wiki/Geoweb>`_. This is 
analogous to the `Apache Web Server <http://httpd.apache.org/>`_ which 
enables the World Wide Web. GeoServer is focused on connecting existing 
sources of geospatial data (Shapefiles, PostGIS, DB2, ArcSDE, MySQL, 
GeoTIFF, etc.) to the wider web through open standards (KML, WMS, WFS, 
GeoRSS). GeoServer also provides a powerful platform for collaboratively 
editing geospatial data through the web. Currently this is only 
available through front end clients like `OpenLayers 
<http://openlayers.org/>`_ , and `uDig <http://udig.refractions.net/>`_, 
but support for editing data through Google Earth is planned. 


Why use GeoServer with Google Earth?
------------------------------------

For many cases creating a KML file by hand is more than sufficient. 
GeoServer is useful when one wants to put a lot of data on to Google 
Earth. GeoServer operates entirely through a `Network Link 
<http://code.google.com/apis/kml/documentation/kml_tut.html#network_link 
s>`_, which allows it to selectively return information for the area 
being viewed. By providing an open source implementation of this 
powerful feature we hope to enable many more organizations to be seen on 
Google Earth and the wider Geoweb. If you've got existing GIS data we 
encourage you to `try out GeoServer 
<http://geoserver.org/display/GEOS/Download>`_ and test the 
visualization capabilities of Google Earth. 


Standards-based implementation
------------------------------

GeoServer supports Google Earth by providing KML as a `Web Map Service 
<http://en.wikipedia.org/wiki/Web_Map_Service>`_ (WMS) output format. 
This means that adding data published by GeoServer is as simple as 
constructing a standard WMS request and specifying 
"**application/vnd.google-earth.kml+xml**" as the ``outputFormat``. 
Since generating KML is just a WMS call, it fully supports styling via 
the `SLD standard 
<http://en.wikipedia.org/wiki/Styled_Layer_Descriptor>`_. A user can 
even load their own SLD file and GeoServer will render the data as the 
user wants. GeoServer also supports the `WFS 
<http://en.wikipedia.org/wiki/Web_Feature_Service>`_ and `WCS 
<http://en.wikipedia.org/wiki/Web_Coverage_Service>`_ standards for 
access to the raw data, the 'source code' of the maps, for further 
analysis, modifications, and modeling. 


See the :ref:`google-earth-quickstart` to view GeoServer and Google 
Earth in action. 

