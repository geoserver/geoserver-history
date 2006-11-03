package org.geoserver.wfs.xml.v1_0_0;

import net.opengis.ows.v1_0_0.OWSFactory;
import net.opengis.wfs.WFSFactory;

import org.eclipse.xsd.util.XSDSchemaLocationResolver;
import org.geoserver.data.GeoServerCatalog;
import org.geoserver.wfs.xml.FeatureTypeSchema;
import org.geoserver.wfs.xml.WFSHandlerFactory;
import org.geotools.filter.v1_0.OGCConfiguration;
import org.geotools.gml2.GMLConfiguration;
import org.geotools.gml2.bindings.GML;
import org.geotools.xml.BindingConfiguration;
import org.geotools.xml.Configuration;
import org.picocontainer.MutablePicoContainer;

/**
 * Parser configuration for wfs 1.0.
 * 
 * @author Justin Deoliveira, The Open Planning Project
 *
 */
public class WFSConfiguration extends Configuration {

	GeoServerCatalog catalog;
	
	public WFSConfiguration( GeoServerCatalog catalog ) {
		super();
		
		this.catalog = catalog;
		
		addDependency( new OGCConfiguration() );
		addDependency( new GMLConfiguration() );
	}
	
	public String getNamespaceURI() {
		return WFS.NAMESPACE;
	}

	public String getSchemaFileURL() {
		return 
			getSchemaLocationResolver().resolveSchemaLocation( null, WFS.NAMESPACE, "WFS-transaction.xsd" );		
		
	}

	public BindingConfiguration getBindingConfiguration() {
		return new WFSBindingConfiguration();
	}

	public XSDSchemaLocationResolver getSchemaLocationResolver() {
		return new WFSSchemaLocationResolver();
	}

	public void configureContext(MutablePicoContainer context) {
		super.configureContext( context );
		
		context.registerComponentInstance( OWSFactory.eINSTANCE );
		context.registerComponentInstance( WFSFactory.eINSTANCE );
		context.registerComponentInstance( new WFSHandlerFactory( catalog, FeatureTypeSchema.GML2.class ) );
		context.registerComponentInstance( catalog );
	}
	
	protected void configureBindings(MutablePicoContainer bindings) {
		super.configureBindings( bindings );
		
		//override the GMLAbstractFeatureTypeBinding
		bindings.registerComponentImplementation( GML.ABSTRACTFEATURETYPE, GMLAbstractFeatureTypeBinding.class );
	}
	
	
}
