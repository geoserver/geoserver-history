GeoServer 1.2.0-rc1 README file
---------------------------

The 1.2.0-rc1 release is the result of pounding out many bugs by the
GeoServer team.  The documentation for installation and execution is available
online at http://geoserver.sf.net/documentation/1.2.0-rc1. It can also be 
built with the ant document target.  The struts based web interface tool is
coming along nicely, it is available at http://localhost:8080/geoserver/.


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
Ability to edit the login user name and password.

Faster WMS rendering.

Demo page for the web interface.

Fixed errors with JSPCompiler.

Fixed errors with nulls in datastores.

Many small tweaks.



Bugs
----------

We are expecting a few minor bugs and would appreciate bug reports on our
JIRA task tracker http://jira.codehaus.org/secure/BrowseProject.jspa?id=10311

There also may be a major bug with oracle, as we have not
had any users successfully test it with 1.2.  But none of the core developers
has good access to an oracle database, so it's hard for us to debug.
Another known issues to date include wfs/wms services not detecting server 
ports correctly through firewalls unless the incoming and outgoing ports 
match. This is an effect of dynamically testing for the ports through the 
Java Servlets.  Working with styles through the web interface is also a bit
buggy.  We will hit all these and more for 1.2.0
There are quite a few known bugs right now, see 
 for complete listings.
For the 1.2.0 release we will enumerate all the bugs here.

If you have feature requests or find bugs, please make use of our JIRA task 
tracker, available thanks to codehaus at 
http://jira.codehaus.org/secure/BrowseProject.jspa?id=10311.  More support 
is also available on the new geoserver-users@lists.sourceforge.net, for those 
who do not want all the developer updates of 
geoserver-devel@lists.sourceforge.net. List information is available at 
http://lists.sourceforge.net/lists/listinfo/geoserver-users.  You can also 
contact us directly at geoserver@openplans.org.



