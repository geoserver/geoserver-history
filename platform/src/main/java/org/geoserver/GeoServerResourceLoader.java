package org.geoserver;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;

/**
 * Manages resources in GeoServer.
 * <p>
 * GeoServer has the notion of a "data directory", where it keeps all the data 
 * used to configure the server. This maps to the {@link #baseDirectory} 
 * property of the resource loader. The loader also maintains a search path in 
 * which it will also use to look up resources. The baseDirectory is implicitly
 * added to the search path.
 * </p>
 * <p>
 * File dataDirectory = ...
 * GeoServerResourceLoader loader = new GeoServerResourceLoader( dataDirectory );
 * loader.addSearchLocation( new File( "/WEB-INF/" ) );
 * loader.addSearchLocation( new File( "/data" ) );
 * ...
 * File catalog = loader.find( "catalog.xml" );
 * </p>
 * 
 * @author Justin Deoliveira, The Open Planning Project, jdeolive@openplans.org
 *
 */
public class GeoServerResourceLoader extends DefaultResourceLoader {

	/**
	 * "path" for resource lookups
	 */
	Set searchLocations;
	
	/**
	 * Base directory
	 */
	File baseDirectory;
	
	/**
	 * Creates a new resource loader with no base directory.
	 * <p>
	 * Such a constructed resource loader is not capable of creating resources
	 * from relative paths.
	 * </p>
	 */
	public GeoServerResourceLoader() {
		searchLocations = new TreeSet();
	}
	
	/**
	 * Creates a new resource loader.
	 * 
	 * @param baseDirectory The directory in which 
	 */
	public GeoServerResourceLoader( File baseDirectory ) {
		this();
		this.baseDirectory = baseDirectory;
		searchLocations.add( baseDirectory );
	}
	
	/**
	 * Adds a location to the path used for resource lookups.
	 *
	 * @param A directory containing resources.
	 */
	public void addSearchLocation( File searchLocation ) {
		searchLocations.add( searchLocation );
	}
	
	/**
	 * @return The base directory.
	 */
	public File getBaseDirectory() {
		return baseDirectory;
	}
	
	/**
	 * Sets the base directory.
	 * 
	 * @param baseDirectory
	 */
	public void setBaseDirectory(File baseDirectory) {
		this.baseDirectory = baseDirectory;
		
		searchLocations.add( baseDirectory );
	} 
	
	/**
	 * Performs a resource lookup. 
	 * 
	 * @param location The name of the resource to lookup, can be absolute or 
	 * relative.
	 * 
	 * @return The file handle representing the resource, or null if the 
	 * resource could not be found.
	 * 
	 * @throws IOException In the event of an I/O error.
	 */
	public File find ( String location ) throws IOException {
		
		//first to an existance check
		File file = new File( location );
		if ( file.isAbsolute() ) {
			return file;
		}
		else {
			//try a relative url
			for ( Iterator f = searchLocations.iterator(); f.hasNext(); ) {
				File base = (File) f.next();
				file = new File( base, location );
				
				if ( file.exists() ) {
					return file;
				}
			}
		}
		
		Resource resource = getResource( location );
		if ( resource.exists() ) {
			return resource.getFile();
		}
		
		return null;
	}
	
	/**
	 * Creates a new directory.
	 * <p>
	 * Relative paths are created relative to {@link #baseDirectory}.
	 * </p>
	 * .
	 * @param location Location of directory to create, either absolute or 
	 * relative.
	 * 
	 * @return The file handle of the created directory.
	 * 
	 * @throws IOException In the event of an I/O error.
	 */
	public File createDirectory ( String location ) throws IOException {
		File file = find( location );
		if ( file != null ) {
			if ( !file.isDirectory() ) {
				String msg = location + " already exists and is not directory";
				throw new IOException( msg );
			}
		}
			
		
		file = new File( location );
		if ( file.isAbsolute() ) {
			file.mkdir();
			return file;
		}
		
		//no base directory set, cannot create a relative path
		if ( baseDirectory == null ) {
			//TODO: log or throw exception
			return null;
		}
		
		file = new File( baseDirectory, location );
		file.mkdir();
		return file;
	}
	
	/**
	 * Creates a new file.
	 * <p>
	 * Relative paths are created relative to {@link #baseDirectory}.
	 * </p>
	 * .
	 * @param location Location of file to create, either absolute or relative.
	 * 
	 * @return The file handle of the created file.
	 * 
	 * @throws IOException In the event of an I/O error.
	 */
	public File createFile( String location ) throws IOException {
		File file = find( location );
		if ( file != null ) 
			return file;
		
		file = new File( location );
		if ( file.isAbsolute() ) {
			file.createNewFile();
			return file;
		}
		
		//no base directory set, cannot create a relative path
		if ( baseDirectory == null ) {
			//TODO: log or throw exception
			return null;
		}
		
		file = new File( baseDirectory, location );
		file.createNewFile();
		return file;
	}
	
}
