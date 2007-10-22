package org.vfny.geoserver.wms.responses.map.png;

import javax.servlet.ServletResponse;

import org.geoserver.data.test.MockData;
import org.geoserver.wms.WMSTestSupport;

public class GetMapTest extends WMSTestSupport {

    public void testRemoteOWS() throws Exception {
        if(!isRemoteStatesAvailable())
            return;
        
        ServletResponse response = getAsServletResponse(
            "wms?request=getmap&service=wms&version=1.1.1" + 
            "&format=image/png" + 
            "&layers=" + TOPP_STATES + "," + MockData.BASIC_POLYGONS.getPrefix() + ":" + MockData.BASIC_POLYGONS.getLocalPart() + 
            "&styles=Population," + MockData.BASIC_POLYGONS.getLocalPart() +
            "&remote_ows_type=WFS" +
            "&remote_ows_url=" + SIGMA_WFS_URL +
            "&height=1024&width=1024&bbox=-180,-90,180,90&srs=EPSG:4326" 
        );
        
        assertEquals("image/png", response.getContentType());
    }

}
