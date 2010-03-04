Property Listing
================

This page lists the supported rendering properties.  See :doc:`values` for more
information about the value types for each.

Point Symbology
---------------

.. list-table::

    - * **Property**
      * **Type**
      * **Meaning**
      * **Accepts Expression?**
    - * ``mark``     
      * url,symbol
      * The image or well-known shape to render for points
      * yes
    - * ``mark-geometry`` 
      * expression
      * An expression to use for the geometry when rendering features
      * yes
    - * ``mark-size`` 
      * length   
      * The width to assume for the provided image.  The height will be
        adjusted to preserve the source aspect ratio. 
      * yes
    - * ``mark-rotation``
      * angle 
      * A rotation to be applied (clockwise) to the mark image.
      * yes

Line Symbology
--------------

.. list-table:: 

    - * **Property** 
      * **Type**
      * **Meaning**
      * **Accepts Expression?**
    - * ``stroke``
      * color, url, symbol
      * The color, graphic, or well-known shape to use to stroke lines or outlines
      * yes
    - * ``stroke-geometry``
      * expression
      * An expression to use for the geometry when rendering features. 
      * yes
    - * ``stroke-mime``      
      * string           
      * The mime-type of the external graphic provided.  This is **required**
        when using external graphics
      * yes
    - * ``stroke-opacity``   
      * percentage       
      * A value in the range of 0 (fully transparent) to 1.0 (fully opaque)  
      * yes
    - * ``stroke-width``     
      * length           
      * The width to use for stroking the line.  The image or shape will be
        forced to this size as well.
      * yes
    - * ``stroke-rotation``  
      * angle            
      * A rotation to be applied (clockwise) to the stroke image. See also the
        ``stroke-repeat`` property.
      * yes
    - * ``stroke-linecap``   
      * keyword: miter, butt, square
      * The style to apply to the ends of lines drawn 
      * yes
    - * ``stroke-linejoin``  
      * keyword: miter, butt, square
      * The style to apply to the "elbows" where segments of multi-line features meet. 
      * yes
    - * ``stroke-dasharray`` 
      * list of lengths  
      * The lengths of segments to use in a dashed line. 
      * no
    - * ``stroke-dashoffset``
      * length           
      * How far to offset the dash pattern from the ends of the lines.  
      * yes|
    - * ``stroke-repeat``
      * keyword: repeat, stipple
      * How to use the provided graphic to paint the line.  If repeat, then the
        graphic is repeatedly painted along the length of the line (rotated
        appropriately to match the line's direction).  If stipple, then the line
        is treated as a polygon to be filled.
      * yes

Polygon Symbology
-----------------

.. list-table:: 

    - * **Property** 
      * **Type**
      * **Meaning**
      * **Accepts Expression?**
    - * ``fill``         
      * color, url, symbol 
      * The color, graphic, or well-known shape to use to stroke lines or outlines 
      * yes
    - * ``fill-geometry``
      * expression 
      * An expression to use for the geometry when rendering features. 
      * yes
    - * ``fill-mime``    
      * string            
      * The mime-type of the external graphic provided.  This is *required*
        when using external graphics 
      * yes
    - * ``fill-opacity`` 
      * percentage        
      * A value in the range of 0 (fully transparent) to 1.0 (fully opaque) 
      * yes
    - * ``fill-size``    
      * length            
      * The width to assume for the image or graphic provided. 
      * yes
    - * ``fill-rotation``
      * angle             
      * A rotation to be applied (clockwise) to the fill image. 
      * yes

Text Symbology
--------------

.. list-table:: 

    - * **Property** 
      * **Type**
      * **Meaning**
      * **Accepts Expression?**
    - * ``label``      
      * string
      * The text to display as labels for features
      * yes
    - * ``label-geometry``
      * expression 
      * An expression to use for the geometry when rendering features. 
      * yes
    - * ``font-family``
      * string
      * The name of the font or font family to use for labels
      * yes
    - * ``font-style`` 
      * keyword: normal, italic, oblique
      * The style for the lettering 
      * yes
    - * ``font-weight``
      * keyword: normal, bold
      * The weight for the lettering 
      * yes
    - * ``font-size``  
      * length
      * The size for the font to display. 
      * yes
    - * ``halo-size``  
      * length
      * The size of a halo to display around the lettering (to enhance
        readability). This is *required* to activate the halo feature. 
      * yes
    - * ``halo-color`` 
      * color 
      * The color for the halo 
      * yes
    - * ``halo-opacity``
      * percentage
      * The opacity of the halo, from 0 (fully transparent) to 1.0 (fully opaque). 
      * yes

Shared
------

.. list-table:: 

    - * **Property** 
      * **Type**
      * **Meaning**
      * **Accepts Expression?**
    - * ``geometry``
      * expression 
      * An expression to use for the geometry when rendering features. This
        provides a geometry for all types of symbology, but can be overridden
        by the symbol-specific geometry properties. 
      * yes
