README for GeoSearch Extension to GeoServer

This package contains a geosearch service extension that is 
distributed as a separate plug-in.  

Please report any bugs with jira (http://jira.codehaus.org/browse/GEOS). 

Any other issues can be discussed on the mailing list (http://lists.sourceforge.net/lists/listinfo/geoserver-users).

Contains the following data stores:

INSTALLATION

1. Copy included geosearch jar included to your GeoServer library directory.  
   In a binary install this is at: 

     [GEOSERVER_HOME]/server/geoserver/WEB-INF/lib/

   In a war install this is [container]/webapps/geoserver/WEB-INF/lib/

2. Restart GeoServer.

GeoSearch sitemaps should now show up on your web server at
http://<host:port/context>/rest/<namespace:layer>/sitemap.xml.  If a layer is
not showing up there, ensure that geosearch indexing is enabled in the
featuretype configuration.  You can submit these sitemaps to Google via the
normal Google webmaster tools (www.google.com/webmasters/tools/)  For more
information see http://geoserver.org/display/GEOS/GeoSearch+Module .


COMPATIBILITY

This jar should work with any version of GeoServer 1.7.4 or later.  1.7.0 and
up may work with the rest service support extension.
