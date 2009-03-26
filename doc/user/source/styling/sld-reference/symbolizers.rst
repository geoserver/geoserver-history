.. _sld_reference_symbolizers:

Symbolizers
===========

The notion of a *symbolizer* is the heart of SLD. A symbolizer specifies how data should be visualized. There are 5 types of
symbolizers:

  .. list-table::
     :widths: 20 80

     * - ``PointSymbolizer``
       - Used to portray **point** data. Supports the ability to specify point shape and size, color, marker shape, and more.
     * - ``LineSymbolizer``
       - Used to portray **line** data. Supports the ability to specify line width, stroke, color, and more.
     * - ``PolygonSymbolizer``
       - Used to portray **polygon** data. Supports to the ability to specify polygon fill color, pattern, border width, and more.
     * - ``RasterSymbolizer``
       - Used to portray **raster** data. Supports the ability to specify channel selection, color maps, and more.
     * - ``TextSymbolizer``
       - Used to portray **text labels**. Supports the ability to specify font, size, color, offset, and more.


PointSymbolizer
---------------

The PointSymbolizer styles **points**,  Points are zero-dimensional elements that contain only position information.

Schema
``````

The outermost element is the ``<Graphic>`` tag.  This determines the type of visualization.  There are five possible tags to include inside the ``<Graphic>`` tag:

.. list-table::
   :widths: 20 20 60
   
   * - **Tag**
     - **Required?**
     - **Description**
   * - ``<ExternalGraphic>``
     - No (when using ``<Mark>``)
     - Specifies an image file to use as the symbolizer.  
   * - ``<Mark>``
     - No (when using ``<ExternalGraphic>``)
     - Specifies a common shape to use as the symbolizer.
   * - ``<Opacity>``
     - No
     - Determines the opacity (transparency) of symbolizers.  Values range from 0 (completely transparent) to 1 (completely opaque).  Default is 1.
   * - ``<Size>``
     - Yes 
     - Determines the size of the symbolizer in pixels.  When used with an image file, this will specify the height of the image, with the width scaled accordingly.
   * - ``<Rotation>``
     - No
     - Determines the rotation of the graphic in degrees.  The rotation increases in the clockwise direction.  Negative values indicate counter-clockwise rotation.  Default is 0.

Within the ``<ExternalGraphic>`` tag, there are also additional parameters:

.. list-table::
   :widths: 20 20 60
   
   * - **Parameter**
     - **Required?**
     - **Description**
   * - ``OnlineResource``
     - Yes
     - The location of the image file.  Can be either a URL or a local path relative to the SLD.
   * - ``Format``
     - Yes
     - The MIME type of the image format.  Most standard web image formats are supported.  

Within the ``<Mark>`` tag, there are also additional parameters:

.. list-table::
   :widths: 20 20 60
   
   * - **Parameter**
     - **Required?**
     - **Description**
   * - ``WellKnownName``
     - Yes
     - The name of the common shape.  Options are circle, square, triangle, star, cross, or x.  Default is square.
   * - ``Fill``
     - No (when using ``Stroke``)
     - Specifies how the symbolizer should be filled.  Options are a ``<CssParameter>`` specifying a color in the form ``#RRGGBB``, or ``<GraphicFill>`` for a repeated graphic.
   * - ``Stroke``
     - No (when using ``Fill``)
     - Specifies how the symbolizer should be drawn on its border.  Options are a ``<CssParameter>`` specifying a color in the form ``#RRGGBB`` or ``<GraphicStroke>`` for a repeated graphic.

     

Example
```````

Consider the following simple symbolizer

.. code-block:: xml 
   :linenos: 

   <PointSymbolizer>
     <Graphic>
       <Mark>
         <Fill><CssParameter name="fill">#FF0000</CssParameter>
       </Mark>
     </Graphic>
     <Size>2</Size>
   </PointSymbolizer>

The above symbolizer specifies the following:
   
   * Data should be rendered as points (``PointSymbolizer``)
   
   * Points should be filled with the color red (``#FF0000``)
  
   * Points should be rendered with a radius of 2 pixels (``Size``)

For more examples, please see the :ref:`sld_cook_book_points` section of the :ref:`sld_cook_book`.
   

LineSymbolizer
--------------

PolygonSymbolizer
-----------------

RasterSymbolizer
----------------

TextSymbolizer
--------------
   