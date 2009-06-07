.. _jndi_connection_pools:

JNDI and connection pools
=========================

JNDI (Java Naming and Directory Interface) is a J2EE technology which, among the others, allows the  administrator to provide a ready made connection pool to application running in a web container. Some of the advantages of this approach are:

* the details of the database connection are not shared with the applications
* several applications can share the same connection pool
* it allows the administrator to choose custom, higher performance connection pools, in specific cases (think, for example, a Java application running inside the Oracle web container and accessing the Oracle database)

In the specific case of GeoServer there is a further advantage: GeoServer brings down and re-creates the connection pools every time the configuration is changed, meaning the requests being executed while this happens will most likely fail. If the connection pool is externally managed this does not happen.

The downside of JNDI lies mostly in the procedure to setup the connection pool: it is different for each web container and requires hand editing of some XML files (such as the GeoServer own :file:`web.xml` file).

Retrieving a connection pool from JNDI
=======================================

Starting with GeoServer 1.7.5 some datastore can retrieve their connection from JNDI. To leverage this functionality, use the datastores marked as *JNDI* capable in the drop down list, and provide the name of the connection pool as the JNDI reference parameter using the syntax ``java:comp/env/{poolPath}``

For example, if the pool was bound to the ``jdbc\oralocal`` name, the configuration of the ``jndiReferenceName`` parameter will be ``java:comp/env/jdbc/oralocal``

For further examples look at the :ref:`tomcat_jndi` tutorial.
