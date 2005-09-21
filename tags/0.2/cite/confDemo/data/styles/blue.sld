<?xml version="1.0" encoding="ISO-8859-1"?>
<StyledLayerDescriptor version="1.0.0" xsi:schemaLocation="http://www.opengis.net/sld StyledLayerDescriptor.xsd" xmlns="http://www.opengis.net/sld" xmlns:ogc="http://www.opengis.net/ogc" xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
<!-- a named layer is the basic building block of an sld document -->
<!-- with in a layer you have Named Styles -->
<UserStyle>
    <!-- again they have names, titles and abstracts -->
  <Name>blue</Name>
  <Title>a blue style</Title>
  <Abstract>I like things blue</Abstract>
    <!-- FeatureTypeStyles describe how to render different features -->
    <!-- a feature type for polygons -->
    <FeatureTypeStyle>
      <FeatureTypeName>Feature</FeatureTypeName>
      <Rule>
        <!-- like a linesymbolizer but with a fill too -->
        <LineSymbolizer> 
           <Stroke> 
             <CssParameter name="stroke">#0000ff</CssParameter> 
             <CssParameter name="stroke-width">1</CssParameter> 
           </Stroke> 
        </LineSymbolizer>
      </Rule>
    </FeatureTypeStyle>
</UserStyle>
</StyledLayerDescriptor>

