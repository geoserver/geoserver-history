package org.geoserver.wfs;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;

import org.geoserver.util.ReaderUtils;
import org.w3c.dom.Element;

public class GetCapabilitiesTest extends WFSTestSupport {

	GetCapabilities getCapabilities;
	
	protected void setUp() throws Exception {
		super.setUp();
		
		getCapabilities = new GetCapabilities( wfs, catalog );
	}
	
	public void testGetCapabilities() throws Exception {
		
		WFSCapsTransformer tx = getCapabilities.getCapabilities();
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		tx.transform( null, outputStream );
		
		Element capsElement = ReaderUtils.parse( 
			new InputStreamReader( new ByteArrayInputStream( outputStream.toByteArray() ) )
		);
		assertEquals( "WFS_Capabilities", capsElement.getNodeName() );
	}
	
}
 