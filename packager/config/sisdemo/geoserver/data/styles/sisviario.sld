<?xml version="1.0" encoding="ISO-8859-1"?>
<StyledLayerDescriptor version="1.0.0" xsi:schemaLocation="http://www.opengis.net/sld StyledLayerDescriptor.xsd" xmlns="http://www.opengis.net/sld" xmlns:ogc="http://www.opengis.net/ogc" xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
<NamedLayer>
	<Name>sisviario</Name>
	<Title>SIS viario layer</Title>
	<Abstract>
		A styling layer used for the viario of SIS applications
	</Abstract>
<UserStyle>
  <Name>sisviario</Name>
  <Title>SIS viario style</Title>
	<Abstract>
		A style used for the viario of SIS applications
	</Abstract>
    <FeatureTypeStyle>
			<FeatureTypeName>grafo_str_mi</FeatureTypeName>
      <Rule>
       <TextSymbolizer>
        <Label>
	        <ogc:PropertyName>NOMECOMUNE</ogc:PropertyName>
        </Label>
        <Font>
         <CssParameter name="font-family">Arial</CssParameter>
         <CssParameter name="font-family">Sans-Serif</CssParameter>
         <CssParameter name="font-style">normal</CssParameter>
         <CssParameter name="font-weight">bold</CssParameter>
         <CssParameter name="font-size">8</CssParameter>
        </Font>
				<Fill>
					<CssParameter name="fill">#000000</CssParameter>
				</Fill>
       </TextSymbolizer>
      </Rule>
    </FeatureTypeStyle>
</UserStyle>
</NamedLayer>
</StyledLayerDescriptor>

