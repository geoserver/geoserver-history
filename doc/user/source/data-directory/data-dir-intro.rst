.. _data_dir_intro:

Introduction to the Data Directory
==================================

The following figure shows the structure of a "vanilla" GeoServer data directory::

   data_directory/
      catalog.xml
      services.xml
      coverages/
      data/
      demo/
      featureTypes/
      palettes/
      plugIns/
      security/
      styles/
      templates/
      user_projections/
      www/

catalog.xml and services.xml
----------------------------

The ``catalog.xml`` file contains a list of all the *data sources* that GeoServer is configured to serve. It contains references to shapefiles, PostGIS databases, GeoTIFF files, and many other types of data. The catalog file also contains other information such as a set of namespaces used by the WFS, and a set of SLD styles used by the WMS.

``catalog.xml`` is a XML file which has the following format::

  <catalog>
    <datastores>
      ...
    </datastores>

    <formats>
      ...
    </formats>

    <namespaces>
      ...
    </namespaces>

    <styles>
      ...
    </styles>
  </catalog>

The ``services.xml`` file contains all *service level configuration*. Among many things this validation options for the WFS, image rendering parameters for the WMS, etc... The services file contains an entry for each service published by GeoServer. This currently includes a WMS, WFS, and WCS entry.
	
``services.xml`` is a XML file which has the following format::

  <services>
    <service type="WMS">
      ..
    </service>
    <service type="WFS">
      ..
    </service>
    <service type="WCS">
      ..
    </service>
  </services>

coverages and featureTypes
--------------------------

	The ``coverages`` and ``featureTypes`` directories contains metadata about "layers" which are published by GeoServer. A *vector* layer corresponds to the ``featureTypes`` directory, and a *raster* layer corresponds to the ``coverages`` directory. The term *layer* refers to both types.

For each layer published by GeoServer a sub-directory is created under the ``coverages`` or ``featureTypes`` directory, depending on if the layer is raster or vector based. The following figure shows the contains of the two respective directories from the data directory of a vanilla GeoServer installation::

   coverages/
      arc_sample/
      img_sample/
      img_sample2_Pk50095/
      mosaic_sample/
      sfDem_dem/

   featureTypes/
      DS_giant_polygon_giant_polygon/
      DS_poi_poi/
      DS_poly_landmarks_poly_landmarks/
      DS_tiger_roads_tiger_roads/
      sfArchsites_archsites/
      sfBugsites_bugsites/
      sfRestricted_restricted/
      sfRoads_roads/
      sfStreams_streams/
      states/
      tasmania_cities/
      tasmania_roads/
      tasmania_state_boundaries/
      tasmania_water_bodies/

Each sub-directory contains a file named ``info.xml`` which contains metadata about the layer. Such metadata includes:

   * The spatial reference system or "projection" of the dataset
   * The spatial extent of the dataset
   * The default style used by the WMS when rendering the layer

TODO: document info.xml in more detail
TODO: write a "todo" role extension :)

data
----
Not to the confused with the "GeoServer data directory" itself, the ``data`` directory is a location where actual data can be stored. This directly is commonly used to store shapefiles and raster files but can be used for any data that is file based.

The main benefit of storing data files inside of the ``data`` directory is portability. Consider a shapefile located external to the data directory at a location ``C:\gis_data\foo.shp``. The ``datastore`` entry in ``catalog.xml`` for this shapefile would like the following::

   <datastore id="foo_shapefile">
      <connectionParams>
        <parameter name="url" value="file://C:/gis_data/foo.shp" />
      </connectionParams>
    </datastore>

Now consider trying to port this data directory to another host running GeoServer. The problem exists in that the location ``C:\gis_data\foo.shp`` probably does not exist on the second host. So either the file must be copied to the new host, or ``catalog.xml`` must be changed to reflect a new location.

Such steps can be avoided by storing ``foo.shp`` inside of the ``data`` directory. In such a case the ``datastore`` entry in ``catalog.xml`` becomes::

    <datastore id="foo_shapefile">
      <connectionParams>
        <parameter name="url" value="file:data/foo.shp"/>
      </connectionParams>
    </datastore>

The ``value`` attribute is re-written to be relative. In this way the entire data directory can be archived, copied to the new host, un-archived, and used directly with no additional changes.

demo
----

The ``demo`` directory contains files which define the *sample requests* available in the *Sample Request Tool* (http://localhost:8080/geoserver/demoRequest.do). For more information see the :ref:`sample_request_tool` section for more information.

palettes
--------

The ``palettes`` directory is used to store pre-computed *Image Palettes*. Image palettes are used by the GeoServer WMS as way to reduce the size of produced images while maintaining image quality. See the :ref:`paletted_images` section for more information.

security
--------
The ``security`` directory contains all the files used to configure the GeoServer security subsystem. This includes a set of property files which define *access roles*, along with the services and data each role is authorized to access. See the :ref:`security` section for more information.

styles
------

The ``styles`` directory contains a number of Styled Layer Descriptor (SLD) files which contain styling information used by the GeoServer WMS. For each file in this directory there is a corresponding entry in ``catalog.xml``::

   <style id="point_style" file="default_point.sld"/>

See the :ref:`styling` for more information about styling and SLD .

templates
---------

The ``template`` directory contains files used by the GeoServer *templating subsystem*. Templates are used to customize the output of various GeoServer operations. See the :ref:`templates` section for more information about templates.

user_projections
----------------

The ``user_projections`` directory contains a single file called ``epsg.properties`` which is used to define *custom* spatial reference systems which are not part of the official `EPSG database <http://www.epsg.org/CurrentDB.html>`_. See the :ref:`user_projections` section for more information about defining custom projections.

www
---

The ``www`` directory is used to allow GeoServer to act like a regular web server and serve regular files. While not a replacement for a full blown web server the ``www`` directory can be useful for serving `OpenLayers <http://openlayers.org>`_ map applications.


