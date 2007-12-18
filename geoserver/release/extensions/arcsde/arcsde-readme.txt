GEOSERVER 1.6+ DATA STORE EXTRAS README

This package contains an ArcSDE DataStore implementation that is 
distributed as a separate plug-in.

Please report any bugs with jira (http://jira.codehaus.org/browse/GEOS). 

Any other issues can be discussed on the mailing list (http://lists.sourceforge.net/lists/listinfo/geoserver-users).

Contains the following data stores:

INSTALLATION

1. Copy gt2-arcsde jar in to your GeoServer library directory.  In
   a binary install this is [GEOSERVER_HOME]/server/geoserver/WEB-INF/lib/
   In a war install this is [container]/webapps/geoserver/WEB-INF/lib/

2. Restart GeoServer.

Note the ArcSDE jars must be on your classpath for the ArcSDE datastore
to show up on the menu.  For more information see on needed libraries
and parameters, see: http://docs.codehaus.org/display/GEOSDOC/ArcSDE+DataStore

COMPATIBILITY

This jar should work with any version of GeoServer based on GeoTools 2.2.x.  
Currently this is anything in 1.6.x.  
