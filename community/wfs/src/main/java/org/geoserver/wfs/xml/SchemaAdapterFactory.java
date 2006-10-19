package org.geoserver.wfs.xml;

import java.io.IOException;

import org.geoserver.data.GeoServerCatalog;
import org.geoserver.data.feature.FeatureTypeInfo;
import org.geoserver.wfs.WFS;
import org.geotools.catalog.Resolve;
import org.geotools.catalog.ResolveAdapterFactory;
import org.geotools.catalog.adaptable.AdaptingResolve;
import org.geotools.catalog.adaptable.AdaptingResolveAware;
import org.geotools.util.ProgressListener;

/**
 * Adapts a geo resource representing a wfs feature type into an instance of 
 * {@link org.geoserver.wfs.xml.FeatureTypeSchema}
 * <p>
 * To be adaptable to a {@link org.geoserver.wfs.xml.FeatureTypeSchema}, the resource must also 
 * be adaptable to {@link org.geoserver.data.feature.FeatureTypeInfo}. 
 * </p>
 * <p>
 * This adapter currents supports the following  implementations of 
 * {@link org.geoserver.wfs.xml.FeatureTypeSchema}
 * <ul>
 *	<li> {@link FeatureTypeSchema.GML2}
 *	<li> {@link FeatureTypeSchema.GML3} ( default )
 * </ul>
 * </p>
 * <p>
 * Example:
 * <code>
 * 	<pre>
 *	
 * 	GeoResource resource = ...
 * 	
 * 	//gml2
 * 	FeatureTypeSchema schema = resource.resolve( FeatureTypeSchema.GML2.class );
 *  
 * 	//gml3
 * 	FeatureTypeSchema schema = resource.resolve( FeatureTypeSchema.GML3.class );
 * 
 *  //default ( gml3 )
 *  FeatureTypeSchema schema = resource.resolve( FeatureTypeSchema.class );
 *  
 * 	</pre>
 * </code>
 * </p>
 * 
 * @author Justin Deoliveira, The Open Planning Project
 * 
 */
public class SchemaAdapterFactory implements ResolveAdapterFactory, AdaptingResolveAware {

	WFS wfs;
	
	GeoServerCatalog catalog;
	
	AdaptingResolve adaptingResolve;
	
	public SchemaAdapterFactory ( WFS wfs, GeoServerCatalog catalog ) {
		this.wfs = wfs;
		this.catalog = catalog;
	}
	
	public void setAdaptingResolve( AdaptingResolve adaptingResolve ) {
		this.adaptingResolve = adaptingResolve;
	}
	
	public boolean canAdapt( Resolve resolve, Class adaptee  ) {
		if ( !( FeatureTypeSchema.class.isAssignableFrom( adaptee ) ) )
			return false;
		
		return adaptingResolve.canResolve( FeatureTypeInfo.class );
	}

	public Object adapt( Resolve resolve, Class adaptee, ProgressListener monitor )
			throws IOException {
		
		//get the feature type metadata first
		FeatureTypeInfo meta = 
			(FeatureTypeInfo) adaptingResolve.resolve( FeatureTypeInfo.class, monitor );
		if ( meta == null ) 
			return null;
		
		//check for gml2
		if ( adaptee == FeatureTypeSchema.GML2.class ) {
			return new FeatureTypeSchema.GML2( meta, wfs, catalog );	
		}
		
		//default is gml3
		return new FeatureTypeSchema.GML3( meta, wfs, catalog );
	}

	

}
