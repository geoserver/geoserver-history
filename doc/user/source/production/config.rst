.. _production_config:

Configuration Considerations
============================

Use production logging
----------------------

Logging may visibly affect the performance of your server. High logging levels are often necessary to track down issues, but by default you should run with low levels.  (You can switch the logging levels at runtime, so don't worry about having to stop the server to gather more information.)  

You can change the logging level by going to the :ref:web_admin_config_server section of the :ref:`web_admin`.  You'll want to choose the ``PRODUCTION`` configuration.

Set a service strategy
----------------------

A service strategy is the method in which output is served to the client.  This is a balance between proper form (being absolutely sure of reporting errors with the proper OGC codes, etc) and speed (serving output as quickly as possible).  This is a decision to be made based on the function that GeoServer is providing for you.  You can configure the service strategy by modifying the ``web.xml`` file of your GeoServer instance.

The possible strategies are:

.. list-table::
   :widths: 20 80

   * - **Strategy**
     - **Description**
   * - ``SPEED``
     - Serves output right away. This is the fastest strategy, but proper OGC errors are usually omitted.
   * - ``BUFFER``
     - Stores the whole result in memory, and then serves it after the output is complete.  This ensures proper OGC error reporting, but delays the response quite a bit and can exhaust memory if the response is large.
   * - ``FILE``
     - Similar to ``BUFFER``, but stores the whole result in a file instead of in memory. Slower than ``BUFFER``, but ensures there won't be memory issues.
   * - ``PARTIAL-BUFFER`` 
     - A balance between ``BUFFER`` and ``SPEED``, this strategy tries to buffer in memory a few KB of response, then serves the full output.

Personalize your server
-----------------------

This is isn't a performance consideration, but is just as important.  In order to make GeoServer as useful as possible, you should customize the server's metadata to your organization.  It may be tempting to skip some of the configuration steps, and leave in the same keywords and abstract as the sample.  But this will only confuse potential users.

Here is a short list:

* Fill out WFS, WMS, and WCS Contents sections (this info will be broadcast as part of the GetCapabilities documents)
* Serve your data with your own namespace (and provide a correct URI)
* Remove default layers (such as ``topp:states``)

Set security
------------

GeoServer by default includes WFS-T (transactions), which lets users modify your data. If you don't want your database modified, you can turn off transactions in the Config WFS Content section of the :ref:`web_admin`. Set the ``Service Level`` to ``Basic``. If you'd like some users to be able to modify some but not all of your data, you will have to set up an external security service. An easy way to accomplish this is to run two GeoServer instances and configure them differently, and use a simple authentication to only allow certain users to have access.

For extra security, make sure that the connection to the datastore that is open to all is with a user who has read only permissions. This will eliminate the possibility of a SQL injection (though GeoServer is generally not vulnerable to that sort of attack).

Cache your data
---------------

Server-side caching of WMS tiles is the best way to increase performance.  In caching, pre-rendered tiles will be saved, eliminating the need for redundant WMS calls.  There are several ways to set up WMS caching for GeoServer.  GeoWebCache is the simplest method, as it comes bundled with GeoServer.  (See the section on :ref:`geowebcache` for more details.)  Another option is `TileCache <http://tilecache.org>`_.  You can also use a more generic caching system, such as `OSCache <http://www.opensymphony.com/oscache/>`_ (an embedded cache service) or `Squid <http://www.squid-cache.org>`_ (a web cache proxy).