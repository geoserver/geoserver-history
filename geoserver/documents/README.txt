GeoServer 1.2.1 README file
---------------------------

The 1.2.1 release is focused on easier installation.  First is a binary
release, consisting of a built in servlet container to run the included
geoserver war, and startup and shutdown scripts for windows and linux.  
Please report any errors to: geoserver-devel@lists.sourceforge.net
The only changes to the source code are two minor fixes to datastores,
a fix in ArcSDE to deal with requests that are out of bounds, and an 
improvement in postgis to speed up spatial requests in large tables.
The readme for 1.2.0 is as follows:

Welcome to GeoServer 1.2.0!  This release has been a long time coming, but we 
think it's well worth the wait.  First off a huge thanks go out to everyone
who helped make this possible, from the core developers to the bug fixers to 
everyone who downloaded the betas and release candidates to help out with 
testing.  The main thrust of this release comes from the hard work of 
Refractions Research, as part of their GeoConnections project of 2003-4.  

With the 1.2.0 release GeoServer becomes a much more useable product, fully 
supporting a variety of data formats with very easy configuration.  The center
of the release is the new web based administration tool, allowing GeoServer to
be completely configured and administered from any browser, eliminating the
need to dig into complex configuration files.  Refractions also added an 
innovative validation engine which ensures the integrity of the backend 
databases using GeoServer for transactions.  

A number of more evolutionary changes are in the release as well, as the long
wait for 1.2.0 has allowed us to fix a number of bugs.  ArcSDE and Oracle have
been tested much more extensively than in previous releases, and transactions
can now be performed against Shapefiles (as long as they are not on a ntfs 
file system, as windows seems to not allow changes occasionally).  PostGIS 
remains as solid as ever, and includes automatic detection of GEOS support, 
allowing much faster spatial queries.  Improvements have also been made to the
generation of FeatureIDs in Oracle and PostGIS, automatically making use of the
best fid candidates.

Oracle is also no longer dependant on the oftentimes buggy and newly deprecated
sdoapi.jar.  Oracle 8i, 9i, and 10g will all work with the new geotools sdo
library.  So now the only jar that we can not distribute with GeoServer is the
classes12, which can be found with oracle installs or at the otn site.  

The integrated Web Map Service (WMS) has also improved, allowing users to 
visualize their data in a variety of formats.  Its speed has increased as data 
is loaded on the fly like the WFS.  Style configuration has been improved, as
well as error reporting.  The GetFeatureInfo operation was also added, bringing
GeoServer closer to full WMS compliance.  The server should be fully compliant
by 1.3.0, with user defined Styled Layer Description (SLD) capabilities.


Supporting GeoServer
--------------------
If you like GeoServer and are using it, we ask you to kindly add yourself to 
our user map at http://www.moximedia.com:8080/imf-ows/imf.jsp?site=gs_users  
Doing so helps ensure our continued funding, as our primary funder wants to 
know that people are actually using the project.  The map also serves as a 
demonstration of GeoServer as it is built using GeoServer for the queries and 
MapServer to display.

Documentation
-------------
Documentation is available online at 
http://geoserver.sourceforge.net/documentation/index.html
The documentation is also available for download from sourceforge, and can
be built from the source downloading using the 'ant document' command.

Commercial Support
------------------
For users who need support faster than the email lists and forums can provide
Refractions Research offers commercial grade support.  They can assist with
installations, customizations, and day to day operations.  If GeoServer is 
lacking certain features compared to commercial WFS's we recommend contacting
Refractions as the money required to implement the needed features may 
be less than the license for proprietary software.  For more 
information see http://refractions.net/geoserver/support.html 

Bugs
----------
We've tested the release extensively, but we can not possibly test all possible
servlet container, operating system, and data store combinations.  So please 
make use of our JIRA task tracker at: 
http://jira.codehaus.org/secure/BrowseProject.jspa?id=10311 or email 
geoserver-devel@lists.sf.net.  It's the only way we can know to fix them.  And
if you can fix the bugs yourself even better, as the source is open and we're
always more than happy to take patches.  



