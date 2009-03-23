.. _sld_cook_book_points:

Points
======

Points, in theory, are the simplest shape.  However, there are many different ways that a point can be styled, perhaps more than even lines and polygons.

The point layer that is used for the examples below contains information about the major cities of the (fictional) country of Esseldee.  The data contains attribute columns for city names ("``name``") and population ("``pop``").

.. warning:: Add link to point shapefile

.. warning:: The code examples shown on this page are **not the full SLD code**, as they omit the header and footer information required for SLDs for the sake of brevity.  Please use the links to download the full SLDs for each example.


Simple point
------------

 

This example specifies points to be styled as red circles with a diameter of 6 pixels.

.. figure:: pix/simplepoint.png
   :align: center
   
Details
```````

There is one ``<Rule>`` in one ``<FeatureTypeStyle>`` for this style, which is the simplest possible situation.  All examples will share this characteristic unless otherwise specified.  **Line 6** specifies the shape of the symbol (a circle), with **lines 8-10** determining the fill color to be red (``#FF0000``).  **Lines 13-15** determine the size of the graphic to be six pixels.

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

This example adds a stroke (or border) around the simple point, colored black and with a thickness of 2 pixels.

.. figure:: pix/simplepointwithstroke.png
   :align: center

Details
```````

Much of this example is similar to the simple point mentioned above.  **Lines 12-19** specify the stroke, with **lines 13-15** setting the color to black (``#000000``) and **lines 16-18** setting the width to 2 pixels.  (Omitting lines 16-18 would default the stroke-width to 1 pixel.)   
   
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
   :align: center

Details
```````

**Line 6** sets the shape of the point to be a square, with **lines 8-10** setting the color to a muted green (``#009900``).  **Line 14** sets the size of the square to be 12 pixels, and rotation is set to 45 degrees on **line 17**.
   
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

The example replaces the shape with a triangle, retains the black stroke, and sets the fill of the triangle to 20% opacity (mostly transparent).

.. figure:: pix/transparenttriangle.png
   :align: center

Details
```````

**Line 6** once again sets the shape, in this case to a triangle.  **Lines 8-10** sets the fill color, and **lines 11-13** sets the opacity to 0.2 (20% opaque).  This means the green color will look much lighter on a plain white background, although were the point imposed on a dark background, the resulting color would be different.  **Lines 16-18** and **19-21** determine the stroke color and width, respectively.  Finally, **lines 24-26** set the size of the point to be 12 pixels.
   
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

This examples styles points as a graphic instead of as a simple shape.

.. figure:: pix/pointasgraphic.png
   :align: center

Details
```````

This style uses an external graphic.  **Lines 5-10** specify the details.  Here, the graphic is contained in the same directory as the style (in the ``styles`` directory), however an external URL could have been used.  **Line 8** sets the path to the graphic, with **line 9** indicating the file format (MIME type) of the graphic.  **Lines 11-13** determine the size of the graphic, independent of the actual size of the graphic, although in this case they are the same.   
   
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

This example shows a text label on the simple point, showing the name attribute of the point.  In the absence of any other customization, this is how a label will be displayed.

.. figure:: pix/pointwithdefaultlabel.png
   :align: center

Details
```````

**Lines 3-17** (the ``<PointSymbolizer>``) are identical to the simple point example.  The label style is set in the ``<TextSymbolizer>`` on **lines 18-28**.  **Lines 19-21** determine what text to display, which in this case is the value of the "name" attribute for each point.  **Lines 24-26** set the text color.  Don't forget about **line 22**: Although there is content in the ``<Font />`` tag, it is still required by the OGC SLD specification.

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

This example improves the label style by centering it above the point and specifying font and size.

.. figure:: pix/pointwithstyledlabel.png
   :align: center

Details
```````

**Lines 3-17** indicate the same simple point as used above.  The ``<TextSymbolizer>`` on **lines 18-61** contain much more details about the label than in the previous example.  **Lines 19-21** specify the attribute to use to display the label.  **Lines 22-35** set the font information:  **lines 23-24** set the font to be "Arial", **lines 26-28** set the font size to 12, **lines 29-31** set the font style to "normal", and **lines 32-34** set the font weight to "bold".  **Lines 36-55** (``<LabelPlacement>``) determine the placement of the label relative to the point.  There is the ``<AnchorPoint>`` (**lines 38-45**), which sets the place of intersection between the label and point, which here (**lines 39-41**) is set to halfway (0.5) along the horizontal direction.  There is also ``<Displacement>`` (**lines 46-53**), the offset of the label relative to the line, which in this case is zero pixels in the horizontal direction (**lines 47-49**) and five pixels in the vertical direction (**lines 50-52**).  The net result of this is a centered label placed slightly above the point.  Finally, **lines 57-59** set the font color.   
   
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
   :align: center

Details
```````

Much of this is the same as the styled label above.  There are only three important differences.  **Lines 50-52** specify 25 pixels of vertical displacement (instead of 10 as used before).  **Lines 54-56** specify a rotation of 315 degrees.  (Rotation increases clockwise, so a 45 degree angle offset in this case is actually a 315 degree angle.  Setting this value to 45 would cause the label to move from top left to bottom right.)  Finally, **lines 60-62** set the font color to be purple (``#99099``).   
    
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


Attribute-based point
---------------------

This examples alters the look of the points based on the population attribute.  

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
	 
	 
(The labels have been removed in this example to simplify the style.  Please refer to the previous example to see which points refer to which cities.)  This is the first style in this cook book that contains more than one ``<Rule>``.  There are three, and each refers to the population attribute.  The first rule, on **lines 2-26**, specifies the styling of those points whose population attribute is less than 50,000.  **Lines 5-10** set this filter, with **lines 6-9** setting the "less than" filter, **line 7** denoting the attribute, and and **line 8** with the value of 50,000.  The graphic is a circle (**line 14**), and the size is 8 pixels (**lines 21-23**).  

The second rule (**lines 27-57**) is very similar, specifying a style for points whose population attribute is between 50,000 and 100,000.  The population filter is set on **lines 30-41**, and it is longer than the first rule, because two criteria need to be set: ``PropertyIsGreaterThanOrEqualTo`` and ``PropertyIsLessThan``.  The difference in visualization for the style is the size of the circle, which here (**lines 52-54**) is 12 pixels.

The third rule (**lines 58-82**) specifies a style for points whose population attribute is greater than 100,000.  The population filter is set on **lines 61-66**, and the only difference again is the size of the circle, which in this rule is 16 pixels.

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
                  <CssParameter name="fill">
                    <ogc:Literal>#0033cc</ogc:Literal>
                  </CssParameter>
                </Fill>
              </Mark>
              <Size>
                <ogc:Literal>8</ogc:Literal>
              </Size>
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
                  <CssParameter name="fill">
                    <ogc:Literal>#0033cc</ogc:Literal>
                  </CssParameter>
                </Fill>
              </Mark>
              <Size>
                <ogc:Literal>12</ogc:Literal>
              </Size>
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
                  <CssParameter name="fill">
                    <ogc:Literal>#0033cc</ogc:Literal>
                  </CssParameter>
                </Fill>
              </Mark>
              <Size>
                <ogc:Literal>16</ogc:Literal>
              </Size>
            </Graphic>
          </PointSymbolizer>
        </Rule>
      </FeatureTypeStyle>

.. warning:: Add link to SLD

Zoom-based point
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

Zoom-based styles can some of the most complex SLD files.  When combined with attribute-based styles, SLD files can grow quite cumbersome.  However, zoom-based styles can also make your maps much more realistic, since one is used to seeing things get larger as one zooms in.  That is precisely what this example does.  The points, now all styled identically, will vary in size based on the zoom level.

.. note:: Determining the zoom level is beyond the scope of this page.

.. warning:: Where do we point people who want to know more?

This style contains three rules.  The first rule (**lines 2-20**) is for the largest scale denominator (when the view is "zoomed in").  The zoom is set on **line 4**, applicable to any map with a scale denominator of 160,000,000 or less.  The rule draws a circle (**line 8**) with a size of 12 pixels (**lines 15-17**).  The second rule (**lines 21-40**) is the middle zoom level.  The zoom is set on **lines 23-24**, applicable to any map with a scale denominator between 160,000,000 and 320,000,000 (**lines 23-24**).  The rule draws a circle with a size of 8 pixels (**lines 35-37**).  The third rule (**lines 41-59**) is the most zoomed out level.  The zoom is set on **line 43**, applicable to any map with a scale denominator of 320,000,000 or more.  The rule draws a circle with a size of 4 pixels (**lines 54-56**).

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
                  <CssParameter name="fill">
                    <ogc:Literal>#cc3300</ogc:Literal>
                  </CssParameter>
                </Fill>
              </Mark>
              <Size>
                <ogc:Literal>12</ogc:Literal>
              </Size>
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
                  <CssParameter name="fill">
                    <ogc:Literal>#cc3300</ogc:Literal>
                  </CssParameter>
                </Fill>
              </Mark>
              <Size>
                <ogc:Literal>8</ogc:Literal>
              </Size>
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
                  <CssParameter name="fill">
                    <ogc:Literal>#cc3300</ogc:Literal>
                  </CssParameter>
                </Fill>
              </Mark>
              <Size>
                <ogc:Literal>4</ogc:Literal>
              </Size>
            </Graphic>
          </PointSymbolizer>
        </Rule>
      </FeatureTypeStyle>


.. warning:: Add link to SLD


