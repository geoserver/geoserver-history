/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.requests;

import java.util.Map;
import java.util.logging.Logger;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.vfny.geoserver.requests.readers.KvpRequestReader;
import org.vfny.geoserver.requests.readers.XmlRequestReader;
import org.vfny.geoserver.requests.readers.wfs.DescribeKvpReader;
import org.vfny.geoserver.requests.readers.wfs.DescribeXmlReader;
import org.vfny.geoserver.requests.wfs.DescribeRequest;

/**
 * Tests the get capabilities request handling.
 *
 * @author Rob Hranac, TOPP
 * @author Chris Holmes, TOPP
 * @version $Id: DescribeSuite.java,v 1.5.2.1 2003/12/31 00:36:51 dmzwiers Exp $
 */
public class DescribeSuite extends RequestTestCase {
    // Initializes the logger. Uncomment to see log messages.
    //static {
    //org.vfny.geoserver.config.Log4JFormatter.init("org.vfny.geoserver", 
    //java.util.logging.Level.FINE);
    //}

    /** Standard logging instance */
    private static final Logger LOGGER = Logger.getLogger(
            "org.vfny.geoserver.requests");

    /** Base request for comparison */
    private DescribeRequest[] baseRequest = new DescribeRequest[10];

    /**
     * Constructor with super.
     *
     * @param testName The name of this test.
     */
    public DescribeSuite(String testName) {
        super(testName);
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(DescribeSuite.class);

        return suite;
    }

    public void setUp() {
        baseRequest[0] = new DescribeRequest();
        baseRequest[0].addFeatureType("rail");
        baseRequest[0].setVersion("0.0.15");

        baseRequest[1] = new DescribeRequest();
        baseRequest[1].addFeatureType("rail");
        baseRequest[1].addFeatureType("roads");
        baseRequest[1].setVersion("0.0.15");
    }

      protected XmlRequestReader getXmlReader() {
        return new DescribeXmlReader();
    }

    protected KvpRequestReader getKvpReader(Map kvps) {
        return new DescribeKvpReader(kvps);
    }

    /**
     * Gets a BufferedReader from the file to be passed as if it were from
     * a servlet.
     *
     * @param filename The file containing the request.
     *
     * @return A BufferedReader of the input of the file.
     *
     * @throws Exception If anything goes wrong.
     */
    /*private static BufferedReader readFile(String filename)
        throws Exception {
        LOGGER.finer("about to read: " + DATA_DIRECTORY + "/" + filename);

        File inputFile = new File(DATA_DIRECTORY + "/" + filename);
        Reader inputStream = new FileReader(inputFile);

        return new BufferedReader(inputStream);
	}*/

    /**
     * Check to make sure that a standard XML request is handled correctly.
     *
     * @throws Exception If anything goes wrong.
     */
    public void testXml1() throws Exception {
        // instantiates an XML request reader, returns request object
        //DescribeRequest request = XmlRequestReader.readDescribeFeatureType(readFile(
	//          "4.xml"));

	assertTrue(runXmlTest(baseRequest[0], "4", true));
        //LOGGER.fine("XML 1 test passed: " + baseRequest[0].equals(request));
        //LOGGER.finer("base request: " + baseRequest[0].toString());
        //LOGGER.finer("read request: " + request.toString());
        //assertTrue(baseRequest[0].equals(request));
    }

    /**
     * Check to make sure that a standard XML request is handled correctly.
     *
     * @throws Exception If anything goes wrong.
     */
    public void testXml2() throws Exception {
        assertTrue(runXmlTest(baseRequest[1], "5", true));
	// instantiates an XML request reader, returns request object
        //DescribeRequest request = XmlRequestReader.readDescribeFeatureType(readFile(
        //            "5.xml"));

	//        LOGGER.fine("XML 2 test passed: " + baseRequest[1].equals(request));
        //LOGGER.finer("base request: " + baseRequest[1].toString());
        //LOGGER.finer("read request: " + request.toString());
        //assertTrue(baseRequest[1].equals(request));
    }

    /**
     * Checks to make sure that a standard KVP request is handled correctly.
     *
     * @throws Exception If anything goes wrong.
     */
    public void testKvp1() throws Exception {
	String requestString = "service=WFS&typename=rail";
        //DescribeRequest request = reader.getRequest();

        //LOGGER.fine("KVP 1 test passed: " + baseRequest[0].equals(request));
        //LOGGER.finer("base request: " + baseRequest[0].toString());
        //LOGGER.finer("read request: " + request.toString());
        assertTrue(runKvpTest(baseRequest[0],requestString, true));
    }

    /**
     * Checks to make sure that a standard non-matching KVP request is  handled
     * correctly.
     *
     * @throws Exception If anything goes wrong.
     */
    public void testKvp2() throws Exception {
        String requestString = "service=WFS&typename=rail,roads";
        assertTrue(runKvpTest(baseRequest[1],requestString, true));
    }
}
