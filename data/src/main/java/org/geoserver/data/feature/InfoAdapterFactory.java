package org.geoserver.data.feature;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Logger;

import org.geoserver.GeoServerResourceLoader;
import org.geoserver.data.GeoServerCatalog;
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
import org.geotools.styling.StyledLayerDescriptor;
import org.geotools.util.ProgressListener;

/**
 * Resolve catalog handles into "info" objects.
 * <p>
 * Adapts services that can resolve to a {@link org.geotools.data.DataStore} 
 * to resolve into a {@link org.geoserver.data.feature.DataStoreInfo}.
 * </p>
 * <p>
 * Adapts resources that can resolve to a {@link org.geotools.feature.FeatureType} 
 * to resolve into a {@link org.geoserver.data.feature.FeatureTypeInfo}.
 * </p>
 * <p>
 * Adapts resources that can resovle to {@link org.geotools.styling.StyledLayerDescriptor}
 * to resolve into a (@link org.geoserver.data.feature.StyleInfo}.
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
		
		if ( resolve instanceof GeoResource && StyleInfo.class.equals( adaptee ) ) {
			return resolve.canResolve( StyledLayerDescriptor.class );
		}
			
		return false;
	}

	public Object adapt( Resolve resolve, Class adaptee, ProgressListener monitor )
			throws IOException {
		
		if ( DataStoreInfo.class.equals( adaptee ) ) {
			Service handle = (Service) resolve;
			ServiceInfo sInfo = handle.getInfo( monitor );
			
			DataStoreInfo info = new DataStoreInfo();
			info.setDataStore( (DataStore) handle.resolve( DataStore.class, null ) );
			info.setAbstract( sInfo.getAbstract() );
			info.setTitle( sInfo.getTitle() );
			info.setConnectionParameters( handle.getConnectionParams() );
			
			if ( sInfo.getSchema() != null ) {
				String schema = sInfo.getSchema().toString();
				//could be specified as prefix or uri
				if ( catalog.getNamespaceSupport().getURI( schema ) != null ) {
					info.setNamespacePrefix( schema );
				}
				else if ( catalog.getNamespaceSupport().getPrefix( schema ) != null ) {
					info.setNamespacePrefix( catalog.getNamespaceSupport().getPrefix( schema ) );
				}
			}
			else {
				//set the default prefix
				String prefix = catalog.getNamespaceSupport().getPrefix( 
					catalog.getNamespaceSupport().getURI( "" )	
				);
				info.setNamespacePrefix( prefix );
			}
			
			//TODO: set based on config
			info.setEnabled( true );
			
			return info;
		}
		
		if ( FeatureTypeInfo.class.equals( adaptee ) ) {
			GeoResource handle = (GeoResource) resolve;
			GeoResourceInfo rInfo = handle.getInfo( monitor );
			if ( rInfo == null ) 
				throw new NullPointerException();
			
			DataStoreInfo dataStoreInfo = 
				(DataStoreInfo) adapt( handle.parent( null ), DataStoreInfo.class, null );
			
			//look up the feature type info, stores as 'featureTypes/<prefix>_<typeName>/info.xml'
			FeatureTypeInfo info = null;
			File infoFile = null;
			if ( dataStoreInfo.getNamespacePrefix() != null ) {
				
				String prefix = dataStoreInfo.getNamespacePrefix();
				infoFile = loader.find( "featureTypes/" + prefix + "_" + rInfo.getName() + "/info.xml" );
			}
			else {
				//try straight up, with just name
				infoFile = loader.find( "featureTypes/" + rInfo.getName() + "/info.xml" );
				if ( infoFile == null ) {
					//try with default namespace
					String prefix = catalog.getNamespaceSupport().getPrefix( 
						catalog.getNamespaceSupport().getURI( "" )
					);
					infoFile = loader.find( "featureTypes/" + prefix + "_" + rInfo.getName() + "/info.xml" );
				}
			}
			
			if ( infoFile != null && infoFile.exists() ) {
				//create from stored metadata
				FeatureTypeReader reader = new FeatureTypeReader( catalog );
				info = reader.read( infoFile );
				
				info.setEnabled( true );
			}
			else {
				//create default
				info = new FeatureTypeInfo();
				info.setTypeName( rInfo.getName() );
				info.setAbstract( rInfo.getDescription() );
				info.setTitle( rInfo.getTitle() );
			
				if ( rInfo.getKeywords() != null ) {
					info.setKeywords( rInfo.getKeywords() );
				}
				
				info.setEnabled( false );
			}
			
			info.setDataStore( dataStoreInfo );
			
			return info;
		}
			
		if ( StyleInfo.class.equals( adaptee ) ) {
			StyledLayerDescriptor sld = 
				(StyledLayerDescriptor) resolve.resolve( StyledLayerDescriptor.class, monitor ); 
			StyleInfo info = new StyleInfo();
			info.setSLD( sld );
			
			return info;
		}
		
		return null;
	}

}
