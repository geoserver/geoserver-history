$Id$

GeoServer 1.2.4 README file
---------------------------

Whew!  This release was a lot more difficult than expected.  The lesson 
learned is that it is in fact _possible_ to release software from an 
internet care, but it is certainly not recommended ;)  Yes, thats right, 
this release is coming you straight from the Village Novo internet cafe in
beautiful Salvador, Brazil.  This is what happens when your ac adapter breaks
in Brazil.  The release already felt late before the adapter broke, so it felt
worthwhile to get all the latest bug fixes and documentation out.  

The biggest improvement of this release is our completely revised user 
documentation.  We are hoping to blow away the conception that it is hard to 
find good documentation with open source.  Though yes, really this should have
been done far before the 1.2.4 release.  But please give us feedback on typos,
what doesn't make sense, and suggestions for improvement.  We have also made a
good effort to explain more about the roots and goals of the GeoServer project,
so please tell us what you think about it all.  

Though we thought that this release would be all documentation, we got some 
great contributions from our users.  Artie Konin is contributor of the 
release, with really nice bug fixes for internationalization.  Though we think
we may have a bit more work to do to get things exactly right, we are doing 
better.  The other excellent contribution is from Sergio Baños Calvo, a member
of AGIL (Asociación para la promoción del GIS Libre).  He translated the 
web admin tool into Spanish.  This came in right before the release, so we 
would love feedback.  We _think_ that if you start up GeoServer on a computer
set to Spanish the web admin tool should automatically come up in Spanish.  We
will have more details in the future.  

Working through the documentation also lead us to finally fix a few nasty bugs
users have been complaining about for awhile.  The first is the infamous 
deleting shapefile bug, where adding shapefiles through the web admin tool 
would often blow away the data.  This should now be fixed, and details are in
the new user docs.  The other one is the DescribeFeatureType generation - we
designed GeoServer to allow users to customize their responses, but it turns
out that the behaviour never made it all the way out.  So we have fixed that,
you now have greater control over your featureType, and can customize the 
schema either through the web admin tool, or by editing the schema.xml file 
directly.  We also improved the featureType editor, so editing the attributes
is a breeze.  And there were a few other minor bugs fixed.  The full changelog
can be viewed at:
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
The documentation is also available for download from sourceforge, can
be built from the source downloading using the 'ant document' command, and
should be included in the binary releases in the documents folder, just 
point your web browser at the index.html file in that folder..

Bugs
----------
We've tested the release extensively, but we can not possibly test all possible
servlet container, operating system, and data store combinations.  So please 
make use of our JIRA task tracker at: 
http://jira.codehaus.org/secure/BrowseProject.jspa?id=10311 or email 
geoserver-devel@lists.sf.net.  It's the only way we can know to fix them.  And
if you can fix the bugs yourself even better, as the source is open and we're
always more than happy to take patches.  



