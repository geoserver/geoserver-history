GeoServer 1.2.0-rc2 README file
---------------------------

The 1.2.0-rc2 release is the result of getting rid of a few pesky bugs from 
rc1, as well as testing for the many changes occuring in GeoTools, our main  
library.  The documentation for installation and execution is available
online at http://geoserver.sf.net/documentation/1.2.0-rc2. It can also be 
built with the ant document target.  The struts based web interface tool is
available at http://localhost:8080/geoserver/.


Supporting GeoServer
--------------------
If you like GeoServer and are using it, we ask you to kindly add yourself to our
user map at http://www.moximedia.com:8080/imf-ows/imf.jsp?site=gs_users  Doing
so helps ensure our continued funding, as our primary funder wants to know that
people are actually using the project.  The map also serves as a demonstration
of GeoServer as it is built using GeoServer for the queries and MapServer to
display.

New Features and Bug Fixes since 1.2-beta
--------------------------------------
Fixed bug with WMS leaving PostGIS connections open.

Nicer messages if JAI is not present.

Stopped styles from being clobbered if they have the same name.

Cleaned up documentation a bit.

WMS dtd's now referenced locally.

Added more Demo files.

Fixed cursor and memory leaks with heavy PostGIS usage.


Bugs
----------

We are moving along to 1.2.0 quite nicely, so would love to hear of any lingering
problems that you may experience.  Please make use of our JIRA task tracker 
http://jira.codehaus.org/secure/BrowseProject.jspa?id=10311

Oracle still has not been tested extensively, as we did not quite have time
to go through the whole install and configuration.  We think things should
work, a few geotools developers have tested.  But for 1.2.0 we will completely
confirm it all ourselves.  Check the wiki for the latest updates at:
http://docs.codehaus.org/display/GEOS/Home

More support is also available on the new geoserver-users@lists.sourceforge.net, 
for those who do not want all the developer updates of 
geoserver-devel@lists.sourceforge.net. List information is available at 
http://lists.sourceforge.net/lists/listinfo/geoserver-users.  You can also 
contact us directly at geoserver@openplans.org.  



