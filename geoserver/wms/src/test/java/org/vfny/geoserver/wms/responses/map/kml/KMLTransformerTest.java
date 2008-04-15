/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.wms.responses.map.kml;

import com.mockrunner.mock.web.MockHttpServletRequest;
import com.vividsolutions.jts.geom.Envelope;
import junit.framework.Test;

import org.geoserver.data.test.MockData;
import org.geoserver.util.ReaderUtils;
import org.geoserver.wms.WMSTestSupport;
import org.geotools.data.FeatureSource;
import org.geotools.map.DefaultMapLayer;
import org.geotools.map.MapLayer;
import org.geotools.referencing.CRS;
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
import org.w3c.dom.NodeList;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;


public class KMLTransformerTest extends WMSTestSupport {
    WMSMapContext mapContext;
    MapLayer mapLayer;
    
    /**
     * This is a READ ONLY TEST so we can use one time setup
     */
    public static Test suite() {
        return new OneTimeTestSetup(new KMLTransformerTest());
    }
    
    protected void setUpInternal() throws Exception {
        super.setUpInternal();

        mapLayer = createMapLayer( MockData.BASIC_POLYGONS );
        
        mapContext = new WMSMapContext(createGetMapRequest(MockData.BASIC_POLYGONS));
        mapContext.addLayer(mapLayer);
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
        assertEquals("kml", element.getNodeName());
        assertEquals(nfeatures, element.getElementsByTagName("Style").getLength());
        assertEquals(nfeatures, element.getElementsByTagName("Placemark").getLength());
    }
    
    //JD: commented out because there is no way to specify a coordinate reference
    // system in property datastores.
//    public void testReprojection() throws Exception {
//        KMLTransformer transformer = new KMLTransformer();
//        transformer.setIndentation(2);
//           
//        ByteArrayOutputStream output = new ByteArrayOutputStream();
//        transformer.transform(mapContext, output);
//        transformer.transform(mapContext,System.out);
//        DocumentBuilder docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
//        Document doc1 = docBuilder.parse(new ByteArrayInputStream(output.toByteArray()));
//
//        mapContext.setCoordinateReferenceSystem(CRS.decode("EPSG:3005"));
//        output = new ByteArrayOutputStream();
//        transformer.transform(mapContext, output);
//        transformer.transform(mapContext,System.out);
//        Document doc2 = docBuilder.parse(new ByteArrayInputStream(output.toByteArray()));
//        
//        NodeList docs1 = doc1.getDocumentElement().getElementsByTagName("Document");
//        NodeList docs2 = doc2.getDocumentElement().getElementsByTagName("Document");
//        
//        assertEquals( docs1.getLength(), docs2.getLength() );
//        for ( int i = 0; i < docs1.getLength(); i++ ) {
//            Element e1 = (Element) docs1.item(i);
//            Element e2 = (Element) docs2.item(i);
//            
//            String name1 = ReaderUtils.getChildText( e1, "name" );
//            String name2 = ReaderUtils.getChildText( e2, "name" );
//            
//            assertEquals( name1, name2 );
//            
//            Element p1 = (Element) e1.getElementsByTagName("Placemark").item(0);
//            Element p2 = (Element) e2.getElementsByTagName("Placemark").item(0);
//            
//            Element poly1 = (Element) p1.getElementsByTagName("Polygon").item(0);
//            Element poly2 = (Element) p2.getElementsByTagName("Polygon").item(0);
//            
//            Element c1 = (Element) poly1.getElementsByTagName("coordinates").item(0);
//            Element c2 = (Element) poly2.getElementsByTagName("coordinates").item(0);
//            
//            assertFalse(c1.getFirstChild().getNodeValue().equals( c2.getFirstChild().getNodeValue()));
//        }
//        
//    }

    public void testRasterTransformerInline() throws Exception {
        KMLRasterTransformer transformer = new KMLRasterTransformer(mapContext);
        transformer.setInline(true);

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        transformer.transform(mapLayer, output);

        DocumentBuilder docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document document = docBuilder.parse(new ByteArrayInputStream(output.toByteArray()));

        assertEquals("kml", document.getDocumentElement().getNodeName());

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

        assertEquals("kml", document.getDocumentElement().getNodeName());

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

        assertEquals("kml", document.getDocumentElement().getNodeName());
        assertEquals( 3, document.getElementsByTagName("Region").getLength() );
        assertEquals( 2, document.getElementsByTagName("NetworkLink").getLength() );
        assertEquals( 2, document.getElementsByTagName("GroundOverlay").getLength() );
        
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
