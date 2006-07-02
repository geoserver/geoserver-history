package org.geoserver.wfs;

import org.geoserver.data.DataTestSupport;
import org.springframework.context.support.GenericApplicationContext;

/**
 * Abstract base class for wfs unit tests.
 
 * 
 * @author Justin Deoliveira, The Open Planning Project, jdeolive@openplans.org
 *
 */
public class WFSTestSupport extends DataTestSupport {

	WFS wfs;
	GenericApplicationContext context;
	
	protected void setUp() throws Exception {
		//create wfs service bean and populate
		wfs = new WFS();
	}
	
}
