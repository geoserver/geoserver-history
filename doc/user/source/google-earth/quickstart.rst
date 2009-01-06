.. _google-earth-quickstart:

Google Earth Quickstart
=======================

.. note::

   This tutorial requires GeoServer **1.5.1** or higher.
   
.. note:: 
   
   This tutorial will assume version **1.7.1** is being used, and that
   GeoServer is running on ``http://localhost:8080/geoserver``. 

.. warning::  

   ..
   
      Note that as of version **1.7.1**, the link for the KML reflector has changed:
      
   ..

      **From version 1.5.1 to 1.7.0:**
      ``http://localhost:8080/geoserver/wms/kml_reflect?layer=<layername>``
	  
   ..
   
      **1.7.1 and after:**
      ``http://localhost:8080/geoserver/wms/kml?layer=<layername>``
			
  

QuickStart
----------

Once GeoServer is installed and running, open up a web browser and 
navigate to the welcome page: ``http://localhost:8080/geoserver``. 
Navigate to the **Map Preview** page by clicking **Demo** then **Map 
Preview**. You will be presented with a list of the currently configured 
layers (FeatureTypes) in your GeoServer (see Figure 1). Find the row 
that says ``topp:states``. To the right of the layer click on the link 
that says **KML**. 



   .. figure:: demopage.png
      :align: center
	  
      *Figure 1: The Map Preview page*

If Google Earth is correctly installed on your computer, you will see 
dialog asking how to open the file. Select **Open with Google Earth**. 



   .. figure:: openingkml.png
      :align: center
	  
      *Figure 2: Open with Google Earth*

When Google Earth is finished loading the result will be similar to Figure 3.

   .. figure:: googleearth.jpg
      :align: center

      *Figure 3: The topp:states layer rendered in Google Earth*

Direct Access to KML
--------------------

All of the configured FeatureTypes are available to be output as KML 
(and thus loaded into Google Earth). The URL structure for KMLs is: 


::

   http://localhost:8080/geoserver/wms/kml?layers=<layername>

For example, the topp:states layer URL is

::

   http://localhost:8080/geoserver/wms/kml?layers=topp:states

Alternative: Adding a Network Link
----------------------------------

An alternative to outputting KML directly into Google Earth is to use a 
Network Link. A Network Link allows for better integration into Google 
Earth. An advantage of this method is being able to refresh the data 
within Google Earth, withouth having to retype a URL, or clicking on 
links in the GeoServer Map Preview. 

To add a Network Link, pull down the **Add** menu, and go to **Network 
Link**. The **New Network Link** dialog box will appear. (See Figure 4.) 
Name your layer in the **Name** field. (This will show up in **My 
Places** on the main Google Earth screen.) Set **Link** to: 

::

   http://localhost:8080/geoserver/wms/kml?layers=topp:states

Click **OK**. You can now save this layer in your **My Places**.

   .. figure:: networklink.png
      :align: center

      *Figure 4: Adding a network link*

Check out the :ref:`google-earth-tutorials` and the 
:ref:`google-earth-kml-styling` for more information. 

