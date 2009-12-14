################################################################################
### BUILDING

This project is designed to be compiled as a GeoServer community module.

################################################################################
### Customizing the db

The catalog will use an embedded h2 db.

If you want to customize it to use another db, you'll either have to:
- put a gs-db-config.properties file in the classpath
- put a gs-db-config.properties file in the current dir
- define a GeoServerDBConfigPropertiesFile env var, pointing to the property files.

The prop file should contains these props:

  dataSource.driverClassName=org.postgresql.Driver
  dataSource.url=jdbc:postgresql://localhost/gscatalog
  dataSource.username=geosolutions
  dataSource.password=gscatalog

  entityManagerFactory.jpaVendorAdapter.databasePlatform=org.hibernate.dialect.PostgreSQLDialect
  entityManagerFactory.jpaVendorAdapter.database=POSTGRESQL


Please make sure not to run the junit tests on your production DB.
The tests also leave the db in an unconsistent state; please clean the db before running the webapp, so that
it will be properly initialized.


################################################################################
### Enabling statistics

You may want to check if/how the 2nd level cache is working.
You may run geoserver by
   mvn -Dcom.sun.management.jmxremote jetty:run
and then run JConsole.
You'll find the hibernate cache info in tab MBeans, item hibernate/statistics/GeoServer-Hib Statistics




