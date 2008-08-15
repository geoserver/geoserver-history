H2="$HOME/.m2/repository/org/h2database/h2/1.0-SNAPSHOT/h2-1.0-SNAPSHOT.jar"
M2_REPO="$HOME/.m2/repository"
CP="$H2:$M2_REPO/org/openplans/spatialdbbox/1.0-SNAPSHOT/spatialdbbox-1.0-SNAPSHOT.jar:$M2_REPOy/org/geotools/gt2-main/2.4-SNAPSHOT/gt2-main-2.4-SNAPSHOT.jar:$M2_REPO/com/vividsolutions/jts/1.8/jts-1.8.jar"
SF="0"

if [ "load" = "$1" ]; then
   java -cp $CP org.h2.tools.DeleteDbFiles -dir . -db cite
   java -cp $CP org.h2.tools.RunScript -url jdbc:h2:cite -script h2.sql -user "" -password ""
   java -cp $CP org.h2.tools.RunScript -url jdbc:h2:cite -script dataset-sf${SF}.sql -user "" -password ""
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

