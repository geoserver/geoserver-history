package org.geoserver.wfs.v1_1_0;

import java.io.StringWriter;
import java.net.URL;

import org.geoserver.data.DataTestSupport;
import org.geoserver.wfs.WFS;

import junit.framework.TestCase;

public class WFSCapabiltiiesTransformerTest extends DataTestSupport {

	public void testTransform() throws Exception {
		
		WFS wfs = new WFS();
		wfs.setOnlineResource( new URL( "http://localhost:8080/geoserver/wfs" ) );
		
		
		WFSCapabilitiesTransformer tx = new WFSCapabilitiesTransformer( wfs, catalog );
		tx.setIndentation( 2 );
		
		StringWriter writer = new StringWriter();
		tx.transform( null, writer );
		
	}
}
