#!/bin/sh
# -----------------------------------------------------------------------------
# Start Script for GEOSERVER
#
# $Id: test.sh,v 1.2 2003/01/23 21:59:32 cholmesny Exp $
# -----------------------------------------------------------------------------

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
cd $GEOSERVER_HOME
ant stop
ant test
