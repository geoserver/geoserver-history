.. _geowebcache:

Caching with GeoWebCache
========================

.. image:: geowebcache.png

GeoWebCache is a WMS tiling client.  It runs as a proxy server between a map client and map server, caching tiles as they are requested, eliminating redundant requests and saving large amounts of processing time.  GeoWebCache has been integrated into GeoServer, although it is also available as a `standalone product <http://geowebcache.org>`_.

These pages will discuss GeoServer's built-in GeoWebCache instance.  For information about the standalone product, please see the `GeoWebCache homepage <http://geowebcache.org>`_.

.. toctree::
   :maxdepth: 2

   setup
   using
   demopage
   seeding
