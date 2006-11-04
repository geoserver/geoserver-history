package org.geoserver.wfs.v1_1_0;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.geoserver.util.ErrorHandler;
import org.geoserver.util.ReaderUtils;
import org.geoserver.wfs.CapabilitiesTransformer;
import org.geoserver.wfs.WFSTestSupport;
import org.geoserver.wfs.xml.v1_1_0.WFS;

public class CapabilitiesTransformerTest extends WFSTestSupport {

	static Logger logger = Logger.getLogger( "org.geoserver.wfs.test" );
	
	public void test() throws Exception {
		CapabilitiesTransformer tx = new CapabilitiesTransformer.WFS1_1( wfs, catalog );
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		tx.transform( null, output );
		
		InputStreamReader reader = 
			new InputStreamReader( new ByteArrayInputStream( output.toByteArray() ) );
	
		ErrorHandler handler = new ErrorHandler( logger, Level.WARNING );
		ReaderUtils.validate( 
			reader, handler, WFS.NAMESPACE, "http://schemas.opengis.net/wfs/1.1.0/wfs.xsd"  
		);
		
		assertTrue( handler.errors.isEmpty() );
	}
}
