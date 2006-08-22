package org.geoserver.wfs.v1_1_0;

import net.opengis.ows.v1_0_0.GetCapabilitiesType;
import net.opengis.ows.v1_0_0.OWSFactory;
import net.opengis.wfs.v1_1_0.WFSFactory;

import org.geoserver.xml.ows.v1_0_0.OWSBindingConfiguration;
import org.geoserver.xml.ows.v1_0_0.OWSSchemaLocationResolver;
import org.geoserver.xml.wfs.v1_1_0.WFS;
import org.geoserver.xml.wfs.v1_1_0.WFSBindingConfiguration;
import org.geoserver.xml.wfs.v1_1_0.WFSSchemaLocationResolver;
import org.geotools.filter.FilterFactoryFinder;
import org.geotools.filter.v1_1.OGCSchemaLocationResolver;
import org.geotools.gml3.bindings.GMLBindingConfiguration;
import org.geotools.gml3.bindings.GMLSchemaLocationResolver;
import org.geotools.gml3.bindings.smil.SMIL20BindingConfiguration;
import org.geotools.gml3.bindings.smil.SMIL20SchemaLocationResolver;
import org.geotools.xlink.bindings.XLINKBindingConfiguration;
import org.geotools.xlink.bindings.XLINKSchemaLocationResolver;
import org.geotools.xml.Configuration;
import org.geotools.xml.test.XMLTestSupport;
import org.picocontainer.MutablePicoContainer;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.impl.CoordinateArraySequenceFactory;

public class WFSXmlTest extends XMLTestSupport {

	protected Element createRootElement( Document doc ) {
		Element root = doc.createElementNS( 
			WFS.GETCAPABILITIES.getNamespaceURI(), WFS.GETCAPABILITIES.getLocalPart()
		);
		root.setAttribute( "version", "1.1.0" );
		
		return root;
	}

	protected void registerSchemaLocation( Element element ) {
		element.setAttribute( 
			"xs:schemaLocation", WFS.NAMESPACE + " wfs.xsd"	
		);
	}

	protected Configuration createConfiguration() {
		return new Configuration() {

			public void configureBindings(MutablePicoContainer container) {
				new XLINKBindingConfiguration().configure( container );
				new SMIL20BindingConfiguration().configure( container );
				new GMLBindingConfiguration().configure( container );
				new OWSBindingConfiguration().configure( container );
				new WFSBindingConfiguration().configure( container );
			}

			public void configureContext(MutablePicoContainer container) {
				container.registerComponentImplementation( GeometryFactory.class );
				container.registerComponentInstance( CoordinateArraySequenceFactory.instance() );
				container.registerComponentInstance( FilterFactoryFinder.createFilterFactory() );
				container.registerComponentInstance( OWSFactory.eINSTANCE );
				container.registerComponentInstance( WFSFactory.eINSTANCE );
		
				container.registerComponentImplementation( XLINKSchemaLocationResolver.class );
				container.registerComponentImplementation( OGCSchemaLocationResolver.class );
				container.registerComponentImplementation( SMIL20SchemaLocationResolver.class );
				container.registerComponentImplementation( GMLSchemaLocationResolver.class );
				container.registerComponentImplementation( OWSSchemaLocationResolver.class );
				container.registerComponentImplementation( WFSSchemaLocationResolver.class );
			}
			
		};

	}
	
	public void test() throws Exception {
		GetCapabilitiesType getCapabilities = (GetCapabilitiesType) parse();
		assertNotNull( getCapabilities );
	}

}
