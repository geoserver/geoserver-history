/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.requests;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.geom.PrecisionModel;
import junit.framework.TestCase;
import org.geotools.filter.AbstractFilter;
import org.geotools.filter.AttributeExpression;
import org.geotools.filter.FidFilter;
import org.geotools.filter.FilterFactory;
import org.geotools.filter.GeometryFilter;
import org.geotools.filter.LiteralExpression;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.Reader;
import java.util.logging.Logger;


/**
 * Tests the lock feature request handling.
 *
 * @author Rob Hranac, TOPP
 * @author Chris Holmes, TOPP
 * @version $Id: LockSuite.java,v 1.4 2003/09/16 03:33:31 cholmesny Exp $
 */
public class LockSuite extends TestCase {
    // Initializes the logger. Uncomment to see log messages.
    //static {
    //org.vfny.geoserver.config.Log4JFormatter.init("org.vfny.geoserver", 
    //java.util.logging.Level.FINE);
    //}

    /** Class logger */
    private static final Logger LOGGER = Logger.getLogger(
            "org.vfny.geoserver.requests");

    /** Unit test data directory */
    private static final String DATA_DIRECTORY = System.getProperty("user.dir")
        + "/misc/unit/requests";

    /** Holds mappings between HTTP and ASCII encodings */
    private static FilterFactory factory = FilterFactory.createFilterFactory();

    /**
     * Constructor with super.
     *
     * @param testName The name of the test.
     */
    public LockSuite(String testName) {
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
    private static boolean runXmlTest(LockRequest baseRequest, String fileName,
        boolean match) throws Exception {
        // Read the file and parse it
        File inputFile = new File(DATA_DIRECTORY + "/" + fileName + ".xml");
        Reader inputStream = new FileReader(inputFile);
        LockRequest request = XmlRequestReader.readLockRequest((Reader) new BufferedReader(
                    inputStream));
        LOGGER.finer("base request: " + baseRequest);
        LOGGER.finer("read request: " + request);
        LOGGER.fine("XML " + fileName + " test passed: "
            + baseRequest.equals(request));

        // Compare parsed request to base request
        if (match) {
            return request.equals(baseRequest); //baseRequest.toString().equals(request.toString());
        } else {
            return !baseRequest.equals(request);
        }
    }

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
    private static boolean runKvpTest(LockRequest baseRequest,
        String requestString, boolean match) throws Exception {
        // Read the file and parse it
        LockKvpReader reader = new LockKvpReader(requestString);
        LockRequest request = reader.getRequest();

        LOGGER.finer("base request: " + baseRequest);
        LOGGER.finer("read request: " + request);
        LOGGER.fine("KVP test passed: " + baseRequest.equals(request));

        // Compare parsed request to base request
        if (match) {
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

    public void testXml1() throws Exception {
        LockRequest request = new LockRequest();
        request.setLockAll(false);
        addLock1(request);
        assertTrue(runXmlTest(request, "lock1", true));
    }

    public void testXml2() throws Exception {
        LockRequest request = new LockRequest();
        request.addLock("roads", null);
        assertTrue(runXmlTest(request, "lock2", true));
    }

    public void testXml3() throws Exception {
        LockRequest request = new LockRequest();
        addLock3(request);
        assertTrue(runXmlTest(request, "lock3", true));
    }

    public void testXml4() throws Exception {
        LockRequest request = new LockRequest();
        addLock3(request);
        addLock1(request);
        request.setExpiry(3);
        assertTrue(runXmlTest(request, "lock4", true));
    }

    private void addLock1(LockRequest request) throws Exception {
        FidFilter tempFilter = factory.createFidFilter("rail.1013");
        tempFilter.addFid("rail.1014");
        tempFilter.addFid("rail.1015");
        tempFilter.addFid("rail.1016");
        tempFilter.addFid("rail.1017");
        request.addLock("rail", tempFilter, "lock1");
    }

    private void addLock3(LockRequest request) throws Exception {
        org.geotools.filter.GeometryFilter gf = factory.createGeometryFilter(AbstractFilter.GEOMETRY_WITHIN);
        int srid = 0;
        Coordinate[] points = {
            new Coordinate(-95.7, 38.1), new Coordinate(-97.8, 38.2),
            new Coordinate(-97.8, 42.9), new Coordinate(-95.7, 42.9),
            new Coordinate(-95.7, 38.1)
        };
        PrecisionModel precModel = new PrecisionModel();
        LinearRing shell = new LinearRing(points, precModel, srid);
        Polygon testPoly = new Polygon(shell, precModel, srid);

        LiteralExpression right = factory.createLiteralExpression(testPoly);
        gf.addRightGeometry(right);
        gf.addLeftGeometry(factory.createAttributeExpression(null, "the_geom"));
        request.addLock("INWATERA_1M", gf, "Lock3");
    }

    /**
     * KVP TESTS KVP GetFeature parsing tests.  Each test reads from a specific
     * KVP string and compares it to the base request defined in the test
     * itself. Tests are run via the static methods in this suite.  The tests
     * themselves are quite generic, so documentation is minimal.
     */
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
       baseRequest.addLock("rail", null, null);
       // run test
       assertTrue( runKvpTest( baseRequest, testRequest, true));
       } 
    /*
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
             // baseRequest.addFeatureType("rail");
             FidFilter filter = factory.
                 createFidFilter("123");
             //baseRequest.addFilter(filter);
             baseRequest.addLock("rail", filter, null);
             // run test
             assertTrue(runKvpTest(baseRequest, testRequest, true));
	 }
    /*
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
             baseRequest.addLock("rail", null);
             baseRequest.addLock("roads", null);
             // run test
             assertTrue(runKvpTest(baseRequest, testRequest, true));
          }
    /*
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
             baseRequest.addLock("rail", filter);
             baseRequest.addLock("roads", filter);
             //        baseRequest.addFilter(filter);
             //baseRequest.addFilter(filter);
             // run test
             assertTrue(runKvpTest(baseRequest, testRequest, true));
         }
}
