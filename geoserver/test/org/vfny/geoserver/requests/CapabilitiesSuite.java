/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.requests;

import java.io.File;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.Reader;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import java.util.logging.Logger;


/**
 * Tests the get capabilities request handling.
 *
 * @author Rob Hranac, TOPP
 * @version $Id: CapabilitiesSuite.java,v 1.3 2003/09/16 03:33:31 cholmesny Exp $
 */
public class CapabilitiesSuite extends TestCase {
    // Initializes the logger. Uncomment to see log messages.
    //static {
        //org.vfny.geoserver.config.Log4JFormatter.init("org.vfny.geoserver", 
	//java.util.logging.Level.FINE);
    //}

    /** Standard logging instance */
    private static final Logger LOGGER = Logger.getLogger(
            "org.vfny.geoserver.requests");

    /** The unit test data directory */
    private static final String DATA_DIRECTORY = System.getProperty("user.dir")
        + "/misc/unit/requests";

    /** Base request for comparison */
    private CapabilitiesRequest[] baseRequest = new CapabilitiesRequest[10];

    /**
     * Initializes the database and request handler.
     *
     * @param testName The name of this test.
     */
    public CapabilitiesSuite(String testName) {
        super(testName);
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(CapabilitiesSuite.class);

        return suite;
    }

    public void setUp() {
        baseRequest[0] = new CapabilitiesRequest();
        baseRequest[0].setService("WFS");
        baseRequest[0].setVersion("0.0.15");

        baseRequest[1] = new CapabilitiesRequest();
        baseRequest[1].setVersion("0.0.15");
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
    private static BufferedReader readFile(String filename)
        throws Exception {
        LOGGER.finer("about to read: " + DATA_DIRECTORY + "/" + filename);

        File inputFile = new File(DATA_DIRECTORY + "/" + filename);
        Reader inputStream = new FileReader(inputFile);

        return new BufferedReader(inputStream);
    }

    /**
     * Check to make sure that a standard XML request is handled correctly.
     *
     * @throws Exception If anything goes wrong.
     */
    public void testXml1() throws Exception {
        // instantiates an XML request reader, returns request object
        Object request = XmlRequestReader.readGetCapabilities(readFile("2.xml"));

        LOGGER.fine("XML 1 test passed: " + baseRequest[1].equals(request));
        LOGGER.finer("base request: " + baseRequest[1].toString());
        LOGGER.finer("read request: " + request.toString());
        assertTrue(baseRequest[1].equals(request));
    }

    /**
     * Check to make sure that a standard non-matching XML request is handled
     * correctly.
     *
     * @throws Exception If anything goes wrong.
     */
    public void testXml2() throws Exception {
        // instantiates an XML request reader, returns request object
        CapabilitiesRequest request = XmlRequestReader.readGetCapabilities(readFile(
                    "3.xml"));

        LOGGER.fine("XML 2 test passed: " + !baseRequest[1].equals(request));
        LOGGER.finer("base request: " + baseRequest[1].toString());
        LOGGER.finer("read request: " + request.toString());
        assertTrue(!baseRequest[1].equals(request));
    }

    /**
     * Checks to make sure that a standard KVP request is handled correctly.
     *
     * @throws Exception If anything goes wrong.
     */
    public void testKvp1() throws Exception {
        CapabilitiesKvpReader reader = new CapabilitiesKvpReader(
                "service=WFS&version=0.0.15");
        CapabilitiesRequest request = reader.getRequest();

        LOGGER.fine("KVP 1 test passed: " + baseRequest[0].equals(request));
        LOGGER.finer("base request: " + baseRequest[0].toString());
        LOGGER.finer("read request: " + request.toString());
        assertTrue(baseRequest[0].equals(request));
        request.setService("WMS");
        assertTrue(!baseRequest[0].equals(request));
    }

    /**
     * Checks to make sure that a standard non-matching KVP request is  handled
     * correctly.
     *
     * @throws Exception If anything goes wrong.
     */
    public void testKvp2() throws Exception {
        CapabilitiesKvpReader reader = new CapabilitiesKvpReader(
                "service=WFS&version=0.0.14");
        CapabilitiesRequest request = reader.getRequest();

        LOGGER.fine("KVP 2 test passed: " + !baseRequest[0].equals(request));
        LOGGER.finer("base request: " + baseRequest[0].toString());
        LOGGER.finer("read request: " + request.toString());
        assertTrue(!baseRequest[0].equals(request));
    }
}
