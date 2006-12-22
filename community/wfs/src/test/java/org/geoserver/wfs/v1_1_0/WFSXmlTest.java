package org.geoserver.wfs.v1_1_0;

import net.opengis.ows.v1_0_0.GetCapabilitiesType;

import org.geoserver.data.DefaultGeoServerCatalog;
import org.geoserver.data.GeoServerCatalog;
import org.geoserver.data.GeoServerResolveAdapterFactoryFinder;
import org.geoserver.wfs.xml.v1_1_0.WFS;
import org.geoserver.wfs.xml.v1_1_0.WFSConfiguration;
import org.geotools.xml.Configuration;
import org.geotools.xml.Parser;
import org.geotools.xml.test.XMLTestSupport;
import org.w3c.dom.Element;

public class WFSXmlTest extends XMLTestSupport {

	GeoServerCatalog catalog = new DefaultGeoServerCatalog( 
		new GeoServerResolveAdapterFactoryFinder()
	);
	
	protected Configuration createConfiguration() {
		return new WFSConfiguration( catalog );
	}
	
	public void test() throws Exception {
		Element root = document.createElementNS( 
			WFS.GETCAPABILITIES.getNamespaceURI(), WFS.GETCAPABILITIES.getLocalPart()
		);
		root.setAttribute( "version", "1.1.0" );
		
		document.appendChild( root );
			
		GetCapabilitiesType getCapabilities = (GetCapabilitiesType) parse();
		assertNotNull( getCapabilities );
	}
	
	public void testValid() throws Exception {
		Parser parser = new Parser( new WFSConfiguration( catalog ) );
		parser.parse( getClass().getResourceAsStream( "GetFeature.xml" ) );
		
		assertEquals( 0, parser.getValidationErrors().size() );
	}
	
	public void testInvalid() throws Exception {
		Parser parser = new Parser( new WFSConfiguration( catalog ) );
		parser.parse( getClass().getResourceAsStream( "GetFeature-invalid.xml" ) );
		
		assertTrue( parser.getValidationErrors().size() > 0 );
	}

}
