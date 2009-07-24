.. _production_data:

Data Considerations
===================

Use an external data directory
------------------------------

GeoServer comes with a data directory inside the container.  However, it is a good idea to separate the data from the application.  
Using an external data directory allows for much easier upgrades, since there is no risk of configuration information being overwritten.  An external data directory also makes it easy to transfer your configuration elsewhere if desired.  To point to an external data directory, you only need  to edit the ``web.xml`` file.

Use a spatial database
----------------------

Shapefiles are a very common format for geospatial data.. But if you are running GeoServer in a production environment, it is better to convert your shapefiles to a spatial database, such as `PostGIS <http://postgis.refractions.net>`_.  This is essential if doing transactions (WFS-T). Most of the spatial databases provide shapefile conversion tools. Although there are many options (see the section on :ref:`data`), PostGIS is recommended. Oracle, DB2, and ArcSDE are also supported.

Pick the best performing coverage formats
-----------------------------------------

There are very significant differences between performance of the various coverage formats.

.. warning:: http://geoserver.org/display/GEOSDOC/High+performance+coverage+serving is linked to here.  Once that page is added to these docs, let's update this link.