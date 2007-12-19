/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.requests.wms;

import com.meterware.httpunit.GetMethodWebRequest;
import com.meterware.httpunit.PostMethodWebRequest;
import com.meterware.httpunit.WebConversation;
import com.meterware.httpunit.WebRequest;
import com.meterware.httpunit.WebResponse;
import org.vfny.geoserver.AbstractGeoserverHttpTest;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import javax.imageio.ImageIO;


public class GetMapTestHttp extends AbstractGeoserverHttpTest {
    public static final String STATES_SLD = "<StyledLayerDescriptor version=\"1.0.0\">"
        + "<UserLayer>" + "<Name>topp:states</Name>" + "<UserStyle>" + "<Name>UserSelection</Name>"
        + "<FeatureTypeStyle>" + "<Rule>" + "<Filter xmlns:gml=\"http://www.opengis.net/gml\">"
        + "<PropertyIsEqualTo>" + "<PropertyName>STATE_NAME</PropertyName>"
        + "<Literal>Illinois</Literal>" + "</PropertyIsEqualTo>" + "</Filter>"
        + "<PolygonSymbolizer>" + "<Fill>" + "<CssParameter name=\"fill\">#FF0000</CssParameter>"
        + "</Fill>" + "</PolygonSymbolizer>" + "</Rule>" + "<Rule>" + "<LineSymbolizer>"
        + "<Stroke/>" + "</LineSymbolizer>" + "</Rule>" + "</FeatureTypeStyle>" + "</UserStyle>"
        + "</UserLayer>" + "</StyledLayerDescriptor>";
    public static final String STATES_GETMAP =  //
        "<?xml version=\"1.0\" encoding=\"UTF-8\"?> \n "
        + "<ogc:GetMap service=\"WMS\"  version=\"1.1.1\" \n "
        + "        xmlns:gml=\"http://www.opengis.net/gml\"\n "
        + "        xmlns:ogc=\"http://www.opengis.net/ows\"\n "
        + "        xmlns:sld=\"http://www.opengis.net/sld\"\n "
        + "        xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n "
        + "        xsi:schemaLocation=\"http://www.opengis.net/ows GetMap.xsd http://www.opengis.net/gml geometry.xsd http://www.opengis.net/sld StyledLayerDescriptor.xsd \">\n "
        + "        <sld:StyledLayerDescriptor>\n " + "                <sld:NamedLayer>\n "
        + "                        <sld:Name>topp:states</sld:Name>\n "
        + "                        <sld:NamedStyle>\n "
        + "                                <sld:Name>population</sld:Name>\n "
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
    String bbox = "-130,24,-66,50";
    String styles = "population";
    String layers = "states";

    public void testImage() throws Exception {
        if (isOffline()) {
            return;
        }

        WebConversation conversation = new WebConversation();
        WebRequest request = new GetMethodWebRequest(getBaseUrl() + "/wms?bbox=" + bbox
                + "&styles=" + styles + "&layers=" + layers + "&Format=image/png"
                + "&request=GetMap" + "&width=550" + "&height=250" + "&srs=EPSG:4326");

        WebResponse response = conversation.getResponse(request);
        assertEquals("image/png", response.getContentType());

        try {
            BufferedImage image = ImageIO.read(response.getInputStream());
            assertNotNull(image);
            assertEquals(image.getWidth(), 550);
            assertEquals(image.getHeight(), 250);
        } catch (Throwable t) {
            t.printStackTrace();
            fail("Could not read image returned from GetMap:" + t.getLocalizedMessage());
        }
    }

    public void testSldBody() throws Exception {
        if (isOffline()) {
            return;
        }

        WebConversation conversation = new WebConversation();
        WebRequest request = new GetMethodWebRequest(getBaseUrl() + "/wms?bbox=" + bbox
                + "&styles=" + styles + "&layers=" + layers + "&Format=image/png"
                + "&request=GetMap" + "&width=550" + "&height=250" + "&srs=EPSG:4326"
                + "&SLD_BODY=%3CStyledLayerDescriptor%20version=%221.0.0%22%3E%3CUserLayer%3E%3CName%3Etopp:states%3C/Name%3E%3CUserStyle%3E%3CName%3EUserSelection%3C/Name%3E%3CFeatureTypeStyle%3E%3CRule%3E%3CFilter%20xmlns:gml=%22http://www.opengis.net/gml%22%3E%3CPropertyIsEqualTo%3E%3CPropertyName%3ESTATE_NAME%3C/PropertyName%3E%3CLiteral%3EIllinois%3C/Literal%3E%3C/PropertyIsEqualTo%3E%3C/Filter%3E%3CPolygonSymbolizer%3E%3CFill%3E%3CCssParameter%20name=%22fill%22%3E%23FF0000%3C/CssParameter%3E%3C/Fill%3E%3C/PolygonSymbolizer%3E%3C/Rule%3E%3CRule%3E%3CLineSymbolizer%3E%3CStroke/%3E%3C/LineSymbolizer%3E%3C/Rule%3E%3C/FeatureTypeStyle%3E%3C/UserStyle%3E%3C/UserLayer%3E%3C/StyledLayerDescriptor%3E");

        WebResponse response = conversation.getResponse(request);
        assertEquals("image/png", response.getContentType());

        try {
            BufferedImage image = ImageIO.read(response.getInputStream());
            assertNotNull(image);
            assertEquals(image.getWidth(), 550);
            assertEquals(image.getHeight(), 250);
        } catch (Throwable t) {
            t.printStackTrace();
            fail("Could not read image returned from GetMap:" + t.getLocalizedMessage());
        }
    }

    public void testSldBodyPost() throws Exception {
        if (isOffline()) {
            return;
        }

        InputStream in = new ByteArrayInputStream(STATES_SLD.getBytes());

        WebConversation conversation = new WebConversation();
        WebRequest request = new PostMethodWebRequest(getBaseUrl() + "/wms?bbox=" + bbox
                + "&Format=image/png" + "&request=GetMap" + "&width=550" + "&height=250"
                + "&srs=EPSG:4326", in, "text/xml");

        WebResponse response = conversation.getResponse(request);
        assertEquals("image/png", response.getContentType());

        try {
            BufferedImage image = ImageIO.read(response.getInputStream());
            assertNotNull(image);
            assertEquals(image.getWidth(), 550);
            assertEquals(image.getHeight(), 250);
        } catch (Throwable t) {
            t.printStackTrace();
            fail("Could not read image returned from GetMap:" + t.getLocalizedMessage());
        }
    }

    public void testXmlPost() throws Exception {
        if (isOffline()) {
            return;
        }

        InputStream in = new ByteArrayInputStream(STATES_GETMAP.getBytes());

        WebConversation conversation = new WebConversation();
        WebRequest request = new PostMethodWebRequest(getBaseUrl() + "/wms", in, "text/xml");

        WebResponse response = conversation.getResponse(request);
        assertEquals("image/png", response.getContentType());

        try {
            BufferedImage image = ImageIO.read(response.getInputStream());
            assertNotNull(image);
            assertEquals(image.getWidth(), 550);
            assertEquals(image.getHeight(), 250);
        } catch (Throwable t) {
            t.printStackTrace();
            fail("Could not read image returned from GetMap:" + t.getLocalizedMessage());
        }
    }
}
