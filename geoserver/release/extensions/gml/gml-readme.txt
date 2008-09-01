GEOSERVER 1.3.1+ DATA STORE EXTRAS README

This package contains a GML DataStore implementation that is 
distributed as a separate plug-in.  

Please report any bugs with jira (http://jira.codehaus.org/browse/GEOS). 

Any other issues can be discussed on the mailing list (http://lists.sourceforge.net/lists/listinfo/geoserver-users).

Contains the following data stores:

INSTALLATION

1. Copy included gt2-gml jar included to your 
   GeoServer library directory.  In a binary install this is at 
   [GEOSERVER_HOME]/server/geoserver/WEB-INF/lib/
   In a war install this is [container]/webapps/geoserver/WEB-INF/lib/

2. Restart GeoServer.

GML should now show up as an option in the web admin tool at 
Config -> Data -> DataStores -> New.  Fill out the appropriate params.  For more
information see http://docs.codehaus.org/display/GEOSDOC/GML+DataStore


COMPATIBILITY

This jar should work with any version of GeoServer based on GeoTools 2.5.x.  
Currently this is anything in 1.3.x above 1.3.1.  GeoServer 1.4 should also
be compatible.
