package org.vfny.geoserver.wms.responses.featureinfo;

import junit.framework.Test;

import org.geoserver.data.test.MockData;
import org.geoserver.wms.WMSTestSupport;

public class GetFeatureInfoTest extends WMSTestSupport {
    
    /**
     * This is a READ ONLY TEST so we can use one time setup
     */
    public static Test suite() {
        return new OneTimeTestSetup(new GetFeatureInfoTest());
    }
    

    /**
     * Tests a simple GetFeatureInfo works, and that the result contains the expected polygon
     * @throws Exception
     */
    public void testSimple() throws Exception {
        String layer = MockData.FORESTS.getPrefix() + ":" + MockData.FORESTS.getLocalPart();
        String request = "wms?bbox=-0.002,-0.002,0.002,0.002&styles=&format=jpeg&info_format=text/plain&request=GetFeatureInfo&layers=" + layer + "&query_layers=" + layer + "&width=20&height=20&x=10&y=10";
        String result = getAsString(request); 
        assertNotNull(result);
        assertTrue(result.indexOf("Green Forest") > 0);
    }
}
