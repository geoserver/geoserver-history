package org.vfny.geoserver.wms.responses.map.png;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.servlet.ServletResponse;
import javax.xml.namespace.QName;

import junit.framework.Test;

import org.geoserver.data.test.MockData;
import org.geoserver.wms.RemoteOWSTestSupport;
import org.geoserver.wms.WMSTestSupport;

import com.mockrunner.mock.web.MockHttpServletResponse;

public class GetMapTest extends WMSTestSupport {
    String bbox = "-130,24,-66,50";
    String styles = "states";
    String layers = "sf:states";
    
    public static final String STATES_SLD = "<StyledLayerDescriptor version=\"1.0.0\">" +
    		"<UserLayer><Name>sf:states</Name><UserStyle><Name>UserSelection</Name>" +
    		"<FeatureTypeStyle><Rule><Filter xmlns:gml=\"http://www.opengis.net/gml\">" +
    		"<PropertyIsEqualTo><PropertyName>STATE_ABBR</PropertyName><Literal>IL</Literal></PropertyIsEqualTo>" +
    		"</Filter><PolygonSymbolizer><Fill><CssParameter name=\"fill\">#FF0000</CssParameter></Fill>" +
    		"</PolygonSymbolizer></Rule><Rule><LineSymbolizer><Stroke/></LineSymbolizer></Rule>" +
    		"</FeatureTypeStyle></UserStyle></UserLayer></StyledLayerDescriptor>";
    
    public static final String STATES_GETMAP =  //
        "<?xml version=\"1.0\" encoding=\"UTF-8\"?> \n "
        + "<ogc:GetMap service=\"WMS\"  version=\"1.1.1\" \n "
        + "        xmlns:gml=\"http://www.opengis.net/gml\"\n "
        + "        xmlns:ogc=\"http://www.opengis.net/ows\"\n "
        + "        xmlns:sld=\"http://www.opengis.net/sld\"\n "
        + "        xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n "
        + "        xsi:schemaLocation=\"http://www.opengis.net/ows GetMap.xsd http://www.opengis.net/gml geometry.xsd http://www.opengis.net/sld StyledLayerDescriptor.xsd \">\n "
        + "        <sld:StyledLayerDescriptor>\n " + "                <sld:NamedLayer>\n "
        + "                        <sld:Name>sf:states</sld:Name>\n "
        + "                        <sld:NamedStyle>\n "
        + "                                <sld:Name>Default</sld:Name>\n "
        + "                        </sld:NamedStyle>\n " + "                </sld:NamedLayer>\n "
        + "        </sld:StyledLayerDescriptor>\n "
        + "        <ogc:BoundingBox srsName=\"4326\">\n " + "                <gml:coord>\n "
        + "                        <gml:X>-130</gml:X>\n "
        + "                        <gml:Y>24</gml:Y>\n " + "                </gml:coord>\n "
        + "                <gml:coord>\n " + "                        <gml:X>-66</gml:X>\n "
        + "                        <gml:Y>50</gml:Y>\n " + "                </gml:coord>\n "
        + "        </ogc:BoundingBox>\n " + "        <ogc:Output>\n "
        + "                <ogc:Format>image/png</ogc:Format>\n " + "                <ogc:Size>\n "
        + "                        <ogc:Width>550</ogc:Width>\n "
        + "                        <ogc:Height>250</ogc:Height>\n "
        + "                </ogc:Size>\n " + "        </ogc:Output>\n " + "</ogc:GetMap>\n ";

    /**
     * This is a READ ONLY TEST so we can use one time setup
     */
    public static Test suite() {
        return new OneTimeTestSetup(new GetMapTest());
    }
    
    
    @Override
    protected void populateDataDirectory(MockData dataDirectory) throws Exception {
        super.populateDataDirectory(dataDirectory);
        dataDirectory.addStyle("Population", GetMapTest.class.getResource("Population.sld"));
        dataDirectory.addPropertiesType( 
                new QName( MockData.SF_URI, "states", MockData.SF_PREFIX ), getClass().getResource("states.properties"), null
            );
    }
    
    
//    protected String getDefaultLogConfiguration() {
//        return "/DEFAULT_LOGGING.properties";
//    }
    

    public void testImage() throws Exception {
        MockHttpServletResponse response = getAsServletResponse("wms?bbox=" + bbox
                + "&styles=&layers=" + layers + "&Format=image/png"
                + "&request=GetMap" + "&width=550" + "&height=250" + "&srs=EPSG:4326");
        checkImage(response);
    }
    
    public void testSldBody() throws Exception {
        MockHttpServletResponse response = getAsServletResponse("wms?bbox=" + bbox
                + "&styles=" + "&layers=" + layers + "&Format=image/png"
                + "&request=GetMap" + "&width=550" + "&height=250" + "&srs=EPSG:4326"
                + "&SLD_BODY=" + STATES_SLD.replaceAll("=", "%3D"));
        checkImage(response);
    }

    public void testSldBodyPost() throws Exception {
        MockHttpServletResponse response = postAsServletResponse("wms?bbox=" + bbox
                + "&format=image/png&request=GetMap&width=550&height=250"
                + "&srs=EPSG:4326", STATES_SLD);
        
        checkImage(response);
    }
    
    public void testXmlPost() throws Exception {
        MockHttpServletResponse response = postAsServletResponse("wms?", STATES_GETMAP);
        checkImage(response);
    }
    
    private void checkImage(MockHttpServletResponse response) {
        assertEquals("image/png", response.getContentType());
        try {
            BufferedImage image = ImageIO.read(getBinaryInputStream(response));
            assertNotNull(image);
            assertEquals(image.getWidth(), 550);
            assertEquals(image.getHeight(), 250);
        } catch (Throwable t) {
            t.printStackTrace();
            fail("Could not read image returned from GetMap:" + t.getLocalizedMessage());
        }
    }
    
    public void testRemoteOWSGet() throws Exception {
        if(!RemoteOWSTestSupport.isRemoteStatesAvailable(LOGGER))
            return;
        
        ServletResponse response = getAsServletResponse(
            "wms?request=getmap&service=wms&version=1.1.1" + 
            "&format=image/png" + 
            "&layers=" + RemoteOWSTestSupport.TOPP_STATES + "," + MockData.BASIC_POLYGONS.getPrefix() + ":" + MockData.BASIC_POLYGONS.getLocalPart() + 
            "&styles=Population," + MockData.BASIC_POLYGONS.getLocalPart() +
            "&remote_ows_type=WFS" +
            "&remote_ows_url=" + RemoteOWSTestSupport.WFS_SERVER_URL +
            "&height=1024&width=1024&bbox=-180,-90,180,90&srs=EPSG:4326" 
        );
        
        assertEquals("image/png", response.getContentType());
    }
    
    public void testRemoteOWSUserStyleGet() throws Exception {
        if (!RemoteOWSTestSupport.isRemoteStatesAvailable(LOGGER)) {
            return;
        }

        URL url = GetMapTest.class.getResource("remoteOws.sld");

        ServletResponse response = getAsServletResponse("wms?request=getmap&service=wms&version=1.1.1"
                + "&format=image/png"
                + "&sld="
                + url.toString()
                + "&height=1024&width=1024&bbox=-180,-90,180,90&srs=EPSG:4326");

        assertEquals("image/png", response.getContentType());
    }
    
}
