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
 * This class contains convenience methods for locating GeoServer 
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
	 * @return
	 */
	public File featureTypes() {
		try {
			File featureTypes = loader.find( "featureTypes" );
			if ( featureTypes == null ) {
				throw new NullPointerException();
			}
			if ( !featureTypes.exists() ) {
				throw new FileNotFoundException( featureTypes.getAbsolutePath() );
			}
			if ( !featureTypes.isDirectory() ) {
				String msg = featureTypes.getAbsolutePath() + " is a file, not a directory";
				throw new IllegalStateException( msg );
			}
			
			return featureTypes;
		} 
		catch ( IOException e ) {
			String msg = "Could not locate featureTypes directory";
			logger.log( Level.WARNING, msg, e );
		}
		
		return null;
	}
	
}
