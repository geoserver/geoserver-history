/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.requests;

import junit.framework.Test;
import junit.framework.TestSuite;
import org.vfny.geoserver.requests.readers.KvpRequestReader;
import org.vfny.geoserver.requests.readers.XmlRequestReader;
import org.vfny.geoserver.requests.readers.wfs.CapabilitiesKvpReader;
import org.vfny.geoserver.requests.readers.wfs.CapabilitiesXmlReader;
import java.util.Map;
import java.util.logging.Logger;


/**
 * Tests the get capabilities request handling.
 *
 * @author Rob Hranac, TOPP
 * @author Chris Holmes, TOPP
 * @version $Id: CapabilitiesSuite.java,v 1.6 2004/01/21 18:42:26 jive Exp $
 */
public class CapabilitiesSuite extends RequestTestCase {
    // Initializes the logger. Uncomment to see log messages.
    //static {
    //org.vfny.geoserver.config.Log4JFormatter.init("org.vfny.geoserver", 
    //java.util.logging.Level.FINE);
    //}

    /** Standard logging instance */
    private static final Logger LOGGER = Logger.getLogger(
            "org.vfny.geoserver.requests");

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
        baseRequest[0] = new CapabilitiesRequest("WFS");

        //baseRequest[0].setService("WFS");
        baseRequest[0].setVersion("1.0.0");

        baseRequest[1] = new CapabilitiesRequest("WFS");
        baseRequest[1].setVersion("0.0.14");
    }

    protected XmlRequestReader getXmlReader() {
        return new CapabilitiesXmlReader();
    }

    protected KvpRequestReader getKvpReader(Map kvps) {
        return new CapabilitiesKvpReader(kvps);
    }

    /**
     * Check to make sure that a standard XML request is handled correctly.
     *
     * @throws Exception If anything goes wrong.
     */
    public void testXml1() throws Exception {
        assertTrue(runXmlTest(baseRequest[0], "2", true));
    }

    /**
     * Check to make sure that a standard non-matching XML request is handled
     * correctly.
     *
     * @throws Exception If anything goes wrong.
     */
    public void testXml2() throws Exception {
        // instantiates an XML request reader, returns request object
        assertTrue(runXmlTest(baseRequest[1], "3", true));

        /*CapabilitiesRequest request = XmlRequestReader.readGetCapabilities(readFile(
           "3.xml"));
           LOGGER.fine("XML 2 test passed: " + !baseRequest[1].equals(request));
           LOGGER.finer("base request: " + baseRequest[1].toString());
           LOGGER.finer("read request: " + request.toString());
           assertTrue(!baseRequest[1].equals(request));*/
    }

    /**
     * Checks to make sure that a standard KVP request is handled correctly.
     *
     * @throws Exception If anything goes wrong.
     */
    public void testKvp1() throws Exception {
        String requestString = ("service=WFS&version=1.0.0");
        assertTrue(runKvpTest(baseRequest[0], requestString, true));
        baseRequest[0].setService("WMS");
        assertTrue(runKvpTest(baseRequest[0], requestString, false));
    }

    /**
     * Checks to make sure that a standard non-matching KVP request is  handled
     * correctly.
     *
     * @throws Exception If anything goes wrong.
     */
    public void testKvp2() throws Exception {
        String requestString = ("service=WFS&version=0.0.14");
        assertTrue(runKvpTest(baseRequest[1], requestString, true));
    }
}
