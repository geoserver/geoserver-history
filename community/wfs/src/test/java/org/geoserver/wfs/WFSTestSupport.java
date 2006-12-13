package org.geoserver.wfs;

import java.net.URL;
import java.nio.charset.Charset;

import org.geoserver.data.test.DataTestSupport;
import org.geotools.filter.FilterFactoryFinder;

/**
 * Abstract base class for wfs unit tests.
 *
 * 
 * @author Justin Deoliveira, The Open Planning Project, jdeolive@openplans.org
 *
 */
public class WFSTestSupport extends DataTestSupport {

	/**
	 * The wfs configuration
	 */
	protected WFS wfs;
	
	/**
	 * The service implementation
	 */
	protected DefaultWebFeatureService webFeatureService;
	
	/**
	 * Configures the application context and the WFS configuraiton bean.
	 * <p>
	 * This method should be <b>extended</b>, not <b>overridden</b>
	 * </p>
	 */
	protected void setUpInternal() throws Exception {
		super.setUpInternal();
		
		//create wfs service bean and populate
		wfs = new WFS();
		wfs.setOnlineResource( new URL( "http://localhost:8080/geoserver/wfs?" ) );
		wfs.setSchemaBaseURL( "http://schemas.opengis.net/" );
		wfs.setCharSet( Charset.forName( "UTF-8" ) );
		
		//create the service implementation
		webFeatureService = new DefaultWebFeatureService( wfs, catalog );
		webFeatureService.setFilterFactory( FilterFactoryFinder.createFilterFactory() );
		
		context.getBeanFactory().registerSingleton( "wfs", wfs );
		context.getBeanFactory().registerSingleton( "webFeatureService", webFeatureService );
	}
}
