.. _sld_reference:

SLD Reference
=============

*Styled Layer Descriptor* (SLD) is an XML based language for
 specifying the portrayal of Web Map Service (WMS) layers. Like WMS,
 SLD is itself an OGC standard. The current version of SLD is 1.1 and
 the specification is `freely available  <http://www.opengeospatial.org/standards/sld>`_.

The specification provides a good overview of SLD and map styling in
general. It is a recommended read. In this document some of the key
concepts will be further introduced.

Symbolizers
-----------

The notion of a *symbolizer* is at the heart of SLD. A symbolizer
specifies exactly how data should be visualized. There are 5 types of
symbolizer:

  .. list-table::
     :widths: 20 80

     * - ``PointSymbolizer``
       - Used to portray *point* data. Supports the ability to specify point shape and size, color, marker glyph, etc... See :ref:`styling_point_data` for more information.
     * - ``LineSymbolizer``
       - Used to portray *line* data. Supports the ability to specify line width, stroke, and color, etc... See :ref:`styling_line_data` for more information.
     * - ``PolygonSymbolizer``
       - Used to portray *polygon* data. Supports to the ability to specify polygon fill color and pattern, polygon border with and color, etc... See :ref:`styling_polygon_data` for more information.
     * - ``RasterSymbolizer``
       - Used to portray *raster* data. Supports the ability to specify band selection, color maps, etc... See :ref:`styling_raster_data` for more information.
     * - ``TextSymbolizer``
       - Used to portray *labels*. Supports the ability to specify label font, size, color, offset, etc... See :ref:`labeling` for more information.

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

.. _styling_point_data:

Filters
-------

A *filter* is the mechanism in SLD for specifying predicates. Similar in nature to a "WHERE" clause in SQL, filters are the language for specifying which styles should be applied to which features in a data set.

The filter language used by SLD is itself an OGC standard defined in the Filter Encoding specification freely available `here <http://www.opengeospatial.org/standards/filter>`_.

A filter is used to select a subset of features of a dataset to apply a symbolizer to. See the :ref:`rules` section for more information about applying filters. 

There are three types of filters:

Attribute filters
^^^^^^^^^^^^^^^^^

Attribute filters are used to constrain the non-spatial attributes of a feature. Example

.. code-block:: xml 
   :linenos: 
   

   <PropertyIsEqualTo>
      <PropertyName>NAME</PropertyName>
      <Literal>Bob</Literal>
   </PropertyIsEqualTo>

The above filter selects those features which have a {{NAME}} attribute which has a value of "Bob". A variety of equality operators are available:

   * PropertyIsEqualTo
   * PropertyIsNotEqualTo
   * PropertyIsLessThan
   * PropertyIsLessThanOrEqualTo
   * PropertyIsGreatherThan
   * PropertyIsGreatherThanOrEqualTo
   * PropertyIsBetween

Spatial filters
^^^^^^^^^^^^^^^

Spatial filters used to constrain the spatial attributes of a feature. Example

.. code-block:: xml 
   :linenos: 

   <Intersects>
      <PropertyName>GEOMETRY</PropertyName>
      <Literal>
         <gml:Point>
            <gml:coordinates>1 1</gml:coordinates>
         </gml:Point>
      </Literal>
   </Intersects>

The above filter selects those features with a geometry that intersects the point (1,1). A variety of spatial operators are available:

   * Intersects
   * Equals
   * Disjoint
   * Within
   * Overlaps
   * Crosses
   * DWithin
   * Beyond
   * Distance

Logical filters
^^^^^^^^^^^^^^^

Logical filters are used to create combinations of filters using the logical operators And, Or, and Not. Example

.. code-block:: xml 
   :linenos: 

  
   <And>
      <PropertyIsEqualTo>
         <PropertyName>NAME</PropertyName>
         <Literal>Bob</Literal>
      </PropertyIsEqualTo>
      <Intersects>
         <PropertyName>GEOMETRY</PropertyName>
         <Literal>
            <gml:Point>
                <gml:coordinates>1 1</gml:coordinates>
            </gml:Point>
         </Literal>
      </Intersects>
   </And>

.. _rules:

Rules
-----

A *rule* combines a number of symbolizers with a filter to define the portrayal of a feature. Consider the following example:: 



  <Rule>
     <ogc:Filter>
       <ogc:PropertyIsGreaterThan>
         <ogc:PropertyName>POPULATION</ogc:PropertyName>
         <ogc:Literal>100000</ogc:Literal>
       </ogc:PropertyIsGreaterThan>
     </ogc:Filter>
     <PointSymbolizer>
       <Graphic>
         <Mark>
           <Fill><CssParameter name="fill">#FF0000</CssParameter>
         </Mark>
       </Graphic>
     </PointSymbolizer>
  </Rule>



The above rule applies only to features which have a ``POPULATION`` attribute greater than ``100,000`` and symbolizes then with a red point. 

An SLD document can contain many rules. Multiple rule SLD's are the basis for  :ref:`thematic_styling`. Consider the above example expanded::


  <Rule>
     <ogc:Filter>
       <ogc:PropertyIsGreaterThan>
         <ogc:PropertyName>POPULATION</ogc:PropertyName>
         <ogc:Literal>100000</ogc:Literal>
       </ogc:PropertyIsGreaterThan>
     </ogc:Filter>
     <PointSymbolizer>
       <Graphic>
         <Mark>
           <Fill><CssParameter name="fill">#FF0000</CssParameter>
         </Mark>
       </Graphic>
     </PointSymbolizer>
  </Rule>
  <Rule>
     <ogc:Filter>
       <ogc:PropertyIsLessThan>
         <ogc:PropertyName>POPULATION</ogc:PropertyName>
         <ogc:Literal>100000</ogc:Literal>
       </ogc:PropertyIsLessThan>
     </ogc:Filter>
     <PointSymbolizer>
       <Graphic>
         <Mark>
           <Fill><CssParameter name="fill">#0000FF</CssParameter>
         </Mark>
       </Graphic>
     </PointSymbolizer>
  </Rule>

The above snippet defines an additional rule which engages when ``POPULATION`` is less than 100,000 and symbolizes the feature as a green point.

Rules support the notion of *scale dependence* which allows one to specify the scale at which a rule should engage. This allows for different portrayals of a feature based on map scale. Consider the following example:: 



  <Rule>
     <MaxScaleDenominator>20000</MaxScaleDenominator>
     <PointSymbolizer>
       <Graphic>
         <Mark>
           <Fill><CssParameter name="fill">#FF0000</CssParameter>
         </Mark>
       </Graphic>
     </PointSymbolizer>
  </Rule>
  <Rule>
     <MinScaleDenominator>20000</MinScaleDenominator>
     <PointSymbolizer>
       <Graphic>
         <Mark>
           <Fill><CssParameter name="fill">#0000FF</CssParameter>
         </Mark>
       </Graphic>
     </PointSymbolizer>
  </Rule>

The above rules specify that at a scale below ``1:20000`` features are symbolized with red points, and at a scale above ``1:20000`` features are symbolized with blue points.

Styling point data
------------------

.. _styling_line_data:

Styling line data
-----------------

.. _styling_polygon_data:

Styling polygon data
--------------------

.. _styling_raster_data:

Styling raster data
-------------------

.. _labeling:

Labeling
--------

.. _thematic_styling:

Thematic styling
----------------

