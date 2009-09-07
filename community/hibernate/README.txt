================================================================================
=== BUILDING

This project is designed to be compiled as a GeoServer community module.

################################################################################
### EDIT DB CONFIGURATION

Before creating the .war or installing it, you need to configure the DB where the catalog will be stored.
You can find the connection information in
   community/hibernate/src/main/resources/postgis.properties
Please edit the info in the file to match the DB you created for this purpose.


WARNING: Please note that at the moments the tests and the running configuration point to the same db; 
it means that rebuilding or testing the module will trash your data.
The tests also leave the db in an unconsistent state; please clean the db before running the webapp, so that 
it will be properly initialized.
