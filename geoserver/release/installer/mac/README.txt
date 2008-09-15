GeoServer Mac Installer Build Instructions
------------------------------------------

A. Creating the GeoServer Application Bundle

1. Change directory to 'GeoServer.app/Contents/Resources/Java'

2. Unpack the GeoServer binary release:

  unzip /some/path/geoserver-*-bin.zip
  mv geoserver/* .
  rm -rf geoserver

3. Move the data directory into the webapp:

  mv data_dir webapps/geoserver/data

B. Create the GeoServer Disk Image

1. Using the Mac OSX Disk Utility application create a new image called 
   'GeoServer'. 

2. Mount the new image.

3. Create a directory called 'background' directly under the root of the image: 

  mkdir /Volumes/GeoServer/background

4. Copy 'background.png' into the 'background' folder:

  cp background.png /Volumes/GeoServer/background

5. Set the 'background' folder to invisisble:

  /Developer/Tools/SetFile -a V /Volumes/GeoServer/background

6. Copy 'GeoSever.app' into the mounted image: 

  cp -R GeoServer.app /Volumes/GeoServer

7. Create an alias to the 'Applications' folder on your system and copy it into
   the mounted image.  

8. Using the Disk Utility application convert the image, compressing it and 
   making it read only.
