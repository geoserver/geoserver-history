/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.wcs.test;

import static org.custommonkey.xmlunit.XMLAssert.*;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.xml.XMLConstants;
import javax.xml.namespace.QName;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.custommonkey.xmlunit.SimpleNamespaceContext;
import org.custommonkey.xmlunit.XMLUnit;
import org.custommonkey.xmlunit.XpathEngine;
import org.geoserver.data.test.MockData;
import org.geoserver.test.ows.KvpRequestReaderTestSupport;
import org.vfny.geoserver.global.WCS;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

/**
 * Base support class for wcs tests.
 * 
 * @author Andrea Aime, TOPP
 * 
 */
public class WCSTestSupport extends KvpRequestReaderTestSupport {
    protected static final String BASEPATH = "wcs";

    public static String WCS_PREFIX = "wcs";

    public static String WCS_URI = "http://www.opengis.net/wcs/1.1.1";

    public static String TIFF = "tiff";

    public static QName TASMANIA_DEM = new QName(WCS_URI, "DEM", WCS_PREFIX);

    public static QName TASMANIA_BM = new QName(WCS_URI, "BlueMarble", WCS_PREFIX);
    
    public static QName ROTATED_CAD = new QName(WCS_URI, "RotatedCad", WCS_PREFIX);
    
    public static QName WORLD = new QName(WCS_URI, "World", WCS_PREFIX);
    
    protected static XpathEngine xpath;
    
    protected static final boolean IS_WINDOWS;
    
    protected static final Schema WCS11_SCHEMA;
    
    static {
        try {
            final SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            WCS11_SCHEMA = factory.newSchema(new File("./schemas/wcs/1.1.1/wcsAll.xsd"));
        } catch(Exception e) {
            throw new RuntimeException("Could not parse the WCS 1.1.1 schemas", e);
        }
        boolean windows = false;
        try {
            windows = System.getProperty("os.name").matches(".*Windows.*");
        } catch(Exception e) {
            // no os.name? oh well, never mind
        }
        IS_WINDOWS = windows;
    }

    /**
     * @return The global wfs instance from the application context.
     */
    protected WCS getWCS() {
        return (WCS) applicationContext.getBean("wcs");
    }

    @Override
    protected void oneTimeSetUp() throws Exception {
        super.oneTimeSetUp();
        
        // init xmlunit
        Map<String, String> namespaces = new HashMap<String, String>();
        namespaces.put("wcs", "http://www.opengis.net/wcs/1.1.1");
        namespaces.put("ows", "http://www.opengis.net/ows/1.1");
        namespaces.put("xlink", "http://www.w3.org/1999/xlink");
        XMLUnit.setXpathNamespaceContext(new SimpleNamespaceContext(namespaces));
        xpath = XMLUnit.newXpathEngine();
    }
    
    @Override
    protected boolean isMemoryCleanRequired() {
        return IS_WINDOWS;
    }

    @Override
    protected void populateDataDirectory(MockData dataDirectory) throws Exception {
        dataDirectory.addCoverage(TASMANIA_DEM, WCSTestSupport.class.getResource("tazdem.tiff"),
                TIFF, null);
        dataDirectory.addCoverage(TASMANIA_BM, WCSTestSupport.class.getResource("tazbm.tiff"),
                TIFF, null);
        dataDirectory.addCoverage(ROTATED_CAD, WCSTestSupport.class.getResource("rotated.tiff"),
                TIFF, null);
        dataDirectory.addCoverage(WORLD, WCSTestSupport.class.getResource("world.tiff"),
                TIFF, null);
    }
    
    protected void checkOws11Exception(Document dom) throws Exception {
        assertEquals("ows:ExceptionReport", dom.getFirstChild().getNodeName());
        assertXpathEvaluatesTo("1.1.0", "/ows:ExceptionReport/@version", dom);
        Node root  = xpath.getMatchingNodes("/ows:ExceptionReport", dom).item(0);
        Node attr = root.getAttributes().getNamedItem("xmlns:ows");
        assertEquals("http://www.opengis.net/ows/1.1", attr.getTextContent());
    }
}
