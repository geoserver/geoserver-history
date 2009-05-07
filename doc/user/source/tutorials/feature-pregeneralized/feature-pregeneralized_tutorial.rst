..  _feature-pregeneralized_tutorial:

Feature-Pregeneralized
=======================

Introduction
------------

This tutorial shows how to use the geotools feature-pregeneralized module in geoserver. The feature-pregeneralized module is used to improve performance and lower memory usage and IO traffic.

.. note::
  
  Vector generalization reduces the number of vertices of a geometry for a given purpose. It makes no sense drawing a polygon with 500000 vertices on a screen. A much smaller number of vertices is enough to draw a topological correct picture of the polygon. 

This module needs features with already generalized geometries, selecting the best fit geometry on demand. 

The full documentation is available here:`<http://docs.codehaus.org/display/GEOTDOC/Feature-Pregeneralized>`_

This tutorial will show two possible scenarios, explaining step by step what to do for using this module in geoserver.


Getting Started
---------------

First, find the location of the GEOSERVER_DATA_DIR. This info is contained in the log file when starting geoserver.::

  ----------------------------------
  - GEOSERVER_DATA_DIR: /home/mcr/geoserver-1.7.x/1.7.x/data/release
  ----------------------------------

Within this direcory, we have to place the shapefiles. There is already a subdirectory **data** which will be used.
Within this subdirectory, create a directory **streams**.

Within **<GEOSERVER_DATA_DIR>/data/streams** create anoter sub directory called **0**. ( 0 meaning "no generalized geometries"). 

This tutorial is based on on a shape file, which you can download from here :download:`Streams <streams.zip>`.
Unzip this file into **<GEOSERVER_DATA_DIR>/data/streams/0**. 

Look for the **WEB-INF/lib/** directory of your geoserver installation. There must be a file called **gt-feature-pregeneralized-<version>-jar**. This jar file includes a tool for generalizing shape files. Open a cmd line and execute the following::

  cd <GEOSERVER_DATA_DIR>/data/streams/0
  java -jar <GEOSERVER_INSTALLATION>/WEB-INF/lib/gt-feature-pregeneralized-<version>.jar generalize 0/streams.shp . 5,10,20,50

You should see the following output::

  Shape file          	0/streams.shp
  Target directory    	.
  Distances           	5,10,20,50
  % |####################################################################################################|

Now there are four additonal directories **5.0** , **10.0** , **20.0** , **50.0** . Look at the size of files with the extension **shp** within these directories, increasing the generalization distance reduces the file size.


.. note::

  The generalized geometries can be stored in additional properties of a feature or the features can be duplicated.
  Mixed variations are also possible. Since we are working with shape files we have to duplicate the features.

(TO BE CONTINUED)





