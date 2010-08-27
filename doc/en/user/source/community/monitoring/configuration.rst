.. _monitor_configuration:

Monitor Configuration
=====================

Many aspects of the monitor extension are configurable. All configuration files
are stored in the data directory under the ``monitoring`` directory::

  <data_directory>
      monitoring/
          db.properties
          filter.properties
          hibernate.properties
          monitor.properties

Monitor Mode
------------

The monitoring extension supports different "monitoring modes" that control how
request data is captured and stored. Currently three modes are supported:

  * live - Only information about live requests is maintained.
  * history - Only historical request information is available. No live information is maintained.
  * mixed - A combination of live and history. This mode is experimental.

Live Mode
^^^^^^^^^

Live mode only maintains short lived information about requests that are 
currently executing. It also maintains a small buffer of recent requests. No 
external database is used with this mode and no information is persisted for 
the long term.

This mode is most appropriate in cases where a user only cares about what a 
server is doing in real time and is not interested about request history.

History Mode
^^^^^^^^^^^^

History mode persists information about all requests in an external database. It
does not provide any real time information. This mode is appropriate in cases
where a user is most interested in analyzing request history over a past time
period.

Mixed Mode
^^^^^^^^^^

Mixed mode combines both live and history mode in that it maintains both real 
time information and persists all request data to the monitoring database. This
mode however is experimental and comes with more overhead than the other two 
modes. This is due to the fact that in order to mainain live information mixed
mode must perform numerous database transactions over the life cycle of a single
request. Whereas history mode only has to perform a single database transaction
for a request.

This mode is most appropriate when both real time request information and 
request history are desired. This mode is also most appropriate in a clustered
server environment in which a user is interested in viewing real time requset
information about multiple nodes in a cluster.

Monitor Database 
----------------

By default monitored request data is stored in an embedded H2 database located
in the ``monitoring`` directory. This can be changed by editing the 
``db.properties`` file::

   # default configuration is for h2 
   driver=org.h2.Driver
   url=jdbc:h2:file:${GEOSERVER_DATA_DIR}/monitoring/monitoring

For example to store request data in an external postgresql database::

   # sample configuration for postgres
   driver=org.postgresql.Driver 
   url=jdbc:postgresql://192.168.1.124:5432/monitoring
   username=bob
   password=foobar
   
Request Filters
---------------

By default not all requests are monitored requests. Those not monitored include any web admin requests or any monitor http api requests. This is configured in
the ``filter.properties`` file:: 

   /rest/monitor/**
   /web
   /web/** 

These default filters can be changed or extended upon to filter more types of 
requests. For example to filter out all wfs requests::

   /wfs

The contents of ``filter.properties`` are a series of ant-style patterns that 
are applied to the *path* of the request. Consider the following request::

   http://localhost:8080/geoserver/wms?request=getcapabilities

The path of the above request is "/wms". In the following request::

   http://localhost:8080/geoserver/rest/workspaces/topp/datastores.xml

The path is "/workspaces/topp/datastores.xml". It is the component of the url
after the first component after "/geoserver" and before the query string "?" and
includes the preceding "/":: 

   http://<host>:<port>/geoserver/<first>/<path>?<queryString>

.. note::

   For more information about ant style pattern matching see the `apache ant manual <http://ant.apache.org/manual/dirtasks.html>`_.
   


