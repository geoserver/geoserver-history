<?xml version="1.0" encoding="ISO-8859-1"?>
<StyledLayerDescriptor version="1.0.0"
    xsi:schemaLocation="http://www.opengis.net/sld StyledLayerDescriptor.xsd"
    xmlns="http://www.opengis.net/sld"
    xmlns:ogc="http://www.opengis.net/ogc"
    xmlns:xlink="http://www.w3.org/1999/xlink"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
    <!-- a named layer is the basic building block of an sld document -->

  <NamedLayer>
    <Name>Default Point</Name>
    <UserStyle>
        <!-- they have names, titles and abstracts -->
     
      <Title>A boring default style</Title>
      <Abstract>A sample style that just prints out a purple square</Abstract>
      <!-- FeatureTypeStyles describe how to render different features -->
      <!-- a feature type for points -->

      <FeatureTypeStyle>
        <!--FeatureTypeName>Feature</FeatureTypeName-->
        <Rule>
          <Name>Rule 1</Name>
          <Title>RedSquare</Title>
          <Abstract>A red fill with an 11 pixel size</Abstract>

          <PointSymbolizer>
            <Graphic>
              <ExternalGraphic>
                <OnlineResource xlink:type="simple" xlink:href="burg02.svg"/>
                <Format>image/svg+xml</Format>
              </ExternalGraphic>
              <Size><ogc:Literal>20</ogc:Literal></Size>
            </Graphic>
          </PointSymbolizer>
        </Rule>

        </FeatureTypeStyle>
    </UserStyle>
  </NamedLayer>
</StyledLayerDescriptor> 
