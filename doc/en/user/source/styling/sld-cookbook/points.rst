.. _sld_cook_book_points:

Points
======

Points, in theory, are the simplest shape.  However, there are many different ways that a point can be styled, perhaps more than even lines and polygons.

The point layer that is used for the examples below contains information about the major cities for a fictional country.  The data contains attribute columns for city names ("name") and population ("pop").

.. warning:: Add link to point shapefile

.. warning:: The code examples shown on this page are **not the full SLD code**, as they omit the header and footer information required for SLDs for the sake of brevity.  Please use the links to download the full SLDs for each example.


Simple Point
------------

 

This example specifies points to be styled as red circles with a diameter of 6 pixels.

.. figure:: pix/simplepoint.png
   :align: center
   
Details
```````

There is one ``<Rule>`` in one ``<FeatureTypeStyle>`` for this style, which is the simplest possible situation.  (All subsequent examples will share this characteristic unless otherwise specified.)  **Line 6** specifies the shape of the symbol (a circle), with **line 8** determining the fill color to be red (``#FF0000``).  **Line 11** sets the size of the graphic to be 6 pixels.

Code
````

.. code-block:: xml 
   :linenos: 

      <FeatureTypeStyle>
        <Rule>
          <PointSymbolizer>
            <Graphic>
              <Mark>
                <WellKnownName>circle</WellKnownName>
                <Fill>
                  <CssParameter name="fill">#FF0000</CssParameter>
                </Fill>
              </Mark>
              <Size>6</Size>
            </Graphic>
          </PointSymbolizer>
        </Rule>
      </FeatureTypeStyle>

.. warning:: Add link to SLD


Simple Point With Stroke
------------------------

This example adds a stroke (or border) around the simple point, colored black and with a thickness of 2 pixels.

.. figure:: pix/simplepointwithstroke.png
   :align: center

Details
```````

Much of this example is similar to the simple point mentioned above.  **Lines 10-13** specify the stroke, with **line 11** setting the color to black (``#000000``) and **line 12** setting the width to 2 pixels.  (Omitting line 12 would revert the stroke-width to the default of 1 pixel.)
   
Code
````

.. code-block:: xml 
   :linenos: 

      <FeatureTypeStyle>
        <Rule>
          <PointSymbolizer>
            <Graphic>
              <Mark>
                <WellKnownName>circle</WellKnownName>
                <Fill>
                  <CssParameter name="fill">#FF0000</CssParameter>
                </Fill>
                <Stroke>
                  <CssParameter name="stroke">#000000</CssParameter>
                  <CssParameter name="stroke-width">2</CssParameter>
                </Stroke>
              </Mark>
              <Size>6</Size>
            </Graphic>
          </PointSymbolizer>
        </Rule>
      </FeatureTypeStyle>

.. warning:: Add link to SLD


Rotated Square
--------------

This example uses a square instead of a circle, colors it green, sizes it larger, and rotates the square by 45 degrees.

.. figure:: pix/rotatedsquare.png
   :align: center

Details
```````

**Line 6** sets the shape of the point to be a square, with **line 8** setting the color to a muted green (``#009900``).  **Line 11** sets the size of the square to be 12 pixels, and rotation is set to 45 degrees on **line 12**.
   
Code
````

.. code-block:: xml 
   :linenos: 

      <FeatureTypeStyle>
        <Rule>
          <PointSymbolizer>
            <Graphic>
              <Mark>
                <WellKnownName>square</WellKnownName>
                <Fill>
                  <CssParameter name="fill">#009900</CssParameter>
                </Fill>
              </Mark>
              <Size>12</Size>
              <Rotation>45</Rotation>
            </Graphic>
          </PointSymbolizer>
        </Rule>
      </FeatureTypeStyle>

.. warning:: Add link to SLD


Transparent Triangle
--------------------

The example replaces the shape with a triangle, retains the black stroke, and sets the fill of the triangle to 20% opacity (mostly transparent).

.. figure:: pix/transparenttriangle.png
   :align: center

Details
```````

**Line 6** once again sets the shape, in this case to a triangle.  **Line 8** sets the fill color, and **line 9** sets the opacity to 0.2 (20% opaque).  This means the green color looks much lighter on a plain white background, although were the point imposed on a dark background, the resulting color would be different.  **Line 12** and **line 13** determine the stroke color and width, respectively.  Finally, **line 16** sets the size of the point to be 12 pixels.
   
Code
````   
   
.. code-block:: xml 
   :linenos:

      <FeatureTypeStyle>
        <Rule>
          <PointSymbolizer>
            <Graphic>
              <Mark>
                <WellKnownName>triangle</WellKnownName>
                <Fill>
                  <CssParameter name="fill">#009900</CssParameter>
                  <CssParameter name="fill-opacity">0.2</CssParameter>
                </Fill>
                <Stroke>
                  <CssParameter name="stroke">#000000</CssParameter>
                  <CssParameter name="stroke-width">2</CssParameter>
                </Stroke>
              </Mark>
              <Size>12</Size>
            </Graphic>
          </PointSymbolizer>
        </Rule>
      </FeatureTypeStyle>

.. warning:: Add link to SLD


Point As Graphic
----------------

This examples styles points as a graphic instead of as a simple shape.

.. figure:: pix/pointasgraphic.png
   :align: center

Details
```````

This style uses an external graphic.  **Lines 5-10** specify the details.  Here, the graphic is contained in the same directory as the style (the ``styles`` directory), so no path information is necessary, however an external URL could have been used.  **Line 8** sets the path and file name of the graphic, with **line 9** indicating the file format (MIME type) of the graphic (``image/png``).  **Lines 11** determine the size of the displayed graphic, which can be independent of the actual size of the graphic itself, although in this case they are the same (32 pixels).
   
Code
````

.. code-block:: xml 
   :linenos:

      <FeatureTypeStyle>
        <Rule>
          <PointSymbolizer>
            <Graphic>
              <ExternalGraphic>
                <OnlineResource
                  xlink:type="simple"
                  xlink:href="smileyface.png" />
                <Format>image/png</Format>
              </ExternalGraphic>
              <Size>32</Size>
            </Graphic>
          </PointSymbolizer>
        </Rule>
      </FeatureTypeStyle>
	  
.. warning:: Add link to SLD


Point With Default Label
------------------------

This example shows a text label on the simple point, showing the "name" attribute of the point.  In the absence of any other customization, this is how a label will be displayed.

.. figure:: pix/pointwithdefaultlabel.png
   :align: center

Details
```````

**Lines 3-13** (the ``<PointSymbolizer>``) are identical to the simple point example.  The label style is set in the ``<TextSymbolizer>`` on **lines 14-22**.  **Lines 15-17** determine what text to display, which in this case is the value of the "name" attribute.  **Line 20** sets the text color.  Don't forget about **line 18**: although there isn't any content in the ``<Font />`` tag, it is still required by the OGC SLD specification.

Code
````

.. code-block:: xml 
   :linenos:

      <FeatureTypeStyle>
        <Rule>
          <PointSymbolizer>
            <Graphic>
              <Mark>
                <WellKnownName>circle</WellKnownName>
                <Fill>
                  <CssParameter name="fill">#FF0000</CssParameter>
                </Fill>
              </Mark>
              <Size>6</Size>
            </Graphic>
          </PointSymbolizer>
          <TextSymbolizer>
            <Label>
              <ogc:PropertyName>name</ogc:PropertyName>
            </Label>
            <Font />
            <Fill>
              <CssParameter name="fill">#000000</CssParameter>
            </Fill>
          </TextSymbolizer>
        </Rule>
      </FeatureTypeStyle>

.. warning:: Add link to SLD


Point With Styled Label
-----------------------

This example improves the label style by centering the label above the point and specifying the font name and size.

.. figure:: pix/pointwithstyledlabel.png
   :align: center

Details
```````

**Lines 3-13** indicate the same simple point as above.  The ``<TextSymbolizer>`` on **lines 14-39** contain much more details about the label than in the previous example.  **Lines 15-17** specify the attribute to use to display the label (again, "name").  **Lines 18-23** set the font information:  **line 19** sets the font to be "Arial", **line 20** sets the font size to 12, **line 21** sets the font style to "normal", and **line 22** sets the font weight to "bold".  **Lines 24-35** (``<LabelPlacement>``) determine the placement of the label relative to the point.  There is the ``<AnchorPoint>`` (**lines 26-29**), which sets the place of intersection between the label and point, which here (**line 27**) is set to halfway (0.5) along the horizontal direction.  There is also ``<Displacement>`` (**lines 30-33**), the offset of the label relative to the line, which in this case is 0 pixels in the horizontal direction (**line 31**) and 5 pixels in the vertical direction (**lines 32**).  Finally, **line 37** sets the font color to black (``#000000``).

The net result is a centered label placed slightly above the point.

Code
````   

.. code-block:: xml 
   :linenos:

      <FeatureTypeStyle>
        <Rule>
          <PointSymbolizer>
            <Graphic>
              <Mark>
                <WellKnownName>circle</WellKnownName>
                <Fill>
                  <CssParameter name="fill">#FF0000</CssParameter>
                </Fill>
              </Mark>
              <Size>6</Size>
            </Graphic>
          </PointSymbolizer>
          <TextSymbolizer>
            <Label>
              <ogc:PropertyName>name</ogc:PropertyName>
            </Label>
            <Font>
              <CssParameter name="font-family">Arial</CssParameter>
              <CssParameter name="font-size">12</CssParameter>
              <CssParameter name="font-style">normal</CssParameter>
              <CssParameter name="font-weight">bold</CssParameter>
            </Font>
            <LabelPlacement>
              <PointPlacement>
                <AnchorPoint>
                  <AnchorPointX>0.5</AnchorPointX>
                  <AnchorPointY>0.0</AnchorPointY>
                </AnchorPoint>
                <Displacement>
                  <DisplacementX>0</DisplacementX>
                  <DisplacementY>5</DisplacementY>
                </Displacement>
              </PointPlacement>
            </LabelPlacement>
            <Fill>
              <CssParameter name="fill">#000000</CssParameter>
            </Fill>
          </TextSymbolizer>
        </Rule>
      </FeatureTypeStyle>

.. warning:: Add link to SLD


Point With Rotated Label
------------------------

This example rotates the previous label by 45 degrees and sets 25 pixels of displacement to make the label farther away from the point.

.. figure:: pix/pointwithrotatedlabel.png
   :align: center

Details
```````

Much of this is the same as the styled label above.  There are only three important differences.  **Line 32** specifies 25 pixels of vertical displacement (instead of 5 pixels as used before).  **Lines 54-56** specify a rotation of 45 degrees counter-clockwse.  (Rotation values increase clockwise, which is why the value is negative.)  Finally, **line 38** sets the font color to be a shade of purple (``#99099``).   
    
Code
````
 
.. code-block:: xml 
   :linenos:

      <FeatureTypeStyle>
        <Rule>
          <PointSymbolizer>
            <Graphic>
              <Mark>
                <WellKnownName>circle</WellKnownName>
                <Fill>
                  <CssParameter name="fill">#FF0000</CssParameter>
                </Fill>
              </Mark>
              <Size>6</Size>
            </Graphic>
          </PointSymbolizer>
          <TextSymbolizer>
            <Label>
              <ogc:PropertyName>name</ogc:PropertyName>
            </Label>
            <Font>
              <CssParameter name="font-family">Arial</CssParameter>
              <CssParameter name="font-size">12</CssParameter>
              <CssParameter name="font-style">normal</CssParameter>
              <CssParameter name="font-weight">bold</CssParameter>
            </Font>
            <LabelPlacement>
              <PointPlacement>
                <AnchorPoint>
                  <AnchorPointX>0.5</AnchorPointX>
                  <AnchorPointY>0.0</AnchorPointY>
                </AnchorPoint>
                <Displacement>
                  <DisplacementX>0</DisplacementX>
                  <DisplacementY>25</DisplacementY>
                </Displacement>
                <Rotation>-45</Rotation>
              </PointPlacement>
            </LabelPlacement>
            <Fill>
              <CssParameter name="fill">#990099</CssParameter>
            </Fill>
          </TextSymbolizer>
        </Rule>
      </FeatureTypeStyle>

.. warning:: Add link to SLD


Attribute-Based Point
---------------------

This example alters the look of the points based on the population attribute.  

.. figure:: pix/attributebasedpoint.png
   :align: center
   
   
Details
```````
   
The populations of the cities are as follows:

.. list-table::
   :widths: 20 80
   
   * - **City ("name")**
     - **Population ("pop")**
   * - Borfin
     - 157860
   * - Supox City
     - 578231
   * - Ruckis
     - 98159
   * - Thisland
     - 34879
   * - Synopolis
     - 24567
   * - San Glissando
     - 76024
   * - Detrainia
     - 205609
	 
	 
(The labels have been removed in this example to simplify the style.  Please refer to the previous example to see which points refer to which cities.)

This style contains more than one ``<Rule>``.  Each rule varies the style based on the value of the population ("pop") attribute for each point.  The order of the rules does not matter.

The first rule, on **lines 2-22**, specifies the styling of those points whose population attribute is less than 50,000.  **Lines 5-10** set this filter, with **lines 6-9** setting the "less than" filter, **line 7** denoting the attribute, and and **line 8** with the value of 50,000.  The graphic is a circle (**line 14**), and the size is 8 pixels (**line 19**).  

The second rule (**lines 23-49**) is very similar, specifying a style for points whose population attribute is between 50,000 and 100,000.  The population filter is set on **lines 36-37**, and it is longer than the first rule because two criteria need to be set: ``PropertyIsGreaterThanOrEqualTo`` and ``PropertyIsLessThan``.  (Notice the ``And`` in **lines 27** and **36**.)  The size of the graphic is changed to be 12 pixels on **line 46**.

The third rule (**lines 50-70**) specifies a style for points whose population attribute is greater than 100,000.  The population filter is set on **lines 53-58**, and the only other difference again is the size of the circle, which in this rule (**line 67**) is 16 pixels.

The result of this style is that cities with a larger population have larger points.


Code
````

.. code-block:: xml 
   :linenos:

      <FeatureTypeStyle>
        <Rule>
          <Name>SmallPop</Name>
          <Title>1 to 50000</Title>
          <ogc:Filter>
            <ogc:PropertyIsLessThan>
              <ogc:PropertyName>pop</ogc:PropertyName>
              <ogc:Literal>50000</ogc:Literal>
            </ogc:PropertyIsLessThan>
          </ogc:Filter>
          <PointSymbolizer>
            <Graphic>
              <Mark>
                <WellKnownName>circle</WellKnownName>
                <Fill>
                  <CssParameter name="fill">#0033CC</CssParameter>
                </Fill>
              </Mark>
              <Size>8</Size>
            </Graphic>
          </PointSymbolizer>
        </Rule>
        <Rule>
          <Name>MediumPop</Name>
          <Title>50000 to 100000</Title>
          <ogc:Filter>
            <ogc:And>
              <ogc:PropertyIsGreaterThanOrEqualTo>
                <ogc:PropertyName>pop</ogc:PropertyName>
                <ogc:Literal>50000</ogc:Literal>
              </ogc:PropertyIsGreaterThanOrEqualTo>
              <ogc:PropertyIsLessThan>
                <ogc:PropertyName>pop</ogc:PropertyName>
                <ogc:Literal>100000</ogc:Literal>
              </ogc:PropertyIsLessThan>
            </ogc:And>
          </ogc:Filter>
          <PointSymbolizer>
            <Graphic>
              <Mark>
                <WellKnownName>circle</WellKnownName>
                <Fill>
                  <CssParameter name="fill">#0033CC</CssParameter>
                </Fill>
              </Mark>
              <Size>12</Size>
            </Graphic>
          </PointSymbolizer>
        </Rule>
        <Rule>
          <Name>LargePop</Name>
          <Title>Greater than 100000</Title>
          <ogc:Filter>
            <ogc:PropertyIsGreaterThanOrEqualTo>
              <ogc:PropertyName>pop</ogc:PropertyName>
              <ogc:Literal>100000</ogc:Literal>
            </ogc:PropertyIsGreaterThanOrEqualTo>
          </ogc:Filter>
          <PointSymbolizer>
            <Graphic>
              <Mark>
                <WellKnownName>circle</WellKnownName>
                <Fill>
                  <CssParameter name="fill">#0033CC</CssParameter>
                </Fill>
              </Mark>
              <Size>16</Size>
            </Graphic>
          </PointSymbolizer>
        </Rule>
      </FeatureTypeStyle>

.. warning:: Add link to SLD

Zoom-Based Point
----------------

This example alters the look of the points based on zoom level.

.. figure:: pix/pointzoomsmall.png
   :align: center
   
   *Zoomed out*

.. figure:: pix/pointzoommedium.png
   :align: center
   
   *Partially zoomed*

.. figure:: pix/pointzoomlarge.png
   :align: center

   *Zoomed in*
   
Details
```````

Zoom-based styles can be some of the most complex SLD files.  When combined with attribute-based styles, SLD files can grow quite cumbersome.  However, zoom-based styles can also make your maps much more realistic, since one is used to seeing things get larger as one zooms in.  That is precisely what this example shows.  The points (now all styled identically) will vary in size based on the zoom level.

.. note:: Determining the zoom level is beyond the scope of this page.

.. warning:: Where do we point people who want to know more?

This style contains three rules.  (The order of the rules does not matter.)  The first rule (**lines 2-16**) is for the largest scale denominator (when the view is "zoomed in").  The zoom is set on **line 4**, applicable to any map with a scale denominator of 160,000,000 or less.  The rule draws a circle (**line 8**) with a size of 12 pixels (**line 13**).  The second rule (**lines 17-32**) is the middle zoom level.  The zoom is set on **lines 19-20**, applicable to any map with a scale denominator between 160,000,000 and 320,000,000.  The rule draws a circle with a size of 8 pixels (**line 29**).  The third rule (**lines 33-47**) is the display when the map is the most zoomed out.  The zoom is set on **line 35**, applicable to any map with a scale denominator of 320,000,000 or more.  The rule draws a circle with a size of 4 pixels (**lines 44**).

The result of this style is that points are drawn larger as one zooms in and smaller as one zooms out.


Code
````

.. code-block:: xml 
   :linenos:

      <FeatureTypeStyle>
        <Rule>
          <Name>Large</Name>
          <MaxScaleDenominator>160000000</MaxScaleDenominator>
          <PointSymbolizer>
            <Graphic>
              <Mark>
                <WellKnownName>circle</WellKnownName>
                <Fill>
                  <CssParameter name="fill">#CC3300</CssParameter>
                </Fill>
              </Mark>
              <Size>12</Size>
            </Graphic>
          </PointSymbolizer>
        </Rule>
        <Rule>
          <Name>Medium</Name>
          <MinScaleDenominator>160000000</MinScaleDenominator>
          <MaxScaleDenominator>320000000</MaxScaleDenominator>
          <PointSymbolizer>
            <Graphic>
              <Mark>
                <WellKnownName>circle</WellKnownName>
                <Fill>
                  <CssParameter name="fill">#CC3300</CssParameter>
                </Fill>
              </Mark>
              <Size>8</Size>
            </Graphic>
          </PointSymbolizer>
        </Rule>
        <Rule>
          <Name>Small</Name>
          <MinScaleDenominator>320000000</MinScaleDenominator>
          <PointSymbolizer>
            <Graphic>
              <Mark>
                <WellKnownName>circle</WellKnownName>
                <Fill>
                  <CssParameter name="fill">#CC3300</CssParameter>
                </Fill>
              </Mark>
              <Size>4</Size>
            </Graphic>
          </PointSymbolizer>
        </Rule>
      </FeatureTypeStyle>


.. warning:: Add link to SLD


