GeoServer 1.2.0 Alpha README file
---------------------------

The 1.2.0 release is an extension of all the hard work put into creating 
the recently released version 1.1.0. The documentation for instalation and 
execution remains in doc/usr/install.htm. All previous users should consult the
documentation for 1.1.0, as some of the configuration information has changed.
The major changes from a user's point of view is the added configuration
support, with a Struts based configuration tool
(http://localhost:8080/geoserver/Welcome.do).

There were also a number of code organization and design changes surrounding
the configuration portion of GeoServer. Although this will not affect response
time, these changes are intended to improve future maintenance initiatives.
This alpha release does not include dynamic validation configurations,
which will come at a later date.

Thanks to all of you listed below for version 1.1.0. The Refractions team (Jody
- Lead, Richard - UI Creation, David - GeoServer Modifications) would also like
to thank Chris and Steve for their design insights. The Refractions team would 
also like to thank all the other GeoServer developers who helped in our online
IRC brainstorming sessions. The Refractions team would also like to thank our
sponsor, GeoInnovations (www.geoconnections.org),
and our partner, the Open Planning Project. 

Because this is an Alpha release, we are expecting some minor bugs. Please
report such items on the JIRA task tracker, following the instructions included
in the last paragraph of this document. Known issues to date include wfs/wms
services not detecting server ports correctly through firewalls unless the
incoming and outgoing ports match. This is an effect of dynamically testing for
the ports through the Java Servlets.  

Supporting GeoServer
--------------------
If you like GeoServer and are using it, we ask you to kindly add yourself to our
user map at http://www.moximedia.com:8080/imf-ows/imf.jsp?site=gs_users  Doing
so helps ensure our continued funding, as our primary funder wants to know that
people are actually using the project.  The map also serves as a demonstration
of GeoServer as it is built using GeoServer for the queries and MapServer to
display.

New Features and Bug Fixes since 1.1.0
--------------------------------------

* Web based configuration tool

* Redesign of configuration internals, to a more flexible architecture.

* SVG Encoder now handles polygons.

* numDecimals fixed to limit the number of decimals in coordinates of gml.

* verbose fixed (at least for GetFeature the one where it really matters, the others
  need more work).

* maxFeatures makes a comeback, by popular request.

* No more baseUrl configuration parameter, the server now detects where requests
  come from.


Bugs
----------

There are quite a few known bugs right now, see 
http://jira.codehaus.org/secure/BrowseProject.jspa?id=10311 for complete listings.
For the 1.2.0 release we will enumerate all the bugs here.

If you have feature requests or find bugs, please make use of our JIRA task tracker, available thanks to codehaus at http://jira.codehaus.org/secure/BrowseProject.jspa?id=10311.  More support is also available on the new geoserver-users@lists.sourceforge.net, for those who do not want all the developer updates of geoserver-devel@lists.sourceforge.net. List information is available at http://lists.sourceforge.net/lists/listinfo/geoserver-users.  You can also contact us directly at geoserver@openplans.org.



