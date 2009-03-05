.. _directory:

Directory
=========

Loading and configuring multiple shapefiles can be a very time consuming process. But if your shapefiles all exist in one directory, they can be added in as individual datastores simultaneously by using the directory datastore.

.. note:: 

   While GeoServer has good support for the Shapefile format, it is not the recommended format of choice in a production environment. Databases such as PostGIS are more suitable in production and offer better performance and scalability.

Adding a directory
------------------

Navigate to the new datastore page (**Config** > **Data** > **Datastores** > **New**) in the :ref:`web_admin`.  In the drop down list for **Feature Data Set Description**, select **Directory of spatial files**. Name this datastore in the field marked **Feature Data Set ID** and click **New**.

On the next page, select the namespace to use (this will be used as a prefix for each shapefile in the directory), a description (if any), and the path to the directory. This should be one level inside the GeoServer data directory's data directory. (Example: ``file:data/myshpfolder``) When done, click **Submit**. (Apply and Save as you would normally do.)

All of the shapefiles contained in the directory will be registered, but none of them will be available until they are individually configured (by going to the **Create New FeatureType** page at **Config** > **Data** > **Featuretypes** > **New**).