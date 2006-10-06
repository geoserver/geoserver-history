package org.geoserver.data;

import java.io.IOException;
import java.util.List;

import org.geoserver.data.feature.DataStoreInfo;
import org.geoserver.data.feature.FeatureTypeInfo;
import org.geoserver.data.feature.StyleInfo;
import org.geotools.catalog.Catalog;
import org.geotools.catalog.GeoResource;
import org.geotools.catalog.Service;
import org.geotools.util.ProgressListener;
import org.xml.sax.helpers.NamespaceSupport;

/**
 * GeoServer catalog.
 * <p>
 * The GeoServer catalog extends the geotools catalog providing additional 
 * convenience methods specific to geoserver.
 * </p>
 * 
 * @author Justin Deoliveira, The Open Planning Project, jdeolive@openplans.org
 *
 */
public interface GeoServerCatalog extends Catalog {

	/**
	 * The namespace / prefix mappings for the application.
	 * 
	 * @return NamespaceSupport containing declared namespace uris and prefixed.
	 */
	NamespaceSupport getNamespaceSupport();
	
	/**
	 * Returns a list of service handles from the catalog which can resolve to 
	 * the supplied the class.
	 * 
	 * @param resolvee The class to test a resolve to.
	 * 
	 * @return List of {@link Service}, possbily empty, never null.
	 */
	List services( Class resolvee ) throws IOException;
	
	/**
	 * Returns a list of resource handles from the catalog which can resolve to 
	 * the supplied the class.
	 * 
	 * @param resolvee The class to test a resolve to.
	 * 
	 * @return List of {@link GeoResource}, possbily empty, never null.
	 */
	List resources( Class resolvee ) throws IOException;
		
	/**
	 * Returns a list of data store meta data objects respresnting active datastores
	 * in the catalog.
	 * 
	 * @return A list of {@link org.geoserver.data.feature.DataStoreInfo}.
	 * 
	 * @throws IOException
	 */
	List dataStores() throws IOException;
	
	/**
	 * Returns a single data store meta data object based on an identifier.
	 * <p>
	 * Returns <code>null</code> if no such data store could be found.
	 * </p>
	 * 
	 * @param id The unique identifier for the datastore.
	 * 
	 * @return The data store meta data, or <code>null</code>
	 * 
	 * @throws IOException
	 */
	DataStoreInfo dataStore( String id ) throws IOException;
	
	/**
	 * Returns a list of feature type meta data objects representing active
	 * feature types in the catalog.
	 * 
	 * @return A list of {@link org.geoserver.data.feature.FeatureTypeInfo}.
	 * 
	 * @throws IOException
	 */
	List featureTypes() throws IOException;
	
	/**
	 * Returns a single feature type meta data object based on a single identifier.
	 * <p>
	 * The feature type identifier is namespace prefix qualified as in <code>cfg:Points</code>.
	 * If a namespace prefix is absent as in <code>Points</code>, the default application 
	 * namespace is assumed. 
	 * </p>
	 * 
	 * @param identifier The namespace qualified feature type identifier.
	 *  
	 * @return The feature type meta data object, or <code>null</code> if none matching
	 * the specified identifier.
	 * 
	 * @throws IOException
	 */
	FeatureTypeInfo featureType( String identifier ) throws IOException;
	
	/**
	 * Returns a single feature type meta data object based on a namespace prefix
	 * and type name which together uniquley identify the feature type.
	 * <p>
	 * Returns <code>null</code> if no such feature type can be found.
	 * </p>
	 * 
	 * @param nsPrefix The namespace prefix of the feature type.
	 * @param typeName The type name of hte feautre type.
	 * 
	 * @return The feature type meta data, or <code>null</code>
	 * 
	 * @throws IOException
	 */
	FeatureTypeInfo featureType( String nsPrefix, String typeName ) throws IOException;

	/**
	 * Returns a list of style meta data object currently active in the caatalog.
	 * 
	 * @return A list of {@link org.geoserver.data.feature.StyleInfo}
	 * 
	 * @throws IOException
	 */
	List styles() throws IOException;
	
	/**
	 * Returns a single style meta data object based on its identifier.
	 * <p>
	 * Returns <code>null</code> if no style could be found.
	 * </p>
	 * 
	 * @param id The identifier of the style.
	 * 
	 * @throws IOException
	 */
	StyleInfo style( String id ) throws IOException;
	
}
	
