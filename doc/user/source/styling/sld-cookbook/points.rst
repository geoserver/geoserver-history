.. _sld_cook_book_points:

Points
======

Points, in theory, are the simplest shape.  However, there are many different ways that a point can be styled, perhaps moreso than lines and polygons.

This point layer contains information about the major cities of the (fictional) country of Esseldee.  The data contains attribute columns for name and population.  Download this layer.

.. warning:: Add link to point shapefile

Simple point
------------

This example specifies points to be styled as red circles with a diameter of six (6) pixels.

.. figure:: pix/simplepoint.png

.. code-block:: xml 
   :linenos: 

      <FeatureTypeStyle>
        <Rule>
          <PointSymbolizer>
            <Graphic>
              <Mark>
                <WellKnownName>circle</WellKnownName>
                <Fill>
                  <CssParameter name="fill">
                    <ogc:Literal>#FF0000</ogc:Literal>
                  </CssParameter>
                </Fill>
              </Mark>
              <Size>
                <ogc:Literal>6</ogc:Literal>
              </Size>
            </Graphic>
          </PointSymbolizer>
        </Rule>
      </FeatureTypeStyle>

.. warning:: Add link to SLD


Simple point with stroke
------------------------

This example adds a stroke (or edge/border) around the above point colored black with a thickness of two (2) pixels.

.. figure:: pix/simplepointwithstroke.png

.. code-block:: xml 
   :linenos: 

      <FeatureTypeStyle>
        <Rule>
          <PointSymbolizer>
            <Graphic>
              <Mark>
                <WellKnownName>circle</WellKnownName>
                <Fill>
                  <CssParameter name="fill">
                    <ogc:Literal>#FF0000</ogc:Literal>
                  </CssParameter>
                </Fill>
                <Stroke>
                  <CssParameter name="stroke">
                    <ogc:Literal>#000000</ogc:Literal>
                  </CssParameter>
                  <CssParameter name="stroke-width">
                    <ogc:Literal>2</ogc:Literal>
                  </CssParameter>
                </Stroke>
              </Mark>
              <Size>
                <ogc:Literal>6</ogc:Literal>
              </Size>
            </Graphic>
          </PointSymbolizer>
        </Rule>
      </FeatureTypeStyle>

.. warning:: Add link to SLD

Rotated square
--------------

This example uses a square instead of a circle, colors it green, sizes it larger, and rotates the square by 45 degrees.

.. figure:: pix/rotatedsquare.png

.. code-block:: xml 
   :linenos: 

      <FeatureTypeStyle>
        <Rule>
          <PointSymbolizer>
            <Graphic>
              <Mark>
                <WellKnownName>square</WellKnownName>
                <Fill>
                  <CssParameter name="fill">
                    <ogc:Literal>#009900</ogc:Literal>
                  </CssParameter>
                </Fill>
              </Mark>
              <Size>
                <ogc:Literal>12</ogc:Literal>
              </Size>
              <Rotation>
                <ogc:Literal>45</ogc:Literal>
              </Rotation>
            </Graphic>
          </PointSymbolizer>
        </Rule>
      </FeatureTypeStyle>

.. warning:: Add link to SLD

Transparent triangle
--------------------

The example replaces the shape with a triangle, adds a black stroke with a width of two pixels, and sets the fill of the triangle to 20% opacity (partially transparent).

.. figure:: pix/transparenttriangle.png

.. code-block:: xml 
   :linenos:

      <FeatureTypeStyle>
        <Rule>
          <PointSymbolizer>
            <Graphic>
              <Mark>
                <WellKnownName>triangle</WellKnownName>
                <Fill>
                  <CssParameter name="fill">
                    <ogc:Literal>#009900</ogc:Literal>
                  </CssParameter>
                  <CssParameter name="fill-opacity">
                    <ogc:Literal>0.2</ogc:Literal>
                  </CssParameter>
                </Fill>
                <Stroke>
                  <CssParameter name="stroke">
                    <ogc:Literal>#000000</ogc:Literal>
                  </CssParameter>
                  <CssParameter name="stroke-width">
                    <ogc:Literal>2</ogc:Literal>
                  </CssParameter>
                </Stroke>
              </Mark>
              <Size>
                <ogc:Literal>12</ogc:Literal>
              </Size>
            </Graphic>
          </PointSymbolizer>
        </Rule>
      </FeatureTypeStyle>

.. warning:: Add link to SLD

Point as graphic
----------------

This examples styles points as a graphic instead of a simple shape (with the graphic saved in the same directory as the style).

.. figure:: pix/pointasgraphic.png

.. code-block:: xml 
   :linenos:

      <FeatureTypeStyle>
        <Rule>
          <PointSymbolizer>
            <Graphic>
              <ExternalGraphic>
                <OnlineResource
                  xlink:type="simple"
                  xlink:href="smileyface.png"/>
                <Format>image/png</Format>
              </ExternalGraphic>
              <Size>
                <ogc:Literal>32</ogc:Literal>
              </Size>
            </Graphic>
          </PointSymbolizer>
        </Rule>
      </FeatureTypeStyle>
	  
.. warning:: Add link to SLD

Point with default label
------------------------

This example shows a text label on the simple point, showing the name of the point.  In the absence of any other customization, this is how a label will be displayed.

.. figure:: pix/pointwithdefaultlabel.png

.. code-block:: xml 
   :linenos:

      <FeatureTypeStyle>
        <Rule>
          <PointSymbolizer>
            <Graphic>
              <Mark>
                <WellKnownName>circle</WellKnownName>
                <Fill>
                  <CssParameter name="fill">
                    <ogc:Literal>#FF0000</ogc:Literal>
                  </CssParameter>
                </Fill>
              </Mark>
              <Size>
                <ogc:Literal>6</ogc:Literal>
              </Size>
            </Graphic>
          </PointSymbolizer>
          <TextSymbolizer>
            <Label>
              <ogc:PropertyName>name</ogc:PropertyName>
            </Label>
            <Font />
            <Fill>
              <CssParameter name="fill">
                <ogc:Literal>#000000</ogc:Literal>
              </CssParameter>
            </Fill>
          </TextSymbolizer>
        </Rule>
      </FeatureTypeStyle>

.. warning:: Add link to SLD

Point with styled label
-----------------------

This example centers the label above the point.

.. figure:: pix/pointwithstyledlabel.png

.. code-block:: xml 
   :linenos:

      <FeatureTypeStyle>
        <Rule>
          <PointSymbolizer>
            <Graphic>
              <Mark>
                <WellKnownName>circle</WellKnownName>
                <Fill>
                  <CssParameter name="fill">
                    <ogc:Literal>#FF0000</ogc:Literal>
                  </CssParameter>
                </Fill>
              </Mark>
              <Size>
                <ogc:Literal>6</ogc:Literal>
              </Size>
            </Graphic>
          </PointSymbolizer>
          <TextSymbolizer>
            <Label>
              <ogc:PropertyName>name</ogc:PropertyName>
            </Label>
            <Font>
              <CssParameter name="font-family">
                <ogc:Literal>Arial</ogc:Literal>
              </CssParameter>
              <CssParameter name="font-size">
                <ogc:Literal>12</ogc:Literal>
              </CssParameter>
              <CssParameter name="font-style">
                <ogc:Literal>normal</ogc:Literal>
              </CssParameter>
              <CssParameter name="font-weight">
                <ogc:Literal>bold</ogc:Literal>
              </CssParameter>
            </Font>
            <LabelPlacement>
              <PointPlacement>
                <AnchorPoint>
                  <AnchorPointX>
                    <ogc:Literal>0.5</ogc:Literal>
                  </AnchorPointX>
                  <AnchorPointY>
                    <ogc:Literal>0.0</ogc:Literal>
                  </AnchorPointY>
                </AnchorPoint>
                <Displacement>
                  <DisplacementX>
                    <ogc:Literal>0</ogc:Literal>
                  </DisplacementX>
                  <DisplacementY>
                    <ogc:Literal>5</ogc:Literal>
                  </DisplacementY>
                </Displacement>
              </PointPlacement>
            </LabelPlacement>
            <Fill>
              <CssParameter name="fill">
                <ogc:Literal>#000000</ogc:Literal>
              </CssParameter>
            </Fill>
          </TextSymbolizer>
        </Rule>
      </FeatureTypeStyle>

.. warning:: Add link to SLD

Point with rotated label
------------------------

This example rotates the previous label by 45 degrees and adds 20 pixels of displacement to make the label farther away from the point.

.. figure:: pix/pointwithrotatedlabel.png

.. code-block:: xml 
   :linenos:

      <FeatureTypeStyle>
        <Rule>
          <PointSymbolizer>
            <Graphic>
              <Mark>
                <WellKnownName>circle</WellKnownName>
                <Fill>
                  <CssParameter name="fill">
                    <ogc:Literal>#FF0000</ogc:Literal>
                  </CssParameter>
                </Fill>
              </Mark>
              <Size>
                <ogc:Literal>6</ogc:Literal>
              </Size>
            </Graphic>
          </PointSymbolizer>
          <TextSymbolizer>
            <Label>
              <ogc:PropertyName>name</ogc:PropertyName>
            </Label>
            <Font>
              <CssParameter name="font-family">
                <ogc:Literal>Arial</ogc:Literal>
              </CssParameter>
              <CssParameter name="font-size">
                <ogc:Literal>12</ogc:Literal>
              </CssParameter>
              <CssParameter name="font-style">
                <ogc:Literal>normal</ogc:Literal>
              </CssParameter>
              <CssParameter name="font-weight">
                <ogc:Literal>bold</ogc:Literal>
              </CssParameter>
            </Font>
            <LabelPlacement>
              <PointPlacement>
                <AnchorPoint>
                  <AnchorPointX>
                    <ogc:Literal>0.5</ogc:Literal>
                  </AnchorPointX>
                  <AnchorPointY>
                    <ogc:Literal>0.0</ogc:Literal>
                  </AnchorPointY>
                </AnchorPoint>
                <Displacement>
                  <DisplacementX>
                    <ogc:Literal>0</ogc:Literal>
                  </DisplacementX>
                  <DisplacementY>
                    <ogc:Literal>25</ogc:Literal>
                  </DisplacementY>
                </Displacement>
                <Rotation>
                  <ogc:Literal>315</ogc:Literal>
                </Rotation>
              </PointPlacement>
            </LabelPlacement>
            <Fill>
              <CssParameter name="fill">
                <ogc:Literal>#990099</ogc:Literal>
              </CssParameter>
            </Fill>
          </TextSymbolizer>
        </Rule>
      </FeatureTypeStyle>

.. warning:: Add link to SLD

Attribute-based style
---------------------

This examples alters the look of the points based on the population attribute.  

.. warning:: Explain specifics

.. warning:: Add code

.. warning:: Add screenshot

.. warning:: Add link to SLD

Zoom-based style
----------------

This example alters the look of the points based on zoom level.

.. warning:: Explain specifics

.. warning:: Add code

.. warning:: Add screenshot

.. warning:: Add link to SLD


