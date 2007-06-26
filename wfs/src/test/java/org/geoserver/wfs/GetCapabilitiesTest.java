package org.geoserver.wfs;

import org.w3c.dom.Document;

public class GetCapabilitiesTest extends WFSTestSupport {

    public void testGet() throws Exception {
        Document doc = getAsDOM("wfs?service=WFS&request=getCapabilities");
        assertEquals("wfs:WFS_Capabilities", doc.getDocumentElement()
                .getNodeName());
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
}
