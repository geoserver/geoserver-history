/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.zserver;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.apache.lucene.document.Field;
import org.w3c.dom.NodeList;
import java.io.FileInputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * Tests the GeoSummary classes's setting of fields and creation of summaries.
 *
 * @author Chris Holmes, TOPP
 * @version $Id: GeoSummarySuite.java,v 1.2 2003/09/09 21:43:05 cholmesny Exp $
 *
 * @task HACK: we need to test xml output.  Our old way of comparing strings.
 *       no longer works, as different xml implementations lead to different
 *       string representations.
 */
public class GeoSummarySuite extends TestCase {
    /* Initializes the logger. Uncomment to see log messages.*/

    //static {
    //    org.vfny.geoserver.config.Log4JFormatter.init("org.vfny.geoserver", Level.FINEST);
    //}

    /** Standard logging instance */
    private static final Logger LOGGER = Logger.getLogger(
            "org.vfny.geoserver.zserver");

    /** Unit test data directory */
    private static final String DATA_DIRECTORY = System.getProperty("user.dir")
        + "/misc/unit/zserver";
    private String testMapPath = DATA_DIRECTORY + "/geo.map";
    private Properties testMap;
    private org.apache.lucene.document.Document doc;

    /**
     * Constructor that calls super.
     *
     * @param testName Name of the test.
     */
    public GeoSummarySuite(String testName) {
        super(testName);
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(GeoSummarySuite.class);
        LOGGER.finer("Creating GeoSummary suite.");

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
    }

    public void testTitleField() {
        GeoSummary testSum = new GeoSummary();
        testSum.setTitle("test");
        assertTrue(testSum.getTextSummary().equals("TITLE=test\n"));
        assertTrue(testSum.getHtmlSummary().equals("<P><B>TITLE=test</B></P>\n"));

        //testXmlSummary(testSum,
        //  "<metadata><idinfo><citation><citeinfo><title>test</title></citeinfo></citation></idinfo></metadata>");
    }

    public void testEditionField() {
        GeoSummary testSum = new GeoSummary();
        testSum.setEdition("2");
        LOGGER.finer("Summary is " + testSum.getHtmlSummary());
        assertTrue(testSum.getTextSummary().equals("EDITION=2\n"));
        assertTrue(testSum.getHtmlSummary().equals("<P>EDITION=2</P>\n"));

        //testXmlSummary(testSum,
        //    "<metadata><idinfo><citation><citeinfo><edition>2</edition></citeinfo></citation></idinfo></metadata>");
    }

    public void testGeoFormField() {
        GeoSummary testSum = new GeoSummary();
        testSum.setGeoform("river");
        LOGGER.finer("Summary is " + testSum.getHtmlSummary());
        assertTrue(testSum.getTextSummary().equals("GEOFORM=river\n"));
        assertTrue(testSum.getHtmlSummary().equals("<P>GEOFORM=river</P>\n"));

        //testXmlSummary(testSum,
        //    "<metadata><idinfo><citation><citeinfo><geoform>river</geoform></citeinfo></citation></idinfo></metadata>");
    }

    public void testIndsprefField() {
        GeoSummary testSum = new GeoSummary();
        testSum.setIndspref("blorg");
        LOGGER.finer("Summary is " + testSum.getHtmlSummary());
        assertTrue(testSum.getTextSummary().equals("INDSPREF=blorg\n"));
        assertTrue(testSum.getHtmlSummary().equals("<P>INDSPREF=blorg</P>\n"));

        //testXmlSummary(testSum,
        //    "<metadata><idinfo /><spdoinfo><indspref>blorg</indspref></spdoinfo></metadata>");
    }

    public void testUpdateField() {
        GeoSummary testSum = new GeoSummary();
        testSum.setUpdate("twice");
        LOGGER.finer("Summary is " + testSum.getHtmlSummary());
        assertTrue(testSum.getTextSummary().equals("UPDATE=twice\n"));
        assertTrue(testSum.getHtmlSummary().equals("<P>UPDATE=twice</P>\n"));

        //testXmlSummary(testSum,
        //    "<metadata><idinfo><status><update>twice</update></status></idinfo></metadata>");
    }

    public void testBoundings() {
        GeoSummary testSum = new GeoSummary();
        testSum.setBounds("-15", "20", "25", "10");
        LOGGER.finer("Summary is " + testSum.getHtmlSummary());
        assertTrue(testSum.getTextSummary().equals("WESTBC=-15\nEASTBC=20\nNORTHBC=25\nSOUTHBC=10\n"));
        assertTrue(testSum.getHtmlSummary().equals("<P>WESTBC=-15</P>\n<P>EASTBC=20</P>\n<P>NORTHBC=25</P>\n<P>SOUTHBC=10</P>\n"));

        //testXmlSummary(testSum,
        //    "<metadata><idinfo><spdom><bounding><westbc>-15</westbc><eastbc>20</eastbc><northbc>25</northbc><southbc>10</southbc></bounding></spdom></idinfo></metadata>");
    }

    public void testDates() {
        GeoSummary testSum = new GeoSummary();
        testSum.setBeginningDate("1995");
        testSum.setEndingDate("19960304");
        LOGGER.finer("Summary is " + testSum.getHtmlSummary());
        assertTrue(testSum.getTextSummary().equals("BEGDATE=1995\nENDDATE=19960304\n"));
        assertTrue(testSum.getHtmlSummary().equals("<P>BEGDATE=1995</P>\n<P>ENDDATE=19960304</P>\n"));

        //testXmlSummary(testSum,
        //    "<metadata><idinfo><timeperd><timeinfo><rngdates><begdate>1995</begdate><enddate>19960304</enddate></rngdates></timeinfo></timeperd></idinfo></metadata>");
    }

    public void testDocConstructor() {
        doc = new org.apache.lucene.document.Document();

        String fieldName = testMap.getProperty(GeoProfile.Attribute.TITLE);
        doc.add(Field.Text(fieldName, "test title"));
        fieldName = testMap.getProperty(GeoProfile.Attribute.ONLINK);
        doc.add(Field.Text(fieldName, "test online linkage"));
        fieldName = testMap.getProperty(GeoProfile.Attribute.BEGDATE);
        doc.add(Field.Text(fieldName, "19790212"));
        fieldName = testMap.getProperty(GeoProfile.Attribute.ENDDATE);
        doc.add(Field.Text(fieldName, "19790212"));
        fieldName = testMap.getProperty(GeoProfile.Attribute.WESTBC);
        doc.add(Field.Text(fieldName, NumericField.numberToString("-30")));
        fieldName = testMap.getProperty(GeoProfile.Attribute.EASTBC);
        doc.add(Field.Text(fieldName, NumericField.numberToString("88.34")));
        fieldName = testMap.getProperty(GeoProfile.Attribute.NORTHBC);
        doc.add(Field.Text(fieldName, NumericField.numberToString("10.2")));
        fieldName = testMap.getProperty(GeoProfile.Attribute.SOUTHBC);
        doc.add(Field.Text(fieldName, NumericField.numberToString("-44")));

        GeoSummary testSum = new GeoSummary(doc, testMap);
        LOGGER.finest("Summary is " + testSum.getTextSummary());
        assertTrue(testSum.getTextSummary().equals("TITLE=test title\n"
                + "BEGDATE=19790212\n" + "ENDDATE=19790212\n"
                + "WESTBC=-30.0\n" + "EASTBC=88.34\n" + "NORTHBC=10.2\n"
                + "SOUTHBC=-44.0\n" + "ONLINK=test online linkage\n"));

        //testXmlSummary(testSum,
        //    "<metadata><idinfo><citation><citeinfo><title>test "
        //    + "title</title><onlink>test online linkage</onlink></citeinfo>"
        //    + "</citation><timeperd><timeinfo><rngdates><begdate>19790212"
        //    + "</begdate><enddate>19790212</enddate></rngdates></timeinfo>"
        //    + "</timeperd><spdom><bounding><westbc>-30.0</westbc><eastbc>"
        //    + "88.34</eastbc><northbc>10.2</northbc><southbc>-44.0</southbc>"
        //    + "</bounding></spdom></idinfo></metadata>");
    }

    private void testXmlSummary(GeoSummary summary, String expected) {
        org.w3c.dom.Document doc = summary.getXmlSummary();
        NodeList nodes = doc.getElementsByTagName("metadata");
        String nodeString = nodes.item(0).toString();
        LOGGER.finest(nodeString);
        assertTrue(nodeString.equals(expected));
    }
}
