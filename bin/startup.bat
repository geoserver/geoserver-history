@echo off
REM -----------------------------------------------------------------------------
REM Start Script for GEOSERVER
REM
REM $Id: startup.bat,v 1.3 2003/09/19 15:26:32 cholmesny Exp $
REM -----------------------------------------------------------------------------

if "%JAVA_HOME%" == "" goto noJava1

if not exist "%JAVA_HOME%\bin\java.exe" goto noJava2

if "%GEOSERVER_HOME%" == "" goto noGeo1

if not exist "%GEOSERVER_HOME%\bin\startup.bat" goto noGeo2

REM -------------
REM OK, we're ready to try actually runnning it.
REM -------------

java -classpath "%GEOSERVER_HOME%\lib\catalina\bin\bootstrap.jar";"%GEOSERVER_HOME%\lib\catalina\server\lib\catalina.jar";"%GEOSERVER_HOME%\lib\catalina\server\lib\servlet-cgi.jar";"%GEOSERVER_HOME%\lib\catalina\server\lib\servlets-common.jar";"%GEOSERVER_HOME%\lib\catalina\server\lib\servlets-default.jar";"%GEOSERVER_HOME%\lib\catalina\server\lib\servlets-invoker.jar";"%GEOSERVER_HOME%\lib\catalina\server\lib\servlets-manager.jar";"%GEOSERVER_HOME%\lib\catalina\server\lib\servlets-snoop.jar";"%GEOSERVER_HOME%\lib\catalina\server\lib\servlets-ssi.jar";"%GEOSERVER_HOME%\lib\catalina\server\lib\servlets.webdav.jar";"%GEOSERVER_HOME%\lib\catalina\server\lib\jakarta-regexp-1.2.jar";"%GEOSERVER_HOME%\lib\catalina\lib\naming-factory.jar";"%GEOSERVER_HOME%\lib\catalina\common\lib\crimson.jar";"%GEOSERVER_HOME%\lib\catalina\common\lib\jasper-compiler.jar";"%GEOSERVER_HOME%\lib\catalina\common\lib\jasper-runtime.jar";"%GEOSERVER_HOME%\lib\catalina\common\lib\jaxp.jar";"%GEOSERVER_HOME%\lib\catalina\common\lib\jndi.jar";"%GEOSERVER_HOME%\lib\catalina\common\lib\naming-common.jar";"%GEOSERVER_HOME%\lib\catalina\common\lib\naming-resources.jar";"%GEOSERVER_HOME%\lib\catalina\common\lib\servlet.jar";"%GEOSERVER_HOME%\lib\catalina\common\lib\tools.jar";"%GEOSERVER_HOME%\lib\catalina\common\lib\log4j.jar";"%GEOSERVER_HOME%\lib\embedded.jar";"%GEOSERVER_HOME%\lib\jdbcPostgres.jar";"%GEOSERVER_HOME%\lib\geotools.jar";"%GEOSERVER_HOME%\lib\JTS-1.3.jar";"%GEOSERVER_HOME%\lib\xerces.jar";"%GEOSERVER_HOME%\lib\lucene.jar" org.vfny.geoserver.EmbeddedTomcat start &
goto end

:noJava1
  echo The JAVA_HOME environment variable is not defined.
  echo This environment variable is needed to run this program.
goto end

:noJava2
  echo The JAVA_HOME environment variable is defined, but 'java.exe'
  echo was not found there.
goto end

:noGeo1
  echo The GEOSERVER_HOME environment variable is not defined.
  echo This environment variable is needed to run this program.
goto end

:noGeo2
  echo The GEOSERVER_HOME environment variable is not defined correctly.
  echo This environment variable is needed to run this program.
goto end

:end
