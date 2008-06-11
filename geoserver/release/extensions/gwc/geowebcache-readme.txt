1) Unpack and put the .jar files into <Geoserver webapp>/WEB-INF/lib

2) If you are using GeoServer prior to 1.6.4:
Edit <Geoserver webapp>/WEB-INF/web.xml, find
<mime-mapping>

BEFORE, add
 <servlet-mapping>
   <servlet-name>dispatcher</servlet-name>
   <url-pattern>/gwc/*</url-pattern>
 </servlet-mapping>

3) If GeoServer cannot be reached through http://localhost:8080/geoserver
then GEOSERVER_WMS_URL must be set in web.xml, as follows:

 <context-param>
   <param-name>GEOSERVER_WMS_URL</param-name>
   <param-value>http://sigma.openplans.org:8080/geosearch/wms?request=GetCapabilities</param-value>
 </context-param>


4) By default GeoWebCache will use GEOSERVER_DATA_DIR/gwc to
store cached tiles. If this is not desireable you should set
GEOWEBCACHE_CACHE_DIR in web.xml:

 <context-param>
   <param-name>GEOWEBCACHE_CACHE_DIR</param-name>
   <param-value>/tmp</param-value>
 </context-param>


USING THE EXTENSION
This extension works just like the standalone version of GeoWebCache,
the main difference is the URL you use to access it.

If you access GeoServer through http://localhost:8080/geoserver and you 
have the topp:states dataset loaded (part of the release) you can try
http://localhost:8080/geoserver/gwc/service/ve?layers=topp:states&quadkey=02

Note that if you find GeoWebCache documentation that refers to 
http://localhost:8080/geowebcache/*
the equivalent, when using the extension, is
http://localhost:8080/geoserver/gwc/*

PLEASE CHECK YOUR LOGFILES IF YOU ENCOUNTER ANY PROBLEMS

See http://geowebcache.org for bugtracker, mailinglists
or join #geoserver on irc.freenode.net
