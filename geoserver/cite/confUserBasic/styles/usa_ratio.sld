<?xml version="1.0" encoding="ISO-8859-1"?>
<StyledLayerDescriptor version="1.0.0" xsi:schemaLocation="http://www.opengis.net/sld StyledLayerDescriptor.xsd" xmlns="http://www.opengis.net/sld" xmlns:ogc="http://www.opengis.net/ogc" xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
<!-- a named layer is the basic building block of an sld document -->
<NamedLayer>
<Name>A Test Layer</Name>

<!-- with in a layer you have Named Styles -->
<UserStyle>
    <!-- again they have names, titles and abstracts -->
  <Name>bbox test</Name>
    <!-- FeatureTypeStyles describe how to render different features -->
    <!-- a feature type for polygons -->
    <FeatureTypeStyle>
        <Rule>
        <!-- like a linesymbolizer but with a fill too -->
        <ogc:Filter  xmlns:gml="http://www.opengis.net/gml">
            <ogc:PropertyIsLessThan>
                <ogc:PropertyName>P_MALE</ogc:PropertyName>
                <ogc:PropertyName>P_FEMALE</ogc:PropertyName>
            </ogc:PropertyIsLessThan>
        </ogc:Filter>
        <PolygonSymbolizer>
           <Fill>
              <!-- CssParameters allowed are fill (the color) and fill-opacity -->
              <CssParameter name="fill">#ffaaaa</CssParameter>
              <CssParameter name="opacity">.5</CssParameter>
           </Fill>
           <Stroke/>     
        </PolygonSymbolizer>
        
      </Rule>
      <Rule>
        <!-- like a linesymbolizer but with a fill too -->
        <ogc:Filter  xmlns:gml="http://www.opengis.net/gml">
            <ogc:PropertyIsLessThan>
                <ogc:PropertyName>P_FEMALE</ogc:PropertyName>
                <ogc:PropertyName>P_MALE</ogc:PropertyName>
            </ogc:PropertyIsLessThan>
        </ogc:Filter>
        <PolygonSymbolizer>
           <Fill>
              <!-- CssParameters allowed are fill (the color) and fill-opacity -->
              <CssParameter name="fill">#aaaaff</CssParameter>
              <CssParameter name="opacity">.5</CssParameter>
           </Fill> 
           <Stroke/>    
        </PolygonSymbolizer>
        
      </Rule>
      <Rule>
        <!-- like a linesymbolizer but with a fill too -->
        <ogc:Filter  xmlns:gml="http://www.opengis.net/gml">
            <ogc:PropertyIsEqualTo>
                <ogc:PropertyName>P_FEMALE</ogc:PropertyName>
                <ogc:PropertyName>P_MALE</ogc:PropertyName>
            </ogc:PropertyIsEqualTo>
        </ogc:Filter>
        <PolygonSymbolizer>
           <Fill>
              <!-- CssParameters allowed are fill (the color) and fill-opacity -->
              <CssParameter name="fill">#ffaaff</CssParameter>
              <CssParameter name="opacity">.5</CssParameter>
           </Fill> 
           <Stroke/>    
        </PolygonSymbolizer>
        
      </Rule>
    </FeatureTypeStyle>
</UserStyle>
</NamedLayer>
</StyledLayerDescriptor>

