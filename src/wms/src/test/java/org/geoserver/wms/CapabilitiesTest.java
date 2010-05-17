package org.geoserver.wms;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import junit.framework.Test;

import org.custommonkey.xmlunit.XMLUnit;
import org.custommonkey.xmlunit.XpathEngine;
import org.geoserver.catalog.LayerGroupInfo;
import org.geoserver.catalog.LayerInfo;
import org.geoserver.config.GeoServerInfo;
import org.geoserver.data.test.MockData;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;


public class CapabilitiesTest extends WMSTestSupport {
    
    public CapabilitiesTest() {
        super();
    }

    /**
     * This is a READ ONLY TEST so we can use one time setup
     */
    public static Test suite() {
        return new OneTimeTestSetup(new CapabilitiesTest());
    }

    @Override
    protected void oneTimeSetUp() throws Exception {
        super.oneTimeSetUp();
        GeoServerInfo global = getGeoServer().getGlobal();
        global.setProxyBaseUrl("src/test/resources/geoserver");
        getGeoServer().save(global);
    }

    @Override
    protected void populateDataDirectory(MockData dataDirectory)
            throws Exception {
        super.populateDataDirectory(dataDirectory);
        dataDirectory.addWellKnownCoverageTypes();
    }

    public void testCapabilities() throws Exception {
        Document dom = dom(get("wms?request=getCapabilities"), false);
        Element e = dom.getDocumentElement();
        assertEquals("WMT_MS_Capabilities", e.getLocalName());
    }
    
    public void testFilteredCapabilitiesCite() throws Exception {
        Document dom = dom(get("wms?request=getCapabilities&namespace=cite"), true);
        Element e = dom.getDocumentElement();
        assertEquals("WMT_MS_Capabilities", e.getLocalName());
        XpathEngine xpath =  XMLUnit.newXpathEngine();
        assertTrue(xpath.getMatchingNodes("//Layer/Name[starts-with(., cite)]", dom).getLength() > 0);
        assertEquals(0, xpath.getMatchingNodes("//Layer/Name[not(starts-with(., cite))]", dom).getLength());
    }
    
    public void testLayerCount() throws Exception {
        List<LayerInfo> layers = new ArrayList<LayerInfo>(getCatalog().getLayers());
        for (ListIterator<LayerInfo> it = layers.listIterator(); it.hasNext();) {
            LayerInfo next = it.next();
            if (next.getName().equals(MockData.GEOMETRYLESS.getLocalPart())) {
                it.remove();
            }
        }

        Document dom = dom(get("wms?request=getCapabilities"), true);

        XpathEngine xpath = XMLUnit.newXpathEngine();
        NodeList nodeLayers = xpath.getMatchingNodes("/WMT_MS_Capabilities/Capability/Layer/Layer",
                dom);

        assertEquals(layers.size(), nodeLayers.getLength());
    }
}
