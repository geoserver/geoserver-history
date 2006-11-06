package org.geoserver.wfs.v1_1_0.http;

import org.geoserver.wfs.http.WfsHttpTestSupport;
import org.w3c.dom.Document;

public class GetCapabilitiesHttpTest extends WfsHttpTestSupport {

	public void testGet() throws Exception {
		Document doc = getAsDOM( "wfs?service=WFS&request=getCapabilities&version=1.1.0" );
		
		assertEquals( "wfs:WFS_Capabilities", doc.getDocumentElement().getNodeName() );
		assertEquals( "1.1.0", doc.getDocumentElement().getAttribute( "version" ) );
	}
	
	public void testPost() throws Exception {
		
		String xml = "<GetCapabilities service=\"WFS\" version=\"1.1.0\"" + 
				 " xmlns=\"http://www.opengis.net/wfs\" " + 
				 " xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" " + 
				 " xsi:schemaLocation=\"http://www.opengis.net/wfs " +
				 " http://schemas.opengis.net/wfs/1.1.0/wfs.xsd\"/>";
	    
		Document doc = postAsDOM( "wfs", xml );
        assertEquals( "wfs:WFS_Capabilities", doc.getDocumentElement().getNodeName() );
		assertEquals( "1.1.0", doc.getDocumentElement().getAttribute( "version" ) );
	}
	
	public void testPostNoSchemaLocation() throws Exception {
		String xml = "<GetCapabilities service=\"WFS\" version=\"1.1.0\"" + 
				 " xmlns=\"http://www.opengis.net/wfs\" " + 
				 " xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" />";
	    
		Document doc = postAsDOM( "wfs", xml );
        assertEquals( "wfs:WFS_Capabilities", doc.getDocumentElement().getNodeName() );
		assertEquals( "1.1.0", doc.getDocumentElement().getAttribute( "version" ) );
	}
	
}
