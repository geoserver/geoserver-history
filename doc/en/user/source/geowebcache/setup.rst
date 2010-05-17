.. _gwc_setup:

Setup
=====

Pointing GeoWebCache to GeoServer's WMS
---------------------------------------

As of GeoServer 2.0.1, all communication between GeoServer and GeoWebCache happens by passing messages inside the JVM. The ``GEOWESERVER_WMS_URL`` parameter in ``web.xml`` is therefore deprecated.

Setting the directory for cached data
-------------------------------------

GeoWebCache will automatically store cached tiles in the ``gwc`` directory inside your GeoServer data directory.  To change this, stop GeoServer (if it is running) and add the following code to your GeoServer's ``web.xml`` file (located in the ``WEB-INF`` directory):

.. code-block:: xml 

   <context-param>
      <param-name>GEOWEBCACHE_CACHE_DIR</param-name>
      <param-value>C:\temp</param-value>
   </context-param>

Make sure to change the path inside ``<param-value>`` to the desired path.  Restart GeoServer when done.

Manually configuring GeoWebCache
--------------------------------

If you need to use more of the GeoWebCache's features than the automatic configuration offers then you can create a ``gwc`` directory inside the GeoServer data directory, and in it place a ``geowebcache.xml`` file. Please refer to the GeoWebCache documentation for details.


Check the logfiles after starting GeoServer to verify that this file has been read.


GeoWebCache with multiple GeoServer instances
---------------------------------------------

GeoWebCache does not work when multiple GeoServer instances are configured to use the same data directory. The H2 database crashes during initialization when t\
he second instance is started.

Two possible workarounds::

   * Delete gwc*.jar in WEB-INF/lib and restart GeoServer. This disables GeoWebCache. If you wish, you can run a separate instance in front of all your GeoServer instances.
   * Set the variable GEOWEBCACHE_CACHE_DIR, described above, to point to a local directory for each instance.

