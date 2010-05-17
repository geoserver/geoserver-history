package org.vfny.geoserver.wms.responses;

import static org.custommonkey.xmlunit.XMLAssert.*;
import static org.custommonkey.xmlunit.XMLUnit.*;

import org.custommonkey.xmlunit.XpathEngine;
import org.geoserver.catalog.AttributionInfo;
import org.geoserver.catalog.LayerInfo;
import org.geoserver.catalog.StyleInfo;
import org.geoserver.data.test.MockData;
import org.geoserver.wms.WMSTestSupport;
import org.w3c.dom.Document;

public class GetCapabilitiesTest extends WMSTestSupport {
    
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
        // Uncomment the following lines if you want to use DTD validation for these tests
        // (by passing false as the second param to getAsDOM())
        // BUG: Currently, this doesn't seem to actually validate the document, although 
        // 'validation' fails if the DTD is missing

        // GeoServerInfo global = getGeoServer().getGlobal();
        // global.setProxyBaseUrl("src/test/resources/geoserver");
        // getGeoServer().save(global);

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
        // print(doc);
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
        // print(doc);
        assertXpathEvaluatesTo("1", "count(//Attribution)", doc);
        assertXpathEvaluatesTo("1", "count(//Attribution/Title)", doc);
        assertXpathEvaluatesTo("1", "count(//Attribution/LogoURL)", doc);
    }
    
    public void testAlternateStyles() throws Exception {
        // add an alternate style to Fifteen
        StyleInfo pointStyle = getCatalog().getStyleByName("point");
        LayerInfo layer = getCatalog().getLayerByName("Fifteen");
        layer.getStyles().add(pointStyle);
        getCatalog().save(layer);
        
        Document doc = getAsDOM("wms?service=WMS&request=getCapabilities", true);
        print(doc);
        
        assertXpathEvaluatesTo("1", "count(//Layer[Name='cdf:Fifteen'])", doc);
        assertXpathEvaluatesTo("2", "count(//Layer[Name='cdf:Fifteen']/Style)", doc);
        
        XpathEngine xpath = newXpathEngine();
        String href = xpath.evaluate("//Layer[Name='cdf:Fifteen']/Style[Name='Default']/LegendURL/OnlineResource/@xlink:href", doc);
        assertTrue(href.contains("GetLegendGraphic"));
        assertTrue(href.contains("layer=Fifteen"));
        assertFalse(href.contains("style"));
        href = xpath.evaluate("//Layer[Name='cdf:Fifteen']/Style[Name='point']/LegendURL/OnlineResource/@xlink:href", doc);
        assertTrue(href.contains("GetLegendGraphic"));
        assertTrue(href.contains("layer=Fifteen"));
        assertTrue(href.contains("style=point"));
    }
}
