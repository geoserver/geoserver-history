GeoServer 1.3.0-beta2 README file
--------------------------------

Welcome to GeoServer 1.3.0-Beta2!  This release contains a number of bug fixes 
and improvements to the 1.3.0-beta release.

Quick to do list for the 1.3.0-beta2 release:

1.  (done) Gabriel's code re-organization
2.  (done) Solve some install issues
3.  (done) Fixing some obvious bugs
4.  (done) Finish CITE WFS testing & fixing
5.  (done) PostGIS 1.0 WKB changes
6.  (done) PostGIS column escaping for PK
7.  (done) Bounds for tables w/o geometries.  
8.  (done) NameSpace problem udigers reported
9.  (done) change how the packaging/installation is done
10. (done) work with UDIGer to get it working with geoserver better
11. (done) minor changes to how the load/save configuration works (bug fixes)

release process/content changes
-------------------------------
1. (done) changed release compression and content.
       binary release is tar.gz  -- for unix people (and winzip/winrar handles it)
       .exe for windows
       .war for containers
       .zip for documentation-only
       
      {thanks to paul ramsey for pointing out .zip and permissions in unix}
      
    NOTE: there were a lot of changes here, please ensure everything is in your release!
    
    
2. (done) removed most .jars from lib/ in the .exe 
3. (done) added in the "binary installer"
4. (done) added documentation to .exe and binary releases
5. (done) documentation changes (index.htmls and such)
6. (done) removed a bunch of stuff from the userbasic config's demo (I deleted these earlier, but someone re-commited them)
7. (done) tried the mapbuilder stuff and added a link to it on the "welcome page" so others can try it out
8. (done) problems with compiling .jsps 


  
We hope it will make it much easier for new developers to 
get acquainted and involved with the GeoServer Project.  The full 
changelog can be viewed at:
http://jira.codehaus.org/secure/BrowseProject.jspa?id=10311&report=changelog


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

Additional  wiki-based documentation is available at:
http://docs.codehaus.org/display/GEOS/Home

Additional Geotools documentation is available at:
http://www.geotools.org/

Commercial Support
------------------
For users who need support faster than the email lists and forums can provide
Refractions Research offers commercial grade support.  They can assist with
installations, customizations, and day to day operations.  If GeoServer is 
lacking certain features compared to commercial WFS's we recommend contacting
Refractions as the money required to implement the needed features may 
be less than the license for proprietary software.  For more 
information see http://refractions.net/geoserver/support.html

Support is also available by axios (http://axios.es/index_en.html) the 
contact person is gabriel (groldan@axios.es). 

Bugs
----------
We've tested the release extensively, but we can not possibly test all possible
servlet container, operating system, and data store combinations.  So please 
make use of our JIRA task tracker at: 
http://jira.codehaus.org/secure/BrowseProject.jspa?id=10311 or email 
geoserver-devel@lists.sourceforge.net or geoserver-user@lists.sourceforge.net.  
It's the only way we can know to fix them.  And
if you can fix the bugs yourself even better, as the source is open and we're
always more than happy to take patches.  



