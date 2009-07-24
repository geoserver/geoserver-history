.. _production_java:

Java Considerations
===================

Use Sun JRE
-----------

.. note::  As of version 2.0, a Java Runtime Environment (JRE) is sufficient to run GeoServer.  GeoServer no longer requires a Java Development Kit (JDK).

GeoServer's speed depends a lot on the chosen Java Runtime Environment (JRE).  For best performance, use `Sun JRE 6 <http://java.sun.com/javase/downloads/index.jsp>`_ (also known as JRE 1.6).  If this is not possible, use Sun JRE 5 (also known as JRE 1.5).  JREs other than those released by Sun may work correctly, but are generally not tested or supported. Specifically, OpenJDK does not work with GeoServer, as it lacks sufficient support for 2D rendering.

Install native JAI extensions
-----------------------------

The `Java Advanced Imaging API <http://java.sun.com/javase/technologies/desktop/media/>`_ (JAI) is an advanced image manipulation library built by Sun.  GeoServer requires JAI to work with coverages and leverages it for WMS output generation. By default, GeoServer ships with the pure Java version of JAI, but for best performance, install the native JAI version in your JRE.

For installation help, please refer to the `GeoTools page on JAI installation <http://docs.codehaus.org/display/GEOT/Manual+JAI+Installation>`_

Once you have installed JAI, you may remove the JAI related files from your GeoServer instance::

   jai_core-x.y.z.jar
   jai_imageio-x.y.jar 
   jai_codec-x.y.z.jar
   
where ``x``, ``y``, and ``z`` refer to specific version numbers.