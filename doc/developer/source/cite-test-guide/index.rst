.. _cite_test_guide:

Cite Test Guide
===============

A step by step guide to the GeoServer Compliance Interoperability Test Engine (CITE).

.. contents::
   :depth: 2

Check out CITE tools
--------------------

#. Check out the cite tools from subversion::

     svn co http://svn.codehaus.org/geoserver/trunk/community/cite cite

#. From the ``cite`` directory check out the test engine::

     cd cite
     svn co -r 288 http://teamengine.svn.sourceforge.net/svnroot/teamengine/trunk engine

#. From the ``cite`` directory check out the test sources:: 

     svn co -r 2363 https://svn.opengeospatial.org:8443/ogc-projects/cite/trunk tests

   .. warning::

    To check out the test sources an account on the OGG portal is required. If
    you do not have one ask on the developer list for someone to check out the
    tests for you.

Build the CITE tools
--------------------

#. From the ``cite`` directory execute the following command::

     mvn clean install

Run WFS 1.0 tests
-----------------

.. note::

   Running WFS 1.0 tests require PostGIS to be installed on the system.

#. Create a PostGIS user named "cite"::

     createuser cite

#. Create a PostGIS databased named "cite", owned by the "cite" user::

     createdb -T template_postgis -U cite cite

#. Change directory to the ``citewfs-1.0`` data directory and execute the script
   ``cite_data.sql``::

     psql -U cite cite < cite_data.sql

#. Start GeoServer with the ``citewfs-1.0`` data directory

#. Change back to the ``cite`` directory and start up the test engine::

     sh run.sh wfs-1.0.0

   .. note::

      The test engine can take a few minutes to start up.
    
#. On the setup screen:

   #. Under ``Capabilities Setup`` enter the url::

        http://localhost:8080/geoserver/wfs?service=wfs&request=getcapabilities&version=1.0.0

   #. Under ``Optional Tests`` ensure ``Enable tests with complex property 
      values`` is *unchecked*

   #. Under ``Function Groups`` ensure ``All`` is *checked*

   #. Click the ``OK`` button

.. note::

   The wfs 1.0 test suite takes several minutes to run. 
 
Run WFS 1.1 tests
-----------------

.. note::

   Running the wfs 1.0 test suite requires that GeoServer is running with
   the H2 extension enabled.

#. Start GeoServer with the ``citewfs-1.1`` data directory.

#. On the setup screen:

   #. Under ``Service Metadata`` enter the url::

        http://localhost:8080/geoserver/wfs?service=wfs&request=getcapabilities&version=1.1.0

   #. Under ``Supported Conformance Classes``:

      * Ensure ``WFS-Transaction`` is *checked*
      * Ensure ``WFS-Xlink`` is *unchecked*

   #. Under ``GML Simple Features`` ensure ``SF-0`` is selected

   #. Click ``Start``

Run WMS 1.1 tests
-----------------

#. Start GeoServer with the ``citewms-1.1`` data directory.

#. Change back to the ``cite`` directory and start up the test engine::

     sh run.sh wms-1.1.1

#. On the setup screen:

   #. Under ``Capabilities Setup`` enter the url::

        http://localhost:8080/geoserver/wms?&service=wms&request=getcapabilities

   #. Under ``UpdateSequence Values`` enter:

      * Ensure ``Automatic`` is selected
      * "2" for ``value that is lexically higher``
      * "0" for ``value that is lexically lower``

   #. Under ``Certification Profile`` ensure ``QUERYABLE`` is selected

   #. Under ``Optional Tests``:

      * Ensure ``Recommendation Support`` is *checked*
      * Ensure ``GML FeatureInfo`` is *checked*
      * Ensure ``Fees and Access Constraints`` is *checked*
      * For ``BoundingBox Constraints`` ensure ``Either`` is selected
     
   #. Click ``OK``

Run WCS 1.1 tests
-----------------

#. Start GeoServer with the ``citewcs-1.1`` data directory.

#. Change back to the ``cite`` directory and start up the test engine::
    
      sh run.sh wcs-1.1.1

#. On the setup screen:

   #. Enter the url::

         http://localhost:8080/geoserver/wcs?&service=wcsrequest=getcapabilities&version=1.1.1
     
      And click ``Next``

   #. Accept the default values and click ``Submit``

Run WCS 1.0 tests
-----------------

.. warning:: 

   The WCS specification does not allow a cite compliant WCS 1.0 and
   1.1 version to co-exist. To successfully run the WCS 1.0 cite tests
   the ``wms1_1-<VERSION>.jar`` must be removed from the geoserver 
   ``WEB-INF/lib`` directory.
   
#. Remove the ``wcs1_1-<VERSION>.jar`` from ``WEB-INF/lib`` directory.

#. Start GeoServer with the ``citewcs-1.0`` data directory.

#. Change back to the ``cite`` directory and start up the test engine::

     sh run.sh wcs-1.0.0

#. On the setup screen:

   #. Under ``Capabilities Setup`` enter the url::
        
        http://localhost:8080/geoserver/wcs?service=wcs&request=getcapabilities&version=1.0.0

   #. Under ``MIME Header Setup`` enter "image/tiff"

   #. Under ``Update Sequence Values``:

      * "2" for ``value that is lexically higher``
      * "0" for ``value that is lexically lower``

   #. Under ``Grid Resolutions`` enter:

      * "0.1" for ``RESX``
      * "0.1" for ``RESY``

   #. Under ``Options``:
  
      * Ensure ``Verify that the server supports XML encoding`` is *checked*
      * Ensure ``Verify that the server supports range set axis`` is *checked*

   #. Under ``Schemas`` ensure that ``original schemas`` is selected

   #. Click ``OK``
