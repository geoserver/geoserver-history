.. _gwc_using:

Using GeoWebCache
=================

.. note:: For an advanced discussion of using GeoWebCache, please see the `GeoWebCache documentation <http://geowebcache.org/trac/wiki/Documentation>`_.

Cached requests vs. non-cached requests
---------------------------------------

GeoWebCache works as a proxy between your client and GeoServer, and therefore acts as its own WMS.  To direct your client to GeoWebCache (and thus receive cached tiles) you only need to change the WMS URL.

If your application requests WMS tiles from GeoServer at this URL::

   http://example.com/geoserver/wms

You can invoke GeoWebCache's WMS instead at this URL::

   http://example.com/geoserver/gwc/service/wms
   
In other words, add ``/gwc/service`` in between the path to your GeoServer instance and the WMS call.

As soon as tiles are requested through GeoWebCache, GeoWebCache automatically starts caching.  This means that initial requests for tiles will not be accelerated.  To automate this process of requesting tiles, you can seed the cache.  See the section on :ref:`gwc_seeding` for more details.

Integration with external mapping sites
---------------------------------------

The `Documentation <http://geowebcache.org/trac/wiki/Documentation>`_ on the GeoWebCache homepage contains examples for creating applications that integrate with:

* `Google Maps <http://geowebcache.org/trac/wiki/google_maps>`_
* `Google Earth <http://geowebcache.org/trac/wiki/google_earth>`_
* `Microsoft Virtual Earth <http://geowebcache.org/trac/wiki/virtual_earth>`_

Support for custom projections
------------------------------

The version of GeoWebCache that comes bundled with GeoServer automatically configures every layer served in GeoServer with the two most common projections:

* **EPSG:4326** (standard Latitude/Longitude)
* **EPSG:900913** (Spherical Mercator, the projection used in Google Maps)

If you need another projection, you can create a custom configuration file, ``geowebcache.xml``, in the same directory that contains the cache (this path was determined in the :ref:`gwc_setup` section).  This configuration file is the same as used by the standalone version of GeoWebCache and documented on the `GeoWebCache Configuration <http://geowebcache.org/trac/wiki/configuration>`_ page.  The configuration syntax directly supports the most common WMS parameters, such as style, palette, and background color.  To avoid conflicts, the layers in this file should be named differently from the ones that are automatically loaded from GeoServer.

Troubleshooting
---------------

Sometimes errors will occur when requesting data from GeoWebCache.  Below are some of the most common reasons.

Grid misalignment
`````````````````

Sometimes errors will occur saying that the "resolution is not supported" or the "bounds do not align."  When this happens, it is usually due to the client making requests that do not align with the grid of tiles that GeoWebCache has created.  The map bounds or layer bounds may differ from those of the grid, or the resolution may not be correct.  If you are using OpenLayers as a client, looking at the source code of the included demos may provide more clues to matching up the grid.  