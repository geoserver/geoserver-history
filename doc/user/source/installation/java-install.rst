.. _java_install:

Installing Java
===============

Installing a Java JDK is dependent on operating system. Follow the instructions below for the target platform.

Windows
-------

1. Visit http://java.sun.com/javase/downloads/index.jsp. The current version of Java is 1.6/6.0. Choose the ``Java SE Development Kit (JDK) 6 ...`` option.
2. Choose the appropriate platform, ``Windows`` or ``Windows x64``, and proceed with the download. 
3. Run the installer accepting the defaults.

   .. image:: java-install1.png
      :align: center

.. warning::

   If a Java Runtime Environment (JRE) is already installed on the system it may be desirable to disable the JRE that comes with the JDK. This can be done on the *Custom Setup* screen of the installer. 
   
   .. image:: java-install2.png
      :align: center


4. Create a new environment variable named ``JAVA_HOME`` and set it to the location of the JDK installed in the previous step.

   .. image:: java_home.png
      :align: center


Linux
-----

.. warning::

   Many Linux distributions come with a JDK pre installed. While theoretically GeoServer will run on any Java virtual machine it is *strongly* recommended that a Sun virtual machine is used.

Mac OS X
--------

On Mac OS X systems Apple provides a JDK so no installation is necessary.
