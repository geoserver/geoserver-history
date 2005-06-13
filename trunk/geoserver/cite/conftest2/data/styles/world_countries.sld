<?xml version="1.0" encoding="UTF-8"?>
<StyledLayerDescriptor version="1.0.0" 
	xsi:schemaLocation="http://www.opengis.net/sld StyledLayerDescriptor.xsd" 
	xmlns="http://www.opengis.net/sld" xmlns:ogc="http://www.opengis.net/ogc" 
	xmlns:xlink="http://www.w3.org/1999/xlink" 
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
<NamedLayer> <Name> area landmarks </Name>
    <UserStyle>
        
        
        <FeatureTypeStyle>
            <FeatureTypeName>Feature</FeatureTypeName>
            
<!-- underlay -->

            <Rule>  
		
 
                <PolygonSymbolizer>
                    <Fill>
                        <CssParameter name="fill">
                            <ogc:Literal>#FFC46F</ogc:Literal>
                        </CssParameter>
                        <CssParameter name="fill-opacity">
                            <ogc:Literal>1.0</ogc:Literal>
                        </CssParameter>
                    </Fill>
                    <Stroke>
                    <CssParameter name="stroke">
		                     <ogc:Literal>#BC7004</ogc:Literal>
                        </CssParameter>
                    </Stroke>
                    
                </PolygonSymbolizer>
            </Rule>
   
            
            <Rule>
            <MaxScaleDenominator>53000000</MaxScaleDenominator>
            <TextSymbolizer>
            
	    		    <Label>
	    			<ogc:PropertyName>NAME</ogc:PropertyName>
	    		    </Label>
	    
	    		    <Font>
	    			<CssParameter name="font-family">Times New Roman</CssParameter>
	    			<CssParameter name="font-style">Normal</CssParameter>
	    			<CssParameter name="font-size">14</CssParameter>
	    			<CssParameter name="font-weight">bold</CssParameter>
	    		    </Font>
	    		    
	    		    <Halo>
	    		    
	    			    <Radius>
	    			   	 <ogc:Literal>1</ogc:Literal>
	    			    </Radius>
	    			    <Fill>
	    			    	<CssParameter name="fill">#FDE5A5</CssParameter>
	    			    	<CssParameter name="fill-opacity">0.75</CssParameter>
	    			    </Fill>
	    			    
	    		    </Halo>
	    		    <Fill>
	    			<CssParameter name="fill">#000000</CssParameter>
	    		    </Fill>
	</TextSymbolizer>

            </Rule>
        </FeatureTypeStyle>
    </UserStyle>
    </NamedLayer>
</StyledLayerDescriptor>
