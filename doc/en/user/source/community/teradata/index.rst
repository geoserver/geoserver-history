.. _community_teradata:

Teradata
========

.. note:: Teradata database support is not enabled by default and requires the Teradata extension to be installed prior to use.  Please see the section on :ref:`community_teradata_install` for details.

The Teradata Database is a commercial relational database (RDBMS) that specializes in parallel processing and scalability.  (NOT SURE HOW MUCH I SHOULD SAY HERE.)  From version 12.0, Teradata has added geospatial support, closely following the standard known as SQL/MM (SQL Multimedia and Applications Packages) Geospatial support was available through an add-on in version 12.0 and became standard in version 13.0.

GeoServer connects to a Teradata database via a JDBC connection.  

.. warning:: More details here.

For more information on Teradata and the Teradata Database system, please go to `<http://www.teradata.com>`_.

Compatibility
-------------

The GeoServer Teradata extension is compatible with GeoServer 2.2.x and higher.  GeoServer can connect to Teradata databases with version 12.0 or higher.  Version 12.0 of the Teradata Database requires the optional geospatial extension to be installed.


.. _community_teradata_install:

Installing the Teradata extension
---------------------------------

Teradata database support is not enabled by default and requires the GeoServer Teradata extension to be installed prior to use.  In addition to this extension, an additional artifact will need to be downloaded from the Teradata website.

GeoServer artifacts
~~~~~~~~~~~~~~~~~~~

As the Teradata extension is currently a community module, it is not listed on the standard GeoServer download page and must be downloaded as part of a `nightly build <http://geoserver.org/display/GEOS/Nightly>`_.

The latest community extension builds on the nightly build server are located at `<http://gridlock.opengeo.org/geoserver/trunk/community-latest/>`_ .

#. Download the file called :file:`geoserver-[VERSION]-teradata-plugin.zip` (where ``[VERSION]`` will look something like ``2.2-SNAPSHOT``).

#. Extract the contents of the archive into the :file:`WEB-INF/lib` directory of the GeoServer installation.

Teradata artifacts
~~~~~~~~~~~~~~~~~~

In addition to the GeoServer artifacts, it is also necessary to download the Teradata JDBC driver.  This file cannot be redistributed, so must be downloaded directly from the Teradata website.  

#. Download the Teradata JDBC driver at `<https://downloads.teradata.com/download/connectivity/jdbc-driver>`_.

   .. note:: You will need to log in to Teradata's site in order to download this artifact.

#. Extract the contents of the archive into the :file:`WEB-INF/lib` directory of the GeoServer installation.

When all files have been downloaded and extracted, restart GeoServer.  To verify that the installation was successful, see the section on :ref:`community_teradata_add`.

.. note:: The full list of files required are:

    * ``gt-jdbc-teradata-8-SNAPSHOT.jar``
    * ``tdgssconfig.jar``
    * ``terajdbc4.jar``


.. _community_teradata_add:

Adding a Teradata datastore
---------------------------

Once the extension has been added, it will now be possible to load an existing Teradata database as a store in GeoServer.  In the :ref:`web_admin`, click on :ref:`webadmin_stores` then go to :guilabel:`Add a new Store`.  You will see a option, under :guilabel:`Vector Data Stores`, for :guilabel:`Teradata`.  Select this option.

.. figure:: images/teradata_addnewstore.png
   :align: center

   *Teradata in the list of readable stores*

.. note:: If you don't Teradata in this list, the extension has not been installed properly.  Please ensure that the steps in the :ref:`community_teradata_install` have been followed correctly.

On the next screen, enter in the details on how to connect to the Teradata database.  You will need to include the following information:

.. list-table::
   :widths: 20 80
   :header-rows: 1

   * - Option
     - Description
   * - :guilabel:`Workspace`
     - Name of the workspace to contain the database.  This will also be the prefix of any layers server from tables in the database.
   * - :guilabel:`Data Source Name`
     - Name of the database in GeoServer.  This can be different from the name of the Teradata database, if desired.
   * - :guilabel:`Description`
     - Description of the database/store. 
   * - :guilabel:`Enabled`
     - Enables the store.  If disabled, no layers from the database will be served.
   * - :guilabel:`host`
     - Host name where the database exists.  Can be a URL or IP address.
   * - :guilabel:`port`
     - Port number on which to connect to the above host.
   * - :guilabel:`database`
     - Name of the Teradata database.  
   * - :guilabel:`user`
     - User name to connect to use to connect to the database.
   * - :guilabel:`passwd`
     - Password associated with the above user.
   * - :guilabel:`namespace`
     - Namespace to be associated with the database.  This field is altered automatically by the above Workspace field.
   * - :guilabel:`Expose primary keys`
     - .. warning:: TBD
   * - :guilabel:`max connections`
     - Maximum amount of open/pooled connections to the database. 
   * - :guilabel:`min connections`
     - Minimum number of open/pooled connections.
   * - :guilabel:`fetch size`
     - Number of records read with each interaction with the database.
   * - :guilabel:`Connection timeout`
     - Time (in seconds) the connection pool will wait before timing out.
   * - :guilabel:`validate connections`
     - Checks the connection is alive before using it.
   * - :guilabel:`Primary key metadata table`
     - .. warning:: TBD
   * - :guilabel:`Loose bbox`
     - If checked, performs only the primary filter on the bounding box.
   * - :guilabel:`tessellationTable`
     - The name of the database table that contains the tessellations
   * - :guilabel:`estimatedBounds`
     - .. warning:: TBD
   * - :guilabel:`Max open prepared statements`
     - The miximum number of prepared statements.

When finished, click :guilabel:`Save`.

.. figure:: images/teradata_store1.png
   :align: center

.. figure:: images/teradata_store2.png
   :align: center

   *Adding a Teradata store*


Using JNDI
~~~~~~~~~~

GeoServer can also connect to a Teradata database using `JNDI <http://www.oracle.com/technetwork/java/jndi/index.html>`_ (Java Naming and Directory Interface).

To begin, in the :ref:`web_admin`, click on :ref:`webadmin_stores` then go to :guilabel:`Add a new Store`.  You will see a option, under :guilabel:`Vector Data Stores`, for :guilabel:`Teradata (JNDI)`.  Select this option.

.. figure:: images/teradata_selectionjndi.png
   :align: center

   *Teradata (JNDI) in the list of readable stores*

On the next screen, enter in the details on how to connect to the Teradata database.  You will need to include the following information:

.. list-table::
   :widths: 20 80
   :header-rows: 1

   * - Option
     - Description
   * - :guilabel:`Workspace`
     - Name of the workspace to contain the database.  This will also be the prefix of any layers server from tables in the database.
   * - :guilabel:`Data Source Name`
     - Name of the database in GeoServer.  This can be different from the name of the Teradata database, if desired.
   * - :guilabel:`Description`
     - Description of the database/store. 
   * - :guilabel:`Enabled`
     - Enables the store.  If disabled, no layers from the database will be served.
   * - :guilabel:`jndiReferenceName`
     - JNDI path to the database.
   * - :guilabel:`schema`
     - Schema for the above database.
   * - :guilabel:`namespace`
     - Namespace to be associated with the database.  This field is altered by changing the workspace name.
   * - :guilabel:`Expose primary keys`
     - .. warning:: TBD
   * - :guilabel:`Primary key metadata table`
     - .. warning:: TBD
   * - :guilabel:`Loose bbox`
     - If checked, performs only the primary filter on the bounding box.

When finished, click :guilabel:`Save`.

.. figure:: images/teradata_storejndi.png
   :align: center

   *Adding a Teradata store with JNDI*

Adding layers
-------------

One the store has been loaded into GeoServer, the process for loading data layers from database tables is the same as any other database source.  Please see the :ref:`webadmin_layers` section for more information. 

.. note:: Only those database tables that have spatial information will be able to be loaded into GeoServer.

