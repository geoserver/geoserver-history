/* Copyright (c) 2001 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root 
 * application directory.
 */
package org.vfny.geoserver.requests;

import java.io.*;
import java.util.*;
import java.util.logging.Logger;
import java.util.logging.Level;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.geotools.resources.Geotools;

/**
 * Tests the get capabilities request handling.
 *
 * @author Rob Hranac, TOPP
 * @version $VERSION$
 */
public class CapabilitiesSuite extends TestCase {

    /** Standard logging instance */
    private static final Logger LOGGER = 
        Logger.getLogger("org.vfny.geoserver.requests");

    /** The unit test data directory */
    private static final String DATA_DIRECTORY = 
        System.getProperty("user.dir") + "/misc/testData/unit/requests";

    /** Base request for comparison */
    private CapabilitiesRequest baseRequest[] = 
        new CapabilitiesRequest[10];

    /* Initializes the logger. */
    static {
        Geotools.init("Log4JFormatter", Level.FINEST);
    }
    

    /**
     * Initializes the database and request handler.
     */
    public CapabilitiesSuite (String testName) {
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
     * Initializes the database and request handler.
     *
     * @param response The query from the request object.
     * @param maxFeatures The query from the request object.
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
     */
    public void testXml1()
        throws Exception {

        // instantiates an XML request reader, returns request object
        CapabilitiesRequest request = 
            XmlRequestReader.readGetCapabilities(readFile("2.xml"));

        LOGGER.info("XML 1 test passed: " + baseRequest[1].equals(request));
        LOGGER.finer("base request: " + baseRequest[1].toString());
        LOGGER.finer("read request: " + request.toString());
        assertTrue(baseRequest[1].equals(request));
    }

    /**
     * Check to make sure that a standard non-matching XML request is handled 
     * correctly.
     */
    public void testXml2()
        throws Exception {

        // instantiates an XML request reader, returns request object
        CapabilitiesRequest request = 
            XmlRequestReader.readGetCapabilities(readFile("3.xml"));

        LOGGER.info("XML 2 test passed: " + !baseRequest[1].equals(request));
        LOGGER.finer("base request: " + baseRequest[1].toString());
        LOGGER.finer("read request: " + request.toString());
        assertTrue(!baseRequest[1].equals(request));
    }

    /**
     * Checks to make sure that a standard KVP request is handled correctly.
     */
    public void testKvp1()
        throws Exception {


        CapabilitiesKvpReader reader = 
            new CapabilitiesKvpReader("service=WFS&version=0.0.15");
        CapabilitiesRequest request = reader.getRequest();

        LOGGER.info("KVP 1 test passed: " + baseRequest[0].equals(request));
        LOGGER.finer("base request: " + baseRequest[0].toString());
        LOGGER.finer("read request: " + request.toString());
        assertTrue(baseRequest[0].equals(request));
    }

    /**
     * Checks to make sure that a standard non-matching KVP request is 
     * handled correctly.
     */
    public void testKvp2()
        throws Exception {


        CapabilitiesKvpReader reader = 
            new CapabilitiesKvpReader("service=WFS&version=0.0.14");
        CapabilitiesRequest request = reader.getRequest();

        LOGGER.info("KVP 2 test passed: " + !baseRequest[0].equals(request));
        LOGGER.finer("base request: " + baseRequest[0].toString());
        LOGGER.finer("read request: " + request.toString());
        assertTrue(!baseRequest[0].equals(request));
    }


}
