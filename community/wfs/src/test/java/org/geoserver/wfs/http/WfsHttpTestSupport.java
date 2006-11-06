package org.geoserver.wfs.http;

import org.geoserver.data.test.DataApplicationContext;
import org.geoserver.web.test.GeoServerHttpTestSupport;
import org.geoserver.web.test.WebApplicationContext;
import org.geoserver.wfs.test.WfsApplicationContext;

/**
 * Test support class for WFS.
 * 
 * @author Justin Deoliveira, The Open Planning Project
 *
 */
public class WfsHttpTestSupport extends GeoServerHttpTestSupport {

	protected void setUp() throws Exception {
		super.setUp();
		
		//load up some bean definitions
		getGeoServer().loadBeanDefinitions( DataApplicationContext.getBeanDefinitions() );
		getGeoServer().loadBeanDefinitions( WebApplicationContext.getBeanDefinitions() );
		getGeoServer().loadBeanDefinitions( WfsApplicationContext.getBeanDefinitions() );
	}
	
	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
}
