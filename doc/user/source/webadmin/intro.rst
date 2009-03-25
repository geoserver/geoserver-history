.. _web_admin_intro:

Interface basics
================

The Web Administration Interface is a easy-to-use, web-based graphical interface for viewing and configuring GeoServer. 

Accessing
---------

By default, GeoServer will start a web server on localhost at port 8080 accessible by the following URL:

::

   http://localhost:8080/geoserver
   
.. note:: How to change this?

When correctly configured, the welcome page will show up in your browser.
   
.. note:: Image of main page here


.. _web_admin_welcome:

Welcome page
------------

The Welcome page contains lots of information at a glance, in the form of the health bars on the left side of the page.  These colored bars will show graphically any misconfigurations of the WFS, WMS, and WCS aspects of GeoServer (if applicable).

.. warning:: Add description of what the bar colors mean

The welcome page contains many links for through various areas of the GeoServer configuration.  There are also externals links to the GeoServer homepage at http://geoserver.org .

Handy links are available to the WCS GetCapabilities, WFS :ref:`wfs_getcap`, and WMS GetCapabilities documents.  Also, a quick link to the SRS List, a listing of all projections accesible from within GeoServer.

The majority of the configuration settings are contained in three menu trees, the :ref:`web_admin_admin`, :ref:`web_admin_config`, and :ref:`web_admin_demo`.

