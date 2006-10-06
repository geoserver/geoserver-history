package org.geoserver.data;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Writes the GeoServer catalog.xml file.
 * <p>
 * Usage:
 * 
 * <pre>
 * 	<code>
 * 		
 * 		Map dataStores = ...
 * 		Map nameSpaces = ...
 * 
 * 		CatalogWriter writer = new CatalogWriter();
 * 		writer.dataStores( dataStores );
 * 		writer.nameSpaces( nameSpaces );
 * 		
 * 		File catalog = new File( ".../catalog.xml" );
 * 		writer.write( catalog );
 * 
 * 	</code>
 * </pre>
 * </p>
 * 
 * @author Justin Deoliveira, The Open Planning Project, jdeolive@openplans.org
 * 
 */
public class CatalogWriter {

	/**
	 * The xml document
	 */
	Document document;
	/** 
	 * Root catalog element.
	 */
	Element catalog;
	
	public CatalogWriter() {
		try {
			DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
			builderFactory.setNamespaceAware( false );
			builderFactory.setValidating( false );
			
			document = builderFactory.newDocumentBuilder().newDocument();
			catalog = document.createElement( "catalog" );
			document.appendChild( catalog );
		} 
		catch( Exception e ) {
			throw new RuntimeException( e );
		}
	}
	
	/**
	 * Writes "datastore" elements to the catalog.xml file.
	 * 
	 * @param map of id to connection parameter map 
	 
	 * @throws Exception If error writing "datastores" element.
	 */
	public void dataStores( Map/*<String,Map>*/ dataStores ) throws Exception {
		Element dataStoresElement = document.createElement( "datastores" );
		catalog.appendChild( dataStoresElement );
		
		for ( Iterator d = dataStores.entrySet().iterator(); d.hasNext(); ) {
			Map.Entry dataStore = (Map.Entry) d.next();
			String id = (String) dataStore.getKey();
			Map params = (Map) dataStore.getValue();
			
			Element dataStoreElement = document.createElement( "datastore" );
			dataStoresElement.appendChild( dataStoreElement );
			
			//set the datastore id
			dataStoreElement.setAttribute( "id", id );
			
			//encode hte ocnnection paramters
			Element connectionParamtersElement = document.createElement( "connectionParams" );
			dataStoreElement.appendChild( connectionParamtersElement );
			
			for ( Iterator p = params.entrySet().iterator(); p.hasNext(); ) {
				Map.Entry param = (Map.Entry) p.next();
				String name = (String) param.getKey();
				Object value = param.getValue();
				
				//skip null values 
				if ( value == null ) 
					continue;
				
				Element parameterElement = document.createElement( "parameter" );
				connectionParamtersElement.appendChild( parameterElement );
				
				parameterElement.setAttribute( "name", name );
				parameterElement.setAttribute( "value", value.toString() );
			}
		}
		
	}
	
	/**
	 * Writes "namespace" elements to the catalog.xml file.
	 * 
	 * @param namespaces map of <prefix,uri>, default uri is located under 
	 * the empty string key.
	 *  
	 * @throws Exception If error writing "namespaces" element.
	 */
	public void namespaces( Map namespaces ) throws Exception {
		Element namespacesElement = document.createElement( "namespaces" );
		catalog.appendChild( namespacesElement );
		
		for ( Iterator n = namespaces.entrySet().iterator(); n.hasNext(); ) {
			Map.Entry namespace = (Map.Entry) n.next();
			String prefix = (String) namespace.getKey();
			String uri = (String) namespace.getValue();
			
			//dont write out default prefix
			if ( "".equals( prefix ) )
				continue;
			
			Element namespaceElement = document.createElement( "namespace" );
			namespacesElement.appendChild( namespaceElement );
			
			namespaceElement.setAttribute( "uri", uri );
			namespaceElement.setAttribute( "prefix", prefix );
			
			//check for default
			if ( uri.equals( namespaces.get( "" ) ) ) {
				namespaceElement.setAttribute( "default", "true" );
			}
		}
	}
	
	/**
	 * Writes "style" elements to the catalog.xml file.
	 * 
	 * @param styles map of <id,filename>
	 * 
	 * @throws Exception If error writing "styles" element.
	 */
	public void styles( Map styles ) throws Exception {
		Element stylesElement = document.createElement( "styles" );
		catalog.appendChild( stylesElement );
		
		for ( Iterator s = styles.entrySet().iterator(); s.hasNext(); ) {
			Map.Entry style = (Map.Entry) s.next();
			String id = (String) style.getKey();
			String filename = (String) style.getValue();
			
			Element styleElement = document.createElement( "style" );
			stylesElement.appendChild( styleElement );
			
			styleElement.setAttribute( "id", id );
			styleElement.setAttribute( "filename", filename );
		}
	}
	
	/**
	 * WRites the catalog.xml file.
	 * <p>
	 * This method *must* be called after any other methods.
	 * </p>
	 * 
	 * @param file The catalog.xml file.
	 * 
	 * @throws IOException In event of a writing error.
	 */
	public void write( File file ) throws IOException {
		try {
			Transformer tx = TransformerFactory.newInstance().newTransformer();
			DOMSource source = new DOMSource( document );
			StreamResult result = new StreamResult( file );
			
			tx.transform( source, result );
		} 
		catch( Exception e ) {
			String msg = "Could not write catalog to " + file;
			throw (IOException) new IOException( msg ).initCause( e );
		}
		
	}
}
