/* Copyright (c) 2001 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root 
 * application directory.
 */
package org.vfny.geoserver.responses;

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
import org.vfny.geoserver.requests.FeatureRequest;
import org.vfny.geoserver.requests.Query;

/**
 * Tests the get feature request handling.
 *
 * @author Rob Hranac, TOPP
 * @version $VERSION$
 */
public class FeatureSuite extends TestCase {

    /* Initializes the logger. */
    static {
        Geotools.init("Log4JFormatter", Level.FINEST);
    }
    
    /** Class logger */
    private static final Logger LOGGER =  
        Logger.getLogger("org.vfny.geoserver.requests");

    /** Unit test data directory */
    private static final String DATA_DIRECTORY = 
        System.getProperty("user.dir") + "/misc/testData/unit/requests";

    /** Holds mappings between HTTP and ASCII encodings */
    private static FilterFactory factory = FilterFactory.createFilterFactory();


    /** Constructor with super. */
    public FeatureSuite (String testName) { super(testName); }


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
        query.setFeatureTypeName("rail");
        FeatureRequest request = new FeatureRequest();
        request.addQuery(query);       

        FeatureTypeBean info = new FeatureTypeBean(query.getFeatureTypeName());
        String gml = FeatureResponse.getXmlResponse(request, info);
        LOGGER.finest(gml);
        // run test
        //assertTrue(runXmlTest(baseRequest, "10", true));
    }
}
