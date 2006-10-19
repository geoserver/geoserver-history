package org.geoserver.wfs.v1_1_0.http;

import org.geoserver.http.GeoServerHttpTestSupport;
import org.w3c.dom.Document;

import com.meterware.httpunit.WebResponse;

public class GetCapabilitiesHttpTest extends GeoServerHttpTestSupport {

	public void testGet() throws Exception {
		if ( isOffline() )
			return;
		
	    WebResponse response = get( "wfs?service=WFS&request=getCapabilities&version=1.1.0" );
		Document doc = response.getDOM();
		assertEquals( "WFS_Capabilities", doc.getDocumentElement().getNodeName() );
		assertEquals( "1.1.0", doc.getDocumentElement().getAttribute( "version" ) );
	}
	
	public void testPost() throws Exception {
		if ( isOffline() )
			return;
		
		String xml = "<GetCapabilities service=\"WFS\" version=\"1.1.0\"" + 
				 " xmlns=\"http://www.opengis.net/wfs\" " + 
				 " xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" " + 
				 " xsi:schemaLocation=\"http://www.opengis.net/wfs " +
				 " http://schemas.opengis.net/wfs/1.1.0/wfs.xsd\"/>";
	    
        WebResponse response = post( "wfs", xml );
        		
		Document doc = response.getDOM();
		assertEquals( "WFS_Capabilities", doc.getDocumentElement().getNodeName() );
		assertEquals( "1.1.0", doc.getDocumentElement().getAttribute( "version" ) );
	}
	
	public void testPostNoSchemaLocation() throws Exception {
		if ( isOffline() )
			return;
		
		String xml = "<GetCapabilities service=\"WFS\" version=\"1.1.0\"" + 
				 " xmlns=\"http://www.opengis.net/wfs\" " + 
				 " xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" />";
	    
        WebResponse response = post( "wfs", xml );
        		
		Document doc = response.getDOM();
		assertEquals( "WFS_Capabilities", doc.getDocumentElement().getNodeName() );
		assertEquals( "1.1.0", doc.getDocumentElement().getAttribute( "version" ) );
	}
	
}
