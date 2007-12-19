/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.wms.responses.featureinfo;

import org.geoserver.data.test.MockData;
import org.geoserver.wms.WMSTestSupport;


public class GetFeatureInfoTest extends WMSTestSupport {
    /**
     * Tests a simple GetFeatureInfo works, and that the result contains the expected polygon
     * @throws Exception
     */
    public void testSimple() throws Exception {
        String layer = MockData.FORESTS.getPrefix() + ":" + MockData.FORESTS.getLocalPart();
        String request = "wms?bbox=-0.002,-0.002,0.002,0.002&styles=&format=jpeg&info_format=text/plain&request=GetFeatureInfo&layers="
            + layer + "&query_layers=" + layer + "&width=20&height=20&x=10&y=10";
        String result = getAsString(request);
        assertNotNull(result);
        assertTrue(result.indexOf("Green Forest") > 0);
    }
}
