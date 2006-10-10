package org.geoserver.data;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.geoserver.data.feature.DataStoreInfo;
import org.geoserver.data.feature.FeatureTypeInfo;
import org.geoserver.data.feature.StyleInfo;
import org.geotools.catalog.Catalog;
import org.geotools.catalog.GeoResource;
import org.geotools.catalog.Service;
import org.geotools.catalog.adaptable.AdaptingCatalog;
import org.geotools.catalog.adaptable.ResolveAdapterFactoryFinder;
import org.geotools.catalog.defaults.DefaultCatalog;
import org.geotools.util.ProgressListener;
import org.xml.sax.helpers.NamespaceSupport;

/**
 * Default catalog implementation. 
 * 
 * @author Justin Deoliveira, The Open Planning Project, jdeolive@openplans.org
 *
 */
public class DefaultGeoServerCatalog extends AdaptingCatalog implements 
	GeoServerCatalog {
	
	NamespaceSupport namespaceSupport;
	
	public DefaultGeoServerCatalog( ResolveAdapterFactoryFinder adapterFinder ) {
		super( new DefaultCatalog(), adapterFinder );
		namespaceSupport = new NamespaceSupport();
	}
	
	public NamespaceSupport getNamespaceSupport() {
		return namespaceSupport;
	}
	
	public List dataStores() throws IOException {
		List all = services( DataStoreInfo.class );
		List active = new ArrayList();

		for ( Iterator i = all.iterator(); i.hasNext(); ) {
			Service service = (Service) i.next();
			DataStoreInfo info = 
				(DataStoreInfo) service.resolve( DataStoreInfo.class, null );
			
			if ( info.isEnabled() ) 
				active.add( info );
		}
		
		return active;
	}
	
	public DataStoreInfo dataStore(String id) throws IOException {
		if ( id == null ) 
			return null;
		
		List dataStores = dataStores();
		for ( Iterator i = dataStores.iterator(); i.hasNext(); ) {
			DataStoreInfo info = (DataStoreInfo) i.next();
			if ( id.equals( info.getId() ) )
				return info;
		}
		
		return null;
	}
	
	public List featureTypes() throws IOException {
		List all = resources( FeatureTypeInfo.class );
		List active = new ArrayList();

		for ( Iterator i = all.iterator(); i.hasNext(); ) {
			GeoResource resource = (GeoResource) i.next();
			FeatureTypeInfo info = 
				(FeatureTypeInfo) resource.resolve( FeatureTypeInfo.class, null );
			
			if ( info.isEnabled() ) 
				active.add( info );
		}
		
		return active;
	}
	
	public FeatureTypeInfo featureType(String identifier) throws IOException {
		if ( identifier == null ) 
			return null;
		
		if ( identifier.indexOf( ':' ) != -1 ) {
			String[] prefixLocal = identifier.split( ":" );
			return featureType( prefixLocal[ 0 ], prefixLocal[ 1 ] );
		}
		
		return featureType( null, identifier );
	}
	
	public FeatureTypeInfo featureType(String nsPrefix, String typeName) throws IOException {
	
		if ( typeName == null ) {
			return null;
		}
		
		NamespaceSupport ns = getNamespaceSupport();
		
		if ( nsPrefix == null ) {
			//assume default namespace
			nsPrefix = ns.getPrefix( ns.getURI( "" ) );
		}
		
		if ( ns.getURI( nsPrefix ) == null ) {
			//did they pass us in a uri?
			String prefix = ns.getPrefix( nsPrefix );
			if ( prefix != null ) {
				nsPrefix = prefix;
			}
		}
		
		List featureTypes = featureTypes();
		for ( Iterator i = featureTypes.iterator() ; i.hasNext(); ) {
			FeatureTypeInfo info = (FeatureTypeInfo) i.next();
			if ( nsPrefix.equals( info.namespacePrefix() ) ) {
				if ( typeName.equals( info.getTypeName() ) )
					return info;
			}
		}
		
		return null;
	}
	
	public List styles() throws IOException {
		return resources( StyleInfo.class );
	}
	
	public StyleInfo style(String id) throws IOException {
		if ( id == null ) 
			return null;
		
		List styles = styles();
		for ( Iterator i = styles.iterator(); i.hasNext(); ) {
			StyleInfo info = (StyleInfo) i.next();
			if ( id.equals( info.getId() ) ) {
				return info;
			}
		}
		
		return null;
	}
	
	
	public List services( Class resolvee ) throws IOException {
		return services( this, resolvee );
	}
	
	protected static List services( Catalog catalog, Class resolvee ) 
		throws IOException {
		
		List handles = new ArrayList();
		List services = catalog.members( null );
		for ( Iterator s = services.iterator(); s.hasNext(); ) {
			Service service = (Service) s.next();
			if ( service.canResolve( resolvee ) ) {
				handles.add( service );
			}
		}
	
		return handles;
	}
	
	public List resources( Class resolvee ) 
		throws IOException {
		
		return resources( this, resolvee );
	}
	
	protected static List resources( Catalog catalog, Class resolvee )
		throws IOException {
		
		List handles = new ArrayList(); 
		List services = catalog.members( null );
		for ( Iterator s = services.iterator(); s.hasNext(); ) {
			
			Service service = (Service) s.next();
			List resources = service.members( null );
			for ( Iterator r = resources.iterator(); r.hasNext(); ) {
				GeoResource resource = (GeoResource) r.next();
				if ( resource.canResolve( resolvee ) ) {
					handles.add( resource );
				}
			}
			
		}
		
		return handles;
	}
	
}
