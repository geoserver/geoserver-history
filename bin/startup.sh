#!/bin/sh
# -----------------------------------------------------------------------------
# Start Script for GEOSERVER
#
# $Id: startup.sh,v 1.7 2004/08/24 17:37:53 cholmesny Exp $
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

if [ -z $GEOSERVER_HOME ]; then
  #If GEOSERVER_HOME not set then guess a few locations before giving
  # up and demanding user set it.
  if [ -r bin/start.jar ]; then
     echo "GEOSERVER_HOME environment variable not found, using current "
     echo "directory.  If not set then running this script from other "
     echo "directories will not work in the future."
     export GEOSERVER_HOME=.
  else 
    if [ -r start.jar ]; then
      echo "GEOSERVER_HOME environment variable not found, using current "
      echo "location.  If not set then running this script from other "
      echo "directories will not work in the future."
      export GEOSERVER_HOME=..
    fi
  fi 

  if [ -z "$GEOSERVER_HOME" ]; then
    echo "The GEOSERVER_HOME environment variable is not defined"
    echo "This environment variable is needed to run this program"
    echo "Please set it to the directory where geoserver was installed"
    exit 1
  fi

fi

if [ ! -r "$GEOSERVER_HOME"/bin/startup.sh ]; then
  echo "The GEOSERVER_HOME environment variable is not defined correctly"
  echo "This environment variable is needed to run this program"
  exit 1
fi



cd "$GEOSERVER_HOME"
if [ ! -r server/ ]; then
     #if there is no server then we are in a source install that has not
     #been built.
     if [ -z "$ANT_HOME"]; then
        echo "ANT_HOME not found, Ant is needed to run the embedded server "
        echo "in the source download.  Please install Ant or download "
        echo "the binary release of GeoServer."
	exit 1
     else
        "$ANT_HOME"/bin/ant prepareEmbedded
     fi
fi
#added headless to true by default, if this messes anyone up let the list
#know and we can change it back, but it seems like it won't hurt -ch
exec "$_RUNJAVA" -Djava.awt.headless=true -jar bin/start.jar 
