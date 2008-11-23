package org.vfny.geoserver.wms.responses;

import static org.custommonkey.xmlunit.XMLAssert.*;
import junit.framework.Test;

import org.geoserver.data.test.MockData;
import org.geoserver.wms.WMSTestSupport;
import org.w3c.dom.Document;

public class GetCapabilitiesTest extends WMSTestSupport {
    /**
     * This is a READ ONLY TEST so we can use one time setup
     */
    public static Test suite() {
        return new OneTimeTestSetup(new GetCapabilitiesTest());
    }
    
    @Override
    protected void populateDataDirectory(MockData dataDirectory) throws Exception {
        super.populateDataDirectory(dataDirectory);
        dataDirectory.disableDataStore(MockData.CITE_PREFIX);
    }
    

    public void testGet() throws Exception {
        Document doc = getAsDOM("wms?service=WMS&request=getCapabilities", true);
//        print(doc);
        assertEquals("WMT_MS_Capabilities", doc.getDocumentElement().getNodeName());
        
        // see that disabled elements are disabled for good
        assertXpathEvaluatesTo("0", "count(//Name[text()='cite:Buildings'])", doc);
    }
}
