..  _app_schema:

Application Schema Support
==========================

The application schema (app-schema) module provides support for complex features in GeoServer WFS. This tutorial demonstrates how to configure an app-schema complex feature type.

GeoServer provides support for a broad selection of simple feature data stores, including property files, shapefiles, and JDBC data stores such as PostGIS and Oracle Spatial. The app-schema module takes one of these simple feature data stores and applies a mapping to convert the simple feature into a complex feature. The app-schema module looks to GeoServer just like any other data store and so can be loaded and used to service WFS requests. In effect, the app-schema data store is a wrapper or adapter that converts a simple feature data store into complex features for delivery via WFS. The mapping works both ways, so queries against properties of complex features are supported.

Configuration of an app-schema complex feature type requires manual construction of a GeoServer data directory that contains a mapping file and a ``datastore.xml`` that points at this mapping file. The data directory also requires all the other ancillary configuration files used by GeoServer for simple features. GeoServer can serve simple and complex features at the same time.

.. note:: This tutorial describes the new GeoServer ``web2`` data directory layout.


Workspace layout
----------------

The GeoServer data directory contains a folder called ``workspaces`` with the following structure::

    workspaces
       - gsml
           - SomeDataStore
               - SomeFeatureType
                   - featuretype.xml
               - datastore.xml
               - SomeFeatureType-mapping-file.xml

.. note:: The folder inside ``workspaces`` must have a name (the workspace name) that is the same as the namespace prefix (``gsml`` in this example).


Datastore
`````````
Each data store folder contains a file ``datastore.xml`` that contains the configuration parameters of the data store. To create an app-schema feature type, the data store must be configured to load the app-schema service module and process the mapping file. These options are contained in the ``connectionParameters``:

* ``namespace`` defines the XML namespace of the complex feature type.

* ``url`` is a ``file:`` URL that gives the location of the app-schema mapping file relative to the root of the GeoServer data directory.

* ``dbtype`` must be ``app-schema`` to trigger the creation of an app-schema feature type.


Mapping file
````````````

An app-schema feature type is configured using a mapping file that defines the data source for the feature and the mappings from the source data to XPaths in the output XML.

Here is an outline of a mapping file::

    <?xml version="1.0" encoding="UTF-8"?>
    <as:AppSchemaDataAccess xmlns:as="http://www.geotools.org/app-schema"
    	xmlns:ogc="http://www.opengis.net/ogc" xmlns:xs="http://www.w3.org/2001/XMLSchema"
    	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    	xsi:schemaLocation="http://www.geotools.org/app-schema AppSchemaDataAccess.xsd
            http://www.opengis.net/ogc http://schemas.opengis.net/filter/1.1.0/expr.xsd">
    	<namespaces>...</namespaces>
        <includedTypes>...</includedTypes>
    	<sourceDataStores>...</sourceDataStores>
    	<catalog>...</catalog>
    	<targetTypes...</targetTypes>
    	<typeMappings>...</typeMappings>
    </as:AppSchemaDataAccess>

* ``namespaces`` defines all the namespace prefixes used in the mapping file.

* ``includedTypes`` (optional) defines all the included non-feature type mapping file locations that are referred in the mapping file. 

* ``sourceDataStores`` provides the configuration information for the source data stores.

* ``catalog`` is the location of the OASIS Catalog used to resolve XML Schema locations.

* ``targetTypes`` is the location of the XML Schema that defines the feature type.

* ``typeMappings`` give the relationships between the fields of the source data store and the elements of the output complex feature.

* ``AppSchemaDataAccess.xsd`` is optional because it is not used by GeoServer. The presence of ``AppSchemaDataAccess.xsd`` in the same folder as the mapping file enables XML editors to observe its grammar and provide contextual help.


