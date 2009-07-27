.. _wfs_output_formats:

WFS output formats
==================

WFS returns features and feature information in a number of possible formats.  This page shows a list of the output formats.  In all cases the syntax for setting an output format is::

   outputFormat=<outputformat>

where ``<outputformat>`` is any of the options below.

.. note:: Some additional output formats are available with the use of an extension, such as Excel.  This list applies just to the basic GeoServer installation.  The full list of output formats supported by your GeoServer instance can be found by requesting your WFS :ref:`wfs_getcap`.

.. list-table::
   :widths: 30 30 40
   
   * - **Format**
     - **Syntax**
     - **Notes**
   * - GML2
     - ``outputFormat=GML2``
     - Default option using WFS 1.0.0
   * - GML3
     - ``outputFormat=GML3``
     - Default option using WFS 1.1.0
   * - Shapefile
     - ``outputFormat=shape-zip``
     - Created in a ZIP archive
   * - JSON
     - ``outputFormat=json``
     - 
   * - CSV
     - ``outputFormat=csv``
     - 

