.. _data_gdal:

GDAL Image Formats
==================

GeoServer can leverage the `ImageIO-ext <https://imageio-ext.dev.java.net>`_ GDAL libraries to read selected coverage formats. `GDAL <http://www.gdal.org>`_ is able to read many formats, but for the moment GeoServer supports only a few general interest formats and those that can be legally redistributed and operated in an open source server.

The following image formats can be read by GeoServer using GDAL:

* DTED
* EHdr
* ERDASImg
* JP2MrSID
* MrSID
* NITF

Installing GDAL
---------------

GDAL is not a standard GeoServer extension, as the GDAL library files are built into GeoServer by default.  However, in order for GeoServer to leverage these libraries, the GDAL (binary) program itself must be installed through your host system's OS.  Once this program is installed, GeoServer will be able to recognize GDAL data types.

#. Navigate to the `imageio-ext download page <https://imageio-ext.dev.java.net>`_.
#. Select the most recent stable binary release.
#. Select "native libraries".
#. Download and extract/install the correct version for your OS.

   .. note:: If you are on Windows, make sure that the GDAL DLL files are on your PATH. If you are on Linux, be sure to set the LD_LIBRARY_PATH environment variable to be the folder where the SOs are extracted.

#. Download and extract the GDAL CRS definitions.

   .. note:: Make sure to set a GDAL_DATA environment variable to the folder where you have extracted this file.

Once these steps have been completed, restart GeoServer.  If done correctly, new data formats will be in the :guilabel:`Raster Data Sources` list when creating a new data store.

.. figure:: images/gdalcreate.png
   :align: center

   *GDAL image formats in the list of raster data stores*

Adding support for ECW and Kakadu
---------------------------------

Configuring a DTED data store
-----------------------------

.. figure:: images/gdaldtedconfigure.png
   :align: center

   *Configuring a DTED data store*

Configuring a EHdr data store
-----------------------------

.. figure:: images/gdalehdrconfigure.png
   :align: center

   *Configuring a EHdr data store*

Configuring a ERDASImg data store
---------------------------------

.. figure:: images/gdalerdasimgconfigure.png
   :align: center

   *Configuring a ERDASImg data store*

Configuring a JP2MrSID data store
---------------------------------

.. figure:: images/gdaljp2mrsidconfigure.png
   :align: center

   *Configuring a JP2MrSID data store*

Configuring a NITF data store
-----------------------------

.. figure:: images/gdalnitfconfigure.png
   :align: center

   *Configuring a NITF data store*


