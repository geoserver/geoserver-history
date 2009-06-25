package org.vfny.geoserver.wms.responses;

import static org.custommonkey.xmlunit.XMLAssert.assertXpathEvaluatesTo;
import junit.framework.Test;

import org.geoserver.data.test.MockData;
import org.geoserver.wms.WMSTestSupport;
import org.geoserver.catalog.AttributionInfo;
import org.geoserver.catalog.LayerInfo;
import org.w3c.dom.Document;

public class GetCapabilitiesTest extends WMSTestSupport {
    /**
     * This is a READ ONLY TEST so we can use one time setup
     */
 //    public static Test suite() {
//         return new OneTimeTestSetup(new GetCapabilitiesTest());
  //   }
    
    @Override
    protected void populateDataDirectory(MockData dataDirectory) throws Exception {
        super.populateDataDirectory(dataDirectory);
        dataDirectory.disableDataStore(MockData.CITE_PREFIX);
    }
    

    public void testGet() throws Exception {
        Document doc = getAsDOM("wms?service=WMS&request=getCapabilities", true);
        // print(doc);
        assertEquals("WMT_MS_Capabilities", doc.getDocumentElement().getNodeName());
        
        // see that disabled elements are disabled for good
        assertXpathEvaluatesTo("0", "count(//Name[text()='cite:Buildings'])", doc);
    }

    public void testAttribution() throws Exception {
        Document doc = getAsDOM("wms?service=WMS&request=getCapabilities", true);
        assertXpathEvaluatesTo("0", "count(//Attribution)", doc);

        // Add attribution to one of the layers
        LayerInfo points = getCatalog().getLayerByName(MockData.POINTS.getLocalPart());
        AttributionInfo attr = points.getAttribution();

        attr.setTitle("Point Provider");
        getCatalog().save(points);

        doc = getAsDOM("wms?service=WMS&request=getCapabilities", true);
        assertXpathEvaluatesTo("1", "count(//Attribution)", doc);
        assertXpathEvaluatesTo("1", "count(//Attribution/Title)", doc);

        // Add href to same layer
        attr = points.getAttribution();
        attr.setHref("http://example.com/points/provider");
        getCatalog().save(points);

        doc = getAsDOM("wms?service=WMS&request=getCapabilities", true);
        print(doc);
        assertXpathEvaluatesTo("1", "count(//Attribution)", doc);
        assertXpathEvaluatesTo("1", "count(//Attribution/Title)", doc);
        assertXpathEvaluatesTo("1", "count(//Attribution/OnlineResource)", doc);

        // Add logo to same layer
        attr = points.getAttribution();
        attr.setLogoURL("http://example.com/points/logo");
        attr.setLogoType("image/logo");
        attr.setLogoHeight(50);
        attr.setLogoWidth(50);
        getCatalog().save(points);

        doc = getAsDOM("wms?service=WMS&request=getCapabilities", true);
        print(doc);
        assertXpathEvaluatesTo("1", "count(//Attribution)", doc);
        assertXpathEvaluatesTo("1", "count(//Attribution/Title)", doc);
        assertXpathEvaluatesTo("1", "count(//Attribution/LogoURL)", doc);
    }
}
