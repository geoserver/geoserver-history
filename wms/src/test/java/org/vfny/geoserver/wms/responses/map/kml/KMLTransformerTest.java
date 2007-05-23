/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.wms.responses.map.kml;

import com.mockrunner.mock.web.MockHttpServletRequest;
import com.vividsolutions.jts.geom.Envelope;
import org.geoserver.data.test.MockData;
import org.geoserver.wms.WMSTestSupport;
import org.geotools.data.FeatureSource;
import org.geotools.map.DefaultMapLayer;
import org.geotools.map.MapLayer;
import org.geotools.styling.Style;
import org.vfny.geoserver.config.FeatureTypeConfig;
import org.vfny.geoserver.global.Data;
import org.vfny.geoserver.global.FeatureTypeInfo;
import org.vfny.geoserver.global.MapLayerInfo;
import org.vfny.geoserver.global.dto.FeatureTypeInfoDTO;
import org.vfny.geoserver.wms.WMSMapContext;
import org.vfny.geoserver.wms.requests.GetMapRequest;
import org.vfny.geoserver.wms.servlets.GetMap;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;


public class KMLTransformerTest extends WMSTestSupport {
    WMSMapContext mapContext;
    MapLayer mapLayer;

    protected void setUp() throws Exception {
        super.setUp();

        //get some data
        Data catalog = getCatalog();
        FeatureSource featureSource = getFeatureSource(MockData.BASIC_POLYGONS);
        Style style = catalog.getStyle(MockData.BASIC_POLYGONS.getLocalPart());

        //create a map context
        GetMap getMap = new GetMap(getWMS());
        GetMapRequest request = new GetMapRequest(getMap);
        request.setBbox(new Envelope(-180, -90, 180, 90));

        FeatureTypeInfo ftInfo = catalog.getFeatureTypeInfo(MockData.BASIC_POLYGONS);

        MapLayerInfo mapLayerInfo = new MapLayerInfo((FeatureTypeInfoDTO) ftInfo.toDTO(), catalog);
        request.setLayers(new MapLayerInfo[] { mapLayerInfo });
        mapContext = new WMSMapContext(request);

        mapLayer = new DefaultMapLayer(featureSource, style);
        mapLayer.setTitle(featureSource.getSchema().getTypeName());
        mapContext.addLayer(mapLayer);

        MockHttpServletRequest httpRequest = createRequest("wms");
        request.setHttpServletRequest(httpRequest);
    }

    public void testVectorTransformer() throws Exception {
        KMLVectorTransformer transformer = new KMLVectorTransformer(mapContext, mapLayer);
        transformer.setIndentation(2);

        FeatureSource featureSource = mapLayer.getFeatureSource();
        int nfeatures = featureSource.getFeatures().size();

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        transformer.transform(featureSource.getFeatures(), output);

        DocumentBuilder docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document document = docBuilder.parse(new ByteArrayInputStream(output.toByteArray()));

        Element element = document.getDocumentElement();
        assertEquals("Document", element.getNodeName());
        assertEquals(nfeatures, element.getElementsByTagName("Document").getLength());
    }

    public void testRasterTransformerInline() throws Exception {
        KMLRasterTransformer transformer = new KMLRasterTransformer(mapContext);
        transformer.setInline(true);

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        transformer.transform(mapLayer, output);

        DocumentBuilder docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document document = docBuilder.parse(new ByteArrayInputStream(output.toByteArray()));

        assertEquals("Document", document.getDocumentElement().getNodeName());

        assertEquals(mapContext.getLayerCount(), document.getElementsByTagName("Folder").getLength());
        assertEquals(mapContext.getLayerCount(),
            document.getElementsByTagName("GroundOverlay").getLength());

        assertEquals(mapContext.getLayerCount(), document.getElementsByTagName("href").getLength());

        Element href = (Element) document.getElementsByTagName("href").item(0);
        assertEquals("layer_0.png", href.getFirstChild().getNodeValue());
    }

    public void testRasterTransformerNotInline() throws Exception {
        KMLRasterTransformer transformer = new KMLRasterTransformer(mapContext);
        transformer.setInline(false);

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        transformer.transform(mapLayer, output);

        DocumentBuilder docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document document = docBuilder.parse(new ByteArrayInputStream(output.toByteArray()));

        assertEquals("Document", document.getDocumentElement().getNodeName());

        assertEquals(mapContext.getLayerCount(), document.getElementsByTagName("Folder").getLength());
        assertEquals(mapContext.getLayerCount(),
            document.getElementsByTagName("GroundOverlay").getLength());

        assertEquals(mapContext.getLayerCount(), document.getElementsByTagName("href").getLength());

        Element href = (Element) document.getElementsByTagName("href").item(0);
        assertTrue(href.getFirstChild().getNodeValue().startsWith("http://localhost"));
    }

    public void testSuperOverlayTransformer() throws Exception {
        KMLSuperOverlayTransformer transformer = new KMLSuperOverlayTransformer(mapContext);
        transformer.setIndentation(2);

        mapContext.setAreaOfInterest(new Envelope(-180, 180, -90, 90));

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        transformer.transform(mapLayer, output);

        DocumentBuilder docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document document = docBuilder.parse(new ByteArrayInputStream(output.toByteArray()));

        assertEquals("Document", document.getDocumentElement().getNodeName());
    }

    public void testTransformer() throws Exception {
        KMLTransformer transformer = new KMLTransformer();

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        transformer.transform(mapContext, output);

        DocumentBuilder docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document document = docBuilder.parse(new ByteArrayInputStream(output.toByteArray()));

        assertEquals("kml", document.getDocumentElement().getNodeName());
    }
}
