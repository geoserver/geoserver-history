/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.wms.responses.map.png;

import org.geoserver.data.test.MockData;
import org.geoserver.wms.RemoteOWSTestSupport;
import org.geoserver.wms.WMSTestSupport;
import org.geoserver.wms.kvp.GetMapKvpRequestReader;
import java.net.URL;
import javax.servlet.ServletResponse;


public class GetMapTest extends WMSTestSupport {
    public void testRemoteOWSGet() throws Exception {
        if (!RemoteOWSTestSupport.isRemoteStatesAvailable()) {
            return;
        }

        ServletResponse response = getAsServletResponse(
                "wms?request=getmap&service=wms&version=1.1.1" + "&format=image/png" + "&layers="
                + RemoteOWSTestSupport.TOPP_STATES + "," + MockData.BASIC_POLYGONS.getPrefix()
                + ":" + MockData.BASIC_POLYGONS.getLocalPart() + "&styles=Population,"
                + MockData.BASIC_POLYGONS.getLocalPart() + "&remote_ows_type=WFS"
                + "&remote_ows_url=" + RemoteOWSTestSupport.WFS_SERVER_URL
                + "&height=1024&width=1024&bbox=-180,-90,180,90&srs=EPSG:4326");

        assertEquals("image/png", response.getContentType());
    }

    public void testRemoteOWSUserStyleGet() throws Exception {
        if (!RemoteOWSTestSupport.isRemoteStatesAvailable()) {
            return;
        }

        URL url = GetMapTest.class.getResource("remoteOws.sld");

        ServletResponse response = getAsServletResponse(
                "wms?request=getmap&service=wms&version=1.1.1" + "&format=image/png" + "&sld="
                + url.toString() + "&height=1024&width=1024&bbox=-180,-90,180,90&srs=EPSG:4326");

        assertEquals("image/png", response.getContentType());
    }
}
