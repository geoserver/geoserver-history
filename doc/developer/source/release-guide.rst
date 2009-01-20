.. _release_guide:

Release Guide
=============

Instructions for performing a GeoServer release.

.. toctree::
   :maxdepth: 1

Update source code
------------------

#. Update or check out the branch to be released from.
#. Ensure that ``svn status`` yields no local modifications.

Update the README
-----------------

#. Add an entry to ``release/README.txt`` using the following template::

     GeoServer <VERSION> ( <DATE> )
     ==============================

     <Short paragraph describing release>.

     <List of notable features / improvements / bug fixes>

     The entire changelog can be found at:

     <link to jira changelog> 

     This release is based on <GeoTools version>.

   Example::

     GeoServer 1.7.1 (December 08, 2008)
     ----------------------------------

     The second release of the 1.7.1 series includes some great KML and Google 
     Earth improvements, along with other new features and bug fixes. The new 
     and note worthy for this release includes:

      * KML Super Overlay and Regionating Support
      * KML Extrude Support
      * KML Reflector Improvements
      * Mac OS X Installer
      * New SQL Server DataStore Extension
      * Improved Oracle DataStore Extension

    And much more. The entire changelog can be found at :

      http://jira.codehaus.org/browse/GEOS/fixforversion/14502

    This release is based on GeoTools 2.5.2.
#. Commit changes to the README::

     svn commit -m "Updating README for <VERSION>" release/README.txt

Update version numbers
----------------------

#. Upgrade the version number in the following files::

     release/installer/win/geoserver.nsi
     release/installer/mac/GeoServer.app/Contents/Info.plist
     release/bin.xml
     release/doc.xml
     release/src.xml
     web/src/main/java/ApplicationResources*

   Example::

     sed -i 's/1.7.1/1.7.2/g' release/installer/win/geoserver.nsi

#. Commit changes::

     svn commit -m "Updated version numbers for <VERSION>" release web/src/main/java
  
Create a release tag
--------------------

#. Create a tag for the release::

     svn copy -m "Create tag for release <VERION>" https://svn.codehaus.org/geoserver/<BRANCH> https://svn.codehaus.org/geoserver/tags/<VERSION>

#. Checkout the release tag::

     svn co https://svn.codehaus.org/geoserver/tags/<VERSION> 

   .. note::

      svn switch may also be used to get to the release tag but caution must be
      taken to switch back to the branch after the release has been performed. 

Upgrade branch pom versions
---------------------------

#. Upgrade branch pom version numbers::

     find . -name pom.xml -exec sed 's/<VERSION>-SNAPSHOT/<NEWVERSION>-SNAPSHOT/g' {} \;

   Example::

      find . -name pom.xml -exec sed 's/1.7.1-SNAPSHOT/1.7.2-SNAPSHOT/g' {} \; 

#. Commit changes::

      svn commit -m "Upgrading pom version to <NEWVERSION>-SNAPSHOT" .


Set tag pom versions
--------------------

#. Set tag pom version numbers::

     find . -name pom.xml -exec sed 's/<VERSION>-SNAPSHOT/<VERSION>/g' {} \;

   Example::

     find . -name pom.xml -exec sed 's/1.7.1-SNAPSHOT/1.7.1/g' {} \;

#. Commit changes::

     svn commit -m "Setting pom versions to 1.7.1" .

Build release artifcacts
------------------------

.. warning::

   All operations for the remainder of this guide must be performed from the
   release tag.

#. Compile from the root of the source tree with the folling command::

     mvn clean install -P release

#. Build javadocs::

     mvn javadoc:javadoc

#. Build artifacts::

     mvn assembly:attached

At this point the release artifacts will be located in ``target/release``.

CITE test 
---------

#. Change directory to ``target/release`` and unzip the binary package::

     cd target/release
     unzip geoserver-*-bin.zip

#. Execute the GeoServer CITE tests as described in the :ref:`cite_test_guide`.

#. Unzip the war package and deploy the war in a servlet container such as
   Tomcat::

    unzip geoserver-*-war.zip
    cp geoserver.war /opt/tomcat5/webapps

#. Re-run GeoServer CITE tests.

Hand test
---------

Start GeoServer with the release data directory and hand test. A checklist of 
things to test can be found in the :ref:`release_testing_checklist`.

Build Windows installer
-----------------------

.. note:: 

   This step requires a windows machine.

#. If necessary download and install `NSIS <http://nsis.sourceforge.net/Main_Page>`_.
#. Unzip the binary package.
#. Copy the files from ``release/installer/win`` to the root of the unpacked 
   archive.

   .. image:: win-installer1.png
      :align: center

#. Right-click on the installer script ``geoserver.nsi`` and select "Compile Script".  

   .. image:: win-installer2.png
      :align: center

After successfully compiling the script an installer named 
``geoserver-<VERSION>.exe`` will be located in the root of the unpacked archive.
 
Build Mac OS X installer
------------------------

.. note::

   This step requires a mac os machine.

Change directory to ``release/installer/mac`` and follow the instructions in 
``README.txt``.

Release on Jira
---------------

.. note::

   This step requires administrative privileges in Jira.

#. Log into `GeoServer Jira <http://jira.codehaus.org/login.jsp?os_destination=/browse/GEOS>`_.
#. Click the ``Administer Project`` link on the left hand side of the page.
  
   .. image:: jira1.png
      :align: center

#. Click the `Mange` link on the right hand side of the page.

   .. image:: jira2.png
      :align: center

#. Find the row for the version being released and click the ``Release`` link
   located on the right.

   .. image:: jira3.png
      :align: center

#. Move back any open issues to the next version, and click the ``Release`` 
   button.

   .. image:: jira4.png
      :align: center

Upload release artifacts to SourceForge
---------------------------------------

#. Using WebDAV or SFTP connect to https://frs.sourceforge.net/<u>/<us>/<username>/uploads. 
   Here <u> and <us> are the first and and first two characters of the username
   and <username> is the full user name. Example::

     https://frs.sourceforge.net/j/js/jsmith/uploads

#. Copy all release artifacts to the ``uploads`` directory. 

.. note::

   More information available in the SourceForge `File System Release Guide
   <http://alexandria.wiki.sourceforge.net/File+Release+System+-+Offering+Files+for+Download>`_.

Release on SourceForge
----------------------

.. note::

   This step requires administrative privileges in SourceForge.

#. Log in to `SourceForge <http://sourceforge.net/account/login.php>`_.

#. Go to the `GeoServer SourceForge page 
   <https://sourceforge.net/projects/geoserver/>`_.

#. Under the ``Admin`` tab select ``File Releases``. 

   .. image:: sf1.png
      :align: center

#. Click ``Add Release`` next to the ``GeoServer`` package.

   .. image:: sf2.png
      :align: center

#. Enter the release version and click the ``Create This Release`` button.

   .. image:: sf3.png
      :align: center

   
