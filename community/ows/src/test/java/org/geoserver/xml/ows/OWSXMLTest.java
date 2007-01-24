package org.geoserver.xml.ows;

import junit.framework.TestCase;
import net.opengis.ows.v1_0_0.GetCapabilitiesType;

import org.geoserver.xml.ows.v1_0_0.OWSConfiguration;
import org.geotools.xml.Configuration;
import org.geotools.xml.Parser;

public class OWSXMLTest extends TestCase {

	public void testGetCapabilities() throws Exception {
		Configuration configuration = new OWSConfiguration(); 
			
		Parser parser = new Parser( configuration );
		Object o = parser.parse( getClass().getResourceAsStream( "getCapabilities.xml") );
		
		assertTrue( o instanceof GetCapabilitiesType );
	}
}
