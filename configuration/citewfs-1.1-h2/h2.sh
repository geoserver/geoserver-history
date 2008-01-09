H2_HOME="/opt/h2"
M2_REPO="/home/jdeolive/.m2/repository"
CP="$H2_HOME/bin/h2.jar:$M2_REPO/org/openplans/spatialdbbox/1.0-SNAPSHOT/spatialdbbox-1.0-SNAPSHOT.jar:$M2_REPOy/org/geotools/gt2-main/2.4-SNAPSHOT/gt2-main-2.4-SNAPSHOT.jar:$M2_REPO/com/vividsolutions/jts/1.8/jts-1.8.jar"

if [ "load" = "$1" ]; then
   java -cp $CP org.h2.tools.DeleteDbFiles -dir . -db cite
   java -cp $CP org.h2.tools.RunScript -url jdbc:h2:cite -script h2.sql -user "" -password ""
   java -cp $CP org.h2.tools.RunScript -url jdbc:h2:cite -script dataset-sf2-h2.sql -user "" -password ""
else
  if [ "clean" = "$1" ]; then
     java -cp $CP org.h2.tools.DeleteDbFiles -dir . -db cite
  else
      if [ "run" = "$1" ]; then
        java -cp $CP org.h2.tools.Server
      else
         echo "Usage: $0 <load|clean|run>"
      fi
  fi
fi

