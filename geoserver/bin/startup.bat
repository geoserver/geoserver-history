@echo off
REM -----------------------------------------------------------------------------
REM Start Script for GEOSERVER
REM
REM $Id: startup.bat,v 1.6 2004/08/24 17:37:53 cholmesny Exp $
REM -----------------------------------------------------------------------------

if "%JAVA_HOME%" == "" goto noJava1

if not exist "%JAVA_HOME%\bin\java.exe" goto noJava2

if "%GEOSERVER_HOME%" == "" goto noGeo1

if not exist "%GEOSERVER_HOME%\bin\startup.bat" goto noGeo2

REM -------------
REM OK, we're ready to try actually runnning it.
REM -------------

goto run

:noJava1
  echo The JAVA_HOME environment variable is not defined.
  echo This environment variable is needed to run this program.
goto end

:noJava2
  echo The JAVA_HOME environment variable is defined, but 'java.exe'
  echo was not found there.
goto end

:noGeo1
  if exist start.jar goto doGeo1
  echo The GEOSERVER_HOME environment variable is not defined.
  echo This environment variable is needed to run this program.
goto end

:doGeo1
echo GEOSERVER_HOME environment variable not found.  Using current
echo directory.  Please set GEOSERVER_HOME for future uses.
 goto run

:noGeo2
  if exist start.jar goto doGeo2
  echo The GEOSERVER_HOME environment variable is not defined correctly.
  echo This environment variable is needed to run this program.
goto end

:doGeo2
  echo GEOSERVER_HOME environment variable not properly set.  Using parent
  echo directory of this script.  Please set GEOSERVER_HOME correctly for 
  echo future uses.
goto run

:run
  if not exist "%GEOSERVER_HOME%/server/geoserver" goto noServer
  goto execJava

:noServer
  if "%ANT_HOME%" == "" goto noAnt
  %ANT_HOME%/bin/ant -f %GEOSERVER_HOME%/build.xml prepareEmbedded
  goto execJava

:noAnt
  echo ANT_HOME not found, Ant is needed to run the embedded server
  echo in the source download.  Please install ant or download the 
  echo binary release of GeoServer
goto end

:execJava
  java -jar start.jar
  goto end  

:end
 pause