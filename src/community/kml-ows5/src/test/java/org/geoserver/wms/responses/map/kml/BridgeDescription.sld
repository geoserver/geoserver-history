<?xml version="1.0" encoding="UTF-8"?>
<StyledLayerDescriptor version="1.0.0" 
	xmlns="http://www.opengis.net/sld" xmlns:ogc="http://www.opengis.net/ogc" 
	xmlns:xlink="http://www.w3.org/1999/xlink" 
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xsi:schemaLocation="http://www.opengis.net/sld http://schemas.opengis.net/sld/1.0.0/StyledLayerDescriptor.xsd">
    <NamedLayer>
      <Name>Sample layer</Name>
      <UserStyle>
          <Name>Default Styler</Name>
          <Title>Default Styler</Title>
          <FeatureTypeStyle>
              <FeatureTypeName>Feature</FeatureTypeName>
              <Rule>
                  <Name>name</Name>
                  <Abstract>Abstract</Abstract>
                  <TextSymbolizer>
                    <Label>Hello</Label>
                    <Snippet>This is bridge <ogc:PropertyName>FID</ogc:PropertyName></Snippet>
                    <FeatureDescription>The bridge name is: <ogc:PropertyName>NAME</ogc:PropertyName></FeatureDescription>
                  </TextSymbolizer>
              </Rule>
          </FeatureTypeStyle>
      </UserStyle>
    </NamedLayer>
</StyledLayerDescriptor>
