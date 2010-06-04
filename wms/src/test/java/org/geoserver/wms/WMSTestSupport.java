/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.wms;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.custommonkey.xmlunit.SimpleNamespaceContext;
import org.custommonkey.xmlunit.XMLUnit;
import org.geoserver.catalog.Catalog;
import org.geoserver.catalog.LayerInfo;
import org.geoserver.data.test.MockData;
import org.geoserver.test.GeoServerTestSupport;
import org.geotools.data.FeatureSource;
import org.geotools.map.FeatureSourceMapLayer;
import org.geotools.map.MapLayer;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.styling.Style;
import org.geotools.xml.transform.TransformerBase;
import org.opengis.feature.Feature;
import org.opengis.feature.type.FeatureType;
import org.vfny.geoserver.Request;
import org.vfny.geoserver.wms.requests.GetMapRequest;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import com.vividsolutions.jts.geom.Envelope;


/**
 * Base support class for wms tests.
 * <p>
 * Deriving from this test class provides the test case with preconfigured
 * geoserver and wms objects.
 * </p>
 * @author Justin Deoliveira, The Open Planning Project, jdeolive@openplans.org
 *
 */
public abstract class WMSTestSupport extends GeoServerTestSupport {
   
    /**
     * @return The global wms singleton from the application context.
     */
    protected WMS getWMS() {
        return new WMS(getGeoServer());
    }
    
    @Override
    protected void oneTimeSetUp() throws Exception {
        super.oneTimeSetUp();
        
        Map<String, String> namespaces = new HashMap<String, String>();
        namespaces.put("xlink", "http://www.w3.org/1999/xlink");
        namespaces.put("wfs", "http://www.opengis.net/wfs");
        namespaces.put("wcs", "http://www.opengis.net/wcs/1.1.1");
        namespaces.put("gml", "http://www.opengis.net/gml");
        getTestData().registerNamespaces(namespaces);
        XMLUnit.setXpathNamespaceContext(new SimpleNamespaceContext(namespaces));
    }
    
    @Override
    protected void populateDataDirectory(MockData dataDirectory) throws Exception {
        super.populateDataDirectory(dataDirectory);
        dataDirectory.addStyle("default", MockData.class.getResource("Default.sld"));
    }

    /**
     * Convenience method for subclasses to create a map layer from a layer name.
     * <p>
     * The map layer is created with the default style for the layer.
     * </p>
     * @param layerName The name of the layer.
     *
     * @return A new map layer.
     */
    protected MapLayer createMapLayer(QName layerName)
        throws IOException {
        return createMapLayer(layerName, null);
    }
    
    /**
     * Convenience method for subclasses to create a map layer from a layer name and a style name.
     * <p>
     * The map layer is created with the default style for the layer.
     * </p>
     * @param layerName The name of the layer.
     * @param a style in the catalog (or null if you want to use the default style)
     *
     * @return A new map layer.
     */
    protected MapLayer createMapLayer(QName layerName, String styleName)
        throws IOException {
        //TODO: support coverages
        Catalog catalog = getCatalog();
        org.geoserver.catalog.FeatureTypeInfo info = catalog.getFeatureTypeByName(layerName.getNamespaceURI(), layerName.getLocalPart());
        LayerInfo layerInfo = catalog.getLayerByName(layerName.getLocalPart());
        Style style = layerInfo.getDefaultStyle().getStyle();
        if(styleName != null){
            style = catalog.getStyleByName(styleName).getStyle();
        }
        
        FeatureSource<? extends FeatureType, ? extends Feature> featureSource;
        featureSource = info.getFeatureSource(null, null);
        
        MapLayer layer = new FeatureSourceMapLayer(featureSource, style);
        layer.setTitle( layer.getTitle() );
        
        return layer;
    }

    /**
     * Calls through to {@link #createGetMapRequest(QName[])}.
     *
     */
    protected GetMapRequest createGetMapRequest(QName layerName) {
        return createGetMapRequest(new QName[] { layerName });
    }

    /**
     * Convenience method for subclasses to create a new GetMapRequest object.
     * <p>
     * The returned object has the following properties:
     *  <ul>
     *    <li>styles set to default styles for layers specified
     *    <li>bbox set to (-180,-90,180,180 )
     *    <li>crs set to epsg:4326
     *  </ul>
     *  Caller must set additional parameters of request as need be.
     * </p>
     *
     * @param The layer names of the request.
     *
     * @return A new GetMapRequest object.
     */
    protected GetMapRequest createGetMapRequest(QName[] layerNames) {
        GetMapRequest request = new GetMapRequest(getWMS());
        request.setBaseUrl("http://localhost:8080/geoserver");
        request.setHttpServletRequest(createRequest("wms"));

        MapLayerInfo[] layers = new MapLayerInfo[layerNames.length];
        List styles = new ArrayList();

        for (int i = 0; i < layerNames.length; i++) {
            LayerInfo layerInfo = getCatalog().getLayerByName(layerNames[i].getLocalPart());
            try {
                styles.add(layerInfo.getDefaultStyle().getStyle());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            layers[i] = new MapLayerInfo(layerInfo);
        }

        request.setLayers(layers);
        request.setStyles(styles);
        request.setBbox(new Envelope(-180, -90, 180, 90));
        request.setCrs(DefaultGeographicCRS.WGS84);
        request.setSRS("EPSG:4326");
        request.setRawKvp(new HashMap());
        return request;
    }
    
    
    /**
     * Asserts that the image is not blank, in the sense that there must be
     * pixels different from the passed background color.
     *
     * @param testName the name of the test to throw meaningfull messages if
     *        something goes wrong
     * @param image the imgage to check it is not "blank"
     * @param bgColor the background color for which differing pixels are
     *        looked for
     */
    protected void assertNotBlank(String testName, BufferedImage image, Color bgColor) {
        int pixelsDiffer = 0;

        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                if (image.getRGB(x, y) != bgColor.getRGB()) {
                    ++pixelsDiffer;
                }
            }
        }

        LOGGER.info(testName + ": pixel count=" + (image.getWidth() * image.getHeight())
            + " non bg pixels: " + pixelsDiffer);
        assertTrue(testName + " image is comlpetely blank", 0 < pixelsDiffer);
    }

    /**
     * Utility method to run the transformation on tr with the provided request and returns the
     * result as a DOM.
     * <p>
     * Parsing the response is done in a namespace aware way.
     * </p>
     * 
     * @param req,
     *                the Object to run the xml transformation against with {@code tr}, usually an
     *                instance of a {@link Request} subclass
     * @param tr,
     *                the transformer to run the transformation with and produce the result as a DOM
     */
    public static Document transform(Object req, TransformerBase tr) throws Exception {
        return transform(req, tr, true);
    }
    
    /**
     * Utility method to run the transformation on tr with the provided request and returns the
     * result as a DOM
     * 
     * @param req,
     *                the Object to run the xml transformation against with {@code tr}, usually an
     *                instance of a {@link Request} subclass
     * @param tr,
     *                the transformer to run the transformation with and produce the result as a DOM
     * @param namespaceAware
     *                whether to use a namespace aware parser for the response or not
     */
    public static Document transform(Object req, TransformerBase tr, boolean namespaceAware) throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        tr.transform(req, out);
    
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(namespaceAware);
    
        DocumentBuilder db = dbf.newDocumentBuilder();
    
        /**
         * Resolves everything to an empty xml document, useful for skipping errors due to missing
         * dtds and the like
         * 
         * @author Andrea Aime - TOPP
         */
        class EmptyResolver implements org.xml.sax.EntityResolver {
            public InputSource resolveEntity(String publicId, String systemId)
                    throws org.xml.sax.SAXException, IOException {
                StringReader reader = new StringReader("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
                InputSource source = new InputSource(reader);
                source.setPublicId(publicId);
                source.setSystemId(systemId);
    
                return source;
            }
        }
        db.setEntityResolver(new EmptyResolver());
    
        //System.out.println(out.toString());
    
        Document doc = db.parse(new ByteArrayInputStream(out.toByteArray()));
        return doc;
    }
    
}
