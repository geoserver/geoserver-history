/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.zserver;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.apache.lucene.document.DateField;
import org.apache.lucene.document.Document;
import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;
import java.util.logging.Logger;


/**
 * Tests the XMLDocument converter.
 *
 * @author Chris Holmes, TOPP
 * @version $Id: XMLDocumentSuite.java,v 1.8 2004/01/21 19:13:06 dmzwiers Exp $
 */
public class XMLDocumentSuite extends TestCase {
    /* Initializes the logger. Uncomment to see log messages.*/

    //static {
    //    org.vfny.geoserver.config.Log4JFormatter.init("org.vfny.geoserver", Level.FINEST);
    //}

    /** Standard logging instance */
    private static final Logger LOGGER = Logger.getLogger(
            "org.vfny.geoserver.zserver");
    private static final String UPDATE_FIELD = "//metadata/idinfo/status/update";
    private static final String EAST_FIELD = "//metadata/idinfo/spdom/bounding/eastbc";

    /** Unit test data directory */
    private static final String DATA_DIRECTORY = System.getProperty("user.dir")
        + "/misc/unit/zserver";
    private String testPath = DATA_DIRECTORY + "/test1/metadata.xml";
    private String testMapPath = DATA_DIRECTORY + "/geo.map";
    private Properties testMap;

    /**
     * Constructor that calls super.
     *
     * @param testName the name of the test.
     */
    public XMLDocumentSuite(String testName) {
        super(testName);
    }

    /**
     * Creates an instance of this suite.
     *
     * @return the new suite.
     */
    public static Test suite() {
        TestSuite suite = new TestSuite(XMLDocumentSuite.class);
        LOGGER.fine("Creating XMLDocument suite.");

        return suite;
    }

    public void setUp() {
        try {
            FileInputStream fis = new FileInputStream(testMapPath);
            testMap = new Properties();
            testMap.load(fis);
        } catch (Exception e) {
            LOGGER.severe("problem loading property file: " + e);
        }

        //String fieldName = testMap.getProperty(GeoProfile.Attribute.TITLE);
    }

    public void testBasics() {
        Properties map = new Properties();

        //String xpath =  "/document/name/first_name/";
        //map.setProperty("1", xpath);
        String barPath = DATA_DIRECTORY + "/bar.xml";
        File barFile = null;
        Document doc = null;

        try {
            barFile = new File(barPath);
            doc = XMLDocument.Document(barFile, map);
        } catch (Exception e) {
            fail("exception creating document: " + e);
        }

        //assertTrue(doc.get(xpath).equals("John"));
/*        assertTrue(doc.get("path").equals(barPath));

        String lastMod = DateField.timeToString(barFile.lastModified());
        assertTrue(doc.get("modified").equals(lastMod));
        assertTrue(doc.get("true").equals("true"));*/
    }

    public void testPaths() {
        Properties map = new Properties();

        //String xpath =  "/document/name/first_name/";
        //map.setProperty("1", xpath);
        String barPath = DATA_DIRECTORY + "/bar.xml";
        File barFile = null;
        Document doc = null;

        try {
            barFile = new File(barPath);
            doc = XMLDocument.Document(barFile, map);
            LOGGER.fine("doc is " + doc);
        } catch (Exception e) {
            fail("exception creating document: " + e);
        }

        String htmlPath = DATA_DIRECTORY + "/bar.html";
        String sgmlPath = DATA_DIRECTORY + "/bar.sgml";
        String textPath = DATA_DIRECTORY + "/bar.txt";
 // TODO fix this, commented for alpha release
 //       assertTrue(doc.get("html").equals(htmlPath));
 /*       assertTrue(doc.get("sgml").equals(sgmlPath));
        assertTrue(doc.get("sutrs").equals(textPath));*/
    }

    public void testWithMap() {
        Properties map = new Properties();
        String xpath = "/document/name/first_name/";
        map.setProperty("1", xpath);

        String barPath = DATA_DIRECTORY + "/bar.xml";
        File barFile = null;
        Document doc = null;

        try {
            barFile = new File(barPath);
            doc = XMLDocument.Document(barFile, map);
        } catch (Exception e) {
            fail("exception creating document: " + e);
        }

        assertTrue(doc.get(xpath).equals("John"));
    }

    public void testConvenience1() {
        Properties map = new Properties();
        String xpath = "/document/name/last_name/";
        map.setProperty("1", xpath);

        String barPath = DATA_DIRECTORY + "/bar.xml";
        Document doc = null;

        try {
            doc = XMLDocument.Document(barPath, map);
        } catch (Exception e) {
            fail("exception creating document: " + e);
        }

        assertTrue(doc.get(xpath).equals("Doe"));
    }

    public void testConvenience2() {
        Document doc = null;

        try {
            doc = XMLDocument.Document(testPath, testMapPath);
        } catch (Exception e) {
            fail("exception creating document: " + e);
        }

        assertTrue(doc.get(UPDATE_FIELD).equals("As needed"));
    }

    public void testNumbers() {
        Document doc = null;

        try {
            doc = XMLDocument.Document(testPath, testMapPath);
        } catch (Exception e) {
            fail("exception creating document: " + e);
        }

        double east = NumericField.stringToNumber(doc.get(EAST_FIELD))
                                  .doubleValue();
        LOGGER.fine("east " + east);
        assertTrue(east == -75.631);

        double extent = NumericField.stringToNumber(doc.get("extent"))
                                    .doubleValue();
        LOGGER.fine("extent " + extent);
        assertTrue(extent == 19.77124208);
    }
}
