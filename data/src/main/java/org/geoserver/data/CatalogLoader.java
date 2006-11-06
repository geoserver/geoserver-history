package org.geoserver.data;

import java.io.File;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.geoserver.GeoServerResourceLoader;
import org.geoserver.data.feature.DataStoreInfo;
import org.geoserver.data.feature.StyleInfo;
import org.geotools.catalog.GeoResource;
import org.geotools.catalog.Service;
import org.geotools.catalog.ServiceFinder;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

/**
 * Loads the GeoServer catalog on container startup.
 * <p>
 * This bean needs to be defined in a spring context as follows:
 * <pre>
 * 	<code>
 *  &lt;bean id="loader" class="org.geoserver.GeoServerResourceLoader"/&gt;
 * 	&lt;bean id="catalog" class="org.geoserver.data.GeoServerCatalog"/&gt;
 *  &lt;bean id="serviceFinder" class="org.geoserver.data.GeoServerServiceFinder"/&gt;
 * 	&lt;bean id="catalogLoader" class="org.geoserver.data.CatalogLoader"&gt;
 * 		&lt;constructor-arg ref="loader"/&gt;
 * 		&lt;constructor-arg ref="catalog"/&gt;
 *  	    &lt;constructor-arg ref="serviceFinder"/&gt;
 * 	&lt;/bean&gt;
 * 	</code>
 * </pre>
 * </p>
 */
public class CatalogLoader implements InitializingBean, DisposableBean {

	/**
	 * Logger
	 */
	Logger logger = Logger.getLogger( "org.geoserver.data" );
	
	/**
	 * Resource loader
	 */
	GeoServerResourceLoader loader;
	
	/**
	 * the catlaog
	 */
	GeoServerCatalog catalog;
	
	/**
	 * Service finder for populating catalog
	 */
	ServiceFinder finder;
	
	public CatalogLoader( 
		GeoServerResourceLoader loader, GeoServerCatalog catalog, ServiceFinder finder  
	) {
		this.loader = loader;
		this.catalog = catalog;
		this.finder = finder;
	}
	
	public void afterPropertiesSet() throws Exception {
		File catalogFile = loader.find("catalog.xml");
		if ( catalogFile == null ) {
			logger.severe( "Could not locate catalog.xml" );
			return;
		}
		
		//read the catalog file
		CatalogReader reader = new CatalogReader();
		reader.read( catalogFile );
		
		//setup namespace mappings
		Map nsMappings = reader.namespaces();
		for ( Iterator ns = nsMappings.entrySet().iterator(); ns.hasNext(); ) {
			Map.Entry nsMapping = (Map.Entry) ns.next();
			
			String pre = (String) nsMapping.getKey();
			String uri = (String) nsMapping.getValue();
			
			catalog.getNamespaceSupport().declarePrefix( pre, uri );
		}
		
		//populate the catalog with datastores
		for ( Iterator d = reader.dataStores().entrySet().iterator(); d.hasNext(); ) {
			Map.Entry entry = (Map.Entry) d.next();
			String id = (String) entry.getKey();
			Map params = (Map) entry.getValue();
			
			List services = finder.aquire( params );
		
			if ( services.isEmpty() )
				logger.warning( "No services found for datastore:" + id );
			if ( services.size() > 1 ) {
				logger.warning( "Multiple services found for datastore:" + id );
			}
			
			for ( Iterator s = services.iterator(); s.hasNext(); ) {
				Service service = (Service) s.next();
				if ( service.canResolve( DataStoreInfo.class ) ) {
					DataStoreInfo info = (DataStoreInfo) service.resolve( DataStoreInfo.class, null );
					info.setId( id );
	
				}
					
				catalog.add( service );
			}
		}
		
		//add styles
		Map styles = reader.styles();
		for ( Iterator s = styles.entrySet().iterator(); s.hasNext(); ) {
			Map.Entry style = (Map.Entry) s.next();
			
			String id = (String) style.getKey();
			String filename = (String) style.getValue();
			
			File styleFile = loader.find( "styles/" + filename );
			if ( styleFile == null ) {
				logger.warning( "Could not locate style: " + filename );
				continue;
			}
			
			List services = finder.aquire( styleFile.toURI() );
			if ( services.isEmpty() )
				logger.warning( "No services found for style:" + id );
			if ( services.size() > 1 ) {
				logger.warning( "Multiple services found for style:" + id );
			}
			
			for ( Iterator i = services.iterator(); i.hasNext(); ) {
				Service service = (Service) i.next();
			
				//should only have one resource
				GeoResource resource = (GeoResource) service.members( null ).get( 0 );
				StyleInfo info = (StyleInfo) resource.resolve( StyleInfo.class, null );
				if ( info == null )
					continue;
				
				info.setId( id );
				
				catalog.add( service );
			}
		}
	}

	public void destroy() throws Exception {
		//TODO: write out catalog
//		File catalogFile = loader.find("catalog.xml");
//		if ( catalogFile == null || !catalogFile.exists() ) {
//			logger.info( "Could not find catalog.xml, creating" );
//			catalogFile = loader.createFile( "catalog.xml" );
//		}
//		
//		CatalogWriter writer = new CatalogWriter();
//		
//		//datastors
//		Map dataStores = new HashMap();
//		for ( Iterator d = catalog.dataStores().iterator(); d.hasNext(); ) {
//			DataStoreInfo dataStore = (DataStoreInfo) d.next();
//			dataStores.put( dataStore.getId(), dataStore.getConnectionParameters() );
//		}
//		writer.dataStores( dataStores );
//		
//		//namespaces
//		Map namespaces = new HashMap();
//		for ( Enumeration p = catalog.getNamespaceSupport().getPrefixes(); p.hasMoreElements(); ) {
//			String prefix = (String) p.nextElement();
//			String uri = catalog.getNamespaceSupport().getURI( prefix );
//			namespaces.put( prefix, uri );
//		}
//		namespaces.put( "", catalog.getNamespaceSupport().getURI( "" ) );
//		writer.namespaces( namespaces );
//		
//		writer.write( catalogFile );
	}
}
