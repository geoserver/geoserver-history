<?xml version="1.0" encoding="UTF-8"?>
<sld:NamedLayer xmlns:sld="http://www.opengis.net/sld" xmlns:ogc="http://www.opengis.net/ogc" xmlns:gml="http://www.opengis.net/gml">
    <sld:UserStyle>
        <sld:Name>Default Styler</sld:Name>
        <sld:Title>Default Styler</sld:Title>
        <sld:Abstract></sld:Abstract>
        <sld:FeatureTypeStyle>
            <sld:FeatureTypeName>Feature</sld:FeatureTypeName>
			<sld:Rule>
				<sld:Name>dirt_road</sld:Name>
				<sld:Title>Dirt Road by Green Forest</sld:Title>
				<ogc:Filter>
					<ogc:PropertyIsEqualTo>
						<ogc:PropertyName>NAME</ogc:PropertyName>
						<ogc:Literal>Dirt Road by Green Forest</ogc:Literal>
					</ogc:PropertyIsEqualTo>
				</ogc:Filter>
				<sld:LineSymbolizer>
					<sld:Stroke>
						<sld:CssParameter name="stroke">
							<ogc:Literal>#C0A000</ogc:Literal>
						</sld:CssParameter>
						<sld:CssParameter name="stroke-width">
							<ogc:Literal>4</ogc:Literal>
						</sld:CssParameter>
					</sld:Stroke>
				</sld:LineSymbolizer>
			</sld:Rule>
			<sld:Rule>
				<sld:Name>route_5</sld:Name>
				<sld:Title>Route 5</sld:Title>
				<ogc:Filter>
					<ogc:PropertyIsEqualTo>
						<ogc:PropertyName>NAME</ogc:PropertyName>
						<ogc:Literal>Route 5</ogc:Literal>
					</ogc:PropertyIsEqualTo>
				</ogc:Filter>
				<sld:LineSymbolizer>
					<sld:Stroke>
						<sld:CssParameter name="stroke">
							<ogc:Literal>#000000</ogc:Literal>
						</sld:CssParameter>
						<sld:CssParameter name="stroke-width">
							<ogc:Literal>4</ogc:Literal>
						</sld:CssParameter>
					</sld:Stroke>
				</sld:LineSymbolizer>
			</sld:Rule>
			<sld:Rule>
				<sld:Name>main_street</sld:Name>
				<sld:Title>Main Street</sld:Title>
				<ogc:Filter>
					<ogc:PropertyIsEqualTo>
						<ogc:PropertyName>NAME</ogc:PropertyName>
						<ogc:Literal>Main Street</ogc:Literal>
					</ogc:PropertyIsEqualTo>
				</ogc:Filter>
				<sld:LineSymbolizer>
					<sld:Stroke>
						<sld:CssParameter name="stroke">
							<ogc:Literal>#E04000</ogc:Literal>
						</sld:CssParameter>
						<sld:CssParameter name="stroke-width">
							<ogc:Literal>4</ogc:Literal>
						</sld:CssParameter>
					</sld:Stroke>
				</sld:LineSymbolizer>
			</sld:Rule>
        </sld:FeatureTypeStyle>
    </sld:UserStyle>
</sld:NamedLayer>
