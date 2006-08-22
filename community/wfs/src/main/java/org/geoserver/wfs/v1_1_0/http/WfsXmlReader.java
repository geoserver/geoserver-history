package org.geoserver.wfs.v1_1_0.http;

import java.io.InputStream;

import net.opengis.ows.v1_0_0.OWSFactory;
import net.opengis.wfs.v1_1_0.WFSFactory;

import org.geoserver.ows.http.XmlReader;

import org.geoserver.xml.ows.v1_0_0.OWSBindingConfiguration;
import org.geoserver.xml.ows.v1_0_0.OWSSchemaLocationResolver;
import org.geoserver.xml.wfs.v1_1_0.WFS;
import org.geoserver.xml.wfs.v1_1_0.WFSBindingConfiguration;
import org.geoserver.xml.wfs.v1_1_0.WFSSchemaLocationResolver;
import org.geoserver.xml.wfs.v1_1_0.WFSSchemaLocator;
import org.geotools.filter.FilterFactoryFinder;
import org.geotools.filter.v1_1.OGCSchemaLocationResolver;

import org.geotools.gml3.bindings.GMLBindingConfiguration;
import org.geotools.gml3.bindings.GMLSchemaLocationResolver;
import org.geotools.gml3.bindings.smil.SMIL20BindingConfiguration;
import org.geotools.gml3.bindings.smil.SMIL20SchemaLocationResolver;

import org.geotools.xlink.bindings.XLINKBindingConfiguration;
import org.geotools.xlink.bindings.XLINKSchemaLocationResolver;
import org.geotools.xml.Configuration;
import org.geotools.xml.Parser;
import org.picocontainer.MutablePicoContainer;

import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.impl.CoordinateArraySequenceFactory;

public class WfsXmlReader extends XmlReader  {

	public WfsXmlReader( String element ) {
		super( WFS.NAMESPACE, element, "1.1.0" );
	}

	public Object parse(InputStream input) throws Exception {
		Configuration configuration = new Configuration() {

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
				container.registerComponentImplementation( WFSSchemaLocator.class );
			}
			
		};
		
		Parser parser = new Parser( configuration, input );
		return parser.parse();
	}

}
