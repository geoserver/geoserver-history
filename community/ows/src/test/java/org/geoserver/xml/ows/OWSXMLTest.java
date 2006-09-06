package org.geoserver.xml.ows;

import net.opengis.ows.v1_0_0.GetCapabilitiesType;
import net.opengis.ows.v1_0_0.OWSFactory;

import org.geoserver.xml.ows.v1_0_0.OWSBindingConfiguration;
import org.geoserver.xml.ows.v1_0_0.OWSSchemaLocationResolver;
import org.geotools.xlink.bindings.XLINKBindingConfiguration;
import org.geotools.xlink.bindings.XLINKSchemaLocationResolver;
import org.geotools.xml.Configuration;
import org.geotools.xml.Parser;
import org.geotools.xs.bindings.XSBindingConfiguration;
import org.picocontainer.MutablePicoContainer;

import junit.framework.TestCase;

public class OWSXMLTest extends TestCase {

	public void testGetCapabilities() throws Exception {
		Configuration configuration = new Configuration() {

			public void configureBindings( MutablePicoContainer container ) {
				new XSBindingConfiguration().configure( container );
				new XLINKBindingConfiguration().configure( container );
				new OWSBindingConfiguration().configure( container );
				
			}

			public void configureContext( MutablePicoContainer container ) {
				container.registerComponentInstance( OWSFactory.eINSTANCE );
				container.registerComponentImplementation( XLINKSchemaLocationResolver.class );
				container.registerComponentImplementation( OWSSchemaLocationResolver.class );
				
			}
		};
		
		Parser parser = new Parser( configuration );
		Object o = parser.parse( getClass().getResourceAsStream( "getCapabilities.xml") );
		
		assertTrue( o instanceof GetCapabilitiesType );
	}
}
