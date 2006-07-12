package org.geoserver.data;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import junit.framework.TestCase;

import org.geotools.catalog.ServiceFinder;
import org.geotools.catalog.defaults.DefaultServiceFinder;
import org.geotools.catalog.property.PropertyServiceFactory;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class CatalogLoaderTest extends TestCase {


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
		
		Element connectionParamsElement = doc.createElement( "connectionParameters" );
		dataStoreElement.appendChild( connectionParamsElement );
		
		Element parameterElement = doc.createElement( "parameter" );
		parameterElement.setAttribute( "name", "directory" );
		parameterElement.setAttribute( "value", System.getProperty( "java.io.tmpdir" ) );
		connectionParamsElement.appendChild( parameterElement );
		
		Transformer tx = TransformerFactory.newInstance().newTransformer();
		
		DOMSource source = new DOMSource( doc );
		
		final File catalogFile = File.createTempFile( "catalog", "xml" );
		catalogFile.deleteOnExit();
		StreamResult result = new StreamResult( catalogFile );
		
		tx.transform( source, result );
		
		GeoServerCatalog catalog = new DefaultGeoServerCatalog();
		ServiceFinder finder = new DefaultServiceFinder( catalog ) {
			
			public List getServiceFactories() {
				ArrayList list = new ArrayList();
				list.add( new PropertyServiceFactory() );
				
				return list;
			};
		};
		
		CatalogLoader loader = new CatalogLoader( catalog, finder );
		loader.setResourceLoader( 
			new ResourceLoader() {
				public Resource getResource(String path) {
					return new FileSystemResource( catalogFile );
				}
			}
		);
		
		loader.afterPropertiesSet();
		assertFalse( catalog.members( null ).isEmpty() );
		
	}
}
