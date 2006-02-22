package org.geoserver.test.complex;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.apache.xerces.parsers.DOMParser;
import org.vfny.geoserver.AbstractGeoserverHttpTest;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.meterware.httpunit.GetMethodWebRequest;
import com.meterware.httpunit.PostMethodWebRequest;
import com.meterware.httpunit.WebConversation;
import com.meterware.httpunit.WebRequest;
import com.meterware.httpunit.WebResponse;

public class ComplexTest extends AbstractGeoserverHttpTest {

	protected void setUp() throws Exception {
		super.setUp();
	}
	
	public void testDescribeFeatureType() throws Exception {
		String s = "<DescribeFeatureType " + 
		"version=\"1.0.0\" " +  
		  "service=\"WFS\" " + 
		  "xmlns=\"http://www.opengis.net/wfs\" " + 
		  "xmlns:topp=\"http://www.openplans.org/topp\" " + 
		  "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-insance\" " + 
		  "xsi:schemaLocation=\"http://www.opengis.net/wfs http://schemas.opengis.net/wfs/1.0.0/WFS-basic.xsd\">" +
		  
		  	"<TypeName>topp:RoadSegment</TypeName>" + 
		    
		"</DescribeFeatureType>";
		
		
		InputStream in = new ByteArrayInputStream(s.getBytes());
		
		WebConversation conversation = new WebConversation();
        WebRequest request = 
        	new PostMethodWebRequest(getBaseUrl()+"/wfs",in,"text/xml");
        
        WebResponse response = conversation.getResponse( request );
        DOMParser parser = new DOMParser();
        parser.parse(new InputSource(response.getInputStream()));
        
        Element e = parser.getDocument().getDocumentElement();
        assertEquals("schema", e.getLocalName());
        
        Element type = null;
        for (int i = 0; i < e.getChildNodes().getLength(); i++) {
        	Node child = e.getChildNodes().item(i);
        	if ("complexType".equals(child.getLocalName())) {
        		type = (Element) child;
        		break;
        	}
        }
        assertNotNull(type);
        assertEquals("RoadSegment_Type", type.getAttribute("name"));
    }

	public void testGetFeature() throws Exception {
		String s = "<wfs:GetFeature service=\"WFS\" version=\"1.0.0\" " + 
			  "outputFormat=\"GML2\" " + 
			  "xmlns:topp=\"http://www.openplans.org/topp\" " + 
			  "xmlns:wfs=\"http://www.opengis.net/wfs\" " + 
			  "xmlns:ogc=\"http://www.opengis.net/ogc\" " + 
			  "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" " + 
			  "xsi:schemaLocation=\"http://www.opengis.net/wfs http://schemas.opengis.net/wfs/1.0.0/WFS-basic.xsd\">" +
			  "<wfs:Query typeName=\"topp:states\"></wfs:Query>" + 
			"</wfs:GetFeature>";
		
		InputStream in = new ByteArrayInputStream(s.getBytes());
		
		WebConversation conversation = new WebConversation();
        WebRequest request = 
        	new PostMethodWebRequest(getBaseUrl()+"/wfs",in,"text/xml");
        
        WebResponse response = conversation.getResponse( request );
        DOMParser parser = new DOMParser();
        parser.parse(new InputSource(response.getInputStream()));
        
        assertNotSame("ServiceExceptionReport",parser.getDocument().getDocumentElement().getLocalName());
	}
}
