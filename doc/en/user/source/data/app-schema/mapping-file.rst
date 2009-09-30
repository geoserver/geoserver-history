.. _app-schema.mapping-file:

Mapping File
============

An app-schema feature type is configured using a mapping file that defines the data source for the feature and the mappings from the source data to XPaths in the output XML.


Outline
-------

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


Mapping file schema
```````````````````

* ``AppSchemaDataAccess.xsd`` is optional because it is not used by GeoServer. The presence of ``AppSchemaDataAccess.xsd`` in the same folder as the mapping file enables XML editors to observe its grammar and provide contextual help.


Settings
--------

``namespaces``
``````````````

The ``namespaces`` section defines all the XML namespaces used in the mapping file::

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


``includedTypes`` (optional)
````````````````````````````

Non-feature types (eg. gsml:CompositionPart is a data type that is nested in gsml:GeologicUnit) may be mapped separately for its reusability, but we don't want to configure it as a feature type as we don't want to individually access it. Related feature types don't need to be explicitly included here as it would have its own workspace configuration for Geoserver to find it. The location path in ``Include`` tag is relative to the mapping file. For an example, if gsml:CompositionPart configuration file is located in the same directory as the gsml:GeologicUnit configuration::

    <includedTypes>
        <Include>gsml_CompositionPart.xml</Include>
    </includedTypes>



``sourceDataStores``
````````````````````

All Geoserver data sources are available for use. Parameters can be provided in any order.


``catalog``
```````````

An OASIS XML Catalog is a standard configuration file format that instructs an XML processing system how to process entity references:

* http://en.wikipedia.org/wiki/XML_Catalog
* http://www.oasis-open.org/committees/entity/spec-2001-08-06.html

In the case of GeoServer app-schema, the catalog is a local filesystem cache of all the schemas used to define the output feature types. The ``catalog`` element provides the location of an OASIS XML catalog that contains all dependent schemas, given as a path relative to the mapping file, for example::

    <catalog>../../../schemas/catalog.xml</catalog>

In practice it is mandatory to provide an OASIS catalog because the implementation otherwise has difficulty resolving relative imports in schema files.



``targetTypes``
```````````````

The ``targetTypes`` section lists all the application schemas required to define the mapping. Typically one one is required. For example::

    <targetTypes>
        <FeatureType>
            <schemaUri>http://www.geosciml.org/geosciml/2.0/xsd/geosciml.xsd</schemaUri>
        </FeatureType>
    </targetTypes>


Mappings
--------

``typeMappings``
````````````````

The ``typeMappings`` section is the heart of the app-schema module. It defines the mapping from simple features to the the nested structure of one or more simple features.

For example::

    <typeMappings>
        <FeatureTypeMapping>
            <sourceDataStore>datastore</sourceDataStore>
            <sourceType>mappedfeature</sourceType>
            <targetElement>gsml:MappedFeature</targetElement>
            <attributeMappings>
                <AttributeMapping>
                    ...

* ``sourceDataStore`` must be an identifier you provided when you defined a source data store the ``sourceDataStores`` section.
* ``sourceType`` is the simple feature type name

    * table or view name, lowercase for PostGIS, uppercase for Oracle.
    * property file name (without the .properties suffix)

* ``targetElement`` is the the element name in the target application schema. This is the same as the WFS feature type name.
* ``attributeMappings`` lists


``attributeMappings``
`````````````````````

``attributeMappings`` comprises a list of ``AttributeMapping`` elements.


``AttributeMapping``
````````````````````

Outline::

    <AttributeMapping>
        <targetAttribute>...</targetAttribute>
        <idExpression>...</idExpression>
        <sourceExpression>...</sourceExpression>
        <targetAttributeNode>...</targetAttributeNode>
        <isMultiple>...</isMultiple>
        <ClientProperty>...</ClientProperty>
    </AttributeMapping>

``targetAttribute``
```````````````````

``targetAttribute`` is the XPath to the output element, in the context of the target element. For example, if the containing mapping is for a feature, you should be able to map a ``gml:name`` property by setting the target attribute::

    <targetAttribute>gml:name</targetAttribute>

Multivalued attributes resulting from denormalised sources are automatically encoded. If you wish to encode multivalued attributes from different input columns as a specific instance of an attribute, you can use a (one-based) index. For example, you can set the third ``gml:name`` with::

    <targetAttribute>gml:name[3]</targetAttribute>


``idExpression`` (optional)
```````````````````````````
A CQL expression that is used to set the ``gml:id`` of the output feature type. This could be a column in a database, the automatically generated simple feature ID obtained with ``getId()``, or some other expression.


``sourceExpression``
````````````````````

Use a ``sourceExpression`` tag to set the element content from source data. For example, to set the element content from a column call ``DESCRIPTION``::

    <sourceExpression><OCQL>DESCRIPTION</OCQL></sourceExpression>

You can use CQL expressions to calculate the content of the element. Ths example concatenated strings from two columns and a literal::

    <sourceExpression>
        <OCQL>strConCat(FIRST , strConCat(' followed by ', SECOND))</OCQL>
    </sourceExpression>

.. warning:: Avoid use of CQL expressions for properties that users will want to query, because the current implementation cannot reverse these expressions to generate efficient SQL, and will instead read all features to calculate the property to find the features that match the filter query. Falling back to brute force search makes queries on CQL-calculated expressions very slow. If you must concatenate strings to generate content, you may find that doing this in your database is much faster.


``targetAttributeNode`` (optional)
``````````````````````````````````

``targetAttributeNode`` is required wherever a property type contains an abstract element and app-schema cannot determine the type of the enclosed attribute. This mapping must come before the mapping for the enclosed elements. In this example, ``gsml:positionalAccuracy`` is a ``gsml:CGI_ValuePropertyType`` which contains a gsml:CGI_Value, which is abstract. In this case, ``targetAttributeNode`` must be used to set the type of the property type to a type that encloses a non-abstract element::

    <AttributeMapping>
        <targetAttribute>gsml:positionalAccuracy</targetAttribute>
        <targetAttributeNode>gsml:CGI_TermValuePropertyType</targetAttributeNode>
    </AttributeMapping>

Note that the GML encoding rules require that complex types are never the direct property of another complex type; they are always contained in a property type to ensure that their type is encoded in a surrounding element. Encoded GML is always type/property/type/property. This is also known as the GML "striping" rule. The consequence of this for app-schema mapping files is that ``targetAttributeNode`` must be applied to the property and the type must be set to the XSD property type not to the type of the contained attribute (``gsml:CGI_TermValuePropertyType`` not ``gsml:CGI_TermValueType``).

Because the XPath refers to a property type not the encodes content, ``targetAttributeNode`` often appears in a mapping with ``targetAttribute`` and no other elements.

``isMultiple`` (optional)
`````````````````````````

The ``isMultiple`` element states whether there might be multiple values for this attribute.Because the default value is ``false`` and it is omitted in this case, it is most usually seen as::

    <isMultiple>true</isMultiple>


``ClientProperty`` (optional, multivalued)
``````````````````````````````````````````

A mapping can have one or more ``ClientProperty`` elements, which set XML attributes on the mapping target. Each ``ClientProperty`` has a ``name`` and a ``value`` which is an arbitrary CQL expression. No ``OCQL`` element is used inside these.

This ``ClientProperty`` element sets the ``codeSpace`` XML attribute to the literal string ``urn:ietf:rfc:2141``. Note the use of single quotes around the literal string. This could be applied to any GML CodeType::

    <ClientProperty>
        <name>codeSpace</name>
        <value>'urn:ietf:rfc:2141'</value>
    </ClientProperty>

This ``ClientProperty`` element sets the ``xlink:href`` XML attribute to to the value of the ``RELATED_FEATURE_URN`` field in the data source (could be a column in an Oracle database table). This could be applied to any property type, such a GML FeaturePropertyType, or other type modelled on the GML association pattern::

    <ClientProperty>
        <name>xlink:href</name>
        <value>RELATED_FEATURE_URN</value>
    </ClientProperty>


CQL
---

* String literals are enclosed in single quotes, for example ``'urn:ogc:def:nil:OGC:missing'``.

* The uDig manual contains information on CQL:

    * http://udig.refractions.net/confluence/display/EN/Common+Query+Language




