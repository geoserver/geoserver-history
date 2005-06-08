<?xml version="1.0" encoding="UTF-8"?>
<StyledLayerDescriptor version="1.0.0" 
	xsi:schemaLocation="http://www.opengis.net/sld StyledLayerDescriptor.xsd" 
	xmlns="http://www.opengis.net/sld" xmlns:ogc="http://www.opengis.net/ogc" 
	xmlns:xlink="http://www.w3.org/1999/xlink" 
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xmlns:dave="http://blasby.com"
	xmlns:gml="http://www.opengis.net/gml">
	
	<UserLayer> 
		<Name>Inline</Name>		
	 
	 	<InlineFeature>
	 		<FeatureCollection>
	 		   <featureMember>
	 		   
	 		  	 <dave:MyFeature>
	 		  	    <the_geom>
				  	<gml:Point srsName="http://www.opengis.net/gml/srs/epsg.xml#4326">
					   <gml:coordinates decimal="." cs="," ts=" ">-73.944,40.775</gml:coordinates>
				        </gml:Point>
				    </the_geom>
				    <Name>Dave Blasby</Name>
	 		 	  </dave:MyFeature>
	 		   
	 		   </featureMember>
	 		</FeatureCollection>
	 		
	 	</InlineFeature>
	 <LayerFeatureConstraints>
		 <FeatureTypeConstraint>
		 </FeatureTypeConstraint>
	 </LayerFeatureConstraints>
	 	<UserStyle>
	 		<FeatureTypeStyle>
	 			<Rule>
	 			   <PointSymbolizer>
	 			   <Graphic>
					<Mark>
					    <WellKnownName>circle</WellKnownName>
					    <Fill>
						<CssParameter name="fill">#993333</CssParameter>
					    </Fill>
					</Mark>
					<Size>20</Size>
		   		 </Graphic>
	 			   </PointSymbolizer>
	 			    <TextSymbolizer>
				       <Label>
					   <ogc:PropertyName>Name</ogc:PropertyName>
				       </Label>
				       <Font>
					    <CssParameter name="font-family">Times New Roman</CssParameter>
					    <CssParameter name="font-style">Normal</CssParameter>
					    <CssParameter name="font-size">20</CssParameter>
				       </Font>
					   <LabelPlacement>
						<PointPlacement/>
					   </LabelPlacement>

				       <Fill>
					   <CssParameter name="fill">#336600</CssParameter>
				       </Fill>
             			   </TextSymbolizer>
                
	 			</Rule>
	 		</FeatureTypeStyle>
	 	</UserStyle>
	 	
	 </UserLayer>
 
</StyledLayerDescriptor>