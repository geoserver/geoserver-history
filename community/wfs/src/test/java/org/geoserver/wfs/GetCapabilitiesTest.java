package org.geoserver.wfs;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;

import org.geoserver.util.ReaderUtils;
import org.geotools.xml.transform.TransformerBase;
import org.w3c.dom.Element;

public class GetCapabilitiesTest extends WFSTestSupport {

	protected void setUp() throws Exception {
		super.setUp();
	}
	
	public void testGetCapabilities() throws Exception {
		
		TransformerBase tx = webFeatureService.getCapabilities();
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		tx.transform( null, outputStream );
		
		Element capsElement = ReaderUtils.parse( 
			new InputStreamReader( new ByteArrayInputStream( outputStream.toByteArray() ) )
		);
		assertEquals( "WFS_Capabilities", capsElement.getNodeName() );
	}
	
}
 