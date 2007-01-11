package org.geoserver.wfs.v1_1_0;

import org.geoserver.wfs.WFSTestSupport;
import org.geoserver.wfs.xml.FeatureTypeSchemaBuilder;
import org.geoserver.wfs.xml.v1_1_0.WFSConfiguration;
import org.geotools.xml.Parser;

public class WFSXmlTest extends WFSTestSupport {

	WFSConfiguration configuration() {
		return new WFSConfiguration( 
			catalog, new FeatureTypeSchemaBuilder.GML3( wfs, catalog, loader )
		);
	}
	
	public void testValid() throws Exception {
		Parser parser = new Parser( configuration() );
		parser.parse( getClass().getResourceAsStream( "GetFeature.xml" ) );
		
		assertEquals( 0, parser.getValidationErrors().size() );
	}
	
	public void testInvalid() throws Exception {
		Parser parser = new Parser( configuration() );
		parser.parse( getClass().getResourceAsStream( "GetFeature-invalid.xml" ) );
		
		assertTrue( parser.getValidationErrors().size() > 0 );
	}

}
