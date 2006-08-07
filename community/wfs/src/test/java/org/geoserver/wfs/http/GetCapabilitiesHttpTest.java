package org.geoserver.wfs.http;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.geoserver.http.GeoServerHttpTestSupport;
import org.w3c.dom.Document;

import com.meterware.httpunit.GetMethodWebRequest;
import com.meterware.httpunit.PostMethodWebRequest;
import com.meterware.httpunit.WebConversation;
import com.meterware.httpunit.WebRequest;
import com.meterware.httpunit.WebResponse;

public class GetCapabilitiesHttpTest extends GeoServerHttpTestSupport {

	
	public void testGet() throws Exception {
		if ( isOffline() )
			return;
		
		WebConversation conversation = new WebConversation();
        WebRequest request = 
        		new GetMethodWebRequest(getBaseUrl()+"/wfs?service=WFS&request=getCapabilities");
        
        WebResponse response = 
        		conversation.getResponse( request );
		Document doc = response.getDOM();
		assertEquals( "WFS_Capabilities", doc.getDocumentElement().getNodeName() );
        
	}
	
	public void testPost() throws Exception {
		if ( isOffline() )
			return;
		
		String xml = "<GetCapabilities service=\"WFS\" version=\"1.0.0\"" + 
				 " xmlns=\"http://www.opengis.net/wfs\" " + 
				 " xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" " + 
				 " xsi:schemaLocation=\"http://www.opengis.net/wfs " +
				 " http://schemas.opengis.net/wfs/1.0.0/WFS-basic.xsd\"/>";
	
		InputStream input = new ByteArrayInputStream( xml.getBytes() );
		WebConversation conversation = new WebConversation();
        PostMethodWebRequest request = 
        		new PostMethodWebRequest(getBaseUrl()+"/wfs", input, "text/xml" );
        
        WebResponse response = 
        		conversation.getResponse( request );
		Document doc = response.getDOM();
		assertEquals( "WFS_Capabilities", doc.getDocumentElement().getNodeName() );
		
	}
}
