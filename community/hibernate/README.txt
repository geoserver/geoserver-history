================================================================================
=== BUILDING

This project is designed to be compiled as a GeoServer community module.

################################################################################
### SOURCE FILES
# This step will be obsoleted when the module will be imported into the GeoServer svn repository

In order to make it work, put it into trunk/src/community/.
You'll have:
trunk/src/community/
|-- hibernate
|   |-- src
|   `-- target
...

################################################################################
### EDIT COMMUNITY pom.xml
# This step will be obsoleted when the module will be imported into the GeoServer svn repository

Then, edit your community/pom.xml and add the lines

    <profile>
      <id>hibernate</id>
      <modules>
        <module>hibernate</module>
      </modules>
    </profile>

With this change, you added the hibernate profile so that, enabling it, the hibernate module will be compiled.

################################################################################
### EDIT WEB pom.xml
# This step will be obsoleted when the module will be imported into the GeoServer svn repository

We also have to add the hibernate module to the final .war file.
Edit the src/web/app/pom.xml file, and add this profile to the profile list:

    <profile>
      <id>hibernate</id>
      <dependencies>
        <dependency>
          <groupId>org.geoserver.community</groupId>
          <artifactId>hibernate</artifactId>
          <version>${project.version}</version>
        </dependency>
      </dependencies>
    </profile>

################################################################################
### EDIT DB CONFIGURATION

Before creating the .war or installing it, you need to configure the DB where the catalog will be stored.
You can find the connection information in
   community/hibernate/src/main/resources/postgis.properties {TODO: we'll have to change this filename}
Please edit the info in the file to match the DB you created for this purpose.

