#!/bin/sh
# -----------------------------------------------------------------------------
# Start Script for GEOSERVER
#
# $Id: test.sh,v 1.1 2002/08/22 15:40:40 robhranac Exp $
# -----------------------------------------------------------------------------

cd ~/wfs/geoserver
sh bin/stop.sh
ant testPrep
sh bin/start.sh
cd bin