<?xml version="1.0" encoding="UTF-8"?>
<sld:NamedLayer xmlns:sld="http://www.opengis.net/sld"
	xmlns:ogc="http://www.opengis.net/ogc"
	xmlns:gml="http://www.opengis.net/gml">
	<sld:UserStyle>
		<sld:Name>Default Styler</sld:Name>
		<sld:Title>Default Styler</sld:Title>
		<sld:Abstract></sld:Abstract>
		<sld:FeatureTypeStyle>
			<sld:FeatureTypeName>Feature</sld:FeatureTypeName>
			<sld:Rule>
				<sld:Name>ashton</sld:Name>
				<sld:Title>Ashton</sld:Title>
				<ogc:Filter>
					<ogc:PropertyIsEqualTo>
						<ogc:PropertyName>NAME</ogc:PropertyName>
						<ogc:Literal>Ashton</ogc:Literal>
					</ogc:PropertyIsEqualTo>
				</ogc:Filter>
				<sld:PolygonSymbolizer>
					<sld:Fill>
						<sld:CssParameter name="fill">
							<ogc:Literal>#AAAAAA</ogc:Literal>
						</sld:CssParameter>
					</sld:Fill>
					<sld:Stroke>
						<sld:CssParameter name="stroke">
							<ogc:Literal>#000000</ogc:Literal>
						</sld:CssParameter>
					</sld:Stroke>
				</sld:PolygonSymbolizer>
			</sld:Rule>
			<sld:Rule>
				<sld:Name>goose_island</sld:Name>
				<sld:Title>Goose Island</sld:Title>
				<ogc:Filter>
					<ogc:PropertyIsEqualTo>
						<ogc:PropertyName>NAME</ogc:PropertyName>
						<ogc:Literal>Goose Island</ogc:Literal>
					</ogc:PropertyIsEqualTo>
				</ogc:Filter>
				<sld:PolygonSymbolizer>
					<sld:Fill>
						<sld:CssParameter name="fill">
							<ogc:Literal>#FFFFFF</ogc:Literal>
						</sld:CssParameter>
					</sld:Fill>
					<sld:Stroke>
						<sld:CssParameter name="stroke">
							<ogc:Literal>#000000</ogc:Literal>
						</sld:CssParameter>
					</sld:Stroke>
				</sld:PolygonSymbolizer>
			</sld:Rule>
		</sld:FeatureTypeStyle>
	</sld:UserStyle>
</sld:NamedLayer>
