GeoServer 1.2.2 README file
---------------------------

Welcome to GeoServer 1.2.2!  This release marks the culmination of a
number of bug fixes and speed improvements to several datastores.  Most of
the fixes are minor, slight tweaks on the user interface to be a bit more
friendly.  The biggest news is the PostGIS datastore, which now contains
two new options aimed at speed.  The first is Well Known Binary (WKB)
support, which in our tests has increased speed by 50 - 400%, depending on
the size of the geometries.  The PostGIS driver used to read Well Known 
Text, which was slower and could lead to inaccuracies.  As the WKB driver 
has not been extensively tested yet it remains a configuration option, we 
turn it on by default, but if it seems to be causing problems it is easily 
switched off, in the UI and config files.  The second improvement is 
allowing users to tell GeoServer to not be as deadly precise for BBOX 
geometry filters.  Instead of operating against the geometry it will 
operate against the envelope of the geometry.  This leads to a nice speed 
increase for users who do not care so much if the answer is exactly 
correct, such as in the WMS, where you are just displaying the results.  
Again this option is user configurable in the web admin interface.

The Oracle DataStore has also received a potential speed increase, it now 
allows users to make a connection using the JDBC OCI (thick) client 
interface.  Not all clients will have this capability, but when GeoServer 
is installed on the same computer as Oracle then it will automatically 
have the drivers available.  Using them is a good bit faster than going 
through the thin interface.  For more information see the oracle section 
of the documentation.  

One other thing to note is that MySQL is now available as a DataStore for 
GeoServer.  We did not include in this release, as it has not been as well 
tested as the other stores.  But we encourage you to download it and give 
us feedback, it is easily plugged into this release.  It can be found from 
the geoserver home page, or on the sourceforge download page under the 
'GeoServer Extensions' package.


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



