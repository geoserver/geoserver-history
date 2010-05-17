if [ ! -e "workspaces" ]; then
  echo "This is not a 2.x style data directory."
  exit
fi

mv catalog.xml.old catalog.xml
mv services.xml.old services.xml
rm -rf layergroups
rm -rf workspaces
rm logging.xml global.xml
rm wms.xml wfs.xml wcs.xml
cd styles
for sld in `ls *.sld`; do
  base=${sld:0:${#sld}-4}
  if [ -e "$base.xml" ]; then
    rm $base.xml
  fi
done
cd ..

