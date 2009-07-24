..  _app_schema_tutorial:

Application Schema Tutorial
===========================

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


Worked example
--------------

This example uses `GeoScience Markup Language (GeoSciML) 2.0 <http://geosciml.org/geosciml/2.0/doc/>`_ to deliver two complex feature types:

#. The feature type ``gsml:GeologicUnit`` describes the intensive properties of a piece of geology.

#. The feature type ``gsml:MappedFeature`` in this case describes one location on a map of a ``gsml:GeologicUnit``.

Because a single ``gsml:GeologicUnit`` can be observed at several distinct locations on the Earth's surface, it can have a multivalued ``gsml:occurrence`` property, each being a ``gsml:MappedFeature``.

* The example configuration can be found in the `AuScope subversion repository <https://svn.auscope.org/subversion/AuScope/geoserver/config/geoserver-app-schema-tutorial-config/trunk/>`_. [#auscope]_

* The configuration is complete can be used immediately. Get it from subversion and set it as your ``GEOSERVER_DATA_DIR``.


gsml:GeologicUnit datastore
```````````````````````````

``workspaces/gsml/gsml_GeologicUnit/datastore.xml``::

    <dataStore>
    	<id>gsml_GeologicUnit_datastore</id>
    	<name>gsml_GeologicUnit</name>
    	<enabled>true</enabled>
    	<workspace>
    		<id>gsml_workspace</id>
    	</workspace>
    	<connectionParameters>
    		<entry key="namespace">urn:cgi:xmlns:CGI:GeoSciML:2.0</entry>
    		<entry key="url">file:workspaces/gsml/gsml_GeologicUnit/gsml_GeologicUnit.xml</entry>
    		<entry key="dbtype">app-schema</entry>
    	</connectionParameters>
    </dataStore>

.. note:: Ensure that there is no whitespace inside an ``entry`` element.


gsml:MappedFeature datastore
````````````````````````````

``workspaces/gsml/gsml_MappedFeature/datastore.xml``::

    <dataStore>
    	<id>gsml_MappedFeature_datastore</id>
    	<name>gsml_MappedFeature</name>
    	<enabled>true</enabled>
    	<workspace>
    		<id>gsml_workspace</id>
    	</workspace>
    	<connectionParameters>
    		<entry key="namespace">urn:cgi:xmlns:CGI:GeoSciML:2.0</entry>
    		<entry key="url">file:workspaces/gsml/gsml_MappedFeature/gsml_MappedFeature.xml</entry>
    		<entry key="dbtype">app-schema</entry>
    	</connectionParameters>
    </dataStore>

.. note:: Ensure that there is no whitespace inside an ``entry`` element.

Namespaces
``````````

Each mapping file contains the same namespace prefix definitions::

	<Namespace>
		<prefix>gsml</prefix>
		<uri>urn:cgi:xmlns:CGI:GeoSciML:2.0</uri>
	</Namespace>
	<Namespace>
		<prefix>gml</prefix>
		<uri>http://www.opengis.net/gml</uri>
	</Namespace>
	<Namespace>
		<prefix>xlink</prefix>
		<uri>http://www.w3.org/1999/xlink</uri>
	</Namespace>

Included types
``````````````

Non-feature types (eg. gsml:CompositionPart is a data type that is nested in gsml:GeologicUnit) may be mapped separately for its reusability, but we don't want to configure it as a feature type as we don't want to individually access it.
Related feature types don't need to be explicitly included here as it would have its own workspace configuration for Geoserver to find it.
The location path in ``Include`` tag is relative to the mapping file. 
For an example, if gsml:CompositionPart configuration file is located in the same directory as the gsml:GeologicUnit configuration::

    <includedTypes>	
        <Include>gsml_CompositionPart.xml</Include>
    </includedTypes>


Source data store
`````````````````

For this example, each feature type uses an identical source data store configuration. The ``directory`` indicates that the source data is contained in property files named by their feature type::

	<sourceDataStores>
		<DataStore>
			<id>datastore</id>
			<parameters>
				<Parameter>
					<name>directory</name>
					<value>file:./</value>
				</Parameter>
			</parameters>
		</DataStore>
	</sourceDataStores>

A more realistic configuration would contain database connection parameters, such as `this mapping file that connects to Oracle Spatial <https://svn.auscope.org/subversion/AuScope/geoserver/config/geoserver-pirsa-minocc-config/trunk/workspaces/gsml/gsml_MappedFeature/gsml_MappedFeature.xml>`_. Note that the database example uses `property interpolation <https://www.seegrid.csiro.au/twiki/bin/view/Infosrvices/GeoserverAppSchemaConfiguration>`_.


OASIS Catalog
`````````````

Both feature types use a common OASIS catalog, given as a path relative to the mapping file::

	<catalog>../../../schemas/catalog.xml</catalog>

This catalog is an svn external in the data directory subversion repository, but you can see it at this `browsable catalog location <https://svn.auscope.org/subversion/AuScope/geoserver/schemas/trunk/catalog.xml>`_. This is the catalog for the `AuScope schema collection <https://svn.auscope.org/subversion/AuScope/geoserver/schemas/trunk/>`_. [#auscope]_

Use of a catalog is required because the implementation otherwise fails to honour relative imports.


Target types
````````````

Both feature types use the same XML Schema, the top-level schema for GeoSciML 2.0::

	<targetTypes>
		<FeatureType>
			<schemaUri>http://www.geosciml.org/geosciml/2.0/xsd/geosciml.xsd</schemaUri>
		</FeatureType>
	</targetTypes>

In this case the schema is published, but because the OASIS Catalog is used for XML Schema resolution, a private or modified XML Schema in the catalog can be used if desired.


Mappings
````````

The ``typeMappings`` element begins with configuration elements. From the mapping file for ``gsml:GeologicUnit``::

	<typeMappings>
		<FeatureTypeMapping>
			<sourceDataStore>datastore</sourceDataStore>
			<sourceType>gsml_GeologicUnit</sourceType>
			<targetElement>gsml:GeologicUnit</targetElement>


* The mapping starts with ``sourceDataStore``, which gives the arbitrary identifier used above to name the source of the input data. For this example, it is a directory containing one or more property files.

* ``sourceType`` gives the name of the source simple feature type. In this case it is the fake simple feature type ``gsml_GeologicUnit``, sourced from the rows of the file ``gsml_GeologicUnit.properties`` in the same directory as the mapping file.

* When working with databases ``sourceType`` is the name of a table or view. Database identifiers must be lowercase for PostGIS or uppercase for Oracle Spatial.


``targetElement`` is the name of the output complex feature type.


gml:id mapping
``````````````

The first mapping sets the ``gml:id`` to be the feature id specified in the source property file::

    <AttributeMapping>
    	<targetAttribute>
    		gsml:GeologicUnit
    	</targetAttribute>
    	<idExpression>
    		<OCQL>getId()</OCQL>
    	</idExpression>
    </AttributeMapping>

* ``targetAttribute`` is the XPath to the element for which the mapping applies, in this case, the top-level feature type.

* ``idExpression`` is a special form that can only be used to set the ``gml:id`` on a feature. For database sources, ``getId()`` will synthesise an id from the table or view name, a dot ".", and the primary key of the table. If this is not desirable, any other field or CQL expression can be used.

.. note: Do not set ``gml:id`` to a string containing colons, because ``gml:id`` is an XML NCNAME and must not contain colons.


Ordinary mapping
````````````````

Most mappings consist of a target and source::

    <AttributeMapping>
    	<targetAttribute>
            gml:description
        </targetAttribute>
    	<sourceExpression>
    		<OCQL>DESCRIPTION</OCQL>
    	</sourceExpression>
    </AttributeMapping>

* In this case, the value of ``gml:description`` is just the value of the ``DESCRIPTION`` field in the property file.

* For a database, the field name is the name of the column (the table/view is set in ``sourceType`` above). Database identifiers must be lowercase for PostGIS or uppercase for Oracle Spatial.

* CQL expressions can be used to calculate content. Use caution because queries on CQL-calculated values prevent the construction of efficient SQL queries.

* Source expressions can be CQL literals, which are single-quoted.


Client properties
`````````````````

In addition to the element content, a mapping can set one or more "client properties" (XML attributes)::

    <AttributeMapping>
    	<targetAttribute>
            gsml:specification
        </targetAttribute>
    	<ClientProperty>
    		<name>xlink:href</name>
    		<value>GU_URN</value>
    	</ClientProperty>
    </AttributeMapping>

* This example from the mapping file for gsml:MappedFeature leaves the content of the ``gsml:specification`` element empty but sets an ``xlink:href`` attribute to the value of the ``GU_URN`` field.

* Multiple ``ClientProperty`` mappings can be set.

In this example from the mapping for ``gsml:GeologicUnit`` both element content and an attribute are provided::

    <AttributeMapping>
    	<targetAttribute>
            gml:name[1]
            </targetAttribute>
    	<sourceExpression>
    		<OCQL>NAME</OCQL>
    	</sourceExpression>
    	<ClientProperty>
    		<name>codeSpace</name>
    		<value>'urn:x-test:classifierScheme:TestAuthority:GeologicUnitName'</value>
    	</ClientProperty>
    </AttributeMapping>

* The ``codespace`` XML attribute is set to a fixed value by providing a CQL literal.


Feature chaining
````````````````

In feature chaining, one feature type is used as a property of an enclosing feature type, by value or by reference::

    <AttributeMapping>
    	<targetAttribute>
            gsml:occurrence
        </targetAttribute>
    	<sourceExpression>
    		<OCQL>URN</OCQL>
    		<linkElement>gsml:MappedFeature</linkElement>
    		<linkField>gml:name[2]</linkField>
    	</sourceExpression>
    	<isMultiple>true</isMultiple>
    </AttributeMapping>

* In this case from the mapping for ``gsml:GeologicUnit``, we specify a mapping for its ``gsml:occurrence``.

* The ``URN`` field of the source ``gsml_GeologicUnit`` simple feature is use as the "foreign key", which maps the the second ``gml:name`` in each ``gsml:MappedFeature``.

* Every ``gsml:MappedFeature`` with ``gml:name[2]`` equal to the ``URN`` of the ``gsml:GeologicUnit`` under construction is included as a ``gsml:occurrence`` property of the ``gsml:GeologicUnit``.



gsml:GeologicUnit WFS response
``````````````````````````````

* :download:`The WFS response <gsml_GeologicUnit-wfs-response.xml>` for ``gsml:GeologicUnit`` contains two features corresponding to the two rows in ``gsml_GeologicUnit.properties``.

* Note that the first ``gsml:GeologicUnit`` has two ``gsml:occurrence`` properties, while the second has one. Feature chaining has been used to construct a multivalued property. 

* The response document has been pretty-printed so contains more whitespace than the original GeoServer response.


Further reading
---------------

* :ref:`GeoServer Feature Chaining User Guide <feature_chaining>`

* `GeoServer Mapping File Property Interpolation <https://www.seegrid.csiro.au/twiki/bin/view/Infosrvices/GeoserverAppSchemaConfiguration>`_

Footnotes
---------

.. [#auscope] AuScope Ltd is funded under the National Collaborative Research Infrastructure Strategy (NCRIS), an Australian Commonwealth Government Programme. `http://www.auscope.org.au/category.php?id=10 <http://www.auscope.org.au/category.php?id=10>`_

