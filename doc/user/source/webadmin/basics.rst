.. _basics:

Interface basics
================

The Web Administration Interface is a easy-to-use, web-based graphical interface for viewing and configuring GeoServer. 

Welcome Page
------------

By default, GeoServer will start a web server on localhost at port 8080 accessible by the following URL:

::

   http://localhost:8080/geoserver   

When correctly configured, a welcome page will show up in your browser.

.. figure:: images/8080welcome.png
   :align: center
   
   *Welcome Page*
   
The welcome page contains links to various areas of the GeoServer configuration.  The about section in the Server Menu provides external links to the GeoServer documentation, homepage and bug tracker.  The page also provides log in access to the geoserver console. This security measure prevents unauthorized persons from making changes to your GeoServer configuration. The default username and password is ``admin`` and ``geoserver``.  These can be changed only by editing the ``security/users.properties`` file in the :ref:`data_directory`.  

.. figure:: images/8080login.png
   :align: left
   
   *Login*

Regardless of authorization access, the web admin menu links to the ``demo`` and ``layers preview`` portion of the console. The :ref:`demos` page contains helpful links to various information pages, while the :ref:`layerspreview` page provides spatial data in various output formats. 


Service Capabilities
````````````````````
Geoserver Web Coverage Service (WCS), Web Feature Service (WFS), and Web Map Service (WMS) specifications can be accessed from this index page. GeoServer serves data using standard protocols established by the `Open Geospatial Consortium <http://www.opengeospatial.org/>`_ (OGC). WCS allows for requests of coverage data (rasters); WFS allows for requests of geographical feature data (vectors); and WMS allows for requests of images generated from geographical data. 

GeoServer implements the WCS specification 1.0 and 1.1.1; supports all WFS 1.0.0 and WFS 1.1.0 operations, including Transation; and is compliant with WMS 1.1.1, including most of the SLD 1.0 extensions. 