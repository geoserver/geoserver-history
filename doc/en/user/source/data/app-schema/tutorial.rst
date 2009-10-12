.. _app-schema.tutorial:

Tutorial
========

This tutorial demonstrates how to configure two complex feature types using the app-schema plugin and data from two property files.


GeoSciML
---------

This example uses `Geoscience Markup Language (GeoSciML) 2.0 <http://geosciml.org/geosciml/2.0/doc/>`_, a GML application schema:

    *"GeoSciML is an application schema that specifies a set of feature-types and supporting structures for information used in the solid-earth geosciences."*

The tutorial defines two feature types:

#. ``gsml:GeologicUnit``, which describes "a body of material in the Earth".

#. ``gsml:MappedFeature``, which describes the representation on a map of a feature, in this case ``gsml:GeologicUnit``.

Because a single ``gsml:GeologicUnit`` can be observed at several distinct locations on the Earth's surface, it can have a multivalued ``gsml:occurrence`` property, each being a ``gsml:MappedFeature``.


Configuration
-------------

* Install GeoServer as usual.

* Install the app-schema plugin (place the jar files in ``WEB-INF/lib``).

* The tutorial configuration is a complete working GeoServer data directory. It includes all the schema (XSD) files required to use GeoSciML 2.0, the data files, and the app-schema configuration files. There are two ways you can get it:

    #. Download :download:`geoserver-app-schema-tutorial-config.zip` and unzip it into the folder that you will use as your data directory.

    #. Check it out from the `AuScope subversion repository <https://svn.auscope.org/subversion/AuScope/geoserver/config/geoserver-app-schema-tutorial-config/trunk/>`_.

* If the data directory differs from the default, edit ``WEB-INF/web.xml`` to set ``GEOSERVER_DATA_DIR``. (Be sure to uncomment the section that sets ``GEOSERVER_DATA_DIR``.)

* Perform any configuration required by your servlet container, and then start the servlet. For example, if you are using Tomcat, configure a new context in ``server.xml`` and then restart Tomcat.


Test
----

Test the GeoServer app-schema WFS in a web browser. If GeoServer is listening on ``localhost:8080`` you can query the two feature types using these links:

* http://localhost:8080/geoserver/wfs?request=GetFeature&typeName=gsml:GeologicUnit

* http://localhost:8080/geoserver/wfs?request=GetFeature&typeName=gsml:MappedFeature

The data in this tutorial is fictitious. Some of the text and numbers have been taken from real data, but have been modified to the extent that they have no real-world meaning.


gsml:GeologicUnit WFS response
``````````````````````````````

* :download:`The WFS response <gsml_GeologicUnit-wfs-response.xml>` for ``gsml:GeologicUnit`` contains two features corresponding to the two rows in ``gsml_GeologicUnit.properties``.

* Note that the first ``gsml:GeologicUnit`` has two ``gsml:occurrence`` properties, while the second has one. Feature chaining has been used to construct a multivalued property. 

* The response document has been manually pretty-printed, so contains more whitespace than the original GeoServer response, but is otherwise a complete WFS response.


``datastore.xml``
-----------------

Each data store configuration file ``datastore.xml`` specifies the location of a mapping file and triggers its loading as an app-schema data source. This file should not be confused with the source data store, which is specified inside the mapping file.

For ``gsml_GeologicUnit`` the file is ``workspaces/gsml/gsml_GeologicUnit/datastore.xml``::

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


For ``gsml:MappedFeature`` the file is ``workspaces/gsml/gsml_MappedFeature/datastore.xml``::

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


Mapping files
-------------

The mapping files are:

* ``workspaces/gsml/gsml_GeologicUnit/gsml_GeologicUnit.xml``

* ``workspaces/gsml/gsml_MappedFeature/gsml_MappedFeature.xml``


Namespaces
``````````

Each mapping file contains namespace prefix definitions::

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

Only those namespace prefixes used in the mapping file need to be declared.


Source data store
`````````````````

The data for this tutorial is contained in two property files:

* ``workspaces/gsml/gsml_GeologicUnit/gsml_GeologicUnit.properties``

* ``workspaces/gsml/gsml_MappedFeature/gsml_MappedFeature.properties``

:ref:`data_java_properties` describes the format of property files.

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


Catalog
```````

Both feature types use the same OASIS XML Catalog, given as a path relative to the mapping file::

	<catalog>../../../schemas/catalog.xml</catalog>

* The catalog contains the GeoSciML 2.0 schemas and its dependencies.
* Note that some dependencies are imported as relative filesystem paths, and so are not resolved through the catalog.
* GML 3.1.1 is also a dependency, but is not required because it is distributed with GeoServer.
* Use of a catalog is required because the implementation otherwise fails to honour relative imports.


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




