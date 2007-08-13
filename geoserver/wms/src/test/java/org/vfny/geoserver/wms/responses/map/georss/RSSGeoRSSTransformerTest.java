/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.wms.responses.map.georss;

import org.geoserver.data.test.MockData;
import org.geoserver.wms.WMSTestSupport;
import org.geotools.data.DefaultQuery;
import org.geotools.data.Query;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.factory.GeoTools;
import org.geotools.map.MapLayer;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory;
import org.vfny.geoserver.wms.WMSMapContext;
import org.vfny.geoserver.wms.responses.map.georss.GeoRSSTransformerBase.GeometryEncoding;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;


public class RSSGeoRSSTransformerTest extends WMSTestSupport {
    FilterFactory ff = CommonFactoryFinder.getFilterFactory(GeoTools.getDefaultHints());

    protected void setUp() throws Exception {
        super.setUp();

    }

    public void testLatLong() throws Exception {
        WMSMapContext map = new WMSMapContext(createGetMapRequest(MockData.BASIC_POLYGONS));
        map.addLayer(createMapLayer(MockData.BASIC_POLYGONS));

        Document document = getRSSResponse(map, AtomGeoRSSTransformer.GeometryEncoding.LATLONG);

        Element element = document.getDocumentElement();
        assertEquals("rss", element.getNodeName());

        NodeList items = element.getElementsByTagName("item");

        int n = getFeatureSource(MockData.BASIC_POLYGONS).getCount(Query.ALL);

        assertEquals(n, items.getLength());

        for (int i = 0; i < items.getLength(); i++) {
            Element item = (Element) items.item(i);
            assertEquals(1, item.getElementsByTagName("geo:lat").getLength());
            assertEquals(1, item.getElementsByTagName("geo:long").getLength());
        }
    }

    public void testFilter() throws Exception {
        // Set up a map context with a filtered layer
        WMSMapContext map = new WMSMapContext(createGetMapRequest(MockData.BUILDINGS));
        MapLayer layer = createMapLayer(MockData.BUILDINGS);
        Filter f = ff.equals(ff.property("ADDRESS"), ff.literal("215 Main Street"));
        layer.setQuery(new DefaultQuery(MockData.BUILDINGS.getLocalPart(), f));
        map.addLayer(layer);
        
        Document document = getRSSResponse(map, AtomGeoRSSTransformer.GeometryEncoding.LATLONG);
        NodeList items = document.getDocumentElement().getElementsByTagName("item");
        assertEquals(1, items.getLength());
    }
    
    public void testReproject() throws Exception {
        // Set up a map context with a projected layer
        WMSMapContext map = new WMSMapContext(createGetMapRequest(MockData.LINES));
        map.addLayer(createMapLayer(MockData.LINES));
        
        Document document = getRSSResponse(map, AtomGeoRSSTransformer.GeometryEncoding.LATLONG);
        NodeList items = document.getDocumentElement().getElementsByTagName("item");
        
        // check all items are there
        assertEquals(1, items.getLength());
        
        // check coordinates are in wgs84, originals aren't
        for (int i = 0; i < items.getLength(); i++) {
            Element item = (Element) items.item(i);
            double lat = Double.parseDouble(getOrdinate(item, "geo:lat"));
            double lon = Double.parseDouble(getOrdinate(item, "geo:long"));
            assertTrue(lat >= -90 && lat <= 90);
            assertTrue(lon >= -180 && lon <= 180);
        }
    }

    String getOrdinate(Element item, String ordinate) {
        return item.getElementsByTagName(ordinate).item(0).getChildNodes().item(0).getNodeValue();
    }
    
    /**
     * Returns a DOM given a map context and a geometry encoder
     */
    Document getRSSResponse(WMSMapContext map, GeometryEncoding encoding)
            throws TransformerException, ParserConfigurationException, FactoryConfigurationError,
            SAXException, IOException {
        RSSGeoRSSTransformer tx = new RSSGeoRSSTransformer();
        tx.setGeometryEncoding(encoding);
        tx.setIndentation(2);

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        tx.transform(map, output);

        DocumentBuilder docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document document = docBuilder.parse(new ByteArrayInputStream(output.toByteArray()));
        return document;
    }
}
