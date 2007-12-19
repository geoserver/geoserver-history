/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.wms.responses.map.kml;

import org.geoserver.data.test.MockData;
import org.geoserver.wms.RemoteOWSTestSupport;
import org.geoserver.wms.WMSTestSupport;
import org.w3c.dom.Document;


public class KMLTest extends WMSTestSupport {
    public void testVector() throws Exception {
        Document doc = getAsDOM("wms?request=getmap&service=wms&version=1.1.1" + "&format="
                + KMLMapProducerFactory.MIME_TYPE + "&layers="
                + MockData.BASIC_POLYGONS.getPrefix() + ":"
                + MockData.BASIC_POLYGONS.getLocalPart() + "&styles="
                + MockData.BASIC_POLYGONS.getLocalPart()
                + "&height=1024&width=1024&bbox=-180,-90,180,90&srs=EPSG:4326");

        assertEquals(getFeatureSource(MockData.BASIC_POLYGONS).getFeatures().size(),
            doc.getElementsByTagName("Placemark").getLength());
    }

    public void testVectorWithFeatureId() throws Exception {
        Document doc = getAsDOM("wms?request=getmap&service=wms&version=1.1.1" + "&format="
                + KMLMapProducerFactory.MIME_TYPE + "&layers="
                + MockData.BASIC_POLYGONS.getPrefix() + ":"
                + MockData.BASIC_POLYGONS.getLocalPart() + "&styles="
                + MockData.BASIC_POLYGONS.getLocalPart()
                + "&height=1024&width=1024&bbox=-180,-90,180,90&srs=EPSG:4326"
                + "&featureid=BasicPolygons.1107531493643");

        assertEquals(1, doc.getElementsByTagName("Placemark").getLength());
    }

    public void testVectorWithRemoteLayer() throws Exception {
        if (!RemoteOWSTestSupport.isRemoteStatesAvailable()) {
            return;
        }

        Document doc = getAsDOM("wms?request=getmap&service=wms&version=1.1.1" + "&format="
                + KMLMapProducerFactory.MIME_TYPE + "&layers=topp:states" + "&styles=Default"
                + "&height=1024&width=1024&bbox=-180,-90,180,90&srs=EPSG:4326"
                + "&remote_ows_type=wfs" + "&remote_ows_url=" + RemoteOWSTestSupport.WFS_SERVER_URL
                + "&featureid=states.1");

        assertEquals(1, doc.getElementsByTagName("Placemark").getLength());
    }
}
