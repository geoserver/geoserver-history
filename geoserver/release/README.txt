GeoServer 1.5.4
---------------
GeoServer 1.5.4 is a bugfix and minor feature release. It provides a bunch
of KML and templates related fixes, support for Arabic labeling, propper Google projection
support,
validating GML 2.1.2.1 schemas, and a lot more.

Full changelog is here:
http://jira.codehaus.org/secure/ReleaseNote.jspa?projectId=10311&styleName=Html&version=13609

This release is based off Geoserver 2.3.5

GeoServer 1.5.3
---------------
GeoServer 1.5.3 contains some minor bug fixes relating to GeoRSS and KML. The
changlog can be found here:

http://jira.codehaus.org/secure/ReleaseNote.jspa?projectId=10311&styleName=Html&version=13649

This release is based off of Geotools 2.3.4.

Geoserver 1.5.2
---------------
Geoserver 1.5.2 is a bugfix and minor feature release... you should
not underestimate. The changelog contains a whopping 77 issues fixed,
between improvements and fixes.

Major improvements include:
- Full support for 256 color images, thru the new png8 and gif output formats,
  as well as very good user provided palette support. Generated image sizes
  go down 2-4 times, and the quality is the same as ever in most maps (and
  if you really want it both very fast and good, just provide a custom palette so
  that GeoServer does not have to devise one at each request);
- Full templates for GetFeatureInfo as well as improvements in the KML ones;
- GeoRSS output;
- So many minor improvement/fixes in the KML output it's hard to provide a list (have a
  look in the changelog);
- GeoServer can now serve your static files as well, just put them into the "www"
  subdirectory of the GeoServer data directory. This means your data dir can now
  host your full application, and you don't need to play anymore with proxies
  when setting up a WFS-T javascript client such as OL or MapBuilder.
- Revamped demo page, with more mashup demos, and improved preview page that
  lets you try out each of the WMS output formats;
- Various Oracle datastore improvements;
- Extended character now working from the datastore up to the map (tested with a Chinese map,
  look here: http://jira.codehaus.org/secure/attachment/28200/wms-chinese.png);
- Sorted elements in WMS GetCapabilities preview, so that clients can show an easily searchable 
  list to the user;
- Various WMS GetLegendGraphics improvements, leading to a better looking output and better labels
  (thanks Saul Farber for working on this)

Full changelog is here:
http://jira.codehaus.org/secure/ReleaseNote.jspa?projectId=10311&styleName=Html&version=13503

This release is based off Geoserver 2.3.3


GeoServer 1.5.1
---------------

The next minor release of GeoServer 1.5.x. The focus for this release has been
to bring some interesting new features into the fold:

- Chinese translation
- Portuguese translation
- OpenLayers map preview
- KML legend support
- KML superoverlay / region support
- WMS layer grouping

And as always numerous bug fixes. The entire issue log can be found here:

http://jira.codehaus.org/secure/IssueNavigator.jspa?reset=true&&fixfor=13320&pid=10311&sorter/field=issuekey&sorter/order=DESC

This release is based off of geotools 2.3.2.

GeoServer 1.5.1-RC1
-------------------

The first release of the 1.5.1 stream. This release contains a number of 
interesting new features such as:

- Chinese translation
- Portuguese translation
- OpenLayers map preview
- KML legend support
- KML superoverlay / region support
- WMS layer grouping

And as always numerous bug fixes. The entire issue log can be found here:

http://jira.codehaus.org/secure/IssueNavigator.jspa?reset=true&&pid=10311&fixfor=13320&sorter/field=issuekey&sorter/order=DESC

This release is based off of geotools 2.3.2-SNAPSHOT ( revision = 25651, tag =
geoserver-1.5.1-RC1 )

GeoServer 1.5.0
-------------------

The first official release of the 1.5 series of GeoServer. This
release is a major milestone for the GeoServer. This version of
GeoServer brings some exciting new functionality like:

- Coverage support and an implementation of Web Coverage Service
- WMS raster support
- WFS and WMS performance improvments
- KML improvements

And as always a heap of bug fixes and other . The entire issue log for this release can be found here:

http://jira.codehaus.org/secure/IssueNavigator.jspa?reset=true&&pid=10311&fixfor=12975&fixfor=13177&fixfor=13176&fixfor=12974&fixfor=13178&fixfor=12973&fixfor=12870&sorter/field=priority&sorter/order=DESC

This release is based off of Geotools 2.3.1 ( tag = 2.3.1 )

See 'RUNNING.txt' for instructions on how to run GeoServer.

GeoServer 1.5.0-RC4
-------------------

The fourth release candidate for the 1.5 series of GeoServer.
The noteable bug fixes in this release include:

- Inline features in a GET request
- Styles parameter optional with KML reflector
- SLD editor aware of context path

The complete issue log can be found here:

http://jira.codehaus.org/secure/IssueNavigator.jspa?reset=true&pid=10311&fixfor=13177

Based on GeoTools 2.3.1-SNAPSHOT ( tag = geoserver-1.5.0-RC4 )

GeoServer 1.5.0-RC3
--------------------

The third and final release candidate for the 1.5 series of GeoServer.
The noteable features / bug fixes in this release include:

- Rotation in world image files
- Directory datastore improvements for easy management of shapefiles
- UI error reporting with missing files, file permissions, etc...
- PostGIS estimated_extent support for large datasets
- Multi geometry with inline SLD features
- WMS reflector for making wms requests easier
- SLD parsing improvements 
- Logging cleanup

The complete issue log can be found here:

http://jira.codehaus.org/secure/IssueNavigator.jspa?reset=true&pid=10311&fixfor=13176

Based on GeoTools 2.3.1-SNAPSHOT ( tag = geoserver-1.5.0-RC3 )

GeoServer 1.5.0-RC2
---------------------

This is the second release candidate for the 1.5 sereies of GeoServer.
Major features / improvements of this release include:

- Bug fixes
- Kml returns proper PNG files
- Kml strips out illegal characters now
- Improved error messages
- UI improvements
- Inline features are back
- Links to old homepage updated
- Performance optimization


The issue log for this release is here:

http://jira.codehaus.org/secure/IssueNavigator.jspa?reset=true&pid=10311&fixfor=12974

Based on GeoTools 2.3.1-SNAPSHOT


GeoServer 1.5.0-RC1
---------------------

This is the first release candidate for the 1.5 sereies of GeoServer.
Major features / improvements of this release include:

- Many bug fixes
- Reduced download size
- Reorginization of some UI components
- More hand testing and tutorials
- Performance optimization


The issue log for this release is here:
http://jira.codehaus.org/secure/IssueNavigator.jspa?reset=true&pid=10311&fixfor=13178

Based on GeoTools 2.3.1-SNAPSHOT


GeoServer 1.5.0-beta2
---------------------

This is the second beta release for the 1.5 sereies of GeoServer.
Major features / improvements of this release include:

- Many bug fixes
- The ability to locate raster files outside of the data_dir


The issue log for this release is here:
http://jira.codehaus.org/secure/IssueNavigator.jspa?reset=true&pid=10311&fixfor=12973

Based on GeoTools 2.3.1-SNAPSHOT

NOTES:
1.In order to get more performances, you should have a complete JAI/ImageIO install (with native interfaces too) into your JRE.
Moreover, we suggest to use the latest daily distributions of JAI 1.1.4 and ImageIO 1.2.
2.Very few CITE Tests are still failing. This distribution passes most of the WMS,WFS and WCS CITE Tests.

GeoServer 1.5.0-beta1
---------------------

This is the first beta release for the 1.5 sereies of GeoServer.
Major features / improvements of this release include:

- A lot of Bug fixed against 1.4 series
- JAI/ImageIO for Map Producers
- WCS 0.4 (2D Coverages)
- WMS Raster support
- KML Raster support


The issue log for this release is here:
http://jira.codehaus.org/secure/IssueNavigator.jspa?reset=true&pid=10311&fixfor=12870

Based on GeoTools 2.3.1-SNAPSHOT

NOTES:
1.In order to get more performances, you should have a complete JAI/ImageIO install (with native interfaces too) into your JRE.
Moreover, we suggest to use the latest daily distributions of JAI 1.1.4 and ImageIO 1.2.
2.Very few CITE Tests are still failing. This distribution passes most of the WMS,WFS and WCS CITE Tests.

GeoServer 1.4.0-RC1
-------------------

This is the first release candidate for the 1.4 series of GeoServer.
Major features / improvements of this release include:

- WMS GetMap filter support
- More robust KML point styling
- Integration with OSCache, please see: http://docs.codehaus.org/display/GEOSDOC/Caching
- Numerous bug fixes and improvements


The issue log for this release is here:
http://jira.codehaus.org/secure/IssueNavigator.jspa?reset=true&pid=10311&fixfor=12666

Based on GeoTools 2.2.1

GeoServer 1.4.0-M2-WCS
----------------------

Major features / improvements of this release include:

- WCS 0.4 (2D Coverages)
- WMS Raster support
- KML Raster support

The issue log for this release is here:

Base on Geotools 2.3.0-M0 (tag = 2.3.0-M0)
        JAI      1.1.4+   (https://jai.dev.java.net/)
        ImageIO  1.1+     (https://jai-imageio.dev.java.net/)

GeoServer 1.4.0-M2
------------------

Major features / improvements of this release include:

- Upgraded MapBuilder demo version
- WMS Base-map option
- GetFeatureInfo now supports reprojection
- KML Reprojection

The issue log for this release is here:
http://jira.codehaus.org/secure/IssueNavigator.jspa?reset=true&pid=10311&fixfor=12950

Base on Geotools 2.2.0 (tag = 2.2.0)


GeoServer 1.4.0-M1
------------------

Major features / improvements of this release include:

- GetMap factoring out to spring extension point
- Developer Documentation 

Along with numerous ui bug fixes coming out of the move to Spring.
Special thanks to the following people for contributions / bug fixing:

- Saul Farber
- Alessio Fabiani

The issue log for this release is here:

http://jira.codehaus.org/secure/IssueNavigator.jspa?reset=true&mode=hide&sorter/order=ASC&sorter/field=priority&pid=10311&fixfor=12710

Base on Geotools 2.2.0 (tag = 2.2.0)

GeoServer 1.4.0-M0
------------------

The issue log for this release is here:

http://jira.codehaus.org/secure/IssueNavigator.jspa?reset=true&pid=10311&fixfor=11555

The first release of the GeoServer 1.4.x series. This development 
stream represents a major refactoring of the core codebase. The 
purpose of which is to move to a more component-oriented framework. 

The major changehas been the move to the spring framework. Spring 
is a IOC container that GeoServer now loads to bootstrap itself and 
manages component dependencies in the system.

Based on geotools 2.2.x (tag = geoserver-1.4.0-M0)

Geoserver 1.3.2
---------------
SLD Wizard to create SLD files with a nice user interface.
Up to 60-70% speed increases in WMS rendering.
Built on GeoTools 2.2.x
Including KML/KMZ output formats for WMS.
PDF output format for WMS.
Caching headers for WMS (http://jira.codehaus.org/browse/GEOS-454).
Support for more math functions in filters.
Many big fixes and improvements.

The change log for this release is here:
http://jira.codehaus.org/secure/IssueNavigator.jspa?reset=true&pid=10311&fixfor=12332


Geoserver 1.3.1
---------------
Fully tested with GeoTools 2.2.x. This release also contains KML and KMZ support for 
data viewing in Google Earth. For more information visit 
http://docs.codehaus.org/display/GEOSDOC/Google+Earth 


Geoserver 1.3.1 beta
--------------------
The main adition to this release is the move from Geotools 2.1.x to Geotools 2.2.x
Bug fixes and new features listed in Jira will be added once this beta version has been tested
by the community for a couple of days.

The build.xml file has also been reformatted to reflect the changes in the data_dir system, and it 
has been cleaned up.

Geoserver 1.3.0
---------------

The issue log for this release is here:

http://jira.codehaus.org/secure/IssueNavigator.jspa?reset=true&pid=10311&fixfor=12106

This is the official 1.3.0 release. 1.3.0 Represents a major milestone for the 
GeoServer project. Months of bug fixes and patches have allowed us to acheive 
our goal of a stable release. Much thanks to everyone who helped out with 
contributions and bug reports.

Based on the geotools 2.1.1 release (tag = 2.1.1)

Geoserver 1.3.0 PR1
-------------------

The issue log for this release is here:

http://jira.codehaus.org/secure/IssueNavigator.jspa?reset=true&pid=10311&fixfor=12106

This is the first 1.3.0 feature complete release. Changes from RC7 include bug
fixes and documentation.

Based on the geotools 2.1.1.PR0 release (tag = 2.1.1.PR0)

Geoserver 1.3.0 RC7
-------------------

The issue log for this release is here:
http://jira.codehaus.org/secure/IssueNavigator.jspa?reset=true&pid=10311&fixfor=12105

The major changes since RC6 have been:

* Reorganization of the data directory structure
* SLD Validation
* Lenient CRS Transforms
* Logging
* Http Unit Testing

Based on the geotools 2.1.x branch (tag = geoserver-1.3.0.RC7)

Geoserver 1.3.0 RC6
-------------------

The issue log for this release is here:
http://jira.codehaus.org/secure/IssueNavigator.jspa?reset=true&pid=10311&fixfor=12080

The major changes since RC5 have been:

* support for Java 5
* option to output verbose GML (boundedBy element per feature)
* handling of form-based POST requests
* Oracle spatial support for MultiPoints
* Fast SVG Renderer - support for heterogeneos geometry types

Based on the geotools 2.1.x branch (tag = geoserver-1.3.0.RC6)

Geoserver 1.3.0 RC5
-------------------

The issue log for this release is here:
http://jira.codehaus.org/secure/IssueNavigator.jspa?reset=true&pid=10311&fixfor=12031

The major changes since RC4 have been:
* strategy -- partial buffer. Now the default strategy. Please TEST this one. If you are 
    having weird errors, set WEB-INF/web.xml serviceStratagy back to SPEED.
* NullPointerException in XMLConfigWriter
* GIF - improvments
* Cannot turn off Antialias in config
* GIF -- no feature gives invalid image
* tomcat error re-writing
* better error reporting
* easier upgrade path
* Website upgrade
* Performed more testing with Java 1.5, still not fully complete
* Ant buildfile no work with 1.5.3
* Error while trying to "Apply" or "Load" data config changes


Geoserver 1.3.0 RC4
-------------------

The issue log for this release is here:
http://jira.codehaus.org/secure/IssueNavigator.jspa?reset=true&pid=10311&fixfor=11906

The major changes since RC3 have been:
* SVG Support with full support for sld
* Map builder preview with custom configuration works
* Writing logs to disk
* GetLegendGraphic will now find the required style in a multiple styles remote SLD document
* Color Scheme
* Indexed Shapefile support
* Compression/Decompression of remote SLD documents
* Data Store Editor User Interface Improvments
* XML request character set detection
* PostGIS support - more intuitive error messages regarding permissions
* Shapefile support - url handling
* Linux support - start / stop scripts
* Developer Documentation



Geoserver 1.3.0 RC4.SC1
-------------------

The issue log for this release is here:
http://jira.codehaus.org/secure/IssueNavigator.jspa?reset=true&pid=10311&fixfor=11906

The major changes since RC3 have been:
* Color Scheme
* Indexed Shapefile support
* Compression/Decompression of remote SLD documents
* Data Store Editor User Interface Improvments
* XML request character set detection
* PostGIS support - more intuitive error messages regarding permissions
* Shapefile support - url handling
* Linux support - start / stop scripts
* Developer Documentation

Geoserver 1.3.0 RC3
-------------------

The issue log for this release is here:
http://jira.codehaus.org/secure/IssueNavigator.jspa?reset=true&pid=10311&fixfor=11901

The major changes since RC2 have been:
* You can now have the same FeatureType name for different datasets (each in different namespaces).
* Large improvements to GIF format support
* Improved performance for GetFeature
* New output format for WFS: SHAPE-ZIP  (shapefile-in-zip-file)
* FeatureType summary page with link to Mapbuilder demo for each layer
* Many improvements to the Renderer, labeling, etc...
* Currently compatible with Geotools 2.1.x branch and Geotools 2.2 (trunk)
* added "Strict CITE conformance" checkbox to WFS because of problems in the way CITE test verifies the GetCapabilitites document
* fixed VM crash for labeling (linux only)
* <PropertyIsLike> now optimized in PostGIS and Oracle (SQL92 implementation)
* Labeling option (SLD) for grouping, priority, overlapping
* Useability changes & testing
* Now based on StreamingRenderer/GTRenderer interfaces

Based on the geotools 2.1.x branch (tag = for_geoserver_1_3_rc3)

GeoServer 1.3.0-RC2 README file
--------------------------------

The issue log for this release is here:
http://jira.codehaus.org/secure/IssueNavigator.jspa?reset=true&pid=10311&fixfor=11771

This release is based on the geotools 2.1.0 release (2.1.x svn branch).

1. JAI should no longer be required - this time I mean it.
      If you get an error regarding a security issue (because the jars a 'sealed')
      please leave a message on the geoserver-devel mailing list.  This will only
      occur if you installed JAI.  You can do one of the following:
          a) remove from the lib/ directory "jai-core-1.1.3-alpha.jar",
              "jai_imageio-1.1-alpha.jar" and "jai_codec-1.1.3-alpha.jar".
          b) uninstall JAI
      I've had one report of this problem and I think I solved it.  I
      have been unable to reproduce the issue.
      
2. I believe I fixed the error-logging issue for tomcat users.  Please
   tell me if the problem still occurs.
   
3. You can now use <Function> elements in your WFS <Filter>

4. KML (google map) output supported (james macgill)

5. Several other bugs fixed in geoserver and geotools


If you look at the roadmap for the next few releases 
(http://jira.codehaus.org/browse/GEOS?report=com.atlassian.jira.plugin.system.project:roadmap-panel)
you should see we are very rapidly approaching whats required for the 1.3.0 release.

The geoserver-devel mailing list has been "bursting at the seams" recently with 
good ideas and discussions for whats going to be happening over the longer term.

GeoServer 1.3.0-RC1 README file
--------------------------------
0.  No longer relies on JAI!

1.  Moved to Geotools 2.1.x branch
2.  GetCapabilities (WFS and WMS) better reflect the added functionality + other fixes
3.  Better SLD support and error reporting
4.  Better handling of CRS xformations
5.  Support for Min/MaxScaleDenominator in SLD
6.  Better type system for <Filter>
7.  Documentation updates
8.  FeatureReader/FeatureCollection merge
9.  New PNG writer (Jai-independent)
10. GIF writer improvements
11. Transparency support improvements
12. non-JAI JPEG writer
13. Memory leak fixed
14. WMS speed improvements
15. Data/Time problem fixed
16. Other bug fixes, improvements, and testing


GeoServer 1.3.0-beta4 README file
--------------------------------

Geoserver WFS is in excellent shape, and the WMS has improved
significantly.

* (WMS) Better label rendering
* (WMS) Better renderer performance
* (WMS) Better reprojection support
* (WMS) SLD-POST
* (WMS) SLD-InlineFeatures
* (WMS) GetMap POST
* (WMS) SLD-POST/GetMap POST schema validation
* (WMS) Parser error checking improvements

* (FILTER) added autogeneration of custom <Function name="..">
* (FILTER) parser improvements
* (FILTER) smarter/faster processing

* (PostGIS) minor bug fixes
* (PostGIS) allow use of VIEWs as FeatureTypes

* (CONFIG) GEOSERVER_DATA_DIR improvements
* (WAR)    WAR generation improvements


Plus a large set of bug fixes and testing.

NOTE: This release is based on the "for_Geoserver-1.3.0beta4" tag.

GeoServer 1.3.0-beta3 README file
--------------------------------

Major Changes
-------------
1. Geoserver now passes all WFS and WMS CITE tests
2. Geoserver WMS now does reprojection
     You can use "&SRS=NONE" if you dont want to do any reprojection.
3. Geoserver now allows you to define your geoserver data directory
    (previously called "GEOSERVER_HOME") - see below
4. Added a bunch of "helpers" for SRS (spatial referencing system) since people will have to define their SRSs now
5. Full SLD schema validation support



Minor Changes
-------------
1. bug fixes
2. set of lite renderer improvements
3. added a WFS lock tutorial (http://docs.codehaus.org/display/GEOS/Feature+Locking)
4. upgraded to latest geotools


You can define your geoserver data directory in three ways:

1."GEOSERVER_DATA_DIR" system property.
   this will most likely have come from "java -DGEOSERVER_DATA_DIR=..."
or from you web container's GUI
2. "GEOSERVER_DATA_DIR" in the web.xml document:
     <context-param>
            <param-name>GEOSERVER_DATA_DIR</param-name>
            <param-value>c:\myGeoserverData</param-value>
     </context-param>
3. Defaults to the old behavior - ie. the application root - usually "server/geoserver" in your .WAR.




To make a new one of these data directories, just:

1. create the data directory
2. copy "data/" from an already running geoserver
3. create a "WEB-INF/" directory
5. copy "catalog.xml" and "services.xml" into the WEB-INF/ directory


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



