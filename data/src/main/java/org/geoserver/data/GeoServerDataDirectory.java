package org.geoserver.data;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.geoserver.GeoServerResourceLoader;

/**
 * The GeoServer data directory.
 * <p>
 * This class is a facade which contains convenience methods for locating GeoServer 
 * configuration and data files.
 * </p>
 * @author Justin Deoliveira, The Open Planning Project
 *
 */
public class GeoServerDataDirectory {

	static Logger logger = Logger.getLogger( "org.geoserver.data" );
	
	/**
	 * The underlying resource loader
	 */
	GeoServerResourceLoader loader;
	
	public GeoServerDataDirectory( GeoServerResourceLoader loader ) {
		this.loader = loader;
	}
	
	/**
	 * The 'featureTypes' directory which contains feature type metadata.
	 * <p>
	 * In the event the directory does not exists, an attempt will be made to create it.
	 * </p>
	 * 
	 * @return The featureTypes directory.
	 * 
	 * @throws IOException Any io errors, or if the directory does not exist and could
	 * not be created.
	 */
	public File featureTypes() throws IOException {
		return directory( "featureTypes" );
	}
	
	/**
	 * The 'styles' directory which contains style data.
	 * <p>
	 * In the event the directory does not exists, an attempt will be made to create it.
	 * </p>
	 * 
	 * @return The styles directory.
	 * 
	 * @throws IOException Any io errors, or if the directory does not exist and could
	 * not be created.
	 */
	public File styles() throws IOException {
		return directory( "styles" );
	}
	
	File directory( String path ) throws IOException {
		File directory = loader.find( path );
		if ( directory == null ) {
			logger.warning( path + " not found, creating." );
			directory = loader.createDirectory( path );
		}
		
		return directory;
	}
}
