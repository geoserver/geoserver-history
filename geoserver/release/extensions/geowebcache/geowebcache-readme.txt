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

PLEASE CHECK YOUR LOGFILES IF YOU ENCOUNTER ANY PROBLEMS

See http://geowebcache.org for bugtracker, mailinglists
or join #geoserver on irc.freenode.net
