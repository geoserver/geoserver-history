.. _release_guide:

Release Guide
=============

Instructions for performing a GeoServer release.

.. contents::
   :depth: 2
   
Notify developer lists
----------------------

It is good practice to notify the geoserver developer list of the intention
to make the release a few days in advance, even though the release date as been
agreed upon before hand. GeoServer releases are usually made in conjunction with
GeoTools releases, so the geotools developer list should also be notified of the
associated geotools release a few days in advance.

On the day the release is being made is it also good practice to send a warning
to the list asking that developers refrain from comitting until the release tag
has been created.

Prerequisites
-------------

The following are necessary to perform a GeoServer release:

#. commit access to GeoServer svn
#. edit access to the GeoServer wiki
#. administration rights to the GeoServer JIRA

For 2 and 3 above you may also simply ask someone on the developer list to 
perform the associated steps.

If a parallel GeoTools release is being preformed see the `GeoTools Release Guide <http://docs.codehaus.org/display/GEOT/How+to+cut+a+release>`_. Alternatively
you can (nicely) ask one of the GeoTools developers to perform the release.

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
    
  .. note::

     The ``fixforversion`` for the JIRA changelog can be found by exploring the
     GeoServer main page on JIRA even before actually building the changelog. 
     See the links to the various unreleased versions.

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

     find . -name pom.xml -exec sed -i 's/<VERSION>-SNAPSHOT/<NEWVERSION>-SNAPSHOT/g' {} \;

   Example::

      find . -name pom.xml -exec sed -i 's/1.7.1-SNAPSHOT/1.7.2-SNAPSHOT/g' {} \; 

#. Commit changes::

      svn commit -m "Upgrading pom version to <NEWVERSION>-SNAPSHOT" .


Set tag pom versions
--------------------

#. Set tag pom version numbers::

     find . -name pom.xml -exec sed -i 's/<VERSION>-SNAPSHOT/<VERSION>/g' {} \;

   Example::

     find . -name pom.xml -exec sed -i 's/1.7.1-SNAPSHOT/1.7.1/g' {} \;

#. Commit changes::

     svn commit -m "Setting pom versions to 1.7.1" .

Build release artifacts
-----------------------

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

Build documentation
-------------------

.. note::

   Building the GeoServer document requires the following be installed

     * `Sphinx <http://sphinx.pocoo.org/>`_, version 0.6 or greather
     * Make

#. Change to the root of the documentation directory, or check it out from
   http://svn.codehaus.org/geoserver/branches/1.7.x/doc

#. Change directory to ``doc/user``.

#. Run the ``make`` command::

      make html

#. Change directory to ``build/html`` and archive its contents::

      cd build/html     
      zip -r geoserver-1.7.1-userguide.zip *

#. Upload the userguide to SourceForge following steps in the 
   :ref:`upload_artifacts` section.

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

#. Right-click on the installer script ``geoserver.nsi`` and select "Compile Script".  

   .. image:: win-installer2.png

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

#. Click the `Mange` link on the right hand side of the page.

   .. image:: jira2.png

#. Find the row for the version being released and click the ``Release`` link
   located on the right.

   .. image:: jira3.png

#. Move back any open issues to the next version, and click the ``Release`` 
   button.

   .. image:: jira4.png

.. _upload_artifacts:

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

Primary artifacts
^^^^^^^^^^^^^^^^^

#. Log in to `SourceForge <http://sourceforge.net/account/login.php>`_.

#. Go to the `GeoServer SourceForge page 
   <https://sourceforge.net/projects/geoserver/>`_.

#. Under the ``Project admin`` tab select ``Feature Settings``.
#. Click ``Manage`` in the ``File Release System`` row

   .. image:: sf1.png

#. Click ``Add Release`` next to the ``GeoServer`` package.

   .. image:: sf2.png

#. Enter the release version and click the ``Create This Release`` button.

   .. image:: sf3.png

#. Copy the contents of the README (from previous step) into the ``Release 
   Notes`` text box.

#. Generate the change log from 
   `jira <http://jira.codehaus.org/secure/ConfigureReleaseNote.jspa?projectId=10311>`_ 
   (text format) and  copy the contents into the ``Change Log`` text box.

#. Click the ``Preserve my pre-formatted text`` check box.

#. Click the ``Submit/Refresh`` button.
   
   .. image:: sf4.png

#. Scroll down to the ``Add Files To This Release`` section and check off all
   the primary artifacts. 

   .. warning:: 

      Be sure not to include the plugin artifacts in this step.

   .. image:: sf5.png

#. Click the ``Add Files and/or Refresh View`` button. 

#. Scroll down to the ``Edit Files In This Release Section``.

#. For the .dmg artifact set the ``Processor`` to ``i386`` and the ``File
   Type`` to ``.dmg``.

   .. image:: sf6.png

#. For the .exe artifact set the ``Processor`` to ``i386`` and the ``File
   Type`` to ``.exe.``.

#. For the src artifact set the ``Processor`` to ``Platform-Independent`` and 
   the ``File Type`` to ``Source .zip``.

#. For all other artifacts set the ``Processor`` to ``Platform-Independent`` and
   the ``File Type`` to ``.zip``.

.. note::

   The processor and file type must be set one artifact at a time, clicking the
   the ``Update/Refresh`` button at each step.

Extension artifacts
^^^^^^^^^^^^^^^^^^^

Following steps from the previous section create a release of the ``GeoServer 
Extensions`` package consisting of all the plugin artifacts. A few things to 
note:

* The release version is the same as the primary artifact release.
* The ``Release Notes`` and ``Change Log`` may be omitted.
* Each plugin artifact is ``Platform-Independent`` and of file type ``.zip``.

Create a download page 
----------------------

#. Go to http://geoserver.org/display/GEOS/Stable and log in.

   .. note::

      If creating an experimental release, replace the above link with 
      http://geoserver.org/display/GEOS/Latest

#. Click the ``Add Page`` link under the ``Page Operations`` menu.

#. Name the page "GeoServer <VERSION>".

#. Click the ``Select a page template`` link.

   .. image:: wiki1.png

#. Select ``Download`` and click the ``Next>>`` button.

#. Enter in the ``VERSION``, ``DATE``, ``JIRA_VERSION``, and ``SF_RELEASE_ID``.

   .. note::

      The ``SF_RELEASE_ID`` is the release number assigned by SourceForge for
      the release created in the previous step.

#. Click the ``Insert Variables`` button.

#. Click the ``Save`` button.

Announce the release
--------------------

Mailing lists
^^^^^^^^^^^^^

Send an email to both the developers list and users list announcing the
release. The message should be relatively short, save the marketing for the
blog post. The following is an example::

   Subject: GeoServer 1.7.1 Released

   The GeoServer team is happy to announce the release of GeoServer 1.7.1.
   The release is available for download from:

   http://geoserver.org/display/GEOS/GeoServer+1.7.1
   
   This release comes with some exciting new features. The new and
   noteworthy include:
   
      * KML Super Overlay and Regionating Support
      * KML Extrude Support
      * KML Reflector Improvements
      * Mac OS X Installer
      * Dutch Translation
      * Improved Style for Web Admin Interface
      * New SQL Server DataStore Extension
      * Improved Oracle DataStore Extension
      * Default Templates per Namespace
   
   Along with many other improvements and bug fixes. The entire change log
   for the 1.7.1 series is available in the issue tracker:
   
   http://jira.codehaus.org/browse/GEOS/fixforversion/14502
   
   A very special thanks to all those who contributed bug fixes, new
   features, bug reports, and testing to this release.
   
   --
   The GeoServer Team

SourceForge
^^^^^^^^^^^

#. Log in to `SourceForge <http://sourceforge.net/account/login.php>`_.
#. Edit the release, and scroll down to the bottom of the page.
#. Check the ``I'm sure`` check box, and click the ``Send Notice`` button.

   .. image:: sfnotice.png
  
#. Repeat for the extension release.

GeoServer blog
^^^^^^^^^^^^^^

.. note:: 

   This step requires an account on http://blog.geoserver.org

#. Log into `Wordpress <http://blog.geoserver.org/wp-login.php>`_.

#. Create a new post. The post should be more "colorful" than the average 
   announcement. It is meant to market and show off any and all new
   features. Examples of previous posts:

   * http://blog.geoserver.org/2008/12/09/geoserver-171-released/
   * http://blog.geoserver.org/2008/10/27/geoserver-170-released/

#. Do not publish the post. Instead present it to the GeoServer outreach 
   team for review, and they will publish it.

SlashGeo
^^^^^^^^

.. note:: 

   This step requires an account on http://slashgeo.org

#. Go to http://slashgeo.org, and log in, creating an account if necessary.

#. Click the ``Submit Story`` link on the left hand side of the page. 
   Examples of previous stories:

   * http://technology.slashgeo.org/technology/08/12/09/1745249.shtml
   * http://industry.slashgeo.org/article.pl?sid=08/10/27/137216

FreeGIS
^^^^^^^

Send an email to ``bjoern dot broscheit at uni-osnabrueck dot de``. 
Example::

  Subject: GeoServer update for freegis

  GeoServer 1.7.1 has been released with some exciting new features. The big
  push for this release has been improved KML support. The new and noteworthy 
  include:

    * KML Super Overlay and Regionating Support
    * KML Extrude Support
    * KML Reflector Improvements
    * Mac OS X Installer
    * Dutch Translation
    * Improved Style for Web Admin Interface
    * New SQL Server DataStore Extension
    * Improved Oracle DataStore Extension
    * Default Templates per Namespace

  Along with many other improvements and bug fixes. The entire change log for
  the 1.7.1 series is available in the issue tracker:

  http://jira.codehaus.org/browse/GEOS/fixforversion/14502

FreshMeat
^^^^^^^^^
.. note:: 

   This step requires an account on http://freshmeat.net/

#. Go to http://freshmeat.net/ and log in.
#. Search for "geoserver" and click the resulting link.
#. Click the ``add release`` link at the top of the page.
#. Choose the ``Default`` branch
#. Enter the version and choose the appropriate ``Release focus``.

   .. note::

      The release focus is usually 4,5,6, or 7. Choose which ever is
      appropriate.

#. Enter a succinct description (less than 600 characters) of the ``Changes``.
#. Update the links to:

   * the ``Zip``
   * the ``OS X package``
   * the ``Changelog``

#. Click the ``Step 3`` button.
#. Click the ``Finish`` button.
