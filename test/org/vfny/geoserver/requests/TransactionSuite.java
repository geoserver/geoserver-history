/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.requests;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.geotools.filter.FilterFactory;
import org.vfny.geoserver.config.ConfigInfo;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.Reader;
import java.util.logging.Logger;


/**
 * Tests the get feature request handling.
 *
 * @author Chris Holmes, TOPP
 * @version $Id: TransactionSuite.java,v 1.3 2003/09/16 03:32:10 cholmesny Exp $
 *
 * @task REVISIT: This should serve as the place for the sub transaction suites
 *       to run their tests.
 */
public class TransactionSuite extends TestCase {
    // Initializes the logger. Uncomment to see log messages.
    //static {
    //org.vfny.geoserver.config.Log4JFormatter.init("org.vfny.geoserver", 
    //java.util.logging.Level.FINE);
    //}

    /** Class logger */
    protected static final Logger LOGGER = Logger.getLogger(
            "org.vfny.geoserver.requests");

    /** Unit test data directory */
    private static final String DATA_DIRECTORY = System.getProperty("user.dir")
        + "/misc/unit/requests";

    /** Holds mappings between HTTP and ASCII encodings */
    protected static FilterFactory factory = FilterFactory.createFilterFactory();

    /** Unit test data directory */
    private static final String CONFIG_DIR = System.getProperty("user.dir")
        + "/misc/unit/config/";

    //classes complain if we don't set up a valid config info.
    static {
        ConfigInfo.getInstance(CONFIG_DIR);
    }

    /**
     * Constructor with super.
     *
     * @param testName The name of the test.
     */
    public TransactionSuite(String testName) {
        super(testName);
    }

    public static void main(java.lang.String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static Test suite() {
        TestSuite suite = new TestSuite("All transaction tests");
        suite.addTestSuite(UpdateSuite.class);

        //suite.addTestSuite(InsertSuite.class);
        suite.addTestSuite(DeleteSuite.class);

        return suite;
    }

    public void setUp() {
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
    static boolean runXmlTest(TransactionRequest baseRequest, String fileName,
        boolean match) throws Exception {
        // Read the file and parse it
        File inputFile = new File(DATA_DIRECTORY + "/" + fileName + ".xml");
        Reader inputStream = new FileReader(inputFile);
        TransactionRequest request = XmlRequestReader.readTransaction(new BufferedReader(
                    inputStream));
        LOGGER.finer("base request: " + baseRequest);
        LOGGER.finer("read request: " + request);
        LOGGER.fine("XML " + fileName + " test passed: "
            + baseRequest.equals(request));

        // Compare parsed request to base request
        if (match) {
            return assertEquals(baseRequest, request);
        } else {
            return !(assertEquals(baseRequest, request));
        }
    }

    protected static boolean assertEquals(TransactionRequest baseRequest,
        TransactionRequest testRequest) {
        return baseRequest.equals(testRequest);
    }
}
