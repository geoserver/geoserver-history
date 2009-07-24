.. _production_java:

Java Considerations
===================

Use Sun's JDK
-------------

GeoServer speed depends a lot on the chosen Java Development Kit (JDK).  **For best performance, use Sun JDK 1.6** (also known as JDK 6).  If this is not possible, use Sun JDK 1.5.  Non-Sun JDKs may work, but are generally not tested or supported. OpenJDK currently does not work with GeoServer, as it lacks sufficient support for 2D rendering.

Install native JAI extensions
-----------------------------

The `Java Advanced Imaging API <http://java.sun.com/javase/technologies/desktop/media/>`_ (JAI) is an advanced image manipulation library built by Sun.  GeoServer requires JAI to work with coverages and leverages it for WMS output generation. By default, GeoServer ships with the pure Java version of JAI, but **for best performance, install the native JAI version in your JDK**.

Installing native JAI
`````````````````````

Please refer to the `GeoTools page on JAI installation <http://docs.codehaus.org/display/GEOT/Manual+JAI+Installation>`_

Once you have installed JAI, you may remove the JAI related files from your GeoServer instance::

   jai_core-x.y.z.jar
   jai_imageio-x.y.jar 
   jai_codec-x.y.z.jar
   
where ``x``, ``y``, and ``z`` refer to specific version numbers.

  