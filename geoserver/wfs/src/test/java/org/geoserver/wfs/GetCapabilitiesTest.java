/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.wfs;

import org.geoserver.platform.GeoServerExtensions;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;


public class GetCapabilitiesTest extends WFSTestSupport {
    public void testGet() throws Exception {
        Document doc = getAsDOM("wfs?service=WFS&request=getCapabilities");
        assertEquals("wfs:WFS_Capabilities",
            doc.getDocumentElement().getNodeName());
    }

    public void testPost() throws Exception {
        String xml = "<GetCapabilities service=\"WFS\" version=\"1.0.0\""
            + " xmlns=\"http://www.opengis.net/wfs\" "
            + " xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" "
            + " xsi:schemaLocation=\"http://www.opengis.net/wfs "
            + " http://schemas.opengis.net/wfs/1.0.0/WFS-basic.xsd\"/>";
        Document doc = postAsDOM("wfs", xml);

        assertEquals("WFS_Capabilities", doc.getDocumentElement().getNodeName());
    }

    public void testOutputFormats() throws Exception {
        Document doc = getAsDOM(
                "wfs?service=WFS&request=getCapabilities&version=1.0.0");

        Element outputFormats = getFirstElementByTagName(doc, "ResultFormat");
        NodeList formats = outputFormats.getChildNodes();

        TreeSet s1 = new TreeSet();

        for (int i = 0; i < formats.getLength(); i++) {
            String format = formats.item(i).getNodeName();
            s1.add(format);
        }

        List extensions = GeoServerExtensions.extensions(WFSGetFeatureOutputFormat.class);

        TreeSet s2 = new TreeSet();

        for (Iterator e = extensions.iterator(); e.hasNext();) {
            WFSGetFeatureOutputFormat extension = (WFSGetFeatureOutputFormat) e
                .next();
            s2.add(extension.getCapabilitiesElementName());
        }

        assertEquals(s1, s2);
    }
}
