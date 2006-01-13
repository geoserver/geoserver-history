package org.vfny.geoserver.requests.wms;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import javax.imageio.ImageIO;

import org.vfny.geoserver.AbstractGeoserverHttpTest;

import com.meterware.httpunit.GetMethodWebRequest;
import com.meterware.httpunit.PostMethodWebRequest;
import com.meterware.httpunit.WebConversation;
import com.meterware.httpunit.WebRequest;
import com.meterware.httpunit.WebResponse;

public class GetMapTest extends AbstractGeoserverHttpTest {
	
	String bbox = "-130,24,-66,50";
	String styles = "population";
	String layers = "states";
	
	public void testImage() throws Exception {
		WebConversation conversation = new WebConversation();
		WebRequest request = 
        	new GetMethodWebRequest(
    			getBaseUrl()+"/wms?bbox=" + bbox + 
    				"&styles=" + styles + 
    				"&layers=" + layers + 
    				"&Format=image/png" + 
    				"&request=GetMap" +
    				"&width=550" +
    				"&height=250" +
    				"&srs=EPSG:4326"
				);

        WebResponse response = conversation.getResponse( request );
        assertEquals("image/png",response.getContentType());
        
        try {
        	BufferedImage image = ImageIO.read(response.getInputStream());
        	assertNotNull(image);
        	assertEquals(image.getWidth(),550);
        	assertEquals(image.getHeight(),250);
        }
        catch(Throwable t) {
        	t.printStackTrace();
        	fail("Could not read image returned from GetMap:" + t.getLocalizedMessage());
        }
	}
	
	public void testSldBody() throws Exception {
		WebConversation conversation = new WebConversation();
		WebRequest request = 
        	new GetMethodWebRequest(
    			getBaseUrl()+"/wms?bbox=" + bbox + 
    				"&styles=" + styles + 
    				"&layers=" + layers + 
    				"&Format=image/png" + 
    				"&request=GetMap" +
    				"&width=550" +
    				"&height=250" +
    				"&srs=EPSG:4326" + 
    				"&SLD_BODY=%3CStyledLayerDescriptor%20version=%221.0.0%22%3E%3CUserLayer%3E%3CName%3Etopp:states%3C/Name%3E%3CUserStyle%3E%3CName%3EUserSelection%3C/Name%3E%3CFeatureTypeStyle%3E%3CRule%3E%3CFilter%20xmlns:gml=%22http://www.opengis.net/gml%22%3E%3CPropertyIsEqualTo%3E%3CPropertyName%3ESTATE_NAME%3C/PropertyName%3E%3CLiteral%3EIllinois%3C/Literal%3E%3C/PropertyIsEqualTo%3E%3C/Filter%3E%3CPolygonSymbolizer%3E%3CFill%3E%3CCssParameter%20name=%22fill%22%3E%23FF0000%3C/CssParameter%3E%3C/Fill%3E%3C/PolygonSymbolizer%3E%3C/Rule%3E%3CRule%3E%3CLineSymbolizer%3E%3CStroke/%3E%3C/LineSymbolizer%3E%3C/Rule%3E%3C/FeatureTypeStyle%3E%3C/UserStyle%3E%3C/UserLayer%3E%3C/StyledLayerDescriptor%3E"
				);

        WebResponse response = conversation.getResponse( request );
        assertEquals("image/png",response.getContentType());
        
        try {
        	BufferedImage image = ImageIO.read(response.getInputStream());
        	assertNotNull(image);
        	assertEquals(image.getWidth(),550);
        	assertEquals(image.getHeight(),250);
        }
        catch(Throwable t) {
        	t.printStackTrace();
        	fail("Could not read image returned from GetMap:" + t.getLocalizedMessage());
        }
	}
	
	public void testSldBodyPost() throws Exception {
		InputStream in = 
			new ByteArrayInputStream(STATES_SLD.getBytes());
		
		WebConversation conversation = new WebConversation();
		WebRequest request = 
        	new PostMethodWebRequest(
    			getBaseUrl()+"/wms?bbox=" + bbox + 
    				"&Format=image/png" + 
    				"&request=GetMap" +
    				"&width=550" +
    				"&height=250" +
    				"&srs=EPSG:4326", 
    				in, "text/xml"
				);

        WebResponse response = conversation.getResponse( request );
        assertEquals("image/png",response.getContentType());
        
        try {
        	BufferedImage image = ImageIO.read(response.getInputStream());
        	assertNotNull(image);
        	assertEquals(image.getWidth(),550);
        	assertEquals(image.getHeight(),250);
        }
        catch(Throwable t) {
        	t.printStackTrace();
        	fail("Could not read image returned from GetMap:" + t.getLocalizedMessage());
        }
    }
	
	public static final String STATES_SLD = 
"<StyledLayerDescriptor version=\"1.0.0\">" + 
  "<UserLayer>" + 
    "<Name>topp:states</Name>" + 
    "<UserStyle>" + 
      "<Name>UserSelection</Name>" + 
      "<FeatureTypeStyle>" + 
        "<Rule>" + 
          "<Filter xmlns:gml=\"http://www.opengis.net/gml\">" + 
            "<PropertyIsEqualTo>" + 
              "<PropertyName>STATE_NAME</PropertyName>" + 
              "<Literal>Illinois</Literal>" + 
            "</PropertyIsEqualTo>" + 
          "</Filter>" + 
          "<PolygonSymbolizer>" + 
            "<Fill>" + 
              "<CssParameter name=\"fill\">#FF0000</CssParameter>" + 
            "</Fill>" + 
          "</PolygonSymbolizer>" + 
        "</Rule>" + 
        "<Rule>" + 
          "<LineSymbolizer>" + 
            "<Stroke/>" + 
          "</LineSymbolizer>" + 
        "</Rule>" + 
      "</FeatureTypeStyle>" + 
    "</UserStyle>" + 
  "</UserLayer>" + 
"</StyledLayerDescriptor>"; 
}
