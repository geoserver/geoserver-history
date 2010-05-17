.. _data_sqlserver:

Microsoft SQL Server
====================

.. note:: GeoServer does not come built-in with support for SQL Server; it must be installed through an extension. Proceed to :ref:`sqlserver_install` for installation details.

Microsoft's `SQL Server <http://www.microsoft.com/sqlserver/2008>`_ is a relational database with spatial functionality.

Supported versions
------------------

The extension supports SQL Server 2008.

.. _sqlserver_install:

Installing the SQL Server extension
-----------------------------------

.. warning:: Due to licensing requirements, not all files are included with the extension.  To install SQL Server support, it is necessary to download additional files. 

GeoServer files
```````````````

#. Download the SQL Server extension from the `GeoServer download page <http://geoserver.org/display/GEOS/Download>`_.

   .. warning:: Make sure to match the version of the extension to the version of the GeoServer instance!

#. Extract the contents of the archive into the ``WEB-INF/lib`` directory of the GeoServer installation.

Required external files
```````````````````````

#. Navigate to `Microsoft's JDBC driver download page <http://msdn.microsoft.com/en-us/data/aa937724.aspx>`_.

#. Click the `` Download SQL Server JDBC Driver 2.0`` link.

#. Click the ``Download`` button.

# Accept the license and download the appropriate archive for your operating system.

# Extract the contents of the archive and copy the file ``sqljdbc.jar`` to the ``WEB-INF/lib`` directory of the GeoServer instance.

Adding a SQL Server database
----------------------------

Once the extension is properly installed ``SQL Server`` will show up as an option when creating a new data store.

.. figure:: images/sqlservercreate.png
   :align: center

   *SQL Server in the list of vector data sources*

Configuring a SQL Server data store
-----------------------------------

.. figure:: images/sqlserverconfigure.png
   :align: center

   *Configuring a SQL Server data store*

.. list-table::
   :widths: 20 80

   * - ``host``
     - The sql server instance host name or ip address.
   * - ``port``
     - The port on which the SQL server instance is accepting connections.
   * - ``database``
     - The name of the database to connect to.
   * - ``schema``
     - The database schema to access tables from.
   * - ``user``
     - The name of the user to connect to the oracle database as.
   * - ``password``     
     - The password to use when connecting to the database. Leave blank for no password.
   * - ``max connections``
 
       ``min connections``

     - Connection pool configuration parameters. See the :ref:`connection_pooling` section for details.

Adding a SQL Server database with JNDI
--------------------------------------

Configuring a SQL Server database with JNDI
-------------------------------------------
