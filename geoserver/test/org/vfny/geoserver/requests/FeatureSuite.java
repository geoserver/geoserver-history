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
import org.vfny.geoserver.config.ConfigInfo;

/**
 * Tests the get feature request handling.
 *
 * @author Rob Hranac, TOPP
 * @version $VERSION$
 */
public class FeatureSuite extends TestCase {

    /* Initializes the logger. */
    static {
        Geotools.init("Log4JFormatter", Level.INFO);
    }
    
    /** Class logger */
    private static final Logger LOGGER =  
        Logger.getLogger("org.vfny.geoserver.requests");

    /** Unit test data directory */
    private static final String DATA_DIRECTORY = 
        System.getProperty("user.dir") + "/misc/unit/requests";

    /** Holds mappings between HTTP and ASCII encodings */
    private static FilterFactory factory = FilterFactory.createFilterFactory();

      /** Unit test data directory */
    private static final String CONFIG_DIR = 
        System.getProperty("user.dir") + "/misc/unit/config/";

    //classes complain if we don't set up a valid config info.
    private static ConfigInfo config = ConfigInfo.getInstance(CONFIG_DIR);

    /** Constructor with super. */
    public FeatureSuite (String testName) { super(testName); }


    /*************************************************************************
     * STATIC METHODS FOR TEST RUNNING                                       *
     *************************************************************************/
    /**
     * Handles actual XML test running details.
     *
     * @param baseRequest Base request, for comparison.
     * @param fileName File name to parse.
     * @param match Whether or not base request and parse request should match.
     * @throws Exception If there is any problem running the test.
     */
    private static boolean runXmlTest(FeatureRequest baseRequest,
                                      String fileName, 
                                      boolean match)
        throws Exception {

        // Read the file and parse it
        File inputFile = new File(DATA_DIRECTORY + "/" + fileName + ".xml");
        Reader inputStream = new FileReader(inputFile);
        FeatureRequest request = 
            XmlRequestReader.readGetFeature(new BufferedReader(inputStream));
        LOGGER.info("base request: " + baseRequest);
        LOGGER.info("read request: " + request);
        LOGGER.info("XML " + fileName +" test passed: " +  
                    baseRequest.equals(request));

        // Compare parsed request to base request
        if(match) {
            return baseRequest.equals(request);
        } else {
            return !baseRequest.equals(request);
        }
    }

    /**
     * Handles actual XML test running details.
     *
     * @param baseRequest Base request, for comparison.
     * @param fileName File name to parse.
     * @param match Whether or not base request and parse request should match.
     * @throws Exception If there is any problem running the test.
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


    /** Handles test set up details. */
    public void setUp() {}
    

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
        Query query = new Query();
        query.setTypeName("rail");
        FeatureRequest baseRequest = new FeatureRequest();
        baseRequest.addQuery(query);       
        // run test
        assertTrue(runXmlTest(baseRequest, "10", true));
    }

    public void test2() throws Exception { 
        // make base comparison objects        
        Query query = new Query();
        query.setTypeName("rail");
        query.addFilter(factory.createFidFilter("123"));
        FeatureRequest baseRequest = new FeatureRequest();
        baseRequest.addQuery(query);
        // run test       
        assertTrue(runXmlTest( baseRequest, "6", true));
    }
    
    public void test3() throws Exception { 
        // make base comparison objects                
        FeatureRequest baseRequest = new FeatureRequest();
        Query query = new Query();
        query.setTypeName("rail");
        query.addFilter(factory.createFidFilter("123"));
        query.addPropertyName("name");
        query.addPropertyName("tracks");
        query.addPropertyName("id");
        baseRequest.addQuery(query);
        // run test
        assertTrue(runXmlTest( baseRequest, "7", true));
    }
    
    public void test4() throws Exception { 
        // make base comparison objects                
        FidFilter tempFilter = factory.createFidFilter("123");
        tempFilter.addFid("456");
        tempFilter.addFid("789");
        Query query = new Query();
        query.setTypeName("rail");
        query.addFilter(tempFilter);
        FeatureRequest baseRequest = new FeatureRequest();
        baseRequest.addQuery(query);
        // run test        
        assertTrue(runXmlTest( baseRequest, "8", true));
    }
    
    public void test5() throws Exception { 
        // make base comparison objects               
        FidFilter tempFilter = factory.createFidFilter("123");
        tempFilter.addFid("456");
        tempFilter.addFid("789");
        Query query = new Query();
        query.setTypeName("rail");
        query.addFilter(tempFilter);
        query.addPropertyName("name");
        query.addPropertyName("tracks");
        FeatureRequest baseRequest = new FeatureRequest();
        baseRequest.addQuery(query);
        // run test        
        assertTrue(runXmlTest( baseRequest, "9", true));
     }

    public void test6() throws Exception { 
        // make base comparison objects               
        Coordinate coord[] = new Coordinate[5];
        coord[0] = new Coordinate(-57.9188,46.2023);
        coord[1] = new Coordinate(-57.9188,51.8145);
        coord[2] = new Coordinate(-46.6873,51.8145);
        coord[3] = new Coordinate(-46.6873,46.2023);
        coord[4] = new Coordinate(-57.9188,46.2023);
        LinearRing outerRing = new LinearRing(coord, new PrecisionModel(), 0);
        LinearRing innerRing[] = new LinearRing[0];
        Polygon tempPoly = new Polygon(outerRing, innerRing, 
                                       new PrecisionModel(), 0);
        AttributeExpression tempLeftExp = factory.
            createAttributeExpression(null); 
        tempLeftExp.setAttributePath("location");
        LiteralExpression tempRightExp = factory.
            createLiteralExpression(DefaultExpression.LITERAL_GEOMETRY); 
        tempRightExp.setLiteral(tempPoly);
        org.geotools.filter.GeometryFilter tempFilter = 
            factory.createGeometryFilter(AbstractFilter.GEOMETRY_DISJOINT);
        tempFilter.addLeftGeometry(tempLeftExp);
        tempFilter.addRightGeometry(tempRightExp);
        Query query = new Query();
        query.setTypeName("rail");
        query.addFilter(tempFilter.not());
        query.addPropertyName("name");
        query.addPropertyName("id");
        FeatureRequest baseRequest = new FeatureRequest();
        baseRequest.addQuery(query);
        baseRequest.setMaxFeatures(10000);
        // run test        
        assertTrue(runXmlTest( baseRequest, "11", true));
     }

    public void test7() throws Exception { 
        // make base comparison objects               
        Coordinate coord[] = new Coordinate[5];
        coord[0] = new Coordinate(50,40);
        coord[1] = new Coordinate(50,100);
        coord[2] = new Coordinate(60,100);
        coord[3] = new Coordinate(60,40);
        coord[4] = new Coordinate(50,40);
        LinearRing outerRing = new LinearRing(coord, new PrecisionModel(), 0);
        LinearRing innerRing[] = new LinearRing[0];
        Polygon tempPoly = 
            new Polygon(outerRing, innerRing, new PrecisionModel(), 0);
        AttributeExpression tempLeftExp = 
            factory.createAttributeExpression(null); 
        tempLeftExp.setAttributePath("location");
        LiteralExpression tempRightExp = factory.
            createLiteralExpression(DefaultExpression.LITERAL_GEOMETRY);
        tempRightExp.setLiteral(tempPoly);
        org.geotools.filter.GeometryFilter tempFilter = 
            factory.createGeometryFilter(AbstractFilter.GEOMETRY_WITHIN);
        tempFilter.addLeftGeometry(tempLeftExp);
        tempFilter.addRightGeometry(tempRightExp);
        FeatureRequest baseRequest = new FeatureRequest();
        Query query = new Query();
        query.setTypeName("rail");
        query.addFilter(tempFilter);
        query.addPropertyName("name");
        query.addPropertyName("id");
        baseRequest.addQuery(query);
        query = new Query();
        query.setTypeName("roads");
        query.addFilter(tempFilter);
        query.addPropertyName("length");
        query.addPropertyName("id");
        baseRequest.addQuery(query);
        baseRequest.setMaxFeatures(10000);
        // run test        
        assertTrue(runXmlTest( baseRequest, "12", true));
     }
   
 public void test77() throws Exception { 
        // make base comparison objects               
        Coordinate coord[] = new Coordinate[5];
        coord[0] = new Coordinate(50,40);
        coord[1] = new Coordinate(50,100);
        coord[2] = new Coordinate(60,100);
        coord[3] = new Coordinate(60,40);
        coord[4] = new Coordinate(50,40);
        LinearRing outerRing = new LinearRing(coord, new PrecisionModel(), 0);
        LinearRing innerRing[] = new LinearRing[0];
        Polygon tempPoly = 
            new Polygon(outerRing, innerRing, new PrecisionModel(), 0);
        AttributeExpression tempLeftExp = 
            factory.createAttributeExpression(null); 
        tempLeftExp.setAttributePath("location");
        LiteralExpression tempRightExp = factory.
            createLiteralExpression(DefaultExpression.LITERAL_GEOMETRY);
        tempRightExp.setLiteral(tempPoly);
        org.geotools.filter.GeometryFilter tempFilter = 
            factory.createGeometryFilter(AbstractFilter.GEOMETRY_WITHIN);
        tempFilter.addLeftGeometry(tempLeftExp);
        tempFilter.addRightGeometry(tempRightExp);
        FeatureRequest baseRequest = new FeatureRequest();
       
	FidFilter temp1 = factory.createFidFilter("123");
	temp1.addFid("124");
	DeleteRequest delete2 = new DeleteRequest();
	FidFilter temp2 = factory.createFidFilter("1023");
	temp2.addFid("16");
	Query query = new Query();
        query.setTypeName("rail");
        query.addFilter(temp1);
        query.addPropertyName("name");
        query.addPropertyName("id");
        baseRequest.addQuery(query);
        query = new Query();
        query.setTypeName("roads");
        query.addFilter(temp2);
        query.addPropertyName("length");
        query.addPropertyName("id");
        baseRequest.addQuery(query);
        baseRequest.setMaxFeatures(10000);
        // run test        
        assertTrue(runXmlTest( baseRequest, "15", true));
     }
     
    public void test8() throws Exception { 
        // make base comparison objects               
        LiteralExpression tempExp1 = 
            factory.createLiteralExpression(DefaultExpression.LITERAL_STRING); 
        tempExp1.setLiteral("Main St.");
        AttributeExpression tempExp2 = 
            factory.createAttributeExpression(null); 
        tempExp2.setAttributePath("Person/Address/StreetName");
        CompareFilter tempFilter = 
            factory.createCompareFilter(AbstractFilter.COMPARE_EQUALS);
        tempFilter.addRightValue(tempExp1);
        tempFilter.addLeftValue(tempExp2);
        Filter logicFilter = factory.
            createLogicFilter(tempFilter, AbstractFilter.LOGIC_AND);
        LOGGER.fine("filter: " + logicFilter);
        tempFilter =  factory.
            createCompareFilter(AbstractFilter.COMPARE_EQUALS);
        tempExp1 = factory.
            createLiteralExpression(DefaultExpression.LITERAL_STRING); 
        tempExp2 = factory.createAttributeExpression(null); 
        tempExp1.setLiteral("Main St.");
        tempExp1.setLiteral("SomeTown");
        tempExp2.setAttributePath("Person/Address/City");
        tempFilter.addRightValue(tempExp1);
        tempFilter.addLeftValue(tempExp2);
        logicFilter = logicFilter.and(tempFilter);
        LOGGER.fine("filter: " + logicFilter);
        tempFilter =  factory.
            createCompareFilter(AbstractFilter.COMPARE_EQUALS);
        tempExp1 = factory.
            createLiteralExpression(DefaultExpression.LITERAL_STRING); 
        tempExp2 = factory.createAttributeExpression(null); 
        tempExp1.setLiteral("Female");
        tempExp2.setAttributePath("Person/Sex");
        tempFilter.addRightValue(tempExp1);
        tempFilter.addLeftValue(tempExp2);
        logicFilter = logicFilter.and(tempFilter);
        LOGGER.fine("filter: " + logicFilter);
        tempFilter =  factory.
            createCompareFilter(AbstractFilter.COMPARE_GREATER_THAN);
        tempExp1 = factory.
            createLiteralExpression(DefaultExpression.LITERAL_INTEGER); 
        tempExp2 = factory.createAttributeExpression(null); 
        tempExp1.setLiteral("35000");
        tempExp2.setAttributePath("Person/Salary");
        tempFilter.addLeftValue(tempExp2);
        tempFilter.addRightValue(tempExp1);
        logicFilter = logicFilter.and(tempFilter);
        Filter finalFilter = factory.
            createLogicFilter(logicFilter, AbstractFilter.LOGIC_AND);
        LOGGER.fine("filter: " + finalFilter);
        tempFilter =  factory.
            createCompareFilter(AbstractFilter.COMPARE_GREATER_THAN_EQUAL);
        tempExp1 = factory.
            createLiteralExpression(DefaultExpression.LITERAL_INTEGER); 
        tempExp2 = factory.createAttributeExpression(null); 
        tempExp1.setLiteral("10000");
        tempExp2.setAttributePath("Person/Address/StreetNumber");
        tempFilter.addLeftValue(tempExp2);
        tempFilter.addRightValue(tempExp1);
        finalFilter = finalFilter.and(tempFilter);
        LOGGER.fine("filter: " + finalFilter);
        tempFilter = factory.
            createCompareFilter(AbstractFilter.COMPARE_LESS_THAN_EQUAL);
        tempExp1 = factory.
            createLiteralExpression(DefaultExpression.LITERAL_INTEGER); 
        tempExp2 = factory.createAttributeExpression(null); 
        tempExp1.setLiteral("10999");
        tempExp2.setAttributePath("Person/Address/StreetNumber");
        tempFilter.addLeftValue(tempExp2);
        tempFilter.addRightValue(tempExp1);
        finalFilter = finalFilter.and(tempFilter);
        LOGGER.fine("filter: " + finalFilter);
        FeatureRequest baseRequest = new FeatureRequest();
        Query query = new Query();
        query.setTypeName("Person");
        query.addFilter(finalFilter);
        query.addPropertyName("Person/LastName");
        baseRequest.addQuery(query);
        baseRequest.setMaxFeatures(10000);
        // run test        
        //assertTrue(runXmlTest( baseRequest, "13", true));
     }

    /*************************************************************************
     * KVP TESTS                                                             *
     *************************************************************************
     * KVP GetFeature parsing tests.  Each test reads from a specific KVP    *
     * string and compares it to the base request defined in the test itself.*
     * Tests are run via the static methods in this suite.  The tests        *
     * themselves are quite generic, so documentation is minimal.            *
     *************************************************************************/
     public void test9() 
        throws Exception {
         String testRequest = "VERSION=0.0.14&" + 
             "REQUEST=GETFEATURE&" + 
             "SERVICE=WFS&" + 
             "TYPENAME=rail";

        // make base comparison objects        
        Query query = new Query();
        query.setTypeName("rail");
        FeatureRequest baseRequest = new FeatureRequest();
        baseRequest.addQuery(query);
        baseRequest.setVersion("0.0.14");
        // run test       
        assertTrue(runKvpTest( baseRequest, testRequest, true));
     }

    public void test10() 
        throws Exception {
         String testRequest = "VERSION=0.0.14&" + 
             "REQUEST=GETFEATURE&" + 
             "SERVICE=WFS&" + 
             "PROPERTYNAME=location,id&" + 
             "TYPENAME=rail";

        // make base comparison objects        
        Query query = new Query();
        query.setTypeName("rail");
        query.addPropertyName("location");
        query.addPropertyName("id");
        FeatureRequest baseRequest = new FeatureRequest();
        baseRequest.addQuery(query);
        baseRequest.setVersion("0.0.14");
        // run test       
        assertTrue(runKvpTest( baseRequest, testRequest, true));
     }


    public void test11() 
        throws Exception {
         String testRequest = "VERSION=1.0.0&" + 
             "SERVICE=WFS&" + 
             "REQUEST=GETFEATURE&" + 
             "TYPENAME=rail&" +
             "FEATUREID=123";

        // make base comparison objects        
        Query query = new Query();
        query.setTypeName("rail");
        query.addFilter(factory.createFidFilter("123"));
        FeatureRequest baseRequest = new FeatureRequest();
        baseRequest.addQuery(query);
        baseRequest.setVersion("1.0.0");
        // run test       
        assertTrue(runKvpTest( baseRequest, testRequest, true));
     }

    public void test12() 
        throws Exception {
        String testRequest = "VERSION=1.0.0&" + 
            "SERVICE=WFS&" + 
            "REQUEST=GETFEATURE&" + 
            "TYPENAME=rail&" +
            "FEATUREID=123,456,789";
        
        // make base comparison objects        
        FidFilter filter = factory.createFidFilter("123");
        filter.addFid("456");
        filter.addFid("789");
        Query query = new Query();
        query.setTypeName("rail");
        query.addFilter(filter);
        FeatureRequest baseRequest = new FeatureRequest();
        baseRequest.addQuery(query);
        baseRequest.setVersion("1.0.0");
        // run test       
        assertTrue(runKvpTest( baseRequest, testRequest, true));
    }

    public void test13() 
        throws Exception {
        String testRequest = "VERSION=1.0.0&" + 
            "SERVICE=WFS&" + 
            "REQUEST=GETFEATURE&" + 
            "TYPENAME=rail1,rail2,rail3&" +
            "PROPERTYNAME=(loc1,id1)(loc2,id2)(loc3,id3)&" +
            "FEATUREID=123,456,789";
        
        // make base comparison objects        
        FeatureRequest baseRequest = new FeatureRequest();
        baseRequest.setVersion("1.0.0");

        FidFilter filter = factory.createFidFilter("123");
        filter.addFid("456");
        filter.addFid("789");
        Query query = new Query();
        query.setTypeName("rail1");
        query.addFilter(filter);
        query.addPropertyName("loc1");
        query.addPropertyName("id1");
        baseRequest.addQuery(query);
        query = new Query();
        query.setTypeName("rail2");
        query.addFilter(filter);
        query.addPropertyName("loc2");
        query.addPropertyName("id2");
        baseRequest.addQuery(query);
        query = new Query();
        query.setTypeName("rail3");
        query.addFilter(filter);
        query.addPropertyName("loc3");
        query.addPropertyName("id3");
        baseRequest.addQuery(query);
        // run test       
        assertTrue(runKvpTest( baseRequest, testRequest, true));
    }

    public void test14() 
        throws Exception {
        String testRequest = "VERSION=1.0.0&" + 
            "SERVICE=WFS&" + 
            "REQUEST=GETFEATURE&" + 
            "TYPENAME=rail&" +
            "FILTER=<Filter xmlns:gml='http://www.opengis.net/gml'><Within><PropertyName>location</PropertyName><gml:Box><gml:coordinates>10,10 20,20</gml:coordinates></gml:Box></Within></Filter>";

        // make base comparison objects
        org.geotools.filter.GeometryFilter filter = 
            factory.createGeometryFilter(AbstractFilter.GEOMETRY_WITHIN);
        AttributeExpression leftExpression = factory.createAttributeExpression(null); 
        leftExpression.setAttributePath("location");

        // Creates coordinates for the linestring
        Coordinate[] coords = new Coordinate[5];
        coords[0] = new Coordinate(10,10);
        coords[1] = new Coordinate(10,20);
        coords[2] = new Coordinate(20,20);
        coords[3] = new Coordinate(20,10);
        coords[4] = new Coordinate(10,10);
        LinearRing outerShell = new LinearRing(coords, new PrecisionModel(), 0);
        Polygon polygon = new Polygon(outerShell, new PrecisionModel(), 0);
        LiteralExpression rightExpression = factory.createLiteralExpression(polygon); 

        filter.addLeftGeometry(leftExpression);
        filter.addRightGeometry(rightExpression);

        Query query = new Query();
        query.setTypeName("rail");
        query.addFilter(filter);

        FeatureRequest baseRequest = new FeatureRequest();
        baseRequest.setVersion("1.0.0");
        baseRequest.addQuery(query);

        // run test       
        assertTrue(runKvpTest( baseRequest, testRequest, true));
    }

    public void test15() 
        throws Exception {
        String testRequest = "VERSION=1.0.0&" + 
            "SERVICE=WFS&" + 
            "REQUEST=GETFEATURE&" + 
            "TYPENAME=rail,roads";
        
        // make base comparison objects        
        FeatureRequest baseRequest = new FeatureRequest();
        Query query = new Query();
        query.setTypeName("rail");
        baseRequest.addQuery(query);

        query = new Query();
        query.setTypeName("roads");
        baseRequest.addQuery(query);

        baseRequest.setVersion("1.0.0");
        // run test       
        assertTrue(runKvpTest( baseRequest, testRequest, true));
    }

    public void test16() 
        throws Exception {
        String testRequest = "VERSION=1.0.0&" + 
            "SERVICE=WFS&" + 
            "REQUEST=GETFEATURE&" + 
            "PROPERTYNAME=(loc1,id1)(*)&" +
            "TYPENAME=rail,roads";
        
        // make base comparison objects        
        FeatureRequest baseRequest = new FeatureRequest();
        Query query = new Query();
        query.setTypeName("rail");
        query.addPropertyName("loc1");
        query.addPropertyName("id1");
        baseRequest.addQuery(query);

        query = new Query();
        query.setTypeName("roads");
        baseRequest.addQuery(query);

        baseRequest.setVersion("1.0.0");
        // run test       
        assertTrue(runKvpTest( baseRequest, testRequest, true));
    }

    public void test17() 
        throws Exception {
        String testRequest = "VERSION=1.0.0&" + 
            "SERVICE=WFS&" + 
            "REQUEST=GETFEATURE&" + 
            "TYPENAME=rail&" +
            "FEATUREID=123,456";
        
        // make base comparison objects        
        FeatureRequest baseRequest = new FeatureRequest();
        baseRequest.setVersion("1.0.0");

        FidFilter filter = factory.createFidFilter("123");
        filter.addFid("456");
        Query query = new Query();
        query.setTypeName("rail");
        query.addFilter(filter);
        baseRequest.addQuery(query);
        // run test       
        assertTrue(runKvpTest( baseRequest, testRequest, true));
    }

    public void test18() 
        throws Exception {
        String testRequest = "VERSION=1.0.0&" + 
            "SERVICE=WFS&" + 
            "REQUEST=GETFEATURE&" + 
            "TYPENAME=rail1,rail2&" +
            "PROPERTYNAME=(loc1,id1)(loc2)&" +
            "FEATUREID=123,456";
        
        // make base comparison objects        
        FeatureRequest baseRequest = new FeatureRequest();
        baseRequest.setVersion("1.0.0");

        FidFilter filter = factory.createFidFilter("123");
        filter.addFid("456");
        Query query = new Query();
        query.setTypeName("rail1");
        query.addFilter(filter);
        query.addPropertyName("loc1");
        query.addPropertyName("id1");
        baseRequest.addQuery(query);
        query = new Query();
        query.setTypeName("rail2");
        query.addFilter(filter);
        query.addPropertyName("loc2");
        baseRequest.addQuery(query);
        query = new Query();
        // run test       
        assertTrue(runKvpTest( baseRequest, testRequest, true));
    }

    public void test19() 
        throws Exception {
        String testRequest = "VERSION=1.0.0&" + 
            "SERVICE=WFS&" + 
            "REQUEST=GETFEATURE&" + 
            "TYPENAME=rail,roads&" +
            "FILTER=(<Filter xmlns:gml='http://www.opengis.net/gml'><Within><PropertyName>location</PropertyName><gml:Box><gml:coordinates>10,10 20,20</gml:coordinates></gml:Box></Within></Filter>)(<Filter xmlns:gml='http://www.opengis.net/gml'><Within><PropertyName>location</PropertyName><gml:Box><gml:coordinates>10,10 20,20</gml:coordinates></gml:Box></Within></Filter>)";

        FeatureRequest baseRequest = new FeatureRequest();
        baseRequest.setVersion("1.0.0");

        // make base comparison objects
        org.geotools.filter.GeometryFilter filter = 
            factory.createGeometryFilter(AbstractFilter.GEOMETRY_WITHIN);
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
        LinearRing outerShell = new LinearRing(coords, new PrecisionModel(),0);
        Polygon polygon = new Polygon(outerShell, new PrecisionModel(), 0);
        LiteralExpression rightExpression = factory.
            createLiteralExpression(polygon); 
        filter.addLeftGeometry(leftExpression);
        filter.addRightGeometry(rightExpression);

        Query query = new Query();
        query.setTypeName("rail");
        query.addFilter(filter);
        baseRequest.addQuery(query);

        query = new Query();
        query.setTypeName("roads");
        query.addFilter(filter);
        baseRequest.addQuery(query);

        // run test       
        assertTrue(runKvpTest( baseRequest, testRequest, true));
    }

    public void test20() 
        throws Exception {
        String testRequest = "VERSION=1.0.0&" + 
            "SERVICE=WFS&" + 
            "REQUEST=GETFEATURE&" + 
            "TYPENAME=rail,roads&" +
            "PROPERTYNAME=(loc1,id1,cat1)(loc2)&" +
            "FILTER=(<Filter xmlns:gml='http://www.opengis.net/gml'><Within><PropertyName>location</PropertyName><gml:Box><gml:coordinates>10,10 20,20</gml:coordinates></gml:Box></Within></Filter>)(<Filter xmlns:gml='http://www.opengis.net/gml'><Within><PropertyName>location</PropertyName><gml:Box><gml:coordinates>10,10 20,20</gml:coordinates></gml:Box></Within></Filter>)";

        FeatureRequest baseRequest = new FeatureRequest();
        baseRequest.setVersion("1.0.0");

        // make base comparison objects
        org.geotools.filter.GeometryFilter filter = 
            factory.createGeometryFilter(AbstractFilter.GEOMETRY_WITHIN);
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
        LinearRing outerShell = new LinearRing(coords, new PrecisionModel(),0);
        Polygon polygon = new Polygon(outerShell, new PrecisionModel(), 0);
        LiteralExpression rightExpression = factory.
            createLiteralExpression(polygon); 
        filter.addLeftGeometry(leftExpression);
        filter.addRightGeometry(rightExpression);

        Query query = new Query();
        query.setTypeName("rail");
        query.addFilter(filter);
        query.addPropertyName("loc1");
        query.addPropertyName("id1");
        query.addPropertyName("cat1");
        baseRequest.addQuery(query);

        query = new Query();
        query.setTypeName("roads");
        query.addPropertyName("loc2");
        query.addFilter(filter);
        baseRequest.addQuery(query);

        // run test       
        assertTrue(runKvpTest( baseRequest, testRequest, true));
    }

    public void test21() 
        throws Exception {
        String testRequest = "VERSION=1.0.0&" + 
            "SERVICE=WFS&" + 
            "REQUEST=GETFEATURE&" + 
            "TYPENAME=rail&" +
            "BBOX=10,10,20,20";

        FeatureRequest baseRequest = new FeatureRequest();
        baseRequest.setVersion("1.0.0");

        // make base comparison objects
        org.geotools.filter.GeometryFilter filter = 
            factory.createGeometryFilter(AbstractFilter.GEOMETRY_BBOX);
        //AttributeExpression leftExpression = 
        //    factory.createAttributeExpression(null); 
        //leftExpression.setAttributePath("@");
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
        //filter.addLeftGeometry(leftExpression);
        filter.addRightGeometry(rightExpression);

        Query query = new Query();
        query.setTypeName("rail");
        query.addFilter(filter);
        baseRequest.addQuery(query);

        // run test       
        assertTrue(runKvpTest(baseRequest, testRequest, true));
    }
}
