.. _release_testing_checklist:

Release Testing Checklist
=========================

A checklist of things to manually test for every release.

Sample requests
---------------

Go to the sample request page, http://localhost:8080/geoserver/demoRequest.do, 
and execute every sample request, ensuring the correct response for each 
request.

Map preview
-----------

#. Go to the map preview page, http://localhost:8080/geoserver/mapPreview.do
#. Click the ``OpenLayers`` link next to ``nurc:ArcSample``

   .. image:: arc_sample.png 

#. Go back to the map preview and click the ``GeoRSS`` link next to 
   ``topp:states`` 

   .. image:: states_georss.png

#. Go back to the map preview and click the ``OpenLayers`` link next to 
   ``topp:states``.
#. Enable the options toolbar and specify the CQL filter:: 

     STATE_ABBR EQ 'TX'

   .. image:: states_cql.png

KML
---

#. Go back to the map preview and click the ``KML`` link next to ``topp:states``

#. Open the result in Google Earth

#. Zoom out as far as possible and notice the smaller states (on the east coast)
   disappear.

   .. image:: states_kml_bestguess.png

#. Close Google Earth 

   .. warning::

      If you do not shut down Google Earth it will cache information and throw 
      off the next steps.

#. Go to the feature type editor page for the ``topp:states`` feature type

#. Change the ``KML Regionating Attribute`` to "SAMP_POP" and change the ``KML
   Regionating Strategy`` to "external-sorting"

   .. image:: states_kml_config.png

#. Submit and Apply changes

#. Go back to the map preview page and again click the ``KML`` link next to 
   ``topp:states``, opening the result in Google Earth

#. Zoom out as far as possible and notice the smaller population states (green)
   disappear

   .. image:: states_kml_sort.png

#. Go back to the map preview page and click the ``KML`` link next to 
   ``nurc:Img_Sample``, opening the result in Google Earth

   .. image:: img_sample_kml.png

#. Zoom in and notice tiles load

#. Follow the link http://localhost:8080/geoserver/wms/kml?layers=topp:states&mode=refresh 
   , opening the result in Google Earth

#. Notice the KML reload every time the camera is stopped

#. Append the parameter "kmscore=0" to the above link and open the result in 
   Google Earth

#. Notice the rasterized version of the KML 

   .. image:: states_rasterized.png

#. Follow the link http://localhost:8080/geoserver/wms/kml?layers=topp:states&mode=download
   , saving the result to disk.

#. Examine the file on disk and notice a raw dump of all placemarks for the 
   layer.
