/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.requests;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.Reader;
import java.util.Map;
import java.util.logging.Logger;

import junit.framework.TestCase;

import org.geotools.filter.FilterFactory;
import org.vfny.geoserver.requests.readers.KvpRequestReader;
import org.vfny.geoserver.requests.readers.XmlRequestReader;


/**
 * Abstract test case to run request tests.  Subclasses must implement
 * getXmlReader and getKvpReader to be able to call the runXmlTest and
 * runKvpTest.  If one of the readers does not exist it is fine to just
 * return null, as long as that test runner is not called by the client at
 * all.
 *
 * @author Chris Holmes, TOPP
 * @version $Id: RequestTestCase.java,v 1.4 2004/01/12 21:01:28 dmzwiers Exp $
 */
public abstract class RequestTestCase extends TestCase {
    //Initializes the logger. Uncomment to see log messages.
    //static {
    // org.vfny.geoserver.config.Log4JFormatter.init("org.vfny.geoserver", 
    //					       java.util.logging.Level.FINER);
    //}

    /** Class logger */
    private static final Logger LOGGER = Logger.getLogger(
            "org.vfny.geoserver.requests");

    /** Unit test data directory */
    private static final String DATA_DIRECTORY = System.getProperty("user.dir")
        + "/misc/unit/requests";

    /** Holds mappings between HTTP and ASCII encodings */
    protected static FilterFactory factory = FilterFactory.createFilterFactory();

    /** Unit test data directory */
    private static final String CONFIG_DIR = System.getProperty("user.dir")
        + "/misc/unit/config/";

    /**
     * Constructor with super.
     *
     * @param testName The name of the test.
     */
    public RequestTestCase(String testName) {
        super(testName);
    }

    /**
     * Handles actual XML test running details.
     *
     * @param baseRequest Base request, for comparison.
     * @param fileName File name to parse.
     * @param match Whether or not base request and parse request should match.
     *
     * @return <tt>true</tt> if the test passed.
     *
     * @throws Exception If there is any problem running the test.
     */
    protected boolean runXmlTest(Request baseRequest, String fileName,
        boolean match) throws Exception {
        // Read the file and parse it
        File inputFile = new File(DATA_DIRECTORY + "/" + fileName + ".xml");
        Reader inputStream = new FileReader(inputFile);
        XmlRequestReader reader = getXmlReader();
        Request request = reader.read(new BufferedReader(inputStream));
        LOGGER.finer("base request: " + baseRequest);
        LOGGER.finer("read request: " + request);
        LOGGER.fine("XML " + fileName + " test passed: "
            + baseRequest.equals(request));

        // Compare parsed request to base request
        if (match) {
            assertEquals(baseRequest, request);

            return baseRequest.equals(request);
        } else {
            return !baseRequest.equals(request);
        }
    }

    /**
     * This should return the appropriate xml reader to be used in running the
     * tests.
     *
     * @return DOCUMENT ME!
     */
    protected abstract XmlRequestReader getXmlReader();

    /**
     * Handles actual XML test running details.
     *
     * @param baseRequest Base request, for comparison.
     * @param requestString File name to parse.
     * @param match Whether or not base request and parse request should match.
     *
     * @return <tt>true</tt> if the test passed.
     *
     * @throws Exception If there is any problem running the test.
     */
    protected boolean runKvpTest(Request baseRequest, String requestString,
        boolean match) throws Exception {
        // Read the file and parse it
        Map kvps = KvpRequestReader.parseKvpSet(requestString);
        KvpRequestReader reader = getKvpReader(kvps);
        Request request = reader.getRequest();

        LOGGER.finer("base request: " + baseRequest);
        LOGGER.finer("read request: " + request);
        LOGGER.fine("KVP test passed: " + baseRequest.equals(request));

        // Compare parsed request to base request
        if (match) {
            assertEquals(baseRequest, request);

            return baseRequest.equals(request);
        } else {
            return !baseRequest.equals(request);
        }
    }

    /**
     * This should return the appropriate xml reader to be used in running the
     * tests.
     *
     * @return DOCUMENT ME!
     */
    protected abstract KvpRequestReader getKvpReader(Map kvps);
}
