/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.requests;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.geotools.feature.AttributeType;
import org.geotools.feature.AttributeTypeFactory;
import org.geotools.feature.Feature;
import org.geotools.feature.FeatureType;
import org.geotools.feature.FeatureTypeFactory;
import org.geotools.feature.IllegalAttributeException;
import org.geotools.feature.SchemaException;
import org.geotools.filter.FilterFactory;
import org.vfny.geoserver.global.GeoServer;
import org.vfny.geoserver.util.requests.readers.KvpRequestReader;
import org.vfny.geoserver.util.requests.readers.XmlRequestReader;
import org.vfny.geoserver.wfs.requests.TransactionRequest;
import org.vfny.geoserver.wfs.requests.readers.DeleteKvpReader;
import org.vfny.geoserver.wfs.requests.readers.TransactionXmlReader;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.geom.PrecisionModel;


/**
 * Tests the get feature request handling.
 *
 * @author Chris Holmes, TOPP
 * @version $Id: TransactionSuite.java,v 1.9 2004/01/31 00:17:52 jive Exp $
 *
 * @task REVISIT: This should serve as the place for the sub transaction suites
 *       to run their tests.
 */
public class TransactionSuite extends RequestTestCase {
    // Initializes the logger. Uncomment to see log messages.
    //static {
    //org.vfny.geoserver.config.Log4JFormatter.init("org.vfny.geoserver", 
    //java.util.logging.Level.FINE);
    //}

    /** Class logger */
    protected static final Logger LOGGER = Logger.getLogger(
            "org.vfny.geoserver.requests");

    /** Holds mappings between HTTP and ASCII encodings */
    protected static FilterFactory factory = FilterFactory.createFilterFactory();
    protected FeatureType schema;
    protected Feature testFeature;
    protected GeoServer config;

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

        //suite.addTestSuite(UpdateSuite.class);
        //suite.addTestSuite(InsertSuite.class);
        //suite.addTestSuite(DeleteSuite.class);
        return suite;
    }

    public void setUp() throws Exception {
        Map values = new HashMap();

        //ServerConfig.load(values, new DefaultCatalog());
        //config = ConfigInfo.getInstance(CONFIG_DIR);
        //config.setTypeDir(TYPE_DIR);
        //repo = TypeRepository.getInstance();
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

    protected XmlRequestReader getXmlReader() {
        return new TransactionXmlReader();
    }

    protected KvpRequestReader getKvpReader(Map kvps) {
        return new DeleteKvpReader(kvps);
    }

    /**
     * Handles actual XML test running details.
     *
     * @param baseRequest Base request, for comparison.
     * @param testRequest File name to parse.
     *
     * @return <tt>true</tt> if the test passed.
     */

    /*static boolean runXmlTest(TransactionRequest baseRequest, String fileName,
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
       }*/
    protected static boolean assertEquals(TransactionRequest baseRequest,
        TransactionRequest testRequest) {
        return baseRequest.equals(testRequest);
    }
}
