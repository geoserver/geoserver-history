package org.geoserver.template;

import java.io.File;
import java.io.IOException;
import java.io.Reader;

import org.vfny.geoserver.global.ConfigurationException;
import org.vfny.geoserver.global.GeoserverDataDirectory;

import freemarker.cache.FileTemplateLoader;
import freemarker.cache.TemplateLoader;
import freemarker.template.Configuration;

/**
 * A freemarker template loader which can load templates from locations under
 * a GeoServer data directory.
 * <p>
 * To use this template loader, use the {@link Configuration#setTemplateLoader(TemplateLoader)}
 * method:
 * <pre>
 * 	<code>
 *  Configuration cfg = new Configuration();
 *  cfg.setTemplateLoader( new GeoServerTemplateLoader() );
 *  ...
 *  Template template = cfg.getTemplate( "foo.ftl" );
 *  ...
 * 	</code>
 * </pre>
 * </p>
 * <p>
 * In {@link #findTemplateSource(String)}, the following lookup heuristic is 
 * applied to locate a file based on the given path.
 * <ol>
 * 	<li>The path directly
 * 	<li>The path relative to '<data_dir>/templates'
 *  <li>The path relative to '<data_dir>/featureTypes'
 *  <li>The path relative each directory under '<data_dir>/featureTypes'
 * </ol>
 * </p>
 *
 * @author Justin Deoliveira, The Open Planning Project, jdeolive@openplans.org
 *
 */
public class GeoServerTemplateLoader implements TemplateLoader {

	/**
	 * Delegate file based template loader
	 */
	FileTemplateLoader loader;
	
	public GeoServerTemplateLoader() throws IOException {
		//create a file template loader to delegate to
		loader = new FileTemplateLoader();
	}
	
	public Object findTemplateSource(String path) throws IOException {
		//first try the template directly
		File template = (File) loader.findTemplateSource( path );
		if ( template != null ) {
			return template;
		}
		
		//next, check the templates directory
		try {
			File templates = GeoserverDataDirectory.findConfigFile( "templates" );
			if ( templates.exists() && templates.isDirectory() ) {
				template = (File) loader.findTemplateSource( 
					new File( templates, path ).getAbsolutePath()
				);
			}
			if ( template != null ) {
				return template;
			}
		} 
		catch (ConfigurationException e) {
			throw (IOException) new IOException().initCause( e );
		}
		
		//next, try relative to feature types
		try {
			File featureTypes = 
				GeoserverDataDirectory.findConfigFile( "featureTypes" );
			if ( featureTypes.exists() && featureTypes.isDirectory() ) {
				template = (File) loader.findTemplateSource( 
					new File( featureTypes, path ).getAbsolutePath()	
				);
				if( template != null ) {
					return template;
				}
			}
			
			//lastly, try looking for each feature type
			File[] dirs = featureTypes.listFiles();
			for ( int i = 0; i < dirs.length; i++ ) {
				if ( dirs[ i ].isDirectory() ) {
					template = (File) loader.findTemplateSource(
						new File( dirs[ i ], path ).getAbsolutePath()	
					);
					if ( template != null ) {
						return null;
					}
				}
			}
			
		}
		catch(ConfigurationException e ) {
			throw (IOException) new IOException().initCause( e );
		}
		
		return null;
	}

	public long getLastModified(Object source) {
		return loader.getLastModified( source );
	}

	public Reader getReader(Object source, String encoding) throws IOException {
		return loader.getReader( source, encoding );
	}
	
	public void closeTemplateSource(Object source) throws IOException {
		loader.closeTemplateSource( source );
	}

}
