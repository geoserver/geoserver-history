<?xml version="1.0" encoding="ISO-8859-1"?>
<StyledLayerDescriptor version="1.0.0" xsi:schemaLocation="http://www.opengis.net/sld StyledLayerDescriptor.xsd" xmlns="http://www.opengis.net/sld" xmlns:ogc="http://www.opengis.net/ogc" xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
<!-- a named layer is the basic building block of an sld document -->
<NamedLayer>
<Name>A Test Layer</Name>
<!-- with in a layer you have Named Styles -->
<UserStyle>
    <!-- again they have names, titles and abstracts -->
  <Name>Blue</Name>
  <Abstract>A sample style that uses a filter, printing only the
            lines with a LENGTH property of over 5000.  This will work
            with the default bc_roads layer</Abstract>
    <!-- FeatureTypeStyles describe how to render different features -->
    <!-- a feature type for polygons -->
    <FeatureTypeStyle>
      <Rule>
        <!-- like a linesymbolizer but with a fill too -->
        <ogc:Filter>
           <ogc:PropertyIsGreaterThan>
		<ogc:PropertyName>LENGTH</ogc:PropertyName>
		<ogc:Literal>5000</ogc:Literal>
	   </ogc:PropertyIsGreaterThan>
        </ogc:Filter>
        <LineSymbolizer> 
           <Stroke> 
             <CssParameter name="stroke">#0000ff</CssParameter> 
             <CssParameter name="stroke-width">2</CssParameter> 
           </Stroke> 
        </LineSymbolizer>
      </Rule>
    </FeatureTypeStyle>
</UserStyle>
</NamedLayer>
</StyledLayerDescriptor>

