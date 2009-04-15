.. _mysql:

MySQL
=====

`MySQL <http://www.mysql.com/>`_ is an open source relational database with
some limited spatial functionality.

.. note::
  
   GeoServer does not come built-in with support for MySQL, it must be 
   installed through an extension. Proceed to :ref:`mysql_installation` for
   installation details.

.. warning::

   Currently the MySQL extension is unmaintained and carries unsupported
   status. While still usable, do not expect the same reliability as with
   other drivers.

.. _mysql_installation:

Installing the MySQL extension
------------------------------

#. Download the MySQL extension from the `GeoServer download page 
   <http://geoserver.org/display/GEOS/Download>`_.

   .. warning::

      Ensure the extension matching the version of the GeoServer installation 
      is downloaded.

#. Extract the contents of the archive into the ``WEB-INF/lib`` directory of 
   the GeoServer installation.

Adding a MySQL database
-----------------------

Once the extension is properly installed ``MySQL`` will show up as an option 
when creating a new datastore.

.. figure:: pix/mysql_create.png
   :align: center

   *Creating a MySQL datastore*

.. figure:: pix/mysql_configure.png
   :align: center

   *Configuring a MySQL datastore*

MySQL options
-------------

.. list-table::
   :widths: 20 80

   * - ``host``
     - The mysql server host name or ip address.
   * - ``port``
     - The port on which the mysql server is accepting connections.
   * - ``database``
     - The name of the database to connect to.
   * - ``user``
     - The name of the user to connect to the mysql database as.
   * - ``password``     
     - The password to use when connecting to the database. Left blank for no
       password.
   * - ``max connections``

       ``min connections``

       ``validate connections``

     - Connection pool configuration parameters. See the 
       :ref:`connection_pooling` section for details.
  