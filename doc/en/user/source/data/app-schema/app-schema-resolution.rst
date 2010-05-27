.. _app-schema.app-schema-resolution:

Application Schema Resolution
=============================

.. note:: Ignore this page if you are using web-published GML 3.1.1 application schemas and have GeoServer deployed on a network that permits it to download these schemas via http/https. Under these circumstances, GeoServer will automatically download and cache all the schemas it needs the first time it starts. No manual handling of schemas will be required.

To be able to encode XML responses conforming to an application schema, the app-schema plugin must be able to locate the application schema files (XSDs) that define the schema. The order of sources used to resolve application schemas (except GML 3.1.1) is:

#. `OASIS Catalog`_
#. `Classpath`_
#. `Cache`_

Every attempt to load a schema works down this list, so imports can be resolved from sources other than that used for the originating document. For example, an application schema in the cache that references a schema found in the catalog will use the version in the catalog, rather than caching it. This allows users to supply unpublished or modified schemas sourced from, for example, the catalog, at the cost of interoperability (how do WFS clients get them?).

.. note:: At this time, only GML 3.1.1 application schemas are supported.

.. note:: GML 3.1.1 is distributed with GeoServer and does not need to be supplied by the user.


OASIS Catalog
-------------

An `OASIS XML Catalog <http://www.oasis-open.org/committees/entity/spec-2001-08-06.html>`_ is a standard configuration file format that instructs an XML processing system how to process entity references. The GeoServer app-schema resolver uses catalog URI semantics to locate application schemas, so ``uri`` or ``rewriteURI`` entries should be present in your catalog. The optional mapping file  ``catalog`` element provides the location of the OASIS XML Catalog configuration file, given as a path relative to the mapping file, for example::

    <catalog>../../../schemas/catalog.xml</catalog>

Earlier versions of the app-schema plugin required all schemas to be present in the catalog. This is no longer the case. Because the catalog is searched first, existing catalog-based deployments will continue to work as before.


Classpath
---------

Java applications such as GeoServer can load resources from the Java classpath. GeoServer app-schema uses a simple mapping from an http or https URL to a classpath resource location. For example, an application schema published at ``http://schemas.example.org/exampleml/exml.xsd`` would be found on the classpath if it was stored either:

* at ``/org/example/schemas/exampleml/exml.xsd`` in a JAR file on the classpath (for example, a JAR file in ``WEB-INF/lib``) or,
* on the local filesystem at ``WEB-INF/classes/org/example/schemas/exampleml/exml.xsd`` .

The ability to load schemas from the classpath is intended to support testing, but may be useful to users whose communities supply JAR files containing their application schemas.


Cache
-----

If an application schema cannot be found in the catalog or on the classpath, it is downloaded from the network and stored in a subdirectory called ``app-schema-cache`` of the GeoServer data directory.

GeoServer app-schema uses a simple mapping from an http or https URL to local filesystem path. For example, an application schema published at ``http://schemas.example.org/exampleml/exml.xsd`` would be downloaded and stored as ``app-schema-cache/org/example/schemas/exampleml/exml.xsd`` . Note that:

* Only ``http`` and ``https`` URLs are supported.
* Port numbers, queries, and fragments are ignored.

The cache is the last schema source examined when a schema is resolved, so schemas found in the catalog or on the classpath take priority.

.. note:: If your GeoServer instance is deployed on a network whose firewall rules prevent outgoing TCP connections on port 80 (http) or 443 (https), schema downloading will not work. (For security reasons, some service networks ["demilitarised zones"] prohibit such outgoing connections.)

If schema downloading is not permitted on your network, there are three solutions:

* Install and configure GeoServer on another network that can make outgoing TCP connections, start GeoServer to trigger schema download, and then manually copy the ``app-schema-cache`` directory to the production server. This is the easiest option because GeoServer automatically downloads all the schemas it needs, including dependencies.
* Deploy JAR files containing all required schema files on the classpath (see `Classpath`_ above).
* Use a catalog (see `OASIS Catalog`_ above).

