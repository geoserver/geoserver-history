<?xml version="1.0" encoding="ISO-8859-1"?>
<StyledLayerDescriptor version="1.0.0" xsi:schemaLocation="http://www.opengis.net/sld StyledLayerDescriptor.xsd" xmlns="http://www.opengis.net/sld" xmlns:ogc="http://www.opengis.net/ogc" xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
<!-- a named layer is the basic building block of an sld document -->

<Name>A Test Layer</Name>
<Title>The title of the layer</Title>
<Abstract>
A styling layer used for the unit tests of sldstyler
</Abstract>
<!-- with in a layer you have Named Styles -->
<NamedLayer>
    <!-- again they have names, titles and abstracts -->
  <Name>sillypolyshp</Name>
  <UserStyle>
    <!-- FeatureTypeStyles describe how to render different features -->
    <!-- a feature type for polygons -->
    <FeatureTypeStyle>
      <Rule>
        <!-- like a linesymbolizer but with a fill too -->
        <PolygonSymbolizer>
          
          <Fill>

            <GraphicFill>
                <Graphic>
                   
                    <Mark>
                        <WellKnownName>circle</WellKnownName>
                        <Fill>
                            <!-- CssParameters allowed are fill (the color) and fill-opacity -->
                            <CssParameter name="fill">#FFFF00</CssParameter>
            			<CssParameter name="fill-opacity">0.5</CssParameter>
                        </Fill>
			<Stroke>
			    <CssParameter name="stroke">#FFFF00</CssParameter>
			     <CssParameter name="stroke-width">1</CssParameter>
		       </Stroke>


                    </Mark>
                                             <Size>10</Size>
                </Graphic>
            </GraphicFill>
          </Fill>
            <Stroke>
	                  <CssParameter name="stroke-width">
	                  <ogc:Literal>2 </ogc:Literal>
	                  </CssParameter>
	                  <CssParameter name="stroke">#FFaaaa</CssParameter>
          </Stroke>
        </PolygonSymbolizer>
      </Rule>
    </FeatureTypeStyle>
</UserStyle>
</NamedLayer>
</StyledLayerDescriptor>

