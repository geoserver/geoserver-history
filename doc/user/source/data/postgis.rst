.. _data_postgis: 

PostGIS
=======

`PostGIS <http://postgis.refractions.net>`_ is an open source spatial
database based on PostgreSQL and is currently the most popular open
source spatial database today. PostGIS was developed by `Refractions
Research <http://www.refractions.net>`_.


Adding a PostGIS database
-------------------------

As with all vector formats, adding a PostGIS database to GeoServer
involves adding a new data store through the web administration
tool. This process is described step by step in the
:ref:`postgis_quickstart` tutorial.

.. image:: postgis.png
   :align: center

PostGIS options
---------------

.. list-table::
   :widths: 20 80

   * - ``host``
     - The database host.
   * - ``port``
     - The port the database is listening for connections on. The default is 
       5432 for PostGIS.
   * - ``schema``
     - The database schema to load tables from. The default for PostGIS is the
       *public* schema.
   * - ``user``
     - The name of the user to connect to the database as.
   * - ``password``
     - The password to use when connecting to the database. Left blank for no 
       password.
   * - ``wkb_enabled``
     - Specifies wether Well Known Binary (WKB) or Well Known Text (WKT) should be used when reading and writing geometry objects to and from PostGIS. It is recommended that WKB be used as it is faster and more accurate than WKT.
   * - ``loose bbox``
     - Flag which controls how bounding box comparisons are made against geometries in the database. See the :ref:`using_loose_bbox` section below for more information.
   * - ``estimated_extent``
     - Flag affecting how table bounds are calculated. See the :ref:`using_estimated_extent` section below.
   * - ``max connections``

       ``min connections``

       ``validate connections``

     - Connection pool configuration parameters. See the :ref:`connection_pooling` section for details. 

.. _using_loose_bbox:

Using loose bounding box
------------------------

When set only the bounding box of a geometry is used which results in a significant performance gain. The downside is that some geometries may be considered inside of a bounding box when they are technically not. 

If the primary use of the database is through the WMS this flag can be set as a loss of some accuracy is usually acceptable. However if using with the WFS and making use of BBOX filtering capabilities, this flag should not be set.

.. _using_estimated_extent:

Using estimated extent
----------------------

When set GeoServer will make use the PostGIS ``estimated_extent`` function when calculating the spatial extent of a table. While setting this flag can result in a serious performance gain **caution** should be taken when setting it.

Publishing a PostGIS view
-------------------------

Publishing a view follows the same process as publishing a table. The only additional step is to manually ensure that the view has an entry in the ``geometry_columns`` table. 

For example consider a table with the schema::

  my_table( id int PRIMARY KEY, name VARCHAR, the_geom GEOMETRY )

Also consider the following view::

  CREATE VIEW my_view as SELECT id, the_geom FROM my_table;

Ad this point the table cannot be published by GeoServer. The following step to manually create the ``geometry_columns`` entry is necessary::

  INSERT INTO geometry_columns VALUES ('','public','my_view','my_geom`, 2, 4326, 'POINT' );


Performance considerations
--------------------------

GEOS
^^^^

GEOS (Geometry Engine Open Source) is an additional component which optionally can be installed with PostGIS. ...

If PostGIS is installed on Windows via the installer it is automatically included. On other platforms like Linux it is optional. It is recommended that GEOS be installed in any PostGIS instance used by GeoServer as GeoServer makes use of its functionality when doing spatial operations like intersections. When GEOS is disabled these operations are performed internally in GeoServer which results in a performance hit.

Spatial indexing
^^^^^^^^^^^^^^^^

Creating a spatial index on tables with a spatial component (ie geometry column) is *strongly recommended*. Any table of non-trivial size which does not have a spatial index will be slow to query spatially. ...

Common problems
---------------

Primary keys
^^^^^^^^^^^^

The transactional nature of a relational database makes formats such as PostGIS ideal for transactional WFS. However in order to enable the transactional extensions on a table it **must** have a primary key. Any table without a primary key defined on it is considered as *read only*.
