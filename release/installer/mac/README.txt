GeoServer Mac Installer Build Instructions
------------------------------------------

A. Creating the GeoServer Application Bundle

1. Change directory to 'GeoServer.app/Contents/Resources/Java'

2. Unpack the GeoServer binary release:

  unzip /some/path/geoserver-*-bin.zip
  mv geoserver-*/* .
  rmdir geoserver-*

3. Change directory back to the root of the installer.

4. Build the console application:

  cd console
  mvn clean install
  cp target/console-*.jar ../GeoServer.app/Contents/Resources/Java/console.jar

5. Edit 'GeoServer.app/Contents/Info.plist' and update the GeoServer version.
   Update the values for the keys:  

  * CFBundleVersion  
  * CFBundleShortVersionString


B. Create the GeoServer Disk Image

1. Using the Mac OSX Disk Utility application create a new image called 
   'GeoServer-<VERSION>':

  a. Set the Volume Name to 'GeoServer-<VERSION>'
  b. Set the Volume Size to 100 MB
  c. Set Partition to 'No Partition Map' 
  d. Accept the rest of the defaults

2. Mount the new image.

3. Create a directory called 'background' directly under the root of the image: 

  mkdir /Volumes/GeoServer-<VERSION>/background

4. Copy 'background.png' into the 'background' folder:

  cp background.png /Volumes/GeoServer-<VERSION>/background

5. Create a directory called 'GeoServer.app' under the root of the image:

  mkdir /Volumes/GeoServer-<VERSION>/GeoServer.app

6. Copy 'GeoSever.app' into the mounted image: 

  cp -R GeoServer.app /Volumes/GeoServer-<VERSION>/GeoServer.app

7. Create an alias to the 'Applications' folder on your system and copy it 
   into the mounted image.  

8. Navigate to the root of the mounted image in the Finder and open up the 
   View Options (Command+J): 

   a. Change the view to Icon View 
   b. Click the 'Always open in icon view' checkbox
   c. Set the icon size to 108 x 108
   d. Set the background to 'Picture' and select the file 
      'background/background.png' 
   e. Remove the sidebar and menubar from the view by clicking the button
      in the right hand corner of the window.
   f. Resize the window to match the size of the background image.
    
9. Set the 'background' folder to invisisble:

  /Developer/Tools/SetFile -a V /Volumes/GeoServer-<VERSION>/background

10. Using the Disk Utility application convert the image: 

  a. Set 'Image Format' to 'compressed'
  b. Set 'Encryption' to 'none' 

