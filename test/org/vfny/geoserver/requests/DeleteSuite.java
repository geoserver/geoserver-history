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
import org.vfny.geoserver.config.ConfigInfo;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.Reader;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * Tests the Delete request handling.
 *
 * @author Rob Hranac, TOPP
 * @author Chris Holmes, TOPP
 * @version $Id: DeleteSuite.java,v 1.7 2003/09/13 00:20:07 cholmesny Exp $
 */
public class DeleteSuite extends TestCase {
    // Initializes the logger. Uncomment to see log messages.
    //static {
    //    org.vfny.geoserver.config.Log4JFormatter.init("org.vfny.geoserver", Level.FINEST);
    //}

    /** Class logger */
    private static final Logger LOGGER = Logger.getLogger(
            "org.vfny.geoserver.requests");

    /** Unit test data directory */
    private static final String DATA_DIRECTORY = System.getProperty("user.dir")
        + "/misc/unit/requests";

    /** Holds mappings between HTTP and ASCII encodings */
    private static FilterFactory factory = FilterFactory.createFilterFactory();

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
     * @param testName DOCUMENT ME!
     */
    public DeleteSuite(String testName) {
        super(testName);
    }

    /**
     * Handles actual XML test running details.
     *
     * @param baseRequest Base request, for comparison.
     * @param fileName File name to parse.
     * @param match Whether or not base request and parse request should match.
     *
     * @return DOCUMENT ME!
     *
     * @throws Exception If there is any problem running the test.
     */
    private static boolean runXmlTest(TransactionRequest baseRequest,
        String fileName, boolean match) throws Exception {
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
            return baseRequest.equals(request);
        } else {
            return !baseRequest.equals(request);
        }
    }

    /**
     * Handles actual KVP test running details.
     *
     * @param baseRequest Base request, for comparison.
     * @param requestString File name to parse.
     * @param match Whether or not base request and parse request should match.
     *
     * @return DOCUMENT ME!
     *
     * @throws Exception If there is any problem running the test.
     */
    private static boolean runKvpTest(TransactionRequest baseRequest,
        String requestString, boolean match) throws Exception {
        // Read the file and parse it
        DeleteKvpReader reader = new DeleteKvpReader(requestString);
        TransactionRequest request = reader.getRequest();

        LOGGER.finer("base request: " + baseRequest);
        LOGGER.finer("read request: " + request);
        LOGGER.fine("KVP test passed: " + baseRequest.equals(request));

        // Compare parsed request to base request
        if (match) {
            //return baseRequest.equals(request);
            return baseRequest.toString().equals(request.toString());
        } else {
            return !baseRequest.equals(request);
        }
    }

    
    /* ********************************************************************
     * KVP TESTS
     * KVP GetFeature parsing tests.  Each test reads from a specific KVP
     * string and compares it to the base request defined in the test itself.
     * Tests are run via the static methods in this suite.  The tests
     * themselves are quite generic, so documentation is minimal.
     * *********************************************************************/

    /**
     * Example 1 from the WFS 1.0 specification.
     *
     * @throws Exception DOCUMENT ME!
     */
    public void testKVP1() throws Exception {
        String testRequest = "VERSION=1.0.0&" + "SERVICE=WFS&"
            + "REQUEST=TRANSACTION&" + "OPERATION=delete&" + "TYPENAME=rail&"
            + "featureID=123";

        // make base comparison objects        
        TransactionRequest baseRequest = new TransactionRequest();
        DeleteRequest internalRequest = new DeleteRequest();
        internalRequest.setTypeName("rail");

        FidFilter filter = factory.createFidFilter("123");
        internalRequest.setFilter(filter);
        baseRequest.addSubRequest(internalRequest);

        // run test       
        assertTrue(runKvpTest(baseRequest, testRequest, true));
    }

    /**
     * Example 2 from the WFS 1.0 specification.
     *
     * @throws Exception DOCUMENT ME!
     */
    public void testKVP2() throws Exception {
        String testRequest = "VERSION=1.0.0&" + "SERVICE=WFS&"
            + "REQUEST=TRANSACTION&" + "OPERATION=delete&"
            + "TYPENAME=rail,roads&"
            + "FILTER=(<Filter xmlns:gml='http://www.opengis.net/gml'><Within><PropertyName>location</PropertyName><gml:Box><gml:coordinates>10,10 20,20</gml:coordinates></gml:Box></Within></Filter>)(<Filter xmlns:gml='http://www.opengis.net/gml'><Within><PropertyName>location</PropertyName><gml:Box><gml:coordinates>10,10 20,20</gml:coordinates></gml:Box></Within></Filter>)";

        TransactionRequest baseRequest = new TransactionRequest();
        baseRequest.setVersion("1.0.0");

        DeleteRequest internalRequest1 = new DeleteRequest();
        internalRequest1.setTypeName("rail");

        //baseRequest.setReleaseAction(true);
        // make base comparison objects
        GeometryFilter filter = factory.createGeometryFilter(AbstractFilter.GEOMETRY_WITHIN);
        AttributeExpression leftExpression = factory.createAttributeExpression(null);
        leftExpression.setAttributePath("location");

        // Creates coordinates for the linear ring
        Coordinate[] coords = new Coordinate[5];
        coords[0] = new Coordinate(10, 10);
        coords[1] = new Coordinate(10, 20);
        coords[2] = new Coordinate(20, 20);
        coords[3] = new Coordinate(20, 10);
        coords[4] = new Coordinate(10, 10);

        LinearRing outerShell = new LinearRing(coords, new PrecisionModel(), 0);
        Polygon polygon = new Polygon(outerShell, new PrecisionModel(), 0);
        LiteralExpression rightExpression = factory.createLiteralExpression(polygon);
        filter.addLeftGeometry(leftExpression);
        filter.addRightGeometry(rightExpression);

        internalRequest1.setFilter(filter);

        DeleteRequest internalRequest2 = new DeleteRequest();
        internalRequest2.setTypeName("roads");
        internalRequest2.setFilter(filter);

        baseRequest.addSubRequest(internalRequest1);
        baseRequest.addSubRequest(internalRequest2);

        // run test       
        assertTrue(runKvpTest(baseRequest, testRequest, true));
    }

    /**
     * Example 3 from the WFS 1.0 specification.
     *
     * @throws Exception DOCUMENT ME!
     */
    public void testKVP3() throws Exception {
        String testRequest = "VERSION=1.0.0&" + "SERVICE=WFS&"
            + "REQUEST=TRANSACTION&" + "OPERATION=delete&" + "TYPENAME=rail&"
            + "BBOX=10,10,20,20";

        TransactionRequest baseRequest = new TransactionRequest();
        baseRequest.setVersion("1.0.0");

        DeleteRequest internalRequest1 = new DeleteRequest();
        internalRequest1.setTypeName("rail");

        // make base comparison objects
        GeometryFilter filter = factory.createGeometryFilter(AbstractFilter.GEOMETRY_BBOX);

        // Creates coordinates for the linear ring
        Coordinate[] coords = new Coordinate[5];
        coords[0] = new Coordinate(10, 10);
        coords[1] = new Coordinate(10, 20);
        coords[2] = new Coordinate(20, 20);
        coords[3] = new Coordinate(20, 10);
        coords[4] = new Coordinate(10, 10);

        LinearRing outerShell = new LinearRing(coords, new PrecisionModel(), 0);
        Polygon polygon = new Polygon(outerShell, new PrecisionModel(), 0);
        LiteralExpression rightExpression = factory.createLiteralExpression(polygon);
        filter.addRightGeometry(rightExpression);

        internalRequest1.setFilter(filter);
        baseRequest.addSubRequest(internalRequest1);

        // run test       
        assertTrue(runKvpTest(baseRequest, testRequest, true));
    }

    public void testXml1() throws Exception {
        // make base comparison objects        
        DeleteRequest delete = new DeleteRequest();
        delete.setFilter(factory.createFidFilter("123"));

        TransactionRequest baseRequest = new TransactionRequest();
        baseRequest.addSubRequest(delete);

        // run test       
        assertTrue(runXmlTest(baseRequest, "22", true));
    }

    public void testXml2() throws Exception {
        // make base comparison objects        
        DeleteRequest delete = new DeleteRequest();
        FidFilter tempFilter = factory.createFidFilter("123");
        tempFilter.addFid("124");
        tempFilter.addFid("1023");
        tempFilter.addFid("16");
        tempFilter.addFid("5001");
        delete.setFilter(tempFilter);

        TransactionRequest baseRequest = new TransactionRequest();
        baseRequest.addSubRequest(delete);

        // run test       
        assertTrue(runXmlTest(baseRequest, "23", true));
    }

    /*  Need updated geotools jar...big fix takes care of this problem
       The fix is in cvs right now, hopefully release will come soon. */
    public void testXml3() throws Exception {
        // make base comparison objects
        DeleteRequest delete1 = new DeleteRequest();
        FidFilter temp1 = factory.createFidFilter("123");
        temp1.addFid("124");
        delete1.setFilter(temp1);

        DeleteRequest delete2 = new DeleteRequest();
        FidFilter temp2 = factory.createFidFilter("1023");
        temp2.addFid("16");
        delete2.setFilter(temp2);

        DeleteRequest delete3 = new DeleteRequest();
        delete3.setFilter(factory.createFidFilter("5001"));

        TransactionRequest baseRequest = new TransactionRequest();
        baseRequest.addSubRequest(delete1);
        baseRequest.addSubRequest(delete2);
        baseRequest.addSubRequest(delete3);

        // run test
        assertTrue(runXmlTest(baseRequest, "24", true));
    }
}
