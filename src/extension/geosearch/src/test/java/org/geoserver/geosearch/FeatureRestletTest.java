package org.geoserver.geosearch;

import javax.xml.namespace.QName;

import org.geoserver.data.test.MockData;
import org.geoserver.test.GeoServerTestSupport;

import com.mockrunner.mock.web.MockHttpServletResponse;

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
