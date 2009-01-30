.. _web_admin_config:

Config menu
===========

Logging In
----------

When first navigating to the Config menu, you will be asked to log in.  This security measure prevents unauthorized persons from making changes to GeoServer.  The default username and password is ``admin`` and ``geoserver``.  These can be changed only by editing the ``security/users.properties`` file in the :ref:`data_directory`.  

.. figure:: login.png
   :align: center

   *Figure 1: The login screen*

Enter your username and password in the form, and click *Submit*.  You may also check the box that says "Remember my login on this computer" in order to prevent needing to log in subsequently (although this setting is specific only the computer, and will not globally effect security).

Apply, Save, Load
-----------------

.. note:: Talk about Apply, Save, Load

.. figure:: configmenu.png
   :align: center

   *Figure 2: The Config menu*


Server
------

The Server Configuration page, accessed from the *Server* link, is where global server settings are made.

.. note:: Screenshot of Server page

.. list-table::
   :widths: 20 80

   * - **Option**
     - **Description**
   * - ``Maximum Features``
     -
   * - ``Verbose``	
     -
   * - ``VerboseExceptions``
     -
   * - ``Number of Decimals``
     -
   * - ``Character Set``
     -
   * - ``Proxy base URL`` 	
     -
   * - ``Logging Profile``
     -
   * - ``Suppress StdOut logging``
     -
   * - ``Log Location`` 
     -
   * - ``JAI mem capacity``
     - 	
   * - ``JAI mem threshold``
     -
   * - ``JAI tile threads``
     -
   * - ``JAI tile priority``
     -
   * - ``JAI cache recycling``
     -  	
   * - ``JAI Image-I/O Caching``
     -
   * - ``JAI JPEG native acceleration``
     -
   * - ``JAI PNG native acceleration``
     -
   * - ``JAI Mosaic operation native acceleration``
     - 
   * - ``Tile Cache``	 
     - 

This page also has space for contact information.  This information is contained in the capabilities documents and will be publicly accessible.

.. list-table::
   :widths: 40
   
   * - **Option**
   * - ``Contact Person``
   * - ``Organization``
   * - ``Position``
   * - ``Address Type``
   * - ``Address``
   * - ``City``
   * - ``State/Province``
   * - ``Postal Code``
   * - ``Country``
   * - ``Phone Number``
   * - ``Fax Number``
   * - ``Email Address``

	 
	 
WCS
---

This section is for configuring the Web Coverage Service in GeoServer.

.. note:: Screenshot of WCS

Contents
````````

The WCS Contents page allows the WCS to be enabled or disabled, via a check box.  When disabled, WCS requests will not be processed.  The Online Resource box is a URL which contains information relevant to the WCS.

.. note:: Screenshot of Contents

Description
```````````

The WCS Description page is the area where information about the WCS is populated.  This information is publicly available via the WCS capabilities document.

.. note:: Screenshot of Description

.. list-table::
   :widths: 20 80

   * - **Option**
     - **Description**
   * - ``Name``
     -
   * - ``Title``
     -
   * - ``Access Constraints``
     -
   * - ``Fees``
     -
   * - ``Maintainer``
     -
   * - ``Keywords``
     -
   * - ``Abstract``
     -
 

CoveragePlugins
```````````````

The WCS CoveragePlugins page displays a list of all the coverage formats installed in GeoServer.  Additional coverage formats can be supproted by installing extensions.  Please see the section on :ref:`data` for other formats.

.. note:: Screenshot of default plugins

WFS
---

This section is for configuring the Web Feature Service in GeoServer.

.. note:: Screenshot of WFS

Contents
````````

The WFS Contents page allows cofiguration of the WFS.  The WFS can be enable or disabled, via the **Enabled** check box.  When disabled, WFS requests will not be processed.  There are three other checkboxes:  **srsName as XML** which does WHAT, **Strict CITE Test Conformance**, which does WHAT, and **Generate feature bounds** which does WHAT.  The **Service Level** determines WHAT.  The **Online Resource** box is a URL which contains information relevant to the WFS.

.. note:: Screenshot of WFS Contents

Description
```````````

The WFS Description page is the area where information about the WFS is populated.  This information is publicly available via the WFS capabilities document.

.. note:: Screenshot of Description

.. list-table::
   :widths: 20 80

   * - **Option**
     - **Description**
   * - ``Name``
     -
   * - ``Title``
     -
   * - ``Access Constraints``
     -
   * - ``Fees``
     -
   * - ``Maintainer``
     -
   * - ``Keywords``
     -
   * - ``Abstract``
     -

Validation
``````````

.. note:: Description on Validation

.. note:: Screenshot of Validation ((more than 1?)

WMS
---

This section is for configuring the Web Map Service in GeoServer.

.. note:: Screenshot of WMS

Contents
````````

.. note:: Screenshot of WMS

The WMS Contents page allows the WMS to be enabled or disabled, via the **Enabled** check box.  When disabled, WCS requests will not be processed.  The **Online Resource** box is a URL which contains information relevant to the WCS.

The **Limited Capabilities CRS LIST** is a box listing of the supported spatial reference systems (SRS).  GeoServer supports a large amount of SRSs, and a list of them is contained in the WMS capabilities document, making the document cumbersome.  By default, this box is empty, which means that GeoServer will return all supported SRS.  By populating this box, only those specifically mentioned will be supported.

The **Base Maps** section is where layer groups are configured.  Layer groups are collections of WMS layers that can be conveniently referenced as one.

The page displays a list of currently configured layer groups.  The configuration options for each layer group are:

.. list-table::
   :widths: 20 80

   * - **Option**
     - **Description**
   * - ``Layer-group Name``
     - The name of the layer group.
   * - ``Base Map Layers``
     - A list of layers comprising the layer group, separated by commas.
   * - ``Base Map Styles``
     - The style used for the layer group.  If blank, the layers will use their default configured styles.
   * - ``SRS``
     - The projection to use for the layer group.
   * - ``Envelope``
     - The bounding box for the layer group.  Enter coordinates, or click the **GEnerate** button to automatically generate them.

To create a new layer group, click the **Add New Layer-Group** button.

Description
```````````

The WMS Description page is the area where information about the WMS is populated.  This information is publicly available via the WFS capabilities document.

.. note:: Screenshot of Description

.. list-table::
   :widths: 20 80

   * - **Option**
     - **Description**
   * - ``Name``
     -
   * - ``Title``
     -
   * - ``Access Constraints``
     -
   * - ``Fees``
     -
   * - ``Maintainer``
     -
   * - ``Keywords``
     -
   * - ``Abstract``
     -

Rendering
`````````

.. note:: Screenshot of Rendering

The WMS Rendering page has various options for how to generate WMS tiles.  The **SVG Rendering** option does WHAT.  The **Interpolation** does WHAT.

The other options concern **watermarking**.  CONTINUE.



Data
----

Namespace
`````````

New
'''

CoverageStores
``````````````

New
'''

DataStores
``````````

New
'''

Style
`````

New
'''

FeatureTypes
````````````

New
'''

Coverages
`````````

New
'''