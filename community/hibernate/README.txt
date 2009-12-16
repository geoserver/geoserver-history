================================================================================
=== BUILDING

This project is designed to be compiled as a GeoServer community module.

This module needs some changes in the main trunk that are yet to be approved.
Before building, please apply the patches provided with these jira tickets:
- http://jira.codehaus.org/browse/GEOS-3569


================================================================================
=== DB CONFIGURATION

By default the Hibernate catalog will use an embedded H2 db.

You may redefine the db to be used either by:
- putting a gs-db-config.properties file in the classpath;
- putting a gs-db-config.properties file in your home dir;
- setting the env var GeoServerDBConfigPropertiesFile to point to the
  desired properties file.

The property file should contain these props:

  dataSource.driverClassName
  dataSource.url
  dataSource.username
  dataSource.password
  entityManagerFactory.jpaVendorAdapter.databasePlatform
  entityManagerFactory.jpaVendorAdapter.database

For instance, a sample PostgreSQL setup would be:

  dataSource.driverClassName=org.postgresql.Driver
  dataSource.url=jdbc:postgresql://localhost/gscatalog
  dataSource.username=geosolutions
  dataSource.password=gscatalog

  entityManagerFactory.jpaVendorAdapter.databasePlatform=org.hibernate.dialect.PostgreSQLDialect
  entityManagerFactory.jpaVendorAdapter.database=POSTGRESQL


Be warned not to run the maven tests against your configured DB.

================================================================================
=== Enabling statistics
 
You may want to check if/how the 2nd level cache is working.
You may run geoserver by
    mvn -Dcom.sun.management.jmxremote jetty:run
 and then run JConsole.
You'll find the hibernate cache info in tab MBeans, item hibernate/statistics/GeoServer-Hib Statistics
 
