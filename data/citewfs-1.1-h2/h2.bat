@echo off

set HOME=c:\Documents and Settings\YourUser
set M2_REPO=%HOME%\.m2\repository
set H2=%M2_REPO%\org\h2database\h2\1.1.104\h2-1.1.104.jar
set GT_VERSION=2.5-SNAPSHOT
set CP="%H2%";"%M2_REPO%/org/geotools/gt-main/%GT_VERSION%/gt-main-%GT_VERSION%.jar";"%M2_REPO%/org/geotools/gt-jdbc-h2/%GT_VERSION%/gt-jdbc-h2-%GT_VERSION%.jar";"%M2_REPO%/com/vividsolutions/jts/1.9/jts-1.9.jar"
echo Classpath: %CP%
set SF="2"

if "load" == "%1" goto load
if "clean" == "%1" goto clean
if "run" == "%1" goto run

:usage
echo "Usage: %0 <load|clean|run>"
goto:eof

:load
java -cp %CP% org.h2.tools.DeleteDbFiles -dir . -db cite
java -cp %CP% org.h2.tools.RunScript -url jdbc:h2:cite -script h2.sql -user "" -password ""
java -cp %CP% org.h2.tools.RunScript -url jdbc:h2:cite -script dataset-sf%SF%.sql -user "" -password ""
goto:eof

:run
java -cp %CP% org.h2.tools.Server

