package org.geoserver.data;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import junit.framework.TestCase;

import org.geoserver.GeoServerResourceLoader;
import org.geoserver.data.feature.InfoAdapterFactory;
import org.geotools.catalog.Service;
import org.geotools.catalog.ServiceFinder;
import org.geotools.catalog.adaptable.AdaptingServiceFinder;
import org.geotools.catalog.adaptable.ResolveAdapterFactoryFinder;
import org.geotools.catalog.defaults.DefaultServiceFinder;
import org.geotools.catalog.property.PropertyServiceFactory;
import org.geotools.data.property.PropertyDataStoreFactory;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class CatalogLoaderTest extends TestCase {

	/**
	 * tmp data directory
	 */
	File data;
	/**
	 * the catalog
	 */
	DefaultGeoServerCatalog catalog;
	/**
	 * the service finder
	 */
	ServiceFinder serviceFinder;
	/**
	 * catalog loader
	 */
	CatalogLoader catalogLoader;
	
	protected void setUp() throws Exception {
		data = File.createTempFile( "data", "data" );
		data.delete();
		data.mkdir();
		
		File propertiesFile = new File( data, "dummy.properties" );
		propertiesFile.createNewFile();
		
		final GeoServerResourceLoader loader = new GeoServerResourceLoader( data );
		ResolveAdapterFactoryFinder adapterFinder = new ResolveAdapterFactoryFinder() {
			
			public Collection getResolveAdapterFactories() {
				ArrayList list = new ArrayList();
				list.add( new InfoAdapterFactory( catalog, loader ) );
				
				return list;
			} 
		};
		
		catalog = new DefaultGeoServerCatalog( adapterFinder );
		serviceFinder = new AdaptingServiceFinder( 
			catalog, 
			new DefaultServiceFinder( catalog ) {
			
				public List getServiceFactories() {
					ArrayList list = new ArrayList();
					list.add( new PropertyServiceFactory() );
					
					return list;
				}
			}
		);
		
		catalogLoader = new CatalogLoader( loader, catalog, serviceFinder );
	}
	
	protected void tearDown() throws Exception {
		File[] files = data.listFiles();
		for ( int i = 0; i < files.length; i++ ) {
			files[ i ].delete();
		}
		
		data.delete();
	}
	
	public void testLoadCatalog() throws Exception {
		DocumentBuilder builder = 
			DocumentBuilderFactory.newInstance().newDocumentBuilder();
		
		Document doc = builder.newDocument();
		doc.appendChild( doc.createElement( "catalog" ) );
		
		//create namespaces
		Element namespacesElement = doc.createElement( "namespaces" );
		doc.getDocumentElement().appendChild( namespacesElement );
		
		Element namespaceElement = doc.createElement( "namespace" );
		namespaceElement.setAttribute( "uri", "http://somenamespace" );
		namespaceElement.setAttribute( "prefix", "some" );
		namespacesElement.appendChild( namespaceElement );
		
		//create data stores
		Element dataStoresElement = doc.createElement( "datastores" );
		doc.getDocumentElement().appendChild( dataStoresElement );
		
		Element dataStoreElement = doc.createElement( "datastore" );
		dataStoresElement.appendChild( dataStoreElement );
		
		Element connectionParamsElement = doc.createElement( "connectionParams" );
		dataStoreElement.appendChild( connectionParamsElement );
		
		Element parameterElement = doc.createElement( "parameter" );
		parameterElement.setAttribute( "name", "directory" );
		parameterElement.setAttribute( "value", System.getProperty( "java.io.tmpdir" ) );
		connectionParamsElement.appendChild( parameterElement );
		
		Element stylesElement = doc.createElement( "styles" );
		doc.getDocumentElement().appendChild( stylesElement );
		
		Transformer tx = TransformerFactory.newInstance().newTransformer();
		
		DOMSource source = new DOMSource( doc );
		
		File catalogFile = new File( data, "catalog.xml" );
		StreamResult result = new StreamResult( catalogFile );
		
		tx.transform( source, result );
		
		catalogLoader.afterPropertiesSet();
		assertFalse( catalog.members( null ).isEmpty() );
		
		catalogFile.delete();
		
	}
}
