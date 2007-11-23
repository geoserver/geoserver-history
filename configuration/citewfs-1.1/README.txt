WFS 1.1 Cite Configuration README
---------------------------------

* Running With H2

0. Download the H2 Database from 'http://www.h2database.com' and install it 
   somewhere on your system.

1. Edit 'catalog.xml' and comment out the postgis datastore, and uncomment the
   h2 datastore.

2. Edit 'h2-run.sh', edit the 'H2_HOME' variable to point to your H2
   installation.

3. From the command line run 'h2-run.sh'

4. In a web browser: 

   1. visit 'http://localhost:8082'
   2. Enter 'jdbc:h2:cite' in the 'JDBC URL' field, and click 'Connect'
   3. Copy and paste the contents of 'dataset-sf0-h2.sql' into the text area,
      and click 'Run'
   4. Disconnect by clicking the icon in the upper left hand corner

5. Run GeoServer with the 'citewfs-1.1' configuration.

