This folder is to allow users wishing to test geoserver against the CITE
conformance suite to get their geoserver install up and running quickly.
You could do it all by hand, it's just a long and tedious process, setting
up the right directory structure, postgis tables, and schema and info 
files.  So we recommend using these shortcuts.  This document assumes
you've successfully built and run GeoServer.

You'll need to have completed the geoserver install, with a postgis
database.  The cite_data.sql script will create all the needed tables
and fill them with data needed to pass the tests.  The easiest way to
use these is to create a database named cite and a user named cite, 
and give that user permissions on the database.  You can create a user
with the createuser utility in pgsql/bin.  And be sure to run the 
postgis scripts to initialize that db for postgis.  Then run 
'psql -f cite_data.sql cite', and the script should do all the work.


If you want to just use your current database that's fine too, but 
you'll have to edit a few of the files.  The cite_data.sql should 
replace cite with your user name.  And then all the info.xml files of 
the featureTypes also need to be changed to use the username, password, 
and database that you'd like.  Perhaps in the future we'll provide some 
nice scripts to do just that, but for now you're stuck with hand editing.

The next step is the featureTypes.  You should back up your current
misc/data/featureTypes directory.  Then go to misc/data, and the best
bet is probably to just remove the featureTypes directory (make sure
you backed it up!), and untar and gunzip the citeTypes.tar.gz file
in the misc/data directory.  This will create all the appropriate 
featureTypes to run against the CITE test engine, with cite as the
database name, user and password.  As stated before if you want to 
change these you'll have to change them all individually by hand.

The final step is the configuration.xml file.  You can just copy the
one in this directory to your misc/documents folder, changing the URL
tag to the location of your server.  Or you can just copy the two 
namespace elements to your configuration.xml file.

The trans_reset.sql file is not needed till after the cite suite completely 
tests your instance.  The test engine modifies some of the files, and
if the suite is run again then some of the transaction tests will fail
if this script is not run.  It is run the same way cite_data.sql is:
'psql -f trans_reset.sql cite', where cite is the name of your database,

