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
import org.geotools.filter.CompareFilter;
import org.geotools.filter.DefaultExpression;
import org.geotools.filter.AttributeExpression;
import org.geotools.filter.LiteralExpression;
import org.geotools.resources.Geotools;

/**
 * Tests the get feature request handling.
 *
 * @version $VERSION$
 * @author Rob Hranac, TOPP
 */
public class LockSuite extends TestCase {

    /** Class logger */
    private static final Logger LOGGER =  
        Logger.getLogger("org.vfny.geoserver.requests");

    /** Unit test data directory */
    private static final String DATA_DIRECTORY = 
        System.getProperty("user.dir") + "/misc/testData/unit/requests";

    /** Holds mappings between HTTP and ASCII encodings */
    private static FilterFactory factory = FilterFactory.createFilterFactory();

    /* Initializes the logger. */
    static {
        Geotools.init("Log4JFormatter", Level.FINEST);
    }
    
    /** Constructor with super. */
    public LockSuite (String testName) { super(testName); }

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
    private static boolean runXmlTest(GetFeatureRequest baseRequest,
                                      String fileName, 
                                      boolean match)
        throws Exception {
        // Read the file and parse it
        File inputFile = new File(DATA_DIRECTORY + "/" + fileName + ".xml");
        Reader inputStream = new FileReader(inputFile);
        LockFeatureRequest request = 
            GetFeatureReaderXml.readGetFeature(new BufferedReader(inputStream));
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
     * @param baseRequest Base request, for comparison.
     * @param fileName File name to parse.
     * @param match Whether or not base request and parse request should match.
     * @throws Excpetion If there is any problem running the test.
     */
    private static boolean runKvpTest(LockRequest baseRequest,
                                      String requestString, 
                                      boolean match)
        throws Exception {

        // Read the file and parse it
        LockKvpReader reader = new LockKvpReader(requestString);
        LockRequest request = reader.getRequest();

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
     * KVP TESTS                                                             *
     *************************************************************************
     * KVP GetFeature parsing tests.  Each test reads from a specific KVP    *
     * string and compares it to the base request defined in the test itself.*
     * Tests are run via the static methods in this suite.  The tests        *
     * themselves are quite generic, so documentation is minimal.            *
     *************************************************************************/
    /**
     * Example 1 from the WFS 1.0 specification.
     */   
    public void testKVP1() 
        throws Exception {
         String testRequest = "VERSION=1.0.0&" + 
             "REQUEST=lockFEATURE&" + 
             "SERVICE=WFS&" + 
             "TYPENAME=rail";

        // make base comparison objects        
        LockRequest baseRequest = new LockRequest();
        baseRequest.addFeatureType("rail");
        // run test       
        assertTrue( runKvpTest( baseRequest, testRequest, true));
     }

    /**
     * Example 2 from the WFS 1.0 specification.
     */   
    public void testKVP2() 
        throws Exception {
         String testRequest = "VERSION=1.0.0&" + 
             "REQUEST=lockFEATURE&" + 
             "SERVICE=WFS&" + 
             "TYPENAME=rail&" +
             "featureID=123";

        // make base comparison objects        
        LockRequest baseRequest = new LockRequest();
        baseRequest.addFeatureType("rail");
        FidFilter filter = factory.
            createFidFilter("123");
        baseRequest.addFilter(filter);
        // run test       
        assertTrue(runKvpTest(baseRequest, testRequest, true));
     }

    /**
     * Example 3 from the WFS 1.0 specification.
     */   
    public void testKVP3() 
        throws Exception {
         String testRequest = "VERSION=1.0.0&" + 
             "REQUEST=lockFEATURE&" + 
             "SERVICE=WFS&" + 
             "TYPENAME=rail,roads";

        // make base comparison objects        
        LockRequest baseRequest = new LockRequest();
        baseRequest.addFeatureType("rail");
        baseRequest.addFeatureType("roads");
        // run test       
        assertTrue(runKvpTest(baseRequest, testRequest, true));
     }

    /**
     * Example 13 from the WFS 1.0 specification.
     */   
    public void testKVP4() 
        throws Exception {
        String testRequest = "VERSION=1.0.0&" + 
            "SERVICE=WFS&" + 
            "REQUEST=LockFEATURE&" + 
            "LOCKACTION=all&" + 
            "TYPENAME=rail,roads&" +
            "FILTER=(<Filter xmlns:gml='http://www.opengis.net/gml'><Within><PropertyName>location</PropertyName><gml:Box><gml:coordinates>10,10 20,20</gml:coordinates></gml:Box></Within></Filter>)(<Filter xmlns:gml='http://www.opengis.net/gml'><Within><PropertyName>location</PropertyName><gml:Box><gml:coordinates>10,10 20,20</gml:coordinates></gml:Box></Within></Filter>)";

        LockRequest baseRequest = new LockRequest();
        baseRequest.setVersion("1.0.0");
        baseRequest.addFeatureType("rail");
        baseRequest.addFeatureType("roads");
        baseRequest.setLockAll(true);

        // make base comparison objects
        GeometryFilter filter = factory.
            createGeometryFilter(AbstractFilter.GEOMETRY_WITHIN);
        AttributeExpression leftExpression = factory.
            createAttributeExpression(null); 
        leftExpression.setAttributePath("location");
        // Creates coordinates for the linear ring
        Coordinate[] coords = new Coordinate[5];
        coords[0] = new Coordinate(10,10);
        coords[1] = new Coordinate(10,20);
        coords[2] = new Coordinate(20,20);
        coords[3] = new Coordinate(20,10);
        coords[4] = new Coordinate(10,10);
        LinearRing outerShell = new LinearRing(coords,new PrecisionModel(), 0);
        Polygon polygon = new Polygon(outerShell, new PrecisionModel(), 0);
        LiteralExpression rightExpression = factory.
            createLiteralExpression(polygon); 
        filter.addLeftGeometry(leftExpression);
        filter.addRightGeometry(rightExpression);

        baseRequest.addFilter(filter);
        baseRequest.addFilter(filter);

        // run test       
        assertTrue(runKvpTest(baseRequest, testRequest, true));
    }
}
