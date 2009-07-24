.. _eclipse_guide:

Eclipse Guide
=============

A reference for developing GeoServer with Eclipse.

Importing modules
-----------------

See the Eclipse section of the :ref:`maven_guide`.

Running and debugging 
---------------------

Run or debug the class ``org.vfny.geoserver.jetty.Start`` inside of the web 
module. The steps to do so are detailed in the :ref:`quickstart`.

Setting the data directory
^^^^^^^^^^^^^^^^^^^^^^^^^^

If unset, GeoServer will default to the ``minimal`` directory inside of the 
``web`` module for its data directory. To change this:

 #. Open ``Debug Configurations...`` from the Eclipse menu

    .. image:: dd1.jpg

 #. Select the ``Start`` configuration, select the ``Arguments`` panel and 
    specify the ``-DGEOSERVER_DATA_DIR`` parameter, setting it to the absolute
    path of the data directory

    .. image:: dd2.jpg

Changing the default port for Jetty
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

If unset, Jetty will default to port ``8080``. To change this:

  #. Open the ``Arguments`` panel of the ``Start`` configuration as described
     in the above section

  #. Specify the ``-Djetty.port`` paremeter, setting it to the desired port

     .. image:: port.jpg
  
Eclipse preferences
-------------------

Code formatting
^^^^^^^^^^^^^^^

#. Download http://svn.osgeo.org/geotools/trunk/build/eclipse/formatter.xml
#. Navigate to ``Java``, ``Code Style``, ``Formatter`` and click ``Import...``

   .. image:: code_formatting1.jpg

#. Select the ``formatter.xml`` file downloaded in step 1
#. Click ``Apply``

   .. image:: code_formatting2.jpg

Code templates
^^^^^^^^^^^^^^

#. Download http://svn.osgeo.org/geotools/trunk/build/eclipse/codetemplates.xml
#. Navigate to ``Java``, ``Code Style``, ``Formatter`` and click ``Import...``

   .. image:: code_templates.jpg

#. Select the ``formatter.xml`` file downloaded in step 1
#. Click ``Apply``

Text editors
^^^^^^^^^^^^

#. Navigate to ``General``, ``Editors``, ``Text Editors``
#. Check ``Insert spaces for tabs``
#. Check ``Show print margin`` and set ``Print margin column`` to "100"
#. Check ``Show line numbers``
#. Check ``Show whitespace characters`` (optional)

   .. note::

      Showing whitespace characters can help insure that unecessary whitespace 
      is not unintentionaly comitted.
   
   .. image:: text_editors.jpg

#. Click ``Apply``

Compiler
^^^^^^^^

#. Navigate to ``Java``, ``Compiler``, ``Building``
#. Expand ``Output folder`` and add ".svn/" to the list of 
   ``Filtered resources``

   .. image:: compiler.jpg

#. Click ``Apply``

