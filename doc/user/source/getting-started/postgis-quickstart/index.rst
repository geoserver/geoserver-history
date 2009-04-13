.. _postgis_quickstart:

Adding a PostGIS Table
======================

This tutorial walks through the steps of publishing a PostGIS table with GeoServer.

.. note::

   This tutorial assumes that GeoServer is running on http://localhost:8080/geoserver.

.. note::

   This tutorial assumes PostGIS has been previously installed on the system.

Getting started
---------------

#. Download the zip file `<nyc_buildings.zip>`_. It contains a PostGIS dump of a subset of buildings from New York City that will be used during in this tutorial.

#. Create a PostGIS database called "nyc". This can be done with the following command line::

         createdb -T template_postgis nyc

   If the PostGIS install is not set up with the "postgis_template" then the following sequence of commands will perform the equivalent::

        ...

#. Unzip ``nyc_buildings.zip`` to some location on the file system. This will result in the file ``nyc_buildings.sql``. 

#. Import ``nyc_buildings.sql`` into the ``nyc`` database::

         psql -f nyc_buildings.sql nyc


Create a new data store
-----------------------

The first step is to create a *data store* for the PostGIS database "nyc". The data store tells GeoServer how to connect to the database.

    #. In a web browser navigate to http://localhost:8080/geoserver.

    #. Navigate to ``Config->Data->DataStores``.

       .. figure:: 11-datastores.png
          :alt: Data stores

    #. Create a new data store by clicking the ``New`` link.

       .. figure:: 12-new-datastore.png
          :alt: Creating a new data store

    #. Select ``PostGIS`` from the drop down and enter "nyc_postgis" in the text field. Then click the ``New`` button.

       .. figure:: 13-new-postgis.png
          :alt: Adding a new PostGIS database

    #. Specify the PostGIS database connection parameters:

       .. list-table::

          * - ``host``
            - localhost
          * - ``port``
            - 5432
          * - ``schema``
            - public
          * - ``database``
            - nyc

       .. warning::

          The **username** and **password** parameters specific to the user who created the postgis database. Depending on how PostgreSQL is configured the password parameter may be unnecessary.
           
       .. figure:: 14-postgis-connect.png
          :alt: Specifying PostGIS connection parameters

    #. Click the ``Submit`` button.

    #. Click the ``Apply`` button located in the upper left hand corner of the page.

       .. figure:: apply.png


Create a new feature type
-------------------------

The next step is to configure the *feature type* for the ``nyc_buildings`` table. The feature type tells GeoServer how the table should be published. 

    #. Set the *style* by selecting ``polygon`` from the ``Style`` drop down list.

       .. figure:: 21-style.png
          :alt: Setting style

    #. Generate the *bounds* by clicking the ``Generate`` button.

       .. figure:: 22-bounds.png
          :alt: Generating bounds

    #. Scroll to the bottom of the and click the ``Submit`` button.

    #. Finalize changes by clicking the ``Apply`` button in the upper left hand corner of the page.

       .. figure:: apply.png
          :alt: Applying changes


Preview the layer
-----------------

The final step is to verify that the table has been published properly. To do this :ref:`map_preview` will be used.

Navigate to the map preview and select the ``topp:nyc_buildings`` link.

    .. figure:: 32-nyc_buildings-preview-link.png

If the table was added properly the result should be an OpenLayers map:

    .. figure:: 33-nyc_buildings-preview.png

