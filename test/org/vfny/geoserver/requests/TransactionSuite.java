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
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.geom.PrecisionModel;
import org.geotools.filter.FilterFactory;
import org.geotools.filter.Filter;
import org.geotools.filter.AbstractFilter;
import org.geotools.filter.FidFilter;
import org.geotools.filter.GeometryFilter;
import org.geotools.filter.AttributeExpression;
import org.geotools.filter.LiteralExpression;
import org.geotools.resources.Geotools;

/**
 * Tests the get feature request handling.
 *
 * @author Rob Hranac, TOPP
 * @version $VERSION$
 */
public class TransactionSuite extends TestCase {

    /** Class logger */
    private static final Logger LOGGER =  
        Logger.getLogger("org.vfny.geoserver.requests");

    /** Unit test data directory */
    private static final String DATA_DIRECTORY = 
        System.getProperty("user.dir") + "/misc/testData/unit/requests";

    /* Initializes the logger. */
    static {
        Geotools.init("Log4JFormatter", Level.INFO);
    }
    
    /** Constructor with super. */
    public TransactionSuite (String testName) { super(testName); }

    /*************************************************************************
     * STATIC METHODS FOR TEST RUNNING                                       *
     *************************************************************************/
    /**
     * Handles actual XML test running details.
     * @param baseRequest Base request, for comparison.
     * @param fileName File name to parse.
     * @param match Whether or not base request and parse request should match.
     * @throws Excpetion If there is any problem running the test.
     */
    /*
    private static boolean runXmlTest(TransactionRequest baseRequest,
                                      String fileName, 
                                      boolean match)
        throws Exception {

        // Read the file and parse it
        File inputFile = new File(DATA_DIRECTORY + "/" + fileName + ".xml");
        Reader inputStream = new FileReader(inputFile);
        TransactionRequest request = 
            XmlRequestReader.readGetFeature(new BufferedReader(inputStream));
        LOGGER.fine("base request: " + baseRequest);
        LOGGER.fine("read request: " + request);
        LOGGER.info("XML " + fileName +" test passed: " +  
                    baseRequest.equals(request));

        // Compare parsed request to base request
        if(match) {
            return baseRequest.equals(request);
        } else {
            return !baseRequest.equals(request);
        }
    }
    */
    /**
     * Handles actual XML test running details.
     *
     * @param baseRequest Base request, for comparison.
     * @param fileName File name to parse.
     * @param match Whether or not base request and parse request should match.
     * @throws Excpetion If there is any problem running the test.
     */
    private static boolean runKvpTest(FeatureRequest baseRequest,
                                      String requestString, 
                                      boolean match)
        throws Exception {

        // Read the file and parse it
        FeatureKvpReader reader = new FeatureKvpReader(requestString);
        FeatureRequest request = reader.getRequest();

        LOGGER.fine("base request: " + baseRequest);
        LOGGER.fine("read request: " + request);
        LOGGER.info("KVP test passed: " +  
                    baseRequest.equals(request));

        // Compare parsed request to base request
        if(match) {
            return baseRequest.equals(request);
        } else {
            return !baseRequest.equals(request);
        }
    }


    /**
     * Handles test set up details.
     */
    public void setUp() {
    }
    

    /*************************************************************************
     * XML TESTS                                                             *
     *************************************************************************
     * XML GetFeature parsing tests.  Each test reads from a specific XML    *
     * file and compares it to the base request defined in the test itself.  *
     * Tests are run via the static methods in this suite.  The tests        *
     * themselves are quite generic, so documentation is minimal.            *
     *************************************************************************/
    public void test1() throws Exception {        
        // make base comparison objects
        TransactionRequest baseRequest = new TransactionRequest();
        // run test
    }
    
    

    /*************************************************************************
     * KVP TESTS                                                             *
     *************************************************************************
     * KVP GetFeature parsing tests.  Each test reads from a specific KVP    *
     * string and compares it to the base request defined in the test itself.*
     * Tests are run via the static methods in this suite.  The tests        *
     * themselves are quite generic, so documentation is minimal.            *
     *************************************************************************/
}
