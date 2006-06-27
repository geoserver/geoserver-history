package org.geoserver.data;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.geoserver.util.ReaderUtils;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * Reads the GeoServer catalog.xml file.
 * <p>
 * Usage:
 * 
 * <pre>
 * 	<code>
 * 		File catalog = new File( ".../catalog.xml" );
 * 		CatalogReader reader = new CatalogReader();
 * 		reader.read( catalog );
 * 		List dataStores = reader.dataStores();
 * 		LIst nameSpaces = reader.nameSpaces();
 * 	</code>
 * </pre>
 * </p>
 * 
 * @author Justin Deoliveira, The Open Planning Project, jdeolive@openplans.org
 *
 */
public class CatalogReader {

	/** 
	 * Root catalog element.
	 */
	Element catalog;
	
	/**
	 * Parses the catalog.xml file into a DOM.
	 * <p>
	 * This method *must* be called before any other methods.
	 * </p>
	 * 
	 * @param file The catalog.xml file.
	 * 
	 * @throws IOException In event of a parser error.
	 */
	public void read( File file ) throws IOException {
		catalog = ReaderUtils.parse( new FileReader( file ) );
	}
	
	/**
	 * Reads "datastore" elements from the catalog.xml file.
	 * <p>
	 *  For each datastore element read, a map of the connection parameters is
	 *  created.
	 *  </p>
	 * 
	 * @return A list of Map objects containg the datastore connection parameters.
	 * 
	 * @throws Exception If error processing "datastores" element.
	 */
	public List/*<Map>*/ dataStores() throws Exception {
		Element dataStoresElement = 
			ReaderUtils.getChildElement( catalog, "datastores", true );
		
		NodeList dataStoreElements = 
			dataStoresElement.getElementsByTagName( "datastore" );
		ArrayList dataStores = new ArrayList();
		
		for ( int i = 0; i < dataStoreElements.getLength(); i++ ) {
			Element dataStoreElement = (Element) dataStoreElements.item( i );
			
			try {
				Map params = params( dataStoreElement );
				dataStores.add( params );
			} 
			catch (Exception e) {
				//TODO: log this
				continue;
			}
		}
		
		return dataStores;
	}
	
	/**
	 * Convenience method for reading connection parameters from a datastore
	 * element.
	 * 
	 * @param dataStoreElement The "datastore" element.
	 * 
	 * @return The map of connection paramters.
	 * 
	 * @throws Exception If problem parsing any parameters.
	 */
	protected Map params( Element dataStoreElement ) throws Exception {
		Element paramsElement = ReaderUtils.getChildElement( 
				dataStoreElement, "connectionParameters", true ); 
		NodeList paramList = paramsElement.getElementsByTagName( "parameter" );
		
		Map params = new HashMap();
		for ( int i = 0; i < paramList.getLength(); i++ ) {
			Element paramElement = (Element) paramList.item( i );
			String key = ReaderUtils.getAttribute( paramElement, "name", true );
			String value = 
				ReaderUtils.getAttribute( paramElement, "value", true );
			
			params.put( key, value );
		}
		
		return params;
	}
}
