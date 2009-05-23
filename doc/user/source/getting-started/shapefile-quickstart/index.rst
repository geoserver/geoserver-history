.. _shapefile_quickstart:

Adding a Shapefile
==================

This tutorial walks through the steps of publishing a Shapefile with GeoServer.

.. note::

   This tutorial assumes that GeoServer is running on http://localhost:8080/geoserver.

Getting started
---------------

#. Download the zip file :download:`nyc_roads.zip`. It contains a Shapefile with a subset of roads from New York City that will be used during in this tutorial.

#. Unzip ``nyc_roads.zip`` into ``<GEOSERVER_DATA_DIR>/data`` where ``GEOSERVER_DATA_DIR`` is the root of the GeoServer data directory. Unzipping the archive will result in the following four files::

      nyc_roads.shp
      nyc_roads.shx
      nyc_roads.dbf
      nyc_roads.prj

Create a new data store
-----------------------

The first step is to create a *data store* for the Shapefile. The data store tells GeoServer how the Shapefile should be loaded, in particular where it is located.

    #. In a web browser navigate to http://localhost:8080/geoserver.

    #. Navigate to ``Config->Data->DataStores``.

       .. figure:: 11-datastores.png
          :alt: Data stores

    #. Create a new data store by clicking the ``New`` link.

       .. figure:: 12-new-datastore.png
          :alt: Creating a new data store

    #. Select ``Shapefile`` from the drop down and enter "nyc_roads_shapefile" in the text field. Then click the ``New`` button.

       .. figure:: 13-new-shapefile.png
          :alt: Adding a new Shapefile

    #. Specify the Shapefile location by entering ``file:data/nyc_roads.shp`` in the ``url`` text field. Then click the ``Submit`` button.

       .. figure:: 14-shapefile.png
          :alt: Specifying Shapefile location

    #. Click the ``Apply`` button located in the upper left hand corner of the page.

       .. figure:: apply.png
          :alt:


Configure the feature type
--------------------------

The next step is to configure the *feature type* for the Shapefile. The feature type tells GeoServer how the Shapefile should be published. 

    #. Set the *style* by selecting ``line`` from the ``Style`` drop down list.

       .. figure:: 21-style.png
          :alt:

    #. Generate the *bounds* by clicking the ``Generate`` button.

       .. figure:: 22-bounds.png
          :alt:

    #. Scroll to the bottom of the and click the ``Submit`` button.

    #. Finalize changes by clicking the ``Apply`` button in the upper left hand corner of the page.

       .. figure:: apply.png
          :alt:

Preview the Layer
-----------------

The final step is to verify that the Shapefile has been published properly. To do this :ref:`map_preview` will be used.

Navigate to the map preview and select the ``topp:nyc_roads`` link.

    .. figure:: 32-nyc_roads-preview-link.png

If the shapefile was added properly the result should be an OpenLayers map:

    .. figure:: 33-nyc_roads-preview.png

