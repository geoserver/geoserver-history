#!/bin/sh
# -----------------------------------------------------------------------------
# Start Script for GEOSERVER
#
# $Id: startup.sh,v 1.4 2003/09/19 15:10:30 cholmesny Exp $
# -----------------------------------------------------------------------------

# Make sure prerequisite environment variables are set
if [ -z "$JAVA_HOME" ]; then
  echo "The JAVA_HOME environment variable is not defined"
  echo "This environment variable is needed to run this program"
  exit 1
fi
if [ ! -r "$JAVA_HOME"/bin/java ]; then
  echo "The JAVA_HOME environment variable is not defined correctly"
  echo "This environment variable is needed to run this program"
  exit 1
fi
# Set standard commands for invoking Java.
_RUNJAVA="$JAVA_HOME"/bin/java

if [ -z "$GEOSERVER_HOME" ]; then
  echo "The GEOSERVER_HOME environment variable is not defined"
  echo "This environment variable is needed to run this program"
  exit 1
fi
if [ ! -r "$GEOSERVER_HOME"/bin/startup.sh ]; then
  echo "The GEOSERVER_HOME environment variable is not defined correctly"
  echo "This environment variable is needed to run this program"
  exit 1
fi
CATALINA_HOME="$GEOSERVER_HOME"/lib/catalina


GEOSERVER_PATH="$CATALINA_HOME"/bin/bootstrap.jar
GEOSERVER_PATH="$GEOSERVER_PATH":"$CATALINA_HOME"/server/lib/catalina.jar
GEOSERVER_PATH="$GEOSERVER_PATH":"$CATALINA_HOME"/server/lib/servlet-cgi.jar
GEOSERVER_PATH="$GEOSERVER_PATH":"$CATALINA_HOME"/server/lib/servlets-common.jar
GEOSERVER_PATH="$GEOSERVER_PATH":"$CATALINA_HOME"/server/lib/servlets-default.jar
GEOSERVER_PATH="$GEOSERVER_PATH":"$CATALINA_HOME"/server/lib/servlets-invoker.jar
GEOSERVER_PATH="$GEOSERVER_PATH":"$CATALINA_HOME"/server/lib/servlets-manager.jar
GEOSERVER_PATH="$GEOSERVER_PATH":"$CATALINA_HOME"/server/lib/servlets-snoop.jar
GEOSERVER_PATH="$GEOSERVER_PATH":"$CATALINA_HOME"/server/lib/servlets-ssi.jar
GEOSERVER_PATH="$GEOSERVER_PATH":"$CATALINA_HOME"/server/lib/servlets.webdav.jar
GEOSERVER_PATH="$GEOSERVER_PATH":"$CATALINA_HOME"/server/lib/jakarta-regexp-1.2.jar
GEOSERVER_PATH="$GEOSERVER_PATH":"$CATALINA_HOME"/lib/naming-factory.jar
GEOSERVER_PATH="$GEOSERVER_PATH":"$CATALINA_HOME"/common/lib/crimson.jar
GEOSERVER_PATH="$GEOSERVER_PATH":"$CATALINA_HOME"/common/lib/jasper-compiler.jar
GEOSERVER_PATH="$GEOSERVER_PATH":"$CATALINA_HOME"/common/lib/jasper-runtime.jar
GEOSERVER_PATH="$GEOSERVER_PATH":"$CATALINA_HOME"/common/lib/jaxp.jar
GEOSERVER_PATH="$GEOSERVER_PATH":"$CATALINA_HOME"/common/lib/jndi.jar
GEOSERVER_PATH="$GEOSERVER_PATH":"$CATALINA_HOME"/common/lib/naming-common.jar
GEOSERVER_PATH="$GEOSERVER_PATH":"$CATALINA_HOME"/common/lib/naming-resources.jar
GEOSERVER_PATH="$GEOSERVER_PATH":"$CATALINA_HOME"/common/lib/servlet.jar
GEOSERVER_PATH="$GEOSERVER_PATH":"$CATALINA_HOME"/common/lib/tools.jar
GEOSERVER_PATH="$GEOSERVER_PATH":"$CATALINA_HOME"/common/lib/log4j.jar
GEOSERVER_PATH="$GEOSERVER_PATH":"$GEOSERVER_HOME"/lib/embedded.jar
GEOSERVER_PATH="$GEOSERVER_PATH":"$GEOSERVER_HOME"/lib/geotools.jar
GEOSERVER_PATH="$GEOSERVER_PATH":"$GEOSERVER_HOME"/lib/a2jruntime.jar
GEOSERVER_PATH="$GEOSERVER_PATH":"$GEOSERVER_HOME"/lib/jdbcPostgres.jar
GEOSERVER_PATH="$GEOSERVER_PATH":"$GEOSERVER_HOME"/lib/JTS-1.3.jar
GEOSERVER_PATH="$GEOSERVER_PATH":"$GEOSERVER_HOME"/lib/xalan.jar
GEOSERVER_PATH="$GEOSERVER_PATH":"$GEOSERVER_HOME"/lib/xerces.jar
GEOSERVER_PATH="$GEOSERVER_PATH":"$GEOSERVER_HOME"/lib/lucene.jar
GEOSERVER_PATH="$GEOSERVER_PATH":"$GEOSERVER_HOME"/lib/castor.jar
GEOSERVER_PATH="$GEOSERVER_PATH":"$GEOSERVER_HOME"/lib/a2jruntime.jar
GEOSERVER_PATH="$GEOSERVER_PATH":"$GEOSERVER_HOME"/lib/ki-jzkit-iface.jar
GEOSERVER_PATH="$GEOSERVER_PATH":"$GEOSERVER_HOME"/lib/ki-jzkit-z3950.jar
GEOSERVER_PATH="$GEOSERVER_PATH":"$GEOSERVER_HOME"/lib/ki-util.jar

exec "$_RUNJAVA" -classpath "$GEOSERVER_PATH" org.vfny.geoserver.EmbeddedTomcat "start" &
