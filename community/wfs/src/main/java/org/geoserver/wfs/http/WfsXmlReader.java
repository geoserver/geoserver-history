package org.geoserver.wfs.http;

import java.io.InputStream;

import net.opengis.wfs.WFSFactory;

import org.geoserver.ows.http.XmlReader;
import org.geoserver.wfs.xml.wfs.WFS;
import org.geoserver.wfs.xml.wfs.WFSBindingConfiguration;
import org.geoserver.wfs.xml.wfs.WFSSchemaLocationResolver;
import org.geoserver.wfs.xml.wfs.WFSSchemaLocator;
import org.geotools.filter.FilterFactoryFinder;
import org.geotools.filter.v1_0.OGCBindingConfiguration;
import org.geotools.filter.v1_0.OGCSchemaLocationResolver;
import org.geotools.gml2.bindings.GMLBindingConfiguration;
import org.geotools.gml2.bindings.GMLSchemaLocationResolver;
import org.geotools.xlink.bindings.XLINKBindingConfiguration;
import org.geotools.xlink.bindings.XLINKSchemaLocationResolver;
import org.geotools.xml.Configuration;
import org.geotools.xml.Parser;
import org.geotools.xs.bindings.XSBindingConfiguration;
import org.picocontainer.MutablePicoContainer;

import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.impl.CoordinateArraySequenceFactory;

public class WfsXmlReader extends XmlReader {

	public WfsXmlReader( String element ) {
		super( WFS.NAMESPACE, element, "1.0.0" );
	}

	public Object parse( InputStream input ) throws Exception {
		Configuration configuration = new Configuration() {

			public void configureBindings(MutablePicoContainer container) {
				new XSBindingConfiguration().configure( container );
				new XLINKBindingConfiguration().configure( container );
				new OGCBindingConfiguration().configure( container );
				new GMLBindingConfiguration().configure( container );
				new WFSBindingConfiguration().configure( container );
			}

			public void configureContext(MutablePicoContainer container) {
				container.registerComponentImplementation( GeometryFactory.class );
				container.registerComponentInstance( CoordinateArraySequenceFactory.instance() );
				container.registerComponentInstance( FilterFactoryFinder.createFilterFactory() );
				
				container.registerComponentInstance( WFSFactory.eINSTANCE );
		
				container.registerComponentImplementation( XLINKSchemaLocationResolver.class );
				container.registerComponentImplementation( OGCSchemaLocationResolver.class );
				container.registerComponentImplementation( GMLSchemaLocationResolver.class );
				container.registerComponentImplementation( WFSSchemaLocationResolver.class );
		
				container.registerComponentImplementation( WFSSchemaLocator.class );
			}
			
		};
		
		Parser parser = new Parser( configuration, input );
		return parser.parse();
	}

}
