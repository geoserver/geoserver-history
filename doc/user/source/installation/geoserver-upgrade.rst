.. _geoserver_upgrade:

Upgrading GeoServer
===================

.. _minor_upgrade:

Performing a minor upgrade
--------------------------

A *minor* GeoServer upgrade occurs when moving to a newer version of GeoServer that has a different "patch" number. For example moving from GeoServer 1.7.0 to 1.7.1, or moving from 1.7.0 to 1.7.4, etc...

For all upgrades it is strongly recommended that GeoServer is running with an *external* data directory. See the :ref: section.

.. warning::

  It is *strongly* recommend that the GeoServer data directory is backed up before performing any upgrade.

Standalone upgrade
^^^^^^^^^^^^^^^^^^

.. note::
   
   This section applies if GeoServer is being run in Standalone mode and not inside of a servlet container.

If GeoServer is configured to run with an **external** data directory and the ``GEOSERVER_DATA_DIR`` is set appropriately, then upgrading only involves installing the new GeoServer version. The new version will recognize the envioronment variable and run accordingly.

If GeoServer is configured to run with an **internal** data directory then the data directory from the old installation must be copied and overwrite the data directory from the new installation::

   % ls
     geoserver-1.7.0   geoserver-1.7.1
   % rm -rf geoserver-1.7.1/data_dir
   % cp -R geoserver-1.7.0/data_dir geoserver-1.7.1


Web Archive upgrade
^^^^^^^^^^^^^^^^^^^

.. note::

   This section applies if GeoServer is being run in Web Archive mode inside of a servlet container or application server.

Upgrading GeoServer inside of a servlet container is specific to the container being run. The instructions below describe the generic process but it is important to review the servlet container documentation regarding web application upgrades.

If GeoServer is configured to run with an **external** data directory:

   #. Undeploy the old GeoServer version.
   #. Deploy the new GeoServer version.

If the web application uses the ``GEOSERVER_DATA_DIR`` context parameter in the ``web.xml`` file to locate the directory then perform the following additional steps:

   3. Edit ``web.xml``, and point the ``GEOSERVER_DATA_DIR`` context parameter to the location of the data directory.

   4. Restart the GeoServer web application. This step may be unnecessary if the servlet container supports automatic reloading upon file modification.

If GeoServer is configured to run with an **internal** data directory:

.. warning::

   Undeploying the web application will delete the data directory. You **must** copy the data directory to a location external to the web application.

#. Backup the data directory.
#. Undeploy the old geoserver version.
#. Deploy the new geoserver version.
#. Copy the backed up data directory into the web application root over-writting the existing directory.
#. Restart the web application.

Example::

   % cp -R /opt/tomcat5/webapps/geoserver/data /backups
   <<undeploy old version>>
   <<deploy new version>>
   % rm -rf /opt/tomcat5/webapps/geoserver/data
   % cp -R /backups/data /opt/tomcat5/webapps/geoserver
   <<restart web application>>
   
.. _major_upgrade:

Performing a major upgrade
--------------------------

A *minor* GeoServer upgrade occurs when moving to a newer version of GeoServer that has a different "minor" number. For example moving from GeoServer 1.6.0 to 1.7.0, or moving from 1.5.5 to 1.7.1, etc...

Performing a major upgrade is the same process as performing a minor upgrade with an additional step. To perform a full upgrade:

#. Follow the steps in the previous section: :ref:`minor_upgrade`. 

#. Stop GeoServer.

#. Remove the ``Geotools`` directory from the operating system temporary file system storage.

   .. note::

      On Windows systems this directory is ``C:\Documents and Settings\<user>\Local Settings\Temp``. On Linux and Mac OS systems this directory is usually ``/tmp``.

#. Restart GeoServer.

