package org.geoserver.geosearch;

import com.mockrunner.mock.web.MockHttpServletResponse;

import javax.xml.namespace.QName;

import org.geoserver.data.test.MockData;
import org.geoserver.test.GeoServerTestSupport;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;

public class FeatureRestletTest extends GeoServerTestSupport {
    public void testSingleFeature() throws Exception{
        QName typename = MockData.BASIC_POLYGONS;
        final String path = 
            "/rest/layers/" 
            + typename.getPrefix() 
            + ":" 
            + typename.getLocalPart() 
            + "/"
            + typename.getLocalPart()
            + ".1107531493630.kml";

        getFeatureTypeInfo(typename).getMetadata().put("indexingEnabled", true);

        MockHttpServletResponse response = getAsServletResponse(path);
        assertEquals(200, response.getStatusCode());
    }
}
