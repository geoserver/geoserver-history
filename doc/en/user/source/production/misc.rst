.. _production_misc:

Other Considerations
====================

Host your application separately
--------------------------------

GeoServer includes a few sample applications in the :ref:`web_admin_demo`, embedded right in the GeoServer container.  We recommend against doing this for production instances.  To make upgrades and troubleshooting easier, please use a separate container for your application.  It is fine to use one container manager (such as Tomcat or Jetty) to host both GeoServer and your application.

Proxy your server
-----------------

GeoServer can have the capabilities documents properly report a proxy.  You can configure this in the Server :ref:`web_admin_config` of the :ref:`web_admin` and entering the URL of the external proxy in the field labeled ``Proxy base URL``.

Set up clustering
-----------------

Publish your server's capabilities documents
--------------------------------------------

In order to make it easier to find your data, put a link to your capabilities document somewhere on the web. This will ensure that a search engine will crawl and index it.  Also, see the section on :ref:`geosearch_extension` on how to get your data crawled by Google's geo crawler.