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
                    <Snippet>
                      <ogc:Function name="freemarker">
                        <ogc:Literal><![CDATA[<html><body>
This is bridge <b>${FID.value}</b> whose name is <i>${NAME.value}</i>
</body></html>]]></ogc:Literal>
                      </ogc:Function>
                    </Snippet>
                  </TextSymbolizer>
              </Rule>
          </FeatureTypeStyle>
      </UserStyle>
    </NamedLayer>
</StyledLayerDescriptor>
