.. _rest_config_examples_curl:

cURL
====

The examples in this section use the `cURL <http://curl.haxx.se/>`_
utility, which is a handy command line tool for executing HTTP requests and 
transferring files. Though cURL is used the examples apply to any HTTP-capable
tool or library.

Adding a new workspace
----------------------

The following creates a new workspace named "acme" with a POST request::

  curl -v -XPOST -H 'Content-type: text/xml' -d '<workspace><name>acme</name></workspace>' \ 
     http://localhost:8080/geoserver/rest/workspaces

The response should contain the following::
 
  < HTTP/1.1 201 Created
  < Date: Fri, 20 Feb 2009 01:56:28 GMT
  < Location: http://localhost:8080/geoserver/rest/workspaces/acme
  < Server: Noelios-Restlet-Engine/1.0.5
  < Transfer-Encoding: chunked

Note the ``Location`` response header which specifies the location of the 
newly created workspace. The following retrieves the new workspace as XML with a
GET request::

  curl -XGET -H 'Accept: text/xml' http://localhost:8080/geoserver/rest/workspaces/acme.xml

The response should look like:

.. code-block:: xml

   <workspace>
     <name>acme</name>
     <dataStores>
       <atom:link xmlns:atom="http://www.w3.org/2005/Atom" rel="alternate" href="http://localhost:8080/geoserver/rest/workspaces/acme/datastores.xml" type="application/xml"/>
     </dataStores>
     <coverageStores>
       <atom:link xmlns:atom="http://www.w3.org/2005/Atom" rel="alternate" href="http://localhost:8080/geoserver/rest/workspaces/acme/coveragestores.xml" type="application/xml"/>
     </coverageStores>
   </workspace>

Specifying the ``Accept`` header to relay the desired representation of the 
workspace can be tedious. The following is an equivalent (yet less RESTful)
request::

  curl -XGET http://localhost:8080/geoserver/rest/workspaces/acme.xml

Uploading a Shapefile
---------------------

In this example a new datastore will be created by uploading a Shapefile. The 
following uploads the zipped shapefile ``buildings.zip`` and creates a new 
datastore named ``buildings``::

  curl -XPUT -H 'Content-type: application/zip' --data-binary @buildings.zip \ 
     http://localhost:8080/geoserver/rest/workspaces/acme/datastores/buildings/file.shp

The following retrieves the create data store as XML::

  curl -XGET http://localhost:8080/geoserver/rest/workspaces/acme/datastores/roads.xml

.. code-block:: xml

   <dataStore>
     <name>roads</name>
     <enabled>true</enabled>
     <workspace>
       <name>acme</name>
       <atom:link xmlns:atom="http://www.w3.org/2005/Atom" rel="alternate" href="http://localhost:8080/geoserver/rest/workspaces/acme.xml" type="application/xml"/>
     </workspace>
     <connectionParameters>
       <namespace>
         <string>http://acme</string>
       </namespace>
       <url>
         <url>file:/Users/jdeolive/devel/geoserver/1.7.x/data/minimal/data/roads/roads.shp</url>
       </url>
     </connectionParameters>
     <featureTypes>
       <atom:link xmlns:atom="http://www.w3.org/2005/Atom" rel="alternate" href="http://localhost:8080/geoserver/rest/workspaces/acme/datastores/roads/featuretypes.xml" type="application/xml"/>
     </featureTypes>
   </dataStore>

Changing a feature type style
-----------------------------


