/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.requests;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.geom.PrecisionModel;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.geotools.feature.AttributeType;
import org.geotools.feature.AttributeTypeFactory;
import org.geotools.feature.Feature;
import org.geotools.feature.FeatureType;
import org.geotools.feature.FeatureTypeFactory;
import org.geotools.feature.IllegalAttributeException;
import org.geotools.feature.SchemaException;
import org.geotools.filter.FilterFactory;
import org.vfny.geoserver.config.ConfigInfo;
import org.vfny.geoserver.config.TypeRepository;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.Reader;
import java.util.logging.Logger;


/**
 * Tests the get feature request handling.
 *
 * @author Chris Holmes, TOPP
 * @version $Id: TransactionSuite.java,v 1.4 2003/09/19 19:06:05 cholmesny Exp $
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
    private static final String TYPE_DIR = System.getProperty("user.dir")
        + "/misc/unit/featureTypes";

    /** Unit test data directory */
    private static final String CONFIG_DIR = System.getProperty("user.dir")
        + "/misc/unit/config/";
    private ConfigInfo config;
    private TypeRepository repo;
    protected FeatureType schema;
    protected Feature testFeature;

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

        suite.addTestSuite(InsertSuite.class);
        suite.addTestSuite(DeleteSuite.class);

        return suite;
    }

    public void setUp() {
        config = ConfigInfo.getInstance(CONFIG_DIR);
        config.setTypeDir(TYPE_DIR);
        repo = TypeRepository.getInstance();

        AttributeType[] atts = {
            AttributeTypeFactory.newAttributeType("fid", Integer.class),
            AttributeTypeFactory.newAttributeType("geom", Polygon.class),
            AttributeTypeFactory.newAttributeType("name", String.class)
        };

        try {
            schema = FeatureTypeFactory.newFeatureType(atts, "rail");
        } catch (SchemaException e) {
            LOGGER.finer("problem with creating schema");
        }

        Coordinate[] points = {
            new Coordinate(15, 15), new Coordinate(15, 25),
            new Coordinate(25, 25), new Coordinate(25, 15),
            new Coordinate(15, 15)
        };
        PrecisionModel precModel = new PrecisionModel();
        int srid = 2035;
        LinearRing shell = new LinearRing(points, precModel, srid);
        Polygon the_geom = new Polygon(shell, precModel, srid);

        Integer featureId = new Integer(44);
        String name = "insert polygon";
        Object[] attributes = { featureId, the_geom, name };

        try {
            testFeature = schema.create(attributes, String.valueOf(featureId));
        } catch (IllegalAttributeException ife) {
            LOGGER.warning("problem in setup " + ife);
        }
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
