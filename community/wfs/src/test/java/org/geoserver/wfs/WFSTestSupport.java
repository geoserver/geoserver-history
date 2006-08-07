package org.geoserver.wfs;

import java.net.URL;
import java.nio.charset.Charset;

import org.geoserver.data.DataTestSupport;
import org.geoserver.data.GeoServerCatalog;
import org.geoserver.data.GeoServerResolveAdapterFactoryFinder;
import org.geoserver.data.feature.InfoAdapterFactory;
import org.springframework.context.support.GenericApplicationContext;

/**
 * Abstract base class for wfs unit tests.
 
 * 
 * @author Justin Deoliveira, The Open Planning Project, jdeolive@openplans.org
 *
 */
public class WFSTestSupport extends DataTestSupport {

	protected GeoServerResolveAdapterFactoryFinder finder;
	
	protected GenericApplicationContext context;
	
	protected WFS wfs;
	
	/**
	 * Configures the application context and the WFS configuraiton bean.
	 * <p>
	 * This method should be <b>extended</b>, not <b>overridden</b>
	 * </p>
	 */
	protected void setUp() throws Exception {
		super.setUp();
		
		context = new GenericApplicationContext();
		context.getBeanFactory().registerSingleton( "loader", loader );
		context.getBeanFactory().registerSingleton( "catalog", catalog );
		context.getBeanFactory().registerSingleton( "finder", finder );
		context.getBeanFactory().registerSingleton( 
			"infoAdapterFactory", new InfoAdapterFactory( catalog, loader ) 
		);
		
		finder.setApplicationContext( context );
	
		//create wfs service bean and populate
		wfs = new WFS();
		wfs.setOnlineResource( new URL( "http://localhost:8080/geoserver/wfs?" ) );
		wfs.setSchemaBaseURL( "http://schemas.opengis.net/" );
		wfs.setCharSet( Charset.forName( "UTF-8" ) );
		
	}
}
