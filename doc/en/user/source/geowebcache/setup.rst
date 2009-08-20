.. _gwc_setup:

Setup
=====

Pointing GeoWebCache to GeoServer's WMS
---------------------------------------

By default, GeoWebCache expects GeoServer to be accessible at the following URL::

   http://localhost:8080/geoserver
   
To point GeoWebCache elsewhere, you will need to specify the location of your GeoServer instance's WMS GetCapabilities path.  Stop GeoServer if it is currently running, and then add the following code to your GeoServer's ``web.xml`` file (located in the ``WEB-INF`` directory):

.. code-block:: xml 

   <context-param>
      <param-name>GEOSERVER_WMS_URL</param-name>
      <param-value>http://example.com/path/wms?request=GetCapabilities</param-value>
   </context-param>

Make sure to replace the URL inside ``<param-value>`` with your GeoServer instance's WMS GetCapabilities path.  Restart GeoServer when done.

.. note:: There is a quick link to the WMS GetCapabilities path accessible from the Welcome page of the :ref:`web_admin`.  (See :ref:`webadmin_basics` for details.)

Setting the directory for cached data
-------------------------------------

GeoWebCache will automatically store cached tiles in the ``gwc`` directory inside your GeoServer data directory.  To change this, stop GeoServer (if it is running) and add the following code to your GeoServer's ``web.xml`` file (located in the ``WEB-INF`` directory):

.. code-block:: xml 

   <context-param>
      <param-name>GEOWEBCACHE_CACHE_DIR</param-name>
      <param-value>C:\temp</param-value>
   </context-param>

Make sure to change the path inside ``<param-value>`` to the desired path.  Restart GeoServer when done.

GeoWebCache with multiple GeoServer instances
---------------------------------------------

GeoWebCache does not work when multiple GeoServer instances are configured to use the same data directory. The H2 database crashes during initialization when t\
he second instance is started.

Two possible workarounds::

   #. Delete gwc*.jar in WEB-INF/lib and restart GeoServer. This disables GeoWebCache. If you wish, you can run a separate instance in front of all your GeoSer\
ver instances.
   #. Set the variable GEOWEBCACHE_CACHE_DIR, described above, to point to a local directory for each instance.


