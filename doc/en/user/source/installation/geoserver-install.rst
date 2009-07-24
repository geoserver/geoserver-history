.. _geoserver_install:

Installing GeoServer
====================

GeoServer installation can take one of two forms:

#. **Web Archive** (WAR) mode inside of an external servlet container such as `Tomcat <http://tomcat.apache.org/>`_
#. **Standalone** mode inside of an embedded `Jetty <http://www.mortbay.org/jetty/>`_ container

Instructions for the *WAR* mode can be found in the :ref:`war_install` section. Instructions for the *Standalone* mode are dependent on operating system. Following the instructions for below for the target platform.

Windows
-------

#. Visit http://geoserver.org/display/GEOS/Stable.

#. Choose the ``Windows Installer`` option and proceed with the download.

#. Run the installer accepting the defaults.

.. warning::

   If you receive the following dialect while running the installer:

   .. image:: jdk-warning.png
      :align: center
  
   It means one of two things. Either:

   #. A Java JDK is not installed on the system or
   #. The ``JAVA_HOME`` environment variable is not set properly

   Proceed to the :ref:`java_install` section for instructions.

4. Start GeoServer by navigating from the *Start Menu* to ``Programs -> GeoServer 1.7.0 -> Start GeoServer``.

5. Visit http://localhost:8080/geoserver in a web browser.

Linux
-----

#. Visit http://geoserver.org/display/GEOS/Stable.

#. Choose the ``Binary Package`` option and proceed with the download.

#. From the command line unzip the archive::

	% unzip geoserver-1.7.0-bin.zip
	
#. Start GeoServer by changing directory to ``geoserver-1.7.0/bin`` and executing the ``startup.sh`` script::

	% cd geoserver-1.7.0/bin
  	% sh startup.sh

#. Visit http://localhost:8080/geoserver in a web browser.

Mac OS X
--------

As of 1.7.0 we should have a dmg installer. Update the docs.

.. _war_install:

Web Archive 
-----------

#. Visit http://geoserver.org/display/GEOS/Stable.

#. Choose the ``Web Archive`` option and proceed with the download.

#. Extract the file ``geoserver.war`` file from the downloaded archive.

#. Deploy ``geoserver.war`` inside the servlet container.

#. Visit http://localhost:8080/geoserver in a web browser.

.. note::

   Replace port 8080 with whatever port the servlet container is configured to listen on.

Post installation
-----------------

At this point GeoServer should be up and running but it is *strongly recommend* that some post-install configuration is carried out.

Data directory
``````````````

By default GeoServer is configured to run of an *internal* data directory. To ensure a smooth upgrade path when installing new versions of GeoServer it is recommended that an *external* data directory is set up. Proceed to the next section: :ref:`data_directory`.

Server performance
``````````````````

By default GeoServer is not configured for optimal performance. If GeoServer is being run in a production environment in which maximal performance is necessary it is recommended that the default configuration be changed based on :ref:`production`.


