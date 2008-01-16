Here's the summary:
1. get postgres and postgis
2. create a "spearfish" superuser with spearfish password: 
   createuser -s -d -P spearfish
3. create a "spearfish" database: 
   createdb spearfish -U spearfish
4. uncompress data_dir/spearfish.sql.gz
   gzip -d spearfish.sql.gz
5. restore the db:
   psql -U spearfish spearfish < spearfish.sql
6. copy all the jars in this extension to geoserver/WEB-INF/lib
7. run geoserver specifying a data dir pointing to the versioning data dir found
   in this demo. 
   If the versioning data dir has been unpacked to ${versioning_data_dir} (you'll
   substituite with your path), the following may work:
   - modify geoserver/WEB-INF/web.xml, uncomment the GEOSERVER_DATA_DIR
     and change it so that it reads:
     <context-param>
       <param-name>GEOSERVER_DATA_DIR</param-name>
        <param-value>${versiong_data_dir}</param-value>
    </context-param> 
   - set and environment variable pointing at the versioning data dir so that
     it enters the web container environment
       - SET GEOSERVER_DATA_DIR=${versiong_data_dir} (on windows)
       - EXPORT GEOSERVER_DATA_DIR=${versiong_data_dir} (on linux)
       
8. start geoserver, you should find only the spearfish data set, in versioning
   configuration. Also have a look at the sample requests for ideas on the
   versioning WFS protocol.
   
For more information you can refer to http://docs.codehaus.org/display/GEOS/Versioning+WFS+-+Extensions or ask on the GeoServer users mailing list.
