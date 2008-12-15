.. _java_install:

Installing Java
===============

The instructions for installing a Java Development Kit (JDK) are dependent on your operating system.  Please follow the  instructions below for your operating system.

Windows
-------

#. Visit the `Sun Java downloads <http://java.sun.com/javase/downloads/index.jsp>`_ page. The most recent version of Java is Java 1.6 (which is also known as Java 6). Choose the ``Java SE Development Kit (JDK) 6 ...`` option.
#. Choose the appropriate platform, ``Windows`` or ``Windows x64``, and proceed with the download. 
#. Run the installer accepting the defaults.

   .. image:: java-install1.png
      :align: center

.. warning::

   If a Java Runtime Environment (JRE) is already installed on the system it may be desirable to disable the JRE that comes with the JDK. This can be done on the *Custom Setup* screen of the installer. 
   
   .. image:: java-install2.png
      :align: center


4. Create a new environment variable named ``JAVA_HOME`` and set it to the location of the JDK installed in the previous step.

   .. image:: java_home_alt.png
      :align: center


Linux
-----

.. warning::

   Many Linux distributions come with a JDK pre installed. While theoretically GeoServer will run on any Java virtual machine it is *strongly* recommended that a Sun virtual machine is used.

Mac OS X
--------

On Mac OS X systems Apple provides a JDK so no installation is necessary.
