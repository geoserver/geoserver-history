package org.geoserver.wfs.feature;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import org.geoserver.GeoServerResourceLoader;
import org.geoserver.data.GeoServerCatalog;
import org.geoserver.data.feature.FeatureTypeReader;
import org.geotools.catalog.GeoResource;
import org.geotools.catalog.GeoResourceInfo;
import org.geotools.catalog.Resolve;
import org.geotools.catalog.ResolveAdapterFactory;
import org.geotools.catalog.Service;
import org.geotools.catalog.ServiceInfo;
import org.geotools.catalog.adaptable.AdaptingResolve;
import org.geotools.catalog.adaptable.AdaptingResolveAware;
import org.geotools.data.DataStore;
import org.geotools.feature.FeatureType;
import org.geotools.util.ProgressListener;

/**
 * Resolve catalog handles into "info" objects.
 * <p>
 * Adapts services that can resolve to a {@link org.geotools.data.DataStore} 
 * to resolve into a {@link org.geoserver.wfs.feature.DataStoreInfo}.
 * </p>
 * <p>
 * Adapts resources that can resolve to a {@link org.geotools.feature.FeatureType} 
 * to resolve into a {@link org.geoserver.wfs.feature.FeatureTypeInfo}.
 * </p>
 * @author Justin Deoliveira, The Open Planning Project, jdeolive@openplans.org
 *
 */
public class InfoAdapterFactory implements ResolveAdapterFactory, 
	AdaptingResolveAware {

	/**
	 * Catalog reference
	 */
	GeoServerCatalog catalog;

	/**
	 * Resource loader
	 */
	GeoServerResourceLoader loader;

	/**
	 * Adapting resolve
	 */
	AdaptingResolve adaptingResolve;
	
		
	public InfoAdapterFactory ( GeoServerCatalog catalog, GeoServerResourceLoader loader ) {
		this.catalog = catalog;
		this.loader = loader;
	}
	
	public void setAdaptingResolve(AdaptingResolve adaptingResolve) {
		this.adaptingResolve = adaptingResolve;
	}

	public boolean canAdapt( Resolve resolve, Class adaptee ) {
		if ( resolve instanceof Service && DataStoreInfo.class.equals( adaptee ) ) {
			return resolve.canResolve( DataStore.class );
		}
		
		if ( resolve instanceof GeoResource && FeatureTypeInfo.class.equals( adaptee ) ) {
			return resolve.canResolve( FeatureType.class );
		}
		
		return false;
	}

	public Object adapt( Resolve resolve, Class adaptee, ProgressListener monitor )
			throws IOException {
		
		if ( DataStoreInfo.class.equals( adaptee ) ) {
			Service handle = (Service) resolve;
			ServiceInfo sInfo = handle.getInfo( monitor );
			
			DataStoreInfo info = new DataStoreInfo( (Service) adaptingResolve );
			
			info.setAbstract( sInfo.getAbstract() );
			info.setTitle( sInfo.getTitle() );
			
			//TODO: set based on config
			info.setEnabled( true );
			
			return info;
		}
		
		if ( FeatureTypeInfo.class.equals( adaptee ) ) {
			GeoResource handle = (GeoResource) resolve;
			GeoResourceInfo rInfo = handle.getInfo( monitor );
		
			FeatureTypeInfo info = new FeatureTypeInfo( (GeoResource) adaptingResolve );
			
			//set some common atts
			info.setTypeName( rInfo.getName() );
			if ( rInfo.getSchema() != null ) {
				String uri = rInfo.getSchema().toString();
				String prefix = catalog.getNamespaceSupport().getPrefix( uri );
				info.setNamespacePrefix( prefix );
			}
			
			//look up the feature type info
			String id = info.getNamespacePrefix();
			id = id != null ? id + "_" + info.getTypeName() : info.getTypeName();
			
			File f = loader.find( "featureTypes/" + id + "/info.xml" );
			if ( f.exists() ) {
				//create from stored metadata
				FeatureTypeReader reader = new FeatureTypeReader();
				reader.read( f );
				
				info.setAbstract( reader.abstrct() );
				info.setTitle( reader.title() );
				info.setKeywords( Arrays.asList( reader.keywords() ) );
				//info.setDefaultStyle( reader.defaultStyle() );
				info.setNumDecimals( reader.numDecimals() );
				info.setSRS( reader.srs() );
				info.setLatLongBoundingBox( info.boundingBox() );
				info.setEnabled( true );
			}
			else {
				//create default
				info.setTypeName( rInfo.getName() );
				info.setAbstract( rInfo.getDescription() );
				info.setTitle( rInfo.getTitle() );
			
				if ( rInfo.getKeywords() != null ) {
					info.setKeywords( new ArrayList( Arrays.asList( rInfo.getKeywords() ) ) );
				}
				
				info.setEnabled( false );
			}
			
			return info;
		}
			
		return null;
	}

}
