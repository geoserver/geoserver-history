package org.geoserver.wfs.xml.v1_1_0;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import net.opengis.wfs.WFSFactory;

import org.eclipse.xsd.util.XSDSchemaLocationResolver;
import org.geoserver.data.GeoServerCatalog;
import org.geoserver.data.feature.FeatureTypeInfo;
import org.geoserver.wfs.xml.FeatureTypeSchema;
import org.geoserver.wfs.xml.WFSHandlerFactory;
import org.geoserver.wfs.xml.filter.v1_1.FilterTypeBinding;
import org.geoserver.wfs.xml.filter.v1_1.PropertyNameTypeBinding;
import org.geoserver.wfs.xml.gml3.AbstractGeometryTypeBinding;
import org.geoserver.wfs.xml.gml3.CircleTypeBinding;
import org.geoserver.wfs.xml.xs.DateBinding;
import org.geoserver.xml.ows.v1_0_0.OWSConfiguration;
import org.geotools.feature.FeatureType;
import org.geotools.filter.v1_1.OGC;
import org.geotools.filter.v1_1.OGCConfiguration;
import org.geotools.gml2.FeatureTypeCache;
import org.geotools.gml3.GMLConfiguration;
import org.geotools.gml3.bindings.GML;
import org.geotools.xml.BindingConfiguration;
import org.geotools.xml.Configuration;
import org.geotools.xml.Parser;
import org.geotools.xml.Schemas;
import org.geotools.xs.bindings.XS;
import org.picocontainer.MutablePicoContainer;

public class WFSConfiguration extends Configuration {

	GeoServerCatalog catalog;
	
	public WFSConfiguration( GeoServerCatalog catalog ) {
		super();
		
		this.catalog = catalog;
		
		addDependency( new OGCConfiguration() );
		addDependency( new GMLConfiguration() );
		addDependency( new OWSConfiguration() );
	}
	
	public void addDependency(Configuration dependency) {
		//override to make public
		super.addDependency( dependency );
	}
	
	public String getNamespaceURI() {
		return WFS.NAMESPACE;
	}

	public String getSchemaFileURL() {
		return
			getSchemaLocationResolver().resolveSchemaLocation( null, WFS.NAMESPACE, "wfs.xsd" );		
		 
	}

	public BindingConfiguration getBindingConfiguration() {
		return new WFSBindingConfiguration();
	}

	public XSDSchemaLocationResolver getSchemaLocationResolver() {
		return new WFSSchemaLocationResolver();
	}

	public void configureContext(MutablePicoContainer context) {
		super.configureContext( context );
		
		context.registerComponentInstance( WFSFactory.eINSTANCE );
		context.registerComponentInstance( new WFSHandlerFactory( catalog, FeatureTypeSchema.GML3.class ) );
		context.registerComponentInstance( catalog );
		
		//seed the cache with entries from the catalog
		FeatureTypeCache featureTypeCache = 
			(FeatureTypeCache) context.getComponentInstanceOfType( FeatureTypeCache.class );
		try {
			List featureTypes = catalog.featureTypes();
			for ( Iterator f = featureTypes.iterator(); f.hasNext(); ) {
				FeatureTypeInfo meta = (FeatureTypeInfo) f.next();
				FeatureType featureType = meta.featureType();
				
				featureTypeCache.put( featureType );
			}
		} catch (IOException e) {
			throw new RuntimeException( e );
		}
	}

	protected void configureBindings(MutablePicoContainer container) {
		//register our custom bindings
		container.registerComponentImplementation( XS.DATE, DateBinding.class );
		container.registerComponentImplementation( OGC.FILTER, FilterTypeBinding.class );
		container.registerComponentImplementation( OGC.PROPERTYNAMETYPE, PropertyNameTypeBinding.class );
		container.registerComponentImplementation( GML.CircleType, CircleTypeBinding.class );
		container.registerComponentImplementation( GML.AbstractGeometryType, AbstractGeometryTypeBinding.class );
		
		//remove bindings for MultiPolygon and MultiLineString
		//TODO: make this cite configurable
		Schemas.unregisterComponent( container, GML.MultiPolygonType );
		
	}
	
}
