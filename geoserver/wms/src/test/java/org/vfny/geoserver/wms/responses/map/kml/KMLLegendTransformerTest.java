/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.wms.responses.map.kml;

import static org.custommonkey.xmlunit.XMLAssert.*;
import junit.framework.TestCase;

import org.geoserver.wms.WMSMockData;
import org.geoserver.wms.WMSTestSupport;
import org.geotools.data.FeatureSource;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureCollections;
import org.geotools.map.DefaultMapLayer;
import org.geotools.map.MapLayer;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.vfny.geoserver.global.MapLayerInfo;
import org.vfny.geoserver.wms.WMSMapContext;
import org.vfny.geoserver.wms.requests.GetMapRequest;
import org.w3c.dom.Document;

import com.mockrunner.mock.web.MockHttpServletRequest;
import com.vividsolutions.jts.geom.Point;

/**
 * Unit test suite for {@link KMLLegendTransformer}
 * 
 * @author Gabriel Roldan (OpenGeo)
 * @version $Id$
 */
public class KMLLegendTransformerTest extends TestCase {

    private WMSMockData mockData;

    /**
     * The map context for the transformer constructor. It shouldn't be needed, see the comment at
     * {@link KMLLegendTransformer#KMLLegendTransformer(WMSMapContext)}
     */
    private WMSMapContext mapContext;

    /**
     * The layer to encode the legend url for
     */
    private MapLayer mapLayer;

    /**
     * @see junit.framework.TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
        mockData = new WMSMockData();
        mockData.setUp();

        // Map<String, String> namespaces = new HashMap<String, String>();
        // namespaces.put("atom", "http://purl.org/atom/ns#");
        // XMLUnit.setXpathNamespaceContext(new SimpleNamespaceContext(namespaces));

        MapLayerInfo layer = mockData.addFeatureTypeLayer("TestPoints", Point.class);
        mapContext = new WMSMapContext();
        GetMapRequest request = mockData.createRequest();
        request.setLayers(new MapLayerInfo[] { layer });

        FeatureSource<SimpleFeatureType, SimpleFeature> featureSource = layer.getFeature()
                .getFeatureSource();
        mapLayer = new DefaultMapLayer(featureSource, mockData.getDefaultStyle().getStyle());

        MockHttpServletRequest httpreq = (MockHttpServletRequest) request.getHttpServletRequest();
        httpreq.setScheme("http");
        httpreq.setServerName("geoserver.org");
        httpreq.setServerPort(8181);
        httpreq.setContextPath("/geoserver");
        mapContext.setRequest(request);
    }

    /**
     * @see junit.framework.TestCase#tearDown()
     */
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Test method for
     * {@link KMLLegendTransformer#KMLLegendTransformer(org.vfny.geoserver.wms.WMSMapContext)}.
     * 
     * @throws Exception
     */
    public void testKMLLegendTransformer() throws Exception {
        FeatureCollection<SimpleFeatureType, SimpleFeature> features = FeatureCollections
                .newCollection();

        KMLLegendTransformer transformer = new KMLLegendTransformer(mapContext);
        transformer.setIndentation(2);
        Document dom = WMSTestSupport.transform(mapLayer, transformer);
        assertXpathEvaluatesTo("Legend", "//kml/ScreenOverlay/name", dom);
        assertXpathEvaluatesTo("0", "//kml/ScreenOverlay/overlayXY/@x", dom);
        assertXpathEvaluatesTo("0", "//kml/ScreenOverlay/overlayXY/@y", dom);
        assertXpathEvaluatesTo("pixels", "//kml/ScreenOverlay/overlayXY/@xunits", dom);
        assertXpathEvaluatesTo("pixels", "//kml/ScreenOverlay/overlayXY/@yunits", dom);

        String expectedLegendUrl = "http://geoserver.org:8181/geoserver/wms?service=wms&width=20&height=20&style=Default+Style&request=GetLegendGraphic&layer=&format=image%2Fpng&version=1.1.1";
        assertXpathEvaluatesTo(expectedLegendUrl, "//kml/ScreenOverlay/Icon/href", dom);
    }

}
